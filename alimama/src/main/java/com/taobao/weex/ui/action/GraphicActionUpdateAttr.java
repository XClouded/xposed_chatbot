package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;

public class GraphicActionUpdateAttr extends BasicGraphicAction {
    private WXComponent component = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
    private Map<String, String> mAttrs;

    public GraphicActionUpdateAttr(WXSDKInstance wXSDKInstance, String str, Map<String, String> map) {
        super(wXSDKInstance, str);
        this.mAttrs = map;
        if (this.component != null && this.mAttrs != null) {
            this.component.addAttr(this.mAttrs);
        }
    }

    public void executeAction() {
        if (this.component != null) {
            this.component.getAttrs().mergeAttr();
            this.component.updateAttrs((Map<String, Object>) this.mAttrs);
        }
    }
}
