package com.huawei.hianalytics.log.e;

import android.content.Context;
import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.b.a;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class a {
    public static synchronized void a(Context context) {
        synchronized (a.class) {
            if (context != null) {
                b.b("HiAnalytics/logServer", "Clear all data of local log");
                String path = context.getFilesDir().getPath();
                b(new File(path + a.C0006a.a));
            }
        }
    }

    public static void a(File file) {
        com.huawei.hianalytics.util.b.a(file);
    }

    public static void a(String str) {
        String str2;
        String str3;
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                String name = file.getName();
                if (name.endsWith(".log") || name.endsWith(".zip")) {
                    if (!"eventinfo.log".equals(name)) {
                        String[] split = name.substring(0, name.lastIndexOf(".")).split("_");
                        if (System.currentTimeMillis() - b(split[split.length - 1].trim()) >= DateUtils.WEEK && file.delete()) {
                            str2 = "HiAnalytics/logServer";
                            str3 = "out time file del ok";
                        }
                    }
                } else if (file.delete()) {
                    str2 = "HiAnalytics/logServer";
                    str3 = "this file is not our fileName :" + name;
                }
                b.b(str2, str3);
            }
        }
    }

    private static long b(String str) {
        try {
            Date parse = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).parse(str);
            if (parse == null) {
                return 0;
            }
            return parse.getTime();
        } catch (ParseException unused) {
            b.c("HiAnalytics/logServer", "Time conversion Exception : getTimeMillis!");
            return 0;
        }
    }

    public static void b(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                if (file.delete()) {
                    return;
                }
            } else if (file.isDirectory()) {
                String[] list = file.list();
                if (list != null) {
                    for (String file2 : list) {
                        b(new File(file, file2));
                    }
                    if (file.delete()) {
                        return;
                    }
                } else if (file.delete()) {
                    return;
                }
            } else {
                return;
            }
            b.c("HiAnalytics/logServer", "refresh clear file fail");
        }
    }
}
