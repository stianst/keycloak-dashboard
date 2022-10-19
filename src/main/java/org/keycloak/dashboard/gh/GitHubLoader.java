package org.keycloak.dashboard.gh;

import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.rep.GitHubPRStat;
import org.keycloak.dashboard.util.Date;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedSearchIterable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GitHubLoader {

    private GitHub gitHub;

    public GitHubLoader() throws IOException {
        gitHub = GitHubBuilder.fromEnvironment().build();
    }

    public GitHubData query() throws IOException {
        GitHubData data = new GitHubData();
        data.setPrStat(queryPRStat());
        data.setAreas(queryAreas());
        data.setIssues(queryIssues());
        data.setIssuesWithPr(queryIssuesWithPr());
        return data;
    }

    private List<String> queryAreas() throws IOException {
        System.out.print("Fetching areas: ");
        List<String> areas = new LinkedList<>();
        GHRepository repository = gitHub.getRepository("keycloak/keycloak");
        for (GHLabel l : repository.listLabels()) {
            if (l.getName().startsWith("area/")) {
                areas.add(l.getName());
            }
        }
        System.out.println(".");
        return areas;
    }

    private GitHubPRStat queryPRStat() {
        System.out.print("Fetching PR stats: ");
        GitHubPRStat prStat = new GitHubPRStat();

        prStat.setOpen(queryPrCount("is:open"));
        prStat.setPriority(queryPrCount("is:open label:priority/important,priority/critical"));

        prStat.setOlderThan6Months(queryPrCount("is:open created:<=" + Date.MINUS_6_MONTHS_STRING));
        prStat.setOlderThan12Months(queryPrCount("is:open created:<=" + Date.MINUS_12_MONTHS_STRING));
        prStat.setOlderThan18Months(queryPrCount("is:open created:<=" + Date.MINUS_18_MONTHS_STRING));

        prStat.setCreatedLast7Days(queryPrCount("created:>=" + Date.MINUS_7_DAYS_STRING));
        prStat.setClosedLast7Days(queryPrCount("is:closed closed:>=" + Date.MINUS_7_DAYS_STRING));
        prStat.setCreatedLast30Days(queryPrCount("created:>=" + Date.MINUS_30_DAYS_STRING));
        prStat.setClosedLast30Days(queryPrCount("is:closed closed:>=" + Date.MINUS_30_DAYS_STRING));
        prStat.setCreatedLast90Days(queryPrCount("created:>=" + Date.MINUS_90_DAYS_STRING));
        prStat.setClosedLast90Days(queryPrCount("is:closed closed:>=" + Date.MINUS_90_DAYS_STRING));
        System.out.println();
        return prStat;
    }

    private int queryPrCount(String query) {
        int count = gitHub.searchIssues().q("repo:keycloak/keycloak is:pr " + query).list().withPageSize(1).getTotalCount();
        System.out.print(".");
        return count;
    }

    private List<GitHubIssue> queryIssues() throws IOException {
        List<GitHubIssue> issues = new LinkedList<>();

        System.out.print("Fetching open issues: ");
        issues.addAll(queryIssues("repo:keycloak/keycloak is:issue is:open label:kind/bug"));

        System.out.print("Fetching closed issues: ");
        issues.addAll(queryIssues("repo:keycloak/keycloak is:issue is:closed label:kind/bug closed:>=" + Date.MINUS_90_DAYS_STRING));

        return issues;
    }

    private List<GitHubIssue> queryIssues(String q) throws IOException {
        List<GitHubIssue> issues = new LinkedList<>();

        PagedSearchIterable<GHIssue> list = gitHub.searchIssues().q(q).list();

        int pageSize = 100;
        list.withPageSize(pageSize);

        int fetchCount = 0;
        for (GHIssue i : list) {
            GitHubIssue issue = convert(i);
            issues.add(issue);

            if (fetchCount % pageSize == 0) {
                System.out.print(".");
            }
            fetchCount++;
        }

        System.out.println();

        return issues;
    }

    private GitHubIssue convert(GHIssue i) throws IOException {
        GitHubIssue issue = new GitHubIssue();
        issue.setCreatedAt(i.getCreatedAt());
        issue.setUpdatedAt(i.getUpdatedAt());
        issue.setClosedAt(i.getClosedAt());
        issue.setNumber(i.getNumber());
        issue.setTitle(i.getTitle());
        issue.setCommentsCount(i.getCommentsCount());
        issue.setHasAssignee(i.getAssignee() != null);
        issue.setMilestone(i.getMilestone() != null ? i.getMilestone().getTitle() : null);

        List<String> labels = new LinkedList<>();
        for (GHLabel l : i.getLabels()) {
            labels.add(l.getName());
        }
        issue.setLabels(labels);
        return issue;
    }

    private int queryIssuesWithPr() throws IOException {
        return gitHub.searchIssues().q("repo:keycloak/keycloak is:issue is:open label:kind/bug linked:pr").list().withPageSize(1).getTotalCount();
    }
}
