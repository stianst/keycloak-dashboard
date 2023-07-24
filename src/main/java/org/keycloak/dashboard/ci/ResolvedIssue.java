package org.keycloak.dashboard.ci;

import java.util.List;

public class ResolvedIssue {

    private String id;
    private String description;
    private Integer issue;
    private boolean resolved;
    private String resolution;
    private List<String> resolves;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getResolves() {
        return resolves;
    }

    public void setResolves(List<String> resolves) {
        this.resolves = resolves;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
