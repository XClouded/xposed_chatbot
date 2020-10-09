package com.alibaba.aliweex.adapter;

import com.alibaba.fastjson.JSONObject;

public interface ICallback {
    void failure(JSONObject jSONObject);

    void success(JSONObject jSONObject);
}
