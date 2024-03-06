package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.DateUtil;
import org.keycloak.dashboard.util.GHQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
                .filter(i -> i.hasLabel("flaky-test") && i.isOpen() && i.getTitle().startsWith("Flaky test:")).map(f -> new FlakyTest(f))
                .filter(i -> !(i.getPackage().startsWith("org.keycloak.testsuite.model") && "Backlog".equals(i.getMilestone())))
                .filter(i -> !(i.getPackage().equals("org.keycloak.testsuite.ui.account2") && "Backlog".equals(i.getMilestone())))
                .sorted(Comparator.comparing(FlakyTest::getUpdatedAt).reversed())
                .collect(Collectors.toList());

        areaStats = convertToAreaStats(issues);
        teamStats = convertToTeamStats(issues, teams);

        stats = new LinkedList<>();

        stats.add(new BugStat("With PR", data.getIssuesWithPr(), Config.BUG_PR_WARN, Config.BUG_PR_ERROR, "is:open label:kind/bug linked:pr"));

        stats.add(new BugStat("Open", i -> i.isOpen() && !i.isTriage(), Config.BUG_OPEN_WARN, Config.BUG_OPEN_ERROR, "is:open label:kind/bug -label:status/triage"));
        stats.add(new BugStat("Triage", i -> i.isOpen() && i.isTriage(), Config.BUG_TRIAGE_WARN, Config.BUG_TRIAGE_ERROR, "is:open label:kind/bug label:status/triage"));

        stats.add(new BugStat("Weakness", i -> i.isOpen() && i.hasLabel("kind/weakness"), Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR, "is:open label:kind/bug label:kind/weakness"));

        stats.add(new BugStat("Blocker", i -> i.isOpen() && i.hasLabel("priority/blocker"), Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR, "is:open label:kind/bug label:priority/blocker"));
        stats.add(new BugStat("Important", i -> i.isOpen() && i.hasLabel("priority/important"), Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR, "is:open label:kind/bug label:priority/important"));
        stats.add(new BugStat("Normal", i -> i.isOpen() && i.hasLabel("priority/normal"), Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR, "is:open label:kind/bug label:priority/normal"));
        stats.add(new BugStat("Low", i -> i.isOpen() && i.hasLabel("priority/low"), Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR, "is:open label:kind/bug label:priority/low"));

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
                                stats.add(new BugStat("Milestone: " + e.getKey(),
                                        openCount, 1, 10, "is:open label:kind/bug milestone:" + e.getKey(),
                                        closedCount, -1, -1, "is:closed label:kind/bug milestone:" + e.getKey()));
                            }
                        });

        stats.add(new BugStat("Missing area", i -> i.isOpen() && i.getAreas().isEmpty(), -1, 1, "is:open label:kind/bug " + data.getAreas().stream().map(s -> "-label:" + s).collect(Collectors.joining(" "))));
        stats.add(new BugStat("Missing priority", i -> i.isOpen() && !i.getLabels().stream().anyMatch(l -> l.equals("status/missing-information") || l.startsWith("priority/")), -1, 1, "is:open label:kind/bug -label:priority/critical,priority/important,priority/normal,priority/low,status/missing-information"));
        stats.add(new BugStat("Missing team", i -> i.isOpen() && !i.getLabels().stream().anyMatch(l -> l.startsWith("team/")), -1, 1, "is:open label:kind/bug -label:" + teams.keySet().stream().collect(Collectors.joining(","))));
        stats.add(new BugStat("Missing information", i -> i.isOpen() && i.getLabels().stream().anyMatch(l -> l.equals("status/missing-information")), -1, -1, "is:open label:kind/bug label:status/missing-information"));
    }

    private List<BugAreaStat> convertToAreaStats(List<GitHubIssue> issues) {
        Map<String, BugAreaStat> areas = new LinkedHashMap<>();
        for (GitHubIssue i : issues) {
            if (i.isOpen()) {
                for (String a : i.getAreas()) {
                    BugAreaStat areaStat = areas.computeIfAbsent(a, s -> new BugAreaStat(a, nextRelease));
                    if (nextRelease.equals(i.getMilestone())) {
                        areaStat.nextRelease++;
                    } else if ("Backlog".equals(i.getMilestone())) {
                        if (i.isTriage()) {
                            areaStat.backlogTriage++;
                        } else {
                            areaStat.backlog++;
                        }
                    } else {
                        if (i.isTriage()) {
                            areaStat.triage++;
                        } else {
                            areaStat.open++;
                        }
                    }
                }
            }
        }
        return areas.values().stream().sorted(Comparator.comparingInt(BugAreaStat::getTotal).reversed()).collect(Collectors.toList());
    }

    private List<BugTeamStat> convertToTeamStats(List<GitHubIssue> issues, Teams teams) {
        Map<String, List<GitHubIssue>> teamIssues = new LinkedHashMap<>();
        for (String teamLabel : teams.keySet()) {
            teamIssues.put(teamLabel, new LinkedList<>());
        }

        if (!teamIssues.containsKey("no-team")) {
            teamIssues.put("no-team", new LinkedList<>());
        }

        for (GitHubIssue i : issues) {
            if (i.isOpen()) {
                List<List<GitHubIssue>> addToTeams = teamIssues.entrySet().stream().filter(e -> i.getTeams().contains(e.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
                if (addToTeams.isEmpty()) {
                    addToTeams.add(teamIssues.get("no-team"));
                }

                for (List<GitHubIssue> s : addToTeams) {
                    s.add(i);
                }
            }
        }

        List<BugTeamStat> teamStats = new LinkedList<>();
        for (Map.Entry<String, List<GitHubIssue>> e : teamIssues.entrySet()) {
            String teamName = e.getKey().replaceFirst("team/", "");
            List<String> subTeams = getSubTeams(teams, teamName);
            String teamQuery;
            if (teamName.equals("no-team")) {
                teamQuery = "is:issue is:open label:kind/bug -label:" + teams.keySet().stream().collect(Collectors.joining(","));
            } else {
                teamQuery = "is:issue is:open label:kind/bug label:team/" + teamName;

                if (!subTeams.isEmpty()) {
                    for (String t : subTeams) {
                        teamQuery += " -label:team/" + t;
                    }
                }
            }

            List<GitHubIssue> l = e.getValue();
            if (!subTeams.isEmpty()) {
                l = l.stream().filter(i -> !i.getTeams().stream().anyMatch(t -> subTeams.contains(t.replaceFirst("team/", "")))).toList();
            }

            teamStats.add(new BugTeamStat(teamName, teamQuery, l, nextRelease));
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

        public BugStat(String title, int openCount, int openWarnCount, int openErrorCount, String openQuery) {
            this.title = title;
            this.openCount = openCount;
            this.openWarnCount = openWarnCount;
            this.openErrorCount = openErrorCount;
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
