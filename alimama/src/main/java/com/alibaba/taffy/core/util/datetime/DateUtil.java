package com.alibaba.taffy.core.util.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static SimpleDateFormat SDF_GIFT_START_TIME = new SimpleDateFormat("MM月dd日HH:mm");
    public static SimpleDateFormat SDF_MMDD = new SimpleDateFormat("MM月dd日");
    public static SimpleDateFormat SDF_YMDHHMMSS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final SimpleDateFormat SDF_YYYY = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat SDF_YYYYMMDD1 = new SimpleDateFormat("yyyy.MM.dd");

    public enum DateFormaterType {
        SDF_YYYYMMDD,
        SDF_YYYYMMDD1
    }

    public static long converDatetime(long j) {
        return j * 1000;
    }

    public static String formatDateGiftEndTime(long j) {
        return getTwoTimeDifference(new Date(System.currentTimeMillis()), new Date(j));
    }

    public static String getTimeDifferenceToNow(long j) {
        return j == 0 ? "未知" : getTwoTimeDifference(j, System.currentTimeMillis());
    }

    public static String getTwoTimeDifference(Date date, Date date2) {
        long time = date2.getTime() - date.getTime();
        long j = time / 86400000;
        long j2 = time % 86400000;
        long j3 = j2 / 3600000;
        long j4 = j2 % 3600000;
        long j5 = j4 / 60000;
        long j6 = j4 % 60000;
        return j + "天" + j3 + "小时" + j5 + "分";
    }

    public static String getTwoTimeDifference(long j, long j2) {
        long time = (new Date(j2).getTime() - new Date(j).getTime()) / 1000;
        int ceil = (int) Math.ceil((double) (time / 60));
        if (ceil <= 1) {
            return "刚刚";
        }
        int ceil2 = (int) Math.ceil((double) (time / 3600));
        if (ceil2 < 1) {
            return ceil + "分钟前";
        } else if (ceil2 >= 24) {
            int ceil3 = (int) Math.ceil((double) (time / 86400));
            if (ceil3 >= 1 && ceil3 < 2) {
                return "昨天";
            }
            if (ceil3 >= 2 && ceil3 < 3) {
                return "前天";
            }
            if (SDF_YYYY.format(new Date(System.currentTimeMillis())).equals(SDF_YYYY.format(new Date(j)))) {
                return SDF_MMDD.format(new Date(j));
            }
            return SDF_YYYYMMDD.format(new Date(j));
        } else {
            return ceil2 + "小时前";
        }
    }

    public static String getDateFormatStr(long j) {
        return SDF_YYYYMMDD.format(new Date(j));
    }

    public static String getDateFormatStr(long j, DateFormaterType dateFormaterType) {
        if (dateFormaterType == DateFormaterType.SDF_YYYYMMDD) {
            return SDF_YYYYMMDD.format(new Date(j));
        }
        if (dateFormaterType == DateFormaterType.SDF_YYYYMMDD1) {
            return SDF_YYYYMMDD1.format(new Date(j));
        }
        return j + "";
    }

    public static String getDataTimeFormatStr(long j) {
        return SDF_YMDHHMMSS.format(new Date(j));
    }

    public static String getDateFormatStr(long j, String str) {
        return new SimpleDateFormat(str).format(new Date(j));
    }

    public static String getDateFormatStr(long j, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date(j));
    }

    public static long getLongTimeByDateFormaterStr(String str) {
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
            if (parse != null) {
                return parse.getTime();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getBetweenTime(long j, long j2) {
        return (int) ((j2 - j) / 1000);
    }

    public static boolean compareDate(long j, long j2) {
        if (j <= 0) {
            return false;
        }
        try {
            String dateFormatStr = getDateFormatStr(j, "MM-dd HH:mm");
            String dateFormatStr2 = getDateFormatStr(j2, "MM-dd HH:mm");
            Calendar instance = Calendar.getInstance();
            Calendar instance2 = Calendar.getInstance();
            instance.setTime(new SimpleDateFormat("MM-dd HH:mm").parse(dateFormatStr));
            instance2.setTime(new SimpleDateFormat("MM-dd HH:mm").parse(dateFormatStr2));
            if (instance.compareTo(instance2) >= 0) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long getTimesTodaymorning() {
        Calendar instance = Calendar.getInstance();
        instance.set(11, 24);
        instance.set(13, 0);
        instance.set(12, 0);
        instance.set(14, 0);
        return instance.getTimeInMillis();
    }
}
