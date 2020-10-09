package com.alibaba.aliweex.adapter.module;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IAliPayModuleAdapter;
import com.alibaba.aliweex.adapter.ICallback;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class WXAliPayModule extends WXModule {
    @JSMethod
    public void tradePay(JSONObject jSONObject, final JSCallback jSCallback, final JSCallback jSCallback2) {
        IAliPayModuleAdapter aliPayModuleAdapter = AliWeex.getInstance().getAliPayModuleAdapter();
        if (aliPayModuleAdapter != null) {
            aliPayModuleAdapter.tradePay(this.mWXSDKInstance, jSONObject, new ICallback() {
                public void success(JSONObject jSONObject) {
                    jSCallback.invoke(jSONObject);
                }

                public void failure(JSONObject jSONObject) {
                    jSCallback2.invoke(jSONObject);
                }
            });
        } else {
            notSupported(jSCallback2);
        }
    }

    private void notSupported(JSCallback jSCallback) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("result", (Object) "WX_NOT_SUPPORTED");
        jSCallback.invoke(jSONObject);
    }
}
