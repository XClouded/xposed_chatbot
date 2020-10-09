package mtopsdk.mtop.stat;

import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.orange.OConstant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.TBSdkLog;

public class UploadStatAppMonitorImpl implements IUploadStats {
    private static final String TAG = "mtopsdk.UploadStatImpl";
    private static boolean mAppMonitorValid = false;

    public UploadStatAppMonitorImpl() {
        try {
            Class.forName(OConstant.REFLECT_APPMONITOR);
            mAppMonitorValid = true;
        } catch (Throwable unused) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                TBSdkLog.e(TAG, "didn't find app-monitor-sdk or ut-analytics sdk.");
            }
        }
    }

    public void onRegister(String str, String str2, Set<String> set, Set<String> set2, boolean z) {
        DimensionSet dimensionSet;
        if (mAppMonitorValid) {
            MeasureSet measureSet = null;
            if (set != null) {
                try {
                    dimensionSet = DimensionSet.create((Collection<String>) set);
                } catch (Throwable th) {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                        TBSdkLog.e(TAG, "call AppMonitor.register error.", th);
                        return;
                    }
                    return;
                }
            } else {
                dimensionSet = null;
            }
            if (set2 != null) {
                measureSet = MeasureSet.create((Collection<String>) set2);
            }
            AppMonitor.register(str, str2, measureSet, dimensionSet, z);
        }
    }

    public void onCommit(String str, String str2, Map<String, String> map, Map<String, Double> map2) {
        DimensionValueSet dimensionValueSet;
        if (mAppMonitorValid) {
            MeasureValueSet measureValueSet = null;
            if (map != null) {
                try {
                    dimensionValueSet = DimensionValueSet.create();
                    dimensionValueSet.setMap(map);
                } catch (Throwable th) {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                        TBSdkLog.e(TAG, "call AppMonitor.onCommit error.", th);
                        return;
                    }
                    return;
                }
            } else {
                dimensionValueSet = null;
            }
            if (map2 != null) {
                measureValueSet = MeasureValueSet.create();
                for (Map.Entry next : map2.entrySet()) {
                    measureValueSet.setValue((String) next.getKey(), ((Double) next.getValue()).doubleValue());
                }
            }
            AppMonitor.Stat.commit(str, str2, dimensionValueSet, measureValueSet);
        }
    }
}
