package org.keycloak.dashboard.reports;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.reports.utils.MathHelper;
import org.keycloak.dashboard.reports.utils.PrStats;
import org.keycloak.dashboard.util.DateUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.keycloak.dashboard.reports.utils.Predicates.closed;
import static org.keycloak.dashboard.reports.utils.Predicates.closedBetween;
import static org.keycloak.dashboard.reports.utils.Predicates.createdBetween;
import static org.keycloak.dashboard.reports.utils.Predicates.external;
import static org.keycloak.dashboard.reports.utils.Predicates.team;
import static org.keycloak.dashboard.reports.utils.Printer.print;
import static org.keycloak.dashboard.reports.utils.Printer.printColumnHeaders;
import static org.keycloak.dashboard.reports.utils.Printer.printTableHeader;
import static org.keycloak.dashboard.reports.utils.Printer.println;

public class PRsClosedRatioStats extends AbstractPRsReport {

    public static void main(String[] args) throws IOException {
        new PRsClosedRatioStats();
    }

    public PRsClosedRatioStats() throws IOException {
        byMonth();
    }

    private void byMonth() {
        printTableHeader("Closed Ratio");
        printColumnHeaders("Month", "Team", "External");

        List<Date> months = getMonthsFromExpiration();
        for (int monthIndex = 0; monthIndex + 1 < months.size(); monthIndex++) {
            Date after = months.get(monthIndex);
            Date before = months.get(monthIndex+1);

            print(DateUtil.monthString(after));

            PrStats prStats = PrStats.create(data);

            PrStats team = prStats.filter(team(teamMembers));

            long teamCreated = team.filter(createdBetween(after, before)).count();
            long teamClosed = team.filter(closed()).filter(closedBetween(after, before)).count();

            print(MathHelper.percentage(teamClosed, teamCreated));

            PrStats external = prStats.filter(external(teamMembers));

            long externalCreated = external.filter(createdBetween(after, before)).count();
            long externalClosed = external.filter(closed()).filter(closedBetween(after, before)).count();

            print(MathHelper.percentage(externalClosed, externalCreated));

            println();
        }
    }

}
