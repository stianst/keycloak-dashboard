package org.keycloak.dashboard.beans.filters;

class MissingInformationFilter extends LabelFilter {

    public MissingInformationFilter(boolean include) {
        super("status/missing-information", include);
    }

}
