package org.keycloak.dashboard;

import org.keycloak.dashboard.util.Date;
import org.keycloak.dashboard.util.GHQuery;
import org.kohsuke.github.GitHub;

import java.util.LinkedList;
import java.util.List;

public class PR {

    private GitHub gitHub;

    private List<PRStat> stats;
    private List<String> warnings;

    public PR(GitHub gitHub, boolean mockGitHub) {
        this.gitHub = gitHub;

        int open;
        int priority;

        int olderThan6Months;
        int olderThan12Months;
        int olderThan18Months;

        int createdLast7Days;
        int closedLast7Days;

        int createdLast30Days;
        int closedLast30Days;

        if (!mockGitHub) {
            System.out.print("Fetching PR counts; ");

            open = getPrCount("is:open");
            priority = getPrCount("is:open label:priority/important,priority/critical");

            olderThan6Months = getPrCount("is:open created:<" + Date.MINUS_6_MONTHS);
            olderThan12Months = getPrCount("is:open created:<" + Date.MINUS_12_MONTHS);
            olderThan18Months = getPrCount("is:open created:<" + Date.MINUS_18_MONTHS);

            createdLast7Days = getPrCount("is:open created:<" + Date.MINUS_8_DAYS);
            closedLast7Days = getPrCount("is:closed closed:>" + Date.MINUS_8_DAYS);
            createdLast30Days = getPrCount("is:open created:<" + Date.MINUS_31_DAYS);
            closedLast30Days = getPrCount("is:closed closed:>" + Date.MINUS_31_DAYS);

            System.out.println();
        } else {
            open = 168;
            priority = 5;

            olderThan6Months = 45;
            olderThan12Months = 23;
            olderThan18Months = 2;

            createdLast7Days = 18;
            closedLast7Days = 17;
            createdLast30Days = 123;
            closedLast30Days = 145;
        }

        warnings = new LinkedList<>();
        if (open > 150) {
            warnings.add("More than 150 open PRs (#" + open + ")");
        }
        if (priority > 0) {
            warnings.add("Several open priority PRs (#" + priority + ")");
        }
        if (olderThan12Months > 0) {
            warnings.add("PRs have been around for more than 12 months (#" + olderThan12Months + ")");
        }

        stats = new LinkedList<>();

        stats.add(new PRStat("Open PRs", open, Constants.PR_OPEN_WARN, "is:open"));
        stats.add(new PRStat("Priority PRs", priority, Constants.PR_PRIORITY_WARN, "is:open label:priority/important,priority/critical"));

        stats.add(new PRStat("Older than 6 months", olderThan6Months, Constants.PR_OLD_6_WARN, "is:open created:<" + Date.MINUS_6_MONTHS));
        stats.add(new PRStat("Older than 12 months", olderThan12Months, Constants.PR_OLD_12_WARN, "is:open created:<" + Date.MINUS_12_MONTHS));
        stats.add(new PRStat("Older than 18 months", olderThan18Months, Constants.PR_OLD_18_WARN, "is:open created:<" + Date.MINUS_18_MONTHS));

        stats.add(new PRStat("Created last 7 days", createdLast7Days, createdLast7Days > closedLast7Days ? 0 : 999, "created:>" + Date.MINUS_8_DAYS));
        stats.add(new PRStat("Closed last 7 days", closedLast7Days, createdLast7Days > closedLast7Days ? 0 : 999, "is:closed closed:>" + Date.MINUS_8_DAYS));

        stats.add(new PRStat("Created last 30 days", createdLast30Days, createdLast30Days > closedLast30Days ? 0 : 999, "created:>" + Date.MINUS_31_DAYS));
        stats.add(new PRStat("Closed last 30 days", closedLast30Days, createdLast30Days > closedLast30Days ? 0 : 999, "is:closed closed:>" + Date.MINUS_31_DAYS));
    }

    public List<PRStat> getStats() {
        return stats;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public class PRStat {

        private String title;
        private int count;
        private int warnCount;
        private String ghLink;

        public PRStat(String title, int count, int warnCount, String query) {
            this.title = title;
            this.count = count;
            this.warnCount = warnCount;

            ghLink = "https://github.com/keycloak/keycloak/pulls";
            if (query != null) {
                ghLink += "?q=" + GHQuery.encode(query);
            }
        }

        public String getTitle() {
            return title;
        }

        public int getCount() {
            return count;
        }

        public String getGhLink() {
            return ghLink;
        }

        public String getCssClasses() {
            if (warnCount >= 0) {
                return count < warnCount ? "success" : "warn";
            } else {
                return "blank";
            }
        }
    }

    private int getPrCount(String query) {
        int count = gitHub.searchIssues().q("repo:keycloak/keycloak is:pr " + query).list().withPageSize(1).getTotalCount();
        System.out.print(".");
        return count;
    }

}
