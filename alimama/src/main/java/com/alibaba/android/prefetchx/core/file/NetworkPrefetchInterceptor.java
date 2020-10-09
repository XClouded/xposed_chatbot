package com.alibaba.android.prefetchx.core.file;

import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import anet.channel.request.Request;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import anetwork.channel.interceptor.InterceptorManager;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.alibaba.android.prefetchx.core.file.PrefetchManager;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class NetworkPrefetchInterceptor implements Interceptor {
    private static final String TAG = "NetworkInterceptor";
    private RemoteConfigSpec.IFileModuleRemoteConfig mRemoteConfig = PrefetchX.getInstance().getGlobalOnlineConfigManager().getFileModuleConfig();

    private NetworkPrefetchInterceptor() {
    }

    public static void registerSelf() {
        try {
            InterceptorManager.addInterceptor(new NetworkPrefetchInterceptor());
        } catch (Throwable th) {
            PFLog.File.w("error in register InterceptorManager", th);
            PFMonitor.File.fail(PFConstant.File.PF_FILE_REGISTER_TO_NETWORK, "error in register InterceptorManager", th.getMessage());
        }
    }

    public Future intercept(Interceptor.Chain chain) {
        Request request = chain.request();
        Callback callback = chain.callback();
        if (Looper.myLooper() == Looper.getMainLooper() || !this.mRemoteConfig.isSwitchOn()) {
            return chain.proceed(request, callback);
        }
        if (request == null || TextUtils.isEmpty(request.getUrlString())) {
            return chain.proceed(request, callback);
        }
        Map<String, String> headers = request.getHeaders();
        if (headers != null && !"weex".equals(headers.get("f-refer"))) {
            return chain.proceed(request, callback);
        }
        String urlString = request.getUrlString();
        String findAlikeUrlInCache = findAlikeUrlInCache(urlString, PrefetchManager.getPrefetchEntries());
        if (!TextUtils.isEmpty(findAlikeUrlInCache) && !urlString.equals(findAlikeUrlInCache)) {
            request = request.newBuilder().setUrl(findAlikeUrlInCache).build();
        }
        return chain.proceed(request, callback);
    }

    @VisibleForTesting
    @Nullable
    static String findAlikeUrlInCache(@NonNull String str, @NonNull Set<PrefetchManager.PrefetchEntry> set) {
        PrefetchManager.PrefetchEntry findAlikeEntryInCache = PrefetchManager.findAlikeEntryInCache(str, set);
        if (findAlikeEntryInCache != null) {
            return findAlikeEntryInCache.url;
        }
        return null;
    }
}
