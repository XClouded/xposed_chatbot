package com.taobao.android.ultron.datamodel.imp;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.common.model.IDMEvent;
import java.io.Serializable;
import java.util.List;

public class DMEvent implements IDMEvent, Cloneable, Serializable {
    private List<IDMComponent> mComponents;
    private JSONObject mFields;
    private JSONObject mStashFields;
    private String mType;

    public DMEvent(String str, JSONObject jSONObject, List<IDMComponent> list) {
        this.mType = str;
        this.mFields = jSONObject;
        this.mComponents = list;
    }

    public String getType() {
        return this.mType;
    }

    public JSONObject getFields() {
        return this.mFields;
    }

    public List<IDMComponent> getComponents() {
        return this.mComponents;
    }

    public void writeFields(String str, Object obj) {
        if (this.mFields != null && !TextUtils.isEmpty(str) && obj != null) {
            this.mFields.put(str, obj);
        }
    }

    public void rollBack() {
        if (this.mStashFields != null) {
            this.mFields = this.mStashFields;
            this.mStashFields = null;
        }
    }

    public void record() {
        if (this.mFields != null) {
            this.mStashFields = JSON.parseObject(this.mFields.toJSONString());
        }
    }
}
