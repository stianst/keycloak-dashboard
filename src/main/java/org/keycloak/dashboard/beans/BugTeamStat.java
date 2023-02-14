package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.util.GHQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BugTeamStat {

    String team;
    List<String> areas;
    Set<Integer> open = new HashSet<>();
    Set<Integer> triage = new HashSet<>();

    String ghLink;
    String ghOpenLink;

    String ghTriageLink;

    public BugTeamStat(String team, List<String> areas) {
        this.team = team;
        this.areas = areas;

        String link = "https://github.com/keycloak/keycloak/issues";
        String areaLabels = areas.stream().collect(Collectors.joining(","));

        ghLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:" + areaLabels);
        ghOpenLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage label:" + areaLabels);
        ghTriageLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage label:" + areaLabels);
    }

    public String getTitle() {
        return team.substring("team/".length());
    }

    public List<String> getAreas() {
        return areas;
    }

    public int getTotal() {
        return open.size() + triage.size();
    }

    public int getOpen() {
        return open.size();
    }

    public int getTriage() {
        return triage.size();
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
        return open.size() < Config.BUG_TEAM_OPEN_WARN ? "success" : "warn";
    }

    public String getTriageCssClasses() {
        return triage.size() < Config.BUG_TEAM_TRIAGE_WARN ? "success" : "warn";
    }
}
