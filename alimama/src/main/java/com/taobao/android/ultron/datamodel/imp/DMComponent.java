package com.taobao.android.ultron.datamodel.imp;

import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.cons.c;
import com.taobao.android.ultron.common.ValidateResult;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.common.model.IDMEvent;
import com.taobao.android.ultron.common.model.LinkageType;
import com.taobao.android.ultron.common.utils.UnifyLog;
import com.taobao.android.ultron.datamodel.IDMContext;
import com.taobao.android.ultron.datamodel.util.RollbackUtils;
import com.taobao.weex.ui.component.richtext.node.RichTextNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DMComponent implements IDMComponent, Cloneable, Serializable {
    private static final String BOOL_TRUE_VALUE = "true";
    private static final String STRING_UNDERLINE = "_";
    private static final String TAG = "DMComponent";
    String componentKey;
    JSONObject layout;
    String mBizName;
    private List<IDMComponent> mChildren = new ArrayList();
    JSONObject mContainerInfo;
    String mContainerType = "native";
    private IDMComponent.CustomValidate mCustomValidate;
    JSONObject mData;
    private Map<String, List<IDMEvent>> mEventMap;
    JSONObject mEvents;
    private ArrayMap<String, Object> mExtMap = new ArrayMap<>();
    boolean mExtendBlock;
    JSONObject mFields;
    boolean mHasMore;
    JSONObject mHidden;
    String mID;
    LinkageType mLinkageType = LinkageType.REFRESH;
    int mModifiedCount;
    DMComponent mParent;
    private JSONObject mStashData;
    String mSubmit;
    String mTag;
    String mTriggerEvent;
    String mType;

    public DMComponent(JSONObject jSONObject, String str, JSONObject jSONObject2, Map<String, List<IDMEvent>> map) {
        this.mContainerType = str;
        this.mContainerInfo = jSONObject2;
        this.mEventMap = map;
        loadData(jSONObject);
    }

    public String getContainerType() {
        return this.mContainerType;
    }

    public JSONObject getContainerInfo() {
        return this.mContainerInfo;
    }

    public JSONObject getData() {
        return this.mData;
    }

    public JSONObject getFields() {
        return this.mFields;
    }

    public JSONObject getHidden() {
        return this.mHidden;
    }

    public String getId() {
        if (this.mData == null) {
            return "unknown";
        }
        return this.mData.getString("id");
    }

    public String getTag() {
        if (this.mData == null) {
            return "unknown";
        }
        return this.mData.getString("tag");
    }

    public String getPosition() {
        if (this.mData == null) {
            return "unknown";
        }
        return this.mData.getString("position");
    }

    public String getCardGroup() {
        if (this.mData == null) {
            return "unknown";
        }
        return this.mData.getString(ProtocolConst.KEY_CARDGROUP);
    }

    public String getType() {
        if (this.mData == null) {
            return "unknown";
        }
        return this.mData.getString("type");
    }

    public void setComponentKey(String str) {
        this.componentKey = str;
    }

    public String getKey() {
        if (!TextUtils.isEmpty(this.componentKey)) {
            return this.componentKey;
        }
        String tag = getTag();
        String id = getId();
        if (tag == null || id == null) {
            return null;
        }
        return tag + "_" + id;
    }

    public Map<String, List<IDMEvent>> getEventMap() {
        return this.mEventMap;
    }

    public LinkageType getLinkageType() {
        return this.mLinkageType;
    }

    public IDMComponent getParent() {
        return this.mParent;
    }

    /* access modifiers changed from: package-private */
    public void setLinkageType(LinkageType linkageType) {
        this.mLinkageType = linkageType;
    }

    /* access modifiers changed from: protected */
    public void onReload(IDMContext iDMContext, JSONObject jSONObject) {
        loadData(jSONObject);
    }

    /* access modifiers changed from: protected */
    public void onReloadEvent(Map<String, List<IDMEvent>> map) {
        this.mEventMap = map;
    }

    /* access modifiers changed from: protected */
    public JSONObject submitData() {
        return this.mData;
    }

    /* access modifiers changed from: protected */
    public JSONObject adjustData() {
        return this.mData;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldSubmit() {
        if (this.mSubmit != null) {
            return this.mSubmit.equalsIgnoreCase("true");
        }
        return false;
    }

    public void setParent(DMComponent dMComponent) {
        this.mParent = dMComponent;
    }

    private void loadData(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.mData = jSONObject;
            this.mID = jSONObject.getString("id");
            this.mTag = jSONObject.getString("tag");
            this.mType = this.mData.getString("type");
            this.mSubmit = jSONObject.getString(ProtocolConst.KEY_SUBMIT);
            this.mBizName = jSONObject.getString(ProtocolConst.KEY_BIZNAME);
            JSONObject jSONObject2 = jSONObject.getJSONObject(ProtocolConst.KEY_FIELDS);
            boolean z = false;
            if (jSONObject2 == null) {
                jSONObject2 = new JSONObject();
                jSONObject.put(ProtocolConst.KEY_FIELDS, (Object) jSONObject2);
                UnifyLog.e(TAG, "original fields not exist， fix it", this.mTag, this.mID);
            }
            this.mFields = jSONObject2;
            if (this.mTag == null) {
                this.mTag = "";
                jSONObject.put("tag", (Object) this.mTag);
            }
            if (this.mType == null) {
                this.mType = "";
                jSONObject.put("type", (Object) this.mType);
            }
            this.mHidden = this.mData.getJSONObject("hidden");
            this.mEvents = this.mData.getJSONObject(ProtocolConst.KEY_EVENTS);
            this.mExtendBlock = this.mData.containsKey(ProtocolConst.KEY_EXTEND_BLOCK) ? this.mData.getBoolean(ProtocolConst.KEY_EXTEND_BLOCK).booleanValue() : false;
            if (this.mData.containsKey(ProtocolConst.KEY_HAS_MORE)) {
                z = this.mData.getBoolean(ProtocolConst.KEY_HAS_MORE).booleanValue();
            }
            this.mHasMore = z;
            this.layout = this.mData.containsKey("layout") ? this.mData.getJSONObject("layout") : null;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void addChildren(List<IDMComponent> list) {
        this.mChildren.addAll(list);
    }

    public void addChild(IDMComponent iDMComponent) {
        this.mChildren.add(iDMComponent);
    }

    public List<IDMComponent> getChildren() {
        return this.mChildren;
    }

    public ArrayMap<String, Object> getExtMap() {
        return this.mExtMap;
    }

    public void writeFields(String str, Object obj) {
        if (this.mFields != null && !TextUtils.isEmpty(str) && obj != null) {
            this.mFields.put(str, obj);
        }
    }

    public void record() {
        this.mStashData = JSON.parseObject(this.mData.toJSONString());
        if (this.mEventMap != null) {
            RollbackUtils.recordEventMap(this.mEventMap);
        }
    }

    public JSONObject getStashData() {
        return this.mStashData;
    }

    public void rollBack() {
        if (this.mStashData != null) {
            loadData(this.mStashData);
            this.mStashData = null;
        }
        if (this.mEventMap != null) {
            RollbackUtils.rollbackEventMap(this.mEventMap);
        }
    }

    public void setCustomValidate(IDMComponent.CustomValidate customValidate) {
        this.mCustomValidate = customValidate;
    }

    public ValidateResult validate() {
        JSONObject jSONObject;
        int size;
        Pattern pattern;
        if (this.mCustomValidate != null) {
            return this.mCustomValidate.onCustomValidate(this);
        }
        ValidateResult validateResult = new ValidateResult();
        if (this.mData == null || (jSONObject = this.mData.getJSONObject(c.j)) == null) {
            return validateResult;
        }
        JSONArray jSONArray = jSONObject.getJSONArray(ProtocolConst.KEY_FIELDS);
        JSONArray jSONArray2 = jSONObject.getJSONArray("regex");
        JSONArray jSONArray3 = jSONObject.getJSONArray("msg");
        if (jSONArray != null && jSONArray2 != null && jSONArray3 != null && !jSONArray.isEmpty() && (size = jSONArray.size()) == jSONArray2.size() && size == jSONArray3.size()) {
            int i = 0;
            while (i < size) {
                String string = this.mFields.getString(jSONArray.getString(i));
                if (string == null) {
                    string = "";
                }
                String string2 = jSONArray2.getString(i);
                String string3 = jSONArray3.getString(i);
                try {
                    pattern = Pattern.compile(string2);
                } catch (Exception unused) {
                    pattern = null;
                }
                if (pattern == null || pattern.matcher(string).find()) {
                    i++;
                } else {
                    validateResult.setValidateState(false);
                    validateResult.setValidateFailedMsg(string3);
                    validateResult.setValidateFailedComponent(this);
                    return validateResult;
                }
            }
        }
        return validateResult;
    }

    public int getStatus() {
        if (this.mData == null) {
            return 2;
        }
        String string = this.mData.getString("status");
        if ("hidden".equals(string)) {
            return 0;
        }
        return "disable".equals(string) ? 1 : 2;
    }

    public int getModifiedCount() {
        return this.mModifiedCount;
    }

    public void updateModifiedCount() {
        this.mModifiedCount++;
    }

    public JSONObject getEvents() {
        return this.mEvents;
    }

    public boolean isExtendBlock() {
        return this.mExtendBlock;
    }

    public boolean hasMore() {
        return this.mHasMore;
    }

    public String getTriggerEvent() {
        return this.mTriggerEvent;
    }

    public void setTriggerEvent(String str) {
        this.mTriggerEvent = str;
    }

    public JSONObject getLayout() {
        return this.layout;
    }

    public String getLayoutType() {
        return this.layout != null ? this.layout.getString("type") : "";
    }

    public JSONObject getLayoutStyle() {
        if (this.layout != null) {
            return this.layout.getJSONObject(RichTextNode.STYLE);
        }
        return null;
    }

    public JSONObject toJsonInfo() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("id", (Object) this.mID);
        jSONObject.put("tag", (Object) this.mTag);
        jSONObject.put("type", (Object) this.mType);
        String str = null;
        jSONObject.put("data", (Object) this.mData != null ? this.mData.toJSONString() : null);
        jSONObject.put(ProtocolConst.KEY_FIELDS, (Object) this.mFields != null ? this.mFields.toJSONString() : null);
        jSONObject.put("hidden", (Object) this.mHidden != null ? this.mHidden.toJSONString() : null);
        jSONObject.put("linkageType", (Object) this.mLinkageType != null ? this.mLinkageType.toString() : null);
        jSONObject.put(ProtocolConst.KEY_CONTAINER_TYPE, (Object) this.mContainerType);
        jSONObject.put("containerInfo", (Object) this.mContainerInfo != null ? this.mContainerInfo.toJSONString() : null);
        jSONObject.put(ProtocolConst.KEY_SUBMIT, (Object) this.mSubmit);
        if (this.mEvents != null) {
            str = this.mEvents.toJSONString();
        }
        jSONObject.put(ProtocolConst.KEY_EVENTS, (Object) str);
        jSONObject.put(ProtocolConst.KEY_EXTEND_BLOCK, (Object) Boolean.valueOf(this.mExtendBlock));
        return jSONObject;
    }
}
