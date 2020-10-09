package com.taobao.android.ultron.common.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.weex.el.parse.Operators;

public class DynamicTemplate {
    public String containerType;
    public String md5;
    public String name;
    public JSONArray type;
    public String url;
    public String version;

    public DynamicTemplate() {
    }

    public DynamicTemplate(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.type = jSONObject.getJSONArray("type");
            this.containerType = jSONObject.getString(ProtocolConst.KEY_CONTAINER_TYPE);
            this.name = jSONObject.getString("name");
            this.url = jSONObject.getString("url");
            this.version = jSONObject.getString("version");
            this.md5 = jSONObject.getString("md5");
        }
    }

    public String toString() {
        if (("DynamicTemplate [type=" + this.type) != null) {
            return this.type.toJSONString();
        }
        return "null, containerType=" + this.containerType + ", name=" + this.name + ", url=" + this.url + ", version=" + this.version + Operators.ARRAY_END_STR;
    }
}
