package com.taobao.android.ultron.datamodel.imp;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.IDMComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExtendBlock implements Cloneable, Serializable {
    Map<String, List<IDMComponent>> blockComponents = new LinkedHashMap();
    Map<String, JSONObject> blockHierarchy = new HashMap();
    private IDMComponent mExtendBlock;

    public ExtendBlock(IDMComponent iDMComponent) {
        this.mExtendBlock = iDMComponent;
    }

    public void addBlock(String str, List<IDMComponent> list) {
        this.blockComponents.put(str, list);
    }

    public void addHierarchy(String str, DMContext dMContext) {
        if (dMContext != null && !TextUtils.isEmpty(str)) {
            JSONObject jSONObject = this.blockHierarchy.get(str);
            if (jSONObject == null) {
                jSONObject = new JSONObject();
            }
            JSONObject structure = dMContext.getStructure();
            if (structure.containsKey(str)) {
                JSONArray jSONArray = structure.getJSONArray(str);
                jSONObject.put(str, (Object) jSONArray);
                for (int i = 0; i < jSONArray.size(); i++) {
                    Object obj = jSONArray.get(i);
                    if (obj instanceof String) {
                        handleSubKeyArray((String) obj, structure, jSONObject);
                    }
                }
                this.blockHierarchy.put(str, jSONObject);
                return;
            }
            this.blockHierarchy.remove(str);
        }
    }

    private void handleSubKeyArray(String str, JSONObject jSONObject, JSONObject jSONObject2) {
        if (str != null && jSONObject.containsKey(str)) {
            JSONArray jSONArray = jSONObject.getJSONArray(str);
            jSONObject2.put(str, (Object) jSONArray);
            for (int i = 0; i < jSONArray.size(); i++) {
                Object obj = jSONArray.get(i);
                if (obj instanceof String) {
                    handleSubKeyArray((String) obj, jSONObject, jSONObject2);
                }
            }
        }
    }

    public List<IDMComponent> getblockComponentList() {
        List list;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry next : this.blockComponents.entrySet()) {
            if (!(next == null || (list = (List) next.getValue()) == null || list.isEmpty())) {
                arrayList.addAll(list);
            }
        }
        return arrayList;
    }

    public JSONObject getBlockHierarchy(String str) {
        if (this.blockHierarchy.containsKey(str)) {
            return this.blockHierarchy.get(str);
        }
        return null;
    }

    public IDMComponent getExtendBlock() {
        return this.mExtendBlock;
    }

    public void updateExtendBlock(IDMComponent iDMComponent) {
        this.mExtendBlock = iDMComponent;
    }
}
