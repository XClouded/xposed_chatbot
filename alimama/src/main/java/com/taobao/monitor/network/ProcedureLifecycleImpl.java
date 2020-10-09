package com.taobao.monitor.network;

import alimama.com.unweventparse.constants.EventConstants;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.monitor.common.ThreadUtils;
import com.taobao.monitor.logger.Logger;
import com.taobao.monitor.procedure.Header;
import com.taobao.monitor.procedure.ProcedureImpl;
import com.taobao.monitor.procedure.Value;
import com.taobao.monitor.procedure.model.Biz;
import com.taobao.monitor.procedure.model.Event;
import com.taobao.monitor.procedure.model.Stage;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProcedureLifecycleImpl implements ProcedureImpl.IProcedureLifeCycle {
    private static final String TAG = "NetworkDataUpdate";

    public void begin(Value value) {
    }

    public void event(Value value, Event event) {
    }

    public void stage(Value value, Stage stage) {
    }

    public void end(final Value value) {
        ThreadUtils.start(new Runnable() {
            public void run() {
                ProcedureLifecycleImpl.this.doSendData(value);
            }
        });
    }

    /* access modifiers changed from: private */
    public void doSendData(Value value) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("version", Header.apmVersion);
            jSONObject.put("topic", value.topic());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("X-timestamp", value.timestamp()).put("X-appId", Header.appId).put("X-appKey", Header.appKey).put("X-appBuild", Header.appBuild).put("X-appPatch", Header.appPatch).put("X-channel", Header.channel).put("X-utdid", Header.utdid).put("X-brand", Header.brand).put("X-deviceModel", Header.deviceModel).put("X-os", Header.os).put("X-osVersion", Header.osVersion).put("X-userId", Header.userId).put("X-userNick", Header.userNick).put("X-session", Header.session).put("X-processName", Header.processName).put("X-appVersion", Header.appVersion).put("X-launcherMode", Header.launcherMode);
            jSONObject.put(EventConstants.Mtop.HEADERS, jSONObject2);
            jSONObject.put("value", valueJson(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jSONObject3 = jSONObject.toString();
        Logger.i(TAG, jSONObject3);
        NetworkSenderProxy.instance().send(value.topic(), jSONObject3);
    }

    private JSONObject valueJson(Value value) throws Exception {
        boolean z;
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        Map<String, Object> properties = value.properties();
        if (properties == null || properties.size() == 0) {
            z = false;
        } else {
            for (Map.Entry next : properties.entrySet()) {
                safePutJson(jSONObject2, (String) next.getKey(), next.getValue());
            }
            z = true;
        }
        List<Biz> bizs = value.bizs();
        if (!(bizs == null || bizs.size() == 0)) {
            JSONObject jSONObject3 = new JSONObject();
            for (Biz next2 : bizs) {
                Map<String, Object> properties2 = next2.properties();
                JSONObject jSONObject4 = new JSONObject();
                if (!(properties2 == null || properties2.size() == 0)) {
                    mapInsert2Json(jSONObject4, properties2);
                }
                Map<String, Object> abTest = next2.abTest();
                if (!(abTest == null || abTest.size() == 0)) {
                    JSONObject jSONObject5 = new JSONObject();
                    mapInsert2Json(jSONObject5, abTest);
                    jSONObject4.put("abtest", jSONObject5);
                }
                Map<String, Object> stages = next2.stages();
                if (!(stages == null || stages.size() == 0)) {
                    JSONObject jSONObject6 = new JSONObject();
                    mapInsert2Json(jSONObject6, stages);
                    jSONObject4.put("stages", jSONObject6);
                }
                jSONObject3.put(next2.bizID(), jSONObject4);
            }
            jSONObject2.put("bizTags", jSONObject3);
            z = true;
        }
        if (z) {
            jSONObject.put("properties", jSONObject2);
        }
        Map<String, Object> statistics = value.statistics();
        JSONObject jSONObject7 = new JSONObject();
        if (!(statistics == null || statistics.size() == 0)) {
            mapInsert2Json(jSONObject7, statistics);
        }
        Map<String, Integer> counters = value.counters();
        if (!(counters == null || counters.size() == 0)) {
            mapInsert2Json(jSONObject7, counters);
        }
        if (!(counters.size() == 0 && statistics.size() == 0)) {
            jSONObject.put("stats", jSONObject7);
        }
        List<Event> events = value.events();
        if (!(events == null || events.size() == 0)) {
            JSONArray jSONArray = new JSONArray();
            for (Event next3 : events) {
                JSONObject jSONObject8 = new JSONObject();
                jSONObject8.put("timestamp", next3.timestamp());
                jSONObject8.put("name", next3.name());
                mapInsert2Json(jSONObject8, next3.properties());
                jSONArray.put(jSONObject8);
            }
            jSONObject.put(ProtocolConst.KEY_EVENTS, jSONArray);
        }
        List<Stage> stages2 = value.stages();
        if (!(stages2 == null || stages2.size() == 0)) {
            JSONObject jSONObject9 = new JSONObject();
            for (Stage next4 : stages2) {
                jSONObject9.put(next4.name(), next4.timestamp());
            }
            jSONObject.put("stages", jSONObject9);
        }
        List<Value> subValues = value.subValues();
        if (!(subValues == null || subValues.size() == 0)) {
            JSONArray jSONArray2 = new JSONArray();
            for (Value next5 : subValues) {
                JSONObject valueJson = valueJson(next5);
                JSONObject jSONObject10 = new JSONObject();
                jSONObject10.put(next5.topic(), valueJson);
                jSONArray2.put(jSONObject10);
            }
            jSONObject.put("subProcedures", jSONArray2);
        }
        return jSONObject;
    }

    private void mapInsert2Json(JSONObject jSONObject, Map<String, ?> map) throws Exception {
        mapInsert2Json(jSONObject, map, 2);
    }

    private void mapInsert2Json(JSONObject jSONObject, Map<String, ?> map, int i) throws Exception {
        if (map != null && i > 0) {
            for (Map.Entry next : map.entrySet()) {
                safePutJson(jSONObject, (String) next.getKey(), next.getValue(), i);
            }
        }
    }

    private void safePutJson(JSONObject jSONObject, String str, Object obj) throws Exception {
        safePutJson(jSONObject, str, obj, 2);
    }

    private void safePutJson(JSONObject jSONObject, String str, Object obj, int i) throws Exception {
        if (obj instanceof Integer) {
            jSONObject.put(str, ((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            jSONObject.put(str, ((Long) obj).longValue());
        } else if (obj instanceof Float) {
            jSONObject.put(str, (double) ((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            jSONObject.put(str, ((Double) obj).doubleValue());
        } else if (obj instanceof Boolean) {
            jSONObject.put(str, ((Boolean) obj).booleanValue());
        } else if (obj instanceof Character) {
            jSONObject.put(str, ((Character) obj).charValue());
        } else if (obj instanceof Short) {
            jSONObject.put(str, ((Short) obj).shortValue());
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.size() != 0) {
                JSONObject jSONObject2 = new JSONObject();
                mapInsert2Json(jSONObject2, map, i - 1);
                jSONObject.put(str, jSONObject2);
            }
        } else {
            jSONObject.put(str, obj);
        }
    }
}
