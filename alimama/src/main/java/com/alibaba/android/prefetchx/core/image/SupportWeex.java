package com.alibaba.android.prefetchx.core.image;

import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.List;
import java.util.Map;

public class SupportWeex extends WXModule {
    public static final String MODULE_NAME = "PrefetchXImage";

    public static void register() {
        try {
            WXSDKEngine.registerModule("PrefetchXImage", SupportWeex.class, true);
        } catch (Exception e) {
            PFLog.Image.w("error in register weex module. e.getMessage() is " + e.getMessage(), new Throwable[0]);
        }
    }

    @JSMethod(uiThread = false)
    public void prefetchImage(Map<String, Object> map, JSCallback jSCallback, JSCallback jSCallback2) {
        if (map == null || !map.containsKey("params")) {
            jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605017", "input is empty"));
        } else if (map.get("params") == null || !(map.get("params") instanceof List)) {
            jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605018", "input no params key"));
        } else {
            try {
                List list = (List) map.get("params");
                JSONObject valid = PFImage.getInstance().valid(list, "prefetchImage");
                if (valid != null) {
                    jSCallback2.invoke(valid);
                } else if (((Boolean) PFImage.getInstance().prefetchImage(list).first).booleanValue()) {
                    jSCallback.invoke(PFUtil.getJSCallbackSuccess("success"));
                } else {
                    jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605014", "phenix no response"));
                }
            } catch (Throwable th) {
                jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605012", "exception " + th.getMessage()));
            }
        }
    }

    @JSMethod(uiThread = false)
    public void prefetchImageWithSize(Map<String, List<Map<String, String>>> map, JSCallback jSCallback, JSCallback jSCallback2) {
        if (map == null || !map.containsKey("params")) {
            jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605017", "input is empty"));
        } else if (map.get("params") == null || !(map.get("params") instanceof List)) {
            jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605018", "input no params key"));
        } else {
            try {
                List list = map.get("params");
                JSONObject valid = PFImage.getInstance().valid(list, "prefetchImageWithSize");
                if (valid != null) {
                    jSCallback2.invoke(valid);
                } else if (((Boolean) PFImage.getInstance().prefetchImageWithSize(list, this.mWXSDKInstance).first).booleanValue()) {
                    jSCallback.invoke(PFUtil.getJSCallbackSuccess("success"));
                } else {
                    jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605024", "phenix no response"));
                }
            } catch (Throwable th) {
                jSCallback2.invoke(PFUtil.getJSCallbackError("", "-16605022", "exception " + th.getMessage()));
            }
        }
    }
}
