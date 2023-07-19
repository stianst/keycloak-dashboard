package org.keycloak.dashboard.ci;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FailedRun {

    private String runId;

    private Date date;

    private ResolvedIssue resolvedBy;
    private List<FailedJob> failedJobs = new LinkedList<>();

    public FailedRun(String runId) {
        this.runId = runId;
    }

    public String getRunId() {
        return runId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<FailedJob> getFailedJobs() {
        return failedJobs;
    }

    public void add(FailedJob failedJob) {
        this.failedJobs.add(failedJob);
    }

    public void addAll(List<FailedJob> failedJob) {
        this.failedJobs.addAll(failedJob);
    }

    public void setFailedJobs(List<FailedJob> failedJobs) {
        this.failedJobs = failedJobs;
    }

    public ResolvedIssue getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(ResolvedIssue resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
}
