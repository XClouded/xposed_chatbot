package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;

public class GraphicActionAppendTreeCreateFinish extends BasicGraphicAction {
    WXComponent component;

    public void executeAction() {
    }

    public GraphicActionAppendTreeCreateFinish(WXSDKInstance wXSDKInstance, String str) {
        super(wXSDKInstance, str);
        this.component = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), str);
        if (this.component != null && (this.component instanceof WXVContainer)) {
            ((WXVContainer) this.component).appendTreeCreateFinish();
        }
    }
}
