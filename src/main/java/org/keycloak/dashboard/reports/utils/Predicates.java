package org.keycloak.dashboard.reports.utils;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.TeamMembers;

import java.util.Date;
import java.util.function.Predicate;

public class Predicates {

    public static Predicate<GitHubIssue> createdAfter(Date after) {
        return issue -> issue.getCreatedAt().after(after);
    }

    public static Predicate<GitHubIssue> closedAfter(Date after) {
        return issue -> issue.getClosedAt().after(after);
    }

    public static Predicate<GitHubIssue> createdBetween(Date after, Date before) {
        return issue -> issue.getCreatedAt().after(after) && issue.getCreatedAt().before(before);
    }

    public static Predicate<GitHubIssue> closedBetween(Date after, Date before) {
        return issue -> issue.getClosedAt().after(after) && issue.getClosedAt().before(before);
    }

    public static Predicate<GitHubIssue> team(TeamMembers teamMembers) {
        return issue -> teamMembers.isDeveloper(issue.getUserLogin(), true) && !teamMembers.isBot(issue.getUserLogin());
    }

    public static Predicate<GitHubIssue> external(TeamMembers teamMembers) {
        return issue -> !teamMembers.isDeveloper(issue.getUserLogin(), true) && !teamMembers.isBot(issue.getUserLogin());
    }

    public static Predicate<GitHubIssue> open() {
        return issue -> issue.isOpen();
    }

    public static Predicate<GitHubIssue> closed() {
        return issue -> !issue.isOpen();
    }

}
