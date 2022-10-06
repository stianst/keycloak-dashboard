package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Constants;
import org.keycloak.dashboard.util.GHQuery;

public class BugAreaStat {
    String area;
    int open;
    int triage;

    String ghLink;
    String ghOpenLink;

    String ghTriageLink;

    public BugAreaStat(String area, int open, int triage) {
        this.area = area;
        this.open = open;
        this.triage = triage;

        String link = "https://github.com/keycloak/keycloak/issues";

        ghLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:" + area);
        ghOpenLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage label:" + area);
        ghTriageLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage label:" + area);
    }

    public String getTitle() {
        return area.replaceFirst("area/", "");
    }

    public String getArea() {
        return area;
    }

    public int getTotal() {
        return open + triage;
    }

    public int getOpen() {
        return open;
    }

    public int getTriage() {
        return triage;
    }

    public String getGhLink() {
        return ghLink;
    }

    public String getGhOpenLink() {
        return ghOpenLink;
    }

    public String getGhTriageLink() {
        return ghTriageLink;
    }

    public String getOpenCssClasses() {
        return open < Constants.BUG_AREA_OPEN_WARN ? "success" : "warn";
    }

    public String getTriageCssClasses() {
        return triage < Constants.BUG_AREA_TRIAGE_WARN ? "success" : "warn";
    }
}
