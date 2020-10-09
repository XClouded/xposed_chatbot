package com.taobao.vessel.web;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.alibaba.fastjson.JSON;
import com.taobao.vessel.base.ResultCallback;
import com.taobao.vessel.base.VesselCallbackManager;
import java.util.HashMap;
import java.util.Map;

public class VesselWebApiPlugin extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"vessel".equals(str)) {
            return false;
        }
        Map hashMap = new HashMap();
        try {
            hashMap = JSON.parseObject(str2);
        } catch (Exception e) {
            hashMap.put("err", e);
        }
        VesselCallbackManager.getInstance().notifyCallback(wVCallBackContext.getWebview(), hashMap, new ResultCallback(wVCallBackContext));
        return true;
    }
}
