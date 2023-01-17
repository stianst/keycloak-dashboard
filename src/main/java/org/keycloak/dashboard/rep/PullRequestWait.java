package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public PullRequestWait() {
    }

    public PullRequestWait(int number, int minutes, Date mergedAt, Date completedAt) {
        this.number = number;
        this.minutes = minutes;
        this.mergedAt = mergedAt;
        this.completedAt = completedAt;
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
}
