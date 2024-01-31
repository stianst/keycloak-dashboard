package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;
import org.keycloak.dashboard.util.DateUtil;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Bugs {

    private String nextRelease;

    private List<BugStat> stats;

    private List<BugAreaStat> areaStats;
    private List<BugTeamStat> teamStats;

    private List<FlakyTest> flakyTests;

    public Bugs(GitHubData data, Teams teams) {
        List<GitHubIssue> issues = data.getIssues();

        nextRelease = issues.stream().filter(i -> i.isOpen() && i.getMilestone() != null && i.getMilestone().endsWith(".0.0")).map(i -> i.getMilestone()).sorted().findFirst().get();

        int nonTriaged = (int) issues.stream().filter(i -> i.isOpen() && i.isTriage()).count();
        int open = (int) issues.stream().filter(i -> i.isOpen() && !i.isTriage()).count();
        int missingAreaLabel = (int) issues.stream().filter(i -> i.isOpen() && i.getAreas().isEmpty()).count();
        int oldWithoutComments = (int) issues.stream().filter(i -> i.isOpen() && i.getUpdatedAt().before(DateUtil.MINUS_6_MONTHS) && i.getCommentsCount() == 0).count();
        int priority = (int) issues.stream().filter(i -> i.isOpen() && i.hasLabel("priority/important", "priority/critical")).count();

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

        stats.add(new BugStat("Total", open, Config.BUG_OPEN_WARN, Config.BUG_OPEN_ERROR, "is:open label:kind/bug -label:status/triage"));

        stats.add(new BugStat("Priority", priority, Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR, "is:open label:kind/bug label:priority/important,priority/critical"));

        stats.add(new BugStat("Non-triaged", nonTriaged, Config.BUG_TRIAGE_WARN, Config.BUG_TRIAGE_ERROR, "is:open label:kind/bug label:status/triage"));

        stats.add(new BugStat("Missing area", missingAreaLabel, Config.BUG_AREA_MISSING_WARN, Config.BUG_AREA_MISSING_ERROR, "is:open label:kind/bug " + data.getAreas().stream().map(s -> "-label:" + s).collect(Collectors.joining(" "))));

        stats.add(new BugStat("Old without comments", oldWithoutComments, Config.BUG_OLD_NO_COMMENT_WARN, Config.BUG_OLD_NO_COMMENT_ERROR, "is:issue is:open label:kind/bug comments:0 updated:<=" + DateUtil.MINUS_6_MONTHS_STRING));

        stats.add(new BugStat("Last 7 days",
                createdLast7Days, 0, createdLast7Days > closedLast7Days ? 1 : 999, "label:kind/bug created:>=" + DateUtil.MINUS_7_DAYS_STRING,
                closedLast7Days, 0, createdLast7Days > closedLast7Days ? 1 : 999, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_7_DAYS_STRING));

        stats.add(new BugStat("Last 30 days",
                createdLast30Days, 0, createdLast30Days > closedLast30Days ? 1 : 999, "label:kind/bug created:>=" + DateUtil.MINUS_30_DAYS_STRING,
                closedLast30Days, 0, createdLast30Days > closedLast30Days ? 1 : 999, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_30_DAYS_STRING));

        stats.add(new BugStat("Last 90 days",
                createdLast90Days, 0, createdLast90Days > closedLast90Days ? 1 : 999, "label:kind/bug created:>=" + DateUtil.MINUS_90_DAYS_STRING,
                closedLast90Days, 0, createdLast90Days > closedLast90Days ? 1 : 999, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_90_DAYS_STRING));

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
            String teamQuery;
            if (teamName.equals("no-team")) {
                teamQuery = "is:issue is:open label:kind/bug -label:" + teams.keySet().stream().collect(Collectors.joining(","));
            } else {
                teamQuery = "is:issue is:open label:kind/bug label:team/" + teamName;
            }
            teamStats.add(new BugTeamStat(teamName, teamQuery, e.getValue(), nextRelease));
        }

        teamStats.sort(Comparator.comparing(BugTeamStat::getTitle));

        return teamStats;
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
}