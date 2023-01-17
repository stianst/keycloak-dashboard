package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.PullRequestWait;
import org.keycloak.dashboard.util.DateUtil;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkflowWaitTimes {

    private final List<WorkFlowWaitPerMonth> workFlowWaitPerMonthList;
    private GitHubData data;

    public WorkflowWaitTimes(GitHubData data) {
        this.data = data;

        workFlowWaitPerMonthList = data.getPullRequestWaits().stream()
                .filter(p -> data.getKeycloakDevelopers().contains(p.getAuthor()))
                .collect(Collectors.groupingBy(p -> DateUtil.monthString(p.getCompletedAt())))
                .entrySet().stream().map(e -> new WorkFlowWaitPerMonth(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(WorkFlowWaitPerMonth::getMonth).reversed())
                .collect(Collectors.toList());
    }

    public List<WorkFlowWaitPerMonth> getWorkFlowWaitPerMonthList() {
        return workFlowWaitPerMonthList;
    }

    public static class WorkFlowWaitPerMonth {

        private String month;

        private List<PullRequestWait> list;

        public WorkFlowWaitPerMonth(String month, List<PullRequestWait> list) {
            this.month = month;
            this.list = list;
        }

        public String getMonth() {
            return month;
        }

        public long getCount() {
            return list.size();
        }

        public long getAverage() {
            return Math.round(list.stream().mapToDouble(PullRequestWait::getMinutes).average().getAsDouble());
        }

        public long getMin() {
            return list.stream().min(Comparator.comparing(PullRequestWait::getMinutes)).get().getMinutes();
        }

        public long getMax() {
            return list.stream().max(Comparator.comparing(PullRequestWait::getMinutes)).get().getMinutes();
        }

        public long getPercentage60m() {
            return Math.round(100 * list.stream().filter(p -> p.getMinutes() < 60).count() / getCount());
        }

        public long getPercentage90m() {
            return Math.round(100 * list.stream().filter(p -> p.getMinutes() < 90).count() / getCount());
        }

        public long getPercentage120m() {
            return Math.round(100 * list.stream().filter(p -> p.getMinutes() < 120).count() / getCount());
        }

        public long getPercentage180m() {
            return Math.round(100 * list.stream().filter(p -> p.getMinutes() < 180).count() / getCount());
        }

        public List<PullRequestWait> getSlowest() {
            return list.stream()
                    .sorted(Comparator.comparing(PullRequestWait::getMinutes).reversed())
                    .limit(Config.PR_WAIT_TIME_MAX_SLOW)
                    .collect(Collectors.toList());
        }

    }

}
