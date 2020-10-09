package com.alibaba.aliweex.adapter.module.calendar;

import com.alibaba.aliweex.adapter.module.WXCalendarModule;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DateUtils {
    private static final DateFormat[] CUSTOM_DATE_FORMATS;
    public static final long DAY = 86400000;
    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");
    public static final long WEEK = 604800000;

    static {
        String[] strArr = {"EEE, dd MMM yy HH:mm:ss z", "EEE, dd MMM yyyy HH:mm:ss z", "EEE, dd MMM yy HH:mm:ss", "EEE, MMM dd yy HH:mm:ss", "EEE, dd MMM yy HH:mm z", "EEE dd MMM yyyy HH:mm:ss", "dd MMM yy HH:mm:ss z", "dd MMM yy HH:mm z", "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:sszzzz", "yyyy-MM-dd'T'HH:mm:ss z", "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss.SSSz", "yyyy-MM-dd'T'HHmmss.SSSz", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mmZ", "yyyy-MM-dd'T'HH:mm'Z'", "dd MMM yyyy HH:mm:ss z", "dd MMM yyyy HH:mm z", "yyyy-MM-dd", "MMM dd, yyyy"};
        CUSTOM_DATE_FORMATS = new SimpleDateFormat[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            CUSTOM_DATE_FORMATS[i] = new SimpleDateFormat(strArr[i], Locale.ENGLISH);
            CUSTOM_DATE_FORMATS[i].setTimeZone(TIMEZONE);
        }
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static Date parseDate(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        String trim = str.trim();
        if (trim.length() == 14 && isNumeric(trim)) {
            try {
                return new SimpleDateFormat("yyyyMMddHHmmss").parse(trim);
            } catch (ParseException e) {
                WXLogUtils.w(WXCalendarModule.TAG, (Throwable) e);
            }
        } else if (trim.length() == 19 && trim.indexOf(Operators.SPACE_STR) == 10) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(trim);
            } catch (ParseException e2) {
                WXLogUtils.w(WXCalendarModule.TAG, (Throwable) e2);
            }
        }
        int i = 0;
        if (trim.length() > 10) {
            if ((trim.substring(trim.length() - 5).indexOf("+") == 0 || trim.substring(trim.length() - 5).indexOf("-") == 0) && trim.substring(trim.length() - 5).indexOf(":") == 2) {
                trim = trim.substring(0, trim.length() - 5) + trim.substring(trim.length() - 5, trim.length() - 4) + "0" + trim.substring(trim.length() - 4);
            }
            String substring = trim.substring(trim.length() - 6);
            if ((substring.indexOf("-") == 0 || substring.indexOf("+") == 0) && substring.indexOf(":") == 3 && !"GMT".equals(trim.substring(trim.length() - 9, trim.length() - 6))) {
                trim = trim.substring(0, trim.length() - 6) + (substring.substring(0, 3) + substring.substring(4));
            }
        }
        synchronized (DateUtils.class) {
            while (i < CUSTOM_DATE_FORMATS.length) {
                try {
                    Date parse = CUSTOM_DATE_FORMATS[i].parse(trim);
                    return parse;
                } catch (ParseException unused) {
                    i++;
                } catch (NumberFormatException unused2) {
                    i++;
                }
            }
            return null;
        }
    }
}
