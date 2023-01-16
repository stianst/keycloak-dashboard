package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.util.GHQuery;

public class PRStat {

    private String title;
    private int openCount;
    private int openWarnCount;
    private String openGhLink;

    private Integer closedCount;
    private Integer closedWarnCount;
    private String closedGhLink;

    public PRStat(String title, int openCount, int openWarnCount, String openQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openGhLink = getQueryGhLink(openQuery);
    }

    public PRStat(String title, int openCount, int openWarnCount, String openQuery, int closedCount, int closedWarnCount, String closedQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openGhLink = getQueryGhLink(openQuery);
        this.closedCount = closedCount;
        this.closedWarnCount = closedWarnCount;
        this.closedGhLink = getQueryGhLink(closedQuery);
    }

    private String getQueryGhLink(String query) {
        query = GHQuery.encode("is:pr" + (query != null ? " " + query : ""));
        return "https://github.com/keycloak/keycloak/pulls?q=" + query;
    }

    public String getTitle() {
        return title;
    }

    public int getOpenCount() {
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
