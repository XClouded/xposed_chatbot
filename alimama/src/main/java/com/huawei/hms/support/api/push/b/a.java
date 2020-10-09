package com.huawei.hms.support.api.push.b;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.taobao.ju.track.constants.Constants;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/* compiled from: CommFun */
public abstract class a {
    private static final Object a = new Object();
    private static int b = -1;

    private static boolean d(Context context) {
        com.huawei.hms.support.log.a.a("CommFun", "existFrameworkPush:" + b);
        try {
            File file = new File("/system/framework/" + "hwpush.jar");
            if (a()) {
                com.huawei.hms.support.log.a.a("CommFun", "push jarFile is exist");
            } else if (!file.isFile()) {
                return false;
            } else {
                com.huawei.hms.support.log.a.a("CommFun", "push jarFile is exist");
            }
            List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(new Intent().setClassName("android", "com.huawei.android.pushagentproxy.PushService"), 128);
            if (queryIntentServices != null) {
                if (!queryIntentServices.isEmpty()) {
                    com.huawei.hms.support.log.a.b("CommFun", "framework push exist, use framework push first");
                    return true;
                }
            }
            com.huawei.hms.support.log.a.b("CommFun", "framework push not exist, need vote apk or sdk to support pushservice");
            return false;
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("CommFun", "get Apk version faild ,Exception e= " + e.toString());
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        if (1 != b) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.content.Context r5) {
        /*
            java.lang.String r0 = "CommFun"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "existFrameworkPush:"
            r1.append(r2)
            int r2 = b
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.huawei.hms.support.log.a.a(r0, r1)
            java.lang.Object r0 = a
            monitor-enter(r0)
            r1 = -1
            int r2 = b     // Catch:{ all -> 0x003b }
            r3 = 0
            r4 = 1
            if (r1 == r2) goto L_0x0029
            int r5 = b     // Catch:{ all -> 0x003b }
            if (r4 != r5) goto L_0x0027
            r3 = 1
        L_0x0027:
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            return r3
        L_0x0029:
            boolean r5 = d(r5)     // Catch:{ all -> 0x003b }
            if (r5 == 0) goto L_0x0032
            b = r4     // Catch:{ all -> 0x003b }
            goto L_0x0034
        L_0x0032:
            b = r3     // Catch:{ all -> 0x003b }
        L_0x0034:
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            int r5 = b
            if (r4 != r5) goto L_0x003a
            r3 = 1
        L_0x003a:
            return r3
        L_0x003b:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hms.support.api.push.b.a.a(android.content.Context):boolean");
    }

    public static boolean a() {
        try {
            Class<?> cls = Class.forName("huawei.cust.HwCfgFilePolicy");
            int intValue = ((Integer) cls.getDeclaredField("CUST_TYPE_CONFIG").get(cls)).intValue();
            File file = (File) cls.getDeclaredMethod("getCfgFile", new Class[]{String.class, Integer.TYPE}).invoke(cls, new Object[]{"jars/hwpush.jar", Integer.valueOf(intValue)});
            if (file != null && file.exists()) {
                com.huawei.hms.support.log.a.a("CommFun", "get push cust File path is " + file.getCanonicalPath());
                return true;
            }
        } catch (ClassNotFoundException unused) {
            com.huawei.hms.support.log.a.d("CommFun", "HwCfgFilePolicy ClassNotFoundException");
        } catch (SecurityException unused2) {
            com.huawei.hms.support.log.a.d("CommFun", "check cust exist push SecurityException.");
        } catch (NoSuchFieldException unused3) {
            com.huawei.hms.support.log.a.d("CommFun", "check cust exist push NoSuchFieldException.");
        } catch (NoSuchMethodException unused4) {
            com.huawei.hms.support.log.a.d("CommFun", "check cust exist push NoSuchMethodException.");
        } catch (IllegalArgumentException unused5) {
            com.huawei.hms.support.log.a.d("CommFun", "check cust exist push IllegalArgumentException.");
        } catch (IllegalAccessException unused6) {
            com.huawei.hms.support.log.a.d("CommFun", "check cust exist push IllegalAccessException.");
        } catch (InvocationTargetException unused7) {
            com.huawei.hms.support.log.a.d("CommFun", "check cust exist push InvocationTargetException.");
        } catch (IOException unused8) {
            com.huawei.hms.support.log.a.d("CommFun", "check jarFile exist but get not path");
        }
        return false;
    }

    public static String b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            com.huawei.hms.support.log.a.a("CommFun", "package not exist");
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("CommFun", "getApkVersionName error" + e.getMessage());
        }
        return Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    }

    public static long c(Context context) {
        try {
            return (long) context.getPackageManager().getPackageInfo("com.huawei.android.pushagent", 0).versionCode;
        } catch (Exception unused) {
            com.huawei.hms.support.log.a.d("CommFun", "get nc versionCode error");
            return -1;
        }
    }
}
