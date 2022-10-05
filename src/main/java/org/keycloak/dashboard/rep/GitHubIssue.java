package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class GitHubIssue {

    public Date createdAt;

    public Date updatedAt;

    @JsonProperty
    public int number;

    @JsonProperty
    public String title;
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
}
