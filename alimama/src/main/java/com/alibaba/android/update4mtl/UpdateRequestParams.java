package com.alibaba.android.update4mtl;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequestParams {
    public static final String PARAM_BRAND = "param_brand";
    public static final String PARAM_CITY = "param_city";
    public static final String PARAM_IS_MANUAL = "param_is_manual";
    public static final String PARAM_LOCALE = "param_locale";
    public static final String PARAM_MODEL = "param_model";
    public static final String PARAM_USER_ID = "param_user_id";
    private Map<String, String> mParams = new HashMap();

    public void put(String str, String str2) {
        this.mParams.put(str, str2);
    }

    public String get(String str) {
        return this.mParams.get(str);
    }

    public Map<String, String> getParams() {
        return this.mParams;
    }

    public String toString() {
        return super.toString();
    }
}
