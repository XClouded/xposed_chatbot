package com.alimama.moon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkType {
    public static final int TYPE_2G = 1;
    public static final int TYPE_3G = 2;
    public static final int TYPE_4G = 4;
    public static final int TYPE_NOT_CONNECTED = -1;
    private static final int TYPE_UNKNOWN = 11;
    public static final int TYPE_WIFI = 10;

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return -1;
        }
        if (activeNetworkInfo.getType() == 1) {
            return 10;
        }
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 1;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 2;
            case 13:
                return 4;
            default:
                return 11;
        }
    }
}
