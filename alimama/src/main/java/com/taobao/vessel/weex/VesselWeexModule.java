package com.taobao.vessel.weex;

import com.taobao.vessel.base.ResultCallback;
import com.taobao.vessel.base.VesselCallbackManager;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import java.util.Map;

public class VesselWeexModule extends WXModule {
    @WXModuleAnno
    public void vesselCall(Map<String, Object> map, JSCallback jSCallback) {
        VesselCallbackManager.getInstance().notifyCallback(this.mWXSDKInstance, map, new ResultCallback(jSCallback));
    }
}
