package io.github.kennethfan.openapi.gateway.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    public static long datetimeStrToTimestamp(String datetimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(datetimeStr, formatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String timestampToDatetimeStr(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.ofInstant(instant, zone).format(formatter);
    }
}
