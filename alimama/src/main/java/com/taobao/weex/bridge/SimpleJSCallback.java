package com.taobao.weex.bridge;

public class SimpleJSCallback implements JSCallback {
    String mCallbackId;
    String mInstanceId;
    private InvokerCallback mInvokerCallback;

    interface InvokerCallback {
        void onInvokeSuccess();
    }

    public void setInvokerCallback(InvokerCallback invokerCallback) {
        this.mInvokerCallback = invokerCallback;
    }

    public String getCallbackId() {
        return this.mCallbackId;
    }

    public SimpleJSCallback(String str, String str2) {
        this.mCallbackId = str2;
        this.mInstanceId = str;
    }

    public void invoke(Object obj) {
        WXBridgeManager.getInstance().callbackJavascript(this.mInstanceId, this.mCallbackId, obj, false);
        if (this.mInvokerCallback != null) {
            this.mInvokerCallback.onInvokeSuccess();
        }
    }

    public void invokeAndKeepAlive(Object obj) {
        WXBridgeManager.getInstance().callbackJavascript(this.mInstanceId, this.mCallbackId, obj, true);
    }
}
