package com.taobao.android.dxcontainer;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.BuildConfig;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.thread.DXMonitorRunnable;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dxcontainer.DXContainerError;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.weex.el.parse.Operators;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXContainerAppMonitor {
    public static final int DX_CONTAINER_LOG_UPLOAD_LEVEL_ALL = 3;
    public static final int DX_CONTAINER_LOG_UPLOAD_LEVEL_NONE = 0;
    public static final int DX_CONTAINER_LOG_UPLOAD_LEVEL_T_LOG = 2;
    public static final int DX_CONTAINER_LOG_UPLOAD_LEVEL_UT = 1;
    private static final String DX_MONITOR_PAGE = "Page_DXContainer";
    private static final String DX_MONITOR_POINT = "DXContainer";
    private static final String DX_MONITOR_TAG = "DXContainer";
    private static final String DX_MONITOR_VERSION = "1.0";
    private static final String TAG = "DinamicXContainer";
    /* access modifiers changed from: private */
    public static IDXContainerAppMonitor dxcAppMonitor;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXContainerMonitorLevel {
    }

    public static void setDxcAppMonitor(IDXContainerAppMonitor iDXContainerAppMonitor) {
        dxcAppMonitor = iDXContainerAppMonitor;
    }

    public static void trackerInfo(int i, @NonNull String str, String str2, DXContainerModel dXContainerModel, Map<String, String> map) {
        if (i != 0 && str2 != null) {
            final int i2 = i;
            final String str3 = str;
            final String str4 = str2;
            final DXContainerModel dXContainerModel2 = dXContainerModel;
            final Map<String, String> map2 = map;
            DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                public void run() {
                    if (DXContainerAppMonitor.dxcAppMonitor != null) {
                        boolean z = true;
                        if (((i2 & 1) == 1) && DXContainerAppMonitor.getFailSampleResult() && !DinamicXEngine.isDebug()) {
                            DXContainerAppMonitor.dxcAppMonitor.alarm_commitSuccess(DXContainerAppMonitor.DX_MONITOR_PAGE, "DXContainer", DXContainerAppMonitor.createArg(str3, str4, dXContainerModel2, map2).toString());
                        }
                        if ((i2 & 2) != 2) {
                            z = false;
                        }
                        if (z) {
                            DXRemoteLog.remoteLogi("DXContainer", "DXContainer", DXContainerAppMonitor.buildRemoteLogContent(str3, str4, dXContainerModel2, map2, ""));
                        }
                    }
                }
            });
        }
    }

    public static void trackerError(DXContainerError dXContainerError) {
        trackerError(dXContainerError, (DXContainerModel) null);
    }

    public static void trackerError(final DXContainerError dXContainerError, final DXContainerModel dXContainerModel) {
        try {
            if (dxcAppMonitor != null && dXContainerError != null && dXContainerError.bizType != null && dXContainerError.dxErrorInfoList != null) {
                if (dXContainerError.dxErrorInfoList.size() > 0) {
                    DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                        public void run() {
                            String str = dXContainerError.bizType;
                            List<DXContainerError.DXContainerErrorInfo> list = dXContainerError.dxErrorInfoList;
                            int size = list.size();
                            for (int i = 0; i < size; i++) {
                                DXContainerError.DXContainerErrorInfo dXContainerErrorInfo = list.get(i);
                                if (dXContainerErrorInfo != null && !TextUtils.isEmpty(dXContainerErrorInfo.serviceId)) {
                                    if (dXContainerErrorInfo.extraParams == null) {
                                        dXContainerErrorInfo.extraParams = new HashMap();
                                    }
                                    DXContainerAppMonitor.trackerError(str, dXContainerModel, dXContainerErrorInfo.serviceId, dXContainerErrorInfo.extraParams, dXContainerErrorInfo.code, dXContainerErrorInfo.reason, dXContainerErrorInfo.timeStamp);
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

    public static void trackerError(String str, DXContainerModel dXContainerModel, String str2, int i, String str3) {
        try {
            DXContainerError dXContainerError = new DXContainerError(str);
            dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(str2, i, str3));
            trackerError(dXContainerError, dXContainerModel);
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }

    public static void trackerError(String str, DXContainerModel dXContainerModel, String str2, int i, String str3, Map<String, String> map) {
        try {
            DXContainerError dXContainerError = new DXContainerError(str);
            DXContainerError.DXContainerErrorInfo dXContainerErrorInfo = new DXContainerError.DXContainerErrorInfo(str2, i, str3);
            dXContainerErrorInfo.extraParams = map;
            dXContainerError.dxErrorInfoList.add(dXContainerErrorInfo);
            trackerError(dXContainerError, dXContainerModel);
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }

    public static void trackerCount(String str, String str2, double d) {
        if (dxcAppMonitor != null) {
            dxcAppMonitor.counter_commit(DX_MONITOR_PAGE, str, str2, d);
        }
    }

    /* access modifiers changed from: private */
    public static void trackerError(@NonNull String str, DXContainerModel dXContainerModel, @NonNull String str2, Map<String, String> map, int i, String str3, long j) {
        JSONObject createArg = createArg(str, str2, dXContainerModel, map);
        if (createArg != null) {
            createArg.put("timeStamp", (Object) Long.valueOf(j));
            if (str3 != null) {
                createArg.put(ILocatable.ERROR_MSG, (Object) str3);
            }
        }
        if (!DinamicXEngine.isDebug()) {
            IDXContainerAppMonitor iDXContainerAppMonitor = dxcAppMonitor;
            String jSONString = createArg.toJSONString();
            iDXContainerAppMonitor.alarm_commitFail(DX_MONITOR_PAGE, "DXContainer", jSONString, i + "", str3);
        }
        DXRemoteLog.remoteLoge("DXContainer", "DXContainer", buildRemoteLogContent(str, str2, dXContainerModel, map, "errorCode:" + i + "_errorMsg:" + str3));
    }

    /* access modifiers changed from: private */
    public static String buildRemoteLogContent(String str, String str2, DXContainerModel dXContainerModel, Map<String, String> map, String str3) {
        DXTemplateItem templateItem;
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        sb.append(str);
        sb.append("]:");
        sb.append(str2);
        sb.append("|");
        JSONObject jSONObject = new JSONObject();
        if (!(dXContainerModel == null || (templateItem = dXContainerModel.getTemplateItem()) == null)) {
            jSONObject.put("template", (Object) templateItem.name);
            jSONObject.put("version", (Object) Long.valueOf(templateItem.version));
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
    public static JSONObject createArg(String str, String str2, DXContainerModel dXContainerModel, Map<String, String> map) {
        DXTemplateItem templateItem;
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ProtocolConst.KEY_BIZNAME, (Object) "DXContainer1.0");
        if (!TextUtils.isEmpty(str)) {
            jSONObject.put("sceneName", (Object) str);
        }
        if (!TextUtils.isEmpty(str2)) {
            jSONObject.put("serviceId", (Object) str2);
        } else {
            jSONObject.put("serviceId", (Object) DXContainerErrorConstant.DXC_DEFAULT_SERVICE_ID);
        }
        String featureType = getFeatureType(str2);
        if (!TextUtils.isEmpty(featureType)) {
            jSONObject.put("featureType", (Object) featureType);
        }
        jSONObject.put("dxVersion", (Object) BuildConfig.DINAMICX_SDK_VERSION);
        jSONObject.put("dxcVersion", (Object) BuildConfig.DXCONTAINER_SDK_VERSION);
        jSONObject.put("samplingRate", (Object) "1.0");
        if (!(dXContainerModel == null || (templateItem = dXContainerModel.getTemplateItem()) == null)) {
            if (!TextUtils.isEmpty(templateItem.name)) {
                jSONObject.put("templateName", (Object) templateItem.name);
            }
            jSONObject.put("templateVersion", (Object) templateItem.version + "");
            if (!TextUtils.isEmpty(templateItem.templateUrl)) {
                jSONObject.put("templateUrl", (Object) templateItem.templateUrl);
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.DXContainerAppMonitor.getFeatureType(java.lang.String):java.lang.String");
    }

    public static boolean getFailSampleResult() {
        return 0.001d > Math.random();
    }

    public static void logi(String str) {
        Log.i(TAG, str);
    }
}
