package com.taobao.android.ultron.datamodel.imp;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.datamodel.ISubmitModule;
import com.taobao.android.ultron.datamodel.imp.ParseResponseHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SubmitModule implements ISubmitModule {
    public JSONObject asyncRequestData(DMContext dMContext, IDMComponent iDMComponent) {
        if (dMContext == null) {
            return null;
        }
        try {
            Map<String, DMComponent> renderComponentMap = dMContext.getRenderComponentMap();
            if (renderComponentMap == null) {
                return null;
            }
            HashSet hashSet = new HashSet();
            if (iDMComponent != null) {
                hashSet.add(iDMComponent);
            }
            JSONArray input = dMContext.getInput();
            if (input != null) {
                if (!input.isEmpty()) {
                    Iterator<Object> it = input.iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        if (renderComponentMap.get(str) != null) {
                            hashSet.add(renderComponentMap.get(str));
                        }
                    }
                    return linkProcess(dMContext, hashSet, iDMComponent, true);
                }
            }
            return linkProcess(dMContext, hashSet, iDMComponent, true);
        } catch (Throwable unused) {
            return null;
        }
    }

    public JSONObject submitRequestData(DMContext dMContext) {
        Map<String, DMComponent> renderComponentMap;
        if (dMContext == null || (renderComponentMap = dMContext.getRenderComponentMap()) == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(renderComponentMap.size());
        for (DMComponent next : renderComponentMap.values()) {
            if (next.shouldSubmit()) {
                arrayList.add(next);
            }
        }
        return linkProcess(dMContext, arrayList, (IDMComponent) null, false);
    }

    public JSONObject linkProcess(DMContext dMContext, Collection<?> collection, IDMComponent iDMComponent, boolean z) {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            DMComponent dMComponent = (DMComponent) it.next();
            if (!z) {
                jSONObject = dMComponent.submitData();
            } else {
                jSONObject = dMComponent.adjustData();
            }
            if (jSONObject != null) {
                jSONObject2.put(dMComponent.getKey(), (Object) jSONObject);
            }
        }
        JSONObject jSONObject3 = new JSONObject();
        JSONObject common = dMContext.getCommon();
        if (common != null) {
            JSONObject jSONObject4 = new JSONObject();
            String string = common.getString(ProtocolConst.KEY_VALIDATEPARAMS);
            boolean booleanValue = common.getBooleanValue(ProtocolConst.KEY_COMPRESS);
            if (!z) {
                String string2 = common.getString(ProtocolConst.KEY_SUBMITPARAMS);
                if (!(string2 == null && string == null)) {
                    if (string2 != null && !string2.isEmpty()) {
                        jSONObject4.put(ProtocolConst.KEY_SUBMITPARAMS, (Object) string2);
                    }
                    if (string != null && !string.isEmpty()) {
                        jSONObject4.put(ProtocolConst.KEY_VALIDATEPARAMS, (Object) string);
                    }
                    jSONObject4.put(ProtocolConst.KEY_COMPRESS, (Object) Boolean.valueOf(booleanValue));
                }
                jSONObject3.put("common", (Object) common);
            } else {
                String string3 = common.getString(ProtocolConst.KEY_QUERYPARAMS);
                if (!(string3 == null && string == null)) {
                    if (string3 != null && !string3.isEmpty()) {
                        jSONObject4.put(ProtocolConst.KEY_QUERYPARAMS, (Object) string3);
                    }
                    if (string != null && !string.isEmpty()) {
                        jSONObject4.put(ProtocolConst.KEY_VALIDATEPARAMS, (Object) string);
                    }
                    jSONObject4.put(ProtocolConst.KEY_COMPRESS, (Object) Boolean.valueOf(booleanValue));
                }
                jSONObject3.put("common", (Object) common);
            }
            common = jSONObject4;
            jSONObject3.put("common", (Object) common);
        }
        String string4 = dMContext.getLinkage().getString(ProtocolConst.KEY_SIGNATURE);
        if (string4 != null && !string4.isEmpty()) {
            jSONObject3.put(ProtocolConst.KEY_SIGNATURE, (Object) string4);
        }
        JSONObject jSONObject5 = new JSONObject();
        jSONObject5.put(ProtocolConst.KEY_STRUCTURE, (Object) dMContext.getStructure());
        JSONObject jSONObject6 = new JSONObject();
        jSONObject6.put("data", (Object) jSONObject2);
        jSONObject6.put(ProtocolConst.KEY_LINKAGE, (Object) jSONObject3);
        jSONObject6.put(ProtocolConst.KEY_HIERARCHY, (Object) jSONObject5);
        JSONObject endpoint = dMContext.getEndpoint();
        if (endpoint != null) {
            JSONObject jSONObject7 = endpoint.getJSONObject("meta");
            if (jSONObject7 == null) {
                jSONObject7 = new JSONObject();
            }
            jSONObject7.remove("template");
            List<ParseResponseHelper.TemplateInfo> templateInfo = ParseResponseHelper.getTemplateInfo(dMContext.getContext(), dMContext.getBizName());
            if (templateInfo != null) {
                jSONObject7.put("templates", (Object) JSON.toJSONString(templateInfo));
            }
        }
        jSONObject6.put(ProtocolConst.KEY_ENDPOINT, (Object) endpoint);
        if (iDMComponent != null) {
            jSONObject6.put(ProtocolConst.KEY_OPERATOR, (Object) iDMComponent.getKey());
            boolean z2 = iDMComponent instanceof DMComponent;
            if (z2) {
                DMComponent dMComponent2 = (DMComponent) iDMComponent;
                if (!TextUtils.isEmpty(dMComponent2.getTriggerEvent())) {
                    jSONObject6.put(ProtocolConst.KEY_OPERATOR_EVENT, (Object) dMComponent2.getTriggerEvent());
                }
            }
            if (z2 && iDMComponent.getExtMap().containsKey(ProtocolConst.KEY_SUB_OPERATOR)) {
                jSONObject6.put(ProtocolConst.KEY_SUB_OPERATOR, iDMComponent.getExtMap().get(ProtocolConst.KEY_SUB_OPERATOR));
            }
            jSONObject6.put(ProtocolConst.KEY_OPERATOR_TIME, (Object) Long.valueOf(System.currentTimeMillis()));
        }
        return jSONObject6;
    }
}
