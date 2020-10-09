package com.alibaba.android.prefetchx.core.data;

import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.core.data.StorageInterface;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class SupportWeex extends WXModule {
    public static final String MODULE_NAME = "PrefetchXData";

    public static void register() {
        try {
            WXSDKEngine.registerModule(MODULE_NAME, SupportWeex.class, true);
        } catch (Exception e) {
            PFLog.Data.w("error in register weex module of data. e.getMessage() is " + e.getMessage(), new Throwable[0]);
        }
    }

    public static String prefetchData(WXSDKInstance wXSDKInstance, String str) {
        return PFMtop.getInstance().prefetch(wXSDKInstance, str);
    }

    @JSMethod
    public String prefetchData(String str) {
        return PFMtop.getInstance().prefetch(this.mWXSDKInstance, str);
    }

    @JSMethod
    public void getResult(String str, final JSCallback jSCallback) {
        StorageWeex.getInstance().readAsync(str, new StorageInterface.Callback() {
            public void onSuccess(String str) {
                jSCallback.invoke(PFUtil.getJSCallbackSuccess(str));
            }

            public void onError(String str, String str2) {
                jSCallback.invoke(PFUtil.getJSCallbackError("", str, str2));
            }
        });
    }

    @JSMethod
    public void removeResult(String str) {
        StorageWeex.getInstance().remove(str);
    }
}
