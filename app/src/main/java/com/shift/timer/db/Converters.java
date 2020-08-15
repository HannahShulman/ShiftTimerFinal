package com.shift.timer.db;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by roy on 6/13/2017.
 */

public class Converters {

    @TypeConverter
    public static Date fromTimeStampToDate(Long value) {
        if (value == null) return null;
        return new Date(value);
    }

    @TypeConverter
    public static Long toDateTimestamp(Date value) {
        if (value == null) return null;
        return value.getTime();
    }
}