package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GitHubData {

    @JsonProperty
    public List<String> areas;
    @JsonProperty
    public GitHubPRStat prStat;
    @JsonProperty
    public List<GitHubIssue> issues;

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    public GitHubPRStat getPrStat() {
        return prStat;
    }

    public void setPrStat(GitHubPRStat prStat) {
        this.prStat = prStat;
    }

    public List<GitHubIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<GitHubIssue> issues) {
        this.issues = issues;
    }
}
