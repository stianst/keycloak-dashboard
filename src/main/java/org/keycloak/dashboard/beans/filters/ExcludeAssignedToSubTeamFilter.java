package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ExcludeAssignedToSubTeamFilter implements IssueFilter {
    private final String team;
    private final Teams teams;

    private final Set<String> subTeams;

    public ExcludeAssignedToSubTeamFilter(String team, Teams teams) {
        this.team = team;
        this.teams = teams;
        this.subTeams = getSubTeams();
    }

    @Override
    public Predicate<GitHubIssue> predicate() {
        return gitHubIssue -> subTeams.isEmpty() || gitHubIssue.getLabels().stream().noneMatch(subTeams::contains);
    }

    @Override
    public String ghQuery() {
        return subTeams.isEmpty() ? "" : "-label:" + String.join(",", subTeams);
    }

    private Set<String> getSubTeams() {
        if (team.endsWith("-shared")) {
            return teams.keySet().stream()
                    .filter(s -> s.startsWith(team.substring(0, team.length() - "shared".length())))
                    .filter(s -> !s.equals(team))
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

}
