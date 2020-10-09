package com.alibaba.android.prefetchx.core.data;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.alibaba.android.prefetchx.core.data.adapter.PFDataCallback;
import com.alibaba.android.prefetchx.core.data.adapter.PFDataUrlKeysAdapter;
import com.alibaba.android.prefetchx.core.file.WXFilePrefetchModule;
import com.alibaba.android.prefetchx.core.image.PFImage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import com.taobao.weaver.prefetch.PrefetchDataResponse;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.util.ReflectUtil;

public class PFMtop {
    private static final String MTOP_CONFIG_AVFS_KEY = "data_mtop_config_json";
    private static final String STATUS_KEY = "prefetchx_data_status";
    private static volatile PFMtop instance;
    public PFDataCallback dataCallback = null;
    public PFDataUrlKeysAdapter dataUrlKeysAdapter = null;
    public String lastGeoLatitude = "";
    public String lastGeolongitude = "";
    /* access modifiers changed from: private */
    public long lastRefreshTimeOfGeoInfo = 0;
    private long lastTimeRefreshMapConfig = 0;
    /* access modifiers changed from: private */
    public RemoteConfigSpec.IDataModuleRemoteConfig mDataModuleRemoteConfig = PrefetchX.getInstance().getGlobalOnlineConfigManager().getDataModuleConfig();
    private JSONObject mapConfigFromZcache = null;
    public StorageInterface<String> memoryStorage = null;
    private MtopSender mtopSender = new DefaultMtopSender();
    public StorageInterface<String> weexStorage = null;

    public interface MtopCallback {
        void onFailure(@Nullable String str);

        void onSuccess(@NonNull String str);
    }

    public interface MtopSender {
        void sendMtopRequestData(@NonNull JSONObject jSONObject, @NonNull MtopCallback mtopCallback);
    }

    private PFMtop() {
        if (this.mDataModuleRemoteConfig.isRefreshGeoWhenInit()) {
            refreshGeoInfo(PrefetchX.sContext);
        }
    }

    public static PFMtop getInstance() {
        if (instance == null) {
            synchronized (PFMtop.class) {
                if (instance == null) {
                    instance = new PFMtop();
                }
            }
        }
        return instance;
    }

    public void setMtopSender(MtopSender mtopSender2) {
        this.mtopSender = mtopSender2;
    }

    public String prefetch(String str) {
        return doPrefetch(PrefetchX.sContext, (WXSDKInstance) null, str, (PrefetchDataCallback) null, (Map<String, Object>) null);
    }

    public String prefetch(String str, @Nullable Map<String, Object> map) {
        return doPrefetch(PrefetchX.sContext, (WXSDKInstance) null, str, (PrefetchDataCallback) null, map);
    }

    public String prefetch(String str, PrefetchDataCallback prefetchDataCallback) {
        return doPrefetch(PrefetchX.sContext, (WXSDKInstance) null, str, prefetchDataCallback, (Map<String, Object>) null);
    }

    public String prefetch(Context context, String str) {
        return doPrefetch(context, (WXSDKInstance) null, str, (PrefetchDataCallback) null, (Map<String, Object>) null);
    }

    public String prefetch(Context context, String str, @Nullable Map<String, Object> map) {
        return doPrefetch(context, (WXSDKInstance) null, str, (PrefetchDataCallback) null, map);
    }

    public String prefetch(Context context, String str, PrefetchDataCallback prefetchDataCallback) {
        return doPrefetch(context, (WXSDKInstance) null, str, prefetchDataCallback, (Map<String, Object>) null);
    }

    public String prefetch(WXSDKInstance wXSDKInstance, String str) {
        return doPrefetch(wXSDKInstance.getContext(), wXSDKInstance, str, (PrefetchDataCallback) null, (Map<String, Object>) null);
    }

    private String doPrefetch(Context context, @Nullable WXSDKInstance wXSDKInstance, String str, PrefetchDataCallback prefetchDataCallback, Map<String, Object> map) {
        if (!this.mDataModuleRemoteConfig.isDataEnable()) {
            PFLog.Data.w("Oh! I am disabled", new Throwable[0]);
            return str;
        } else if (TextUtils.isEmpty(str)) {
            PFLog.Data.w("empty jsModuleUrl", new Throwable[0]);
            return str;
        } else {
            Uri parse = Uri.parse(str);
            if (!parse.isHierarchical()) {
                PFLog.Data.w("not valid jsModuleUrl. " + str, new Throwable[0]);
                return str;
            } else if (needLogin(parse)) {
                return str;
            } else {
                Map<String, Object> addParam = addParam(addParam(map, "originalUrl", str), LoginConstant.START_TIME, Long.valueOf(SystemClock.uptimeMillis()));
                String generatePrefetchString = generatePrefetchString(context, processUrl(context, this.dataCallback.beforeProcessUrl(context, parse, addParam), addParam), addParam);
                if (TextUtils.isEmpty(generatePrefetchString)) {
                    return str;
                }
                Map<String, Object> addParam2 = addParam(addParam, WXFilePrefetchModule.PREFETCH_MODULE_NAME, generatePrefetchString);
                JSONObject mtopApiAndParams = getMtopApiAndParams(context, generatePrefetchString);
                String changeKeyBeforeMtopSend = this.dataCallback.changeKeyBeforeMtopSend(generatePrefetchString, addParam2);
                saveStatusToStorage("init", changeKeyBeforeMtopSend);
                if (mtopApiAndParams == null) {
                    return str;
                }
                String replaceUrlParameter = PFUtil.replaceUrlParameter(str, "wh_prefetch", changeKeyBeforeMtopSend);
                processMtop(context, changeKeyBeforeMtopSend, mtopApiAndParams, prefetchDataCallback, addParam(addParam2, "assembledUrl", replaceUrlParameter));
                saveStatusToStorage(PFConstant.Data.STATUS_QUEUE, changeKeyBeforeMtopSend);
                return replaceUrlParameter;
            }
        }
    }

    private Map<String, Object> addParam(Map<String, Object> map, String str, Object obj) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(str, obj);
        return map;
    }

    /* access modifiers changed from: protected */
    public boolean isUrlValidForPrefetch(@NonNull Uri uri) {
        String uri2 = uri.toString();
        boolean isUrlInMappingJSONFile = isUrlInMappingJSONFile(uri2);
        String queryParameter = uri.getQueryParameter(this.dataUrlKeysAdapter.getKeyFlag());
        String queryParameter2 = uri.getQueryParameter(this.dataUrlKeysAdapter.getKeyEnable());
        String queryParameter3 = uri.getQueryParameter("mtop_prefetch_enable");
        String queryParameter4 = uri.getQueryParameter(this.dataUrlKeysAdapter.getKeyId());
        if (!TextUtils.isEmpty(queryParameter) || !TextUtils.isEmpty(queryParameter2) || !TextUtils.isEmpty(queryParameter3) || !TextUtils.isEmpty(queryParameter4) || isUrlInMappingJSONFile) {
            return true;
        }
        PFLog.Data.d("Not support data_prefetch ", uri2);
        return false;
    }

    private boolean needLogin(@Nullable Uri uri) {
        if (uri == null) {
            return false;
        }
        String queryParameter = uri.getQueryParameter(this.dataUrlKeysAdapter.getKeyNeedLogin());
        if (TextUtils.isEmpty(queryParameter) || ((!"1".equals(queryParameter) && !"true".equals(queryParameter)) || RemoteLogin.isSessionValid())) {
            return false;
        }
        PFLog.Data.w("jsModuleUrl needlogin, but now user is NOT login. " + uri.toString(), new Throwable[0]);
        PFMonitor.Data.fail(PFConstant.Data.PF_DATA_NEED_LOGIN_ERROR, "user not login exception", new Object[0]);
        return true;
    }

    public boolean isUrlInMappingJSONFile(String str) {
        try {
            String mtopConfigKeyFromUrl = getInstance().getMtopConfigKeyFromUrl(Uri.parse(str));
            if (TextUtils.isEmpty(mtopConfigKeyFromUrl) || this.mapConfigFromZcache == null || !this.mapConfigFromZcache.containsKey(mtopConfigKeyFromUrl)) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            PFLog.Data.w("error in configMapUrls", th);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Uri processUrl(Context context, Uri uri, Map<String, Object> map) {
        String uri2 = uri.toString();
        boolean z = false;
        if (uri.getBooleanQueryParameter(this.dataUrlKeysAdapter.getKeyRefreshGeo(), false)) {
            refreshGeoInfo(context);
        }
        boolean z2 = true;
        if (uri2.contains(PFConstant.Data.PF_DATA_GEO_LONGITUDE) && !TextUtils.isEmpty(this.lastGeolongitude)) {
            uri2 = uri2.replace(PFConstant.Data.PF_DATA_GEO_LONGITUDE, this.lastGeolongitude);
            z = true;
        }
        if (!uri2.contains(PFConstant.Data.PF_DATA_GEO_LATITUDE) || TextUtils.isEmpty(this.lastGeoLatitude)) {
            z2 = z;
        } else {
            uri2 = uri2.replace(PFConstant.Data.PF_DATA_GEO_LATITUDE, this.lastGeoLatitude);
        }
        return z2 ? Uri.parse(uri2) : uri;
    }

    @Nullable
    public String generatePrefetchString(Context context, Uri uri, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        String mtopConfigByUrl = this.dataCallback.getMtopConfigByUrl(context, uri, map);
        if (TextUtils.isEmpty(mtopConfigByUrl)) {
            mtopConfigByUrl = getMtopConfig(uri);
        }
        String afterMtopConfigAssembled = this.dataCallback.afterMtopConfigAssembled(context, uri, mtopConfigByUrl, map);
        return !TextUtils.isEmpty(afterMtopConfigAssembled) ? afterMtopConfigAssembled : mtopConfigByUrl;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getMtopConfig(Uri uri) {
        if (!isUrlValidForPrefetch(uri)) {
            return null;
        }
        String queryParameter = uri.getQueryParameter(getInstance().dataUrlKeysAdapter.getKeyFlag());
        if (TextUtils.isEmpty(queryParameter)) {
            String mtopConfigKeyFromUrl = getMtopConfigKeyFromUrl(uri);
            if (!TextUtils.isEmpty(mtopConfigKeyFromUrl)) {
                queryParameter = getPrefetchStringFromMtopConfigKey(mtopConfigKeyFromUrl);
            }
        }
        if (TextUtils.isEmpty(queryParameter)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (String next : uri.getQueryParameterNames()) {
            hashMap.put(next, uri.getQueryParameter(next));
        }
        if (uri.toString().contains("#")) {
            hashMap.putAll(queryMap(uri.toString()));
        }
        if (!TextUtils.isEmpty(this.lastGeoLatitude)) {
            hashMap.put("_geo_latitude_", this.lastGeoLatitude);
        }
        if (!TextUtils.isEmpty(this.lastGeolongitude)) {
            hashMap.put("_geo_longitude_", this.lastGeolongitude);
        }
        String replaceDynamicParam = replaceDynamicParam(queryParameter, hashMap);
        PFLog.Data.d("prefetch after replaceDynamicParam is ", replaceDynamicParam);
        return replaceDynamicParam;
    }

    /* access modifiers changed from: private */
    @Nullable
    public String getPrefetchStringFromMtopConfigKey(String str) {
        JSONObject mtopConfigWithMemoryCache = getMtopConfigWithMemoryCache(this.mDataModuleRemoteConfig.getConfigMapUrl(), this.mDataModuleRemoteConfig.getConfigMapMaxAgeInMemory());
        if ("nothing_but_prefetchx_init".equals(str)) {
            return null;
        }
        if (mtopConfigWithMemoryCache == null) {
            PFLog.Data.w("config is null", new Throwable[0]);
            return null;
        } else if (mtopConfigWithMemoryCache.containsKey(str)) {
            return JSONObject.toJSONString(mtopConfigWithMemoryCache.get(str));
        } else {
            PFLog.Data.w("config not contains key " + str, new Throwable[0]);
            return null;
        }
    }

    public void createMtopConfigCacheAndConfigMapUrls() {
        PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
            public void run() {
                PFLog.Data.d("going to create data prefetch mapping. delayed ", Integer.valueOf(PFMtop.this.mDataModuleRemoteConfig.getInitMtopConfigProcessDelay()), " ms to start.");
                String unused = PFMtop.this.getPrefetchStringFromMtopConfigKey("nothing_but_prefetchx_init");
            }
        }, this.mDataModuleRemoteConfig.getInitMtopConfigProcessDelay());
    }

    @NonNull
    private String getMtopConfigKeyFromUrl(Uri uri) {
        if (uri == null) {
            return "";
        }
        String queryParameter = uri.getQueryParameter(getInstance().dataUrlKeysAdapter.getKeyId());
        if (!TextUtils.isEmpty(queryParameter)) {
            return queryParameter;
        }
        String str = uri.getHost() + uri.getPath();
        if (str.endsWith("\\") || str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        r8 = com.alibaba.android.prefetchx.PrefetchX.getInstance().getAssetAdapter().getAssetFromZCache(r7);
        r0 = new java.lang.Object[4];
        r0[0] = "mtop config from ZCache (";
        r0[1] = r7;
        r0[2] = ") is ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
        if (r8 != null) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
        r3 = com.taobao.weex.BuildConfig.buildJavascriptFrameworkVersion;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003d, code lost:
        r3 = r8.length() + " length";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0052, code lost:
        r0[3] = r3;
        com.alibaba.android.prefetchx.PFLog.Data.d(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005b, code lost:
        if (android.text.TextUtils.isEmpty(r8) == false) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005f, code lost:
        if (r6.mapConfigFromZcache == null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0063, code lost:
        return r6.mapConfigFromZcache;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0064, code lost:
        com.alibaba.android.prefetchx.PrefetchX.getInstance().getThreadExecutor().executeImmediately(new com.alibaba.android.prefetchx.core.data.PFMtop.AnonymousClass2(r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0078, code lost:
        if (android.text.TextUtils.isEmpty(r8) == false) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007a, code lost:
        com.alibaba.android.prefetchx.PFMonitor.Data.fail(com.alibaba.android.prefetchx.PFConstant.Data.PF_DATA_ZIP_PACKAGE_CACHE, "package cache get error by data_prefetch=true at " + r7, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0093, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0098, code lost:
        return setMapConfig(r8);
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.fastjson.JSONObject getMtopConfigWithMemoryCache(final java.lang.String r7, int r8) {
        /*
            r6 = this;
            java.lang.Class<com.alibaba.android.prefetchx.core.data.PFMtop> r0 = com.alibaba.android.prefetchx.core.data.PFMtop.class
            monitor-enter(r0)
            long r1 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x0099 }
            long r3 = r6.lastTimeRefreshMapConfig     // Catch:{ all -> 0x0099 }
            r5 = 0
            long r1 = r1 - r3
            int r8 = r8 * 1000
            long r3 = (long) r8     // Catch:{ all -> 0x0099 }
            int r8 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r8 >= 0) goto L_0x001a
            com.alibaba.fastjson.JSONObject r8 = r6.mapConfigFromZcache     // Catch:{ all -> 0x0099 }
            if (r8 == 0) goto L_0x001a
            com.alibaba.fastjson.JSONObject r7 = r6.mapConfigFromZcache     // Catch:{ all -> 0x0099 }
            monitor-exit(r0)     // Catch:{ all -> 0x0099 }
            return r7
        L_0x001a:
            monitor-exit(r0)     // Catch:{ all -> 0x0099 }
            com.alibaba.android.prefetchx.PrefetchX r8 = com.alibaba.android.prefetchx.PrefetchX.getInstance()
            com.alibaba.android.prefetchx.adapter.AssetAdapter r8 = r8.getAssetAdapter()
            java.lang.String r8 = r8.getAssetFromZCache(r7)
            r0 = 4
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.String r1 = "mtop config from ZCache ("
            r2 = 0
            r0[r2] = r1
            r1 = 1
            r0[r1] = r7
            r1 = 2
            java.lang.String r3 = ") is "
            r0[r1] = r3
            r1 = 3
            if (r8 != 0) goto L_0x003d
            java.lang.String r3 = "null"
            goto L_0x0052
        L_0x003d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            int r4 = r8.length()
            r3.append(r4)
            java.lang.String r4 = " length"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
        L_0x0052:
            r0[r1] = r3
            com.alibaba.android.prefetchx.PFLog.Data.d(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            if (r0 == 0) goto L_0x0074
            com.alibaba.fastjson.JSONObject r0 = r6.mapConfigFromZcache
            if (r0 == 0) goto L_0x0064
            com.alibaba.fastjson.JSONObject r7 = r6.mapConfigFromZcache
            return r7
        L_0x0064:
            com.alibaba.android.prefetchx.PrefetchX r0 = com.alibaba.android.prefetchx.PrefetchX.getInstance()
            com.alibaba.android.prefetchx.adapter.IThreadExecutor r0 = r0.getThreadExecutor()
            com.alibaba.android.prefetchx.core.data.PFMtop$2 r1 = new com.alibaba.android.prefetchx.core.data.PFMtop$2
            r1.<init>(r7)
            r0.executeImmediately(r1)
        L_0x0074:
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            if (r0 == 0) goto L_0x0094
            java.lang.String r8 = "-30006"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "package cache get error by data_prefetch=true at "
            r0.append(r1)
            r0.append(r7)
            java.lang.String r7 = r0.toString()
            java.lang.Object[] r0 = new java.lang.Object[r2]
            com.alibaba.android.prefetchx.PFMonitor.Data.fail(r8, r7, r0)
            r7 = 0
            return r7
        L_0x0094:
            com.alibaba.fastjson.JSONObject r7 = r6.setMapConfig(r8)
            return r7
        L_0x0099:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0099 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.data.PFMtop.getMtopConfigWithMemoryCache(java.lang.String, int):com.alibaba.fastjson.JSONObject");
    }

    /* access modifiers changed from: private */
    public JSONObject setMapConfig(String str) {
        JSONObject jSONObject = null;
        try {
            if (!TextUtils.isEmpty(str)) {
                jSONObject = JSON.parseObject(str);
            }
        } catch (Exception e) {
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_JSON_PRASE_FAILED_ERROR, !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "data prase error", new Object[0]);
        }
        synchronized (PFMtop.class) {
            if (jSONObject != null) {
                try {
                    this.mapConfigFromZcache = jSONObject;
                    this.lastTimeRefreshMapConfig = SystemClock.elapsedRealtime();
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return jSONObject;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    @NonNull
    public String replaceDynamicParam(String str, Map<String, String> map) {
        String str2;
        String[] split;
        String str3;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String matchReplace = matchReplace("(\\$).*?(\\$)", map, str, "\\$", 0);
        String matchReplace2 = !TextUtils.isEmpty(matchReplace) ? matchReplace("(#).*?(#)", map, matchReplace, "#", 1) : matchReplace;
        if (!TextUtils.isEmpty(matchReplace2)) {
            matchReplace2 = matchReplace("(@).*?(@)", map, matchReplace2, DinamicConstant.DINAMIC_PREFIX_AT, 2);
        }
        if (matchReplace2 == null || !matchReplace2.contains("prefetchxMerge")) {
            return matchReplace2;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            String valueOf = String.valueOf(PFUtil.getJSONValueRecursion(JSON.parseObject(matchReplace2), "prefetchxMerge"));
            if (valueOf != null && valueOf.length() > 16 && valueOf.contains("},")) {
                String[] split2 = valueOf.trim().substring("prefetchxMerge".length() + 1, valueOf.length() - 1).split("},");
                if (split2 != null) {
                    for (int length = split2.length - 1; length >= 0; length--) {
                        if (!split2[length].endsWith("}")) {
                            str3 = split2[length] + "}";
                        } else {
                            str3 = split2[length];
                        }
                        jSONObject.putAll(JSON.parseObject(str3));
                    }
                }
            } else if (!(valueOf == null || !valueOf.contains(",") || (split = valueOf.trim().substring("prefetchxMerge".length() + 1, valueOf.length() - 1).split(AVFSCacheConstants.COMMA_SEP)) == null)) {
                for (int length2 = split.length - 1; length2 >= 0; length2--) {
                    if (split[length2].length() != 0) {
                        JSONObject parseObject = JSON.parseObject(split[length2]);
                        for (String next : parseObject.keySet()) {
                            String string = parseObject.getString(next);
                            if (string != null && string.length() > 0) {
                                jSONObject.put(next, (Object) string);
                            }
                        }
                    }
                }
            }
            if (jSONObject.keySet().size() > 0) {
                str2 = matchReplace2.replace("\"" + valueOf + "\"", "\"" + jSONObject.toJSONString().replace("\"", "\\\"") + "\"");
            } else {
                str2 = matchReplace2.replace("\"" + valueOf + "\"", "\"\"");
            }
            return str2;
        } catch (Throwable th) {
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_MERGE_ERROR, th.getMessage() + Operators.SPACE_STR + str, new Object[0]);
            return matchReplace2;
        }
    }

    @NonNull
    private String matchReplace(String str, Map<String, String> map, String str2, String str3, int i) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return "";
        }
        Matcher matcher = Pattern.compile(str).matcher(str2);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String str4 = "";
            if (matcher.group() != null) {
                str4 = matcher.group().replaceAll(str3, "");
            }
            String str5 = "";
            if (str4 == null || !str4.contains("||")) {
                str5 = map.get(str4);
            } else {
                String[] split = str4.split("\\|\\|");
                if (split != null) {
                    int i2 = 0;
                    while (i2 < split.length && ((str5 = map.get(split[i2])) == null || TextUtils.isEmpty(str5))) {
                        i2++;
                    }
                }
            }
            if (i == 0) {
                str5 = Uri.decode(str5);
            } else if (i == 2) {
                str5 = Uri.encode(str5);
            }
            if (str5 == null || TextUtils.isEmpty(str5)) {
                matcher.appendReplacement(stringBuffer, "");
            } else {
                matcher.appendReplacement(stringBuffer, str5);
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public void doSaveToStorage(String str, String str2) {
        try {
            if (this.weexStorage == null && this.memoryStorage == null) {
                PFLog.Data.w("both weex and memory storage is not available. There must be one place for PrefetchX to save result", new Throwable[0]);
                return;
            }
            if (this.weexStorage != null) {
                this.weexStorage.save(str, str2);
            }
            if (this.memoryStorage != null) {
                this.memoryStorage.save(str, str2);
            }
        } catch (Exception e) {
            PFLog.Data.w("error in doSaveToStorage key:" + str + ", value:" + str2, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, e.getMessage(), new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public String doReadFromStorage(String str) {
        try {
            if (this.weexStorage != null) {
                return this.weexStorage.read(str);
            }
            if (this.memoryStorage != null) {
                return this.memoryStorage.read(str);
            }
            return "";
        } catch (Exception e) {
            PFLog.Data.w("error in doReadFromStorage key:" + str, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, e.getMessage(), new Object[0]);
            return "";
        }
    }

    /* access modifiers changed from: protected */
    public void saveStatusToStorage(final String str, final String str2) {
        if (this.mDataModuleRemoteConfig.isDataStatueReportEnable()) {
            PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                public void run() {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("status", (Object) str);
                    jSONObject.put("t", (Object) Long.valueOf(System.currentTimeMillis()));
                    jSONObject.put("query", (Object) str2);
                    PFMtop.this.doSaveToStorage(PFMtop.STATUS_KEY, jSONObject.toJSONString());
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    @Nullable
    public JSONObject getMtopApiAndParams(Context context, @NonNull String str) {
        try {
            return JSON.parseObject(str);
        } catch (Exception e) {
            PFLog.Data.w("mtop params parse to json failed," + e.getMessage(), new Throwable[0]);
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_JSON_PRASE_FAILED_ERROR, e.getMessage(), new Object[0]);
            resaveLastResult(str);
            return null;
        }
    }

    private void resaveLastResult(final String str) {
        PFMonitor.Data.fail(PFConstant.USELESS_CODE, "error in parseObject(prefetch). use last storage to save again.", new Object[0]);
        PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
            public void run() {
                String doReadFromStorage = PFMtop.this.doReadFromStorage(str);
                if (doReadFromStorage != null) {
                    JSONObject jSONObject = (JSONObject) JSON.parse(doReadFromStorage);
                    if (jSONObject == null) {
                        jSONObject = new JSONObject();
                    }
                    jSONObject.put("status", (Object) -1);
                    jSONObject.put("t", (Object) String.valueOf(System.currentTimeMillis()));
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(ApiConstants.ApiField.INFO, (Object) "error in parse prefetch");
                    jSONObject2.put("network", (Object) "");
                    jSONObject.put("errorExt", (Object) jSONObject2);
                    PFMtop.this.doSaveToStorage(str, JSON.toJSONString(jSONObject));
                }
            }
        });
    }

    private Map<String, String> queryMap(String str) {
        HashMap hashMap = new HashMap();
        if (str == null || !str.contains("?")) {
            return hashMap;
        }
        int indexOf = str.indexOf("?") + 1;
        do {
            int indexOf2 = str.indexOf(38, indexOf);
            if (indexOf2 == -1) {
                indexOf2 = str.length();
            }
            int indexOf3 = str.indexOf(61, indexOf);
            if (indexOf3 > indexOf2 || indexOf3 == -1) {
                indexOf3 = indexOf2;
            }
            if (indexOf3 > indexOf) {
                String substring = str.substring(indexOf, indexOf3);
                int i = indexOf3 + 1;
                hashMap.put(Uri.decode(substring), Uri.decode(i <= indexOf2 ? str.substring(i, indexOf2) : ""));
            }
            indexOf = indexOf2 + 1;
        } while (indexOf < str.length());
        return hashMap;
    }

    /* access modifiers changed from: protected */
    public void processMtop(Context context, String str, JSONObject jSONObject, PrefetchDataCallback prefetchDataCallback, Map<String, Object> map) {
        if (this.dataCallback != null ? this.dataCallback.beforeMtopSend(context, jSONObject, prefetchDataCallback, map) : true) {
            sendMtopRequestInternal(jSONObject, str, prefetchDataCallback, map);
        } else {
            PFLog.Data.w("beforeMtopSend() return false means ignore the actual mtop send this time.", new Throwable[0]);
        }
        if (this.dataCallback != null) {
            this.dataCallback.afterMtopSend(context, jSONObject, map);
        }
    }

    /* access modifiers changed from: private */
    public void mtopSuccess(final String str, String str2, PrefetchDataCallback prefetchDataCallback, Map<String, Object> map) {
        String str3;
        if (this.dataCallback != null) {
            String onMtopReturn = this.dataCallback.onMtopReturn(true, str2, map);
            if (PFUtil.isDebug() && (str2 == null || !str2.equals(onMtopReturn))) {
                PFLog.Data.w("onMtopReturn() calls change from " + str2 + " to " + onMtopReturn + ", and will use the second one to process", new Throwable[0]);
            }
            str2 = onMtopReturn;
        }
        final String changeValueBeforeMtopSave = this.dataCallback != null ? this.dataCallback.changeValueBeforeMtopSave(str, str2, map) : str2;
        String str4 = "";
        if (map.get(LoginConstant.START_TIME) instanceof Long) {
            str4 = (SystemClock.uptimeMillis() - ((Long) map.get(LoginConstant.START_TIME)).longValue()) + "ms";
        }
        Object[] objArr = new Object[7];
        objArr[0] = "";
        objArr[1] = "going to save storage. ";
        if (!TextUtils.isEmpty(str4)) {
            str3 = "total cost " + str4;
        } else {
            str3 = "";
        }
        objArr[2] = str3;
        objArr[3] = " key is ";
        objArr[4] = str;
        objArr[5] = " | value is ";
        objArr[6] = changeValueBeforeMtopSave;
        PFLog.Data.d(objArr);
        PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap(2);
                hashMap.put("data", changeValueBeforeMtopSave);
                hashMap.put("t", String.valueOf(System.currentTimeMillis()));
                PFMtop.this.doSaveToStorage(str, JSON.toJSONString((Object) hashMap, true));
            }
        });
        saveStatusToStorage(PFConstant.Data.STATUS_GOT_RESPONSE, str);
        if (this.dataCallback != null) {
            this.dataCallback.afterMtopSave(str, changeValueBeforeMtopSave, map);
        }
        if (prefetchDataCallback != null) {
            PrefetchDataResponse prefetchDataResponse = new PrefetchDataResponse();
            HashMap hashMap = new HashMap(2);
            hashMap.put("data", JSON.parseObject(str2));
            hashMap.put("t", String.valueOf(System.currentTimeMillis()));
            prefetchDataResponse.data = hashMap;
            PFLog.Data.d("prefetchDataCallback is ", prefetchDataResponse);
            prefetchDataCallback.onComplete(prefetchDataResponse);
        }
        PFMonitor.Data.success();
        final String valueOf = String.valueOf(map.get("originalUrl"));
        PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
            public void run() {
                PFImage.getInstance().prefetchImage(changeValueBeforeMtopSave, valueOf);
            }
        });
    }

    /* access modifiers changed from: private */
    public void mtopFail(String str, String str2, PrefetchDataCallback prefetchDataCallback, Map<String, Object> map) {
        if (this.dataCallback != null) {
            String onMtopReturn = this.dataCallback.onMtopReturn(false, str2, map);
            if (PFUtil.isDebug()) {
                if (str2 == null || !str2.equals(onMtopReturn)) {
                    PFLog.Data.w("onMtopReturn() in error. Change from " + str2 + " to " + onMtopReturn + ", and will use the second one to process", new Throwable[0]);
                } else {
                    PFLog.Data.d("onMtopReturn() in error with no change");
                }
            }
            str2 = onMtopReturn;
        }
        saveStatusToStorage(PFConstant.Data.STATUS_GOT_RESPONSE_FAIL, str);
        resaveLastResult(str);
        if (str2 != null) {
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_MTOP_QUERY_ERROR, "mtop fail. error msg is" + str2 + "|" + str, new Object[0]);
        } else {
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_MTOP_QUERY_ERROR, "system error. |" + str, new Object[0]);
        }
        Object[] objArr = new Object[4];
        objArr[0] = "received mtop failed. params is ";
        objArr[1] = str;
        objArr[2] = ",error msg is ";
        objArr[3] = str2 != null ? str2 : "system error";
        PFLog.Data.d(objArr);
        if (prefetchDataCallback != null) {
            prefetchDataCallback.onError("500", str2);
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshGeoInfo(Context context) {
        if (SystemClock.uptimeMillis() >= ((long) (this.mDataModuleRemoteConfig.refreshGeoDelay() * 1000)) && SystemClock.uptimeMillis() - this.lastRefreshTimeOfGeoInfo < ((long) (this.mDataModuleRemoteConfig.refreshGeoDelay() * 1000))) {
            return;
        }
        if ((SystemClock.uptimeMillis() >= ((long) (this.mDataModuleRemoteConfig.refreshGeoDelay() * 1000)) || this.lastRefreshTimeOfGeoInfo <= 0) && ActivityCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            MtopPrefetchLocationListener mtopPrefetchLocationListener = new MtopPrefetchLocationListener(context, locationManager);
            if (!(locationManager == null || locationManager.getAllProviders() == null || !locationManager.getAllProviders().contains("network"))) {
                locationManager.requestLocationUpdates("network", (long) 20000, (float) 5, mtopPrefetchLocationListener);
            }
            if (locationManager != null && locationManager.getAllProviders() != null && locationManager.getAllProviders().contains("gps")) {
                locationManager.requestLocationUpdates("gps", (long) 20000, (float) 5, mtopPrefetchLocationListener);
            }
        }
    }

    class MtopPrefetchLocationListener implements LocationListener, Handler.Callback {
        private static final int TIME_OUT_WHAT = 3235841;
        private Context mContext;
        /* access modifiers changed from: private */
        public Handler mHandler = new Handler(this);
        private LocationManager mLocationManager;

        public MtopPrefetchLocationListener(Context context, LocationManager locationManager) {
            this.mContext = context;
            this.mLocationManager = locationManager;
            this.mHandler.post(new Runnable(PFMtop.this) {
                public void run() {
                    MtopPrefetchLocationListener.this.mHandler.sendEmptyMessageDelayed(MtopPrefetchLocationListener.TIME_OUT_WHAT, 10000);
                }
            });
        }

        public void onLocationChanged(Location location) {
            this.mHandler.removeMessages(TIME_OUT_WHAT);
            if (location != null) {
                PFMtop.this.lastGeolongitude = String.valueOf(location.getLongitude());
                PFMtop.this.lastGeoLatitude = String.valueOf(location.getLatitude());
                long unused = PFMtop.this.lastRefreshTimeOfGeoInfo = SystemClock.uptimeMillis();
                this.mLocationManager.removeUpdates(this);
            }
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            PFLog.Data.d("into--[onStatusChanged] provider111:", str, " status:", Integer.valueOf(i));
        }

        public void onProviderEnabled(String str) {
            PFLog.Data.d("into--[onProviderEnabled] provider111:", str);
        }

        public void onProviderDisabled(String str) {
            PFLog.Data.d("into--[onProviderDisabled] provider111:", str);
            this.mLocationManager.removeUpdates(this);
        }

        public boolean handleMessage(Message message) {
            try {
                if (message.what == TIME_OUT_WHAT) {
                    PFLog.Data.d("into--[handleMessage] Location Time Out!");
                    if (this.mContext != null) {
                        if (this.mLocationManager != null) {
                            this.mLocationManager.removeUpdates(this);
                            return true;
                        }
                    }
                    return false;
                }
            } catch (Throwable unused) {
            }
            return false;
        }
    }

    private void sendMtopRequestInternal(JSONObject jSONObject, final String str, final PrefetchDataCallback prefetchDataCallback, final Map<String, Object> map) {
        if (this.mtopSender != null && !TextUtils.isEmpty(str) && jSONObject != null) {
            this.mtopSender.sendMtopRequestData(jSONObject, new MtopCallback() {
                public void onSuccess(@NonNull String str) {
                    PFMtop.this.mtopSuccess(str, str, prefetchDataCallback, map);
                }

                public void onFailure(@Nullable String str) {
                    PFMtop.this.mtopFail(str, str, prefetchDataCallback, map);
                }
            });
        }
    }

    static class DefaultMtopSender implements MtopSender {
        DefaultMtopSender() {
        }

        public void sendMtopRequestData(@NonNull JSONObject jSONObject, @NonNull final MtopCallback mtopCallback) {
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName(p(jSONObject, "api"));
            mtopRequest.setVersion(p(jSONObject, "v"));
            mtopRequest.setNeedEcode("true".equals(p(jSONObject, "ecode")));
            JSONObject jSONObject2 = jSONObject.getJSONObject("data");
            if (jSONObject2 == null) {
                jSONObject2 = jSONObject.getJSONObject("param");
            }
            HashMap hashMap = new HashMap();
            if (jSONObject2 != null) {
                for (String next : jSONObject2.keySet()) {
                    hashMap.put(next, jSONObject2.getString(next));
                }
            }
            mtopRequest.dataParams = hashMap;
            mtopRequest.setData(ReflectUtil.converMapToDataStr(mtopRequest.dataParams));
            RemoteBusiness build = RemoteBusiness.build(mtopRequest);
            build.protocol(ProtocolEnum.HTTPSECURE);
            build.useCache();
            build.reqMethod(MethodEnum.GET);
            build.registeListener((MtopListener) new IRemoteBaseListener() {
                public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
                    mtopCallback.onFailure((String) null);
                }

                public void onError(int i, MtopResponse mtopResponse, Object obj) {
                    mtopCallback.onFailure(mtopResponse.getBytedata() == null ? "{}" : new String(mtopResponse.getBytedata()));
                }

                public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
                    try {
                        mtopCallback.onSuccess(mtopResponse.getBytedata() == null ? "{}" : new String(mtopResponse.getBytedata()));
                    } catch (Exception e) {
                        mtopCallback.onFailure(e.getMessage());
                        if (PFUtil.isDebug()) {
                            e.printStackTrace();
                        }
                    }
                }
            }).startRequest();
        }

        private String p(JSONObject jSONObject, String str) {
            Object obj = jSONObject.get(str);
            return obj != null ? obj.toString() : "";
        }
    }
}
