package com.example.myapplication.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter shortTimeFormatter;
    private static final DateTimeFormatter shortDateFormatter;

    static {
        shortTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        shortDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public static String formatToTime(LocalDateTime localDateTime) {
        return shortTimeFormatter.format(localDateTime);
    }

    public static String formatToDate(LocalDateTime localDateTime) {
        return shortDateFormatter.format(localDateTime);
    }
}
