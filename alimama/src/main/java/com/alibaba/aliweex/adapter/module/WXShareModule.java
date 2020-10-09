package com.alibaba.aliweex.adapter.module;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IShareModuleAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class WXShareModule extends WXModule {
    @JSMethod
    public void doShare(String str, JSCallback jSCallback) {
        IShareModuleAdapter shareModuleAdapter = AliWeex.getInstance().getShareModuleAdapter();
        if (shareModuleAdapter != null) {
            shareModuleAdapter.doShare(this.mWXSDKInstance.getContext(), str, jSCallback);
        }
    }
}
