package com.alimama.unionwl.uiframe.views.market;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static Date tsToDateLong(long j) {
        return new Date(j);
    }

    public static Date tsToDate(long j, String str) {
        try {
            return new SimpleDateFormat(str, Locale.US).parse(tsToDateStr(j, str));
        } catch (ParseException unused) {
            return null;
        }
    }

    public static String tsToDateStr(long j, String str) {
        return new SimpleDateFormat(str, Locale.US).format(new Date(j));
    }
}
