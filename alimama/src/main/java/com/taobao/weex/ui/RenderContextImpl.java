package com.taobao.weex.ui;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.RenderContext;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RenderContextImpl implements RenderContext {
    private Map<String, WXComponent> mRegistry = new ConcurrentHashMap();
    private WXSDKInstance mWXSDKInstance;

    public RenderContextImpl(WXSDKInstance wXSDKInstance) {
        this.mWXSDKInstance = wXSDKInstance;
    }

    public void destroy() {
        this.mWXSDKInstance = null;
        try {
            this.mRegistry.clear();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public WXSDKInstance getWXSDKInstance() {
        return this.mWXSDKInstance;
    }

    public WXSDKInstance getInstance() {
        return this.mWXSDKInstance;
    }

    public WXComponent getComponent(String str) {
        return this.mRegistry.get(str);
    }

    public void registerComponent(String str, WXComponent wXComponent) {
        this.mRegistry.put(str, wXComponent);
    }

    public WXComponent unregisterComponent(String str) {
        return this.mRegistry.remove(str);
    }

    public int getComponentCount() {
        return this.mRegistry.size();
    }
}
