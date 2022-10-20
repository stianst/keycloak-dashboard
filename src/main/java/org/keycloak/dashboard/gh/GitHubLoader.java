package org.keycloak.dashboard.gh;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GitHubLoader {

    private GitHub gitHub;

    private GitHubIssuesLoader issuesLoader;

    public GitHubLoader() throws IOException {
        gitHub = GitHubBuilder.fromEnvironment().build();
        issuesLoader = new GitHubIssuesLoader(gitHub);
    }

    public GitHubData load() throws IOException {
        GitHubData data = new GitHubData();
        data.setAreas(queryAreas());
        data.setIssues(loadIssues());
        data.setPrs(loadPRs());
        data.setIssuesWithPr(queryIssuesWithPr());
        return data;
    }


    public GitHubData update(GitHubData data) throws IOException {
        data.setAreas(queryAreas());
        data.setIssues(updateIssues(data.getIssues()));
        data.setPrs(updatePRs(data.getPrs()));
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

    private List<GitHubIssue> loadIssues() throws IOException {
        return issuesLoader.loadIssues(
                "repo:keycloak/keycloak is:issue is:open label:kind/bug",
                "repo:keycloak/keycloak is:issue is:closed label:kind/bug closed:>=" + Config.EXPIRATION_OLD_ISSUES_STRING);
    }

    private List<GitHubIssue> updateIssues(List<GitHubIssue> issues) throws IOException {
        return issuesLoader.updateIssues(issues, "repo:keycloak/keycloak is:issue is:open label:kind/bug");
    }

    private List<GitHubIssue> loadPRs() throws IOException {
        return issuesLoader.loadPRs(
                "repo:keycloak/keycloak is:pr is:open",
                "repo:keycloak/keycloak is:pr is:closed closed:>=" + Config.EXPIRATION_OLD_ISSUES_STRING);
    }

    private List<GitHubIssue> updatePRs(List<GitHubIssue> issues) throws IOException {
        return issuesLoader.updatePRs(issues, "repo:keycloak/keycloak is:pr is:open");
    }

    private int queryIssuesWithPr() throws IOException {
        System.out.print("Fetching bugs with PRs: ");
        int totalCount = gitHub.searchIssues().q("repo:keycloak/keycloak is:issue is:open label:kind/bug linked:pr").list().withPageSize(1).getTotalCount();
        System.out.println(".");
        return totalCount;
    }
}
