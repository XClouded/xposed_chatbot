package com.alimama.moon.windvane.jsbridge;

import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;

public class JsBridgeUtil {
    public static String parseParams(String str, String str2, WVCallBackContext wVCallBackContext) {
        String str3;
        try {
            str3 = JSON.parseObject(str).getString(str2);
        } catch (Exception unused) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            str3 = "";
        }
        if (TextUtils.isEmpty(str3)) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
        }
        return str3;
    }
}
