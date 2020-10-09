package com.alibaba.android.umbrella.export;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.UMLinkLogInterface;
import com.alibaba.android.umbrella.link.UMRefContext;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.export.UMTagKey;
import com.alibaba.android.umbrella.link.export.UMUserData;
import java.util.Map;

final class UMLinkLogFallbackImpl implements UMLinkLogInterface {
    public void commitFailure(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
    }

    public void commitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
    }

    public UMRefContext createLinkId(String str) {
        return null;
    }

    public void logBegin(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
    }

    public void logEnd(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
    }

    public void logError(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
    }

    public void logErrorRawDim(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<String, Object> map, @Nullable UMUserData uMUserData) {
    }

    public void logInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
    }

    public void logInfoRawDim(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<String, Object> map, @Nullable UMUserData uMUserData) {
    }

    public void logMtopReq(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
    }

    public void logMtopResponse(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull Object obj, @NonNull String str3, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
    }

    public void logPageLifecycle(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, int i2, @NonNull String str3, @NonNull String str4, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
    }

    public void logPageLifecycle2(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
    }

    public void logSimpleInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable String str4) {
    }

    public void logUIAction(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, @NonNull String str3, @NonNull String str4, @Nullable String str5, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
    }

    public void logUIAction2(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable String str6, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
    }

    UMLinkLogFallbackImpl() {
    }
}
