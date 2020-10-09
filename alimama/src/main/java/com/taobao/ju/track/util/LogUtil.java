package com.taobao.ju.track.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {
    private static final String DEFAULT_PREFIX = "LogUtil";
    private static final String LAST_TIME = "last_time";
    private static final String PRINT_INTERVAL = "print_interval";
    private static final String TIMES = "times";
    private static final String TOTAL_TIME = "total_time";
    private static final boolean isDebug = true;
    private static Map<String, Map> mAvgTime = new HashMap();
    private static long time;

    public static void avgTimeStart(String str, long j, Object... objArr) {
        Map map = mAvgTime.get(str);
        if (map == null) {
            map = new HashMap();
            map.put(TOTAL_TIME, 0L);
            map.put(PRINT_INTERVAL, Long.valueOf(j));
            map.put(TIMES, 0L);
            mAvgTime.put(str, map);
        }
        map.put(LAST_TIME, Long.valueOf(System.currentTimeMillis()));
    }

    public static void avgTimeEnd(String str, Object... objArr) {
        Map map = mAvgTime.get(str);
        if (map != null) {
            long longValue = (Long.valueOf(String.valueOf(map.get(TOTAL_TIME))).longValue() + System.currentTimeMillis()) - Long.valueOf(String.valueOf(map.get(LAST_TIME))).longValue();
            map.put(TOTAL_TIME, Long.valueOf(longValue));
            long longValue2 = Long.valueOf(String.valueOf(map.get(PRINT_INTERVAL))).longValue();
            long longValue3 = Long.valueOf(String.valueOf(map.get(TIMES))).longValue() + 1;
            if (longValue3 >= longValue2) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" end ");
                double d = (double) longValue;
                double d2 = (double) longValue2;
                Double.isNaN(d);
                Double.isNaN(d2);
                sb.append(d / d2);
                sb.append(Operators.SPACE_STR);
                sb.append(getMsg(objArr));
                Log.d(DEFAULT_PREFIX, sb.toString());
                mAvgTime.put(str, (Object) null);
                return;
            }
            map.put(TIMES, Long.valueOf(longValue3));
        }
    }

    public static void timeStart(String str, Object... objArr) {
        time = System.currentTimeMillis();
        Log.d(DEFAULT_PREFIX, str + " start " + getMsg(objArr));
    }

    public static void timeEnd(String str, Object... objArr) {
        Log.d(DEFAULT_PREFIX, str + " end " + (System.currentTimeMillis() - time) + Operators.SPACE_STR + getMsg(objArr));
    }

    public static void d(String str, Object... objArr) {
        Log.d(DEFAULT_PREFIX, str + "  " + getMsg(objArr));
    }

    public static void fd(String str, String str2, Object... objArr) {
        toFile(str, str2, objArr);
    }

    public static void toast(Context context, Object... objArr) {
        Toast.makeText(context, getMsg(new Object[0]), 0).show();
    }

    private static String getMsg(Object... objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (objArr != null) {
            for (Object append : objArr) {
                stringBuffer.append(append);
                stringBuffer.append(Operators.SPACE_STR);
            }
        }
        return stringBuffer.toString();
    }

    private static void toFile(String str, String str2, Object... objArr) {
        File file;
        try {
            file = createFile(str, true);
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }
        if (file != null) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.append(str2 + "  " + getMsg(objArr));
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static File createFile(String str, boolean z) throws IOException {
        File file = !TextUtils.isEmpty(str) ? new File(str) : null;
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                if (z) {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    try {
                        file.createNewFile();
                    } catch (IOException e2) {
                        throw e2;
                    }
                } else {
                    throw e;
                }
            }
        }
        return file;
    }
}
