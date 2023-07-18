package org.keycloak.dashboard.reports;

import org.keycloak.dashboard.Config;
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

public class PRsOpenClosedStats extends AbstractPRsReport {

    public static void main(String[] args) throws IOException {
        new PRsOpenClosedStats();
    }

    public PRsOpenClosedStats() throws IOException {
        byMonth();
    }

    private void byMonth() {
        printTableHeader("Open vs Closed");
        printColumnHeaders("Month", "Team", "Team Closed", "External", "External Closed");

        List<Date> months = getMonthsFromExpiration();
        for (int monthIndex = 0; monthIndex + 1 < months.size(); monthIndex++) {
            Date after = months.get(monthIndex);
            Date before = months.get(monthIndex+1);

            print(DateUtil.monthString(after));

            PrStats prStats = PrStats.create(data);

            PrStats team = prStats.filter(team(teamMembers));
            print(team.filter(createdBetween(after, before)).count());
            print(team.filter(closed()).filter(closedBetween(after, before)).count());

            PrStats external = prStats.filter(external(teamMembers));
            print(external.filter(createdBetween(after, before)).count());
            print(external.filter(closed()).filter(closedBetween(after, before)).count());

            println();
        }
    }

}
