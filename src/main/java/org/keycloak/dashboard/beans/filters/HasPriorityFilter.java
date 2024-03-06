package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.function.Predicate;

class HasPriorityFilter implements IssueFilter {

    private final boolean include;

    public HasPriorityFilter(boolean include) {
        this.include = include;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> include == gitHubIssue.getLabels().stream().anyMatch(l -> l.startsWith("priority/"));
    }

    @Override
    public String ghQuery() {
        return (include ? "" : "-") + "label:priority/critical,priority/important,priority/normal,priority/low";
    }

}
