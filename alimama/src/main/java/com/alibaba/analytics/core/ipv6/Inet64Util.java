package com.alibaba.analytics.core.ipv6;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.utils.Logger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class Inet64Util {
    public static final int IPV4_ONLY = 1;
    public static final int IPV6_ONLY = 2;
    public static final int IP_DUAL_STACK = 3;
    public static final int IP_STACK_UNKNOWN = 0;
    private static final String TAG = "Inet64Util";
    private static int mIpStack = -1;

    Inet64Util() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:8|9|10|11|12|13|14|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0018 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized int detectIpStack() {
        /*
            java.lang.Class<com.alibaba.analytics.core.ipv6.Inet64Util> r0 = com.alibaba.analytics.core.ipv6.Inet64Util.class
            monitor-enter(r0)
            int r1 = mIpStack     // Catch:{ all -> 0x003f }
            if (r1 < 0) goto L_0x000b
            int r1 = mIpStack     // Catch:{ all -> 0x003f }
            monitor-exit(r0)
            return r1
        L_0x000b:
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x003f }
            r3 = 0
            mIpStack = r3     // Catch:{ all -> 0x003f }
            int r4 = getIpStack()     // Catch:{ Exception -> 0x0018 }
            mIpStack = r4     // Catch:{ Exception -> 0x0018 }
        L_0x0018:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x003f }
            r6 = 0
            long r4 = r4 - r1
            int r1 = mIpStack     // Catch:{ all -> 0x003f }
            com.alibaba.analytics.core.ipv6.Ipv6Monitor.setIpStack(r1)     // Catch:{ all -> 0x003f }
            com.alibaba.analytics.core.ipv6.Ipv6Monitor.sendIpv6DetectEvent(r4)     // Catch:{ all -> 0x003f }
            java.lang.String r1 = "Inet64Util"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x003f }
            java.lang.String r4 = "detectIpStack status"
            r2[r3] = r4     // Catch:{ all -> 0x003f }
            r3 = 1
            int r4 = mIpStack     // Catch:{ all -> 0x003f }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x003f }
            r2[r3] = r4     // Catch:{ all -> 0x003f }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r1, (java.lang.Object[]) r2)     // Catch:{ all -> 0x003f }
            int r1 = mIpStack     // Catch:{ all -> 0x003f }
            monitor-exit(r0)
            return r1
        L_0x003f:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.ipv6.Inet64Util.detectIpStack():int");
    }

    private static int getIpStack() throws SocketException {
        int i;
        TreeMap treeMap = new TreeMap();
        Iterator<T> it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        while (true) {
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            NetworkInterface networkInterface = (NetworkInterface) it.next();
            if (!networkInterface.getInterfaceAddresses().isEmpty()) {
                String displayName = networkInterface.getDisplayName();
                Logger.d(TAG, "find NetworkInterface", displayName);
                int i2 = 0;
                for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                    InetAddress address2 = address.getAddress();
                    if (address2 instanceof Inet6Address) {
                        Inet6Address inet6Address = (Inet6Address) address2;
                        if (!filterAddress(inet6Address)) {
                            Logger.i(TAG, "Found IPv6 address", inet6Address.toString());
                            i2 |= 2;
                        }
                    } else if (address2 instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address2;
                        if (!filterAddress(inet4Address) && !inet4Address.getHostAddress().startsWith("192.168.43.")) {
                            Logger.d(TAG, "Found IPv4 address", inet4Address.toString());
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
        String str = null;
        if (NetworkUtil.isWifi(Variables.getInstance().getContext())) {
            str = "wlan";
        } else if (NetworkUtil.isMobile(Variables.getInstance().getContext())) {
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
        if (i != 2 || !treeMap.containsKey("v4-wlan0")) {
            return i;
        }
        return 3;
    }

    private static boolean filterAddress(InetAddress inetAddress) {
        return inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isAnyLocalAddress();
    }
}
