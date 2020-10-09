package com.alipay.sdk.packet.impl;

import android.content.Context;
import com.ali.user.mobile.rpc.safe.AES;
import com.alipay.sdk.packet.b;
import com.alipay.sdk.packet.e;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.mtop.upload.domain.UploadConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class d extends e {
    public static final String t = "log_v";

    /* access modifiers changed from: protected */
    public String a(String str, JSONObject jSONObject) {
        return str;
    }

    /* access modifiers changed from: protected */
    public JSONObject a() throws JSONException {
        return null;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> a(boolean z, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(e.a, String.valueOf(z));
        hashMap.put("content-type", UploadConstants.FILE_CONTENT_TYPE);
        hashMap.put(e.g, AES.BLOCK_MODE);
        return hashMap;
    }

    /* access modifiers changed from: protected */
    public String c() throws JSONException {
        HashMap hashMap = new HashMap();
        hashMap.put("api_name", "/sdk/log");
        hashMap.put(e.j, "1.0.0");
        HashMap hashMap2 = new HashMap();
        hashMap2.put(t, "1.0");
        return a((HashMap<String, String>) hashMap, (HashMap<String, String>) hashMap2);
    }

    public b a(Context context, String str) throws Throwable {
        return a(context, str, "https://mcgw.alipay.com/sdklog.do", true);
    }
}
