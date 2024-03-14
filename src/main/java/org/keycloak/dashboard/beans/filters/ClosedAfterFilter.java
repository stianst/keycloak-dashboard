package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.util.DateUtil;

import java.util.Date;
import java.util.function.Predicate;

class ClosedAfterFilter implements IssueFilter {

    private final Date date;

    public ClosedAfterFilter(Date date) {
        this.date = date;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> gitHubIssue.getClosedAt() != null && gitHubIssue.getClosedAt().after(date);
    }

    @Override
    public String ghQuery() {
        return "closed:>=" + DateUtil.toString(date);
    }

}
