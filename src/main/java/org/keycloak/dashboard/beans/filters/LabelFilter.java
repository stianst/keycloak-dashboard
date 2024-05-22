package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

class LabelFilter implements IssueFilter {

    private final String labelConcated;
    private final Set<String> labels;
    private final boolean include;

    public LabelFilter(String label, boolean include) {
        this.labels = Set.of(label);
        this.labelConcated = label;
        this.include = include;
    }

    public LabelFilter(Set<String> labels) {
        this.labels = Collections.unmodifiableSet(labels);
        this.labelConcated = String.join(",", labels);
        this.include = true;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> include == gitHubIssue.getLabels().stream().anyMatch(labels::contains);
    }

    @Override
    public String ghQuery() {
        return (include ? "" : "-") + "label:" + labelConcated;
    }

}
