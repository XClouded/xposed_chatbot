package com.taobao.android.dxcontainer.render;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class DXContainerRenderManager {
    private DXContainerNativeComponentRenderManager componentRenderManager = new DXContainerNativeComponentRenderManager();
    private Map<String, IDXContainerRender> renderMap = new HashMap();

    public DXContainerNativeComponentRenderManager getComponentRenderManager() {
        return this.componentRenderManager;
    }

    public void register(String str, IDXContainerRender iDXContainerRender) {
        if (iDXContainerRender != null && !TextUtils.isEmpty(str)) {
            this.renderMap.put(str, iDXContainerRender);
        }
    }

    public IDXContainerRender getRender(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return this.renderMap.get(str);
    }
}
