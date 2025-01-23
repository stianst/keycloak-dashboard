package org.keycloak.dashboard.util;

public class Css {

    public static String getCountClass(int count, int warnThreshold, int errorThreshold, boolean greyOnEmpty) {
        if (count == 0 && greyOnEmpty) {
            return "empty";
        } else if (errorThreshold <= 0 && warnThreshold <= 0) {
            return "blank";
        } else if (errorThreshold != -1 && count >= errorThreshold) {
            return "error";
        } if (warnThreshold != -1 && count >= warnThreshold && count > 0) {
            return "warn";
        } else {
            return "success";
        }
    }

}
