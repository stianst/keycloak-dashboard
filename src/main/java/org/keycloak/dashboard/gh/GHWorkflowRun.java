package org.keycloak.dashboard.gh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GHWorkflowRun {

    private Long id;
    @JsonProperty("created_at")
    private String createdAt;
    private String event;
    @JsonProperty("run_attempt")
    private int runAttempt;

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
}
