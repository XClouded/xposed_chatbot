package com.alibaba.android.prefetchx.core.jsmodule;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFDevice;
import com.alibaba.android.prefetchx.PFException;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.adapter.HttpAdapter;
import com.alibaba.android.prefetchx.adapter.PFResponse;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.util.e;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class PFJSModule {
    public static int JS_MODULE_SIZE = 128;
    private static final int MAX_TRY_TIMES = 10;
    private static final String NO_CDN_COMBO_URL = "no_cdn_combo_url";
    private static volatile PFJSModule instance;
    private AtomicBoolean isReady;
    int jsFromHttpCount;
    protected RemoteConfigSpec.IJSModuleRemoteConfig jsModuleRemoteConfig;
    JSServiceManager jsServiceManager;
    JSServiceSizeManager jsServiceSizeManager;
    private long lastConfigStartTime;
    private Set<String> lastNewConfigKeys;
    Set<String> loadedKeys;
    boolean lowMemoryStatus;
    List<JSModulePojo> toLoadPojos;
    Set<String> todoKeys;

    private PFJSModule() {
        this(true);
    }

    private PFJSModule(boolean z) {
        this.todoKeys = new ConcurrentSkipListSet();
        this.loadedKeys = new ConcurrentSkipListSet();
        this.lowMemoryStatus = false;
        this.toLoadPojos = new ArrayList(JS_MODULE_SIZE);
        this.isReady = new AtomicBoolean(false);
        this.lastNewConfigKeys = new ConcurrentSkipListSet();
        this.lastConfigStartTime = 0;
        this.jsFromHttpCount = 0;
        this.jsServiceManager = JSServiceManager.getInstance();
        this.jsServiceSizeManager = JSServiceSizeManager.getInstance();
        this.jsModuleRemoteConfig = PrefetchX.getInstance().getGlobalOnlineConfigManager().getJSModuleConfig();
        this.jsModuleRemoteConfig.registerJSModuleListener(new JSModuleContentChange());
        if (z) {
            PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                public void run() {
                    PFJSModule.this.refresh(false);
                }
            }, this.jsModuleRemoteConfig.getInitOrangeConfigProcessDelay() + 15000);
        }
    }

    public static PFJSModule getInstance() {
        if (instance == null) {
            synchronized (PFJSModule.class) {
                if (instance == null) {
                    instance = new PFJSModule();
                }
            }
        }
        return instance;
    }

    protected class JSModuleContentChange implements RemoteConfigSpec.IConfigChangeListener {
        protected JSModuleContentChange() {
        }

        public void onConfigChange(final String str, final boolean z, final String str2) {
            if (!PFJSModule.this.jsModuleRemoteConfig.isJSModuleEnable()) {
                PFLog.JSModule.w("getting config change, but JSModule is disabled by orange config.", new Throwable[0]);
                return;
            }
            if ((str2 == null || !str2.contains(Constants.Name.X)) && !PFUtil.isDebug()) {
                PFLog.JSModule.w(PFUtil.s("getting config changed. delay ", Integer.valueOf(PFJSModule.this.jsModuleRemoteConfig.getInitOrangeConfigProcessDelay()), " ms & divided into ", Integer.valueOf(Math.max(1, PFJSModule.this.jsModuleRemoteConfig.getInitOrangeConfigThreadCount())), " thread to load. fromCache is ", Boolean.valueOf(z), ", version is ", str2), new Throwable[0]);
            } else {
                PFLog.JSModule.w(PFUtil.s("getting config changed. delay ", Integer.valueOf(PFJSModule.this.jsModuleRemoteConfig.getInitOrangeConfigProcessDelay()), " ms & divided into ", Integer.valueOf(Math.max(1, PFJSModule.this.jsModuleRemoteConfig.getInitOrangeConfigThreadCount())), " thread to load. fromCache is ", Boolean.valueOf(z), ", version is ", str2, ", value is ", str), new Throwable[0]);
            }
            PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                public void run() {
                    PFJSModule.this.processOrangeConfig(str, z, str2);
                }
            }, PFJSModule.this.jsModuleRemoteConfig.getInitOrangeConfigProcessDelay());
        }
    }

    /* access modifiers changed from: private */
    public void processOrangeConfig(String str, boolean z, String str2) {
        List<JSModuleConfigV2> parseArray = JSON.parseArray(str, JSModuleConfigV2.class);
        ArrayList<JSModulePojo> arrayList = new ArrayList<>(JS_MODULE_SIZE);
        if (parseArray != null) {
            for (JSModuleConfigV2 jSModulePojo : parseArray) {
                arrayList.add(jSModulePojo.toJSModulePojo());
            }
        }
        this.isReady.set(false);
        this.lastConfigStartTime = SystemClock.uptimeMillis();
        this.toLoadPojos = new ArrayList(JS_MODULE_SIZE);
        for (JSModulePojo jSModulePojo2 : arrayList) {
            if (jSModulePojo2 != null && !this.todoKeys.contains(jSModulePojo2.getKey())) {
                if ("load".equals(jSModulePojo2.action)) {
                    if (!this.loadedKeys.contains(jSModulePojo2.getKey())) {
                        this.todoKeys.add(jSModulePojo2.getKey());
                        this.lastNewConfigKeys.add(jSModulePojo2.getKey());
                        this.toLoadPojos.add(jSModulePojo2);
                    }
                } else if (JSModulePojo.UNLOAD.equals(jSModulePojo2.action) && !z) {
                    unloadJSModule(jSModulePojo2);
                }
            }
        }
        if (this.toLoadPojos.size() > 0) {
            int initOrangeConfigThreadCount = this.jsModuleRemoteConfig.getInitOrangeConfigThreadCount();
            if (initOrangeConfigThreadCount > 1) {
                for (List next : PFUtil.averageAssign(this.toLoadPojos, initOrangeConfigThreadCount)) {
                    fireDownloadJSByPojo(this.toLoadPojos);
                }
            } else {
                fireDownloadJSByPojo(this.toLoadPojos);
            }
        }
        checkAllLoaded();
    }

    private void fireDownloadJSByPojo(@NonNull List<JSModulePojo> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (JSModulePojo next : list) {
            if (!cacheLoadJSModule(next)) {
                arrayList.add(next);
            }
        }
        downloadJSModules(arrayList);
    }

    /* access modifiers changed from: private */
    public void checkAllLoaded() {
        boolean z = true;
        for (String contains : this.lastNewConfigKeys) {
            if (!this.loadedKeys.contains(contains)) {
                z = false;
            }
        }
        if (this.todoKeys.size() == 0 && z) {
            PFLog.JSModule.d("i am ready. ", "lastNewConfig ", Integer.valueOf(this.lastNewConfigKeys.size()), " items. ", "total ", Integer.valueOf(this.loadedKeys.size()), " items. ", "cost ", Long.valueOf(SystemClock.uptimeMillis() - this.lastConfigStartTime), " ms. ", "size ", this.jsServiceSizeManager.getAllSize());
            HashMap hashMap = new HashMap();
            hashMap.put("isReady", "true");
            hashMap.put("totalItems", String.valueOf(this.loadedKeys.size()));
            hashMap.put("lastNewConfigItems", String.valueOf(this.lastNewConfigKeys.size()));
            hashMap.put("configCostTime", String.valueOf(SystemClock.uptimeMillis() - this.lastConfigStartTime));
            hashMap.put("size", this.jsServiceSizeManager.getAllSize().toString());
            PFMonitor.JSModule.success(JSON.toJSONString(hashMap));
            this.isReady.set(true);
            this.lastNewConfigKeys = new ConcurrentSkipListSet();
        }
    }

    public void refresh() {
        refresh(true);
    }

    public void refresh(boolean z) {
        if (this.jsModuleRemoteConfig.isJSModuleEnable()) {
            if (!z && isReady()) {
                return;
            }
            if (!this.lowMemoryStatus || PFDevice.getMemoryRuntimeLevel() >= this.jsModuleRemoteConfig.lowMemoryPercent()) {
                this.lowMemoryStatus = false;
                PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                    public void run() {
                        Map<String, String> orangeConfigJSModulePOJO = PFJSModule.this.jsModuleRemoteConfig.getOrangeConfigJSModulePOJO();
                        if (orangeConfigJSModulePOJO != null) {
                            String str = orangeConfigJSModulePOJO.get("configV2");
                            String str2 = orangeConfigJSModulePOJO.get("version");
                            if (!TextUtils.isEmpty(str)) {
                                if ((str2 != null && str2.contains(Constants.Name.X)) || PFUtil.isDebug()) {
                                    PFLog.JSModule.w(PFUtil.s("refresh config manually. divided into ", Integer.valueOf(Math.max(1, PFJSModule.this.jsModuleRemoteConfig.getInitOrangeConfigThreadCount())), " thread to load. version is ", str2, ", content is ", str), new Throwable[0]);
                                }
                                PFJSModule.this.processOrangeConfig(str, false, str2);
                            }
                        }
                    }
                });
            }
        }
    }

    public boolean isReady() {
        return this.isReady.get();
    }

    private boolean cacheLoadJSModule(JSModulePojo jSModulePojo) {
        if (jSModulePojo == null) {
            PFLog.JSModule.w("DownloadJSModuleToLoadTask param empty", new Throwable[0]);
            return false;
        }
        String key = jSModulePojo.getKey();
        JSModulePojo jSModuleFromFile = PrefetchX.getInstance().getAssetAdapter().getJSModuleFromFile(key);
        boolean z = (jSModuleFromFile == null || TextUtils.isEmpty(jSModuleFromFile.jsModule) || jSModuleFromFile.lastModified == null || 0 == jSModuleFromFile.lastModified.longValue()) ? false : true;
        int maxCacheAge = PrefetchX.getInstance().getGlobalOnlineConfigManager().getJSModuleConfig().maxCacheAge();
        if (jSModuleFromFile != null && (jSModuleFromFile.lastModified == null || System.currentTimeMillis() - jSModuleFromFile.lastModified.longValue() > ((long) (maxCacheAge * 1000)))) {
            PrefetchX.getInstance().getAssetAdapter().removeJSModule(key);
            z = false;
        }
        if (!z) {
            return false;
        }
        jSModulePojo.jsModule = jSModuleFromFile.jsModule;
        PFLog.JSModule.v("load started. from cache. ", jSModulePojo);
        loadJSService(jSModulePojo);
        this.todoKeys.remove(jSModulePojo.getKey());
        this.loadedKeys.add(jSModulePojo.getKey());
        checkAllLoaded();
        return true;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void downloadJSModules(List<JSModulePojo> list) {
        if (list != null) {
            long uptimeMillis = SystemClock.uptimeMillis();
            if (this.jsModuleRemoteConfig.cdnComboCount() > 1) {
                HashMap hashMap = new HashMap(4);
                for (JSModulePojo next : list) {
                    String str = (next.cdnComobPrefix == null || TextUtils.isEmpty(next.cdnComobPrefix)) ? NO_CDN_COMBO_URL : next.cdnComobPrefix;
                    List list2 = (List) hashMap.get(str);
                    if (list2 == null) {
                        list2 = new ArrayList(JS_MODULE_SIZE);
                        hashMap.put(str, list2);
                    }
                    list2.add(next);
                }
                final HashMap hashMap2 = new HashMap(16);
                for (String str2 : hashMap.keySet()) {
                    if (!NO_CDN_COMBO_URL.equals(str2)) {
                        List list3 = (List) hashMap.get(str2);
                        double size = (double) list3.size();
                        double cdnComboCount = (double) this.jsModuleRemoteConfig.cdnComboCount();
                        Double.isNaN(size);
                        Double.isNaN(cdnComboCount);
                        for (List<JSModulePojo> it : PFUtil.averageAssign(list3, (int) Math.max(1.0d, Math.ceil(size / cdnComboCount)))) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str2);
                            sb.append("??");
                            ArrayList arrayList = new ArrayList(JS_MODULE_SIZE);
                            for (JSModulePojo jSModulePojo : it) {
                                if (!TextUtils.isEmpty(jSModulePojo.cdnComobUrl)) {
                                    sb.append(jSModulePojo.cdnComobUrl);
                                    sb.append(",");
                                    arrayList.add(jSModulePojo);
                                }
                            }
                            hashMap2.put(sb.toString(), arrayList);
                        }
                    }
                }
                final int i = 0;
                for (final String str3 : hashMap2.keySet()) {
                    int i2 = this.jsFromHttpCount + 1;
                    this.jsFromHttpCount = i2;
                    int delayBetweenEachJSDownload = i2 * this.jsModuleRemoteConfig.delayBetweenEachJSDownload();
                    i++;
                    PFLog.JSModule.v(Operators.ARRAY_START_STR, Integer.valueOf(i), "] will send download request. delay ", Integer.valueOf(delayBetweenEachJSDownload), " ms to start. length: ", str3.length() + ", url : " + str3);
                    PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                        public void run() {
                            long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
                            PrefetchX.getInstance().getHttpAdapter().sendRequest(str3, (HttpAdapter.HttpListener) new PFHttpListenerCombo((List) hashMap2.get(str3)));
                            PFLog.JSModule.d(Operators.ARRAY_START_STR, Integer.valueOf(i), "] start send download request. used ", Long.valueOf(SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis), " ms this thread. length: ", str3.length() + ", url : " + str3);
                        }
                    }, delayBetweenEachJSDownload);
                }
                List<JSModulePojo> list4 = (List) hashMap.get(NO_CDN_COMBO_URL);
                if (list4 != null) {
                    for (final JSModulePojo jSModulePojo2 : list4) {
                        int i3 = this.jsFromHttpCount + 1;
                        this.jsFromHttpCount = i3;
                        int delayBetweenEachJSDownload2 = i3 * this.jsModuleRemoteConfig.delayBetweenEachJSDownload();
                        i++;
                        PFLog.JSModule.v(Operators.ARRAY_START_STR, Integer.valueOf(i), "] will send download request. delay ", Integer.valueOf(delayBetweenEachJSDownload2), " ms to start. pojo: ", jSModulePojo2);
                        PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                            public void run() {
                                PFLog.JSModule.d(Operators.ARRAY_START_STR, Integer.valueOf(i), "] start send download request", " pojo: ", jSModulePojo2);
                                PrefetchX.getInstance().getHttpAdapter().sendRequest(jSModulePojo2.jsModuleUrl, (HttpAdapter.HttpListener) new PFHttpListener(jSModulePojo2, 0));
                            }
                        }, delayBetweenEachJSDownload2);
                    }
                }
            } else {
                final int i4 = 0;
                for (final JSModulePojo next2 : list) {
                    int i5 = this.jsFromHttpCount + 1;
                    this.jsFromHttpCount = i5;
                    int delayBetweenEachJSDownload3 = i5 * this.jsModuleRemoteConfig.delayBetweenEachJSDownload();
                    i4++;
                    PFLog.JSModule.v(Operators.ARRAY_START_STR, Integer.valueOf(i4), "] will send download request. delay ", Integer.valueOf(delayBetweenEachJSDownload3), " ms to start. pojo: ", next2);
                    PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                        public void run() {
                            PFLog.JSModule.d(Operators.ARRAY_START_STR, Integer.valueOf(i4), "] start send download request", " pojo: ", next2);
                            PrefetchX.getInstance().getHttpAdapter().sendRequest(next2.jsModuleUrl, (HttpAdapter.HttpListener) new PFHttpListener(next2, 0));
                        }
                    }, delayBetweenEachJSDownload3);
                }
            }
            PFLog.JSModule.d("divide into ", Integer.valueOf(this.jsFromHttpCount), " part to load. cost ", Long.valueOf(SystemClock.uptimeMillis() - uptimeMillis), " ms");
            this.jsFromHttpCount = 0;
        }
    }

    private void unloadJSModule(JSModulePojo jSModulePojo) {
        try {
            this.todoKeys.add(jSModulePojo.getKey());
            PFLog.JSModule.d("unload started. ", jSModulePojo);
            unloadJSService(jSModulePojo);
            this.todoKeys.remove(jSModulePojo.getKey());
            this.loadedKeys.remove(jSModulePojo.getKey());
            PrefetchX.getInstance().getAssetAdapter().removeJSModule(jSModulePojo.getKey());
        } catch (PFException e) {
            PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_EXCEPTION, "error in unloadJSModule", e.getMessage() + PFLog.getStackTrace(e));
        }
    }

    public void onLowMemory() {
        if (!this.jsModuleRemoteConfig.isJSModuleEnable()) {
            PFLog.JSModule.w("onLowMemory fire, but JSModule is disabled by orange config. so nothing to release", new Throwable[0]);
        } else if (this.jsModuleRemoteConfig.unloadAllJSModuleOnLowMemory()) {
            this.lowMemoryStatus = true;
            this.isReady.set(false);
            PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_EXCEPTION, "low memory occur", new Object[0]);
            PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                public void run() {
                    if (PFJSModule.this.toLoadPojos != null) {
                        for (JSModulePojo next : PFJSModule.this.toLoadPojos) {
                            next.action = JSModulePojo.UNLOAD;
                            PFLog.JSModule.d("unload on low memery started. ", next, "memory level " + PFDevice.getMemoryRuntimeLevel());
                            PFJSModule.this.unloadJSService(next);
                            PFJSModule.this.loadedKeys.remove(next.getKey());
                        }
                    }
                }
            });
        }
    }

    public void removeAllJSModuleCache() {
        PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
            public void run() {
                if (PFJSModule.this.toLoadPojos != null) {
                    for (JSModulePojo next : PFJSModule.this.toLoadPojos) {
                        PFLog.JSModule.d("removing avfs cache file ", next);
                        PrefetchX.getInstance().getAssetAdapter().removeJSModule(next.getKey());
                    }
                }
            }
        });
    }

    private class PFHttpListener extends HttpAdapter.AbstractHttpListner {
        JSModulePojo pojo;
        int tryTimes;

        PFHttpListener(JSModulePojo jSModulePojo, int i) {
            this.pojo = jSModulePojo;
            this.tryTimes = i;
        }

        public void onHttpFinish(final PFResponse pFResponse) {
            PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:20:0x00ee  */
                /* JADX WARNING: Removed duplicated region for block: B:21:0x0110  */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r6 = this;
                        r0 = 1
                        com.alibaba.android.prefetchx.adapter.PFResponse r1 = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        byte[] r1 = r1.originalData     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r2 = 0
                        if (r1 == 0) goto L_0x0098
                        com.alibaba.android.prefetchx.adapter.PFResponse r1 = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r1 = r1.data     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        if (r1 != 0) goto L_0x0017
                        com.alibaba.android.prefetchx.adapter.PFResponse r1 = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r1 = r1.data     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        goto L_0x0022
                    L_0x0017:
                        java.lang.String r1 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.adapter.PFResponse r3 = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        byte[] r3 = r3.originalData     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r4 = "utf-8"
                        r1.<init>(r3, r4)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                    L_0x0022:
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r3.jsModule = r1     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        long r4 = java.lang.System.currentTimeMillis()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r3.lastModified = r4     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        if (r1 != 0) goto L_0x00dc
                        r1 = 2
                        java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r3 = "load started. from http. "
                        r1[r2] = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1[r0] = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.PFLog.JSModule.v(r1)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1.loadJSService(r3)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.util.Set<java.lang.String> r1 = r1.todoKeys     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r3 = r3.getKey()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1.remove(r3)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.util.Set<java.lang.String> r1 = r1.loadedKeys     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r3 = r3.getKey()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1.add(r3)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1.checkAllLoaded()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.PrefetchX r1 = com.alibaba.android.prefetchx.PrefetchX.getInstance()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.adapter.AssetAdapter r1 = r1.getAssetAdapter()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r3 = r3.getKey()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r4 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r4 = r4.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1.putJSModuleToFile(r3, r4)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        goto L_0x00dc
                    L_0x0098:
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r2 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r2 = r2.pojo     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r3 = "error in http"
                        com.alibaba.android.prefetchx.adapter.PFResponse r4 = r3     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        java.lang.String r4 = r4.toSimpleString()     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        r1.fireCallback(r2, r3, r4)     // Catch:{ UnsupportedEncodingException -> 0x00ce, Exception -> 0x00ac }
                        goto L_0x00db
                    L_0x00ac:
                        r1 = move-exception
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r2 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r2 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r3 = r3.pojo
                        java.lang.StringBuilder r4 = new java.lang.StringBuilder
                        r4.<init>()
                        java.lang.String r5 = "unkown exception."
                        r4.append(r5)
                        java.lang.String r1 = r1.getMessage()
                        r4.append(r1)
                        java.lang.String r1 = r4.toString()
                        r2.fireCallback((com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo) r3, (java.lang.Object) r1)
                        goto L_0x00db
                    L_0x00ce:
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r2 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r2 = r2.pojo
                        java.lang.String r3 = "UnsupportedEncodingException"
                        r1.fireCallback((com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo) r2, (java.lang.Object) r3)
                    L_0x00db:
                        r2 = 1
                    L_0x00dc:
                        if (r2 == 0) goto L_0x0110
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        int r1 = r1.tryTimes
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r3 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this
                        com.alibaba.android.prefetchx.config.RemoteConfigSpec$IJSModuleRemoteConfig r3 = r3.jsModuleRemoteConfig
                        int r3 = r3.retryDownloadTimes()
                        if (r1 >= r3) goto L_0x0110
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        int r2 = r1.tryTimes
                        int r2 = r2 + r0
                        r1.tryTimes = r2
                        com.alibaba.android.prefetchx.PrefetchX r0 = com.alibaba.android.prefetchx.PrefetchX.getInstance()
                        com.alibaba.android.prefetchx.adapter.IThreadExecutor r0 = r0.getThreadExecutor()
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener$1$1 r1 = new com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener$1$1
                        r1.<init>()
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r2 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r2 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this
                        com.alibaba.android.prefetchx.config.RemoteConfigSpec$IJSModuleRemoteConfig r2 = r2.jsModuleRemoteConfig
                        int r2 = r2.retryDownloadDelay()
                        r0.executeWithDelay(r1, r2)
                        goto L_0x0133
                    L_0x0110:
                        if (r2 == 0) goto L_0x0133
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r0 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        int r0 = r0.tryTimes
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this
                        com.alibaba.android.prefetchx.config.RemoteConfigSpec$IJSModuleRemoteConfig r1 = r1.jsModuleRemoteConfig
                        int r1 = r1.retryDownloadTimes()
                        if (r0 < r1) goto L_0x0133
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r0 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r0 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.this
                        java.util.Set<java.lang.String> r0 = r0.todoKeys
                        com.alibaba.android.prefetchx.core.jsmodule.PFJSModule$PFHttpListener r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.this
                        com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo r1 = r1.pojo
                        java.lang.String r1 = r1.getKey()
                        r0.remove(r1)
                    L_0x0133:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.PFHttpListener.AnonymousClass1.run():void");
                }
            });
        }
    }

    private class PFHttpListenerCombo extends HttpAdapter.AbstractHttpListner {
        List<JSModulePojo> pojoList;

        PFHttpListenerCombo(List<JSModulePojo> list) {
            this.pojoList = list;
        }

        public void onHttpFinish(final PFResponse pFResponse) {
            PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                public void run() {
                    String str = "";
                    boolean z = true;
                    try {
                        if (pFResponse.originalData != null) {
                            String[] split = (!TextUtils.isEmpty(pFResponse.data) ? pFResponse.data : new String(pFResponse.originalData, "utf-8")).split("service.register\\(options.serviceName");
                            if (PFHttpListenerCombo.this.pojoList != null) {
                                if (split.length - 1 == PFHttpListenerCombo.this.pojoList.size()) {
                                    int i = 0;
                                    while (true) {
                                        if (i >= PFHttpListenerCombo.this.pojoList.size()) {
                                            z = false;
                                            break;
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("service.register(options.serviceName");
                                        int i2 = i + 1;
                                        sb.append(split[i2]);
                                        String sb2 = sb.toString();
                                        final JSModulePojo jSModulePojo = PFHttpListenerCombo.this.pojoList.get(i);
                                        if (!sb2.contains(jSModulePojo.name)) {
                                            str = "cdnCombo return js not contains pojo name " + jSModulePojo.name;
                                            break;
                                        }
                                        jSModulePojo.jsModule = sb2;
                                        jSModulePojo.lastModified = Long.valueOf(System.currentTimeMillis());
                                        PFLog.JSModule.v("load started. from http. ", jSModulePojo);
                                        PFJSModule.this.loadJSService(jSModulePojo);
                                        PFJSModule.this.todoKeys.remove(jSModulePojo.getKey());
                                        PFJSModule.this.loadedKeys.add(jSModulePojo.getKey());
                                        PFJSModule.this.checkAllLoaded();
                                        PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                                            public void run() {
                                                PrefetchX.getInstance().getAssetAdapter().putJSModuleToFile(jSModulePojo.getKey(), jSModulePojo);
                                            }
                                        });
                                        i = i2;
                                    }
                                }
                            }
                            str = "cdnCombo return size not equal pojoList";
                        } else {
                            str = "cdnCombo error in http";
                        }
                    } catch (UnsupportedEncodingException unused) {
                        str = "cdnCombo UnsupportedEncodingException";
                    } catch (Throwable th) {
                        str = "cdnCombo unkown exception." + th.getMessage() + " | " + PFLog.getStackTrace(th);
                    }
                    if (z && PFHttpListenerCombo.this.pojoList != null) {
                        for (JSModulePojo access$200 : PFHttpListenerCombo.this.pojoList) {
                            PFJSModule.this.fireCallback(access$200, str, pFResponse.toSimpleString());
                        }
                        for (int i3 = 0; i3 < PFHttpListenerCombo.this.pojoList.size(); i3++) {
                            final JSModulePojo jSModulePojo2 = PFHttpListenerCombo.this.pojoList.get(i3);
                            PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                                public void run() {
                                    PrefetchX.getInstance().getHttpAdapter().sendRequest(jSModulePojo2.jsModuleUrl, (HttpAdapter.HttpListener) new PFHttpListener(jSModulePojo2, PFJSModule.this.jsModuleRemoteConfig.retryDownloadTimes()));
                                }
                            }, PFJSModule.this.jsModuleRemoteConfig.retryDownloadDelay() + (PFJSModule.this.jsModuleRemoteConfig.delayBetweenEachJSDownload() * i3));
                        }
                    }
                }
            });
        }
    }

    public boolean loadJSService(JSModulePojo jSModulePojo) {
        if (jSModulePojo == null || !verifyParams(jSModulePojo)) {
            return false;
        }
        Pair<Boolean, String> load = this.jsServiceSizeManager.load(jSModulePojo);
        if (((Boolean) load.first).booleanValue()) {
            boolean registerJSService = this.jsServiceManager.registerJSService(jSModulePojo);
            fireCallback(jSModulePojo, (Object) Boolean.valueOf(registerJSService));
            return registerJSService;
        }
        fireCallback(jSModulePojo, load.second);
        return false;
    }

    public void loadJSServiceByUrl(JSModulePojo jSModulePojo) {
        if (verifyParams(jSModulePojo)) {
            PrefetchX.getInstance().getHttpAdapter().sendRequest(jSModulePojo.jsModuleUrl, (HttpAdapter.HttpListener) new PFHttpListener(jSModulePojo, 10));
        }
    }

    private boolean verifyParams(JSModulePojo jSModulePojo) {
        if (!this.jsModuleRemoteConfig.isJSModuleEnable()) {
            PFLog.JSModule.w("Oh! I am disabled", new Throwable[0]);
            fireCallback(jSModulePojo, (Object) "disabled");
            return false;
        } else if (!TextUtils.isEmpty(jSModulePojo.jsModule) || !TextUtils.isEmpty(jSModulePojo.jsModuleUrl)) {
            return true;
        } else {
            PFLog.JSModule.w("jsModule is empty", new Throwable[0]);
            fireCallback(jSModulePojo, (Object) "jsModule is empty");
            return false;
        }
    }

    public boolean unloadJSService(JSModulePojo jSModulePojo) {
        Pair<Boolean, String> unload = this.jsServiceSizeManager.unload(jSModulePojo);
        if (((Boolean) unload.first).booleanValue()) {
            boolean unRegisterJSService = this.jsServiceManager.unRegisterJSService(jSModulePojo);
            fireCallback(jSModulePojo, (Object) Boolean.valueOf(unRegisterJSService));
            return unRegisterJSService;
        }
        fireCallback(jSModulePojo, unload.second);
        return false;
    }

    private void fireCallback(List<JSModulePojo> list, Object obj) {
        if (list != null) {
            for (JSModulePojo fireCallback : list) {
                fireCallback(fireCallback, obj);
            }
        }
    }

    /* access modifiers changed from: private */
    public void fireCallback(JSModulePojo jSModulePojo, Object obj) {
        if (jSModulePojo == null) {
            PFLog.JSModule.w("null pojo in fireCallback", new Throwable[0]);
            return;
        }
        try {
            if (obj instanceof Boolean) {
                if (((Boolean) obj).booleanValue()) {
                    PFLog.JSModule.d(jSModulePojo.action, " success. ", jSModulePojo);
                    PFMonitor.JSModule.success(jSModulePojo.getKey());
                    if (jSModulePojo.callback != null && jSModulePojo.callback.get() != null) {
                        ((JSModulePojo.Callback) jSModulePojo.callback.get()).done("success");
                        return;
                    }
                    return;
                }
                PFLog.JSModule.w(jSModulePojo.action + " failed. " + jSModulePojo, new Throwable[0]);
                PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_ERROR_WEEX, "error in weex call", jSModulePojo.toString());
                if (jSModulePojo.callback != null && jSModulePojo.callback.get() != null) {
                    ((JSModulePojo.Callback) jSModulePojo.callback.get()).done(e.a);
                }
            } else if (obj instanceof String) {
                PFLog.JSModule.w(jSModulePojo.action + " failed by " + obj.toString() + jSModulePojo, new Throwable[0]);
                PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_ERROR_PREFETCHX, obj.toString(), jSModulePojo.toString());
                if (jSModulePojo.callback != null && jSModulePojo.callback.get() != null) {
                    ((JSModulePojo.Callback) jSModulePojo.callback.get()).done(obj.toString());
                }
            }
        } catch (Throwable th) {
            PFLog.JSModule.w("exception in PFJSModule.fireCallback, Not so serious, can be ignored. ", th);
        }
    }

    /* access modifiers changed from: private */
    @Deprecated
    public void fireCallback(JSModulePojo jSModulePojo, String str, String str2) {
        PFLog.JSModule.w(jSModulePojo.action + " failed by " + str + jSModulePojo + ", detail is " + str2, new Throwable[0]);
        HashMap hashMap = new HashMap(2);
        hashMap.put("pojo", jSModulePojo.toString());
        hashMap.put("detailMessage", str2);
        PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_ERROR_PREFETCHX, str, JSON.toJSONString(hashMap));
        if (jSModulePojo.callback != null && jSModulePojo.callback.get() != null) {
            ((JSModulePojo.Callback) jSModulePojo.callback.get()).done(str);
        }
    }
}
