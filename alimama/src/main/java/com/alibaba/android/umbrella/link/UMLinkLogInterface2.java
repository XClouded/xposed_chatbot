package com.alibaba.android.umbrella.link;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.export.UMTagKey;
import java.util.Map;

public interface UMLinkLogInterface2 {
    void commitFailure(String str, String str2, String str3, String str4, String str5, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, ?> map2, String str6, String str7);

    void commitSuccess(String str, String str2, String str3, String str4, String str5, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, ?> map2);

    UMRefContext createLinkId(String str);

    void logBegin(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, ?> map2);

    void logEnd(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, ?> map2);

    void logError(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, ?> map2);

    void logInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, ?> map2);

    void logMtopReq(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable Map<UMTagKey, String> map, @Nullable Map<String, ?> map2);

    void logMtopResponse(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull Object obj, @NonNull String str3, @Nullable Map<UMTagKey, String> map, @Nullable Map<String, ?> map2);

    void logPageLifecycle(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, int i2, @NonNull String str3, @NonNull String str4, @Nullable Map<UMTagKey, String> map, @Nullable Map<String, ?> map2);

    void logSimpleInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable String str4);

    void logUIAction(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, @NonNull String str3, @NonNull String str4, @Nullable String str5, @Nullable Map<UMTagKey, String> map, @Nullable Map<String, ?> map2);
}
