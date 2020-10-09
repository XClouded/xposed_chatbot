package com.taobao.accs.utl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.taobao.accs.common.Constants;
import com.taobao.accs.update.ACCSClassLoader;

public class Utils {
    public static final String SP_AGOO_BIND_FILE_NAME = "AGOO_BIND";
    private static final String TAG = "Utils";
    private static final byte[] mLock = new byte[0];
    static int mode = -1;
    private static int targetSdkVersion = -1;

    public static boolean isTarget26(Context context) {
        if (context == null) {
            return false;
        }
        if (targetSdkVersion == -1) {
            targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        }
        if (targetSdkVersion < 26 || Build.VERSION.SDK_INT < 26) {
            return false;
        }
        return true;
    }

    public static void setMode(Context context, int i) {
        try {
            synchronized (mLock) {
                mode = i;
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.putInt(Constants.SP_KEY_DEBUG_MODE, i);
                edit.apply();
            }
        } catch (Throwable th) {
            ALog.e(TAG, "setMode", th, new Object[0]);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v11, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v13, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v14, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v15, resolved type: int} */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
        r4 = r4;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getMode(android.content.Context r4) {
        /*
            int r0 = mode
            r1 = -1
            if (r0 == r1) goto L_0x0008
            int r4 = mode
            return r4
        L_0x0008:
            r0 = 0
            byte[] r1 = mLock     // Catch:{ Throwable -> 0x0022 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0022 }
            java.lang.String r2 = "ACCS_SDK"
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r2, r0)     // Catch:{ all -> 0x001a }
            java.lang.String r2 = "debug_mode"
            int r4 = r4.getInt(r2, r0)     // Catch:{ all -> 0x001a }
            monitor-exit(r1)     // Catch:{ all -> 0x0020 }
            goto L_0x002d
        L_0x001a:
            r2 = move-exception
            r4 = 0
        L_0x001c:
            monitor-exit(r1)     // Catch:{ all -> 0x0020 }
            throw r2     // Catch:{ Throwable -> 0x001e }
        L_0x001e:
            r1 = move-exception
            goto L_0x0024
        L_0x0020:
            r2 = move-exception
            goto L_0x001c
        L_0x0022:
            r1 = move-exception
            r4 = 0
        L_0x0024:
            java.lang.String r2 = "Utils"
            java.lang.String r3 = "getMode"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r2, r3, r1, r0)
        L_0x002d:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.Utils.getMode(android.content.Context):int");
    }

    public static void clearAllSharePreferences(Context context) {
        try {
            synchronized (mLock) {
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.clear();
                edit.apply();
            }
        } catch (Throwable th) {
            ALog.e(TAG, "clearAllSharePreferences", th, new Object[0]);
        }
    }

    public static void killService(Context context) {
        try {
            Class<?> loadClass = ACCSClassLoader.getInstance().getClassLoader().loadClass("com.taobao.accs.utl.UtilityImpl");
            loadClass.getMethod("killService", new Class[]{Context.class}).invoke(loadClass, new Object[]{context});
        } catch (Throwable th) {
            ALog.e(TAG, "killService", th, new Object[0]);
        }
    }

    public static boolean isMainProcess(Context context) {
        boolean z;
        try {
            Class<?> loadClass = ACCSClassLoader.getInstance().getClassLoader().loadClass("com.taobao.accs.utl.UtilityImpl");
            z = ((Boolean) loadClass.getMethod("isMainProcess", new Class[]{Context.class}).invoke(loadClass, new Object[]{context})).booleanValue();
        } catch (Throwable th) {
            ALog.e(TAG, "killservice", th, new Object[0]);
            th.printStackTrace();
            z = true;
        }
        ALog.i(TAG, "isMainProcess", "result", Boolean.valueOf(z));
        return z;
    }

    public static void setAgooAppkey(Context context, String str) {
        try {
            Class<?> loadClass = ACCSClassLoader.getInstance().getClassLoader().loadClass("org.android.agoo.common.Config");
            loadClass.getMethod("setAgooAppKey", new Class[]{Context.class, String.class}).invoke(loadClass, new Object[]{context, str});
        } catch (Throwable th) {
            ALog.e(TAG, "setAgooAppkey", th, new Object[0]);
            th.printStackTrace();
        }
    }

    @Deprecated
    public static void initConfig() {
        try {
            Class<?> loadClass = ACCSClassLoader.getInstance().getClassLoader().loadClass("com.taobao.accs.client.AccsConfig");
            loadClass.getMethod("build", new Class[0]).invoke(loadClass, new Object[0]);
        } catch (Throwable th) {
            ALog.e(TAG, "initConfig", th, new Object[0]);
            th.printStackTrace();
        }
    }

    public static void setSpValue(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            ALog.e(TAG, "setSpValue null", new Object[0]);
            return;
        }
        try {
            synchronized (mLock) {
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.putString(str, str2);
                edit.apply();
            }
            ALog.i(TAG, "setSpValue", "key", str, "value", str2);
        } catch (Exception e) {
            ALog.e(TAG, "setSpValue fail", e, new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        com.taobao.accs.utl.ALog.i(TAG, "getSpValue", "value", r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
        if (android.text.TextUtils.isEmpty(r4) == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
        com.taobao.accs.utl.ALog.e(TAG, "getSpValue use default!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003c, code lost:
        com.taobao.accs.utl.ALog.e(TAG, "getSpValue fail", r5, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getSpValue(android.content.Context r4, java.lang.String r5, java.lang.String r6) {
        /*
            r0 = 0
            r1 = 0
            byte[] r2 = mLock     // Catch:{ Throwable -> 0x003a }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x003a }
            java.lang.String r3 = "ACCS_SDK"
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r3, r1)     // Catch:{ all -> 0x0037 }
            java.lang.String r4 = r4.getString(r5, r0)     // Catch:{ all -> 0x0037 }
            monitor-exit(r2)     // Catch:{ all -> 0x0034 }
            java.lang.String r5 = "Utils"
            java.lang.String r0 = "getSpValue"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0032 }
            java.lang.String r3 = "value"
            r2[r1] = r3     // Catch:{ Throwable -> 0x0032 }
            r3 = 1
            r2[r3] = r4     // Catch:{ Throwable -> 0x0032 }
            com.taobao.accs.utl.ALog.i(r5, r0, r2)     // Catch:{ Throwable -> 0x0032 }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x0032 }
            if (r5 == 0) goto L_0x0045
            java.lang.String r5 = "Utils"
            java.lang.String r0 = "getSpValue use default!"
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0032 }
            com.taobao.accs.utl.ALog.e(r5, r0, r2)     // Catch:{ Throwable -> 0x0032 }
            r4 = r6
            goto L_0x0045
        L_0x0032:
            r5 = move-exception
            goto L_0x003c
        L_0x0034:
            r5 = move-exception
            r0 = r4
            goto L_0x0038
        L_0x0037:
            r5 = move-exception
        L_0x0038:
            monitor-exit(r2)     // Catch:{ all -> 0x0037 }
            throw r5     // Catch:{ Throwable -> 0x003a }
        L_0x003a:
            r5 = move-exception
            r4 = r0
        L_0x003c:
            java.lang.String r6 = "Utils"
            java.lang.String r0 = "getSpValue fail"
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.e(r6, r0, r5, r1)
        L_0x0045:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.Utils.getSpValue(android.content.Context, java.lang.String, java.lang.String):java.lang.String");
    }

    public static void clearAgooBindCache(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("AGOO_BIND", 0).edit();
            edit.clear();
            edit.apply();
        } catch (Exception e) {
            ALog.e(TAG, "clearAgooBindCache", e, new Object[0]);
        }
    }

    public static Bundle getMetaInfo(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                return applicationInfo.metaData;
            }
            return null;
        } catch (Throwable th) {
            ALog.e(TAG, "getMetaInfo", th, new Object[0]);
            return null;
        }
    }
}
