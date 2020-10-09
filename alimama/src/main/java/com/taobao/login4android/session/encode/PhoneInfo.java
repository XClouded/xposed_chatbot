package com.taobao.login4android.session.encode;

import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import com.taobao.weex.el.parse.Operators;
import java.util.Random;

public class PhoneInfo {
    public static final String GLOBAL_SHARED_PREFERENCES = "aliuserSP";
    public static final String IMEI = "imei";
    public static final String IMSI = "imsi";

    private static String generateImei() {
        StringBuffer stringBuffer = new StringBuffer();
        long currentTimeMillis = System.currentTimeMillis();
        String l = Long.toString(currentTimeMillis);
        stringBuffer.append(l.substring(l.length() - 5));
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(Build.MODEL.replaceAll(Operators.SPACE_STR, ""));
        while (stringBuffer2.length() < 6) {
            stringBuffer2.append('0');
        }
        stringBuffer.append(stringBuffer2.substring(0, 6));
        Random random = new Random(currentTimeMillis);
        long j = 0;
        while (j < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            j = random.nextLong();
        }
        stringBuffer.append(Long.toHexString(j).substring(0, 4));
        return stringBuffer.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004d A[Catch:{ Throwable -> 0x006d }, LOOP:0: B:18:0x0045->B:21:0x004d, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0077 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getImei(android.content.Context r4) {
        /*
            java.lang.String r0 = "aliuserSP"
            r1 = 0
            android.content.SharedPreferences r0 = r4.getSharedPreferences(r0, r1)
            java.lang.String r1 = "imei"
            r2 = 0
            java.lang.String r1 = r0.getString(r1, r2)
            if (r1 == 0) goto L_0x0016
            int r2 = r1.length()
            if (r2 != 0) goto L_0x0071
        L_0x0016:
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 28
            if (r2 > r3) goto L_0x0071
            java.lang.String r2 = "phone"
            java.lang.Object r4 = r4.getSystemService(r2)     // Catch:{ Throwable -> 0x006d }
            android.telephony.TelephonyManager r4 = (android.telephony.TelephonyManager) r4     // Catch:{ Throwable -> 0x006d }
            java.lang.String r4 = r4.getDeviceId()     // Catch:{ Throwable -> 0x006d }
            if (r4 == 0) goto L_0x0034
            int r1 = r4.length()     // Catch:{ Throwable -> 0x0031 }
            if (r1 != 0) goto L_0x0039
            goto L_0x0034
        L_0x0031:
            r0 = move-exception
            r1 = r4
            goto L_0x006e
        L_0x0034:
            java.lang.String r1 = generateImei()     // Catch:{ Throwable -> 0x0031 }
            r4 = r1
        L_0x0039:
            java.lang.String r1 = " "
            java.lang.String r2 = ""
            java.lang.String r1 = r4.replaceAll(r1, r2)     // Catch:{ Throwable -> 0x0031 }
            java.lang.String r1 = r1.trim()     // Catch:{ Throwable -> 0x0031 }
        L_0x0045:
            int r4 = r1.length()     // Catch:{ Throwable -> 0x006d }
            r2 = 15
            if (r4 >= r2) goto L_0x0060
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x006d }
            r4.<init>()     // Catch:{ Throwable -> 0x006d }
            java.lang.String r2 = "0"
            r4.append(r2)     // Catch:{ Throwable -> 0x006d }
            r4.append(r1)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x006d }
            r1 = r4
            goto L_0x0045
        L_0x0060:
            android.content.SharedPreferences$Editor r4 = r0.edit()     // Catch:{ Throwable -> 0x006d }
            java.lang.String r0 = "imei"
            r4.putString(r0, r1)     // Catch:{ Throwable -> 0x006d }
            r4.apply()     // Catch:{ Throwable -> 0x006d }
            goto L_0x0071
        L_0x006d:
            r0 = move-exception
        L_0x006e:
            r0.printStackTrace()
        L_0x0071:
            boolean r4 = android.text.TextUtils.isEmpty(r1)
            if (r4 == 0) goto L_0x007a
            java.lang.String r4 = ""
            goto L_0x007e
        L_0x007a:
            java.lang.String r4 = r1.trim()
        L_0x007e:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.session.encode.PhoneInfo.getImei(android.content.Context):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004d A[Catch:{ Throwable -> 0x006d }, LOOP:0: B:18:0x0045->B:21:0x004d, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0077 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getImsi(android.content.Context r4) {
        /*
            java.lang.String r0 = "aliuserSP"
            r1 = 0
            android.content.SharedPreferences r0 = r4.getSharedPreferences(r0, r1)
            java.lang.String r1 = "imsi"
            r2 = 0
            java.lang.String r1 = r0.getString(r1, r2)
            if (r1 == 0) goto L_0x0016
            int r2 = r1.length()
            if (r2 != 0) goto L_0x0071
        L_0x0016:
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 28
            if (r2 > r3) goto L_0x0071
            java.lang.String r2 = "phone"
            java.lang.Object r4 = r4.getSystemService(r2)     // Catch:{ Throwable -> 0x006d }
            android.telephony.TelephonyManager r4 = (android.telephony.TelephonyManager) r4     // Catch:{ Throwable -> 0x006d }
            java.lang.String r4 = r4.getSubscriberId()     // Catch:{ Throwable -> 0x006d }
            if (r4 == 0) goto L_0x0034
            int r1 = r4.length()     // Catch:{ Throwable -> 0x0031 }
            if (r1 != 0) goto L_0x0039
            goto L_0x0034
        L_0x0031:
            r0 = move-exception
            r1 = r4
            goto L_0x006e
        L_0x0034:
            java.lang.String r1 = generateImei()     // Catch:{ Throwable -> 0x0031 }
            r4 = r1
        L_0x0039:
            java.lang.String r1 = " "
            java.lang.String r2 = ""
            java.lang.String r1 = r4.replaceAll(r1, r2)     // Catch:{ Throwable -> 0x0031 }
            java.lang.String r1 = r1.trim()     // Catch:{ Throwable -> 0x0031 }
        L_0x0045:
            int r4 = r1.length()     // Catch:{ Throwable -> 0x006d }
            r2 = 15
            if (r4 >= r2) goto L_0x0060
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x006d }
            r4.<init>()     // Catch:{ Throwable -> 0x006d }
            java.lang.String r2 = "0"
            r4.append(r2)     // Catch:{ Throwable -> 0x006d }
            r4.append(r1)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x006d }
            r1 = r4
            goto L_0x0045
        L_0x0060:
            android.content.SharedPreferences$Editor r4 = r0.edit()     // Catch:{ Throwable -> 0x006d }
            java.lang.String r0 = "imsi"
            r4.putString(r0, r1)     // Catch:{ Throwable -> 0x006d }
            r4.apply()     // Catch:{ Throwable -> 0x006d }
            goto L_0x0071
        L_0x006d:
            r0 = move-exception
        L_0x006e:
            r0.printStackTrace()
        L_0x0071:
            boolean r4 = android.text.TextUtils.isEmpty(r1)
            if (r4 == 0) goto L_0x0079
            java.lang.String r1 = ""
        L_0x0079:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.session.encode.PhoneInfo.getImsi(android.content.Context):java.lang.String");
    }
}
