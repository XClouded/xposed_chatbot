package anet.channel.status;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ProxySetting;
import anet.channel.util.StringUtils;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class NetworkStatusHelper {
    private static final String TAG = "awcn.NetworkStatusHelper";
    static CopyOnWriteArraySet<INetworkStatusChangeListener> listeners = new CopyOnWriteArraySet<>();

    public interface INetworkStatusChangeListener {
        void onNetworkStatusChanged(NetworkStatus networkStatus);
    }

    public enum NetworkStatus {
        NONE,
        NO,
        G2,
        G3,
        G4,
        WIFI;

        public boolean isMobile() {
            return this == G2 || this == G3 || this == G4;
        }

        public boolean isWifi() {
            return this == WIFI;
        }

        public String getType() {
            if (this == G2) {
                return "2G";
            }
            if (this == G3) {
                return "3G";
            }
            if (this == G4) {
                return "4G";
            }
            return toString();
        }
    }

    public static synchronized void startListener(Context context) {
        synchronized (NetworkStatusHelper.class) {
            if (context != null) {
                NetworkStatusMonitor.context = context;
                NetworkStatusMonitor.registerNetworkReceiver();
                NetworkStatusMonitor.registerNetworkCallback();
            } else {
                throw new NullPointerException("context is null");
            }
        }
    }

    public void stopListener(Context context) {
        NetworkStatusMonitor.unregisterNetworkReceiver();
    }

    public static void addStatusChangeListener(INetworkStatusChangeListener iNetworkStatusChangeListener) {
        listeners.add(iNetworkStatusChangeListener);
    }

    public static void removeStatusChangeListener(INetworkStatusChangeListener iNetworkStatusChangeListener) {
        listeners.remove(iNetworkStatusChangeListener);
    }

    static void notifyStatusChanged(final NetworkStatus networkStatus) {
        ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
            public void run() {
                try {
                    Iterator<INetworkStatusChangeListener> it = NetworkStatusHelper.listeners.iterator();
                    while (it.hasNext()) {
                        INetworkStatusChangeListener next = it.next();
                        long currentTimeMillis = System.currentTimeMillis();
                        next.onNetworkStatusChanged(networkStatus);
                        if (System.currentTimeMillis() - currentTimeMillis > 500) {
                            ALog.e(NetworkStatusHelper.TAG, "call back cost too much time", (String) null, "listener", next);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        });
    }

    public static NetworkStatus getStatus() {
        return NetworkStatusMonitor.status;
    }

    public static String getNetworkSubType() {
        return NetworkStatusMonitor.subType;
    }

    public static String getApn() {
        return NetworkStatusMonitor.apn;
    }

    public static String getCarrier() {
        return NetworkStatusMonitor.carrier;
    }

    public static String getSimOp() {
        return NetworkStatusMonitor.simOp;
    }

    public static boolean isRoaming() {
        return NetworkStatusMonitor.isRoaming;
    }

    public static String getWifiBSSID() {
        return NetworkStatusMonitor.bssid;
    }

    public static String getWifiSSID() {
        return NetworkStatusMonitor.ssid;
    }

    public static String getDnsServerAddress() {
        if (!NetworkStatusMonitor.dnsServers.isEmpty()) {
            return NetworkStatusMonitor.dnsServers.get(0).getHostAddress();
        }
        return NetworkStatusMonitor.getDnsServerFromSystemProperties();
    }

    public static boolean isConnected() {
        if (Build.VERSION.SDK_INT >= 24) {
            if (NetworkStatusMonitor.isNetworkAvailable) {
                return true;
            }
        } else if (NetworkStatusMonitor.status != NetworkStatus.NO) {
            return true;
        }
        try {
            NetworkInfo networkInfo = NetworkStatusMonitor.getNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return true;
        }
    }

    public static boolean isProxy() {
        NetworkStatus networkStatus = NetworkStatusMonitor.status;
        String str = NetworkStatusMonitor.apn;
        if (networkStatus == NetworkStatus.WIFI && getWifiProxy() != null) {
            return true;
        }
        if (networkStatus.isMobile()) {
            return str.contains("wap") || ProxySetting.get() != null;
        }
        return false;
    }

    public static String getProxyType() {
        NetworkStatus networkStatus = NetworkStatusMonitor.status;
        if (networkStatus == NetworkStatus.WIFI && getWifiProxy() != null) {
            return "proxy";
        }
        if (!networkStatus.isMobile() || !NetworkStatusMonitor.apn.contains("wap")) {
            return (!networkStatus.isMobile() || ProxySetting.get() == null) ? "" : "auth";
        }
        return "wap";
    }

    public static Pair<String, Integer> getWifiProxy() {
        if (NetworkStatusMonitor.status != NetworkStatus.WIFI) {
            return null;
        }
        return NetworkStatusMonitor.proxy;
    }

    public static void printNetworkDetail() {
        try {
            NetworkStatus status = getStatus();
            StringBuilder sb = new StringBuilder(128);
            sb.append("\nNetwork detail*******************************\n");
            sb.append("Status: ");
            sb.append(status.getType());
            sb.append(10);
            sb.append("Subtype: ");
            sb.append(getNetworkSubType());
            sb.append(10);
            if (status != NetworkStatus.NO) {
                if (status.isMobile()) {
                    sb.append("Apn: ");
                    sb.append(getApn());
                    sb.append(10);
                    sb.append("Carrier: ");
                    sb.append(getCarrier());
                    sb.append(10);
                } else {
                    sb.append("BSSID: ");
                    sb.append(getWifiBSSID());
                    sb.append(10);
                    sb.append("SSID: ");
                    sb.append(getWifiSSID());
                    sb.append(10);
                }
            }
            if (isProxy()) {
                sb.append("Proxy: ");
                sb.append(getProxyType());
                sb.append(10);
                Pair<String, Integer> wifiProxy = getWifiProxy();
                if (wifiProxy != null) {
                    sb.append("ProxyHost: ");
                    sb.append((String) wifiProxy.first);
                    sb.append(10);
                    sb.append("ProxyPort: ");
                    sb.append(wifiProxy.second);
                    sb.append(10);
                }
            }
            sb.append("*********************************************");
            ALog.i(TAG, sb.toString(), (String) null, new Object[0]);
        } catch (Exception unused) {
        }
    }

    public static String getUniqueId(NetworkStatus networkStatus) {
        if (networkStatus.isWifi()) {
            String md5ToHex = StringUtils.md5ToHex(getWifiBSSID());
            if (TextUtils.isEmpty(md5ToHex)) {
                md5ToHex = "";
            }
            return "WIFI$" + md5ToHex;
        } else if (!networkStatus.isMobile()) {
            return "";
        } else {
            return networkStatus.getType() + "$" + getApn();
        }
    }
}
