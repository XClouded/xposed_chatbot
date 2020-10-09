package com.alibaba.android.prefetchx.core.image;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.WVResult;
import android.util.Pair;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.uikit.feature.features.FeatureFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SupportH5 extends WVApiPlugin {
    public static final String MODULE_NAME = "PrefetchXImage";

    public static void register() {
        try {
            WVPluginManager.registerPlugin("PrefetchXImage", (Class<? extends WVApiPlugin>) SupportH5.class);
        } catch (Exception e) {
            PFLog.Image.w("error in register windvane module. e.getMessage() is " + e.getMessage(), new Throwable[0]);
        }
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        JSONArray jSONArray = JSON.parseObject(str2).getJSONArray("params");
        if (jSONArray == null) {
            WVResult wVResult = new WVResult();
            wVResult.addData(ILocatable.ERROR_MSG, "no params key");
            wVCallBackContext.error(wVResult);
            return false;
        } else if ("prefetchImage".equals(str)) {
            prefetchImage(jSONArray, wVCallBackContext);
            return true;
        } else if ("prefetchImageWithSize".equals(str)) {
            prefetchImageWithSize(jSONArray, wVCallBackContext);
            return true;
        } else {
            WVResult wVResult2 = new WVResult();
            wVResult2.addData(ILocatable.ERROR_MSG, "no matched method");
            wVCallBackContext.error(wVResult2);
            return false;
        }
    }

    private void prefetchImage(JSONArray jSONArray, WVCallBackContext wVCallBackContext) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            Iterator<Object> it = jSONArray.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof String) {
                    arrayList.add(next.toString());
                }
            }
        }
        if (PFImage.getInstance().valid(arrayList, "prefetchImageWindvane") != null) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
            return;
        }
        Pair<Boolean, Map<String, Object>> prefetchImage = PFImage.getInstance().prefetchImage(arrayList);
        if (((Boolean) prefetchImage.first).booleanValue()) {
            wVCallBackContext.success();
            return;
        }
        WVResult wVResult = WVResult.RET_FAIL;
        wVResult.addData("errorCode", ((Map) prefetchImage.second).get("errorCode"));
        wVResult.addData(ILocatable.ERROR_MSG, ((Map) prefetchImage.second).get(ILocatable.ERROR_MSG));
        wVCallBackContext.error(wVResult);
    }

    private void prefetchImageWithSize(JSONArray jSONArray, WVCallBackContext wVCallBackContext) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            Iterator<Object> it = jSONArray.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof JSONObject) {
                    HashMap hashMap = new HashMap();
                    JSONObject jSONObject = (JSONObject) next;
                    hashMap.put("url", jSONObject.getString("url"));
                    hashMap.put("size", jSONObject.getString("size"));
                    arrayList.add(hashMap);
                }
            }
        }
        if (PFImage.getInstance().valid(arrayList, "prefetchImageWithSizeWindvane") != null) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
            return;
        }
        Pair<Boolean, Map<String, Object>> prefetchImageWithSize = PFImage.getInstance().prefetchImageWithSize(arrayList, FeatureFactory.PRIORITY_ABOVE_NORMAL, this.mWebView.getUrl());
        if (((Boolean) prefetchImageWithSize.first).booleanValue()) {
            wVCallBackContext.success();
            return;
        }
        WVResult wVResult = WVResult.RET_FAIL;
        wVResult.addData("errorCode", ((Map) prefetchImageWithSize.second).get("errorCode"));
        wVResult.addData(ILocatable.ERROR_MSG, ((Map) prefetchImageWithSize.second).get(ILocatable.ERROR_MSG));
        wVCallBackContext.error(wVResult);
    }
}
