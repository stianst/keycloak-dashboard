package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PullRequestWait {

    @JsonProperty
    private int number;

    @JsonProperty
    private int minutes;

    @JsonProperty
    private Date mergedAt;

    @JsonProperty
    private Date completedAt;

    @JsonProperty
    private String author;

    @JsonProperty
    private String baseRef;

    public PullRequestWait() {
    }

    public PullRequestWait(int number, String author, int minutes, Date mergedAt, Date completedAt, String baseRef) {
        this.number = number;
        this.author = author;
        this.minutes = minutes;
        this.mergedAt = mergedAt;
        this.completedAt = completedAt;
        this.baseRef = baseRef;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Date getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(Date mergedAt) {
        this.mergedAt = mergedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBaseRef() {
        return baseRef;
    }

    public void setBaseRef(String baseRef) {
        this.baseRef = baseRef;
    }
}
