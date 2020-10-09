package com.alimama.moon;

import androidx.room.TypeConverter;
import java.util.Date;

public class RoomConverters {
    @TypeConverter
    public static Date fromTimestamp(Long l) {
        if (l == null) {
            return null;
        }
        return new Date(l.longValue() * 1000);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime() / 1000);
    }
}
