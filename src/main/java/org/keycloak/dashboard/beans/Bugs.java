package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Constants;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;
import org.keycloak.dashboard.util.Date;

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

    public Bugs(GitHubData data, Teams teams) {
        List<GitHubIssue> issues = data.getIssues();

        int nonTriaged = (int) issues.stream().filter(GitHubIssue::isTriage).count();
        int open = issues.size() - nonTriaged;
        int missingAreaLabel = (int) issues.stream().filter(i -> i.getAreas().isEmpty()).count();
        int oldWithoutComments = (int) issues.stream().filter(i -> i.getUpdatedAt().before(Date.MINUS_6_MONTHS) && i.getCommentsCount() == 0).count();

        areaStats = convertToAreaStats(issues);
        teamStats = convertToTeamStats(issues, teams);

        stats = new LinkedList<>();
        stats.add(new BugStat("With PR", data.getIssuesWithPr(), Constants.BUG_OPEN_WARN, "is:open label:kind/bug linked:pr"));
        stats.add(new BugStat("Open bugs", open, 10, "is:open label:kind/bug -label:status/triage"));
        stats.add(new BugStat("Non-triaged", nonTriaged, Constants.BUG_TRIAGE_WARN, "is:open label:kind/bug label:status/triage"));
        stats.add(new BugStat("Bugs without area label", missingAreaLabel, Constants.BUG_AREA_MISSING_WARN, "is:open label:kind/bug " + data.getAreas().stream().map(s -> "-label:" + s).collect(Collectors.joining(" "))));
        stats.add(new BugStat("Old bugs without comments", oldWithoutComments, Constants.BUG_OLD_NO_COMMENT_WARN, "is:issue is:open label:kind/bug comments:0 updated:<" + Date.MINUS_6_MONTHS_STRING));

        issues.stream().filter(i -> i.getMilestone() != null)
                .collect(Collectors.groupingBy(GitHubIssue::getMilestone, Collectors.counting())).entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach((e) ->
                        stats.add(new BugStat("Milestone: " + e.getKey(), e.getValue().intValue(), 10, "is:open label:kind/bug milestone:" + e.getKey()))
                );
    }

    private List<BugAreaStat> convertToAreaStats(List<GitHubIssue> issues) {
        Map<String, BugAreaStat> areas = new LinkedHashMap<>();
        for (GitHubIssue i : issues) {
            for (String a : i.getAreas()) {
                BugAreaStat areaStat = areas.computeIfAbsent(a, s -> new BugAreaStat(a, 0, 0));
                if (i.isTriage()) {
                    areaStat.triage++;
                } else {
                    areaStat.open++;
                }
            }
        }
        return areas.values().stream().sorted(Comparator.comparingInt(BugAreaStat::getTotal).reversed()).collect(Collectors.toList());
    }

    private List<BugTeamStat> convertToTeamStats(List<GitHubIssue> issues, Teams teams) {
        Map<String, BugTeamStat> areas = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : teams.entrySet()) {
            areas.put(e.getKey(), new BugTeamStat(e.getKey(), e.getValue(), 0, 0));
        }

        for (GitHubIssue i : issues) {
            for (String a : i.getAreas()) {
                for (BugTeamStat s : areas.values()) {
                    if (s.getAreas().contains(a)) {
                        if (i.isTriage()) {
                            s.triage++;
                        } else {
                            s.open++;
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

}