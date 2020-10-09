package com.ta.audid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.alibaba.analytics.core.network.NetworkUtil;

public class NetworkInfoUtils {
    public static final String NETWORK_CLASS_2_G = "2G";
    public static final String NETWORK_CLASS_3_G = "3G";
    public static final String NETWORK_CLASS_4_G = "4G";
    public static final String NETWORK_CLASS_UNKNOWN = "Unknown";
    public static final String NETWORK_CLASS_WIFI = "Wi-Fi";
    private static String[] arrayOfString = {"Unknown", "Unknown"};

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

    public static String getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo;
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
        } catch (Exception e) {
            UtdidLogger.e("", e, new Object[0]);
            return true;
        }
    }

    private static String[] getNetworkState(Context context) {
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            arrayOfString[0] = "Unknown";
            arrayOfString[1] = "Unknown";
        } else if (1 == activeNetworkInfo.getType()) {
            arrayOfString[0] = "Wi-Fi";
        } else if (activeNetworkInfo.getType() == 0) {
            arrayOfString[0] = NetworkUtil.NETWORK_CLASS_2_3_G;
            arrayOfString[1] = activeNetworkInfo.getSubtypeName();
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
            return networkState[0].equals(NetworkUtil.NETWORK_CLASS_2_3_G) ? networkState[1] : "Unknown";
        } catch (Exception unused) {
            return "Unknown";
        }
    }

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

    private static String convertIntToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static boolean isWifi(Context context) {
        if (context != null) {
            try {
                if (getNetworkState(context)[0].equals("Wi-Fi")) {
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }
}
