package com.alibaba.android.enhance.svg;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Collections;
import java.util.Map;

public class SVGHelperModule extends WXModule {
    @JSMethod(uiThread = false)
    public int getTotalLength(@Nullable String str) {
        RenderableSVGVirtualComponent targetComponentOrNull;
        if (!TextUtils.isEmpty(str) && (targetComponentOrNull = getTargetComponentOrNull(str)) != null) {
            return targetComponentOrNull.getTotalLength();
        }
        return 0;
    }

    @JSMethod(uiThread = false)
    public Map<String, Float> getPointAtLength(@Nullable String str, float f) {
        if (TextUtils.isEmpty(str) || f < 0.0f) {
            return Collections.emptyMap();
        }
        RenderableSVGVirtualComponent targetComponentOrNull = getTargetComponentOrNull(str);
        if (targetComponentOrNull == null) {
            return Collections.emptyMap();
        }
        return targetComponentOrNull.getPointAtLength(f);
    }

    @JSMethod(uiThread = false)
    public double getDegreeAtLength(@Nullable String str, float f) {
        RenderableSVGVirtualComponent targetComponentOrNull;
        if (!TextUtils.isEmpty(str) && (targetComponentOrNull = getTargetComponentOrNull(str)) != null) {
            return targetComponentOrNull.getDegreeAtLength(f);
        }
        return 0.0d;
    }

    private RenderableSVGVirtualComponent getTargetComponentOrNull(String str) {
        if (this.mWXSDKInstance == null || TextUtils.isEmpty(this.mWXSDKInstance.getInstanceId())) {
            return null;
        }
        WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.mWXSDKInstance.getInstanceId(), str);
        if (wXComponent instanceof RenderableSVGVirtualComponent) {
            return (RenderableSVGVirtualComponent) wXComponent;
        }
        return null;
    }
}
