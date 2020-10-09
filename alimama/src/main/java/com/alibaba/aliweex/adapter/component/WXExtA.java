package com.alibaba.aliweex.adapter.component;

import com.alibaba.aliweex.plugin.SpmMonitor;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXA;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;

public class WXExtA extends WXA {
    public WXExtA(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(WXFrameLayout wXFrameLayout) {
        addClickListener(new WXComponent.OnClickListener() {
            public void onHostViewClick() {
                new SpmMonitor(WXExtA.this.getInstance().getRootComponent()).doTrace(WXExtA.this);
            }
        });
        super.onHostViewInitialized(wXFrameLayout);
    }
}
