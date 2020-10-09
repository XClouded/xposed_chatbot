package com.alibaba.motu.tbrest.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.ta.utdid2.device.UTDevice;
import java.util.Locale;
import java.util.Random;

public class DeviceUtils {
    private static final String NETWORK_CLASS_2_G = "2G";
    private static final String NETWORK_CLASS_3_G = "3G";
    private static final String NETWORK_CLASS_4_G = "4G";
    private static final String NETWORK_CLASS_UNKNOWN = "Unknown";
    public static final String NETWORK_CLASS_WIFI = "Wi-Fi";
    private static String[] arrayOfString = {"Unknown", "Unknown"};
    private static String carrier;
    private static String cpuName;
    private static String imei = null;
    private static String imsi = null;

    private static String getNetworkClass(int i) {
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 13:
                return "4G";
            default:
                return "Unknown";
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x005c, code lost:
        if (r0 != null) goto L_0x0039;
     */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004c A[SYNTHETIC, Splitter:B:30:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0051 A[Catch:{ Exception -> 0x0054 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0059 A[SYNTHETIC, Splitter:B:40:0x0059] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCpuName() {
        /*
            java.lang.String r0 = cpuName
            if (r0 == 0) goto L_0x0007
            java.lang.String r0 = cpuName
            return r0
        L_0x0007:
            java.lang.String r0 = "/proc/cpuinfo"
            r1 = 0
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ IOException -> 0x0055, all -> 0x0046 }
            r2.<init>(r0)     // Catch:{ IOException -> 0x0055, all -> 0x0046 }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0044, all -> 0x003f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x0044, all -> 0x003f }
        L_0x0014:
            java.lang.String r3 = r0.readLine()     // Catch:{ IOException -> 0x0057, all -> 0x003d }
            if (r3 == 0) goto L_0x0036
            java.lang.String r4 = "Hardware"
            boolean r4 = r3.contains(r4)     // Catch:{ IOException -> 0x0057, all -> 0x003d }
            if (r4 == 0) goto L_0x0014
            java.lang.String r4 = ":"
            java.lang.String[] r3 = r3.split(r4)     // Catch:{ IOException -> 0x0057, all -> 0x003d }
            r4 = 1
            r3 = r3[r4]     // Catch:{ IOException -> 0x0057, all -> 0x003d }
            cpuName = r3     // Catch:{ IOException -> 0x0057, all -> 0x003d }
            java.lang.String r3 = cpuName     // Catch:{ IOException -> 0x0057, all -> 0x003d }
            r2.close()     // Catch:{ Exception -> 0x0035 }
            r0.close()     // Catch:{ Exception -> 0x0035 }
        L_0x0035:
            return r3
        L_0x0036:
            r2.close()     // Catch:{ Exception -> 0x005f }
        L_0x0039:
            r0.close()     // Catch:{ Exception -> 0x005f }
            goto L_0x005f
        L_0x003d:
            r1 = move-exception
            goto L_0x004a
        L_0x003f:
            r0 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x004a
        L_0x0044:
            r0 = r1
            goto L_0x0057
        L_0x0046:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
        L_0x004a:
            if (r2 == 0) goto L_0x004f
            r2.close()     // Catch:{ Exception -> 0x0054 }
        L_0x004f:
            if (r0 == 0) goto L_0x0054
            r0.close()     // Catch:{ Exception -> 0x0054 }
        L_0x0054:
            throw r1
        L_0x0055:
            r0 = r1
            r2 = r0
        L_0x0057:
            if (r2 == 0) goto L_0x005c
            r2.close()     // Catch:{ Exception -> 0x005f }
        L_0x005c:
            if (r0 == 0) goto L_0x005f
            goto L_0x0039
        L_0x005f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.utils.DeviceUtils.getCpuName():java.lang.String");
    }

    public static String getCarrier(Context context) {
        try {
            if (carrier != null) {
                return carrier;
            }
            carrier = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
            return carrier;
        } catch (Exception unused) {
            return null;
        }
    }

    @SuppressLint({"WrongConstant"})
    public static String[] getNetworkType(Context context) {
        if (context == null) {
            return arrayOfString;
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
                return arrayOfString;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return arrayOfString;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return arrayOfString;
            }
            if (activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == 1) {
                    arrayOfString[0] = "Wi-Fi";
                    return arrayOfString;
                } else if (activeNetworkInfo.getType() == 0) {
                    arrayOfString[0] = getNetworkClass(activeNetworkInfo.getSubtype());
                    arrayOfString[1] = activeNetworkInfo.getSubtypeName();
                    return arrayOfString;
                }
            }
            return arrayOfString;
        } catch (Throwable unused) {
        }
    }

    public static String getLanguage() {
        try {
            return Locale.getDefault().getLanguage();
        } catch (Exception e) {
            LogUtil.e("get country error ", e);
            return null;
        }
    }

    public static String getCountry() {
        try {
            return Locale.getDefault().getCountry();
        } catch (Exception e) {
            LogUtil.e("get country error ", e);
            return null;
        }
    }

    public static String getResolution() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            int i = displayMetrics.widthPixels;
            int i2 = displayMetrics.heightPixels;
            return i + "*" + i2;
        } catch (Exception e) {
            LogUtil.e("get resolution error", e);
            return null;
        }
    }

    public static String getUtdid(Context context) {
        try {
            return UTDevice.getUtdid(context);
        } catch (Exception e) {
            LogUtil.e("get utdid error ", e);
            return null;
        }
    }

    public static String getImsi(Context context) {
        if (imsi != null) {
            return imsi;
        }
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    imsi = telephonyManager.getSubscriberId();
                }
            } catch (Exception unused) {
            }
        }
        if (StringUtils.isEmpty(imsi)) {
            imsi = getUniqueID();
        }
        return imsi;
    }

    public static String getImei(Context context) {
        if (imei != null) {
            return imei;
        }
        imei = getUniqueID();
        return imei;
    }

    public static byte[] IntGetBytes(int i) {
        byte[] bArr = new byte[4];
        bArr[3] = (byte) (i % 256);
        int i2 = i >> 8;
        bArr[2] = (byte) (i2 % 256);
        int i3 = i2 >> 8;
        bArr[1] = (byte) (i3 % 256);
        bArr[0] = (byte) ((i3 >> 8) % 256);
        return bArr;
    }

    public static final String getUniqueID() {
        try {
            int nextInt = new Random().nextInt();
            int nextInt2 = new Random().nextInt();
            byte[] IntGetBytes = IntGetBytes((int) (System.currentTimeMillis() / 1000));
            byte[] IntGetBytes2 = IntGetBytes((int) System.nanoTime());
            byte[] IntGetBytes3 = IntGetBytes(nextInt);
            byte[] IntGetBytes4 = IntGetBytes(nextInt2);
            byte[] bArr = new byte[16];
            System.arraycopy(IntGetBytes, 0, bArr, 0, 4);
            System.arraycopy(IntGetBytes2, 0, bArr, 4, 4);
            System.arraycopy(IntGetBytes3, 0, bArr, 8, 4);
            System.arraycopy(IntGetBytes4, 0, bArr, 12, 4);
            return Base64.encodeBase64String(bArr);
        } catch (Exception unused) {
            return null;
        }
    }
}
