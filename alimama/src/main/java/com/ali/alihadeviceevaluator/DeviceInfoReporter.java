package com.ali.alihadeviceevaluator;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import com.ali.alihadeviceevaluator.mem.AliHAMemoryTracker;
import com.ali.alihadeviceevaluator.old.HardWareInfo;
import com.ali.alihadeviceevaluator.util.Global;
import com.ali.alihadeviceevaluator.util.KVStorageUtils;
import com.ali.alihadeviceevaluator.util.ProcessUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import java.io.File;

public class DeviceInfoReporter {
    private static final String DEFAULT = "ALI_DEFAULT";
    public static final String K_REPORT_LAST_TIMESTAMP = "report_lasttimestamp";
    public static final String K_REPORT_VALID_PERIOD = "report_validperiod";
    private static volatile boolean isFirstTime = true;
    private static AliAIHardware sAliAIHardware;

    public static void reportDeviceInfo(HardWareInfo hardWareInfo) {
        DimensionValueSet create;
        MeasureValueSet create2;
        if (!ProcessUtils.isInMainProcess()) {
            Log.i(Global.TAG, "report info just in main process");
        } else if (!doINeedReportDeviceInfo()) {
            Log.i(Global.TAG, "report info , but i am not need this time");
        } else {
            init();
            try {
                create = DimensionValueSet.create();
                create2 = MeasureValueSet.create();
                setDimensionValue(create, WXDebugConstants.ENV_DEVICE_MODEL, Build.MODEL);
                setDimensionValue(create, "cpuBrand", hardWareInfo.mCpuBrand);
                setDimensionValue(create, "cpuName", hardWareInfo.mCpuName);
                setDimensionValue(create, "cpuCount", hardWareInfo.mCpuCount);
                setDimensionValue(create, "cpuMaxFreq", hardWareInfo.mCpuMaxFreq);
                setDimensionValue(create, "cpuMinFreq", hardWareInfo.mCpuMinFreq);
                float[] fArr = hardWareInfo.mCpuFreqArray;
                StringBuilder sb = new StringBuilder();
                if (fArr != null) {
                    sb.append(fArr[0]);
                    for (int i = 1; i < fArr.length; i++) {
                        sb.append(",");
                        sb.append(fArr[i]);
                    }
                }
                setDimensionValue(create, "cpuFreqArray", sb.toString());
                setDimensionValue(create, "gpuName", hardWareInfo.mGpuName);
                setDimensionValue(create, "gpuBrand", hardWareInfo.mGpuBrand);
                setDimensionValue(create, "gpuFreq", (float) hardWareInfo.mGpuFreq);
                setDimensionValue(create, "cpuArch", hardWareInfo.getCpuArch());
                setDimensionValue(create, "displayWidth", hardWareInfo.mWidth);
                setDimensionValue(create, "displayHeight", hardWareInfo.mHeight);
                setDimensionValue(create, "displayDensity", hardWareInfo.mDesty);
                setDimensionValue(create, "openGLVersion", AliHAHardware.getInstance().getDisplayInfo().mOpenGLVersion);
                setDimensionValue(create, "memTotal", (float) AliHAHardware.getInstance().getMemoryInfo().deviceTotalMemory);
                setDimensionValue(create, "memJava", (float) AliHAHardware.getInstance().getMemoryInfo().jvmTotalMemory);
                setDimensionValue(create, "memNative", (float) AliHAHardware.getInstance().getMemoryInfo().nativeTotalMemory);
                int[] javaHeapLimit = new AliHAMemoryTracker().getJavaHeapLimit(Global.context);
                setDimensionValue(create, "memLimitedHeap", javaHeapLimit[0]);
                setDimensionValue(create, "memLimitedLargeHeap", javaHeapLimit[1]);
                setDimensionValue(create, WXDebugConstants.ENV_OS_VERSION, Build.VERSION.SDK_INT);
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                long blockSize = (long) statFs.getBlockSize();
                setDimensionValue(create, "storeTotal", (float) ((((((long) statFs.getBlockCount()) * blockSize) / 1024) / 1024) / 1024));
                setDimensionValue(create, "storeFree", (float) ((((blockSize * ((long) statFs.getAvailableBlocks())) / 1024) / 1024) / 1024));
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
                Log.e(Global.TAG, "report info failed!!");
                return;
            }
            try {
                File file = new File("/sdcard/Android/");
                if (file.exists()) {
                    setDimensionValue(create, "deviceUsedTime", (float) (Math.abs(System.currentTimeMillis() - file.lastModified()) / 86400000));
                }
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
            setDimensionValue(create, "deviceIsRoot", isRoot() + "");
            setDimensionValue(create, "memTotalUsed", (float) AliHAHardware.getInstance().getMemoryInfo().deviceUsedMemory);
            setDimensionValue(create, "memJavaUsed", (float) AliHAHardware.getInstance().getMemoryInfo().jvmUsedMemory);
            setDimensionValue(create, "memNativeUsed", (float) AliHAHardware.getInstance().getMemoryInfo().nativeUsedMemory);
            setDimensionValue(create, "pssTotal", (float) AliHAHardware.getInstance().getMemoryInfo().totalPSSMemory);
            setDimensionValue(create, "pssJava", (float) AliHAHardware.getInstance().getMemoryInfo().dalvikPSSMemory);
            setDimensionValue(create, "pssNative", (float) AliHAHardware.getInstance().getMemoryInfo().nativePSSMemory);
            create2.setValue("oldDeviceScore", (double) hardWareInfo.getScore());
            if (sAliAIHardware != null) {
                create2.setValue("deviceScore", (double) sAliAIHardware.getDeviceScore());
            }
            create2.setValue("cpuScore", (double) hardWareInfo.getCpuScore());
            create2.setValue("gpuScore", (double) hardWareInfo.getGpuScore());
            create2.setValue("memScore", (double) hardWareInfo.getMemScore());
            Log.i(Global.TAG, "report info success");
            AppMonitor.Stat.commit(Global.TAG, "DeviceInfo", create, create2);
            KVStorageUtils.getSP().edit().putLong(K_REPORT_LAST_TIMESTAMP, System.currentTimeMillis());
            KVStorageUtils.getSP().edit().commit();
        }
    }

    private static boolean isRoot() {
        String[] strArr = {"/system/bin/su", "/system/xbin/su", "/system/sbin/su", "/sbin/su", "/vendor/bin/su"};
        int i = 0;
        while (i < strArr.length) {
            try {
                if (new File(strArr[i]).exists()) {
                    return true;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void setDimensionValue(DimensionValueSet dimensionValueSet, String str, String str2) {
        if (!TextUtils.isEmpty(str2) && dimensionValueSet != null) {
            dimensionValueSet.setValue(str, str2);
        }
    }

    private static void setDimensionValue(DimensionValueSet dimensionValueSet, String str, int i) {
        if (i > 0 && dimensionValueSet != null) {
            dimensionValueSet.setValue(str, i + "");
        }
    }

    private static void setDimensionValue(DimensionValueSet dimensionValueSet, String str, float f) {
        if (f > 0.0f && dimensionValueSet != null) {
            dimensionValueSet.setValue(str, f + "");
        }
    }

    private static void init() {
        if (isFirstTime) {
            isFirstTime = false;
            AppMonitor.register(Global.TAG, "DeviceInfo", MeasureSet.create().addMeasure("oldDeviceScore").addMeasure("deviceScore").addMeasure("cpuScore").addMeasure("gpuScore").addMeasure("memScore"), DimensionSet.create().addDimension(WXDebugConstants.ENV_DEVICE_MODEL, DEFAULT).addDimension("cpuBrand", DEFAULT).addDimension("cpuName", DEFAULT).addDimension("cpuCount", DEFAULT).addDimension("cpuMaxFreq", DEFAULT).addDimension("cpuMinFreq", DEFAULT).addDimension("cpuFreqArray", DEFAULT).addDimension("gpuName", DEFAULT).addDimension("gpuBrand", DEFAULT).addDimension("gpuFreq", DEFAULT).addDimension("cpuArch", DEFAULT).addDimension("displayWidth", DEFAULT).addDimension("displayHeight", DEFAULT).addDimension("displayDensity", DEFAULT).addDimension("openGLVersion", DEFAULT).addDimension("memTotal", DEFAULT).addDimension("memJava", DEFAULT).addDimension("memNative", DEFAULT).addDimension("memLimitedHeap", DEFAULT).addDimension("memLimitedLargeHeap", DEFAULT).addDimension(WXDebugConstants.ENV_OS_VERSION, DEFAULT).addDimension("storeTotal", DEFAULT).addDimension("storeFree", DEFAULT).addDimension("deviceUsedTime", DEFAULT).addDimension("deviceIsRoot", DEFAULT).addDimension("memTotalUsed", DEFAULT).addDimension("memJavaUsed", DEFAULT).addDimension("memNativeUsed", DEFAULT).addDimension("pssTotal", DEFAULT).addDimension("pssJava", DEFAULT).addDimension("pssNative", DEFAULT));
        }
    }

    public static void setAIHardware(AliAIHardware aliAIHardware) {
        sAliAIHardware = aliAIHardware;
    }

    private static boolean doINeedReportDeviceInfo() {
        long j;
        if (!KVStorageUtils.getSP().contains(K_REPORT_LAST_TIMESTAMP)) {
            return true;
        }
        if (!KVStorageUtils.getSP().contains(K_REPORT_VALID_PERIOD)) {
            j = 24;
        } else {
            j = KVStorageUtils.getSP().getLong(K_REPORT_VALID_PERIOD, 0);
        }
        if (System.currentTimeMillis() >= KVStorageUtils.getSP().getLong(K_REPORT_LAST_TIMESTAMP, 0) + Global.hour2Ms(j)) {
            return true;
        }
        return false;
    }
}
