package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.function.Predicate;

class MilestoneFilter implements IssueFilter {

    private final String milestone;
    private final boolean include;

    public MilestoneFilter(String milestone, boolean include) {
        this.milestone = milestone;
        this.include = include;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> include == milestone.equals(gitHubIssue.getMilestone());
    }

    @Override
    public String ghQuery() {
        return (include ? "" : "-") + "milestone:" + milestone;
    }

}
