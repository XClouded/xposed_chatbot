package com.alibaba.android.update;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkInfo {

    public enum NetWorkType {
        NETWORK_TYPE_WIFI("wifi"),
        NETWORK_TYPE_MOBILE("2g/3g"),
        NETWORK_TYPE_NONE("");
        
        private String value;

        private NetWorkType(String str) {
            this.value = str;
        }

        public String value() {
            return this.value;
        }
    }

    public static NetWorkType getCurrentNetType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        android.net.NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        android.net.NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return NetWorkType.NETWORK_TYPE_WIFI;
        }
        if (networkInfo2 == null || networkInfo2.getState() != NetworkInfo.State.CONNECTED) {
            return NetWorkType.NETWORK_TYPE_NONE;
        }
        return NetWorkType.NETWORK_TYPE_MOBILE;
    }
}
