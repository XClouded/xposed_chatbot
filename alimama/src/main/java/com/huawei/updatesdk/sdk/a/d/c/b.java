package com.huawei.updatesdk.sdk.a.d.c;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class b {
    private static String a = "NetworkUtil";
    private static int b = -1;
    private static int c = -1;
    private static Proxy d;

    public static int a(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();
            if (1 == type || 13 == type) {
                return 1;
            }
            if (type == 0) {
                switch (networkInfo.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return 2;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return 3;
                    case 13:
                        return 4;
                }
            }
        }
        return 0;
    }

    public static Proxy a() {
        return d;
    }

    public static void a(int i) {
        c = i;
    }

    public static void a(Proxy proxy) {
        d = proxy;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000c, code lost:
        r1 = (r1 = (android.net.ConnectivityManager) r1.getSystemService("connectivity")).getActiveNetworkInfo();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.content.Context r1) {
        /*
            if (r1 == 0) goto L_0x001a
            java.lang.String r0 = "connectivity"
            java.lang.Object r1 = r1.getSystemService(r0)
            android.net.ConnectivityManager r1 = (android.net.ConnectivityManager) r1
            if (r1 == 0) goto L_0x001a
            android.net.NetworkInfo r1 = r1.getActiveNetworkInfo()
            if (r1 == 0) goto L_0x001a
            boolean r1 = r1.isConnected()
            if (r1 == 0) goto L_0x001a
            r1 = 1
            return r1
        L_0x001a:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.a.d.c.b.a(android.content.Context):boolean");
    }

    private static boolean a(NetworkInfo networkInfo, Context context) {
        int type = networkInfo.getType();
        boolean b2 = a.b(context);
        if (type != 0 || !b2) {
            return false;
        }
        String host = android.net.Proxy.getHost(context);
        int port = android.net.Proxy.getPort(context);
        Proxy proxy = null;
        if (!(host == null || host.length() <= 0 || port == -1)) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        }
        a(proxy);
        return true;
    }

    public static boolean b(Context context) {
        if (-1 == c) {
            c(context);
        }
        return c == -3;
    }

    public static int c(Context context) {
        if (-1 == b) {
            a.a(a, "getPsType() need init");
            if (context != null) {
                d(context);
            }
        }
        return b;
    }

    public static void d(Context context) {
        a(0);
        NetworkInfo f = f(context);
        if (f == null) {
            a.a(a, "setPsType() info = null");
            return;
        }
        b = a(f);
        if (1 != b) {
            a(a(f, context) ? -3 : -2);
        }
    }

    public static Proxy e(Context context) {
        if (b(context)) {
            return a();
        }
        return null;
    }

    private static NetworkInfo f(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }
}
