package com.taobao.onlinemonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alipay.sdk.data.a;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.onlinemonitor.TraceDetail;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class OnLineMonitor {
    public static final int ANR_CHECK_INTERVAL = 3000;
    public static final int BUNDLE_WATCH_INSTALL_FINISHED = 1;
    public static final int BUNDLE_WATCH_START_BUNDLE = 2;
    public static final int BUNDLE_WATCH_START_BUNDLE_FINISHED = 3;
    public static final int BUNDLE_WATCH_START_INSTALL = 0;
    static final int MSG_CHECK_ANR = 5;
    static final int MSG_CHECK_APP_IMPORTANCE = 8;
    static final int MSG_CHECK_THREAD = 12;
    static final int MSG_COMMIT_DEVICE_INFO = 9;
    static final int MSG_EMPTY_ON_IDEL = 1;
    static final int MSG_GET_SYSTEM_INFO = 2;
    static final int MSG_NOTIFY_CHANGED = 4;
    static final int MSG_ON_BACKBROUND = 11;
    static final int MSG_ON_BOOT_END = 13;
    static final int MSG_ON_CHECK_ACTIVITY_LOAD = 16;
    static final int MSG_ON_CHECK_LIFECYCLE = 15;
    static final int MSG_ON_RECORD_BOOTRESOURCE = 18;
    static final int MSG_ON_STARTED_BUNDLE = 17;
    static final int MSG_ON_START_MONITOR = 14;
    static final int MSG_UPLOAD_BOOT_PERFORMANCE = 19;
    static final int NUM_CATEGORIES = 7;
    static final String TAG = "OnLineMonitor";
    public static final int TASK_TYPE_FROM_BOOT = 100000;
    static final int offsetPrivateClean = 4;
    static final int offsetPrivateDirty = 2;
    static final int offsetPss = 0;
    static final int offsetSharedClean = 5;
    static final int offsetSharedDirty = 3;
    static final int offsetSwappablePss = 1;
    static final int offsetSwappedOut = 6;
    static int sApiLevel = Build.VERSION.SDK_INT;
    public static boolean sIsDetailDebug = false;
    public static boolean sIsNormalDebug = false;
    public static boolean sIsTraceDetail = false;
    static OnLineMonitor sOnLineMonitor = null;
    static String sOnLineMonitorFileDir = null;
    public static boolean sPerformanceLog = true;
    static int sThreadPriorty = 0;
    long mActivityIdleFistTime;
    long mActivityIdleTime;
    ActivityLifecycleCallback mActivityLifecycleCallback;
    ActivityManager mActivityManager;
    String mActivityName;
    ActivityRuntimeInfo mActivityRuntimeInfo;
    ActivityRuntimeInfo mActivityTraceRuntimeInfo;
    HashMap<String, Integer> mActivitysHotOpenMap = new HashMap<>(64);
    HashMap<String, Integer> mActivitysMap = new HashMap<>(64);
    int mAnrCount;
    volatile int mAppProgressImportance = 100;
    volatile Context mApplicationContext;
    long mAvailMemory;
    int mAvgIOWaitTime;
    short mAvgMyPidScore;
    short mAvgSystemRunningScore;
    /* access modifiers changed from: private */
    public BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            OnLineMonitor.this.mCheckAnrTime = System.nanoTime() / 1000000;
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                OnLineMonitor.this.mBatteryPercent = intent.getIntExtra("level", 0);
                OnLineMonitor.this.mBatteryV = intent.getIntExtra("voltage", 0);
                OnLineMonitor.this.mBatteryTemp = (double) intent.getIntExtra("temperature", 0);
                OnLineMonitor.this.mBatteryStatus = intent.getIntExtra("status", 1);
                OnLineMonitor.this.mBatteryHealth = intent.getIntExtra("health", 1);
                if (OnLineMonitor.this.mInitBatteryPercent < 0) {
                    OnLineMonitor.this.mInitBatteryPercent = OnLineMonitor.this.mBatteryPercent;
                }
                if (OnLineMonitor.this.mBatteryStatus != 2 && OnLineMonitor.this.mBatteryPercent <= 15) {
                    if (!OnLineMonitor.this.mBatteryLowStat) {
                        OnLineMonitor.this.mBatteryLowStat = true;
                        OnLineMonitor.this.showMessage("电池电量低!");
                        OnLineMonitor.this.notifyOnlineRuntimeStat(40);
                    } else {
                        OnLineMonitor.this.mBatteryLowStat = false;
                    }
                }
                if (!(OnLineMonitor.this.mOnLineStat.batteryInfo.batteryStatus == OnLineMonitor.this.mBatteryStatus || OnLineMonitor.this.mBatteryStatus == 5)) {
                    if (OnLineMonitor.this.mBatteryStatus == 2) {
                        OnLineMonitor.this.showMessage("电池充电中!");
                    } else {
                        OnLineMonitor.this.showMessage("电池未充电!");
                    }
                    OnLineMonitor.this.notifyOnlineRuntimeStat(41);
                }
                if (OnLineMonitor.this.mOnLineStat.batteryInfo.batteryHealth != OnLineMonitor.this.mBatteryHealth && OnLineMonitor.this.mBatteryHealth == 3) {
                    OnLineMonitor.this.showMessage("电池过热!");
                    OnLineMonitor.this.notifyOnlineRuntimeStat(42);
                }
                OnLineMonitor.this.mOnLineStat.batteryInfo.batteryPercent = OnLineMonitor.this.mBatteryPercent;
                OnLineMonitor.this.mOnLineStat.batteryInfo.batteryStatus = OnLineMonitor.this.mBatteryStatus;
                OnLineMonitor.this.mOnLineStat.batteryInfo.batteryV = OnLineMonitor.this.mBatteryV;
                OnLineMonitor.this.mOnLineStat.batteryInfo.batteryHealth = OnLineMonitor.this.mBatteryHealth;
                OnLineMonitor.this.mOnLineStat.batteryInfo.batteryTemp = OnLineMonitor.this.mBatteryTemp;
            }
        }
    };
    int mBatteryHealth;
    boolean mBatteryLowStat;
    int mBatteryPercent;
    int mBatteryStatus;
    double mBatteryTemp;
    int mBatteryV;
    int mBgCpuTresholdCounter;
    ConcurrentHashMap<String, ThreadIoInfo> mBlockGuardThreadInfo = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ThreadIoInfo> mBlockGuardThreadInfoTid = new ConcurrentHashMap<>();
    int mBlockingGCCount;
    int mBootActivityLoadTime;
    long mBootEndTime;
    long mBootJiffyTime = 0;
    int mBootLoadTimeTryCount;
    protected ArrayList<ResourceUsedInfo> mBootResourceUsedInfoList = new ArrayList<>(100);
    int mBootStartActivityTime;
    int mBootTotalTime;
    int mBootUsedTime;
    volatile int mCheckAnrCounter;
    long mCheckAnrTime;
    CheckFinalizerReference mCheckFinalizerReference;
    int mCheckIdleTimes = 0;
    Class mClassFragmentActivity;
    int mColdBootOffsetTime = 1000;
    volatile Context mContext;
    int mCpuCheckIntervalControl = 1000;
    float mCpuMaxFreq;
    short mCpuProcessCount;
    long mDalvikAllocated;
    long mDalvikFree;
    long mDalvikHeapSize;
    long mDalvikPss;
    long mDeviceTotalMemory;
    short mDevicesScore;
    Class<?> mDmVmInternalClazz;
    EvaluateScore mEvaluateScore;
    boolean mFileSchedIsNotExists;
    long mFirstMobileRxBytes = -1;
    long mFirstMobileTxBytes;
    short mFirstSystemRunningScore;
    long mFirstTotalRxBytes;
    long mFirstTotalTxBytes;
    Method mGetStackTraceById;
    Method mGetSupportFragmentManager;
    Method mGetTotalUss;
    int mGpuScore;
    long mGraphicsSize;
    Handler mHandler;
    protected MyHandlerThread mHandlerThread = new MyHandlerThread(TAG, 0);
    protected int mHandlerThreadTid;
    HardWareInfo mHardWareInfo;
    int mIdleCheckIntervalControl = 100;
    MessageQueue.IdleHandler mIdleHandler = new MessageQueue.IdleHandler() {
        public boolean queueIdle() {
            if (OnLineMonitor.this.mIsIdleGeted || OnLineMonitor.this.mActivityName == null) {
                return true;
            }
            long nanoTime = System.nanoTime() / 1000000;
            OnLineMonitor.this.mCheckAnrTime = nanoTime;
            if (OnLineMonitor.this.mActivityIdleTime == 0) {
                OnLineMonitor.this.mActivityIdleTime = nanoTime;
                OnLineMonitor.this.mActivityIdleFistTime = nanoTime;
            } else {
                long j = nanoTime - OnLineMonitor.this.mActivityIdleTime;
                if (j >= ((long) OnLineMonitor.this.mIdleCheckIntervalControl)) {
                    OnLineMonitor.this.mActivityIdleTime = nanoTime;
                }
                if (OnLineMonitor.this.mMaxBlockIdletime < j) {
                    OnLineMonitor.this.mMaxBlockIdletime = j;
                }
            }
            OnLineMonitor.this.mIdleNotifyCount++;
            if (OnLineMonitor.this.mActivityIdleTime - OnLineMonitor.this.mActivityIdleFistTime >= 1000) {
                OnLineMonitor.this.mHandler.removeMessages(1);
                if (!OnLineMonitor.this.mIsActivityColdOpen) {
                    OnLineMonitor.this.mActivityIdleTime -= OnLineMonitor.this.mActivityLifecycleCallback.mActivityResumeTime;
                    OnLineMonitor.this.mActivityIdleFistTime -= OnLineMonitor.this.mActivityLifecycleCallback.mActivityResumeTime;
                } else {
                    OnLineMonitor.this.mActivityIdleTime -= OnLineMonitor.this.mActivityLifecycleCallback.mActivityOncreateTime;
                    OnLineMonitor.this.mActivityIdleFistTime -= OnLineMonitor.this.mActivityLifecycleCallback.mActivityOncreateTime;
                }
                OnLineMonitor.this.mIsIdleGeted = true;
                if (OnLineMonitor.this.mIsBootEndActivity && OnLineMonitor.this.mThreadHandler != null) {
                    OnLineMonitor.this.mThreadHandler.removeMessages(13);
                    OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(13, 6000);
                }
                OnLineMonitor.this.notifyOnActivityLifeCycleList(OnLineMonitor.this.mActivityLifecycleCallback.mActivity, 0);
                OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(2, (long) TraceDetail.sTraceThreadInterval);
                if (OnLineMonitor.sIsNormalDebug) {
                    Log.e(OnLineMonitor.TAG, OnLineMonitor.getSimpleName(OnLineMonitor.this.mActivityName) + " is idle，use time=" + OnLineMonitor.this.mActivityIdleTime + ",idleNotifyCount=" + OnLineMonitor.this.mIdleNotifyCount + ", FistIdleTime=" + OnLineMonitor.this.mActivityIdleFistTime + ",MaxBlockIdletime=" + OnLineMonitor.this.mMaxBlockIdletime);
                }
                if (OnLineMonitor.this.mActivityRuntimeInfo != null) {
                    OnLineMonitor.this.mActivityRuntimeInfo.idleTime = (int) OnLineMonitor.this.mActivityIdleTime;
                    OnLineMonitor.this.mActivityRuntimeInfo.checkIdleTimes = (short) OnLineMonitor.this.mIdleNotifyCount;
                }
                return false;
            }
            OnLineMonitor.this.mHandler.sendEmptyMessageDelayed(1, 10);
            return false;
        }
    };
    int mIdleNotifyCount;
    int mInitBatteryPercent = -1;
    boolean mInited;
    boolean mIoIsWaitNow;
    short mIoWiatCount;
    boolean mIsActivityColdOpen = true;
    volatile boolean mIsBootEndActivity;
    boolean mIsCheckAnrStat;
    boolean mIsCheckPerfromanceRunning = true;
    boolean mIsDeviceSampling = true;
    boolean mIsFirstOpenActivity;
    boolean mIsFullInBackGround = true;
    boolean mIsIdleGeted;
    boolean mIsInBackGround;
    volatile boolean mIsInBootStep = true;
    boolean mIsInitedActivity = false;
    boolean mIsLowMemroy;
    protected boolean mIsOnTouch = false;
    boolean mIsRooted = false;
    int mJavaHeapLimitLargeMemory;
    int mJavaHeapLimitMemory;
    long mJavaUsedMemoryPercent;
    long mLastCPUCheckTime;
    long mLastMemroyCheckTime;
    int mLastNotifyType = -1;
    long mLastThreadPoolCheckTime = 0;
    volatile int mLastTimeThreadCount;
    short mLayoutTimes;
    WeakHashMap<Object, Object> mLeakMemoryWeakMap = new WeakHashMap<>(64);
    protected LoadTimeCalculate mLoadTimeCalculate;
    Thread mMainThread;
    int mMainThreadTid;
    int mMajorFault;
    long mMaxBlockIdletime;
    int mMaxCanUseJavaMemory;
    short mMaxPidRunningScore;
    int mMaxRunningThreadCount;
    int mMaxRuntimeThreadCount;
    short mMaxSystemRunningScore;
    int mMaxThreadCount;
    ActivityManager.MemoryInfo mMemoryInfo = new ActivityManager.MemoryInfo();
    long mMemoryThreshold = 0;
    MessageQueue mMessageQueue;
    short mMinPidRunningScore;
    short mMinSystemRunningScore;
    long mMobileRxBytes;
    long mMobileTxBytes;
    int mMyAvgPidCPUPercent;
    MyCallback mMyCallback;
    int mMyPid = Process.myPid();
    int mMyPidCPUPercent;
    short mMyPidScore;
    short mMyPidScoreTestCounter;
    short mMyPidTotalScore;
    long mNativeHeapAllocatedSize;
    long mNativeHeapPss;
    long mNativeHeapSize;
    int mOldAnrCount;
    int mOldMajorFault;
    int mOldMemoryNotify;
    short mOldMyPidScore;
    short mOldSystemRunningScore;
    int mOldThreadCount = 0;
    int mOldTrimMemoryLevel = -1;
    protected ArrayList<OnAccurateBootListener> mOnAccurateBootListener = new ArrayList<>();
    protected ArrayList<OnActivityLifeCycle> mOnActivityLifeCycleList = new ArrayList<>();
    protected ArrayList<OnActivityLoadListener> mOnActivityLoadListenerList = new ArrayList<>();
    protected ArrayList<OnBackForGroundListener> mOnBackForGroundListener = new ArrayList<>(20);
    protected ArrayList<OnBootFinished> mOnBootFinishedList = new ArrayList<>();
    protected ArrayList<OnCheckViewTree> mOnCheckViewTreeList = new ArrayList<>();
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    protected ArrayList<OnLineMonitorNotify> mOnLineMonitorNotifyList = new ArrayList<>(20);
    OnLineStat mOnLineStat = new OnLineStat();
    ArrayList<OnlineStatistics> mOnlineStatistics = new ArrayList<>();
    int mOpenFileCount;
    String mOpenGlVersion;
    int mPerformanceBadTimes;
    long mPerformanceCheckTimes;
    boolean mPerformanceDecined;
    float mPidExeRunTime;
    int mPidIoWaitCount;
    int mPidIoWaitCountInit;
    int mPidIoWaitCountLast;
    int mPidIoWaitCountOld;
    int mPidIoWaitCountStart;
    int mPidIoWaitSum;
    int mPidIoWaitSumAvg;
    int mPidIoWaitSumInit = -1;
    int mPidIoWaitSumLast;
    int mPidIoWaitSumOld;
    int mPidIoWaitSumStart;
    int mPidIoWaitTotal;
    int mPidOldWaitCount;
    float mPidOldWaitSum;
    float mPidPerCpuLoad;
    float mPidPerCpuLoadAvg;
    float mPidPerCpuLoadInit;
    float mPidPerCpuLoadLast;
    float mPidPerCpuLoadStart;
    float mPidPerCpuLoadTotal;
    int mPidWaitCount;
    float mPidWaitMax;
    float mPidWaitSum;
    ProblemCheck mProblemCheck;
    ProcessCpuTracker mProcessCpuTracker = new ProcessCpuTracker(Process.myPid());
    int mRemainAvailMemory;
    ResourceUsedInfo mResourceUsedInfoCalBgApp;
    int mRunningThreadCount;
    int mRuntimeThreadCount;
    boolean mSchedIsWaitNow;
    protected SmoothCalculate mSmoothCalculate;
    protected SmoothDetailDataNotify mSmoothDetailDataNotify;
    int mStartBlockingGCCount = -1;
    long mStartBlockingGCTime = -1;
    int mStartGcCount;
    int mStatusBarHeight;
    int mSysAvgCPUPercent;
    int mSysCPUPercent;
    int mSysGetCounter;
    short mSysScoreTestCounter;
    float[] mSystemLoadAvg = new float[3];
    short mSystemRunningScore;
    short mSystemRunningTotalScore;
    short mTest;
    long mTestForTime;
    long mTestSleepTime;
    int mThreadCount = 0;
    Handler mThreadHandler;
    HashMap<String, ThreadInfo> mThreadInfoHashMap = new HashMap<>(512);
    HashMap<Integer, ThreadInfo> mThreadInfoTidHashMap = new HashMap<>(512);
    Method mThreadStats;
    long mTotalBlockingGCTime;
    int mTotalGcCount;
    int mTotalIOWaitTime;
    int mTotalMyPidCPUPercent;
    int mTotalPidRunningScore;
    int mTotalPidRunningScoreCount;
    long mTotalRxBytes;
    int mTotalSysCPUPercent;
    int mTotalSysRunningScore;
    int mTotalSysRunningScoreCount;
    long mTotalTxBytes;
    long mTotalUsedMemory;
    TraceDetail mTraceDetail;
    int mTrimMemoryLevel;
    long mUIHiddenTime;
    WeakHashMap<Bitmap, String> mWeakBitmapHashMap = new WeakHashMap<>(64);
    WeakHashMap<ThreadPoolExecutor, String> mWeakCheckedThreadPool = new WeakHashMap<>(64);

    public static class ActivityRuntimeInfo implements Serializable {
        public int[] activityBadSmoothStepCount = new int[20];
        long activityCreateTime;
        public long activityInstanceCount;
        public int activityLoadBadSmCount;
        public int activityLoadBadSmUsedTime;
        public int activityLoadSmCount;
        public int activityLoadSmUsedTime;
        public String activityName;
        public int activityScore;
        public int[] activitySingleBadSmoothStepCount = new int[20];
        public int activityTotalBadSmCount;
        public int activityTotalBadSmUsedTime;
        public int activityTotalFlingCount;
        public int activityTotalFpsCount;
        public int activityTotalSmCount;
        public int activityTotalSmLayoutTimes;
        public int activityTotalSmUsedTime;
        public int activityViewCount;
        public int activityVisibleViewCount;
        public short anrTime;
        public long appContextInstanceCount;
        public long arrayListInstanceCount;
        public int avgSm;
        public short battery;
        public int binderDeathObjectCount;
        public int binderLocalObjectCount;
        public int binderProxyObjectCount;
        public int bitmap10M;
        public int bitmap12M;
        public int bitmap15M;
        public int bitmap1M;
        public int bitmap20M;
        public int bitmap2M;
        public int bitmap4M;
        public int bitmap565Count;
        public int bitmap6M;
        public int bitmap8888Count;
        public int bitmap8M;
        public int bitmapByteCount;
        public int bitmapCount;
        public long bitmapInstanceCount;
        public int bitmapSize14Screen;
        public int bitmapSize2Screen;
        public int bitmapSizeHashScreen;
        public int bitmapSizeScreen;
        public int blockGc;
        public long blockTime;
        public long byteArrayInstanceCount;
        public long byteBufferInstanceCount;
        public short checkIdleTimes;
        int checkSystemInfoCount;
        public int classCount;
        public int cleanerObjectGetCount;
        public Map<String, Integer> cleanerObjectMap;
        public int cleanerObjectSize;
        public long collectionInstanceCount;
        public long concurrentHashMapInstanceCount;
        public int databaseMemory;
        public int dragFlingCount;
        ArrayList<TraceDetail.SmStat> dragList = new ArrayList<>(20);
        public long drawableInstanceCount;
        public long fileInstanceCount;
        public long fileStreamInstanceCount;
        public long filereadwriteInstanceCount;
        public String finalizerObject;
        public int finalizerSize;
        public short firstRelativeLayoutDepth;
        ArrayList<TraceDetail.SmStat> fpsList = new ArrayList<>(20);
        public long fragmentInstanceCount;
        public int gcCount;
        short getMemoryCount;
        public int globalAssetCount;
        public int globalAssetManagerCount;
        public long hashMapInstanceCount;
        public int idleTime;
        public long ioWait;
        public boolean isColdOpen;
        public boolean isFistTimeOpen;
        public short javaAllocal;
        public short javaEnd;
        public short javaHeapFree;
        public short javaHeapSize;
        public short javaMax;
        public short javaMemPercent;
        public short javaMin;
        public short javaStart;
        public long lastGetCleanerObjectTime;
        public long lastGetFinalizerTime;
        public long lastOpenFileGetTime;
        public short layoutTimesOnLoad;
        long[] lifeCycleArrayUsedTime;
        public long linkedListInstanceCount;
        public long listInstanceCount;
        public float loadAvg1Min;
        public String loadRelason;
        public int loadTime;
        public long mapInstanceCount;
        public short maxIdleDelayTime;
        public short maxLayoutDepth;
        public long maxLayoutUseTime;
        public float maxLoadAvg;
        public short maxRelativeLayoutDepth;
        public int maxRunningThread;
        public int maxThread;
        public short measureTimes;
        public short memEnd;
        public short memMax;
        public short memMin;
        public int memOtherApk;
        public int memOtherArt;
        public int memOtherAshmem;
        public int memOtherDex;
        public int memOtherJar;
        public int memOtherMap;
        public int memOtherOat;
        public int memOtherSo;
        public int memOtherTtf;
        public short memStart;
        public long messageInstanceCount;
        public short nativeAllocal;
        public short nativeEnd;
        public short nativeHeapFree;
        public short nativeHeapSize;
        public short nativeMax;
        public short nativeMin;
        public short nativeStart;
        public short openFile;
        public int openFileGetCount;
        public long openSslSocketCount;
        public short overDraw3xCount;
        public short overDraw4xCount;
        public long parcelCount;
        public long parcelSize;
        public short pidAvgCpu;
        public int pidIoWaitCount;
        public int pidIoWaitSumAvg;
        public float pidPerCpuLoadAvg;
        public short pidScore;
        public short redundantLayout;
        public long runnableInstanceCount;
        public int smoothViewOutRevLayoutDepth;
        public long startActivityTime;
        public boolean statisticsDiscard;
        public long stayTime;
        public long stringInstanceCount;
        public int summaryCode;
        public int summaryGraphics;
        public int summaryJavaHeap;
        public int summaryNativeHeap;
        public int summaryPrivateOther;
        public int summaryStack;
        public int summarySystem;
        public int summaryTotalpss;
        public int summaryTotalswap;
        public short suspectRelativeLayout;
        public short sysAvgCpu;
        public short sysScore;
        public int threadInterval;
        public long threadPoolExecutorInstanceCount;
        public short totalCanUseJavaMemory;
        public short totalCanUseMemory;
        public short totalLayoutCount;
        public long totalLayoutUseTime;
        public short totalMemPercent;
        public int totalPrivateClean;
        public int totalPrivateDirty;
        public int totalPss;
        public short totalRx;
        public int totalSharedClean;
        public int totalSharedDirty;
        public int totalSwappablePss;
        public short totalTx;
        public int totalUss;
        public long viewInstanceCount;
        public long viewRootInstanceCount;
    }

    public static class BatteryInfo implements Serializable {
        public int batteryHealth;
        public int batteryPercent;
        public int batteryStatus;
        public double batteryTemp;
        public int batteryV;
    }

    public static class BootStepResourceInfo {
        public int blockGcCount;
        public int blockGcTime;
        public int classCount;
        public int javaHeap;
        public float loadAvg;
        public long mainthreadJiffyTime;
        public long majorFault;
        public int nativeHeap;
        public int pidIoWaitCout;
        public int pidIoWaitTime;
        public long pidJiffyTime;
        public float pidPerCpuLoad;
        public int pidSchedWaitCout;
        public float pidSchedWaitMax;
        public float pidSchedWaitTime;
        public int pss;
        public long systemJiffyTime;
        public int threadCount;
        public long totalJiffyTime;
    }

    public static class BundleInfo implements Serializable {
        public String activityName;
        public int bundleAction;
        public String bundleName;
        public boolean isInBoot;
        ResourceUsedInfo resourceUsedInfo;
        public long threadId;
        public String threadName;
        public int tid;
    }

    public static class CpuStat implements Serializable {
        public boolean cpuAlarmInBg;
        public int iOWaitTimeAvg;
        public int myAVGPidCPUPercent;
        public int myMaxPidCPUPercent;
        public int myPidCPUPercent;
        public int pidIoWaitCount;
        public float pidPerCpuLoadAvg;
        public int pidWaitCount;
        public float pidWaitMax;
        public float pidWaitSum;
        public int sysAvgCPUPercent;
        public int sysCPUPercent;
        public int sysMaxCPUPercent;
        public float systemLoadAvg;
    }

    public static class DeviceInfo implements Serializable {
        public int apiLevel = Build.VERSION.SDK_INT;
        public String cpuArch;
        public String cpuBrand;
        public float[] cpuFreqArray;
        public float cpuMaxFreq;
        public float cpuMinFreq;
        public String cpuModel;
        public int cpuProcessCount;
        public float density;
        public long deviceTotalAvailMemory;
        public long deviceTotalMemory;
        public String gpuBrand;
        public long gpuMaxFreq;
        public String gpuModel;
        public boolean isEmulator;
        public boolean isRooted;
        public int memoryThreshold;
        public String mobileBrand = Build.BRAND;
        public String mobileModel = Build.MODEL;
        public int screenHeght;
        public int screenWidth;
        public int storeFreesize;
        public int storeTotalSize;
    }

    public static class IOStat implements Serializable {
        public int avgIOWaitTime;
        public int currentIOWaitTime;
        public int openedFileCount;
    }

    public static class MemroyStat implements Serializable {
        public int blockingGCCount = 0;
        public int dalvikPss;
        public long deviceAvailMemory;
        public int finalizerSize;
        public boolean isLowMemroy;
        public int majorFault;
        public int maxCanUseJavaMemory;
        public int maxCanUseTotalMemory;
        public boolean memoryAlert;
        public long nativePss;
        public int remainAvailMemory;
        public int summaryGraphics;
        public long totalBlockingGCTime;
        public int totalGcCount = 0;
        public long totalJavaPercent;
        public long totalMemoryPercent;
        public long totalUsedMemory;
        public int trimMemoryLevel;
    }

    public interface OnAccurateBootListener {
        void OnAccurateBootFinished(OnLineStat onLineStat, int i);
    }

    public interface OnActivityLifeCycle {
        void onActivityCreate(Activity activity, OnLineStat onLineStat);

        void onActivityDestroyed(Activity activity, OnLineStat onLineStat);

        void onActivityIdle(Activity activity, OnLineStat onLineStat);

        void onActivityPaused(Activity activity, OnLineStat onLineStat);

        void onActivityResume(Activity activity, OnLineStat onLineStat);

        void onActivityStarted(Activity activity, OnLineStat onLineStat);

        void onActivityStoped(Activity activity, OnLineStat onLineStat);
    }

    public interface OnActivityLoadListener {
        void onActivityLoadFinish(Activity activity, OnLineStat onLineStat, ActivityRuntimeInfo activityRuntimeInfo);

        void onActivityLoadStart(Activity activity, OnLineStat onLineStat, ActivityRuntimeInfo activityRuntimeInfo);
    }

    public interface OnBackForGroundListener {
        void onJustToggleBackGround();

        void onJustToggleForGround();

        void onToggleBackGround();

        void onToggleForGround();
    }

    public interface OnBootFinished {
        void onBootFinished(OnLineStat onLineStat);
    }

    public interface OnCheckViewTree {
        void onCheckViewTree(OnLineStat onLineStat, Activity activity, int i);
    }

    public interface OnDesignatedActivityName {
    }

    public interface OnLineMonitorNotify {
        void onLineMonitorNotify(int i, OnLineStat onLineStat);
    }

    public static class OnLineStat implements Serializable {
        public String activityName;
        public ActivityRuntimeInfo activityRuntimeInfo = new ActivityRuntimeInfo();
        public BatteryInfo batteryInfo = new BatteryInfo();
        public BootStepResourceInfo[] bootStepResourceInfo = new BootStepResourceInfo[3];
        public CpuStat cpuStat = new CpuStat();
        public DeviceInfo deviceInfo = new DeviceInfo();
        public IOStat iOStat = new IOStat();
        public boolean isActivityLoading;
        public boolean isActivityTouched;
        public boolean isFirstInstall;
        public boolean isFlingMode;
        public boolean isFullInBackGround;
        public boolean isInBackGround;
        public boolean isSystemIdle;
        public boolean isTouchMode;
        public WeakReference<Activity> mHomeActivity;
        public MemroyStat memroyStat = new MemroyStat();
        public PerformanceInfo performanceInfo = new PerformanceInfo();
        public int preparePidTime;
        public TrafficStatsInfo trafficStatsInfo = new TrafficStatsInfo();
    }

    public static class PerformanceInfo implements Serializable {
        public int anrCount;
        public int appProgressImportance;
        public int cpuPercentScore;
        public int cpuScore;
        public int deviceScore;
        public int eglScore;
        public int gpuScore;
        public int ioWaitScore;
        public boolean isLowPerformance;
        public int memPercentScore;
        public int memScore;
        public int myPidScore = 0;
        public String openglVersion;
        public int runTimeThreadCount;
        public int runningThreadCount;
        public int schedWaitScore;
        public int systemRunningScore = 0;
        public int threadCount;
    }

    public static class ResourceUsedInfo implements Serializable {
        public String activityName;
        Map<Thread, StackTraceElement[]> baseTheadMap;
        public int blockGcCount;
        public int blockGcTime;
        public long debugUsedCpuTime;
        public long debugUsedTime;
        public long ioWaitCout;
        public long ioWaitTime;
        public boolean isInBootStep;
        public int loadClassCount;
        public int memEnd;
        public int memJavaEnd;
        public int memJavaMax;
        public int memJavaMin;
        public int memJavaStart;
        public int memMax;
        public int memMin;
        public int memNativeEnd;
        public int memNativeMax;
        public int memNativeMin;
        public int memNativeStart;
        public int memStart;
        public Map<String, String> newTheadMap;
        public int newThreadCount;
        public int pidIoWaitCout;
        public int pidIoWaitTime;
        public long pidJiffyTime;
        public int pidSchedWaitCout;
        public long pidSchedWaitTime;
        public long schedWaitCout;
        public long schedWaitTime;
        public long systemJiffyTime;
        public String taskName;
        public int taskQueuePriority;
        public long taskThreadId;
        public int taskThreadTid;
        public int threadEnd;
        public long threadJiffyTime;
        public int threadMax;
        public int threadMin;
        public String threadName;
        public int threadStart;
        public long totalJiffyTime;
        public int type;
    }

    public interface SmoothDetailDataNotify {
        void onSmoothDetailNotify(int i, OnLineStat onLineStat, long j, long j2, short s, short[] sArr);
    }

    public static class ThreadIoInfo implements Serializable {
        public String activityName;
        public long id;
        public int ioWaitCount;
        public int ioWaitTime;
        public String methodName;
        public boolean multiplex;
        public String name;
        public int netTimes;
        public int nice;
        public int readTimes;
        public int readWriteTimes;
        public String stacks;
        public int tid;
        public long useTime;
        public int writeTimes;
    }

    public static class TrafficStatsInfo implements Serializable {
        public float activityMobileRxBytes;
        public float activityMobileTxBytes;
        public float activityTotalRxBytes;
        public float activityTotalTxBytes;
        public float totalMobileRxBytes;
        public float totalMobileTxBytes;
        public float totalTotalRxBytes;
        public float totalTotalTxBytes;
    }

    OnLineMonitor() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:65|66|67|68) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:67:0x0323 */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    OnLineMonitor(android.content.Context r12, com.taobao.onlinemonitor.ActivityLifecycleCallback r13) {
        /*
            r11 = this;
            r11.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = 20
            r0.<init>(r1)
            r11.mOnLineMonitorNotifyList = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>(r1)
            r11.mOnBackForGroundListener = r0
            com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r0 = new com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread
            java.lang.String r2 = "OnLineMonitor"
            r3 = 0
            r0.<init>(r2, r3)
            r11.mHandlerThread = r0
            r11.mIsOnTouch = r3
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r11.mOnActivityLifeCycleList = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r11.mOnBootFinishedList = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r11.mOnCheckViewTreeList = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r11.mOnActivityLoadListenerList = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r11.mOnAccurateBootListener = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r2 = 100
            r0.<init>(r2)
            r11.mBootResourceUsedInfoList = r0
            r0 = 1
            r11.mIsActivityColdOpen = r0
            r4 = 0
            r11.mMemoryThreshold = r4
            r11.mIsRooted = r3
            r11.mOldThreadCount = r3
            r11.mThreadCount = r3
            r11.mCheckIdleTimes = r3
            r6 = -1
            r11.mOldTrimMemoryLevel = r6
            r11.mLastNotifyType = r6
            r11.mStartBlockingGCCount = r6
            r7 = -1
            r11.mStartBlockingGCTime = r7
            r11.mIsCheckPerfromanceRunning = r0
            android.app.ActivityManager$MemoryInfo r9 = new android.app.ActivityManager$MemoryInfo
            r9.<init>()
            r11.mMemoryInfo = r9
            com.taobao.onlinemonitor.ProcessCpuTracker r9 = new com.taobao.onlinemonitor.ProcessCpuTracker
            int r10 = android.os.Process.myPid()
            r9.<init>(r10)
            r11.mProcessCpuTracker = r9
            r9 = 3
            float[] r9 = new float[r9]
            r11.mSystemLoadAvg = r9
            r11.mAppProgressImportance = r2
            r11.mInitBatteryPercent = r6
            r11.mFirstMobileRxBytes = r7
            r11.mIsInitedActivity = r3
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = new com.taobao.onlinemonitor.OnLineMonitor$OnLineStat
            r7.<init>()
            r11.mOnLineStat = r7
            int r7 = android.os.Process.myPid()
            r11.mMyPid = r7
            r11.mPidIoWaitSumInit = r6
            r11.mIsFullInBackGround = r0
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r11.mOnlineStatistics = r6
            java.util.HashMap r6 = new java.util.HashMap
            r7 = 512(0x200, float:7.175E-43)
            r6.<init>(r7)
            r11.mThreadInfoHashMap = r6
            java.util.HashMap r6 = new java.util.HashMap
            r6.<init>(r7)
            r11.mThreadInfoTidHashMap = r6
            r11.mIdleCheckIntervalControl = r2
            r2 = 1000(0x3e8, float:1.401E-42)
            r11.mCpuCheckIntervalControl = r2
            r11.mLastThreadPoolCheckTime = r4
            r11.mColdBootOffsetTime = r2
            r11.mIsInBootStep = r0
            r11.mIsDeviceSampling = r0
            java.util.HashMap r2 = new java.util.HashMap
            r6 = 64
            r2.<init>(r6)
            r11.mActivitysMap = r2
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>(r6)
            r11.mActivitysHotOpenMap = r2
            java.util.WeakHashMap r2 = new java.util.WeakHashMap
            r2.<init>(r6)
            r11.mLeakMemoryWeakMap = r2
            java.util.WeakHashMap r2 = new java.util.WeakHashMap
            r2.<init>(r6)
            r11.mWeakCheckedThreadPool = r2
            java.util.WeakHashMap r2 = new java.util.WeakHashMap
            r2.<init>(r6)
            r11.mWeakBitmapHashMap = r2
            java.util.concurrent.ConcurrentHashMap r2 = new java.util.concurrent.ConcurrentHashMap
            r2.<init>()
            r11.mBlockGuardThreadInfo = r2
            java.util.concurrent.ConcurrentHashMap r2 = new java.util.concurrent.ConcurrentHashMap
            r2.<init>()
            r11.mBlockGuardThreadInfoTid = r2
            r11.mBootJiffyTime = r4
            com.taobao.onlinemonitor.OnLineMonitor$1 r2 = new com.taobao.onlinemonitor.OnLineMonitor$1
            r2.<init>()
            r11.mIdleHandler = r2
            com.taobao.onlinemonitor.OnLineMonitor$2 r2 = new com.taobao.onlinemonitor.OnLineMonitor$2
            r2.<init>()
            r11.mBatInfoReceiver = r2
            r11.mContext = r12
            android.content.Context r2 = r11.mContext
            if (r2 != 0) goto L_0x010d
            java.lang.String r12 = "OnLineMonitor"
            java.lang.String r13 = "Context is null"
            android.util.Log.e(r12, r13)
            return
        L_0x010d:
            android.os.Looper r2 = android.os.Looper.myLooper()
            android.os.Looper r4 = android.os.Looper.getMainLooper()
            if (r2 == r4) goto L_0x011e
            java.lang.String r2 = "OnLineMonitor"
            java.lang.String r4 = "错误，请不要在非主线程初始化OnLineMonitor！"
            android.util.Log.e(r2, r4)
        L_0x011e:
            long r4 = java.lang.System.nanoTime()
            r6 = 1000000(0xf4240, double:4.940656E-318)
            long r4 = r4 / r6
            sOnLineMonitor = r11     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r2 = com.taobao.onlinemonitor.OnLineMonitorApp.sPropertyFilePath     // Catch:{ Throwable -> 0x0480 }
            if (r2 == 0) goto L_0x0155
            boolean r2 = sIsTraceDetail     // Catch:{ Throwable -> 0x0480 }
            if (r2 == 0) goto L_0x0155
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0480 }
            r2.<init>()     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r8 = com.taobao.onlinemonitor.OnLineMonitorApp.sPropertyFilePath     // Catch:{ Throwable -> 0x0480 }
            r2.append(r8)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r8 = "/OnlineMonitor"
            r2.append(r8)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0480 }
            sOnLineMonitorFileDir = r2     // Catch:{ Throwable -> 0x0480 }
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r8 = sOnLineMonitorFileDir     // Catch:{ Throwable -> 0x0480 }
            r2.<init>(r8)     // Catch:{ Throwable -> 0x0480 }
            boolean r8 = r2.exists()     // Catch:{ Throwable -> 0x0480 }
            if (r8 != 0) goto L_0x0155
            r2.mkdir()     // Catch:{ Throwable -> 0x0480 }
        L_0x0155:
            r11.mActivityLifecycleCallback = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.ActivityLifecycleCallback r13 = r11.mActivityLifecycleCallback     // Catch:{ Throwable -> 0x0480 }
            r13.mOnLineMonitor = r11     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.LoadTimeCalculate r13 = new com.taobao.onlinemonitor.LoadTimeCalculate     // Catch:{ Throwable -> 0x0480 }
            r13.<init>(r11)     // Catch:{ Throwable -> 0x0480 }
            r11.mLoadTimeCalculate = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.SmoothCalculate r13 = new com.taobao.onlinemonitor.SmoothCalculate     // Catch:{ Throwable -> 0x0480 }
            r13.<init>(r11)     // Catch:{ Throwable -> 0x0480 }
            r11.mSmoothCalculate = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.ActivityLifecycleCallback r13 = r11.mActivityLifecycleCallback     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.LoadTimeCalculate r2 = r11.mLoadTimeCalculate     // Catch:{ Throwable -> 0x0480 }
            r13.mLoadTimeCalculate = r2     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.ActivityLifecycleCallback r13 = r11.mActivityLifecycleCallback     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.SmoothCalculate r2 = r11.mSmoothCalculate     // Catch:{ Throwable -> 0x0480 }
            r13.mSmoothCalculate = r2     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.ProblemCheck r13 = new com.taobao.onlinemonitor.ProblemCheck     // Catch:{ Throwable -> 0x0480 }
            r13.<init>(r11)     // Catch:{ Throwable -> 0x0480 }
            r11.mProblemCheck = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.CheckFinalizerReference r13 = new com.taobao.onlinemonitor.CheckFinalizerReference     // Catch:{ Throwable -> 0x0480 }
            r13.<init>(r11)     // Catch:{ Throwable -> 0x0480 }
            r11.mCheckFinalizerReference = r13     // Catch:{ Throwable -> 0x0480 }
            r11.getTrafficStats()     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.HardWareInfo r13 = r11.mHardWareInfo     // Catch:{ Throwable -> 0x0480 }
            if (r13 != 0) goto L_0x0191
            com.taobao.onlinemonitor.HardWareInfo r13 = new com.taobao.onlinemonitor.HardWareInfo     // Catch:{ Throwable -> 0x0480 }
            r13.<init>(r11, r12)     // Catch:{ Throwable -> 0x0480 }
            r11.mHardWareInfo = r13     // Catch:{ Throwable -> 0x0480 }
        L_0x0191:
            com.taobao.onlinemonitor.EvaluateScore r12 = r11.mEvaluateScore     // Catch:{ Throwable -> 0x0480 }
            if (r12 != 0) goto L_0x019c
            com.taobao.onlinemonitor.EvaluateScore r12 = new com.taobao.onlinemonitor.EvaluateScore     // Catch:{ Throwable -> 0x0480 }
            r12.<init>()     // Catch:{ Throwable -> 0x0480 }
            r11.mEvaluateScore = r12     // Catch:{ Throwable -> 0x0480 }
        L_0x019c:
            boolean r12 = sIsTraceDetail     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x01c7
            com.taobao.onlinemonitor.TraceDetail r12 = new com.taobao.onlinemonitor.TraceDetail     // Catch:{ Throwable -> 0x0480 }
            r12.<init>(r11)     // Catch:{ Throwable -> 0x0480 }
            r11.mTraceDetail = r12     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.TraceDetail r12 = r11.mTraceDetail     // Catch:{ Throwable -> 0x0480 }
            int r13 = android.os.Process.myTid()     // Catch:{ Throwable -> 0x0480 }
            r12.mMainThreadTid = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.TraceDetail r12 = r11.mTraceDetail     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Throwable -> 0x0480 }
            int r13 = r13.mMainThreadTid     // Catch:{ Throwable -> 0x0480 }
            int r13 = android.os.Process.getThreadPriority(r13)     // Catch:{ Throwable -> 0x0480 }
            r12.mMainThreadNice = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.TraceDetail r12 = r11.mTraceDetail     // Catch:{ Throwable -> 0x0480 }
            java.lang.Thread r13 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0480 }
            int r13 = r13.getPriority()     // Catch:{ Throwable -> 0x0480 }
            r12.mMainThreadPriority = r13     // Catch:{ Throwable -> 0x0480 }
        L_0x01c7:
            java.lang.Thread r12 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0480 }
            long r12 = r12.getId()     // Catch:{ Throwable -> 0x0480 }
            r8 = 1
            int r2 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r2 != 0) goto L_0x01e1
            java.lang.Thread r12 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0480 }
            r11.mMainThread = r12     // Catch:{ Throwable -> 0x0480 }
            int r12 = android.os.Process.myTid()     // Catch:{ Throwable -> 0x0480 }
            r11.mMainThreadTid = r12     // Catch:{ Throwable -> 0x0480 }
        L_0x01e1:
            java.lang.String r12 = "org.apache.harmony.dalvik.ddmc.DdmVmInternal"
            java.lang.Class r12 = java.lang.Class.forName(r12)     // Catch:{ Exception -> 0x0280 }
            java.lang.String r13 = "getThreadStats"
            java.lang.Class[] r2 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r13 = r12.getMethod(r13, r2)     // Catch:{ Exception -> 0x0280 }
            r11.mThreadStats = r13     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r13 = r11.mThreadStats     // Catch:{ Exception -> 0x0280 }
            r13.setAccessible(r0)     // Catch:{ Exception -> 0x0280 }
            java.lang.String r13 = "getStackTraceById"
            java.lang.Class[] r2 = new java.lang.Class[r0]     // Catch:{ Exception -> 0x0280 }
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0280 }
            r2[r3] = r8     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r13 = r12.getMethod(r13, r2)     // Catch:{ Exception -> 0x0280 }
            r11.mGetStackTraceById = r13     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r13 = r11.mGetStackTraceById     // Catch:{ Exception -> 0x0280 }
            r13.setAccessible(r0)     // Catch:{ Exception -> 0x0280 }
            r11.mDmVmInternalClazz = r12     // Catch:{ Exception -> 0x0280 }
            boolean r13 = sIsTraceDetail     // Catch:{ Exception -> 0x0280 }
            if (r13 == 0) goto L_0x0243
            boolean r13 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryAllocator     // Catch:{ Exception -> 0x0280 }
            if (r13 == 0) goto L_0x0243
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            if (r13 == 0) goto L_0x0243
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.String r2 = "enableRecentAllocations"
            java.lang.Class[] r8 = new java.lang.Class[r0]     // Catch:{ Exception -> 0x0280 }
            java.lang.Class r9 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x0280 }
            r8[r3] = r9     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r2 = r12.getMethod(r2, r8)     // Catch:{ Exception -> 0x0280 }
            r13.mEnableRecentAllocations = r2     // Catch:{ Exception -> 0x0280 }
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r13 = r13.mEnableRecentAllocations     // Catch:{ Exception -> 0x0280 }
            r13.setAccessible(r0)     // Catch:{ Exception -> 0x0280 }
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.String r2 = "getRecentAllocations"
            java.lang.Class[] r8 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r12 = r12.getMethod(r2, r8)     // Catch:{ Exception -> 0x0280 }
            r13.mGetRecentAllocations = r12     // Catch:{ Exception -> 0x0280 }
            com.taobao.onlinemonitor.TraceDetail r12 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Method r12 = r12.mGetRecentAllocations     // Catch:{ Exception -> 0x0280 }
            r12.setAccessible(r0)     // Catch:{ Exception -> 0x0280 }
            java.lang.String r12 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryAllocatorActivity     // Catch:{ Exception -> 0x0280 }
        L_0x0243:
            boolean r12 = sIsTraceDetail     // Catch:{ Exception -> 0x0280 }
            if (r12 == 0) goto L_0x0288
            java.lang.String r12 = "java.lang.Thread"
            java.lang.Class r12 = java.lang.Class.forName(r12)     // Catch:{ Exception -> 0x0280 }
            int r13 = sApiLevel     // Catch:{ Exception -> 0x0280 }
            r2 = 23
            if (r13 > r2) goto L_0x025e
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.String r2 = "count"
            java.lang.reflect.Field r12 = r12.getDeclaredField(r2)     // Catch:{ Exception -> 0x0280 }
            r13.mFieldThreadCount = r12     // Catch:{ Exception -> 0x0280 }
            goto L_0x0268
        L_0x025e:
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.String r2 = "threadInitNumber"
            java.lang.reflect.Field r12 = r12.getDeclaredField(r2)     // Catch:{ Exception -> 0x0280 }
            r13.mFieldThreadCount = r12     // Catch:{ Exception -> 0x0280 }
        L_0x0268:
            com.taobao.onlinemonitor.TraceDetail r12 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Field r12 = r12.mFieldThreadCount     // Catch:{ Exception -> 0x0280 }
            r12.setAccessible(r0)     // Catch:{ Exception -> 0x0280 }
            com.taobao.onlinemonitor.TraceDetail r12 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            int[] r12 = r12.mNewTheadCountAyr     // Catch:{ Exception -> 0x0280 }
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Exception -> 0x0280 }
            java.lang.reflect.Field r13 = r13.mFieldThreadCount     // Catch:{ Exception -> 0x0280 }
            java.lang.Class<java.lang.Thread> r2 = java.lang.Thread.class
            int r13 = r13.getInt(r2)     // Catch:{ Exception -> 0x0280 }
            r12[r3] = r13     // Catch:{ Exception -> 0x0280 }
            goto L_0x0288
        L_0x0280:
            r12 = move-exception
            boolean r13 = sIsNormalDebug     // Catch:{ Throwable -> 0x0480 }
            if (r13 == 0) goto L_0x0288
            r12.printStackTrace()     // Catch:{ Throwable -> 0x0480 }
        L_0x0288:
            com.taobao.onlinemonitor.HardWareInfo r12 = r11.mHardWareInfo     // Catch:{ Throwable -> 0x0480 }
            int r12 = r12.getCpuCore()     // Catch:{ Throwable -> 0x0480 }
            short r12 = (short) r12     // Catch:{ Throwable -> 0x0480 }
            r11.mCpuProcessCount = r12     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.HardWareInfo r12 = r11.mHardWareInfo     // Catch:{ Throwable -> 0x0480 }
            float r12 = r12.getMaxCpuFreq()     // Catch:{ Throwable -> 0x0480 }
            r11.mCpuMaxFreq = r12     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            float r13 = r11.mCpuMaxFreq     // Catch:{ Throwable -> 0x0480 }
            r12.cpuMaxFreq = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.HardWareInfo r12 = r11.mHardWareInfo     // Catch:{ Throwable -> 0x0480 }
            long r12 = r12.getMaxGpuFreq()     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r2 = r2.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            r2.gpuMaxFreq = r12     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            short r13 = r11.mCpuProcessCount     // Catch:{ Throwable -> 0x0480 }
            r12.cpuProcessCount = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$MyCallback r12 = new com.taobao.onlinemonitor.OnLineMonitor$MyCallback     // Catch:{ Throwable -> 0x0480 }
            r12.<init>()     // Catch:{ Throwable -> 0x0480 }
            r11.mMyCallback = r12     // Catch:{ Throwable -> 0x0480 }
            android.content.Context r12 = r11.mContext     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = "activity"
            java.lang.Object r12 = r12.getSystemService(r13)     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager r12 = (android.app.ActivityManager) r12     // Catch:{ Throwable -> 0x0480 }
            r11.mActivityManager = r12     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager r12 = r11.mActivityManager     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x03a5
            com.taobao.onlinemonitor.ProcessCpuTracker r12 = r11.mProcessCpuTracker     // Catch:{ Throwable -> 0x0480 }
            short r13 = r11.mCpuProcessCount     // Catch:{ Throwable -> 0x0480 }
            r12.mCpuCount = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.ProcessCpuTracker r12 = r11.mProcessCpuTracker     // Catch:{ Throwable -> 0x0480 }
            r12.update()     // Catch:{ Throwable -> 0x0480 }
            r11.getCpuInfo(r0, r0, r0)     // Catch:{ Throwable -> 0x0480 }
            r11.recordBootResource(r3, r3)     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager r12 = r11.mActivityManager     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager$MemoryInfo r13 = r11.mMemoryInfo     // Catch:{ Throwable -> 0x0480 }
            r12.getMemoryInfo(r13)     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager r12 = r11.mActivityManager     // Catch:{ Throwable -> 0x0480 }
            int r12 = r12.getMemoryClass()     // Catch:{ Throwable -> 0x0480 }
            r11.mJavaHeapLimitMemory = r12     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager r12 = r11.mActivityManager     // Catch:{ Throwable -> 0x0480 }
            int r12 = r12.getLargeMemoryClass()     // Catch:{ Throwable -> 0x0480 }
            r11.mJavaHeapLimitLargeMemory = r12     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager r12 = r11.mActivityManager     // Catch:{ Throwable -> 0x0301 }
            android.content.pm.ConfigurationInfo r12 = r12.getDeviceConfigurationInfo()     // Catch:{ Throwable -> 0x0301 }
            java.lang.String r12 = r12.getGlEsVersion()     // Catch:{ Throwable -> 0x0301 }
            r11.mOpenGlVersion = r12     // Catch:{ Throwable -> 0x0301 }
            goto L_0x0305
        L_0x0301:
            java.lang.String r12 = "2.0"
            r11.mOpenGlVersion = r12     // Catch:{ Throwable -> 0x0480 }
        L_0x0305:
            boolean r12 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsLargeHeap     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x030e
            int r12 = r11.mJavaHeapLimitLargeMemory     // Catch:{ Throwable -> 0x0480 }
            r11.mMaxCanUseJavaMemory = r12     // Catch:{ Throwable -> 0x0480 }
            goto L_0x0312
        L_0x030e:
            int r12 = r11.mJavaHeapLimitMemory     // Catch:{ Throwable -> 0x0480 }
            r11.mMaxCanUseJavaMemory = r12     // Catch:{ Throwable -> 0x0480 }
        L_0x0312:
            int r12 = sApiLevel     // Catch:{ Throwable -> 0x0480 }
            r13 = 16
            r2 = 1024(0x400, double:5.06E-321)
            if (r12 < r13) goto L_0x032d
            android.app.ActivityManager$MemoryInfo r12 = r11.mMemoryInfo     // Catch:{ Throwable -> 0x0323 }
            long r12 = r12.totalMem     // Catch:{ Throwable -> 0x0323 }
            long r12 = r12 / r2
            long r12 = r12 / r2
            r11.mDeviceTotalMemory = r12     // Catch:{ Throwable -> 0x0323 }
            goto L_0x0336
        L_0x0323:
            int r12 = r11.getTotalMemFromFile()     // Catch:{ Throwable -> 0x0480 }
            int r12 = r12 / 1024
            long r12 = (long) r12     // Catch:{ Throwable -> 0x0480 }
            r11.mDeviceTotalMemory = r12     // Catch:{ Throwable -> 0x0480 }
            goto L_0x0336
        L_0x032d:
            int r12 = r11.getTotalMemFromFile()     // Catch:{ Throwable -> 0x0480 }
            int r12 = r12 / 1024
            long r12 = (long) r12     // Catch:{ Throwable -> 0x0480 }
            r11.mDeviceTotalMemory = r12     // Catch:{ Throwable -> 0x0480 }
        L_0x0336:
            android.app.ActivityManager$MemoryInfo r12 = r11.mMemoryInfo     // Catch:{ Throwable -> 0x033f }
            long r12 = r12.threshold     // Catch:{ Throwable -> 0x033f }
            long r12 = r12 / r2
            long r12 = r12 / r2
            r11.mMemoryThreshold = r12     // Catch:{ Throwable -> 0x033f }
            goto L_0x0343
        L_0x033f:
            r12 = 64
            r11.mMemoryThreshold = r12     // Catch:{ Throwable -> 0x0480 }
        L_0x0343:
            android.app.ActivityManager$MemoryInfo r12 = r11.mMemoryInfo     // Catch:{ Throwable -> 0x0480 }
            long r12 = r12.availMem     // Catch:{ Throwable -> 0x0480 }
            long r12 = r12 / r2
            long r12 = r12 / r2
            r11.mAvailMemory = r12     // Catch:{ Throwable -> 0x0480 }
            android.app.ActivityManager$MemoryInfo r12 = r11.mMemoryInfo     // Catch:{ Throwable -> 0x0480 }
            boolean r12 = r12.lowMemory     // Catch:{ Throwable -> 0x0480 }
            r11.mIsLowMemroy = r12     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r12 = r12.memroyStat     // Catch:{ Throwable -> 0x0480 }
            int r13 = r11.mMaxCanUseJavaMemory     // Catch:{ Throwable -> 0x0480 }
            r12.maxCanUseJavaMemory = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            long r2 = r11.mMemoryThreshold     // Catch:{ Throwable -> 0x0480 }
            int r13 = (int) r2     // Catch:{ Throwable -> 0x0480 }
            r12.memoryThreshold = r13     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            long r2 = r11.mDeviceTotalMemory     // Catch:{ Throwable -> 0x0480 }
            r12.deviceTotalAvailMemory = r2     // Catch:{ Throwable -> 0x0480 }
            long r12 = r11.mDeviceTotalMemory     // Catch:{ Throwable -> 0x0480 }
            r2 = 256(0x100, double:1.265E-321)
            int r8 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r8 >= 0) goto L_0x0375
            r11.mDeviceTotalMemory = r2     // Catch:{ Throwable -> 0x0480 }
            goto L_0x0392
        L_0x0375:
            long r12 = r11.mDeviceTotalMemory     // Catch:{ Throwable -> 0x0480 }
            r2 = 512(0x200, double:2.53E-321)
            int r8 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r8 >= 0) goto L_0x0380
            r11.mDeviceTotalMemory = r2     // Catch:{ Throwable -> 0x0480 }
            goto L_0x0392
        L_0x0380:
            r12 = 1
        L_0x0381:
            if (r12 > r1) goto L_0x0392
            int r13 = r12 * 1024
            long r2 = r11.mDeviceTotalMemory     // Catch:{ Throwable -> 0x0480 }
            long r8 = (long) r13     // Catch:{ Throwable -> 0x0480 }
            int r13 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r13 >= 0) goto L_0x038f
            r11.mDeviceTotalMemory = r8     // Catch:{ Throwable -> 0x0480 }
            goto L_0x0392
        L_0x038f:
            int r12 = r12 + 1
            goto L_0x0381
        L_0x0392:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            long r1 = r11.mDeviceTotalMemory     // Catch:{ Throwable -> 0x0480 }
            r12.deviceTotalMemory = r1     // Catch:{ Throwable -> 0x0480 }
            r11.evaluateSystemPerformance()     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.DynamicActivityManagerNative r12 = new com.taobao.onlinemonitor.DynamicActivityManagerNative     // Catch:{ Throwable -> 0x0480 }
            r12.<init>(r11)     // Catch:{ Throwable -> 0x0480 }
            r12.doProxy()     // Catch:{ Throwable -> 0x0480 }
        L_0x03a5:
            r11.mInited = r0     // Catch:{ Throwable -> 0x0480 }
            boolean r12 = sIsTraceDetail     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x0400
            long r12 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0480 }
            long r12 = r12 / r6
            java.lang.String r1 = "OnLineMonitor"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0480 }
            r2.<init>()     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r3 = "最大可用Java内存="
            r2.append(r3)     // Catch:{ Throwable -> 0x0480 }
            int r3 = r11.mMaxCanUseJavaMemory     // Catch:{ Throwable -> 0x0480 }
            r2.append(r3)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r3 = ", 设备总内存="
            r2.append(r3)     // Catch:{ Throwable -> 0x0480 }
            long r6 = r11.mDeviceTotalMemory     // Catch:{ Throwable -> 0x0480 }
            r2.append(r6)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r3 = ",MemoryThreshold="
            r2.append(r3)     // Catch:{ Throwable -> 0x0480 }
            long r6 = r11.mMemoryThreshold     // Catch:{ Throwable -> 0x0480 }
            r2.append(r6)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r3 = ",剩余可用内存="
            r2.append(r3)     // Catch:{ Throwable -> 0x0480 }
            long r6 = r11.mAvailMemory     // Catch:{ Throwable -> 0x0480 }
            r2.append(r6)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r3 = ",OnLineMonitor初始化耗时="
            r2.append(r3)     // Catch:{ Throwable -> 0x0480 }
            r3 = 0
            long r12 = r12 - r4
            r2.append(r12)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = " ms"
            r2.append(r12)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = r2.toString()     // Catch:{ Throwable -> 0x0480 }
            android.util.Log.e(r1, r12)     // Catch:{ Throwable -> 0x0480 }
            boolean r12 = r11.mIsLowMemroy     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x0400
            java.lang.String r12 = "OnLineMonitor"
            java.lang.String r13 = "目前处于低内存状态，运行会比较慢!"
            android.util.Log.e(r12, r13)     // Catch:{ Throwable -> 0x0480 }
        L_0x0400:
            boolean r12 = sIsTraceDetail     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x043a
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0480 }
            r12.<init>()     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = "设备得分"
            r12.append(r13)     // Catch:{ Throwable -> 0x0480 }
            short r13 = r11.mDevicesScore     // Catch:{ Throwable -> 0x0480 }
            r12.append(r13)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = ",属于"
            r12.append(r13)     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = r13.getDeviceStatus()     // Catch:{ Throwable -> 0x0480 }
            r12.append(r13)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = ",系统"
            r12.append(r13)     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.TraceDetail r13 = r11.mTraceDetail     // Catch:{ Throwable -> 0x0480 }
            short r1 = r11.mFirstSystemRunningScore     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = r13.getRunStatus(r1)     // Catch:{ Throwable -> 0x0480 }
            r12.append(r13)     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = r12.toString()     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = "OnLineMonitor"
            android.util.Log.e(r13, r12)     // Catch:{ Throwable -> 0x0480 }
        L_0x043a:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x0484
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = r12.cpuBrand     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x0484
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = r12.cpuBrand     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = "GOLDFISH"
            boolean r12 = r12.equalsIgnoreCase(r13)     // Catch:{ Throwable -> 0x0480 }
            if (r12 != 0) goto L_0x0472
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = r12.cpuBrand     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = "RANCHU"
            boolean r12 = r12.equalsIgnoreCase(r13)     // Catch:{ Throwable -> 0x0480 }
            if (r12 != 0) goto L_0x0472
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r12 = r12.cpuBrand     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r13 = "VBOX86"
            boolean r12 = r12.equalsIgnoreCase(r13)     // Catch:{ Throwable -> 0x0480 }
            if (r12 == 0) goto L_0x0484
        L_0x0472:
            java.lang.String r12 = "OnLineMonitor"
            java.lang.String r13 = "你正在使用模拟器，该设备得分可能会不准确!"
            android.util.Log.e(r12, r13)     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r12 = r11.mOnLineStat     // Catch:{ Throwable -> 0x0480 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r12 = r12.deviceInfo     // Catch:{ Throwable -> 0x0480 }
            r12.isEmulator = r0     // Catch:{ Throwable -> 0x0480 }
            goto L_0x0484
        L_0x0480:
            r12 = move-exception
            r12.printStackTrace()
        L_0x0484:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.<init>(android.content.Context, com.taobao.onlinemonitor.ActivityLifecycleCallback):void");
    }

    public static OnLineMonitor getInstance() {
        if (sOnLineMonitor == null) {
            sOnLineMonitor = new OnLineMonitor();
        }
        return sOnLineMonitor;
    }

    public int getDeviceScore() {
        if (sOnLineMonitor == null) {
            return 60;
        }
        return sOnLineMonitor.mDevicesScore;
    }

    public int getSystemScore() {
        if (sOnLineMonitor == null) {
            return 50;
        }
        return sOnLineMonitor.mSystemRunningScore;
    }

    public int getPidScore() {
        if (sOnLineMonitor == null) {
            return 50;
        }
        return sOnLineMonitor.mMyPidScore;
    }

    public ActivityRuntimeInfo getActivityRuntimeInfo() {
        if (this.mActivityRuntimeInfo == null) {
            return new ActivityRuntimeInfo();
        }
        return this.mActivityRuntimeInfo;
    }

    public int getActivityLoadTime() {
        if (this.mActivityRuntimeInfo == null) {
            return 0;
        }
        return this.mActivityRuntimeInfo.loadTime;
    }

    public OnLineStat getStat() {
        if (this.mOnLineStat == null) {
            return new OnLineStat();
        }
        return this.mOnLineStat;
    }

    public DeviceInfo getDeviceInfo() {
        return getStat().deviceInfo;
    }

    public PerformanceInfo getPerformanceInfo() {
        return getStat().performanceInfo;
    }

    public CpuStat getCpuStat() {
        return getStat().cpuStat;
    }

    public IOStat getIOStat() {
        return getStat().iOStat;
    }

    public BatteryInfo getBatteryInfo() {
        return getStat().batteryInfo;
    }

    public TrafficStatsInfo getTrafficStatsInfo() {
        return getStat().trafficStatsInfo;
    }

    public MemroyStat getMemroyStat() {
        return getStat().memroyStat;
    }

    @Deprecated
    public static OnLineStat getOnLineStat() {
        if (sOnLineMonitor == null) {
            return new OnLineStat();
        }
        return sOnLineMonitor.mOnLineStat;
    }

    public static boolean isTraceDetail() {
        return sIsTraceDetail;
    }

    public static boolean registerOnActivityLifeCycle(OnActivityLifeCycle onActivityLifeCycle) {
        if (sOnLineMonitor == null || onActivityLifeCycle == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnActivityLifeCycleList) {
            sOnLineMonitor.mOnActivityLifeCycleList.add(onActivityLifeCycle);
            if (TraceDetail.sTraceOnLineListener) {
                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                methodInfo.activityName = sOnLineMonitor.mActivityName;
                methodInfo.methodName = onActivityLifeCycle.getClass().getName();
                if (sOnLineMonitor.mTraceDetail != null) {
                    sOnLineMonitor.mTraceDetail.mOnActivityLifeCycleList.add(methodInfo);
                }
            }
        }
        return true;
    }

    public static void unregisterOnActivityLifeCycle(OnActivityLifeCycle onActivityLifeCycle) {
        if (sOnLineMonitor != null && onActivityLifeCycle != null) {
            synchronized (sOnLineMonitor.mOnActivityLifeCycleList) {
                sOnLineMonitor.mOnActivityLifeCycleList.remove(onActivityLifeCycle);
            }
        }
    }

    public static boolean registerOnBootFinished(OnBootFinished onBootFinished) {
        if (sOnLineMonitor == null || onBootFinished == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnBootFinishedList) {
            sOnLineMonitor.mOnBootFinishedList.add(onBootFinished);
        }
        return true;
    }

    public static boolean registerOnCheckViewTree(OnCheckViewTree onCheckViewTree) {
        if (sOnLineMonitor == null || onCheckViewTree == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnCheckViewTreeList) {
            sOnLineMonitor.mOnCheckViewTreeList.add(onCheckViewTree);
        }
        return true;
    }

    @Deprecated
    public static boolean regsterOnBootFinished(OnBootFinished onBootFinished) {
        return registerOnBootFinished(onBootFinished);
    }

    public static void unregisterOnBootFinished(OnBootFinished onBootFinished) {
        if (sOnLineMonitor != null && onBootFinished != null) {
            synchronized (sOnLineMonitor.mOnBootFinishedList) {
                sOnLineMonitor.mOnBootFinishedList.remove(onBootFinished);
            }
        }
    }

    public static void unregisterOnCheckViewTree(OnCheckViewTree onCheckViewTree) {
        if (sOnLineMonitor != null && onCheckViewTree != null) {
            synchronized (sOnLineMonitor.mOnCheckViewTreeList) {
                sOnLineMonitor.mOnCheckViewTreeList.remove(onCheckViewTree);
            }
        }
    }

    public static boolean registerOnActivityLoadListener(OnActivityLoadListener onActivityLoadListener) {
        if (sOnLineMonitor == null || onActivityLoadListener == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnActivityLoadListenerList) {
            sOnLineMonitor.mOnActivityLoadListenerList.add(onActivityLoadListener);
        }
        return true;
    }

    public static void unregisterOnActivityLoadListener(OnActivityLoadListener onActivityLoadListener) {
        if (sOnLineMonitor != null && onActivityLoadListener != null) {
            synchronized (sOnLineMonitor.mOnActivityLoadListenerList) {
                sOnLineMonitor.mOnActivityLoadListenerList.remove(onActivityLoadListener);
            }
        }
    }

    public static boolean registerOnAccurateBootListener(OnAccurateBootListener onAccurateBootListener) {
        if (sOnLineMonitor == null || onAccurateBootListener == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnAccurateBootListener) {
            sOnLineMonitor.mOnAccurateBootListener.add(onAccurateBootListener);
        }
        return true;
    }

    public static void unregisterOnAccurateBootListener(OnAccurateBootListener onAccurateBootListener) {
        if (sOnLineMonitor != null && onAccurateBootListener != null) {
            synchronized (sOnLineMonitor.mOnAccurateBootListener) {
                sOnLineMonitor.mOnAccurateBootListener.remove(onAccurateBootListener);
            }
        }
    }

    public static Application.ActivityLifecycleCallbacks getActivityLifecycle() {
        if (sOnLineMonitor == null) {
            return null;
        }
        return sOnLineMonitor.mActivityLifecycleCallback;
    }

    public static ActivityLifecycleCallback getOnLineMonitorLifecycle() {
        if (sOnLineMonitor == null) {
            return null;
        }
        return sOnLineMonitor.mActivityLifecycleCallback;
    }

    public static boolean isInHomeActivity() {
        if (sOnLineMonitor == null || OnLineMonitorApp.sBootActivityAry == null || OnLineMonitorApp.sBootActivityAry.length == 0) {
            return false;
        }
        String str = OnLineMonitorApp.sBootActivityAry[OnLineMonitorApp.sBootActivityAry.length - 1];
        if (str == null) {
            return false;
        }
        return str.equals(sOnLineMonitor.mActivityName);
    }

    public static boolean isActivityTouched() {
        if (sOnLineMonitor == null || sOnLineMonitor.mOnLineStat == null) {
            return false;
        }
        return sOnLineMonitor.mOnLineStat.isActivityTouched;
    }

    @Deprecated
    public static void setOnlineStatistics(OnlineStatistics onlineStatistics) {
        if (sOnLineMonitor != null) {
            registerOnlineStatistics(onlineStatistics);
        }
    }

    public static boolean registerOnlineStatistics(OnlineStatistics onlineStatistics) {
        if (sOnLineMonitor == null || onlineStatistics == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnlineStatistics) {
            sOnLineMonitor.mOnlineStatistics.add(onlineStatistics);
        }
        return true;
    }

    public static boolean unregisterOnlineStatistics(OnlineStatistics onlineStatistics) {
        boolean remove;
        if (sOnLineMonitor == null || onlineStatistics == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnlineStatistics) {
            remove = sOnLineMonitor.mOnlineStatistics.remove(onlineStatistics);
        }
        return remove;
    }

    public static void putCheckedThreadPool(ThreadPoolExecutor threadPoolExecutor, String str) {
        if (sOnLineMonitor != null && threadPoolExecutor != null && sOnLineMonitor.mWeakCheckedThreadPool != null) {
            sOnLineMonitor.mWeakCheckedThreadPool.put(threadPoolExecutor, str);
        }
    }

    static String getStackTraceElement(StackTraceElement[] stackTraceElementArr, int i, int i2) {
        if (stackTraceElementArr == null) {
            return null;
        }
        if (i >= stackTraceElementArr.length) {
            i = 0;
        }
        StringBuilder sb = new StringBuilder(200);
        while (i < stackTraceElementArr.length) {
            sb.append(stackTraceElementArr[i].toString());
            sb.append(";</br>");
            if (i2 > 0 && i >= i2) {
                break;
            }
            i++;
        }
        if (sb.length() < 100 && sb.length() > 0) {
            sb.append("Test For StackTraceElement;Test For StackTraceElement；Test For StackTraceElement；");
        }
        return sb.toString();
    }

    @Deprecated
    public static void setThreadRunTimeInfo(String str, int i, long j, long j2, long j3, String str2, int i2, ThreadPoolExecutor threadPoolExecutor) {
        int i3;
        StackTraceElement[] stackTraceElementArr;
        int i4 = i;
        if (sOnLineMonitor != null) {
            OnLineMonitor onLineMonitor = sOnLineMonitor;
            if (sIsTraceDetail && TraceDetail.sTraceStatisticsThread && j2 >= ((long) TraceDetail.sTraceRegThreadThreshold)) {
                if (threadPoolExecutor == null || threadPoolExecutor.getQueue() == null) {
                    i3 = 0;
                } else {
                    int size = threadPoolExecutor.getQueue().size();
                    if (size > 256 && sOnLineMonitor != null) {
                        long nanoTime = System.nanoTime() / 1000000;
                        if (nanoTime - sOnLineMonitor.mLastThreadPoolCheckTime >= 5000) {
                            sOnLineMonitor.showMessage("线程池过于繁忙!");
                            sOnLineMonitor.notifyOnlineRuntimeStat(11);
                            sOnLineMonitor.mLastThreadPoolCheckTime = nanoTime;
                        }
                    }
                    i3 = size;
                }
                int threadPriority = Process.getThreadPriority(i);
                String str3 = "";
                if (threadPoolExecutor != null) {
                    str3 = threadPoolExecutor.getClass().getName() + '@' + Integer.toHexString(threadPoolExecutor.hashCode());
                }
                String str4 = str3;
                float[] fArr = new float[7];
                if (i4 > 0) {
                    sOnLineMonitor.getThreadIoWaitAndLoadAvg(i4, fArr);
                }
                ThreadInfo threadInfo = new ThreadInfo((int) Thread.currentThread().getId(), str, 0, i, j2, j3, false, threadPriority, j, 0, 0, sOnLineMonitor.mIsInBackGround, sOnLineMonitor.mActivityName, (int) fArr[5], (int) fArr[5], (int) fArr[3], (int) fArr[0], "");
                threadInfo.mClassName = str2;
                threadInfo.mQueuePriority = i2;
                threadInfo.mPoolName = str4;
                threadInfo.mQueueSize = i3;
                sOnLineMonitor.mTraceDetail.getThreadSchedTime(threadInfo);
                TraceDetail traceDetail = sOnLineMonitor.mTraceDetail;
                if (TraceDetail.sTraceStatisticsPercent && sOnLineMonitor.mIsInBootStep) {
                    try {
                        synchronized (sOnLineMonitor) {
                            Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
                            int size2 = allStackTraces.size();
                            if (sOnLineMonitor.mLastTimeThreadCount < size2) {
                                sOnLineMonitor.mLastTimeThreadCount = size2;
                                if (sOnLineMonitor.mIsInBootStep) {
                                    StringBuilder sb = new StringBuilder(300);
                                    for (Map.Entry next : allStackTraces.entrySet()) {
                                        if (next != null) {
                                            Thread thread = (Thread) next.getKey();
                                            String str5 = thread.getName() + DinamicConstant.DINAMIC_PREFIX_AT + thread.getClass();
                                            if (!sOnLineMonitor.mTraceDetail.mBootDiffThreadMap.containsKey(str5) && (stackTraceElementArr = (StackTraceElement[]) next.getValue()) != null) {
                                                for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                                                    sb.append(stackTraceElement.toString());
                                                }
                                                sOnLineMonitor.mTraceDetail.mBootDiffThreadMap.put(str5, sb.substring(0));
                                                if (threadInfo.mIncreaseThreadList == null) {
                                                    threadInfo.mIncreaseThreadList = new ArrayList<>();
                                                }
                                                threadInfo.mIncreaseThreadList.add(str5);
                                                sb.setLength(0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        threadInfo.mTotalThreadCount = sOnLineMonitor.mLastTimeThreadCount;
                        sOnLineMonitor.mProcessCpuTracker.update();
                        int i5 = sOnLineMonitor.mProcessCpuTracker.mTotalSysPercent;
                        threadInfo.mMaxPercentInPid = (short) sOnLineMonitor.mProcessCpuTracker.mMyPidPercent;
                        threadInfo.mMaxPercentInDevice = (short) i5;
                    } catch (Throwable unused) {
                    }
                }
                synchronized (sOnLineMonitor.mTraceDetail.mExecuteThreadInfoList) {
                    sOnLineMonitor.mTraceDetail.mExecuteThreadInfoList.add(threadInfo);
                }
            }
        }
    }

    @Deprecated
    public static boolean regsterOnlineNotify(OnLineMonitorNotify onLineMonitorNotify) {
        return registerOnlineNotify(onLineMonitorNotify);
    }

    public static boolean registerOnlineNotify(OnLineMonitorNotify onLineMonitorNotify) {
        if (sOnLineMonitor == null || onLineMonitorNotify == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnLineMonitorNotifyList) {
            sOnLineMonitor.mOnLineMonitorNotifyList.add(onLineMonitorNotify);
            if (TraceDetail.sTraceOnLineListener) {
                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                methodInfo.activityName = sOnLineMonitor.mActivityName;
                methodInfo.methodName = onLineMonitorNotify.getClass().getName();
                if (sOnLineMonitor.mTraceDetail != null) {
                    sOnLineMonitor.mTraceDetail.mOnLineMonitorNotifyList.add(methodInfo);
                }
            }
        }
        return true;
    }

    public static boolean unregisterOnlineNotify(OnLineMonitorNotify onLineMonitorNotify) {
        boolean remove;
        if (sOnLineMonitor == null || onLineMonitorNotify == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnLineMonitorNotifyList) {
            remove = sOnLineMonitor.mOnLineMonitorNotifyList.remove(onLineMonitorNotify);
        }
        return remove;
    }

    public static boolean registerBackForGroundListener(OnBackForGroundListener onBackForGroundListener) {
        if (sOnLineMonitor == null || onBackForGroundListener == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnBackForGroundListener) {
            sOnLineMonitor.mOnBackForGroundListener.add(onBackForGroundListener);
            if (TraceDetail.sTraceOnLineListener) {
                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                methodInfo.activityName = sOnLineMonitor.mActivityName;
                methodInfo.methodName = onBackForGroundListener.getClass().getName();
                if (sOnLineMonitor.mTraceDetail != null) {
                    sOnLineMonitor.mTraceDetail.mOnLineMonitorNotifyList.add(methodInfo);
                }
            }
        }
        return true;
    }

    public static boolean unregisterBackForGroundListener(OnBackForGroundListener onBackForGroundListener) {
        boolean remove;
        if (sOnLineMonitor == null || onBackForGroundListener == null) {
            return false;
        }
        synchronized (sOnLineMonitor.mOnBackForGroundListener) {
            remove = sOnLineMonitor.mOnBackForGroundListener.remove(onBackForGroundListener);
        }
        return remove;
    }

    public static boolean registerSmoothDetailListener(SmoothDetailDataNotify smoothDetailDataNotify) {
        if (sOnLineMonitor == null || smoothDetailDataNotify == null || sOnLineMonitor.mSmoothCalculate == null) {
            return false;
        }
        sOnLineMonitor.mSmoothDetailDataNotify = smoothDetailDataNotify;
        sOnLineMonitor.mSmoothCalculate.mFrameTimeByteArray = new short[3600];
        sOnLineMonitor.mSmoothCalculate.mFrameTimeIndex = 0;
        sOnLineMonitor.mLoadTimeCalculate.mFrameTimeByteArray = new short[3600];
        sOnLineMonitor.mLoadTimeCalculate.mFrameTimeIndex = 0;
        return true;
    }

    public static boolean unregisterSmoothDetailListener() {
        if (sOnLineMonitor == null || sOnLineMonitor.mSmoothCalculate == null) {
            return false;
        }
        sOnLineMonitor.mSmoothDetailDataNotify = null;
        sOnLineMonitor.mSmoothCalculate.mFrameTimeIndex = 0;
        sOnLineMonitor.mLoadTimeCalculate.mFrameTimeByteArray = null;
        sOnLineMonitor.mLoadTimeCalculate.mFrameTimeIndex = 0;
        return true;
    }

    public static void start() {
        if (sOnLineMonitor != null && sOnLineMonitor.mHandlerThread != null) {
            sOnLineMonitor.mHandlerThread.start();
        }
    }

    public static Activity getCurrentActivity() {
        if (sOnLineMonitor == null || sOnLineMonitor.mActivityLifecycleCallback == null) {
            return null;
        }
        return sOnLineMonitor.mActivityLifecycleCallback.mActivity;
    }

    public static ResourceUsedInfo onTaskStart(String str, int i, int i2) {
        if (sOnLineMonitor == null) {
            return null;
        }
        if (!sIsTraceDetail && !sOnLineMonitor.mIsDeviceSampling) {
            return null;
        }
        ResourceUsedInfo resourceUsedInfo = getResourceUsedInfo((ResourceUsedInfo) null, false, false, false, false);
        resourceUsedInfo.taskName = str;
        resourceUsedInfo.taskQueuePriority = i;
        resourceUsedInfo.type = i2;
        resourceUsedInfo.isInBootStep = sOnLineMonitor.mIsInBootStep;
        return resourceUsedInfo;
    }

    public static void onTaskEnd(ResourceUsedInfo resourceUsedInfo) {
        if (sOnLineMonitor != null && resourceUsedInfo != null) {
            getResourceUsedInfo(resourceUsedInfo, false, false, false, false);
            if (resourceUsedInfo.type == 100000 && sOnLineMonitor.mBootResourceUsedInfoList != null) {
                synchronized (sOnLineMonitor.mBootResourceUsedInfoList) {
                    sOnLineMonitor.mBootResourceUsedInfoList.add(resourceUsedInfo);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void recordBootResource(int i, boolean z) {
        BootStepResourceInfo bootStepResourceInfo;
        if (i == 0) {
            bootStepResourceInfo = new BootStepResourceInfo();
            if (this.mOnLineStat.bootStepResourceInfo != null) {
                this.mOnLineStat.bootStepResourceInfo[0] = bootStepResourceInfo;
            }
        } else if (i < 3) {
            if (this.mOnLineStat.bootStepResourceInfo[i] == null) {
                BootStepResourceInfo bootStepResourceInfo2 = new BootStepResourceInfo();
                this.mOnLineStat.bootStepResourceInfo[i] = bootStepResourceInfo2;
                bootStepResourceInfo = bootStepResourceInfo2;
            } else {
                bootStepResourceInfo = this.mOnLineStat.bootStepResourceInfo[i];
            }
            if (z) {
                getCpuInfo(true, false, false);
                if (this.mMainThreadTid > 0) {
                    bootStepResourceInfo.mainthreadJiffyTime = sOnLineMonitor.mProcessCpuTracker.loadTaskTime(this.mMainThreadTid);
                }
            }
            if (Looper.myLooper() != Looper.getMainLooper()) {
                z = false;
            }
            if (!z) {
                if (bootStepResourceInfo.pss <= 0) {
                    getMemInfo(true);
                    bootStepResourceInfo.pss = (int) this.mTotalUsedMemory;
                    bootStepResourceInfo.javaHeap = (int) this.mDalvikPss;
                    bootStepResourceInfo.nativeHeap = (int) this.mNativeHeapPss;
                    bootStepResourceInfo.blockGcCount = this.mBlockingGCCount;
                    bootStepResourceInfo.blockGcTime = (int) this.mTotalBlockingGCTime;
                } else {
                    return;
                }
            }
        } else {
            bootStepResourceInfo = null;
        }
        bootStepResourceInfo.pidIoWaitTime = this.mPidIoWaitSum;
        bootStepResourceInfo.pidIoWaitCout = this.mPidIoWaitCount;
        bootStepResourceInfo.pidSchedWaitTime = this.mPidWaitSum;
        bootStepResourceInfo.pidSchedWaitCout = this.mPidWaitCount;
        bootStepResourceInfo.pidSchedWaitMax = this.mPidWaitMax;
        bootStepResourceInfo.pidPerCpuLoad = this.mPidPerCpuLoad;
        bootStepResourceInfo.classCount = Debug.getLoadedClassCount();
        bootStepResourceInfo.majorFault = this.mProcessCpuTracker.mMajorFault;
        bootStepResourceInfo.loadAvg = this.mProcessCpuTracker.mLoadAverageData[0];
        bootStepResourceInfo.pidJiffyTime = this.mProcessCpuTracker.mPidJiffyTime;
        bootStepResourceInfo.totalJiffyTime = this.mProcessCpuTracker.mSystemTotalCpuTime;
        bootStepResourceInfo.systemJiffyTime = this.mProcessCpuTracker.mSystemRunCpuTime;
        bootStepResourceInfo.threadCount = this.mRuntimeThreadCount;
    }

    static ResourceUsedInfo getResourceUsedInfo(ResourceUsedInfo resourceUsedInfo, boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5;
        ResourceUsedInfo resourceUsedInfo2;
        if (resourceUsedInfo == null) {
            z5 = true;
            resourceUsedInfo2 = new ResourceUsedInfo();
            resourceUsedInfo2.activityName = sOnLineMonitor.mActivityName == null ? "" : sOnLineMonitor.mActivityName;
            resourceUsedInfo2.threadName = Thread.currentThread().getName();
            resourceUsedInfo2.taskThreadId = Thread.currentThread().getId();
            resourceUsedInfo2.taskThreadTid = Process.myTid();
        } else {
            resourceUsedInfo2 = resourceUsedInfo;
            z5 = false;
        }
        if (z5) {
            sOnLineMonitor.getCpuInfo(sIsTraceDetail, false, false);
            float[] fArr = new float[7];
            if (resourceUsedInfo2.taskThreadTid > 0) {
                resourceUsedInfo2.threadJiffyTime = sOnLineMonitor.mProcessCpuTracker.loadTaskTime(resourceUsedInfo2.taskThreadTid);
                if (sIsTraceDetail) {
                    sOnLineMonitor.getThreadIoWaitAndLoadAvg(resourceUsedInfo2.taskThreadTid, fArr);
                }
            }
            resourceUsedInfo2.pidIoWaitTime = sOnLineMonitor.mPidIoWaitSum;
            resourceUsedInfo2.pidIoWaitCout = sOnLineMonitor.mPidIoWaitCount;
            resourceUsedInfo2.pidSchedWaitTime = (long) sOnLineMonitor.mPidWaitSum;
            resourceUsedInfo2.pidSchedWaitCout = sOnLineMonitor.mPidWaitCount;
            resourceUsedInfo2.pidJiffyTime = sOnLineMonitor.mProcessCpuTracker.mPidJiffyTime;
            resourceUsedInfo2.totalJiffyTime = sOnLineMonitor.mProcessCpuTracker.mSystemTotalCpuTime;
            resourceUsedInfo2.systemJiffyTime = sOnLineMonitor.mProcessCpuTracker.mSystemRunCpuTime;
            if (sOnLineMonitor.mPidIoWaitSum > 0) {
                resourceUsedInfo2.ioWaitTime = (long) fArr[4];
                resourceUsedInfo2.ioWaitCout = (long) fArr[5];
                resourceUsedInfo2.schedWaitTime = (long) fArr[0];
                resourceUsedInfo2.schedWaitCout = (long) fArr[3];
            }
            resourceUsedInfo2.threadStart = sOnLineMonitor.mRuntimeThreadCount;
            resourceUsedInfo2.threadEnd = sOnLineMonitor.mRuntimeThreadCount;
            resourceUsedInfo2.threadMax = sOnLineMonitor.mRuntimeThreadCount;
            resourceUsedInfo2.threadMin = sOnLineMonitor.mRuntimeThreadCount;
            if (sIsTraceDetail) {
                try {
                    resourceUsedInfo2.newThreadCount = sOnLineMonitor.mTraceDetail.mFieldThreadCount.getInt(Thread.class);
                    resourceUsedInfo2.loadClassCount = Debug.getLoadedClassCount();
                } catch (Throwable unused) {
                }
            }
            if (z) {
                sOnLineMonitor.getMemInfo(z);
            }
            resourceUsedInfo2.memStart = (int) sOnLineMonitor.mTotalUsedMemory;
            resourceUsedInfo2.memEnd = resourceUsedInfo2.memStart;
            resourceUsedInfo2.memMax = resourceUsedInfo2.memStart;
            resourceUsedInfo2.memMin = resourceUsedInfo2.memStart;
            resourceUsedInfo2.memJavaStart = (int) sOnLineMonitor.mDalvikPss;
            resourceUsedInfo2.memJavaEnd = resourceUsedInfo2.memJavaStart;
            resourceUsedInfo2.memJavaMax = resourceUsedInfo2.memJavaStart;
            resourceUsedInfo2.memJavaMin = resourceUsedInfo2.memJavaStart;
            resourceUsedInfo2.memNativeStart = (int) sOnLineMonitor.mNativeHeapPss;
            resourceUsedInfo2.memNativeEnd = resourceUsedInfo2.memNativeStart;
            resourceUsedInfo2.memNativeMax = resourceUsedInfo2.memNativeStart;
            resourceUsedInfo2.memNativeMin = resourceUsedInfo2.memNativeStart;
            if (z3) {
                resourceUsedInfo2.baseTheadMap = Thread.getAllStackTraces();
            }
            resourceUsedInfo2.debugUsedTime = System.nanoTime() / 1000000;
            resourceUsedInfo2.debugUsedCpuTime = Debug.threadCpuTimeNanos() / 1000000;
        } else {
            resourceUsedInfo2.debugUsedTime = (System.nanoTime() / 1000000) - resourceUsedInfo2.debugUsedTime;
            if (resourceUsedInfo2.taskThreadTid == Process.myTid()) {
                resourceUsedInfo2.debugUsedCpuTime = (Debug.threadCpuTimeNanos() / 1000000) - resourceUsedInfo2.debugUsedCpuTime;
            }
            sOnLineMonitor.getCpuInfo(sIsTraceDetail, false, false);
            float[] fArr2 = new float[7];
            if (resourceUsedInfo2.taskThreadTid > 0) {
                resourceUsedInfo2.threadJiffyTime = sOnLineMonitor.mProcessCpuTracker.loadTaskTime(resourceUsedInfo2.taskThreadTid) - resourceUsedInfo2.threadJiffyTime;
                if (resourceUsedInfo2.taskThreadTid != Process.myTid()) {
                    resourceUsedInfo2.debugUsedCpuTime = resourceUsedInfo2.threadJiffyTime * 10;
                }
                if (sIsTraceDetail) {
                    sOnLineMonitor.getThreadIoWaitAndLoadAvg(resourceUsedInfo2.taskThreadTid, fArr2);
                }
            }
            resourceUsedInfo2.pidJiffyTime = sOnLineMonitor.mProcessCpuTracker.mPidJiffyTime - resourceUsedInfo2.pidJiffyTime;
            resourceUsedInfo2.totalJiffyTime = sOnLineMonitor.mProcessCpuTracker.mSystemTotalCpuTime - resourceUsedInfo2.totalJiffyTime;
            resourceUsedInfo2.systemJiffyTime = sOnLineMonitor.mProcessCpuTracker.mSystemRunCpuTime - resourceUsedInfo2.systemJiffyTime;
            if (resourceUsedInfo2.systemJiffyTime > resourceUsedInfo2.totalJiffyTime) {
                resourceUsedInfo2.systemJiffyTime = resourceUsedInfo2.totalJiffyTime;
            }
            if (resourceUsedInfo2.pidJiffyTime > resourceUsedInfo2.totalJiffyTime) {
                resourceUsedInfo2.pidJiffyTime = resourceUsedInfo2.totalJiffyTime;
            }
            if (resourceUsedInfo2.systemJiffyTime < resourceUsedInfo2.pidJiffyTime) {
                resourceUsedInfo2.systemJiffyTime = resourceUsedInfo2.pidJiffyTime;
            }
            if (sOnLineMonitor.mPidIoWaitSum > 0) {
                resourceUsedInfo2.ioWaitTime = ((long) fArr2[4]) - resourceUsedInfo2.ioWaitTime;
                resourceUsedInfo2.ioWaitCout = ((long) fArr2[5]) - resourceUsedInfo2.ioWaitCout;
                resourceUsedInfo2.schedWaitTime = ((long) fArr2[0]) - resourceUsedInfo2.schedWaitTime;
                resourceUsedInfo2.schedWaitCout = ((long) fArr2[3]) - resourceUsedInfo2.schedWaitCout;
            } else {
                resourceUsedInfo2.ioWaitTime = (sOnLineMonitor.mProcessCpuTracker.mIoWaitTime * 10) - resourceUsedInfo2.ioWaitTime;
            }
            resourceUsedInfo2.threadEnd = sOnLineMonitor.mRuntimeThreadCount;
            resourceUsedInfo2.threadMax = Math.max(resourceUsedInfo2.threadStart, sOnLineMonitor.mRuntimeThreadCount);
            resourceUsedInfo2.threadMin = Math.min(sOnLineMonitor.mRuntimeThreadCount, resourceUsedInfo2.threadMin);
            try {
                resourceUsedInfo2.newThreadCount = sOnLineMonitor.mTraceDetail.mFieldThreadCount.getInt(Thread.class) - resourceUsedInfo2.newThreadCount;
                resourceUsedInfo2.loadClassCount = Debug.getLoadedClassCount() - resourceUsedInfo2.loadClassCount;
            } catch (Throwable unused2) {
            }
            if (z) {
                sOnLineMonitor.getMemInfo(z);
            }
            resourceUsedInfo2.memStart = (int) sOnLineMonitor.mTotalUsedMemory;
            resourceUsedInfo2.memEnd = (int) sOnLineMonitor.mTotalUsedMemory;
            resourceUsedInfo2.memMax = Math.max((int) sOnLineMonitor.mTotalUsedMemory, resourceUsedInfo2.memMax);
            resourceUsedInfo2.memMin = Math.min((int) sOnLineMonitor.mTotalUsedMemory, resourceUsedInfo2.memMin);
            resourceUsedInfo2.memJavaEnd = (int) sOnLineMonitor.mDalvikPss;
            resourceUsedInfo2.memJavaMax = Math.max(resourceUsedInfo2.memJavaMax, (int) sOnLineMonitor.mDalvikPss);
            resourceUsedInfo2.memJavaMin = Math.min(resourceUsedInfo2.memJavaMin, (int) sOnLineMonitor.mDalvikPss);
            resourceUsedInfo2.memNativeEnd = (int) sOnLineMonitor.mNativeHeapPss;
            resourceUsedInfo2.memNativeMax = Math.max(resourceUsedInfo2.memNativeMax, (int) sOnLineMonitor.mNativeHeapPss);
            resourceUsedInfo2.memNativeMin = Math.min(resourceUsedInfo2.memNativeMin, (int) sOnLineMonitor.mNativeHeapPss);
            if (z3 && resourceUsedInfo2.baseTheadMap != null) {
                Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
                if (allStackTraces != null) {
                    for (Map.Entry next : allStackTraces.entrySet()) {
                        if (next != null) {
                            Thread thread = (Thread) next.getKey();
                            if (!resourceUsedInfo2.baseTheadMap.containsKey(thread)) {
                                if (resourceUsedInfo2.newTheadMap == null) {
                                    resourceUsedInfo2.newTheadMap = new HashMap();
                                }
                                StackTraceElement[] stackTraceElementArr = allStackTraces.get(thread);
                                if (stackTraceElementArr != null) {
                                    resourceUsedInfo2.newTheadMap.put(thread.getName(), stackTraceElementArr.toString());
                                }
                            }
                        }
                    }
                }
                if (z4) {
                    resourceUsedInfo2.baseTheadMap = null;
                } else {
                    resourceUsedInfo2.baseTheadMap = allStackTraces;
                }
            }
        }
        return resourceUsedInfo2;
    }

    public static void onInstallBundler(String str, int i) {
        BundleInfo bundleInfo;
        if (sOnLineMonitor != null && sOnLineMonitor.mTraceDetail != null && sIsTraceDetail) {
            if (sOnLineMonitor.mTraceDetail.mInstallBundleInfoMap == null) {
                sOnLineMonitor.mTraceDetail.mInstallBundleInfoMap = new LinkedHashMap();
                sOnLineMonitor.mTraceDetail.mStartBundleInfoMap = new LinkedHashMap();
            }
            if (i == 0 || i == 2) {
                int myTid = Process.myTid();
                BundleInfo bundleInfo2 = new BundleInfo();
                bundleInfo2.activityName = sOnLineMonitor.mActivityName;
                bundleInfo2.threadId = Thread.currentThread().getId();
                bundleInfo2.threadName = Thread.currentThread().getName();
                if (bundleInfo2.threadName != null && bundleInfo2.threadName.contains(DinamicConstant.DINAMIC_PREFIX_AT)) {
                    bundleInfo2.threadName = bundleInfo2.threadName.substring(0, bundleInfo2.threadName.indexOf(64));
                }
                bundleInfo2.bundleName = str;
                bundleInfo2.isInBoot = sOnLineMonitor.mIsInBootStep;
                bundleInfo2.tid = myTid;
                bundleInfo2.resourceUsedInfo = getResourceUsedInfo((ResourceUsedInfo) null, false, false, false, false);
                if (i == 0) {
                    sOnLineMonitor.mTraceDetail.mInstallBundleInfoMap.put(str, bundleInfo2);
                } else {
                    sOnLineMonitor.mTraceDetail.mStartBundleInfoMap.put(str, bundleInfo2);
                }
            } else if (i == 1 || i == 3) {
                if (i == 1) {
                    bundleInfo = sOnLineMonitor.mTraceDetail.mInstallBundleInfoMap.get(str);
                } else {
                    bundleInfo = sOnLineMonitor.mTraceDetail.mStartBundleInfoMap.get(str);
                }
                if (bundleInfo != null) {
                    getResourceUsedInfo(bundleInfo.resourceUsedInfo, false, false, false, true);
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean startPerformanceTrace() {
        if (sOnLineMonitor == null || Thread.currentThread().getId() == 1) {
            return false;
        }
        try {
            ActivityRuntimeInfo activityRuntimeInfo = new ActivityRuntimeInfo();
            sOnLineMonitor.mActivityTraceRuntimeInfo = activityRuntimeInfo;
            activityRuntimeInfo.stayTime = System.nanoTime() / 1000000;
            activityRuntimeInfo.activityName = sOnLineMonitor.mActivityName;
            sOnLineMonitor.mMaxRunningThreadCount = 0;
            sOnLineMonitor.mMaxThreadCount = 0;
            sOnLineMonitor.mSysGetCounter = 0;
            sOnLineMonitor.getCpuInfo(true, true, true);
            sOnLineMonitor.getMemInfo(true);
            sOnLineMonitor.getTrafficStats();
            activityRuntimeInfo.threadInterval = sOnLineMonitor.mRuntimeThreadCount;
            activityRuntimeInfo.memStart = (short) ((int) sOnLineMonitor.mTotalUsedMemory);
            activityRuntimeInfo.javaStart = (short) ((int) sOnLineMonitor.mDalvikPss);
            activityRuntimeInfo.nativeStart = (short) ((int) sOnLineMonitor.mNativeHeapPss);
            if (sApiLevel >= 23) {
                String runtimeStat = Debug.getRuntimeStat("art.gc.gc-count");
                if (runtimeStat != null && runtimeStat.length() > 0) {
                    activityRuntimeInfo.gcCount = Integer.parseInt(runtimeStat);
                }
                String runtimeStat2 = Debug.getRuntimeStat("art.gc.blocking-gc-count");
                if (runtimeStat2 != null && runtimeStat2.length() > 0) {
                    activityRuntimeInfo.blockGc = Integer.parseInt(runtimeStat2);
                }
                String runtimeStat3 = Debug.getRuntimeStat("art.gc.blocking-gc-time");
                if (runtimeStat3 != null && runtimeStat3.length() > 0) {
                    activityRuntimeInfo.blockTime = Long.parseLong(runtimeStat3);
                }
            }
            activityRuntimeInfo.openFile = (short) sOnLineMonitor.mOpenFileCount;
            activityRuntimeInfo.classCount = Debug.getLoadedClassCount();
            activityRuntimeInfo.totalTx = (short) ((int) sOnLineMonitor.mOnLineStat.trafficStatsInfo.totalTotalTxBytes);
            activityRuntimeInfo.totalRx = (short) ((int) sOnLineMonitor.mOnLineStat.trafficStatsInfo.totalTotalRxBytes);
            activityRuntimeInfo.battery = (short) sOnLineMonitor.mBatteryPercent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @SuppressLint({"NewApi"})
    public static ActivityRuntimeInfo stopPerformanceTrace() {
        ActivityRuntimeInfo activityRuntimeInfo;
        if (sOnLineMonitor == null || Thread.currentThread().getId() == 1 || (activityRuntimeInfo = sOnLineMonitor.mActivityTraceRuntimeInfo) == null) {
            return null;
        }
        try {
            activityRuntimeInfo.stayTime = (System.nanoTime() / 1000000) - activityRuntimeInfo.stayTime;
            activityRuntimeInfo.activityName += " - " + sOnLineMonitor.mActivityName;
            sOnLineMonitor.getCpuInfo(true, true, true);
            sOnLineMonitor.getMemInfo(true);
            sOnLineMonitor.getTrafficStats();
            activityRuntimeInfo.threadInterval = sOnLineMonitor.mRuntimeThreadCount - activityRuntimeInfo.threadInterval;
            activityRuntimeInfo.maxRunningThread = sOnLineMonitor.mMaxRunningThreadCount;
            activityRuntimeInfo.maxThread = sOnLineMonitor.mMaxThreadCount;
            activityRuntimeInfo.memEnd = (short) ((int) sOnLineMonitor.mTotalUsedMemory);
            activityRuntimeInfo.javaEnd = (short) ((int) sOnLineMonitor.mDalvikPss);
            activityRuntimeInfo.javaAllocal = (short) ((int) (sOnLineMonitor.mDalvikHeapSize - sOnLineMonitor.mDalvikFree));
            activityRuntimeInfo.nativeEnd = (short) ((int) sOnLineMonitor.mNativeHeapPss);
            activityRuntimeInfo.nativeAllocal = (short) ((int) sOnLineMonitor.mNativeHeapAllocatedSize);
            if (sApiLevel >= 23) {
                String runtimeStat = Debug.getRuntimeStat("art.gc.gc-count");
                if (runtimeStat != null && runtimeStat.length() > 0) {
                    activityRuntimeInfo.gcCount = Integer.parseInt(runtimeStat) - activityRuntimeInfo.gcCount;
                }
                String runtimeStat2 = Debug.getRuntimeStat("art.gc.blocking-gc-count");
                if (runtimeStat2 != null && runtimeStat2.length() > 0) {
                    activityRuntimeInfo.blockGc = Integer.parseInt(runtimeStat2) - activityRuntimeInfo.blockGc;
                }
                String runtimeStat3 = Debug.getRuntimeStat("art.gc.blocking-gc-time");
                if (runtimeStat3 != null && runtimeStat3.length() > 0) {
                    activityRuntimeInfo.blockTime = Long.parseLong(runtimeStat3) - activityRuntimeInfo.blockTime;
                }
            }
            activityRuntimeInfo.pidScore = sOnLineMonitor.mMyPidScore;
            activityRuntimeInfo.sysScore = sOnLineMonitor.mSystemRunningScore;
            activityRuntimeInfo.pidAvgCpu = (short) sOnLineMonitor.mMyAvgPidCPUPercent;
            activityRuntimeInfo.sysAvgCpu = (short) sOnLineMonitor.mSysAvgCPUPercent;
            activityRuntimeInfo.openFile = (short) (sOnLineMonitor.mOpenFileCount - activityRuntimeInfo.openFile);
            activityRuntimeInfo.classCount = Debug.getLoadedClassCount() - activityRuntimeInfo.classCount;
            activityRuntimeInfo.pidIoWaitCount = (sOnLineMonitor.mPidIoWaitCount > 0 ? sOnLineMonitor.mPidIoWaitCount : sOnLineMonitor.mIoWiatCount) - activityRuntimeInfo.pidIoWaitCount;
            activityRuntimeInfo.pidIoWaitSumAvg = sOnLineMonitor.mPidIoWaitSumAvg * 10;
            activityRuntimeInfo.pidPerCpuLoadAvg = sOnLineMonitor.mPidPerCpuLoadAvg / ((float) sOnLineMonitor.mCpuProcessCount);
            activityRuntimeInfo.loadAvg1Min = sOnLineMonitor.mSystemLoadAvg[0];
            activityRuntimeInfo.totalTx = (short) ((int) (sOnLineMonitor.mOnLineStat.trafficStatsInfo.totalTotalTxBytes - ((float) activityRuntimeInfo.totalTx)));
            activityRuntimeInfo.totalRx = (short) ((int) (sOnLineMonitor.mOnLineStat.trafficStatsInfo.totalTotalRxBytes - ((float) activityRuntimeInfo.totalRx)));
            activityRuntimeInfo.battery = (short) sOnLineMonitor.mBatteryPercent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityRuntimeInfo;
    }

    public static String getSimpleName(String str) {
        if (str == null) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf < 0) {
            lastIndexOf = str.lastIndexOf(36);
        }
        if (lastIndexOf < 0) {
            return str;
        }
        return str.substring(lastIndexOf + 1);
    }

    public static String getVersionName(Context context) {
        PackageInfo packageInfo;
        if (context == null || (packageInfo = getPackageInfo(context)) == null) {
            return "";
        }
        return packageInfo.versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyBackForGroundListener(int i) {
        long j;
        int i2 = i;
        if (this.mOnBackForGroundListener != null) {
            synchronized (this.mOnBackForGroundListener) {
                for (int i3 = 0; i3 < this.mOnBackForGroundListener.size(); i3++) {
                    try {
                        OnBackForGroundListener onBackForGroundListener = this.mOnBackForGroundListener.get(i3);
                        if (onBackForGroundListener != null) {
                            long j2 = 0;
                            if (TraceDetail.sTraceOnLineListener) {
                                long nanoTime = System.nanoTime() / 1000000;
                                j2 = Debug.threadCpuTimeNanos();
                                j = nanoTime;
                            } else {
                                j = 0;
                            }
                            if (i2 == 10) {
                                onBackForGroundListener.onJustToggleBackGround();
                            } else if (i2 == 20) {
                                onBackForGroundListener.onJustToggleForGround();
                            } else if (i2 == 1) {
                                onBackForGroundListener.onToggleBackGround();
                            } else if (i2 == 2) {
                                onBackForGroundListener.onToggleForGround();
                            }
                            if (TraceDetail.sTraceOnLineListener) {
                                long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
                                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                                methodInfo.activityName = this.mActivityName;
                                methodInfo.cpuTime = (threadCpuTimeNanos - j2) / 1000000;
                                methodInfo.realTime = (System.nanoTime() / 1000000) - j;
                                if (i2 == 10) {
                                    methodInfo.methodName = "onJustToggleBackGround()";
                                } else if (i2 == 20) {
                                    methodInfo.methodName = "onJustToggleForGround()";
                                } else if (i2 == 1) {
                                    methodInfo.methodName = "onToggleBackGround()";
                                } else if (i2 == 2) {
                                    methodInfo.methodName = "onToggleForGround()";
                                }
                                if (this.mTraceDetail != null) {
                                    this.mTraceDetail.mOnBackForGroundList.add(methodInfo);
                                }
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ViewTreeObserver.OnGlobalLayoutListener createOnGlobalLayoutListener(int i) {
        return new OnLineMonitorGlobalLayoutListener(i);
    }

    /* access modifiers changed from: package-private */
    public void showMessage(String str) {
        if (sIsTraceDetail) {
            Log.e(TAG, str);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkIfSimpling() {
        String valueOf;
        if (!sIsTraceDetail && OnLineMonitorApp.sPublishRelease && (valueOf = String.valueOf(System.currentTimeMillis())) != null) {
            char charAt = valueOf.charAt(valueOf.length() - 1);
            if (charAt == '1' || charAt == '3' || charAt == '7') {
                this.mIsDeviceSampling = true;
            } else {
                this.mIsDeviceSampling = false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void onHandlerThreadPrepared() {
        calculateSystemCheckValue();
        this.mThreadHandler.sendEmptyMessageDelayed(2, 0);
        if (this.mStatusBarHeight == 0) {
            try {
                Class<?> cls = Class.forName("com.android.internal.R$dimen");
                this.mStatusBarHeight = this.mContext.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
            } catch (Exception unused) {
                this.mStatusBarHeight = 75;
            }
        }
        if (this.mGetTotalUss == null) {
            try {
                this.mGetTotalUss = Debug.MemoryInfo.class.getDeclaredMethod("getTotalUss", new Class[0]);
                this.mGetTotalUss.setAccessible(true);
            } catch (Throwable unused2) {
            }
        }
        try {
            this.mClassFragmentActivity = Class.forName("androidx.fragment.app.FragmentActivity");
            this.mGetSupportFragmentManager = this.mClassFragmentActivity.getDeclaredMethod("getSupportFragmentManager", new Class[0]);
            this.mGetSupportFragmentManager.setAccessible(true);
        } catch (Throwable unused3) {
        }
        if (sIsDetailDebug) {
            Log.e(TAG, "StatusBarHeight=" + this.mStatusBarHeight);
        }
        if (this.mOnLineStat != null) {
            String str = null;
            String versionName = getVersionName(this.mContext);
            File filesDir = this.mContext.getFilesDir();
            if (filesDir != null) {
                str = filesDir.getAbsolutePath() + "/onlinemonitorversion";
            }
            if (str != null) {
                File file = new File(str);
                if (file.exists()) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                        String readLine = bufferedReader.readLine();
                        bufferedReader.close();
                        if (versionName != null) {
                            if (versionName.equals(readLine)) {
                                this.mOnLineStat.isFirstInstall = false;
                            } else {
                                this.mOnLineStat.isFirstInstall = true;
                                file.delete();
                                saveVersionInfo(file, versionName);
                            }
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    this.mOnLineStat.isFirstInstall = true;
                    saveVersionInfo(file, versionName);
                }
            }
        }
        if (sIsTraceDetail) {
            new DynamicCloseGuard(this).doProxy();
        }
        if (sIsTraceDetail || (!OnLineMonitorApp.sIsDebug && this.mIsDeviceSampling && this.mDevicesScore >= 75 && sApiLevel >= 19)) {
            new DynamicBlockGuard(this).doProxy();
        }
        this.mIsRooted = isRooted();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0023 A[SYNTHETIC, Splitter:B:16:0x0023] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0031 A[SYNTHETIC, Splitter:B:21:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveVersionInfo(java.io.File r4, java.lang.String r5) {
        /*
            r3 = this;
            r0 = 0
            java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x001d }
            java.io.FileWriter r2 = new java.io.FileWriter     // Catch:{ Exception -> 0x001d }
            r2.<init>(r4)     // Catch:{ Exception -> 0x001d }
            r1.<init>(r2)     // Catch:{ Exception -> 0x001d }
            r1.write(r5)     // Catch:{ Exception -> 0x0018, all -> 0x0015 }
            r1.flush()     // Catch:{ IOException -> 0x002a }
            r1.close()     // Catch:{ IOException -> 0x002a }
            goto L_0x002e
        L_0x0015:
            r4 = move-exception
            r0 = r1
            goto L_0x002f
        L_0x0018:
            r4 = move-exception
            r0 = r1
            goto L_0x001e
        L_0x001b:
            r4 = move-exception
            goto L_0x002f
        L_0x001d:
            r4 = move-exception
        L_0x001e:
            r4.printStackTrace()     // Catch:{ all -> 0x001b }
            if (r0 == 0) goto L_0x002e
            r0.flush()     // Catch:{ IOException -> 0x002a }
            r0.close()     // Catch:{ IOException -> 0x002a }
            goto L_0x002e
        L_0x002a:
            r4 = move-exception
            r4.printStackTrace()
        L_0x002e:
            return
        L_0x002f:
            if (r0 == 0) goto L_0x003c
            r0.flush()     // Catch:{ IOException -> 0x0038 }
            r0.close()     // Catch:{ IOException -> 0x0038 }
            goto L_0x003c
        L_0x0038:
            r5 = move-exception
            r5.printStackTrace()
        L_0x003c:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.saveVersionInfo(java.io.File, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    public int getAllowCheckCountPerActivity() {
        if (this.mDevicesScore >= 90) {
            return 10;
        }
        if (this.mDevicesScore >= 85) {
            return 8;
        }
        if (this.mDevicesScore >= 75) {
            return 6;
        }
        if (this.mDevicesScore >= 60) {
            return 4;
        }
        return this.mDevicesScore >= 50 ? 3 : 2;
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void calculateSystemCheckValue() {
        if (this.mDevicesScore >= 85) {
            this.mIdleCheckIntervalControl = 80;
            this.mCpuCheckIntervalControl = 1000;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 80;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = 250;
        } else if (this.mDevicesScore >= 85) {
            this.mIdleCheckIntervalControl = 100;
            this.mCpuCheckIntervalControl = 2000;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 100;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = 400;
        } else if (this.mDevicesScore >= 60) {
            this.mIdleCheckIntervalControl = 200;
            this.mCpuCheckIntervalControl = 2500;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 120;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = 500;
            this.mColdBootOffsetTime = 1200;
        } else if (this.mDevicesScore >= 60) {
            this.mIdleCheckIntervalControl = 250;
            this.mCpuCheckIntervalControl = 3000;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 150;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = 600;
        } else if (this.mDevicesScore >= 50) {
            this.mIdleCheckIntervalControl = 250;
            this.mCpuCheckIntervalControl = a.a;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 180;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = 700;
            this.mColdBootOffsetTime = 1500;
        } else if (this.mDevicesScore >= 50) {
            this.mIdleCheckIntervalControl = 300;
            this.mCpuCheckIntervalControl = 4000;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 220;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = 800;
            this.mColdBootOffsetTime = 1500;
        } else if (this.mFirstSystemRunningScore <= 50) {
            this.mIdleCheckIntervalControl = 300;
            this.mCpuCheckIntervalControl = 4500;
            this.mLoadTimeCalculate.mLayoutCheckFreqControl = 250;
            this.mLoadTimeCalculate.mLayoutMinTimeControl = SecExceptionCode.SEC_ERROR_UMID_VALID;
            this.mColdBootOffsetTime = 2000;
        }
    }

    /* access modifiers changed from: package-private */
    public int getTotalMemFromFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"));
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            int parseInt = (readLine != null ? Integer.parseInt(readLine.replace("MemTotal:", "").replace("kB", "").replace(Operators.SPACE_STR, "")) : 0) / 1024;
            if (sIsDetailDebug) {
                Log.e(TAG, "getTotalMemFromFile=" + parseInt);
            }
            return parseInt;
        } catch (Exception unused) {
            return 1024;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:3|4|5|6|7|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0017 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerComponentCallbacks() {
        /*
            r4 = this;
            android.content.Context r0 = r4.mApplicationContext
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.content.Context r0 = r4.mApplicationContext
            r4.mContext = r0
            android.content.Context r0 = r4.mContext     // Catch:{ Exception -> 0x0017 }
            android.content.BroadcastReceiver r1 = r4.mBatInfoReceiver     // Catch:{ Exception -> 0x0017 }
            android.content.IntentFilter r2 = new android.content.IntentFilter     // Catch:{ Exception -> 0x0017 }
            java.lang.String r3 = "android.intent.action.BATTERY_CHANGED"
            r2.<init>(r3)     // Catch:{ Exception -> 0x0017 }
            r0.registerReceiver(r1, r2)     // Catch:{ Exception -> 0x0017 }
        L_0x0017:
            android.content.Context r0 = r4.mContext     // Catch:{ Exception -> 0x001e }
            com.taobao.onlinemonitor.OnLineMonitor$MyCallback r1 = r4.mMyCallback     // Catch:{ Exception -> 0x001e }
            r0.registerComponentCallbacks(r1)     // Catch:{ Exception -> 0x001e }
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.registerComponentCallbacks():void");
    }

    /* access modifiers changed from: package-private */
    public void startPerformanceMonitor() {
        if (this.mThreadHandler != null) {
            this.mThreadHandler.removeMessages(11);
            this.mCheckAnrTime = System.nanoTime() / 1000000;
            this.mThreadHandler.sendEmptyMessageDelayed(5, 5000);
            if (sIsTraceDetail) {
                this.mThreadHandler.removeMessages(12);
                this.mThreadHandler.sendEmptyMessage(12);
            }
        } else if (this.mHandler != null) {
            this.mHandler.sendEmptyMessage(14);
        }
    }

    /* access modifiers changed from: package-private */
    public void back2ForeChanged() {
        if (this.mIsFullInBackGround) {
            this.mIsFullInBackGround = false;
            this.mOldTrimMemoryLevel = this.mTrimMemoryLevel;
            this.mApplicationContext = this.mContext.getApplicationContext();
            if (this.mApplicationContext != null) {
                registerComponentCallbacks();
            }
            startPerformanceMonitor();
            this.mOnLineStat.isFullInBackGround = false;
        }
        if (this.mIsInBackGround) {
            notifyBackForGroundListener(2);
            this.mIsInBackGround = false;
            if (this.mThreadInfoHashMap != null) {
                this.mThreadInfoHashMap.clear();
            }
            if (this.mTrimMemoryLevel == 20) {
                this.mTrimMemoryLevel = 0;
            }
            if (this.mThreadHandler != null) {
                notifyOnlineRuntimeStat(51);
            }
            if (!this.mIsFullInBackGround && this.mThreadHandler != null) {
                this.mThreadHandler.removeMessages(11);
            }
        }
        this.mUIHiddenTime = 0;
    }

    /* access modifiers changed from: package-private */
    public void getTrafficStats() {
        try {
            this.mMobileRxBytes = TrafficStats.getMobileRxBytes();
            this.mMobileTxBytes = TrafficStats.getMobileTxBytes();
            this.mTotalRxBytes = TrafficStats.getTotalRxBytes();
            this.mTotalTxBytes = TrafficStats.getTotalTxBytes();
        } catch (Throwable unused) {
        }
        if (this.mFirstMobileRxBytes < 0) {
            this.mFirstMobileRxBytes = this.mMobileRxBytes;
            this.mFirstMobileTxBytes = this.mMobileTxBytes;
            this.mFirstTotalRxBytes = this.mTotalRxBytes;
            this.mFirstTotalTxBytes = this.mTotalTxBytes;
        }
        if (sIsTraceDetail && this.mTraceDetail != null && this.mTraceDetail.mFirstMobileRxBytes < 0) {
            this.mTraceDetail.mFirstMobileRxBytes = this.mFirstMobileRxBytes;
            this.mTraceDetail.mFirstMobileTxBytes = this.mFirstMobileTxBytes;
            this.mTraceDetail.mFirstTotalRxBytes = this.mFirstTotalRxBytes;
            this.mTraceDetail.mFirstTotalTxBytes = this.mFirstTotalTxBytes;
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityCreate(Activity activity) {
        this.mActivityIdleTime = 0;
        this.mActivityIdleFistTime = 0;
        this.mIdleNotifyCount = 0;
        this.mMaxBlockIdletime = 0;
        this.mActivityName = this.mActivityLifecycleCallback.mActivityName;
        this.mOnLineStat.activityName = this.mActivityName;
        this.mUIHiddenTime = 0;
        this.mIsIdleGeted = false;
        if (this.mMessageQueue == null) {
            this.mMessageQueue = Looper.myQueue();
        }
        this.mMessageQueue.addIdleHandler(this.mIdleHandler);
        if (this.mHandler == null) {
            this.mHandler = new MyHandler();
            this.mCheckAnrTime = this.mActivityLifecycleCallback.mActivityOncreateTime;
        }
        this.mIsActivityColdOpen = true;
        this.mOldAnrCount = this.mAnrCount;
        if (this.mThreadHandler != null) {
            this.mThreadHandler.sendEmptyMessageDelayed(2, 100);
            if (this.mAppProgressImportance != 100) {
                this.mThreadHandler.removeMessages(8);
                this.mThreadHandler.sendEmptyMessage(8);
            }
        }
        if (this.mIsInitedActivity) {
            getTrafficStats();
        }
        back2ForeChanged();
        this.mIsFirstOpenActivity = true ^ this.mActivitysMap.containsKey(this.mActivityName);
        onActivityLoadStart(this.mActivityLifecycleCallback.mActivityOncreateTime);
        if (sIsTraceDetail) {
            this.mTraceDetail.onActivityCreate(activity);
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityLoadStart(long j) {
        if (sIsTraceDetail) {
            if (this.mThreadHandler != null) {
                this.mThreadHandler.sendEmptyMessage(2);
            }
            this.mActivityRuntimeInfo = new ActivityRuntimeInfo();
            TraceDetail.sTracedActivityCount = (short) (TraceDetail.sTracedActivityCount + 1);
        } else if (!sIsTraceDetail && this.mActivityRuntimeInfo == null) {
            this.mActivityRuntimeInfo = new ActivityRuntimeInfo();
        }
        if (sIsTraceDetail && this.mActivityRuntimeInfo != null && this.mActivityRuntimeInfo.lifeCycleArrayUsedTime == null) {
            this.mActivityRuntimeInfo.lifeCycleArrayUsedTime = new long[6];
        }
        this.mOnLineStat.activityRuntimeInfo = this.mActivityRuntimeInfo;
        this.mOnLineStat.isActivityLoading = true;
        this.mIoWiatCount = 0;
        this.mActivityRuntimeInfo.activityCreateTime = this.mActivityLifecycleCallback.mActivityOncreateTime;
        this.mActivityRuntimeInfo.isFistTimeOpen = this.mIsFirstOpenActivity;
        this.mActivityRuntimeInfo.activityName = this.mActivityName;
        this.mActivityRuntimeInfo.stayTime = j;
        this.mActivityRuntimeInfo.memStart = (short) ((int) this.mTotalUsedMemory);
        this.mActivityRuntimeInfo.javaStart = (short) ((int) this.mDalvikPss);
        this.mActivityRuntimeInfo.nativeStart = (short) ((int) this.mNativeHeapPss);
        if (sIsTraceDetail) {
            this.mActivityRuntimeInfo.classCount = Debug.getLoadedClassCount();
        }
        this.mActivityRuntimeInfo.anrTime = (short) this.mAnrCount;
        this.mActivityRuntimeInfo.isColdOpen = this.mIsActivityColdOpen;
        this.mActivityRuntimeInfo.threadInterval = this.mRuntimeThreadCount;
        this.mOnLineStat.isSystemIdle = false;
        this.mActivityRuntimeInfo.totalLayoutUseTime = 0;
        this.mActivityRuntimeInfo.layoutTimesOnLoad = 0;
        this.mActivityRuntimeInfo.maxLayoutUseTime = 0;
        this.mActivityRuntimeInfo.measureTimes = 0;
        this.mActivityRuntimeInfo.suspectRelativeLayout = 0;
        this.mActivityRuntimeInfo.maxLayoutDepth = 0;
        this.mActivityRuntimeInfo.redundantLayout = 0;
        this.mActivityRuntimeInfo.loadTime = 0;
        this.mActivityRuntimeInfo.firstRelativeLayoutDepth = 0;
        this.mActivityRuntimeInfo.maxRelativeLayoutDepth = 0;
        this.mActivityRuntimeInfo.activityViewCount = 0;
        this.mActivityRuntimeInfo.activityVisibleViewCount = 0;
        this.mActivityRuntimeInfo.activityScore = 0;
        this.mActivityRuntimeInfo.avgSm = 0;
        this.mActivityRuntimeInfo.dragFlingCount = 0;
        this.mActivityRuntimeInfo.activityTotalSmCount = 0;
        this.mActivityRuntimeInfo.activityTotalSmUsedTime = 0;
        this.mActivityRuntimeInfo.activityTotalBadSmUsedTime = 0;
        this.mActivityRuntimeInfo.activityTotalSmLayoutTimes = 0;
        this.mActivityRuntimeInfo.activityTotalBadSmCount = 0;
        this.mActivityRuntimeInfo.activityViewCount = 0;
        this.mActivityRuntimeInfo.activityVisibleViewCount = 0;
        this.mActivityRuntimeInfo.totalLayoutCount = 0;
        this.mActivityRuntimeInfo.smoothViewOutRevLayoutDepth = 0;
        this.mActivityRuntimeInfo.checkSystemInfoCount = 0;
        this.mActivityRuntimeInfo.getMemoryCount = 0;
        this.mActivityRuntimeInfo.activityLoadSmUsedTime = 0;
        this.mActivityRuntimeInfo.activityLoadBadSmCount = 0;
        this.mActivityRuntimeInfo.activityLoadSmCount = 0;
        this.mActivityRuntimeInfo.activityLoadBadSmUsedTime = 0;
        this.mActivityRuntimeInfo.cleanerObjectGetCount = 0;
        this.mActivityRuntimeInfo.cleanerObjectSize = 0;
        this.mActivityRuntimeInfo.lastGetCleanerObjectTime = 0;
        this.mActivityRuntimeInfo.lastGetFinalizerTime = 0;
        if (this.mActivityRuntimeInfo.cleanerObjectMap != null) {
            this.mActivityRuntimeInfo.cleanerObjectMap.clear();
        }
        this.mActivityRuntimeInfo.openFileGetCount = 0;
        this.mActivityRuntimeInfo.lastOpenFileGetTime = 0;
        this.mActivityRuntimeInfo.openFile = 0;
        this.mActivityRuntimeInfo.bitmapCount = 0;
        this.mActivityRuntimeInfo.bitmapByteCount = 0;
        this.mActivityRuntimeInfo.bitmap1M = 0;
        this.mActivityRuntimeInfo.bitmap2M = 0;
        this.mActivityRuntimeInfo.bitmap4M = 0;
        this.mActivityRuntimeInfo.bitmap6M = 0;
        this.mActivityRuntimeInfo.bitmap8M = 0;
        this.mActivityRuntimeInfo.bitmap10M = 0;
        this.mActivityRuntimeInfo.bitmap15M = 0;
        this.mActivityRuntimeInfo.bitmapSizeScreen = 0;
        this.mActivityRuntimeInfo.bitmapSizeHashScreen = 0;
        this.mActivityRuntimeInfo.bitmapSize2Screen = 0;
        this.mActivityRuntimeInfo.bitmapSize14Screen = 0;
        notifyActivityLoadStart();
    }

    /* access modifiers changed from: package-private */
    public void onActivityPause() {
        if (this.mActivityRuntimeInfo != null) {
            this.mActivityRuntimeInfo.stayTime = (System.nanoTime() / 1000000) - this.mActivityRuntimeInfo.stayTime;
            this.mActivityRuntimeInfo.threadInterval = this.mRuntimeThreadCount - this.mActivityRuntimeInfo.threadInterval;
            this.mActivityRuntimeInfo.memEnd = (short) ((int) this.mTotalUsedMemory);
            this.mActivityRuntimeInfo.javaEnd = (short) ((int) this.mDalvikPss);
            this.mActivityRuntimeInfo.nativeEnd = (short) ((int) this.mNativeHeapPss);
            this.mActivityRuntimeInfo.anrTime = (short) (this.mActivityRuntimeInfo.anrTime - this.mAnrCount);
            this.mActivityRuntimeInfo.gcCount = this.mTotalGcCount - this.mStartGcCount;
            this.mActivityRuntimeInfo.blockGc = this.mBlockingGCCount - this.mStartBlockingGCCount;
            this.mActivityRuntimeInfo.blockTime = this.mTotalBlockingGCTime - this.mStartBlockingGCTime;
            this.mActivityRuntimeInfo.ioWait = (long) ((short) this.mAvgIOWaitTime);
            this.mActivityRuntimeInfo.pidScore = this.mAvgMyPidScore;
            this.mActivityRuntimeInfo.sysScore = this.mAvgSystemRunningScore;
            this.mActivityRuntimeInfo.pidAvgCpu = (short) this.mMyAvgPidCPUPercent;
            this.mActivityRuntimeInfo.sysAvgCpu = (short) this.mSysAvgCPUPercent;
            if (sIsTraceDetail) {
                this.mActivityRuntimeInfo.classCount = Debug.getLoadedClassCount() - this.mActivityRuntimeInfo.classCount;
            }
            this.mActivityRuntimeInfo.dragFlingCount = this.mSmoothCalculate.mActivityFlingCount;
            if (this.mSmoothCalculate.mActivityTotalSmUsedTime > 0 && this.mSmoothCalculate.mActivityTotalSmUsedTime < 600000) {
                this.mActivityRuntimeInfo.activityTotalSmCount = this.mSmoothCalculate.mActivityTotalSmCount;
                this.mActivityRuntimeInfo.activityTotalSmUsedTime = this.mSmoothCalculate.mActivityTotalSmUsedTime;
                this.mActivityRuntimeInfo.activityTotalSmLayoutTimes = this.mSmoothCalculate.mActivityTotalSmLayoutTimes;
                this.mActivityRuntimeInfo.activityTotalBadSmUsedTime = this.mSmoothCalculate.mActivityTotalBadSmUsedTime;
                this.mActivityRuntimeInfo.activityTotalBadSmCount = this.mSmoothCalculate.mActivityTotalBadSmCount;
                this.mActivityRuntimeInfo.activityTotalFpsCount = this.mSmoothCalculate.mActivityTotalFpsCount;
                this.mActivityRuntimeInfo.activityTotalFlingCount = this.mSmoothCalculate.mActivityTotalFlingCount;
                int[] iArr = this.mActivityRuntimeInfo.activityBadSmoothStepCount;
                if (iArr != null) {
                    int i = 0;
                    for (int i2 : iArr) {
                        i += i2;
                    }
                    if (i > this.mActivityRuntimeInfo.activityTotalSmCount || i > this.mActivityRuntimeInfo.activityTotalBadSmCount) {
                        if (sIsTraceDetail) {
                            Log.e(TAG, "滑动中的问题帧数量有误，将丢弃！");
                        }
                        for (int i3 = 0; i3 < iArr.length; i3++) {
                            iArr[i3] = 0;
                        }
                    }
                }
            }
            this.mActivityRuntimeInfo.pidIoWaitSumAvg = this.mPidIoWaitSumAvg * 10;
            this.mPidIoWaitCountLast = this.mPidIoWaitCount - this.mPidIoWaitSumStart;
            this.mActivityRuntimeInfo.pidIoWaitCount = this.mPidIoWaitCountLast;
            this.mActivityRuntimeInfo.pidPerCpuLoadAvg = this.mPidPerCpuLoadAvg / ((float) this.mCpuProcessCount);
            this.mPidPerCpuLoadLast = this.mPidPerCpuLoadAvg;
            this.mActivityRuntimeInfo.loadAvg1Min = this.mSystemLoadAvg[0];
            if (this.mActivityRuntimeInfo.pidPerCpuLoadAvg == 0.0f) {
                this.mActivityRuntimeInfo.pidPerCpuLoadAvg = this.mSystemLoadAvg[0];
            }
            if (this.mTraceDetail != null && TraceDetail.sTraceMemory && (this.mActivityRuntimeInfo.memStart == 0 || this.mActivityRuntimeInfo.totalUss == 0)) {
                getMemInfo(false);
            }
            if (this.mEvaluateScore != null && this.mActivityRuntimeInfo.isColdOpen) {
                this.mActivityRuntimeInfo.activityScore = this.mEvaluateScore.evaluateActivityScore(this, this.mActivityRuntimeInfo);
            }
            if (sIsTraceDetail) {
                this.mTraceDetail.mActivityRuntimeInfoList.add(this.mActivityRuntimeInfo);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:3|(3:4|5|(8:9|10|(3:12|(3:15|16|13)|34)|17|(3:22|23|(2:25|(1:(3:29|30|27))))|31|32|35))|19|(0)|31|32|35) */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x008a */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005d A[SYNTHETIC, Splitter:B:22:0x005d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityDestroyed(android.app.Activity r8) {
        /*
            r7 = this;
            if (r8 == 0) goto L_0x009f
            boolean r0 = r8.isFinishing()
            if (r0 != 0) goto L_0x000a
            goto L_0x009f
        L_0x000a:
            java.lang.ref.WeakReference r0 = new java.lang.ref.WeakReference
            r0.<init>(r8)
            java.util.WeakHashMap<java.lang.Object, java.lang.Object> r1 = r7.mLeakMemoryWeakMap
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            r1.put(r8, r2)
            r1 = 0
            r2 = 1
            java.lang.Class r3 = r7.mClassFragmentActivity     // Catch:{ Throwable -> 0x005a }
            if (r3 == 0) goto L_0x005a
            java.lang.Class r3 = r7.mClassFragmentActivity     // Catch:{ Throwable -> 0x005a }
            java.lang.Class r4 = r8.getClass()     // Catch:{ Throwable -> 0x005a }
            boolean r3 = r3.isAssignableFrom(r4)     // Catch:{ Throwable -> 0x005a }
            if (r3 == 0) goto L_0x005a
            java.lang.reflect.Method r3 = r7.mGetSupportFragmentManager     // Catch:{ Throwable -> 0x0058 }
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0058 }
            java.lang.Object r3 = r3.invoke(r8, r4)     // Catch:{ Throwable -> 0x0058 }
            java.lang.Class r4 = r3.getClass()     // Catch:{ Throwable -> 0x0058 }
            java.lang.String r5 = "mAdded"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x0058 }
            r4.setAccessible(r2)     // Catch:{ Throwable -> 0x0058 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Throwable -> 0x0058 }
            java.util.ArrayList r3 = (java.util.ArrayList) r3     // Catch:{ Throwable -> 0x0058 }
            if (r3 == 0) goto L_0x0058
            r4 = 0
        L_0x0046:
            int r5 = r3.size()     // Catch:{ Throwable -> 0x0058 }
            if (r4 >= r5) goto L_0x0058
            java.util.WeakHashMap<java.lang.Object, java.lang.Object> r5 = r7.mLeakMemoryWeakMap     // Catch:{ Throwable -> 0x0058 }
            java.lang.Object r6 = r3.get(r4)     // Catch:{ Throwable -> 0x0058 }
            r5.put(r6, r0)     // Catch:{ Throwable -> 0x0058 }
            int r4 = r4 + 1
            goto L_0x0046
        L_0x0058:
            r3 = 1
            goto L_0x005b
        L_0x005a:
            r3 = 0
        L_0x005b:
            if (r3 != 0) goto L_0x008a
            android.app.FragmentManager r3 = r8.getFragmentManager()     // Catch:{ Throwable -> 0x008a }
            if (r3 == 0) goto L_0x008a
            java.lang.Class r4 = r3.getClass()     // Catch:{ Throwable -> 0x008a }
            java.lang.String r5 = "mAdded"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x008a }
            r4.setAccessible(r2)     // Catch:{ Throwable -> 0x008a }
            java.lang.Object r2 = r4.get(r3)     // Catch:{ Throwable -> 0x008a }
            java.util.ArrayList r2 = (java.util.ArrayList) r2     // Catch:{ Throwable -> 0x008a }
            if (r2 == 0) goto L_0x008a
        L_0x0078:
            int r3 = r2.size()     // Catch:{ Throwable -> 0x008a }
            if (r1 >= r3) goto L_0x008a
            java.util.WeakHashMap<java.lang.Object, java.lang.Object> r3 = r7.mLeakMemoryWeakMap     // Catch:{ Throwable -> 0x008a }
            java.lang.Object r4 = r2.get(r1)     // Catch:{ Throwable -> 0x008a }
            r3.put(r4, r0)     // Catch:{ Throwable -> 0x008a }
            int r1 = r1 + 1
            goto L_0x0078
        L_0x008a:
            android.view.Window r8 = r8.getWindow()     // Catch:{ Throwable -> 0x009e }
            android.view.View r8 = r8.getDecorView()     // Catch:{ Throwable -> 0x009e }
            android.view.View r8 = r8.getRootView()     // Catch:{ Throwable -> 0x009e }
            java.util.WeakHashMap<java.lang.Object, java.lang.Object> r1 = r7.mLeakMemoryWeakMap     // Catch:{ Throwable -> 0x009e }
            r1.put(r8, r0)     // Catch:{ Throwable -> 0x009e }
            r7.addSmoothViewToLeak(r8, r0)     // Catch:{ Throwable -> 0x009e }
        L_0x009e:
            return
        L_0x009f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.onActivityDestroyed(android.app.Activity):void");
    }

    /* access modifiers changed from: package-private */
    public void addSmoothViewToLeak(View view, WeakReference<Activity> weakReference) {
        if (this.mSmoothCalculate != null && this.mSmoothCalculate.isSmoothView(view)) {
            this.mLeakMemoryWeakMap.put(view, weakReference);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                addSmoothViewToLeak(viewGroup.getChildAt(i), weakReference);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void doLifeCycleCheck(Activity activity, int i) {
        if (sIsTraceDetail) {
            if (this.mActivityRuntimeInfo == null) {
                this.mActivityRuntimeInfo = new ActivityRuntimeInfo();
                this.mActivityRuntimeInfo.lifeCycleArrayUsedTime = new long[6];
                this.mActivityRuntimeInfo.activityName = this.mActivityLifecycleCallback.mActivityName;
            }
            if (this.mHandler == null) {
                this.mHandler = new MyHandler();
            }
            this.mHandler.sendMessageAtFrontOfQueue(Message.obtain(this.mHandler, 15, i, 0, (Object) null));
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityResume(Activity activity) {
        this.mActivityName = this.mActivityLifecycleCallback.mActivityName;
        this.mCheckAnrTime = this.mActivityLifecycleCallback.mActivityResumeTime;
        this.mActivityIdleTime = 0;
        this.mActivityIdleFistTime = 0;
        this.mIdleNotifyCount = 0;
        this.mUIHiddenTime = 0;
        this.mMaxBlockIdletime = 0;
        back2ForeChanged();
        if (!this.mIsActivityColdOpen && this.mMessageQueue != null) {
            this.mIsIdleGeted = false;
            this.mMessageQueue.addIdleHandler(this.mIdleHandler);
        }
        if (this.mThreadHandler != null) {
            this.mThreadHandler.sendEmptyMessageDelayed(2, 100);
            if (this.mAppProgressImportance != 100) {
                this.mThreadHandler.removeMessages(8);
                this.mThreadHandler.sendEmptyMessage(8);
            }
        }
        getTrafficStats();
        if (!this.mIsActivityColdOpen) {
            this.mIsFirstOpenActivity = !this.mActivitysMap.containsKey(this.mActivityName);
            onActivityLoadStart(this.mActivityLifecycleCallback.mActivityResumeTime);
            this.mLoadTimeCalculate.needStopLoadTimeCalculate(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityPause(Activity activity) {
        int size;
        int i;
        int i2;
        if (this.mActivityRuntimeInfo != null) {
            if (this.mIsActivityColdOpen) {
                Integer num = this.mActivitysMap.get(this.mActivityName);
                if (num == null) {
                    this.mActivitysMap.put(this.mActivityName, 1);
                    i2 = 1;
                } else {
                    i2 = num.intValue() + 1;
                    this.mActivitysMap.put(this.mActivityName, Integer.valueOf(i2));
                }
                if (this.mActivityRuntimeInfo != null) {
                    this.mActivityRuntimeInfo.statisticsDiscard = false;
                    if (i2 > OnLineMonitorApp.sColdOpenMaxTimesForStatistics) {
                        this.mActivityRuntimeInfo.statisticsDiscard = true;
                    }
                }
            } else if (this.mSmoothCalculate.mActivityTotalSmCount > 30) {
                Integer num2 = this.mActivitysHotOpenMap.get(this.mActivityName);
                if (num2 == null) {
                    this.mActivitysHotOpenMap.put(this.mActivityName, 1);
                    i = 1;
                } else {
                    i = num2.intValue() + 1;
                    this.mActivitysHotOpenMap.put(this.mActivityName, Integer.valueOf(i));
                }
                if (this.mActivityRuntimeInfo != null) {
                    this.mActivityRuntimeInfo.statisticsDiscard = false;
                    if (i > OnLineMonitorApp.sHotOpenMaxTimesForStatistics) {
                        this.mActivityRuntimeInfo.statisticsDiscard = true;
                    }
                }
            } else if (this.mActivityRuntimeInfo != null) {
                this.mActivityRuntimeInfo.statisticsDiscard = true;
            }
        }
        this.mIsActivityColdOpen = false;
        if (this.mSmoothCalculate != null && this.mSmoothCalculate.mIsFlingStart) {
            this.mSmoothCalculate.stopSmoothSmCalculate();
        }
        if (this.mIsBootEndActivity) {
            commmitBootFinished();
            notifyBootFinished();
            this.mIsBootEndActivity = false;
        }
        try {
            if (!(this.mOnlineStatistics == null || this.mWeakCheckedThreadPool == null || this.mWeakCheckedThreadPool.size() <= 0)) {
                for (Map.Entry next : this.mWeakCheckedThreadPool.entrySet()) {
                    if (next != null) {
                        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) next.getKey();
                        String str = (String) next.getValue();
                        if (threadPoolExecutor != null && threadPoolExecutor.getQueue() != null && (size = threadPoolExecutor.getQueue().size()) >= OnLineMonitorApp.sThreadPoolQueueCommitSize && size >= this.mCpuProcessCount * 4) {
                            int size2 = this.mOnlineStatistics.size();
                            for (int i3 = 0; i3 < size2; i3++) {
                                OnlineStatistics onlineStatistics = this.mOnlineStatistics.get(i3);
                                if (onlineStatistics != null) {
                                    onlineStatistics.onThreadPoolProblem(this.mOnLineStat, this.mActivityName, threadPoolExecutor, str);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
        if (sIsTraceDetail && TraceDetail.sTraceMemory) {
            try {
                System.gc();
                System.runFinalization();
                System.gc();
            } catch (Throwable unused2) {
            }
        }
        this.mCheckFinalizerReference.mergeFinalize();
        commitOnActivityPaused();
        clear();
    }

    /* access modifiers changed from: package-private */
    public void onHomePageLoadEnd(int i, int i2) {
        this.mBootActivityLoadTime = i;
        this.mBootStartActivityTime = i2;
        if (this.mThreadHandler != null) {
            recordBootResource(2, true);
            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.mThreadHandler.sendMessage(Message.obtain(this.mThreadHandler, 18, 2, 0));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void commmitBootFinished() {
        if (this.mIsBootEndActivity && OnLineMonitorApp.sIsBootCorrect && this.mOnlineStatistics != null && this.mOnLineStat != null && this.mBootActivityLoadTime > 0) {
            int i = this.mBootUsedTime + this.mBootActivityLoadTime;
            if (sIsNormalDebug) {
                Log.e(TAG, "纯启动耗时=" + this.mBootUsedTime + ", 启动加首页耗时=" + i + ", 首页的StartActivityTime=" + this.mBootStartActivityTime + "，广告耗时=" + OnLineMonitorApp.sAdvertisementTime);
            }
            if (!OnLineMonitorApp.sBackInGroundOnBoot) {
                this.mBootTotalTime = i;
                if (i <= OnLineMonitorApp.sMaxBootTotalTime) {
                    if (this.mOnLineStat.activityRuntimeInfo == null) {
                        this.mOnLineStat.activityRuntimeInfo = this.mActivityRuntimeInfo;
                    }
                    int size = this.mOnlineStatistics.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        OnlineStatistics onlineStatistics = this.mOnlineStatistics.get(i2);
                        if (onlineStatistics != null) {
                            onlineStatistics.onBootFinished(this.mOnLineStat, (long) this.mBootUsedTime, (long) i, OnLineMonitorApp.sIsCodeBoot, OnLineMonitorApp.sBootExtraType);
                        }
                    }
                    this.mIsBootEndActivity = false;
                    if (this.mBootResourceUsedInfoList != null && this.mBootResourceUsedInfoList.size() > 0 && this.mIsDeviceSampling && this.mThreadHandler != null) {
                        this.mThreadHandler.sendEmptyMessageDelayed(19, 15000);
                    }
                } else if (sIsNormalDebug) {
                    Log.e(TAG, "无效的启动,数据异常!");
                }
            } else if (sIsNormalDebug) {
                Log.e(TAG, "无效的启动,启动过程中界面进入过后台!");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityStopped(Activity activity) {
        if (this.mActivityLifecycleCallback.mStartCounter == 0) {
            this.mOnLineStat.isInBackGround = true;
            this.mCheckAnrTime = System.nanoTime() / 1000000;
            this.mUIHiddenTime = this.mCheckAnrTime;
            this.mThreadHandler.removeMessages(8);
            this.mThreadHandler.sendEmptyMessageDelayed(8, (long) OnLineMonitorApp.sWritePerformanceInfo);
            notifyBackForGroundListener(10);
            if (sIsNormalDebug) {
                Log.e(TAG, "界面不可见");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnlineRuntimeStat(int i) {
        if (this.mThreadHandler != null && this.mLastNotifyType != i) {
            this.mLastNotifyType = i;
            this.mThreadHandler.sendMessage(this.mThreadHandler.obtainMessage(4, i, 0));
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyActivityLoadStart() {
        if (this.mOnActivityLoadListenerList != null) {
            synchronized (this.mOnActivityLoadListenerList) {
                for (int i = 0; i < this.mOnActivityLoadListenerList.size(); i++) {
                    this.mOnActivityLoadListenerList.get(i).onActivityLoadStart(this.mActivityLifecycleCallback.mActivity, this.mOnLineStat, this.mActivityRuntimeInfo);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyActivityLoadFinish() {
        if (this.mOnLineStat != null) {
            this.mOnLineStat.isActivityLoading = false;
        }
        if (this.mIsBootEndActivity && OnLineMonitorApp.sIsBootCorrect) {
            notifyBootAccurateFinished(1);
            this.mProcessCpuTracker.update();
            this.mBootJiffyTime = this.mProcessCpuTracker.mPidJiffyTime;
            if (this.mTraceDetail != null) {
                this.mTraceDetail.mMainThreadEndCpu = Debug.threadCpuTimeNanos();
                if (sIsTraceDetail) {
                    this.mTraceDetail.mExecuteThreadInfoBootSize = this.mTraceDetail.mExecuteThreadInfoList.size();
                }
                this.mTraceDetail.onBootStep2();
            }
            if (sIsTraceDetail) {
                this.mTraceDetail.onBootEnd();
                if (TraceDetail.sTraceThread) {
                    try {
                        for (Map.Entry<String, ThreadInfo> value : this.mThreadInfoHashMap.entrySet()) {
                            ThreadInfo threadInfo = (ThreadInfo) value.getValue();
                            if (threadInfo != null) {
                                threadInfo.onBootEnd();
                            }
                        }
                    } catch (Throwable unused) {
                    }
                    if (!TraceDetail.sTraceThreadWait) {
                        this.mTraceDetail.getThreadIoWaitTime();
                    }
                }
            }
        }
        if (this.mOnActivityLoadListenerList != null) {
            synchronized (this.mOnActivityLoadListenerList) {
                for (int i = 0; i < this.mOnActivityLoadListenerList.size(); i++) {
                    this.mOnActivityLoadListenerList.get(i).onActivityLoadFinish(this.mActivityLifecycleCallback.mActivity, this.mOnLineStat, this.mActivityRuntimeInfo);
                }
            }
        }
        this.mIsInBootStep = false;
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] array;
        synchronized (this.mOnActivityLifeCycleList) {
            array = this.mOnActivityLifeCycleList.size() > 0 ? this.mOnActivityLifeCycleList.toArray() : null;
        }
        return array;
    }

    /* access modifiers changed from: package-private */
    public void notifyOnActivityLifeCycleList(Activity activity, int i) {
        long j;
        Activity activity2 = activity;
        int i2 = i;
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null && collectActivityLifecycleCallbacks.length != 0) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                OnActivityLifeCycle onActivityLifeCycle = (OnActivityLifeCycle) obj;
                if (onActivityLifeCycle != null) {
                    try {
                        long j2 = 0;
                        if (TraceDetail.sTraceOnLineListener) {
                            j2 = System.nanoTime() / 1000000;
                            j = Debug.threadCpuTimeNanos();
                        } else {
                            j = 0;
                        }
                        if (i2 == 0) {
                            onActivityLifeCycle.onActivityIdle(activity2, this.mOnLineStat);
                        } else if (i2 == 1) {
                            onActivityLifeCycle.onActivityCreate(activity2, this.mOnLineStat);
                        } else if (i2 == 2) {
                            onActivityLifeCycle.onActivityStarted(activity2, this.mOnLineStat);
                        } else if (i2 == 3) {
                            onActivityLifeCycle.onActivityResume(activity2, this.mOnLineStat);
                        } else if (i2 == 4) {
                            onActivityLifeCycle.onActivityPaused(activity2, this.mOnLineStat);
                        } else if (i2 == 5) {
                            onActivityLifeCycle.onActivityStoped(activity2, this.mOnLineStat);
                        } else if (i2 == 6) {
                            onActivityLifeCycle.onActivityDestroyed(activity2, this.mOnLineStat);
                        }
                        if (TraceDetail.sTraceOnLineListener) {
                            long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
                            long nanoTime = (System.nanoTime() / 1000000) - j2;
                            if (nanoTime >= ((long) TraceDetail.sTraceOnLineDuration)) {
                                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                                methodInfo.activityName = this.mActivityName;
                                methodInfo.cpuTime = (threadCpuTimeNanos - j) / 1000000;
                                methodInfo.realTime = nanoTime;
                                methodInfo.priority = i2;
                                methodInfo.methodName = onActivityLifeCycle.getClass().getName();
                                if (this.mTraceDetail != null) {
                                    this.mTraceDetail.mOnActivityLifeCycleTimeList.add(methodInfo);
                                }
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnCheckViewTree(int i) {
        if (this.mOnCheckViewTreeList != null) {
            synchronized (this.mOnCheckViewTreeList) {
                for (int i2 = 0; i2 < this.mOnCheckViewTreeList.size(); i2++) {
                    try {
                        OnCheckViewTree onCheckViewTree = this.mOnCheckViewTreeList.get(i2);
                        if (onCheckViewTree != null) {
                            onCheckViewTree.onCheckViewTree(this.mOnLineStat, this.mActivityLifecycleCallback.mActivity, i);
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyBootFinished() {
        long j;
        if (this.mOnBootFinishedList != null) {
            synchronized (this.mOnBootFinishedList) {
                for (int i = 0; i < this.mOnBootFinishedList.size(); i++) {
                    try {
                        OnBootFinished onBootFinished = this.mOnBootFinishedList.get(i);
                        if (onBootFinished != null) {
                            long j2 = 0;
                            if (TraceDetail.sTraceOnLineListener) {
                                long nanoTime = System.nanoTime() / 1000000;
                                j2 = Debug.threadCpuTimeNanos();
                                j = nanoTime;
                            } else {
                                j = 0;
                            }
                            onBootFinished.onBootFinished(this.mOnLineStat);
                            if (TraceDetail.sTraceOnLineListener) {
                                long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
                                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                                methodInfo.activityName = this.mActivityName;
                                methodInfo.cpuTime = (threadCpuTimeNanos - j2) / 1000000;
                                methodInfo.realTime = (System.nanoTime() / 1000000) - j;
                                methodInfo.methodName = onBootFinished.getClass().getName();
                                if (this.mTraceDetail != null) {
                                    this.mTraceDetail.mOnBootFinishedList.add(methodInfo);
                                }
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
                this.mOnBootFinishedList.clear();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyBootAccurateFinished(int i) {
        long j;
        int i2 = i;
        if (this.mOnAccurateBootListener != null) {
            synchronized (this.mOnAccurateBootListener) {
                for (int i3 = 0; i3 < this.mOnAccurateBootListener.size(); i3++) {
                    try {
                        OnAccurateBootListener onAccurateBootListener = this.mOnAccurateBootListener.get(i3);
                        if (onAccurateBootListener != null) {
                            long j2 = 0;
                            if (TraceDetail.sTraceOnLineListener) {
                                long nanoTime = System.nanoTime() / 1000000;
                                j2 = Debug.threadCpuTimeNanos();
                                j = nanoTime;
                            } else {
                                j = 0;
                            }
                            onAccurateBootListener.OnAccurateBootFinished(this.mOnLineStat, i2);
                            if (TraceDetail.sTraceOnLineListener) {
                                long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
                                TraceDetail.MethodInfo methodInfo = new TraceDetail.MethodInfo();
                                methodInfo.activityName = this.mActivityName;
                                methodInfo.cpuTime = (threadCpuTimeNanos - j2) / 1000000;
                                methodInfo.realTime = (System.nanoTime() / 1000000) - j;
                                methodInfo.methodName = onAccurateBootListener.getClass().getName();
                                if (this.mTraceDetail != null) {
                                    this.mTraceDetail.mOnBootFinishedList.add(methodInfo);
                                }
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
                if (i2 > 0) {
                    this.mOnAccurateBootListener.clear();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(14:64|65|66|(2:69|70)|71|72|(3:75|76|(1:78)(2:79|(1:81)))|82|83|(10:88|(1:90)|91|(1:93)|94|(1:96)|97|(14:99|(1:101)|102|(1:104)|105|(1:109)|110|(1:112)|113|(1:115)|116|(1:118)|119|(3:121|(1:123)(1:124)|125))|126|(6:128|129|130|(1:132)|133|(1:135)))|136|137|(1:144)|(2:148|(2:150|(2:152|153)))) */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x07fc, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x07fd, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:136:0x07cb */
    /* JADX WARNING: Missing exception handler attribute for start block: B:71:0x01c3 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:82:0x033d */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0753 A[Catch:{ Throwable -> 0x07fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01cd A[Catch:{ Throwable -> 0x033d }] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x02b3 A[Catch:{ Throwable -> 0x033d }] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x034d A[Catch:{ Throwable -> 0x07fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0487 A[Catch:{ Throwable -> 0x07fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0579 A[Catch:{ Throwable -> 0x07fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x061b A[Catch:{ Throwable -> 0x07fc }] */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getMemInfo(boolean r19) {
        /*
            r18 = this;
            r1 = r18
            android.app.ActivityManager r0 = r1.mActivityManager
            if (r0 == 0) goto L_0x0aa9
            int r0 = sApiLevel
            r2 = 15
            if (r0 >= r2) goto L_0x000e
            goto L_0x0aa9
        L_0x000e:
            android.app.ActivityManager r0 = r1.mActivityManager
            android.app.ActivityManager$MemoryInfo r2 = r1.mMemoryInfo
            r0.getMemoryInfo(r2)
            android.app.ActivityManager$MemoryInfo r0 = r1.mMemoryInfo
            long r2 = r0.availMem
            r4 = 1024(0x400, double:5.06E-321)
            long r2 = r2 / r4
            long r2 = r2 / r4
            r1.mAvailMemory = r2
            android.app.ActivityManager$MemoryInfo r0 = r1.mMemoryInfo
            boolean r0 = r0.lowMemory
            r1.mIsLowMemroy = r0
            r0 = 2
            short r2 = r1.mDevicesScore
            r3 = 90
            r6 = 8
            r8 = 60
            if (r2 < r3) goto L_0x0033
            r2 = 10
            goto L_0x004d
        L_0x0033:
            short r2 = r1.mDevicesScore
            r3 = 85
            if (r2 < r3) goto L_0x003c
            r2 = 8
            goto L_0x004d
        L_0x003c:
            short r2 = r1.mDevicesScore
            r3 = 75
            if (r2 < r3) goto L_0x0045
            r0 = 5
            r2 = 5
            goto L_0x004d
        L_0x0045:
            short r2 = r1.mDevicesScore
            if (r2 < r8) goto L_0x004c
            r0 = 4
            r2 = 4
            goto L_0x004d
        L_0x004c:
            r2 = 2
        L_0x004d:
            if (r19 != 0) goto L_0x0073
            boolean r0 = r1.mIsLowMemroy
            if (r0 != 0) goto L_0x0073
            int r0 = r1.mTrimMemoryLevel
            if (r0 >= r8) goto L_0x0073
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x0062
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.getMemoryCount
            if (r0 < r2) goto L_0x0062
            return
        L_0x0062:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            boolean r0 = r0.isActivityLoading
            if (r0 == 0) goto L_0x0073
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x0073
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.getMemoryCount
            if (r0 <= 0) goto L_0x0073
            return
        L_0x0073:
            r0 = 0
            long r2 = java.lang.System.nanoTime()
            r9 = 1000000(0xf4240, double:4.940656E-318)
            long r2 = r2 / r9
            int r2 = sApiLevel
            r3 = 23
            r9 = 1
            r10 = 0
            if (r2 < r3) goto L_0x0097
            int[] r2 = new int[r9]
            int r11 = android.os.Process.myPid()
            r2[r10] = r11
            android.app.ActivityManager r11 = r1.mActivityManager
            android.os.Debug$MemoryInfo[] r2 = r11.getProcessMemoryInfo(r2)
            if (r2 == 0) goto L_0x00a8
            r0 = r2[r10]
            goto L_0x00a8
        L_0x0097:
            android.os.Debug$MemoryInfo r0 = new android.os.Debug$MemoryInfo
            r0.<init>()
            android.os.Debug.getMemoryInfo(r0)
            int r2 = r0.getTotalPrivateDirty()
            if (r2 != 0) goto L_0x00a8
            android.os.Debug.getMemoryInfo(r0)
        L_0x00a8:
            r2 = r0
            if (r2 != 0) goto L_0x00ac
            return
        L_0x00ac:
            int r0 = r2.dalvikPss
            int r0 = r0 / 1024
            long r11 = (long) r0
            r1.mDalvikPss = r11
            long r11 = r1.mDalvikPss
            r13 = 0
            int r0 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r0 != 0) goto L_0x00c7
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()
            long r11 = r0.totalMemory()
            long r11 = r11 / r4
            long r11 = r11 / r4
            r1.mDalvikPss = r11
        L_0x00c7:
            int r0 = r2.nativePss
            int r0 = r0 / 1024
            long r11 = (long) r0
            r1.mNativeHeapPss = r11
            long r11 = r1.mNativeHeapPss
            int r0 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r0 != 0) goto L_0x00dc
            long r11 = android.os.Debug.getNativeHeapSize()
            long r11 = r11 / r4
            long r11 = r11 / r4
            r1.mNativeHeapPss = r11
        L_0x00dc:
            int r0 = r2.getTotalPss()
            int r0 = r0 / 1024
            long r11 = (long) r0
            r1.mTotalUsedMemory = r11
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x00fe
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.memMax
            long r11 = (long) r0
            long r13 = r1.mTotalUsedMemory
            int r0 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r0 >= 0) goto L_0x00fe
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r11 = r1.mTotalUsedMemory
            int r11 = (int) r11
            short r11 = (short) r11
            r0.memMax = r11
            r11 = 1
            goto L_0x00ff
        L_0x00fe:
            r11 = 0
        L_0x00ff:
            boolean r0 = sIsTraceDetail
            if (r0 == 0) goto L_0x0131
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0130 }
            long r12 = r0.freeMemory()     // Catch:{ Throwable -> 0x0130 }
            long r12 = r12 / r4
            long r12 = r12 / r4
            r1.mDalvikFree = r12     // Catch:{ Throwable -> 0x0130 }
            long r12 = r0.totalMemory()     // Catch:{ Throwable -> 0x0130 }
            long r12 = r12 / r4
            long r12 = r12 / r4
            r1.mDalvikHeapSize = r12     // Catch:{ Throwable -> 0x0130 }
            long r12 = r1.mDalvikHeapSize     // Catch:{ Throwable -> 0x0130 }
            long r7 = r1.mDalvikFree     // Catch:{ Throwable -> 0x0130 }
            r0 = 0
            long r12 = r12 - r7
            r1.mDalvikAllocated = r12     // Catch:{ Throwable -> 0x0130 }
            long r7 = android.os.Debug.getNativeHeapSize()     // Catch:{ Throwable -> 0x0130 }
            long r7 = r7 / r4
            long r7 = r7 / r4
            r1.mNativeHeapSize = r7     // Catch:{ Throwable -> 0x0130 }
            long r7 = android.os.Debug.getNativeHeapAllocatedSize()     // Catch:{ Throwable -> 0x0130 }
            long r7 = r7 / r4
            long r7 = r7 / r4
            r1.mNativeHeapAllocatedSize = r7     // Catch:{ Throwable -> 0x0130 }
            goto L_0x0131
        L_0x0130:
        L_0x0131:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x0880
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r4 = r0.getMemoryCount
            int r4 = r4 + r9
            short r4 = (short) r4
            r0.getMemoryCount = r4
            r4 = 19
            int r0 = sApiLevel     // Catch:{ Throwable -> 0x07fc }
            if (r0 < r3) goto L_0x01c3
            if (r11 == 0) goto L_0x01c3
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.graphics"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryGraphics = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.code"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryCode = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.stack"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryStack = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.system"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summarySystem = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.java-heap"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryJavaHeap = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.native-heap"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryNativeHeap = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.private-other"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryPrivateOther = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.total-pss"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryTotalpss = r5     // Catch:{ Throwable -> 0x01c3 }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01c3 }
            java.lang.String r5 = "summary.total-swap"
            java.lang.String r5 = r2.getMemoryStat(r5)     // Catch:{ Throwable -> 0x01c3 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x01c3 }
            r0.summaryTotalswap = r5     // Catch:{ Throwable -> 0x01c3 }
        L_0x01c3:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x033d
            if (r11 == 0) goto L_0x033d
            int r0 = sApiLevel     // Catch:{ Throwable -> 0x033d }
            if (r0 < r3) goto L_0x02b3
            java.lang.Class<android.os.Debug$MemoryInfo> r0 = android.os.Debug.MemoryInfo.class
            java.lang.String r5 = "getOtherPrivate"
            java.lang.Class[] r7 = new java.lang.Class[r9]     // Catch:{ Throwable -> 0x033d }
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x033d }
            r7[r10] = r8     // Catch:{ Throwable -> 0x033d }
            java.lang.reflect.Method r0 = r0.getDeclaredMethod(r5, r7)     // Catch:{ Throwable -> 0x033d }
            r0.setAccessible(r9)     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r8 = 6
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x033d }
            r7[r10] = r8     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r7 = r0.invoke(r2, r7)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r7 = (java.lang.Integer) r7     // Catch:{ Throwable -> 0x033d }
            int r7 = r7.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherSo = r7     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r8 = 7
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x033d }
            r7[r10] = r8     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r7 = r0.invoke(r2, r7)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r7 = (java.lang.Integer) r7     // Catch:{ Throwable -> 0x033d }
            int r7 = r7.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherJar = r7     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x033d }
            r7[r10] = r6     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r6 = r0.invoke(r2, r7)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = (java.lang.Integer) r6     // Catch:{ Throwable -> 0x033d }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherApk = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r7 = 9
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x033d }
            r6[r10] = r7     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r6 = r0.invoke(r2, r6)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = (java.lang.Integer) r6     // Catch:{ Throwable -> 0x033d }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherTtf = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r7 = 10
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x033d }
            r6[r10] = r7     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r6 = r0.invoke(r2, r6)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = (java.lang.Integer) r6     // Catch:{ Throwable -> 0x033d }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherDex = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r7 = 11
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x033d }
            r6[r10] = r7     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r6 = r0.invoke(r2, r6)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = (java.lang.Integer) r6     // Catch:{ Throwable -> 0x033d }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherOat = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r7 = 12
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x033d }
            r6[r10] = r7     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r6 = r0.invoke(r2, r6)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = (java.lang.Integer) r6     // Catch:{ Throwable -> 0x033d }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherArt = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r7 = 13
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x033d }
            r6[r10] = r7     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r6 = r0.invoke(r2, r6)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r6 = (java.lang.Integer) r6     // Catch:{ Throwable -> 0x033d }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherMap = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x033d }
            r7 = 3
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x033d }
            r6[r10] = r7     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r0 = r0.invoke(r2, r6)     // Catch:{ Throwable -> 0x033d }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x033d }
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x033d }
            r5.memOtherAshmem = r0     // Catch:{ Throwable -> 0x033d }
            goto L_0x033d
        L_0x02b3:
            int r0 = sApiLevel     // Catch:{ Throwable -> 0x033d }
            if (r0 < r4) goto L_0x033d
            java.lang.Class<android.os.Debug$MemoryInfo> r0 = android.os.Debug.MemoryInfo.class
            java.lang.String r5 = "otherStats"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r5)     // Catch:{ Throwable -> 0x033d }
            r0.setAccessible(r9)     // Catch:{ Throwable -> 0x033d }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ Throwable -> 0x033d }
            int[] r0 = (int[]) r0     // Catch:{ Throwable -> 0x033d }
            int[] r0 = (int[]) r0     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 46
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 44
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherSo = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 53
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 51
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherJar = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 60
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 58
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherApk = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 67
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 65
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherTtf = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 74
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 72
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherDex = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 81
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 79
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherOat = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 88
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 86
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherArt = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 95
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r7 = 93
            r7 = r0[r7]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r7
            r5.memOtherMap = r6     // Catch:{ Throwable -> 0x033d }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x033d }
            r6 = 25
            r6 = r0[r6]     // Catch:{ Throwable -> 0x033d }
            r0 = r0[r3]     // Catch:{ Throwable -> 0x033d }
            int r6 = r6 + r0
            r5.memOtherAshmem = r6     // Catch:{ Throwable -> 0x033d }
        L_0x033d:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x07cb
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x07cb
            if (r11 == 0) goto L_0x07cb
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryInstance     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0483
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.stringInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.stringInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.lang.Runnable> r8 = java.lang.Runnable.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.runnableInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.runnableInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.concurrent.ThreadPoolExecutor> r8 = java.util.concurrent.ThreadPoolExecutor.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.threadPoolExecutorInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.threadPoolExecutorInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.String r8 = "com.android.org.conscrypt.OpenSSLSocketImpl"
            java.lang.Class r8 = java.lang.Class.forName(r8)     // Catch:{ Throwable -> 0x07fc }
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.openSslSocketCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.openSslSocketCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.graphics.drawable.Drawable> r8 = android.graphics.drawable.Drawable.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.drawableInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.drawableInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.nio.ByteBuffer> r8 = java.nio.ByteBuffer.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.byteBufferInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.byteBufferInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<byte[]> r8 = byte[].class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.byteArrayInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.byteArrayInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.graphics.Bitmap> r8 = android.graphics.Bitmap.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.bitmapInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.bitmapInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Message> r8 = android.os.Message.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.messageInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.messageInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x0483:
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryInstance     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0575
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.ArrayList> r8 = java.util.ArrayList.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.arrayListInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.arrayListInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.LinkedList> r8 = java.util.LinkedList.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.linkedListInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.linkedListInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.List> r8 = java.util.List.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.listInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.listInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.HashMap> r8 = java.util.HashMap.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.hashMapInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.hashMapInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.concurrent.ConcurrentHashMap> r8 = java.util.concurrent.ConcurrentHashMap.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.concurrentHashMapInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.concurrentHashMapInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.Map> r8 = java.util.Map.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.mapInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.mapInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.util.Collection> r8 = java.util.Collection.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.collectionInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.collectionInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x0575:
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryInstance     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0617
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.io.File> r8 = java.io.File.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.fileInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.fileInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.io.InputStream> r8 = java.io.InputStream.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            r0.fileStreamInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.fileStreamInstanceCount     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.fileStreamInstanceCount     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r12 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r12 = r12.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r13 = android.os.Debug.class
            java.lang.Object[] r14 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.io.OutputStream> r17 = java.io.OutputStream.class
            r14[r10] = r17     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r12 = r12.invoke(r13, r14)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r12 = (java.lang.Long) r12     // Catch:{ Throwable -> 0x07fc }
            long r12 = r12.longValue()     // Catch:{ Throwable -> 0x07fc }
            r14 = 0
            long r7 = r7 + r12
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.fileStreamInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.lang.Readable> r8 = java.lang.Readable.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r7 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r7 = r7.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r8 = android.os.Debug.class
            java.lang.Object[] r12 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<java.lang.Appendable> r13 = java.lang.Appendable.class
            r12[r10] = r13     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r7 = r7.invoke(r8, r12)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r7 = (java.lang.Long) r7     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.longValue()     // Catch:{ Throwable -> 0x07fc }
            r12 = 0
            long r5 = r5 + r7
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.filereadwriteInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.filereadwriteInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x0617:
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryInstance     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x074f
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mGetViewInstanceCount     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x063f
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mGetViewInstanceCount     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.view.ViewDebug> r6 = android.view.ViewDebug.class
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.viewInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.viewInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x063f:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mGetViewRootImplCount     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0663
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mGetViewRootImplCount     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.view.ViewDebug> r6 = android.view.ViewDebug.class
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.viewRootInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.viewRootInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x0663:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0693
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class r0 = r0.mClassContextImpl     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0693
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r8 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class r8 = r8.mClassContextImpl     // Catch:{ Throwable -> 0x07fc }
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.appContextInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.appContextInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x0693:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x06bb
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.app.Activity> r8 = android.app.Activity.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.activityInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.activityInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x06bb:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mGetGlobalAssetCount     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x06df
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mGetGlobalAssetCount     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.content.res.AssetManager> r6 = android.content.res.AssetManager.class
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ Throwable -> 0x07fc }
            int r5 = r5.intValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r6 = r6.globalAssetCount     // Catch:{ Throwable -> 0x07fc }
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            r0.globalAssetCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x06df:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mGetGlobalAssetManagerCount     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0703
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mGetGlobalAssetManagerCount     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.content.res.AssetManager> r6 = android.content.res.AssetManager.class
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ Throwable -> 0x07fc }
            int r5 = r5.intValue()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r6 = r6.globalAssetManagerCount     // Catch:{ Throwable -> 0x07fc }
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            r0.globalAssetManagerCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x0703:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x074f
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class r0 = r0.mClassV4Fragment     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0729
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r5 = android.os.Debug.class
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r7 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class r7 = r7.mClassV4Fragment     // Catch:{ Throwable -> 0x07fc }
            r6[r10] = r7     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r0 = r0.invoke(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Throwable -> 0x07fc }
            long r13 = r0.longValue()     // Catch:{ Throwable -> 0x07fc }
            r15 = r13
            goto L_0x072b
        L_0x0729:
            r15 = 0
        L_0x072b:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r5 = r5.mCountInstancesOfClass     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.os.Debug> r6 = android.os.Debug.class
            java.lang.Object[] r7 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class<android.app.Fragment> r8 = android.app.Fragment.class
            r7[r10] = r8     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07fc }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07fc }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07fc }
            r7 = 0
            long r5 = r5 + r15
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            long r7 = r7.fragmentInstanceCount     // Catch:{ Throwable -> 0x07fc }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07fc }
            r0.fragmentInstanceCount = r5     // Catch:{ Throwable -> 0x07fc }
        L_0x074f:
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceMemoryInstance     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x07cb
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r5 = android.os.Debug.getBinderLocalObjectCount()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r6 = r6.binderLocalObjectCount     // Catch:{ Throwable -> 0x07fc }
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            r0.binderLocalObjectCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r5 = android.os.Debug.getBinderProxyObjectCount()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r6 = r6.binderLocalObjectCount     // Catch:{ Throwable -> 0x07fc }
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            r0.binderProxyObjectCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r5 = android.os.Debug.getBinderDeathObjectCount()     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r6 = r6.binderLocalObjectCount     // Catch:{ Throwable -> 0x07fc }
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            r0.binderDeathObjectCount = r5     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07cb }
            java.lang.reflect.Method r0 = r0.mGetGlobalAllocSize     // Catch:{ Throwable -> 0x07cb }
            if (r0 == 0) goto L_0x07a7
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07cb }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07cb }
            java.lang.reflect.Method r5 = r5.mGetGlobalAllocSize     // Catch:{ Throwable -> 0x07cb }
            java.lang.Class<android.os.Parcel> r6 = android.os.Parcel.class
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07cb }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07cb }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07cb }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07cb }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07cb }
            long r7 = r7.parcelSize     // Catch:{ Throwable -> 0x07cb }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07cb }
            r0.parcelSize = r5     // Catch:{ Throwable -> 0x07cb }
        L_0x07a7:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07cb }
            java.lang.reflect.Method r0 = r0.mGetGlobalAllocCount     // Catch:{ Throwable -> 0x07cb }
            if (r0 == 0) goto L_0x07cb
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07cb }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07cb }
            java.lang.reflect.Method r5 = r5.mGetGlobalAllocCount     // Catch:{ Throwable -> 0x07cb }
            java.lang.Class<android.os.Parcel> r6 = android.os.Parcel.class
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07cb }
            java.lang.Object r5 = r5.invoke(r6, r7)     // Catch:{ Throwable -> 0x07cb }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ Throwable -> 0x07cb }
            long r5 = r5.longValue()     // Catch:{ Throwable -> 0x07cb }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r7 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07cb }
            long r7 = r7.parcelCount     // Catch:{ Throwable -> 0x07cb }
            long r5 = java.lang.Math.max(r5, r7)     // Catch:{ Throwable -> 0x07cb }
            r0.parcelCount = r5     // Catch:{ Throwable -> 0x07cb }
        L_0x07cb:
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0800
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mGetDatabaseInfo     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0800
            if (r11 == 0) goto L_0x0800
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Method r0 = r0.mGetDatabaseInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r5 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.Class r5 = r5.mClassSQLiteDebug     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object[] r6 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x07fc }
            java.lang.Object r0 = r0.invoke(r5, r6)     // Catch:{ Throwable -> 0x07fc }
            if (r0 == 0) goto L_0x0800
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.TraceDetail r6 = r1.mTraceDetail     // Catch:{ Throwable -> 0x07fc }
            java.lang.reflect.Field r6 = r6.mDataBaseMemoryUsed     // Catch:{ Throwable -> 0x07fc }
            int r0 = r6.getInt(r0)     // Catch:{ Throwable -> 0x07fc }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x07fc }
            int r6 = r6.databaseMemory     // Catch:{ Throwable -> 0x07fc }
            int r0 = java.lang.Math.max(r0, r6)     // Catch:{ Throwable -> 0x07fc }
            r5.databaseMemory = r0     // Catch:{ Throwable -> 0x07fc }
            goto L_0x0800
        L_0x07fc:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0800:
            if (r11 == 0) goto L_0x0880
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r5 = r2.getTotalPrivateDirty()
            int r5 = r5 / 1024
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo
            int r6 = r6.totalPrivateDirty
            int r5 = java.lang.Math.max(r5, r6)
            r0.totalPrivateDirty = r5
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r5 = r2.getTotalSharedDirty()
            int r5 = r5 / 1024
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r6 = r1.mActivityRuntimeInfo
            int r6 = r6.totalSharedDirty
            int r5 = java.lang.Math.max(r5, r6)
            r0.totalSharedDirty = r5
            int r0 = sApiLevel
            if (r0 < r4) goto L_0x0880
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r4 = r2.getTotalPrivateClean()
            int r4 = r4 / 1024
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo
            int r5 = r5.totalPrivateClean
            int r4 = java.lang.Math.max(r4, r5)
            r0.totalPrivateClean = r4
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r4 = r2.getTotalSharedClean()
            int r4 = r4 / 1024
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo
            int r5 = r5.totalSharedClean
            int r4 = java.lang.Math.max(r4, r5)
            r0.totalSharedClean = r4
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r4 = r2.getTotalSwappablePss()
            int r4 = r4 / 1024
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo
            int r5 = r5.totalSwappablePss
            int r4 = java.lang.Math.max(r4, r5)
            r0.totalSwappablePss = r4
            java.lang.reflect.Method r0 = r1.mGetTotalUss
            if (r0 == 0) goto L_0x0880
            java.lang.reflect.Method r0 = r1.mGetTotalUss     // Catch:{ Throwable -> 0x087f }
            java.lang.Object[] r4 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x087f }
            java.lang.Object r0 = r0.invoke(r2, r4)     // Catch:{ Throwable -> 0x087f }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x087f }
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x087f }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r4 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x087f }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r5 = r1.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x087f }
            int r5 = r5.totalUss     // Catch:{ Throwable -> 0x087f }
            int r0 = java.lang.Math.max(r5, r0)     // Catch:{ Throwable -> 0x087f }
            r4.totalUss = r0     // Catch:{ Throwable -> 0x087f }
            goto L_0x0880
        L_0x087f:
        L_0x0880:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x08a4
            int r0 = sApiLevel
            if (r0 >= r3) goto L_0x08a4
            if (r11 == 0) goto L_0x08a4
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r4 = r2.getTotalPss()
            r0.summaryTotalpss = r4
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r4 = r2.dalvikPss
            r0.summaryJavaHeap = r4
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r4 = r2.otherPrivateDirty
            r0.summaryPrivateOther = r4
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r2 = r2.nativePss
            r0.summaryNativeHeap = r2
        L_0x08a4:
            long r4 = r1.mAvailMemory
            int r0 = (int) r4
            r1.mRemainAvailMemory = r0
            int r0 = r1.mMaxCanUseJavaMemory
            r4 = 100
            if (r0 <= 0) goto L_0x08bb
            long r6 = r1.mDalvikPss
            long r6 = r6 * r4
            int r0 = r1.mMaxCanUseJavaMemory
            long r8 = (long) r0
            long r6 = r6 / r8
            int r0 = (int) r6
            long r6 = (long) r0
            r1.mJavaUsedMemoryPercent = r6
        L_0x08bb:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            int r2 = r1.mRemainAvailMemory
            r0.maxCanUseTotalMemory = r2
            int r0 = sApiLevel
            if (r0 < r3) goto L_0x0904
            java.lang.String r0 = "art.gc.gc-count"
            java.lang.String r0 = android.os.Debug.getRuntimeStat(r0)
            if (r0 == 0) goto L_0x08db
            int r2 = r0.length()
            if (r2 <= 0) goto L_0x08db
            int r0 = java.lang.Integer.parseInt(r0)
            r1.mTotalGcCount = r0
        L_0x08db:
            java.lang.String r0 = "art.gc.blocking-gc-count"
            java.lang.String r0 = android.os.Debug.getRuntimeStat(r0)
            if (r0 == 0) goto L_0x08ef
            int r2 = r0.length()
            if (r2 <= 0) goto L_0x08ef
            int r0 = java.lang.Integer.parseInt(r0)
            r1.mBlockingGCCount = r0
        L_0x08ef:
            java.lang.String r0 = "art.gc.blocking-gc-time"
            java.lang.String r0 = android.os.Debug.getRuntimeStat(r0)
            if (r0 == 0) goto L_0x090e
            int r2 = r0.length()
            if (r2 <= 0) goto L_0x090e
            long r2 = java.lang.Long.parseLong(r0)
            r1.mTotalBlockingGCTime = r2
            goto L_0x090e
        L_0x0904:
            int r0 = android.os.Debug.getGlobalGcInvocationCount()
            r1.mBlockingGCCount = r0
            int r0 = r1.mBlockingGCCount
            r1.mTotalGcCount = r0
        L_0x090e:
            int r0 = r1.mStartBlockingGCCount
            if (r0 >= 0) goto L_0x091e
            int r0 = r1.mBlockingGCCount
            r1.mStartBlockingGCCount = r0
            long r2 = r1.mTotalBlockingGCTime
            r1.mStartBlockingGCTime = r2
            int r0 = r1.mTotalGcCount
            r1.mStartGcCount = r0
        L_0x091e:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x0a41
            long r2 = r1.mTotalUsedMemory
            long r2 = r2 * r4
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r0 = r0.deviceInfo
            long r6 = r0.deviceTotalAvailMemory
            long r2 = r2 / r6
            int r0 = (int) r2
            short r0 = (short) r0
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r2 = r1.mActivityRuntimeInfo
            short r2 = r2.totalMemPercent
            if (r2 >= r0) goto L_0x0940
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r2 = r1.mActivityRuntimeInfo
            r2.totalMemPercent = r0
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r2 = r1.mRemainAvailMemory
            short r2 = (short) r2
            r0.totalCanUseMemory = r2
        L_0x0940:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.javaMemPercent
            long r2 = (long) r0
            long r6 = r1.mJavaUsedMemoryPercent
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x09a0
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mJavaUsedMemoryPercent
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaMemPercent = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r2 = r1.mMaxCanUseJavaMemory
            long r2 = (long) r2
            long r6 = r1.mDalvikPss
            long r2 = r2 - r6
            int r2 = (int) r2
            short r2 = (short) r2
            r0.totalCanUseJavaMemory = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.totalCanUseJavaMemory
            if (r0 >= 0) goto L_0x0969
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            r0.totalCanUseJavaMemory = r10
        L_0x0969:
            boolean r0 = sIsTraceDetail
            if (r0 == 0) goto L_0x09a0
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikAllocated
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaAllocal = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikHeapSize
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaHeapSize = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikFree
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaHeapFree = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mNativeHeapAllocatedSize
            int r2 = (int) r2
            short r2 = (short) r2
            r0.nativeAllocal = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mNativeHeapSize
            int r2 = (int) r2
            short r2 = (short) r2
            r0.nativeHeapSize = r2
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mNativeHeapSize
            long r6 = r1.mNativeHeapAllocatedSize
            long r2 = r2 - r6
            int r2 = (int) r2
            short r2 = (short) r2
            r0.nativeHeapFree = r2
        L_0x09a0:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.javaStart
            if (r0 != 0) goto L_0x09ae
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaStart = r2
        L_0x09ae:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.memMin
            if (r0 != 0) goto L_0x09bc
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mTotalUsedMemory
            int r2 = (int) r2
            short r2 = (short) r2
            r0.memMin = r2
        L_0x09bc:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.javaMin
            if (r0 != 0) goto L_0x09ca
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaMin = r2
        L_0x09ca:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.nativeMin
            if (r0 != 0) goto L_0x09d8
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mNativeHeapPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.nativeMin = r2
        L_0x09d8:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.memMin
            long r2 = (long) r0
            long r6 = r1.mTotalUsedMemory
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x09eb
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mTotalUsedMemory
            int r2 = (int) r2
            short r2 = (short) r2
            r0.memMin = r2
        L_0x09eb:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.javaMax
            long r2 = (long) r0
            long r6 = r1.mDalvikPss
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x09fe
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaMax = r2
        L_0x09fe:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.javaMin
            long r2 = (long) r0
            long r6 = r1.mDalvikPss
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0a11
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mDalvikPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.javaMin = r2
        L_0x0a11:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.nativeMax
            long r2 = (long) r0
            long r6 = r1.mNativeHeapPss
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x0a24
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mNativeHeapPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.nativeMax = r2
        L_0x0a24:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            short r0 = r0.nativeMin
            long r2 = (long) r0
            long r6 = r1.mNativeHeapPss
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0a37
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            long r2 = r1.mNativeHeapPss
            int r2 = (int) r2
            short r2 = (short) r2
            r0.nativeMin = r2
        L_0x0a37:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r2 = r1.mActivityRuntimeInfo
            int r2 = r2.summaryGraphics
            r0.summaryGraphics = r2
        L_0x0a41:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            int r2 = r1.mBlockingGCCount
            r0.blockingGCCount = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mAvailMemory
            r0.deviceAvailMemory = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            boolean r2 = r1.mIsLowMemroy
            r0.isLowMemroy = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            int r2 = r1.mRemainAvailMemory
            r0.remainAvailMemory = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mTotalBlockingGCTime
            r0.totalBlockingGCTime = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            int r2 = r1.mTotalGcCount
            int r3 = r1.mStartGcCount
            int r2 = r2 - r3
            r0.totalGcCount = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mTotalUsedMemory
            r0.totalUsedMemory = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mTotalUsedMemory
            long r2 = r2 * r4
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r4 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r4 = r4.deviceInfo
            long r4 = r4.deviceTotalAvailMemory
            long r2 = r2 / r4
            r0.totalMemoryPercent = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mJavaUsedMemoryPercent
            r0.totalJavaPercent = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mNativeHeapPss
            int r2 = (int) r2
            long r2 = (long) r2
            r0.nativePss = r2
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r1.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r0 = r0.memroyStat
            long r2 = r1.mDalvikPss
            int r2 = (int) r2
            r0.dalvikPss = r2
            return
        L_0x0aa9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.getMemInfo(boolean):void");
    }

    /* access modifiers changed from: package-private */
    public void evaluatePidPerformance() {
        int evaluatePidScore;
        if (this.mEvaluateScore != null && (evaluatePidScore = this.mEvaluateScore.evaluatePidScore(this)) > 0) {
            this.mMyPidScore = (short) evaluatePidScore;
            this.mOnLineStat.performanceInfo.myPidScore = this.mMyPidScore;
            if (this.mMinPidRunningScore == 0) {
                this.mMinPidRunningScore = this.mMyPidScore;
            }
            this.mTotalPidRunningScore += this.mMyPidScore;
            this.mTotalPidRunningScoreCount++;
            this.mMyPidScoreTestCounter = (short) (this.mMyPidScoreTestCounter + 1);
            this.mMyPidTotalScore = (short) (this.mMyPidTotalScore + this.mMyPidScore);
            short s = this.mMyPidScoreTestCounter;
            if (s <= 0) {
                s = 1;
            }
            this.mAvgMyPidScore = (short) (this.mMyPidTotalScore / s);
            if (this.mMaxPidRunningScore < this.mMyPidScore) {
                this.mMaxPidRunningScore = this.mMyPidScore;
            }
            if (this.mMinPidRunningScore > this.mMyPidScore) {
                this.mMinPidRunningScore = this.mMyPidScore;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void evaluateSystemPerformance() {
        if (this.mEvaluateScore != null) {
            if (this.mDevicesScore == 0) {
                this.mDevicesScore = (short) this.mEvaluateScore.evaluateDeviceScore(this, this.mHardWareInfo);
                this.mOnLineStat.performanceInfo.deviceScore = this.mDevicesScore;
            }
            int evaluateSystemScore = this.mEvaluateScore.evaluateSystemScore(this);
            if (evaluateSystemScore > 0) {
                this.mSystemRunningScore = (short) evaluateSystemScore;
                this.mTotalSysRunningScore += this.mSystemRunningScore;
                this.mTotalSysRunningScoreCount++;
                if (this.mFirstSystemRunningScore == 0) {
                    this.mFirstSystemRunningScore = this.mSystemRunningScore;
                    this.mMinSystemRunningScore = this.mSystemRunningScore;
                    this.mMaxSystemRunningScore = this.mSystemRunningScore;
                }
                this.mOnLineStat.performanceInfo.systemRunningScore = this.mSystemRunningScore;
                this.mSysScoreTestCounter = (short) (this.mSysScoreTestCounter + 1);
                this.mSystemRunningTotalScore = (short) (this.mSystemRunningTotalScore + this.mSystemRunningScore);
                this.mAvgSystemRunningScore = (short) (this.mSystemRunningTotalScore / this.mSysScoreTestCounter);
                if (this.mMaxSystemRunningScore < this.mSystemRunningScore) {
                    this.mMaxSystemRunningScore = this.mSystemRunningScore;
                }
                if (this.mMinSystemRunningScore > this.mSystemRunningScore) {
                    this.mMinSystemRunningScore = this.mSystemRunningScore;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x00fa */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getThreadIoWaitAndLoadAvg(int r5, float[] r6) {
        /*
            r4 = this;
            boolean r0 = r4.mFileSchedIsNotExists     // Catch:{ Exception -> 0x00fe }
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x00fe }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00fe }
            r1.<init>()     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "/proc/"
            r1.append(r2)     // Catch:{ Exception -> 0x00fe }
            int r2 = r4.mMyPid     // Catch:{ Exception -> 0x00fe }
            r1.append(r2)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "/task/"
            r1.append(r2)     // Catch:{ Exception -> 0x00fe }
            r1.append(r5)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r5 = "/sched"
            r1.append(r5)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r5 = r1.toString()     // Catch:{ Exception -> 0x00fe }
            r0.<init>(r5)     // Catch:{ Exception -> 0x00fe }
            boolean r5 = r0.exists()     // Catch:{ Exception -> 0x00fe }
            if (r5 != 0) goto L_0x0031
            return
        L_0x0031:
            java.io.FileReader r5 = new java.io.FileReader     // Catch:{ Exception -> 0x00fe }
            r5.<init>(r0)     // Catch:{ Exception -> 0x00fe }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00fe }
            r0.<init>(r5)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r5 = r0.readLine()     // Catch:{ Throwable -> 0x00fa }
        L_0x003f:
            if (r5 == 0) goto L_0x00fa
            java.lang.String r1 = ".wait_sum"
            boolean r1 = r5.contains(r1)     // Catch:{ Throwable -> 0x00fa }
            r2 = 32
            if (r1 == 0) goto L_0x0062
            int r1 = r5.lastIndexOf(r2)     // Catch:{ Throwable -> 0x00fa }
            if (r1 <= 0) goto L_0x00f4
            int r1 = r1 + 1
            java.lang.String r5 = r5.substring(r1)     // Catch:{ Throwable -> 0x00fa }
            r1 = 0
            float r5 = java.lang.Float.parseFloat(r5)     // Catch:{ Throwable -> 0x00fa }
            int r5 = (int) r5     // Catch:{ Throwable -> 0x00fa }
            float r5 = (float) r5     // Catch:{ Throwable -> 0x00fa }
            r6[r1] = r5     // Catch:{ Throwable -> 0x00fa }
            goto L_0x00f4
        L_0x0062:
            java.lang.String r1 = ".wait_max"
            boolean r1 = r5.contains(r1)     // Catch:{ Throwable -> 0x00fa }
            r3 = 1
            if (r1 == 0) goto L_0x0081
            int r1 = r5.lastIndexOf(r2)     // Catch:{ Throwable -> 0x00fa }
            if (r1 <= 0) goto L_0x00f4
            int r1 = r1 + 1
            java.lang.String r5 = r5.substring(r1)     // Catch:{ Throwable -> 0x00fa }
            float r5 = java.lang.Float.parseFloat(r5)     // Catch:{ Throwable -> 0x00fa }
            int r5 = (int) r5     // Catch:{ Throwable -> 0x00fa }
            float r5 = (float) r5     // Catch:{ Throwable -> 0x00fa }
            r6[r3] = r5     // Catch:{ Throwable -> 0x00fa }
            goto L_0x00f4
        L_0x0081:
            java.lang.String r1 = ".wait_count"
            boolean r1 = r5.contains(r1)     // Catch:{ Throwable -> 0x00fa }
            if (r1 == 0) goto L_0x009e
            int r1 = r5.lastIndexOf(r2)     // Catch:{ Throwable -> 0x00fa }
            if (r1 <= 0) goto L_0x00f4
            int r1 = r1 + 1
            java.lang.String r5 = r5.substring(r1)     // Catch:{ Throwable -> 0x00fa }
            r1 = 3
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x00fa }
            float r5 = (float) r5     // Catch:{ Throwable -> 0x00fa }
            r6[r1] = r5     // Catch:{ Throwable -> 0x00fa }
            goto L_0x00f4
        L_0x009e:
            java.lang.String r1 = "iowait_sum"
            boolean r1 = r5.contains(r1)     // Catch:{ Throwable -> 0x00fa }
            if (r1 == 0) goto L_0x00bc
            int r1 = r5.lastIndexOf(r2)     // Catch:{ Throwable -> 0x00fa }
            if (r1 <= 0) goto L_0x00f4
            int r1 = r1 + 1
            java.lang.String r5 = r5.substring(r1)     // Catch:{ Throwable -> 0x00fa }
            r1 = 4
            float r5 = java.lang.Float.parseFloat(r5)     // Catch:{ Throwable -> 0x00fa }
            int r5 = (int) r5     // Catch:{ Throwable -> 0x00fa }
            float r5 = (float) r5     // Catch:{ Throwable -> 0x00fa }
            r6[r1] = r5     // Catch:{ Throwable -> 0x00fa }
            goto L_0x00f4
        L_0x00bc:
            java.lang.String r1 = "iowait_count"
            boolean r1 = r5.contains(r1)     // Catch:{ Throwable -> 0x00fa }
            if (r1 == 0) goto L_0x00d9
            int r1 = r5.lastIndexOf(r2)     // Catch:{ Throwable -> 0x00fa }
            if (r1 <= 0) goto L_0x00f4
            int r1 = r1 + 1
            java.lang.String r5 = r5.substring(r1)     // Catch:{ Throwable -> 0x00fa }
            r1 = 5
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x00fa }
            float r5 = (float) r5     // Catch:{ Throwable -> 0x00fa }
            r6[r1] = r5     // Catch:{ Throwable -> 0x00fa }
            goto L_0x00f4
        L_0x00d9:
            java.lang.String r1 = "avg_per_cpu"
            boolean r1 = r5.contains(r1)     // Catch:{ Throwable -> 0x00fa }
            if (r1 == 0) goto L_0x00f4
            int r1 = r5.lastIndexOf(r2)     // Catch:{ Throwable -> 0x00fa }
            if (r1 <= 0) goto L_0x00fa
            int r1 = r1 + r3
            java.lang.String r5 = r5.substring(r1)     // Catch:{ Throwable -> 0x00fa }
            r1 = 6
            float r5 = java.lang.Float.parseFloat(r5)     // Catch:{ Throwable -> 0x00fa }
            r6[r1] = r5     // Catch:{ Throwable -> 0x00fa }
            goto L_0x00fa
        L_0x00f4:
            java.lang.String r5 = r0.readLine()     // Catch:{ Throwable -> 0x00fa }
            goto L_0x003f
        L_0x00fa:
            r0.close()     // Catch:{ Exception -> 0x00fe }
            goto L_0x0102
        L_0x00fe:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0102:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.getThreadIoWaitAndLoadAvg(int, float[]):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x0111 */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0118 A[Catch:{ Exception -> 0x0135 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getIoWaitAndLoadAvg() {
        /*
            r5 = this;
            boolean r0 = r5.mFileSchedIsNotExists     // Catch:{ Exception -> 0x0135 }
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x0135 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0135 }
            r1.<init>()     // Catch:{ Exception -> 0x0135 }
            java.lang.String r2 = "/proc/"
            r1.append(r2)     // Catch:{ Exception -> 0x0135 }
            int r2 = r5.mMyPid     // Catch:{ Exception -> 0x0135 }
            r1.append(r2)     // Catch:{ Exception -> 0x0135 }
            java.lang.String r2 = "/sched"
            r1.append(r2)     // Catch:{ Exception -> 0x0135 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0135 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x0135 }
            boolean r1 = r0.exists()     // Catch:{ Exception -> 0x0135 }
            r2 = 1
            if (r1 != 0) goto L_0x002c
            r5.mFileSchedIsNotExists = r2     // Catch:{ Exception -> 0x0135 }
            return
        L_0x002c:
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ Exception -> 0x0135 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0135 }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0135 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x0135 }
            java.lang.String r1 = r0.readLine()     // Catch:{ Throwable -> 0x0111 }
            float r3 = r5.mPidWaitSum     // Catch:{ Throwable -> 0x0111 }
            r5.mPidOldWaitSum = r3     // Catch:{ Throwable -> 0x0111 }
            int r3 = r5.mPidWaitCount     // Catch:{ Throwable -> 0x0111 }
            r5.mPidOldWaitCount = r3     // Catch:{ Throwable -> 0x0111 }
        L_0x0042:
            if (r1 == 0) goto L_0x0111
            java.lang.String r3 = ".wait_sum"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            r4 = 32
            if (r3 == 0) goto L_0x0064
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x010b
            int r3 = r3 + 1
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Throwable -> 0x0111 }
            int r1 = (int) r1     // Catch:{ Throwable -> 0x0111 }
            float r1 = (float) r1     // Catch:{ Throwable -> 0x0111 }
            r5.mPidWaitSum = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x010b
        L_0x0064:
            java.lang.String r3 = ".sum_exec_runtime"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            if (r3 == 0) goto L_0x0082
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x010b
            int r3 = r3 + 1
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Throwable -> 0x0111 }
            int r1 = (int) r1     // Catch:{ Throwable -> 0x0111 }
            float r1 = (float) r1     // Catch:{ Throwable -> 0x0111 }
            r5.mPidExeRunTime = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x010b
        L_0x0082:
            java.lang.String r3 = ".wait_max"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            if (r3 == 0) goto L_0x009f
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x010b
            int r3 = r3 + 1
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Throwable -> 0x0111 }
            int r1 = (int) r1     // Catch:{ Throwable -> 0x0111 }
            float r1 = (float) r1     // Catch:{ Throwable -> 0x0111 }
            r5.mPidWaitMax = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x010b
        L_0x009f:
            java.lang.String r3 = ".wait_count"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            if (r3 == 0) goto L_0x00ba
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x010b
            int r3 = r3 + 1
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ Throwable -> 0x0111 }
            r5.mPidWaitCount = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x010b
        L_0x00ba:
            java.lang.String r3 = "iowait_sum"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            if (r3 == 0) goto L_0x00d6
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x010b
            int r3 = r3 + 1
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Throwable -> 0x0111 }
            int r1 = (int) r1     // Catch:{ Throwable -> 0x0111 }
            r5.mPidIoWaitSum = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x010b
        L_0x00d6:
            java.lang.String r3 = "iowait_count"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            if (r3 == 0) goto L_0x00f1
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x010b
            int r3 = r3 + 1
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ Throwable -> 0x0111 }
            r5.mPidIoWaitCount = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x010b
        L_0x00f1:
            java.lang.String r3 = "avg_per_cpu"
            boolean r3 = r1.contains(r3)     // Catch:{ Throwable -> 0x0111 }
            if (r3 == 0) goto L_0x010b
            int r3 = r1.lastIndexOf(r4)     // Catch:{ Throwable -> 0x0111 }
            if (r3 <= 0) goto L_0x0111
            int r3 = r3 + r2
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Throwable -> 0x0111 }
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Throwable -> 0x0111 }
            r5.mPidPerCpuLoad = r1     // Catch:{ Throwable -> 0x0111 }
            goto L_0x0111
        L_0x010b:
            java.lang.String r1 = r0.readLine()     // Catch:{ Throwable -> 0x0111 }
            goto L_0x0042
        L_0x0111:
            r0.close()     // Catch:{ Exception -> 0x0135 }
            int r0 = r5.mPidIoWaitSumInit     // Catch:{ Exception -> 0x0135 }
            if (r0 >= 0) goto L_0x0139
            int r0 = r5.mPidIoWaitSum     // Catch:{ Exception -> 0x0135 }
            r5.mPidIoWaitSumInit = r0     // Catch:{ Exception -> 0x0135 }
            int r0 = r5.mPidIoWaitCount     // Catch:{ Exception -> 0x0135 }
            r5.mPidIoWaitCountInit = r0     // Catch:{ Exception -> 0x0135 }
            float r0 = r5.mPidPerCpuLoad     // Catch:{ Exception -> 0x0135 }
            r5.mPidPerCpuLoadInit = r0     // Catch:{ Exception -> 0x0135 }
            int r0 = r5.mPidIoWaitCount     // Catch:{ Exception -> 0x0135 }
            r5.mPidIoWaitCountLast = r0     // Catch:{ Exception -> 0x0135 }
            float r0 = r5.mPidPerCpuLoad     // Catch:{ Exception -> 0x0135 }
            r5.mPidPerCpuLoadLast = r0     // Catch:{ Exception -> 0x0135 }
            int r0 = r5.mPidIoWaitSum     // Catch:{ Exception -> 0x0135 }
            r5.mPidIoWaitSumOld = r0     // Catch:{ Exception -> 0x0135 }
            int r0 = r5.mPidIoWaitCount     // Catch:{ Exception -> 0x0135 }
            r5.mPidIoWaitCountOld = r0     // Catch:{ Exception -> 0x0135 }
            goto L_0x0139
        L_0x0135:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0139:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.getIoWaitAndLoadAvg():void");
    }

    /* access modifiers changed from: package-private */
    public void getCpuInfo(boolean z, boolean z2, boolean z3) {
        this.mPidIoWaitSumOld = this.mPidIoWaitSum;
        this.mPidIoWaitCountOld = this.mPidIoWaitCount;
        this.mProcessCpuTracker.update();
        if (z) {
            getIoWaitAndLoadAvg();
        }
        this.mSystemLoadAvg = this.mProcessCpuTracker.mLoadAverageData;
        this.mOnLineStat.cpuStat.systemLoadAvg = this.mSystemLoadAvg[0];
        if (sIsTraceDetail && this.mActivityRuntimeInfo != null && this.mActivityRuntimeInfo.maxLoadAvg < this.mSystemLoadAvg[0]) {
            this.mActivityRuntimeInfo.maxLoadAvg = this.mSystemLoadAvg[0];
        }
        if ((this.mInited || sIsTraceDetail) && z2) {
            pidOpenFileCount();
        }
        this.mThreadCount = (int) this.mProcessCpuTracker.mThreadCount;
        if (this.mOldThreadCount <= 0) {
            this.mOldThreadCount = this.mThreadCount;
        }
        this.mOldMajorFault = this.mMajorFault;
        this.mMajorFault = (int) this.mProcessCpuTracker.mMajorFault;
        this.mOnLineStat.performanceInfo.threadCount = this.mThreadCount;
        this.mOnLineStat.memroyStat.majorFault = this.mMajorFault;
        if ((this.mInited || sIsTraceDetail) && z3) {
            getThreadStat();
        }
        if (this.mSysGetCounter == 0) {
            this.mTotalSysCPUPercent = 0;
            this.mTotalMyPidCPUPercent = 0;
            this.mTotalIOWaitTime = 0;
            this.mPidIoWaitSumStart = this.mPidIoWaitSumLast;
            this.mPidIoWaitCountStart = this.mPidIoWaitCountLast;
            this.mPidPerCpuLoadStart = this.mPidPerCpuLoadLast;
            this.mBgCpuTresholdCounter = 0;
        }
        this.mSysCPUPercent = this.mProcessCpuTracker.mTotalSysPercent;
        this.mMyPidCPUPercent = this.mProcessCpuTracker.mMyPidPercent;
        this.mTotalSysCPUPercent += this.mSysCPUPercent;
        this.mTotalMyPidCPUPercent += this.mMyPidCPUPercent;
        this.mTotalIOWaitTime += this.mProcessCpuTracker.mRelIoWaitTime;
        this.mPidIoWaitTotal += this.mPidIoWaitSumOld;
        this.mPidPerCpuLoadTotal += this.mPidPerCpuLoad;
        if (this.mSysGetCounter <= 0) {
            this.mSysAvgCPUPercent = this.mSysCPUPercent;
            this.mMyAvgPidCPUPercent = this.mMyPidCPUPercent;
            this.mAvgIOWaitTime = this.mProcessCpuTracker.mRelIoWaitTime;
            this.mPidIoWaitSumAvg = this.mPidIoWaitSumStart;
            this.mPidPerCpuLoadAvg = this.mPidPerCpuLoadStart;
        } else {
            this.mSysAvgCPUPercent = this.mTotalSysCPUPercent / (this.mSysGetCounter + 1);
            this.mMyAvgPidCPUPercent = this.mTotalMyPidCPUPercent / (this.mSysGetCounter + 1);
            this.mAvgIOWaitTime = this.mTotalIOWaitTime / (this.mSysGetCounter + 1);
            this.mPidIoWaitSumAvg = this.mPidIoWaitTotal / (this.mSysGetCounter + 1);
            this.mPidPerCpuLoadAvg = this.mPidPerCpuLoadTotal / ((float) (this.mSysGetCounter + 1));
        }
        if (this.mProcessCpuTracker.mRelIoWaitTime > 0) {
            this.mIoWiatCount = (short) (this.mIoWiatCount + 1);
        }
        this.mSysGetCounter++;
        if (this.mActivityRuntimeInfo != null && this.mActivityRuntimeInfo.openFile < this.mOpenFileCount) {
            this.mActivityRuntimeInfo.openFile = (short) this.mOpenFileCount;
        }
        this.mOnLineStat.iOStat.currentIOWaitTime = this.mProcessCpuTracker.mRelIoWaitTime;
        this.mOnLineStat.iOStat.avgIOWaitTime = this.mAvgIOWaitTime;
        this.mOnLineStat.cpuStat.sysCPUPercent = this.mSysCPUPercent;
        this.mOnLineStat.cpuStat.sysAvgCPUPercent = this.mSysAvgCPUPercent;
        this.mOnLineStat.cpuStat.myPidCPUPercent = this.mMyPidCPUPercent;
        this.mOnLineStat.cpuStat.myAVGPidCPUPercent = this.mMyAvgPidCPUPercent;
        this.mOnLineStat.cpuStat.iOWaitTimeAvg = this.mPidIoWaitSumAvg;
        this.mOnLineStat.cpuStat.pidIoWaitCount = this.mPidIoWaitCount - this.mPidIoWaitCountStart;
        this.mOnLineStat.cpuStat.pidPerCpuLoadAvg = this.mPidPerCpuLoadAvg;
        this.mOnLineStat.cpuStat.pidWaitSum = this.mPidWaitSum;
        this.mOnLineStat.cpuStat.pidWaitMax = this.mPidWaitMax;
        this.mOnLineStat.cpuStat.pidWaitCount = this.mPidWaitCount;
        if (this.mOnLineStat.cpuStat.myMaxPidCPUPercent < this.mMyPidCPUPercent) {
            this.mOnLineStat.cpuStat.myMaxPidCPUPercent = this.mMyPidCPUPercent;
        }
        if (this.mOnLineStat.cpuStat.sysMaxCPUPercent < this.mSysCPUPercent) {
            this.mOnLineStat.cpuStat.sysMaxCPUPercent = this.mSysCPUPercent;
        }
        if (this.mIsInBackGround) {
            if (this.mMyPidCPUPercent >= OnLineMonitorApp.sBgCpuUseTreshold) {
                this.mBgCpuTresholdCounter++;
            }
            if (this.mBgCpuTresholdCounter >= OnLineMonitorApp.sBgCpuUseTresholdTimes) {
                this.mOnLineStat.cpuStat.cpuAlarmInBg = true;
            }
        }
        this.mOldThreadCount = this.mThreadCount;
        this.mOnLineStat.performanceInfo.runTimeThreadCount = this.mRuntimeThreadCount;
        this.mOnLineStat.performanceInfo.runningThreadCount = this.mRunningThreadCount;
        this.mMaxThreadCount = Math.max(this.mThreadCount, this.mMaxThreadCount);
        this.mMaxRuntimeThreadCount = Math.max(this.mRuntimeThreadCount, this.mMaxRuntimeThreadCount);
        this.mMaxRunningThreadCount = Math.max(this.mRunningThreadCount, this.mMaxRunningThreadCount);
        if (this.mIsInBackGround && this.mResourceUsedInfoCalBgApp != null) {
            int i = (int) (this.mProcessCpuTracker.mPidJiffyTime - ((long) this.mResourceUsedInfoCalBgApp.threadStart));
            int i2 = (int) (this.mProcessCpuTracker.mSystemRunCpuTime - ((long) this.mResourceUsedInfoCalBgApp.threadMax));
            int i3 = (int) (this.mProcessCpuTracker.mSystemTotalCpuTime - ((long) this.mResourceUsedInfoCalBgApp.threadEnd));
            if (i2 > 0 && i3 > 0) {
                int i4 = i * 100;
                int i5 = i4 / i2;
                int i6 = i4 / i3;
                if (this.mResourceUsedInfoCalBgApp.memStart < i5) {
                    this.mResourceUsedInfoCalBgApp.memStart = i5;
                }
                if (this.mResourceUsedInfoCalBgApp.memEnd < i6) {
                    this.mResourceUsedInfoCalBgApp.memEnd = i6;
                }
                this.mResourceUsedInfoCalBgApp.threadStart = (int) this.mProcessCpuTracker.mPidJiffyTime;
                this.mResourceUsedInfoCalBgApp.threadEnd = (int) this.mProcessCpuTracker.mSystemTotalCpuTime;
                this.mResourceUsedInfoCalBgApp.threadMax = (int) this.mProcessCpuTracker.mSystemRunCpuTime;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkToStopPerformance(long j) {
        if ((this.mSmoothCalculate.mLastTouchTime == 0 && j - this.mLoadTimeCalculate.mLoadStartTime > 15000) || (this.mSmoothCalculate.mLastTouchTime > 0 && j - this.mSmoothCalculate.mLastTouchTime > 15000)) {
            this.mThreadHandler.removeMessages(2);
            if (sIsTraceDetail && TraceDetail.sTraceThread) {
                this.mThreadHandler.removeMessages(12);
            }
            this.mOnLineStat.isSystemIdle = true;
            this.mIsCheckPerfromanceRunning = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void onBootEnd(long j, long j2) {
        this.mBootUsedTime = (int) j2;
        this.mBootEndTime = j;
        this.mIsBootEndActivity = true;
        if (this.mThreadHandler != null) {
            recordBootResource(1, true);
            this.mThreadHandler.sendMessage(Message.obtain(this.mThreadHandler, 18, 1, 0));
            this.mThreadHandler.sendEmptyMessageDelayed(13, 8000);
        }
        notifyBootAccurateFinished(0);
        if (OnLineMonitorApp.sIsStartMethodTrace) {
            OnLineMonitorApp.sIsStartMethodTrace = false;
            Debug.stopMethodTracing();
        }
    }

    /* JADX WARNING: type inference failed for: r0v31, types: [int] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0382 A[Catch:{ Throwable -> 0x03b4, Throwable -> 0x03c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0395 A[Catch:{ Throwable -> 0x03b4, Throwable -> 0x03c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x050b  */
    /* JADX WARNING: Removed duplicated region for block: B:210:0x050f A[Catch:{ Throwable -> 0x0569 }] */
    /* JADX WARNING: Removed duplicated region for block: B:234:0x056d A[SYNTHETIC, Splitter:B:234:0x056d] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0201 A[Catch:{ Throwable -> 0x05e2 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getThreadStat() {
        /*
            r67 = this;
            r1 = r67
            java.lang.reflect.Method r0 = r1.mThreadStats
            if (r0 == 0) goto L_0x05e6
            long r2 = com.taobao.onlinemonitor.OnLineMonitorApp.sLaunchTime     // Catch:{ Throwable -> 0x05e2 }
            java.lang.reflect.Method r0 = r1.mThreadStats     // Catch:{ Throwable -> 0x05e2 }
            r4 = 0
            r5 = 0
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Object r0 = r0.invoke(r4, r6)     // Catch:{ Throwable -> 0x05e2 }
            byte[] r0 = (byte[]) r0     // Catch:{ Throwable -> 0x05e2 }
            byte[] r0 = (byte[]) r0     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x05e6
            java.nio.ByteBuffer r4 = java.nio.ByteBuffer.wrap(r0)     // Catch:{ Throwable -> 0x05e2 }
            byte r0 = r4.get()     // Catch:{ Throwable -> 0x05e2 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte r6 = r4.get()     // Catch:{ Throwable -> 0x05e2 }
            r6 = r6 & 255(0xff, float:3.57E-43)
            short r7 = r4.getShort()     // Catch:{ Throwable -> 0x05e2 }
            r1.mRuntimeThreadCount = r7     // Catch:{ Throwable -> 0x05e2 }
            r1.mRunningThreadCount = r5     // Catch:{ Throwable -> 0x05e2 }
            int r0 = r0 + -4
        L_0x0032:
            int r8 = r0 + -1
            if (r0 <= 0) goto L_0x003b
            r4.get()     // Catch:{ Throwable -> 0x05e2 }
            r0 = r8
            goto L_0x0032
        L_0x003b:
            int r8 = r6 + -18
            java.lang.StringBuffer r9 = new java.lang.StringBuffer     // Catch:{ Throwable -> 0x05e2 }
            r10 = 30
            r9.<init>(r10)     // Catch:{ Throwable -> 0x05e2 }
            long r11 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x05e2 }
            r13 = 1000000(0xf4240, double:4.940656E-318)
            long r11 = r11 / r13
            boolean r0 = r1.mIsInBackGround     // Catch:{ Throwable -> 0x05e2 }
            r15 = 1
            if (r0 == 0) goto L_0x005e
            long r13 = r1.mUIHiddenTime     // Catch:{ Throwable -> 0x05e2 }
            r0 = 0
            long r13 = r11 - r13
            r18 = 10000(0x2710, double:4.9407E-320)
            int r0 = (r13 > r18 ? 1 : (r13 == r18 ? 0 : -1))
            if (r0 <= 0) goto L_0x005e
            r13 = 1
            goto L_0x005f
        L_0x005e:
            r13 = 0
        L_0x005f:
            r0 = 0
            long r2 = r11 - r2
            boolean r0 = sIsTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            r42 = 0
            if (r0 == 0) goto L_0x01f2
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysCpuPercentRecords     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x01f2
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysCpuPercentRecords     // Catch:{ Throwable -> 0x05e2 }
            int r0 = r0.size()     // Catch:{ Throwable -> 0x05e2 }
            r14 = 3000(0xbb8, float:4.204E-42)
            if (r0 >= r14) goto L_0x01f2
            float r0 = (float) r2     // Catch:{ Throwable -> 0x05e2 }
            r2 = 1148846080(0x447a0000, float:1000.0)
            float r0 = r0 / r2
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            r2.mLastCheckTime = r11     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            r2.update()     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.mTotalSysPercent     // Catch:{ Throwable -> 0x05e2 }
            if (r2 >= 0) goto L_0x0093
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.mMyPidPercent     // Catch:{ Throwable -> 0x05e2 }
            if (r2 < 0) goto L_0x01f2
        L_0x0093:
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.mTotalSysPercent     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r3 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.mMyPidPercent     // Catch:{ Throwable -> 0x05e2 }
            if (r2 < r3) goto L_0x01f2
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysCpuPercentRecords     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r3 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r3 = r3.mSysCpuPercentRecords     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.size()     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r14 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r14 = r14.mTotalSysPercent     // Catch:{ Throwable -> 0x05e2 }
            r2.put(r3, r14)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mPidCpuPercentRecords     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r3 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r3 = r3.mPidCpuPercentRecords     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.size()     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r14 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r14 = r14.mMyPidPercent     // Catch:{ Throwable -> 0x05e2 }
            r2.put(r3, r14)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            java.util.ArrayList<java.lang.Float> r2 = r2.mCpuPercentTimestamps     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Float r0 = java.lang.Float.valueOf(r0)     // Catch:{ Throwable -> 0x05e2 }
            r2.add(r0)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            java.util.List<java.lang.Long> r0 = r0.mMajorFaults     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            long r2 = r2.mMajorFault     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ Throwable -> 0x05e2 }
            r0.add(r2)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysIoWaitPercent     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysIoWaitPercent     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r3 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.mTotalIoWaitPercent     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysIoWaitCounts     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysIoWaitCounts     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mPidIoWaitCount     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysIoWaitSums     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysIoWaitSums     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mPidIoWaitSum     // Catch:{ Throwable -> 0x05e2 }
            if (r3 <= 0) goto L_0x0126
            int r3 = r1.mPidIoWaitSum     // Catch:{ Throwable -> 0x05e2 }
            r45 = r11
            goto L_0x012d
        L_0x0126:
            com.taobao.onlinemonitor.ProcessCpuTracker r3 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            r45 = r11
            long r10 = r3.mIoWaitTime     // Catch:{ Throwable -> 0x05e2 }
            int r3 = (int) r10     // Catch:{ Throwable -> 0x05e2 }
        L_0x012d:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysSchedWaitSums     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysSchedWaitSums     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            float r3 = r1.mPidWaitSum     // Catch:{ Throwable -> 0x05e2 }
            int r3 = (int) r3     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysSchedWaitCounts     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysSchedWaitCounts     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mPidWaitCount     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mMemoryLevels     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mMemoryLevels     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mTrimMemoryLevel     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            java.util.ArrayList<java.lang.Float> r0 = r0.mPerCpuLoads     // Catch:{ Throwable -> 0x05e2 }
            float r2 = r1.mPidPerCpuLoad     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Throwable -> 0x05e2 }
            r0.add(r2)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            java.util.ArrayList<java.lang.Float> r0 = r0.mSysLoads1Min     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            float[] r2 = r2.mLoadAverageData     // Catch:{ Throwable -> 0x05e2 }
            r2 = r2[r5]     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Throwable -> 0x05e2 }
            r0.add(r2)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            java.util.ArrayList<java.lang.Float> r0 = r0.mSysLoads5Min     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            float[] r2 = r2.mLoadAverageData     // Catch:{ Throwable -> 0x05e2 }
            r2 = r2[r15]     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Throwable -> 0x05e2 }
            r0.add(r2)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mRunningSysScores     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mRunningSysScores     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            short r3 = r1.mSystemRunningScore     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Short r3 = java.lang.Short.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            short r3 = r3.shortValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mRunningPidScores     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mRunningPidScores     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            short r3 = r1.mMyPidScore     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Short r3 = java.lang.Short.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            short r3 = r3.shortValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r0 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            long r2 = r0.mPidRunCpuTimeInterval     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.ProcessCpuTracker r0 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x05e2 }
            long r10 = r0.mSystemTotalCpuTimeInterval     // Catch:{ Throwable -> 0x05e2 }
            r47 = r10
            r10 = r2
            r2 = 1
            goto L_0x01f9
        L_0x01f2:
            r45 = r11
            r10 = r42
            r47 = r10
            r2 = 0
        L_0x01f9:
            r3 = 6
            long[] r12 = new long[r3]     // Catch:{ Throwable -> 0x05e2 }
            java.lang.String[] r14 = new java.lang.String[r15]     // Catch:{ Throwable -> 0x05e2 }
            r3 = 0
        L_0x01ff:
            if (r3 >= r7) goto L_0x058e
            int r5 = r4.getInt()     // Catch:{ Throwable -> 0x05e2 }
            byte r15 = r4.get()     // Catch:{ Throwable -> 0x05e2 }
            r49 = r7
            int r7 = r4.getInt()     // Catch:{ Throwable -> 0x05e2 }
            r0 = 5
            r50 = r8
            r8 = 1
            if (r15 == r8) goto L_0x021a
            if (r15 < r0) goto L_0x021f
            r0 = 7
            if (r15 > r0) goto L_0x021f
        L_0x021a:
            int r0 = r1.mRunningThreadCount     // Catch:{ Throwable -> 0x05e2 }
            int r0 = r0 + r8
            r1.mRunningThreadCount = r0     // Catch:{ Throwable -> 0x05e2 }
        L_0x021f:
            int r8 = r4.getInt()     // Catch:{ Throwable -> 0x05e2 }
            r51 = r2
            int r2 = r4.getInt()     // Catch:{ Throwable -> 0x05e2 }
            r52 = r3
            r3 = 18
            if (r6 < r3) goto L_0x023b
            byte r0 = r4.get()     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x0237
            r0 = 1
            goto L_0x0238
        L_0x0237:
            r0 = 0
        L_0x0238:
            r27 = r0
            goto L_0x023d
        L_0x023b:
            r27 = 0
        L_0x023d:
            boolean r0 = sIsTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x024b
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceThread     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x024b
            int r0 = sApiLevel     // Catch:{ Throwable -> 0x05e2 }
            r3 = 18
            if (r0 >= r3) goto L_0x025b
        L_0x024b:
            boolean r0 = r1.mIsInBackGround     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x0555
            short r0 = r1.mDevicesScore     // Catch:{ Throwable -> 0x05e2 }
            r3 = 60
            if (r0 < r3) goto L_0x0555
            int r0 = sApiLevel     // Catch:{ Throwable -> 0x05e2 }
            r3 = 19
            if (r0 < r3) goto L_0x0555
        L_0x025b:
            int r3 = android.os.Process.getThreadPriority(r7)     // Catch:{ Throwable -> 0x0555 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x0555 }
            r53 = r6
            short r6 = r0.mCheckThreadCount     // Catch:{ Throwable -> 0x0552 }
            r18 = 1
            int r6 = r6 + 1
            short r6 = (short) r6     // Catch:{ Throwable -> 0x0552 }
            r0.mCheckThreadCount = r6     // Catch:{ Throwable -> 0x0552 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0552 }
            r0.<init>()     // Catch:{ Throwable -> 0x0552 }
            r0.append(r5)     // Catch:{ Throwable -> 0x0552 }
            java.lang.String r6 = "@"
            r0.append(r6)     // Catch:{ Throwable -> 0x0552 }
            r0.append(r7)     // Catch:{ Throwable -> 0x0552 }
            java.lang.String r6 = r0.toString()     // Catch:{ Throwable -> 0x0552 }
            java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r0 = r1.mThreadInfoHashMap     // Catch:{ Throwable -> 0x0552 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ Throwable -> 0x0552 }
            r54 = r4
            r4 = r0
            com.taobao.onlinemonitor.ThreadInfo r4 = (com.taobao.onlinemonitor.ThreadInfo) r4     // Catch:{ Throwable -> 0x0559 }
            if (r4 == 0) goto L_0x03dd
            long r0 = (long) r8
            r55 = r9
            long r8 = (long) r2
            r18 = r4
            r19 = r15
            r20 = r0
            r22 = r8
            r24 = r45
            r26 = r10
            r28 = r47
            r30 = r3
            r31 = r13
            r18.updateThread(r19, r20, r22, r24, r26, r28, r30, r31)     // Catch:{ Throwable -> 0x03d6 }
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceThreadWait     // Catch:{ Throwable -> 0x03d6 }
            if (r0 == 0) goto L_0x0343
            r1 = r67
            boolean r0 = r1.mFileSchedIsNotExists     // Catch:{ Throwable -> 0x03d8 }
            if (r0 != 0) goto L_0x0345
            r0 = 7
            float[] r0 = new float[r0]     // Catch:{ Throwable -> 0x03d8 }
            r1.getThreadIoWaitAndLoadAvg(r7, r0)     // Catch:{ Throwable -> 0x03d8 }
            r2 = 0
            r3 = r0[r2]     // Catch:{ Throwable -> 0x03d8 }
            int r2 = (int) r3     // Catch:{ Throwable -> 0x03d8 }
            int r3 = r4.mLastSchedWaitSum     // Catch:{ Throwable -> 0x03d8 }
            int r2 = r2 - r3
            r3 = 3
            r6 = r0[r3]     // Catch:{ Throwable -> 0x03d8 }
            int r6 = (int) r6     // Catch:{ Throwable -> 0x03d8 }
            int r7 = r4.mLastSchedWaitCount     // Catch:{ Throwable -> 0x03d8 }
            int r6 = r6 - r7
            if (r2 > 0) goto L_0x02cd
            if (r6 <= 0) goto L_0x02c9
            goto L_0x02cd
        L_0x02c9:
            r2 = 5
            r9 = 30
            goto L_0x031a
        L_0x02cd:
            int r7 = r1.mPidWaitCount     // Catch:{ Throwable -> 0x03d8 }
            int r8 = r4.mPidLastSchedWaitCount     // Catch:{ Throwable -> 0x03d8 }
            int r7 = r7 - r8
            float r8 = r1.mPidWaitSum     // Catch:{ Throwable -> 0x03d8 }
            int r8 = (int) r8     // Catch:{ Throwable -> 0x03d8 }
            int r9 = r4.mPidLastSchedWaitSum     // Catch:{ Throwable -> 0x03d8 }
            int r8 = r8 - r9
            int r9 = r7 * 10
            if (r6 > r9) goto L_0x0315
            r9 = 100
            if (r2 <= r9) goto L_0x02e3
            if (r2 < r8) goto L_0x02e3
            goto L_0x0315
        L_0x02e3:
            int r9 = r7 * 7
            if (r6 > r9) goto L_0x0303
            r9 = 60
            if (r2 <= r9) goto L_0x02f0
            int r9 = r8 / 2
            if (r2 < r9) goto L_0x02f0
            goto L_0x0303
        L_0x02f0:
            int r7 = r7 * 4
            if (r6 > r7) goto L_0x02fd
            r9 = 30
            if (r2 <= r9) goto L_0x0319
            int r8 = r8 / 3
            if (r2 < r8) goto L_0x0319
            goto L_0x02ff
        L_0x02fd:
            r9 = 30
        L_0x02ff:
            r2 = 1
            r4.mTooMuchLock = r2     // Catch:{ Throwable -> 0x0309 }
            goto L_0x0319
        L_0x0303:
            r9 = 30
            r2 = 2
            r4.mTooMuchLock = r2     // Catch:{ Throwable -> 0x0309 }
            goto L_0x0319
        L_0x0309:
            r56 = r10
            r61 = r12
            r60 = r13
            r64 = r14
            r59 = r55
            goto L_0x0563
        L_0x0315:
            r9 = 30
            r4.mTooMuchLock = r3     // Catch:{ Throwable -> 0x03da }
        L_0x0319:
            r2 = 5
        L_0x031a:
            r2 = r0[r2]     // Catch:{ Throwable -> 0x03da }
            int r2 = (int) r2     // Catch:{ Throwable -> 0x03da }
            r4.mLastIoWaitCount = r2     // Catch:{ Throwable -> 0x03da }
            r2 = 4
            r2 = r0[r2]     // Catch:{ Throwable -> 0x03da }
            int r2 = (int) r2     // Catch:{ Throwable -> 0x03da }
            r4.mLastIoWaitTime = r2     // Catch:{ Throwable -> 0x03da }
            r2 = r0[r3]     // Catch:{ Throwable -> 0x03da }
            int r2 = (int) r2     // Catch:{ Throwable -> 0x03da }
            r4.mLastSchedWaitCount = r2     // Catch:{ Throwable -> 0x03da }
            r2 = 0
            r3 = r0[r2]     // Catch:{ Throwable -> 0x03da }
            int r2 = (int) r3     // Catch:{ Throwable -> 0x03da }
            r4.mLastSchedWaitSum = r2     // Catch:{ Throwable -> 0x03da }
            r2 = 1
            r3 = r0[r2]     // Catch:{ Throwable -> 0x03da }
            int r2 = (int) r3     // Catch:{ Throwable -> 0x03da }
            r4.mLastSchedWaitMax = r2     // Catch:{ Throwable -> 0x03da }
            float r2 = r4.mMaxAvgPerCpu     // Catch:{ Throwable -> 0x03da }
            r44 = 6
            r0 = r0[r44]     // Catch:{ Throwable -> 0x03c6 }
            float r0 = java.lang.Math.max(r2, r0)     // Catch:{ Throwable -> 0x03c6 }
            r4.mMaxAvgPerCpu = r0     // Catch:{ Throwable -> 0x03c6 }
            goto L_0x0349
        L_0x0343:
            r1 = r67
        L_0x0345:
            r9 = 30
            r44 = 6
        L_0x0349:
            com.taobao.onlinemonitor.ProcessCpuTracker r0 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x03c6 }
            long r2 = r0.mSystemTotalCpuTime     // Catch:{ Throwable -> 0x03c6 }
            r4.mDeviceLastTotalTime = r2     // Catch:{ Throwable -> 0x03c6 }
            com.taobao.onlinemonitor.ProcessCpuTracker r0 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x03c6 }
            long r2 = r0.mSystemRunCpuTime     // Catch:{ Throwable -> 0x03c6 }
            r4.mPidLastTotalTime = r2     // Catch:{ Throwable -> 0x03c6 }
            int r0 = r1.mPidIoWaitCount     // Catch:{ Throwable -> 0x03c6 }
            r4.mPidLastIoWaitCount = r0     // Catch:{ Throwable -> 0x03c6 }
            int r0 = r1.mPidIoWaitSum     // Catch:{ Throwable -> 0x03c6 }
            r4.mPidLastIoWaitTime = r0     // Catch:{ Throwable -> 0x03c6 }
            int r0 = r1.mPidWaitCount     // Catch:{ Throwable -> 0x03c6 }
            r4.mPidLastSchedWaitCount = r0     // Catch:{ Throwable -> 0x03c6 }
            float r0 = r1.mPidWaitSum     // Catch:{ Throwable -> 0x03c6 }
            int r0 = (int) r0     // Catch:{ Throwable -> 0x03c6 }
            r4.mPidLastSchedWaitSum = r0     // Catch:{ Throwable -> 0x03c6 }
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceThreadStack     // Catch:{ Throwable -> 0x03c6 }
            if (r0 == 0) goto L_0x03c6
            int r0 = r4.mCpuPercentInPid     // Catch:{ Throwable -> 0x03c6 }
            int r2 = com.taobao.onlinemonitor.OnLineMonitorApp.sThreadPidCpuPercentForStatistics     // Catch:{ Throwable -> 0x03c6 }
            if (r0 >= r2) goto L_0x0376
            int r0 = r4.mCpuPercentInDevice     // Catch:{ Throwable -> 0x03c6 }
            int r2 = com.taobao.onlinemonitor.OnLineMonitorApp.sThreadDeviceCpuPercentForStatistics     // Catch:{ Throwable -> 0x03c6 }
            if (r0 < r2) goto L_0x03c6
        L_0x0376:
            boolean r0 = sIsTraceDetail     // Catch:{ Throwable -> 0x03c6 }
            if (r0 == 0) goto L_0x03c6
            boolean r0 = com.taobao.onlinemonitor.TraceDetail.sTraceThread     // Catch:{ Throwable -> 0x03c6 }
            if (r0 == 0) goto L_0x03c6
            java.util.ArrayList<java.lang.String> r0 = r4.mCpuStacks     // Catch:{ Throwable -> 0x03c6 }
            if (r0 != 0) goto L_0x038b
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Throwable -> 0x03c6 }
            r2 = 10
            r0.<init>(r2)     // Catch:{ Throwable -> 0x03c6 }
            r4.mCpuStacks = r0     // Catch:{ Throwable -> 0x03c6 }
        L_0x038b:
            java.util.ArrayList<java.lang.String> r0 = r4.mCpuStacks     // Catch:{ Throwable -> 0x03c6 }
            int r0 = r0.size()     // Catch:{ Throwable -> 0x03c6 }
            r2 = 10
            if (r0 >= r2) goto L_0x03c6
            java.lang.String r2 = ""
            java.lang.reflect.Method r0 = r1.mGetStackTraceById     // Catch:{ Throwable -> 0x03b4 }
            java.lang.Class<?> r3 = r1.mDmVmInternalClazz     // Catch:{ Throwable -> 0x03b4 }
            r6 = 1
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x03b4 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x03b4 }
            r6 = 0
            r7[r6] = r5     // Catch:{ Throwable -> 0x03b4 }
            java.lang.Object r0 = r0.invoke(r3, r7)     // Catch:{ Throwable -> 0x03b4 }
            java.lang.StackTraceElement[] r0 = (java.lang.StackTraceElement[]) r0     // Catch:{ Throwable -> 0x03b4 }
            java.lang.StackTraceElement[] r0 = (java.lang.StackTraceElement[]) r0     // Catch:{ Throwable -> 0x03b4 }
            r3 = 10
            java.lang.String r0 = getStackTraceElement(r0, r6, r3)     // Catch:{ Throwable -> 0x03b4 }
            goto L_0x03b9
        L_0x03b4:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x03c6 }
            r0 = r2
        L_0x03b9:
            java.util.ArrayList<java.lang.String> r2 = r4.mCpuStacks     // Catch:{ Throwable -> 0x03c6 }
            boolean r2 = r2.contains(r0)     // Catch:{ Throwable -> 0x03c6 }
            if (r2 != 0) goto L_0x03c6
            java.util.ArrayList<java.lang.String> r2 = r4.mCpuStacks     // Catch:{ Throwable -> 0x03c6 }
            r2.add(r0)     // Catch:{ Throwable -> 0x03c6 }
        L_0x03c6:
            r56 = r10
            r61 = r12
            r60 = r13
            r64 = r14
            r59 = r55
            r3 = 0
            r16 = 1000000(0xf4240, double:4.940656E-318)
            goto L_0x0569
        L_0x03d6:
            r1 = r67
        L_0x03d8:
            r9 = 30
        L_0x03da:
            r44 = 6
            goto L_0x03c6
        L_0x03dd:
            r55 = r9
            r9 = 30
            r44 = 6
            com.taobao.onlinemonitor.ProcessCpuTracker r0 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x03c6 }
            int r4 = r1.mMyPid     // Catch:{ Throwable -> 0x03c6 }
            r23 = 0
            r18 = r0
            r19 = r4
            r20 = r7
            r21 = r14
            r22 = r12
            boolean r0 = r18.getProcInfo(r19, r20, r21, r22, r23)     // Catch:{ Throwable -> 0x03c6 }
            if (r0 == 0) goto L_0x0438
            r4 = 0
            r0 = r14[r4]     // Catch:{ Throwable -> 0x0428 }
            r4 = 5
            r18 = r12[r4]     // Catch:{ Throwable -> 0x0428 }
            com.taobao.onlinemonitor.ProcessCpuTracker r4 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x0428 }
            int r4 = r4.mJiffyMillis     // Catch:{ Throwable -> 0x0428 }
            r56 = r10
            long r9 = (long) r4
            long r18 = r18 * r9
            long r9 = android.os.SystemClock.elapsedRealtime()     // Catch:{ Throwable -> 0x042a }
            r4 = 0
            long r9 = r9 - r18
            long r18 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x042a }
            r16 = 1000000(0xf4240, double:4.940656E-318)
            long r18 = r18 / r16
            r4 = 0
            long r9 = r18 - r9
            int r4 = (r9 > r42 ? 1 : (r9 == r42 ? 0 : -1))
            if (r4 >= 0) goto L_0x0421
            r9 = r42
        L_0x0421:
            r20 = r0
            r45 = r9
            r10 = r55
            goto L_0x047b
        L_0x0428:
            r56 = r10
        L_0x042a:
            r16 = 1000000(0xf4240, double:4.940656E-318)
        L_0x042d:
            r61 = r12
            r60 = r13
            r64 = r14
            r59 = r55
        L_0x0435:
            r3 = 0
            goto L_0x0569
        L_0x0438:
            r56 = r10
            r16 = 1000000(0xf4240, double:4.940656E-318)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x042d }
            r0.<init>()     // Catch:{ Throwable -> 0x042d }
            java.lang.String r4 = "/proc/"
            r0.append(r4)     // Catch:{ Throwable -> 0x042d }
            int r4 = r1.mMyPid     // Catch:{ Throwable -> 0x042d }
            r0.append(r4)     // Catch:{ Throwable -> 0x042d }
            java.lang.String r4 = "/task/"
            r0.append(r4)     // Catch:{ Throwable -> 0x042d }
            r0.append(r7)     // Catch:{ Throwable -> 0x042d }
            java.lang.String r4 = "/comm"
            r0.append(r4)     // Catch:{ Throwable -> 0x042d }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x042d }
            r4 = 60
            char[] r4 = new char[r4]     // Catch:{ Throwable -> 0x042d }
            java.io.FileReader r9 = new java.io.FileReader     // Catch:{ Throwable -> 0x042d }
            r9.<init>(r0)     // Catch:{ Throwable -> 0x042d }
            r9.read(r4)     // Catch:{ Throwable -> 0x042d }
            r10 = r55
            r10.append(r4)     // Catch:{ Throwable -> 0x0548 }
            r9.close()     // Catch:{ Throwable -> 0x0548 }
            r4 = 0
            java.lang.String r0 = r10.substring(r4)     // Catch:{ Throwable -> 0x0548 }
            r10.setLength(r4)     // Catch:{ Throwable -> 0x0548 }
            r20 = r0
        L_0x047b:
            java.lang.String r4 = ""
            boolean r0 = sIsTraceDetail     // Catch:{ Throwable -> 0x0548 }
            if (r0 == 0) goto L_0x04b1
            int r0 = sApiLevel     // Catch:{ Throwable -> 0x0548 }
            r9 = 18
            if (r0 < r9) goto L_0x04b1
            java.lang.reflect.Method r0 = r1.mGetStackTraceById     // Catch:{ Throwable -> 0x04aa }
            java.lang.Class<?> r9 = r1.mDmVmInternalClazz     // Catch:{ Throwable -> 0x04aa }
            r58 = r4
            r11 = 1
            java.lang.Object[] r4 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x04a8 }
            java.lang.Integer r18 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x04a8 }
            r11 = 0
            r4[r11] = r18     // Catch:{ Throwable -> 0x04a8 }
            java.lang.Object r0 = r0.invoke(r9, r4)     // Catch:{ Throwable -> 0x04a8 }
            java.lang.StackTraceElement[] r0 = (java.lang.StackTraceElement[]) r0     // Catch:{ Throwable -> 0x04a8 }
            java.lang.StackTraceElement[] r0 = (java.lang.StackTraceElement[]) r0     // Catch:{ Throwable -> 0x04a8 }
            r4 = 10
            java.lang.String r0 = getStackTraceElement(r0, r11, r4)     // Catch:{ Throwable -> 0x04a8 }
            r41 = r0
            goto L_0x04b5
        L_0x04a8:
            r0 = move-exception
            goto L_0x04ad
        L_0x04aa:
            r0 = move-exception
            r58 = r4
        L_0x04ad:
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0548 }
            goto L_0x04b3
        L_0x04b1:
            r58 = r4
        L_0x04b3:
            r41 = r58
        L_0x04b5:
            com.taobao.onlinemonitor.ThreadInfo r0 = new com.taobao.onlinemonitor.ThreadInfo     // Catch:{ Throwable -> 0x0548 }
            long r8 = (long) r8
            r59 = r10
            long r10 = (long) r2
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x054a }
            r61 = r12
            r60 = r13
            long r12 = r2.mSystemRunCpuTime     // Catch:{ Throwable -> 0x054e }
            com.taobao.onlinemonitor.ProcessCpuTracker r2 = r1.mProcessCpuTracker     // Catch:{ Throwable -> 0x054e }
            r62 = r12
            long r12 = r2.mSystemTotalCpuTime     // Catch:{ Throwable -> 0x054e }
            java.lang.String r2 = r1.mActivityName     // Catch:{ Throwable -> 0x054e }
            int r4 = r1.mPidIoWaitCount     // Catch:{ Throwable -> 0x054e }
            r64 = r14
            int r14 = r1.mPidIoWaitSum     // Catch:{ Throwable -> 0x0435 }
            r65 = r6
            int r6 = r1.mPidWaitCount     // Catch:{ Throwable -> 0x0435 }
            r66 = r6
            float r6 = r1.mPidWaitSum     // Catch:{ Throwable -> 0x0435 }
            int r6 = (int) r6     // Catch:{ Throwable -> 0x0435 }
            r40 = r6
            r18 = r0
            r19 = r5
            r21 = r15
            r22 = r7
            r23 = r8
            r25 = r10
            r28 = r3
            r29 = r45
            r31 = r62
            r33 = r12
            r35 = r60
            r36 = r2
            r37 = r4
            r38 = r14
            r39 = r66
            r18.<init>(r19, r20, r21, r22, r23, r25, r27, r28, r29, r31, r33, r35, r36, r37, r38, r39, r40, r41)     // Catch:{ Throwable -> 0x0435 }
            java.util.HashMap<java.lang.Integer, com.taobao.onlinemonitor.ThreadInfo> r2 = r1.mThreadInfoTidHashMap     // Catch:{ Throwable -> 0x0435 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x0435 }
            java.lang.Object r2 = r2.remove(r3)     // Catch:{ Throwable -> 0x0435 }
            com.taobao.onlinemonitor.ThreadInfo r2 = (com.taobao.onlinemonitor.ThreadInfo) r2     // Catch:{ Throwable -> 0x0435 }
            if (r2 == 0) goto L_0x050f
            r3 = 0
            r2.mStatus = r3     // Catch:{ Throwable -> 0x0569 }
            goto L_0x0510
        L_0x050f:
            r3 = 0
        L_0x0510:
            java.util.HashMap<java.lang.Integer, com.taobao.onlinemonitor.ThreadInfo> r2 = r1.mThreadInfoTidHashMap     // Catch:{ Throwable -> 0x0569 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x0569 }
            r2.put(r4, r0)     // Catch:{ Throwable -> 0x0569 }
            java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r2 = r1.mThreadInfoHashMap     // Catch:{ Throwable -> 0x0569 }
            r4 = r65
            r2.put(r4, r0)     // Catch:{ Throwable -> 0x0569 }
            boolean r2 = sIsTraceDetail     // Catch:{ Throwable -> 0x0569 }
            if (r2 == 0) goto L_0x0569
            boolean r2 = com.taobao.onlinemonitor.TraceDetail.sTraceThreadWait     // Catch:{ Throwable -> 0x0569 }
            if (r2 == 0) goto L_0x0569
            int r2 = sApiLevel     // Catch:{ Throwable -> 0x0569 }
            r4 = 18
            if (r2 < r4) goto L_0x0569
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x0569 }
            r2.getThreadSchedTime(r0)     // Catch:{ Throwable -> 0x0569 }
            int r2 = r0.mIoWaitCount     // Catch:{ Throwable -> 0x0569 }
            r0.mLastIoWaitCount = r2     // Catch:{ Throwable -> 0x0569 }
            int r2 = r0.mIoWaitTime     // Catch:{ Throwable -> 0x0569 }
            r0.mLastIoWaitTime = r2     // Catch:{ Throwable -> 0x0569 }
            int r2 = r0.mSchedWaitCount     // Catch:{ Throwable -> 0x0569 }
            r0.mLastSchedWaitCount = r2     // Catch:{ Throwable -> 0x0569 }
            int r2 = r0.mSchedWaitSum     // Catch:{ Throwable -> 0x0569 }
            r0.mLastSchedWaitSum = r2     // Catch:{ Throwable -> 0x0569 }
            int r2 = r0.mSchedWaitMax     // Catch:{ Throwable -> 0x0569 }
            r0.mLastSchedWaitMax = r2     // Catch:{ Throwable -> 0x0569 }
            goto L_0x0569
        L_0x0548:
            r59 = r10
        L_0x054a:
            r61 = r12
            r60 = r13
        L_0x054e:
            r64 = r14
            goto L_0x0435
        L_0x0552:
            r54 = r4
            goto L_0x0559
        L_0x0555:
            r54 = r4
            r53 = r6
        L_0x0559:
            r59 = r9
            r56 = r10
            r61 = r12
            r60 = r13
            r64 = r14
        L_0x0563:
            r3 = 0
            r16 = 1000000(0xf4240, double:4.940656E-318)
            r44 = 6
        L_0x0569:
            r0 = r50
        L_0x056b:
            if (r0 <= 0) goto L_0x0573
            r54.get()     // Catch:{ Throwable -> 0x05e2 }
            int r0 = r0 + -1
            goto L_0x056b
        L_0x0573:
            int r0 = r52 + 1
            r3 = r0
            r7 = r49
            r8 = r50
            r2 = r51
            r6 = r53
            r4 = r54
            r10 = r56
            r9 = r59
            r13 = r60
            r12 = r61
            r14 = r64
            r5 = 0
            r15 = 1
            goto L_0x01ff
        L_0x058e:
            r51 = r2
            boolean r0 = sIsTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            if (r0 == 0) goto L_0x05e6
            if (r51 == 0) goto L_0x05e6
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mSysThreadsCount     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mSysThreadsCount     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mThreadCount     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mVmThreadsCount     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mVmThreadsCount     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mRuntimeThreadCount     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r0 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r0 = r0.mRunningThreadsCount     // Catch:{ Throwable -> 0x05e2 }
            com.taobao.onlinemonitor.TraceDetail r2 = r1.mTraceDetail     // Catch:{ Throwable -> 0x05e2 }
            android.util.SparseIntArray r2 = r2.mRunningThreadsCount     // Catch:{ Throwable -> 0x05e2 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r1.mRunningThreadCount     // Catch:{ Throwable -> 0x05e2 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x05e2 }
            int r3 = r3.intValue()     // Catch:{ Throwable -> 0x05e2 }
            r0.put(r2, r3)     // Catch:{ Throwable -> 0x05e2 }
            goto L_0x05e6
        L_0x05e2:
            r0 = move-exception
            r0.printStackTrace()
        L_0x05e6:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            if (r0 == 0) goto L_0x0606
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r0 = r0.maxThread
            int r2 = r1.mRuntimeThreadCount
            if (r0 >= r2) goto L_0x05f8
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r2 = r1.mRuntimeThreadCount
            r0.maxThread = r2
        L_0x05f8:
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r0 = r0.maxRunningThread
            int r2 = r1.mRunningThreadCount
            if (r0 >= r2) goto L_0x0606
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r1.mActivityRuntimeInfo
            int r2 = r1.mRunningThreadCount
            r0.maxRunningThread = r2
        L_0x0606:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.getThreadStat():void");
    }

    /* access modifiers changed from: package-private */
    public void onTouchDown(long j) {
        this.mCheckAnrTime = j;
        this.mIsOnTouch = true;
        this.mLayoutTimes = 0;
        needStratAntCheck();
        this.mThreadHandler.removeMessages(2);
        this.mThreadHandler.sendEmptyMessageDelayed(2, 500);
    }

    /* access modifiers changed from: package-private */
    public void startMemoryMonitor() {
        this.mCheckAnrTime = System.nanoTime() / 1000000;
        this.mLayoutTimes = 0;
        this.mThreadHandler.removeMessages(2);
        this.mThreadHandler.sendEmptyMessageDelayed(2, 500);
        if (!this.mIsCheckPerfromanceRunning) {
            this.mIsCheckPerfromanceRunning = true;
            if (sIsTraceDetail && TraceDetail.sTraceThread) {
                this.mThreadHandler.sendEmptyMessage(12);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void needStratAntCheck() {
        if (this.mCheckAnrCounter < 0) {
            this.mCheckAnrCounter = 0;
            this.mCheckAnrTime = System.nanoTime() / 1000000;
            this.mThreadHandler.sendEmptyMessageDelayed(5, 5000);
        }
    }

    /* access modifiers changed from: package-private */
    public void commitOnActivityPaused() {
        if (this.mIsIdleGeted || !this.mIsInitedActivity) {
            long j = this.mMobileRxBytes;
            long j2 = this.mMobileTxBytes;
            long j3 = this.mTotalRxBytes;
            long j4 = this.mTotalTxBytes;
            getTrafficStats();
            float f = (float) ((this.mMobileRxBytes - j) / 1024);
            float f2 = (float) ((this.mMobileTxBytes - j2) / 1024);
            float f3 = (float) ((this.mTotalRxBytes - j3) / 1024);
            float f4 = (float) ((this.mTotalTxBytes - j4) / 1024);
            this.mOnLineStat.trafficStatsInfo.activityMobileRxBytes = f;
            this.mOnLineStat.trafficStatsInfo.activityMobileTxBytes = f2;
            this.mOnLineStat.trafficStatsInfo.activityTotalRxBytes = f3;
            this.mOnLineStat.trafficStatsInfo.activityTotalTxBytes = f2;
            this.mOnLineStat.trafficStatsInfo.totalMobileRxBytes = (float) ((this.mMobileRxBytes - this.mFirstMobileRxBytes) / 1024);
            this.mOnLineStat.trafficStatsInfo.totalMobileTxBytes = (float) ((this.mMobileTxBytes - this.mFirstMobileTxBytes) / 1024);
            this.mOnLineStat.trafficStatsInfo.totalTotalRxBytes = (float) ((this.mTotalRxBytes - this.mFirstTotalRxBytes) / 1024);
            this.mOnLineStat.trafficStatsInfo.totalTotalTxBytes = (float) ((this.mTotalTxBytes - this.mFirstTotalTxBytes) / 1024);
            if (sIsDetailDebug) {
                Log.e(TAG, "MobileRxBytes=" + f + ",MobileTxBytes=" + f2 + ",TotalRxBytes=" + f3 + ",TotalTxBytes=" + f4);
            }
        }
        if (!this.mIsInitedActivity) {
            this.mIsInitedActivity = true;
        }
        if (this.mActivityRuntimeInfo != null) {
            this.mActivityRuntimeInfo.totalTx = (short) ((int) this.mOnLineStat.trafficStatsInfo.totalTotalTxBytes);
            this.mActivityRuntimeInfo.totalRx = (short) ((int) this.mOnLineStat.trafficStatsInfo.totalTotalRxBytes);
            if (this.mActivityRuntimeInfo.loadTime == 0) {
                this.mLoadTimeCalculate.needStopLoadTimeCalculate(true);
                if (this.mActivityRuntimeInfo.loadTime <= 0) {
                    this.mActivityRuntimeInfo.loadTime = 0;
                    this.mLoadTimeCalculate.setActivityInfo(this.mActivityRuntimeInfo);
                }
            }
            if (this.mActivityRuntimeInfo.idleTime <= 0) {
                this.mActivityRuntimeInfo.idleTime = 0;
                this.mActivityRuntimeInfo.checkIdleTimes = (short) this.mIdleNotifyCount;
            }
            int i = this.mSmoothCalculate.mActivityTotalSmUsedTime == 0 ? 0 : (this.mSmoothCalculate.mActivityTotalSmCount * 1000) / this.mSmoothCalculate.mActivityTotalSmUsedTime;
            if (i >= 60) {
                if (this.mSmoothCalculate.mTotalBadSmTime >= 0) {
                    this.mSmoothCalculate.mActivityTotalSmUsedTime = (this.mSmoothCalculate.mActivityTotalSmCount * 1000) / 60;
                    this.mSmoothCalculate.mActivityTotalSmUsedTime += this.mSmoothCalculate.mTotalBadSmTime;
                }
                i = this.mSmoothCalculate.mActivityTotalSmUsedTime == 0 ? 0 : (this.mSmoothCalculate.mActivityTotalSmCount * 1000) / this.mSmoothCalculate.mActivityTotalSmUsedTime;
            }
            this.mActivityRuntimeInfo.avgSm = i;
            if (this.mActivityRuntimeInfo.finalizerSize == 0) {
                this.mActivityRuntimeInfo.finalizerSize = this.mOnLineStat.memroyStat.finalizerSize;
            }
            onActivityPause();
            if (!(this.mOnlineStatistics == null || this.mActivityRuntimeInfo == null || this.mActivityRuntimeInfo.loadTime <= 0)) {
                int size = this.mOnlineStatistics.size();
                for (int i2 = 0; i2 < size; i2++) {
                    OnlineStatistics onlineStatistics = this.mOnlineStatistics.get(i2);
                    if (onlineStatistics != null) {
                        if (this.mActivityRuntimeInfo != null) {
                            this.mActivityRuntimeInfo.activityName = this.mActivityName;
                        }
                        onlineStatistics.onActivityPaused(this.mActivityLifecycleCallback.mActivity, this.mOnLineStat, this.mActivityRuntimeInfo);
                    }
                }
            }
            if (!sIsTraceDetail && this.mActivityRuntimeInfo != null) {
                Arrays.fill(this.mActivityRuntimeInfo.activityBadSmoothStepCount, 0);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        if (sIsTraceDetail) {
            this.mActivityRuntimeInfo = null;
        }
        if (this.mMessageQueue != null) {
            this.mMessageQueue.removeIdleHandler(this.mIdleHandler);
            this.mHandler.removeMessages(1);
        }
        this.mCheckIdleTimes = 0;
        this.mStartBlockingGCCount = this.mBlockingGCCount;
        this.mStartBlockingGCTime = this.mTotalBlockingGCTime;
        this.mStartGcCount = this.mTotalGcCount;
        this.mActivityIdleTime = 0;
        this.mSysGetCounter = 0;
        this.mTotalSysCPUPercent = 0;
        this.mTotalMyPidCPUPercent = 0;
        this.mTotalIOWaitTime = 0;
        this.mOldMajorFault = 0;
        this.mMyPidScoreTestCounter = 0;
        this.mSysScoreTestCounter = 0;
        this.mSystemRunningTotalScore = 0;
        this.mMyPidTotalScore = 0;
        this.mLastMemroyCheckTime = 0;
        this.mLastCPUCheckTime = 0;
        this.mFirstMobileRxBytes = -1;
        if (this.mOnLineStat != null) {
            this.mOnLineStat.isActivityTouched = false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isRooted() {
        String[] strArr = {"/system/bin/su", "/system/xbin/su", "/system/sbin/su", "/sbin/su", "/vendor/bin/su"};
        int i = 0;
        while (i < strArr.length) {
            try {
                if (new File(strArr[i]).exists()) {
                    this.mOnLineStat.deviceInfo.isRooted = true;
                    return true;
                }
                i++;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void pidOpenFileCount() {
        String[] list;
        if (!this.mIsInBackGround && this.mIsDeviceSampling) {
            try {
                if ((this.mActivityRuntimeInfo == null ? 0 : this.mActivityRuntimeInfo.openFileGetCount) <= getAllowCheckCountPerActivity()) {
                    long nanoTime = System.nanoTime() / 1000000;
                    if (this.mActivityRuntimeInfo != null) {
                        if (this.mActivityRuntimeInfo.lastOpenFileGetTime <= 0 || nanoTime - this.mActivityRuntimeInfo.lastOpenFileGetTime >= ((long) this.mCpuCheckIntervalControl)) {
                            this.mActivityRuntimeInfo.openFileGetCount++;
                            this.mActivityRuntimeInfo.lastOpenFileGetTime = nanoTime;
                        } else {
                            return;
                        }
                    }
                    long nanoTime2 = System.nanoTime() / 1000000;
                    File file = new File("/proc/" + this.mMyPid + "/fd");
                    if (file.exists() && file.isDirectory() && (list = file.list()) != null) {
                        this.mOpenFileCount = list.length;
                    }
                    long nanoTime3 = System.nanoTime() / 1000000;
                    if (sIsDetailDebug) {
                        Log.e(TAG, "pidOpenFileCount time=" + (nanoTime3 - nanoTime2));
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkMemoryLeack(ArrayList<Object> arrayList) {
        if (this.mOnlineStatistics != null) {
            try {
                Iterator<Map.Entry<Object, Object>> it = this.mLeakMemoryWeakMap.entrySet().iterator();
                while (it.hasNext()) {
                    Object value = it.next().getValue();
                    if ((value instanceof WeakReference) && ((WeakReference) value).get() != null) {
                        it.remove();
                    }
                }
            } catch (Throwable unused) {
            }
            for (Object next : this.mLeakMemoryWeakMap.keySet()) {
                if (next != null) {
                    if (arrayList != null) {
                        arrayList.add(next);
                    }
                    if (next != null) {
                        int size = this.mOnlineStatistics.size();
                        for (int i = 0; i < size; i++) {
                            OnlineStatistics onlineStatistics = this.mOnlineStatistics.get(i);
                            if (onlineStatistics != null) {
                                onlineStatistics.onMemoryLeak(next.getClass().getName(), 0, "");
                            }
                        }
                    }
                }
            }
        }
    }

    static class ProcFilenameFilter implements FilenameFilter {
        ProcFilenameFilter() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
            r4 = r4.charAt(0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean accept(java.io.File r3, java.lang.String r4) {
            /*
                r2 = this;
                r3 = 0
                if (r4 == 0) goto L_0x0017
                int r0 = r4.length()
                r1 = 1
                if (r0 < r1) goto L_0x0017
                char r4 = r4.charAt(r3)
                r0 = 48
                if (r4 < r0) goto L_0x0017
                r0 = 57
                if (r4 > r0) goto L_0x0017
                return r1
            L_0x0017:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.ProcFilenameFilter.accept(java.io.File, java.lang.String):boolean");
        }
    }

    class OnLineMonitorGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mIndex;

        public OnLineMonitorGlobalLayoutListener(int i) {
            this.mIndex = i;
        }

        public void onGlobalLayout() {
            if (OnLineMonitor.this.mActivityLifecycleCallback == null || this.mIndex == OnLineMonitor.this.mActivityLifecycleCallback.mCreateIndex) {
                OnLineMonitor onLineMonitor = OnLineMonitor.this;
                onLineMonitor.mLayoutTimes = (short) (onLineMonitor.mLayoutTimes + 1);
                if (OnLineMonitor.this.mActivityRuntimeInfo != null) {
                    ActivityRuntimeInfo activityRuntimeInfo = OnLineMonitor.this.mActivityRuntimeInfo;
                    activityRuntimeInfo.totalLayoutCount = (short) (activityRuntimeInfo.totalLayoutCount + 1);
                }
                OnLineMonitor.this.mCheckAnrTime = System.nanoTime() / 1000000;
            }
        }
    }

    class MyHandler extends Handler {
        MyHandler() {
        }

        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i != 1) {
                    if (i != 5) {
                        switch (i) {
                            case 14:
                                OnLineMonitor.this.startPerformanceMonitor();
                                return;
                            case 15:
                                switch (message.arg1) {
                                    case 0:
                                        if (OnLineMonitor.this.mActivityRuntimeInfo != null) {
                                            OnLineMonitor.this.mActivityRuntimeInfo.lifeCycleArrayUsedTime[0] = OnLineMonitor.this.mActivityLifecycleCallback.mActivityStartTime - OnLineMonitor.this.mActivityLifecycleCallback.mActivityOncreateTime;
                                            OnLineMonitor.this.mActivityRuntimeInfo.lifeCycleArrayUsedTime[1] = (System.nanoTime() / 1000000) - OnLineMonitor.this.mActivityLifecycleCallback.mActivityStartTime;
                                            return;
                                        }
                                        return;
                                    case 1:
                                        OnLineMonitor.this.mActivityRuntimeInfo.lifeCycleArrayUsedTime[1] = (System.nanoTime() / 1000000) - OnLineMonitor.this.mActivityLifecycleCallback.mActivityStartTime;
                                        return;
                                    case 2:
                                        if (OnLineMonitor.this.mActivityRuntimeInfo != null) {
                                            OnLineMonitor.this.mActivityRuntimeInfo.lifeCycleArrayUsedTime[2] = (System.nanoTime() / 1000000) - OnLineMonitor.this.mActivityLifecycleCallback.mActivityResumeTime;
                                            return;
                                        }
                                        return;
                                    case 3:
                                        if (OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList == null) {
                                            return;
                                        }
                                        if (OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.size() != 0) {
                                            ActivityRuntimeInfo activityRuntimeInfo = OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.get(OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.size() - 1);
                                            if (activityRuntimeInfo != null) {
                                                activityRuntimeInfo.lifeCycleArrayUsedTime[3] = (System.nanoTime() / 1000000) - OnLineMonitor.this.mActivityLifecycleCallback.mActivityPausedTime;
                                                return;
                                            }
                                            return;
                                        }
                                        return;
                                    case 4:
                                        if (OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList == null) {
                                            return;
                                        }
                                        if (OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.size() != 0) {
                                            ActivityRuntimeInfo activityRuntimeInfo2 = OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.get(OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.size() - 1);
                                            if (activityRuntimeInfo2 != null) {
                                                activityRuntimeInfo2.lifeCycleArrayUsedTime[4] = (System.nanoTime() / 1000000) - OnLineMonitor.this.mActivityLifecycleCallback.mActivityStopedTime;
                                                return;
                                            }
                                            return;
                                        }
                                        return;
                                    case 5:
                                        if (OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList == null) {
                                            return;
                                        }
                                        if (OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.size() != 0) {
                                            int size = OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.size() - 1;
                                            while (size >= 0) {
                                                ActivityRuntimeInfo activityRuntimeInfo3 = OnLineMonitor.this.mTraceDetail.mActivityRuntimeInfoList.get(size);
                                                if (activityRuntimeInfo3 == null || activityRuntimeInfo3.activityName == null || !activityRuntimeInfo3.activityName.equals(OnLineMonitor.this.mTraceDetail.mDestroyedActivityName)) {
                                                    size--;
                                                } else {
                                                    activityRuntimeInfo3.lifeCycleArrayUsedTime[5] = (System.nanoTime() / 1000000) - OnLineMonitor.this.mActivityLifecycleCallback.mActivityDestroyTime;
                                                    return;
                                                }
                                            }
                                            return;
                                        }
                                        return;
                                    default:
                                        return;
                                }
                            default:
                                return;
                        }
                    } else {
                        long nanoTime = System.nanoTime() / 1000000;
                        if (nanoTime - OnLineMonitor.this.mCheckAnrTime < TBToast.Duration.MEDIUM) {
                            OnLineMonitor.this.mIsCheckAnrStat = false;
                        }
                        OnLineMonitor.this.mCheckAnrTime = nanoTime;
                    }
                } else if (OnLineMonitor.this.mMessageQueue != null) {
                    OnLineMonitor.this.mMessageQueue.addIdleHandler(OnLineMonitor.this.mIdleHandler);
                }
            } catch (Throwable unused) {
            }
        }
    }

    class MyHandlerThread extends HandlerThread {
        public MyHandlerThread(String str, int i) {
            super(str, i);
        }

        /* access modifiers changed from: protected */
        public void onLooperPrepared() {
            OnLineMonitor.this.mHandlerThreadTid = Process.myTid();
            OnLineMonitor.this.mThreadHandler = new Handler() {
                /* JADX WARNING: Can't wrap try/catch for region: R(13:135|(1:141)|142|(3:148|149|(4:152|(2:158|464)|460|150))|159|160|(5:162|163|168|169|170)|175|(1:177)|178|(1:180)|181|495) */
                /* JADX WARNING: Can't wrap try/catch for region: R(7:230|(1:232)(1:233)|234|235|236|237|(2:239|(2:241|(2:243|469)(1:473))(1:472))(1:471)) */
                /* JADX WARNING: Missing exception handler attribute for start block: B:159:0x0456 */
                /* JADX WARNING: Missing exception handler attribute for start block: B:236:0x0745 */
                /* JADX WARNING: Removed duplicated region for block: B:162:0x045a A[SYNTHETIC, Splitter:B:162:0x045a] */
                /* JADX WARNING: Removed duplicated region for block: B:177:0x04af A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:180:0x04c7 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:239:0x0749 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:276:0x084e A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:279:0x0938 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:286:0x0952 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:297:0x09bc A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:298:0x09bd A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:323:0x0aad A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:324:0x0ab7 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:339:0x0b37 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:354:0x0bca A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:357:0x0bdb A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:360:0x0c1b A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:369:0x0c44 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:370:0x0c4b A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:373:0x0c56 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:374:0x0c57 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:384:0x0ca7 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:402:0x0d0e A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:404:0x0d13 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:419:0x0d5d A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:431:0x0d9c A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:437:0x0dcb A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:447:0x0e07 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:450:0x0e63 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:451:0x0e64 A[Catch:{ all -> 0x07a1, all -> 0x007b, Throwable -> 0x0e6c }] */
                /* JADX WARNING: Removed duplicated region for block: B:471:0x0798 A[SYNTHETIC] */
                /* JADX WARNING: Unknown top exception splitter block from list: {B:428:0x0d90=Splitter:B:428:0x0d90, B:159:0x0456=Splitter:B:159:0x0456} */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void handleMessage(android.os.Message r25) {
                    /*
                        r24 = this;
                        r1 = r24
                        r2 = r25
                        int r3 = r2.what     // Catch:{ Throwable -> 0x0e6c }
                        r4 = 12
                        r5 = 0
                        r6 = 400(0x190, float:5.6E-43)
                        r8 = 3000(0xbb8, double:1.482E-320)
                        r10 = 0
                        r13 = 3
                        r14 = 5
                        r15 = 2
                        r12 = 0
                        r7 = 1
                        switch(r3) {
                            case 2: goto L_0x07a5;
                            case 3: goto L_0x0017;
                            case 4: goto L_0x0705;
                            case 5: goto L_0x05b2;
                            case 6: goto L_0x0017;
                            case 7: goto L_0x0017;
                            case 8: goto L_0x038e;
                            case 9: goto L_0x0017;
                            case 10: goto L_0x0017;
                            case 11: goto L_0x0156;
                            case 12: goto L_0x0128;
                            case 13: goto L_0x00a6;
                            case 14: goto L_0x0017;
                            case 15: goto L_0x0017;
                            case 16: goto L_0x0093;
                            case 17: goto L_0x008a;
                            case 18: goto L_0x007f;
                            case 19: goto L_0x0019;
                            default: goto L_0x0017;
                        }     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0017:
                        goto L_0x0e71
                    L_0x0019:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo> r2 = r2.mBootResourceUsedInfoList     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0e71
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo> r2 = r2.mBootResourceUsedInfoList     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.size()     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 <= 0) goto L_0x0e71
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo> r2 = r2.mBootResourceUsedInfoList     // Catch:{ Throwable -> 0x0e6c }
                        monitor-enter(r2)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x007b }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r3 = r3.mOnlineStatistics     // Catch:{ all -> 0x007b }
                        int r3 = r3.size()     // Catch:{ all -> 0x007b }
                    L_0x003e:
                        if (r12 >= r3) goto L_0x006b
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x007b }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r4 = r4.mOnlineStatistics     // Catch:{ all -> 0x007b }
                        java.lang.Object r4 = r4.get(r12)     // Catch:{ all -> 0x007b }
                        r5 = r4
                        com.taobao.onlinemonitor.OnlineStatistics r5 = (com.taobao.onlinemonitor.OnlineStatistics) r5     // Catch:{ all -> 0x007b }
                        if (r5 == 0) goto L_0x0068
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r6 = r4.mOnLineStat     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x007b }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo> r7 = r4.mBootResourceUsedInfoList     // Catch:{ all -> 0x007b }
                        boolean r8 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsCodeBoot     // Catch:{ all -> 0x007b }
                        java.lang.String r9 = com.taobao.onlinemonitor.OnLineMonitorApp.sBootExtraType     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x007b }
                        long r10 = r4.mBootJiffyTime     // Catch:{ all -> 0x007b }
                        r5.onBootPerformance(r6, r7, r8, r9, r10)     // Catch:{ all -> 0x007b }
                    L_0x0068:
                        int r12 = r12 + 1
                        goto L_0x003e
                    L_0x006b:
                        boolean r3 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ all -> 0x007b }
                        if (r3 != 0) goto L_0x0078
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x007b }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x007b }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo> r3 = r3.mBootResourceUsedInfoList     // Catch:{ all -> 0x007b }
                        r3.clear()     // Catch:{ all -> 0x007b }
                    L_0x0078:
                        monitor-exit(r2)     // Catch:{ all -> 0x007b }
                        goto L_0x0e71
                    L_0x007b:
                        r0 = move-exception
                        r3 = r0
                        monitor-exit(r2)     // Catch:{ all -> 0x007b }
                        throw r3     // Catch:{ Throwable -> 0x0e6c }
                    L_0x007f:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.arg1     // Catch:{ Throwable -> 0x0e6c }
                        r3.recordBootResource(r2, r12)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x008a:
                        java.lang.Object r2 = r2.obj     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor.onInstallBundler(r2, r13)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x0093:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.LoadTimeCalculate r2 = r2.mLoadTimeCalculate     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0e71
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.LoadTimeCalculate r2 = r2.mLoadTimeCalculate     // Catch:{ Throwable -> 0x0e6c }
                        r2.needStopLoadTimeCalculate(r12)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x00a6:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIsBootEndActivity     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x00b0
                        goto L_0x0e71
                    L_0x00b0:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.mBootActivityLoadTime     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x00d5
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.mBootLoadTimeTryCount     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 >= r14) goto L_0x00d5
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r2.mBootLoadTimeTryCount     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3 + r7
                        r2.mBootLoadTimeTryCount = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 13
                        r2.sendEmptyMessageDelayed(r3, r8)     // Catch:{ Throwable -> 0x0e6c }
                        return
                    L_0x00d5:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$BootStepResourceInfo[] r2 = r2.bootStepResourceInfo     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0115
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$BootStepResourceInfo[] r2 = r2.bootStepResourceInfo     // Catch:{ Throwable -> 0x0e6c }
                        r2 = r2[r12]     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0115
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$BootStepResourceInfo[] r2 = r2.bootStepResourceInfo     // Catch:{ Throwable -> 0x0e6c }
                        r2 = r2[r7]     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0115
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$BootStepResourceInfo[] r2 = r2.bootStepResourceInfo     // Catch:{ Throwable -> 0x0e6c }
                        r2 = r2[r15]     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x0115
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 18
                        r2.removeMessages(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.recordBootResource(r15, r12)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0115:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.commmitBootFinished()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitorApp.startJitCompilation()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.notifyBootFinished()     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x0128:
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0e71
                        boolean r2 = com.taobao.onlinemonitor.TraceDetail.sTraceThread     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0e71
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIsFullInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x0e71
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.getCpuInfo(r7, r7, r7)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.removeMessages(r4)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        short r3 = com.taobao.onlinemonitor.TraceDetail.sTraceThreadInterval     // Catch:{ Throwable -> 0x0e6c }
                        long r5 = (long) r3     // Catch:{ Throwable -> 0x0e6c }
                        r2.sendEmptyMessageDelayed(r4, r5)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x0156:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.mIsFullInBackGround = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Exception -> 0x016c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Exception -> 0x016c }
                        android.content.Context r2 = r2.mContext     // Catch:{ Exception -> 0x016c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Exception -> 0x016c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Exception -> 0x016c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyCallback r3 = r3.mMyCallback     // Catch:{ Exception -> 0x016c }
                        r2.unregisterComponentCallbacks(r3)     // Catch:{ Exception -> 0x016c }
                        goto L_0x0171
                    L_0x016c:
                        r0 = move-exception
                        r2 = r0
                        r2.printStackTrace()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0171:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Exception -> 0x0183 }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Exception -> 0x0183 }
                        android.content.Context r2 = r2.mContext     // Catch:{ Exception -> 0x0183 }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Exception -> 0x0183 }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Exception -> 0x0183 }
                        android.content.BroadcastReceiver r3 = r3.mBatInfoReceiver     // Catch:{ Exception -> 0x0183 }
                        r2.unregisterReceiver(r3)     // Catch:{ Exception -> 0x0183 }
                        goto L_0x0188
                    L_0x0183:
                        r0 = move-exception
                        r2 = r0
                        r2.printStackTrace()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0188:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.removeCallbacksAndMessages(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.getCpuInfo(r7, r7, r7)     // Catch:{ Throwable -> 0x0e6c }
                        java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Throwable -> 0x0e6c }
                        r2.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        if (r3 == 0) goto L_0x026b
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.systemJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
                        if (r6 <= 0) goto L_0x026b
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.totalJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
                        if (r6 <= 0) goto L_0x026b
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r4 = r4.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r4.mPidJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r4 = r4.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r10 = r4.pidJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        r4 = 0
                        long r8 = r8 - r10
                        r3.pidJiffyTime = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r4 = r4.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r4.mSystemTotalCpuTime     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r4 = r4.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r10 = r4.totalJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        r4 = 0
                        long r8 = r8 - r10
                        r3.totalJiffyTime = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r4 = r4.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r4.mSystemRunCpuTime     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r4 = r4.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r10 = r4.systemJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        r4 = 0
                        long r8 = r8 - r10
                        r3.systemJiffyTime = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.pidJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 100
                        long r3 = r3 * r8
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r6 = r6.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r6.systemJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3 / r8
                        int r3 = (int) r3     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r4 = "MaxCpuSysRun"
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r6 = r6.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = r6.memStart     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0e6c }
                        r2.put(r4, r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r4 = "CpuSysRun"
                        java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0e6c }
                        r2.put(r4, r3)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = "MaxCpuSysTotal"
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r4 = r4.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = r4.memEnd     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0e6c }
                        r2.put(r3, r4)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = r3.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.pidJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 100
                        long r3 = r3 * r8
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r6 = r6.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r6.totalJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3 / r8
                        int r3 = (int) r3     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r4 = "CpuSysTotal"
                        java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0e6c }
                        r2.put(r4, r3)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x026b:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r3 = r3.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        if (r3 == 0) goto L_0x02a9
                        boolean r3 = com.taobao.onlinemonitor.OnLineMonitorApp.sIsDebug     // Catch:{ Throwable -> 0x0e6c }
                        if (r3 != 0) goto L_0x02a9
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r3 = r3.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3.size()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0281:
                        if (r12 >= r3) goto L_0x02a9
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r4 = r4.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Object r4 = r4.get(r12)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnlineStatistics r4 = (com.taobao.onlinemonitor.OnlineStatistics) r4     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x02a6
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r6 = r6.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r8 = r8.mThreadInfoHashMap     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r9 = r9.mBlockGuardThreadInfo     // Catch:{ Throwable -> 0x0e6c }
                        r4.onGotoSleep(r6, r8, r2, r9)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x02a6:
                        int r12 = r12 + 1
                        goto L_0x0281
                    L_0x02a9:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfo     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x02ba
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfo     // Catch:{ Throwable -> 0x0e6c }
                        r2.clear()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x02ba:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x02cb
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0e6c }
                        r2.clear()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x02cb:
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x02e1 }
                        r2.gc()     // Catch:{ Throwable -> 0x02e1 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x02e1 }
                        r2.runFinalization()     // Catch:{ Throwable -> 0x02e1 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x02e1 }
                        r2.gc()     // Catch:{ Throwable -> 0x02e1 }
                        goto L_0x02ea
                    L_0x02e1:
                        r0 = move-exception
                        r2 = r0
                        java.lang.String r3 = "OnLineMonitor"
                        java.lang.String r4 = "手动执行内存回收异常"
                        android.util.Log.e(r3, r4, r2)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x02ea:
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0360
                        boolean r2 = com.taobao.onlinemonitor.TraceDetail.sMemoryLeakDetector     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x02fc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mHasMemroyLeack     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x030b
                    L_0x02fc:
                        boolean r2 = com.taobao.onlinemonitor.TraceDetail.sMemoryAnalysis     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x030b
                        boolean r2 = com.taobao.onlinemonitor.TraceDetail.sTraceThread     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0356
                        int r2 = com.taobao.onlinemonitor.OnLineMonitorApp.sToSleepTime     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 120000(0x1d4c0, float:1.68156E-40)
                        if (r2 < r3) goto L_0x0356
                    L_0x030b:
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0356
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0325 }
                        r2.gc()     // Catch:{ Throwable -> 0x0325 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0325 }
                        r2.runFinalization()     // Catch:{ Throwable -> 0x0325 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0325 }
                        r2.gc()     // Catch:{ Throwable -> 0x0325 }
                        goto L_0x032e
                    L_0x0325:
                        r0 = move-exception
                        r2 = r0
                        java.lang.String r3 = "OnLineMonitor"
                        java.lang.String r4 = "手动执行内存回收异常"
                        android.util.Log.e(r3, r4, r2)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x032e:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        r2.writePageInfo(r7)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x034d }
                        r2.gc()     // Catch:{ Throwable -> 0x034d }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x034d }
                        r2.runFinalization()     // Catch:{ Throwable -> 0x034d }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x034d }
                        r2.gc()     // Catch:{ Throwable -> 0x034d }
                        goto L_0x0356
                    L_0x034d:
                        r0 = move-exception
                        r2 = r0
                        java.lang.String r3 = "OnLineMonitor"
                        java.lang.String r4 = "手动执行内存回收异常"
                        android.util.Log.e(r3, r4, r2)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0356:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        r2.clear()     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0367
                    L_0x0360:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.checkMemoryLeack(r5)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0367:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        r2.isFullInBackGround = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 52
                        r2.notifyOnlineRuntimeStat(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = "开始进入休眠!"
                        r2.showMessage(r3)     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsNormalDebug     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0e71
                        java.lang.String r2 = "OnLineMonitor"
                        java.lang.String r3 = "Now go to sleep!"
                        android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x038e:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.isInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x058f
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r2 = r2.performanceInfo     // Catch:{ Throwable -> 0x0e6c }
                        r2.appProgressImportance = r6     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$CpuStat r2 = r2.cpuStat     // Catch:{ Throwable -> 0x0e6c }
                        r2.cpuAlarmInBg = r12     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = "进入背景通知!"
                        r2.showMessage(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 50
                        r2.notifyOnlineRuntimeStat(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.notifyBackForGroundListener(r7)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProblemCheck r2 = r2.mProblemCheck     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x03fa
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProblemCheck r2 = r2.mProblemCheck     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIsUploadBroadCastProblem     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x03fa
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProblemCheck r2 = r2.mProblemCheck     // Catch:{ Throwable -> 0x0e6c }
                        android.app.Application r3 = com.taobao.onlinemonitor.OnLineMonitorApp.sApplication     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.checkBroadCastCount(r3, r12)     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 300(0x12c, float:4.2E-43)
                        if (r2 < r3) goto L_0x03fa
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProblemCheck r2 = r2.mProblemCheck     // Catch:{ Throwable -> 0x0e6c }
                        android.app.Application r3 = com.taobao.onlinemonitor.OnLineMonitorApp.sApplication     // Catch:{ Throwable -> 0x0e6c }
                        r2.checkBroadCastCount(r3, r7)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProblemCheck r2 = r2.mProblemCheck     // Catch:{ Throwable -> 0x0e6c }
                        r2.mIsUploadBroadCastProblem = r7     // Catch:{ Throwable -> 0x0e6c }
                    L_0x03fa:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0456
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.size()     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 <= 0) goto L_0x0456
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mFileSchedIsNotExists     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x0456
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0456 }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0456 }
                        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0456 }
                        java.util.Set r2 = r2.entrySet()     // Catch:{ Throwable -> 0x0456 }
                        java.util.Iterator r2 = r2.iterator()     // Catch:{ Throwable -> 0x0456 }
                    L_0x0424:
                        boolean r3 = r2.hasNext()     // Catch:{ Throwable -> 0x0456 }
                        if (r3 == 0) goto L_0x0456
                        java.lang.Object r3 = r2.next()     // Catch:{ Throwable -> 0x0456 }
                        java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ Throwable -> 0x0456 }
                        java.lang.Object r3 = r3.getValue()     // Catch:{ Throwable -> 0x0456 }
                        com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo r3 = (com.taobao.onlinemonitor.OnLineMonitor.ThreadIoInfo) r3     // Catch:{ Throwable -> 0x0456 }
                        if (r3 == 0) goto L_0x0424
                        boolean r4 = r3.multiplex     // Catch:{ Throwable -> 0x0456 }
                        if (r4 != 0) goto L_0x0424
                        int r4 = r3.tid     // Catch:{ Throwable -> 0x0456 }
                        if (r4 <= 0) goto L_0x0424
                        r5 = 7
                        float[] r5 = new float[r5]     // Catch:{ Throwable -> 0x0456 }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0456 }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0456 }
                        r8.getThreadIoWaitAndLoadAvg(r4, r5)     // Catch:{ Throwable -> 0x0456 }
                        r4 = r5[r14]     // Catch:{ Throwable -> 0x0456 }
                        int r4 = (int) r4     // Catch:{ Throwable -> 0x0456 }
                        r3.ioWaitCount = r4     // Catch:{ Throwable -> 0x0456 }
                        r4 = 4
                        r4 = r5[r4]     // Catch:{ Throwable -> 0x0456 }
                        int r4 = (int) r4     // Catch:{ Throwable -> 0x0456 }
                        r3.ioWaitTime = r4     // Catch:{ Throwable -> 0x0456 }
                        goto L_0x0424
                    L_0x0456:
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x04a1
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0470 }
                        r2.gc()     // Catch:{ Throwable -> 0x0470 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0470 }
                        r2.runFinalization()     // Catch:{ Throwable -> 0x0470 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0470 }
                        r2.gc()     // Catch:{ Throwable -> 0x0470 }
                        goto L_0x0479
                    L_0x0470:
                        r0 = move-exception
                        r2 = r0
                        java.lang.String r3 = "OnLineMonitor"
                        java.lang.String r4 = "手动执行内存回收异常"
                        android.util.Log.e(r3, r4, r2)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0479:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        r2.writePageInfo(r12)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0498 }
                        r2.gc()     // Catch:{ Throwable -> 0x0498 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0498 }
                        r2.runFinalization()     // Catch:{ Throwable -> 0x0498 }
                        java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0498 }
                        r2.gc()     // Catch:{ Throwable -> 0x0498 }
                        goto L_0x04a1
                    L_0x0498:
                        r0 = move-exception
                        r2 = r0
                        java.lang.String r3 = "OnLineMonitor"
                        java.lang.String r4 = "手动执行内存回收异常"
                        android.util.Log.e(r3, r4, r2)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x04a1:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.mIsInBackGround = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r2 = r2.mThreadInfoHashMap     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x04b8
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.HashMap<java.lang.String, com.taobao.onlinemonitor.ThreadInfo> r2 = r2.mThreadInfoHashMap     // Catch:{ Throwable -> 0x0e6c }
                        r2.clear()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x04b8:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.getCpuInfo(r7, r7, r7)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x04d2
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r3 = new com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo     // Catch:{ Throwable -> 0x0e6c }
                        r3.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        r2.mResourceUsedInfoCalBgApp = r3     // Catch:{ Throwable -> 0x0e6c }
                    L_0x04d2:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.mPidJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        r2.pidJiffyTime = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.mSystemTotalCpuTime     // Catch:{ Throwable -> 0x0e6c }
                        r2.totalJiffyTime = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.mSystemRunCpuTime     // Catch:{ Throwable -> 0x0e6c }
                        r2.systemJiffyTime = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.mPidJiffyTime     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = (int) r3     // Catch:{ Throwable -> 0x0e6c }
                        r2.threadStart = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.mSystemTotalCpuTime     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = (int) r3     // Catch:{ Throwable -> 0x0e6c }
                        r2.threadEnd = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        long r3 = r3.mSystemRunCpuTime     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = (int) r3     // Catch:{ Throwable -> 0x0e6c }
                        r2.threadMax = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        r2.memStart = r12     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$ResourceUsedInfo r2 = r2.mResourceUsedInfoCalBgApp     // Catch:{ Throwable -> 0x0e6c }
                        r2.memEnd = r12     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.evaluateSystemPerformance()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.evaluatePidPerformance()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.calculateSystemCheckValue()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3 = -1
                        r2.mCheckAnrCounter = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.removeMessages(r14)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 11
                        int r4 = com.taobao.onlinemonitor.OnLineMonitorApp.sToSleepTime     // Catch:{ Throwable -> 0x0e6c }
                        long r4 = (long) r4     // Catch:{ Throwable -> 0x0e6c }
                        r2.sendEmptyMessageDelayed(r3, r4)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r3 = r3.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r4 = 500(0x1f4, float:7.0E-43)
                        android.os.Message r3 = r3.obtainMessage(r15, r6, r4)     // Catch:{ Throwable -> 0x0e6c }
                        r2.sendMessage(r3)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x058f:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.evaluateSystemPerformance()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.evaluatePidPerformance()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.calculateSystemCheckValue()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r2 = r2.performanceInfo     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 100
                        r2.appProgressImportance = r3     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x05b2:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mHandler     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x05bc
                        goto L_0x0e71
                    L_0x05bc:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.mCheckAnrCounter     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 > r15) goto L_0x06fc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.mCheckAnrCounter     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 >= 0) goto L_0x05ce
                        goto L_0x06fc
                    L_0x05ce:
                        long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0e6c }
                        r4 = 1000000(0xf4240, double:4.940656E-318)
                        long r2 = r2 / r4
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = r4.mIsCheckAnrStat     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x068a
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r4 = r4.mCheckAnrTime     // Catch:{ Throwable -> 0x0e6c }
                        r6 = 0
                        long r4 = r2 - r4
                        int r6 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                        if (r6 <= 0) goto L_0x06dc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r4.mAnrCount     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 + r7
                        r4.mAnrCount = r5     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r4 = r4.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r4 = r4.performanceInfo     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mAnrCount     // Catch:{ Throwable -> 0x0e6c }
                        r4.anrCount = r5     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = com.taobao.onlinemonitor.OnLineMonitor.sIsNormalDebug     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0624
                        java.lang.String r4 = "OnLineMonitor"
                        java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r5.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = "ANR in "
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = r6.mActivityName     // Catch:{ Throwable -> 0x0e6c }
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0e6c }
                        android.util.Log.e(r4, r5)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0624:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r4 = r4.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0680
                        java.util.Map r4 = java.lang.Thread.getAllStackTraces()     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = com.taobao.onlinemonitor.OnLineMonitor.sApiLevel     // Catch:{ Throwable -> 0x0e6c }
                        r6 = 23
                        if (r5 <= r6) goto L_0x0653
                        if (r4 == 0) goto L_0x0653
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Thread r5 = r5.mMainThread     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 == 0) goto L_0x0653
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Thread r5 = r5.mMainThread     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Thread r6 = r6.mMainThread     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.StackTraceElement[] r6 = r6.getStackTrace()     // Catch:{ Throwable -> 0x0e6c }
                        r4.put(r5, r6)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0653:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r5 = r5.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.size()     // Catch:{ Throwable -> 0x0e6c }
                        r6 = 0
                    L_0x065e:
                        if (r6 >= r5) goto L_0x0680
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r7 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r7 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r7 = r7.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Object r7 = r7.get(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnlineStatistics r7 = (com.taobao.onlinemonitor.OnlineStatistics) r7     // Catch:{ Throwable -> 0x0e6c }
                        if (r7 == 0) goto L_0x067d
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r10 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r10 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r10 = r10.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r11 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r11 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r11 = r11.mActivityName     // Catch:{ Throwable -> 0x0e6c }
                        r7.onAnr(r10, r11, r4)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x067d:
                        int r6 = r6 + 1
                        goto L_0x065e
                    L_0x0680:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 60
                        r4.notifyOnlineRuntimeStat(r5)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x06dc
                    L_0x068a:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = r4.mIsCheckAnrStat     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 != 0) goto L_0x06dc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r4 = r4.mCheckAnrTime     // Catch:{ Throwable -> 0x0e6c }
                        r6 = 0
                        long r4 = r2 - r4
                        r10 = 2000(0x7d0, double:9.88E-321)
                        int r6 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
                        if (r6 < 0) goto L_0x06dc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = r4.mIsInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 != 0) goto L_0x06dc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r4.mCheckAnrTime = r2     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.mIsCheckAnrStat = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r2.mCheckAnrCounter     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3 + r7
                        r2.mCheckAnrCounter = r3     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mHandler     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r3 = r3.mHandler     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Message r3 = r3.obtainMessage(r14)     // Catch:{ Throwable -> 0x0e6c }
                        r2.sendMessageAtFrontOfQueue(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.sendEmptyMessageDelayed(r14, r8)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x06dc:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r4.mCheckAnrTime = r2     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.mIsCheckAnrStat = r12     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.removeMessages(r14)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.sendEmptyMessageDelayed(r14, r8)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x06fc:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3 = -1
                        r2.mCheckAnrCounter = r3     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x0705:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify> r3 = r3.mOnLineMonitorNotifyList     // Catch:{ Throwable -> 0x0e6c }
                        monitor-enter(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x07a1 }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify> r4 = r4.mOnLineMonitorNotifyList     // Catch:{ all -> 0x07a1 }
                        int r4 = r4.size()     // Catch:{ all -> 0x07a1 }
                    L_0x0716:
                        if (r12 >= r4) goto L_0x079e
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x07a1 }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify> r5 = r5.mOnLineMonitorNotifyList     // Catch:{ all -> 0x07a1 }
                        java.lang.Object r5 = r5.get(r12)     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineMonitorNotify r5 = (com.taobao.onlinemonitor.OnLineMonitor.OnLineMonitorNotify) r5     // Catch:{ all -> 0x07a1 }
                        if (r5 != 0) goto L_0x0727
                        goto L_0x0798
                    L_0x0727:
                        int r6 = r2.arg1     // Catch:{ all -> 0x07a1 }
                        boolean r7 = com.taobao.onlinemonitor.TraceDetail.sTraceOnLineListener     // Catch:{ all -> 0x07a1 }
                        if (r7 == 0) goto L_0x073a
                        long r7 = java.lang.System.nanoTime()     // Catch:{ all -> 0x07a1 }
                        r13 = 1000000(0xf4240, double:4.940656E-318)
                        long r7 = r7 / r13
                        long r13 = android.os.Debug.threadCpuTimeNanos()     // Catch:{ all -> 0x07a1 }
                        goto L_0x073c
                    L_0x073a:
                        r7 = r10
                        r13 = r7
                    L_0x073c:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Exception -> 0x0745 }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Exception -> 0x0745 }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r9 = r9.mOnLineStat     // Catch:{ Exception -> 0x0745 }
                        r5.onLineMonitorNotify(r6, r9)     // Catch:{ Exception -> 0x0745 }
                    L_0x0745:
                        boolean r9 = com.taobao.onlinemonitor.TraceDetail.sTraceOnLineListener     // Catch:{ all -> 0x07a1 }
                        if (r9 == 0) goto L_0x0798
                        long r18 = java.lang.System.nanoTime()     // Catch:{ all -> 0x07a1 }
                        r15 = 1000000(0xf4240, double:4.940656E-318)
                        long r18 = r18 / r15
                        long r20 = android.os.Debug.threadCpuTimeNanos()     // Catch:{ all -> 0x07a1 }
                        r9 = 0
                        long r7 = r18 - r7
                        int r9 = com.taobao.onlinemonitor.TraceDetail.sTraceOnLineDuration     // Catch:{ all -> 0x07a1 }
                        long r10 = (long) r9     // Catch:{ all -> 0x07a1 }
                        int r9 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
                        if (r9 < 0) goto L_0x0798
                        com.taobao.onlinemonitor.TraceDetail$MethodInfo r9 = new com.taobao.onlinemonitor.TraceDetail$MethodInfo     // Catch:{ all -> 0x07a1 }
                        r9.<init>()     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r10 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor r10 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x07a1 }
                        java.lang.String r10 = r10.mActivityName     // Catch:{ all -> 0x07a1 }
                        r9.activityName = r10     // Catch:{ all -> 0x07a1 }
                        r10 = 0
                        long r20 = r20 - r13
                        r10 = 1000000(0xf4240, double:4.940656E-318)
                        long r13 = r20 / r10
                        r9.cpuTime = r13     // Catch:{ all -> 0x07a1 }
                        r9.realTime = r7     // Catch:{ all -> 0x07a1 }
                        r9.priority = r6     // Catch:{ all -> 0x07a1 }
                        java.lang.Class r5 = r5.getClass()     // Catch:{ all -> 0x07a1 }
                        java.lang.String r5 = r5.getName()     // Catch:{ all -> 0x07a1 }
                        r9.methodName = r5     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.TraceDetail r5 = r5.mTraceDetail     // Catch:{ all -> 0x07a1 }
                        if (r5 == 0) goto L_0x0798
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ all -> 0x07a1 }
                        com.taobao.onlinemonitor.TraceDetail r5 = r5.mTraceDetail     // Catch:{ all -> 0x07a1 }
                        java.util.ArrayList<com.taobao.onlinemonitor.TraceDetail$MethodInfo> r5 = r5.mOnLineMonitorNotifyTimeList     // Catch:{ all -> 0x07a1 }
                        r5.add(r9)     // Catch:{ all -> 0x07a1 }
                    L_0x0798:
                        int r12 = r12 + 1
                        r10 = 0
                        goto L_0x0716
                    L_0x079e:
                        monitor-exit(r3)     // Catch:{ all -> 0x07a1 }
                        goto L_0x0e71
                    L_0x07a1:
                        r0 = move-exception
                        r2 = r0
                        monitor-exit(r3)     // Catch:{ all -> 0x07a1 }
                        throw r2     // Catch:{ Throwable -> 0x0e6c }
                    L_0x07a5:
                        r3 = -1
                        long r8 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0e6c }
                        r10 = 1000000(0xf4240, double:4.940656E-318)
                        long r8 = r8 / r10
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r10 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r10 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r10 = r10.mLastCPUCheckTime     // Catch:{ Throwable -> 0x0e6c }
                        r18 = 0
                        long r10 = r8 - r10
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3.mCpuCheckIntervalControl     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = r5.mIsFullInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 == 0) goto L_0x07c8
                        goto L_0x0e71
                    L_0x07c8:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mSysGetCounter     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 < r13) goto L_0x07d4
                        int r2 = r2.arg1     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != r6) goto L_0x07d5
                    L_0x07d4:
                        r3 = 0
                    L_0x07d5:
                        long r2 = (long) r3     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
                        if (r5 >= 0) goto L_0x07f0
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r4 = r4.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r4.removeMessages(r15)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r4 = r4.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 0
                        long r2 = r2 - r10
                        r4.sendEmptyMessageDelayed(r15, r2)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x07f0:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r2 = r2.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r2.removeMessages(r15)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.mLastCPUCheckTime = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.getCpuInfo(r7, r7, r7)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.CheckFinalizerReference r2 = r2.mCheckFinalizerReference     // Catch:{ Throwable -> 0x0e6c }
                        r2.getFinalizerReferenceQueueSize()     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x0843
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIsInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x081c
                        goto L_0x0843
                    L_0x081c:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIsInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x084a
                        long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 1000000(0xf4240, double:4.940656E-318)
                        long r2 = r2 / r5
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r5 = r5.mUIHiddenTime     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 0
                        long r2 = r2 - r5
                        r5 = 60000(0xea60, double:2.9644E-319)
                        int r8 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
                        if (r8 >= 0) goto L_0x084a
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.getMemInfo(r12)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x084a
                    L_0x0843:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.getMemInfo(r12)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x084a:
                        boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0930
                        java.lang.String r2 = "OnLineMonitor"
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r3.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "SysCPU="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mSysCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "%,MyPidCPU="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mMyPidCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "%,SysAvgCPUP="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mSysAvgCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "%,MyAvgPidCPU="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mMyAvgPidCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "%,IoWaitTime="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r5 = r5.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mRelIoWaitTime     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = ",AvgIOWaitTime="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mAvgIOWaitTime     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = ",OpenFileCount="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mOpenFileCount     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0e6c }
                        android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r2 = "OnLineMonitor"
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r3.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "进程:Io等待="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPidIoWaitCount     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = r6.mPidIoWaitCountOld     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r6
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "次, Io等待总时间="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPidIoWaitSum     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = r6.mPidIoWaitSumOld     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r6
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0e6c }
                        android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r2 = "OnLineMonitor"
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r3.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "进程:调度等待="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPidWaitCount     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = r6.mPidOldWaitCount     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r6
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "次, 调度总时间="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        float r5 = r5.mPidWaitSum     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        float r6 = r6.mPidOldWaitSum     // Catch:{ Throwable -> 0x0e6c }
                        float r5 = r5 - r6
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0e6c }
                        android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0930:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIoIsWaitNow     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x0943
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mSchedIsWaitNow     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 == 0) goto L_0x0941
                        goto L_0x0943
                    L_0x0941:
                        r2 = 0
                        goto L_0x0944
                    L_0x0943:
                        r2 = 1
                    L_0x0944:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ProcessCpuTracker r3 = r3.mProcessCpuTracker     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3.mTotalIoWaitPercent     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 30
                        r6 = 20
                        if (r3 >= r5) goto L_0x096b
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3.mPidIoWaitSum     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPidIoWaitSumOld     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3 - r5
                        r5 = 50
                        if (r3 < r5) goto L_0x0964
                        goto L_0x096b
                    L_0x0964:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3.mIoIsWaitNow = r12     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0981
                    L_0x096b:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "检测到IO有明显阻塞!"
                        r3.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3.mIoIsWaitNow = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3.notifyOnlineRuntimeStat(r6)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0981:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        float r3 = r3.mPidWaitSum     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        float r5 = r5.mPidOldWaitSum     // Catch:{ Throwable -> 0x0e6c }
                        float r3 = r3 - r5
                        r5 = 1140457472(0x43fa0000, float:500.0)
                        int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                        if (r3 >= 0) goto L_0x09c4
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3.mPidWaitCount     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPidOldWaitCount     // Catch:{ Throwable -> 0x0e6c }
                        int r3 = r3 - r5
                        r5 = 2000(0x7d0, float:2.803E-42)
                        if (r3 >= r5) goto L_0x09c4
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        float[] r3 = r3.mSystemLoadAvg     // Catch:{ Throwable -> 0x0e6c }
                        r3 = r3[r12]     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 1082130432(0x40800000, float:4.0)
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mCpuProcessCount     // Catch:{ Throwable -> 0x0e6c }
                        float r8 = (float) r8     // Catch:{ Throwable -> 0x0e6c }
                        float r8 = r8 * r5
                        int r3 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
                        if (r3 < 0) goto L_0x09bd
                        goto L_0x09c4
                    L_0x09bd:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3.mSchedIsWaitNow = r12     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x09dc
                    L_0x09c4:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "检测到线程调度有明显阻塞!"
                        r3.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r3.mSchedIsWaitNow = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r3 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r3 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 11
                        r3.notifyOnlineRuntimeStat(r5)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x09dc:
                        if (r2 == 0) goto L_0x09f5
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mSchedIsWaitNow     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x09f5
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r2 = r2.mIoIsWaitNow     // Catch:{ Throwable -> 0x0e6c }
                        if (r2 != 0) goto L_0x09f5
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.notifyOnlineRuntimeStat(r4)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x09f5:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.mSysAvgCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 80
                        r4 = 10
                        if (r2 >= r3) goto L_0x0a0b
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r2 = r2.mMyAvgPidCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3 = 80
                        if (r2 < r3) goto L_0x0a43
                    L_0x0a0b:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r3.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "检测到CPU占用过高!System="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mSysCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "%, Pid="
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mMyPidCPUPercent     // Catch:{ Throwable -> 0x0e6c }
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "%"
                        r3.append(r5)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0e6c }
                        r2.showMessage(r3)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r2 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r2 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r2.notifyOnlineRuntimeStat(r4)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0a43:
                        long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 1000000(0xf4240, double:4.940656E-318)
                        long r2 = r2 / r8
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        r5.mOldSystemRunningScore = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        r5.mOldMyPidScore = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5.evaluateSystemPerformance()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5.evaluatePidPerformance()     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = r5.mIsInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 != 0) goto L_0x0b28
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPerformanceBadTimes     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 4
                        if (r5 >= r8) goto L_0x0b28
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r5.mPerformanceCheckTimes     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 0
                        long r8 = r2 - r8
                        r10 = 5000(0x1388, double:2.4703E-320)
                        int r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                        if (r5 < 0) goto L_0x0b28
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mDevicesScore     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 60
                        if (r5 <= r8) goto L_0x0ab7
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mAvgSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 50
                        if (r5 <= r8) goto L_0x0aad
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mAvgMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 > r8) goto L_0x0ab7
                    L_0x0aad:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r5.mPerformanceBadTimes     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8 + r7
                        r5.mPerformanceBadTimes = r8     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0aef
                    L_0x0ab7:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mAvgSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mAvgMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r8
                        r8 = 25
                        if (r5 < r8) goto L_0x0ad2
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r5.mPerformanceBadTimes     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8 + r7
                        r5.mPerformanceBadTimes = r8     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0aef
                    L_0x0ad2:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mAvgSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 40
                        if (r5 <= r8) goto L_0x0ae6
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mAvgMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 40
                        if (r5 > r8) goto L_0x0aef
                    L_0x0ae6:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r5.mPerformanceBadTimes     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8 + r7
                        r5.mPerformanceBadTimes = r8     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0aef:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mPerformanceBadTimes     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 < r13) goto L_0x0b28
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r5 = r5.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r5 = r5.performanceInfo     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = r5.isLowPerformance     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 != 0) goto L_0x0b28
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r5.mPerformanceBadTimes     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8 + r7
                        r5.mPerformanceBadTimes = r8     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r5 = r5.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r5 = r5.performanceInfo     // Catch:{ Throwable -> 0x0e6c }
                        r5.isLowPerformance = r7     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r8 = "检测到设备性能低下!"
                        r5.showMessage(r8)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 35
                        r5.notifyOnlineRuntimeStat(r8)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0b28:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r5.mPerformanceCheckTimes     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 0
                        long r8 = r2 - r8
                        r10 = 10000(0x2710, double:4.9407E-320)
                        int r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                        if (r5 < 0) goto L_0x0bad
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mOldSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r8
                        if (r5 >= r4) goto L_0x0b95
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mOldMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r8
                        if (r5 < r4) goto L_0x0b56
                        goto L_0x0b95
                    L_0x0b56:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = r5.mPerformanceDecined     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 == 0) goto L_0x0bad
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mOldSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r8
                        if (r5 >= r4) goto L_0x0b7c
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r5 = r5.mMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r8 = r8.mOldMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5 - r8
                        if (r5 < r4) goto L_0x0bad
                    L_0x0b7c:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r8 = "检测到性能开始回升!"
                        r5.showMessage(r8)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 31
                        r5.notifyOnlineRuntimeStat(r8)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5.mPerformanceDecined = r12     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0bad
                    L_0x0b95:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 30
                        r5.notifyOnlineRuntimeStat(r8)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r8 = "检测到性能快速下降!"
                        r5.showMessage(r8)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5.mPerformanceDecined = r7     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0bad:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r5.mPerformanceCheckTimes = r2     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r5 = r5.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        r5.removeMessages(r15)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mCpuCheckIntervalControl     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r8 = r8.mIsInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r8 == 0) goto L_0x0bcd
                        r5 = 60000(0xea60, float:8.4078E-41)
                    L_0x0bcd:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        android.os.Handler r8 = r8.mThreadHandler     // Catch:{ Throwable -> 0x0e6c }
                        long r9 = (long) r5     // Catch:{ Throwable -> 0x0e6c }
                        r8.sendEmptyMessageDelayed(r15, r9)     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 == 0) goto L_0x0c13
                        java.lang.String r5 = "OnLineMonitor"
                        java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r8.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r9 = "SystemScore="
                        r8.append(r9)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r9 = r9.mDevicesScore     // Catch:{ Throwable -> 0x0e6c }
                        r8.append(r9)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r9 = ",SystemRunningScore="
                        r8.append(r9)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r9 = r9.mSystemRunningScore     // Catch:{ Throwable -> 0x0e6c }
                        r8.append(r9)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r9 = ",MyPidScore="
                        r8.append(r9)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        short r9 = r9.mMyPidScore     // Catch:{ Throwable -> 0x0e6c }
                        r8.append(r9)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0e6c }
                        android.util.Log.e(r5, r8)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0c13:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = r5.mIsLowMemroy     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 != 0) goto L_0x0c34
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 == r6) goto L_0x0c2e
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        r8 = 15
                        if (r5 < r8) goto L_0x0c2e
                        goto L_0x0c34
                    L_0x0c2e:
                        r5 = 0
                        r22 = -1
                    L_0x0c31:
                        r23 = 0
                        goto L_0x0c4e
                    L_0x0c34:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r5 = r5.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        boolean r5 = r5.isInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r8 = r8.mIsLowMemroy     // Catch:{ Throwable -> 0x0e6c }
                        if (r8 == 0) goto L_0x0c4b
                        java.lang.String r8 = "LowMemroy"
                        r23 = r8
                        r22 = 1
                        goto L_0x0c4e
                    L_0x0c4b:
                        r22 = 1
                        goto L_0x0c31
                    L_0x0c4e:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        if (r8 != r6) goto L_0x0c57
                        goto L_0x0c59
                    L_0x0c57:
                        r15 = r22
                    L_0x0c59:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8.mOldTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r9 = r9.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        if (r8 == r9) goto L_0x0c83
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        if (r8 == 0) goto L_0x0c83
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        if (r8 == r6) goto L_0x0c83
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r9 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r9 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r9 = r9.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        r8.mOldTrimMemoryLevel = r9     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0c83:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r8 = r8.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r8 = r8.deviceInfo     // Catch:{ Throwable -> 0x0e6c }
                        long r8 = r8.deviceTotalAvailMemory     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = (int) r8     // Catch:{ Throwable -> 0x0e6c }
                        int r8 = r8 / r6
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r6 = r6.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r6 = r6.deviceInfo     // Catch:{ Throwable -> 0x0e6c }
                        long r9 = r6.deviceTotalAvailMemory     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = (int) r9     // Catch:{ Throwable -> 0x0e6c }
                        int r6 = r6 / r4
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r9 = r4.mJavaUsedMemoryPercent     // Catch:{ Throwable -> 0x0e6c }
                        r16 = 95
                        int r4 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
                        if (r4 >= 0) goto L_0x0cf9
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r9 = r4.mAvailMemory     // Catch:{ Throwable -> 0x0e6c }
                        long r7 = (long) r8     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
                        if (r4 > 0) goto L_0x0cb3
                        goto L_0x0cf9
                    L_0x0cb3:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r7 = r4.mJavaUsedMemoryPercent     // Catch:{ Throwable -> 0x0e6c }
                        r9 = 85
                        int r4 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
                        if (r4 >= 0) goto L_0x0cca
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r7 = r4.mAvailMemory     // Catch:{ Throwable -> 0x0e6c }
                        long r9 = (long) r6     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
                        if (r4 > 0) goto L_0x0d28
                    L_0x0cca:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "内存使用过高,可用内存不足!"
                        r4.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r4 = r4.mJavaUsedMemoryPercent     // Catch:{ Throwable -> 0x0e6c }
                        r6 = 85
                        int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                        if (r8 < 0) goto L_0x0ce4
                        java.lang.String r23 = "Java85"
                    L_0x0ce1:
                        r5 = 1
                        r15 = 3
                        goto L_0x0d28
                    L_0x0ce4:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = r4.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 15
                        if (r4 >= r5) goto L_0x0cf6
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = r4.mIsLowMemroy     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0ce1
                    L_0x0cf6:
                        java.lang.String r23 = "Total90"
                        goto L_0x0ce1
                    L_0x0cf9:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "内存严重不足，即将发生OOM!"
                        r4.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r4 = r4.mJavaUsedMemoryPercent     // Catch:{ Throwable -> 0x0e6c }
                        r6 = 95
                        int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                        if (r8 < 0) goto L_0x0d13
                        java.lang.String r23 = "Java95"
                    L_0x0d10:
                        r5 = 1
                        r15 = 5
                        goto L_0x0d28
                    L_0x0d13:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = r4.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 15
                        if (r4 >= r5) goto L_0x0d25
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = r4.mIsLowMemroy     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0d10
                    L_0x0d25:
                        java.lang.String r23 = "Total95"
                        goto L_0x0d10
                    L_0x0d28:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r4 = r4.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r4 = r4.memroyStat     // Catch:{ Throwable -> 0x0e6c }
                        r4.memoryAlert = r5     // Catch:{ Throwable -> 0x0e6c }
                        if (r15 < 0) goto L_0x0e03
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = r4.mOldMemoryNotify     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == r15) goto L_0x0e03
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r4.mOldMemoryNotify = r15     // Catch:{ Throwable -> 0x0e6c }
                        if (r23 == 0) goto L_0x0dc3
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r4 = r4.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0dc3
                        java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r5 = 150(0x96, float:2.1E-43)
                        r4.<init>(r5)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ActivityLifecycleCallback r5 = r5.mActivityLifecycleCallback     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<java.lang.String> r5 = r5.mActivityStackList     // Catch:{ Throwable -> 0x0e6c }
                        if (r5 == 0) goto L_0x0d90
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.ActivityLifecycleCallback r5 = r5.mActivityLifecycleCallback     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<java.lang.String> r5 = r5.mActivityStackList     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.size()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0d69:
                        if (r5 < 0) goto L_0x0d90
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0d8d }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0d8d }
                        com.taobao.onlinemonitor.ActivityLifecycleCallback r6 = r6.mActivityLifecycleCallback     // Catch:{ Throwable -> 0x0d8d }
                        java.util.ArrayList<java.lang.String> r6 = r6.mActivityStackList     // Catch:{ Throwable -> 0x0d8d }
                        java.lang.Object r6 = r6.get(r5)     // Catch:{ Throwable -> 0x0d8d }
                        java.lang.String r6 = (java.lang.String) r6     // Catch:{ Throwable -> 0x0d8d }
                        if (r6 == 0) goto L_0x0d85
                        r7 = 64
                        int r7 = r6.indexOf(r7)     // Catch:{ Throwable -> 0x0d8d }
                        java.lang.String r6 = r6.substring(r12, r7)     // Catch:{ Throwable -> 0x0d8d }
                    L_0x0d85:
                        r4.append(r6)     // Catch:{ Throwable -> 0x0d8d }
                        r6 = 59
                        r4.append(r6)     // Catch:{ Throwable -> 0x0d8d }
                    L_0x0d8d:
                        int r5 = r5 + -1
                        goto L_0x0d69
                    L_0x0d90:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r5 = r5.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        int r5 = r5.size()     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0d9a:
                        if (r12 >= r5) goto L_0x0dc3
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r6 = r6.mOnlineStatistics     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.Object r6 = r6.get(r12)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnlineStatistics r6 = (com.taobao.onlinemonitor.OnlineStatistics) r6     // Catch:{ Throwable -> 0x0e6c }
                        if (r6 == 0) goto L_0x0dc0
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r7 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r7 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r8 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r8 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r9 = r8.mActivityName     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r10 = r4.toString()     // Catch:{ Throwable -> 0x0e6c }
                        r11 = 0
                        r8 = r23
                        r6.onMemoryProblem(r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0dc0:
                        int r12 = r12 + 1
                        goto L_0x0d9a
                    L_0x0dc3:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.TraceDetail r4 = r4.mTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0dfc
                        if (r15 != r14) goto L_0x0dd7
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "OOM预警,内存使用接近极限!"
                        r4.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0dfc
                    L_0x0dd7:
                        if (r15 != r13) goto L_0x0de3
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = "内存使用过高,可用内存不足!"
                        r4.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0dfc
                    L_0x0de3:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        int r4 = r4.mTrimMemoryLevel     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0dfc
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r5 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r5 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.TraceDetail r5 = r5.mTraceDetail     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = r5.memoryStatus()     // Catch:{ Throwable -> 0x0e6c }
                        r4.showMessage(r5)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0dfc:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r4.notifyOnlineRuntimeStat(r15)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0e03:
                        boolean r4 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0e5b
                        java.lang.String r4 = "OnLineMonitor"
                        java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0e6c }
                        r5.<init>()     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = "设备剩余内存="
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r6 = r6.mAvailMemory     // Catch:{ Throwable -> 0x0e6c }
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = ",进程使用内存="
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r6 = r6.mTotalUsedMemory     // Catch:{ Throwable -> 0x0e6c }
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = ",JavaUsed="
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r6 = r6.mDalvikPss     // Catch:{ Throwable -> 0x0e6c }
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = ",JavaUsedPercent="
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r6 = r6.mJavaUsedMemoryPercent     // Catch:{ Throwable -> 0x0e6c }
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r6 = "%,NativeHeap="
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r6 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r6 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        long r6 = r6.mNativeHeapPss     // Catch:{ Throwable -> 0x0e6c }
                        r5.append(r6)     // Catch:{ Throwable -> 0x0e6c }
                        java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0e6c }
                        android.util.Log.e(r4, r5)     // Catch:{ Throwable -> 0x0e6c }
                    L_0x0e5b:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        boolean r4 = r4.mIsInBackGround     // Catch:{ Throwable -> 0x0e6c }
                        if (r4 == 0) goto L_0x0e64
                        goto L_0x0e71
                    L_0x0e64:
                        com.taobao.onlinemonitor.OnLineMonitor$MyHandlerThread r4 = com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.this     // Catch:{ Throwable -> 0x0e6c }
                        com.taobao.onlinemonitor.OnLineMonitor r4 = com.taobao.onlinemonitor.OnLineMonitor.this     // Catch:{ Throwable -> 0x0e6c }
                        r4.checkToStopPerformance(r2)     // Catch:{ Throwable -> 0x0e6c }
                        goto L_0x0e71
                    L_0x0e6c:
                        r0 = move-exception
                        r2 = r0
                        r2.printStackTrace()
                    L_0x0e71:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.OnLineMonitor.MyHandlerThread.AnonymousClass1.handleMessage(android.os.Message):void");
                }
            };
            OnLineMonitor.this.onHandlerThreadPrepared();
            if (!OnLineMonitor.this.mThreadHandler.hasMessages(5)) {
                OnLineMonitor.this.mCheckAnrTime = System.nanoTime() / 1000000;
                OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(5, 5000);
            }
            if (OnLineMonitor.sIsTraceDetail) {
                OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(12, (long) TraceDetail.sTraceThreadInterval);
            }
            OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(9, 2000);
            if (OnLineMonitor.this.mDevicesScore < 60) {
                Process.setThreadPriority(OnLineMonitor.sThreadPriorty + 1);
            }
        }
    }

    static Map createArrayMap() {
        Map map;
        try {
            map = sApiLevel >= 19 ? (Map) Class.forName("android.util.ArrayMap").newInstance() : (Map) Class.forName("androidx.collection.ArrayMap").newInstance();
        } catch (Throwable unused) {
            map = null;
        }
        return map == null ? new HashMap() : map;
    }

    public class MyCallback implements ComponentCallbacks2 {
        public void onConfigurationChanged(Configuration configuration) {
        }

        public MyCallback() {
        }

        public void onLowMemory() {
            OnLineMonitor.this.mOnLineStat.memroyStat.isLowMemroy = true;
            OnLineMonitor.this.mThreadHandler.sendEmptyMessage(2);
        }

        public void onTrimMemory(int i) {
            OnLineMonitor.this.mTrimMemoryLevel = i;
            OnLineMonitor.this.mCheckAnrTime = System.nanoTime() / 1000000;
            if (OnLineMonitor.sIsDetailDebug || OnLineMonitor.sIsTraceDetail) {
                Log.e(OnLineMonitor.TAG, "onTrimMemory. Level=" + i);
            }
            OnLineMonitor.this.mOnLineStat.memroyStat.trimMemoryLevel = i;
            if (i != 20) {
                if (i == 40 || i == 80 || i == 60) {
                    OnLineMonitor.this.mThreadHandler.removeMessages(8);
                    OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(8, TBToast.Duration.MEDIUM);
                } else {
                    OnLineMonitor.this.mThreadHandler.removeMessages(8);
                    OnLineMonitor.this.mThreadHandler.sendEmptyMessageDelayed(8, 5000);
                }
            }
            OnLineMonitor.this.mThreadHandler.sendEmptyMessage(2);
        }
    }
}
