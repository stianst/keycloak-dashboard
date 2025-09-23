package org.keycloak.dashboard.gh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        List<GHWorkflowRun> list = new LinkedList<>();
        Set<Long> added = new HashSet<>();

        for (GHWorkflowRuns i : runs) {
            for (GHWorkflowRun r : i.getWorkflowRuns()) {
                if (!added.contains(r.getId())) {
                    added.add(r.getId());
                    list.add(r);
                }
            }
        }

        GHWorkflowRuns combined = new GHWorkflowRuns();
        combined.setWorkflowRuns(list);
        combined.setTotalCount(list.size());
        return combined;
    }

}
