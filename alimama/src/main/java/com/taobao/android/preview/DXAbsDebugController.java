package com.taobao.android.preview;

import com.alibaba.fastjson.JSONObject;

public abstract class DXAbsDebugController {
    public void onClose(int i, String str) {
    }

    public void onFailure(Throwable th) {
    }

    public void onOpen(JSONObject jSONObject) {
    }

    public void onReceiver(String str) {
    }

    public abstract void sendProtocolMessage(int i, String str);
}
