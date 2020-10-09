package com.taobao.android.dinamicx.monitor;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.thread.DXMonitorRunnable;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.HashMap;
import java.util.Map;

public class DXUmbrellaUtil {
    private static final String FEATURE_END = "_umbrella2";
    /* access modifiers changed from: private */
    public static DXAbsUmbrella umbrellaImpl;

    /* access modifiers changed from: private */
    public static String getVersion() {
        return DXMonitorConstant.DX_MONITOR_VERSION;
    }

    public static void setUmbrellaImpl(DXAbsUmbrella dXAbsUmbrella) {
        umbrellaImpl = dXAbsUmbrella;
    }

    public static void commitSuccess(int i, @NonNull String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map, double d) {
        if (umbrellaImpl != null) {
            final String str4 = str2;
            final String str5 = str3;
            final String str6 = str;
            final DXTemplateItem dXTemplateItem2 = dXTemplateItem;
            final Map<String, String> map2 = map;
            DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                public void run() {
                    try {
                        String access$000 = DXUmbrellaUtil.buildFeatureType(str4);
                        DXUmbrellaUtil.umbrellaImpl.commitSuccess(str4, str5, DXUmbrellaUtil.getVersion(), "DinamicX", str6, DXUmbrellaUtil.createArg(str6, access$000, str5, dXTemplateItem2, map2));
                    } catch (Throwable th) {
                        DXExceptionUtil.printStack(th);
                    }
                }
            });
        }
    }

    public static void commitError(final DXError dXError, final boolean z) {
        if (umbrellaImpl != null) {
            DXRunnableManager.runForMonitor(new DXMonitorRunnable() {
                public void run() {
                    try {
                        if (dXError != null && dXError.dxErrorInfoList != null) {
                            if (!dXError.dxErrorInfoList.isEmpty()) {
                                int size = dXError.dxErrorInfoList.size();
                                int i = size - 1;
                                for (int i2 = 0; i2 < size; i2++) {
                                    DXError.DXErrorInfo dXErrorInfo = dXError.dxErrorInfoList.get(i2);
                                    if (dXErrorInfo != null) {
                                        if (z) {
                                            dXErrorInfo.featureType = "SimplePipeline" + dXErrorInfo.featureType;
                                        }
                                        if (i2 == i) {
                                            DXUmbrellaUtil.logError(dXError.biztype, dXError.dxTemplateItem, dXErrorInfo);
                                            DXUmbrellaUtil.commitFail(dXError.biztype, dXError.dxTemplateItem, dXErrorInfo);
                                            return;
                                        }
                                        DXUmbrellaUtil.logError(dXError.biztype, dXError.dxTemplateItem, dXErrorInfo);
                                    }
                                }
                            }
                        }
                    } catch (Throwable th) {
                        DXExceptionUtil.printStack(th);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void commitFail(String str, DXTemplateItem dXTemplateItem, DXError.DXErrorInfo dXErrorInfo) {
        if (dXErrorInfo != null) {
            String buildFeatureType = buildFeatureType(dXErrorInfo.featureType);
            String str2 = dXErrorInfo.serviceId;
            String version = getVersion();
            Map<String, String> createArg = createArg(str, buildFeatureType, dXErrorInfo.serviceId, dXTemplateItem, dXErrorInfo.extraParams);
            String str3 = "" + dXErrorInfo.code;
            String str4 = dXErrorInfo.reason;
            if (createArg != null) {
                createArg.put(ILocatable.ERROR_MSG, str4);
                createArg.put("errorCode", str3);
            }
            umbrellaImpl.commitFailure(buildFeatureType, str2, version, "DinamicX", str, createArg, str3, str4);
        }
    }

    /* access modifiers changed from: private */
    public static void logError(String str, DXTemplateItem dXTemplateItem, DXError.DXErrorInfo dXErrorInfo) {
        if (dXErrorInfo != null) {
            String buildFeatureType = buildFeatureType(dXErrorInfo.featureType);
            String str2 = "" + dXErrorInfo.code;
            String str3 = dXErrorInfo.reason;
            Map<String, Object> buildUmDimMap = buildUmDimMap(dXTemplateItem, dXErrorInfo);
            HashMap hashMap = new HashMap();
            Map<String, String> createArg = createArg(str, buildFeatureType, dXErrorInfo.serviceId, dXTemplateItem, dXErrorInfo.extraParams);
            if (createArg != null) {
                createArg.put(ILocatable.ERROR_MSG, str3);
                createArg.put("errorCode", str2);
            }
            hashMap.put("args", createArg);
            umbrellaImpl.logError("DinamicX", str, buildFeatureType, (String) null, str2, str3, buildUmDimMap, hashMap);
        }
    }

    private static Map<String, Object> buildUmDimMap(DXTemplateItem dXTemplateItem, DXError.DXErrorInfo dXErrorInfo) {
        HashMap hashMap = new HashMap();
        if (dXTemplateItem != null) {
            hashMap.put(DXUmDimKeyConstant.UMB_21, dXTemplateItem.name);
            hashMap.put(DXUmDimKeyConstant.UMB_22, Long.valueOf(dXTemplateItem.version));
            hashMap.put(DXUmDimKeyConstant.UMB_23, dXTemplateItem.templateUrl);
        }
        if (dXErrorInfo != null) {
            hashMap.put(DXUmDimKeyConstant.UMB_24, dXErrorInfo.serviceId);
        }
        return hashMap;
    }

    /* access modifiers changed from: private */
    public static String buildFeatureType(String str) {
        return str + FEATURE_END;
    }

    /* access modifiers changed from: private */
    public static Map<String, String> createArg(String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map) {
        HashMap hashMap = new HashMap();
        hashMap.put(ProtocolConst.KEY_BIZNAME, "DinamicX");
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("sceneName", str);
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("serviceId", str3);
        } else {
            hashMap.put("serviceId", DXMonitorConstant.DX_DEFAULT_SERVICE_ID);
        }
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("featureType", str2);
        }
        hashMap.put("version", getVersion());
        hashMap.put("samplingRate", "1.0");
        if (dXTemplateItem != null) {
            if (!TextUtils.isEmpty(dXTemplateItem.name)) {
                hashMap.put("templateName", dXTemplateItem.name);
            }
            hashMap.put("templateVersion", dXTemplateItem.version + "");
            if (!TextUtils.isEmpty(dXTemplateItem.templateUrl)) {
                hashMap.put("templateUrl", dXTemplateItem.templateUrl);
            }
        }
        if (map != null && map.size() > 0) {
            for (Map.Entry next : map.entrySet()) {
                if (!(next == null || next.getKey() == null || next.getValue() == null)) {
                    hashMap.put(next.getKey(), next.getValue());
                }
            }
        }
        return hashMap;
    }
}
