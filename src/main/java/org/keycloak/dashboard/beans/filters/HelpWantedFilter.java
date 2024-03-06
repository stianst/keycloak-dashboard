package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.function.Predicate;

class HelpWantedFilter implements IssueFilter {

    private final boolean include;

    public HelpWantedFilter(boolean include) {
        this.include = include;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> include == gitHubIssue.getLabels().stream().anyMatch(l -> l.equals("help wanted"));
    }

    @Override
    public String ghQuery() {
        return (include ? "" : "-") + "label:\"help wanted\"";
    }

}
