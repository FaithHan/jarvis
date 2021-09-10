package org.jarvis.date;

import org.junit.jupiter.api.Test;

import java.util.Date;

class DateUtilsTest {

    @Test
    public void parseDate() {
        Date date = new Date();
        String dateString = DateUtils.format(date, DatePattern.DATETIME_FORMAT);
        System.out.println(dateString);
    }

    @Test
    void compare() {

    }

    @Test
    void toLocalDateTime() {
        Date date = new Date();
        String dateString = DateUtils.format(date, DatePattern.DATETIME_FORMAT);
        System.out.println(dateString);
        System.out.println(DateUtils.toLocalDateTime(date));
    }

    @Test
    void toLocalDate() {
        Date date = new Date();
        String dateString = DateUtils.format(date, DatePattern.DATETIME_FORMAT);
        System.out.println(dateString);
        System.out.println(DateUtils.toLocalDate(date));
    }
}