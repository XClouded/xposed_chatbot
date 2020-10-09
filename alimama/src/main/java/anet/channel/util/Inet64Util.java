package anet.channel.util;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.NetTypeStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Inet64Util {
    static final String IPV4ONLY_HOST = "ipv4only.arpa";
    static final byte[][] IPV4ONLY_IP = {new byte[]{-64, 0, 0, -86}, new byte[]{-64, 0, 0, -85}};
    public static final int IPV4_ONLY = 1;
    public static final int IPV6_ONLY = 2;
    public static final int IP_DUAL_STACK = 3;
    public static final int IP_STACK_UNKNOWN = 0;
    static final String TAG = "awcn.Inet64Util";
    static Nat64Prefix defaultNatPrefix;
    static ConcurrentHashMap<String, Integer> ipStackMap = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, Nat64Prefix> nat64PrefixMap = new ConcurrentHashMap<>();
    static volatile String networkId;

    public static boolean isIPv6OnlyNetwork() {
        return false;
    }

    static {
        networkId = null;
        defaultNatPrefix = null;
        try {
            defaultNatPrefix = new Nat64Prefix((Inet6Address) InetAddress.getAllByName("64:ff9b::")[0], 96);
            networkId = generateNetworkId(NetworkStatusHelper.getStatus());
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public static String generateNetworkId(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (networkStatus.isWifi()) {
            String wifiBSSID = NetworkStatusHelper.getWifiBSSID();
            if (TextUtils.isEmpty(wifiBSSID)) {
                wifiBSSID = "";
            }
            return "WIFI$" + wifiBSSID;
        } else if (!networkStatus.isMobile()) {
            return "UnknownNetwork";
        } else {
            return networkStatus.getType() + "$" + NetworkStatusHelper.getApn();
        }
    }

    public static boolean isIPv4OnlyNetwork() {
        Integer num = ipStackMap.get(networkId);
        if (num == null || num.intValue() != 1) {
            return false;
        }
        return true;
    }

    public static int getStackType() {
        Integer num = ipStackMap.get(networkId);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public static Nat64Prefix getNat64Prefix() {
        Nat64Prefix nat64Prefix = nat64PrefixMap.get(networkId);
        return nat64Prefix == null ? defaultNatPrefix : nat64Prefix;
    }

    public static String convertToIPv6(Inet4Address inet4Address) throws Exception {
        if (inet4Address != null) {
            Nat64Prefix nat64Prefix = getNat64Prefix();
            if (nat64Prefix != null) {
                byte[] address = inet4Address.getAddress();
                byte[] address2 = nat64Prefix.mPrefix.getAddress();
                int i = nat64Prefix.mPrefixLength / 8;
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    int i4 = i2 + i;
                    if (i4 <= 15 && i3 < 4) {
                        if (i4 != 8) {
                            address2[i4] = (byte) (address[i3] | address2[i4]);
                            i3++;
                        }
                        i2++;
                    }
                }
                return InetAddress.getByAddress(address2).getHostAddress();
            }
            throw new Exception("cannot get nat64 prefix");
        }
        throw new InvalidParameterException("address in null");
    }

    public static String convertToIPv6ThrowsException(String str) throws Exception {
        return convertToIPv6((Inet4Address) Inet4Address.getByName(str));
    }

    public static String convertToIPv6(String str) {
        try {
            return convertToIPv6((Inet4Address) Inet4Address.getByName(str));
        } catch (Exception unused) {
            return null;
        }
    }

    private static int getIpStack() throws SocketException {
        String str;
        int i;
        TreeMap treeMap = new TreeMap();
        Iterator<T> it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        while (true) {
            str = null;
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            NetworkInterface networkInterface = (NetworkInterface) it.next();
            if (!networkInterface.getInterfaceAddresses().isEmpty()) {
                String displayName = networkInterface.getDisplayName();
                ALog.i(TAG, "find NetworkInterface:" + displayName, (String) null, new Object[0]);
                int i2 = 0;
                for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                    InetAddress address2 = address.getAddress();
                    if (address2 instanceof Inet6Address) {
                        Inet6Address inet6Address = (Inet6Address) address2;
                        if (!filterAddress(inet6Address)) {
                            ALog.e(TAG, "Found IPv6 address:" + inet6Address.toString(), (String) null, new Object[0]);
                            i2 |= 2;
                        }
                    } else if (address2 instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address2;
                        if (!filterAddress(inet4Address) && !inet4Address.getHostAddress().startsWith("192.168.43.")) {
                            ALog.e(TAG, "Found IPv4 address:" + inet4Address.toString(), (String) null, new Object[0]);
                            i2 |= 1;
                        }
                    }
                }
                if (i2 != 0) {
                    treeMap.put(displayName.toLowerCase(), Integer.valueOf(i2));
                }
            }
        }
        if (treeMap.isEmpty()) {
            return 0;
        }
        if (treeMap.size() == 1) {
            return ((Integer) treeMap.firstEntry().getValue()).intValue();
        }
        if (NetworkStatusHelper.getStatus().isWifi()) {
            str = "wlan";
        } else if (NetworkStatusHelper.getStatus().isMobile()) {
            str = "rmnet";
        }
        if (str != null) {
            Iterator it2 = treeMap.entrySet().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Map.Entry entry = (Map.Entry) it2.next();
                if (((String) entry.getKey()).startsWith(str)) {
                    i = ((Integer) entry.getValue()).intValue();
                    break;
                }
            }
        }
        return (i != 2 || !treeMap.containsKey("v4-wlan0")) ? i : i | ((Integer) treeMap.remove("v4-wlan0")).intValue();
    }

    public static void startIpStackDetect() {
        networkId = generateNetworkId(NetworkStatusHelper.getStatus());
        if (ipStackMap.putIfAbsent(networkId, 0) == null) {
            int detectIpStack = detectIpStack();
            ipStackMap.put(networkId, Integer.valueOf(detectIpStack));
            final NetTypeStat netTypeStat = new NetTypeStat();
            netTypeStat.ipStackType = detectIpStack;
            final String str = networkId;
            if (detectIpStack == 2 || detectIpStack == 3) {
                ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                    public void run() {
                        ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                            public void run() {
                                Nat64Prefix access$200;
                                try {
                                    if (str.equals(Inet64Util.generateNetworkId(NetworkStatusHelper.getStatus()))) {
                                        ALog.e(Inet64Util.TAG, "startIpStackDetect double check", (String) null, new Object[0]);
                                        int access$100 = Inet64Util.detectIpStack();
                                        if (netTypeStat.ipStackType != access$100) {
                                            Inet64Util.ipStackMap.put(str, Integer.valueOf(access$100));
                                            netTypeStat.lastIpStackType = netTypeStat.ipStackType;
                                            netTypeStat.ipStackType = access$100;
                                        }
                                        if ((access$100 == 2 || access$100 == 3) && (access$200 = Inet64Util.detectNat64Prefix()) != null) {
                                            Inet64Util.nat64PrefixMap.put(str, access$200);
                                            netTypeStat.nat64Prefix = access$200.toString();
                                        }
                                        if (GlobalAppRuntimeInfo.isTargetProcess()) {
                                            AppMonitor.getInstance().commitStat(netTypeStat);
                                        }
                                    }
                                } catch (Exception unused) {
                                }
                            }
                        }, ThreadPoolExecutorFactory.Priority.LOW);
                    }
                }, TBToast.Duration.VERY_SHORT, TimeUnit.MILLISECONDS);
            } else if (GlobalAppRuntimeInfo.isTargetProcess()) {
                AppMonitor.getInstance().commitStat(netTypeStat);
            }
        }
    }

    /* access modifiers changed from: private */
    public static int detectIpStack() {
        int i;
        try {
            i = getIpStack();
        } catch (Exception unused) {
            i = 0;
        }
        ALog.e(TAG, "startIpStackDetect", (String) null, "ip stack", Integer.valueOf(i));
        return i;
    }

    private static boolean filterAddress(InetAddress inetAddress) {
        return inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isAnyLocalAddress();
    }

    /* access modifiers changed from: private */
    public static Nat64Prefix detectNat64Prefix() throws UnknownHostException {
        InetAddress inetAddress;
        boolean z;
        try {
            inetAddress = InetAddress.getByName(IPV4ONLY_HOST);
        } catch (Exception unused) {
            inetAddress = null;
        }
        if (inetAddress instanceof Inet6Address) {
            ALog.i(TAG, "Resolved AAAA: " + inetAddress.toString(), (String) null, new Object[0]);
            byte[] address = inetAddress.getAddress();
            if (address.length != 16) {
                return null;
            }
            int i = 12;
            while (true) {
                z = true;
                if (i < 0) {
                    z = false;
                    break;
                }
                if ((address[i] & IPV4ONLY_IP[0][0]) != 0 && address[i + 1] == 0 && address[i + 2] == 0) {
                    int i2 = i + 3;
                    if (address[i2] == IPV4ONLY_IP[0][3] || address[i2] == IPV4ONLY_IP[1][3]) {
                        break;
                    }
                }
                i--;
            }
            if (z) {
                address[i + 3] = 0;
                address[i + 2] = 0;
                address[i + 1] = 0;
                address[i] = 0;
                return new Nat64Prefix(Inet6Address.getByAddress(IPV4ONLY_HOST, address, 0), i * 8);
            }
        } else if (inetAddress instanceof Inet4Address) {
            ALog.i(TAG, "Resolved A: " + inetAddress.toString(), (String) null, new Object[0]);
        }
        return null;
    }
}
