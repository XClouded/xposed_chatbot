package com.taobao.android.dxcontainer.render;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class DXContainerNativeComponentRenderManager {
    private Map<String, IDXContainerComponentRender> componentRenderMap = new HashMap();

    public void register(String str, IDXContainerComponentRender iDXContainerComponentRender) {
        if (iDXContainerComponentRender != null && !TextUtils.isEmpty(str)) {
            this.componentRenderMap.put(str, iDXContainerComponentRender);
        }
    }

    public IDXContainerComponentRender getRender(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return this.componentRenderMap.get(str);
    }
}
