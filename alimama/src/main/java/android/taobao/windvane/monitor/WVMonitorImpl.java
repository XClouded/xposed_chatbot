package android.taobao.windvane.monitor;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class WVMonitorImpl implements WVPerformanceMonitorInterface, WVErrorMonitorInterface, WVConfigMonitorInterface, WVMonitorInterface {
    private static final String TAG = "WVMonitor";
    private long appStartTime;
    private String currentUrl;
    private ConcurrentHashMap<String, WVMonitorData> dataMap;
    private boolean enabled;
    private long initTime;
    private boolean isInit;
    private boolean needCommitStartTime;
    private HashSet<String> zipApps;

    public WVMonitorImpl() {
        this.needCommitStartTime = true;
        this.appStartTime = 0;
        this.initTime = 0;
        this.isInit = false;
        this.currentUrl = "";
        this.zipApps = new HashSet<>();
        this.dataMap = new ConcurrentHashMap<>();
        this.enabled = false;
        this.appStartTime = System.currentTimeMillis();
        this.enabled = true;
    }

    private static WVMonitorConfig getConfig() {
        return WVMonitorConfigManager.getInstance().config;
    }

    private boolean isEnabled() {
        if (GlobalConfig.context == null) {
            return false;
        }
        return this.enabled;
    }

    private void upload(String str) {
        WVMonitorData wVMonitorData;
        String str2;
        Object obj;
        if (isEnabled() && this.dataMap != null && (wVMonitorData = this.dataMap.get(str)) != null) {
            if (wVMonitorData.stat.onLoad == 0) {
                str2 = "";
            } else {
                str2 = "" + wVMonitorData.stat.onLoad;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("");
            if (wVMonitorData.stat.onDomLoad == 0 && wVMonitorData.stat.onLoad == 0) {
                obj = "";
            } else {
                obj = Integer.valueOf(wVMonitorData.stat.finish);
            }
            sb.append(obj);
            UserTrackUtil.commitEvent(UserTrackUtil.EVENTID_MONITOR, str, str2, sb.toString(), wVMonitorData.toJsonStringDict());
            if (this.isInit && wVMonitorData.startTime > this.initTime) {
                this.isInit = false;
                wVMonitorData.isInit = true;
                wVMonitorData.init = wVMonitorData.startTime - this.initTime;
            }
            AppMonitorUtil.commitPerformanceInfo(wVMonitorData);
            String str3 = wVMonitorData.stat.packageAppName;
            if (this.zipApps != null && !TextUtils.isEmpty(str3) && !this.zipApps.contains(str3)) {
                AppMonitorUtil.commitPackageVisitStartInfo(str3, System.currentTimeMillis() - this.appStartTime);
                this.zipApps.add(str3);
            }
            if (this.needCommitStartTime && this.appStartTime != 0 && this.appStartTime < wVMonitorData.startTime) {
                AppMonitorUtil.commitStartTimeInfo(wVMonitorData.url, wVMonitorData.startTime - this.appStartTime);
                this.needCommitStartTime = false;
            }
            TaoLog.i(TAG, "upload performance info  URL: " + str + " fromType : " + wVMonitorData.stat.fromType + " packageAppName : " + wVMonitorData.stat.packageAppName);
        }
    }

    private static String formatUrl(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(63);
        if (indexOf <= 0) {
            indexOf = str.length();
        }
        int indexOf2 = str.indexOf(35);
        if (indexOf2 <= 0) {
            indexOf2 = str.length();
        }
        if (indexOf >= indexOf2) {
            indexOf = indexOf2;
        }
        return str.substring(0, indexOf);
    }

    private static boolean errorNeedReport(String str, String str2, Integer num) {
        boolean z = getConfig().isErrorBlacklist;
        for (WVMonitorConfig.ErrorRule next : getConfig().errorRule) {
            if (!(next.url == null || str == null)) {
                if (next.urlPattern == null) {
                    next.urlPattern = Pattern.compile(next.url);
                }
                if (!next.urlPattern.matcher(str).matches()) {
                    continue;
                }
            }
            if (!(next.msg == null || str2 == null)) {
                if (next.msgPattern == null) {
                    next.msgPattern = Pattern.compile(next.msg);
                }
                if (!next.msgPattern.matcher(str2).matches()) {
                    continue;
                }
            }
            if (TextUtils.isEmpty(next.code) || num == null || next.code.equals(num.toString())) {
                return !z;
            }
        }
        return z;
    }

    private WVMonitorData initData(String str) {
        if (this.dataMap == null) {
            return null;
        }
        WVMonitorData wVMonitorData = this.dataMap.get(str);
        if (wVMonitorData == null) {
            synchronized (WVMonitorImpl.class) {
                if (wVMonitorData == null) {
                    try {
                        TaoLog.i(TAG, "monitor data init");
                        wVMonitorData = new WVMonitorData();
                        this.currentUrl = str;
                        this.dataMap.put(str, wVMonitorData);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
        return wVMonitorData;
    }

    public void didPageStartLoadAtTime(String str, long j) {
        if (isEnabled() && str != null && Uri.parse(str) != null && Uri.parse(str).isHierarchical()) {
            TaoLog.d(TAG, String.format("pageStart: %s", new Object[]{str}));
            WVMonitorData initData = initData(str);
            if (initData != null) {
                initData.startTime = j;
                initData.url = str;
            }
        }
    }

    public void didPageFinishLoadAtTime(String str, long j) {
        if (str != null && Uri.parse(str) != null && Uri.parse(str).isHierarchical()) {
            pageFinish(str, j, true);
        }
    }

    public void didPageDomLoadAtTime(String str, long j) {
        WVMonitorData wVMonitorData;
        if (isEnabled() && str != null && this.dataMap != null && (wVMonitorData = this.dataMap.get(str)) != null && wVMonitorData.startTime > 0) {
            long j2 = j - wVMonitorData.startTime;
            if (j2 >= getConfig().stat.onDomLoad) {
                wVMonitorData.stat.onDomLoad = j2;
            }
        }
    }

    public void didPageReceiveFirstByteAtTime(String str, long j) {
        WVMonitorData wVMonitorData;
        if (isEnabled() && str != null && this.dataMap != null && (wVMonitorData = this.dataMap.get(str)) != null) {
            TaoLog.d(TAG, String.format("domLoad: %s", new Object[]{str}));
            if (wVMonitorData.startTime > 0) {
                wVMonitorData.stat.firstByteTime = j - wVMonitorData.startTime;
            }
        }
    }

    public void didPageOccurSelfDefinedEvent(String str, String str2, long j) {
        WVMonitorData wVMonitorData;
        if (isEnabled() && str != null && this.dataMap != null && (wVMonitorData = this.dataMap.get(str)) != null) {
            TaoLog.d(TAG, String.format("domLoad: %s", new Object[]{str}));
            if (wVMonitorData.startTime > 0) {
                Map<String, Long> map = wVMonitorData.args.selfDefine;
                for (Map.Entry next : map.entrySet()) {
                    map.put(next.getKey(), Long.valueOf(((Long) next.getValue()).longValue() - wVMonitorData.startTime));
                }
            }
        }
    }

    public void didGetPageStatusCode(String str, int i, int i2, String str2, String str3, String str4, Map<String, String> map, WVPerformanceMonitorInterface.NetStat netStat) {
        WVMonitorData initData;
        if (isEnabled() && str != null && (initData = initData(str)) != null) {
            initData.args.netStat = netStat;
            if (i > 0) {
                initData.args.statusCode = i;
            }
            if (i2 > 1 && initData.stat.fromType <= 1) {
                initData.stat.fromType = i2;
            }
            if (!TextUtils.isEmpty(str2)) {
                initData.stat.packageAppVersion = str2;
            }
            if (map != null) {
                initData.args.via = map.get("via");
            }
            if (!TextUtils.isEmpty(str3)) {
                initData.stat.packageAppName = str3;
            }
            if (!TextUtils.isEmpty(str4)) {
                initData.stat.appSeq = str4;
            }
        }
    }

    public void didExitAtTime(String str, long j) {
        pageFinish(str, j, false);
    }

    public void didResourceStartLoadAtTime(String str, long j) {
        WVMonitorData wVMonitorData;
        if (this.dataMap != null && (wVMonitorData = this.dataMap.get(this.currentUrl)) != null) {
            try {
                if (checkNeedCollectResInfo(str)) {
                    getResData(str).start = j - wVMonitorData.startTime;
                }
            } catch (Exception e) {
                TaoLog.w(TAG, "didResourceStartLoadAtTime Exception : " + e.getMessage());
            }
        }
    }

    public void didResourceFinishLoadAtTime(String str, long j, String str2, long j2) {
        WVMonitorData wVMonitorData;
        if (this.dataMap != null && (wVMonitorData = this.dataMap.get(this.currentUrl)) != null) {
            try {
                if (checkNeedCollectResInfo(str)) {
                    WVMonitorData.resStat resData = getResData(str);
                    resData.end = j - wVMonitorData.startTime;
                    resData.protocolType = str2;
                    resData.tcpTime = j2;
                } else if (isPage(str)) {
                    wVMonitorData.protocolType = str2;
                }
            } catch (Exception e) {
                TaoLog.w(TAG, "didResourceFinishLoadAtTime Exception : " + e.getMessage());
            }
        }
    }

    public void didGetResourceStatusCode(String str, int i, int i2, Map<String, String> map, WVPerformanceMonitorInterface.NetStat netStat) {
        WVMonitorData.resStat resData;
        if (isPage(str)) {
            didGetPageStatusCode(str, i, i2, (String) null, (String) null, (String) null, map, netStat);
        } else if (checkNeedCollectResInfo(str) && (resData = getResData(str)) != null) {
            resData.fromType = i2;
            resData.statusCode = i;
            resData.via = map != null ? map.get("Via") : "";
            if (netStat != null && getConfig().stat.netstat) {
                resData.netStat = netStat;
            }
        }
    }

    public void didGetResourceVerifyCode(String str, long j, long j2, int i, int i2) {
        WVMonitorData wVMonitorData;
        if (this.dataMap != null && (wVMonitorData = this.dataMap.get(this.currentUrl)) != null) {
            if (isPage(str)) {
                if (isEnabled() && str != null) {
                    wVMonitorData.stat.verifyResTime = j;
                    wVMonitorData.stat.verifyTime = j2;
                    wVMonitorData.stat.verifyError = i;
                } else {
                    return;
                }
            } else if (checkNeedCollectResInfo(str)) {
                WVMonitorData.resStat resData = getResData(str);
                resData.verifyResTime = j;
                resData.verifyTime = j2;
                resData.verifyError = i;
            }
            wVMonitorData.stat.allVerifyTime += j2;
            wVMonitorData.stat.verifyCacheSize = i2;
        }
    }

    public void didPerformanceCheckResult(String str, long j, String str2, String str3, String str4) {
        AppMonitorUtil.commitWebPerfCheckInfo(str, j, str2, str3, str4);
    }

    private boolean isPage(String str) {
        if (this.dataMap == null) {
            return false;
        }
        return formatUrl(this.currentUrl).equals(formatUrl(str));
    }

    private boolean checkNeedCollectResInfo(String str) {
        return isEnabled() && str != null && !isPage(str);
    }

    private WVMonitorData.resStat getResData(String str) {
        WVMonitorData wVMonitorData;
        if (this.dataMap == null || (wVMonitorData = this.dataMap.get(this.currentUrl)) == null) {
            return null;
        }
        WVMonitorData.resStat resstat = wVMonitorData.args.resStat.get(str);
        if (resstat != null) {
            return resstat;
        }
        WVMonitorData.resStat createNewResStatInstance = WVMonitorData.createNewResStatInstance();
        wVMonitorData.args.resStat.put(str, createNewResStatInstance);
        return createNewResStatInstance;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:18|19|20|(2:24|(1:26))|27|28) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0098 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void pageFinish(java.lang.String r9, long r10, boolean r12) {
        /*
            r8 = this;
            boolean r0 = r8.isEnabled()
            if (r0 == 0) goto L_0x00a1
            if (r9 != 0) goto L_0x000a
            goto L_0x00a1
        L_0x000a:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, android.taobao.windvane.monitor.WVMonitorData> r0 = r8.dataMap
            if (r0 != 0) goto L_0x000f
            return
        L_0x000f:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, android.taobao.windvane.monitor.WVMonitorData> r0 = r8.dataMap
            java.lang.Object r0 = r0.get(r9)
            android.taobao.windvane.monitor.WVMonitorData r0 = (android.taobao.windvane.monitor.WVMonitorData) r0
            if (r0 != 0) goto L_0x001a
            return
        L_0x001a:
            java.lang.String r1 = "WVMonitor"
            java.lang.String r2 = "pageFinish: %s"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]
            r5 = 0
            r4[r5] = r9
            java.lang.String r2 = java.lang.String.format(r2, r4)
            android.taobao.windvane.util.TaoLog.d(r1, r2)
            long r1 = r0.startTime
            r6 = 0
            int r4 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r4 <= 0) goto L_0x009b
            long r1 = r0.startTime     // Catch:{ Exception -> 0x009b }
            r4 = 0
            long r10 = r10 - r1
            java.lang.String r1 = "WVMonitor"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x009b }
            r2.<init>()     // Catch:{ Exception -> 0x009b }
            java.lang.String r4 = "url: %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x009b }
            r3[r5] = r9     // Catch:{ Exception -> 0x009b }
            java.lang.String r3 = java.lang.String.format(r4, r3)     // Catch:{ Exception -> 0x009b }
            r2.append(r3)     // Catch:{ Exception -> 0x009b }
            java.lang.String r3 = " onLoad time :"
            r2.append(r3)     // Catch:{ Exception -> 0x009b }
            r2.append(r10)     // Catch:{ Exception -> 0x009b }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x009b }
            android.taobao.windvane.util.TaoLog.d(r1, r2)     // Catch:{ Exception -> 0x009b }
            android.taobao.windvane.monitor.WVMonitorConfig r1 = getConfig()     // Catch:{ Exception -> 0x009b }
            if (r1 == 0) goto L_0x009b
            boolean r2 = r8.isEnabled()     // Catch:{ Exception -> 0x009b }
            if (r2 == 0) goto L_0x009b
            android.taobao.windvane.monitor.WVMonitorConfig$StatRule r1 = r1.stat     // Catch:{ Exception -> 0x009b }
            long r1 = r1.onLoad     // Catch:{ Exception -> 0x009b }
            int r3 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r3 < 0) goto L_0x009b
            android.taobao.windvane.monitor.WVMonitorData$stat r1 = r0.stat     // Catch:{ Exception -> 0x009b }
            r1.onLoad = r10     // Catch:{ Exception -> 0x009b }
            android.taobao.windvane.monitor.WVMonitorData$stat r10 = r0.stat     // Catch:{ Exception -> 0x009b }
            r10.finish = r12     // Catch:{ Exception -> 0x009b }
            android.net.Uri r10 = android.net.Uri.parse(r9)     // Catch:{ Exception -> 0x0098 }
            if (r10 == 0) goto L_0x0098
            boolean r11 = r10.isHierarchical()     // Catch:{ Exception -> 0x0098 }
            if (r11 == 0) goto L_0x0098
            java.lang.String r11 = "wvAppMonitor"
            java.lang.String r10 = r10.getQueryParameter(r11)     // Catch:{ Exception -> 0x0098 }
            boolean r11 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Exception -> 0x0098 }
            if (r11 != 0) goto L_0x0098
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x0098 }
            int r10 = r10.intValue()     // Catch:{ Exception -> 0x0098 }
            r0.wvAppMonitor = r10     // Catch:{ Exception -> 0x0098 }
        L_0x0098:
            r8.upload(r9)     // Catch:{ Exception -> 0x009b }
        L_0x009b:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, android.taobao.windvane.monitor.WVMonitorData> r10 = r8.dataMap
            r10.remove(r9)
            return
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.monitor.WVMonitorImpl.pageFinish(java.lang.String, long, boolean):void");
    }

    @SuppressLint({"DefaultLocale"})
    public void didOccurNativeError(String str, int i, String str2) {
        if (isEnabled() && str != null && str2 != null) {
            TaoLog.d(TAG, String.format("reportNativeError: %s ///// %s ///// %d", new Object[]{str, str2, Integer.valueOf(i)}));
            if (isEnabled() && errorNeedReport(str, str2, Integer.valueOf(i))) {
                AppMonitorUtil.commitFail(AppMonitorUtil.NATIVE_ERROR_POINT, i, String.format("message=%s\ncode=%d", new Object[]{str2, Integer.valueOf(i)}), str);
            }
        }
    }

    public void didOccurJSError(String str, String str2, String str3, String str4) {
        if (isEnabled() && str != null && str2 != null && str4 != null && str3 != null) {
            TaoLog.d(TAG, String.format("reportJsError: %s ///// %s ///// %s ///// %s", new Object[]{str, str3, str2, str4}));
            if (errorNeedReport(str, str2, (Integer) null)) {
                AppMonitorUtil.commitFail(AppMonitorUtil.JS_ERROR_POINT, 1, String.format("message=%s\nline=%s\nfile=%s", new Object[]{str2, str4, str3}), str);
            }
        }
    }

    public void didOccurUpdateConfigError(String str, int i, String str2) {
        if (isEnabled() && str2 != null) {
            AppMonitorUtil.commitConifgUpdateError(str, i, str2);
        }
    }

    public void didUpdateConfig(String str, int i, long j, int i2, int i3) {
        if (isEnabled() && str != null) {
            AppMonitorUtil.commitConifgUpdateInfo(str, i, j, i2, i3);
            TaoLog.i(TAG, "updateConfig " + str + " isSuccess : " + i2 + " count : " + i3);
        }
    }

    public void didOccurUpdateConfigSuccess(String str) {
        if (isEnabled() && str != null) {
            AppMonitorUtil.commitConifgUpdateSuccess(str);
        }
    }

    public void didWebViewInitAtTime(long j) {
        if (isEnabled()) {
            this.isInit = true;
            this.initTime = j;
        }
    }

    public void didPagePerformanceInfo(String str, String str2) {
        WVMonitorData wVMonitorData;
        if (this.dataMap != null && (wVMonitorData = this.dataMap.get(str)) != null) {
            wVMonitorData.performanceInfo = str2;
        }
    }

    public void WebViewWrapType(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "UnKnow";
        }
        AppMonitorUtil.commitWVWrapType(str);
    }

    public void commitCoreInitTime(long j, String str) {
        if (TextUtils.isEmpty(str)) {
            str = "UnKnow";
        }
        AppMonitorUtil.commitCoreInitTime(j, str);
    }

    public void commitCoreTypeByPV(String str, String str2) {
        if (TextUtils.isEmpty("UnKnow")) {
            str = "UnKnow";
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "UnKnow";
        }
        AppMonitorUtil.commitCoreTypeByPV(str, str2);
    }

    public void commitRenderType(String str, String str2, int i) {
        AppMonitorUtil.commitRenderType(str, str2, String.valueOf(i), String.valueOf(GlobalConfig.isBackground));
    }
}
