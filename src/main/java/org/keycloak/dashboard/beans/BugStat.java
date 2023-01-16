package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.util.GHQuery;

public class BugStat {

    private String title;
    private Integer openCount;
    private Integer openWarnCount;
    private String openGhLink;
    private Integer closedCount;
    private Integer closedWarnCount;
    private String closedGhLink;

    public BugStat(String title, int openCount, int openWarnCount, String openQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openGhLink = getQueryGhLink(openQuery);
    }

    public BugStat(String title, int openCount, int openWarnCount, String openQuery, int closedCount, int closedWarnCount, String closedQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.closedCount = closedCount;
        this.closedWarnCount = closedWarnCount;
        this.openGhLink = getQueryGhLink(openQuery);
        this.closedGhLink = getQueryGhLink(closedQuery);
    }

    private String getQueryGhLink(String query) {
        query = GHQuery.encode("is:issue" + (query != null ? " " + query : ""));
        return "https://github.com/keycloak/keycloak/issues?q=" + query;
    }

    public String getTitle() {
        return title;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public Integer getClosedCount() {
        return closedCount;
    }

    public String getOpenGhLink() {
        return openGhLink;
    }

    public String getClosedGhLink() {
        return closedGhLink;
    }

    public String getOpenCssClasses() {
        if (openWarnCount >= 0) {
            return openCount < openWarnCount ? "success" : "warn";
        } else {
            return "blank";
        }
    }

    public String getClosedCssClasses() {
        if (closedWarnCount >= 0) {
            return closedCount < closedWarnCount ? "success" : "warn";
        } else {
            return "blank";
        }
    }
}
