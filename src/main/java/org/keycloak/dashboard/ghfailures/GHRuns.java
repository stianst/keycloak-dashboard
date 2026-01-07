package org.keycloak.dashboard.ghfailures;

import java.util.LinkedList;
import java.util.List;

public class GHRuns {

    private String lastUpdated;
    private List<GHRun> runs = new LinkedList<>();

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<GHRun> getRuns() {
        return runs;
    }

    public void setRuns(List<GHRun> runs) {
        this.runs = runs;
    }
}
