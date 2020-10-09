package com.taobao.weex.jsEngine;

import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.utils.WXLogUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSEngine implements Serializable {
    public static final ConcurrentHashMap<Long, JSContext> mCreatedJSContext = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<JSBiz, EnvCallback> mEnvCallbacks = new ConcurrentHashMap<>();
    private static JSEngine mJsEngine;

    private JSEngine() {
    }

    public static JSEngine getInstance() {
        if (mJsEngine == null) {
            synchronized (JSEngine.class) {
                if (mJsEngine == null) {
                    mJsEngine = new JSEngine();
                }
            }
        }
        return mJsEngine;
    }

    public void registerEnvCallback(JSBiz jSBiz, EnvCallback envCallback) {
        if (jSBiz != null && envCallback != null) {
            mEnvCallbacks.put(jSBiz, envCallback);
        }
    }

    public void unRegisterEnvCallback(JSBiz jSBiz) {
        if (jSBiz != null) {
            mEnvCallbacks.remove(jSBiz);
        }
    }

    @Nullable
    public JSContext createJSContext() {
        if (WXSDKEngine.isInitialized()) {
            return new JSContext();
        }
        WXLogUtils.e("Create JSContext Failed because of weex has not been initialized");
        return null;
    }

    public void engineInitFinished() {
        for (Map.Entry<JSBiz, EnvCallback> value : mEnvCallbacks.entrySet()) {
            ((EnvCallback) value.getValue()).error(CallBackCode.JSENGINE_INIT_FINISH);
        }
    }

    public void engineCrashed() {
        for (Map.Entry<JSBiz, EnvCallback> value : mEnvCallbacks.entrySet()) {
            ((EnvCallback) value.getValue()).error(CallBackCode.ERROR_JSENGINE_CRASHED);
        }
        HashMap hashMap = new HashMap(mCreatedJSContext);
        mCreatedJSContext.clear();
        for (Map.Entry value2 : hashMap.entrySet()) {
            ((JSContext) value2.getValue()).destroy();
        }
        hashMap.clear();
    }
}
