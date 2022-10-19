package org.keycloak.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Date {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static java.util.Date MINUS_6_MONTHS = Date.minusMonths(6);

    public static java.util.Date MINUS_7_DAYS = Date.minusdays(7);
    public static java.util.Date MINUS_30_DAYS = Date.minusdays(30);
    public static java.util.Date MINUS_90_DAYS = Date.minusdays(90);

    public static String MINUS_6_MONTHS_STRING = Date.minusMonthsString(6);
    public static String MINUS_12_MONTHS_STRING = Date.minusMonthsString(12);
    public static String MINUS_18_MONTHS_STRING = Date.minusMonthsString(18);

    public static String MINUS_7_DAYS_STRING = Date.minusDaysString(7);
    public static String MINUS_30_DAYS_STRING = Date.minusDaysString(30);
    public static String MINUS_90_DAYS_STRING = Date.minusDaysString(90);

    public static java.util.Date minusdays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return cal.getTime();
    }
    public static java.util.Date minusMonths(int months) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -months);
        return cal.getTime();
    }

    public static String minusMonthsString(int months) {
        return formatter.format(LocalDateTime.now().minusMonths(months));
    }

    public static String minusDaysString(int days) {
        return formatter.format(LocalDateTime.now().minusDays(days));
    }

}
