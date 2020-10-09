package com.alibaba.android.prefetchx.core.data.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import com.taobao.weaver.prefetch.PrefetchStatusResponse;
import com.taobao.weaver.prefetch.WMLPrefetch;
import java.util.Map;

public class PFDataCallbackImpl implements PFDataCallback {
    private boolean hasWMLPrefetchDependency = true;

    public boolean beforeMtopSend(Context context, JSONObject jSONObject, @Nullable PrefetchDataCallback prefetchDataCallback, @NonNull Map<String, Object> map) {
        return true;
    }

    public Uri beforeProcessUrl(Context context, Uri uri, @NonNull Map<String, Object> map) {
        return uri;
    }

    public String changeKeyBeforeMtopSend(String str, Map<String, Object> map) {
        return str;
    }

    public String changeValueBeforeMtopSave(String str, String str2, Map<String, Object> map) {
        return str2;
    }

    public String getMtopConfigByUrl(Context context, Uri uri, @NonNull Map<String, Object> map) {
        return null;
    }

    public String onMtopReturn(boolean z, String str, @NonNull Map<String, Object> map) {
        return str;
    }

    public String afterMtopConfigAssembled(Context context, Uri uri, String str, @NonNull Map<String, Object> map) {
        if (!this.hasWMLPrefetchDependency) {
            return null;
        }
        try {
            PrefetchStatusResponse prefetchStatusResponse = new PrefetchStatusResponse();
            prefetchStatusResponse.feature = "data";
            prefetchStatusResponse.status = 4;
            prefetchStatusResponse.message = "mtop配置已获取";
            prefetchStatusResponse.extra = map;
            WMLPrefetch.getInstance().getPrefetchStatusCallback().report(String.valueOf(map.get("originalUrl")), prefetchStatusResponse);
            return null;
        } catch (Throwable unused) {
            this.hasWMLPrefetchDependency = false;
            PFLog.Data.w("error in report. point afterMtopConfigAssembled", new Throwable[0]);
            return null;
        }
    }

    public void afterMtopSend(Context context, JSONObject jSONObject, @NonNull Map<String, Object> map) {
        if (this.hasWMLPrefetchDependency) {
            try {
                PrefetchStatusResponse prefetchStatusResponse = new PrefetchStatusResponse();
                prefetchStatusResponse.feature = "data";
                prefetchStatusResponse.status = 4;
                prefetchStatusResponse.message = "mtop已发送";
                prefetchStatusResponse.extra = map;
                WMLPrefetch.getInstance().getPrefetchStatusCallback().report(String.valueOf(map.get("originalUrl")), prefetchStatusResponse);
            } catch (Throwable unused) {
                this.hasWMLPrefetchDependency = false;
                PFLog.Data.w("error in report. point afterMtopSend", new Throwable[0]);
            }
        }
    }

    public void afterMtopSave(String str, String str2, @NonNull Map<String, Object> map) {
        if (this.hasWMLPrefetchDependency) {
            try {
                PrefetchStatusResponse prefetchStatusResponse = new PrefetchStatusResponse();
                prefetchStatusResponse.feature = "data";
                prefetchStatusResponse.status = 1;
                prefetchStatusResponse.message = "mtop请求成功";
                prefetchStatusResponse.extra = map;
                WMLPrefetch.getInstance().getPrefetchStatusCallback().report(String.valueOf(map.get("originalUrl")), prefetchStatusResponse);
            } catch (Throwable unused) {
                this.hasWMLPrefetchDependency = false;
                PFLog.Data.w("error in report. point afterMtopSave", new Throwable[0]);
            }
        }
    }
}
