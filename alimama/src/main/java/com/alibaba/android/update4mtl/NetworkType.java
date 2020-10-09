package com.alibaba.android.update4mtl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public enum NetworkType {
    _2G(1),
    _3G(2),
    _4G(4),
    WIFI(10),
    UNKONW(0),
    NOT_CONNECTED(20),
    NO_NETWORK(30);
    
    private int mValue;

    private NetworkType(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public String toString() {
        return String.valueOf(this.mValue);
    }

    public static NetworkType getCurrentNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            int type = activeNetworkInfo.getType();
            if (type == 0) {
                try {
                    switch (activeNetworkInfo.getSubtype()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            return _2G;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                        case 17:
                        case 18:
                            return _3G;
                        case 13:
                            return _4G;
                        default:
                            return UNKONW;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return UNKONW;
                }
            } else if (type == 1) {
                return WIFI;
            } else {
                return UNKONW;
            }
        } else if (activeNetworkInfo != null && !activeNetworkInfo.isConnected()) {
            return NOT_CONNECTED;
        } else {
            if (activeNetworkInfo == null) {
                return NO_NETWORK;
            }
            return UNKONW;
        }
    }
}
