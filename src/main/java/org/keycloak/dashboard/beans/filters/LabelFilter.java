package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.function.Predicate;

class LabelFilter implements IssueFilter {

    private final String label;
    private final boolean include;

    public LabelFilter(String label, boolean include) {
        this.label = label;
        this.include = include;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> include == gitHubIssue.getLabels().stream().anyMatch(l -> l.equals(label));
    }

    @Override
    public String ghQuery() {
        return (include ? "" : "-") + "label:" + label;
    }

}
