package android.taobao.windvane.packageapp.jsbridge;

import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.utils.WMLAppManager;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;

import com.alibaba.android.prefetchx.core.file.WXFilePrefetchModule;
import com.taobao.weex.el.parse.Operators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class WVZCache extends WVApiPlugin {
    private static final String TAG = "WVZCache";
    private static long last;

    /* access modifiers changed from: protected */
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (WXFilePrefetchModule.PREFETCH_MODULE_NAME.equals(str)) {
            _prefetch(str2, wVCallBackContext);
            return true;
        } else if ("fetch".equals(str)) {
            _fetch(str2, wVCallBackContext);
            return true;
        } else if ("miniPrefetch".equals(str)) {
            if (!EnvUtil.isAppDebug()) {
                return false;
            }
            _miniPrefetch(str2, wVCallBackContext);
            return true;
        } else if (!"normalTask".equals(str) || !EnvUtil.isAppDebug()) {
            return false;
        } else {
            _normalTask(wVCallBackContext);
            return true;
        }
    }

    private void _normalTask(WVCallBackContext wVCallBackContext) {
        wVCallBackContext.success();
        if (!WVConfigManager.getInstance().checkIfUpdate(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeActive)) {
            ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
        }
    }

    private void _miniPrefetch(String str, WVCallBackContext wVCallBackContext) {
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("apps");
            HashSet hashSet = new HashSet();
            for (int i = 0; i < jSONArray.length(); i++) {
                hashSet.add(jSONArray.getString(i));
            }
            WMLAppManager.getInstance().prefetchApps(hashSet);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void _prefetch(String str, WVCallBackContext wVCallBackContext) {
        boolean z;
        String str2;
        wVCallBackContext.success();
        try {
            Iterator<String> keys = new JSONObject(str).keys();
            HashSet hashSet = new HashSet();
            boolean z2 = false;
            while (keys.hasNext()) {
                String next = keys.next();
                if (next.contains("/") || next.contains("//")) {
                    str2 = ZipPrefixesManager.getInstance().getZipAppName(next);
                    z = true;
                } else {
                    z = z2;
                    str2 = next;
                }
                if (str2 == null) {
                    TaoLog.e(TAG, "url: [" + next + "] has no corresponding appName");
                } else {
                    hashSet.add(str2);
                }
                z2 = z;
            }
            doPrefetch(hashSet, z2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doPrefetch(Set<String> set, boolean z) {
        final int i = 0;
        HashSet hashSet = null;
        for (String next : set) {
            ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(next);
            if (appInfo == null) {
                TaoLog.e(TAG, "register app: [" + next + Operators.ARRAY_END_STR);
                ZipAppInfo zipAppInfo = new ZipAppInfo();
                zipAppInfo.isOptional = true;
                zipAppInfo.name = next;
                ConfigManager.getLocGlobalConfig().putAppInfo2Table(next, zipAppInfo);
            } else if (appInfo.isAppInstalled()) {
                TaoLog.e(TAG, "duplicate prefetch app: [" + next + Operators.ARRAY_END_STR);
            } else if (z && appInfo.getPriority() < 7) {
                TaoLog.e(TAG, "error prefetch [app=" + next + "], for priority = " + appInfo.getPriority());
            } else if (appInfo.tempPriority != 0) {
                TaoLog.e(TAG, "[app=" + next + "] already in prefetch task");
            } else {
                appInfo.tempPriority = appInfo.getPriority();
                appInfo.f |= 15;
                appInfo.f &= 9;
                i++;
                if (hashSet == null) {
                    hashSet = new HashSet();
                }
                hashSet.add(appInfo.name);
            }
        }
        if (i == 0) {
            TaoLog.d(TAG, "no prefetch app");
            return;
        }
        TaoLog.d(TAG, "prefetch size = [" + i + Operators.ARRAY_END_STR);
        if (ZipAppDownloaderQueue.getInstance().isUpdateFinish()) {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    ZipAppDownloaderQueue.getInstance().hasPrefetch = true;
                    ZipAppDownloaderQueue.getInstance().needDownloadCount = i;
                    if (!WVConfigManager.getInstance().checkIfUpdate(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeActive)) {
                        ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
                    }
                }
            }, 10);
        } else if (ZipAppDownloaderQueue.getInstance().hasPrefetch) {
            ZipAppDownloaderQueue.getInstance().refreshQueue = true;
            ZipAppDownloaderQueue.getInstance().needDownloadCount += i;
            ZipAppDownloaderQueue.getInstance().prefetch.addAll(hashSet);
        } else {
            ZipAppDownloaderQueue.getInstance().appendDownloadCount += i;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0062 A[Catch:{ JSONException -> 0x0095 }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x006a A[Catch:{ JSONException -> 0x0095 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _fetch(java.lang.String r7, android.taobao.windvane.jsbridge.WVCallBackContext r8) {
        /*
            r6 = this;
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0095 }
            r0.<init>(r7)     // Catch:{ JSONException -> 0x0095 }
            java.util.Iterator r7 = r0.keys()     // Catch:{ JSONException -> 0x0095 }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0095 }
            r0.<init>()     // Catch:{ JSONException -> 0x0095 }
        L_0x000e:
            boolean r1 = r7.hasNext()     // Catch:{ JSONException -> 0x0095 }
            if (r1 == 0) goto L_0x008d
            java.lang.Object r1 = r7.next()     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ JSONException -> 0x0095 }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0095 }
            r2.<init>()     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r3 = "/"
            boolean r3 = r1.contains(r3)     // Catch:{ JSONException -> 0x0095 }
            if (r3 == 0) goto L_0x0053
            java.lang.String r3 = "//"
            boolean r3 = r1.contains(r3)     // Catch:{ JSONException -> 0x0095 }
            if (r3 != 0) goto L_0x0030
            goto L_0x0053
        L_0x0030:
            android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager r3 = android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager.getInstance()     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r3 = r3.getZipAppName(r1)     // Catch:{ JSONException -> 0x0095 }
            if (r3 != 0) goto L_0x0054
            java.lang.String r3 = "status"
            java.lang.String r4 = "FAILED"
            r2.put(r3, r4)     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r3 = "errorCode"
            r4 = 1101(0x44d, float:1.543E-42)
            r2.put(r3, r4)     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r3 = "errorMsg"
            java.lang.String r4 = "error prefix"
            r2.put(r3, r4)     // Catch:{ JSONException -> 0x0095 }
            r0.put(r1, r2)     // Catch:{ JSONException -> 0x0095 }
            goto L_0x000e
        L_0x0053:
            r3 = r1
        L_0x0054:
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r4 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()     // Catch:{ JSONException -> 0x0095 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r3 = r4.getAppInfo(r3)     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r4 = android.taobao.windvane.packageapp.WVPackageAppRuntime.isAvailable(r1, r3)     // Catch:{ JSONException -> 0x0095 }
            if (r4 != 0) goto L_0x006a
            java.lang.String r3 = "status"
            java.lang.String r4 = "SUCCESS"
            r2.put(r3, r4)     // Catch:{ JSONException -> 0x0095 }
            goto L_0x0089
        L_0x006a:
            java.lang.String r4 = "status"
            java.lang.String r5 = "FAILED"
            r2.put(r4, r5)     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r4 = "errorCode"
            if (r3 != 0) goto L_0x0078
            r5 = 3104(0xc20, float:4.35E-42)
            goto L_0x007a
        L_0x0078:
            int r5 = r3.status     // Catch:{ JSONException -> 0x0095 }
        L_0x007a:
            r2.put(r4, r5)     // Catch:{ JSONException -> 0x0095 }
            java.lang.String r4 = "errorMsg"
            if (r3 != 0) goto L_0x0084
            java.lang.String r3 = "not install"
            goto L_0x0086
        L_0x0084:
            java.lang.String r3 = "error install"
        L_0x0086:
            r2.put(r4, r3)     // Catch:{ JSONException -> 0x0095 }
        L_0x0089:
            r0.put(r1, r2)     // Catch:{ JSONException -> 0x0095 }
            goto L_0x000e
        L_0x008d:
            java.lang.String r7 = r0.toString()     // Catch:{ JSONException -> 0x0095 }
            r8.success((java.lang.String) r7)     // Catch:{ JSONException -> 0x0095 }
            goto L_0x0099
        L_0x0095:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0099:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.jsbridge.WVZCache._fetch(java.lang.String, android.taobao.windvane.jsbridge.WVCallBackContext):void");
    }
}
