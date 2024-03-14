package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.function.Predicate;

class MissingPriorityFilter implements IssueFilter {

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> gitHubIssue.getLabels().stream().noneMatch(l -> l.startsWith("priority/"));
    }

    @Override
    public String ghQuery() {
        return "-label:priority/blocker,priority/important,priority/normal,priority/low";
    }
}
