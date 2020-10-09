package com.alibaba.android.prefetchx.core.file;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;

final class DefaultConnectionImpl implements IConnection {
    private ConnectivityManager mConnectivityManager;

    private DefaultConnectionImpl(@NonNull Context context) {
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    static IConnection newInstance(@NonNull Context context) {
        return new DefaultConnectionImpl(context);
    }

    @NonNull
    public String getConnectionType() {
        try {
            NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                return "none";
            }
            if (!activeNetworkInfo.isAvailable()) {
                return "none";
            }
            int type = activeNetworkInfo.getType();
            if (!ConnectivityManager.isNetworkTypeValid(type)) {
                return "unknown";
            }
            if (type == 1) {
                return "wifi";
            }
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return "2g";
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return "3g";
                case 13:
                    return "4g";
                default:
                    return "other";
            }
        } catch (SecurityException e) {
            PFLog.File.w("error in getConnectionType. ", e);
            PFMonitor.File.fail(PFConstant.File.PF_FILE_GET_CONNENCT_TYPE, "error in getConnectionType", e.getMessage());
            return "unknown";
        }
    }
}
