package tool.util;

import org.apache.commons.validator.GenericValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * @className: DatetimeUtil
 * @author: Lying
 * @description: TODO
 * @date: 2022/12/7 下午3:52
 */
@SuppressWarnings("unused")
public class DatetimeUtil {
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String THCL_DATETIME_MIL_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String THCL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String THCL_DAY_FORMAT = "yyyy-MM-dd";

    public static final DateTimeFormatter DATETIME_MIL_FORMATTER = DateTimeFormatter.ofPattern(THCL_DATETIME_MIL_FORMAT);
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * validate is or not Date String
     * @param date
     * @param format
     * @return
     */
    public static boolean isDate(String date,
                                 String... format) {
        final String date_format =
                format.length >= 1 ? format[0] : THCL_DATE_FORMAT;

        return GenericValidator.isDate(date, date_format, true);
    }

    /**
     * 将日期转换为ISO8601格式的字符串
     * @param date
     * @return
     */
    public static String dateToIso8601String(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String date, String... format) throws ParseException {
        final String dateFormat = format.length >= 1 ? format[0] : THCL_DATE_FORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        return formatter.parse(date);
    }

    public static LocalDateTime stringToLocalDateTime(String dateStr) {
        return LocalDateTime.parse(dateStr, DATETIME_FORMATTER);
    }

    public static LocalDate dateStringToLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static String dateToString(Date date, String... format) {
        final String dateFormat = format.length >= 1 ? format[0] : THCL_DATE_FORMAT;

        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static Long getNowTimeStamp() {
        return System.currentTimeMillis();
    }

    public static Long getTenUnitTimestamp(){
        return Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10));
    }

    public static String timestampToString(Long timestamp, String... format) {
        final String dateFormat = format.length >= 1 ? format[0] : THCL_DATE_FORMAT;

        return new SimpleDateFormat(dateFormat).format(new Date(timestamp));
    }

    public static String getSimpleDateTime() {
        Instant now = Instant.now();
        DateTimeFormatter formatter = DATETIME_FORMATTER
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        return formatter.format(now);
    }

    public static String getPreSimpleDateTime(Integer pre){

        LocalDate today = LocalDate.now();
        LocalDate preDate = today.minusDays(pre);

        DateTimeFormatter formatter = DATETIME_FORMATTER
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        return formatter.format(preDate);
    }

    public static String getPreSimpleDateTimeByDay(String startDateStr, Integer pre){

        LocalDateTime startDate  = stringToLocalDateTime(startDateStr);
        LocalDateTime preDate = startDate.minusDays(pre);

        DateTimeFormatter formatter = DATETIME_FORMATTER
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        return formatter.format(preDate);

    }
}
