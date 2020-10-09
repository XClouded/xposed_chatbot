package com.alibaba.android.prefetchx.core.file;

import android.net.Uri;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import anet.channel.strategy.StrategyCenter;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.core.file.PrefetchManager;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WXFilePrefetchModule extends WXSDKEngine.DestroyableModule {
    public static final String PREFETCH_MODULE_NAME = "prefetch";
    private static final String TAG = "WXPrefetchModule";
    private static final String WH_WX = "wh_weex";
    private static final String WX_TPL = "_wx_tpl";
    private PrefetchManager mPrefetchManager;

    @JSMethod
    public void addTask(@Nullable String str, @Nullable List<String> list) {
        if (!TextUtils.isEmpty(str) && this.mWXSDKInstance != null && this.mWXSDKInstance.getContext() != null && this.mWXSDKInstance.getWXHttpAdapter() != null) {
            if (this.mPrefetchManager == null) {
                this.mPrefetchManager = createPrefetchManagerIfNeeded();
            }
            try {
                String uri = this.mWXSDKInstance.rewriteUri(Uri.parse(str), "request").toString();
                if (this.mWXSDKInstance.getInstanceId() != null) {
                    this.mPrefetchManager.doPrefetchWithDelay(uri, supplyIgnoreParams(list), this.mWXSDKInstance.getInstanceId(), true);
                } else {
                    PFLog.File.w("instance id not found", new Throwable[0]);
                }
            } catch (Throwable th) {
                PFLog.File.w("exception in addTask", th);
                HashMap hashMap = new HashMap(4);
                hashMap.put("url", str);
                hashMap.put("ignoreParams", list != null ? list.toString() : BuildConfig.buildJavascriptFrameworkVersion);
                hashMap.put("message", th.getMessage());
                PFMonitor.File.fail(PFConstant.File.PF_FILE_EXCEPTION_ADD_TASK, "exception in addTask", JSON.toJSONString(hashMap));
            }
        }
    }

    @NonNull
    private PrefetchManager createPrefetchManagerIfNeeded() {
        return PrefetchManager.create(this.mWXSDKInstance.getWXHttpAdapter()).withRemoteConfig(PrefetchX.getInstance().getGlobalOnlineConfigManager().getFileModuleConfig()).withUriProcessor(new PrefetchManager.UriProcessor() {
            @NonNull
            public String processUri(@NonNull String str) {
                Uri access$000 = WXFilePrefetchModule.getBundleUri(str);
                String formalizeUrl = access$000 != null ? StrategyCenter.getInstance().getFormalizeUrl(access$000.toString()) : null;
                return formalizeUrl == null ? str : formalizeUrl;
            }
        }).withConnectionCheck(DefaultConnectionImpl.newInstance(this.mWXSDKInstance.getContext())).withExternalCacheChecker(new PrefetchManager.ExternalCacheChecker() {
            public boolean isCachedAlready(@NonNull String str) {
                long currentTimeMillis = WXEnvironment.isApkDebugable() ? System.currentTimeMillis() : 0;
                boolean z = ZipAppUtils.getStreamByUrl(str) != null;
                if (WXEnvironment.isApkDebugable()) {
                    PFLog.File.d("[zcache] elapse time: ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
                return z;
            }
        }).withListener(new PrefetchManager.OnPrefetchResultListener() {
            public void onSuccess(@NonNull String str) {
                if (WXFilePrefetchModule.this.mWXSDKInstance.checkModuleEventRegistered("load", WXFilePrefetchModule.this)) {
                    HashMap hashMap = new HashMap(2);
                    hashMap.put("url", str);
                    WXFilePrefetchModule.this.mWXSDKInstance.fireModuleEvent("load", WXFilePrefetchModule.this, hashMap);
                    PFLog.File.d("send load event success.");
                    return;
                }
                PFLog.File.d("no listener found. drop the load event.");
            }

            public void onFailed(@NonNull String str, @Nullable String str2) {
                if (WXFilePrefetchModule.this.mWXSDKInstance.checkModuleEventRegistered("error", WXFilePrefetchModule.this)) {
                    HashMap hashMap = new HashMap(2);
                    hashMap.put("url", str);
                    hashMap.put("msg", str2);
                    WXFilePrefetchModule.this.mWXSDKInstance.fireModuleEvent("error", WXFilePrefetchModule.this, hashMap);
                    PFLog.File.d("send error event success.");
                    return;
                }
                PFLog.File.d("no listener found. drop the error event.");
            }
        }).build();
    }

    /* access modifiers changed from: private */
    @Nullable
    public static Uri getBundleUri(@NonNull String str) {
        Uri parse;
        Uri parse2 = Uri.parse(str);
        if (parse2.getBooleanQueryParameter("wh_weex", false)) {
            return parse2;
        }
        String queryParameter = parse2.getQueryParameter("_wx_tpl");
        if (TextUtils.isEmpty(queryParameter) || (parse = Uri.parse(queryParameter)) == null) {
            return null;
        }
        Set<String> queryParameterNames = parse2.getQueryParameterNames();
        Uri.Builder buildUpon = parse.buildUpon();
        for (String next : queryParameterNames) {
            if (!TextUtils.equals(next, "_wx_tpl")) {
                buildUpon.appendQueryParameter(next, parse2.getQueryParameter(next));
            }
        }
        return buildUpon.build();
    }

    private List<String> supplyIgnoreParams(@Nullable List<String> list) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("_wx_f_");
        if (list != null && !list.isEmpty()) {
            for (String next : list) {
                if (!"_wx_f_".equals(next)) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public void destroy() {
        if (this.mPrefetchManager != null) {
            this.mPrefetchManager.destroy();
        }
    }
}
