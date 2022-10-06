package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.util.GHQuery;

import java.util.List;
import java.util.stream.Collectors;

public class BugTeamStat {

    String team;
    List<String> areas;
    int open;
    int triage;

    String ghLink;
    String ghOpenLink;

    String ghTriageLink;

    public BugTeamStat(String team, List<String> areas, int open, int triage) {
        this.team = team;
        this.areas = areas;
        this.open = open;
        this.triage = triage;

        String link = "https://github.com/keycloak/keycloak/issues";
        String areaLabels = areas.stream().collect(Collectors.joining(","));

        ghLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:");
        ghOpenLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage label:" + areaLabels);
        ghTriageLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage label:" + areaLabels);
    }

    public String getTitle() {
        return team;
    }

    public List<String> getAreas() {
        return areas;
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
        return open < Config.BUG_TEAM_OPEN_WARN ? "success" : "warn";
    }

    public String getTriageCssClasses() {
        return triage < Config.BUG_TEAM_TRIAGE_WARN ? "success" : "warn";
    }
}
