package org.keycloak.dashboard.reports;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.TeamMembers;
import org.keycloak.dashboard.reports.utils.Data;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPRsReport {

    protected final GitHubData data;
    protected final TeamMembers teamMembers;

    public AbstractPRsReport() throws IOException {
        data = Data.loadGitHubData();
        teamMembers = Data.loadTeamMembers();
    }

    public List<Date> getMonthsFromExpiration() {
        List<Date> months = new LinkedList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(Config.EXPIRATION_OLD_ISSUES);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        while (cal.before(Calendar.getInstance())) {
            Date after = cal.getTime();
            cal.add(Calendar.MONTH, 1);
            months.add(cal.getTime());
        }

        return months;
    }

}