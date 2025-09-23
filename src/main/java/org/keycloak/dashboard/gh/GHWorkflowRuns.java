package org.keycloak.dashboard.gh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GHWorkflowRuns {

    @JsonProperty("total_count")
    private long totalCount;

    @JsonProperty("workflow_runs")
    private List<GHWorkflowRun> workflowRuns;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<GHWorkflowRun> getWorkflowRuns() {
        return workflowRuns;
    }

    public void setWorkflowRuns(List<GHWorkflowRun> workflowRuns) {
        this.workflowRuns = workflowRuns;
    }

    public static GHWorkflowRuns combine(List<GHWorkflowRuns> runs) {
        GHWorkflowRuns combined = new GHWorkflowRuns();
        combined.setWorkflowRuns(new LinkedList<>());
        for (GHWorkflowRuns r : runs) {
            combined.getWorkflowRuns().addAll(r.getWorkflowRuns());
        }
        combined.setTotalCount(combined.getWorkflowRuns().size());
        return combined;
    }

}
