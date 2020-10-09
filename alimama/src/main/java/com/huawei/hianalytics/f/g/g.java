package com.huawei.hianalytics.f.g;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.d;
import com.taobao.orange.OConstant;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class g {
    public static String a(Context context) {
        return (String) b(c(context, "global_v2"), OConstant.CANDIDATE_APPVER, "");
    }

    public static String a(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences == null) {
            return null;
        }
        String str2 = (String) b(sharedPreferences, str, "");
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(str);
        edit.apply();
        return str2;
    }

    public static Set<String> a(SharedPreferences sharedPreferences) {
        return sharedPreferences.getAll().keySet();
    }

    public static void a(Context context, String str) {
        a(c(context, "global_v2"), OConstant.CANDIDATE_APPVER, (Object) str);
    }

    public static void a(Context context, String str, String str2) {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, str + d.b(context, str2) + ".xml");
        if (file.exists() && file.delete()) {
            b.c("SharedPreferenceUtil", "delete sp file");
        }
    }

    public static void a(Context context, String str, String str2, String str3) {
        SharedPreferences.Editor edit = c(context, str2).edit();
        edit.putString(str3, str);
        edit.commit();
    }

    public static void a(SharedPreferences sharedPreferences, String str, Object obj) {
        d.a(sharedPreferences, str, obj);
    }

    private static void a(SharedPreferences sharedPreferences, Set<String> set, Map<String, String> map) {
        for (String next : set) {
            map.put(next, (String) b(sharedPreferences, next, ""));
        }
    }

    public static void a(String str, Context context) {
        if (TextUtils.isEmpty(str)) {
            b.c("SharedPreferenceUtil", "clearTypeDataByTag() eventTag is null or empty!");
            return;
        }
        if (!"_default_config_tag".equals(str)) {
            String str2 = str + "-" + "oper";
            String str3 = str + "-" + "maint";
            str = str + "-" + "diffprivacy";
            a(str2, false, context);
            a(str3, false, context);
        }
        a(str, false, context);
    }

    @SuppressLint({"ApplySharedPref"})
    public static synchronized void a(String str, boolean z, Context context) {
        synchronized (g.class) {
            b.b("SharedPreferenceUtil", "clear data file : eventTag : " + str);
            SharedPreferences c = c(context, "stat_v2_1");
            if (c != null) {
                SharedPreferences.Editor edit = c.edit();
                if (z) {
                    edit.clear();
                } else {
                    edit.remove(str);
                }
                edit.commit();
            }
            SharedPreferences c2 = c(context, "cached_v2_1");
            if (c2 != null) {
                SharedPreferences.Editor edit2 = c2.edit();
                if (z) {
                    edit2.clear();
                } else {
                    edit2.remove(str);
                }
                edit2.commit();
            }
        }
    }

    public static void a(Set<String> set, Set<String> set2, Context context) {
        for (String next : set2) {
            if (!set.contains(next)) {
                a(next, false, context);
            }
        }
    }

    public static boolean a(Context context, String str, int i) {
        long length = b(context, str).length();
        if (length <= ((long) i)) {
            return false;
        }
        b.b("HiAnalytics/event", "reach local file limited size - file len: %d limitedSize: %d", Long.valueOf(length), Integer.valueOf(i));
        return true;
    }

    public static boolean a(SharedPreferences sharedPreferences, int i, String str) {
        return ((String) b(sharedPreferences, str, "")).length() > i;
    }

    public static File b(Context context, String str) {
        String str2 = "hianalytics_" + str + "_" + context.getPackageName() + ".xml";
        return new File(context.getFilesDir(), "../shared_prefs/" + str2);
    }

    public static Object b(SharedPreferences sharedPreferences, String str, Object obj) {
        return d.b(sharedPreferences, str, obj);
    }

    public static Map<String, String> b(SharedPreferences sharedPreferences) {
        Set<String> a = a(sharedPreferences);
        HashMap hashMap = new HashMap(a.size());
        a(sharedPreferences, a, (Map<String, String>) hashMap);
        return hashMap;
    }

    public static boolean b(Context context) {
        long longValue = ((Long) b(c(context, "analytics_key"), "flashKeyTime", -1L)).longValue();
        if (longValue == -1) {
            longValue = ((Long) b(c(context, "Privacy_MY"), "flashKeyTime", -1L)).longValue();
        }
        return System.currentTimeMillis() - longValue > 43200000;
    }

    public static SharedPreferences c(Context context, String str) {
        return d.a(context, str);
    }

    public static void d(Context context, String str) {
        SharedPreferences c = c(context, "backup_event");
        if (c != null && c.contains(str)) {
            b.b("SharedPreferenceUtil", "begin clear backup data! spKey:" + str);
            SharedPreferences.Editor edit = c.edit();
            edit.remove(str);
            edit.commit();
        }
    }
}
