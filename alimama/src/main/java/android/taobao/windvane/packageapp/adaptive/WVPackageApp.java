package android.taobao.windvane.packageapp.adaptive;

import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WVPackageApp {
    private static String PRELOAD_ZIP = "preload_packageapp.zip";
    private static Map<String, List<WVPackageUpdateListener>> PackageUpdateListenerMaps = null;
    private static final String TAG = "WVPackageApp";
    public static boolean isInited = false;

    public static void setPreunzipPackageName(String str) {
        PRELOAD_ZIP = str;
    }

    public static String getPreunzipPackageName() {
        return !TextUtils.isEmpty(PRELOAD_ZIP) ? PRELOAD_ZIP : "preload_packageapp.zip";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0035, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void init(android.content.Context r2, boolean r3) {
        /*
            java.lang.Class<android.taobao.windvane.packageapp.adaptive.WVPackageApp> r0 = android.taobao.windvane.packageapp.adaptive.WVPackageApp.class
            monitor-enter(r0)
            if (r2 != 0) goto L_0x0010
            java.lang.String r2 = "WVPackageApp"
            java.lang.String r3 = "init fail. context cannot be null"
            android.taobao.windvane.util.TaoLog.e(r2, r3)     // Catch:{ all -> 0x000e }
            monitor-exit(r0)
            return
        L_0x000e:
            r2 = move-exception
            goto L_0x0036
        L_0x0010:
            android.app.Application r1 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ all -> 0x000e }
            if (r1 != 0) goto L_0x0026
            boolean r1 = r2 instanceof android.app.Application     // Catch:{ all -> 0x000e }
            if (r1 != 0) goto L_0x0021
            java.lang.String r2 = "WVPackageApp"
            java.lang.String r3 = "init fail. context should be application"
            android.taobao.windvane.util.TaoLog.e(r2, r3)     // Catch:{ all -> 0x000e }
            monitor-exit(r0)
            return
        L_0x0021:
            r1 = r2
            android.app.Application r1 = (android.app.Application) r1     // Catch:{ all -> 0x000e }
            android.taobao.windvane.config.GlobalConfig.context = r1     // Catch:{ all -> 0x000e }
        L_0x0026:
            boolean r1 = isInited     // Catch:{ all -> 0x000e }
            if (r1 != 0) goto L_0x0034
            android.taobao.windvane.packageapp.WVPackageAppManager r1 = android.taobao.windvane.packageapp.WVPackageAppManager.getInstance()     // Catch:{ all -> 0x000e }
            r1.init(r2, r3)     // Catch:{ all -> 0x000e }
            r2 = 1
            isInited = r2     // Catch:{ all -> 0x000e }
        L_0x0034:
            monitor-exit(r0)
            return
        L_0x0036:
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.adaptive.WVPackageApp.init(android.content.Context, boolean):void");
    }

    public static synchronized void registerPackageUpdateListener(String str, WVPackageUpdateListener wVPackageUpdateListener) {
        synchronized (WVPackageApp.class) {
            if (TextUtils.isEmpty(str)) {
                if (GlobalConfig.env != EnvEnum.DAILY) {
                    TaoLog.d(TAG, "appName is null!");
                    return;
                }
                throw new NullPointerException("AppName 不可以为空!");
            } else if (wVPackageUpdateListener != null) {
                TaoLog.d(TAG, "appName:" + str + " listener:" + wVPackageUpdateListener);
                if (PackageUpdateListenerMaps == null) {
                    PackageUpdateListenerMaps = new HashMap();
                }
                List list = PackageUpdateListenerMaps.get(str);
                if (list == null) {
                    list = new ArrayList();
                    PackageUpdateListenerMaps.put(str, list);
                }
                list.add(wVPackageUpdateListener);
            } else if (GlobalConfig.env != EnvEnum.DAILY) {
                TaoLog.d(TAG, "packageUpdateListener is null!");
            } else {
                throw new NullPointerException("PackageUpdateListener 不可以为空!");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void unRegisterPackageUpdateListener(java.lang.String r4, android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.lang.String r0 = "WVPackageApp"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0034 }
            r1.<init>()     // Catch:{ all -> 0x0034 }
            java.lang.String r2 = "appName:"
            r1.append(r2)     // Catch:{ all -> 0x0034 }
            r1.append(r4)     // Catch:{ all -> 0x0034 }
            java.lang.String r2 = " Listener:"
            r1.append(r2)     // Catch:{ all -> 0x0034 }
            r1.append(r5)     // Catch:{ all -> 0x0034 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0034 }
            android.taobao.windvane.util.TaoLog.d(r0, r1)     // Catch:{ all -> 0x0034 }
            java.util.Map<java.lang.String, java.util.List<android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener>> r0 = PackageUpdateListenerMaps     // Catch:{ all -> 0x0034 }
            if (r0 != 0) goto L_0x0025
            monitor-exit(r3)
            return
        L_0x0025:
            java.util.Map<java.lang.String, java.util.List<android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener>> r0 = PackageUpdateListenerMaps     // Catch:{ all -> 0x0034 }
            java.lang.Object r4 = r0.get(r4)     // Catch:{ all -> 0x0034 }
            java.util.List r4 = (java.util.List) r4     // Catch:{ all -> 0x0034 }
            if (r4 == 0) goto L_0x0032
            r4.remove(r5)     // Catch:{ all -> 0x0034 }
        L_0x0032:
            monitor-exit(r3)
            return
        L_0x0034:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.adaptive.WVPackageApp.unRegisterPackageUpdateListener(java.lang.String, android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void notifyPackageUpdateFinish(java.lang.String r4) {
        /*
            java.lang.Class<android.taobao.windvane.packageapp.adaptive.WVPackageApp> r0 = android.taobao.windvane.packageapp.adaptive.WVPackageApp.class
            monitor-enter(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x005d }
            if (r1 == 0) goto L_0x001f
            android.taobao.windvane.config.EnvEnum r1 = android.taobao.windvane.config.GlobalConfig.env     // Catch:{ all -> 0x005d }
            android.taobao.windvane.config.EnvEnum r2 = android.taobao.windvane.config.EnvEnum.DAILY     // Catch:{ all -> 0x005d }
            if (r1 == r2) goto L_0x0017
            java.lang.String r1 = "WVPackageApp"
            java.lang.String r2 = "notify package update finish appName is null!"
            android.taobao.windvane.util.TaoLog.e(r1, r2)     // Catch:{ all -> 0x005d }
            goto L_0x001f
        L_0x0017:
            java.lang.NullPointerException r4 = new java.lang.NullPointerException     // Catch:{ all -> 0x005d }
            java.lang.String r1 = "appName 不能为空!"
            r4.<init>(r1)     // Catch:{ all -> 0x005d }
            throw r4     // Catch:{ all -> 0x005d }
        L_0x001f:
            java.lang.String r1 = "WVPackageApp"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x005d }
            r2.<init>()     // Catch:{ all -> 0x005d }
            java.lang.String r3 = "appName:"
            r2.append(r3)     // Catch:{ all -> 0x005d }
            r2.append(r4)     // Catch:{ all -> 0x005d }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x005d }
            android.taobao.windvane.util.TaoLog.d(r1, r2)     // Catch:{ all -> 0x005d }
            java.util.Map<java.lang.String, java.util.List<android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener>> r1 = PackageUpdateListenerMaps     // Catch:{ all -> 0x005d }
            if (r1 != 0) goto L_0x003b
            monitor-exit(r0)
            return
        L_0x003b:
            java.util.Map<java.lang.String, java.util.List<android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener>> r1 = PackageUpdateListenerMaps     // Catch:{ all -> 0x005d }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x005d }
            java.util.List r1 = (java.util.List) r1     // Catch:{ all -> 0x005d }
            if (r1 == 0) goto L_0x005b
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x005d }
        L_0x0049:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x005d }
            if (r2 == 0) goto L_0x005b
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x005d }
            android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener r2 = (android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener) r2     // Catch:{ all -> 0x005d }
            if (r2 == 0) goto L_0x0049
            r2.onPackageUpdateFinish(r4)     // Catch:{ all -> 0x005d }
            goto L_0x0049
        L_0x005b:
            monitor-exit(r0)
            return
        L_0x005d:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.adaptive.WVPackageApp.notifyPackageUpdateFinish(java.lang.String):void");
    }
}
