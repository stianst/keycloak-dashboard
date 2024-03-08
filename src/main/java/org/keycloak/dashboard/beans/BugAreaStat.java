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
    String ghBacklogLink;
    String ghTriageBacklogLink;

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
        ghOpenLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage no:milestone label:" + area);
        ghTriageLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage -milestone:Backlog label:" + area);
        ghBacklogLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage milestone:Backlog label:" + area);
        ghTriageBacklogLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage milestone:Backlog label:" + area);
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

    public String getGhBacklogLink() {
        return ghBacklogLink;
    }

    public String getGhTriageBacklogLink() {
        return ghTriageBacklogLink;
    }

    public String getNextCssClasses() {
        return Css.getCountClass(nextRelease, Config.getBugsAreaWarn("Milestone"), Config.getBugsAreaError("Milestone"));
    }
    public String getOpenCssClasses() {
        return Css.getCountClass(open, Config.getBugsAreaWarn("Open"), Config.getBugsAreaError("Open"));
    }

    public String getTriageCssClasses() {
        return Css.getCountClass(triage, Config.getBugsAreaWarn("Triage"), Config.getBugsAreaError("Triage"));
    }

    public String getBacklogTriageCssClasses() {
        return Css.getCountClass(backlogTriage, -1, 1);
    }
    public String getBacklogCssClasses() {
        return Css.getCountClass(backlog, -1, 1);
    }
}
