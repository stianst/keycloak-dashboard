package org.keycloak.dashboard.ci;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubIssue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ResolvedIssues {

    public static void main(String[] args) throws IOException {
        ResolvedIssues resolvedRuns = load();

        GitHubData gitHubData = new GitHubData();
        LinkedList<GitHubIssue> issues = new LinkedList<>();
        GitHubIssue issue = new GitHubIssue();
        issue.setNumber(20455);
        issue.setClosedAt(new Date());
        issues.add(issue);
        gitHubData.setIssues(issues);

        FailedRun failedRun = new FailedRun("5550656326");
        FailedJob job = new FailedJob(failedRun, "Base IT", JobConclusion.FAILURE);
        System.out.println(resolvedRuns.isResolved(job, gitHubData));

    }

    public static ResolvedIssues load() throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        return yamlMapper.readValue(new File("resolved-issues.yml"), ResolvedIssues.class);
    }


    public List<ResolvedIssue> issues;

    public boolean isResolved(FailedRun failedRun, GitHubData data) {
        String runMatch = failedRun.getRunId() + "/*";
        for (ResolvedIssue i : issues) {
            for (String r : i.getResolves()) {
                if (r.equals(runMatch)) {
                    failedRun.setResolvedBy(i);
                    if (isResolved(i, data)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isResolved(FailedJob failedJob, GitHubData data) {
        String jobMatch = failedJob.getFailedRun().getRunId() + "/" + failedJob.getName();

        for (ResolvedIssue i : issues) {
            for (String r : i.getResolves()) {
                if (r.equals(jobMatch)) {
                    failedJob.setResolvedBy(i);
                    if (isResolved(i, data)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isResolved(ResolvedIssue resolvedIssue, GitHubData data) {
        Integer ghIssueNumber = resolvedIssue.getIssue();
        if (ghIssueNumber == null) {
            return true;
        } else {
            Optional<GitHubIssue> issue = data.getIssues().stream().filter(ghIssue -> ghIssue.getNumber() == ghIssueNumber).findFirst();
            if (issue.isPresent() && !issue.get().isOpen()) {
                return true;
            }
        }
        return false;
    }

    public List<ResolvedIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<ResolvedIssue> issues) {
        this.issues = issues;
    }

}
