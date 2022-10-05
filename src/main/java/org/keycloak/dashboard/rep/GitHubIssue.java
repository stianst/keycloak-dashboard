package org.keycloak.dashboard.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GitHubIssue {

    @JsonProperty
    public int number;

    @JsonProperty
    public String title;
    @JsonProperty
    public List<String> labels;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
