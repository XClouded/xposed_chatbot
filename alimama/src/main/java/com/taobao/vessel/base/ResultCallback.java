package com.taobao.vessel.base;

import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.taobao.vessel.local.NativeCallbackContext;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.bridge.JSCallback;

public class ResultCallback implements JSCallback {
    private JSCallback mJsCallback;
    private NativeCallbackContext mNativeCallback;
    private WVCallBackContext mWindCallback;

    public void invokeAndKeepAlive(Object obj) {
    }

    public ResultCallback(JSCallback jSCallback) {
        this.mJsCallback = jSCallback;
    }

    public ResultCallback(WVCallBackContext wVCallBackContext) {
        this.mWindCallback = wVCallBackContext;
    }

    public ResultCallback(NativeCallbackContext nativeCallbackContext) {
        this.mNativeCallback = nativeCallbackContext;
    }

    public void invoke(Object obj) {
        if (this.mJsCallback != null) {
            this.mJsCallback.invoke(obj);
        }
        if (this.mWindCallback != null) {
            this.mWindCallback.success(Utils.mapToString(obj));
        }
        if (this.mNativeCallback != null) {
            this.mNativeCallback.invoke(obj);
        }
    }
}
