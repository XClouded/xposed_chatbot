package com.alibaba.android.anynetwork.core;

import android.text.TextUtils;
import com.alibaba.android.anynetwork.core.common.ANConstants;
import com.alibaba.android.anynetwork.core.utils.Utils;
import java.util.HashMap;
import java.util.Map;

public class ANResponse {
    public static final String BASE_RESPONSE_CODE = "base_response_code";
    public static final String BASE_RESPONSE_MSG = "base_response_msg";
    public static final String BASE_TYPE = "base_type";
    public static final String NETWORK_HEADER = "network_header";
    public static final String NETWORK_IS_SUCCESS = "network_is_success";
    public static final String NETWORK_RESPONSE_BYTE_BODY = "network_response_byte_body";
    public static final String NETWORK_RESPONSE_CODE = "network_response_code";
    public static final String NETWORK_RESPONSE_MESSAGE = "network_response_message";
    public static final String NETWORK_RESPONSE_STRING_BODY = "network_reponse_string_body";
    private static HashMap<String, Class> mTypeCheckMap = new HashMap<>();
    private HashMap<String, Object> mPropertyMap = new HashMap<>();

    static {
        if (ANConstants.DEBUG) {
            mTypeCheckMap.put("base_type", String.class);
        }
    }

    public static void setTypeCheckEntry(String str, Class cls) {
        if (ANConstants.DEBUG && !TextUtils.isEmpty(str) && cls != null) {
            mTypeCheckMap.put(str, cls);
        }
    }

    public static ANResponse generateFailResponse(String str, int i, String str2) {
        return new ANResponse().setBaseType(str).setBaseResponseCode(i).setBaseResponseMsg(str2);
    }

    public ANResponse setProperty(String str, Object obj) {
        Class<?> cls;
        if (str == null) {
            throw new IllegalArgumentException("Property key can't be null");
        } else if (obj == null) {
            this.mPropertyMap.put(str, obj);
            return this;
        } else if (!ANConstants.DEBUG || (cls = mTypeCheckMap.get(str)) == null || obj.getClass() == cls) {
            this.mPropertyMap.put(str, obj);
            return this;
        } else {
            throw new IllegalArgumentException("Excepted " + cls + " for " + str + " but is " + obj.getClass());
        }
    }

    public Object getProperty(String str) {
        return this.mPropertyMap.get(str);
    }

    public String toString() {
        return "ANResponse{" + this.mPropertyMap.toString() + "}";
    }

    public ANResponse setBaseType(String str) {
        setProperty("base_type", str);
        return this;
    }

    public String getBaseType() {
        return (String) getProperty("base_type");
    }

    public ANResponse setBaseResponseCode(int i) {
        setProperty(BASE_RESPONSE_CODE, Integer.valueOf(i));
        return this;
    }

    public int getBaseResponseCode() {
        return Utils.getInt(getProperty(BASE_RESPONSE_CODE), 0);
    }

    public ANResponse setBaseResponseMsg(String str) {
        setProperty(BASE_RESPONSE_MSG, str);
        return this;
    }

    public String getBaseResponseMsg() {
        return (String) getProperty(BASE_RESPONSE_MSG);
    }

    public ANResponse setNetworkHeader(Map<String, String> map) {
        setProperty(NETWORK_HEADER, map);
        return this;
    }

    public Map<String, String> getNetworkHeader() {
        return (Map) getProperty(NETWORK_HEADER);
    }

    public ANResponse setNetworkIsSuccess(boolean z) {
        setProperty(NETWORK_IS_SUCCESS, Boolean.valueOf(z));
        return this;
    }

    public boolean getNetworkIsSuccess() {
        return Utils.getBoolean(getProperty(NETWORK_IS_SUCCESS), false);
    }

    public ANResponse setNetworkResponseCode(String str) {
        setProperty(NETWORK_RESPONSE_CODE, str);
        return this;
    }

    public String getNetworkResponseCode() {
        return (String) getProperty(NETWORK_RESPONSE_CODE);
    }

    public ANResponse setNetworkResponseMessage(String str) {
        setProperty(NETWORK_RESPONSE_MESSAGE, str);
        return this;
    }

    public String getNetworkResponseMessage() {
        return (String) getProperty(NETWORK_RESPONSE_MESSAGE);
    }

    public ANResponse setNetworkResponseStringBody(String str) {
        setProperty(NETWORK_RESPONSE_STRING_BODY, str);
        return this;
    }

    public String getNetworkResponseStringBody() {
        return (String) getProperty(NETWORK_RESPONSE_STRING_BODY);
    }

    public ANResponse setNetworkResponseByteBody(byte[] bArr) {
        setProperty(NETWORK_RESPONSE_BYTE_BODY, bArr);
        return this;
    }

    public byte[] getNetworkResponseByteBody() {
        return (byte[]) getProperty(NETWORK_RESPONSE_BYTE_BODY);
    }
}
