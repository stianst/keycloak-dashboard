package org.keycloak.dashboard.reports.utils;

public class MathHelper {
    public static long percentage(long sum, long total) {
        return Math.round((double) sum / total * 100);
    }
}
