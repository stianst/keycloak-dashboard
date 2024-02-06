package org.keycloak.dashboard.gh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GHRunJobs {

    private List<GHRunJob> jobs;

    public List<GHRunJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<GHRunJob> jobs) {
        this.jobs = jobs;
    }
}
