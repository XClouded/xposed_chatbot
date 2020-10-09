package com.alibaba.aliweex.adapter.module.mtop;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;

public class WXMtopModule extends WXModule implements Destroyable {

    public enum MTOP_VERSION {
        V1,
        V2
    }

    public void destroy() {
    }

    @JSMethod
    public void send(String str, JSCallback jSCallback) {
        new WXMtopRequest(MTOP_VERSION.V1).setInstanceId(this.mWXSDKInstance.getInstanceId()).send(this.mWXSDKInstance.getContext(), str, jSCallback, (JSCallback) null);
    }

    @JSMethod
    public void request(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        new WXMtopRequest(MTOP_VERSION.V2).setInstanceId(this.mWXSDKInstance.getInstanceId()).request(this.mWXSDKInstance.getContext(), jSONObject, jSCallback, jSCallback2);
    }
}
