package org.keycloak.dashboard.beans.filters;

class BlockedExternalFilter extends LabelFilter {

    public BlockedExternalFilter(boolean includeBlocked) {
        super("status/blocked-external", includeBlocked);
    }

}
