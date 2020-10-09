package com.alibaba.android.prefetchx.core.file;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import anet.channel.util.HttpConstant;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXThread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.SymbolExpUtil;

final class PrefetchManager {
    static final long DEFAULT_DELAY_MILLIS = 1500;
    static final long DEFAULT_MAX_AGE_IN_MILLIS = 300000;
    static final int DEFAULT_MAX_CACHE_NUM = 5;
    private static final int MAX_CACHED_URL_NUM = 128;
    private static final String MSG_SWITCH_OFF = "switch_off";
    private static final String TAG = "WXPrefetchModule";
    /* access modifiers changed from: private */
    public static volatile Set<PrefetchEntry> mCachedEntries = Collections.newSetFromMap(new LruLinkedHashMap(128));
    private static volatile Map<String, Integer> sPrefetchCounter = new HashMap();
    static volatile AtomicBoolean shouldReport = new AtomicBoolean(false);
    private IConnection mConnection;
    private final long mDelay;
    private ExternalCacheChecker mExternalCacheChecker;
    private Handler mHandler;
    private IWXHttpAdapter mHttpAdapter;
    private String mInstanceId;
    private final int mMaxCacheNum;
    private UriProcessor mProcessor;
    private RemoteConfigSpec.IFileModuleRemoteConfig mRemoteConfig;
    /* access modifiers changed from: private */
    public SafetyPrefetchResultListener mWrappedListener;

    interface ExternalCacheChecker {
        boolean isCachedAlready(@NonNull String str);
    }

    interface OnPrefetchResultListener {
        void onFailed(@NonNull String str, @Nullable String str2);

        void onSuccess(@NonNull String str);
    }

    interface UriProcessor {
        @NonNull
        String processUri(@NonNull String str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0025, code lost:
        if (r3 > 0) goto L_0x002a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private PrefetchManager(@androidx.annotation.Nullable com.alibaba.android.prefetchx.core.file.IConnection r3, @androidx.annotation.Nullable com.alibaba.android.prefetchx.core.file.PrefetchManager.ExternalCacheChecker r4, @androidx.annotation.NonNull com.taobao.weex.adapter.IWXHttpAdapter r5, @androidx.annotation.Nullable com.alibaba.android.prefetchx.config.RemoteConfigSpec.IFileModuleRemoteConfig r6, @androidx.annotation.Nullable com.alibaba.android.prefetchx.core.file.PrefetchManager.UriProcessor r7) {
        /*
            r2 = this;
            r2.<init>()
            android.os.Handler r0 = new android.os.Handler
            android.os.Looper r1 = android.os.Looper.getMainLooper()
            r0.<init>(r1)
            r2.mHandler = r0
            r0 = 0
            r2.mWrappedListener = r0
            r2.mConnection = r3
            r2.mExternalCacheChecker = r4
            r2.mHttpAdapter = r5
            r2.mRemoteConfig = r6
            r2.mProcessor = r7
            if (r6 == 0) goto L_0x0028
            long r3 = r6.getDelay()
            r0 = 0
            int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r5 <= 0) goto L_0x0028
            goto L_0x002a
        L_0x0028:
            r3 = 1500(0x5dc, double:7.41E-321)
        L_0x002a:
            r2.mDelay = r3
            r3 = 2
            java.lang.Object[] r4 = new java.lang.Object[r3]
            java.lang.String r5 = "delay millis:"
            r7 = 0
            r4[r7] = r5
            long r0 = r2.mDelay
            java.lang.Long r5 = java.lang.Long.valueOf(r0)
            r0 = 1
            r4[r0] = r5
            com.alibaba.android.prefetchx.PFLog.File.d(r4)
            r4 = 5
            if (r6 == 0) goto L_0x004a
            int r5 = r6.getMaxCacheNum()
            if (r5 <= 0) goto L_0x004a
            r4 = r5
        L_0x004a:
            r2.mMaxCacheNum = r4
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r4 = new com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener
            android.os.Handler r5 = r2.mHandler
            r4.<init>(r5)
            r2.mWrappedListener = r4
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r4 = "max cache num:"
            r3[r7] = r4
            int r4 = r2.mMaxCacheNum
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r3[r0] = r4
            com.alibaba.android.prefetchx.PFLog.File.d(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.file.PrefetchManager.<init>(com.alibaba.android.prefetchx.core.file.IConnection, com.alibaba.android.prefetchx.core.file.PrefetchManager$ExternalCacheChecker, com.taobao.weex.adapter.IWXHttpAdapter, com.alibaba.android.prefetchx.config.RemoteConfigSpec$IFileModuleRemoteConfig, com.alibaba.android.prefetchx.core.file.PrefetchManager$UriProcessor):void");
    }

    static Builder create(@NonNull IWXHttpAdapter iWXHttpAdapter) {
        return new Builder(iWXHttpAdapter);
    }

    @NonNull
    static Set<PrefetchEntry> getPrefetchEntries() {
        if (mCachedEntries == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(mCachedEntries);
    }

    @VisibleForTesting
    @NonNull
    static String removeSpecificQueryParamsInBaseUrl(@NonNull String str, @Nullable List<String> list) {
        if (list == null || list.isEmpty()) {
            return str;
        }
        int indexOf = str.indexOf(63);
        int indexOf2 = str.indexOf(35);
        int length = str.length();
        if (indexOf <= -1 || indexOf == length - 1) {
            return str;
        }
        if (indexOf2 == -1) {
            indexOf2 = length;
        }
        for (String next : list) {
            if (!"".equals(next)) {
                int indexOf3 = str.indexOf(next + SymbolExpUtil.SYMBOL_EQUAL, indexOf + 1);
                if (indexOf3 > -1) {
                    int indexOf4 = str.indexOf(38, indexOf3);
                    int i = indexOf4 <= -1 ? indexOf2 : indexOf4 + 1;
                    if (i == indexOf2) {
                        indexOf3--;
                    }
                    String substring = str.substring(indexOf3, i);
                    str = str.replace(substring, "");
                    indexOf2 -= substring.length();
                }
            }
        }
        return str;
    }

    /* access modifiers changed from: package-private */
    public void doPrefetchWithDelay(@NonNull String str, @Nullable List<String> list, @NonNull String str2, boolean z) {
        if (this.mHandler != null) {
            final boolean z2 = z;
            final String str3 = str;
            final List<String> list2 = list;
            final String str4 = str2;
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (z2) {
                        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                            public boolean queueIdle() {
                                PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                                    public void run() {
                                        PrefetchManager.this.doPrefetch(str3, list2, str4);
                                    }
                                });
                                return false;
                            }
                        });
                    } else {
                        PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                            public void run() {
                                PrefetchManager.this.doPrefetch(str3, list2, str4);
                            }
                        });
                    }
                }
            }, this.mDelay);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00e6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doPrefetch(@androidx.annotation.NonNull java.lang.String r12, @androidx.annotation.Nullable java.util.List<java.lang.String> r13, @androidx.annotation.NonNull java.lang.String r14) {
        /*
            r11 = this;
            long r0 = java.lang.System.currentTimeMillis()
            r11.mInstanceId = r14
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "{\"pageName\":\""
            r2.append(r3)
            r2.append(r12)
            java.lang.String r3 = "\"}"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.alibaba.android.prefetchx.config.RemoteConfigSpec$IFileModuleRemoteConfig r3 = r11.mRemoteConfig
            r4 = 1
            r5 = 0
            if (r3 == 0) goto L_0x003c
            com.alibaba.android.prefetchx.config.RemoteConfigSpec$IFileModuleRemoteConfig r3 = r11.mRemoteConfig
            boolean r3 = r3.isSwitchOn()
            if (r3 != 0) goto L_0x003c
            java.lang.Object[] r13 = new java.lang.Object[r4]
            java.lang.String r14 = "switch is off."
            r13[r5] = r14
            com.alibaba.android.prefetchx.PFLog.File.d(r13)
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r13 = r11.mWrappedListener
            java.lang.String r14 = "switch_off"
            r13.onFailed(r12, r14)
            return
        L_0x003c:
            com.alibaba.android.prefetchx.core.file.PrefetchManager$UriProcessor r3 = r11.mProcessor
            r6 = 2
            if (r3 == 0) goto L_0x0085
            com.alibaba.android.prefetchx.core.file.PrefetchManager$UriProcessor r3 = r11.mProcessor     // Catch:{ Throwable -> 0x0055 }
            java.lang.String r3 = r3.processUri(r12)     // Catch:{ Throwable -> 0x0055 }
            java.lang.Object[] r12 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x0053 }
            java.lang.String r7 = "process url success:"
            r12[r5] = r7     // Catch:{ Throwable -> 0x0053 }
            r12[r4] = r3     // Catch:{ Throwable -> 0x0053 }
            com.alibaba.android.prefetchx.PFLog.File.d(r12)     // Catch:{ Throwable -> 0x0053 }
            goto L_0x0086
        L_0x0053:
            r12 = move-exception
            goto L_0x0059
        L_0x0055:
            r3 = move-exception
            r10 = r3
            r3 = r12
            r12 = r10
        L_0x0059:
            java.lang.String r7 = "error in process url"
            java.lang.Throwable[] r8 = new java.lang.Throwable[r4]
            r8[r5] = r12
            com.alibaba.android.prefetchx.PFLog.File.w(r7, r8)
            java.util.HashMap r7 = new java.util.HashMap
            r7.<init>(r6)
            java.lang.String r8 = "url"
            r7.put(r8, r3)
            java.lang.String r8 = "message"
            java.lang.String r12 = r12.getMessage()
            r7.put(r8, r12)
            java.lang.String r12 = "-40003"
            java.lang.String r8 = "error in process url"
            java.lang.Object[] r9 = new java.lang.Object[r4]
            java.lang.String r7 = com.alibaba.fastjson.JSON.toJSONString(r7)
            r9[r5] = r7
            com.alibaba.android.prefetchx.PFMonitor.File.fail(r12, r8, r9)
            goto L_0x0086
        L_0x0085:
            r3 = r12
        L_0x0086:
            java.util.Map<java.lang.String, java.lang.Integer> r12 = sPrefetchCounter
            java.lang.Object r12 = r12.get(r14)
            if (r12 != 0) goto L_0x0099
            java.util.Map<java.lang.String, java.lang.Integer> r12 = sPrefetchCounter
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)
            r12.put(r14, r7)
            r12 = 1
            goto L_0x00af
        L_0x0099:
            java.util.Map<java.lang.String, java.lang.Integer> r12 = sPrefetchCounter
            java.lang.Object r12 = r12.get(r14)
            java.lang.Integer r12 = (java.lang.Integer) r12
            int r12 = r12.intValue()
            int r12 = r12 + r4
            java.util.Map<java.lang.String, java.lang.Integer> r7 = sPrefetchCounter
            java.lang.Integer r8 = java.lang.Integer.valueOf(r12)
            r7.put(r14, r8)
        L_0x00af:
            int r14 = r11.mMaxCacheNum
            if (r12 <= r14) goto L_0x00e6
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "exceed cache num : "
            r13.append(r14)
            r13.append(r12)
            java.lang.String r12 = ", maxCacheNum : "
            r13.append(r12)
            int r12 = r11.mMaxCacheNum
            r13.append(r12)
            java.lang.String r12 = r13.toString()
            java.lang.Throwable[] r13 = new java.lang.Throwable[r5]
            com.alibaba.android.prefetchx.PFLog.File.w(r12, r13)
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r12 = r11.mWrappedListener
            java.lang.String r13 = "exceed"
            r12.onFailed(r3, r13)
            java.lang.String r12 = "-40101"
            java.lang.String r13 = "exceed"
            java.lang.Object[] r14 = new java.lang.Object[r4]
            r14[r5] = r2
            com.alibaba.android.prefetchx.PFMonitor.File.fail(r12, r13, r14)
            return
        L_0x00e6:
            java.lang.Object[] r14 = new java.lang.Object[r6]
            java.lang.String r7 = "current size : "
            r14[r5] = r7
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
            r14[r4] = r12
            com.alibaba.android.prefetchx.PFLog.File.d(r14)
            com.alibaba.android.prefetchx.core.file.IConnection r12 = r11.mConnection
            if (r12 == 0) goto L_0x0123
            java.lang.String r12 = "wifi"
            com.alibaba.android.prefetchx.core.file.IConnection r14 = r11.mConnection
            java.lang.String r14 = r14.getConnectionType()
            boolean r12 = r12.equals(r14)
            if (r12 != 0) goto L_0x0123
            java.lang.String r12 = "wrong connection type"
            java.lang.Throwable[] r13 = new java.lang.Throwable[r5]
            com.alibaba.android.prefetchx.PFLog.File.w(r12, r13)
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r12 = r11.mWrappedListener
            java.lang.String r13 = "error_connection_type"
            r12.onFailed(r3, r13)
            java.lang.String r12 = "-40102"
            java.lang.String r13 = "error_connection_type"
            java.lang.Object[] r14 = new java.lang.Object[r4]
            r14[r5] = r2
            com.alibaba.android.prefetchx.PFMonitor.File.fail(r12, r13, r14)
            return
        L_0x0123:
            com.alibaba.android.prefetchx.core.file.PrefetchManager$ExternalCacheChecker r12 = r11.mExternalCacheChecker
            if (r12 == 0) goto L_0x013e
            com.alibaba.android.prefetchx.core.file.PrefetchManager$ExternalCacheChecker r12 = r11.mExternalCacheChecker
            boolean r12 = r12.isCachedAlready(r3)
            if (r12 == 0) goto L_0x013e
            java.lang.String r12 = "page cached already(0)"
            java.lang.Throwable[] r13 = new java.lang.Throwable[r5]
            com.alibaba.android.prefetchx.PFLog.File.w(r12, r13)
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r12 = r11.mWrappedListener
            java.lang.String r13 = "been_cached"
            r12.onFailed(r3, r13)
            return
        L_0x013e:
            if (r13 == 0) goto L_0x016a
            boolean r12 = r13.isEmpty()
            if (r12 != 0) goto L_0x016a
            com.alibaba.android.prefetchx.config.RemoteConfigSpec$IFileModuleRemoteConfig r12 = r11.mRemoteConfig
            if (r12 == 0) goto L_0x016a
            com.alibaba.android.prefetchx.config.RemoteConfigSpec$IFileModuleRemoteConfig r12 = r11.mRemoteConfig
            java.util.List r12 = r12.getIgnoreParamsBlackList()
            boolean r14 = r12.isEmpty()
            if (r14 != 0) goto L_0x016a
            java.util.Iterator r12 = r12.iterator()
        L_0x015a:
            boolean r14 = r12.hasNext()
            if (r14 == 0) goto L_0x016a
            java.lang.Object r14 = r12.next()
            java.lang.String r14 = (java.lang.String) r14
            r13.remove(r14)
            goto L_0x015a
        L_0x016a:
            r12 = 0
            java.lang.String r14 = removeSpecificQueryParamsInBaseUrl(r3, r13)     // Catch:{ Exception -> 0x0170 }
            r12 = r14
        L_0x0170:
            com.alibaba.android.prefetchx.core.file.PrefetchManager$PrefetchEntry r14 = new com.alibaba.android.prefetchx.core.file.PrefetchManager$PrefetchEntry
            r14.<init>()
            if (r12 != 0) goto L_0x0178
            r12 = r3
        L_0x0178:
            r14.url = r12
            r14.ignoreParams = r13
            com.alibaba.android.prefetchx.core.file.PrefetchManager$PrefetchEntry r12 = r11.isUrlRequestedBefore(r14)
            if (r12 == 0) goto L_0x01b4
            boolean r13 = r12.isFresh()
            if (r13 == 0) goto L_0x01b4
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "page cached already(1). max_age : "
            r13.append(r14)
            long r0 = r12.maxAge
            r13.append(r0)
            java.lang.String r14 = ", fresh : "
            r13.append(r14)
            boolean r12 = r12.isFresh()
            r13.append(r12)
            java.lang.String r12 = r13.toString()
            java.lang.Throwable[] r13 = new java.lang.Throwable[r5]
            com.alibaba.android.prefetchx.PFLog.File.w(r12, r13)
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r12 = r11.mWrappedListener
            java.lang.String r13 = "already_requested"
            r12.onFailed(r3, r13)
            return
        L_0x01b4:
            if (r12 == 0) goto L_0x01bb
            java.util.Set<com.alibaba.android.prefetchx.core.file.PrefetchManager$PrefetchEntry> r13 = mCachedEntries
            r13.remove(r12)
        L_0x01bb:
            com.taobao.weex.adapter.IWXHttpAdapter r12 = r11.mHttpAdapter
            if (r12 != 0) goto L_0x01d9
            java.lang.String r12 = "http adapter is null"
            java.lang.Throwable[] r13 = new java.lang.Throwable[r5]
            com.alibaba.android.prefetchx.PFLog.File.w(r12, r13)
            com.alibaba.android.prefetchx.core.file.PrefetchManager$SafetyPrefetchResultListener r12 = r11.mWrappedListener
            java.lang.String r13 = "internal_error"
            r12.onFailed(r3, r13)
            java.lang.String r12 = "-40106"
            java.lang.String r13 = "internal_error"
            java.lang.Object[] r14 = new java.lang.Object[r4]
            r14[r5] = r2
            com.alibaba.android.prefetchx.PFMonitor.File.fail(r12, r13, r14)
            return
        L_0x01d9:
            com.taobao.weex.common.WXRequest r12 = new com.taobao.weex.common.WXRequest
            r12.<init>()
            java.util.HashMap r13 = new java.util.HashMap
            r13.<init>(r6)
            r12.paramMap = r13
            android.app.Application r13 = com.taobao.weex.WXEnvironment.getApplication()
            java.util.Map r3 = com.taobao.weex.WXEnvironment.getConfig()
            java.lang.String r13 = com.taobao.weex.http.WXHttpUtil.assembleUserAgent(r13, r3)
            boolean r3 = android.text.TextUtils.isEmpty(r13)
            if (r3 != 0) goto L_0x01fe
            java.util.Map<java.lang.String, java.lang.String> r3 = r12.paramMap
            java.lang.String r7 = "user-agent"
            r3.put(r7, r13)
        L_0x01fe:
            java.lang.String r13 = "GET"
            r12.method = r13
            java.lang.String r13 = r14.url
            r12.url = r13
            java.util.concurrent.atomic.AtomicBoolean r13 = shouldReport
            r13.set(r5)
            com.taobao.weex.adapter.IWXHttpAdapter r13 = r11.mHttpAdapter
            com.alibaba.android.prefetchx.core.file.PrefetchManager$2 r3 = new com.alibaba.android.prefetchx.core.file.PrefetchManager$2
            r3.<init>(r12, r14, r2)
            r13.sendRequest(r12, r3)
            boolean r12 = com.taobao.weex.WXEnvironment.isApkDebugable()
            if (r12 == 0) goto L_0x022f
            java.lang.Object[] r12 = new java.lang.Object[r6]
            java.lang.String r13 = "[doPrefetch] elapse time :"
            r12[r5] = r13
            long r13 = java.lang.System.currentTimeMillis()
            long r13 = r13 - r0
            java.lang.Long r13 = java.lang.Long.valueOf(r13)
            r12[r4] = r13
            com.alibaba.android.prefetchx.PFLog.File.d(r12)
        L_0x022f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.file.PrefetchManager.doPrefetch(java.lang.String, java.util.List, java.lang.String):void");
    }

    private PrefetchEntry isUrlRequestedBefore(@NonNull PrefetchEntry prefetchEntry) {
        if (mCachedEntries == null) {
            return null;
        }
        for (PrefetchEntry next : mCachedEntries) {
            if (next.equals(prefetchEntry)) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static long resolveMaxAgeFromHeaders(Map<String, List<String>> map) {
        if (map == null || map.isEmpty()) {
            return 300000;
        }
        List<String> headerFieldByKey = getHeaderFieldByKey(map, HttpConstant.CACHE_CONTROL);
        if (headerFieldByKey.isEmpty()) {
            return 300000;
        }
        for (String parseMaxAgeFromSingleValue : headerFieldByKey) {
            long parseMaxAgeFromSingleValue2 = parseMaxAgeFromSingleValue(parseMaxAgeFromSingleValue);
            if (parseMaxAgeFromSingleValue2 != -1) {
                return parseMaxAgeFromSingleValue2 * 1000;
            }
        }
        return 300000;
    }

    private static long parseMaxAgeFromSingleValue(@NonNull String str) {
        String[] split = str.split(",");
        int i = 0;
        while (i < split.length) {
            String trim = split[i].trim();
            if (trim.equals(HttpHeaderConstant.NO_CACHE) || trim.equals("no-store")) {
                return -1;
            }
            if (trim.startsWith("max-age=")) {
                try {
                    return Long.parseLong(trim.substring(8));
                } catch (Exception unused) {
                }
            } else {
                i++;
            }
        }
        return -1;
    }

    @NonNull
    private static List<String> getHeaderFieldByKey(Map<String, List<String>> map, String str) {
        if (map == null || map.isEmpty() || TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry next : map.entrySet()) {
            if (str.equalsIgnoreCase((String) next.getKey()) && next.getValue() != null && !((List) next.getValue()).isEmpty()) {
                arrayList.addAll((Collection) next.getValue());
            }
        }
        return arrayList;
    }

    @Nullable
    static PrefetchEntry findAlikeEntryInCache(@NonNull String str, @NonNull Set<PrefetchEntry> set) {
        if ("".equals(str)) {
            return null;
        }
        try {
            Iterator descendingIterator = new LinkedList(set).descendingIterator();
            while (descendingIterator.hasNext()) {
                PrefetchEntry prefetchEntry = (PrefetchEntry) descendingIterator.next();
                String removeSpecificQueryParamsInBaseUrl = removeSpecificQueryParamsInBaseUrl(str, prefetchEntry.ignoreParams);
                if (prefetchEntry.url != null && prefetchEntry.url.equals(removeSpecificQueryParamsInBaseUrl)) {
                    return prefetchEntry;
                }
            }
        } catch (Throwable th) {
            PFLog.File.w("error in findAlikeEntryInCache", th);
            HashMap hashMap = new HashMap(4);
            hashMap.put("url", str);
            hashMap.put("entries", set != null ? set.toString() : BuildConfig.buildJavascriptFrameworkVersion);
            hashMap.put("message", th.getMessage());
            PFMonitor.File.fail(PFConstant.File.PF_FILE_FIND_A_LIKE_URL, "error in findAlikeEntryInCache", JSON.toJSONString(hashMap));
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void setListener(@Nullable OnPrefetchResultListener onPrefetchResultListener) {
        if (onPrefetchResultListener != null) {
            this.mWrappedListener.setListener(onPrefetchResultListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        this.mExternalCacheChecker = null;
        this.mRemoteConfig = null;
        this.mProcessor = null;
        this.mHttpAdapter = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (sPrefetchCounter != null && !TextUtils.isEmpty(this.mInstanceId)) {
            sPrefetchCounter.remove(this.mInstanceId);
        }
        if (this.mWrappedListener != null) {
            this.mWrappedListener.setListener((OnPrefetchResultListener) null);
        }
    }

    static class Builder {
        private IConnection connection;
        private ExternalCacheChecker externalCacheChecker;
        private IWXHttpAdapter httpAdapter;
        private OnPrefetchResultListener listener;
        private UriProcessor processor;
        private RemoteConfigSpec.IFileModuleRemoteConfig remoteConfig;

        public Builder(@NonNull IWXHttpAdapter iWXHttpAdapter) {
            this.httpAdapter = iWXHttpAdapter;
        }

        /* access modifiers changed from: package-private */
        public Builder withConnectionCheck(IConnection iConnection) {
            this.connection = iConnection;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withExternalCacheChecker(ExternalCacheChecker externalCacheChecker2) {
            this.externalCacheChecker = externalCacheChecker2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withRemoteConfig(RemoteConfigSpec.IFileModuleRemoteConfig iFileModuleRemoteConfig) {
            this.remoteConfig = iFileModuleRemoteConfig;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withUriProcessor(UriProcessor uriProcessor) {
            this.processor = uriProcessor;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withListener(OnPrefetchResultListener onPrefetchResultListener) {
            this.listener = onPrefetchResultListener;
            return this;
        }

        /* access modifiers changed from: package-private */
        public PrefetchManager build() {
            PrefetchManager prefetchManager = new PrefetchManager(this.connection, this.externalCacheChecker, this.httpAdapter, this.remoteConfig, this.processor);
            if (this.listener != null) {
                prefetchManager.setListener(this.listener);
            }
            return prefetchManager;
        }
    }

    static class PrefetchEntry {
        List<String> ignoreParams;
        long lastModified;
        long maxAge;
        String url;

        PrefetchEntry() {
        }

        PrefetchEntry(String str, List<String> list) {
            this.url = str;
            this.ignoreParams = list;
        }

        /* access modifiers changed from: package-private */
        public boolean isFresh() {
            return System.currentTimeMillis() - this.lastModified <= this.maxAge;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PrefetchEntry prefetchEntry = (PrefetchEntry) obj;
            if (this.url == null ? prefetchEntry.url != null : !this.url.equals(prefetchEntry.url)) {
                return false;
            }
            if (this.ignoreParams != null) {
                return this.ignoreParams.equals(prefetchEntry.ignoreParams);
            }
            if (prefetchEntry.ignoreParams == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = (this.url != null ? this.url.hashCode() : 0) * 31;
            if (this.ignoreParams != null) {
                i = this.ignoreParams.hashCode();
            }
            return hashCode + i;
        }
    }

    private static class SafetyPrefetchResultListener implements OnPrefetchResultListener {
        private Handler handler;
        /* access modifiers changed from: private */
        public OnPrefetchResultListener listener;

        SafetyPrefetchResultListener(@NonNull Handler handler2) {
            this.handler = handler2;
        }

        /* access modifiers changed from: package-private */
        public void setListener(OnPrefetchResultListener onPrefetchResultListener) {
            this.listener = onPrefetchResultListener;
        }

        public void onSuccess(@NonNull final String str) {
            PFLog.File.d("onSuccess fired. listener:", this.listener, ",url:", str);
            if (this.listener != null && this.handler != null) {
                this.handler.post(WXThread.secure((Runnable) new Runnable() {
                    public void run() {
                        SafetyPrefetchResultListener.this.listener.onSuccess(str);
                    }
                }));
            }
        }

        public void onFailed(@NonNull final String str, @Nullable final String str2) {
            PFLog.File.d("onFailed fired. listener:", this.listener, ",url:", str, ",msg:", str2);
            if (this.listener != null && this.handler != null) {
                this.handler.post(WXThread.secure((Runnable) new Runnable() {
                    public void run() {
                        SafetyPrefetchResultListener.this.listener.onFailed(str, str2);
                    }
                }));
            }
        }
    }

    @VisibleForTesting
    static class LruLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        int maxSize;

        LruLinkedHashMap(int i) {
            super(16, 0.75f, true);
            this.maxSize = i;
        }

        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry<K, V> entry) {
            return size() > this.maxSize;
        }
    }
}
