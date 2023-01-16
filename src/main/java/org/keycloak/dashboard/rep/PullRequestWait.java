package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PullRequestWait {

    @JsonProperty
    private int number;
    @JsonProperty
    private String title;
    @JsonProperty
    private int minutes;

    @JsonProperty
    private Date mergedAt;

    public PullRequestWait() {
    }

    public PullRequestWait(int number, String title, int minutes, Date mergedAt) {
        this.number = number;
        this.title = title;
        this.minutes = minutes;
        this.mergedAt = mergedAt;
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
}
