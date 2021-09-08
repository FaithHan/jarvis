package org.jarvis.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtils {

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parseDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 比较date大小 date1.timestamp - date2.timestamp , > 1 , = 0 , < -1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compare(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException();
        }
        return date1.compareTo(date2);
    }



}
