package org.keycloak.dashboard.beans.filters;

class PriorityFilter extends LabelFilter {

    public PriorityFilter(String priority) {
        super("priority/" + priority, true);
    }

}
