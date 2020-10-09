package com.taobao.android.ultron.datamodel.imp;

import android.text.TextUtils;
import com.alibaba.android.umbrella.trace.UmbrellaTracker;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.DynamicTemplate;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.common.model.IDMEvent;
import com.taobao.android.ultron.common.model.LinkageType;
import com.taobao.android.ultron.common.utils.UnifyLog;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParseModule {
    private static String CARD_GROUP_TAG = "CardGroupTag";
    private static String POSITION_TAG = "PositionTag";
    private static final String TAG = "ParseModule";
    private IDMComponent mRootComponent;
    boolean useTagIdFeature = false;

    /* access modifiers changed from: package-private */
    public boolean parseComponents(DMContext dMContext, JSONObject jSONObject) {
        List<IDMComponent> list;
        if (dMContext.getProtocolFeatures() != null) {
            try {
                this.useTagIdFeature = ProtocolFeatures.hasFeature(new BigInteger(dMContext.getProtocolFeatures()), ProtocolFeatures.FEATURE_TAG_ID);
            } catch (Exception e) {
                UnifyLog.e(TAG, e.getMessage());
            }
        }
        if (jSONObject == null) {
            return false;
        }
        try {
            if (jSONObject.getBooleanValue("reload")) {
                UnifyLog.e(TAG, "parseComponents", "parseFullResponseJson");
                list = parseFullResponseJson(dMContext, jSONObject);
            } else {
                UnifyLog.e(TAG, "parseComponents", "parseAdjustResponseJson");
                list = parseAdjustResponseJson(dMContext, jSONObject);
            }
            if (list == null) {
                return false;
            }
            dMContext.setComponentList(list);
            UnifyLog.e(TAG, "parseComponents", "parse success");
            return true;
        } catch (Throwable unused) {
            UnifyLog.e(TAG, "parseComponents", "parse failed");
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public IDMComponent getRootComponent() {
        return this.mRootComponent;
    }

    /* access modifiers changed from: package-private */
    public List<IDMComponent> getComponentsByRoot(DMContext dMContext, String str) {
        try {
            List<IDMComponent> resolve = resolve(dMContext, str, (DMComponent) null);
            if (resolve == null || resolve.isEmpty()) {
                UnifyLog.e(TAG, "getComponentsByRoot", "output is empty，rootComponentKey:", str);
            } else {
                setCornerType(resolve);
                dealLinkageType(dMContext);
            }
            return resolve;
        } catch (Throwable unused) {
            return null;
        }
    }

    private void setCornerType(List<IDMComponent> list) {
        int i;
        String str;
        List<IDMComponent> normolComponentList = getNormolComponentList(list);
        int i2 = 0;
        IDMComponent iDMComponent = null;
        String str2 = "";
        int i3 = 1;
        while (true) {
            i = 16;
            if (i2 >= normolComponentList.size() - 1) {
                break;
            }
            IDMComponent iDMComponent2 = normolComponentList.get(i2);
            if (!(iDMComponent2.getExtMap() == null || (str = (String) iDMComponent2.getExtMap().get(CARD_GROUP_TAG)) == null)) {
                IDMComponent iDMComponent3 = normolComponentList.get(i2 + 1);
                String str3 = (String) iDMComponent3.getExtMap().get(CARD_GROUP_TAG);
                if (str.equals(str3)) {
                    if (1 == i3) {
                        setCornerTypeFields(iDMComponent2, 1);
                    }
                    i3++;
                } else {
                    if (1 == i3) {
                        i = 17;
                    } else {
                        i3 = 1;
                    }
                    setCornerTypeFields(iDMComponent2, i);
                }
                String str4 = str3;
                iDMComponent = iDMComponent3;
                str2 = str4;
            }
            i2++;
        }
        if (1 == i3) {
            i = 17;
        }
        if (!TextUtils.isEmpty(str2)) {
            setCornerTypeFields(iDMComponent, i);
        }
    }

    private List<IDMComponent> getNormolComponentList(List<IDMComponent> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (IDMComponent next : list) {
            if (isNormalComponent(next)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    private boolean isNormalComponent(IDMComponent iDMComponent) {
        return (iDMComponent == null || iDMComponent.getStatus() == 0 || iDMComponent.getFields() == null || iDMComponent.getFields().isEmpty()) ? false : true;
    }

    private void setCornerTypeFields(IDMComponent iDMComponent, int i) {
        if (iDMComponent != null) {
            if (i != 1) {
                switch (i) {
                    case 16:
                        iDMComponent.getFields().put(ProtocolConst.KEY_CORNER_TYPE, (Object) "bottom");
                        return;
                    case 17:
                        iDMComponent.getFields().put(ProtocolConst.KEY_CORNER_TYPE, (Object) ProtocolConst.VAL_CORNER_TYPE_BOTH);
                        return;
                    default:
                        return;
                }
            } else {
                iDMComponent.getFields().put(ProtocolConst.KEY_CORNER_TYPE, (Object) "top");
            }
        }
    }

    private List<IDMComponent> parseFullResponseJson(DMContext dMContext, JSONObject jSONObject) {
        JSONObject jSONObject2;
        if (jSONObject == null || dMContext == null) {
            return null;
        }
        JSONObject jSONObject3 = jSONObject.getJSONObject("data");
        JSONObject jSONObject4 = jSONObject.getJSONObject(ProtocolConst.KEY_HIERARCHY);
        JSONObject jSONObject5 = jSONObject.getJSONObject(ProtocolConst.KEY_LINKAGE);
        JSONObject jSONObject6 = jSONObject.getJSONObject(ProtocolConst.KEY_GLOBAL);
        JSONObject jSONObject7 = jSONObject.getJSONObject(ProtocolConst.KEY_ENDPOINT);
        if (jSONObject4 != null) {
            jSONObject2 = jSONObject4.getJSONObject(ProtocolConst.KEY_STRUCTURE);
        } else {
            jSONObject2 = null;
        }
        if (jSONObject3 == null || jSONObject4 == null || jSONObject5 == null || jSONObject2 == null) {
            String str = jSONObject3 == null ? "data" : jSONObject4 == null ? ProtocolConst.KEY_HIERARCHY : "default";
            UmbrellaTracker.commitFailureStability("protocolError", str, "1.0", dMContext.getBizName(), (String) null, (Map<String, String>) null, "errorcode", str + " is empty");
            return null;
        }
        if (dMContext.getData() == null) {
            dMContext.reset();
            dMContext.setData(jSONObject3);
            dMContext.setStructure(jSONObject2);
        } else {
            dMContext.getRenderComponentMap().clear();
            dMContext.mergeData(jSONObject3);
            jSONObject4.put(ProtocolConst.KEY_STRUCTURE, (Object) dMContext.mergeStructure(jSONObject2));
        }
        dMContext.setHierarchy(jSONObject4);
        dMContext.setGlobal(jSONObject6);
        dMContext.setLinkage(jSONObject5);
        dMContext.setEndpoint(jSONObject7);
        if (jSONObject7 != null) {
            dMContext.setProtocolVersion(jSONObject7.getString(ProtocolConst.KEY_PROTOCOL_VERSION));
        }
        parseContainer(dMContext, jSONObject);
        dMContext.setCommon(jSONObject5.getJSONObject("common"));
        String rootComponentKey = dMContext.getRootComponentKey();
        if (TextUtils.isEmpty(rootComponentKey)) {
            rootComponentKey = jSONObject4.getString(ProtocolConst.KEY_ROOT);
        }
        if (TextUtils.isEmpty(rootComponentKey)) {
            return null;
        }
        List<IDMComponent> componentsByRoot = getComponentsByRoot(dMContext, rootComponentKey);
        this.mRootComponent = dMContext.getRenderComponentMap().get(rootComponentKey);
        return componentsByRoot;
    }

    private void parseContainer(DMContext dMContext, JSONObject jSONObject) {
        JSONArray jSONArray;
        if (jSONObject != null && dMContext != null) {
            if ((!dMContext.isUseCache("container") || dMContext.getType2containerInfoMap().isEmpty()) && (jSONArray = jSONObject.getJSONObject("container").getJSONArray("data")) != null) {
                ArrayList arrayList = new ArrayList();
                int size = jSONArray.size();
                StringBuilder sb = new StringBuilder("\n");
                for (int i = 0; i < size; i++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    if (jSONObject2 != null) {
                        DynamicTemplate dynamicTemplate = new DynamicTemplate(jSONObject2);
                        arrayList.add(dynamicTemplate);
                        JSONArray jSONArray2 = jSONObject2.getJSONArray("type");
                        if (jSONArray2 != null) {
                            int size2 = jSONArray2.size();
                            for (int i2 = 0; i2 < size2; i2++) {
                                String string = jSONArray2.getString(i2);
                                sb.append("componentType: ");
                                sb.append(string);
                                sb.append(", containerName: ");
                                sb.append(dynamicTemplate.name);
                                sb.append(", containerUrl: ");
                                sb.append(dynamicTemplate.url);
                                sb.append("\n");
                                dMContext.getType2containerInfoMap().put(string, jSONObject2);
                            }
                        }
                    }
                }
                UnifyLog.e(TAG, "parseContainer", sb.toString());
                dMContext.setTemplateList(arrayList);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        r1 = r1.getType2containerInfoMap().get(r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getContainerTypeByComponentType(com.taobao.android.ultron.datamodel.imp.DMContext r1, java.lang.String r2) {
        /*
            r0 = this;
            if (r2 != 0) goto L_0x0005
            java.lang.String r1 = "native"
            return r1
        L_0x0005:
            java.util.concurrent.ConcurrentHashMap r1 = r1.getType2containerInfoMap()
            java.lang.Object r1 = r1.get(r2)
            com.alibaba.fastjson.JSONObject r1 = (com.alibaba.fastjson.JSONObject) r1
            if (r1 == 0) goto L_0x0018
            java.lang.String r2 = "containerType"
            java.lang.String r1 = r1.getString(r2)
            return r1
        L_0x0018:
            java.lang.String r1 = "native"
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.imp.ParseModule.getContainerTypeByComponentType(com.taobao.android.ultron.datamodel.imp.DMContext, java.lang.String):java.lang.String");
    }

    private JSONObject getContainerInfo(DMContext dMContext, String str) {
        if (str == null) {
            return null;
        }
        return dMContext.getType2containerInfoMap().get(str);
    }

    private void dealLinkageType(DMContext dMContext) {
        Map<String, DMComponent> componentMap = dMContext.getComponentMap();
        JSONArray request = dMContext.getRequest();
        if (request == null) {
            request = new JSONArray();
            dMContext.getLinkage().put("request", (Object) request);
        }
        Iterator<Object> it = request.iterator();
        while (it.hasNext()) {
            DMComponent dMComponent = componentMap.get((String) it.next());
            if (dMComponent != null) {
                dMComponent.setLinkageType(LinkageType.REQUEST);
            }
        }
    }

    private void updateContextData(DMContext dMContext, JSONObject jSONObject) {
        JSONObject data;
        if (dMContext != null && jSONObject != null && (data = dMContext.getData()) != null) {
            data.putAll(jSONObject);
        }
    }

    private List<IDMComponent> parseAdjustResponseJson(DMContext dMContext, JSONObject jSONObject) {
        String[] componentInfo;
        JSONObject jSONObject2;
        List<IDMComponent> components = dMContext.getComponents();
        Map<String, DMComponent> renderComponentMap = dMContext.getRenderComponentMap();
        if (jSONObject == null || renderComponentMap == null || renderComponentMap.size() == 0) {
            return null;
        }
        JSONObject jSONObject3 = jSONObject.getJSONObject("data");
        if (jSONObject3 == null) {
            return components;
        }
        updateContextData(dMContext, jSONObject3);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : jSONObject3.entrySet()) {
            String str = (String) next.getKey();
            JSONObject jSONObject4 = (JSONObject) next.getValue();
            DMComponent dMComponent = renderComponentMap.get(str);
            if (dMComponent != null) {
                try {
                    String string = dMComponent.getFields().getString(ProtocolConst.KEY_CORNER_TYPE);
                    if (!(TextUtils.isEmpty(string) || jSONObject4 == null || (jSONObject2 = jSONObject4.getJSONObject(ProtocolConst.KEY_FIELDS)) == null)) {
                        jSONObject2.put(ProtocolConst.KEY_CORNER_TYPE, (Object) string);
                    }
                    if (str != null && this.useTagIdFeature && (componentInfo = getComponentInfo(str)) != null && componentInfo.length == 2) {
                        jSONObject4.put("tag", (Object) componentInfo[0]);
                        jSONObject4.put("id", (Object) componentInfo[1]);
                    }
                    dMComponent.onReload(dMContext, jSONObject4);
                    dMComponent.onReloadEvent(parseEventMap(dMContext, jSONObject4.getJSONObject(ProtocolConst.KEY_EVENTS)));
                    sb.append("组件reload:");
                    sb.append(str);
                    sb.append("\n");
                } catch (Throwable unused) {
                }
            }
        }
        UnifyLog.e(TAG, "parseAdjustResponseJson", sb.toString());
        JSONObject jSONObject5 = jSONObject.getJSONObject(ProtocolConst.KEY_LINKAGE);
        if (jSONObject5 != null) {
            for (Map.Entry next2 : jSONObject5.entrySet()) {
                if ("common".equals((String) next2.getKey())) {
                    JSONObject jSONObject6 = (JSONObject) next2.getValue();
                    if (jSONObject6 != null) {
                        JSONObject common = dMContext.getCommon();
                        if (common != null) {
                            for (Map.Entry next3 : jSONObject6.entrySet()) {
                                common.put((String) next3.getKey(), next3.getValue());
                            }
                        } else {
                            dMContext.setCommon(jSONObject6);
                        }
                    }
                } else {
                    dMContext.getLinkage().put((String) next2.getKey(), next2.getValue());
                }
            }
        }
        dealLinkageType(dMContext);
        return components;
    }

    private DMComponent createDMComponent(DMContext dMContext, JSONObject jSONObject, String str) {
        String[] componentInfo;
        if (jSONObject == null) {
            return null;
        }
        if (str != null && this.useTagIdFeature && (componentInfo = getComponentInfo(str)) != null && componentInfo.length == 2) {
            jSONObject.put("tag", (Object) componentInfo[0]);
            jSONObject.put("id", (Object) componentInfo[1]);
        }
        String string = jSONObject.getString("type");
        String string2 = jSONObject.getString("tag");
        String containerTypeByComponentType = getContainerTypeByComponentType(dMContext, string);
        JSONObject containerInfo = getContainerInfo(dMContext, string);
        if (containerInfo != null) {
            UnifyLog.e(TAG, "createDMComponent", "type", string, "tag", string2);
        }
        DMComponent dMComponent = new DMComponent(jSONObject, containerTypeByComponentType, containerInfo, parseEventMap(dMContext, jSONObject.getJSONObject(ProtocolConst.KEY_EVENTS)));
        dMComponent.setComponentKey(str);
        return dMComponent;
    }

    public static String[] getComponentInfo(String str) {
        int indexOf;
        if (TextUtils.isEmpty(str) || (indexOf = str.indexOf("_")) <= 0 || indexOf >= str.length() - 1) {
            return null;
        }
        return new String[]{str.substring(0, indexOf), str.substring(indexOf + 1, str.length())};
    }

    private Map<String, List<IDMEvent>> parseEventMap(DMContext dMContext, JSONObject jSONObject) {
        IDMEvent parseEvent;
        if (jSONObject == null || jSONObject.isEmpty()) {
            return null;
        }
        HashMap hashMap = new HashMap(jSONObject.size());
        for (Map.Entry next : jSONObject.entrySet()) {
            String str = (String) next.getKey();
            Object value = next.getValue();
            if (!TextUtils.isEmpty(str) && (value instanceof JSONArray)) {
                JSONArray jSONArray = (JSONArray) value;
                ArrayList arrayList = new ArrayList(jSONArray.size());
                Iterator<Object> it = jSONArray.iterator();
                while (it.hasNext()) {
                    Object next2 = it.next();
                    if ((next2 instanceof JSONObject) && (parseEvent = parseEvent(dMContext, (JSONObject) next2)) != null) {
                        arrayList.add(parseEvent);
                    }
                }
                hashMap.put(str, arrayList);
            }
        }
        return hashMap;
    }

    private IDMEvent parseEvent(DMContext dMContext, JSONObject jSONObject) {
        String str;
        List<IDMComponent> list = null;
        if (jSONObject == null || jSONObject.isEmpty()) {
            return null;
        }
        String string = jSONObject.getString("type");
        JSONObject jSONObject2 = jSONObject.getJSONObject(ProtocolConst.KEY_FIELDS);
        if (jSONObject2 == null) {
            str = "";
        } else {
            str = jSONObject2.getString(ProtocolConst.KEY_NEXT_RENDER_ROOT);
        }
        if (!TextUtils.isEmpty(str)) {
            list = getComponentsByRoot(dMContext, str);
        }
        return new DMEvent(string, jSONObject2, list);
    }

    private List<IDMComponent> resolve(DMContext dMContext, String str, DMComponent dMComponent) {
        DMComponent dMComponent2;
        DMComponent dMComponent3;
        DMContext dMContext2 = dMContext;
        String str2 = str;
        DMComponent dMComponent4 = dMComponent;
        ExtendBlock extendBlock = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        JSONObject data = dMContext.getData();
        JSONObject structure = dMContext.getStructure();
        JSONObject jSONObject = data != null ? data.getJSONObject(str2) : null;
        Map<String, DMComponent> componentMap = dMContext.getComponentMap();
        Map<String, DMComponent> renderComponentMap = dMContext.getRenderComponentMap();
        if (jSONObject == null || dMContext2.isUseCache("data")) {
            dMComponent2 = componentMap.get(str2);
        } else {
            dMComponent2 = null;
        }
        if (dMComponent2 == null) {
            try {
                dMComponent3 = createDMComponent(dMContext2, jSONObject, str2);
            } catch (Throwable th) {
                UnifyLog.e(TAG, "createDMComponent error", th.getMessage());
            }
        } else {
            dMComponent2.getChildren().clear();
            dMComponent3 = dMComponent2;
        }
        if (dMComponent3 != null) {
            dMComponent3.setParent(dMComponent4);
            componentMap.put(str2, dMComponent3);
            renderComponentMap.put(str2, dMComponent3);
        }
        String cardGroupTag = getCardGroupTag(dMComponent3, dMComponent4);
        String position = getPosition(dMComponent3, dMComponent4);
        if (dMComponent3 != null && dMComponent3.isExtendBlock()) {
            extendBlock = dMContext.getExtendBlockComponentMap().get(str2);
            if (extendBlock == null) {
                extendBlock = new ExtendBlock(dMComponent3);
                dMContext.getExtendBlockComponentMap().put(str2, extendBlock);
            } else {
                extendBlock.updateExtendBlock(dMComponent3);
            }
        }
        JSONArray jSONArray = structure.getJSONArray(str2);
        if (jSONArray != null) {
            Iterator<Object> it = jSONArray.iterator();
            while (it.hasNext()) {
                String str3 = (String) it.next();
                setComponentCardGroupTag(dMComponent3, cardGroupTag);
                setComponentPosition(dMComponent3, position);
                List<IDMComponent> resolve = resolve(dMContext2, str3, dMComponent3);
                if (extendBlock != null) {
                    extendBlock.addBlock(str3, resolve);
                    extendBlock.addHierarchy(str3, dMContext2);
                } else if (resolve != null) {
                    arrayList.addAll(resolve);
                    if (componentMap != null) {
                        IDMComponent iDMComponent = componentMap.get(str3);
                        if (!(dMComponent3 == null || iDMComponent == null)) {
                            dMComponent3.addChild(iDMComponent);
                        }
                    }
                }
            }
            if (extendBlock != null) {
                arrayList.addAll(extendBlock.getblockComponentList());
            }
        } else if (dMComponent3 == null || dMComponent3.getFields() == null) {
            UnifyLog.e(TAG, "resolve", "currentComponent fields error: ", str2);
        } else {
            arrayList.add(dMComponent3);
            setComponentCardGroupTag(dMComponent3, cardGroupTag);
            setComponentPosition(dMComponent3, position);
        }
        return arrayList;
    }

    private String getCardGroupTag(IDMComponent iDMComponent, IDMComponent iDMComponent2) {
        String key = (iDMComponent == null || iDMComponent.getFields() == null || !"true".equals(iDMComponent.getCardGroup())) ? null : iDMComponent.getKey();
        return (iDMComponent2 == null || iDMComponent2.getExtMap() == null || iDMComponent2.getExtMap().get(CARD_GROUP_TAG) == null || TextUtils.isEmpty(String.valueOf(iDMComponent2.getExtMap().get(CARD_GROUP_TAG)))) ? key : (String) iDMComponent2.getExtMap().get(CARD_GROUP_TAG);
    }

    private String getPosition(IDMComponent iDMComponent, IDMComponent iDMComponent2) {
        String position = (iDMComponent == null || iDMComponent.getFields() == null || iDMComponent.getPosition() == null) ? null : iDMComponent.getPosition();
        return (iDMComponent2 == null || iDMComponent2.getExtMap() == null || iDMComponent2.getExtMap().get(POSITION_TAG) == null || TextUtils.isEmpty(String.valueOf(iDMComponent2.getExtMap().get(POSITION_TAG)))) ? position : (String) iDMComponent2.getExtMap().get(POSITION_TAG);
    }

    private void setComponentCardGroupTag(IDMComponent iDMComponent, String str) {
        if (!TextUtils.isEmpty(str) && iDMComponent.getExtMap() != null) {
            iDMComponent.getExtMap().put(CARD_GROUP_TAG, str);
        }
    }

    private void setComponentPosition(IDMComponent iDMComponent, String str) {
        if (!TextUtils.isEmpty(str)) {
            iDMComponent.getExtMap().put(POSITION_TAG, str);
        }
    }

    public static String getComponentPosition(IDMComponent iDMComponent) {
        Object obj;
        if (iDMComponent == null || iDMComponent.getExtMap() == null || (obj = iDMComponent.getExtMap().get(POSITION_TAG)) == null) {
            return null;
        }
        return String.valueOf(obj);
    }
}
