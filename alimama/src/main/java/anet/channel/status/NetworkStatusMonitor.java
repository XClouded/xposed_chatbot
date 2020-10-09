package anet.channel.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.Inet64Util;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class NetworkStatusMonitor {
    private static final String TAG = "awcn.NetworkStatusMonitor";
    static volatile String apn = "";
    static volatile String bssid = "";
    static volatile String carrier = "unknown";
    static volatile Context context = null;
    private static String[] dftDnsNames = {"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
    static volatile List<InetAddress> dnsServers = Collections.EMPTY_LIST;
    private static Method getSubInfoMethod;
    static volatile boolean isNetworkAvailable = false;
    private static volatile boolean isRegistered = false;
    static volatile boolean isRoaming = false;
    private static ConnectivityManager mConnectivityManager = null;
    private static TelephonyManager mTelephonyManager = null;
    private static WifiManager mWifiManager = null;
    static volatile Pair<String, Integer> proxy = null;
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (ALog.isPrintLog(1)) {
                ALog.d(NetworkStatusMonitor.TAG, "receiver:" + intent.getAction(), (String) null, new Object[0]);
            }
            ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                public void run() {
                    NetworkStatusMonitor.checkNetworkStatus();
                }
            });
        }
    };
    static volatile String simOp = "";
    static volatile String ssid = "";
    static volatile NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.NetworkStatus.NONE;
    static volatile String subType = "unknown";
    private static SubscriptionManager subscriptionManager = null;

    NetworkStatusMonitor() {
    }

    static void registerNetworkReceiver() {
        if (!isRegistered && context != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            try {
                context.registerReceiver(receiver, intentFilter);
            } catch (Exception unused) {
                ALog.e(TAG, "registerReceiver failed", (String) null, new Object[0]);
            }
            checkNetworkStatus();
            isRegistered = true;
        }
    }

    static void unregisterNetworkReceiver() {
        if (context != null) {
            context.unregisterReceiver(receiver);
        }
    }

    static void registerNetworkCallback() {
        if (Build.VERSION.SDK_INT >= 24) {
            NetworkInfo networkInfo = getNetworkInfo();
            isNetworkAvailable = networkInfo != null && networkInfo.isConnected();
            mConnectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties);
                    NetworkStatusMonitor.dnsServers = new ArrayList(linkProperties.getDnsServers());
                }

                public void onLost(Network network) {
                    super.onLost(network);
                    ALog.i(NetworkStatusMonitor.TAG, "network onLost", (String) null, new Object[0]);
                    NetworkStatusMonitor.isNetworkAvailable = false;
                }

                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    ALog.i(NetworkStatusMonitor.TAG, "network onAvailable", (String) null, new Object[0]);
                    NetworkStatusMonitor.isNetworkAvailable = true;
                }
            });
        }
    }

    static void checkNetworkStatus() {
        NetworkInfo networkInfo;
        boolean z;
        ALog.d(TAG, "[checkNetworkStatus]", (String) null, new Object[0]);
        NetworkStatusHelper.NetworkStatus networkStatus = status;
        String str = apn;
        String str2 = ssid;
        try {
            networkInfo = getNetworkInfo();
            z = false;
        } catch (Exception e) {
            try {
                ALog.e(TAG, "getNetworkInfo exception", (String) null, e, new Object[0]);
                resetStatus(NetworkStatusHelper.NetworkStatus.NONE, "unknown");
                networkInfo = null;
                z = true;
            } catch (Exception e2) {
                ALog.e(TAG, "checkNetworkStatus", (String) null, e2, new Object[0]);
                return;
            }
        }
        if (!z) {
            if (networkInfo != null) {
                if (networkInfo.isConnected()) {
                    ALog.i(TAG, "checkNetworkStatus", (String) null, "info.isConnected", Boolean.valueOf(networkInfo.isConnected()), "info.isAvailable", Boolean.valueOf(networkInfo.isAvailable()), "info.getType", Integer.valueOf(networkInfo.getType()));
                    if (networkInfo.getType() == 0) {
                        String subtypeName = networkInfo.getSubtypeName();
                        String replace = !TextUtils.isEmpty(subtypeName) ? subtypeName.replace(Operators.SPACE_STR, "") : "";
                        resetStatus(parseNetworkStatus(networkInfo.getSubtype(), replace), replace);
                        apn = parseExtraInfo(networkInfo.getExtraInfo());
                        parseCarrierInfo();
                    } else if (networkInfo.getType() == 1) {
                        resetStatus(NetworkStatusHelper.NetworkStatus.WIFI, "wifi");
                        WifiInfo wifiInfo = getWifiInfo();
                        if (wifiInfo != null) {
                            bssid = wifiInfo.getBSSID();
                            ssid = wifiInfo.getSSID();
                        }
                        carrier = "wifi";
                        simOp = "wifi";
                        proxy = parseWifiProxy();
                    } else {
                        resetStatus(NetworkStatusHelper.NetworkStatus.NONE, "unknown");
                    }
                    isRoaming = networkInfo.isRoaming();
                    Inet64Util.startIpStackDetect();
                }
            }
            resetStatus(NetworkStatusHelper.NetworkStatus.NO, "no network");
            ALog.i(TAG, "checkNetworkStatus", (String) null, "no network");
        }
        if (status != networkStatus || !apn.equalsIgnoreCase(str) || !ssid.equalsIgnoreCase(str2)) {
            if (ALog.isPrintLog(2)) {
                NetworkStatusHelper.printNetworkDetail();
            }
            NetworkStatusHelper.notifyStatusChanged(status);
        }
    }

    private static void resetStatus(NetworkStatusHelper.NetworkStatus networkStatus, String str) {
        status = networkStatus;
        subType = str;
        apn = "";
        ssid = "";
        bssid = "";
        proxy = null;
        carrier = "";
        simOp = "";
    }

    private static NetworkStatusHelper.NetworkStatus parseNetworkStatus(int i, String str) {
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return NetworkStatusHelper.NetworkStatus.G2;
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
                return NetworkStatusHelper.NetworkStatus.G3;
            case 13:
            case 18:
            case 19:
                return NetworkStatusHelper.NetworkStatus.G4;
            default:
                if (str.equalsIgnoreCase("TD-SCDMA") || str.equalsIgnoreCase("WCDMA") || str.equalsIgnoreCase("CDMA2000")) {
                    return NetworkStatusHelper.NetworkStatus.G3;
                }
                return NetworkStatusHelper.NetworkStatus.NONE;
        }
    }

    private static String parseExtraInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return "unknown";
        }
        String lowerCase = str.toLowerCase(Locale.US);
        if (lowerCase.contains("cmwap")) {
            return "cmwap";
        }
        if (lowerCase.contains("uniwap")) {
            return "uniwap";
        }
        if (lowerCase.contains("3gwap")) {
            return "3gwap";
        }
        if (lowerCase.contains("ctwap")) {
            return "ctwap";
        }
        if (lowerCase.contains("cmnet")) {
            return "cmnet";
        }
        if (lowerCase.contains("uninet")) {
            return "uninet";
        }
        if (lowerCase.contains("3gnet")) {
            return "3gnet";
        }
        return lowerCase.contains("ctnet") ? "ctnet" : "unknown";
    }

    private static void parseCarrierInfo() {
        try {
            if (mTelephonyManager == null) {
                mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
            }
            simOp = mTelephonyManager.getSimOperator();
            if (Build.VERSION.SDK_INT >= 22) {
                if (subscriptionManager == null) {
                    subscriptionManager = SubscriptionManager.from(context);
                    getSubInfoMethod = subscriptionManager.getClass().getDeclaredMethod("getDefaultDataSubscriptionInfo", new Class[0]);
                }
                if (getSubInfoMethod != null) {
                    carrier = ((SubscriptionInfo) getSubInfoMethod.invoke(subscriptionManager, new Object[0])).getCarrierName().toString();
                }
            }
        } catch (Exception unused) {
        }
    }

    static NetworkInfo getNetworkInfo() {
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        }
        return mConnectivityManager.getActiveNetworkInfo();
    }

    private static WifiInfo getWifiInfo() {
        try {
            if (mWifiManager == null) {
                mWifiManager = (WifiManager) context.getSystemService("wifi");
            }
            return mWifiManager.getConnectionInfo();
        } catch (Throwable th) {
            ALog.e(TAG, "getWifiInfo", (String) null, th, new Object[0]);
            return null;
        }
    }

    private static Pair<String, Integer> parseWifiProxy() {
        try {
            String property = System.getProperty("http.proxyHost");
            if (!TextUtils.isEmpty(property)) {
                return Pair.create(property, Integer.valueOf(Integer.parseInt(System.getProperty("http.proxyPort"))));
            }
            return null;
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    static String getDnsServerFromSystemProperties() {
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            String[] strArr = dftDnsNames;
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                String str = (String) method.invoke((Object) null, new Object[]{strArr[i]});
                if (!TextUtils.isEmpty(str)) {
                    return str;
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }
}
