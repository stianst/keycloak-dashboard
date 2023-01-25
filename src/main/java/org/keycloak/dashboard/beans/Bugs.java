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

    private List<BugStat> stats;

    private List<BugAreaStat> areaStats;
    private List<BugTeamStat> teamStats;

    private List<FlakyTest> flakyTests;

    public Bugs(GitHubData data, Teams teams) {
        List<GitHubIssue> issues = data.getIssues();

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

        flakyTests = issues.stream().filter(i -> i.hasLabel("flaky-test") && i.isOpen()).map(f -> new FlakyTest(f)).sorted(Comparator.comparing(FlakyTest::getUpdatedAt).reversed()).collect(Collectors.toList());

        areaStats = convertToAreaStats(issues);
        teamStats = convertToTeamStats(issues, teams);

        stats = new LinkedList<>();

        stats.add(new BugStat("With PR", data.getIssuesWithPr(), Config.BUG_PR_WARN, "is:open label:kind/bug linked:pr"));

        stats.add(new BugStat("Total", open, Config.BUG_OPEN_WARN, "is:open label:kind/bug -label:status/triage"));

        stats.add(new BugStat("Priority", priority, Config.BUG_PRIORITY_WARN, "is:open label:kind/bug label:priority/important,priority/critical"));

        stats.add(new BugStat("Non-triaged", nonTriaged, Config.BUG_TRIAGE_WARN, "is:open label:kind/bug label:status/triage"));

        stats.add(new BugStat("Missing area", missingAreaLabel, Config.BUG_AREA_MISSING_WARN, "is:open label:kind/bug " + data.getAreas().stream().map(s -> "-label:" + s).collect(Collectors.joining(" "))));

        stats.add(new BugStat("Old without comments", oldWithoutComments, Config.BUG_OLD_NO_COMMENT_WARN, "is:issue is:open label:kind/bug comments:0 updated:<=" + DateUtil.MINUS_6_MONTHS_STRING));

        stats.add(new BugStat("Last 7 days",
                createdLast7Days, createdLast7Days > closedLast7Days ? 0 : 999, "label:kind/bug created:>=" + DateUtil.MINUS_7_DAYS_STRING,
                closedLast7Days, createdLast7Days > closedLast7Days ? 0 : 999, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_7_DAYS_STRING));

        stats.add(new BugStat("Last 30 days",
                createdLast30Days, createdLast30Days > closedLast30Days ? 0 : 999, "label:kind/bug created:>=" + DateUtil.MINUS_30_DAYS_STRING,
                closedLast30Days, createdLast30Days > closedLast30Days ? 0 : 999, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_30_DAYS_STRING));

        stats.add(new BugStat("Last 90 days",
                createdLast90Days, createdLast90Days > closedLast90Days ? 0 : 999, "label:kind/bug created:>=" + DateUtil.MINUS_90_DAYS_STRING,
                closedLast90Days, createdLast90Days > closedLast90Days ? 0 : 999, "is:closed label:kind/bug closed:>=" + DateUtil.MINUS_90_DAYS_STRING));

        issues.stream().filter(i -> i.getMilestone() != null)
                .collect(Collectors.groupingBy(GitHubIssue::getMilestone, Collectors.toList())).entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach((e) -> {
                            int openCount = (int) e.getValue().stream().filter(i -> i.isOpen()).count();
                            int closedCount = (int) e.getValue().stream().filter(i -> !i.isOpen()).count();
                            if (openCount > 0) {
                                stats.add(new BugStat("Milestone: " + e.getKey(),
                                        openCount, 10, "is:open label:kind/bug milestone:" + e.getKey(),
                                        closedCount, -1, "is:closed label:kind/bug milestone:" + e.getKey()));
                            }
                        });
    }

    private List<BugAreaStat> convertToAreaStats(List<GitHubIssue> issues) {
        Map<String, BugAreaStat> areas = new LinkedHashMap<>();
        for (GitHubIssue i : issues) {
            if (i.isOpen()) {
                for (String a : i.getAreas()) {
                    BugAreaStat areaStat = areas.computeIfAbsent(a, s -> new BugAreaStat(a, 0, 0));
                    if (i.isTriage()) {
                        areaStat.triage++;
                    } else {
                        areaStat.open++;
                    }
                }
            }
        }
        return areas.values().stream().sorted(Comparator.comparingInt(BugAreaStat::getTotal).reversed()).collect(Collectors.toList());
    }

    private List<BugTeamStat> convertToTeamStats(List<GitHubIssue> issues, Teams teams) {
        Map<String, BugTeamStat> areas = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : teams.entrySet()) {
            areas.put(e.getKey(), new BugTeamStat(e.getKey(), e.getValue()));
        }

        for (GitHubIssue i : issues) {
            for (String a : i.getAreas()) {
                if (i.isOpen()) {
                    for (BugTeamStat s : areas.values()) {
                        if (s.getAreas().contains(a)) {
                            if (i.isTriage()) {
                                s.triage.add(i.number);
                            } else {
                                s.open.add(i.number);
                            }
                        }
                    }
                }
            }
        }
        return areas.values().stream().sorted(Comparator.comparingInt(BugTeamStat::getTotal).reversed()).collect(Collectors.toList());
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