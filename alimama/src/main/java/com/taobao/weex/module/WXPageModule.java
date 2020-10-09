package com.taobao.weex.module;

import com.taobao.weex.WXActivity;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class WXPageModule extends WXModule {
    @JSMethod
    public void renderFinished() {
        if (this.mWXSDKInstance != null && (this.mWXSDKInstance.getContext() instanceof WXActivity)) {
            ((WXActivity) this.mWXSDKInstance.getContext()).hideImageView();
        }
    }
}
