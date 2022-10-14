package org.keycloak.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Date {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static java.util.Date MINUS_6_MONTHS = Date.minusMonths(6);

    public static java.util.Date MINUS_8_DAYS = Date.minusdays(8);
    public static java.util.Date MINUS_31_DAYS = Date.minusdays(31);
    public static java.util.Date MINUS_91_DAYS = Date.minusdays(91);

    public static String MINUS_6_MONTHS_STRING = Date.minusMonthsString(6);
    public static String MINUS_12_MONTHS_STRING = Date.minusMonthsString(12);
    public static String MINUS_18_MONTHS_STRING = Date.minusMonthsString(18);

    public static String MINUS_8_DAYS_STRING = Date.minusDaysString(8);
    public static String MINUS_31_DAYS_STRING = Date.minusDaysString(31);
    public static String MINUS_91_DAYS_STRING = Date.minusDaysString(91);

    public static java.util.Date minusdays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 24);
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
