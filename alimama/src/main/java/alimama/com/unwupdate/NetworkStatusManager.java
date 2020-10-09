package alimama.com.unwupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStatusManager {
    private static final boolean DBG = true;
    public static final int NETWORK_CLASS_2G = 1;
    public static final String NETWORK_CLASS_2G_NAME = "2G";
    public static final int NETWORK_CLASS_3G = 2;
    public static final String NETWORK_CLASS_3G_NAME = "3G";
    public static final int NETWORK_CLASS_4G = 3;
    public static final String NETWORK_CLASS_4G_NAME = "4G";
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final String NETWORK_CLASS_UNKNOWN_NAME = "UNKNOWN";
    public static final int NETWORK_CLASS_WIFI = 999;
    public static final String NETWORK_CLASS_WIFI_NAME = "WIFI";
    private static final String TAG = "NetworkStatusManager";
    private static NetworkStatusManager sInstance;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public boolean mIsFailOver;
    /* access modifiers changed from: private */
    public boolean mIsWifi = false;
    /* access modifiers changed from: private */
    public boolean mListening;
    /* access modifiers changed from: private */
    public NetworkInfo mNetworkInfo;
    /* access modifiers changed from: private */
    public NetworkInfo mOtherNetworkInfo;
    /* access modifiers changed from: private */
    public String mReason;
    private ConnectivityBroadcastReceiver mReceiver = new ConnectivityBroadcastReceiver();
    /* access modifiers changed from: private */
    public State mState = State.UNKNOWN;

    public enum State {
        UNKNOWN,
        CONNECTED,
        NOT_CONNECTED
    }

    private static int getMobileNetworkClass(int i) {
        switch (i) {
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
                return 3;
            default:
                return 0;
        }
    }

    private NetworkStatusManager() {
    }

    public static void init(Context context) {
        sInstance = new NetworkStatusManager();
        sInstance.mIsWifi = checkIsWifi(context);
        sInstance.startListening(context);
    }

    public static NetworkStatusManager getInstance() {
        return sInstance;
    }

    public static boolean checkIsWifi(Context context) {
        NetworkInfo networkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (networkInfo = connectivityManager.getNetworkInfo(1)) == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public int getNetworkType() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo == null) {
            return 0;
        }
        if (networkInfo.getType() == 1) {
            return 999;
        }
        if (networkInfo.getType() == 0) {
            return getMobileNetworkClass(networkInfo.getSubtype());
        }
        return 0;
    }

    public synchronized void startListening(Context context) {
        if (!this.mListening) {
            this.mContext = context;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            context.registerReceiver(this.mReceiver, intentFilter);
            this.mListening = true;
        }
    }

    public synchronized void stopListening() {
        if (this.mListening) {
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mContext = null;
            this.mNetworkInfo = null;
            this.mOtherNetworkInfo = null;
            this.mIsFailOver = false;
            this.mReason = null;
            this.mListening = false;
        }
    }

    public NetworkInfo getNetworkInfo() {
        return this.mNetworkInfo;
    }

    public NetworkInfo getOtherNetworkInfo() {
        return this.mOtherNetworkInfo;
    }

    public boolean isFailover() {
        return this.mIsFailOver;
    }

    public String getReason() {
        return this.mReason;
    }

    public boolean isWifi() {
        return this.mIsWifi;
    }

    public String getNetworkTypeName() {
        int networkType = getNetworkType();
        if (networkType == 999) {
            return "WIFI";
        }
        switch (networkType) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "2G";
            case 2:
                return "3G";
            case 3:
                return "4G";
            default:
                return "UNKNOWN";
        }
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        private ConnectivityBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String str;
            if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") || !NetworkStatusManager.this.mListening) {
                Log.w(NetworkStatusManager.TAG, "onReceived() called with " + NetworkStatusManager.this.mState.toString() + " and " + intent);
                return;
            }
            boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
            if (booleanExtra) {
                State unused = NetworkStatusManager.this.mState = State.NOT_CONNECTED;
            } else {
                State unused2 = NetworkStatusManager.this.mState = State.CONNECTED;
            }
            NetworkInfo unused3 = NetworkStatusManager.this.mNetworkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            NetworkInfo unused4 = NetworkStatusManager.this.mOtherNetworkInfo = (NetworkInfo) intent.getParcelableExtra("otherNetwork");
            String unused5 = NetworkStatusManager.this.mReason = intent.getStringExtra("reason");
            boolean unused6 = NetworkStatusManager.this.mIsFailOver = intent.getBooleanExtra("isFailover", false);
            StringBuilder sb = new StringBuilder();
            sb.append("onReceive(): mNetworkInfo=");
            sb.append(NetworkStatusManager.this.mNetworkInfo);
            sb.append(" mOtherNetworkInfo = ");
            if (NetworkStatusManager.this.mOtherNetworkInfo == null) {
                str = "[none]";
            } else {
                str = NetworkStatusManager.this.mOtherNetworkInfo + " noConn=" + booleanExtra;
            }
            sb.append(str);
            sb.append(" mState=");
            sb.append(NetworkStatusManager.this.mState.toString());
            Log.d(NetworkStatusManager.TAG, sb.toString());
            boolean unused7 = NetworkStatusManager.this.mIsWifi = NetworkStatusManager.checkIsWifi(NetworkStatusManager.this.mContext);
        }
    }
}
