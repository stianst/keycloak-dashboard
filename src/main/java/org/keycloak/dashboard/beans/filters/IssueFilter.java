package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.function.Predicate;

interface IssueFilter {

    Predicate<GitHubIssue> predicate();

    String ghQuery();

}
