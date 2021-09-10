package org.jarvis.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * LocalDateTime工具类
 *
 * 1. 获取今天 昨天 明天
 * 2. 获取本周第一天，最后一天或者指定星期几的某一天
 * 3. 获取本月第一天，最后一天
 * 4. 获取本年第一天，最后一天
 * 5. parse, format
 * 6. 转Date类
 */

public abstract class LocalDateTimeUtils {


    public static LocalDateTime getToday() {
        return LocalDateTime.now();
    }

    public static LocalDateTime getYesterday() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minus(1L, ChronoUnit.DAYS);
    }

    public static LocalDateTime getTomorrow() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plus(1L, ChronoUnit.DAYS);
    }

    public static LocalDateTime getFirstDayOfWeek() {
        return LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDateTime getLastDayOfWeek() {
        return LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
    }

    public static LocalDateTime getDayOfWeek(DayOfWeek dayOfWeek) {
        return LocalDateTime.now().with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    public static LocalDateTime getFirstDayOfMonth() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime getLastDayOfMonth() {
        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime getFirstDayOfYear() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDateTime getLastDayOfYear() {
        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime beginOfTheDay(LocalDateTime localDateTime) {
        return localDateTime.with(LocalTime.MIN);
    }

    public static LocalDateTime endOfTheDay(LocalDateTime localDateTime) {
        return localDateTime.with(LocalTime.MAX);
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parse(String localDateTime, String pattern) {
        return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static int compare(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return localDateTime1.compareTo(localDateTime2);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }


}
