package com.taobao.weex.jsEngine;

import com.taobao.weex.base.CalledByNative;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.utils.WXLogUtils;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class JSContext implements Serializable {
    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, JSFunction> funcMap = new ConcurrentHashMap<>();
    private JSException mExceptionTransfer = null;
    /* access modifiers changed from: private */
    public long mNativeContextPtr = 0;

    /* access modifiers changed from: private */
    public native void nativeBindFunc(long j, String str);

    /* access modifiers changed from: private */
    public native long nativeCreateContext();

    /* access modifiers changed from: private */
    public native void nativeDestroyContext(long j);

    /* access modifiers changed from: private */
    public native void nativeExecJS(long j, String str);

    /* access modifiers changed from: private */
    public native void nativeUnBindFunc(long j, String str);

    protected JSContext() {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                long unused = JSContext.this.mNativeContextPtr = JSContext.this.nativeCreateContext();
                JSEngine.mCreatedJSContext.put(Long.valueOf(JSContext.this.mNativeContextPtr), JSContext.this);
            }
        });
    }

    public void registerException(JSException jSException) {
        this.mExceptionTransfer = jSException;
    }

    public void destroy() {
        JSEngine.mCreatedJSContext.remove(Long.valueOf(this.mNativeContextPtr));
        this.mExceptionTransfer = null;
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (JSContext.this.mNativeContextPtr != 0) {
                    long access$000 = JSContext.this.mNativeContextPtr;
                    long unused = JSContext.this.mNativeContextPtr = 0;
                    JSContext.this.nativeDestroyContext(access$000);
                }
            }
        });
    }

    public void bindFunction(final String str, final JSFunction jSFunction) {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                JSContext.this.funcMap.put(str, jSFunction);
                JSContext.this.nativeBindFunc(JSContext.this.mNativeContextPtr, str);
            }
        });
    }

    public void unBindFunction(final String str) {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                JSContext.this.funcMap.remove(str);
                JSContext.this.nativeUnBindFunc(JSContext.this.mNativeContextPtr, str);
            }
        });
    }

    public void Eval(final String str) {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                JSContext.this.nativeExecJS(JSContext.this.mNativeContextPtr, str);
            }
        });
    }

    @CalledByNative
    public String Invoke(String str, String str2) {
        JSFunction jSFunction = this.funcMap.get(str);
        if (jSFunction == null) {
            return "";
        }
        WXLogUtils.d("jsEngine invoke " + str + " arg:" + str2);
        return jSFunction.invoke(str2);
    }

    @CalledByNative
    public void Exception(String str) {
        if (str != null && this.mExceptionTransfer != null) {
            this.mExceptionTransfer.exception(str);
        }
    }
}
