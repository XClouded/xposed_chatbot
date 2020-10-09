package mtopsdk.xstate.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.util.Random;
import mtopsdk.common.util.ConfigStoreManager;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;

public class PhoneInfo {
    private static final String IMEI = "mtopsdk_imei";
    private static final String IMSI = "mtopsdk_imsi";
    private static final String MACADDRESS = "mtopsdk_mac_address";
    private static final String TAG = "mtopsdk.PhoneInfo";
    private static ConfigStoreManager storeManager = ConfigStoreManager.getInstance();

    private static String generateImei() {
        StringBuilder sb = new StringBuilder();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String valueOf = String.valueOf(currentTimeMillis);
            sb.append(valueOf.substring(valueOf.length() - 5));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Build.MODEL.replaceAll(Operators.SPACE_STR, ""));
            while (sb2.length() < 6) {
                sb2.append('0');
            }
            sb.append(sb2.substring(0, 6));
            Random random = new Random(currentTimeMillis);
            long j = 0;
            while (j < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
                j = random.nextLong();
            }
            sb.append(Long.toHexString(j).substring(0, 4));
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[generateImei] error --->" + th.toString());
        }
        return sb.toString();
    }

    @TargetApi(8)
    public static String getImei(Context context) {
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        try {
            sb = new StringBuilder(storeManager.getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMEI));
            try {
                if (StringUtils.isNotBlank(sb.toString())) {
                    return new String(Base64.decode(sb.toString(), 0));
                }
                StringBuilder sb3 = new StringBuilder(((TelephonyManager) context.getSystemService("phone")).getDeviceId());
                try {
                    sb2 = StringUtils.isBlank(sb3.toString()) ? new StringBuilder(generateImei()) : sb3;
                    sb = new StringBuilder(sb2.toString().replaceAll(Operators.SPACE_STR, "").trim());
                    while (sb.length() < 15) {
                        sb.insert(0, "0");
                    }
                    storeManager.saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMEI, Base64.encodeToString(sb.toString().getBytes(), 0));
                    return sb.toString().trim();
                } catch (Throwable th) {
                    th = th;
                    sb = sb3;
                    TBSdkLog.e(TAG, "[getImei] error ---" + th.toString());
                    return sb.toString();
                }
            } catch (Throwable th2) {
                th = th2;
                TBSdkLog.e(TAG, "[getImei] error ---" + th.toString());
                return sb.toString();
            }
        } catch (Throwable th3) {
            th = th3;
            sb = sb2;
            TBSdkLog.e(TAG, "[getImei] error ---" + th.toString());
            return sb.toString();
        }
    }

    @TargetApi(8)
    public static String getImsi(Context context) {
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        try {
            sb = new StringBuilder(storeManager.getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMSI));
            try {
                if (StringUtils.isNotBlank(sb.toString())) {
                    return new String(Base64.decode(sb.toString(), 0));
                }
                StringBuilder sb3 = new StringBuilder(((TelephonyManager) context.getSystemService("phone")).getSubscriberId());
                try {
                    sb2 = StringUtils.isBlank(sb3.toString()) ? new StringBuilder(generateImei()) : sb3;
                    sb = new StringBuilder(sb2.toString().replaceAll(Operators.SPACE_STR, "").trim());
                    while (sb.length() < 15) {
                        sb.insert(0, "0");
                    }
                    storeManager.saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, ConfigStoreManager.PHONE_INFO_STORE_PREFIX, IMSI, Base64.encodeToString(sb.toString().getBytes(), 0));
                } catch (Throwable th) {
                    th = th;
                    sb = sb3;
                    TBSdkLog.e(TAG, "[getImsi]error ---" + th.toString());
                    return sb.toString();
                }
                return sb.toString();
            } catch (Throwable th2) {
                th = th2;
                TBSdkLog.e(TAG, "[getImsi]error ---" + th.toString());
                return sb.toString();
            }
        } catch (Throwable th3) {
            th = th3;
            sb = sb2;
            TBSdkLog.e(TAG, "[getImsi]error ---" + th.toString());
            return sb.toString();
        }
    }

    public static String getPhoneBaseInfo(Context context) {
        try {
            String str = Build.VERSION.RELEASE;
            String str2 = Build.MANUFACTURER;
            String str3 = Build.MODEL;
            StringBuilder sb = new StringBuilder(32);
            sb.append("MTOPSDK/");
            sb.append(HttpHeaderConstant.M_SDKVER_VALUE);
            sb.append(" (");
            sb.append(TimeCalculator.PLATFORM_ANDROID);
            sb.append(";");
            sb.append(str);
            sb.append(";");
            sb.append(str2);
            sb.append(";");
            sb.append(str3);
            sb.append(Operators.BRACKET_END_STR);
            return sb.toString();
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[getPhoneBaseInfo] error ---" + th.toString());
            return "";
        }
    }

    public static String getOriginalImei(Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        try {
            String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (deviceId == null) {
                return deviceId;
            }
            try {
                str = deviceId.trim();
            } catch (Throwable th) {
                Throwable th2 = th;
                str = deviceId;
                th = th2;
                TBSdkLog.e(TAG, "[getOriginalImei]error ---" + th.toString());
                return str;
            }
            return str;
        } catch (Throwable th3) {
            th = th3;
            TBSdkLog.e(TAG, "[getOriginalImei]error ---" + th.toString());
            return str;
        }
    }

    public static String getOriginalImsi(Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        try {
            String subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (subscriberId == null) {
                return subscriberId;
            }
            try {
                str = subscriberId.trim();
            } catch (Throwable th) {
                Throwable th2 = th;
                str = subscriberId;
                th = th2;
                TBSdkLog.e(TAG, "[getOriginalImsi]error ---" + th.toString());
                return str;
            }
            return str;
        } catch (Throwable th3) {
            th = th3;
            TBSdkLog.e(TAG, "[getOriginalImsi]error ---" + th.toString());
            return str;
        }
    }

    public static String getSerialNum() {
        if (Build.VERSION.SDK_INT > 27) {
            return null;
        }
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{"ro.serialno", "unknown"});
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[getSerialNum]error ---" + th.toString());
            return null;
        }
    }

    @TargetApi(3)
    public static String getAndroidId(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[getAndroidId]error ---" + th.toString());
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0087 A[Catch:{ Throwable -> 0x009f }] */
    @android.annotation.TargetApi(8)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getLocalMacAddress(android.content.Context r9) {
        /*
            if (r9 != 0) goto L_0x0005
            java.lang.String r9 = ""
            return r9
        L_0x0005:
            java.lang.String r0 = ""
            mtopsdk.common.util.ConfigStoreManager r1 = storeManager     // Catch:{ Throwable -> 0x009f }
            java.lang.String r2 = "MtopConfigStore"
            java.lang.String r3 = "PHONE_INFO_STORE."
            java.lang.String r4 = "mtopsdk_mac_address"
            java.lang.String r1 = r1.getConfigItem(r9, r2, r3, r4)     // Catch:{ Throwable -> 0x009f }
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r1)     // Catch:{ Throwable -> 0x009c }
            r2 = 0
            if (r0 == 0) goto L_0x0024
            java.lang.String r9 = new java.lang.String     // Catch:{ Throwable -> 0x009c }
            byte[] r0 = android.util.Base64.decode(r1, r2)     // Catch:{ Throwable -> 0x009c }
            r9.<init>(r0)     // Catch:{ Throwable -> 0x009c }
            return r9
        L_0x0024:
            java.lang.String r0 = "android.permission.ACCESS_WIFI_STATE"
            int r0 = r9.checkCallingOrSelfPermission(r0)     // Catch:{ Throwable -> 0x009c }
            if (r0 != 0) goto L_0x0080
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x009c }
            r3 = 23
            if (r0 < r3) goto L_0x006d
            java.lang.String r0 = "wlan0"
            java.net.NetworkInterface r0 = java.net.NetworkInterface.getByName(r0)     // Catch:{ Throwable -> 0x009c }
            byte[] r0 = r0.getHardwareAddress()     // Catch:{ Throwable -> 0x009c }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009c }
            r3.<init>()     // Catch:{ Throwable -> 0x009c }
            r4 = 0
        L_0x0042:
            int r5 = r0.length     // Catch:{ Throwable -> 0x009c }
            if (r4 >= r5) goto L_0x0068
            java.lang.String r5 = "%02X%s"
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x009c }
            byte r7 = r0[r4]     // Catch:{ Throwable -> 0x009c }
            java.lang.Byte r7 = java.lang.Byte.valueOf(r7)     // Catch:{ Throwable -> 0x009c }
            r6[r2] = r7     // Catch:{ Throwable -> 0x009c }
            int r7 = r0.length     // Catch:{ Throwable -> 0x009c }
            r8 = 1
            int r7 = r7 - r8
            if (r4 >= r7) goto L_0x005a
            java.lang.String r7 = ":"
            goto L_0x005c
        L_0x005a:
            java.lang.String r7 = ""
        L_0x005c:
            r6[r8] = r7     // Catch:{ Throwable -> 0x009c }
            java.lang.String r5 = java.lang.String.format(r5, r6)     // Catch:{ Throwable -> 0x009c }
            r3.append(r5)     // Catch:{ Throwable -> 0x009c }
            int r4 = r4 + 1
            goto L_0x0042
        L_0x0068:
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x009c }
            goto L_0x0081
        L_0x006d:
            java.lang.String r0 = "wifi"
            java.lang.Object r0 = r9.getSystemService(r0)     // Catch:{ Throwable -> 0x009c }
            android.net.wifi.WifiManager r0 = (android.net.wifi.WifiManager) r0     // Catch:{ Throwable -> 0x009c }
            android.net.wifi.WifiInfo r0 = r0.getConnectionInfo()     // Catch:{ Throwable -> 0x009c }
            if (r0 == 0) goto L_0x0080
            java.lang.String r0 = r0.getMacAddress()     // Catch:{ Throwable -> 0x009c }
            goto L_0x0081
        L_0x0080:
            r0 = r1
        L_0x0081:
            boolean r1 = mtopsdk.common.util.StringUtils.isNotBlank(r0)     // Catch:{ Throwable -> 0x009f }
            if (r1 == 0) goto L_0x00ba
            mtopsdk.common.util.ConfigStoreManager r3 = storeManager     // Catch:{ Throwable -> 0x009f }
            java.lang.String r5 = "MtopConfigStore"
            java.lang.String r6 = "PHONE_INFO_STORE."
            java.lang.String r7 = "mtopsdk_mac_address"
            byte[] r1 = r0.getBytes()     // Catch:{ Throwable -> 0x009f }
            java.lang.String r8 = android.util.Base64.encodeToString(r1, r2)     // Catch:{ Throwable -> 0x009f }
            r4 = r9
            r3.saveConfigItem(r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x009f }
            goto L_0x00ba
        L_0x009c:
            r9 = move-exception
            r0 = r1
            goto L_0x00a0
        L_0x009f:
            r9 = move-exception
        L_0x00a0:
            java.lang.String r1 = "mtopsdk.PhoneInfo"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "[getLocalMacAddress]error ---"
            r2.append(r3)
            java.lang.String r9 = r9.toString()
            r2.append(r9)
            java.lang.String r9 = r2.toString()
            mtopsdk.common.util.TBSdkLog.e(r1, r9)
        L_0x00ba:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.xstate.util.PhoneInfo.getLocalMacAddress(android.content.Context):java.lang.String");
    }
}
