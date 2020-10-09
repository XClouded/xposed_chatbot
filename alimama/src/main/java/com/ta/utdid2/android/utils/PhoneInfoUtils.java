package com.ta.utdid2.android.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.ta.audid.permission.PermissionUtils;
import java.util.Random;

public class PhoneInfoUtils {
    @Deprecated
    public static String getImsi(Context context) {
        return "";
    }

    public static String getUniqueID() {
        int nextInt = new Random().nextInt();
        int nextInt2 = new Random().nextInt();
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
        TelephonyManager telephonyManager;
        String str = null;
        if (!BuildCompatUtils.isAtLeastQ() && context != null) {
            try {
                if (PermissionUtils.checkReadPhoneStatePermissionGranted(context) && (telephonyManager = (TelephonyManager) context.getSystemService("phone")) != null) {
                    str = telephonyManager.getDeviceId();
                }
            } catch (Exception unused) {
            }
        }
        if (StringUtils.isEmpty(str)) {
            str = getYunOSUuid();
        }
        if (StringUtils.isEmpty(str)) {
            str = getCheckAndroidID(context);
        }
        return StringUtils.isEmpty(str) ? getUniqueID() : str;
    }

    private static String getCheckAndroidID(Context context) {
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            try {
                return (TextUtils.isEmpty(string) || string.equalsIgnoreCase("a5f5faddde9e9f02") || string.equalsIgnoreCase("8e17f7422b35fbea") || string.equalsIgnoreCase("ece1e988e8bf71f2") || string.equalsIgnoreCase("9e3ecdf2be3b9a69") || string.equalsIgnoreCase("0000000000000000")) ? "" : string;
            } catch (Throwable unused) {
                return string;
            }
        } catch (Throwable unused2) {
            return "";
        }
    }

    private static String getYunOSUuid() {
        String str = SystemProperties.get("ro.aliyun.clouduuid", "");
        if (TextUtils.isEmpty(str)) {
            str = SystemProperties.get("ro.sys.aliyun.clouduuid", "");
        }
        return TextUtils.isEmpty(str) ? getYunOSTVUuid() : str;
    }

    private static String getYunOSTVUuid() {
        try {
            return (String) Class.forName("com.yunos.baseservice.clouduuid.CloudUUID").getMethod("getCloudUUID", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception unused) {
            return "";
        }
    }
}
