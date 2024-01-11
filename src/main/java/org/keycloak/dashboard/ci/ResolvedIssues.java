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

        GitHubData gitHubData = new GitHubData();
        LinkedList<GitHubIssue> issues = new LinkedList<>();
        GitHubIssue issue = new GitHubIssue();
        issue.setNumber(20455);
        issue.setClosedAt(new Date());
        issues.add(issue);
        gitHubData.setIssues(issues);

        ResolvedIssues resolvedRuns = load(gitHubData);

        FailedRun failedRun = new FailedRun("5550656326");
        FailedJob job = new FailedJob(failedRun, "Base IT", JobConclusion.FAILURE);
        System.out.println(resolvedRuns.getResolved(job).isResolved());

    }

    public static ResolvedIssues load(GitHubData data) throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        ResolvedIssues resolvedIssues = yamlMapper.readValue(new File("failed-jobs-reporting.yml"), ResolvedIssues.class);
        for (ResolvedIssue i : resolvedIssues.issues) {
            if (i.getIssue() == null) {
                i.setResolved(true);
            } else {
                Optional<GitHubIssue> issue = data.getIssues().stream().filter(ghIssue -> ghIssue.getNumber() == i.getIssue()).findFirst();
                if (issue.isPresent() && !issue.get().isOpen()) {
                    i.setResolved(true);
                } else {
                    i.setResolved(false);
                }
                if (issue.isPresent() && i.getDescription() == null) {
                    i.setDescription(issue.get().getTitle());
                }
            }
        }
        return resolvedIssues;
    }


    public List<ResolvedIssue> issues;

    public ResolvedIssue getResolved(FailedRun failedRun) {
        String runMatch = failedRun.getRunId() + "/*";
        for (ResolvedIssue i : issues) {
            for (String r : i.getResolves()) {
                if (r.equals(runMatch)) {
                    return i;
                }
            }
        }
        return null;
    }

    public ResolvedIssue getResolved(FailedJob failedJob) {
        String jobMatch = failedJob.getFailedRun().getRunId() + "/" + failedJob.getName();
        for (ResolvedIssue i : issues) {
            for (String r : i.getResolves()) {
                if (r.equals(jobMatch)) {
                    return i;
                }
            }
        }
        return null;
    }

    public List<ResolvedIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<ResolvedIssue> issues) {
        this.issues = issues;
    }

}
