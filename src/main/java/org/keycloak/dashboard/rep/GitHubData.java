package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class GitHubData {

    public Date updatedDate;

    @JsonProperty
    public List<String> areas;
    @JsonProperty
    public GitHubPRStat prStat;
    @JsonProperty
    public List<GitHubIssue> issues;

    public int issuesWithPr;

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

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

    public int getIssuesWithPr() {
        return issuesWithPr;
    }

    public void setIssuesWithPr(int issuesWithPr) {
        this.issuesWithPr = issuesWithPr;
    }
}
