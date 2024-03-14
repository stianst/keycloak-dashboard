package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.beans.filters.FilteredIssues;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.DateUtil;
import org.keycloak.dashboard.util.GHQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Bugs {

    private final List<GitHubIssue> issues;
    private String nextRelease;

    private List<BugStat> stats;

    private List<BugAreaStat> areaStats;
    private List<BugTeamStat> teamStats;

    private List<FlakyTest> flakyTests;

    public Bugs(GitHubData data, Teams teams) {
        issues = data.getIssues().stream().filter(i -> i.getLabels().contains("kind/bug")).collect(Collectors.toList());

        nextRelease = issues.stream().filter(i -> i.isOpen() && i.getMilestone() != null && i.getMilestone().endsWith(".0.0")).map(i -> i.getMilestone()).sorted().findFirst().get();

        int createdLast7Days = (int) issues.stream().filter(i -> i.getCreatedAt().after(DateUtil.MINUS_7_DAYS)).count();
        int closedLast7Days = (int) issues.stream().filter(i -> i.getClosedAt() != null && i.getClosedAt().after(DateUtil.MINUS_7_DAYS)).count();
        int createdLast30Days = (int) issues.stream().filter(i -> i.getCreatedAt().after(DateUtil.MINUS_30_DAYS)).count();
        int closedLast30Days = (int) issues.stream().filter(i -> i.getClosedAt() != null && i.getClosedAt().after(DateUtil.MINUS_30_DAYS)).count();
        int createdLast90Days = (int) issues.stream().filter(i -> i.getCreatedAt().after(DateUtil.MINUS_90_DAYS)).count();
        int closedLast90Days = (int) issues.stream().filter(i -> i.getClosedAt() != null && i.getClosedAt().after(DateUtil.MINUS_90_DAYS)).count();

        flakyTests = issues.stream()
                .filter(i -> i.hasLabel("flaky-test") && i.isOpen() && i.getTitle().startsWith("Flaky test:")).map(FlakyTest::new)
                .sorted(Comparator.comparing(FlakyTest::getUpdatedAt).reversed())
                .collect(Collectors.toList());

        areaStats = convertToAreaStats(issues);
        teamStats = convertToTeamStats(issues, teams);

        stats = new LinkedList<>();

        stats.add(new BugStat("With PR", data.getIssuesWithPr(), "is:open label:kind/bug linked:pr"));

        stats.add(new BugStat("Open", i -> i.isOpen() && !i.isTriage(), "is:open label:kind/bug -label:status/triage"));
        stats.add(new BugStat("Triage", i -> i.isOpen() && i.isTriage(), "is:open label:kind/bug label:status/triage"));

        stats.add(new BugStat("Weakness", i -> i.isOpen() && i.hasLabel("kind/weakness"), "is:open label:kind/bug label:kind/weakness"));

        stats.add(new BugStat("Blocker", i -> i.isOpen() && i.hasLabel("priority/blocker"), "is:open label:kind/bug label:priority/blocker"));
        stats.add(new BugStat("Important", i -> i.isOpen() && i.hasLabel("priority/important"), "is:open label:kind/bug label:priority/important"));
        stats.add(new BugStat("Normal", i -> i.isOpen() && i.hasLabel("priority/normal"), "is:open label:kind/bug label:priority/normal"));
        stats.add(new BugStat("Low", i -> i.isOpen() && i.hasLabel("priority/low"), "is:open label:kind/bug label:priority/low"));

        stats.add(new BugStat("Last 7 days",
                createdLast7Days, -1, createdLast7Days > closedLast7Days ? 1 : Integer.MAX_VALUE, "label:kind/bug created:>=" + DateUtil.MINUS_7_DAYS_STRING,
                closedLast7Days, -1, createdLast7Days > closedLast7Days ? 1 : Integer.MAX_VALUE, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_7_DAYS_STRING));

        stats.add(new BugStat("Last 30 days",
                createdLast30Days, -1, createdLast30Days > closedLast30Days ? 1 : Integer.MAX_VALUE, "label:kind/bug created:>=" + DateUtil.MINUS_30_DAYS_STRING,
                closedLast30Days, -1, createdLast30Days > closedLast30Days ? 1 : Integer.MAX_VALUE, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_30_DAYS_STRING));

        stats.add(new BugStat("Last 90 days",
                createdLast90Days, -1, createdLast90Days > closedLast90Days ? 1 : Integer.MAX_VALUE, "label:kind/bug created:>=" + DateUtil.MINUS_90_DAYS_STRING,
                closedLast90Days, -1, createdLast90Days > closedLast90Days ? 1 : Integer.MAX_VALUE, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_90_DAYS_STRING));

        issues.stream().filter(i -> i.getMilestone() != null)
                .collect(Collectors.groupingBy(GitHubIssue::getMilestone, Collectors.toList())).entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach((e) -> {
                            int openCount = (int) e.getValue().stream().filter(i -> i.isOpen()).count();
                            int closedCount = (int) e.getValue().stream().filter(i -> !i.isOpen()).count();
                            if (openCount > 0) {
                                stats.add(new BugStat("Milestone: " + e.getKey(), "Milestone",
                                        openCount, "is:open label:kind/bug milestone:" + e.getKey(),
                                        closedCount, "is:closed label:kind/bug milestone:" + e.getKey()));
                            }
                        });

        stats.add(new BugStat("Missing Area", i -> i.isOpen() && i.getAreas().isEmpty(), "is:open label:kind/bug " + data.getAreas().stream().map(s -> "-label:" + s).collect(Collectors.joining(" "))));
        stats.add(new BugStat("Missing Priority", i -> i.isOpen() && !i.getLabels().stream().anyMatch(l -> l.equals("status/missing-information") || l.startsWith("priority/")), "is:open label:kind/bug -label:priority/critical,priority/important,priority/normal,priority/low,status/missing-information"));
        stats.add(new BugStat("Missing Team", i -> i.isOpen() && !i.getLabels().stream().anyMatch(l -> l.startsWith("team/")), "is:open label:kind/bug -label:" + teams.keySet().stream().collect(Collectors.joining(","))));
        stats.add(new BugStat("Missing Information", i -> i.isOpen() && i.getLabels().stream().anyMatch(l -> l.equals("status/missing-information")), "is:open label:kind/bug label:status/missing-information"));
    }

    private List<BugAreaStat> convertToAreaStats(List<GitHubIssue> issues) {
        FilteredIssues filteredIssues = FilteredIssues.create(issues).openBug();
        List<BugAreaStat> areaStats = new LinkedList<>();
        Set<String> allAreas = issues.stream().map(GitHubIssue::getLabels).flatMap(List::stream).filter(l -> l.startsWith("area/")).collect(Collectors.toSet());
        for (String area : allAreas) {
            FilteredIssues areaIssues = filteredIssues.clone().area(area);
            int openCount = areaIssues.count();
            if (areaIssues.count() > 0) {
                areaStats.add(new BugAreaStat(area, areaIssues, openCount, nextRelease));
            }
        }

        areaStats.sort(Comparator.comparing(BugAreaStat::getOpenCount).reversed());

        return areaStats;
    }

    private List<BugTeamStat> convertToTeamStats(List<GitHubIssue> issues, Teams teams) {
        FilteredIssues filteredIssues = FilteredIssues.create(issues).openBug();
        List<BugTeamStat> teamStats = new LinkedList<>();

        for (String team : teams.keySet()) {
            if (!team.equals("no-team")) {
                FilteredIssues teamIssues = filteredIssues.clone().team(team).excludeAssignedToSubTeam(team, teams);
                teamStats.add(new BugTeamStat(team, teamIssues, nextRelease));
            }
        }

        teamStats.sort(Comparator.comparing(BugTeamStat::getTitle));

        return teamStats;
    }

    private List<String> getSubTeams(Teams teams, String teamName) {
        if (teamName.endsWith("-shared")) {
            return teams.keySet().stream()
                .filter(s -> s.startsWith("team/" + teamName.substring(0, teamName.length() - "shared".length())))
                .filter(s -> !s.equals("team/" + teamName))
                .map(s -> s.replaceFirst("team/", ""))
                .toList();
        } else {
            return Collections.emptyList();
        }
    }

    public String getNextRelease() {
        return nextRelease;
    }

    public List<BugStat> getStats() {
        return stats;
    }

    public List<BugAreaStat> getAreaStats() {
        return areaStats;
    }

    public List<BugTeamStat> getTeamStats() {
        return teamStats;
    }

    public List<FlakyTest> getFlakyTests() {
        return flakyTests;
    }

    public class BugStat {

        private String title;
        private Integer openCount;
        private Integer openWarnCount;
        private int openErrorCount;
        private String openGhLink;
        private Integer closedCount;
        private Integer closedWarnCount;
        private int closedErrorCount;
        private String closedGhLink;

        public BugStat(String title, Predicate<GitHubIssue> predicate, int openWarnCount, int openErrorCount, String openQuery) {
            this.title = title;
            this.openCount = (int) issues.stream().filter(predicate).count();
            this.openWarnCount = openWarnCount;
            this.openErrorCount = openErrorCount;
            this.openGhLink = getQueryGhLink(openQuery);
        }

        public BugStat(String title, Predicate<GitHubIssue> predicate, String openQuery) {
            this.title = title;
            this.openCount = (int) issues.stream().filter(predicate).count();
            this.openWarnCount = Config.getBugsWarn(title);
            this.openErrorCount = Config.getBugsError(title);
            this.openGhLink = getQueryGhLink(openQuery);
        }

        public BugStat(String title, int openCount, String openQuery) {
            this.title = title;
            this.openCount = openCount;
            this.openWarnCount = Config.getBugsWarn(title);
            this.openErrorCount = Config.getBugsError(title);
            this.openGhLink = getQueryGhLink(openQuery);
        }

        public BugStat(String title, int openCount, int openWarnCount, int openErrorCount, String openQuery, int closedCount, int closedWarnCount, int closedErrorCount, String closedQuery) {
            this.title = title;
            this.openCount = openCount;
            this.openWarnCount = openWarnCount;
            this.openErrorCount = openErrorCount;
            this.closedCount = closedCount;
            this.closedWarnCount = closedWarnCount;
            this.openGhLink = getQueryGhLink(openQuery);
            this.closedErrorCount = closedErrorCount;
            this.closedGhLink = getQueryGhLink(closedQuery);
        }

        public BugStat(String title, String warnErrorTitle, int openCount, String openQuery, int closedCount, String closedQuery) {
            this.title = title;
            this.openCount = openCount;
            this.openWarnCount = Config.getBugsWarn(warnErrorTitle);
            this.openErrorCount = Config.getBugsError(warnErrorTitle);
            this.closedCount = closedCount;
            this.closedWarnCount = -1;
            this.openGhLink = getQueryGhLink(openQuery);
            this.closedErrorCount = -1;
            this.closedGhLink = getQueryGhLink(closedQuery);
        }

        private String getQueryGhLink(String query) {
            query = GHQuery.encode("is:issue" + (query != null ? " " + query : ""));
            return "https://github.com/keycloak/keycloak/issues?q=" + query;
        }

        public String getTitle() {
            return title;
        }

        public Integer getOpenCount() {
            return openCount;
        }

        public Integer getClosedCount() {
            return closedCount;
        }

        public String getOpenGhLink() {
            return openGhLink;
        }

        public String getClosedGhLink() {
            return closedGhLink;
        }

        public String getOpenCssClasses() {
            return Css.getCountClass(openCount, openWarnCount, openErrorCount);
        }

        public String getClosedCssClasses() {
            return Css.getCountClass(closedCount, closedWarnCount, closedErrorCount);
        }
    }
}
