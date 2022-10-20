package org.keycloak.dashboard.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DateUtil {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static java.util.Date MINUS_6_MONTHS = DateUtil.minusMonths(6);
    public static java.util.Date MINUS_12_MONTHS = DateUtil.minusMonths(12);
    public static java.util.Date MINUS_18_MONTHS = DateUtil.minusMonths(18);

    public static java.util.Date MINUS_7_DAYS = DateUtil.minusdays(7);
    public static java.util.Date MINUS_30_DAYS = DateUtil.minusdays(30);
    public static java.util.Date MINUS_90_DAYS = DateUtil.minusdays(90);

    public static String MINUS_6_MONTHS_STRING = DateUtil.minusMonthsString(6);
    public static String MINUS_12_MONTHS_STRING = DateUtil.minusMonthsString(12);
    public static String MINUS_18_MONTHS_STRING = DateUtil.minusMonthsString(18);

    public static String MINUS_7_DAYS_STRING = DateUtil.minusDaysString(7);
    public static String MINUS_30_DAYS_STRING = DateUtil.minusDaysString(30);
    public static String MINUS_90_DAYS_STRING = DateUtil.minusDaysString(90);

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

    public static String toString(java.util.Date date) {
        return dateFormat.format(date);
    }

}
