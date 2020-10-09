package com.alimamaunion.support.debugimpl;

import android.app.Application;
import android.content.Context;

public class DeviceInfoUtils {
    private static final int NETTYPE_CMNET = 3;
    private static final int NETTYPE_CMWAP = 2;
    private static final int NETTYPE_WIFI = 1;

    public static String getDateAndTime(Context context) {
        return "";
    }

    public static String getDeviceId(Context context) {
        return "";
    }

    public static String getLanguage(Context context) {
        return "";
    }

    public static String getLine1Number(Context context) {
        return "";
    }

    public static String getMetrics(Context context) {
        return "";
    }

    public static String getNetworkCountryIso(Context context) {
        return "";
    }

    public static String getNetworkOperator(Context context) {
        return "";
    }

    public static String getNetworkOperatorName(Context context) {
        return "";
    }

    public static int getNetworkType(Context context) {
        return 0;
    }

    public static String getPhoneModel(Context context) {
        return "";
    }

    public static String getPhoneProduct(Context context) {
        return "";
    }

    public static String getSimSerialNumber(Context context) {
        return "";
    }

    public static String getSubscriberId(Context context) {
        return "";
    }

    public static String getTimeZone(Context context) {
        return "";
    }

    public static String getUtdid(Application application) {
        return "";
    }

    public static boolean isNetworkAvailable(Context context) {
        return false;
    }

    public static String getNetworkTypeStr(Context context) {
        switch (getNetworkType(context)) {
            case 0:
                return "没有网络";
            case 1:
                return "WIFI网络";
            case 2:
                return "WAP网络";
            case 3:
                return " 3：NET网络";
            default:
                return "";
        }
    }
}
