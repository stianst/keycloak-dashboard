package org.keycloak.dashboard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GHQuery {

    public static String encode(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
