package io.github.cruisoring.utility;

import io.github.cruisoring.repository.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeHelper {
    public static String DefaultDateFormat = "yyyy-MM-dd";
    public static String DefaultDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    static Repository<String, DateTimeFormatter> dateFormatRepository = new Repository<>(
        DateTimeFormatter::ofPattern);

    /**
     * Convert the given LocalDate instance to selected format.
     *
     * @param localDate LocalDate instance to be formatted.
     * @param formats   Optional Format String as array, use DefaultDateFormat if not provided.
     * @return Formatted String of the given LocalDate.
     */
    public static String asString(LocalDate localDate, String... formats) {
        String format = (formats == null || formats.length == 0) ? DefaultDateFormat : formats[0];
        DateTimeFormatter formatter = dateFormatRepository.get(format, null);
        return formatter == null ? null : formatter.format(localDate);
    }

    /**
     * Convert the given Date instance to selected format.
     *
     * @param date    Date instance to be formatted.
     * @param formats Optional Format String as array, use DefaultDateFormat if not provided.
     * @return Formatted String of the given Date.
     */
    public static String asString(Date date, String... formats) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return asString(localDate, formats);
    }

    /**
     * Convert the given LocalDateTime instance to selected format.
     *
     * @param localDateTime LocalDateTime instance to be formatted.
     * @param formats       Optional Format String as array, use DefaultDateFormat if not provided.
     * @return Formatted String of the given LocalDateTime.
     */
    public static String asString(LocalDateTime localDateTime, String... formats) {
        String format = (formats == null || formats.length == 0) ? DefaultDateFormat : formats[0];
        DateTimeFormatter formatter = dateFormatRepository.get(format, null);
        return formatter == null ? null : formatter.format(localDateTime);
    }
}
