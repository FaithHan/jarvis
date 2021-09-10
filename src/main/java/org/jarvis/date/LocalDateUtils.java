package org.jarvis.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * LocalDate工具类
 *
 * 1. 获取今天 昨天 明天
 * 2. 获取本周第一天，最后一天或者指定星期几的某一天
 * 3. 获取本月第一天，最后一天
 * 4. 获取本年第一天，最后一天
 * 5. parse, format
 * 6. 转Date类
 */

public abstract class LocalDateUtils {


    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static LocalDate getYesterday() {
        LocalDate localDate = LocalDate.now();
        return localDate.minus(1L, ChronoUnit.DAYS);
    }

    public static LocalDate getTomorrow() {
        LocalDate localDate = LocalDate.now();
        return localDate.plus(1L, ChronoUnit.DAYS);
    }

    public static LocalDate getFirstDayOfWeek() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDate getLastDayOfWeek() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
    }

    public static LocalDate getDayOfWeek(DayOfWeek dayOfWeek) {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    public static LocalDate getFirstDayOfMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate getLastDayOfMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate getFirstDayOfYear() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate getLastDayOfYear() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static String format(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parse(String localDate, String pattern) {
        return LocalDate.parse(localDate, DateTimeFormatter.ofPattern(pattern));
    }

    public static int compare(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.compareTo(localDate2);
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

}
