package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.beans.filters.IssueFilterBuilder;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

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
                IssueFilterBuilder.create().milestone(nextRelease),
                Config.BUG_TEAM_NEXT_WARN, Config.BUG_TEAM_NEXT_ERROR));

        columns.add(new Column("Triage",
                IssueFilterBuilder.create().triage(true).backlog(false),
                Config.BUG_TEAM_TRIAGE_WARN, Config.BUG_TEAM_TRIAGE_ERROR));

        columns.add(new Column("Blocker",
                IssueFilterBuilder.create().triage(false).priority("blocker"),
                -1, 1));

        columns.add(new Column("Important",
                IssueFilterBuilder.create().triage(false).priority("important"),
                Config.BUG_PRIORITY_WARN, Config.BUG_PRIORITY_ERROR));

        columns.add(new Column("Normal",
                IssueFilterBuilder.create().triage(false).priority("normal"),
                Config.BUG_OPEN_WARN, Config.BUG_OPEN_ERROR));

        columns.add(new Column("Low",
                IssueFilterBuilder.create().triage(false).priority("low"),
                Config.BUG_OPEN_WARN, Config.BUG_OPEN_ERROR));

        columns.add(new Column("<p>Cleanup<p><p>Triage backlog</p>",
                IssueFilterBuilder.create().triage(true).backlog(true),
                -1, 1));

        columns.add(new Column("<p>Cleanup</p><p>Missing priority and not in backlog</p>",
                IssueFilterBuilder.create().triage(false).hasPriority(false).missingInformation(false).backlog(false),
                -1, 1));

        columns.add(new Column("<p>Cleanup</p><p>Backlog and not help wanted</p>",
                IssueFilterBuilder.create().triage(false).helpWanted(false).backlog(true),
                -1, 1));

        columns.add(new Column("<p>Cleanup</p><p>Backlog with help wanted</p>",
                IssueFilterBuilder.create().backlog(true).helpWanted(true),
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

        public Column(String label, IssueFilterBuilder issueFilterBuilder, int warnCount, int errorCount) {
            this.label = label;
            this.link = getLink(issueFilterBuilder.toGhQuery());
            this.count = getFilteredCount(issueFilterBuilder.toPredicates());
            this.warnCount = warnCount;
            this.errorCount = errorCount;
        }

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
