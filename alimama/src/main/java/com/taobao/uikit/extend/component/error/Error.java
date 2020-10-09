package com.taobao.uikit.extend.component.error;

import android.text.TextUtils;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.cons.c;
import java.util.HashMap;
import java.util.Map;

public class Error {
    public String apiName;
    public String errorCode;
    public String errorMsg;
    public Map<String, String> extras;
    public String mappingCode;
    public int responseCode;
    public String url;

    private Error(String str, String str2) {
        this(0, "", str, str2);
    }

    private Error(int i, String str, String str2, String str3) {
        this.responseCode = i;
        this.mappingCode = TextUtils.isEmpty(str) ? "" : str;
        this.errorCode = TextUtils.isEmpty(str2) ? "" : str2;
        this.errorMsg = TextUtils.isEmpty(str3) ? "" : str3;
    }

    private Error(String str, int i, String str2, String str3, String str4) {
        this(i, str2, str3, str4);
        this.apiName = str;
    }

    public Map<String, Object> toMap() {
        HashMap hashMap = new HashMap();
        if (this.extras != null) {
            for (String next : this.extras.keySet()) {
                hashMap.put(next, this.extras.get(next));
            }
        }
        hashMap.put("responseCode", Integer.valueOf(this.responseCode));
        hashMap.put("mappingCode", this.mappingCode == null ? "" : this.mappingCode);
        hashMap.put("errorCode", this.errorCode == null ? "" : this.errorCode);
        hashMap.put(ILocatable.ERROR_MSG, this.errorMsg == null ? "" : this.errorMsg);
        hashMap.put("url", this.url == null ? "" : this.url);
        hashMap.put(c.n, this.apiName == null ? "" : this.apiName);
        return hashMap;
    }

    public String toJSON() {
        JSONObject jSONObject = new JSONObject();
        if (this.extras != null) {
            for (String next : this.extras.keySet()) {
                jSONObject.put(next, (Object) this.extras.get(next));
            }
        }
        jSONObject.put("responseCode", (Object) Integer.valueOf(this.responseCode));
        jSONObject.put("mappingCode", (Object) this.mappingCode == null ? "" : this.mappingCode);
        jSONObject.put("errorCode", (Object) this.errorCode == null ? "" : this.errorCode);
        jSONObject.put(ILocatable.ERROR_MSG, (Object) this.errorMsg == null ? "" : this.errorMsg);
        jSONObject.put("url", (Object) this.url == null ? "" : this.url);
        jSONObject.put(c.n, (Object) this.apiName == null ? "" : this.apiName);
        return jSONObject.toJSONString();
    }

    public static class Factory {
        public static Error newError(String str, String str2) {
            return new Error(str, str2);
        }

        public static Error fromMtopResponse(int i, String str, String str2, String str3) {
            return new Error(i, str, str2, str3);
        }

        public static Error fromMtopResponse(String str, int i, String str2, String str3, String str4) {
            return new Error(str, i, str2, str3, str4);
        }
    }
}
