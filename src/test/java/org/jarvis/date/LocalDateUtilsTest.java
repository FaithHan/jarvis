package org.jarvis.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

class LocalDateUtilsTest {

    @Test
    void toDate() {
        LocalDate today = LocalDateUtils.getToday();
        Date date = LocalDateUtils.toDate(today);
        System.out.println(LocalDateUtils.format(today,DatePattern.DATE_FORMAT));
        System.out.println(DateUtils.format(date, DatePattern.DATETIME_FORMAT));
    }

    @Test
    void toLocalDate() {
        LocalDate today = LocalDateUtils.getToday();
        LocalDateTime localDateTime = LocalDateUtils.toLocalDateTime(today);
        System.out.println(LocalDateUtils.format(today,DatePattern.DATE_FORMAT));
        System.out.println(LocalDateTimeUtils.format(localDateTime, DatePattern.DATETIME_FORMAT));
    }
}