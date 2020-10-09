package com.alibaba.aliweex.utils;

import android.view.View;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.WXComponent;

public class WXModuleUtils {
    private WXModuleUtils() {
    }

    @Nullable
    public static View findViewByRef(@Nullable String str, @Nullable String str2) {
        WXComponent findComponentByRef = findComponentByRef(str, str2);
        if (findComponentByRef == null) {
            return null;
        }
        return findComponentByRef.getHostView();
    }

    @Nullable
    public static WXComponent findComponentByRef(@Nullable String str, @Nullable String str2) {
        return WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
    }
}
