package com.taobao.android.dinamicx.bindingx;

import com.alibaba.android.bindingx.plugin.android.model.BindingXSpec;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class DXBindingXSpec extends BindingXSpec {
    public String eventType;
    public JSONObject exitExpression;
    public String name;
    public JSONArray propsJsonArray;
    public boolean repeat;
    public boolean resetOnFinish = true;
    public boolean resetOnStop = true;
    public JSONObject specOriginMap;
    public Map<String, Object> token;
    public boolean updateFlattenOnlyOnFinish = false;
    public boolean updateFlattenOnlyOnStop = false;
}
