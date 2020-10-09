package com.ali.user.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.taobao.windvane.util.NetWork;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.TLogAdapter;

public class NetworkUtil {
    public static final String Tag = "login.Network";

    public static boolean isNetworkConnected() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) DataProviderFactory.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static String getNetworkType(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                TLogAdapter.w(Tag, "can not get Context.CONNECTIVITY_SERVICE");
                return "none";
            }
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            if (networkInfo != null) {
                if (NetworkInfo.State.CONNECTED == networkInfo.getState()) {
                    return "wifi";
                }
            } else {
                TLogAdapter.w(Tag, "can not get ConnectivityManager.TYPE_WIFI");
            }
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
            if (networkInfo2 != null) {
                if (NetworkInfo.State.CONNECTED == networkInfo2.getState()) {
                    return NetWork.CONN_TYPE_GPRS;
                }
                return "none";
            }
            TLogAdapter.w(Tag, "can not get ConnectivityManager.TYPE_MOBILE");
            return "none";
        } catch (Exception e) {
            e.printStackTrace();
            return "none";
        }
    }
}
