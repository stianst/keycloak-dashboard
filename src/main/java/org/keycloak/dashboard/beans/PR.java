package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Constants;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.GitHubPRStat;
import org.keycloak.dashboard.util.Date;
import org.keycloak.dashboard.util.GHQuery;

import java.util.LinkedList;
import java.util.List;

public class PR {

    private List<PRStat> stats;

    public PR(GitHubData data) {
        GitHubPRStat prStat = data.getPrStat();

        stats = new LinkedList<>();

        stats.add(new PRStat("Open PRs", prStat.getOpen(), Constants.PR_OPEN_WARN, "is:open"));
        stats.add(new PRStat("Priority PRs", prStat.getPriority(), Constants.PR_PRIORITY_WARN, "is:open label:priority/important,priority/critical"));

        stats.add(new PRStat("Older than 6 months", prStat.getOlderThan6Months(), Constants.PR_OLD_6_WARN, "is:open created:<" + Date.MINUS_6_MONTHS_STRING));
        stats.add(new PRStat("Older than 12 months", prStat.getOlderThan12Months(), Constants.PR_OLD_12_WARN, "is:open created:<" + Date.MINUS_12_MONTHS_STRING));
        stats.add(new PRStat("Older than 18 months", prStat.getOlderThan18Months(), Constants.PR_OLD_18_WARN, "is:open created:<" + Date.MINUS_18_MONTHS_STRING));

        int createdLast7Days = prStat.getCreatedLast7Days();
        int closedLast7Days = prStat.getClosedLast7Days();

        stats.add(new PRStat("Created last 7 days", createdLast7Days, createdLast7Days > closedLast7Days ? 0 : 999, "created:>" + Date.MINUS_8_DAYS_STRING));
        stats.add(new PRStat("Closed last 7 days", closedLast7Days, createdLast7Days > closedLast7Days ? 0 : 999, "is:closed closed:>" + Date.MINUS_8_DAYS_STRING));

        int createdLast30Days = prStat.getCreatedLast30Days();
        int closedLast30Days = prStat.getClosedLast30Days();

        stats.add(new PRStat("Created last 30 days", createdLast30Days, createdLast30Days > closedLast30Days ? 0 : 999, "created:>" + Date.MINUS_31_DAYS_STRING));
        stats.add(new PRStat("Closed last 30 days", closedLast30Days, createdLast30Days > closedLast30Days ? 0 : 999, "is:closed closed:>" + Date.MINUS_31_DAYS_STRING));
    }

    public List<PRStat> getStats() {
        return stats;
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

            query = GHQuery.encode("is:pr" + (query != null ? " " + query : ""));
            ghLink = "https://github.com/keycloak/keycloak/pulls?q=" + query;
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

}
