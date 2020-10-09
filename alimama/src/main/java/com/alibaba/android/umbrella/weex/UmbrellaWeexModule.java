package com.alibaba.android.umbrella.weex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.export.UmbrellaServiceFetcher;
import com.alibaba.android.umbrella.link.UMLinkLogInterface;
import com.alibaba.android.umbrella.link.UMRefContext;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.export.UMUserData;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import java.util.Map;

public class UmbrellaWeexModule extends WXModule {
    private final UMLinkLogInterface umbrella = UmbrellaServiceFetcher.getUmbrella();

    @JSMethod(uiThread = false)
    public void logInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable Map<String, Object> map2) {
        this.umbrella.logInfo(str, str2, str3, uMRefContext, map, UMUserData.fromMap(map2));
    }

    @JSMethod(uiThread = false)
    public void commitFailure(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        this.umbrella.commitFailure(str, str2, str3, str4, str5, map, str6, str7);
    }

    @JSMethod(uiThread = false)
    public void commitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        this.umbrella.commitSuccess(str, str2, str3, str4, str5, map);
    }
}
