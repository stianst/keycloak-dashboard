package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;

import java.util.function.Predicate;
import java.util.stream.Collectors;

class MissingTeamFilter implements IssueFilter {

    private final Teams teams;

    public MissingTeamFilter(Teams teams) {
        this.teams = teams;
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> gitHubIssue.getLabels().stream().noneMatch(l -> l.startsWith("team/"));
    }

    @Override
    public String ghQuery() {
        return "-label:" + String.join(",", teams.keySet());
    }
}
