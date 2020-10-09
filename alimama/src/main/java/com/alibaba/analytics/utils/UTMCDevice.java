package com.alibaba.analytics.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.network.NetworkOperatorUtil;
import com.alibaba.analytics.core.network.NetworkUtil;
import java.lang.reflect.Field;
import java.util.Map;

public class UTMCDevice {
    private static Map<String, String> s_deviceInfoMap;

    public static synchronized void updateUTMCDeviceNetworkStatus(Context context) {
        synchronized (UTMCDevice.class) {
            updateDeviceNetworkStatus(context, s_deviceInfoMap);
        }
    }

    private static void updateDeviceNetworkStatus(Context context, Map<String, String> map) {
        if (context != null && map != null) {
            Logger.d("UTMCDevice", "updateDeviceNetworkStatus");
            try {
                String[] networkState = NetworkUtil.getNetworkState(context);
                map.put(LogField.ACCESS.toString(), networkState[0]);
                if (networkState[0].equals(NetworkUtil.NETWORK_CLASS_2_3_G)) {
                    map.put(LogField.ACCESS_SUBTYPE.toString(), networkState[1]);
                } else if (networkState[1].equals(NetworkUtil.NETWORK_CLASS_5_G)) {
                    map.put(LogField.ACCESS_SUBTYPE.toString(), NetworkUtil.NETWORK_CLASS_5_G);
                } else {
                    map.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
                }
            } catch (Exception unused) {
                map.put(LogField.ACCESS.toString(), "Unknown");
                map.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
            }
            map.put(LogField.CARRIER.toString(), NetworkOperatorUtil.getCurrentNetworkOperatorName());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r2.put(com.alibaba.analytics.core.model.LogField.RESOLUTION.toString(), "Unknown");
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0087 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:46:0x0165 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.util.Map<java.lang.String, java.lang.String> getDeviceInfo(android.content.Context r7) {
        /*
            java.lang.Class<com.alibaba.analytics.utils.UTMCDevice> r0 = com.alibaba.analytics.utils.UTMCDevice.class
            monitor-enter(r0)
            java.util.Map<java.lang.String, java.lang.String> r1 = s_deviceInfoMap     // Catch:{ all -> 0x017d }
            if (r1 == 0) goto L_0x000b
            java.util.Map<java.lang.String, java.lang.String> r7 = s_deviceInfoMap     // Catch:{ all -> 0x017d }
            monitor-exit(r0)
            return r7
        L_0x000b:
            r1 = 0
            if (r7 == 0) goto L_0x017b
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ all -> 0x017d }
            r2.<init>()     // Catch:{ all -> 0x017d }
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.UTDID     // Catch:{ Exception -> 0x0021 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0021 }
            java.lang.String r4 = com.ut.device.UTDevice.getUtdid(r7)     // Catch:{ Exception -> 0x0021 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0021 }
            goto L_0x0028
        L_0x0021:
            java.lang.String r3 = ""
            java.lang.String r4 = "utdid4all jar doesn't exist"
            android.util.Log.e(r3, r4)     // Catch:{ Exception -> 0x0179 }
        L_0x0028:
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.IMEI     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = com.alibaba.analytics.utils.PhoneInfoUtils.getImei(r7)     // Catch:{ Exception -> 0x0179 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.IMSI     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = com.alibaba.analytics.utils.PhoneInfoUtils.getImsi(r7)     // Catch:{ Exception -> 0x0179 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.DEVICE_MODEL     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = android.os.Build.MODEL     // Catch:{ Exception -> 0x0179 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.BRAND     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = android.os.Build.BRAND     // Catch:{ Exception -> 0x0179 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.OSVERSION     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = android.os.Build.VERSION.RELEASE     // Catch:{ Exception -> 0x0179 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.OS     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = "a"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            android.content.pm.PackageManager r3 = r7.getPackageManager()     // Catch:{ NameNotFoundException -> 0x0087 }
            java.lang.String r4 = r7.getPackageName()     // Catch:{ NameNotFoundException -> 0x0087 }
            r5 = 0
            android.content.pm.PackageInfo r3 = r3.getPackageInfo(r4, r5)     // Catch:{ NameNotFoundException -> 0x0087 }
            com.alibaba.analytics.core.model.LogField r4 = com.alibaba.analytics.core.model.LogField.APPVERSION     // Catch:{ NameNotFoundException -> 0x0087 }
            java.lang.String r4 = r4.toString()     // Catch:{ NameNotFoundException -> 0x0087 }
            java.lang.String r3 = r3.versionName     // Catch:{ NameNotFoundException -> 0x0087 }
            r2.put(r4, r3)     // Catch:{ NameNotFoundException -> 0x0087 }
            goto L_0x0092
        L_0x0087:
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.APPVERSION     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = "Unknown"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
        L_0x0092:
            boolean r3 = isYunOSSystem()     // Catch:{ Exception -> 0x0179 }
            if (r3 == 0) goto L_0x00df
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.OS     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = "y"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = getUUID()     // Catch:{ Exception -> 0x0179 }
            boolean r4 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0179 }
            if (r4 != 0) goto L_0x00b7
            com.alibaba.analytics.core.model.UTMCLogFields r4 = com.alibaba.analytics.core.model.UTMCLogFields.DEVICE_ID     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0179 }
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0179 }
        L_0x00b7:
            java.lang.String r3 = "ro.yunos.version"
            java.lang.String r3 = java.lang.System.getProperty(r3)     // Catch:{ Exception -> 0x0179 }
            boolean r4 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0179 }
            if (r4 != 0) goto L_0x00cc
            com.alibaba.analytics.core.model.LogField r4 = com.alibaba.analytics.core.model.LogField.OSVERSION     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0179 }
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0179 }
        L_0x00cc:
            java.lang.String r3 = getBuildVersion()     // Catch:{ Exception -> 0x0179 }
            boolean r4 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0179 }
            if (r4 != 0) goto L_0x00df
            com.alibaba.analytics.core.model.LogField r4 = com.alibaba.analytics.core.model.LogField.OSVERSION     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0179 }
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0179 }
        L_0x00df:
            boolean r3 = isYunOSTvSystem()     // Catch:{ Exception -> 0x0179 }
            if (r3 == 0) goto L_0x00f0
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.OS     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = "a"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
        L_0x00f0:
            android.content.res.Configuration r3 = new android.content.res.Configuration     // Catch:{ Exception -> 0x0165 }
            r3.<init>()     // Catch:{ Exception -> 0x0165 }
            android.content.ContentResolver r4 = r7.getContentResolver()     // Catch:{ Exception -> 0x0165 }
            android.provider.Settings.System.getConfiguration(r4, r3)     // Catch:{ Exception -> 0x0165 }
            java.util.Locale r4 = r3.locale     // Catch:{ Exception -> 0x0165 }
            if (r4 == 0) goto L_0x0110
            com.alibaba.analytics.core.model.LogField r4 = com.alibaba.analytics.core.model.LogField.LANGUAGE     // Catch:{ Exception -> 0x0165 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0165 }
            java.util.Locale r3 = r3.locale     // Catch:{ Exception -> 0x0165 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0165 }
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0165 }
            goto L_0x011b
        L_0x0110:
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.LANGUAGE     // Catch:{ Exception -> 0x0165 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0165 }
            java.lang.String r4 = "Unknown"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0165 }
        L_0x011b:
            android.content.res.Resources r3 = r7.getResources()     // Catch:{ Exception -> 0x0165 }
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()     // Catch:{ Exception -> 0x0165 }
            int r4 = r3.widthPixels     // Catch:{ Exception -> 0x0165 }
            int r3 = r3.heightPixels     // Catch:{ Exception -> 0x0165 }
            if (r3 <= r4) goto L_0x0147
            com.alibaba.analytics.core.model.LogField r5 = com.alibaba.analytics.core.model.LogField.RESOLUTION     // Catch:{ Exception -> 0x0165 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0165 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0165 }
            r6.<init>()     // Catch:{ Exception -> 0x0165 }
            r6.append(r3)     // Catch:{ Exception -> 0x0165 }
            java.lang.String r3 = "*"
            r6.append(r3)     // Catch:{ Exception -> 0x0165 }
            r6.append(r4)     // Catch:{ Exception -> 0x0165 }
            java.lang.String r3 = r6.toString()     // Catch:{ Exception -> 0x0165 }
            r2.put(r5, r3)     // Catch:{ Exception -> 0x0165 }
            goto L_0x0170
        L_0x0147:
            com.alibaba.analytics.core.model.LogField r5 = com.alibaba.analytics.core.model.LogField.RESOLUTION     // Catch:{ Exception -> 0x0165 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0165 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0165 }
            r6.<init>()     // Catch:{ Exception -> 0x0165 }
            r6.append(r4)     // Catch:{ Exception -> 0x0165 }
            java.lang.String r4 = "*"
            r6.append(r4)     // Catch:{ Exception -> 0x0165 }
            r6.append(r3)     // Catch:{ Exception -> 0x0165 }
            java.lang.String r3 = r6.toString()     // Catch:{ Exception -> 0x0165 }
            r2.put(r5, r3)     // Catch:{ Exception -> 0x0165 }
            goto L_0x0170
        L_0x0165:
            com.alibaba.analytics.core.model.LogField r3 = com.alibaba.analytics.core.model.LogField.RESOLUTION     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r4 = "Unknown"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0179 }
        L_0x0170:
            updateDeviceNetworkStatus(r7, r2)     // Catch:{ Exception -> 0x0179 }
            s_deviceInfoMap = r2     // Catch:{ all -> 0x017d }
            java.util.Map<java.lang.String, java.lang.String> r7 = s_deviceInfoMap     // Catch:{ all -> 0x017d }
            monitor-exit(r0)
            return r7
        L_0x0179:
            monitor-exit(r0)
            return r1
        L_0x017b:
            monitor-exit(r0)
            return r1
        L_0x017d:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.UTMCDevice.getDeviceInfo(android.content.Context):java.util.Map");
    }

    private static boolean isYunOSSystem() {
        if ((System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").toLowerCase().contains("lemur")) && TextUtils.isEmpty(System.getProperty("ro.yunos.version")) && TextUtils.isEmpty(SystemProperties.get("ro.yunos.build.version"))) {
            return isYunOSTvSystem();
        }
        return true;
    }

    private static boolean isYunOSTvSystem() {
        return !TextUtils.isEmpty(SystemProperties.get("ro.yunos.product.chip")) || !TextUtils.isEmpty(SystemProperties.get("ro.yunos.hardware"));
    }

    private static String getUUID() {
        String str = SystemProperties.get("ro.aliyun.clouduuid");
        if (StringUtils.isEmpty(str)) {
            str = SystemProperties.get("ro.sys.aliyun.clouduuid");
        }
        return StringUtils.isEmpty(str) ? getYunOSTVUuid() : str;
    }

    private static String getYunOSTVUuid() {
        try {
            return (String) Class.forName("com.yunos.baseservice.clouduuid.CloudUUID").getMethod("getCloudUUID", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    private static String getBuildVersion() {
        try {
            Field declaredField = Build.class.getDeclaredField("YUNOS_BUILD_VERSION");
            if (declaredField == null) {
                return null;
            }
            declaredField.setAccessible(true);
            return (String) declaredField.get(new String());
        } catch (Throwable unused) {
            return null;
        }
    }

    public static boolean isPad(Context context) {
        if (context == null) {
            return false;
        }
        return isPadByPhoneType(context) || isPadByScrren(context);
    }

    private static boolean isPadByScrren(Context context) {
        try {
            return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static boolean isPadByPhoneType(Context context) {
        try {
            if (((TelephonyManager) context.getSystemService("phone")).getPhoneType() == 0) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }
}
