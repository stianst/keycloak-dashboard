package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GitHubIssue {

    public Date createdAt;

    public Date updatedAt;
    public Date closedAt;

    @JsonProperty
    public int number;

    @JsonProperty
    public String title;

    @JsonProperty
    public String milestone;
    @JsonProperty
    public List<String> labels;

    @JsonProperty
    public boolean hasAssignee;

    @JsonProperty
    public int commentsCount;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public boolean isHasAssignee() {
        return hasAssignee;
    }

    public void setHasAssignee(boolean hasAssignee) {
        this.hasAssignee = hasAssignee;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    @JsonIgnore
    public boolean isOpen() {
        return closedAt == null;
    }

    @JsonIgnore
    public boolean isTriage() {
        return labels.contains("status/triage");
    }

    @JsonIgnore
    public List<String> getAreas() {
        return labels.stream().filter(l -> l.startsWith("area/")).collect(Collectors.toList());
    }
}
