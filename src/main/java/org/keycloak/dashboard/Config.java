package org.keycloak.dashboard;

import org.keycloak.dashboard.util.DateUtil;

import java.util.Date;

public class Config {

    public static final boolean PUBLISH = System.getProperties().containsKey("publish");

    public static final int MAX_HISTORY = 90;

    public static final Date EXPIRATION_OLD_ISSUES = DateUtil.minusdays(MAX_HISTORY);
    public static final String EXPIRATION_OLD_ISSUES_STRING = DateUtil.minusDaysString(MAX_HISTORY);

    public static final int PR_OPEN_WARN = 150;
    public static final int PR_OPEN_ERROR = 200;
    public static final int PR_PRIORITY_WARN = 1;
    public static final int PR_PRIORITY_ERROR = 10;

    public static final int PR_OLD_6_WARN = 50;
    public static final int PR_OLD_6_ERROR = 100;
    public static final int PR_OLD_12_WARN = 1;
    public static final int PR_OLD_12_ERROR = 10;
    public static final int PR_OLD_18_WARN = 1;
    public static final int PR_OLD_18_ERROR = 1;

    public static final int BUG_OPEN_WARN = 100;
    public static final int BUG_OPEN_ERROR = 200;
    public static final int BUG_PRIORITY_WARN = 10;
    public static final int BUG_PRIORITY_ERROR = 50;
    public static final int BUG_PR_WARN = 20;
    public static final int BUG_PR_ERROR = 50;
    public static final int BUG_TRIAGE_WARN = 50;
    public static final int BUG_TRIAGE_ERROR = 100;
    public static final int BUG_OLD_NO_COMMENT_WARN = 10;
    public static final int BUG_OLD_NO_COMMENT_ERROR = 50;
    public static final int BUG_AREA_MISSING_WARN = 1;
    public static final int BUG_AREA_MISSING_ERROR = 10;

    public static final int BUG_AREA_NEXT_WARN = 1;
    public static final int BUG_AREA_NEXT_ERROR = 5;
    public static final int BUG_AREA_OPEN_WARN = 1;
    public static final int BUG_AREA_OPEN_ERROR = 10;
    public static final int BUG_AREA_TRIAGE_WARN = 1;
    public static final int BUG_AREA_TRIAGE_ERROR = 10;
    public static final int BUG_AREA_BACKLOG_WARN = 10;
    public static final int BUG_AREA_BACKLOG_ERROR = 50;
    public static final int BUG_AREA_BACKLOG_TRIAGE_WARN = 10;
    public static final int BUG_AREA_BACKLOG_TRIAGE_ERROR = 50;

    public static final int BUG_TEAM_NEXT_WARN = 1;
    public static final int BUG_TEAM_NEXT_ERROR = 10;
    public static final int BUG_TEAM_OPEN_WARN = 1;
    public static final int BUG_TEAM_OPEN_ERROR = 10;
    public static final int BUG_TEAM_TRIAGE_WARN = 1;
    public static final int BUG_TEAM_TRIAGE_ERROR = 10;
    public static final int BUG_TEAM_BACKLOG_WARN = 10;
    public static final int BUG_TEAM_BACKLOG_ERROR = 50;
    public static final int BUG_TEAM_BACKLOG_TRIAGE_WARN = 10;
    public static final int BUG_TEAM_BACKLOG_TRIAGE_ERROR = 50;

    public static final int PR_WAIT_TIME_MAX_SLOW = 50;
    public static final int PR_WAIT_TIME_SLOW_THRESHOLD = 90;

}
