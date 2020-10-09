package com.taobao.android.ultron.datamodel.imp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Keep;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.taobao.android.ultron.common.model.DynamicTemplate;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.common.model.IDMEvent;
import com.taobao.android.ultron.common.utils.TimeProfileUtil;
import com.taobao.android.ultron.common.utils.UnifyLog;
import com.taobao.android.ultron.datamodel.cache.CacheDataResult;
import com.taobao.android.ultron.datamodel.cache.UltronTemplateManager;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.domain.MtopResponse;

public class ParseResponseHelper {
    private static final String TAG = "ParseResponseHelper";
    /* access modifiers changed from: private */
    public volatile CacheDataResult cacheDataResult;
    private DMContext mDMContext;
    private Map<String, Object> mExtraData = new HashMap();
    private boolean mIsSuccess = false;
    private AsyncTask<Void, Void, CacheDataResult> parseCacheAsyncTask;

    public ParseResponseHelper(DMContext dMContext) {
        this.mDMContext = dMContext;
    }

    public void parseResponse(MtopResponse mtopResponse) {
        if (mtopResponse != null) {
            parseResponse(mtopResponse.getBytedata());
        }
    }

    public void parseResponse(JSONObject jSONObject) {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.cacheDataResult == null && this.parseCacheAsyncTask != null) {
            try {
                this.parseCacheAsyncTask.get();
            } catch (Throwable th) {
                UnifyLog.e(TAG, "parseCacheAsyncTask.get()", Log.getStackTraceString(th));
            }
            UnifyLog.i(TAG, "wait cacheDataResult time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.cacheDataResult != null) {
            parseDataWithCache(jSONObject, this.cacheDataResult);
            long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis2;
            UnifyLog.i(TAG, "parseDataWithCache time:" + currentTimeMillis3 + "ms");
            return;
        }
        parseResponseWithoutCache(jSONObject);
        long currentTimeMillis4 = System.currentTimeMillis() - currentTimeMillis2;
        UnifyLog.i(TAG, "parseResponseWithoutCache time:" + currentTimeMillis4 + "ms");
    }

    public void parseResponseWithoutCache(JSONObject jSONObject) {
        TimeProfileUtil.start("ParseResponse", "start");
        if (this.mDMContext != null && jSONObject != null) {
            DMEngine engine = this.mDMContext.getEngine();
            if (engine == null) {
                engine = new DMEngine(this.mDMContext.mGzip);
                this.mDMContext.setEngine(engine);
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("data");
            this.mIsSuccess = engine.parseProcess(this.mDMContext, jSONObject2);
            this.mExtraData.put(ProtocolConst.KEY_PROTOCOL_VERSION, this.mDMContext.getProtocolVersion());
            if (jSONObject2 == null) {
                this.mExtraData.put("reload", true);
                return;
            }
            this.mExtraData.put("reload", Boolean.valueOf(Boolean.TRUE.toString().equalsIgnoreCase(jSONObject2.getString("reload"))));
            TimeProfileUtil.end("ParseResponse", "end");
        }
    }

    public void parseResponse(byte[] bArr) {
        if (this.mDMContext != null && bArr != null) {
            parseResponse((JSONObject) JSON.parseObject(bArr, (Type) JSONObject.class, new Feature[0]));
        }
    }

    public boolean isSuccess() {
        return this.mIsSuccess;
    }

    public Map<String, Object> getExtraData() {
        return this.mExtraData;
    }

    public void addExtraData(String str, Object obj) {
        this.mExtraData.put(str, obj);
    }

    private String getContainerTypeByComponentType(String str, Map<String, JSONObject> map) {
        JSONObject jSONObject = map.get(str);
        return jSONObject != null ? jSONObject.getString(ProtocolConst.KEY_CONTAINER_TYPE) : "native";
    }

    private Map<String, List<IDMEvent>> parseEventMap(JSONObject jSONObject) {
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
                    if ((next2 instanceof JSONObject) && (parseEvent = parseEvent((JSONObject) next2)) != null) {
                        arrayList.add(parseEvent);
                    }
                }
                hashMap.put(str, arrayList);
            }
        }
        return hashMap;
    }

    private IDMEvent parseEvent(JSONObject jSONObject) {
        if (jSONObject == null || jSONObject.isEmpty()) {
            return null;
        }
        return new DMEvent(jSONObject.getString("type"), jSONObject.getJSONObject(ProtocolConst.KEY_FIELDS), (List<IDMComponent>) null);
    }

    @SuppressLint({"StaticFieldLeak"})
    public void processCache(Context context, String str, JSONObject jSONObject, boolean z, boolean z2) {
        String str2;
        boolean z3;
        boolean z4;
        JSONObject jSONObject2;
        JSONObject jSONObject3 = jSONObject;
        UnifyLog.i(TAG, "processCache");
        if (jSONObject3 != null && z) {
            JSONObject jSONObject4 = null;
            JSONObject jSONObject5 = jSONObject3.getJSONObject(ProtocolConst.KEY_ENDPOINT);
            if (!(jSONObject5 == null || jSONObject5.isEmpty() || (jSONObject2 = jSONObject5.getJSONObject("meta")) == null)) {
                jSONObject4 = jSONObject2.getJSONObject("template");
            }
            if (jSONObject4 != null) {
                UltronTemplateManager instance = UltronTemplateManager.getInstance(context, str);
                UnifyLog.i(TAG, "processCache with cacheConfig:" + jSONObject4);
                final String string = jSONObject4.getString("id");
                final String string2 = jSONObject4.getString("version");
                if (string != null) {
                    if (string2 != null) {
                        str2 = string + "_$_" + string2;
                    } else {
                        str2 = string;
                    }
                    JSONArray jSONArray = jSONObject4.getJSONArray("cacheFields");
                    UnifyLog.i(TAG, "processCache templateKey:" + str2);
                    if (!TextUtils.isEmpty(str2) && jSONArray != null) {
                        Iterator<Object> it = jSONArray.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                if (!jSONObject3.containsKey(it.next())) {
                                    z3 = true;
                                    break;
                                }
                            } else {
                                z3 = false;
                                break;
                            }
                        }
                        if (z3) {
                            JSONObject templateById = instance.getTemplateById(str2);
                            if (templateById != null) {
                                Iterator<Object> it2 = jSONArray.iterator();
                                z4 = false;
                                while (it2.hasNext()) {
                                    Object next = it2.next();
                                    if (!jSONObject3.containsKey(next)) {
                                        Object obj = templateById.get(next);
                                        if (obj != null) {
                                            jSONObject3.put((String) next, obj);
                                        } else {
                                            z4 = true;
                                        }
                                    }
                                }
                                UnifyLog.i(TAG, "processCache use cache");
                            } else {
                                z4 = true;
                            }
                            if (z4) {
                                UnifyLog.e(TAG, "processCache dataWrong");
                                instance.deleteTemplateById(str2);
                            }
                        } else {
                            final JSONArray jSONArray2 = jSONArray;
                            final JSONObject jSONObject6 = jSONObject;
                            final UltronTemplateManager ultronTemplateManager = instance;
                            final String str3 = str2;
                            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                                public void run() {
                                    JSONObject jSONObject = new JSONObject();
                                    Iterator<Object> it = jSONArray2.iterator();
                                    while (it.hasNext()) {
                                        Object next = it.next();
                                        jSONObject.put((String) next, jSONObject6.get(next));
                                    }
                                    ultronTemplateManager.saveTemplate(str3, jSONObject);
                                    UnifyLog.i(ParseResponseHelper.TAG, "processCache save cache");
                                    List<String> templateIds = ultronTemplateManager.getTemplateIds();
                                    if (templateIds != null) {
                                        for (String str : new ArrayList(templateIds)) {
                                            String[] split = str.split("_\\$_");
                                            if (split.length == 2 && TextUtils.equals(split[0], string) && !TextUtils.equals(split[1], string2)) {
                                                ultronTemplateManager.deleteTemplateById(str);
                                                ultronTemplateManager.deleteCacheDataResult(str);
                                                UnifyLog.i(ParseResponseHelper.TAG, "processCache deleteTemplateById:" + str);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        if (z2) {
                            this.cacheDataResult = instance.getCacheDataResult(str2);
                            if (this.cacheDataResult == null) {
                                final JSONObject jSONObject7 = jSONObject;
                                final JSONArray jSONArray3 = jSONArray;
                                final UltronTemplateManager ultronTemplateManager2 = instance;
                                final String str4 = str2;
                                this.parseCacheAsyncTask = new AsyncTask<Void, Void, CacheDataResult>() {
                                    /* access modifiers changed from: protected */
                                    public CacheDataResult doInBackground(Void... voidArr) {
                                        UnifyLog.i(ParseResponseHelper.TAG, "parseCacheData async running");
                                        CacheDataResult unused = ParseResponseHelper.this.cacheDataResult = ParseResponseHelper.this.parseCacheData(jSONObject7, jSONArray3);
                                        if (ParseResponseHelper.this.cacheDataResult != null) {
                                            ultronTemplateManager2.saveCacheDataResult(str4, ParseResponseHelper.this.cacheDataResult);
                                        }
                                        UnifyLog.i(ParseResponseHelper.TAG, " parseCacheData done");
                                        return ParseResponseHelper.this.cacheDataResult;
                                    }
                                };
                                this.parseCacheAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public CacheDataResult parseCacheData(JSONObject jSONObject, JSONArray jSONArray) {
        String str;
        String[] componentInfo;
        JSONArray jSONArray2;
        JSONObject jSONObject2 = jSONObject;
        JSONArray jSONArray3 = jSONArray;
        CacheDataResult cacheDataResult2 = new CacheDataResult();
        if (jSONObject2 == null || jSONArray3 == null || jSONArray.isEmpty()) {
            return cacheDataResult2;
        }
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        JSONObject jSONObject3 = jSONObject2.getJSONObject("container");
        if (!(jSONObject3 == null || (jSONArray2 = jSONObject3.getJSONArray("data")) == null)) {
            int size = jSONArray2.size();
            StringBuilder sb = new StringBuilder("\n");
            for (int i = 0; i < size; i++) {
                JSONObject jSONObject4 = jSONArray2.getJSONObject(i);
                if (jSONObject4 != null) {
                    arrayList.add(new DynamicTemplate(jSONObject4));
                    JSONArray jSONArray4 = jSONObject4.getJSONArray("type");
                    int size2 = jSONArray4.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        String string = jSONArray4.getString(i2);
                        sb.append("componentType:");
                        sb.append(string);
                        sb.append(", container:");
                        sb.append(jSONObject4.toString());
                        sb.append("\n");
                        hashMap.put(string, jSONObject4);
                    }
                }
            }
        }
        if (jSONArray3.contains("container")) {
            cacheDataResult2.setTemplateList(arrayList);
            cacheDataResult2.setContainerMap(hashMap);
        }
        if (jSONArray3.contains("data")) {
            HashMap hashMap2 = new HashMap();
            JSONObject jSONObject5 = jSONObject2.getJSONObject("data");
            if (jSONObject5 != null) {
                boolean hasFeature = hasFeature(ProtocolFeatures.FEATURE_TAG_ID);
                for (Map.Entry next : jSONObject5.entrySet()) {
                    if (!(next == null || (str = (String) next.getKey()) == null)) {
                        Object value = next.getValue();
                        if (value instanceof JSONObject) {
                            if (str != null && hasFeature && (componentInfo = ParseModule.getComponentInfo(str)) != null && componentInfo.length == 2) {
                                jSONObject2.put("tag", (Object) componentInfo[0]);
                                jSONObject2.put("id", (Object) componentInfo[1]);
                            }
                            JSONObject jSONObject6 = (JSONObject) value;
                            String string2 = jSONObject6.getString("type");
                            String string3 = jSONObject6.getString("tag");
                            String containerTypeByComponentType = getContainerTypeByComponentType(string2, hashMap);
                            JSONObject jSONObject7 = (JSONObject) hashMap.get(string2);
                            if (jSONObject7 != null) {
                                UnifyLog.e(TAG, "createDMComponent", "type", string2, "tag", string3);
                            }
                            DMComponent dMComponent = new DMComponent(jSONObject6, containerTypeByComponentType, jSONObject7, parseEventMap(jSONObject2.getJSONObject(ProtocolConst.KEY_EVENTS)));
                            dMComponent.setComponentKey(str);
                            hashMap2.put(str, dMComponent);
                        }
                    }
                }
            }
            cacheDataResult2.setComponentMap(hashMap2);
        }
        return cacheDataResult2;
    }

    public void parseProtocolFeatures(JSONObject jSONObject) {
        JSONObject jSONObject2;
        if (jSONObject != null && jSONObject.containsKey(ProtocolConst.KEY_ENDPOINT) && (jSONObject2 = jSONObject.getJSONObject(ProtocolConst.KEY_ENDPOINT)) != null) {
            String string = jSONObject2.getString(ProtocolConst.KEY_FEATURES);
            if (!TextUtils.isEmpty(string)) {
                this.mDMContext.setProtocolFeatures(string);
                UnifyLog.e(TAG, "protocol features: " + string);
            }
        }
    }

    public boolean hasFeature(BigInteger bigInteger) {
        if (this.mDMContext.getProtocolFeatures() == null) {
            return false;
        }
        return ProtocolFeatures.hasFeature(new BigInteger(this.mDMContext.getProtocolFeatures()), bigInteger);
    }

    public void parseDataWithCache(JSONObject jSONObject, CacheDataResult cacheDataResult2) {
        UnifyLog.e(TAG, "parseDataWithCache");
        if (this.mDMContext != null && jSONObject != null) {
            DMEngine engine = this.mDMContext.getEngine();
            if (engine == null) {
                engine = new DMEngine(this.mDMContext.mGzip);
                this.mDMContext.setEngine(engine);
            }
            if (cacheDataResult2 != null) {
                Map<String, DMComponent> componentMap = cacheDataResult2.getComponentMap();
                List<DynamicTemplate> templateList = cacheDataResult2.getTemplateList();
                Map<String, JSONObject> container = cacheDataResult2.getContainer();
                if (componentMap != null) {
                    this.mDMContext.getComponentMap().putAll(componentMap);
                    this.mDMContext.setUseCache("data");
                }
                if (templateList != null) {
                    this.mDMContext.setTemplateList(templateList);
                    this.mDMContext.setUseCache("container");
                    StringBuilder sb = new StringBuilder();
                    sb.append("template cache info: \n");
                    for (DynamicTemplate next : templateList) {
                        if (next != null) {
                            sb.append(next.toString());
                            sb.append(";\n");
                        }
                    }
                    UnifyLog.e(TAG, sb.toString());
                }
                if (container != null) {
                    this.mDMContext.setType2containerInfoMap(container);
                    this.mDMContext.setUseCache("container");
                }
            } else {
                this.mDMContext.setUseCache((String[]) null);
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("data");
            this.mIsSuccess = engine.parseProcess(this.mDMContext, jSONObject2);
            this.mExtraData.put(ProtocolConst.KEY_PROTOCOL_VERSION, this.mDMContext.getProtocolVersion());
            if (jSONObject2 == null) {
                this.mExtraData.put("reload", true);
                return;
            }
            this.mExtraData.put("reload", Boolean.valueOf(Boolean.TRUE.toString().equalsIgnoreCase(jSONObject2.getString("reload"))));
        }
    }

    public static List<TemplateInfo> getTemplateInfo(Context context, String str) {
        List<String> templateIds = UltronTemplateManager.getInstance(context, str).getTemplateIds();
        UnifyLog.i(TAG, "getTemplateInfo list:" + templateIds);
        if (templateIds == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(templateIds.size());
        for (String split : templateIds) {
            String[] split2 = split.split("_\\$_");
            if (split2.length >= 1) {
                arrayList.add(new TemplateInfo(split2[0], split2.length == 2 ? split2[1] : null));
            }
        }
        return arrayList;
    }

    @Keep
    public static class TemplateInfo implements Serializable {
        public String id;
        public String version;

        public TemplateInfo(String str, String str2) {
            this.id = str;
            this.version = str2;
        }
    }
}
