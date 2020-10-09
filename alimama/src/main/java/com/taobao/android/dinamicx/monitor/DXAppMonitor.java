package com.taobao.android.dinamicx.monitor;

import alimama.com.unweventparse.constants.EventConstants;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.thread.DXMonitorRunnable;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXAppMonitor {
    /* access modifiers changed from: private */
    public static IDXAppMonitor dxAppMonitor;
    private static int monitorLevel;

    private static String getVersion() {
        return DXMonitorConstant.DX_MONITOR_VERSION;
    }

    public static void setDxAppMonitor(IDXAppMonitor iDXAppMonitor) {
        dxAppMonitor = iDXAppMonitor;
    }

    public static void setMonitorLevel(int i) {
        monitorLevel = i;
    }

    public static void trackerPerform(int i, @NonNull String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map, double d, boolean z) {
        trackerAppMonitorPerform(i, str, str2, str3, dXTemplateItem, map, d, z);
        trackerUmSuccess(i, str, str2, str3, dXTemplateItem, map, d);
    }

    private static void trackerUmSuccess(int i, @NonNull String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map, double d) {
        DXUmbrellaUtil.commitSuccess(i, str, str2, str3, dXTemplateItem, map, d);
    }

    private static void trackerAppMonitorPerform(int i, @NonNull String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map, double d, boolean z) {
        try {
            if (monitorLevel == i) {
                final String str4 = str3;
                final DXTemplateItem dXTemplateItem2 = dXTemplateItem;
                final Map<String, String> map2 = map;
                final String str5 = str2;
                final String str6 = str;
                final double d2 = d;
                final boolean z2 = z;
                DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                    public void run() {
                        String str;
                        if (str4 != null) {
                            if (dXTemplateItem2 == null) {
                                str = "";
                            } else {
                                str = dXTemplateItem2.name + "_:" + dXTemplateItem2.version;
                            }
                            if (!DXMonitorConstant.DX_MONITOR_SERVICE_ID_DETAIL_RENDER_CREATE_VIEW_ONCE.equals(str4) && !DXMonitorConstant.DX_MONITOR_SERVICE_ID_DETAIL_RENDER_RENDER_VIEW_ONCE.equals(str4)) {
                                DXLog.performLog(str5, Operators.ARRAY_START_STR + str6 + "]：" + str5 + "性能埋点: " + str4 + ": " + (d2 / 1000000.0d) + "ms" + " templateinfo: " + str);
                            } else if (map2 != null && map2.containsKey(DXMonitorConstant.DX_MONITOR_SERVICE_ID_KEY_VIEW_SIMPLE_NAME)) {
                                DXLog.performLog(str5, Operators.ARRAY_START_STR + str6 + "]：" + str5 + "性能埋点: " + str4 + ": " + Operators.ARRAY_START_STR + ((String) map2.get(DXMonitorConstant.DX_MONITOR_SERVICE_ID_KEY_VIEW_SIMPLE_NAME)) + "]:" + (d2 / 1000000.0d) + "ms" + " templateinfo: " + str);
                            }
                            if (DXAppMonitor.dxAppMonitor != null) {
                                if (DXAppMonitor.getFailSampleResult() && !DinamicXEngine.isDebug()) {
                                    JSONObject access$100 = DXAppMonitor.createArg(str6, str5, str4, dXTemplateItem2, map2);
                                    DXAppMonitor.dxAppMonitor.alarm_commitSuccess(DXMonitorConstant.DX_MONITOR_PAGE, "DinamicX", access$100.toString());
                                    if (d2 > 0.0d) {
                                        DXAppMonitor.dxAppMonitor.counter_commit(DXMonitorConstant.DX_MONITOR_PAGE, "DinamicX", access$100.toString(), d2 / 1000000.0d);
                                    }
                                }
                                if (z2) {
                                    DXRemoteLog.remoteLogi("DinamicX", "DinamicX", DXAppMonitor.buildRemoteLogContent(str6, str4, dXTemplateItem2, map2, ""));
                                }
                            }
                        }
                    }
                });
            }
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }

    public static void trackerAsyncRender(int i, @NonNull final String str, final String str2, @NonNull final String str3, final Map<String, String> map) {
        try {
            if (monitorLevel == i) {
                DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                    public void run() {
                        if (DXAppMonitor.getFailSampleResult() && !DinamicXEngine.isDebug()) {
                            JSONObject unused = DXAppMonitor.createArg(str, str2, str3, (DXTemplateItem) null, map);
                        }
                    }
                });
            }
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }

    public static void trackerError(String str, DXTemplateItem dXTemplateItem, String str2, String str3, int i, String str4) {
        try {
            DXError dXError = new DXError(str);
            dXError.dxTemplateItem = dXTemplateItem;
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(str2, str3, i);
            dXErrorInfo.reason = str4;
            dXError.dxErrorInfoList.add(dXErrorInfo);
            trackerError(dXError);
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }

    public static void trackerError(@NonNull DXError dXError) {
        trackerError(dXError, false);
    }

    public static void trackerError(@NonNull DXError dXError, boolean z) {
        trackerAppMonitorError(dXError, z);
        trackerUmbrellaError(dXError, z);
    }

    private static void trackerUmbrellaError(@NonNull DXError dXError, boolean z) {
        DXUmbrellaUtil.commitError(dXError, z);
    }

    private static void trackerAppMonitorError(@NonNull final DXError dXError, final boolean z) {
        try {
            if (dxAppMonitor != null && dXError != null && dXError.biztype != null && dXError.dxErrorInfoList != null) {
                if (dXError.dxErrorInfoList.size() > 0) {
                    DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                        public void run() {
                            String str = dXError.biztype;
                            List<DXError.DXErrorInfo> list = dXError.dxErrorInfoList;
                            int size = list.size();
                            for (int i = 0; i < size; i++) {
                                DXError.DXErrorInfo dXErrorInfo = list.get(i);
                                if (dXErrorInfo != null && !TextUtils.isEmpty(dXErrorInfo.serviceId)) {
                                    if (dXErrorInfo.extraParams == null) {
                                        dXErrorInfo.extraParams = new HashMap();
                                    }
                                    dXErrorInfo.extraParams.put(EventConstants.EVENTID, dXError.getErrorId());
                                    if (z) {
                                        dXErrorInfo.featureType = "SimplePipeline" + dXErrorInfo.featureType;
                                    }
                                    DXAppMonitor.trackerError(str, dXError.dxTemplateItem, dXErrorInfo.featureType, dXErrorInfo.serviceId, dXErrorInfo.extraParams, dXErrorInfo.code, dXErrorInfo.reason, dXErrorInfo.timeStamp);
                                }
                            }
                        }
                    });
                }
            }
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }

    /* access modifiers changed from: private */
    public static void trackerError(@NonNull String str, DXTemplateItem dXTemplateItem, String str2, @NonNull String str3, Map<String, String> map, int i, String str4, long j) {
        JSONObject createArg = createArg(str, str2, str3, dXTemplateItem, map);
        if (createArg != null) {
            createArg.put("timeStamp", (Object) Long.valueOf(j));
            if (str4 != null) {
                createArg.put(ILocatable.ERROR_MSG, (Object) str4);
            }
        }
        if (!DinamicXEngine.isDebug()) {
            IDXAppMonitor iDXAppMonitor = dxAppMonitor;
            String jSONString = createArg.toJSONString();
            iDXAppMonitor.alarm_commitFail(DXMonitorConstant.DX_MONITOR_PAGE, "DinamicX", jSONString, i + "", str4);
        }
        DXRemoteLog.remoteLoge("DinamicX", "DinamicX", buildRemoteLogContent(str, str3, dXTemplateItem, map, "errorCode:" + i + "_errorMsg:" + str4));
    }

    /* access modifiers changed from: private */
    public static String buildRemoteLogContent(String str, String str2, DXTemplateItem dXTemplateItem, Map<String, String> map, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        sb.append(str);
        sb.append("]:");
        sb.append(str2);
        sb.append("|");
        JSONObject jSONObject = new JSONObject();
        if (dXTemplateItem != null) {
            jSONObject.put("template", (Object) dXTemplateItem.name);
            jSONObject.put("version", (Object) Long.valueOf(dXTemplateItem.version));
        }
        if (map != null && map.size() > 0) {
            for (Map.Entry next : map.entrySet()) {
                jSONObject.put((String) next.getKey(), next.getValue());
            }
        }
        if (str3 != null) {
            jSONObject.put("error", (Object) str3);
        }
        sb.append(jSONObject.toJSONString());
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static JSONObject createArg(String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ProtocolConst.KEY_BIZNAME, (Object) "DinamicX");
        if (!TextUtils.isEmpty(str)) {
            jSONObject.put("sceneName", (Object) str);
        }
        if (!TextUtils.isEmpty(str3)) {
            jSONObject.put("serviceId", (Object) str3);
        } else {
            jSONObject.put("serviceId", (Object) DXMonitorConstant.DX_DEFAULT_SERVICE_ID);
        }
        if (!TextUtils.isEmpty(str2)) {
            jSONObject.put("featureType", (Object) str2);
        }
        jSONObject.put("version", (Object) getVersion());
        jSONObject.put("samplingRate", (Object) "1.0");
        if (dXTemplateItem != null) {
            if (!TextUtils.isEmpty(dXTemplateItem.name)) {
                jSONObject.put("templateName", (Object) dXTemplateItem.name);
            }
            jSONObject.put("templateVersion", (Object) dXTemplateItem.version + "");
            if (!TextUtils.isEmpty(dXTemplateItem.templateUrl)) {
                jSONObject.put("templateUrl", (Object) dXTemplateItem.templateUrl);
            }
        }
        if (map != null && map.size() > 0) {
            for (Map.Entry next : map.entrySet()) {
                if (!(next == null || next.getKey() == null || next.getValue() == null)) {
                    jSONObject.put((String) next.getKey(), next.getValue());
                }
            }
        }
        return jSONObject;
    }

    private static StringBuilder appendStr(StringBuilder sb, String str, String str2) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append(String.format("%s=%s", new Object[]{str, str2}));
        return sb;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        r1 = r2.indexOf("_");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFeatureType(java.lang.String r2) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x0020
            java.lang.String r0 = "_"
            boolean r0 = r2.contains(r0)
            if (r0 != 0) goto L_0x000f
            goto L_0x0020
        L_0x000f:
            r0 = 0
            java.lang.String r1 = "_"
            int r1 = r2.indexOf(r1)
            if (r1 <= 0) goto L_0x001d
            java.lang.String r2 = r2.substring(r0, r1)
            return r2
        L_0x001d:
            java.lang.String r2 = ""
            return r2
        L_0x0020:
            java.lang.String r2 = ""
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.monitor.DXAppMonitor.getFeatureType(java.lang.String):java.lang.String");
    }

    public static int getMonitorLevel() {
        return monitorLevel;
    }

    public static boolean getFailSampleResult() {
        return 0.001d > Math.random();
    }
}
