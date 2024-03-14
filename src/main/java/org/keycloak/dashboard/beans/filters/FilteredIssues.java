package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.Teams;
import org.keycloak.dashboard.util.GHQuery;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilteredIssues {
    public static final String ISSUES_LINK = "https://github.com/keycloak/keycloak/issues";
    private final List<GitHubIssue> issues;
    private Set<IssueFilter> filters;
    public static FilteredIssues create(List<GitHubIssue> issues) {
        return new FilteredIssues(issues);
    }

    private FilteredIssues(List<GitHubIssue> issues) {
        this.issues = issues;
        this.filters = new LinkedHashSet<>();
    }

    public FilteredIssues openBug() {
        filters.add(new OpenBugFilter());
        return this;
    }

    public FilteredIssues team(String team) {
        filters.add(new TeamFilter(team));
        return this;
    }

    public FilteredIssues area(String area) {
        filters.add(new AreaFilter(area));
        return this;
    }

    public FilteredIssues excludeAssignedToSubTeam(String team, Teams teams) {
        filters.add(new ExcludeAssignedToSubTeamFilter(team, teams));
        return this;
    }

    public FilteredIssues milestone(String milestone) {
        filters.add(new MilestoneFilter(milestone, true));
        return this;
    }

    public FilteredIssues backlog(boolean include) {
        filters.add(new BacklogFilter(include));
        return this;
    }

    public FilteredIssues priority(String priority) {
        filters.add(new PriorityFilter(priority));
        return this;
    }

    public FilteredIssues hasPriority(boolean include) {
        filters.add(new HasPriorityFilter(include));
        return this;
    }

    public FilteredIssues triage(boolean include) {
        filters.add(new TriageFilter(include));
        return this;
    }

    public FilteredIssues missingInformation(boolean include) {
        filters.add(new MissingInformationFilter(include));
        return this;
    }

    public FilteredIssues helpWanted(boolean include) {
        filters.add(new HelpWantedFilter(include));
        return this;
    }

    private Predicate<GitHubIssue> toPredicates() {
        return filters.stream().map(IssueFilter::predicate).reduce(Predicate::and).orElse(gitHubIssue -> true);
    }

    public int count() {
        return (int) issues.stream().filter(toPredicates()).count();
    }

    public String ghLink() {
        String query = filters.stream().map(IssueFilter::ghQuery).collect(Collectors.joining(" "));
        return ISSUES_LINK + "?q=" + GHQuery.encode(query);
    }

    @Override
    public FilteredIssues clone() {
        FilteredIssues clone = new FilteredIssues(issues);
        clone.filters = new HashSet<>(filters);
        return clone;
    }
}
