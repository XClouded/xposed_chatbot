package com.alibaba.aliweex.adapter.module;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class WXEventModule extends WXModule {
    @JSMethod
    public void openURL(String str) {
        IEventModuleAdapter eventModuleAdapter = AliWeex.getInstance().getEventModuleAdapter();
        if (eventModuleAdapter != null) {
            eventModuleAdapter.openURL(this.mWXSDKInstance.getContext(), str);
        }
    }
}
