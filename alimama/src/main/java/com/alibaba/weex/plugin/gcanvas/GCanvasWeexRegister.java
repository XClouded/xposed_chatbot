package com.alibaba.weex.plugin.gcanvas;

import com.taobao.gcanvas.util.GLog;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;

public class GCanvasWeexRegister {
    private GCanvasWeexRegister() {
    }

    public static void register() {
        try {
            WXSDKEngine.registerModule("gcanvas", GCanvasLightningModule.class);
            WXSDKEngine.registerComponent("gcanvas", (Class<? extends WXComponent>) WXGCanvasLigntningComponent.class);
        } catch (WXException e) {
            e.printStackTrace();
            GLog.e("GCanvasWeexRegister", "error when register gcanvas:" + e.getCause());
        }
    }
}
