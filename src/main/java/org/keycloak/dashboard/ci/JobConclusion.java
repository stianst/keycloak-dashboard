package org.keycloak.dashboard.ci;

enum JobConclusion {
    SUCCESS,
    FAILURE,
    CANCELLED,
    SKIPPED;

    static JobConclusion fromLog(String value) {
        return valueOf(value.substring(1, value.length() - 1).toUpperCase());
    }
}
