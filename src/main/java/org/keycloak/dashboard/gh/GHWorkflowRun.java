package org.keycloak.dashboard.gh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GHWorkflowRun {

    private Long id;
    private String name;
    private String status;
    private String conclusion;
    @JsonProperty("head_branch")
    private String headBranch;
    @JsonProperty("created_at")
    private String createdAt;
    private String event;
    @JsonProperty("run_attempt")
    private int runAttempt;
    private String path;

    private Repository repository;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getHeadBranch() {
        return headBranch;
    }

    public void setHeadBranch(String headBranch) {
        this.headBranch = headBranch;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getRunAttempt() {
        return runAttempt;
    }

    public void setRunAttempt(int runAttempt) {
        this.runAttempt = runAttempt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repository {

        @JsonProperty("full_name")
        private String fullName;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

}
