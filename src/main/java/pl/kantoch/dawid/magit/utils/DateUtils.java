package pl.kantoch.dawid.magit.utils;

import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils
{
    public static Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }
}
