package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.util.GHQuery;

public class PRStat {

    private String title;
    private int count;
    private int warnCount;
    private String ghLink;

    public PRStat(String title, int count, int warnCount, String query) {
        this.title = title;
        this.count = count;
        this.warnCount = warnCount;

        query = GHQuery.encode("is:pr" + (query != null ? " " + query : ""));
        ghLink = "https://github.com/keycloak/keycloak/pulls?q=" + query;
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
