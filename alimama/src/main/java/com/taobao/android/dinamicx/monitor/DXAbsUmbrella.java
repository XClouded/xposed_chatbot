package com.taobao.android.dinamicx.monitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;

public abstract class DXAbsUmbrella {
    public abstract void commitFailure(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7);

    public abstract void commitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map);

    public abstract void logError(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable String str4, @NonNull String str5, @NonNull String str6, @Nullable Map<String, Object> map, @Nullable Map<String, Object> map2);
}
