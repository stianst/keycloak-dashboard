package org.keycloak.dashboard.ci;

import java.util.LinkedList;
import java.util.List;

public class FailedJob {

    private String name;

    private JobConclusion conclusion;

    public FailedJob(String name, JobConclusion conclusion) {
        this.name = name;
        this.conclusion = conclusion;
    }

    private String failedGoal;

    private List<String> failedTests = new LinkedList<>();

    private List<String> errorLog = new LinkedList<>();

    public String getName() {
        return name;
    }

    public JobConclusion getConclusion() {
        return conclusion;
    }

    public String getFailedGoal() {
        return failedGoal;
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
