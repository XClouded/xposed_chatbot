package com.alibaba.aliweex.adapter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.aliweex.utils.WXUriUtil;
import com.taobao.monitor.performance.APMAdapterFactoryProxy;
import com.taobao.monitor.performance.IWXApmAdapter;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.performance.IWXApmMonitorAdapter;
import com.taobao.weex.performance.WXInstanceApm;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class WXAPMAdapter implements IWXApmMonitorAdapter {
    private static boolean sAliHaClassFound = true;
    private static int sDeviceLevel = -2;
    public static boolean showPerformanceInRelease = false;
    public String instanceId;
    private IWXApmAdapter mAliHaAdapter;
    private String mPageName;
    /* access modifiers changed from: private */
    public TextView mShowTextView;
    private Map<String, Double> mStatsMap = new ConcurrentHashMap();

    public void onSubProcedureEvent(String str, String str2) {
    }

    public void onSubProcedureStage(String str, String str2) {
    }

    public void setSubProcedureProperties(String str, String str2, Object obj) {
    }

    public void setSubProcedureStats(String str, String str2, double d) {
    }

    public void onStart(String str) {
        this.instanceId = str;
        this.mAliHaAdapter = APMAdapterFactoryProxy.instance().createApmAdapter();
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.onStart(str);
            recordDeviceLevel();
        }
    }

    public void onEnd() {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.onEnd();
        }
    }

    public void onEvent(String str, Object obj) {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.onEvent(str, obj);
        }
    }

    public void onStage(String str, long j) {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.onStage(str, j);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("weexInstanceId", TextUtils.isEmpty(this.instanceId) ? "nullId" : this.instanceId);
        if (!TextUtils.isEmpty(this.mPageName)) {
            hashMap.put("mPageName", this.mPageName);
        }
        AliWeex.getInstance().onStage(str, hashMap);
        if (showPerformanceInRelease) {
            showPerformanceUiOnInstance();
        }
    }

    public void addProperty(String str, Object obj) {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.addProperty(str, obj);
            if (showPerformanceInRelease && WXInstanceApm.KEY_PAGE_PROPERTIES_BIZ_ID.equals(str)) {
                this.mPageName = obj.toString();
            }
        }
    }

    public void addStats(String str, double d) {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.addStatistic(str, d);
            if (showPerformanceInRelease) {
                this.mStatsMap.put(str, Double.valueOf(d));
                if (WXInstanceApm.KEY_PAGE_STATS_LAYOUT_TIME.equals(str)) {
                    writeInfoToTmqTask();
                }
            }
        }
    }

    public void onAppear() {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.onStart();
        }
    }

    public void onDisappear() {
        if (this.mAliHaAdapter != null) {
            this.mAliHaAdapter.onStop();
        }
    }

    public String parseReportUrl(String str) {
        String realNameFromNameOrUrl = WXUriUtil.getRealNameFromNameOrUrl(str, false);
        return TextUtils.isEmpty(realNameFromNameOrUrl) ? "emptyParseUrl" : realNameFromNameOrUrl;
    }

    private void recordDeviceLevel() {
        int i;
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null && Boolean.valueOf(configAdapter.getConfig(WXInitConfigManager.WXAPM_CONFIG_GROUP, "collectDeviceLevel", "true")).booleanValue()) {
            if (sDeviceLevel == -2) {
                if (sAliHaClassFound) {
                    try {
                        AliHAHardware.OutlineInfo outlineInfo = AliHAHardware.getInstance().getOutlineInfo();
                        if (outlineInfo == null) {
                            i = -1;
                        } else {
                            i = outlineInfo.deviceLevel;
                        }
                        sDeviceLevel = i;
                    } catch (Throwable unused) {
                        sAliHaClassFound = false;
                        sDeviceLevel = -1;
                    }
                } else {
                    sDeviceLevel = -1;
                }
            }
            addProperty("wxDeviceLevel", Integer.valueOf(sDeviceLevel + 1));
        }
    }

    private void showPerformanceUiOnInstance() {
        WXSDKInstance wXSDKInstance;
        if (showPerformanceInRelease && (wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(this.instanceId)) != null) {
            final View containerView = wXSDKInstance.getContainerView();
            if (containerView instanceof FrameLayout) {
                containerView.post(new Runnable() {
                    public void run() {
                        FrameLayout frameLayout = (FrameLayout) containerView;
                        if (WXAPMAdapter.this.mShowTextView == null) {
                            TextView unused = WXAPMAdapter.this.mShowTextView = new TextView(frameLayout.getContext());
                            WXAPMAdapter.this.mShowTextView.setClickable(false);
                            WXAPMAdapter.this.mShowTextView.setFocusable(false);
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                            layoutParams.gravity = 21;
                            WXAPMAdapter.this.mShowTextView.setLayoutParams(layoutParams);
                            frameLayout.addView(WXAPMAdapter.this.mShowTextView);
                        }
                        WXAPMAdapter.this.mShowTextView.setText(WXAPMAdapter.this.getShowText());
                        WXAPMAdapter.this.mShowTextView.setTextSize(10.0f);
                        if (frameLayout.getChildAt(frameLayout.getChildCount() - 1) != WXAPMAdapter.this.mShowTextView) {
                            ViewParent parent = WXAPMAdapter.this.mShowTextView.getParent();
                            if (parent instanceof ViewGroup) {
                                ((ViewGroup) parent).removeView(WXAPMAdapter.this.mShowTextView);
                            }
                            frameLayout.addView(WXAPMAdapter.this.mShowTextView);
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public String getShowText() {
        Object obj;
        Object obj2;
        if (WXSDKManager.getInstance().getAllInstanceMap().get(this.instanceId) == null) {
            return "";
        }
        TimeInfo infoFromStage = getInfoFromStage("downLoad", WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_START, WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_END);
        TimeInfo infoFromStage2 = getInfoFromStage("interactive", WXInstanceApm.KEY_PAGE_STAGES_RENDER_ORGIGIN, WXInstanceApm.KEY_PAGE_STAGES_INTERACTION);
        TimeInfo infoFromStage3 = getInfoFromStage(WXInstanceApm.KEY_PAGE_STAGES_END_EXCUTE_BUNDLE, WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_END, WXInstanceApm.KEY_PAGE_STAGES_END_EXCUTE_BUNDLE);
        StringBuilder sb = new StringBuilder();
        sb.append("dowlnLoad:");
        sb.append(infoFromStage == null ? "" : Long.valueOf(infoFromStage.cost));
        sb.append("\n evalJsBundle:");
        if (infoFromStage3 == null) {
            obj = "";
        } else {
            obj = Long.valueOf(infoFromStage3.cost);
        }
        sb.append(obj);
        sb.append("\n interaction:");
        if (infoFromStage2 == null) {
            obj2 = "";
        } else {
            obj2 = Long.valueOf(infoFromStage2.cost);
        }
        sb.append(obj2);
        return sb.toString();
    }

    private class TimeInfo {
        long cost;
        long end;
        String name;
        long start;

        private TimeInfo() {
        }

        /* access modifiers changed from: private */
        public JSONObject toJson() throws JSONException {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("stageName", this.name);
            jSONObject.put("beginTime", this.start);
            jSONObject.put("endTime", this.end);
            jSONObject.put("cost", this.cost);
            return jSONObject;
        }
    }

    private TimeInfo getInfoFromStage(String str, String str2, String str3) {
        long j;
        WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(this.instanceId);
        if (wXSDKInstance == null) {
            return null;
        }
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.name = str;
        Long l = wXSDKInstance.getApmForInstance().stageMap.get(str3);
        Long l2 = wXSDKInstance.getApmForInstance().stageMap.get(str2);
        long j2 = -1;
        if (l2 == null) {
            j = -1;
        } else {
            j = l2.longValue();
        }
        timeInfo.start = j;
        if (l != null) {
            j2 = l.longValue();
        }
        timeInfo.end = j2;
        timeInfo.cost = timeInfo.end - timeInfo.start;
        return timeInfo;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x0157 */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x014e A[SYNTHETIC, Splitter:B:44:0x014e] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0154 A[SYNTHETIC, Splitter:B:47:0x0154] */
    /* JADX WARNING: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeInfoToTmqTask() {
        /*
            r10 = this;
            java.lang.String r0 = android.os.Environment.getExternalStorageState()     // Catch:{ Exception -> 0x0158 }
            java.lang.String r1 = "mounted"
            boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x0158 }
            r1 = 0
            if (r0 == 0) goto L_0x0012
            java.io.File r0 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x0158 }
            goto L_0x0013
        L_0x0012:
            r0 = r1
        L_0x0013:
            if (r0 == 0) goto L_0x0038
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0158 }
            java.io.File r0 = r0.getAbsoluteFile()     // Catch:{ Exception -> 0x0158 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0158 }
            r3.<init>()     // Catch:{ Exception -> 0x0158 }
            android.app.Application r4 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ Exception -> 0x0158 }
            r3.append(r4)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "/APM_ONLINE"
            r3.append(r4)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0158 }
            r2.<init>(r0, r3)     // Catch:{ Exception -> 0x0158 }
            r0 = r2
        L_0x0038:
            if (r0 == 0) goto L_0x0040
            boolean r2 = r0.exists()     // Catch:{ Exception -> 0x0158 }
            if (r2 != 0) goto L_0x0043
        L_0x0040:
            r0.createNewFile()     // Catch:{ Exception -> 0x0158 }
        L_0x0043:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x0158 }
            r2.<init>()     // Catch:{ Exception -> 0x0158 }
            java.lang.String r3 = "type"
            java.lang.String r4 = "Weex"
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r3 = "url"
            java.lang.String r4 = r10.mPageName     // Catch:{ Exception -> 0x0158 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r3 = "pageName"
            java.lang.String r4 = r10.mPageName     // Catch:{ Exception -> 0x0158 }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x0158 }
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ Exception -> 0x0158 }
            r3.<init>()     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "stages"
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "downLoad"
            java.lang.String r5 = "wxStartDownLoadBundle"
            java.lang.String r6 = "wxEndDownLoadBundle"
            com.alibaba.aliweex.adapter.adapter.WXAPMAdapter$TimeInfo r4 = r10.getInfoFromStage(r4, r5, r6)     // Catch:{ Exception -> 0x0158 }
            if (r4 == 0) goto L_0x007c
            org.json.JSONObject r4 = r4.toJson()     // Catch:{ Exception -> 0x0158 }
            r3.put(r4)     // Catch:{ Exception -> 0x0158 }
        L_0x007c:
            java.lang.String r4 = "prePareBundle"
            java.lang.String r5 = "wxStartLoadBundle"
            java.lang.String r6 = "wxEndLoadBundle"
            com.alibaba.aliweex.adapter.adapter.WXAPMAdapter$TimeInfo r4 = r10.getInfoFromStage(r4, r5, r6)     // Catch:{ Exception -> 0x0158 }
            if (r4 == 0) goto L_0x0091
            org.json.JSONObject r4 = r4.toJson()     // Catch:{ Exception -> 0x0158 }
            r3.put(r4)     // Catch:{ Exception -> 0x0158 }
        L_0x0091:
            java.lang.String r4 = "evalJsBundle"
            java.lang.String r5 = "wxEndLoadBundle"
            java.lang.String r6 = "wxEndExecuteBundle"
            com.alibaba.aliweex.adapter.adapter.WXAPMAdapter$TimeInfo r4 = r10.getInfoFromStage(r4, r5, r6)     // Catch:{ Exception -> 0x0158 }
            if (r4 == 0) goto L_0x00a6
            org.json.JSONObject r4 = r4.toJson()     // Catch:{ Exception -> 0x0158 }
            r3.put(r4)     // Catch:{ Exception -> 0x0158 }
        L_0x00a6:
            java.lang.String r4 = "interactive"
            java.lang.String r5 = "wxEndExecuteBundle"
            java.lang.String r6 = "wxInteraction"
            com.alibaba.aliweex.adapter.adapter.WXAPMAdapter$TimeInfo r4 = r10.getInfoFromStage(r4, r5, r6)     // Catch:{ Exception -> 0x0158 }
            if (r4 == 0) goto L_0x00c3
            long r5 = r4.cost     // Catch:{ Exception -> 0x0158 }
            r7 = 0
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 <= 0) goto L_0x00c3
            org.json.JSONObject r4 = r4.toJson()     // Catch:{ Exception -> 0x0158 }
            r3.put(r4)     // Catch:{ Exception -> 0x0158 }
        L_0x00c3:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x0158 }
            r3.<init>()     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "extra"
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "mtopRequest"
            java.lang.String r5 = "wxJSAsyncDataStart"
            java.lang.String r6 = "wxJSAsyncDataEnd"
            com.alibaba.aliweex.adapter.adapter.WXAPMAdapter$TimeInfo r4 = r10.getInfoFromStage(r4, r5, r6)     // Catch:{ Exception -> 0x0158 }
            if (r4 == 0) goto L_0x00e4
            java.lang.String r5 = "mtopRequest"
            org.json.JSONObject r4 = r4.toJson()     // Catch:{ Exception -> 0x0158 }
            r3.put(r5, r4)     // Catch:{ Exception -> 0x0158 }
        L_0x00e4:
            java.lang.String r4 = "wxLayoutTime"
            java.util.Map<java.lang.String, java.lang.Double> r5 = r10.mStatsMap     // Catch:{ Exception -> 0x0158 }
            java.lang.String r6 = "wxLayoutTime"
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x0158 }
            r3.put(r4, r5)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "wxViewCost"
            java.util.Map<java.lang.String, java.lang.Double> r5 = r10.mStatsMap     // Catch:{ Exception -> 0x0158 }
            java.lang.String r6 = "wxViewCost"
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x0158 }
            r3.put(r4, r5)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "wxComponentCost"
            java.util.Map<java.lang.String, java.lang.Double> r5 = r10.mStatsMap     // Catch:{ Exception -> 0x0158 }
            java.lang.String r6 = "wxComponentCost"
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x0158 }
            r3.put(r4, r5)     // Catch:{ Exception -> 0x0158 }
            java.lang.String r4 = "wxExecJsCallBack"
            java.util.Map<java.lang.String, java.lang.Double> r5 = r10.mStatsMap     // Catch:{ Exception -> 0x0158 }
            java.lang.String r6 = "wxExecJsCallBack"
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x0158 }
            r3.put(r4, r5)     // Catch:{ Exception -> 0x0158 }
            java.io.BufferedWriter r3 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x0148 }
            java.io.OutputStreamWriter r4 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x0148 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0148 }
            r6 = 1
            r5.<init>(r0, r6)     // Catch:{ Exception -> 0x0148 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0148 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x0143, all -> 0x0140 }
            r3.write(r0)     // Catch:{ Exception -> 0x0143, all -> 0x0140 }
            r0 = 10
            r3.write(r0)     // Catch:{ Exception -> 0x0143, all -> 0x0140 }
            r3.close()     // Catch:{ Exception -> 0x015c }
            goto L_0x015c
        L_0x0140:
            r0 = move-exception
            r1 = r3
            goto L_0x0152
        L_0x0143:
            r0 = move-exception
            r1 = r3
            goto L_0x0149
        L_0x0146:
            r0 = move-exception
            goto L_0x0152
        L_0x0148:
            r0 = move-exception
        L_0x0149:
            r0.printStackTrace()     // Catch:{ all -> 0x0146 }
            if (r1 == 0) goto L_0x015c
            r1.close()     // Catch:{ Exception -> 0x015c }
            goto L_0x015c
        L_0x0152:
            if (r1 == 0) goto L_0x0157
            r1.close()     // Catch:{ Exception -> 0x0157 }
        L_0x0157:
            throw r0     // Catch:{ Exception -> 0x0158 }
        L_0x0158:
            r0 = move-exception
            r0.printStackTrace()
        L_0x015c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.adapter.WXAPMAdapter.writeInfoToTmqTask():void");
    }
}
