package org.keycloak.dashboard.util;

public class GHQuery {

    public static String encode(String query) {
        return query.replaceAll("=", "%3A").replaceAll(" ", "+");
    }

}
