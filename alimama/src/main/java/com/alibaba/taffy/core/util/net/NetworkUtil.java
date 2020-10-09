package com.alibaba.taffy.core.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.alibaba.taffy.core.util.lang.StringUtil;

public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    public static SimpleNetworkType getSimpleNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        if (activeNetworkInfo == null) {
            return SimpleNetworkType.NONE;
        }
        if (activeNetworkInfo.isConnected()) {
            if (1 == activeNetworkInfo.getType()) {
                return SimpleNetworkType.WIFI;
            }
            if (activeNetworkInfo.getType() == 0) {
                switch (activeNetworkInfo.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return SimpleNetworkType.MOBILE_2G;
                    case 3:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return SimpleNetworkType.MOBILE_3G;
                    case 13:
                        return SimpleNetworkType.MOBILE_4G;
                    default:
                        return SimpleNetworkType.UNKNOWN;
                }
            }
        }
        return SimpleNetworkType.UNKNOWN;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        if (activeNetworkInfo == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }

    public static NetworkInfo[] getAllNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            return connectivityManager.getAllNetworkInfo();
        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo = getAllNetworkInfo(context);
        if (allNetworkInfo == null) {
            return false;
        }
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo != null && (networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getState() == NetworkInfo.State.CONNECTING)) {
                return true;
            }
        }
        return false;
    }

    public static HttpHostInfo getHttpsProxyInfo() {
        if (Build.VERSION.SDK_INT < 11) {
            String defaultHost = Proxy.getDefaultHost();
            int defaultPort = Proxy.getDefaultPort();
            if (defaultHost != null) {
                return new HttpHostInfo(defaultHost, defaultPort);
            }
            return null;
        }
        String property = System.getProperty("https.proxyHost");
        String property2 = System.getProperty("https.proxyPort");
        if (!StringUtil.isEmpty(property)) {
            return new HttpHostInfo(property, Integer.parseInt(property2));
        }
        return null;
    }

    public static HttpHostInfo getHttpProxyInfo() {
        if (Build.VERSION.SDK_INT < 11) {
            String defaultHost = Proxy.getDefaultHost();
            int defaultPort = Proxy.getDefaultPort();
            if (defaultHost != null) {
                return new HttpHostInfo(defaultHost, defaultPort);
            }
            return null;
        }
        try {
            int parseInt = Integer.parseInt(System.getProperty("http.proxyPort"));
            String property = System.getProperty("http.proxyHost");
            if (StringUtil.isNotEmpty(property)) {
                return new HttpHostInfo(property, parseInt);
            }
            return null;
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null) {
            return false;
        }
        switch (telephonyManager.getNetworkType()) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return false;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
                return true;
            default:
                return false;
        }
    }
}
