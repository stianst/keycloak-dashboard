package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.beans.filters.FilteredIssues;
import org.keycloak.dashboard.util.Css;

public class BugStat {

    private String label;
    private final BugStatType type;
    FilteredIssues filteredIssues;
    private String warnLabel;
    private Integer warnCount;
    private Integer errorCount;

    public static BugStat team(String label) {
        return new BugStat(label, BugStatType.TEAM);
    }

    public static BugStat area(String label) {
        return new BugStat(label, BugStatType.AREA);
    }

    public BugStat issues(FilteredIssues filteredIssues) {
        this.filteredIssues = filteredIssues;
        return this;
    }

    public BugStat warnErrorKey(String warnErrorKey) {
        this.warnLabel = warnErrorKey;
        return this;
    }

    public BugStat warnCount(int warnCount) {
        this.warnCount = warnCount;
        return this;
    }

    public BugStat errorCount(int errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public String getGhLink() {
        return filteredIssues.ghLink();
    }

    public int getCount() {
        return filteredIssues.count();
    }

    public String getCssClasses() {
        String warnLabel = this.warnLabel != null ? this.warnLabel : label;
        Integer warnCount = this.warnCount;
        Integer errorCount = this.errorCount;

        if (warnCount == null) {
            warnCount = switch (type) {
                case TEAM -> Config.getBugsTeamWarn(warnLabel);
                case AREA -> Config.getBugsAreaWarn(warnLabel);
                case GLOBAL -> Config.getBugsWarn(warnLabel);
            };
        }

        if (errorCount == null) {
            errorCount = switch (type) {
                case TEAM -> Config.getBugsTeamError(warnLabel);
                case AREA -> Config.getBugsAreaError(warnLabel);
                case GLOBAL -> Config.getBugsError(warnLabel);
            };
        }

        return Css.getCountClass(filteredIssues.count(), warnCount, errorCount);
    }

    private BugStat(String label, BugStatType type) {
        this.label = label;
        this.type = type;
    }

    public enum BugStatType {
        GLOBAL,
        TEAM,
        AREA
    }

}
