package org.keycloak.dashboard.util;

public class Css {

    public static String getCountClass(int count, int warnThreshold, int errorThreshold) {
        if (errorThreshold <= 0 && warnThreshold <= 0) {
            return "blank";
        } else if (count >= errorThreshold) {
            return "error";
        } if (count >= warnThreshold && count > 0) {
            return "warn";
        } else {
            return "success";
        }
    }

}
