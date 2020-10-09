package com.alibaba.aliweex.adapter.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.R;
import com.alibaba.aliweex.utils.WXUriUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.utils.WXLogUtils;
import java.io.Serializable;
import java.util.Map;

public class WXUserTrackAdapter implements IWXUserTrackAdapter {
    private static final String TAG = "UserTrack";
    private static final String TAG_PERF_TEST = "Weex_Perf_Test";
    private static boolean initAppMonitor = false;
    private static boolean weexPerfLogIsOpen = false;
    private static String weexPerfLogSwitch = "false";

    public WXUserTrackAdapter() {
        weexPerfLogSwitch = AliWeex.getInstance().getApplication().getString(R.string.weex_performance_log_switch);
        if (TextUtils.equals(weexPerfLogSwitch, "true")) {
            weexPerfLogIsOpen = true;
        }
    }

    public void commit(Context context, String str, String str2, WXPerformance wXPerformance, Map<String, Serializable> map) {
        try {
            initAppMonitor();
            if ("load".equals(str2) && wXPerformance != null) {
                Uri parse = Uri.parse(wXPerformance.pageName);
                String scheme = parse.getScheme();
                String queryParameter = parse.getQueryParameter("spm");
                wXPerformance.pageName = WXUriUtil.handleUTPageNameScheme(wXPerformance.pageName);
                DimensionValueSet create = DimensionValueSet.create();
                Map<String, String> dimensionMap = wXPerformance.getDimensionMap();
                dimensionMap.put("scheme", scheme);
                dimensionMap.put("spm", queryParameter);
                MeasureValueSet create2 = MeasureValueSet.create();
                StringBuilder sb = new StringBuilder("维度埋点数据:");
                if (dimensionMap != null) {
                    if (map != null) {
                        dimensionMap.put("customMonitorInfo", JSON.toJSONString(map));
                    }
                    for (String next : dimensionMap.keySet()) {
                        create.setValue(next, dimensionMap.get(next));
                        if (WXEnvironment.isApkDebugable() || weexPerfLogIsOpen) {
                            sb.append(next);
                            sb.append(":");
                            sb.append(dimensionMap.get(next));
                            sb.append("||");
                        }
                    }
                }
                sb.append("指标埋点数据:");
                Map<String, Double> measureMap = wXPerformance.getMeasureMap();
                if (measureMap != null) {
                    for (String next2 : measureMap.keySet()) {
                        create2.setValue(next2, measureMap.get(next2).doubleValue());
                        if (WXEnvironment.isApkDebugable() || weexPerfLogIsOpen) {
                            sb.append(next2);
                            sb.append(":");
                            sb.append(measureMap.get(next2));
                            sb.append("||");
                        }
                    }
                }
                AppMonitor.Stat.commit("weex", "load", create, create2);
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d(TAG, sb.toString());
                } else if (weexPerfLogIsOpen) {
                    Log.d(TAG_PERF_TEST, sb.toString());
                }
            } else if ((!IWXUserTrackAdapter.DOM_MODULE.equals(str2) && !IWXUserTrackAdapter.JS_BRIDGE.equals(str2) && !WXEnvironment.ENVIRONMENT.equals(str2) && !IWXUserTrackAdapter.STREAM_MODULE.equals(str2) && !IWXUserTrackAdapter.JS_FRAMEWORK.equals(str2) && !IWXUserTrackAdapter.JS_DOWNLOAD.equals(str2)) || wXPerformance == null) {
                if (!IWXUserTrackAdapter.INVOKE_MODULE.equals(str2)) {
                    if (!IWXUserTrackAdapter.INIT_FRAMEWORK.equals(str2)) {
                        if (IWXUserTrackAdapter.COUNTER.equals(str2) && str != null) {
                            AppMonitor.Counter.commit("weex", str, 1.0d);
                            if ("sJSFMStartListener".equals(str) && map != null && map.containsKey("time")) {
                                try {
                                    AppMonitor.Counter.commit("weex", "sJSFMStartListenerTime", Double.valueOf(map.get("time").toString()).doubleValue());
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
                AppMonitor.Alarm.commitFail("weex", str2, (String) map.get("arg"), (String) map.get(IWXUserTrackAdapter.MONITOR_ERROR_CODE), (String) map.get(IWXUserTrackAdapter.MONITOR_ERROR_MSG));
            } else if (!WXErrorCode.WX_SUCCESS.getErrorCode().equals(wXPerformance.errCode)) {
                AppMonitor.Alarm.commitFail("weex", str2, wXPerformance.args, wXPerformance.errCode, wXPerformance.getErrMsg());
            } else {
                AppMonitor.Alarm.commitSuccess("weex", str2, wXPerformance.args);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void initAppMonitor() {
        if (!initAppMonitor) {
            DimensionSet create = DimensionSet.create();
            String[] dimensions = WXPerformance.getDimensions();
            StringBuilder sb = new StringBuilder("维度集:");
            for (String str : dimensions) {
                create.addDimension(str);
                if (WXEnvironment.isApkDebugable()) {
                    sb.append(str);
                    sb.append("||");
                }
            }
            sb.append("指标集:");
            MeasureSet create2 = MeasureSet.create();
            for (WXPerformance.Measure measure : WXPerformance.Measure.values()) {
                Measure measure2 = new Measure(measure.toString());
                measure2.setRange(Double.valueOf(measure.getMinRange()), Double.valueOf(measure.getMaxRange()));
                create2.addMeasure(measure2);
                if (WXEnvironment.isApkDebugable()) {
                    sb.append(measure.toString());
                }
            }
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d(TAG, sb.toString());
            }
            AppMonitor.register("weex", "load", create2, create);
            initAppMonitor = true;
        }
    }
}
