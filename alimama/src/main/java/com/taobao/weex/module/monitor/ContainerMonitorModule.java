package com.taobao.weex.module.monitor;

import android.content.Context;
import com.taobao.weex.WXActivity;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class ContainerMonitorModule extends WXModule {
    @JSMethod(uiThread = false)
    public void pageRenderFinished() {
        if (this.mWXSDKInstance != null) {
            Context context = this.mWXSDKInstance.getContext();
            if (context instanceof WXActivity) {
                ((WXActivity) context).pageFinishCommit();
            }
        }
    }
}
