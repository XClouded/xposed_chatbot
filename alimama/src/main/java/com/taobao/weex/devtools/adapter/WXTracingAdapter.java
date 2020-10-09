package com.taobao.weex.devtools.adapter;

import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.phenix.request.ImageStatistics;
import com.taobao.weex.RenderContainer;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.ITracingAdapter;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.debug.WXDebugBridge;
import com.taobao.weex.devtools.toolbox.PerformanceActivity;
import com.taobao.weex.devtools.trace.DomTracker;
import com.taobao.weex.devtools.trace.HealthReport;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.TimeCalculator;
import com.vivo.push.PushClientConstants;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WXTracingAdapter implements ITracingAdapter {
    /* access modifiers changed from: private */
    public volatile SparseArray<WXTracing.TraceEvent> traceEvents = new SparseArray<>();

    public void disable() {
    }

    public void enable() {
    }

    public WXTracingAdapter() {
        WXSDKManager.getInstance().registerInstanceLifeCycleCallbacks(new WXSDKManager.InstanceLifeCycleCallbacks() {
            public void onInstanceCreated(String str) {
            }

            public void onInstanceDestroyed(String str) {
                if (WXTracingAdapter.this.traceEvents != null) {
                    WXLogUtils.d("WXTracingAdapter", "Destroy trace events with instance id " + str);
                    WXTracingAdapter.this.traceEvents.remove(Integer.parseInt(str));
                }
            }
        });
    }

    public void submitTracingEvent(WXTracing.TraceEvent traceEvent) {
        int parseInt = Integer.parseInt(traceEvent.iid);
        if (parseInt == -1) {
            WXLogUtils.e("Wrong instance id: " + parseInt);
        }
        WXTracing.TraceEvent traceEvent2 = this.traceEvents.get(parseInt);
        if (traceEvent2 == null) {
            traceEvent2 = new WXTracing.TraceEvent();
            traceEvent2.traceId = parseInt;
            traceEvent2.ts = traceEvent.ts;
            traceEvent2.subEvents = new SparseArray<>();
            traceEvent2.extParams = new HashMap();
            this.traceEvents.append(parseInt, traceEvent2);
        }
        if ("renderFinish".equals(traceEvent.fname)) {
            traceEvent2.duration = traceEvent.duration;
            if (traceEvent2.subEvents != null) {
                traceEvent.duration = 0.0d;
                traceEvent2.subEvents.append(traceEvent.traceId, traceEvent);
                sendTracingData(traceEvent.iid);
            }
        } else if (traceEvent.parentId == -1) {
            if (traceEvent2.subEvents == null) {
                traceEvent2.subEvents = new SparseArray<>();
            }
            if ("B".equals(traceEvent.ph) || "X".equals(traceEvent.ph)) {
                traceEvent2.subEvents.append(traceEvent.traceId, traceEvent);
            } else if ("E".equals(traceEvent.ph)) {
                WXTracing.TraceEvent traceEvent3 = traceEvent2.subEvents.get(traceEvent.traceId);
                if (traceEvent3 == null) {
                    WXLogUtils.w("WXTracingAdapter", "begin event not found: " + traceEvent.fname + DinamicConstant.DINAMIC_PREFIX_AT + traceEvent.traceId);
                    return;
                }
                traceEvent3.duration = (double) (traceEvent.ts - traceEvent3.ts);
                traceEvent3.ph = "X";
            }
        } else {
            WXTracing.TraceEvent traceEvent4 = traceEvent2.subEvents.get(traceEvent.parentId);
            if (traceEvent4 != null) {
                if (traceEvent4.subEvents == null) {
                    traceEvent4.subEvents = new SparseArray<>();
                }
                traceEvent4.subEvents.append(traceEvent.traceId, traceEvent);
            }
        }
    }

    public WXTracing.TraceEvent getTraceEventByInstanceId(int i) {
        return this.traceEvents.get(i);
    }

    private void enableMonitor(final String str) {
        final WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            TextView textView = new TextView(sDKInstance.getUIContext());
            textView.setText("Weex MNT:" + str);
            textView.setBackgroundColor(Color.parseColor("#AA1E90FF"));
            textView.setTextColor(-1);
            textView.setPadding(10, 10, 10, 10);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 21;
            textView.setLayoutParams(layoutParams);
            ((RenderContainer) sDKInstance.getContainerView()).addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    PerformanceActivity.start(sDKInstance.getUIContext(), Integer.parseInt(str));
                }
            });
        }
    }

    private void sendTracingData(final String str) {
        if (!WXDebugBridge.getInstance().isSessionActive()) {
            WXLogUtils.w("WXTracingAdapter", "Debug session not active");
            return;
        }
        JSONArray jSONArray = new JSONArray();
        collectNativeTracingData(this.traceEvents.get(Integer.parseInt(str)), jSONArray);
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("data", jSONArray);
            jSONObject.put("method", "WxDebug.sendTracingData");
            jSONObject.put("params", jSONObject2);
            WXDebugBridge.getInstance().sendToRemote(jSONObject.toString());
        } catch (Throwable th) {
            th.printStackTrace();
        }
        WXDebugBridge.getInstance().post(new Runnable() {
            public void run() {
                WXTracingAdapter.this.sendSummaryInfo(String.valueOf(str));
            }
        });
        WXLogUtils.d("WXTracingAdapter", "Send tracing data with instance id " + str);
    }

    private void collectNativeTracingData(WXTracing.TraceEvent traceEvent, JSONArray jSONArray) {
        if (traceEvent.subEvents != null) {
            for (int i = 0; i < traceEvent.subEvents.size(); i++) {
                WXTracing.TraceEvent valueAt = traceEvent.subEvents.valueAt(i);
                if (!valueAt.isSegment) {
                    "domBatch".equals(valueAt.fname);
                    JSONObject parseToJSONObject = parseToJSONObject(valueAt);
                    if ("JSThread".equals(valueAt.tname)) {
                        try {
                            parseToJSONObject.put("duration", valueAt.parseJsonTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jSONArray.put(parseToJSONObject);
                    collectNativeTracingData(valueAt, jSONArray);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendSummaryInfo(String str) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            WXPerformance wXPerformance = sDKInstance.getWXPerformance();
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("platform", TimeCalculator.PLATFORM_ANDROID);
                jSONObject.put("JSTemplateSize", wXPerformance.JSTemplateSize);
                jSONObject.put("screenRenderTime", wXPerformance.screenRenderTime);
                jSONObject.put(ImageStatistics.KEY_TOTAL_TIME, wXPerformance.totalTime);
                jSONObject.put("networkTime", wXPerformance.networkTime);
                HealthReport traverse = new DomTracker(sDKInstance).traverse();
                if (traverse != null) {
                    jSONObject.put("maxDeepViewLayer", traverse.maxLayer);
                    jSONObject.put("componentCount", traverse.componentCount);
                }
                JSONObject jSONObject2 = new JSONObject();
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("summaryInfo", jSONObject);
                jSONObject2.put("method", "WxDebug.sendSummaryInfo");
                jSONObject2.put("params", jSONObject3);
                LogUtil.d("SummaryInfo", jSONObject.toString());
                WXDebugBridge.getInstance().sendToRemote(jSONObject2.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            WXLogUtils.e("WXTracing", "Instance " + str + " not found");
        }
    }

    private JSONObject parseToJSONObject(WXTracing.TraceEvent traceEvent) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("parentId", traceEvent.parentId);
            jSONObject.put("ref", traceEvent.ref);
            jSONObject.put("parentRef", traceEvent.parentRef);
            jSONObject.put(PushClientConstants.TAG_CLASS_NAME, traceEvent.classname);
            jSONObject.put("ts", traceEvent.ts);
            jSONObject.put("traceId", traceEvent.traceId);
            jSONObject.put("iid", traceEvent.iid);
            jSONObject.put("duration", traceEvent.duration);
            jSONObject.put("fName", traceEvent.fname);
            jSONObject.put("ph", traceEvent.ph);
            jSONObject.put("name", traceEvent.name);
            jSONObject.put("tName", traceEvent.tname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
