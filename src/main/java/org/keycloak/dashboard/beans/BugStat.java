package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.util.GHQuery;

public class BugStat {

    private String title;
    private int count;
    private int warnCount;
    private String ghLink;

    public BugStat(String title, int count, int warnCount, String query) {
        this.title = title;
        this.count = count;
        this.warnCount = warnCount;

        query = GHQuery.encode("is:issue" + (query != null ? " " + query : ""));
        ghLink = "https://github.com/keycloak/keycloak/issues?q=" + query;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

    public String getGhLink() {
        return ghLink;
    }

    public String getCssClasses() {
        if (warnCount >= 0) {
            return count < warnCount ? "success" : "warn";
        } else {
            return "blank";
        }
    }
}
