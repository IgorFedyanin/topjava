package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        if (Objects.isNull(startTime) && Objects.isNull(endTime)) return true;
        else if (Objects.isNull(startTime)) return lt.compareTo(endTime) < 0;
        else if (Objects.isNull(endTime)) return lt.compareTo(startTime) >= 0;
        else return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isBetweenDate(LocalDate date, LocalDate startDate, LocalDate endDate){
        if (Objects.isNull(startDate) && Objects.isNull(endDate)) return true;
        else if (Objects.isNull(startDate)) return date.compareTo(endDate) <= 0;
        else if (Objects.isNull(endDate)) return date.compareTo(startDate) >= 0;
        else return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

