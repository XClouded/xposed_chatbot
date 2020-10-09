package anetwork.channel.cookie;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieSyncManager;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anetwork.channel.http.NetworkSdkSetting;
import java.util.List;
import java.util.Map;

public class CookieManager {
    public static final String TAG = "anet.CookieManager";
    private static boolean isCookieValid = true;
    private static volatile boolean isSetup = false;
    private static android.webkit.CookieManager webkitCookMgr;

    public static synchronized void setup(Context context) {
        synchronized (CookieManager.class) {
            if (!isSetup) {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (Build.VERSION.SDK_INT < 21) {
                        CookieSyncManager.createInstance(context);
                    }
                    webkitCookMgr = android.webkit.CookieManager.getInstance();
                    webkitCookMgr.setAcceptCookie(true);
                    if (Build.VERSION.SDK_INT < 21) {
                        webkitCookMgr.removeExpiredCookie();
                    }
                    ALog.e(TAG, "CookieManager setup.", (String) null, "cost", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                } catch (Throwable th) {
                    isCookieValid = false;
                    ALog.e(TAG, "Cookie Manager setup failed!!!", (String) null, th, new Object[0]);
                }
                isSetup = true;
            }
        }
    }

    private static boolean checkSetup() {
        if (!isSetup && NetworkSdkSetting.getContext() != null) {
            setup(NetworkSdkSetting.getContext());
        }
        return isSetup;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void setCookie(java.lang.String r8, java.lang.String r9) {
        /*
            java.lang.Class<anetwork.channel.cookie.CookieManager> r0 = anetwork.channel.cookie.CookieManager.class
            monitor-enter(r0)
            boolean r1 = checkSetup()     // Catch:{ all -> 0x0047 }
            if (r1 == 0) goto L_0x0045
            boolean r1 = isCookieValid     // Catch:{ all -> 0x0047 }
            if (r1 != 0) goto L_0x000e
            goto L_0x0045
        L_0x000e:
            android.webkit.CookieManager r1 = webkitCookMgr     // Catch:{ Throwable -> 0x0027 }
            r1.setCookie(r8, r9)     // Catch:{ Throwable -> 0x0027 }
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0027 }
            r2 = 21
            if (r1 >= r2) goto L_0x0021
            android.webkit.CookieSyncManager r1 = android.webkit.CookieSyncManager.getInstance()     // Catch:{ Throwable -> 0x0027 }
            r1.sync()     // Catch:{ Throwable -> 0x0027 }
            goto L_0x0043
        L_0x0021:
            android.webkit.CookieManager r1 = webkitCookMgr     // Catch:{ Throwable -> 0x0027 }
            r1.flush()     // Catch:{ Throwable -> 0x0027 }
            goto L_0x0043
        L_0x0027:
            r1 = move-exception
            java.lang.String r2 = "anet.CookieManager"
            java.lang.String r3 = "set cookie failed."
            r4 = 0
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0047 }
            r6 = 0
            java.lang.String r7 = "url"
            r5[r6] = r7     // Catch:{ all -> 0x0047 }
            r6 = 1
            r5[r6] = r8     // Catch:{ all -> 0x0047 }
            r8 = 2
            java.lang.String r6 = "cookies"
            r5[r8] = r6     // Catch:{ all -> 0x0047 }
            r8 = 3
            r5[r8] = r9     // Catch:{ all -> 0x0047 }
            anet.channel.util.ALog.e(r2, r3, r4, r1, r5)     // Catch:{ all -> 0x0047 }
        L_0x0043:
            monitor-exit(r0)
            return
        L_0x0045:
            monitor-exit(r0)
            return
        L_0x0047:
            r8 = move-exception
            monitor-exit(r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.cookie.CookieManager.setCookie(java.lang.String, java.lang.String):void");
    }

    public static void setCookie(String str, Map<String, List<String>> map) {
        if (str != null && map != null) {
            try {
                for (Map.Entry next : map.entrySet()) {
                    String str2 = (String) next.getKey();
                    if (str2 != null && (str2.equalsIgnoreCase("Set-Cookie") || str2.equalsIgnoreCase(HttpConstant.SET_COOKIE2))) {
                        for (String cookie : (List) next.getValue()) {
                            setCookie(str, cookie);
                        }
                    }
                }
            } catch (Exception e) {
                ALog.e(TAG, "set cookie failed", (String) null, e, "url", str, "\nheaders", map);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String getCookie(java.lang.String r6) {
        /*
            java.lang.Class<anetwork.channel.cookie.CookieManager> r0 = anetwork.channel.cookie.CookieManager.class
            monitor-enter(r0)
            boolean r1 = checkSetup()     // Catch:{ all -> 0x0035 }
            r2 = 0
            if (r1 == 0) goto L_0x0033
            boolean r1 = isCookieValid     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x000f
            goto L_0x0033
        L_0x000f:
            android.webkit.CookieManager r1 = webkitCookMgr     // Catch:{ Throwable -> 0x0016 }
            java.lang.String r1 = r1.getCookie(r6)     // Catch:{ Throwable -> 0x0016 }
            goto L_0x0031
        L_0x0016:
            r1 = move-exception
            java.lang.String r3 = "anet.CookieManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0035 }
            r4.<init>()     // Catch:{ all -> 0x0035 }
            java.lang.String r5 = "get cookie failed. url="
            r4.append(r5)     // Catch:{ all -> 0x0035 }
            r4.append(r6)     // Catch:{ all -> 0x0035 }
            java.lang.String r6 = r4.toString()     // Catch:{ all -> 0x0035 }
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0035 }
            anet.channel.util.ALog.e(r3, r6, r2, r1, r4)     // Catch:{ all -> 0x0035 }
            r1 = r2
        L_0x0031:
            monitor-exit(r0)
            return r1
        L_0x0033:
            monitor-exit(r0)
            return r2
        L_0x0035:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.cookie.CookieManager.getCookie(java.lang.String):java.lang.String");
    }
}
