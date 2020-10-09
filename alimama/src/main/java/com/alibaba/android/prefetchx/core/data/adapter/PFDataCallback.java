package com.alibaba.android.prefetchx.core.data.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import java.util.Map;

public interface PFDataCallback {
    String afterMtopConfigAssembled(Context context, Uri uri, @Nullable String str, @NonNull Map<String, Object> map);

    void afterMtopSave(String str, String str2, @NonNull Map<String, Object> map);

    void afterMtopSend(Context context, JSONObject jSONObject, @NonNull Map<String, Object> map);

    boolean beforeMtopSend(Context context, JSONObject jSONObject, @Nullable PrefetchDataCallback prefetchDataCallback, @NonNull Map<String, Object> map);

    Uri beforeProcessUrl(Context context, Uri uri, @NonNull Map<String, Object> map);

    String changeKeyBeforeMtopSend(String str, Map<String, Object> map);

    String changeValueBeforeMtopSave(String str, String str2, Map<String, Object> map);

    String getMtopConfigByUrl(Context context, Uri uri, @NonNull Map<String, Object> map);

    String onMtopReturn(boolean z, String str, @NonNull Map<String, Object> map);
}
