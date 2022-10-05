package org.keycloak.dashboard;

import org.keycloak.dashboard.util.GHQuery;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedSearchIterable;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Bugs {

    private GitHub gitHub;

    private List<BugStat> stats;

    private List<AreaStat> areaStats;

    public Bugs(GitHub gitHub, boolean mockGitHub) throws IOException {
        this.gitHub = gitHub;

        int open = 0;
        int nonTriaged = 0;
        int missingAreaLabel = 0;

        String queryWithoutAreaLabels;

        if (mockGitHub) {
            open = 124;
            nonTriaged = 500;
            missingAreaLabel = 5;
            queryWithoutAreaLabels = "-label:area/account/api -label:area/account/ui -label:area/adapter/fuse -label:area/adapter/java-cli -label:area/adapter/javascript -label:area/adapter/jee -label:area/adapter/jee-saml -label:area/adapter/spring -label:area/admin/api -label:area/admin/cli -label:area/admin/fine-grained-permissions -label:area/admin/ui -label:area/authentication -label:area/authentication/webauthn -label:area/authorization-services -label:area/ci -label:area/core -label:area/dependencies -label:area/dist/quarkus -label:area/dist/wildfly -label:area/docs -label:area/identity-brokering -label:area/import-export -label:area/infinispan -label:area/ldap -label:area/oidc -label:area/operator -label:area/saml -label:area/storage -label:area/testsuite -label:area/token-exchange -label:area/translations -label:area/user-profile";

            areaStats = new LinkedList<>();
            areaStats.add(new AreaStat("area/admin/ui", 1, 2));
            areaStats.add(new AreaStat("area/dist/quarkus", 12, 22));
        } else {
            Map<String, AreaStat> areas = new LinkedHashMap<>();

            System.out.print("Fetching bugs; ");

            queryWithoutAreaLabels = getQueryWithoutAreaLabels();

            PagedSearchIterable<GHIssue> list = gitHub.searchIssues().q("repo:keycloak/keycloak is:issue is:open label:kind/bug").list();
            int pageSize = 100;
            list.withPageSize(pageSize);

            System.out.print(list.getTotalCount() + " ");

            int fetchCount = 0;
            for (GHIssue i : list) {
                Set<String> issuesAreas = new HashSet<>();
                boolean triage = false;
                for (GHLabel l : i.getLabels()) {
                    if (l.getName().startsWith("area/")) {
                        issuesAreas.add(l.getName());
                    } else if (l.getName().equals("status/triage")) {
                        triage = true;
                    }
                }

                if (triage) {
                    nonTriaged++;
                } else {
                    open++;
                }

                if (issuesAreas.isEmpty()) {
                    missingAreaLabel++;
                } else {
                    for (String a : issuesAreas) {
                        AreaStat areaStat = areas.computeIfAbsent(a, s -> new AreaStat(a, 0, 0));
                        if (triage) {
                            areaStat.triage++;
                        } else {
                            areaStat.open++;
                        }
                    }
                }

                if (fetchCount % pageSize == 0) {
                    System.out.print(".");
                }
                fetchCount++;
            }

            areaStats = areas.values().stream().sorted(Comparator.comparingInt(AreaStat::getTotal).reversed()).collect(Collectors.toList());
        }

        stats = new LinkedList<>();
        stats.add(new BugStat("Open bugs", open, Constants.BUG_OPEN_WARN, "is:open label:kind/bug -label:status/triage"));
        stats.add(new BugStat("Non-triaged", nonTriaged, Constants.BUG_TRIAGE_WARN, "is:open label:kind/bug label:status/triage"));
        stats.add(new BugStat("Bugs without area label", missingAreaLabel, Constants.BUG_AREA_MISSING_WARN, "is:open label:kind/bug " + queryWithoutAreaLabels));

    }

    public List<BugStat> getStats() {
        return stats;
    }

    public List<AreaStat> getAreaStats() {
        return areaStats;
    }

    private String getQueryWithoutAreaLabels() throws IOException {
        GHRepository repository = gitHub.getRepository("keycloak/keycloak");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (GHLabel l : repository.listLabels()) {
            if (l.getName().startsWith("area/")) {
                if (!first) {
                    sb.append(" ");
                }
                sb.append("-label:" + l.getName());
                first = false;
            }
        }
        return sb.toString();
    }

    public class BugStat {

        private String title;
        private int count;
        private int warnCount;
        private String ghLink;

        public BugStat(String title, int count, int warnCount, String query) {
            this.title = title;
            this.count = count;
            this.warnCount = warnCount;

            query = GHQuery.encode("is:issue" + (query != null ? " " + query : ""));
            ghLink = "https://github.com/keycloak/keycloak/issues?q=" + query;
        }

        public String getTitle() {
            return title;
        }

        public int getCount() {
            return count;
        }

        public String getGhLink() {
            return ghLink;
        }

        public String getCssClasses() {
            if (warnCount >= 0) {
                return count < warnCount ? "success" : "warn";
            } else {
                return "blank";
            }
        }
    }

    public class AreaStat {
        private String area;
        private int open;
        private int triage;

        private String ghLink;
        private String ghOpenLink;

        private String ghTriageLink;

        public AreaStat(String area, int open, int triage) {
            this.area = area;
            this.open = open;
            this.triage = triage;

            String link = "https://github.com/keycloak/keycloak/issues";

            ghLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:" + area);
            ghOpenLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage label:" + area);
            ghTriageLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage label:" + area);
        }

        public String getTitle() {
            return area.replaceFirst("area/", "");
        }

        public String getArea() {
            return area;
        }

        public int getTotal() {
            return open + triage;
        }

        public int getOpen() {
            return open;
        }

        public int getTriage() {
            return triage;
        }

        public String getGhLink() {
            return ghLink;
        }

        public String getGhOpenLink() {
            return ghOpenLink;
        }

        public String getGhTriageLink() {
            return ghTriageLink;
        }

        public String getOpenCssClasses() {
            return open < Constants.BUG_AREA_OPEN_WARN ? "success" : "warn";
        }

        public String getTriageCssClasses() {
            return triage < Constants.BUG_AREA_TRIAGE_WARN ? "success" : "warn";
        }
    }

}