package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BugTeamStat {
    private static final String ISSUES_LINK = "https://github.com/keycloak/keycloak/issues";

    private final String teamQuery;
    private final List<GitHubIssue> openIssues;

    private String team;
    private final String nextRelease;

    private List<Column> columns = new LinkedList<>();

    public BugTeamStat(String team, String teamQuery, List<GitHubIssue> openIssues, String nextRelease) {
        this.team = team;
        this.teamQuery = teamQuery;
        this.openIssues = openIssues;
        this.nextRelease = nextRelease;

        columns.add(new Column(nextRelease,
                i -> nextRelease.equals(i.getMilestone()),
                "milestone:" + nextRelease,
                Config.BUG_TEAM_NEXT_WARN, Config.BUG_TEAM_NEXT_ERROR));

        columns.add(new Column("Triage",
                i -> i.getLabels().contains("status/triage") && (i.getMilestone() == null || !i.getMilestone().equals("Backlog")),
                "label:status/triage -milestone:Backlog",
                Config.BUG_TEAM_TRIAGE_WARN, Config.BUG_TEAM_TRIAGE_ERROR));

        columns.add(new Column("Criticial",
                i -> !i.getLabels().contains("status/triage") && (i.getLabels().contains("priority/blocker")),
                "-label:status/triage label:priority/blocker",
                -1, 1));

        columns.add(new Column("Important",
                i -> !i.getLabels().contains("status/triage") && (i.getLabels().contains("priority/important")),
                "-label:status/triage label:priority/important",
                Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR));

        columns.add(new Column("Normal",
                i -> !i.getLabels().contains("status/triage") && (i.getLabels().contains("priority/normal")),
                "-label:status/triage label:priority/normal",
                Config.BUG_OPEN_WARN, Config.BUG_OPEN_ERROR));

        columns.add(new Column("Low",
                i -> !i.getLabels().contains("status/triage") && (i.getLabels().contains("priority/low")),
                "-label:status/triage label:priority/low",
                Config.BUG_OPEN_WARN, Config.BUG_OPEN_ERROR));

        columns.add(new Column("Triage Backlog",
                i -> i.getLabels().contains("status/triage") && i.getMilestone() != null && i.getMilestone().equals("Backlog"),
                "label:status/triage milestone:Backlog",
                -1, 1));

        columns.add(new Column("No priority",
                i -> !i.getLabels().contains("status/triage") && (!"Backlog".equals(i.getMilestone()) && i.getLabels().stream().noneMatch(l -> l.startsWith("priority/"))),
                "-label:status/triage,priority/blocker,priority/important,priority/normal,priority/low -milestone:Backlog",
                -1, 1));

        columns.add(new Column("Backlog",
                i -> !i.getLabels().contains("status/triage") && !i.getLabels().contains("help wanted") && i.getMilestone() != null && i.getMilestone().equals("Backlog"),
                "-label:status/triage -label:\"help wanted\" milestone:Backlog",
                -1, 1));

        columns.add(new Column("Help Wanted",
                i -> i.getLabels().contains("help wanted"),
                "label:\"help wanted\"",
                -1, 1));
    }

    public String getTitle() {
        return team;
    }

    public int getTotal() {
        return openIssues.size();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getTeamGhLink() {
        return getLink(null);
    }

    public final class Column {
        String label;

        String link;

        int count;

        int warnCount;

        int errorCount;

        public Column(String label, Predicate<GitHubIssue> predicate, String query, int warnCount, int errorCount) {
            this.label = label;
            this.link = getLink(query);
            this.count = getFilteredCount(predicate);
            this.warnCount = warnCount;
            this.errorCount = errorCount;
        }

        public String getLabel() {
            return label;
        }

        public String getGhLink() {
            return link;
        }

        public int getCount() {
            return count;
        }

        public String getCssClasses() {
            return Css.getCountClass(count, warnCount, errorCount);
        }
    }

    private String getLink(String query) {
        String q = query == null ? teamQuery : teamQuery + " " + query;
        return ISSUES_LINK + "?q=" + GHQuery.encode(q);
    }

    private int getFilteredCount(Predicate<GitHubIssue> predicate) {
        return (int) openIssues.stream().filter(predicate).count();
    }
}
