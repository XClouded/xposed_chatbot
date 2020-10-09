package com.alibaba.analytics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class PhoneInfoUtils {
    private static final String STORAGE_KEY = "UTCommon";
    private static boolean bGetSystemImei = false;
    private static boolean bGetSystemImsi = false;
    private static String mSystemImei = "";
    private static String mSystemImsi = "";
    private static final Random s_random = new Random();

    public static final String getUniqueID() {
        int nextInt = s_random.nextInt();
        int nextInt2 = s_random.nextInt();
        byte[] bytes = IntUtils.getBytes((int) (System.currentTimeMillis() / 1000));
        byte[] bytes2 = IntUtils.getBytes((int) System.nanoTime());
        byte[] bytes3 = IntUtils.getBytes(nextInt);
        byte[] bytes4 = IntUtils.getBytes(nextInt2);
        byte[] bArr = new byte[16];
        System.arraycopy(bytes, 0, bArr, 0, 4);
        System.arraycopy(bytes2, 0, bArr, 4, 4);
        System.arraycopy(bytes3, 0, bArr, 8, 4);
        System.arraycopy(bytes4, 0, bArr, 12, 4);
        return Base64.encodeToString(bArr, 2);
    }

    public static String getImei(Context context) {
        String str;
        if (context != null) {
            try {
                String string = context.getSharedPreferences(STORAGE_KEY, 0).getString("_ie", "");
                if (!StringUtils.isEmpty(string)) {
                    String str2 = new String(Base64.decode(string.getBytes(), 2), "UTF-8");
                    if (!StringUtils.isEmpty(str2)) {
                        return str2;
                    }
                }
            } catch (Exception unused) {
            }
            str = getImeiBySystem(context);
        } else {
            str = null;
        }
        if (StringUtils.isEmpty(str)) {
            str = getUniqueID();
        }
        if (context != null) {
            try {
                SharedPreferences.Editor edit = context.getSharedPreferences(STORAGE_KEY, 0).edit();
                edit.putString("_ie", new String(Base64.encode(str.getBytes("UTF-8"), 2)));
                edit.commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static String getImsi(Context context) {
        String str;
        if (context != null) {
            try {
                String string = context.getSharedPreferences(STORAGE_KEY, 0).getString("_is", "");
                if (!StringUtils.isEmpty(string)) {
                    String str2 = new String(Base64.decode(string.getBytes(), 2), "UTF-8");
                    if (!StringUtils.isEmpty(str2)) {
                        return str2;
                    }
                }
            } catch (Exception unused) {
            }
            str = getImsiBySystem(context);
        } else {
            str = null;
        }
        if (StringUtils.isEmpty(str)) {
            str = getUniqueID();
        }
        if (context != null) {
            try {
                SharedPreferences.Editor edit = context.getSharedPreferences(STORAGE_KEY, 0).edit();
                edit.putString("_is", new String(Base64.encode(str.getBytes("UTF-8"), 2)));
                edit.commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0037 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String getImeiBySystem(android.content.Context r6) {
        /*
            java.lang.Class<com.alibaba.analytics.utils.PhoneInfoUtils> r0 = com.alibaba.analytics.utils.PhoneInfoUtils.class
            monitor-enter(r0)
            boolean r1 = bGetSystemImei     // Catch:{ all -> 0x0042 }
            if (r1 == 0) goto L_0x000b
            java.lang.String r6 = mSystemImei     // Catch:{ all -> 0x0042 }
            monitor-exit(r0)
            return r6
        L_0x000b:
            r1 = 0
            if (r6 != 0) goto L_0x0010
            monitor-exit(r0)
            return r1
        L_0x0010:
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = checkPermission(r6, r2)     // Catch:{ Throwable -> 0x001a }
            if (r2 != 0) goto L_0x001a
            monitor-exit(r0)
            return r1
        L_0x001a:
            r1 = 1
            java.lang.String r2 = "PhoneInfoUtils"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            r4 = 0
            java.lang.String r5 = "getImei"
            r3[r4] = r5     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            java.lang.String r2 = "phone"
            java.lang.Object r6 = r6.getSystemService(r2)     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            android.telephony.TelephonyManager r6 = (android.telephony.TelephonyManager) r6     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            if (r6 == 0) goto L_0x0037
            java.lang.String r6 = r6.getDeviceId()     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            mSystemImei = r6     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
        L_0x0037:
            bGetSystemImei = r1     // Catch:{ all -> 0x0042 }
            goto L_0x003e
        L_0x003a:
            r6 = move-exception
            bGetSystemImei = r1     // Catch:{ all -> 0x0042 }
            throw r6     // Catch:{ all -> 0x0042 }
        L_0x003e:
            java.lang.String r6 = mSystemImei     // Catch:{ all -> 0x0042 }
            monitor-exit(r0)
            return r6
        L_0x0042:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.PhoneInfoUtils.getImeiBySystem(android.content.Context):java.lang.String");
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0037 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String getImsiBySystem(android.content.Context r6) {
        /*
            java.lang.Class<com.alibaba.analytics.utils.PhoneInfoUtils> r0 = com.alibaba.analytics.utils.PhoneInfoUtils.class
            monitor-enter(r0)
            boolean r1 = bGetSystemImsi     // Catch:{ all -> 0x0042 }
            if (r1 == 0) goto L_0x000b
            java.lang.String r6 = mSystemImsi     // Catch:{ all -> 0x0042 }
            monitor-exit(r0)
            return r6
        L_0x000b:
            r1 = 0
            if (r6 != 0) goto L_0x0010
            monitor-exit(r0)
            return r1
        L_0x0010:
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = checkPermission(r6, r2)     // Catch:{ Throwable -> 0x001a }
            if (r2 != 0) goto L_0x001a
            monitor-exit(r0)
            return r1
        L_0x001a:
            r1 = 1
            java.lang.String r2 = "PhoneInfoUtils"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            r4 = 0
            java.lang.String r5 = "getImsi"
            r3[r4] = r5     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            java.lang.String r2 = "phone"
            java.lang.Object r6 = r6.getSystemService(r2)     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            android.telephony.TelephonyManager r6 = (android.telephony.TelephonyManager) r6     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            if (r6 == 0) goto L_0x0037
            java.lang.String r6 = r6.getSubscriberId()     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
            mSystemImsi = r6     // Catch:{ Throwable -> 0x0037, all -> 0x003a }
        L_0x0037:
            bGetSystemImsi = r1     // Catch:{ all -> 0x0042 }
            goto L_0x003e
        L_0x003a:
            r6 = move-exception
            bGetSystemImsi = r1     // Catch:{ all -> 0x0042 }
            throw r6     // Catch:{ all -> 0x0042 }
        L_0x003e:
            java.lang.String r6 = mSystemImsi     // Catch:{ all -> 0x0042 }
            monitor-exit(r0)
            return r6
        L_0x0042:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.PhoneInfoUtils.getImsiBySystem(android.content.Context):java.lang.String");
    }

    private static boolean checkPermission(Context context, String str) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        try {
            if (context.checkPermission(str, Binder.getCallingPid(), Binder.getCallingUid()) == 0) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return true;
        }
    }
}
