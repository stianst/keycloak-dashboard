package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.beans.filters.FilteredIssues;

import java.util.LinkedList;
import java.util.List;

public class BugTeamStat {

    private String team;
    private final FilteredIssues issues;

    private List<BugStat> columns = new LinkedList<>();

    public BugTeamStat(String team, FilteredIssues issues, String nextRelease) {
        this.team = team;
        this.issues = issues;

        columns.add(BugStat.team(nextRelease)
                .issues(issues.clone().milestone(nextRelease))
                .warnErrorKey("Milestone"));

        columns.add(BugStat.team("Triage")
                .issues(issues.clone().triage(true).backlog(false)));

        columns.add(BugStat.team("Blocker")
                .issues(issues.clone().triage(false).priority("blocker")));

        columns.add(BugStat.team("Important")
                .issues(issues.clone().triage(false).priority("important")));

        columns.add(BugStat.team("Normal")
                .issues(issues.clone().triage(false).priority("normal")));

        columns.add(BugStat.team("Low")
                .issues(issues.clone().triage(false).priority("low")));

        columns.add(BugStat.team("<p>Cleanup</p><p>No priority</p></p>")
                .issues(issues.clone().triage(false).hasPriority(false).missingInformation(false))
                .warnCount(-1).errorCount(1));

        columns.add(BugStat.team("<p>Cleanup</p><p>Backlog</p>")
                .issues(issues.clone().backlog(true))
                .warnCount(-1).errorCount(1));
    }

    public String getTitle() {
        return team.replace("team/", "");
    }

    public List<BugStat> getColumns() {
        return columns;
    }

    public String getTeamGhLink() {
        return issues.ghLink();
    }

}
