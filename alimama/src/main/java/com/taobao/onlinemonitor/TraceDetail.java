package com.taobao.onlinemonitor;

import alimama.com.unwetaologger.base.LogContent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.Parcel;
import android.os.StatFs;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewDebug;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class TraceDetail {
    static final String LOW_API = "系统版本过低";
    static final String TABLE_TD = "<td class=\"TableBody2\">";
    static final String UNKNOWN = "Unknown";
    public static int sHookDelayTime = 0;
    static Thread sMainThread = null;
    public static boolean sMemoryAnalysis = false;
    public static boolean sMemoryLeakDetector = false;
    public static int sMemoryOccupySize = 1024;
    public static OnlineHookedMethod sOnlineHookedMethod = null;
    public static boolean sRecoredBootStepInfo = false;
    public static short sThreadExecuteTimeInterval = 20;
    public static short sTraceActivityCount = 50;
    public static boolean sTraceBigBitmap = false;
    public static boolean sTraceBootProgress = false;
    public static boolean sTraceBundler = false;
    public static boolean sTraceFinalizer = false;
    public static boolean sTraceLog = false;
    public static boolean sTraceMemory = false;
    public static boolean sTraceMemoryAllocator = false;
    public static String sTraceMemoryAllocatorActivity = null;
    public static boolean sTraceMemoryInstance = false;
    public static int sTraceOnLineDuration = 30;
    public static boolean sTraceOnLineListener = true;
    public static int sTraceRegThreadThreshold = 20;
    public static boolean sTraceStatisticsPercent = false;
    public static boolean sTraceStatisticsThread = false;
    public static boolean sTraceThread = false;
    public static short sTraceThreadInterval = 500;
    public static boolean sTraceThreadStack;
    public static boolean sTraceThreadWait;
    public static boolean sTraceThrowable;
    public static short sTracedActivityCount;
    String[] mActivityLifeCycleName = {"onActivityIdle", "onActivityCreate", Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED, "onActivityResume", Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED, "onActivityStoped", Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED};
    ArrayList<OnLineMonitor.ActivityRuntimeInfo> mActivityRuntimeInfoList = new ArrayList<>(100);
    WeakHashMap<Activity, OnLineMonitor.ActivityRuntimeInfo> mActivityWeakMap = new WeakHashMap<>(64);
    ArrayList<NewThreadInfo> mAsyncTaskInfoList;
    ArrayList<Float> mBootCpuPercentTimestamps;
    Map<String, String> mBootDiffThreadMap = new LinkedHashMap(256);
    SparseIntArray mBootPidCpuPercents;
    int[] mBootStepClass = new int[2];
    int[] mBootStepCpu = new int[4];
    float[] mBootStepCpuLoad = new float[2];
    int[] mBootStepGcCount = new int[2];
    int[] mBootStepIoWait = new int[4];
    long[] mBootStepMainThreadTime = new long[2];
    int[] mBootStepMem = new int[6];
    long[] mBootStepPidTime = new long[2];
    float[] mBootStepSched = new float[6];
    int[] mBootStepThread = new int[4];
    SparseIntArray mBootSysCpuPercents;
    volatile int mBroadCastSize = 0;
    short mCheckThreadCount = 0;
    Class mClassContextImpl;
    Class mClassSQLiteDebug;
    Class mClassV4Fragment;
    HashMap<String, Integer> mCloseGuardInfo;
    HashMap<String, String> mContentHashMap;
    Method mCountInstancesOfClass;
    ArrayList<Float> mCpuPercentTimestamps;
    Field mDataBaseMemoryUsed;
    String mDestroyedActivityName;
    Class mEditorImpl;
    Method mEnableRecentAllocations;
    int mExecuteThreadInfoBootSize;
    ArrayList<ThreadInfo> mExecuteThreadInfoList = new ArrayList<>(500);
    String mExternalPath;
    Field mFieldThreadCount;
    ArrayList<File> mFileToZipList = new ArrayList<>();
    Map<String, Integer> mFinalizerObject;
    long mFirstMobileRxBytes = -1;
    long mFirstMobileTxBytes;
    long mFirstTotalRxBytes;
    long mFirstTotalTxBytes;
    Method mGetDatabaseInfo;
    Method mGetGlobalAllocCount;
    Method mGetGlobalAllocSize;
    Method mGetGlobalAssetCount;
    Method mGetGlobalAssetManagerCount;
    Method mGetRecentAllocations;
    Method mGetViewInstanceCount;
    Method mGetViewRootImplCount;
    boolean mHasMemroyLeack;
    Map<String, OnLineMonitor.BundleInfo> mInstallBundleInfoMap;
    int mJiffyMillis;
    String[] mLifeCycleArray = new String[2];
    HashMap<String, Integer> mMainThreadBlockGuardInfo;
    long mMainThreadEndCpu;
    int mMainThreadNice;
    int mMainThreadPriority = 5;
    int mMainThreadTid;
    List<Long> mMajorFaults;
    SparseIntArray mMemoryLevels;
    int[] mNewTheadCountAyr = new int[3];
    ArrayList<MethodInfo> mOnActivityLifeCycleList;
    ArrayList<MethodInfo> mOnActivityLifeCycleTimeList;
    ArrayList<MethodInfo> mOnBackForGroundList;
    ArrayList<MethodInfo> mOnBootFinishedList;
    OnLineMonitor mOnLineMonitor;
    ArrayList<MethodInfo> mOnLineMonitorNotifyList;
    ArrayList<MethodInfo> mOnLineMonitorNotifyTimeList;
    ArrayList<Float> mPerCpuLoads;
    SparseIntArray mPidCpuPercentRecords;
    SparseIntArray mRunningFinalizerCount;
    SparseIntArray mRunningPidScores;
    SparseIntArray mRunningSysScores;
    SparseIntArray mRunningThreadsCount;
    SparseArray<Long> mSparseArrayBootProgressEnd;
    SparseArray<String> mSparseArrayBootProgressName;
    SparseArray<Long> mSparseArrayBootProgressStart;
    SparseArray<String> mSparseArrayThreadName;
    Map<String, OnLineMonitor.BundleInfo> mStartBundleInfoMap;
    SparseIntArray mSysCpuPercentRecords;
    SparseIntArray mSysIoWaitCounts;
    SparseIntArray mSysIoWaitPercent;
    SparseIntArray mSysIoWaitSums;
    ArrayList<Float> mSysLoads1Min;
    ArrayList<Float> mSysLoads5Min;
    SparseIntArray mSysSchedWaitCounts;
    SparseIntArray mSysSchedWaitSums;
    SparseIntArray mSysThreadsCount;
    long mSystemRunCpuTimeEnd;
    long mSystemRunCpuTimeStart;
    long mSystemTotalCpuTimeEnd;
    long mSystemTotalCpuTimeStart;
    String mTemplateZipFile;
    Map<String, AllocatorInfo> mThreadAllocatorMap;
    ArrayList<NewThreadInfo> mThreadInfoList;
    LinkedHashMap<String, ThreadPoolInfo> mThreadPoolInfoMap;
    ConcurrentHashMap<Runnable, Thread> mThreadPoolRunnableMap;
    HashMap<String, String> mThreadStackHashMap;
    ArrayList<ThreadStackTraceTime> mThreadStackTraceTimeList = new ArrayList<>(30);
    SparseIntArray mVmThreadsCount;
    ArrayList<OnLineMonitor.ThreadIoInfo> mWakeLockInfoList = new ArrayList<>();
    String sTemplateName = "/OnLineMonitorTemplate.zip";
    String sTemplateUrl = "https://os.alipayobjects.com/rmsportal/kOKzRQMUAScJhkFVDOfC.zip";

    public static class BroadCastInfo implements Serializable {
        String activityName;
        String className;
        String option;
        int size;
        String stackTrace;
        String strAction;
    }

    public static class MethodInfo implements Serializable {
        String activityName;
        long cpuTime;
        String methodName;
        int priority;
        long realTime;
        String threadName;
        StackTraceElement[] threadStack;
    }

    public static class NewThreadInfo implements Serializable {
        String activityName;
        String classThreadName;
        int count;
        String createFromThread;
        boolean isInboot;
        int javaPriority;
        HashMap<String, String> mapKeys;
        String name;
        String newTraceElement;
        int strLength;
        long threadId;
        int threadPriority;
    }

    public interface OnlineHookMethod {
        void hookAllConstructors(Class cls);

        void hookMethod(Class cls, String str, Object... objArr);
    }

    public interface OnlineHookedMethod {
        void onHookedAfter(Object obj, String str, Object[] objArr);

        void onHookedBefore(Object obj, String str, Object[] objArr);
    }

    public static class PinCpuTime implements Serializable {
        long cputime;
        String name;
        float percent;
        int pid;
    }

    public static class ServiceInfo implements Serializable {
        String activityName;
        long cpuTime;
        String methodName;
        int priority;
        long realTime;
        String serviceConnection;
        String serviceName;
        String threadName;
    }

    public static class SmStat implements Serializable {
        public short badSmCount;
        public short drawCount;
        public short eventCount;
        public short eventMaxDelaytime;
        public short eventRate;
        public short eventUseTime;
        public int index;
        public short layoutTimes;
        public short maxSMInterval;
        public short sm;
        public short totalBadSmTime;
        public short totalSmCount;
        public short usetime;
        public String viewName;
    }

    public static class ThreadPoolInfo implements Serializable {
        int activeCount;
        String activityName;
        String classBlockingQueue;
        String classExecutor;
        String classThreadFactory;
        long completeCount;
        int coreSize;
        String createFromThread;
        boolean isInboot;
        long keepLiveTime;
        int maxSize;
        int newThreadSize;
        String newTraceElement;
        StringBuilder stringBuilderThreads;
        WeakReference<ThreadPoolExecutor> threadPoolExecutor;
        int totalPoolThread;
        int waitExecuteCount;
        int waitMaxSize;
        int waitTotalSize;
    }

    public static class ThreadStackTraceTime implements Serializable {
        String activityName;
        long cpuTime;
        boolean isBoot;
        String methodName;
        String stackTraceElement;
        long useTime;
    }

    /* access modifiers changed from: package-private */
    public String getRunStatus(int i) {
        return i >= 85 ? "运行很流畅" : i >= 70 ? "运行较流畅" : i >= 60 ? "运行一般" : i >= 50 ? "运行偏慢" : "运行很慢";
    }

    @SuppressLint({"NewApi"})
    TraceDetail(OnLineMonitor onLineMonitor) {
        this.mOnLineMonitor = onLineMonitor;
        OnLineMonitor onLineMonitor2 = this.mOnLineMonitor;
        this.mExternalPath = OnLineMonitor.sOnLineMonitorFileDir;
        if (this.mExternalPath != null) {
            this.mTemplateZipFile = this.mExternalPath + this.sTemplateName;
        }
        this.mSysCpuPercentRecords = new SparseIntArray(400);
        this.mPidCpuPercentRecords = new SparseIntArray(400);
        this.mRunningSysScores = new SparseIntArray(400);
        this.mRunningPidScores = new SparseIntArray(400);
        this.mSysThreadsCount = new SparseIntArray(400);
        this.mVmThreadsCount = new SparseIntArray(400);
        this.mRunningThreadsCount = new SparseIntArray(400);
        this.mCpuPercentTimestamps = new ArrayList<>(400);
        this.mMajorFaults = new ArrayList(400);
        this.mSysIoWaitCounts = new SparseIntArray(400);
        this.mSysIoWaitSums = new SparseIntArray(400);
        this.mSysIoWaitPercent = new SparseIntArray(400);
        this.mSysSchedWaitSums = new SparseIntArray(400);
        this.mSysSchedWaitCounts = new SparseIntArray(400);
        this.mMemoryLevels = new SparseIntArray(400);
        this.mPerCpuLoads = new ArrayList<>(400);
        this.mSysLoads1Min = new ArrayList<>(400);
        this.mSysLoads5Min = new ArrayList<>(400);
        this.mRunningFinalizerCount = new SparseIntArray(400);
        if (!OnLineMonitorApp.sIsPerformanceTest) {
            this.mCloseGuardInfo = new HashMap<>();
            this.mMainThreadBlockGuardInfo = new HashMap<>();
        }
        if (sTraceOnLineListener) {
            this.mOnBootFinishedList = new ArrayList<>();
            this.mOnBackForGroundList = new ArrayList<>();
            this.mOnActivityLifeCycleList = new ArrayList<>();
            this.mOnLineMonitorNotifyList = new ArrayList<>();
            this.mOnActivityLifeCycleTimeList = new ArrayList<>();
            this.mOnLineMonitorNotifyTimeList = new ArrayList<>();
        }
        if (OnLineMonitorApp.sPropertyFilePath != null) {
            try {
                File file = new File(this.mTemplateZipFile);
                if (!file.exists()) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.sTemplateUrl));
                    request.setDestinationUri(Uri.fromFile(file));
                    ((DownloadManager) onLineMonitor.mContext.getSystemService("download")).enqueue(request);
                }
            } catch (Throwable unused) {
            }
        }
        if ((sTraceMemory || OnLineMonitorApp.sIsDebug) && OnLineMonitor.sApiLevel >= 19) {
            try {
                this.mCountInstancesOfClass = Debug.class.getDeclaredMethod("countInstancesOfClass", new Class[]{Class.class});
                this.mCountInstancesOfClass.setAccessible(true);
                this.mGetViewInstanceCount = ViewDebug.class.getDeclaredMethod("getViewInstanceCount", new Class[0]);
                this.mGetViewInstanceCount.setAccessible(true);
                this.mGetViewRootImplCount = ViewDebug.class.getDeclaredMethod("getViewRootImplCount", new Class[0]);
                this.mGetViewRootImplCount.setAccessible(true);
                this.mGetGlobalAssetCount = AssetManager.class.getDeclaredMethod("getGlobalAssetCount", new Class[0]);
                this.mGetGlobalAssetCount.setAccessible(true);
                this.mGetGlobalAssetManagerCount = AssetManager.class.getDeclaredMethod("getGlobalAssetManagerCount", new Class[0]);
                this.mGetGlobalAssetManagerCount.setAccessible(true);
                this.mGetGlobalAllocSize = Parcel.class.getDeclaredMethod("getGlobalAllocSize", new Class[0]);
                this.mGetGlobalAllocSize.setAccessible(true);
                this.mGetGlobalAllocCount = Parcel.class.getDeclaredMethod("getGlobalAllocCount", new Class[0]);
                this.mGetGlobalAllocCount.setAccessible(true);
                this.mClassSQLiteDebug = Class.forName("android.database.sqlite.SQLiteDebug");
                this.mClassContextImpl = Class.forName("android.app.ContextImpl");
                this.mGetDatabaseInfo = this.mClassSQLiteDebug.getDeclaredMethod("getDatabaseInfo", new Class[0]);
                this.mGetDatabaseInfo.setAccessible(true);
                this.mDataBaseMemoryUsed = Class.forName("android.database.sqlite.SQLiteDebug$PagerStats").getDeclaredField("memoryUsed");
                this.mDataBaseMemoryUsed.setAccessible(true);
            } catch (Throwable unused2) {
            }
            try {
                this.mClassV4Fragment = Class.forName("androidx.fragment.app.Fragment");
            } catch (Throwable unused3) {
            }
        }
        boolean z = OnLineMonitor.sIsTraceDetail;
    }

    public static boolean zipFiles(ArrayList<File> arrayList, String str) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(str), 102400));
            Iterator<File> it = arrayList.iterator();
            while (it.hasNext()) {
                File next = it.next();
                byte[] bArr = new byte[com.alibaba.analytics.core.device.Constants.MAX_UPLOAD_SIZE];
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(next), com.alibaba.analytics.core.device.Constants.MAX_UPLOAD_SIZE);
                zipOutputStream.putNextEntry(new ZipEntry("/" + next.getName()));
                while (true) {
                    int read = bufferedInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    zipOutputStream.write(bArr, 0, read);
                }
                bufferedInputStream.close();
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            Log.e("OnLineMonitor", str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00c7 A[SYNTHETIC, Splitter:B:37:0x00c7] */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unZipFiles(java.io.File r11, java.lang.String r12) {
        /*
            r10 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r12)
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x0012
            boolean r1 = r0.isFile()
            if (r1 == 0) goto L_0x0012
            return
        L_0x0012:
            boolean r1 = r0.exists()
            if (r1 != 0) goto L_0x001f
            boolean r0 = r0.mkdir()
            if (r0 != 0) goto L_0x001f
            return
        L_0x001f:
            r0 = 0
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ Throwable -> 0x00cb, all -> 0x00c3 }
            r1.<init>(r11)     // Catch:{ Throwable -> 0x00cb, all -> 0x00c3 }
            java.util.Enumeration r0 = r1.entries()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r2 = 2048(0x800, float:2.87E-42)
        L_0x002b:
            boolean r3 = r0.hasMoreElements()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            if (r3 == 0) goto L_0x00bb
            java.lang.Object r3 = r0.nextElement()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.util.zip.ZipEntry r3 = (java.util.zip.ZipEntry) r3     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            boolean r4 = r3.isDirectory()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            if (r4 == 0) goto L_0x005e
            java.io.File r4 = new java.io.File     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r5.<init>()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r5.append(r12)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.String r6 = "/"
            r5.append(r6)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.String r3 = r3.getName()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r5.append(r3)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.String r3 = r5.toString()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r4.<init>(r3)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r4.mkdirs()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            goto L_0x002b
        L_0x005e:
            java.io.File r4 = new java.io.File     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r5.<init>()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r5.append(r12)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.String r6 = "/"
            r5.append(r6)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.String r6 = r3.getName()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r5.append(r6)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.io.File r5 = r11.getParentFile()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            if (r5 == 0) goto L_0x008a
            boolean r6 = r5.exists()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            if (r6 != 0) goto L_0x008a
            r5.mkdirs()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
        L_0x008a:
            byte[] r5 = new byte[r2]     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.io.BufferedOutputStream r6 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r7.<init>(r4)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r6.<init>(r7)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.io.InputStream r3 = r1.getInputStream(r3)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r7.<init>(r3)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
        L_0x009f:
            r3 = 0
            int r8 = r7.read(r5, r3, r2)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r9 = -1
            if (r8 == r9) goto L_0x00ab
            r6.write(r5, r3, r8)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            goto L_0x009f
        L_0x00ab:
            r6.flush()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r7.close()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r6.close()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            java.util.ArrayList<java.io.File> r3 = r10.mFileToZipList     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            r3.add(r4)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bf }
            goto L_0x002b
        L_0x00bb:
            r1.close()     // Catch:{ IOException -> 0x00cf }
            goto L_0x00cf
        L_0x00bf:
            r11 = move-exception
            goto L_0x00c5
        L_0x00c1:
            goto L_0x00cc
        L_0x00c3:
            r11 = move-exception
            r1 = r0
        L_0x00c5:
            if (r1 == 0) goto L_0x00ca
            r1.close()     // Catch:{ IOException -> 0x00ca }
        L_0x00ca:
            throw r11
        L_0x00cb:
            r1 = r0
        L_0x00cc:
            if (r1 == 0) goto L_0x00cf
            goto L_0x00bb
        L_0x00cf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.TraceDetail.unZipFiles(java.io.File, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.mSystemTotalCpuTimeStart = 0;
        this.mSystemTotalCpuTimeEnd = 0;
        this.mSparseArrayBootProgressStart = null;
        this.mSparseArrayBootProgressEnd = null;
        this.mActivityRuntimeInfoList.clear();
        this.mWakeLockInfoList.clear();
        this.mCheckThreadCount = 0;
        sTracedActivityCount = 0;
        if (this.mThreadStackHashMap != null) {
            this.mThreadStackHashMap.clear();
        }
    }

    /* access modifiers changed from: package-private */
    public void onBootStep1() {
        if (sRecoredBootStepInfo) {
            this.mOnLineMonitor.getCpuInfo(true, true, true);
            this.mOnLineMonitor.getMemInfo(true);
            this.mBootStepCpu[0] = this.mOnLineMonitor.mProcessCpuTracker.mTotalSysPercent;
            this.mBootStepCpu[1] = this.mOnLineMonitor.mProcessCpuTracker.mMyPidPercent;
            this.mBootStepThread[0] = this.mOnLineMonitor.mRuntimeThreadCount;
            this.mBootStepThread[1] = this.mOnLineMonitor.mRunningThreadCount;
            this.mBootStepIoWait[0] = this.mOnLineMonitor.mPidIoWaitCount;
            this.mBootStepIoWait[1] = this.mOnLineMonitor.mPidIoWaitSum;
            this.mBootStepSched[0] = (float) this.mOnLineMonitor.mPidWaitCount;
            this.mBootStepSched[1] = this.mOnLineMonitor.mPidWaitSum;
            this.mBootStepSched[2] = this.mOnLineMonitor.mPidWaitMax;
            this.mBootStepCpuLoad[0] = this.mOnLineMonitor.mPidPerCpuLoad;
            this.mBootStepGcCount[0] = this.mOnLineMonitor.mTotalGcCount;
            this.mBootStepMem[0] = (int) this.mOnLineMonitor.mTotalUsedMemory;
            this.mBootStepMem[1] = (int) this.mOnLineMonitor.mDalvikPss;
            this.mBootStepMem[2] = (int) this.mOnLineMonitor.mNativeHeapPss;
            this.mBootStepClass[0] = Debug.getLoadedClassCount();
            this.mBootStepPidTime[0] = this.mOnLineMonitor.mProcessCpuTracker.mProcessUserTime + this.mOnLineMonitor.mProcessCpuTracker.mProcessSystemTime;
            this.mBootStepMainThreadTime[0] = this.mOnLineMonitor.mProcessCpuTracker.loadTaskTime(this.mMainThreadTid);
        }
    }

    /* access modifiers changed from: package-private */
    public void onBootStep2() {
        if (sRecoredBootStepInfo) {
            this.mOnLineMonitor.getCpuInfo(true, true, true);
            this.mOnLineMonitor.getMemInfo(true);
            this.mBootStepThread[2] = this.mOnLineMonitor.mRuntimeThreadCount;
            this.mBootStepThread[3] = this.mOnLineMonitor.mRunningThreadCount;
            this.mBootStepCpu[2] = this.mOnLineMonitor.mProcessCpuTracker.mTotalSysPercent;
            this.mBootStepCpu[3] = this.mOnLineMonitor.mProcessCpuTracker.mMyPidPercent;
            this.mBootStepMem[3] = (int) this.mOnLineMonitor.mTotalUsedMemory;
            this.mBootStepMem[4] = (int) this.mOnLineMonitor.mDalvikPss;
            this.mBootStepMem[5] = (int) this.mOnLineMonitor.mNativeHeapPss;
            this.mBootStepSched[3] = (float) this.mOnLineMonitor.mPidWaitCount;
            this.mBootStepSched[4] = this.mOnLineMonitor.mPidWaitSum;
            this.mBootStepSched[5] = this.mOnLineMonitor.mPidWaitMax;
            this.mBootStepCpuLoad[1] = this.mOnLineMonitor.mPidPerCpuLoad;
            this.mBootStepGcCount[1] = this.mOnLineMonitor.mTotalGcCount;
            this.mBootStepIoWait[2] = this.mOnLineMonitor.mPidIoWaitCount;
            this.mBootStepIoWait[3] = this.mOnLineMonitor.mPidIoWaitSum;
            this.mBootStepClass[1] = Debug.getLoadedClassCount();
            this.mBootStepPidTime[1] = this.mOnLineMonitor.mProcessCpuTracker.mProcessUserTime + this.mOnLineMonitor.mProcessCpuTracker.mProcessSystemTime;
            this.mBootStepMainThreadTime[1] = this.mOnLineMonitor.mProcessCpuTracker.loadTaskTime(this.mMainThreadTid);
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityCreate(Activity activity) {
        if (this.mActivityWeakMap != null) {
            this.mActivityWeakMap.put(activity, this.mOnLineMonitor.mActivityRuntimeInfo);
        }
        if (sTraceMemoryAllocator && sTraceMemoryAllocatorActivity != null && sTraceMemoryAllocatorActivity.contains(activity.getClass().getName())) {
            startMemoryAllocator();
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityPaused(Activity activity) {
        if (sTraceMemoryAllocator && sTraceMemoryAllocatorActivity != null && sTraceMemoryAllocatorActivity.contains(activity.getClass().getName())) {
            stopMemoryAllocator();
        }
    }

    /* access modifiers changed from: package-private */
    public void startMemoryAllocator() {
        if (sTraceMemoryAllocator) {
        }
    }

    /* access modifiers changed from: package-private */
    public void stopMemoryAllocator() {
        if (sTraceMemoryAllocator) {
        }
    }

    /* access modifiers changed from: package-private */
    public void traceThreadInfo() {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        if (allStackTraces != null) {
            if (this.mSparseArrayThreadName == null) {
                this.mSparseArrayThreadName = new SparseArray<>(allStackTraces.size() + 20);
            }
            if (this.mThreadStackHashMap == null) {
                this.mThreadStackHashMap = new HashMap<>(128);
            }
            StringBuilder sb = new StringBuilder(300);
            Iterator<Map.Entry<Thread, StackTraceElement[]>> it = allStackTraces.entrySet().iterator();
            while (true) {
                int i = 1;
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry next = it.next();
                if (next != null) {
                    Thread thread = (Thread) next.getKey();
                    StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) next.getValue();
                    if (thread != null) {
                        if (thread.getId() != 1) {
                            i = thread instanceof HandlerThread ? ((HandlerThread) thread).getThreadId() : 0;
                        }
                        if (i == 0 && stackTraceElementArr != null) {
                            for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                                sb.append(stackTraceElement.toString());
                                sb.append("<br>");
                            }
                            this.mThreadStackHashMap.put(thread.getName(), sb.substring(0));
                            sb.setLength(0);
                        }
                    }
                }
            }
            for (Map.Entry<String, ThreadInfo> value : this.mOnLineMonitor.mThreadInfoHashMap.entrySet()) {
                ThreadInfo threadInfo = (ThreadInfo) value.getValue();
                if (threadInfo != null) {
                    String str = this.mSparseArrayThreadName.get(threadInfo.mId == 1 ? threadInfo.mId : threadInfo.mThreadId);
                    if (str != null && str.length() > 0) {
                        threadInfo.mName = str;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getThreadSchedTime(ThreadInfo threadInfo) {
        if (threadInfo != null && !this.mOnLineMonitor.mFileSchedIsNotExists) {
            File file = new File("/proc/" + this.mOnLineMonitor.mMyPid + "/task/" + threadInfo.mThreadId + "/sched");
            if (file.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String readLine = bufferedReader.readLine();
                    while (true) {
                        if (readLine == null) {
                            break;
                        }
                        if (readLine.contains(".wait_sum")) {
                            int lastIndexOf = readLine.lastIndexOf(32);
                            if (lastIndexOf > 0) {
                                threadInfo.mSchedWaitSum = (int) Float.parseFloat(readLine.substring(lastIndexOf + 1));
                            }
                        } else if (readLine.contains(".wait_max")) {
                            int lastIndexOf2 = readLine.lastIndexOf(32);
                            if (lastIndexOf2 > 0) {
                                threadInfo.mSchedWaitMax = (int) Float.parseFloat(readLine.substring(lastIndexOf2 + 1));
                            }
                        } else if (readLine.contains(".wait_count")) {
                            int lastIndexOf3 = readLine.lastIndexOf(32);
                            if (lastIndexOf3 > 0) {
                                threadInfo.mSchedWaitCount = Integer.parseInt(readLine.substring(lastIndexOf3 + 1));
                            }
                        } else if (readLine.contains("iowait_sum")) {
                            int lastIndexOf4 = readLine.lastIndexOf(32);
                            if (lastIndexOf4 > 0) {
                                threadInfo.mIoWaitTime = (int) Float.parseFloat(readLine.substring(lastIndexOf4 + 1));
                            }
                        } else if (readLine.contains("iowait_count")) {
                            int lastIndexOf5 = readLine.lastIndexOf(32);
                            if (lastIndexOf5 > 0) {
                                threadInfo.mIoWaitCount = Integer.parseInt(readLine.substring(lastIndexOf5 + 1));
                            }
                        } else if (readLine.contains("iowait_count")) {
                            int lastIndexOf6 = readLine.lastIndexOf(32);
                            if (lastIndexOf6 > 0) {
                                threadInfo.mIoWaitCount = Integer.parseInt(readLine.substring(lastIndexOf6 + 1));
                            }
                        } else if (readLine.contains("avg_per_cpu")) {
                            int lastIndexOf7 = readLine.lastIndexOf(32);
                            if (lastIndexOf7 > 0) {
                                threadInfo.mMaxAvgPerCpu = Math.max(threadInfo.mMaxAvgPerCpu, Float.parseFloat(readLine.substring(lastIndexOf7 + 1)));
                            }
                        }
                        readLine = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getThreadIoWaitTime() {
        if (sTraceThread && this.mOnLineMonitor.mThreadInfoHashMap != null) {
            try {
                for (Map.Entry<String, ThreadInfo> value : this.mOnLineMonitor.mThreadInfoHashMap.entrySet()) {
                    ThreadInfo threadInfo = (ThreadInfo) value.getValue();
                    if (threadInfo.mStatus > 0) {
                        getThreadSchedTime(threadInfo);
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void writePageInfo(boolean z) {
        if (this.mActivityRuntimeInfoList != null && OnLineMonitorApp.sPropertyFilePath != null) {
            Calendar instance = Calendar.getInstance();
            String str = "/" + this.mOnLineMonitor.mOnLineStat.deviceInfo.mobileModel.replace(Operators.SPACE_STR, "") + "-" + (instance.get(2) + 1) + "-" + instance.get(5) + "-" + instance.get(11) + "-" + instance.get(12) + "-" + instance.get(13);
            String str2 = this.mExternalPath + str;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
            generateOutputData(str2, z);
            File file2 = new File(this.mTemplateZipFile);
            if (file2.exists()) {
                unZipFiles(file2, str2);
            }
            if (sTraceBigBitmap) {
                File file3 = new File(this.mExternalPath + "/BigBitmap");
                if (file3.exists()) {
                    file3.renameTo(new File(str2 + "/BigBitmap"));
                }
            }
            Log.e("OnLineMonitor", "性能报告已生成，下载可以通过 adb pull /sdcard/android/data/" + this.mOnLineMonitor.mContext.getPackageName() + "/files/OnlineMonitor" + str + " ./");
            this.mFileToZipList.clear();
            if ((OnLineMonitorApp.sPerformanceReportNotification || this.mHasMemroyLeack) && OnLineMonitor.sApiLevel >= 16) {
                try {
                    NotificationManager notificationManager = (NotificationManager) this.mOnLineMonitor.mContext.getSystemService("notification");
                    String str3 = null;
                    float f = ((float) OnLineMonitorApp.sToSleepTime) / 60000.0f;
                    if (this.mOnLineMonitor.mLeakMemoryWeakMap != null && this.mOnLineMonitor.mLeakMemoryWeakMap.size() > 0) {
                        str3 = "发现内存泄漏," + f + "分钟后开始分析泄漏路径!";
                    }
                    Intent intent = new Intent();
                    intent.addFlags(268435456);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.fromFile(new File(this.mExternalPath + str + "/index.html")), "text/html");
                    intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    Notification build = new Notification.Builder(this.mOnLineMonitor.mContext).setTicker("OnlineMonitor检测报告已经生成").setSmallIcon(17301642).setContentTitle("OnlineMonitor检测报告").setSubText(str3).setContentText("点击可以查看或者通过Adb下载文件夹!").setContentIntent(PendingIntent.getActivity(this.mOnLineMonitor.mContext, 100, intent, 268435456)).setNumber(1).build();
                    build.flags = 16 | build.flags;
                    build.defaults = build.defaults | 1;
                    build.defaults |= 2;
                    build.defaults |= 4;
                    notificationManager.notify(1, build);
                } catch (Throwable unused) {
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:161|162|163|164|165|166|169|170|(3:172|(4:175|(3:179|(5:182|(3:185|186|(5:190|191|192|193|194))|196|197|180)|533)|529|173)|530)) */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:169:0x068c */
    /* JADX WARNING: Missing exception handler attribute for start block: B:196:0x071f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:211:0x0743 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:240:0x07dc */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x05d9 A[Catch:{ Exception -> 0x0620 }] */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0648  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x0692 A[Catch:{ Exception -> 0x0725, Throwable -> 0x07e7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0749 A[Catch:{ Exception -> 0x07e2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:362:0x0b0d  */
    /* JADX WARNING: Removed duplicated region for block: B:365:0x0b34  */
    /* JADX WARNING: Removed duplicated region for block: B:371:0x0b5f  */
    /* JADX WARNING: Removed duplicated region for block: B:382:0x0bdd  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x0c18  */
    /* JADX WARNING: Removed duplicated region for block: B:413:0x0c62  */
    /* JADX WARNING: Removed duplicated region for block: B:428:0x0ca0  */
    /* JADX WARNING: Removed duplicated region for block: B:432:0x0ca7 A[Catch:{ Throwable -> 0x0de5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:446:0x0ce4 A[Catch:{ Throwable -> 0x0de5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:453:0x0d13 A[Catch:{ Throwable -> 0x0de5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:457:0x0db3 A[Catch:{ Throwable -> 0x0de5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:472:0x0e18  */
    /* JADX WARNING: Removed duplicated region for block: B:473:0x0e1a  */
    /* JADX WARNING: Removed duplicated region for block: B:475:0x0e22  */
    /* JADX WARNING: Removed duplicated region for block: B:485:0x0f08  */
    /* JADX WARNING: Removed duplicated region for block: B:488:0x0f29  */
    /* JADX WARNING: Removed duplicated region for block: B:586:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void generateOutputData(java.lang.String r43, boolean r44) {
        /*
            r42 = this;
            r1 = r42
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            com.taobao.onlinemonitor.OutputData r3 = new com.taobao.onlinemonitor.OutputData
            r3.<init>()
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo> r0 = r1.mActivityRuntimeInfoList
            if (r0 == 0) goto L_0x0f4a
            java.lang.String r0 = com.taobao.onlinemonitor.OnLineMonitorApp.sPropertyFilePath
            if (r0 != 0) goto L_0x0016
            goto L_0x0f4a
        L_0x0016:
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            long r4 = r0.mBootJiffyTime
            r6 = 0
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x002a
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.OnLineMonitor r4 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.ProcessCpuTracker r4 = r4.mProcessCpuTracker
            long r4 = r4.mPidJiffyTime
            r0.mBootJiffyTime = r4
        L_0x002a:
            r4 = 10
            r5 = 1
            r8 = 0
            int r0 = com.taobao.onlinemonitor.OnLineMonitor.sApiLevel     // Catch:{ Exception -> 0x007e }
            r9 = 23
            if (r0 < r9) goto L_0x007b
            int r0 = r1.mJiffyMillis     // Catch:{ Exception -> 0x007e }
            if (r0 != 0) goto L_0x007b
            java.lang.String r0 = "libcore.io.Libcore"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Exception -> 0x007e }
            java.lang.String r9 = "libcore.io.Os"
            java.lang.Class r9 = java.lang.Class.forName(r9)     // Catch:{ Exception -> 0x007e }
            java.lang.String r10 = "os"
            java.lang.reflect.Field r10 = r0.getDeclaredField(r10)     // Catch:{ Exception -> 0x007e }
            r10.setAccessible(r5)     // Catch:{ Exception -> 0x007e }
            java.lang.Object r0 = r10.get(r0)     // Catch:{ Exception -> 0x007e }
            java.lang.String r10 = "sysconf"
            java.lang.Class[] r11 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x007e }
            java.lang.Class r12 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x007e }
            r11[r8] = r12     // Catch:{ Exception -> 0x007e }
            java.lang.reflect.Method r9 = r9.getMethod(r10, r11)     // Catch:{ Exception -> 0x007e }
            r9.setAccessible(r5)     // Catch:{ Exception -> 0x007e }
            java.lang.Object[] r10 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x007e }
            int r11 = android.system.OsConstants._SC_CLK_TCK     // Catch:{ Exception -> 0x007e }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Exception -> 0x007e }
            r10[r8] = r11     // Catch:{ Exception -> 0x007e }
            java.lang.Object r0 = r9.invoke(r0, r10)     // Catch:{ Exception -> 0x007e }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Exception -> 0x007e }
            long r9 = r0.longValue()     // Catch:{ Exception -> 0x007e }
            r11 = 1000(0x3e8, double:4.94E-321)
            long r11 = r11 / r9
            int r0 = (int) r11     // Catch:{ Exception -> 0x007e }
            r1.mJiffyMillis = r0     // Catch:{ Exception -> 0x007e }
            goto L_0x0084
        L_0x007b:
            r1.mJiffyMillis = r4     // Catch:{ Exception -> 0x007e }
            goto L_0x0084
        L_0x007e:
            r0 = move-exception
            r0.printStackTrace()
            r1.mJiffyMillis = r4
        L_0x0084:
            boolean r0 = sTraceThread
            if (r0 == 0) goto L_0x008b
            r42.traceThreadInfo()
        L_0x008b:
            boolean r0 = sTraceThreadWait
            if (r0 != 0) goto L_0x0092
            r42.getThreadIoWaitTime()
        L_0x0092:
            java.util.HashMap r0 = r42.createBasicInfo()
            r3.basic = r0
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "mBootCpuTime"
            com.taobao.onlinemonitor.OnLineMonitor r9 = r1.mOnLineMonitor
            long r9 = r9.mBootJiffyTime
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "mBootDeviceCpuTime"
            long r9 = r1.mSystemTotalCpuTimeEnd
            long r11 = r1.mSystemTotalCpuTimeStart
            long r9 = r9 - r11
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "mJiffyMillis"
            int r9 = r1.mJiffyMillis
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "mActivityLifeCycleName"
            java.lang.String[] r9 = r1.mActivityLifeCycleName
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sEnableSimpleAnaliseOnDebug"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sEnableSimpleAnaliseOnDebug
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sBackInGroundOnBoot"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBackInGroundOnBoot
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sPublishRelease"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sPublishRelease
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sBgCpuUseTreshold"
            short r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBgCpuUseTreshold
            java.lang.Short r9 = java.lang.Short.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sMaxBootTotalTime"
            int r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sMaxBootTotalTime
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sInstanceOccupySize"
            int r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sInstanceOccupySize
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sDisableJitOnBoot"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sDisableJitOnBoot
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sSmoothStepInterval"
            int r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sSmoothStepInterval
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sHeapUtilization"
            float r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sHeapUtilization
            java.lang.Float r9 = java.lang.Float.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sBootExtraType"
            java.lang.String r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBootExtraType
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sIsCodeBoot"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsCodeBoot
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sLaunchTime"
            long r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sLaunchTime
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sFirstActivityTime"
            long r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sFirstActivityTime
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sBootActivityAry"
            java.lang.String[] r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBootActivityAry
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sBootAcitvityCount"
            short r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBootAcitvityCount
            java.lang.Short r9 = java.lang.Short.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sBootCorrectAry"
            boolean[] r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBootCorrectAry
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sIsBootCorrect"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsBootCorrect
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sIsLargeHeap"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsLargeHeap
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sIsHardWareAcce"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsHardWareAcce
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sColdBootCheck"
            com.taobao.onlinemonitor.ColdBootCheck r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sColdBootCheck
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sPropertiesFileName"
            java.lang.String r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sPropertiesFileName
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sMethodStartJitCompilation"
            java.lang.reflect.Method r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sMethodStartJitCompilation
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sMethodDisableJitCompilation"
            java.lang.reflect.Method r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sMethodDisableJitCompilation
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sVMRuntime"
            java.lang.Object r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sVMRuntime
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sEnableConfig"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sEnableConfig
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sToSleepTime"
            int r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sToSleepTime
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sMainThreadStartCpu"
            long r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sMainThreadStartCpu
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sPropertyFilePath"
            java.lang.String r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sPropertyFilePath
            r0.put(r4, r9)
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r3.basic
            java.lang.String r4 = "sIsStartMethodTrace"
            boolean r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsStartMethodTrace
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            r0.put(r4, r9)
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo> r0 = r1.mActivityRuntimeInfoList
            r3.activities = r0
            java.util.ArrayList r4 = new java.util.ArrayList
            r0 = 200(0xc8, float:2.8E-43)
            r4.<init>(r0)
            com.taobao.onlinemonitor.OnLineMonitor r9 = r1.mOnLineMonitor
            r9.checkMemoryLeack(r4)
            java.util.ArrayList r9 = new java.util.ArrayList
            com.taobao.onlinemonitor.OnLineMonitor r10 = r1.mOnLineMonitor
            java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r10 = r10.mThreadInfoHashMap
            int r10 = r10.size()
            r9.<init>(r10)
            r3.allthreads = r9
            com.taobao.onlinemonitor.OnLineMonitor r9 = r1.mOnLineMonitor
            java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r9 = r9.mThreadInfoHashMap
            java.util.Set r9 = r9.entrySet()
            java.util.Iterator r9 = r9.iterator()
        L_0x024d:
            boolean r10 = r9.hasNext()
            if (r10 == 0) goto L_0x028a
            java.lang.Object r10 = r9.next()
            java.util.Map$Entry r10 = (java.util.Map.Entry) r10
            if (r10 == 0) goto L_0x024d
            java.lang.Object r11 = r10.getKey()
            java.lang.String r11 = (java.lang.String) r11
            java.lang.Object r10 = r10.getValue()
            com.taobao.onlinemonitor.ThreadInfo r10 = (com.taobao.onlinemonitor.ThreadInfo) r10
            if (r11 == 0) goto L_0x024d
            if (r10 != 0) goto L_0x026c
            goto L_0x024d
        L_0x026c:
            java.lang.String r11 = r10.mName
            java.lang.String r12 = "\\u0000"
            java.lang.String r13 = ""
            java.lang.String r11 = r11.replaceAll(r12, r13)
            r10.mName = r11
            java.lang.String r11 = r10.mName
            java.lang.String r12 = "\\n"
            java.lang.String r13 = ""
            java.lang.String r11 = r11.replaceAll(r12, r13)
            r10.mName = r11
            java.util.List<com.taobao.onlinemonitor.ThreadInfo> r11 = r3.allthreads
            r11.add(r10)
            goto L_0x024d
        L_0x028a:
            java.util.Map r9 = java.lang.Thread.getAllStackTraces()
            if (r9 == 0) goto L_0x0308
            java.util.Set r10 = r9.entrySet()
            java.util.Iterator r10 = r10.iterator()
        L_0x0298:
            boolean r11 = r10.hasNext()
            if (r11 == 0) goto L_0x0308
            java.lang.Object r11 = r10.next()
            java.util.Map$Entry r11 = (java.util.Map.Entry) r11
            if (r11 == 0) goto L_0x0304
            java.lang.Object r12 = r11.getKey()
            java.lang.Thread r12 = (java.lang.Thread) r12
            if (r12 == 0) goto L_0x0304
            java.lang.Object r11 = r11.getValue()
            java.lang.StackTraceElement[] r11 = (java.lang.StackTraceElement[]) r11
            boolean r13 = r12 instanceof android.os.HandlerThread
            if (r13 == 0) goto L_0x02bb
            r18 = 1
            goto L_0x02bd
        L_0x02bb:
            r18 = 0
        L_0x02bd:
            r13 = 15
            java.lang.String r37 = com.taobao.onlinemonitor.OnLineMonitor.getStackTraceElement(r11, r8, r13)
            com.taobao.onlinemonitor.ThreadInfo r11 = new com.taobao.onlinemonitor.ThreadInfo
            r14 = r11
            long r5 = r12.getId()
            int r15 = (int) r5
            java.lang.String r16 = r12.getName()
            r17 = 0
            r19 = 0
            r21 = 0
            boolean r23 = r12.isDaemon()
            r24 = 0
            r25 = 0
            r27 = 0
            r29 = 0
            r31 = 0
            r32 = 0
            r33 = 0
            r34 = 0
            r35 = 0
            r36 = 0
            r14.<init>(r15, r16, r17, r18, r19, r21, r23, r24, r25, r27, r29, r31, r32, r33, r34, r35, r36, r37)
            java.util.List<com.taobao.onlinemonitor.ThreadInfo> r5 = r3.lastthreads
            if (r5 != 0) goto L_0x02ff
            java.util.ArrayList r5 = new java.util.ArrayList
            int r6 = r9.size()
            r5.<init>(r6)
            r3.lastthreads = r5
        L_0x02ff:
            java.util.List<com.taobao.onlinemonitor.ThreadInfo> r5 = r3.lastthreads
            r5.add(r11)
        L_0x0304:
            r5 = 1
            r6 = 0
            goto L_0x0298
        L_0x0308:
            java.util.ArrayList<com.taobao.onlinemonitor.ThreadInfo> r5 = r1.mExecuteThreadInfoList
            r3.statisticsthread = r5
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "mMainThreadTid"
            int r7 = r1.mMainThreadTid
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "mExecuteThreadInfoBootSize"
            int r7 = r1.mExecuteThreadInfoBootSize
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "mMainThreadPriority"
            int r7 = r1.mMainThreadPriority
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "packageName"
            com.taobao.onlinemonitor.OnLineMonitor r7 = r1.mOnLineMonitor
            android.content.Context r7 = r7.mContext
            java.lang.String r7 = r7.getPackageName()
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalInstanceCount"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalInstanceSize"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalInstanceRetainedSize"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalStaticCount"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalStaticSize"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalStaticRetainedSize"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalSingletonCount"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalSingletonSize"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.HashMap<java.lang.String, java.lang.Object> r5 = r3.basic
            java.lang.String r6 = "totalSingletonRetainedSize"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r5.put(r6, r7)
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$ThreadStackTraceTime> r5 = r1.mThreadStackTraceTimeList
            r3.mainthreadtime = r5
            if (r44 != 0) goto L_0x05ca
            com.taobao.onlinemonitor.OnLineMonitor r5 = r1.mOnLineMonitor
            android.content.Context r5 = r5.mContext
            r5.getPackageName()
            com.taobao.onlinemonitor.OnLineMonitor r5 = r1.mOnLineMonitor
            android.content.Context r5 = r5.mContext
            java.lang.String r6 = "activity"
            java.lang.Object r5 = r5.getSystemService(r6)
            android.app.ActivityManager r5 = (android.app.ActivityManager) r5
            if (r5 == 0) goto L_0x05ca
            java.util.List r0 = r5.getRunningServices(r0)
            java.util.List r6 = r5.getRunningAppProcesses()
            if (r6 == 0) goto L_0x04b4
            int r7 = r6.size()
            int[] r9 = new int[r7]
            r10 = 0
        L_0x03d1:
            if (r10 >= r7) goto L_0x03e0
            java.lang.Object r11 = r6.get(r10)
            android.app.ActivityManager$RunningAppProcessInfo r11 = (android.app.ActivityManager.RunningAppProcessInfo) r11
            int r11 = r11.pid
            r9[r10] = r11
            int r10 = r10 + 1
            goto L_0x03d1
        L_0x03e0:
            android.os.Debug$MemoryInfo[] r5 = r5.getProcessMemoryInfo(r9)
            com.taobao.onlinemonitor.TraceDetail$MemoryRunningApp[] r9 = new com.taobao.onlinemonitor.TraceDetail.MemoryRunningApp[r7]
            r10 = 0
        L_0x03e7:
            if (r10 >= r7) goto L_0x0403
            com.taobao.onlinemonitor.TraceDetail$MemoryRunningApp r11 = new com.taobao.onlinemonitor.TraceDetail$MemoryRunningApp
            r11.<init>()
            r9[r10] = r11
            r11 = r9[r10]
            r12 = r5[r10]
            r11.memoryInfo = r12
            r11 = r9[r10]
            java.lang.Object r12 = r6.get(r10)
            android.app.ActivityManager$RunningAppProcessInfo r12 = (android.app.ActivityManager.RunningAppProcessInfo) r12
            r11.app = r12
            int r10 = r10 + 1
            goto L_0x03e7
        L_0x0403:
            r5 = 0
        L_0x0404:
            int r6 = r7 + -1
            if (r5 >= r6) goto L_0x042c
            int r6 = r5 + 1
            r10 = r6
        L_0x040b:
            if (r10 >= r7) goto L_0x042a
            r11 = r9[r5]
            android.os.Debug$MemoryInfo r11 = r11.memoryInfo
            int r11 = r11.getTotalPss()
            r12 = r9[r10]
            android.os.Debug$MemoryInfo r12 = r12.memoryInfo
            int r12 = r12.getTotalPss()
            if (r11 >= r12) goto L_0x0427
            r11 = r9[r5]
            r12 = r9[r10]
            r9[r5] = r12
            r9[r10] = r11
        L_0x0427:
            int r10 = r10 + 1
            goto L_0x040b
        L_0x042a:
            r5 = r6
            goto L_0x0404
        L_0x042c:
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r3.bgapps = r5
            r5 = 0
        L_0x0434:
            if (r5 >= r7) goto L_0x04b4
            r6 = 30
            if (r5 >= r6) goto L_0x04b4
            r6 = r9[r5]
            android.app.ActivityManager$RunningAppProcessInfo r10 = r6.app
            android.os.Debug$MemoryInfo r6 = r6.memoryInfo
            com.taobao.onlinemonitor.OutputData$BackgroundAppInfo r11 = new com.taobao.onlinemonitor.OutputData$BackgroundAppInfo
            r11.<init>()
            int r5 = r5 + 1
            r11.id = r5
            java.lang.String r12 = r10.processName
            r11.processName = r12
            int r12 = r6.getTotalPss()
            int r12 = r12 / 1024
            r11.totalMem = r12
            int r12 = r6.dalvikPss
            int r12 = r12 / 1024
            r11.javaMem = r12
            int r6 = r6.getTotalSharedDirty()
            int r6 = r6 / 1024
            r11.sharedMem = r6
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r11.serviceInfo = r6
            if (r0 == 0) goto L_0x04ae
            java.util.Iterator r6 = r0.iterator()
        L_0x0470:
            boolean r12 = r6.hasNext()
            if (r12 == 0) goto L_0x04ae
            java.lang.Object r12 = r6.next()
            android.app.ActivityManager$RunningServiceInfo r12 = (android.app.ActivityManager.RunningServiceInfo) r12
            java.lang.String r13 = r12.process
            java.lang.String r14 = r10.processName
            boolean r13 = r13.contains(r14)
            if (r13 == 0) goto L_0x0470
            int r13 = r12.pid
            int r14 = r10.pid
            if (r13 != r14) goto L_0x0470
            java.util.List<java.lang.String> r13 = r11.serviceInfo
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            android.content.ComponentName r15 = r12.service
            java.lang.String r15 = r15.getClassName()
            r14.append(r15)
            java.lang.String r15 = " &nbsp;&nbsp;进程: "
            r14.append(r15)
            java.lang.String r12 = r12.process
            r14.append(r12)
            java.lang.String r12 = r14.toString()
            r13.add(r12)
            goto L_0x0470
        L_0x04ae:
            java.util.List<com.taobao.onlinemonitor.OutputData$BackgroundAppInfo> r6 = r3.bgapps
            r6.add(r11)
            goto L_0x0434
        L_0x04b4:
            boolean r0 = sTraceBootProgress
            if (r0 == 0) goto L_0x05ca
            android.util.SparseArray<java.lang.Long> r0 = r1.mSparseArrayBootProgressEnd
            if (r0 == 0) goto L_0x05ca
            android.util.SparseArray<java.lang.Long> r0 = r1.mSparseArrayBootProgressStart
            if (r0 == 0) goto L_0x05ca
            r0 = 0
        L_0x04c1:
            android.util.SparseArray<java.lang.Long> r5 = r1.mSparseArrayBootProgressEnd
            int r5 = r5.size()
            if (r0 >= r5) goto L_0x04e5
            android.util.SparseArray<java.lang.Long> r5 = r1.mSparseArrayBootProgressEnd
            int r5 = r5.keyAt(r0)
            android.util.SparseArray<java.lang.Long> r6 = r1.mSparseArrayBootProgressStart
            java.lang.Object r6 = r6.get(r5)
            if (r6 != 0) goto L_0x04e2
            android.util.SparseArray<java.lang.Long> r6 = r1.mSparseArrayBootProgressStart
            r9 = 0
            java.lang.Long r7 = java.lang.Long.valueOf(r9)
            r6.put(r5, r7)
        L_0x04e2:
            int r0 = r0 + 1
            goto L_0x04c1
        L_0x04e5:
            long r5 = r1.mSystemTotalCpuTimeEnd
            long r9 = r1.mSystemTotalCpuTimeStart
            long r5 = r5 - r9
            long r9 = r1.mSystemRunCpuTimeEnd
            long r11 = r1.mSystemRunCpuTimeStart
            long r9 = r9 - r11
            r11 = 0
            int r0 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1))
            if (r0 <= 0) goto L_0x05ca
            android.util.SparseArray<java.lang.Long> r0 = r1.mSparseArrayBootProgressStart
            int r0 = r0.size()
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>(r0)
            r11 = 0
        L_0x0501:
            if (r11 >= r0) goto L_0x0577
            android.util.SparseArray<java.lang.Long> r12 = r1.mSparseArrayBootProgressStart
            int r12 = r12.keyAt(r11)
            android.util.SparseArray<java.lang.Long> r13 = r1.mSparseArrayBootProgressStart
            java.lang.Object r13 = r13.get(r12)
            java.lang.Long r13 = (java.lang.Long) r13
            android.util.SparseArray<java.lang.Long> r14 = r1.mSparseArrayBootProgressEnd
            java.lang.Object r14 = r14.get(r12)
            java.lang.Long r14 = (java.lang.Long) r14
            if (r13 == 0) goto L_0x0524
            boolean r15 = r13.equals(r14)
            if (r15 == 0) goto L_0x0524
            r38 = r3
            goto L_0x0571
        L_0x0524:
            com.taobao.onlinemonitor.TraceDetail$PinCpuTime r15 = new com.taobao.onlinemonitor.TraceDetail$PinCpuTime
            r15.<init>()
            r15.pid = r12
            android.util.SparseArray<java.lang.String> r8 = r1.mSparseArrayBootProgressName
            java.lang.Object r8 = r8.get(r12)
            java.lang.String r8 = (java.lang.String) r8
            r15.name = r8
            if (r13 == 0) goto L_0x053e
            if (r14 != 0) goto L_0x053e
            r12 = -1
            r15.cputime = r12
            goto L_0x056c
        L_0x053e:
            if (r14 == 0) goto L_0x056c
            if (r13 != 0) goto L_0x0545
            r12 = 0
            goto L_0x0549
        L_0x0545:
            long r12 = r13.longValue()
        L_0x0549:
            long r16 = r14.longValue()
            long r12 = r16 - r12
            r15.cputime = r12
            long r12 = r15.cputime
            float r8 = (float) r12
            r12 = 1120403456(0x42c80000, float:100.0)
            float r8 = r8 * r12
            float r12 = (float) r5
            float r8 = r8 / r12
            r15.percent = r8
            long r12 = r15.cputime
            com.taobao.onlinemonitor.OnLineMonitor r8 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.ProcessCpuTracker r8 = r8.mProcessCpuTracker
            int r8 = r8.mJiffyMillis
            r38 = r3
            long r2 = (long) r8
            long r12 = r12 * r2
            r15.cputime = r12
            goto L_0x056e
        L_0x056c:
            r38 = r3
        L_0x056e:
            r7.add(r15)
        L_0x0571:
            int r11 = r11 + 1
            r3 = r38
            r8 = 0
            goto L_0x0501
        L_0x0577:
            r38 = r3
            int r0 = r7.size()
            com.taobao.onlinemonitor.TraceDetail$PinCpuTime[] r2 = new com.taobao.onlinemonitor.TraceDetail.PinCpuTime[r0]
            r3 = 0
        L_0x0580:
            if (r3 >= r0) goto L_0x058d
            java.lang.Object r8 = r7.get(r3)
            com.taobao.onlinemonitor.TraceDetail$PinCpuTime r8 = (com.taobao.onlinemonitor.TraceDetail.PinCpuTime) r8
            r2[r3] = r8
            int r3 = r3 + 1
            goto L_0x0580
        L_0x058d:
            if (r0 <= 0) goto L_0x05c7
            r7 = 100
            long r7 = r7 * r9
            long r7 = r7 / r5
            int r0 = (int) r7
            com.taobao.onlinemonitor.TraceDetail$PinCpuTime r3 = new com.taobao.onlinemonitor.TraceDetail$PinCpuTime
            r3.<init>()
            com.taobao.onlinemonitor.OnLineMonitor r5 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.ProcessCpuTracker r5 = r5.mProcessCpuTracker
            int r5 = r5.mJiffyMillis
            long r5 = (long) r5
            long r9 = r9 * r5
            r3.cputime = r9
            r5 = 0
            r3.pid = r5
            float r0 = (float) r0
            r3.percent = r0
            java.lang.String r0 = "启动阶段系统CPU平均使用率"
            r3.name = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5 = r38
            r5.bootcpu = r0
            java.util.List<com.taobao.onlinemonitor.TraceDetail$PinCpuTime> r0 = r5.bootcpu
            r0.add(r3)
            java.util.List<com.taobao.onlinemonitor.TraceDetail$PinCpuTime> r0 = r5.bootcpu
            java.util.List r2 = java.util.Arrays.asList(r2)
            r0.addAll(r2)
            goto L_0x05cb
        L_0x05c7:
            r5 = r38
            goto L_0x05cb
        L_0x05ca:
            r5 = r3
        L_0x05cb:
            java.util.LinkedHashMap<java.lang.String, com.taobao.onlinemonitor.TraceDetail$ThreadPoolInfo> r0 = r1.mThreadPoolInfoMap
            if (r0 == 0) goto L_0x0624
            if (r44 != 0) goto L_0x0624
            java.util.LinkedHashMap<java.lang.String, com.taobao.onlinemonitor.TraceDetail$ThreadPoolInfo> r0 = r1.mThreadPoolInfoMap     // Catch:{ Exception -> 0x0620 }
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x0620 }
            if (r0 == 0) goto L_0x0624
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ Exception -> 0x0620 }
            r6.<init>()     // Catch:{ Exception -> 0x0620 }
            r5.threadpoolinfo = r6     // Catch:{ Exception -> 0x0620 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x0620 }
        L_0x05e4:
            boolean r6 = r0.hasNext()     // Catch:{ Exception -> 0x0620 }
            if (r6 == 0) goto L_0x0624
            java.lang.Object r6 = r0.next()     // Catch:{ Exception -> 0x0620 }
            java.util.Map$Entry r6 = (java.util.Map.Entry) r6     // Catch:{ Exception -> 0x0620 }
            java.lang.Object r6 = r6.getValue()     // Catch:{ Exception -> 0x0620 }
            com.taobao.onlinemonitor.TraceDetail$ThreadPoolInfo r6 = (com.taobao.onlinemonitor.TraceDetail.ThreadPoolInfo) r6     // Catch:{ Exception -> 0x0620 }
            if (r6 != 0) goto L_0x05f9
            goto L_0x05e4
        L_0x05f9:
            int r7 = r6.waitExecuteCount     // Catch:{ Exception -> 0x0620 }
            if (r7 != 0) goto L_0x0600
            r7 = 1
            r6.waitExecuteCount = r7     // Catch:{ Exception -> 0x0620 }
        L_0x0600:
            java.lang.ref.WeakReference<java.util.concurrent.ThreadPoolExecutor> r7 = r6.threadPoolExecutor     // Catch:{ Exception -> 0x0620 }
            if (r7 != 0) goto L_0x0606
            r7 = 0
            goto L_0x060c
        L_0x0606:
            java.lang.Object r7 = r7.get()     // Catch:{ Exception -> 0x0620 }
            java.util.concurrent.ThreadPoolExecutor r7 = (java.util.concurrent.ThreadPoolExecutor) r7     // Catch:{ Exception -> 0x0620 }
        L_0x060c:
            if (r7 == 0) goto L_0x061a
            int r8 = r7.getActiveCount()     // Catch:{ Exception -> 0x0620 }
            r6.activeCount = r8     // Catch:{ Exception -> 0x0620 }
            long r7 = r7.getCompletedTaskCount()     // Catch:{ Exception -> 0x0620 }
            r6.completeCount = r7     // Catch:{ Exception -> 0x0620 }
        L_0x061a:
            java.util.List<com.taobao.onlinemonitor.TraceDetail$ThreadPoolInfo> r7 = r5.threadpoolinfo     // Catch:{ Exception -> 0x0620 }
            r7.add(r6)     // Catch:{ Exception -> 0x0620 }
            goto L_0x05e4
        L_0x0620:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0624:
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.mBootDiffThreadMap
            r5.mBootDiffThreadMap = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$NewThreadInfo> r0 = r1.mThreadInfoList
            r5.newthreadinfo = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$NewThreadInfo> r0 = r1.mAsyncTaskInfoList
            r5.asynctaskinfo = r0
            java.lang.String[] r0 = r1.mLifeCycleArray
            java.lang.String r6 = r1.checkLifiCycle(r4)
            r7 = 1
            r0[r7] = r6
            java.lang.String[] r0 = r1.mLifeCycleArray
            r6 = 0
            r0 = r0[r6]
            r5.bootLifecycle = r0
            java.lang.String[] r0 = r1.mLifeCycleArray
            r0 = r0[r7]
            r5.leakLifecycle = r0
            if (r44 != 0) goto L_0x0b0d
            java.lang.String r0 = "android.app.LoadedApk"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x07e7 }
            java.lang.String r6 = "mReceivers"
            java.lang.reflect.Field r6 = r0.getDeclaredField(r6)     // Catch:{ Throwable -> 0x07e7 }
            java.lang.String r7 = "mServices"
            java.lang.reflect.Field r7 = r0.getDeclaredField(r7)     // Catch:{ Throwable -> 0x07e7 }
            java.lang.Class<android.app.Application> r0 = android.app.Application.class
            java.lang.String r8 = "mLoadedApk"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r8)     // Catch:{ Throwable -> 0x07e7 }
            r8 = 1
            r6.setAccessible(r8)     // Catch:{ Throwable -> 0x07e7 }
            r0.setAccessible(r8)     // Catch:{ Throwable -> 0x07e7 }
            r7.setAccessible(r8)     // Catch:{ Throwable -> 0x07e7 }
            android.app.Application r8 = com.taobao.onlinemonitor.OnLineMonitorApp.sApplication     // Catch:{ Throwable -> 0x07e7 }
            java.lang.Object r8 = r0.get(r8)     // Catch:{ Throwable -> 0x07e7 }
            java.lang.Object r0 = r6.get(r8)     // Catch:{ Throwable -> 0x07e7 }
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ Throwable -> 0x07e7 }
            if (r0 == 0) goto L_0x0729
            java.lang.String r6 = "android.app.LoadedApk$ReceiverDispatcher"
            java.lang.Class r6 = java.lang.Class.forName(r6)     // Catch:{ Throwable -> 0x068b }
            java.lang.String r9 = "mContext"
            java.lang.reflect.Field r6 = r6.getDeclaredField(r9)     // Catch:{ Throwable -> 0x068b }
            r9 = 1
            r6.setAccessible(r9)     // Catch:{ Throwable -> 0x068c }
            goto L_0x068c
        L_0x068b:
            r6 = 0
        L_0x068c:
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x0725 }
            if (r0 == 0) goto L_0x0729
            java.util.ArrayList r9 = new java.util.ArrayList     // Catch:{ Exception -> 0x0725 }
            r9.<init>()     // Catch:{ Exception -> 0x0725 }
            r5.leakbroadcast = r9     // Catch:{ Exception -> 0x0725 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x0725 }
        L_0x069d:
            boolean r9 = r0.hasNext()     // Catch:{ Exception -> 0x0725 }
            if (r9 == 0) goto L_0x0729
            java.lang.Object r9 = r0.next()     // Catch:{ Exception -> 0x0725 }
            java.util.Map$Entry r9 = (java.util.Map.Entry) r9     // Catch:{ Exception -> 0x0725 }
            java.lang.Object r9 = r9.getValue()     // Catch:{ Exception -> 0x0725 }
            java.util.Map r9 = (java.util.Map) r9     // Catch:{ Exception -> 0x0725 }
            if (r9 == 0) goto L_0x069d
            java.util.Set r9 = r9.entrySet()     // Catch:{ Exception -> 0x0725 }
            if (r9 == 0) goto L_0x069d
            java.util.Iterator r9 = r9.iterator()     // Catch:{ Exception -> 0x0725 }
        L_0x06bb:
            boolean r10 = r9.hasNext()     // Catch:{ Exception -> 0x0725 }
            if (r10 == 0) goto L_0x069d
            java.lang.Object r10 = r9.next()     // Catch:{ Exception -> 0x0725 }
            java.util.Map$Entry r10 = (java.util.Map.Entry) r10     // Catch:{ Exception -> 0x0725 }
            java.lang.Object r11 = r10.getKey()     // Catch:{ Exception -> 0x0725 }
            java.lang.Object r10 = r10.getValue()     // Catch:{ Exception -> 0x0725 }
            java.lang.Class r11 = r11.getClass()     // Catch:{ Exception -> 0x0725 }
            java.lang.String r11 = r11.getName()     // Catch:{ Exception -> 0x0725 }
            if (r6 == 0) goto L_0x071f
            if (r10 == 0) goto L_0x071f
            java.lang.Object r10 = r6.get(r10)     // Catch:{ Throwable -> 0x071f }
            boolean r12 = r10 instanceof android.app.Activity     // Catch:{ Throwable -> 0x071f }
            if (r12 == 0) goto L_0x071f
            r12 = r10
            android.app.Activity r12 = (android.app.Activity) r12     // Catch:{ Throwable -> 0x071f }
            boolean r12 = r12.isFinishing()     // Catch:{ Throwable -> 0x071f }
            if (r12 == 0) goto L_0x071f
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x071f }
            r12.<init>()     // Catch:{ Throwable -> 0x071f }
            r12.append(r11)     // Catch:{ Throwable -> 0x071f }
            java.lang.String r13 = " ，该广播泄漏了Activity："
            r12.append(r13)     // Catch:{ Throwable -> 0x071f }
            java.lang.Class r10 = r10.getClass()     // Catch:{ Throwable -> 0x071f }
            java.lang.String r10 = r10.getName()     // Catch:{ Throwable -> 0x071f }
            r12.append(r10)     // Catch:{ Throwable -> 0x071f }
            java.lang.String r10 = r12.toString()     // Catch:{ Throwable -> 0x071f }
            java.lang.String r11 = "OnLineMonitor"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x071e }
            r12.<init>()     // Catch:{ Throwable -> 0x071e }
            java.lang.String r13 = "内存泄漏："
            r12.append(r13)     // Catch:{ Throwable -> 0x071e }
            r12.append(r10)     // Catch:{ Throwable -> 0x071e }
            java.lang.String r12 = r12.toString()     // Catch:{ Throwable -> 0x071e }
            android.util.Log.e(r11, r12)     // Catch:{ Throwable -> 0x071e }
        L_0x071e:
            r11 = r10
        L_0x071f:
            java.util.List<java.lang.String> r10 = r5.leakbroadcast     // Catch:{ Exception -> 0x0725 }
            r10.add(r11)     // Catch:{ Exception -> 0x0725 }
            goto L_0x06bb
        L_0x0725:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x07e7 }
        L_0x0729:
            java.lang.Object r0 = r7.get(r8)     // Catch:{ Throwable -> 0x07e7 }
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ Throwable -> 0x07e7 }
            if (r0 == 0) goto L_0x07eb
            java.lang.String r6 = "android.app.LoadedApk$ServiceDispatcher"
            java.lang.Class r6 = java.lang.Class.forName(r6)     // Catch:{ Throwable -> 0x0742 }
            java.lang.String r7 = "mContext"
            java.lang.reflect.Field r6 = r6.getDeclaredField(r7)     // Catch:{ Throwable -> 0x0742 }
            r7 = 1
            r6.setAccessible(r7)     // Catch:{ Throwable -> 0x0743 }
            goto L_0x0743
        L_0x0742:
            r6 = 0
        L_0x0743:
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x07e2 }
            if (r0 == 0) goto L_0x07eb
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ Exception -> 0x07e2 }
            r7.<init>()     // Catch:{ Exception -> 0x07e2 }
            r5.leakservice = r7     // Catch:{ Exception -> 0x07e2 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x07e2 }
        L_0x0754:
            boolean r7 = r0.hasNext()     // Catch:{ Exception -> 0x07e2 }
            if (r7 == 0) goto L_0x07eb
            java.lang.Object r7 = r0.next()     // Catch:{ Exception -> 0x07e2 }
            java.util.Map$Entry r7 = (java.util.Map.Entry) r7     // Catch:{ Exception -> 0x07e2 }
            java.lang.Object r7 = r7.getValue()     // Catch:{ Exception -> 0x07e2 }
            java.util.Map r7 = (java.util.Map) r7     // Catch:{ Exception -> 0x07e2 }
            if (r7 == 0) goto L_0x0754
            java.util.Set r7 = r7.entrySet()     // Catch:{ Exception -> 0x07e2 }
            if (r7 == 0) goto L_0x0754
            java.util.Iterator r7 = r7.iterator()     // Catch:{ Exception -> 0x07e2 }
        L_0x0772:
            boolean r8 = r7.hasNext()     // Catch:{ Exception -> 0x07e2 }
            if (r8 == 0) goto L_0x0754
            java.lang.Object r8 = r7.next()     // Catch:{ Exception -> 0x07e2 }
            java.util.Map$Entry r8 = (java.util.Map.Entry) r8     // Catch:{ Exception -> 0x07e2 }
            java.lang.Object r9 = r8.getKey()     // Catch:{ Exception -> 0x07e2 }
            if (r9 == 0) goto L_0x0772
            java.lang.Object r9 = r8.getKey()     // Catch:{ Exception -> 0x07e2 }
            java.lang.Object r8 = r8.getValue()     // Catch:{ Exception -> 0x07e2 }
            java.lang.Class r9 = r9.getClass()     // Catch:{ Exception -> 0x07e2 }
            java.lang.String r9 = r9.getName()     // Catch:{ Exception -> 0x07e2 }
            if (r6 == 0) goto L_0x07dc
            if (r8 == 0) goto L_0x07dc
            java.lang.Object r8 = r6.get(r8)     // Catch:{ Throwable -> 0x07dc }
            boolean r10 = r8 instanceof android.app.Activity     // Catch:{ Throwable -> 0x07dc }
            if (r10 == 0) goto L_0x07dc
            r10 = r8
            android.app.Activity r10 = (android.app.Activity) r10     // Catch:{ Throwable -> 0x07dc }
            boolean r10 = r10.isFinishing()     // Catch:{ Throwable -> 0x07dc }
            if (r10 == 0) goto L_0x07dc
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x07dc }
            r10.<init>()     // Catch:{ Throwable -> 0x07dc }
            r10.append(r9)     // Catch:{ Throwable -> 0x07dc }
            java.lang.String r11 = " ，该Service泄漏了Activity："
            r10.append(r11)     // Catch:{ Throwable -> 0x07dc }
            java.lang.Class r8 = r8.getClass()     // Catch:{ Throwable -> 0x07dc }
            java.lang.String r8 = r8.getName()     // Catch:{ Throwable -> 0x07dc }
            r10.append(r8)     // Catch:{ Throwable -> 0x07dc }
            java.lang.String r8 = r10.toString()     // Catch:{ Throwable -> 0x07dc }
            java.lang.String r9 = "OnLineMonitor"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x07db }
            r10.<init>()     // Catch:{ Throwable -> 0x07db }
            java.lang.String r11 = "内存泄漏："
            r10.append(r11)     // Catch:{ Throwable -> 0x07db }
            r10.append(r8)     // Catch:{ Throwable -> 0x07db }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x07db }
            android.util.Log.e(r9, r10)     // Catch:{ Throwable -> 0x07db }
        L_0x07db:
            r9 = r8
        L_0x07dc:
            java.util.List<java.lang.String> r8 = r5.leakservice     // Catch:{ Exception -> 0x07e2 }
            r8.add(r9)     // Catch:{ Exception -> 0x07e2 }
            goto L_0x0772
        L_0x07e2:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x07e7 }
            goto L_0x07eb
        L_0x07e7:
            r0 = move-exception
            r0.printStackTrace()
        L_0x07eb:
            java.lang.String r0 = "androidx.localbroadcastmanager.content.LocalBroadcastManager"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Exception -> 0x088d }
            java.lang.String r6 = "getInstance"
            r7 = 1
            java.lang.Class[] r8 = new java.lang.Class[r7]     // Catch:{ Exception -> 0x088d }
            java.lang.Class<android.content.Context> r9 = android.content.Context.class
            r10 = 0
            r8[r10] = r9     // Catch:{ Exception -> 0x088d }
            java.lang.reflect.Method r6 = r0.getDeclaredMethod(r6, r8)     // Catch:{ Exception -> 0x088d }
            r6.setAccessible(r7)     // Catch:{ Exception -> 0x088d }
            java.lang.Object[] r8 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x088d }
            com.taobao.onlinemonitor.OnLineMonitor r7 = r1.mOnLineMonitor     // Catch:{ Exception -> 0x088d }
            android.content.Context r7 = r7.mContext     // Catch:{ Exception -> 0x088d }
            r9 = 0
            r8[r9] = r7     // Catch:{ Exception -> 0x088d }
            java.lang.Object r6 = r6.invoke(r0, r8)     // Catch:{ Exception -> 0x088d }
            if (r6 == 0) goto L_0x0891
            java.lang.String r7 = "mReceivers"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r7)     // Catch:{ Exception -> 0x088d }
            r7 = 1
            r0.setAccessible(r7)     // Catch:{ Exception -> 0x088d }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ Exception -> 0x088d }
            java.util.HashMap r0 = (java.util.HashMap) r0     // Catch:{ Exception -> 0x088d }
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x088d }
            if (r0 == 0) goto L_0x0891
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ Exception -> 0x088d }
            r6.<init>()     // Catch:{ Exception -> 0x088d }
            r5.leaklocalbroadcast = r6     // Catch:{ Exception -> 0x088d }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x088d }
        L_0x0832:
            boolean r6 = r0.hasNext()     // Catch:{ Exception -> 0x088d }
            if (r6 == 0) goto L_0x0891
            java.lang.Object r6 = r0.next()     // Catch:{ Exception -> 0x088d }
            java.util.Map$Entry r6 = (java.util.Map.Entry) r6     // Catch:{ Exception -> 0x088d }
            com.taobao.onlinemonitor.OutputData$LocalBroadReceiverInfo r7 = new com.taobao.onlinemonitor.OutputData$LocalBroadReceiverInfo     // Catch:{ Exception -> 0x088d }
            r7.<init>()     // Catch:{ Exception -> 0x088d }
            java.lang.Object r8 = r6.getKey()     // Catch:{ Exception -> 0x088d }
            android.content.BroadcastReceiver r8 = (android.content.BroadcastReceiver) r8     // Catch:{ Exception -> 0x088d }
            java.lang.Class r8 = r8.getClass()     // Catch:{ Exception -> 0x088d }
            java.lang.String r8 = r8.getName()     // Catch:{ Exception -> 0x088d }
            r7.receiverName = r8     // Catch:{ Exception -> 0x088d }
            java.lang.Object r6 = r6.getValue()     // Catch:{ Exception -> 0x088d }
            java.util.ArrayList r6 = (java.util.ArrayList) r6     // Catch:{ Exception -> 0x088d }
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ Exception -> 0x088d }
            r8.<init>()     // Catch:{ Exception -> 0x088d }
            r7.actions = r8     // Catch:{ Exception -> 0x088d }
            if (r6 == 0) goto L_0x0887
            int r8 = r6.size()     // Catch:{ Exception -> 0x088d }
            r9 = 0
        L_0x0867:
            if (r9 >= r8) goto L_0x0887
            java.lang.Object r10 = r6.get(r9)     // Catch:{ Exception -> 0x088d }
            android.content.IntentFilter r10 = (android.content.IntentFilter) r10     // Catch:{ Exception -> 0x088d }
            r11 = 0
        L_0x0870:
            if (r10 == 0) goto L_0x0884
            int r12 = r10.countActions()     // Catch:{ Exception -> 0x088d }
            if (r11 >= r12) goto L_0x0884
            java.lang.String r12 = r10.getAction(r11)     // Catch:{ Exception -> 0x088d }
            java.util.List<java.lang.String> r13 = r7.actions     // Catch:{ Exception -> 0x088d }
            r13.add(r12)     // Catch:{ Exception -> 0x088d }
            int r11 = r11 + 1
            goto L_0x0870
        L_0x0884:
            int r9 = r9 + 1
            goto L_0x0867
        L_0x0887:
            java.util.List<com.taobao.onlinemonitor.OutputData$LocalBroadReceiverInfo> r6 = r5.leaklocalbroadcast     // Catch:{ Exception -> 0x088d }
            r6.add(r7)     // Catch:{ Exception -> 0x088d }
            goto L_0x0832
        L_0x088d:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0891:
            java.lang.Class<android.app.Application> r0 = android.app.Application.class
            java.lang.String r6 = "mComponentCallbacks"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r6)     // Catch:{ Exception -> 0x08c8 }
            r6 = 1
            r0.setAccessible(r6)     // Catch:{ Exception -> 0x08c8 }
            android.app.Application r6 = com.taobao.onlinemonitor.OnLineMonitorApp.sApplication     // Catch:{ Exception -> 0x08c8 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ Exception -> 0x08c8 }
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Exception -> 0x08c8 }
            if (r0 == 0) goto L_0x08cc
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ Exception -> 0x08c8 }
            r6.<init>()     // Catch:{ Exception -> 0x08c8 }
            r5.componentcallbacks = r6     // Catch:{ Exception -> 0x08c8 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x08c8 }
        L_0x08b2:
            boolean r6 = r0.hasNext()     // Catch:{ Exception -> 0x08c8 }
            if (r6 == 0) goto L_0x08cc
            java.lang.Object r6 = r0.next()     // Catch:{ Exception -> 0x08c8 }
            android.content.ComponentCallbacks r6 = (android.content.ComponentCallbacks) r6     // Catch:{ Exception -> 0x08c8 }
            java.util.List<java.lang.String> r7 = r5.componentcallbacks     // Catch:{ Exception -> 0x08c8 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x08c8 }
            r7.add(r6)     // Catch:{ Exception -> 0x08c8 }
            goto L_0x08b2
        L_0x08c8:
            r0 = move-exception
            r0.printStackTrace()
        L_0x08cc:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.io.File r6 = android.os.Environment.getDataDirectory()
            r0.append(r6)
            java.lang.String r6 = "/data/"
            r0.append(r6)
            com.taobao.onlinemonitor.OnLineMonitor r6 = r1.mOnLineMonitor
            android.content.Context r6 = r6.mContext
            java.lang.String r6 = r6.getPackageName()
            r0.append(r6)
            java.lang.String r6 = "/shared_prefs"
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            java.io.File r6 = new java.io.File
            r6.<init>(r0)
            boolean r0 = r6.exists()
            if (r0 == 0) goto L_0x0b0c
            boolean r0 = r6.isDirectory()
            if (r0 != 0) goto L_0x0904
            goto L_0x0b0c
        L_0x0904:
            java.io.File[] r0 = r6.listFiles()
            if (r0 != 0) goto L_0x090b
            return
        L_0x090b:
            int r6 = r0.length
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r5.sharedpreference = r7
            r7 = 0
        L_0x0914:
            if (r7 >= r6) goto L_0x0a20
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            r9 = r0[r7]
            long r10 = r9.length()
            float r10 = (float) r10
            r11 = 1149239296(0x44800000, float:1024.0)
            float r10 = r10 / r11
            java.lang.String r11 = "name"
            java.lang.String r12 = r9.getName()
            r8.put(r11, r12)
            java.lang.String r11 = "size"
            java.lang.Float r10 = java.lang.Float.valueOf(r10)
            r8.put(r11, r10)
            java.io.FileReader r10 = new java.io.FileReader     // Catch:{ Exception -> 0x0a0b }
            r10.<init>(r9)     // Catch:{ Exception -> 0x0a0b }
            java.io.BufferedReader r9 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0a0b }
            r9.<init>(r10)     // Catch:{ Exception -> 0x0a0b }
            java.lang.String r10 = r9.readLine()     // Catch:{ Exception -> 0x0a0b }
            java.util.ArrayList r11 = new java.util.ArrayList     // Catch:{ Exception -> 0x0a0b }
            r11.<init>()     // Catch:{ Exception -> 0x0a0b }
            java.util.ArrayList r12 = new java.util.ArrayList     // Catch:{ Exception -> 0x0a0b }
            r12.<init>()     // Catch:{ Exception -> 0x0a0b }
            java.util.ArrayList r13 = new java.util.ArrayList     // Catch:{ Exception -> 0x0a0b }
            r13.<init>()     // Catch:{ Exception -> 0x0a0b }
        L_0x0954:
            if (r10 == 0) goto L_0x09f2
            java.lang.String r14 = "\">"
            int r14 = r10.indexOf(r14)     // Catch:{ Exception -> 0x0a0b }
            java.lang.String r15 = "</"
            int r15 = r10.lastIndexOf(r15)     // Catch:{ Exception -> 0x0a0b }
            int r2 = r14 + 2
            if (r15 <= 0) goto L_0x09e0
            if (r14 <= 0) goto L_0x09e0
            r39 = r0
            java.lang.String r0 = "name=\""
            int r0 = r10.indexOf(r0)     // Catch:{ Exception -> 0x0a0d }
            int r15 = r15 - r2
            r40 = r6
            r6 = 100
            if (r15 <= r6) goto L_0x099b
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0997 }
            r6.<init>()     // Catch:{ Exception -> 0x0997 }
            r41 = r4
            int r4 = r0 + 6
            java.lang.String r4 = r10.substring(r4, r14)     // Catch:{ Exception -> 0x0a11 }
            r6.append(r4)     // Catch:{ Exception -> 0x0a11 }
            java.lang.String r4 = ": "
            r6.append(r4)     // Catch:{ Exception -> 0x0a11 }
            r6.append(r15)     // Catch:{ Exception -> 0x0a11 }
            java.lang.String r4 = r6.toString()     // Catch:{ Exception -> 0x0a11 }
            r11.add(r4)     // Catch:{ Exception -> 0x0a11 }
            goto L_0x099d
        L_0x0997:
            r41 = r4
            goto L_0x0a11
        L_0x099b:
            r41 = r4
        L_0x099d:
            java.lang.String r4 = "&quot;"
            int r4 = r10.indexOf(r4, r2)     // Catch:{ Exception -> 0x0a11 }
            if (r4 <= 0) goto L_0x09b6
            r4 = 123(0x7b, float:1.72E-43)
            int r4 = r10.indexOf(r4, r2)     // Catch:{ Exception -> 0x0a11 }
            if (r4 <= 0) goto L_0x09b6
            int r4 = r0 + 6
            java.lang.String r4 = r10.substring(r4, r14)     // Catch:{ Exception -> 0x0a11 }
            r12.add(r4)     // Catch:{ Exception -> 0x0a11 }
        L_0x09b6:
            java.lang.String r4 = "&amp;"
            int r4 = r10.indexOf(r4, r2)     // Catch:{ Exception -> 0x0a11 }
            if (r4 <= 0) goto L_0x09e6
            java.lang.String r4 = "&lt;"
            int r4 = r10.indexOf(r4, r2)     // Catch:{ Exception -> 0x0a11 }
            if (r4 <= 0) goto L_0x09e6
            java.lang.String r4 = "&gt;"
            int r4 = r10.indexOf(r4, r2)     // Catch:{ Exception -> 0x0a11 }
            if (r4 <= 0) goto L_0x09e6
            java.lang.String r4 = "&nbsp;"
            int r2 = r10.indexOf(r4, r2)     // Catch:{ Exception -> 0x0a11 }
            if (r2 <= 0) goto L_0x09e6
            int r0 = r0 + 6
            java.lang.String r0 = r10.substring(r0, r14)     // Catch:{ Exception -> 0x0a11 }
            r13.add(r0)     // Catch:{ Exception -> 0x0a11 }
            goto L_0x09e6
        L_0x09e0:
            r39 = r0
            r41 = r4
            r40 = r6
        L_0x09e6:
            java.lang.String r10 = r9.readLine()     // Catch:{ Exception -> 0x0a11 }
            r0 = r39
            r6 = r40
            r4 = r41
            goto L_0x0954
        L_0x09f2:
            r39 = r0
            r41 = r4
            r40 = r6
            java.lang.String r0 = "bigStrings"
            r8.put(r0, r11)     // Catch:{ Exception -> 0x0a11 }
            java.lang.String r0 = "jsonStrings"
            r8.put(r0, r12)     // Catch:{ Exception -> 0x0a11 }
            java.lang.String r0 = "htmlStrings"
            r8.put(r0, r13)     // Catch:{ Exception -> 0x0a11 }
            r9.close()     // Catch:{ Exception -> 0x0a11 }
            goto L_0x0a11
        L_0x0a0b:
            r39 = r0
        L_0x0a0d:
            r41 = r4
            r40 = r6
        L_0x0a11:
            java.util.List<java.util.HashMap<java.lang.String, java.lang.Object>> r0 = r5.sharedpreference
            r0.add(r8)
            int r7 = r7 + 1
            r0 = r39
            r6 = r40
            r4 = r41
            goto L_0x0914
        L_0x0a20:
            r41 = r4
            java.lang.String r0 = "android.app.ContextImpl"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Exception -> 0x0b07 }
            com.taobao.onlinemonitor.OnLineMonitor r2 = r1.mOnLineMonitor     // Catch:{ Exception -> 0x0b07 }
            int r2 = com.taobao.onlinemonitor.OnLineMonitor.sApiLevel     // Catch:{ Exception -> 0x0b07 }
            r4 = 24
            if (r2 < r4) goto L_0x0a38
            java.lang.String r2 = "sSharedPrefsCache"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r2)     // Catch:{ Exception -> 0x0b07 }
        L_0x0a36:
            r2 = 1
            goto L_0x0a3f
        L_0x0a38:
            java.lang.String r2 = "sSharedPrefs"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r2)     // Catch:{ Exception -> 0x0b07 }
            goto L_0x0a36
        L_0x0a3f:
            r0.setAccessible(r2)     // Catch:{ Exception -> 0x0b07 }
            android.app.Application r2 = com.taobao.onlinemonitor.OnLineMonitorApp.sApplication     // Catch:{ Exception -> 0x0b07 }
            android.content.Context r2 = r2.getApplicationContext()     // Catch:{ Exception -> 0x0b07 }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ Exception -> 0x0b07 }
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ Exception -> 0x0b07 }
            if (r0 == 0) goto L_0x0b0f
            com.taobao.onlinemonitor.OnLineMonitor r2 = r1.mOnLineMonitor     // Catch:{ Exception -> 0x0b07 }
            android.content.Context r2 = r2.mContext     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r2 = r2.getPackageName()     // Catch:{ Exception -> 0x0b07 }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ Exception -> 0x0b07 }
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ Exception -> 0x0b07 }
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x0b07 }
            if (r0 == 0) goto L_0x0b0f
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ Exception -> 0x0b07 }
            r2.<init>()     // Catch:{ Exception -> 0x0b07 }
            r5.loadedsharedpreference = r2     // Catch:{ Exception -> 0x0b07 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x0b07 }
        L_0x0a6f:
            boolean r2 = r0.hasNext()     // Catch:{ Exception -> 0x0b07 }
            if (r2 == 0) goto L_0x0b0f
            java.lang.Object r2 = r0.next()     // Catch:{ Exception -> 0x0b07 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ Exception -> 0x0b07 }
            com.taobao.onlinemonitor.OnLineMonitor r6 = r1.mOnLineMonitor     // Catch:{ Exception -> 0x0b07 }
            int r6 = com.taobao.onlinemonitor.OnLineMonitor.sApiLevel     // Catch:{ Exception -> 0x0b07 }
            if (r6 < r4) goto L_0x0a8c
            java.lang.Object r6 = r2.getKey()     // Catch:{ Exception -> 0x0b07 }
            java.io.File r6 = (java.io.File) r6     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r6 = r6.getAbsolutePath()     // Catch:{ Exception -> 0x0b07 }
            goto L_0x0a92
        L_0x0a8c:
            java.lang.Object r6 = r2.getKey()     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x0b07 }
        L_0x0a92:
            java.lang.Object r2 = r2.getValue()     // Catch:{ Exception -> 0x0b07 }
            java.lang.Class r7 = r2.getClass()     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r8 = "mMap"
            java.lang.reflect.Field r7 = r7.getDeclaredField(r8)     // Catch:{ Exception -> 0x0b07 }
            r8 = 1
            r7.setAccessible(r8)     // Catch:{ Exception -> 0x0b07 }
            java.lang.Object r2 = r7.get(r2)     // Catch:{ Exception -> 0x0b07 }
            java.util.Map r2 = (java.util.Map) r2     // Catch:{ Exception -> 0x0b07 }
            if (r2 == 0) goto L_0x0ad8
            int r8 = r2.size()     // Catch:{ Exception -> 0x0b07 }
            java.util.Set r2 = r2.entrySet()     // Catch:{ Exception -> 0x0b07 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ Exception -> 0x0b07 }
            r7 = 0
            r9 = 0
        L_0x0aba:
            boolean r10 = r2.hasNext()     // Catch:{ Exception -> 0x0b07 }
            if (r10 == 0) goto L_0x0adb
            java.lang.Object r10 = r2.next()     // Catch:{ Exception -> 0x0b07 }
            java.util.Map$Entry r10 = (java.util.Map.Entry) r10     // Catch:{ Exception -> 0x0b07 }
            java.lang.Object r10 = r10.getValue()     // Catch:{ Exception -> 0x0b07 }
            boolean r11 = r10 instanceof java.lang.String     // Catch:{ Exception -> 0x0b07 }
            if (r11 == 0) goto L_0x0aba
            int r7 = r7 + 1
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Exception -> 0x0b07 }
            int r10 = r10.length()     // Catch:{ Exception -> 0x0b07 }
            int r9 = r9 + r10
            goto L_0x0aba
        L_0x0ad8:
            r7 = 0
            r8 = 0
            r9 = 0
        L_0x0adb:
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Exception -> 0x0b07 }
            r2.<init>()     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r10 = "key"
            r2.put(r10, r6)     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r6 = "entryCount"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Exception -> 0x0b07 }
            r2.put(r6, r8)     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r6 = "stringCount"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Exception -> 0x0b07 }
            r2.put(r6, r7)     // Catch:{ Exception -> 0x0b07 }
            java.lang.String r6 = "stringLength"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r9)     // Catch:{ Exception -> 0x0b07 }
            r2.put(r6, r7)     // Catch:{ Exception -> 0x0b07 }
            java.util.List<java.util.HashMap<java.lang.String, java.lang.Object>> r6 = r5.loadedsharedpreference     // Catch:{ Exception -> 0x0b07 }
            r6.add(r2)     // Catch:{ Exception -> 0x0b07 }
            goto L_0x0a6f
        L_0x0b07:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0b0f
        L_0x0b0c:
            return
        L_0x0b0d:
            r41 = r4
        L_0x0b0f:
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r0 = r1.mOnBootFinishedList
            r5.onlinebootnotify = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r0 = r1.mOnBackForGroundList
            r5.onlinebackforgroundnotify = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r0 = r1.mOnActivityLifeCycleList
            r5.onlinelifecyclelist = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r0 = r1.mOnLineMonitorNotifyList
            r5.onlinenotifylist = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r0 = r1.mOnActivityLifeCycleTimeList
            r5.onlinelifecycletimelist = r0
            java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r0 = r1.mOnLineMonitorNotifyTimeList
            r5.onlinenotifytimelist = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5.leak_onlinelifecyclelist = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnActivityLifeCycle> r0 = r0.mOnActivityLifeCycleList
            if (r0 == 0) goto L_0x0b52
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnActivityLifeCycle> r0 = r0.mOnActivityLifeCycleList
            java.util.Iterator r0 = r0.iterator()
        L_0x0b3c:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0b52
            java.lang.Object r2 = r0.next()
            com.taobao.onlinemonitor.OnLineMonitor$OnActivityLifeCycle r2 = (com.taobao.onlinemonitor.OnLineMonitor.OnActivityLifeCycle) r2
            java.util.List<java.lang.String> r4 = r5.leak_onlinelifecyclelist
            java.lang.String r2 = r2.toString()
            r4.add(r2)
            goto L_0x0b3c
        L_0x0b52:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5.leak_onlinenotifylist = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify> r0 = r0.mOnLineMonitorNotifyList
            if (r0 == 0) goto L_0x0b7d
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify> r0 = r0.mOnLineMonitorNotifyList
            java.util.Iterator r0 = r0.iterator()
        L_0x0b67:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0b7d
            java.lang.Object r2 = r0.next()
            com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify r2 = (com.taobao.onlinemonitor.OnLineMonitor.OnLineMonitorNotify) r2
            java.util.List<java.lang.String> r4 = r5.leak_onlinenotifylist
            java.lang.String r2 = r2.toString()
            r4.add(r2)
            goto L_0x0b67
        L_0x0b7d:
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r5.basic
            java.lang.String r2 = "sTraceOnLineDuration"
            int r4 = sTraceOnLineDuration
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r0.put(r2, r4)
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r0 = r1.mWakeLockInfoList
            r5.mWakeLockInfoList = r0
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r1.mCloseGuardInfo
            r5.mCloseGuardInfo = r0
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r1.mMainThreadBlockGuardInfo
            r5.mMainThreadBlockGuardInfo = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo> r0 = r0.mBootResourceUsedInfoList
            r5.mBootResourceUsedInfoList = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.ProblemCheck r0 = r0.mProblemCheck
            java.util.Map<java.lang.String, java.lang.Integer> r0 = r0.mSharedpreferenceKeyFreq
            r5.mSharedpreferenceKeyFreq = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.ProblemCheck r0 = r0.mProblemCheck
            java.util.Map<java.lang.String, java.lang.Integer> r0 = r0.mSharedpreferenceQueuedWork
            r5.mSharedpreferenceQueuedWork = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r0 = r0.mBlockGuardThreadInfo
            if (r0 == 0) goto L_0x0bf3
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r0 = r0.mBlockGuardThreadInfo
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0bf3
            java.util.ArrayList r0 = new java.util.ArrayList
            com.taobao.onlinemonitor.OnLineMonitor r2 = r1.mOnLineMonitor
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfo
            int r2 = r2.size()
            r0.<init>(r2)
            r5.mBlockGuardThreadInfo = r0
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r0 = r0.mBlockGuardThreadInfo
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0bd7:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0bf3
            java.lang.Object r2 = r0.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            if (r2 == 0) goto L_0x0bd7
            java.lang.Object r2 = r2.getValue()
            com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo r2 = (com.taobao.onlinemonitor.OnLineMonitor.ThreadIoInfo) r2
            if (r2 == 0) goto L_0x0bd7
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r4 = r5.mBlockGuardThreadInfo
            r4.add(r2)
            goto L_0x0bd7
        L_0x0bf3:
            java.util.Map<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r0 = r1.mInstallBundleInfoMap
            if (r0 == 0) goto L_0x0c3f
            java.util.Map<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r0 = r1.mInstallBundleInfoMap
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0c3f
            if (r44 != 0) goto L_0x0c3f
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5.mInstallBundleInfoList = r0
            java.util.Map<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r0 = r1.mInstallBundleInfoMap
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0c12:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0c3f
            java.lang.Object r2 = r0.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            if (r2 == 0) goto L_0x0c12
            java.lang.Object r2 = r2.getValue()
            com.taobao.onlinemonitor.OnLineMonitor$BundleInfo r2 = (com.taobao.onlinemonitor.OnLineMonitor.BundleInfo) r2
            if (r2 == 0) goto L_0x0c12
            java.lang.String r3 = r2.activityName
            if (r3 != 0) goto L_0x0c30
            java.lang.String r3 = ""
            r2.activityName = r3
        L_0x0c30:
            com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r2.resourceUsedInfo
            if (r3 == 0) goto L_0x0c39
            com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r2.resourceUsedInfo
            r4 = 0
            r3.baseTheadMap = r4
        L_0x0c39:
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r3 = r5.mInstallBundleInfoList
            r3.add(r2)
            goto L_0x0c12
        L_0x0c3f:
            java.util.Map<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r0 = r1.mStartBundleInfoMap
            if (r0 == 0) goto L_0x0c8d
            java.util.Map<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r0 = r1.mStartBundleInfoMap
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0c8d
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5.mStartBundleInfoList = r0
            java.util.Map<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r0 = r1.mStartBundleInfoMap
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0c5c:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0c8d
            java.lang.Object r2 = r0.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            if (r2 == 0) goto L_0x0c8b
            java.lang.Object r2 = r2.getValue()
            com.taobao.onlinemonitor.OnLineMonitor$BundleInfo r2 = (com.taobao.onlinemonitor.OnLineMonitor.BundleInfo) r2
            if (r2 == 0) goto L_0x0c8b
            java.lang.String r3 = r2.activityName
            if (r3 != 0) goto L_0x0c7a
            java.lang.String r3 = ""
            r2.activityName = r3
        L_0x0c7a:
            com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r2.resourceUsedInfo
            if (r3 == 0) goto L_0x0c84
            com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r2.resourceUsedInfo
            r4 = 0
            r3.baseTheadMap = r4
            goto L_0x0c85
        L_0x0c84:
            r4 = 0
        L_0x0c85:
            java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$BundleInfo> r3 = r5.mStartBundleInfoList
            r3.add(r2)
            goto L_0x0c5c
        L_0x0c8b:
            r4 = 0
            goto L_0x0c5c
        L_0x0c8d:
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r5.basic
            java.lang.String r2 = "sMemoryLeakDetector"
            boolean r3 = sMemoryLeakDetector
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            r0.put(r2, r3)
            int r0 = r41.size()
            if (r0 <= 0) goto L_0x0ca3
            r2 = 1
            r1.mHasMemroyLeack = r2
        L_0x0ca3:
            boolean r0 = sMemoryLeakDetector     // Catch:{ Throwable -> 0x0de5 }
            if (r0 != 0) goto L_0x0cb0
            boolean r0 = sMemoryAnalysis     // Catch:{ Throwable -> 0x0de5 }
            if (r0 == 0) goto L_0x0cac
            goto L_0x0cb0
        L_0x0cac:
            r2 = r41
            goto L_0x0da1
        L_0x0cb0:
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor     // Catch:{ Throwable -> 0x0de5 }
            long r2 = r0.mUIHiddenTime     // Catch:{ Throwable -> 0x0de5 }
            r6 = 0
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0cac
            long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0de5 }
            r6 = 1000000(0xf4240, double:4.940656E-318)
            long r2 = r2 / r6
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor     // Catch:{ Throwable -> 0x0de5 }
            long r6 = r0.mUIHiddenTime     // Catch:{ Throwable -> 0x0de5 }
            r0 = 0
            long r2 = r2 - r6
            int r0 = com.taobao.onlinemonitor.OnLineMonitorApp.sToSleepTime     // Catch:{ Throwable -> 0x0de5 }
            long r6 = (long) r0     // Catch:{ Throwable -> 0x0de5 }
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0cac
            boolean r0 = sMemoryAnalysis     // Catch:{ Throwable -> 0x0de5 }
            if (r0 != 0) goto L_0x0cd9
            int r0 = r41.size()     // Catch:{ Throwable -> 0x0de5 }
            if (r0 <= 0) goto L_0x0de9
        L_0x0cd9:
            com.taobao.onlinemonitor.MemoryDetector r0 = new com.taobao.onlinemonitor.MemoryDetector     // Catch:{ Throwable -> 0x0de5 }
            r0.<init>()     // Catch:{ Throwable -> 0x0de5 }
            int r2 = r41.size()     // Catch:{ Throwable -> 0x0de5 }
            if (r2 <= 0) goto L_0x0d0f
            java.lang.String r3 = "OnLineMonitor"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0de5 }
            r4.<init>()     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r6 = "开始分析 "
            r4.append(r6)     // Catch:{ Throwable -> 0x0de5 }
            r4.append(r2)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r2 = "条内存链路,请稍后!"
            r4.append(r2)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r2 = r4.toString()     // Catch:{ Throwable -> 0x0de5 }
            android.util.Log.e(r3, r2)     // Catch:{ Throwable -> 0x0de5 }
            r2 = r41
            java.util.List r2 = r0.detectLeak(r2)     // Catch:{ Throwable -> 0x0de5 }
            if (r2 == 0) goto L_0x0d0f
            int r3 = r2.size()     // Catch:{ Throwable -> 0x0de5 }
            if (r3 <= 0) goto L_0x0d0f
            r5.memoryleak = r2     // Catch:{ Throwable -> 0x0de5 }
        L_0x0d0f:
            boolean r2 = sMemoryAnalysis     // Catch:{ Throwable -> 0x0de5 }
            if (r2 == 0) goto L_0x0de9
            java.lang.String r2 = "OnLineMonitor"
            java.lang.String r3 = "开始分析内存信息请稍后!"
            android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r2 = "OnLineMonitor"
            java.lang.String r3 = "分析静态变量 ..."
            android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0de5 }
            java.util.List r2 = r0.findStaticVariables()     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalInstanceCount"
            long r6 = r0.mTotalInstanceCount     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalInstanceSize"
            long r6 = r0.mTotalInstanceSize     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalInstanceRetainedSize"
            long r6 = r0.mTotalInstanceRetainedSize     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalStaticCount"
            int r6 = r0.mTotalStaticCount     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalStaticSize"
            int r6 = r0.mTotalStaticSize     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalStaticRetainedSize"
            int r6 = r0.mTotalStaticRetainedSize     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalSingletonCount"
            int r6 = r0.mTotalSingletonCount     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalSingletonSize"
            int r6 = r0.mTotalSingletonSize     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r5.basic     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "totalSingletonRetainedSize"
            int r6 = r0.mTotalSingletonRetainedSize     // Catch:{ Throwable -> 0x0de5 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0de5 }
            r3.put(r4, r6)     // Catch:{ Throwable -> 0x0de5 }
            r5.memstaticlist = r2     // Catch:{ Throwable -> 0x0de5 }
            java.util.List<com.taobao.onlinemonitor.MemoryDetector$StaticVariable> r0 = r0.mMemoryUsedList     // Catch:{ Throwable -> 0x0de5 }
            r5.memusedlist = r0     // Catch:{ Throwable -> 0x0de5 }
            goto L_0x0de9
        L_0x0da1:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0de5 }
            r0.<init>()     // Catch:{ Throwable -> 0x0de5 }
            r5.arrayListLeakObject = r0     // Catch:{ Throwable -> 0x0de5 }
            java.util.Iterator r0 = r2.iterator()     // Catch:{ Throwable -> 0x0de5 }
            r2 = 1
        L_0x0dad:
            boolean r3 = r0.hasNext()     // Catch:{ Throwable -> 0x0de5 }
            if (r3 == 0) goto L_0x0de9
            java.lang.Object r3 = r0.next()     // Catch:{ Throwable -> 0x0de5 }
            java.util.List<java.lang.String> r4 = r5.arrayListLeakObject     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r6 = r3.toString()     // Catch:{ Throwable -> 0x0de5 }
            r4.add(r6)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r4 = "OnLineMonitor"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0de5 }
            r6.<init>()     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r7 = "发现内存泄漏点"
            r6.append(r7)     // Catch:{ Throwable -> 0x0de5 }
            r6.append(r2)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r7 = " :"
            r6.append(r7)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0de5 }
            r6.append(r3)     // Catch:{ Throwable -> 0x0de5 }
            java.lang.String r3 = r6.toString()     // Catch:{ Throwable -> 0x0de5 }
            android.util.Log.e(r4, r3)     // Catch:{ Throwable -> 0x0de5 }
            int r2 = r2 + 1
            goto L_0x0dad
        L_0x0de5:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0de9:
            java.lang.reflect.Method r0 = r1.mGetDatabaseInfo     // Catch:{ Throwable -> 0x0e14 }
            if (r0 == 0) goto L_0x0e14
            java.lang.Class r0 = r1.mClassSQLiteDebug     // Catch:{ Throwable -> 0x0e14 }
            if (r0 == 0) goto L_0x0e14
            java.lang.reflect.Method r0 = r1.mGetDatabaseInfo     // Catch:{ Throwable -> 0x0e14 }
            java.lang.Class r2 = r1.mClassSQLiteDebug     // Catch:{ Throwable -> 0x0e14 }
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0e14 }
            java.lang.Object r0 = r0.invoke(r2, r4)     // Catch:{ Throwable -> 0x0e14 }
            if (r0 == 0) goto L_0x0e14
            java.lang.Class r2 = r0.getClass()     // Catch:{ Throwable -> 0x0e14 }
            java.lang.String r3 = "dbStats"
            java.lang.reflect.Field r2 = r2.getDeclaredField(r3)     // Catch:{ Throwable -> 0x0e14 }
            r3 = 1
            r2.setAccessible(r3)     // Catch:{ Throwable -> 0x0e14 }
            java.lang.Object r0 = r2.get(r0)     // Catch:{ Throwable -> 0x0e14 }
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Throwable -> 0x0e14 }
            r5.mDbStats = r0     // Catch:{ Throwable -> 0x0e14 }
        L_0x0e14:
            android.util.SparseIntArray r0 = r1.mBootPidCpuPercents
            if (r0 != 0) goto L_0x0e1a
            r8 = 0
            goto L_0x0e20
        L_0x0e1a:
            android.util.SparseIntArray r0 = r1.mBootPidCpuPercents
            int r8 = r0.size()
        L_0x0e20:
            if (r8 <= 0) goto L_0x0f08
            android.util.SparseIntArray r0 = r1.mSysThreadsCount
            r5.mSysThreadsCount = r0
            android.util.SparseIntArray r0 = r1.mVmThreadsCount
            r5.mVmThreadsCount = r0
            android.util.SparseIntArray r0 = r1.mRunningThreadsCount
            r5.mRunningThreadsCount = r0
            android.util.SparseIntArray r0 = r1.mRunningSysScores
            r5.mRunningSysScores = r0
            android.util.SparseIntArray r0 = r1.mRunningPidScores
            r5.mRunningPidScores = r0
            android.util.SparseIntArray r0 = r1.mSysCpuPercentRecords
            r5.mSysCpuPercentRecords = r0
            android.util.SparseIntArray r0 = r1.mPidCpuPercentRecords
            r5.mPidCpuPercentRecords = r0
            android.util.SparseIntArray r0 = r1.mBootSysCpuPercents
            r5.mBootSysCpuPercents = r0
            android.util.SparseIntArray r0 = r1.mBootPidCpuPercents
            r5.mBootPidCpuPercents = r0
            java.util.ArrayList<java.lang.Float> r0 = r1.mCpuPercentTimestamps
            r5.mCpuPercentTimestamps = r0
            java.util.ArrayList<java.lang.Float> r0 = r1.mBootCpuPercentTimestamps
            r5.mBootCpuPercentTimestamps = r0
            java.util.List<java.lang.Long> r0 = r1.mMajorFaults
            r5.mMajorFaults = r0
            android.util.SparseIntArray r0 = r1.mSysIoWaitCounts
            r5.mSysIoWaitCounts = r0
            android.util.SparseIntArray r0 = r1.mSysIoWaitSums
            r5.mSysIoWaitSums = r0
            android.util.SparseIntArray r0 = r1.mSysIoWaitPercent
            r5.mSysIoWaitPercent = r0
            android.util.SparseIntArray r0 = r1.mSysSchedWaitSums
            r5.mSysSchedWaitSums = r0
            android.util.SparseIntArray r0 = r1.mSysSchedWaitCounts
            r5.mSysSchedWaitCounts = r0
            android.util.SparseIntArray r0 = r1.mMemoryLevels
            r5.mMemoryLevels = r0
            java.util.ArrayList<java.lang.Float> r0 = r1.mPerCpuLoads
            r5.mPerCpuLoads = r0
            java.util.ArrayList<java.lang.Float> r0 = r1.mSysLoads1Min
            r5.mSysLoads1Min = r0
            java.util.ArrayList<java.lang.Float> r0 = r1.mSysLoads5Min
            r5.mSysLoads5Min = r0
            android.util.SparseIntArray r0 = r1.mRunningFinalizerCount
            r5.mRunningFinalizerCount = r0
            android.util.SparseIntArray r0 = r1.mRunningSysScores     // Catch:{ Throwable -> 0x0f02 }
            android.util.SparseIntArray r2 = r1.mRunningSysScores     // Catch:{ Throwable -> 0x0f02 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x0f02 }
            r3 = 0
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r2)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootRunningSysScores = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mRunningSysScores     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r2 = r1.mRunningPidScores     // Catch:{ Throwable -> 0x0f00 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r2)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootRunningPidScores = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mSysThreadsCount     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysThreadsCount = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mVmThreadsCount     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootVmThreadsCount = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mRunningThreadsCount     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootRunningThreadsCount = r0     // Catch:{ Throwable -> 0x0f00 }
            java.util.List<java.lang.Long> r0 = r1.mMajorFaults     // Catch:{ Throwable -> 0x0f00 }
            java.util.List r0 = r0.subList(r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootMajorFaults = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mSysIoWaitCounts     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysIoWaitCounts = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mSysIoWaitSums     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysIoWaitSums = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mSysIoWaitPercent     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysIoWaitPercent = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mSysSchedWaitSums     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysSchedWaitSums = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mSysSchedWaitCounts     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysSchedWaitCounts = r0     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.mMemoryLevels     // Catch:{ Throwable -> 0x0f00 }
            android.util.SparseIntArray r0 = r1.subSparseIntArray(r0, r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootMemoryLevels = r0     // Catch:{ Throwable -> 0x0f00 }
            java.util.ArrayList<java.lang.Float> r0 = r1.mPerCpuLoads     // Catch:{ Throwable -> 0x0f00 }
            java.util.List r0 = r0.subList(r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootPerCpuLoads = r0     // Catch:{ Throwable -> 0x0f00 }
            java.util.ArrayList<java.lang.Float> r0 = r1.mSysLoads1Min     // Catch:{ Throwable -> 0x0f00 }
            java.util.List r0 = r0.subList(r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysLoads1Min = r0     // Catch:{ Throwable -> 0x0f00 }
            java.util.ArrayList<java.lang.Float> r0 = r1.mSysLoads5Min     // Catch:{ Throwable -> 0x0f00 }
            java.util.List r0 = r0.subList(r3, r8)     // Catch:{ Throwable -> 0x0f00 }
            r5.mBootSysLoads5Min = r0     // Catch:{ Throwable -> 0x0f00 }
            goto L_0x0f09
        L_0x0f00:
            r0 = move-exception
            goto L_0x0f04
        L_0x0f02:
            r0 = move-exception
            r3 = 0
        L_0x0f04:
            r0.printStackTrace()
            goto L_0x0f09
        L_0x0f08:
            r3 = 0
        L_0x0f09:
            java.lang.String r0 = r5.asJsData()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r4 = r43
            r2.append(r4)
            java.lang.String r4 = "/data.js"
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            r1.writeToFile(r0, r2)
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r0 = r0.mOnlineStatistics
            if (r0 == 0) goto L_0x0f49
            com.taobao.onlinemonitor.OnLineMonitor r0 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r0 = r0.mOnlineStatistics
            int r0 = r0.size()
        L_0x0f31:
            if (r3 >= r0) goto L_0x0f49
            com.taobao.onlinemonitor.OnLineMonitor r2 = r1.mOnLineMonitor
            java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r2 = r2.mOnlineStatistics
            java.lang.Object r2 = r2.get(r3)
            com.taobao.onlinemonitor.OnlineStatistics r2 = (com.taobao.onlinemonitor.OnlineStatistics) r2
            if (r2 == 0) goto L_0x0f46
            com.taobao.onlinemonitor.OnLineMonitor r4 = r1.mOnLineMonitor
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r4 = r4.mOnLineStat
            r2.onCreatePerformanceReport(r4, r5)
        L_0x0f46:
            int r3 = r3 + 1
            goto L_0x0f31
        L_0x0f49:
            return
        L_0x0f4a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.TraceDetail.generateOutputData(java.lang.String, boolean):void");
    }

    private SparseIntArray subSparseIntArray(SparseIntArray sparseIntArray, int i, int i2) {
        SparseIntArray sparseIntArray2 = new SparseIntArray(i2 - i);
        int size = sparseIntArray.size();
        for (int max = Math.max(0, i); max < Math.min(size, i2); max++) {
            sparseIntArray2.put(max, sparseIntArray.get(max));
        }
        return sparseIntArray2;
    }

    private HashMap<String, Object> createBasicInfo() {
        String str;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(com.taobao.accs.common.Constants.KEY_MODEL, this.mOnLineMonitor.mOnLineStat.deviceInfo.mobileModel);
        hashMap.put("brand", this.mOnLineMonitor.mOnLineStat.deviceInfo.mobileBrand);
        hashMap.put(LogContent.LOG_VALUE_SOURCE_DEFAULT, this.mOnLineMonitor.mContext.getPackageName());
        hashMap.put("version", OnLineMonitor.getVersionName(this.mOnLineMonitor.mContext));
        hashMap.put("api", Integer.valueOf(Build.VERSION.SDK_INT));
        hashMap.put(ProtocolConst.KEY_ROOT, Boolean.toString(this.mOnLineMonitor.mIsRooted));
        hashMap.put("cpucount", Short.valueOf(this.mOnLineMonitor.mCpuProcessCount));
        hashMap.put("devmem", Long.valueOf(this.mOnLineMonitor.mDeviceTotalMemory));
        hashMap.put("devremainmem", Long.valueOf(this.mOnLineMonitor.mAvailMemory));
        if (this.mOnLineMonitor.mDeviceTotalMemory > 0) {
            hashMap.put("devremainmempercent", Long.valueOf((this.mOnLineMonitor.mAvailMemory * 100) / this.mOnLineMonitor.mDeviceTotalMemory));
        }
        hashMap.put("threshold", Long.valueOf(this.mOnLineMonitor.mMemoryThreshold));
        if (this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuFreqArray == null || this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuMaxFreq == this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuMinFreq) {
            hashMap.put("cpufreq", Float.valueOf(this.mOnLineMonitor.mCpuMaxFreq));
        } else {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < this.mOnLineMonitor.mCpuProcessCount; i3++) {
                if (this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuFreqArray[i3] == this.mOnLineMonitor.mCpuMaxFreq) {
                    i++;
                }
                if (this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuFreqArray[i3] == this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuMinFreq) {
                    i2++;
                }
            }
            if (i <= 0 || i2 <= 0) {
                hashMap.put("cpufreq", Float.valueOf(this.mOnLineMonitor.mCpuMaxFreq));
            } else {
                hashMap.put("cpufreq", this.mOnLineMonitor.mCpuMaxFreq + " * " + i + "<br>" + this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuMinFreq + " * " + i2);
            }
        }
        hashMap.put("pidmem", Long.valueOf(this.mOnLineMonitor.mTotalUsedMemory));
        hashMap.put("maxcanusejavamem", Integer.valueOf(this.mOnLineMonitor.mMaxCanUseJavaMemory));
        hashMap.put("pidremainmem", Integer.valueOf(this.mOnLineMonitor.mRemainAvailMemory));
        hashMap.put("pidmempercent", Long.valueOf(this.mOnLineMonitor.mOnLineStat.memroyStat.totalMemoryPercent));
        if (this.mOnLineMonitor.mOnLineStat.deviceInfo.gpuMaxFreq > 0) {
            hashMap.put("gpufreq", Long.valueOf(this.mOnLineMonitor.mOnLineStat.deviceInfo.gpuMaxFreq));
        } else {
            hashMap.put("gpufreq", "Unknown");
        }
        hashMap.put("gpumodel", this.mOnLineMonitor.mOnLineStat.deviceInfo.gpuModel);
        hashMap.put("gpubrand", this.mOnLineMonitor.mOnLineStat.deviceInfo.gpuBrand);
        hashMap.put("cpumodel", this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuModel);
        hashMap.put("cpubrand", this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuBrand);
        hashMap.put("opengl", this.mOnLineMonitor.mOpenGlVersion);
        hashMap.put("screenwidth", Integer.valueOf(this.mOnLineMonitor.mOnLineStat.deviceInfo.screenWidth));
        hashMap.put("screenheight", Integer.valueOf(this.mOnLineMonitor.mOnLineStat.deviceInfo.screenHeght));
        hashMap.put("screendensity", Float.valueOf(this.mOnLineMonitor.mOnLineStat.deviceInfo.density));
        hashMap.put("majorfault", Integer.valueOf(this.mOnLineMonitor.mMajorFault));
        hashMap.put("javaheap", Long.valueOf(this.mOnLineMonitor.mDalvikPss));
        hashMap.put("javaheapalloc", Long.valueOf(this.mOnLineMonitor.mDalvikAllocated));
        hashMap.put("javaheapfree", Long.valueOf(this.mOnLineMonitor.mDalvikFree));
        hashMap.put("sBackInGroundOnBoot", Boolean.valueOf(OnLineMonitorApp.sBackInGroundOnBoot));
        hashMap.put("sIsBootCorrect", Boolean.valueOf(OnLineMonitorApp.sIsBootCorrect));
        hashMap.put("mBootTotalTime", Integer.valueOf(this.mOnLineMonitor.mBootTotalTime));
        hashMap.put("mBootUsedTime", Integer.valueOf(this.mOnLineMonitor.mBootUsedTime));
        hashMap.put("mPreparePidTime", Integer.valueOf(this.mOnLineMonitor.mOnLineStat.preparePidTime));
        hashMap.put("sBackInGroundOnBoot", Boolean.valueOf(OnLineMonitorApp.sBackInGroundOnBoot));
        hashMap.put("sAdvertisementTime", Integer.valueOf(OnLineMonitorApp.sAdvertisementTime));
        hashMap.put("sIsBootCorrect", Boolean.valueOf(OnLineMonitorApp.sIsBootCorrect));
        hashMap.put("sIsCodeBoot", Boolean.valueOf(OnLineMonitorApp.sIsCodeBoot));
        hashMap.put("sFirstActivityTime", Long.valueOf(OnLineMonitorApp.sFirstActivityTime));
        hashMap.put("sLaunchTime", Long.valueOf(OnLineMonitorApp.sLaunchTime));
        hashMap.put("nativeheap", Long.valueOf(this.mOnLineMonitor.mNativeHeapPss));
        hashMap.put("nativeheapalloc", Long.valueOf(this.mOnLineMonitor.mNativeHeapAllocatedSize));
        hashMap.put("nativeheapfree", Long.valueOf(this.mOnLineMonitor.mNativeHeapPss - this.mOnLineMonitor.mNativeHeapAllocatedSize));
        long j = (this.mOnLineMonitor.mMobileRxBytes - this.mFirstMobileRxBytes) / 1024;
        long j2 = (this.mOnLineMonitor.mMobileTxBytes - this.mFirstMobileTxBytes) / 1024;
        long j3 = (this.mOnLineMonitor.mTotalRxBytes - this.mFirstTotalRxBytes) / 1024;
        long j4 = (this.mOnLineMonitor.mTotalTxBytes - this.mFirstTotalTxBytes) / 1024;
        if (j2 >= 1024) {
            hashMap.put("mobiletx", (j2 / 1024) + " M");
        } else {
            hashMap.put("mobiletx", j2 + " K");
        }
        if (j >= 1024) {
            hashMap.put("mobilerx", (j / 1024) + " M");
        } else {
            hashMap.put("mobilerx", j + " K");
        }
        if (j4 >= 1024) {
            hashMap.put("totaltx", (j4 / 1024) + " M");
        } else {
            hashMap.put("totaltx", j4 + " K");
        }
        if (j3 >= 1024) {
            hashMap.put("totalrx", (j3 / 1024) + " M");
        } else {
            hashMap.put("totalrx", j3 + " K");
        }
        hashMap.put("startbattery", Integer.valueOf(this.mOnLineMonitor.mInitBatteryPercent));
        hashMap.put("endbattery", Integer.valueOf(this.mOnLineMonitor.mBatteryPercent));
        hashMap.put("batterycharging", Boolean.toString(this.mOnLineMonitor.mBatteryStatus == 2));
        if (this.mOnLineMonitor.mBatteryHealth == 3) {
            hashMap.put("batteryhealth", "<font color=red>电池过热</font>");
        } else if (this.mOnLineMonitor.mBatteryHealth == 2) {
            hashMap.put("batteryhealth", "电池良好");
        } else if (this.mOnLineMonitor.mBatteryHealth == 5) {
            hashMap.put("batteryhealth", "<font color=red>电压过高</font>");
        } else {
            hashMap.put("batteryhealth", "未知");
        }
        if (Build.VERSION.SDK_INT >= 19) {
            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            hashMap.put("ussmem", Integer.valueOf((memoryInfo.getTotalPrivateDirty() / 1024) + 0));
            hashMap.put("privateclean", 0);
            hashMap.put("privatedirty", Integer.valueOf(memoryInfo.getTotalPrivateDirty() / 1024));
            hashMap.put("shareddirty", Integer.valueOf(memoryInfo.getTotalSharedDirty() / 1024));
            hashMap.put("sharedclean", Integer.valueOf(memoryInfo.getTotalSharedClean() / 1024));
            hashMap.put("swappablepss", Integer.valueOf(memoryInfo.getTotalSwappablePss() / 1024));
        } else {
            hashMap.put("ussmem", LOW_API);
            hashMap.put("privateclean", LOW_API);
            hashMap.put("privatedirty", LOW_API);
            hashMap.put("shareddirty", LOW_API);
            hashMap.put("sharedclean", LOW_API);
            hashMap.put("swappablepss", LOW_API);
        }
        hashMap.put("devscore", Short.valueOf(this.mOnLineMonitor.mDevicesScore));
        hashMap.put("lowperformance", Boolean.valueOf(this.mOnLineMonitor.mOnLineStat.performanceInfo.isLowPerformance));
        String str2 = null;
        int i4 = this.mOnLineMonitor.mTrimMemoryLevel;
        if (i4 == 0) {
            str2 = "0,正常";
        } else if (i4 == 5) {
            str2 = "<font color=red><a href='#' title='设备可以使用的内存非常低, 可以把不用的资源释放一些'>5</title></font>";
        } else if (i4 == 10) {
            str2 = "<font color=red><a href='#' title='设备使用的内存比较低, 系统级会杀掉一些其它的缓存应用'>10</title></font>";
        } else if (i4 == 15) {
            str2 = "<font color=red><a href='#' title='系统已经把大多数缓存应用杀掉了, 你必须释放掉不是非常关键的资源'>15</title></font>";
        } else if (i4 == 20) {
            str2 = "<font color=red><a href='#' title='内存不足，并且该进程的UI已经不可见了'>20</title></font>";
        } else if (i4 == 40) {
            str2 = "<font color=red><a href='#' title='内存不足，该进程是后台进程，系统已经开始清除缓存列表'>40</title></font>";
        } else if (i4 == 60) {
            str2 = "<font color=red><a href='#' title='内存不足，进程在后台进程列表的中部，内存继续不足，很可能将被杀'>60</title></font>";
        } else if (i4 == 80) {
            str2 = "<font color=red><a href='#' title='内存不足，并且该进程在后台进程列表最后一个，马上就要被清理'>80</title></font>";
        }
        hashMap.put("memorytrim", str2);
        hashMap.put("maxthreadcount", Integer.valueOf(this.mOnLineMonitor.mMaxThreadCount));
        hashMap.put("maxruntimethreadcount", Integer.valueOf(this.mOnLineMonitor.mMaxRuntimeThreadCount));
        hashMap.put("maxrunningthreadcount", Integer.valueOf(this.mOnLineMonitor.mMaxRunningThreadCount));
        hashMap.put("runtimethreadcount", Integer.valueOf(this.mOnLineMonitor.mRuntimeThreadCount));
        hashMap.put("threadcount", Integer.valueOf(this.mOnLineMonitor.mThreadCount));
        hashMap.put("isbackground", Boolean.valueOf(this.mOnLineMonitor.mIsInBackGround));
        hashMap.put("largeheap", Boolean.valueOf(OnLineMonitorApp.sIsLargeHeap));
        hashMap.put("hardwareacce", Boolean.valueOf(OnLineMonitorApp.sIsHardWareAcce));
        if (this.mOnLineMonitor.mDevicesScore >= 90) {
            str = "<font color=red>旗舰机</font>";
        } else if (this.mOnLineMonitor.mDevicesScore >= 85) {
            str = "<font color=red>偏高端</font>";
        } else if (this.mOnLineMonitor.mDevicesScore >= 75) {
            str = "<font color=red>中高端</font>";
        } else if (this.mOnLineMonitor.mDevicesScore >= 60) {
            str = "<font color=red>中端</font>";
        } else if (this.mOnLineMonitor.mDevicesScore >= 50) {
            str = "<font color=red>中低端</font>";
        } else {
            str = this.mOnLineMonitor.mDevicesScore > 40 ? "<font color=red>低端</font>" : "<font color=red>非常低端</font>";
        }
        hashMap.put("devclass", str);
        hashMap.put("sysinitscore", Short.valueOf(this.mOnLineMonitor.mFirstSystemRunningScore));
        hashMap.put("sysinitclass", getRunStatus(this.mOnLineMonitor.mFirstSystemRunningScore));
        if (this.mOnLineMonitor.mTotalPidRunningScoreCount > 0) {
            int i5 = this.mOnLineMonitor.mTotalPidRunningScore / this.mOnLineMonitor.mTotalPidRunningScoreCount;
            String runStatus = getRunStatus(i5);
            hashMap.put("pidavgscore", Integer.valueOf(i5));
            hashMap.put("pidclass", runStatus);
        }
        if (this.mOnLineMonitor.mTotalSysRunningScoreCount > 0) {
            int i6 = this.mOnLineMonitor.mTotalSysRunningScore / this.mOnLineMonitor.mTotalSysRunningScoreCount;
            hashMap.put("sysclass", getRunStatus(i6));
            hashMap.put("sysavgscore", Integer.valueOf(i6));
        }
        hashMap.put("sysmaxscore", Short.valueOf(this.mOnLineMonitor.mMaxSystemRunningScore));
        hashMap.put("sysminscore", Short.valueOf(this.mOnLineMonitor.mMinSystemRunningScore));
        hashMap.put("pidmaxscore", Short.valueOf(this.mOnLineMonitor.mMaxPidRunningScore));
        hashMap.put("pidminscore", Short.valueOf(this.mOnLineMonitor.mMinPidRunningScore));
        long j5 = this.mSystemTotalCpuTimeEnd - this.mSystemTotalCpuTimeStart;
        long j6 = this.mSystemRunCpuTimeEnd - this.mSystemRunCpuTimeStart;
        if (OnLineMonitorApp.sIsBootCorrect && j5 > 0 && j6 > 0) {
            hashMap.put("bootsyscpu", Long.valueOf((j6 * 100) / j5));
            hashMap.put("bootpidcpu", Long.valueOf((this.mOnLineMonitor.mBootJiffyTime * 100) / j5));
            hashMap.put("bootpidrelativecpu", Long.valueOf((this.mOnLineMonitor.mBootJiffyTime * 100) / j6));
        }
        if (this.mOnLineMonitor.mUIHiddenTime > 0) {
            hashMap.put("uihidetime", Long.valueOf((System.nanoTime() / 1000000) - this.mOnLineMonitor.mUIHiddenTime));
        } else {
            hashMap.put("uihidetime", 0);
        }
        long totalRxBytes = (TrafficStats.getTotalRxBytes() - this.mOnLineMonitor.mTotalRxBytes) / 1024;
        long totalTxBytes = (TrafficStats.getTotalTxBytes() - this.mOnLineMonitor.mTotalTxBytes) / 1024;
        if (totalTxBytes >= 1024) {
            hashMap.put("bgtotaltx", (totalTxBytes / 1024) + " M");
        } else {
            hashMap.put("bgtotaltx", totalTxBytes + " K");
        }
        if (totalRxBytes >= 1024) {
            hashMap.put("bgtotalrx", (totalRxBytes / 1024) + " M");
        } else {
            hashMap.put("bgtotalrx", totalRxBytes + " K");
        }
        if (OnLineMonitor.sApiLevel >= 23) {
            hashMap.put("gccount", Integer.valueOf(this.mOnLineMonitor.mTotalGcCount));
            hashMap.put("blockgccount", Integer.valueOf(this.mOnLineMonitor.mBlockingGCCount));
            long j7 = this.mOnLineMonitor.mTotalBlockingGCTime;
            if (j7 >= 60000) {
                hashMap.put("blockgctime", (((float) j7) / 60000.0f) + " 分");
            } else {
                hashMap.put("blockgctime", (((float) j7) / 1000.0f) + " 秒");
            }
        } else {
            hashMap.put("gccount", Integer.valueOf(this.mOnLineMonitor.mTotalGcCount));
            hashMap.put("blockgccount", LOW_API);
            hashMap.put("blockgctime", LOW_API);
        }
        try {
            hashMap.put("innerstore", Float.valueOf((float) Math.round((float) ((((long) this.mOnLineMonitor.mOnLineStat.deviceInfo.storeTotalSize) * 100) / 100))));
            hashMap.put("innerstorefree", Float.valueOf(((float) Math.round((float) ((((long) this.mOnLineMonitor.mOnLineStat.deviceInfo.storeFreesize) * 100) / 1024))) / 100.0f));
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = (long) statFs.getBlockSize();
            hashMap.put("externalstore", Float.valueOf(((float) Math.round((float) (((((blockSize * ((long) statFs.getBlockCount())) / 1024) / 1024) * 100) / 1024))) / 100.0f));
            hashMap.put("externalstorefree", Float.valueOf(((float) Math.round((float) (((((((long) statFs.getAvailableBlocks()) * blockSize) / 1024) / 1024) * 100) / 1024))) / 100.0f));
        } catch (Exception unused) {
        }
        hashMap.put("avg1", Float.valueOf(this.mOnLineMonitor.mSystemLoadAvg[0]));
        hashMap.put("avg5", Float.valueOf(this.mOnLineMonitor.mSystemLoadAvg[1]));
        hashMap.put("avg15", Float.valueOf(this.mOnLineMonitor.mSystemLoadAvg[2]));
        hashMap.put("pidwaitsum", Float.valueOf(((float) Math.round((this.mOnLineMonitor.mPidWaitSum * 100.0f) * ((float) this.mJiffyMillis))) / 100.0f));
        hashMap.put("pidwaitmax", Float.valueOf(((float) Math.round((this.mOnLineMonitor.mPidWaitMax * 100.0f) * ((float) this.mJiffyMillis))) / 100.0f));
        hashMap.put("pidwaitcount", Integer.valueOf(this.mOnLineMonitor.mPidWaitCount));
        if (this.mOnLineMonitor.mOpenFileCount > 0) {
            hashMap.put("openfilecount", Integer.valueOf(this.mOnLineMonitor.mOpenFileCount));
        } else {
            hashMap.put("openfilecount", "本手机无权限");
        }
        hashMap.put("iowiatcount", Integer.valueOf(this.mOnLineMonitor.mPidIoWaitCount));
        hashMap.put("iowiattime", Integer.valueOf(this.mOnLineMonitor.mPidIoWaitSum * this.mJiffyMillis));
        hashMap.put("initnewthread", Integer.valueOf(this.mNewTheadCountAyr[0]));
        hashMap.put("bootactivitythread", Integer.valueOf(this.mNewTheadCountAyr[1]));
        hashMap.put("bootendthread", Integer.valueOf(this.mNewTheadCountAyr[2]));
        hashMap.put("threa_time_threshold", Integer.valueOf(sTraceRegThreadThreshold));
        hashMap.put("statissize", Integer.valueOf(sMemoryOccupySize));
        if (this.mFieldThreadCount != null) {
            try {
                hashMap.put("currentthread", Integer.valueOf(this.mFieldThreadCount.getInt(Thread.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hashMap.put("mBootStepCpu", this.mBootStepCpu);
        hashMap.put("mBootStepGcCount", this.mBootStepGcCount);
        hashMap.put("mBootStepMem", this.mBootStepMem);
        hashMap.put("mBootStepIoWait", this.mBootStepIoWait);
        hashMap.put("mBootStepSched", this.mBootStepSched);
        hashMap.put("mBootStepCpuLoad", this.mBootStepCpuLoad);
        hashMap.put("mBootStepThread", this.mBootStepThread);
        hashMap.put("mBootStepClass", this.mBootStepClass);
        hashMap.put("mBootStepPidTime", this.mBootStepPidTime);
        hashMap.put("mBootStepMainThreadTime", this.mBootStepMainThreadTime);
        hashMap.put("mProcessCpuTracker", this.mOnLineMonitor.mProcessCpuTracker);
        hashMap.put("mBootEndTime", Long.valueOf(this.mOnLineMonitor.mBootEndTime));
        hashMap.put("mBootActivityLoadTime", Integer.valueOf(this.mOnLineMonitor.mBootActivityLoadTime));
        return hashMap;
    }

    /* access modifiers changed from: package-private */
    public String checkLifiCycle(ArrayList<Object> arrayList) {
        if (OnLineMonitorApp.sApplication == null) {
            return null;
        }
        try {
            Field declaredField = Application.class.getDeclaredField("mActivityLifecycleCallbacks");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(OnLineMonitorApp.sApplication);
            StringBuilder sb = new StringBuilder(300);
            if (obj instanceof ArrayList) {
                ArrayList arrayList2 = (ArrayList) obj;
                for (int i = 0; i < arrayList2.size(); i++) {
                    if (!arrayList2.get(i).getClass().getName().contains("onlinemonitor")) {
                        sb.append(i + 1);
                        sb.append("、");
                        sb.append(arrayList2.get(i));
                        sb.append("<br><br>");
                    }
                }
            }
            return sb.substring(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void onBootEnd() {
        try {
            if (this.mSysCpuPercentRecords != null) {
                this.mBootSysCpuPercents = new SparseIntArray(this.mSysCpuPercentRecords.size());
                for (int i = 0; i < this.mSysCpuPercentRecords.size(); i++) {
                    this.mBootSysCpuPercents.put(i, this.mSysCpuPercentRecords.get(i));
                }
                this.mBootPidCpuPercents = new SparseIntArray(this.mPidCpuPercentRecords.size());
                for (int i2 = 0; i2 < this.mPidCpuPercentRecords.size(); i2++) {
                    this.mBootPidCpuPercents.put(i2, this.mPidCpuPercentRecords.get(i2));
                }
                this.mBootCpuPercentTimestamps = new ArrayList<>(this.mCpuPercentTimestamps);
            }
            this.mOnLineMonitor.mProcessCpuTracker.update();
            this.mSystemTotalCpuTimeEnd = this.mOnLineMonitor.mProcessCpuTracker.mSystemTotalCpuTime;
            this.mSystemRunCpuTimeEnd = this.mOnLineMonitor.mProcessCpuTracker.mSystemRunCpuTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mLifeCycleArray[0] = checkLifiCycle((ArrayList<Object>) null);
    }

    /* access modifiers changed from: package-private */
    public String getDeviceStatus() {
        if (this.mOnLineMonitor.mDevicesScore >= 90) {
            return "旗舰机";
        }
        if (this.mOnLineMonitor.mDevicesScore >= 85) {
            return "偏高端";
        }
        if (this.mOnLineMonitor.mDevicesScore >= 75) {
            return "中高端";
        }
        if (this.mOnLineMonitor.mDevicesScore >= 60) {
            return "中端";
        }
        if (this.mOnLineMonitor.mDevicesScore >= 50) {
            return "中低端";
        }
        return this.mOnLineMonitor.mDevicesScore > 40 ? "低端" : "非常低端";
    }

    /* access modifiers changed from: package-private */
    public String memoryStatus() {
        int i = this.mOnLineMonitor.mTrimMemoryLevel;
        if (i == 0) {
            return "正常";
        }
        if (i == 5) {
            return "设备可以使用的内存非常低, 可以把不用的资源释放一些";
        }
        if (i == 10) {
            return "设备使用的内存比较低, 系统级会杀掉一些其它的缓存应用";
        }
        if (i == 15) {
            return "系统已经把大多数缓存应用杀掉了, 你必须释放掉不是非常关键的资源";
        }
        if (i == 20) {
            return "该进程占用较多内存(" + this.mOnLineMonitor.mTotalUsedMemory + "M)，并且该进程的UI已经不可见了";
        } else if (i == 40) {
            return "内存不足，该进程是后台进程，系统已经开始清除缓存列表";
        } else {
            if (i == 60) {
                return "内存不足，进程在后台进程列表的中部，内存继续不足，很可能将被杀";
            }
            if (i != 80) {
                return "";
            }
            return "内存不足，并且该进程在后台进程列表最后一个，马上就要被清理";
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0036 A[SYNTHETIC, Splitter:B:19:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004b A[SYNTHETIC, Splitter:B:26:0x004b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeToFile(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            boolean r5 = r0.exists()
            if (r5 == 0) goto L_0x000e
            r0.delete()
        L_0x000e:
            r5 = 0
            java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x0030 }
            java.io.FileWriter r2 = new java.io.FileWriter     // Catch:{ Exception -> 0x0030 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0030 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0030 }
            r1.write(r4)     // Catch:{ Exception -> 0x002b, all -> 0x0028 }
            r1.flush()     // Catch:{ IOException -> 0x003d }
            r1.close()     // Catch:{ IOException -> 0x003d }
            java.util.ArrayList<java.io.File> r4 = r3.mFileToZipList     // Catch:{ IOException -> 0x003d }
            r4.add(r0)     // Catch:{ IOException -> 0x003d }
            goto L_0x0048
        L_0x0028:
            r4 = move-exception
            r5 = r1
            goto L_0x0049
        L_0x002b:
            r4 = move-exception
            r5 = r1
            goto L_0x0031
        L_0x002e:
            r4 = move-exception
            goto L_0x0049
        L_0x0030:
            r4 = move-exception
        L_0x0031:
            r4.printStackTrace()     // Catch:{ all -> 0x002e }
            if (r5 == 0) goto L_0x003f
            r5.flush()     // Catch:{ IOException -> 0x003d }
            r5.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x003f
        L_0x003d:
            r4 = move-exception
            goto L_0x0045
        L_0x003f:
            java.util.ArrayList<java.io.File> r4 = r3.mFileToZipList     // Catch:{ IOException -> 0x003d }
            r4.add(r0)     // Catch:{ IOException -> 0x003d }
            goto L_0x0048
        L_0x0045:
            r4.printStackTrace()
        L_0x0048:
            return
        L_0x0049:
            if (r5 == 0) goto L_0x0054
            r5.flush()     // Catch:{ IOException -> 0x0052 }
            r5.close()     // Catch:{ IOException -> 0x0052 }
            goto L_0x0054
        L_0x0052:
            r5 = move-exception
            goto L_0x005a
        L_0x0054:
            java.util.ArrayList<java.io.File> r5 = r3.mFileToZipList     // Catch:{ IOException -> 0x0052 }
            r5.add(r0)     // Catch:{ IOException -> 0x0052 }
            goto L_0x005d
        L_0x005a:
            r5.printStackTrace()
        L_0x005d:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.TraceDetail.writeToFile(java.lang.String, java.lang.String):void");
    }

    public void saveBigBitmap(ArrayList<Bitmap> arrayList, String str) {
        if (arrayList != null && arrayList.size() != 0) {
            String str2 = this.mExternalPath + "/BigBitmap";
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            String str3 = str2 + "/" + str;
            File file2 = new File(str3);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                Bitmap bitmap = arrayList.get(i);
                if (bitmap != null) {
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    int byteCount = bitmap.getByteCount();
                    Bitmap.Config config = bitmap.getConfig();
                    Log.e("OnLineMonitor", "检测到大图片: " + OnLineMonitor.getSimpleName(str) + Operators.SPACE_STR + config.name() + Operators.SPACE_STR + bitmap.getWidth() + "*" + bitmap.getHeight() + ",大小：" + ((((float) bitmap.getByteCount()) / 1024.0f) / 1024.0f) + " M " + bitmap.toString());
                    StringBuilder sb = new StringBuilder();
                    sb.append(config.name());
                    sb.append("-");
                    sb.append(bitmap.toString());
                    sb.append(" - ");
                    sb.append(width);
                    sb.append("*");
                    sb.append(height);
                    sb.append("-");
                    int i2 = byteCount / 1024;
                    sb.append(i2);
                    sb.append("kb");
                    sb.append("-");
                    sb.append(i2 / 1024);
                    sb.append("m.png");
                    saveBitmap(bitmap, str3, sb.toString());
                }
            }
        }
    }

    public static void saveBitmap(Bitmap bitmap, String str, String str2) {
        File file = new File(str, str2);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    static class MemoryRunningApp {
        ActivityManager.RunningAppProcessInfo app;
        Debug.MemoryInfo memoryInfo;

        MemoryRunningApp() {
        }
    }

    static class ActivityLifeInfo implements Serializable {
        String activityName;
        long cpuTime;
        String methodName;
        long realTime;

        ActivityLifeInfo() {
        }
    }

    static class AllocatorInfo {
        String className;
        int count;
        int perSize;
        int threadId;
        int threadName;
        int totalSize;

        AllocatorInfo() {
        }
    }
}
