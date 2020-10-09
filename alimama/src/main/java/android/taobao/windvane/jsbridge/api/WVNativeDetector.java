package android.taobao.windvane.jsbridge.api;

import android.content.ContextWrapper;
import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.DeviceInfo;
import android.taobao.windvane.jsbridge.utils.YearClass;
import android.taobao.windvane.util.TaoLog;

import com.taobao.accs.common.Constants;
import com.taobao.application.common.ApmManager;
import com.taobao.application.common.IAppPreferences;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.wireless.security.sdk.SecurityGuardManager;

public class WVNativeDetector extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getDeviceYear".equals(str)) {
            detectYearClass(str2, wVCallBackContext);
            return true;
        } else if ("getCurrentUsage".equals(str)) {
            getCurrentUsage(str2, wVCallBackContext);
            return true;
        } else if ("getModelInfo".equals(str)) {
            getModelInfo(wVCallBackContext, str2);
            return true;
        } else if ("isSimulator".equals(str)) {
            isSimulator(str2, wVCallBackContext);
            return true;
        } else if (!"getPerformanceInfo".equals(str)) {
            return false;
        } else {
            getPerformanceInfo(str2, wVCallBackContext);
            return true;
        }
    }

    private void detectYearClass(String str, WVCallBackContext wVCallBackContext) {
        int i = YearClass.get(this.mContext);
        if (i == -1) {
            wVCallBackContext.error();
            return;
        }
        WVResult wVResult = new WVResult();
        wVResult.addData("deviceYear", Integer.toString(i));
        wVCallBackContext.success(wVResult);
    }

    public void getModelInfo(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        wVResult.addData(Constants.KEY_MODEL, Build.MODEL);
        wVResult.addData("brand", Build.BRAND);
        wVCallBackContext.success(wVResult);
    }

    private void getCurrentUsage(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        if (GlobalConfig.context == null) {
            wVCallBackContext.error();
            return;
        }
        float totalMemory = (float) (DeviceInfo.getTotalMemory(GlobalConfig.context) / 1048576);
        float processCpuRate = DeviceInfo.getProcessCpuRate();
        float freeMemorySize = totalMemory - ((float) (DeviceInfo.getFreeMemorySize(GlobalConfig.context) / 1048576));
        wVResult.addData("cpuUsage", Float.toString(processCpuRate));
        wVResult.addData("memoryUsage", Float.toString(freeMemorySize / totalMemory));
        wVResult.addData("totalMemory", Float.toString(totalMemory));
        wVResult.addData("usedMemory", Float.toString(freeMemorySize));
        wVCallBackContext.success(wVResult);
    }

    private void isSimulator(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        try {
            boolean isSimulator = SecurityGuardManager.getInstance(new ContextWrapper(this.mContext)).getSimulatorDetectComp().isSimulator();
            TaoLog.i(WVAPI.PluginName.API_NATIVEDETECTOR, "Current phone is simulator: " + isSimulator);
            wVResult.addData("isSimulator", (Object) Boolean.valueOf(isSimulator));
            wVCallBackContext.success(wVResult);
        } catch (Throwable th) {
            wVResult.addData(IWXUserTrackAdapter.MONITOR_ERROR_MSG, th.getMessage());
            wVCallBackContext.error(wVResult);
        }
    }

    private void getPerformanceInfo(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        try {
            IAppPreferences appPreferences = ApmManager.getAppPreferences();
            appPreferences.getBoolean("isApm", false);
            int i = appPreferences.getInt("deviceScore", -1);
            int i2 = appPreferences.getInt("cpuScore", -1);
            int i3 = appPreferences.getInt("memScore", -1);
            wVResult.addData("deviceScore", (Object) Integer.valueOf(i));
            wVResult.addData("cpuScore", (Object) Integer.valueOf(i2));
            wVResult.addData("memScore", (Object) Integer.valueOf(i3));
            wVCallBackContext.success(wVResult);
        } catch (Throwable th) {
            wVResult.addData(IWXUserTrackAdapter.MONITOR_ERROR_MSG, th.getMessage());
            wVCallBackContext.error(wVResult);
        }
    }
}
