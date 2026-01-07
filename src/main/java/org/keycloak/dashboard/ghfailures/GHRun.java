package org.keycloak.dashboard.ghfailures;

import java.util.List;

public class GHRun {

    private Long id;
    private Long previousAttemptId;
    private String createdAt;
    private String workflow;
    private String event;
    private String conclusion;
    private String previousAttemptConclusion;
    private int runAttempts;
    private List<GHRunJob> jobs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getRunAttempts() {
        return runAttempts;
    }

    public void setRunAttempts(int runAttempts) {
        this.runAttempts = runAttempts;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public List<GHRunJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<GHRunJob> jobs) {
        this.jobs = jobs;
    }

    public String getPreviousAttemptConclusion() {
        return previousAttemptConclusion;
    }

    public void setPreviousAttemptConclusion(String previousAttemptConclusion) {
        this.previousAttemptConclusion = previousAttemptConclusion;
    }

    public Long getPreviousAttemptId() {
        return previousAttemptId;
    }

    public void setPreviousAttemptId(Long previousAttemptId) {
        this.previousAttemptId = previousAttemptId;
    }

    public static class GHRunJob {

        private String name;
        private String conclusion;

        public String getConclusion() {
            return conclusion;
        }

        public void setConclusion(String conclusion) {
            this.conclusion = conclusion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
