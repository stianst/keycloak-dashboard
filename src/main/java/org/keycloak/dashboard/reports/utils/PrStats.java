package org.keycloak.dashboard.reports.utils;

import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrStats {

    private GitHubData data;
    private List<Predicate<GitHubIssue>> predicates;

    public PrStats(GitHubData data, List<Predicate<GitHubIssue>> predicates) {
        this.data = data;
        this.predicates = predicates;
    }

    public static PrStats create(GitHubData data, Predicate<GitHubIssue>... predicates) {
        return new PrStats(data, Arrays.asList(predicates));
    }

    public PrStats filter(Predicate<GitHubIssue>... predicates) {
        List<Predicate<GitHubIssue>> p = new LinkedList<>();
        p.addAll(this.predicates);
        p.addAll(Arrays.asList(predicates));
        return new PrStats(this.data, p);
    }

    public long count() {
        Stream<GitHubIssue> stream = data.getPrs().stream();
        for (Predicate<GitHubIssue> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.count();
    }

    public List<GitHubIssue> list() {
        Stream<GitHubIssue> stream = data.getPrs().stream();
        for (Predicate<GitHubIssue> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.collect(Collectors.toList());
    }
}
