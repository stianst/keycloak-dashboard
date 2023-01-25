package org.keycloak.dashboard.beans;

import org.keycloak.dashboard.rep.GitHubIssue;

import java.util.Date;

public class FlakyTest {

    private GitHubIssue issue;

    public FlakyTest(GitHubIssue issue) {
        this.issue = issue;
    }

    public String getTitle() {
        return issue.getTitle().replace("Flaky test: ", "");
    }

    public String getPackage() {
        String packageName = getTitle();
        return packageName.substring(0, packageName.lastIndexOf('.'));
    }

    public String getTestClass() {
        String testClass = getTitle();
        return testClass.substring(testClass.lastIndexOf('.') + 1).split("#")[0];
    }

    public String getTestMethod() {
        String[] split = getTitle().split("#");
        return split.length > 1 ? split[1] : "";
    }

    public int getCount() {
        return 1 + issue.getCommentsCount();
    }

    public int getNumber() {
        return issue.getNumber();
    }

    public Date getCreatedAt() {
        return issue.createdAt;
    }

    public Date getUpdatedAt() {
        return issue.getUpdatedAt();
    }


}
