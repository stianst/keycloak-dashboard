package org.keycloak.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Date {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static String MINUS_6_MONTHS = Date.minusMonths(6);
    public static String MINUS_12_MONTHS = Date.minusMonths(12);
    public static String MINUS_18_MONTHS = Date.minusMonths(18);

    public static String MINUS_8_DAYS = Date.minusDays(8);
    public static String MINUS_31_DAYS = Date.minusDays(31);

    public static String minusMonths(int months) {
        return formatter.format(LocalDateTime.now().minusMonths(months));
    }

    public static String minusDays(int days) {
        return formatter.format(LocalDateTime.now().minusDays(days));
    }

}
