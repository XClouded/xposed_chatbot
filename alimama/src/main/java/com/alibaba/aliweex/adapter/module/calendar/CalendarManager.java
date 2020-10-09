package com.alibaba.aliweex.adapter.module.calendar;

import alimama.com.unweventparse.constants.EventConstants;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coloros.mcssdk.mode.Message;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CalendarManager {
    public static boolean addEvent(Context context, String str, String str2, Calendar calendar, Calendar calendar2, int i) {
        if (context == null || calendar == null || calendar2 == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return add14Event(context, str, str2, calendar, calendar2, i);
        }
        return add8Event(context, str, str2, calendar, calendar2, i);
    }

    public static boolean delEvent(Context context, String str, Calendar calendar, Calendar calendar2) {
        long j;
        if (context == null || calendar == null || calendar2 == null) {
            return false;
        }
        ContentResolver contentResolver = context.getContentResolver();
        Uri parse = Uri.parse("content://com.android.calendar/events");
        Cursor query = contentResolver.query(parse, new String[]{"_id", "title", Message.DESCRIPTION, "dtstart", "dtend"}, (String) null, (String[]) null, (String) null);
        while (true) {
            if (!query.moveToNext()) {
                j = 0;
                break;
            }
            Long valueOf = Long.valueOf(query.getLong(0));
            String string = query.getString(1);
            long j2 = query.getLong(3);
            long j3 = query.getLong(4);
            if (!TextUtils.isEmpty(string)) {
                if (string.equals(str) && calendar.getTimeInMillis() >= j2 && calendar2.getTimeInMillis() <= j3) {
                    j = valueOf.longValue();
                    break;
                }
            } else {
                String str2 = str;
            }
        }
        if (j == 0) {
            return false;
        }
        try {
            contentResolver.delete(parse, "_id= ?", new String[]{j + ""});
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean addEvents(Context context, List<CalendarEvent> list) {
        if (context == null || list == null || list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            CalendarEvent calendarEvent = list.get(i);
            if (!add14Event(context, calendarEvent.eventName, calendarEvent.eventDescription, calendarEvent.eventBeginDate, calendarEvent.eventEndDate, calendarEvent.reminderMinutus)) {
                for (int i2 = 0; i2 <= i; i2++) {
                    CalendarEvent calendarEvent2 = list.get(i2);
                    delEvent(context, calendarEvent2.eventName, calendarEvent2.eventBeginDate, calendarEvent2.eventEndDate);
                }
                return false;
            }
        }
        return true;
    }

    public static boolean delEvents(Context context, List<CalendarEvent> list) {
        if (context == null || list == null || list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            CalendarEvent calendarEvent = list.get(i);
            if (!delEvent(context, calendarEvent.eventName, calendarEvent.eventBeginDate, calendarEvent.eventEndDate)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkEvent(Context context, String str, String str2, Calendar calendar, Calendar calendar2) {
        long timeInMillis = calendar.getTimeInMillis();
        long timeInMillis2 = calendar2.getTimeInMillis();
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.android.calendar/events"), new String[]{"_id"}, "title= ? AND dtstart= ? AND dtend= ?", new String[]{str, timeInMillis + "", timeInMillis2 + ""}, (String) null);
        if (query == null || !query.moveToNext()) {
            return false;
        }
        return true;
    }

    public static String checkEvents(Context context, List<CalendarEvent> list) {
        if (context == null || list == null || list.size() <= 0) {
            return "";
        }
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            CalendarEvent calendarEvent = list.get(i);
            boolean checkEvent = checkEvent(context, calendarEvent.eventName, calendarEvent.eventDescription, calendarEvent.eventBeginDate, calendarEvent.eventEndDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = simpleDateFormat.format(calendarEvent.eventBeginDate.getTime());
            String format2 = simpleDateFormat.format(calendarEvent.eventEndDate.getTime());
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("calendar_title", (Object) calendarEvent.eventName);
            jSONObject.put("success", (Object) Boolean.valueOf(checkEvent));
            jSONObject.put("calendar_start_datetime", (Object) format);
            jSONObject.put("calendar_end_datetime", (Object) format2);
            jSONArray.add(jSONObject);
        }
        return jSONArray.toJSONString();
    }

    private static boolean add8Event(Context context, String str, String str2, Calendar calendar, Calendar calendar2, int i) {
        try {
            long timeInMillis = calendar.getTimeInMillis();
            long timeInMillis2 = calendar2.getTimeInMillis();
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("calendar_id", 1L);
            contentValues.put("title", str);
            contentValues.put(Message.DESCRIPTION, str2);
            contentValues.put("dtstart", Long.valueOf(timeInMillis));
            contentValues.put("dtend", Long.valueOf(timeInMillis2));
            contentValues.put("eventStatus", 1);
            contentValues.put("hasAlarm", 1);
            contentValues.put("eventTimezone", "GMT+8");
            Uri parse = Uri.parse("content://com.android.calendar/events");
            Long valueOf = Long.valueOf(Long.parseLong(contentResolver.insert(parse, contentValues).getLastPathSegment()));
            String[] strArr = {"_id", "title", Message.DESCRIPTION, "dtstart", "dtend"};
            if (!contentResolver.query(parse, strArr, "_id=" + valueOf, (String[]) null, (String) null).moveToNext()) {
                return false;
            }
            ContentResolver contentResolver2 = context.getContentResolver();
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("minutes", Integer.valueOf(i));
            contentValues2.put(EventConstants.EVENT_ID, valueOf);
            contentValues2.put("method", 1);
            contentResolver2.insert(Uri.parse("content://com.android.calendar/reminders"), contentValues2);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    @TargetApi(14)
    private static boolean add14Event(Context context, String str, String str2, Calendar calendar, Calendar calendar2, int i) {
        try {
            if (checkEvent(context, str, str2, calendar, calendar2)) {
                return true;
            }
            long timeInMillis = calendar.getTimeInMillis();
            long timeInMillis2 = calendar2.getTimeInMillis();
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("dtstart", Long.valueOf(timeInMillis));
            contentValues.put("dtend", Long.valueOf(timeInMillis2));
            String str3 = str;
            contentValues.put("title", str);
            String str4 = str2;
            contentValues.put(Message.DESCRIPTION, str2);
            contentValues.put("calendar_id", 1L);
            contentValues.put("eventTimezone", "GMT+8");
            Long valueOf = Long.valueOf(Long.parseLong(contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues).getLastPathSegment()));
            Uri uri = CalendarContract.Events.CONTENT_URI;
            String[] strArr = {"_id", "title", Message.DESCRIPTION, "dtstart", "dtend"};
            if (!contentResolver.query(uri, strArr, "_id=" + valueOf, (String[]) null, (String) null).moveToNext()) {
                return false;
            }
            ContentResolver contentResolver2 = context.getContentResolver();
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("minutes", Integer.valueOf(i));
            contentValues2.put(EventConstants.EVENT_ID, valueOf);
            contentValues2.put("method", 1);
            contentResolver2.insert(CalendarContract.Reminders.CONTENT_URI, contentValues2);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
