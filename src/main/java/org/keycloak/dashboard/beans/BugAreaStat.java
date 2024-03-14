package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.beans.filters.FilteredIssues;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

import java.util.LinkedList;
import java.util.List;

public class BugAreaStat {

    private List<BugStat> columns = new LinkedList<>();

    String area;
    private final FilteredIssues issues;

    public BugAreaStat(String area, FilteredIssues issues, String nextRelease) {
        this.area = area;
        this.issues = issues;

        columns.add(BugStat.area(nextRelease)
                .issues(issues.clone().milestone(nextRelease))
                .warnErrorKey("Milestone"));

        columns.add(BugStat.area("Triage")
                .issues(issues.clone().triage(true).backlog(false)));

        columns.add(BugStat.area("Blocker")
                .issues(issues.clone().triage(false).priority("blocker")));

        columns.add(BugStat.area("Important")
                .issues(issues.clone().triage(false).priority("important")));

        columns.add(BugStat.area("Normal")
                .issues(issues.clone().triage(false).priority("normal")));

        columns.add(BugStat.area("Low")
                .issues(issues.clone().triage(false).priority("low")));


    }

    public String getTitle() {
        return area.replaceFirst("area/", "");
    }

    public String getArea() {
        return area;
    }

    public List<BugStat> getColumns() {
        return columns;
    }

    public String getAreaGhLink() {
        return issues.ghLink();
    }

}
