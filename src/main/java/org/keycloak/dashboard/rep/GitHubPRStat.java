package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHubPRStat {

    @JsonProperty
    public int open;
    @JsonProperty
    public int priority;
    @JsonProperty
    public int olderThan6Months;
    @JsonProperty
    public int olderThan12Months;
    @JsonProperty
    public int olderThan18Months;
    @JsonProperty
    public int createdLast7Days;
    @JsonProperty
    public int closedLast7Days;
    @JsonProperty
    public int createdLast30Days;
    @JsonProperty
    public int closedLast30Days;

    @JsonProperty
    public int createdLast90Days;
    @JsonProperty
    public int closedLast90Days;

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOlderThan6Months() {
        return olderThan6Months;
    }

    public void setOlderThan6Months(int olderThan6Months) {
        this.olderThan6Months = olderThan6Months;
    }

    public int getOlderThan12Months() {
        return olderThan12Months;
    }

    public void setOlderThan12Months(int olderThan12Months) {
        this.olderThan12Months = olderThan12Months;
    }

    public int getOlderThan18Months() {
        return olderThan18Months;
    }

    public void setOlderThan18Months(int olderThan18Months) {
        this.olderThan18Months = olderThan18Months;
    }

    public int getCreatedLast7Days() {
        return createdLast7Days;
    }

    public void setCreatedLast7Days(int createdLast7Days) {
        this.createdLast7Days = createdLast7Days;
    }

    public int getClosedLast7Days() {
        return closedLast7Days;
    }

    public void setClosedLast7Days(int closedLast7Days) {
        this.closedLast7Days = closedLast7Days;
    }

    public int getCreatedLast30Days() {
        return createdLast30Days;
    }

    public void setCreatedLast30Days(int createdLast30Days) {
        this.createdLast30Days = createdLast30Days;
    }

    public int getClosedLast30Days() {
        return closedLast30Days;
    }

    public void setClosedLast30Days(int closedLast30Days) {
        this.closedLast30Days = closedLast30Days;
    }

    public int getCreatedLast90Days() {
        return createdLast90Days;
    }

    public void setCreatedLast90Days(int createdLast90Days) {
        this.createdLast90Days = createdLast90Days;
    }

    public int getClosedLast90Days() {
        return closedLast90Days;
    }

    public void setClosedLast90Days(int closedLast90Days) {
        this.closedLast90Days = closedLast90Days;
    }
}
