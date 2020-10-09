package com.alibaba.aliweex.adapter.module.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.adapter.module.net.IWXConnection;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;

final class DefaultWXConnection implements IWXConnection {
    static final String TAG = "WXConnectionModule";
    private boolean hasRegisteredReceiver = false;
    private ConnectivityManager mConnectivityManager;
    private ConnectivityReceiver mConnectivityReceiver;
    private Context mContext;
    private String mCurConnectedType = "";
    private List<IWXConnection.OnNetworkChangeListener> mListeners;

    DefaultWXConnection(@NonNull Context context) {
        this.mContext = context.getApplicationContext();
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mConnectivityReceiver = new ConnectivityReceiver();
    }

    @NonNull
    public String getType() {
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
            if (type == 7) {
                return IWXConnection.TYPE_BLUETOOTH;
            }
            if (type == 6) {
                return IWXConnection.TYPE_WIMAX;
            }
            if (type == 9) {
                return IWXConnection.TYPE_ETHERNET;
            }
            return type == 0 ? IWXConnection.TYPE_CELLULAR : "other";
        } catch (SecurityException e) {
            WXLogUtils.e(TAG, e.getMessage());
            return "unknown";
        }
    }

    @NonNull
    public String getNetworkType() {
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
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
            return "unknown";
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double getDownlinkMax() {
        /*
            r5 = this;
            java.lang.String r0 = r5.getType()
            int r1 = r0.hashCode()
            switch(r1) {
                case -1419358249: goto L_0x0054;
                case -916596374: goto L_0x004a;
                case -284840886: goto L_0x0040;
                case 3387192: goto L_0x0036;
                case 3649301: goto L_0x002b;
                case 106069776: goto L_0x0021;
                case 113134930: goto L_0x0016;
                case 1968882350: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x005e
        L_0x000c:
            java.lang.String r1 = "bluetooth"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 1
            goto L_0x005f
        L_0x0016:
            java.lang.String r1 = "wimax"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 2
            goto L_0x005f
        L_0x0021:
            java.lang.String r1 = "other"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 5
            goto L_0x005f
        L_0x002b:
            java.lang.String r1 = "wifi"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 0
            goto L_0x005f
        L_0x0036:
            java.lang.String r1 = "none"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 4
            goto L_0x005f
        L_0x0040:
            java.lang.String r1 = "unknown"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 6
            goto L_0x005f
        L_0x004a:
            java.lang.String r1 = "cellular"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 7
            goto L_0x005f
        L_0x0054:
            java.lang.String r1 = "ethernet"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005e
            r0 = 3
            goto L_0x005f
        L_0x005e:
            r0 = -1
        L_0x005f:
            r1 = 0
            r3 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            switch(r0) {
                case 0: goto L_0x00a5;
                case 1: goto L_0x00a2;
                case 2: goto L_0x009c;
                case 3: goto L_0x0096;
                case 4: goto L_0x0095;
                case 5: goto L_0x0094;
                case 6: goto L_0x0093;
                case 7: goto L_0x006a;
                default: goto L_0x0069;
            }
        L_0x0069:
            return r1
        L_0x006a:
            java.lang.String r0 = r5.getNetworkType()
            java.lang.String r1 = "2g"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x007c
            r0 = 4600589148945547002(0x3fd89374bc6a7efa, double:0.384)
            return r0
        L_0x007c:
            java.lang.String r1 = "3g"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x0087
            r0 = 4631107791820423168(0x4045000000000000, double:42.0)
            return r0
        L_0x0087:
            java.lang.String r1 = "4g"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0092
            r0 = 4636737291354636288(0x4059000000000000, double:100.0)
            return r0
        L_0x0092:
            return r3
        L_0x0093:
            return r3
        L_0x0094:
            return r3
        L_0x0095:
            return r1
        L_0x0096:
            r0 = 4666723172467343360(0x40c3880000000000, double:10000.0)
            return r0
        L_0x009c:
            r0 = 4645128764097822720(0x4076d00000000000, double:365.0)
            return r0
        L_0x00a2:
            r0 = 4627448617123184640(0x4038000000000000, double:24.0)
            return r0
        L_0x00a5:
            r0 = 4664418596095524864(0x40bb580000000000, double:7000.0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.module.net.DefaultWXConnection.getDownlinkMax():double");
    }

    public void addNetworkChangeListener(@Nullable IWXConnection.OnNetworkChangeListener onNetworkChangeListener) {
        if (onNetworkChangeListener != null) {
            if (this.mListeners == null) {
                this.mListeners = new ArrayList(4);
            }
            this.mListeners.add(onNetworkChangeListener);
            if (!this.hasRegisteredReceiver) {
                this.hasRegisteredReceiver = true;
                registerReceiver();
            }
        }
    }

    public void destroy() {
        if (this.hasRegisteredReceiver) {
            unregisterReceiver();
        }
        if (this.mListeners != null) {
            this.mListeners.clear();
            this.mListeners = null;
        }
        this.hasRegisteredReceiver = false;
    }

    /* access modifiers changed from: private */
    public void notifyOnNetworkChange() {
        if (this.mListeners != null && !this.mListeners.isEmpty()) {
            String type = getType();
            if (type.equals(IWXConnection.TYPE_CELLULAR)) {
                type = getNetworkType();
            }
            if (!type.equalsIgnoreCase(this.mCurConnectedType)) {
                this.mCurConnectedType = type;
                WXLogUtils.d(TAG, "network type changed to " + this.mCurConnectedType);
                for (IWXConnection.OnNetworkChangeListener onNetworkChange : this.mListeners) {
                    onNetworkChange.onNetworkChange();
                }
            }
        }
    }

    private void registerReceiver() {
        if (this.mContext != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.mContext.registerReceiver(this.mConnectivityReceiver, intentFilter);
        }
    }

    private void unregisterReceiver() {
        if (this.mContext != null) {
            try {
                this.mContext.unregisterReceiver(this.mConnectivityReceiver);
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    private class ConnectivityReceiver extends BroadcastReceiver {
        private ConnectivityReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                try {
                    DefaultWXConnection.this.notifyOnNetworkChange();
                } catch (Exception e) {
                    WXLogUtils.e(DefaultWXConnection.TAG, e.getMessage());
                }
            }
        }
    }
}
