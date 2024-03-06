package org.keycloak.dashboard.beans.filters;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IssueFilterBuilder {

    private Set<IssueFilter> filters;

    public static void main(String[] args) {
        IssueFilterBuilder issueFilterBuilder = IssueFilterBuilder.create().backlog(true).hasPriority(false);
        System.out.println(issueFilterBuilder.toGhQuery());
    }

    public static IssueFilterBuilder create() {
        return new IssueFilterBuilder();
    }

    public IssueFilterBuilder milestone(String milestone) {
        filters.add(new MilestoneFilter(milestone, true));
        return this;
    }

    public IssueFilterBuilder backlog(boolean include) {
        filters.add(new BacklogFilter(include));
        return this;
    }

    public IssueFilterBuilder priority(String priority) {
        filters.add(new PriorityFilter(priority));
        return this;
    }

    public IssueFilterBuilder hasPriority(boolean include) {
        filters.add(new HasPriorityFilter(include));
        return this;
    }

    public IssueFilterBuilder triage(boolean include) {
        filters.add(new TriageFilter(include));
        return this;
    }

    public IssueFilterBuilder missingInformation(boolean include) {
        filters.add(new MissingInformationFilter(include));
        return this;
    }

    public IssueFilterBuilder helpWanted(boolean include) {
        filters.add(new HelpWantedFilter(include));
        return this;
    }

    public IssueFilterBuilder() {
        this.filters = new LinkedHashSet<>();
    }

    public Predicate<GitHubIssue> toPredicates() {
        return filters.stream().map(IssueFilter::predicate).reduce(Predicate::and).orElse(gitHubIssue -> true);
    }

    public String toGhQuery() {
        return filters.stream().map(IssueFilter::ghQuery).collect(Collectors.joining(" "));
    }

}
