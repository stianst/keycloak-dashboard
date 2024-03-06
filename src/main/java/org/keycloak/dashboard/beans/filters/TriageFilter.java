package org.keycloak.dashboard.beans.filters;

class TriageFilter extends LabelFilter {

    public TriageFilter(boolean include) {
        super("status/triage", include);
    }

}
