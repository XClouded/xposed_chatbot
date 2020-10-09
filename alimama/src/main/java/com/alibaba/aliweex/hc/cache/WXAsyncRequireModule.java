package com.alibaba.aliweex.hc.cache;

import alimama.com.unwrouter.UNWRouter;
import android.text.TextUtils;
import com.alibaba.aliweex.hc.cache.AssembleManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXModule;

public class WXAsyncRequireModule extends WXModule {
    @JSMethod
    public void require(JSONObject jSONObject, final JSCallback jSCallback, final JSCallback jSCallback2) {
        String string = jSONObject.getString(UNWRouter.PAGE_NAME);
        JSONArray jSONArray = jSONObject.getJSONArray("packages");
        if (!TextUtils.isEmpty(string) && jSONArray != null && jSONArray.size() > 0) {
            AssembleManager.getInstance().processAssembleWithDep(string, jSONObject.toJSONString(), new AssembleManager.IPageLoaderCallback() {
                public void onFinished(String str) {
                    if (jSCallback != null) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("result", (Object) "success");
                        jSONObject.put("data", (Object) str);
                        jSCallback.invoke(jSONObject);
                    }
                }

                public void onFailed() {
                    if (jSCallback2 != null) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("result", (Object) Constants.Event.FAIL);
                        jSONObject.put("message", (Object) "process failed");
                        jSCallback2.invoke(jSONObject);
                    }
                }
            });
        }
    }
}
