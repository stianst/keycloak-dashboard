package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;

import java.util.List;
import java.util.function.Predicate;

class MissingAreaFilter implements IssueFilter {

    private final List<String> areas;

    public MissingAreaFilter(List<String> areas) {
        this.areas = areas;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> gitHubIssue.getLabels().stream().noneMatch(l -> l.startsWith("area/"));
    }

    @Override
    public String ghQuery() {
        return "-label:" + String.join(",", areas);
    }
}
