package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.util.Css;
import org.keycloak.dashboard.util.GHQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BugTeamStat {

    String team;
    List<String> areas;
    Set<Integer> nextRelease = new HashSet<>();
    Set<Integer> open = new HashSet<>();
    Set<Integer> triage = new HashSet<>();
    Set<Integer> backlog = new HashSet<>();
    Set<Integer> backlogTriage = new HashSet<>();

    String ghLink;
    String ghNextReleaseLink;
    String ghOpenLink;

    String ghTriageLink;
    String ghBacklogLink;

    String ghTriageBacklogLink;

    public BugTeamStat(String team, List<String> areas, String nextRelease) {
        this.team = team;
        this.areas = areas;

        String link = "https://github.com/keycloak/keycloak/issues";
        String areaLabels = areas.stream().collect(Collectors.joining(","));

        ghLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:" + areaLabels);
        ghNextReleaseLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug milestone:" + nextRelease + " label:" + areaLabels);
        ghOpenLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage no:milestone label:" + areaLabels);
        ghTriageLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage -milestone:Backlog label:" + areaLabels);
        ghBacklogLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug -label:status/triage milestone:Backlog label:" + areaLabels);
        ghTriageBacklogLink = link + "?q=" + GHQuery.encode("is:issue is:open label:kind/bug label:status/triage milestone:Backlog label:" + areaLabels);
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

    public int getNextRelease() {
        return nextRelease.size();
    }

    public int getOpen() {
        return open.size();
    }

    public int getTriage() {
        return triage.size();
    }

    public int getBacklog() {
        return backlog.size();
    }

    public int getBacklogTriage() {
        return backlogTriage.size();
    }

    public String getGhLink() {
        return ghLink;
    }

    public String getGhOpenLink() {
        return ghOpenLink;
    }

    public String getGhBacklogLink() {
        return ghBacklogLink;
    }

    public String getGhTriageBacklogLink() {
        return ghTriageBacklogLink;
    }

    public String getGhNextReleaseLink() {
        return ghNextReleaseLink;
    }

    public String getGhTriageLink() {
        return ghTriageLink;
    }

    public String getNextCssClasses() {
        return Css.getCountClass(nextRelease.size(), Config.BUG_TEAM_NEXT_WARN, Config.BUG_TEAM_NEXT_ERROR);
    }
    public String getOpenCssClasses() {
        return Css.getCountClass(open.size(), Config.BUG_TEAM_OPEN_WARN, Config.BUG_TEAM_OPEN_ERROR);
    }

    public String getTriageCssClasses() {
        return Css.getCountClass(triage.size(), Config.BUG_TEAM_TRIAGE_WARN, Config.BUG_TEAM_TRIAGE_ERROR);
    }
    public String getBacklogTriageCssClasses() {
        return Css.getCountClass(backlogTriage.size(), Config.BUG_TEAM_BACKLOG_TRIAGE_WARN, Config.BUG_TEAM_BACKLOG_TRIAGE_ERROR);
    }
    public String getBacklogCssClasses() {
        return Css.getCountClass(backlog.size(), Config.BUG_TEAM_BACKLOG_WARN, Config.BUG_TEAM_BACKLOG_ERROR);
    }
}
