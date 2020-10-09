package com.taobao.android.ultron.datamodel.imp;

import android.content.Context;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.ValidateResult;
import com.taobao.android.ultron.common.model.DynamicTemplate;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.datamodel.IDMContext;
import com.taobao.android.ultron.datamodel.ISubmitModule;
import com.taobao.android.ultron.datamodel.cache.CacheDataResult;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DMContext implements IDMContext {
    public static final String LOG_TAG = "ultron-sdk";
    private Map<String, ExtendBlock> extendBlockComponentMap = new HashMap();
    private boolean isCacheData = false;
    private Context mAppContext;
    private String mBizName;
    private CacheDataResult mCacheDataResult;
    private Set<String> mCacheFields = new HashSet();
    private JSONObject mCommon;
    List<IDMComponent> mComponentList;
    Map<String, DMComponent> mComponentMap = new ConcurrentHashMap();
    private JSONObject mData;
    List<DynamicTemplate> mDynamicTemplateList;
    private JSONObject mEndpoint;
    private DMEngine mEngine;
    private JSONObject mGlobal;
    boolean mGzip;
    private JSONObject mHierarchy;
    private JSONObject mLinkage;
    private String mProtocolFeatures;
    private String mProtocolVersion = "";
    Map<String, DMComponent> mRenderComponentMap = new ConcurrentHashMap();
    private String mRootComponentKey;
    private JSONObject mStructure;
    private ConcurrentHashMap<String, JSONObject> mType2containerInfoMap = new ConcurrentHashMap<>();
    private ValidateModule mValidateModule;

    public DMContext(boolean z) {
        this.mEngine = new DMEngine(z);
        this.mGzip = z;
    }

    public ConcurrentHashMap<String, JSONObject> getType2containerInfoMap() {
        return this.mType2containerInfoMap;
    }

    public void setType2containerInfoMap(Map<String, JSONObject> map) {
        if (map != null) {
            this.mType2containerInfoMap.clear();
            this.mType2containerInfoMap.putAll(map);
        }
    }

    public List<IDMComponent> getComponents() {
        return this.mComponentList;
    }

    public void setComponents(List<IDMComponent> list) {
        this.mComponentList = list;
    }

    public IDMComponent getComponentByName(String str) {
        return this.mComponentMap.get(str);
    }

    public JSONObject getGlobal() {
        return this.mGlobal;
    }

    public List<IDMComponent> getComponentsByRoot(String str) {
        return this.mEngine.getComponentsByRoot(this, str);
    }

    public List<DynamicTemplate> getDynamicTemplateList() {
        return this.mDynamicTemplateList;
    }

    public IDMComponent getRootComponent() {
        return this.mEngine.getRootComponent();
    }

    public JSONObject getHierarchy() {
        return this.mHierarchy;
    }

    public JSONObject getData() {
        return this.mData;
    }

    public JSONObject getLinkage() {
        return this.mLinkage;
    }

    public JSONObject getCommon() {
        return this.mCommon;
    }

    public JSONArray getRequest() {
        if (this.mLinkage == null) {
            return null;
        }
        return this.mLinkage.getJSONArray("request");
    }

    public JSONArray getInput() {
        if (this.mLinkage == null) {
            return null;
        }
        return this.mLinkage.getJSONArray("input");
    }

    /* access modifiers changed from: package-private */
    public void setComponentList(List<IDMComponent> list) {
        this.mComponentList = list;
    }

    /* access modifiers changed from: package-private */
    public void setTemplateList(List<DynamicTemplate> list) {
        this.mDynamicTemplateList = list;
    }

    public DMEngine getEngine() {
        return this.mEngine;
    }

    /* access modifiers changed from: package-private */
    public void setEngine(DMEngine dMEngine) {
        this.mEngine = dMEngine;
    }

    public Map<String, DMComponent> getComponentMap() {
        return this.mComponentMap;
    }

    public Map<String, DMComponent> getRenderComponentMap() {
        return this.mRenderComponentMap;
    }

    public JSONObject getStructure() {
        return this.mStructure;
    }

    /* access modifiers changed from: package-private */
    public void setHierarchy(JSONObject jSONObject) {
        this.mHierarchy = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public JSONObject mergeStructure(JSONObject jSONObject) {
        Set<String> keySet = this.extendBlockComponentMap.keySet();
        if (keySet == null || keySet.isEmpty()) {
            this.mStructure = jSONObject;
            return this.mStructure;
        }
        for (Map.Entry next : jSONObject.entrySet()) {
            if (next != null) {
                String str = (String) next.getKey();
                if (keySet.contains(str)) {
                    Object value = next.getValue();
                    Object obj = this.mStructure.get(str);
                    if (obj == null) {
                        this.mStructure.put(str, value);
                    }
                    mergeExtendBlock(value, obj);
                } else {
                    this.mStructure.put((String) next.getKey(), next.getValue());
                }
            }
        }
        return this.mStructure;
    }

    private void mergeExtendBlock(Object obj, Object obj2) {
        if ((obj instanceof JSONArray) && (obj2 instanceof JSONArray)) {
            int i = 0;
            while (true) {
                JSONArray jSONArray = (JSONArray) obj;
                if (i < jSONArray.size()) {
                    Object obj3 = jSONArray.get(i);
                    JSONArray jSONArray2 = (JSONArray) obj2;
                    if (!jSONArray2.contains(obj3)) {
                        jSONArray2.add(obj3);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void setStructure(JSONObject jSONObject) {
        this.mStructure = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public void setData(JSONObject jSONObject) {
        this.mData = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public JSONObject mergeData(JSONObject jSONObject) {
        if (this.mData == null) {
            this.mData = jSONObject;
        } else {
            this.mData.putAll(jSONObject);
        }
        return this.mData;
    }

    /* access modifiers changed from: package-private */
    public void setCommon(JSONObject jSONObject) {
        this.mCommon = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public void setLinkage(JSONObject jSONObject) {
        this.mLinkage = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public void setGlobal(JSONObject jSONObject) {
        this.mGlobal = jSONObject;
    }

    public String getProtocolVersion() {
        return this.mProtocolVersion;
    }

    public ValidateResult validate() {
        if (this.mValidateModule == null) {
            this.mValidateModule = new ValidateModule(this);
        }
        return this.mValidateModule.execute();
    }

    public void setProtocolVersion(String str) {
        this.mProtocolVersion = str;
    }

    public void setSubmitModule(ISubmitModule iSubmitModule) {
        this.mEngine.setSubmitModule(iSubmitModule);
    }

    public void setEndpoint(JSONObject jSONObject) {
        this.mEndpoint = jSONObject;
    }

    public JSONObject getEndpoint() {
        return this.mEndpoint;
    }

    public Map<String, ExtendBlock> getExtendBlockComponentMap() {
        return this.extendBlockComponentMap;
    }

    public boolean isCacheData() {
        return this.isCacheData;
    }

    public void setCacheData(boolean z) {
        this.isCacheData = z;
    }

    public String getBizName() {
        return this.mBizName;
    }

    public void setBizName(String str) {
        this.mBizName = str;
    }

    public void reset() {
        this.mEndpoint = null;
        this.mHierarchy = null;
        this.mStructure = null;
        this.mGlobal = null;
        this.mLinkage = null;
        this.mCommon = null;
        this.mProtocolVersion = null;
        this.mComponentList = null;
        this.mDynamicTemplateList = null;
        this.mData = null;
        if (!isUseCache("data")) {
            this.mComponentMap.clear();
        }
        this.mType2containerInfoMap.clear();
        this.mRenderComponentMap.clear();
        this.extendBlockComponentMap.clear();
    }

    public boolean isUseCache(String str) {
        return this.mCacheFields.contains(str);
    }

    public void setUseCache(String... strArr) {
        if (strArr != null) {
            this.mCacheFields.addAll(Arrays.asList(strArr));
        } else {
            this.mCacheFields.clear();
        }
    }

    public CacheDataResult getCacheDataResult() {
        return this.mCacheDataResult;
    }

    public void setCacheDataResult(CacheDataResult cacheDataResult) {
        this.mCacheDataResult = cacheDataResult;
    }

    public Context getContext() {
        return this.mAppContext;
    }

    public void setContext(Context context) {
        if (context != null) {
            this.mAppContext = context.getApplicationContext();
        }
    }

    public String getProtocolFeatures() {
        return this.mProtocolFeatures;
    }

    public void setProtocolFeatures(String str) {
        this.mProtocolFeatures = str;
    }

    public String getRootComponentKey() {
        return this.mRootComponentKey;
    }

    public void setRootComponentKey(String str) {
        this.mRootComponentKey = str;
    }
}
