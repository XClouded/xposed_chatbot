package com.alibaba.analytics.core.logbuilder;

import android.content.Context;
import android.provider.Settings;
import com.alibaba.analytics.core.Variables;
import mtopsdk.common.util.SymbolExpUtil;

class Reserve5Helper {
    private static final String ANDROID_ID = "aid";
    private static final String OAID_ID = "oaid";
    private static final String WIDEVINE_ID = "wvid";
    private static boolean bInitAndroidId = false;
    private static boolean bInitWideVineId = false;
    private static boolean bReserve = false;
    private static String mAndroidId = "";
    private static String mReserve = "";
    private static String mWideVineId = "";

    Reserve5Helper() {
    }

    static String getReserve(Context context) {
        if (bReserve || context == null) {
            return mReserve;
        }
        synchronized (Reserve5Helper.class) {
            if (bReserve) {
                String str = mReserve;
                return str;
            }
            mReserve = "aid=" + getAndroidID(context) + "," + WIDEVINE_ID + SymbolExpUtil.SYMBOL_EQUAL + getWideVineId() + "," + OAID_ID + SymbolExpUtil.SYMBOL_EQUAL + Variables.getInstance().getOaid();
            bReserve = true;
            return mReserve;
        }
    }

    private static String getAndroidID(Context context) {
        if (bInitAndroidId || context == null) {
            return mAndroidId;
        }
        try {
            mAndroidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Throwable unused) {
        }
        bInitAndroidId = true;
        return mAndroidId;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0037, code lost:
        if (android.os.Build.VERSION.SDK_INT >= 28) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0053, code lost:
        if (android.os.Build.VERSION.SDK_INT >= 28) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0055, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0059, code lost:
        r3.release();
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getWideVineId() {
        /*
            boolean r0 = bInitWideVineId
            if (r0 == 0) goto L_0x0007
            java.lang.String r0 = mWideVineId
            return r0
        L_0x0007:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 18
            if (r0 < r1) goto L_0x005c
            java.util.UUID r0 = new java.util.UUID
            r1 = -1301668207276963122(0xedef8ba979d64ace, double:-3.563403477674908E221)
            r3 = -6645017420763422227(0xa3c827dcd51d21ed, double:-2.5964014370906125E-136)
            r0.<init>(r1, r3)
            r1 = 0
            r2 = 28
            android.media.MediaDrm r3 = new android.media.MediaDrm     // Catch:{ Throwable -> 0x004e, all -> 0x003e }
            r3.<init>(r0)     // Catch:{ Throwable -> 0x004e, all -> 0x003e }
            java.lang.String r0 = "deviceUniqueId"
            byte[] r0 = r3.getPropertyByteArray(r0)     // Catch:{ Throwable -> 0x003c, all -> 0x003a }
            r1 = 0
            java.lang.String r0 = android.util.Base64.encodeToString(r0, r1)     // Catch:{ Throwable -> 0x003c, all -> 0x003a }
            java.lang.String r0 = r0.trim()     // Catch:{ Throwable -> 0x003c, all -> 0x003a }
            mWideVineId = r0     // Catch:{ Throwable -> 0x003c, all -> 0x003a }
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 < r2) goto L_0x0059
            goto L_0x0055
        L_0x003a:
            r0 = move-exception
            goto L_0x0040
        L_0x003c:
            goto L_0x004f
        L_0x003e:
            r0 = move-exception
            r3 = r1
        L_0x0040:
            if (r3 == 0) goto L_0x004d
            int r1 = android.os.Build.VERSION.SDK_INT
            if (r1 < r2) goto L_0x004a
            r3.close()
            goto L_0x004d
        L_0x004a:
            r3.release()
        L_0x004d:
            throw r0
        L_0x004e:
            r3 = r1
        L_0x004f:
            if (r3 == 0) goto L_0x005c
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 < r2) goto L_0x0059
        L_0x0055:
            r3.close()
            goto L_0x005c
        L_0x0059:
            r3.release()
        L_0x005c:
            r0 = 1
            bInitWideVineId = r0
            java.lang.String r0 = mWideVineId
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.logbuilder.Reserve5Helper.getWideVineId():java.lang.String");
    }
}
