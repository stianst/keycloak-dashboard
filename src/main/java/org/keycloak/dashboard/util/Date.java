package org.keycloak.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Date {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static java.util.Date MINUS_6_MONTHS = Date.minusMonths(6);

    public static String MINUS_6_MONTHS_STRING = Date.minusMonthsString(6);
    public static String MINUS_12_MONTHS_STRING = Date.minusMonthsString(12);
    public static String MINUS_18_MONTHS_STRING = Date.minusMonthsString(18);

    public static String MINUS_8_DAYS_STRING = Date.minusDaysString(8);
    public static String MINUS_31_DAYS_STRING = Date.minusDaysString(31);

    public static java.util.Date minusMonths(int months) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        return cal.getTime();
    }

    public static String minusMonthsString(int months) {
        return formatter.format(LocalDateTime.now().minusMonths(months));
    }

    public static String minusDaysString(int days) {
        return formatter.format(LocalDateTime.now().minusDays(days));
    }

}
