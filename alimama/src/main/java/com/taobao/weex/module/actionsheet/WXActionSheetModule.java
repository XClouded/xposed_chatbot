package com.taobao.weex.module.actionsheet;

import android.content.Context;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class WXActionSheetModule extends WXModule {
    @JSMethod(uiThread = true)
    public void show(String str, JSCallback jSCallback) {
        Context context = this.mWXSDKInstance.getContext();
        if (context != null) {
            ActionSheet.showActionSheet(context, str, jSCallback);
        }
    }
}
