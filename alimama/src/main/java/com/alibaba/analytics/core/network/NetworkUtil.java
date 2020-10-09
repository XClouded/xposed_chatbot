package com.alibaba.analytics.core.network;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.analytics.utils.UTMCDevice;
import java.net.NetworkInterface;

public class NetworkUtil {
    public static final String NETWORK_CLASS_2_3_G = "2G/3G";
    public static final String NETWORK_CLASS_2_G = "2G";
    public static final String NETWORK_CLASS_3_G = "3G";
    public static final String NETWORK_CLASS_4_G = "4G";
    public static final String NETWORK_CLASS_5_G = "5G";
    public static final String NETWORK_CLASS_UNKNOWN = "Unknown";
    public static final String NETWORK_CLASS_WIFI = "Wi-Fi";
    private static final String TAG = "NetworkUtil";
    private static final String WIFIADDRESS_UNKNOWN = "00:00:00:00:00:00";
    private static String[] arrayOfString = {"Unknown", "Unknown"};
    private static boolean b5GSupported = false;
    private static boolean bCheck5GSupported = false;
    private static boolean mHaveNetworkStatus = false;
    /* access modifiers changed from: private */
    public static NetWorkStatusChecker netStatusChecker = new NetWorkStatusChecker();
    private static NetworkStatusReceiver netStatusReceiver = new NetworkStatusReceiver();

    private static String getNetworkClass(int i) {
        if (i == 20) {
            return "4G";
        }
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

    private static boolean is5GHz(int i) {
        return i > 4900 && i < 5900;
    }

    public static String getNetworkType() {
        NetworkInfo activeNetworkInfo;
        Context context = Variables.getInstance().getContext();
        if (context == null) {
            return "Unknown";
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0 || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected()) {
                return "Unknown";
            }
            if (activeNetworkInfo.getType() == 1) {
                return "Wi-Fi";
            }
            return activeNetworkInfo.getType() == 0 ? getNetworkClass(activeNetworkInfo.getSubtype()) : "Unknown";
        } catch (Throwable unused) {
            return "Unknown";
        }
    }

    public static boolean isConnectInternet(Context context) {
        if (context == null) {
            return true;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null || context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
                return true;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    public static String[] getNetworkState(Context context) {
        if (!mHaveNetworkStatus) {
            getNetworkStatus(context);
        }
        return arrayOfString;
    }

    public static String getAccess(Context context) {
        try {
            return getNetworkState(context)[0];
        } catch (Exception unused) {
            return "Unknown";
        }
    }

    public static String getAccsssSubType(Context context) {
        try {
            String[] networkState = getNetworkState(context);
            if (networkState[0].equals(NETWORK_CLASS_2_3_G)) {
                return networkState[1];
            }
            return networkState[1].equals(NETWORK_CLASS_5_G) ? NETWORK_CLASS_5_G : "Unknown";
        } catch (Exception unused) {
            return "Unknown";
        }
    }

    public static String getWifiAddress(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getWifiMacID23();
        }
        return getWifiMacID22(context);
    }

    @TargetApi(23)
    private static String getWifiMacID23() {
        try {
            byte[] hardwareAddress = NetworkInterface.getByName("wlan0").getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < hardwareAddress.length) {
                Object[] objArr = new Object[2];
                objArr[0] = Byte.valueOf(hardwareAddress[i]);
                objArr[1] = i < hardwareAddress.length - 1 ? ":" : "";
                sb.append(String.format("%02X%s", objArr));
                i++;
            }
            return sb.toString();
        } catch (Exception unused) {
            return WIFIADDRESS_UNKNOWN;
        }
    }

    private static String getWifiMacID22(Context context) {
        if (context == null) {
            return WIFIADDRESS_UNKNOWN;
        }
        try {
            WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            if (connectionInfo == null) {
                return WIFIADDRESS_UNKNOWN;
            }
            String macAddress = connectionInfo.getMacAddress();
            return TextUtils.isEmpty(macAddress) ? WIFIADDRESS_UNKNOWN : macAddress;
        } catch (Throwable unused) {
            return WIFIADDRESS_UNKNOWN;
        }
    }

    private static String convertIntToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    @Deprecated
    public static String getWifiIpAddress(Context context) {
        if (context != null) {
            try {
                WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                if (connectionInfo != null) {
                    return convertIntToIp(connectionInfo.getIpAddress());
                }
                return null;
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public static boolean isMobile(Context context) {
        if (context != null) {
            try {
                String str = getNetworkState(context)[0];
                if (str.equals("2G") || str.equals("3G") || str.equals("4G")) {
                    Logger.d(TAG, "isMobile");
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        if (context != null) {
            try {
                if (getNetworkState(context)[0].equals("Wi-Fi")) {
                    Logger.d(TAG, "isWifi");
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static void register(Context context) {
        if (context != null) {
            context.registerReceiver(netStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            try {
                NetworkOperatorUtil.registerSIMCardChangedInHandler(context);
            } catch (Exception unused) {
            }
            TaskExecutor.getInstance().submit(netStatusChecker.setContext(context));
        }
    }

    public static void unRegister(Context context) {
        if (context != null && netStatusReceiver != null) {
            context.unregisterReceiver(netStatusReceiver);
        }
    }

    private static class NetworkStatusReceiver extends BroadcastReceiver {
        private NetworkStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            TaskExecutor.getInstance().submit(NetworkUtil.netStatusChecker.setContext(context));
        }
    }

    private static class NetWorkStatusChecker implements Runnable {
        private Context context;

        private NetWorkStatusChecker() {
        }

        public NetWorkStatusChecker setContext(Context context2) {
            this.context = context2;
            return this;
        }

        public void run() {
            if (this.context != null) {
                NetworkUtil.getNetworkStatus(this.context);
                NetworkOperatorUtil.updateNetworkOperatorName(this.context);
                UTMCDevice.updateUTMCDeviceNetworkStatus(this.context);
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x009c, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0099 A[Catch:{ Exception -> 0x008b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void getNetworkStatus(android.content.Context r6) {
        /*
            java.lang.Class<com.alibaba.analytics.core.network.NetworkUtil> r0 = com.alibaba.analytics.core.network.NetworkUtil.class
            monitor-enter(r0)
            r1 = 0
            r2 = 1
            android.content.pm.PackageManager r3 = r6.getPackageManager()     // Catch:{ Exception -> 0x008b }
            java.lang.String r4 = "android.permission.ACCESS_NETWORK_STATE"
            java.lang.String r5 = r6.getPackageName()     // Catch:{ Exception -> 0x008b }
            int r3 = r3.checkPermission(r4, r5)     // Catch:{ Exception -> 0x008b }
            if (r3 == 0) goto L_0x0023
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r1] = r3     // Catch:{ Exception -> 0x008b }
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r2] = r3     // Catch:{ Exception -> 0x008b }
            monitor-exit(r0)
            return
        L_0x0023:
            java.lang.String r3 = "connectivity"
            java.lang.Object r3 = r6.getSystemService(r3)     // Catch:{ Exception -> 0x008b }
            android.net.ConnectivityManager r3 = (android.net.ConnectivityManager) r3     // Catch:{ Exception -> 0x008b }
            if (r3 != 0) goto L_0x003b
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r1] = r3     // Catch:{ Exception -> 0x008b }
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r2] = r3     // Catch:{ Exception -> 0x008b }
            monitor-exit(r0)
            return
        L_0x003b:
            android.net.NetworkInfo r3 = r3.getActiveNetworkInfo()     // Catch:{ Exception -> 0x008b }
            if (r3 == 0) goto L_0x007c
            boolean r4 = r3.isConnected()     // Catch:{ Exception -> 0x008b }
            if (r4 == 0) goto L_0x007c
            int r4 = r3.getType()     // Catch:{ Exception -> 0x008b }
            if (r2 != r4) goto L_0x0067
            java.lang.String[] r3 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r4 = "Wi-Fi"
            r3[r1] = r4     // Catch:{ Exception -> 0x008b }
            boolean r6 = isWifi5G(r6)     // Catch:{ Exception -> 0x008b }
            if (r6 == 0) goto L_0x0060
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "5G"
            r6[r2] = r3     // Catch:{ Exception -> 0x008b }
            goto L_0x0095
        L_0x0060:
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r2] = r3     // Catch:{ Exception -> 0x008b }
            goto L_0x0095
        L_0x0067:
            int r6 = r3.getType()     // Catch:{ Exception -> 0x008b }
            if (r6 != 0) goto L_0x0095
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r4 = "2G/3G"
            r6[r1] = r4     // Catch:{ Exception -> 0x008b }
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = r3.getSubtypeName()     // Catch:{ Exception -> 0x008b }
            r6[r2] = r3     // Catch:{ Exception -> 0x008b }
            goto L_0x0095
        L_0x007c:
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r1] = r3     // Catch:{ Exception -> 0x008b }
            java.lang.String[] r6 = arrayOfString     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "Unknown"
            r6[r2] = r3     // Catch:{ Exception -> 0x008b }
            goto L_0x0095
        L_0x0089:
            r6 = move-exception
            goto L_0x009d
        L_0x008b:
            r6 = move-exception
            java.lang.String r3 = "NetworkUtil"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ all -> 0x0089 }
            r4[r1] = r6     // Catch:{ all -> 0x0089 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ all -> 0x0089 }
        L_0x0095:
            boolean r6 = mHaveNetworkStatus     // Catch:{ all -> 0x0089 }
            if (r6 != 0) goto L_0x009b
            mHaveNetworkStatus = r2     // Catch:{ all -> 0x0089 }
        L_0x009b:
            monitor-exit(r0)
            return
        L_0x009d:
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.network.NetworkUtil.getNetworkStatus(android.content.Context):void");
    }

    private static boolean isWifi5G(Context context) {
        WifiInfo connectionInfo;
        int i = 0;
        if (context == null || (connectionInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo()) == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            i = connectionInfo.getFrequency();
        }
        return is5GHz(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        return b5GSupported;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0031, code lost:
        bCheck5GSupported = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0033, code lost:
        throw r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isDevice5GSupported(android.content.Context r3) {
        /*
            boolean r0 = bCheck5GSupported
            if (r0 == 0) goto L_0x0007
            boolean r3 = b5GSupported
            return r3
        L_0x0007:
            r0 = 1
            android.content.Context r3 = r3.getApplicationContext()     // Catch:{ Throwable -> 0x002c }
            java.lang.String r1 = "wifi"
            java.lang.Object r3 = r3.getSystemService(r1)     // Catch:{ Throwable -> 0x002c }
            android.net.wifi.WifiManager r3 = (android.net.wifi.WifiManager) r3     // Catch:{ Throwable -> 0x002c }
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x002c }
            r2 = 21
            if (r1 < r2) goto L_0x0022
            boolean r3 = r3.is5GHzBandSupported()     // Catch:{ Throwable -> 0x002c }
            b5GSupported = r3     // Catch:{ Throwable -> 0x002c }
            goto L_0x0025
        L_0x0022:
            r3 = 0
            b5GSupported = r3     // Catch:{ Throwable -> 0x002c }
        L_0x0025:
            boolean r3 = b5GSupported     // Catch:{ Throwable -> 0x002c }
            bCheck5GSupported = r0
            return r3
        L_0x002a:
            r3 = move-exception
            goto L_0x0031
        L_0x002c:
            boolean r3 = b5GSupported     // Catch:{ all -> 0x002a }
            bCheck5GSupported = r0
            return r3
        L_0x0031:
            bCheck5GSupported = r0
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.network.NetworkUtil.isDevice5GSupported(android.content.Context):boolean");
    }
}
