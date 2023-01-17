package org.keycloak.dashboard.gh;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPerson;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GitHubLoader {

    private GitHub gitHub;

    private GitHubIssuesLoader issuesLoader;

    private WorkflowRuntimeLoader workflowRuntimeLoader;

    public GitHubLoader() throws IOException {
        gitHub = GitHubBuilder.fromEnvironment().withJwtToken(TokenUtil.token()).build();
        issuesLoader = new GitHubIssuesLoader(gitHub);
        workflowRuntimeLoader = new WorkflowRuntimeLoader();
    }

    public GitHubData load() throws Exception {
        GitHubData data = new GitHubData();
        data.setAreas(queryAreas());
        data.setKeycloakDevelopers(queryDevTeam());
        data.setIssues(loadIssues());
        data.setPrs(loadPRs());
        data.setIssuesWithPr(queryIssuesWithPr());
        data.setPullRequestWaits(workflowRuntimeLoader.load());
        return data;
    }

    public GitHubData update(GitHubData data) throws Exception {
        data.setAreas(queryAreas());
        data.setKeycloakDevelopers(queryDevTeam());
        data.setIssues(updateIssues(data.getIssues()));
        data.setPrs(updatePRs(data.getPrs()));
        data.setIssuesWithPr(queryIssuesWithPr());

        if (data.getPullRequestWaits() == null || data.getPullRequestWaits().isEmpty()) {
            data.setPullRequestWaits(workflowRuntimeLoader.load());
        } else {
            data.setPullRequestWaits(workflowRuntimeLoader.update(data.getPullRequestWaits()));
        }

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

    private List<String> queryDevTeam() throws IOException {
        System.out.print("Fetching kc-developers members: ");
        List<String> members = gitHub.getOrganization("keycloak").getTeamByName("kc-developers").getMembers().stream().map(GHPerson::getLogin).collect(Collectors.toList());
        System.out.println(".");
        return members;
    }

    private List<GitHubIssue> loadIssues() throws IOException {
        return issuesLoader.loadIssues(
                "repo:keycloak/keycloak is:issue is:open label:kind/bug",
                "repo:keycloak/keycloak is:issue is:closed label:kind/bug closed:>=" + Config.EXPIRATION_OLD_ISSUES_STRING);
    }

    private List<GitHubIssue> updateIssues(List<GitHubIssue> issues) throws IOException {
        return issuesLoader.updateIssues(issues, "repo:keycloak/keycloak is:issue label:kind/bug");
    }

    private List<GitHubIssue> loadPRs() throws IOException {
        return issuesLoader.loadPRs(
                "repo:keycloak/keycloak is:pr is:open",
                "repo:keycloak/keycloak is:pr is:closed closed:>=" + Config.EXPIRATION_OLD_ISSUES_STRING);
    }

    private List<GitHubIssue> updatePRs(List<GitHubIssue> issues) throws IOException {
        return issuesLoader.updatePRs(issues, "repo:keycloak/keycloak is:pr");
    }

    private int queryIssuesWithPr() throws IOException {
        System.out.print("Fetching bugs with PRs: ");
        int totalCount = gitHub.searchIssues().q("repo:keycloak/keycloak is:issue is:open label:kind/bug linked:pr").list().withPageSize(1).getTotalCount();
        System.out.println(".");
        return totalCount;
    }



}
