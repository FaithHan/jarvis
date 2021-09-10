package org.jarvis.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

class LocalDateTimeUtilsTest {

    @Test
    void toDate() {
        LocalDateTime today = LocalDateTimeUtils.getToday();
        Date date = LocalDateTimeUtils.toDate(today);
        System.out.println(LocalDateTimeUtils.format(today,DatePattern.DATETIME_FORMAT));
        System.out.println(DateUtils.format(date, DatePattern.DATETIME_FORMAT));
    }

    @Test
    void toLocalDate() {
        LocalDateTime today = LocalDateTimeUtils.getToday();
        LocalDate localDate = LocalDateTimeUtils.toLocalDate(today);
        System.out.println(LocalDateTimeUtils.format(today,DatePattern.DATETIME_FORMAT));
        System.out.println(LocalDateUtils.format(localDate, DatePattern.DATE_FORMAT));
    }
}