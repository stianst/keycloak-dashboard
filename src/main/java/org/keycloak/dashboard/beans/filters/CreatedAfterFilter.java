package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.util.DateUtil;

import java.util.Date;
import java.util.function.Predicate;

class CreatedAfterFilter implements IssueFilter {

    private final Date date;

    public CreatedAfterFilter(Date date) {
        this.date = date;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> gitHubIssue.getCreatedAt().after(date);
    }

    @Override
    public String ghQuery() {
        return "created:>=" + DateUtil.toString(date);
    }

}
