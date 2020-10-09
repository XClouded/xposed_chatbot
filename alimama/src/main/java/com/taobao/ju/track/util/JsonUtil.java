package com.taobao.ju.track.util;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static Map<String, String> jsonToMap(String str) {
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            JSONObject parseObject = JSON.parseObject(str.replace(DXBindingXConstant.SINGLE_QUOTE, "\"").replace(";", ","));
            if (parseObject.keySet().size() > 0) {
                for (String valueOf : parseObject.keySet()) {
                    String valueOf2 = String.valueOf(valueOf);
                    hashMap.put(valueOf2, (String) parseObject.get(valueOf2));
                }
            }
        }
        return hashMap;
    }
}
