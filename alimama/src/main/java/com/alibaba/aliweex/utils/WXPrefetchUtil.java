package com.alibaba.aliweex.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.module.net.IWXConnection;
import com.alibaba.aliweex.adapter.module.net.WXConnectionFactory;
import com.alibaba.aliweex.plugin.MtopPreloader;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.module.WXTBUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class WXPrefetchUtil {
    public static final String TAG = "WXPrefetchUtil";
    private static volatile boolean isAllowMtopPrefetchStatus = true;
    private static volatile Class jSModuleIntegrationClass = null;
    private static volatile Method jSModuleIntegrationDegenerateMethod = null;
    private static volatile Method jSModuleIntegrationEvolveMethod = null;
    private static volatile long lastRefreshTimeOfIsAllowMtopPrefetchStatus;
    private static volatile long lastRefreshTimeOfWhiteUrlList;
    private static volatile long lastTimeRefreshMapConfig = 0;
    @Nullable
    private static volatile JSONObject mapConfigFromZcache = null;
    private static volatile Class prefetchXClass = null;
    private static volatile Method prefetchXMethod = null;
    private static volatile boolean usePrefetchX = true;
    private static volatile boolean usePrefetchXEmbedJSModule = true;
    private static volatile List<String> whiteUrlList = new ArrayList();

    public static String getRealPrefetchIdUrl(String str) {
        Uri parse;
        if (TextUtils.isEmpty(str) || (parse = Uri.parse(str)) == null) {
            return str;
        }
        return parse.getHost() + parse.getPath();
    }

    public static void handResultsSuccess(WXSDKInstance wXSDKInstance, String str, String str2) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("data", JSON.toJSONString(JSON.parseObject(str2)));
            hashMap.put("t", String.valueOf(System.currentTimeMillis()));
            String jSONString = JSON.toJSONString((Object) hashMap, true);
            if (WXEnvironment.isApkDebugable()) {
                String str3 = TAG;
                WXLogUtils.d(str3, "received mtop result:" + jSONString);
            }
            saveToStorage(wXSDKInstance, str, jSONString);
        } catch (Exception e) {
            WXLogUtils.e(e.getMessage());
        }
    }

    @Deprecated
    public static void handResultsFail(WXSDKInstance wXSDKInstance, String str, String str2, String str3) {
        if ("-1".equals(str2)) {
            readStorageAndSetData(wXSDKInstance, str, str3);
        }
    }

    public static boolean allowPreload() {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return true;
        }
        String config = configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, WXTBUtils.ORANGE_KEY, "true");
        if (config != null && "true".equalsIgnoreCase(config)) {
            return true;
        }
        commitFail(WXPrefetchConstant.ORANGE_CONFIG_ERROR, "preload is disabled");
        return false;
    }

    public static boolean usePrefetchX() {
        String config;
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null || (config = configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, "use_prefetchx", "true")) == null || !"true".equalsIgnoreCase(config)) {
            return false;
        }
        return true;
    }

    public static boolean usePrefetchXEmbedJSModule() {
        String config;
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null || (config = configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, "use_prefetchx_embed_jsmodule", "false")) == null || !"true".equalsIgnoreCase(config)) {
            return false;
        }
        return true;
    }

    static boolean allowMtopPrefetchStatus() {
        if (SystemClock.uptimeMillis() - lastRefreshTimeOfIsAllowMtopPrefetchStatus < 60000) {
            return isAllowMtopPrefetchStatus;
        }
        boolean z = true;
        try {
            IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
            if (configAdapter != null) {
                z = "true".equalsIgnoreCase(configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, "mtop_prefetch_status", "true"));
            }
        } catch (Throwable unused) {
        }
        lastRefreshTimeOfIsAllowMtopPrefetchStatus = SystemClock.uptimeMillis();
        isAllowMtopPrefetchStatus = z;
        return z;
    }

    public static List<String> getAllowWhiteUrlList() {
        JSONArray parseArray;
        if (SystemClock.uptimeMillis() - lastRefreshTimeOfWhiteUrlList < 60000) {
            return whiteUrlList;
        }
        ArrayList arrayList = new ArrayList();
        try {
            IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
            if (!(configAdapter == null || (parseArray = JSON.parseArray(configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, "mtop_prefetch_url_contains_white_list", "[]"))) == null)) {
                for (Object obj : parseArray.toArray()) {
                    arrayList.add(obj.toString());
                }
            }
        } catch (Throwable unused) {
        }
        lastRefreshTimeOfWhiteUrlList = SystemClock.uptimeMillis();
        whiteUrlList = arrayList;
        return arrayList;
    }

    @Deprecated
    static void readStorageAndSetData(final WXSDKInstance wXSDKInstance, final String str, final String str2) {
        if (!TextUtils.isEmpty(str)) {
            String str3 = TAG;
            WXLogUtils.e(str3, "mtop预加载 mtop请求错误,尝试读取storage数据 " + str2);
            try {
                IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
                if (iWXStorageAdapter != null) {
                    iWXStorageAdapter.getItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                        @SuppressLint({"WrongConstant"})
                        public void onReceived(Map<String, Object> map) {
                            JSONObject jSONObject;
                            IWXConnection createDefault;
                            try {
                                jSONObject = (JSONObject) JSON.parse(map.get("data").toString());
                            } catch (Exception unused) {
                                jSONObject = new JSONObject();
                            }
                            if (jSONObject == null) {
                                jSONObject = new JSONObject();
                            }
                            jSONObject.put("status", (Object) -1);
                            jSONObject.put("t", (Object) String.valueOf(System.currentTimeMillis()));
                            JSONObject jSONObject2 = new JSONObject();
                            jSONObject2.put(ApiConstants.ApiField.INFO, (Object) TextUtils.isEmpty(str2) ? "UNKOWN" : str2);
                            String str = "";
                            if (!(wXSDKInstance == null || (createDefault = WXConnectionFactory.createDefault(wXSDKInstance.getContext())) == null)) {
                                str = createDefault.getNetworkType();
                            }
                            jSONObject2.put("network", (Object) str);
                            jSONObject.put("errorExt", (Object) jSONObject2);
                            WXPrefetchUtil.saveToStorage(wXSDKInstance, str, JSON.toJSONString(jSONObject));
                        }
                    });
                }
            } catch (Exception e) {
                WXLogUtils.e("mtop预加载 mtop失败场景下，读取storage出错", (Throwable) e);
            }
        }
    }

    public static void saveToStorage(final WXSDKInstance wXSDKInstance, final String str, final String str2) {
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter != null) {
                iWXStorageAdapter.setItem(str, str2, new IWXStorageAdapter.OnResultReceivedListener() {
                    public void onReceived(Map<String, Object> map) {
                        if (map != null) {
                            String str = (String) map.get("result");
                            if (str == null || !str.equals("success")) {
                                WXPrefetchUtil.saveStatusToStorage(MtopPreloader.STATUS_SAVED_TO_STORAGE_FAIL, str);
                                String str2 = WXPrefetchUtil.TAG;
                                WXLogUtils.d(str2, "saveToStorage failed. onReceived map is " + map);
                                return;
                            }
                            WXPrefetchUtil.commitSuccess();
                            if (WXEnvironment.isApkDebugable()) {
                                String str3 = WXPrefetchUtil.TAG;
                                WXLogUtils.d(str3, "saveToStorage result:" + str + " | key:" + str + " | val:" + str2);
                            }
                            HashMap hashMap = new HashMap();
                            hashMap.put("result", "sucess");
                            hashMap.put("url", str);
                            try {
                                if (!(wXSDKInstance == null || wXSDKInstance.getContext() == null)) {
                                    wXSDKInstance.fireGlobalEventCallback("prefetchFinshed", map);
                                }
                            } catch (Exception unused) {
                            }
                            WXPrefetchUtil.saveStatusToStorage(MtopPreloader.STATUS_SAVED_TO_STORAGE, str);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            commitFail(WXPrefetchConstant.SAVE_STORAGE_ERROR, e.getMessage());
            saveStatusToStorage(MtopPreloader.STATUS_SAVED_TO_STORAGE_FAIL, str);
        }
    }

    public static void saveStatusToStorage(String str, String str2) {
        if (!allowMtopPrefetchStatus()) {
            String str3 = TAG;
            WXLogUtils.d(str3, "MtopPrefetchStatus is off by orange. " + str);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("status", (Object) str);
        jSONObject.put("t", (Object) Long.valueOf(new Date().getTime()));
        jSONObject.put("query", (Object) str2);
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter != null) {
                iWXStorageAdapter.setItem(MtopPreloader.STATUS_KEY, jSONObject.toJSONString(), (IWXStorageAdapter.OnResultReceivedListener) null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            commitFail(WXPrefetchConstant.SAVE_STATUS_STORAGE_ERROR, e.getMessage());
        }
    }

    public static void commitFail(String str, String str2) {
        AppMonitor.Alarm.commitFail(WXPrefetchConstant.MODULE_NAME, WXPrefetchConstant.MONITOR_NAME, str, str2);
        AppMonitor.Alarm.commitFail("PrefetchX", PFConstant.JSModule.TAG_BUNDLE, str, str2);
    }

    public static void commitSuccess() {
        AppMonitor.Alarm.commitSuccess(WXPrefetchConstant.MODULE_NAME, WXPrefetchConstant.MONITOR_NAME);
    }

    public static String replaceUrlParameter(String str, String str2, String str3) {
        String sb;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return str;
        }
        try {
            int indexOf = str.indexOf(str2 + SymbolExpUtil.SYMBOL_EQUAL);
            String encode = URLEncoder.encode(str3);
            if (indexOf != -1) {
                sb2.append(str.substring(0, indexOf));
                sb2.append(str2 + SymbolExpUtil.SYMBOL_EQUAL);
                sb2.append(encode);
                int indexOf2 = str.indexOf("&", indexOf);
                if (indexOf2 != -1) {
                    sb2.append(str.substring(indexOf2));
                }
                sb = sb2.toString();
            } else if (str.indexOf("?", indexOf) != -1) {
                sb2.append(str);
                sb2.append("&" + str2 + SymbolExpUtil.SYMBOL_EQUAL + encode);
                sb = sb2.toString();
            } else {
                sb2.append(str);
                sb2.append("?" + str2 + SymbolExpUtil.SYMBOL_EQUAL + encode);
                sb = sb2.toString();
            }
            return sb;
        } catch (Exception e) {
            commitFail("replace url error", "replace url error " + e);
            return str;
        }
    }

    public static String getMtopApiFromZcache(String str) {
        int i;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String str2 = WXPrefetchConstant.WH_PREFETCH_MAP_URL;
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            str2 = configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, "weex_prefetch_map_url", WXPrefetchConstant.WH_PREFETCH_MAP_URL);
        }
        try {
            i = Integer.parseInt(configAdapter.getConfig(WXTBUtils.ORANGE_GROUP, "weex_prefech_map_memery_refresh", "20"));
        } catch (Exception e) {
            WXLogUtils.w("error in weex_prefech_map_memery_refresh", (Throwable) e);
            i = 20;
        }
        JSONObject prefetchMapConfigFromZcacheWithMemoryCache = getPrefetchMapConfigFromZcacheWithMemoryCache(str2, i);
        if (prefetchMapConfigFromZcacheWithMemoryCache == null) {
            return null;
        }
        try {
            if (prefetchMapConfigFromZcacheWithMemoryCache.get(str) == null) {
                return null;
            }
            return JSONObject.toJSONString(prefetchMapConfigFromZcacheWithMemoryCache.get(str));
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
        r6 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.getStreamByUrl(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
        if (com.taobao.weex.WXEnvironment.isApkDebugable() == false) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
        r7 = TAG;
        com.taobao.weex.utils.WXLogUtils.d(r7, "zcahe中的parmas" + r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003e, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0043, code lost:
        if (android.text.TextUtils.isEmpty(r6) != false) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0045, code lost:
        r7 = com.alibaba.fastjson.JSON.parseObject(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0049, code lost:
        r6 = com.alibaba.aliweex.utils.WXPrefetchUtil.class;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004c, code lost:
        if (r7 == null) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        mapConfigFromZcache = r7;
        lastTimeRefreshMapConfig = android.os.SystemClock.elapsedRealtime();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0057, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005a, code lost:
        return r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005c, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.alibaba.fastjson.JSONObject getPrefetchMapConfigFromZcacheWithMemoryCache(java.lang.String r6, int r7) {
        /*
            java.lang.Class<com.alibaba.aliweex.utils.WXPrefetchUtil> r0 = com.alibaba.aliweex.utils.WXPrefetchUtil.class
            monitor-enter(r0)
            long r1 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x005d }
            long r3 = lastTimeRefreshMapConfig     // Catch:{ all -> 0x005d }
            r5 = 0
            long r1 = r1 - r3
            int r7 = r7 * 60
            int r7 = r7 * 1000
            long r3 = (long) r7     // Catch:{ all -> 0x005d }
            int r7 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r7 >= 0) goto L_0x001c
            com.alibaba.fastjson.JSONObject r7 = mapConfigFromZcache     // Catch:{ all -> 0x005d }
            if (r7 == 0) goto L_0x001c
            com.alibaba.fastjson.JSONObject r6 = mapConfigFromZcache     // Catch:{ all -> 0x005d }
            monitor-exit(r0)     // Catch:{ all -> 0x005d }
            return r6
        L_0x001c:
            monitor-exit(r0)     // Catch:{ all -> 0x005d }
            java.lang.String r6 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.getStreamByUrl(r6)
            boolean r7 = com.taobao.weex.WXEnvironment.isApkDebugable()
            if (r7 == 0) goto L_0x003e
            java.lang.String r7 = TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "zcahe中的parmas"
            r0.append(r1)
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r7, (java.lang.String) r0)
        L_0x003e:
            r7 = 0
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 != 0) goto L_0x0049
            com.alibaba.fastjson.JSONObject r7 = com.alibaba.fastjson.JSON.parseObject(r6)
        L_0x0049:
            java.lang.Class<com.alibaba.aliweex.utils.WXPrefetchUtil> r6 = com.alibaba.aliweex.utils.WXPrefetchUtil.class
            monitor-enter(r6)
            if (r7 == 0) goto L_0x0059
            mapConfigFromZcache = r7     // Catch:{ all -> 0x0057 }
            long r0 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x0057 }
            lastTimeRefreshMapConfig = r0     // Catch:{ all -> 0x0057 }
            goto L_0x0059
        L_0x0057:
            r7 = move-exception
            goto L_0x005b
        L_0x0059:
            monitor-exit(r6)     // Catch:{ all -> 0x0057 }
            return r7
        L_0x005b:
            monitor-exit(r6)     // Catch:{ all -> 0x0057 }
            throw r7
        L_0x005d:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x005d }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.utils.WXPrefetchUtil.getPrefetchMapConfigFromZcacheWithMemoryCache(java.lang.String, int):com.alibaba.fastjson.JSONObject");
    }

    public static String getMtopApiAndParams(WXSDKInstance wXSDKInstance, @NonNull String str) {
        try {
            JSONObject parseObject = JSON.parseObject(str);
            WXLogUtils.d(TAG, "get mtop api and params");
            if (parseObject == null) {
                return null;
            }
            if (parseObject.get("data") != null) {
                parseObject.put("param", parseObject.get("data"));
                parseObject.remove("data");
            }
            String jSONString = parseObject.toJSONString();
            if (WXEnvironment.isApkDebugable()) {
                String str2 = TAG;
                WXLogUtils.d(str2, "resolve mtop params success:" + jSONString);
            }
            return jSONString;
        } catch (Exception e) {
            handResultsFail(wXSDKInstance, str, "-1", "");
            String str3 = TAG;
            WXLogUtils.e(str3, "mtop params parse to json failed," + e.getMessage());
            commitFail(WXPrefetchConstant.JSON_PRASE_FAILED_ERROR, e.getMessage());
            return null;
        }
    }

    private static String truncateUrlPage(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split("[?]");
        if (str.length() <= 1 || split.length <= 1 || split[1] == null) {
            return null;
        }
        return split[1];
    }

    public static Map<String, String> getParams(String str) {
        HashMap hashMap = new HashMap();
        String truncateUrlPage = truncateUrlPage(str);
        if (truncateUrlPage == null) {
            return hashMap;
        }
        for (String split : truncateUrlPage.split("[&]")) {
            String[] split2 = split.split("[=]");
            if (split2.length > 1) {
                hashMap.put(split2[0], split2[1]);
            } else if (split2.length == 1 && !TextUtils.isEmpty(split2[0])) {
                hashMap.put(split2[0], "");
            }
        }
        return hashMap;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0099 A[SYNTHETIC, Splitter:B:41:0x0099] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String handleUrl(com.taobao.weex.WXSDKInstance r8, java.lang.String r9) {
        /*
            if (r9 == 0) goto L_0x000b
            java.lang.String r0 = "data_prefetch=false"
            boolean r0 = r9.contains(r0)
            if (r0 == 0) goto L_0x000b
            return r9
        L_0x000b:
            r0 = 0
            boolean r1 = usePrefetchX     // Catch:{ Throwable -> 0x008c }
            if (r1 == 0) goto L_0x0096
            boolean r1 = usePrefetchX()     // Catch:{ Throwable -> 0x008c }
            if (r1 == 0) goto L_0x0096
            java.lang.Class r1 = prefetchXClass     // Catch:{ Throwable -> 0x008c }
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x0020
            java.lang.reflect.Method r1 = prefetchXMethod     // Catch:{ Throwable -> 0x008c }
            if (r1 != 0) goto L_0x005f
        L_0x0020:
            java.lang.Class<com.alibaba.aliweex.utils.WXPrefetchUtil> r1 = com.alibaba.aliweex.utils.WXPrefetchUtil.class
            monitor-enter(r1)     // Catch:{ Throwable -> 0x008c }
            java.lang.Class r4 = prefetchXClass     // Catch:{ all -> 0x0089 }
            if (r4 == 0) goto L_0x002b
            java.lang.reflect.Method r4 = prefetchXMethod     // Catch:{ all -> 0x0089 }
            if (r4 != 0) goto L_0x005e
        L_0x002b:
            com.taobao.weex.WXSDKManager r4 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x0089 }
            com.taobao.weex.adapter.ClassLoaderAdapter r4 = r4.getClassLoaderAdapter()     // Catch:{ all -> 0x0089 }
            java.lang.String r5 = "PrefetchXData"
            java.lang.String r6 = "com.alibaba.android.prefetchx.core.data.SupportWeex"
            android.app.Application r7 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ all -> 0x0089 }
            android.content.Context r7 = r7.getApplicationContext()     // Catch:{ all -> 0x0089 }
            java.lang.Class r4 = r4.getModuleClass(r5, r6, r7)     // Catch:{ all -> 0x0089 }
            prefetchXClass = r4     // Catch:{ all -> 0x0089 }
            java.lang.Class r4 = prefetchXClass     // Catch:{ all -> 0x0089 }
            java.lang.String r5 = "prefetchData"
            java.lang.Class[] r6 = new java.lang.Class[r2]     // Catch:{ all -> 0x0089 }
            java.lang.Class<com.taobao.weex.WXSDKInstance> r7 = com.taobao.weex.WXSDKInstance.class
            r6[r0] = r7     // Catch:{ all -> 0x0089 }
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            r6[r3] = r7     // Catch:{ all -> 0x0089 }
            java.lang.reflect.Method r4 = r4.getDeclaredMethod(r5, r6)     // Catch:{ all -> 0x0089 }
            prefetchXMethod = r4     // Catch:{ all -> 0x0089 }
            java.lang.reflect.Method r4 = prefetchXMethod     // Catch:{ all -> 0x0089 }
            r4.setAccessible(r3)     // Catch:{ all -> 0x0089 }
        L_0x005e:
            monitor-exit(r1)     // Catch:{ all -> 0x0089 }
        L_0x005f:
            java.lang.reflect.Method r1 = prefetchXMethod     // Catch:{ Throwable -> 0x008c }
            if (r1 == 0) goto L_0x007f
            java.lang.Class r1 = prefetchXClass     // Catch:{ Throwable -> 0x008c }
            if (r1 == 0) goto L_0x007f
            java.lang.reflect.Method r1 = prefetchXMethod     // Catch:{ Throwable -> 0x008c }
            java.lang.Class r4 = prefetchXClass     // Catch:{ Throwable -> 0x008c }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x008c }
            r2[r0] = r8     // Catch:{ Throwable -> 0x008c }
            r2[r3] = r9     // Catch:{ Throwable -> 0x008c }
            java.lang.Object r1 = r1.invoke(r4, r2)     // Catch:{ Throwable -> 0x008c }
            boolean r2 = r1 instanceof java.lang.String     // Catch:{ Throwable -> 0x008c }
            if (r2 == 0) goto L_0x0096
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x008c }
            r0 = 1
            goto L_0x0097
        L_0x007f:
            usePrefetchX = r0     // Catch:{ Throwable -> 0x008c }
            java.lang.String r1 = "prefetchx_class_error"
            java.lang.String r2 = "no class or method"
            commitFail(r1, r2)     // Catch:{ Throwable -> 0x008c }
            goto L_0x0096
        L_0x0089:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0089 }
            throw r2     // Catch:{ Throwable -> 0x008c }
        L_0x008c:
            r1 = move-exception
            java.lang.String r2 = "prefetchx_error"
            java.lang.String r1 = r1.getMessage()
            commitFail(r2, r1)
        L_0x0096:
            r1 = r9
        L_0x0097:
            if (r0 != 0) goto L_0x00ab
            java.lang.String r8 = com.alibaba.aliweex.plugin.MtopPreloader.preload(r9, r8)     // Catch:{ Throwable -> 0x009e }
            goto L_0x00ac
        L_0x009e:
            r8 = move-exception
            r8.printStackTrace()
            java.lang.String r9 = "mtop_preload_error"
            java.lang.String r8 = r8.getMessage()
            commitFail(r9, r8)
        L_0x00ab:
            r8 = r1
        L_0x00ac:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.utils.WXPrefetchUtil.handleUrl(com.taobao.weex.WXSDKInstance, java.lang.String):java.lang.String");
    }

    public static Pair<String, Object> evolve(Context context, String str, String str2) {
        try {
            if (!usePrefetchXEmbedJSModule || !usePrefetchXEmbedJSModule()) {
                return null;
            }
            if (jSModuleIntegrationClass == null || jSModuleIntegrationEvolveMethod == null) {
                synchronized (WXPrefetchUtil.class) {
                    if (jSModuleIntegrationClass == null) {
                        jSModuleIntegrationClass = WXSDKManager.getInstance().getClassLoaderAdapter().getModuleClass("PFJSModuleIntegration", "com.alibaba.android.prefetchx.core.jsmodule.PFJSModuleIntegration", WXEnvironment.getApplication().getApplicationContext());
                    }
                    if (jSModuleIntegrationEvolveMethod == null) {
                        jSModuleIntegrationEvolveMethod = jSModuleIntegrationClass.getDeclaredMethod("evolve", new Class[]{Context.class, String.class, String.class});
                        jSModuleIntegrationEvolveMethod.setAccessible(true);
                    }
                }
            }
            Object newInstance = jSModuleIntegrationClass != null ? jSModuleIntegrationClass.newInstance() : null;
            if (jSModuleIntegrationClass == null || jSModuleIntegrationEvolveMethod == null || newInstance == null) {
                usePrefetchXEmbedJSModule = false;
                commitFail("-55001", "prefetchx_evolve_class_error");
                return null;
            }
            Object invoke = jSModuleIntegrationEvolveMethod.invoke(newInstance, new Object[]{context, str, str2});
            if (invoke instanceof String) {
                return new Pair<>(invoke.toString(), newInstance);
            }
            return null;
        } catch (Throwable th) {
            commitFail("-55002", "prefetchx_evolve_error" + th.getMessage() + " | " + WXLogUtils.getStackTrace(th));
            return null;
        }
    }

    public static Map<String, Object> degenerate(Context context, Object obj, String str) {
        HashMap hashMap = null;
        if (obj == null) {
            return null;
        }
        if (!usePrefetchXEmbedJSModule || !usePrefetchXEmbedJSModule()) {
            return null;
        }
        if (jSModuleIntegrationClass == null || jSModuleIntegrationDegenerateMethod == null) {
            synchronized (WXPrefetchUtil.class) {
                if (jSModuleIntegrationClass == null) {
                    jSModuleIntegrationClass = WXSDKManager.getInstance().getClassLoaderAdapter().getModuleClass("PFJSModuleIntegration", "com.alibaba.android.prefetchx.core.jsmodule.PFJSModuleIntegration", WXEnvironment.getApplication().getApplicationContext());
                }
                if (jSModuleIntegrationDegenerateMethod == null) {
                    jSModuleIntegrationDegenerateMethod = jSModuleIntegrationClass.getDeclaredMethod("degenerate", new Class[]{Context.class, String.class, Map.class});
                    jSModuleIntegrationDegenerateMethod.setAccessible(true);
                }
            }
        }
        try {
            if (jSModuleIntegrationClass == null || jSModuleIntegrationDegenerateMethod == null || obj == null) {
                usePrefetchXEmbedJSModule = false;
                commitFail("-55011", "prefetchx_degenerate_class_error");
                return null;
            }
            Object invoke = jSModuleIntegrationDegenerateMethod.invoke(obj, new Object[]{context, str, new HashMap()});
            if (!(invoke instanceof Pair)) {
                return null;
            }
            Pair pair = (Pair) invoke;
            HashMap hashMap2 = new HashMap();
            try {
                hashMap2.put("fatWeexUrl", pair.first);
                hashMap2.put("fatBundleUrl", pair.second);
                hashMap2.put("mPrefetchXIntegration", obj);
                hashMap2.put("originalThinBundleUrl", str);
                return hashMap2;
            } catch (Throwable th) {
                th = th;
                hashMap = hashMap2;
                commitFail("-55012", "prefetchx_degenerate_error" + th.getMessage() + " | " + WXLogUtils.getStackTrace(th));
                return hashMap;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
