package org.keycloak.dashboard.reports;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.keycloak.dashboard.rep.GitHubIssue;
import org.keycloak.dashboard.reports.utils.PrStats;
import org.keycloak.dashboard.util.DateUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import static org.keycloak.dashboard.reports.utils.Predicates.closed;
import static org.keycloak.dashboard.reports.utils.Predicates.closedBetween;
import static org.keycloak.dashboard.reports.utils.Predicates.createdBetween;
import static org.keycloak.dashboard.reports.utils.Predicates.external;
import static org.keycloak.dashboard.reports.utils.Predicates.team;
import static org.keycloak.dashboard.reports.utils.Printer.print;
import static org.keycloak.dashboard.reports.utils.Printer.printColumnHeaders;
import static org.keycloak.dashboard.reports.utils.Printer.printTableHeader;
import static org.keycloak.dashboard.reports.utils.Printer.println;

public class PRsWaitTimesStats extends AbstractPRsReport {

    public static void main(String[] args) throws IOException {
        new PRsWaitTimesStats();
    }

    public PRsWaitTimesStats() throws IOException {
        byMonth();
    }

    private void byMonth() {
        printTableHeader("Waiting Times");
        printColumnHeaders("Month", "Team Average", "Team 80th", "External Average", "External 80th");

        PrStats prStats = PrStats.create(data);
        PrStats team = prStats.filter(team(teamMembers));
        PrStats external = prStats.filter(external(teamMembers));

        DecimalFormat df = new DecimalFormat("0.0");

        List<Date> months = getMonthsFromExpiration();
        for (int monthIndex = 0; monthIndex + 1 < months.size(); monthIndex++) {
            Date after = months.get(monthIndex);
            Date before = months.get(monthIndex + 1);

            print(DateUtil.monthString(after));

            double[] teamWaitTimes = team.filter(closed()).filter(closedBetween(after, before)).list().stream().mapToDouble(i -> getWaitTimeInDays(i)).sorted().toArray();
            double[] externalWaitTimes = external.filter(closed()).filter(closedBetween(after, before)).list().stream().mapToDouble(i -> getWaitTimeInDays(i)).sorted().toArray();

            print(df.format(getAverage(teamWaitTimes)));
            print(df.format(getPercentile(80.0, teamWaitTimes)));
            print(df.format(getAverage(externalWaitTimes)));
            print(df.format(getPercentile(80.0, externalWaitTimes)));

            println();
        }
    }

    private double getAverage(double[] values) {
        return Arrays.stream(values).average().getAsDouble();
    }

    private double getPercentile(double quantile, double[] values) {
        Percentile percentile = new Percentile(quantile);
        percentile.setData(values);
        return percentile.evaluate();
    }

    private double getWaitTimeInDays(GitHubIssue issue) {
        long waitInMs = issue.getClosedAt().getTime() - issue.getCreatedAt().getTime();
        return (double) waitInMs / (24 * 60 * 60 * 1000);
    }

}
