package com.taobao.vessel.local;

import com.taobao.vessel.base.ResultCallback;
import com.taobao.vessel.callback.VesselViewCallback;
import java.util.Map;

class VesselNativePlugin {
    public VesselViewCallback mViewCallback;

    VesselNativePlugin() {
    }

    public boolean execute(Map<String, Object> map, NativeCallbackContext nativeCallbackContext) {
        if (this.mViewCallback == null) {
            return true;
        }
        this.mViewCallback.viewCall(map, new ResultCallback(nativeCallbackContext));
        return true;
    }
}
