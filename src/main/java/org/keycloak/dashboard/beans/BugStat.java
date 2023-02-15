package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

public class BugStat {

    private String title;
    private Integer openCount;
    private Integer openWarnCount;
    private int openErrorCount;
    private String openGhLink;
    private Integer closedCount;
    private Integer closedWarnCount;
    private int closedErrorCount;
    private String closedGhLink;

    public BugStat(String title, int openCount, int openWarnCount, int openErrorCount, String openQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openErrorCount = openErrorCount;
        this.openGhLink = getQueryGhLink(openQuery);
    }

    public BugStat(String title, int openCount, int openWarnCount, int openErrorCount, String openQuery, int closedCount, int closedWarnCount, int closedErrorCount, String closedQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openErrorCount = openErrorCount;
        this.closedCount = closedCount;
        this.closedWarnCount = closedWarnCount;
        this.openGhLink = getQueryGhLink(openQuery);
        this.closedErrorCount = closedErrorCount;
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
        return Css.getCountClass(openCount, openWarnCount, openErrorCount);
    }

    public String getClosedCssClasses() {
        return Css.getCountClass(closedCount, closedWarnCount, closedErrorCount);
    }
}
