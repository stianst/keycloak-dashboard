package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

public class PRStat {

    private String title;
    private int openCount;
    private int openWarnCount;
    private int openErrorCount;
    private String openGhLink;

    private Integer closedCount;
    private Integer closedWarnCount;
    private int closedErrorCount;
    private String closedGhLink;

    public PRStat(String title, int openCount, int openWarnCount, int openErrorCount, String openQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openErrorCount = openErrorCount;
        this.openGhLink = getQueryGhLink(openQuery);
    }

    public PRStat(String title, int openCount, int openWarnCount, int openErrorCount, String openQuery, int closedCount, int closedWarnCount, int closedErrorCount, String closedQuery) {
        this.title = title;
        this.openCount = openCount;
        this.openWarnCount = openWarnCount;
        this.openErrorCount = openErrorCount;
        this.openGhLink = getQueryGhLink(openQuery);
        this.closedCount = closedCount;
        this.closedWarnCount = closedWarnCount;
        this.closedErrorCount = closedErrorCount;
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
        return Css.getCountClass(openCount, openWarnCount, openErrorCount, true);
    }
    public String getClosedCssClasses() {
        return Css.getCountClass(closedCount, closedWarnCount, closedErrorCount, true);
    }
}
