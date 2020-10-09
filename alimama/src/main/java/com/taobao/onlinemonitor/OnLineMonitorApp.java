package com.taobao.onlinemonitor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Debug;
import android.os.StrictMode;
import android.util.Log;
import com.uc.webview.export.extension.UCCore;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

public final class OnLineMonitorApp {
    public static int sAdvertisementTime = 0;
    static Application sApplication = null;
    public static boolean sBackInGroundOnBoot = false;
    public static short sBgCpuUseTreshold = 10;
    public static short sBgCpuUseTresholdTimes = 3;
    static short sBootAcitvityCount = 0;
    static String[] sBootActivityAry = null;
    static boolean[] sBootCorrectAry = null;
    static String sBootExtraType = "0";
    static ColdBootCheck sColdBootCheck = null;
    public static int sColdOpenMaxTimesForStatistics = 4;
    static boolean sDisableJitOnBoot = false;
    static boolean sEnableConfig = true;
    public static boolean sEnableSimpleAnaliseOnDebug = true;
    static long sFirstActivityTime = -1;
    static float sHeapUtilization = 0.0f;
    public static int sHotOpenMaxTimesForStatistics = 2;
    public static int sInstanceOccupySize = 512000;
    static boolean sIsBootCorrect = false;
    static boolean sIsCodeBoot = true;
    public static boolean sIsDebug = false;
    static boolean sIsHardWareAcce = false;
    private static boolean sIsInit = false;
    static boolean sIsLargeHeap = false;
    static boolean sIsPerformanceTest = false;
    static boolean sIsStartMethodTrace = false;
    static long sLaunchTime = -1;
    static long sMainThreadStartCpu = 0;
    public static int sMaxBootTotalTime = 30000;
    static Method sMethodDisableJitCompilation = null;
    static Method sMethodStartJitCompilation = null;
    public static boolean sPerformanceReportNotification = false;
    static String sPropertiesFileName = "OnLineMonitor.txt";
    static String sPropertyFilePath = null;
    public static boolean sPublishRelease = false;
    public static boolean sRunFinalizerOnThreshold = false;
    static int sSmoothStepInterval = 16;
    public static int sThreadDeviceCpuPercentForStatistics = 30;
    public static int sThreadPidCpuPercentForStatistics = 50;
    static int sThreadPoolQueueCommitSize = 20;
    static int sToSleepTime = 300000;
    static Object sVMRuntime = null;
    public static int sWhiteScreenMaxHeightPercent = 15;
    public static int sWhiteScreenMaxWidthPercent = 90;
    static int sWritePerformanceInfo = 15000;

    public interface SmoothView {
    }

    public static boolean isCodeBoot() {
        return sIsCodeBoot;
    }

    public static void disableJitOnBoot(boolean z) {
        sDisableJitOnBoot = z;
    }

    public static void setSmoothStepInterval(int i) {
        sSmoothStepInterval = i;
    }

    public static void setPropertiesFileName(String str) {
        sPropertiesFileName = str;
    }

    public static void setBootExtraType(int i) {
        sBootExtraType = "" + i;
    }

    public static void setBootExtraType(String str) {
        sBootExtraType = str;
    }

    @SuppressLint({"NewApi"})
    public static synchronized void init(Application application, Context context) {
        File externalFilesDir;
        synchronized (OnLineMonitorApp.class) {
            sApplication = application;
            if (!sIsInit && application != null && context != null && Build.VERSION.SDK_INT >= 14) {
                ApplicationInfo applicationInfo = context.getApplicationInfo();
                if (sEnableSimpleAnaliseOnDebug) {
                    boolean z = (applicationInfo.flags & 2) != 0;
                    sIsDebug = z;
                    if (z) {
                        OnLineMonitor.sIsTraceDetail = true;
                        TraceDetail.sMemoryLeakDetector = true;
                        TraceDetail.sTraceBundler = true;
                        sToSleepTime = 60000;
                        if (OnLineMonitor.sApiLevel >= 16) {
                            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().detectLeakedRegistrationObjects().penaltyLog().build());
                        } else {
                            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().build());
                        }
                    }
                }
                sIsLargeHeap = (applicationInfo.flags & 1048576) != 0;
                sIsHardWareAcce = (applicationInfo.flags & UCCore.VERIFY_POLICY_PAK_QUICK) != 0;
                if (!sPublishRelease && (externalFilesDir = context.getExternalFilesDir("")) != null) {
                    sPropertyFilePath = externalFilesDir.getAbsolutePath();
                    if (new File(sPropertyFilePath + "/OnLine_" + context.getPackageName() + ".txt").exists()) {
                        OnLineMonitor.sIsTraceDetail = true;
                        sWritePerformanceInfo = 3000;
                        sIsPerformanceTest = true;
                    }
                    readConfig(context);
                }
                if (Build.VERSION.SDK_INT <= 19 && (sDisableJitOnBoot || sHeapUtilization > 0.0f)) {
                    try {
                        Class<?> cls = Class.forName("dalvik.system.VMRuntime");
                        if (cls != null) {
                            Field declaredField = cls.getDeclaredField("THE_ONE");
                            declaredField.setAccessible(true);
                            sVMRuntime = declaredField.get(cls);
                            sMethodStartJitCompilation = cls.getDeclaredMethod("startJitCompilation", new Class[0]);
                            sMethodStartJitCompilation.setAccessible(true);
                            sMethodDisableJitCompilation = cls.getDeclaredMethod("disableJitCompilation", new Class[0]);
                            sMethodDisableJitCompilation.setAccessible(true);
                            if (sVMRuntime != null) {
                                if (OnLineMonitor.sIsNormalDebug) {
                                    Log.e("OnLineMonitor", "关闭JIT");
                                }
                                if (sDisableJitOnBoot) {
                                    sMethodDisableJitCompilation.invoke(sVMRuntime, new Object[0]);
                                }
                            }
                        }
                        if (sHeapUtilization > 0.0f) {
                            cls.getDeclaredMethod("setTargetHeapUtilization", new Class[]{Float.TYPE}).invoke(sVMRuntime, new Object[]{Float.valueOf(sHeapUtilization)});
                            if (OnLineMonitor.sIsNormalDebug) {
                                Log.e("OnLineMonitor", "setTargetHeapUtilization=" + sHeapUtilization);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ActivityLifecycleCallback activityLifecycleCallback = new ActivityLifecycleCallback(context);
                new OnLineMonitor(context, activityLifecycleCallback);
                application.registerActivityLifecycleCallbacks(activityLifecycleCallback);
                sIsInit = true;
            }
        }
    }

    @SuppressLint({"NewApi"})
    static void readConfig(Context context) {
        if (context != null && sEnableConfig) {
            try {
                File file = new File(sPropertyFilePath + "/" + sPropertiesFileName);
                Properties properties = new Properties();
                if (file.exists()) {
                    boolean z = false;
                    FileInputStream fileInputStream = new FileInputStream(file);
                    properties.load(fileInputStream);
                    fileInputStream.close();
                    String property = properties.getProperty("NormalDebug");
                    if (property != null && property.equalsIgnoreCase("true")) {
                        OnLineMonitor.sIsNormalDebug = true;
                    }
                    String property2 = properties.getProperty("DetailDebug");
                    if (property2 != null && property2.equalsIgnoreCase("true")) {
                        OnLineMonitor.sIsDetailDebug = true;
                    }
                    String property3 = properties.getProperty("TraceDetail");
                    if (property3 != null && property3.equalsIgnoreCase("true")) {
                        OnLineMonitor.sIsTraceDetail = true;
                        String property4 = properties.getProperty("TraceThread");
                        if (property4 != null && property4.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceThread = true;
                        }
                        String property5 = properties.getProperty("RecoredBootStepInfo");
                        if (property5 != null && property5.equalsIgnoreCase("true")) {
                            TraceDetail.sRecoredBootStepInfo = true;
                        }
                        String property6 = properties.getProperty("TraceThreadStack");
                        if (property6 != null && property6.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceThreadStack = true;
                        }
                        String property7 = properties.getProperty("TraceBundler");
                        if (property7 != null && property7.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceBundler = true;
                        }
                        String property8 = properties.getProperty("TraceStatisticsThread");
                        if (property8 != null && property8.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceStatisticsThread = true;
                        }
                        String property9 = properties.getProperty("TraceStatisticsPercent");
                        if (property9 != null && property9.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceStatisticsPercent = true;
                        }
                        String property10 = properties.getProperty("TraceMemory");
                        if (property10 != null && property10.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceMemory = true;
                        }
                        String property11 = properties.getProperty("TraceBigBitmap");
                        if (property11 != null && property11.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceBigBitmap = true;
                        }
                        String property12 = properties.getProperty("TraceMemoryInstance");
                        if (property12 != null && property12.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceMemoryInstance = true;
                        }
                        String property13 = properties.getProperty("MemoryLeakDetector");
                        if (property13 != null && property13.equalsIgnoreCase("true")) {
                            TraceDetail.sMemoryLeakDetector = true;
                        }
                        String property14 = properties.getProperty("MemoryAnalysis");
                        if (property14 != null && property14.equalsIgnoreCase("true")) {
                            TraceDetail.sMemoryAnalysis = true;
                        }
                        String property15 = properties.getProperty("MemoryOccupySize");
                        if (property15 != null) {
                            TraceDetail.sMemoryOccupySize = Integer.parseInt(property15);
                        }
                        String property16 = properties.getProperty("InstanceOccupySize");
                        if (property16 != null) {
                            sInstanceOccupySize = Integer.parseInt(property16);
                        }
                        String property17 = properties.getProperty("TraceRegThreadThreshold");
                        if (property17 != null) {
                            TraceDetail.sTraceRegThreadThreshold = Integer.parseInt(property17);
                        }
                        String property18 = properties.getProperty("TraceBootProgress");
                        if (property18 != null && property18.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceBootProgress = true;
                        }
                        String property19 = properties.getProperty("MethodTrace");
                        if (property19 != null && property19.equalsIgnoreCase("true")) {
                            File file2 = new File(sPropertyFilePath + "/" + "OnlineTrace.trace");
                            if (file.exists()) {
                                file.delete();
                            }
                            sIsStartMethodTrace = true;
                            Debug.startMethodTracing(file2.getAbsolutePath(), 8388608, 1000);
                        }
                        String property20 = properties.getProperty("ThreadExecuteTimeInterval");
                        if (property20 != null) {
                            TraceDetail.sThreadExecuteTimeInterval = Short.parseShort(property20);
                        }
                        String property21 = properties.getProperty("TraceActivityCount");
                        if (property21 != null) {
                            TraceDetail.sTraceActivityCount = Short.parseShort(property21);
                        }
                        String property22 = properties.getProperty("TraceThreadInterval");
                        if (property22 != null) {
                            TraceDetail.sTraceThreadInterval = Short.parseShort(property22);
                        }
                        String property23 = properties.getProperty("SleepTime");
                        if (property23 != null) {
                            sToSleepTime = Integer.parseInt(property23);
                        }
                        String property24 = properties.getProperty("DisableJitOnBoot");
                        if (property24 != null && property24.equalsIgnoreCase("true")) {
                            sDisableJitOnBoot = true;
                        }
                        String property25 = properties.getProperty("WaitForDebug");
                        if (property25 != null && property25.equalsIgnoreCase("true")) {
                            z = true;
                        }
                        String property26 = properties.getProperty("TraceThreadWait");
                        if (property26 != null && property26.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceThreadWait = true;
                        }
                        String property27 = properties.getProperty("TraceMemoryAllocator");
                        if (property27 != null && property27.equalsIgnoreCase("true")) {
                            TraceDetail.sTraceMemoryAllocator = true;
                        }
                        String property28 = properties.getProperty("TraceMemoryAllocatorActivity");
                        if (property28 != null) {
                            TraceDetail.sTraceMemoryAllocatorActivity = property28;
                        }
                        String property29 = properties.getProperty("HeapUtilization");
                        if (property29 != null) {
                            sHeapUtilization = Float.valueOf(property29).floatValue();
                        }
                        String property30 = properties.getProperty("TraceOnLineDuration");
                        if (property30 != null) {
                            TraceDetail.sTraceOnLineDuration = Integer.parseInt(property30);
                        }
                        if (z) {
                            Debug.waitForDebugger();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setBootInfo(String[] strArr, long j) {
        if (strArr != null) {
            sMainThreadStartCpu = Debug.threadCpuTimeNanos();
            sBootActivityAry = strArr;
            sBootAcitvityCount = (short) sBootActivityAry.length;
            sBootCorrectAry = new boolean[sBootAcitvityCount];
            sLaunchTime = System.nanoTime() / 1000000;
        }
        sColdBootCheck = new ColdBootCheck();
        sColdBootCheck.startChecker();
    }

    public static void resetBootArray(String[] strArr) {
        if (strArr != null && sBootActivityAry != null && strArr.length >= sBootActivityAry.length) {
            sBootActivityAry = strArr;
            if (strArr.length > sBootActivityAry.length) {
                boolean[] zArr = sBootCorrectAry;
                short s = sBootAcitvityCount;
                sBootAcitvityCount = (short) sBootActivityAry.length;
                sBootCorrectAry = new boolean[sBootAcitvityCount];
                for (int i = 0; i < s; i++) {
                    sBootCorrectAry[i] = zArr[i];
                }
            }
        }
    }

    static void startJitCompilation() {
        if (Build.VERSION.SDK_INT <= 19 && sDisableJitOnBoot) {
            try {
                if (sMethodStartJitCompilation != null) {
                    if (OnLineMonitor.sIsDetailDebug) {
                        Log.e("OnLineMonitor", "开启JIT");
                    }
                    sMethodStartJitCompilation.invoke(sVMRuntime, new Object[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static boolean isBootCorrect() {
        if (sBootCorrectAry == null) {
            return false;
        }
        if (sIsBootCorrect) {
            return sIsBootCorrect;
        }
        for (boolean z : sBootCorrectAry) {
            if (!z) {
                startJitCompilation();
                return false;
            }
        }
        sIsBootCorrect = true;
        return true;
    }
}
