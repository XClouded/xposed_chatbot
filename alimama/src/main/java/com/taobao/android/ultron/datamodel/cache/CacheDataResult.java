package com.taobao.android.ultron.datamodel.cache;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.DynamicTemplate;
import com.taobao.android.ultron.datamodel.imp.DMComponent;
import java.util.List;
import java.util.Map;

public class CacheDataResult {
    Map<String, DMComponent> mComponentMap;
    Map<String, JSONObject> mTag2containerInfoMap;
    List<DynamicTemplate> mTemplateList;

    public Map<String, JSONObject> getContainer() {
        return this.mTag2containerInfoMap;
    }

    public void setContainerMap(Map<String, JSONObject> map) {
        this.mTag2containerInfoMap = map;
    }

    public Map<String, DMComponent> getComponentMap() {
        return this.mComponentMap;
    }

    public void setComponentMap(Map<String, DMComponent> map) {
        this.mComponentMap = map;
    }

    public List<DynamicTemplate> getTemplateList() {
        return this.mTemplateList;
    }

    public void setTemplateList(List<DynamicTemplate> list) {
        this.mTemplateList = list;
    }
}
