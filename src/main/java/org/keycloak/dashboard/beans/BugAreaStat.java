package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

public class BugAreaStat {
    String area;
    int nextRelease;
    int open;
    int triage;
    int backlog;
    int backlogTriage;

    String ghLink;
    String ghOpenLink;
    String ghNextReleaseLink;

    String ghTriageLink;

    public BugAreaStat(String area, String nextRelease) {
        this.area = area;
        this.nextRelease = 0;
        this.open = 0;
        this.triage = 0;
        this.backlog = 0;
        this.backlogTriage = 0;

        String link = "https://github.com/keycloak/keycloak/issues";

        ghLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:" + area);
        ghNextReleaseLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:" + area + " milestone:" + nextRelease);
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

    public int getNextRelease() {
        return nextRelease;
    }

    public int getOpen() {
        return open;
    }

    public int getTriage() {
        return triage;
    }

    public int getBacklog() {
        return backlog;
    }

    public int getBacklogTriage() {
        return backlogTriage;
    }

    public String getGhLink() {
        return ghLink;
    }

    public String getGhNextReleaseLink() {
        return ghNextReleaseLink;
    }
    public String getGhOpenLink() {
        return ghOpenLink;
    }

    public String getGhTriageLink() {
        return ghTriageLink;
    }

    public String getNextCssClasses() {
        return Css.getCountClass(nextRelease, Config.BUG_AREA_NEXT_WARN, Config.BUG_AREA_NEXT_ERROR);
    }
    public String getOpenCssClasses() {
        return Css.getCountClass(open, Config.BUG_AREA_OPEN_WARN, Config.BUG_AREA_OPEN_ERROR);
    }

    public String getTriageCssClasses() {
        return Css.getCountClass(triage, Config.BUG_AREA_TRIAGE_WARN, Config.BUG_AREA_TRIAGE_ERROR);
    }

    public String getBacklogTriageCssClasses() {
        return Css.getCountClass(backlogTriage, Config.BUG_AREA_BACKLOG_TRIAGE_WARN, Config.BUG_AREA_BACKLOG_TRIAGE_ERROR);
    }
    public String getBacklogCssClasses() {
        return Css.getCountClass(backlog, Config.BUG_AREA_BACKLOG_WARN, Config.BUG_AREA_BACKLOG_ERROR);
    }
}
