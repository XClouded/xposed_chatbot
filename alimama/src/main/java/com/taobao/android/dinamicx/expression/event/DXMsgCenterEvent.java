package com.taobao.android.dinamicx.expression.event;

import com.alibaba.fastjson.JSONObject;

public class DXMsgCenterEvent extends DXEvent {
    private JSONObject params;
    private String targetId;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String str) {
        this.targetId = str;
    }

    public DXMsgCenterEvent(long j) {
        super(j);
    }

    public void setParams(JSONObject jSONObject) {
        this.params = jSONObject;
    }

    public JSONObject getParams() {
        return this.params;
    }
}
