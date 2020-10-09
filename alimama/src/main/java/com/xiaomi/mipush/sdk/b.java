package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import com.xiaomi.push.ai;
import com.xiaomi.push.hl;
import com.xiaomi.push.service.ag;
import java.util.ArrayList;

public class b {
    public static StackTraceElement[] a;

    public static void a() {
        try {
            a = Thread.getAllStackTraces().get(Thread.currentThread());
        } catch (Throwable unused) {
        }
    }

    public static void a(Context context) {
        ai.a(context).a((Runnable) new c(context), 20);
    }

    /* access modifiers changed from: private */
    public static String b(int i) {
        if (a == null || a.length <= 4) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        int i2 = 4;
        while (i2 < a.length && i2 < i + 4) {
            try {
                arrayList.add(a[i2].toString());
                i2++;
            } catch (Exception unused) {
                return "";
            }
        }
        return arrayList.toString();
    }

    private static boolean b(Context context) {
        ag a2 = ag.a(context);
        if (!a2.a(hl.AggregationSdkMonitorSwitch.a(), false)) {
            return false;
        }
        return Math.abs(System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0).getLong("last_upload_call_stack_timestamp", 0)) >= ((long) Math.max(60, a2.a(hl.AggregationSdkMonitorFrequency.a(), 86400)));
    }

    private static void c(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.putLong("last_upload_call_stack_timestamp", System.currentTimeMillis());
        edit.commit();
    }
}
