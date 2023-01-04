package org.keycloak.dashboard.ci;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FailedJob {

    private FailedRun failedRun;
    private String name;

    private JobConclusion conclusion;

    public FailedJob(FailedRun failedRun, String name, JobConclusion conclusion) {
        this.failedRun = failedRun;
        this.name = name;
        this.conclusion = conclusion;
    }

    private String failedGoal;

    private List<String> failedTests = new LinkedList<>();

    private List<String> errorLog = new LinkedList<>();

    public String getName() {
        return name;
    }

    public String getJobName() {
        return name.contains("(") ? name.substring(0, name.indexOf('(') - 1) : name;
    }

    public String getProfileName() {
        return name.contains("(") ? name.substring(name.indexOf('(') + 1, name.indexOf(')')) : null;
    }

    public String getAnchor() {
        return getJobName().toLowerCase(Locale.ROOT).replaceAll(" ", "-").replaceAll("\\(", "-").replaceAll("\\)", "");
    }

    public JobConclusion getConclusion() {
        return conclusion;
    }

    public String getFailedGoal() {
        return failedGoal;
    }

    public FailedRun getFailedRun() {
        return failedRun;
    }

    public void setFailedGoal(String failedGoal) {
        this.failedGoal = failedGoal;
    }

    public List<String> getFailedTests() {
        return failedTests;
    }

    public void addFailedTests(String failedTest) {
        this.failedTests.add(failedTest);
    }

    public List<String> getErrorLog() {
        return errorLog;
    }

    public void addErrorLog(String errorLog) {
        this.errorLog.add(errorLog);
    }
}
