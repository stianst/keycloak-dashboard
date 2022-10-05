package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Constants;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.util.GHQuery;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;

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

    private List<BugStat> stats;

    private List<AreaStat> areaStats;

    public Bugs(GitHubData data) throws IOException {
        int open = 0;
        int nonTriaged = 0;
        int missingAreaLabel = 0;

        String queryWithoutAreaLabels = getQueryWithoutAreaLabels(data);

        Map<String, AreaStat> areas = new LinkedHashMap<>();
        List<GitHubIssue> list = data.getIssues();

        for (GitHubIssue i : list) {
            Set<String> issuesAreas = new HashSet<>();
            boolean triage = false;
            for (String l : i.getLabels()) {
                if (l.startsWith("area/")) {
                    issuesAreas.add(l);
                } else if (l.equals("status/triage")) {
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
        }

        areaStats = areas.values().stream().sorted(Comparator.comparingInt(AreaStat::getTotal).reversed()).collect(Collectors.toList());

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

    private String getQueryWithoutAreaLabels(GitHubData data) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String a : data.getAreas()) {
            if (a.startsWith("area/")) {
                if (!first) {
                    sb.append(" ");
                }
                sb.append("-label:" + a);
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