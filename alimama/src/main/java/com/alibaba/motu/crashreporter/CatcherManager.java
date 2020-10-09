package com.alibaba.motu.crashreporter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.motu.crashreporter.Utils;
import com.alibaba.motu.crashreporter.async.AsyncThreadPool;
import com.alibaba.motu.crashreporter.ignores.NonSystemThreadIgnore;
import com.alibaba.motu.crashreporter.ignores.UncaughtExceptionIgnore;
import com.alibaba.motu.crashreporter.memory.MemoryTracker;
import com.alibaba.motu.tbrest.utils.AppUtils;
import com.alibaba.motu.tbrest.utils.StringUtils;
import com.coloros.mcssdk.mode.CommandMessage;
import com.taobao.android.tlog.protocol.model.joint.point.BackgroundJointPoint;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.weex.BuildConfig;
import com.uc.crashsdk.export.CrashApi;
import com.uc.crashsdk.export.VersionInfo;
import com.uc.webview.export.media.MessageID;
import java.io.File;
import java.io.FileFilter;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

final class CatcherManager {
    private static final int MAX_COUNT = 100;
    private static final String TAG = "CatcherManager";
    /* access modifiers changed from: private */
    public String[] activityInfoList = new String[100];
    /* access modifiers changed from: private */
    public String lastUrl = null;
    ANRCatcher mANRCatcher;
    final String mAppVersion;
    Configuration mConfiguration;
    Context mContext;
    CrashApi mCrashApi = null;
    String mCurrentViewName;
    boolean mIsForeground = false;
    Application.ActivityLifecycleCallbacks mLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        private static final String KEY_PRE = "track";
        private int count = 0;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        /* access modifiers changed from: private */
        public AtomicInteger index = new AtomicInteger(0);
        private int mStartCount;

        public void onActivityCreated(Activity activity, Bundle bundle) {
            CatcherManager.this.mCurrentViewName = activity.getClass().getName();
        }

        public void onActivityStarted(Activity activity) {
            LogUtil.d("onActivityStarted：" + activity.getClass().getName());
            this.mStartCount = this.mStartCount + 1;
            Intent intent = activity.getIntent();
            String dataString = intent != null ? intent.getDataString() : null;
            if (dataString == null) {
                dataString = BuildConfig.buildJavascriptFrameworkVersion;
            }
            if (!CatcherManager.this.mIsForeground) {
                CatcherManager.this.mIsForeground = true;
                CatcherManager.this.mCrashApi.setForeground(CatcherManager.this.mIsForeground);
                LogUtil.d("nativeSetForeground foreground");
                appendActivityInfo(activity, "onForeground", dataString);
            } else {
                appendActivityInfo(activity, UmbrellaConstants.LIFECYCLE_START, dataString);
            }
            CatcherManager.this.mCurrentViewName = activity.getClass().getName();
            CatcherManager.this.addNativeHeaderInfo(Constants.CONTROLLER, CatcherManager.this.mCurrentViewName);
            CatcherManager.this.addNativeHeaderInfo(Constants.FOREGROUND, String.valueOf(CatcherManager.this.mIsForeground));
            String unused = CatcherManager.this.lastUrl = dataString;
            CatcherManager.this.mCrashApi.addHeaderInfo("last_page_url", dataString);
        }

        public void onActivityResumed(Activity activity) {
            LogUtil.d("onActivityResumed：" + activity.getClass().getName());
        }

        public void onActivityPaused(Activity activity) {
            LogUtil.d("onActivityPaused：" + activity.getClass().getName());
        }

        public void onActivityStopped(Activity activity) {
            LogUtil.d("onActivityStopped：" + activity.getClass().getName());
            this.mStartCount = this.mStartCount + -1;
            Intent intent = activity.getIntent();
            String dataString = intent != null ? intent.getDataString() : null;
            if (dataString == null) {
                dataString = BuildConfig.buildJavascriptFrameworkVersion;
            }
            if (this.mStartCount > 0 || !CatcherManager.this.mIsForeground) {
                appendActivityInfo(activity, MessageID.onStop, dataString);
                return;
            }
            this.mStartCount = 0;
            CatcherManager.this.mIsForeground = false;
            CatcherManager.this.mCurrentViewName = BackgroundJointPoint.TYPE;
            CatcherManager.this.mCrashApi.setForeground(CatcherManager.this.mIsForeground);
            LogUtil.d("nativeSetForeground background");
            CatcherManager.this.addNativeHeaderInfo(Constants.FOREGROUND, String.valueOf(CatcherManager.this.mIsForeground));
            appendActivityInfo(activity, "onBackground", dataString);
        }

        @TargetApi(14)
        private void appendActivityInfo(Activity activity, String str, String str2) {
            final int i = this.count;
            this.count++;
            final String simpleName = activity.getClass().getSimpleName();
            final String str3 = str;
            final String str4 = str2;
            final Activity activity2 = activity;
            AsyncThreadPool.threadPoolExecutor.submit(new Runnable() {
                public void run() {
                    StringBuilder sb;
                    try {
                        AnonymousClass1.this.date.setTime(System.currentTimeMillis());
                        sb = new StringBuilder();
                        sb.append(simpleName);
                        sb.append("_");
                        sb.append(str3);
                        sb.append(" ,data:");
                        sb.append(str4);
                        sb.append(" ,");
                        sb.append(AnonymousClass1.this.dateFormat.format(AnonymousClass1.this.date));
                        Debug.MemoryInfo realTimeStatus = MemoryTracker.getRealTimeStatus(activity2);
                        if (realTimeStatus != null) {
                            sb.append(" ,totalPss:");
                            sb.append(realTimeStatus.getTotalPss() >> 10);
                            sb.append(" ,dalvikPss:");
                            sb.append(realTimeStatus.dalvikPss >> 10);
                            sb.append(" ,nativePss:");
                            sb.append(realTimeStatus.nativePss >> 10);
                            if (Build.VERSION.SDK_INT >= 23) {
                                sb.append(" ,graphics:");
                                sb.append(Integer.valueOf(realTimeStatus.getMemoryStat("summary.graphics")).intValue() >> 10);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } catch (Throwable unused) {
                        return;
                    }
                    String sb2 = sb.toString();
                    int andIncrement = AnonymousClass1.this.index.getAndIncrement() % 100;
                    String[] access$300 = CatcherManager.this.activityInfoList;
                    access$300[andIncrement] = "track_" + andIncrement + ": " + i + ":" + sb2;
                    CrashApi crashApi = CatcherManager.this.mCrashApi;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("track_");
                    sb3.append(andIncrement);
                    String sb4 = sb3.toString();
                    crashApi.addHeaderInfo(sb4, i + ":" + sb2);
                }
            });
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            LogUtil.d("onActivitySaveInstanceState：" + activity.getClass().getName());
        }

        public void onActivityDestroyed(Activity activity) {
            LogUtil.d("onActivityDestroyed：" + activity.getClass().getName());
        }
    };
    String mProcessName;
    ReportBuilder mReportBuilder;
    ReporterContext mReporterContext;
    SendManager mSendManager;
    StorageManager mStorageManager;
    UCNativeExceptionCatcher mUCNativeExceptionCatcher;
    UncaughtExceptionCatcher mUncaughtExceptionCatcher;

    interface UncaughtExceptionLinster {
        Map<String, Object> onUncaughtException(Thread thread, Throwable th);

        boolean originalEquals(Object obj);
    }

    public CatcherManager(Context context, String str, ReporterContext reporterContext, Configuration configuration, StorageManager storageManager, ReportBuilder reportBuilder, SendManager sendManager) {
        this.mReporterContext = reporterContext;
        this.mContext = context;
        this.mProcessName = str;
        this.mConfiguration = configuration;
        this.mStorageManager = storageManager;
        this.mReportBuilder = reportBuilder;
        this.mSendManager = sendManager;
        if (this.mReporterContext != null) {
            this.mAppVersion = this.mReporterContext.getProperty(Constants.APP_VERSION);
        } else {
            this.mAppVersion = "DEFAULT";
        }
        if (configuration.getBoolean(Configuration.enableUncaughtExceptionCatch, true)) {
            long currentTimeMillis = System.currentTimeMillis();
            this.mUncaughtExceptionCatcher = new UncaughtExceptionCatcher();
            this.mUncaughtExceptionCatcher.addIgnore(new NonSystemThreadIgnore());
            LogUtil.d("CrashSDK UncaughtExceptionCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms.");
        }
        if (configuration.getBoolean(Configuration.enableNativeExceptionCatch, true)) {
            long currentTimeMillis2 = System.currentTimeMillis();
            this.mUCNativeExceptionCatcher = new UCNativeExceptionCatcher(context);
            LogUtil.d("CrashSDK UCNativeExceptionCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - currentTimeMillis2) + "ms.");
        }
        if (configuration.getBoolean(Configuration.enableANRCatch, true)) {
            long currentTimeMillis3 = System.currentTimeMillis();
            this.mANRCatcher = new ANRCatcher();
            MotuCrashReporter.getInstance().asyncTaskThread.start(this.mANRCatcher);
            LogUtil.d("CrashSDK ANRCatcher initialize complete elapsed time:" + (System.currentTimeMillis() - currentTimeMillis3) + "ms.");
        }
        if (configuration.getBoolean(Configuration.enableMainLoopBlockCatch, true)) {
            LogUtil.d("CrashSDK MainLoopCatcher initialize failure，please use MotuWatch SDK ");
        }
    }

    /* access modifiers changed from: package-private */
    public void enable() {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.enable(this.mContext);
        }
        if (this.mUCNativeExceptionCatcher != null) {
            this.mUCNativeExceptionCatcher.enable();
        }
    }

    /* access modifiers changed from: package-private */
    public void disable() {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.disable();
        }
        if (this.mUCNativeExceptionCatcher != null) {
            this.mUCNativeExceptionCatcher.disable();
        }
    }

    /* access modifiers changed from: package-private */
    public void addUncaughtExceptionIgnore(UncaughtExceptionIgnore uncaughtExceptionIgnore) {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.addIgnore(uncaughtExceptionIgnore);
        }
    }

    /* access modifiers changed from: package-private */
    public void addUncaughtExceptionLinster(UncaughtExceptionLinster uncaughtExceptionLinster) {
        if (this.mUncaughtExceptionCatcher != null) {
            this.mUncaughtExceptionCatcher.addLinster(uncaughtExceptionLinster);
        }
    }

    /* access modifiers changed from: package-private */
    public List<UncaughtExceptionLinster> getAllUncaughtExceptionLinster() {
        if (this.mUncaughtExceptionCatcher != null) {
            return this.mUncaughtExceptionCatcher.getAllLinster();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void doScan() {
        this.mUCNativeExceptionCatcher.doScan();
        this.mANRCatcher.doScan();
    }

    public void refreshNativeInfo() {
        this.mUCNativeExceptionCatcher.refreshNativeInfo();
    }

    public void addNativeHeaderInfo(String str, String str2) {
        this.mUCNativeExceptionCatcher.addNativeHeaderInfo(str, str2);
    }

    public void closeNativeSignalTerm() {
        this.mUCNativeExceptionCatcher.closeNativeSignalTerm();
    }

    @TargetApi(14)
    public void registerLifeCallbacks(Context context) {
        Application application;
        if ((this.mConfiguration.getBoolean(Configuration.enableUncaughtExceptionCatch, true) || this.mConfiguration.getBoolean(Configuration.enableNativeExceptionCatch, true)) && context != null) {
            if (context instanceof Application) {
                application = (Application) context;
            } else {
                application = (Application) context.getApplicationContext();
            }
            if (application != null) {
                LogUtil.d("register lifecycle callbacks");
                application.registerActivityLifecycleCallbacks(this.mLifecycleCallbacks);
            }
        }
    }

    class UncaughtExceptionLinsterAdapterCopyOnWriteArrayList extends CopyOnWriteArrayList<UncaughtExceptionLinster> {
        private static final long serialVersionUID = 4393313111950638180L;

        UncaughtExceptionLinsterAdapterCopyOnWriteArrayList() {
        }

        public boolean remove(Object obj) {
            Iterator it = iterator();
            while (it.hasNext()) {
                UncaughtExceptionLinster uncaughtExceptionLinster = (UncaughtExceptionLinster) it.next();
                if (uncaughtExceptionLinster.originalEquals(obj)) {
                    return super.remove(uncaughtExceptionLinster);
                }
            }
            return false;
        }
    }

    class UncaughtExceptionCatcher implements Thread.UncaughtExceptionHandler {
        Context context;
        private AtomicInteger count = new AtomicInteger(0);
        volatile boolean enable;
        Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
        CopyOnWriteArrayList<UncaughtExceptionIgnore> mIgnoreList = new CopyOnWriteArrayList<>();
        UncaughtExceptionLinsterAdapterCopyOnWriteArrayList mLinsterList = new UncaughtExceptionLinsterAdapterCopyOnWriteArrayList();

        UncaughtExceptionCatcher() {
        }

        public void enable(Context context2) {
            if (context2 != null) {
                this.context = context2;
            }
            if (!this.enable) {
                this.mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                "com.android.internal.osRuntimeInit$UncaughtHandler".equals(this.mDefaultUncaughtExceptionHandler.getClass().getName());
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.enable = true;
            }
        }

        public void disable() {
            if (this.enable) {
                if (this.mDefaultUncaughtExceptionHandler != null) {
                    Thread.setDefaultUncaughtExceptionHandler(this.mDefaultUncaughtExceptionHandler);
                }
                this.enable = false;
            }
        }

        public boolean addIgnore(UncaughtExceptionIgnore uncaughtExceptionIgnore) {
            if (uncaughtExceptionIgnore == null || !StringUtils.isNotBlank(uncaughtExceptionIgnore.getName())) {
                return false;
            }
            return this.mIgnoreList.add(uncaughtExceptionIgnore);
        }

        public boolean addLinster(UncaughtExceptionLinster uncaughtExceptionLinster) {
            if (uncaughtExceptionLinster != null) {
                return this.mLinsterList.add(uncaughtExceptionLinster);
            }
            return false;
        }

        public List<UncaughtExceptionLinster> getAllLinster() {
            return this.mLinsterList;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|(1:2)|3|4|5|(1:7)(1:8)|9|(1:11)(1:12)|13|14|15|(2:17|(6:20|21|22|(2:24|64)(1:63)|62|18))|29|30|(1:32)|33|34|(3:39|(1:41)(1:67)|35)|42|43|44|(1:46)|47|(1:49)|50|(2:52|(1:54)(1:55))(1:56)|57|60|61) */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0063, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            com.alibaba.motu.crashreporter.LogUtil.w("call linster onUncaughtException", r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x010c, code lost:
            r7 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x010d, code lost:
            com.alibaba.motu.crashreporter.LogUtil.e("externalData", r7);
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x003a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x007b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00bb */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0047 A[Catch:{ Throwable -> 0x0063, Throwable -> 0x010c }] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0072 A[Catch:{ Throwable -> 0x007b }] */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x008e A[Catch:{ Throwable -> 0x00bb }] */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x00c3 A[Catch:{ Throwable -> 0x0063, Throwable -> 0x010c }] */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x00d0 A[Catch:{ Throwable -> 0x0063, Throwable -> 0x010c }] */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x00df A[Catch:{ Throwable -> 0x0063, Throwable -> 0x010c }] */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x00f5 A[Catch:{ Throwable -> 0x0063, Throwable -> 0x010c }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void onUncaughtException(java.lang.Thread r5, java.lang.Throwable r6, boolean r7) {
            /*
                r4 = this;
                java.util.HashMap r0 = new java.util.HashMap
                r0.<init>()
                if (r7 == 0) goto L_0x000e
                java.lang.String r7 = "REPORT_IGNORE"
                java.lang.String r1 = "true"
                r0.put(r7, r1)
            L_0x000e:
                java.lang.String r7 = "aliab"
                com.alibaba.motu.tbrest.SendService r1 = com.alibaba.motu.tbrest.SendService.getInstance()     // Catch:{ Throwable -> 0x003a }
                java.lang.String r1 = r1.aliab     // Catch:{ Throwable -> 0x003a }
                if (r1 == 0) goto L_0x001f
                com.alibaba.motu.tbrest.SendService r1 = com.alibaba.motu.tbrest.SendService.getInstance()     // Catch:{ Throwable -> 0x003a }
                java.lang.String r1 = r1.aliab     // Catch:{ Throwable -> 0x003a }
                goto L_0x0021
            L_0x001f:
                java.lang.String r1 = "null"
            L_0x0021:
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x003a }
                java.lang.String r7 = "aliabTest"
                com.alibaba.motu.tbrest.SendService r1 = com.alibaba.motu.tbrest.SendService.getInstance()     // Catch:{ Throwable -> 0x003a }
                java.lang.String r1 = r1.aliabTest     // Catch:{ Throwable -> 0x003a }
                if (r1 == 0) goto L_0x0035
                com.alibaba.motu.tbrest.SendService r1 = com.alibaba.motu.tbrest.SendService.getInstance()     // Catch:{ Throwable -> 0x003a }
                java.lang.String r1 = r1.aliabTest     // Catch:{ Throwable -> 0x003a }
                goto L_0x0037
            L_0x0035:
                java.lang.String r1 = "null"
            L_0x0037:
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x003a }
            L_0x003a:
                com.alibaba.motu.crashreporter.CatcherManager r7 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x010c }
                com.alibaba.motu.crashreporter.Configuration r7 = r7.mConfiguration     // Catch:{ Throwable -> 0x010c }
                java.lang.String r1 = "Configuration.enableExternalLinster"
                r2 = 1
                boolean r7 = r7.getBoolean(r1, r2)     // Catch:{ Throwable -> 0x010c }
                if (r7 == 0) goto L_0x006a
                com.alibaba.motu.crashreporter.CatcherManager$UncaughtExceptionLinsterAdapterCopyOnWriteArrayList r7 = r4.mLinsterList     // Catch:{ Throwable -> 0x010c }
                java.util.Iterator r7 = r7.iterator()     // Catch:{ Throwable -> 0x010c }
            L_0x004d:
                boolean r1 = r7.hasNext()     // Catch:{ Throwable -> 0x010c }
                if (r1 == 0) goto L_0x006a
                java.lang.Object r1 = r7.next()     // Catch:{ Throwable -> 0x010c }
                com.alibaba.motu.crashreporter.CatcherManager$UncaughtExceptionLinster r1 = (com.alibaba.motu.crashreporter.CatcherManager.UncaughtExceptionLinster) r1     // Catch:{ Throwable -> 0x010c }
                java.util.Map r1 = r1.onUncaughtException(r5, r6)     // Catch:{ Throwable -> 0x0063 }
                if (r1 == 0) goto L_0x004d
                r0.putAll(r1)     // Catch:{ Throwable -> 0x0063 }
                goto L_0x004d
            L_0x0063:
                r1 = move-exception
                java.lang.String r2 = "call linster onUncaughtException"
                com.alibaba.motu.crashreporter.LogUtil.w(r2, r1)     // Catch:{ Throwable -> 0x010c }
                goto L_0x004d
            L_0x006a:
                java.lang.Throwable r7 = r4.getCausedThrowable(r6)     // Catch:{ Throwable -> 0x007b }
                boolean r7 = r7 instanceof java.lang.OutOfMemoryError     // Catch:{ Throwable -> 0x007b }
                if (r7 == 0) goto L_0x007b
                java.lang.String r7 = "threads list"
                java.lang.String r1 = com.alibaba.motu.crashreporter.ThreadUtils.getThreadInfos()     // Catch:{ Throwable -> 0x007b }
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x007b }
            L_0x007b:
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00bb }
                r7.<init>()     // Catch:{ Throwable -> 0x00bb }
                r1 = 0
            L_0x0081:
                com.alibaba.motu.crashreporter.CatcherManager r2 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x00bb }
                java.lang.String[] r2 = r2.activityInfoList     // Catch:{ Throwable -> 0x00bb }
                int r2 = r2.length     // Catch:{ Throwable -> 0x00bb }
                if (r1 >= r2) goto L_0x00a7
                r2 = 100
                if (r1 >= r2) goto L_0x00a7
                com.alibaba.motu.crashreporter.CatcherManager r2 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x00bb }
                java.lang.String[] r2 = r2.activityInfoList     // Catch:{ Throwable -> 0x00bb }
                r2 = r2[r1]     // Catch:{ Throwable -> 0x00bb }
                boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x00bb }
                if (r3 != 0) goto L_0x00a7
                r7.append(r2)     // Catch:{ Throwable -> 0x00bb }
                java.lang.String r2 = "\n"
                r7.append(r2)     // Catch:{ Throwable -> 0x00bb }
                int r1 = r1 + 1
                goto L_0x0081
            L_0x00a7:
                java.lang.String r1 = "last_page_url"
                com.alibaba.motu.crashreporter.CatcherManager r2 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x00bb }
                java.lang.String r2 = r2.lastUrl     // Catch:{ Throwable -> 0x00bb }
                r0.put(r1, r2)     // Catch:{ Throwable -> 0x00bb }
                java.lang.String r1 = "track list:"
                java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x00bb }
                r0.put(r1, r7)     // Catch:{ Throwable -> 0x00bb }
            L_0x00bb:
                android.content.Context r7 = r4.context     // Catch:{ Throwable -> 0x010c }
                java.lang.Long r7 = com.alibaba.motu.crashreporter.Utils.getContextFirstInstallTime(r7)     // Catch:{ Throwable -> 0x010c }
                if (r7 == 0) goto L_0x00c8
                java.lang.String r1 = "FIRST_INSTALL_TIME"
                r0.put(r1, r7)     // Catch:{ Throwable -> 0x010c }
            L_0x00c8:
                android.content.Context r7 = r4.context     // Catch:{ Throwable -> 0x010c }
                java.lang.Long r7 = com.alibaba.motu.crashreporter.Utils.getContextLastUpdateTime(r7)     // Catch:{ Throwable -> 0x010c }
                if (r7 == 0) goto L_0x00d5
                java.lang.String r1 = "LAST_UPDATE_TIME"
                r0.put(r1, r7)     // Catch:{ Throwable -> 0x010c }
            L_0x00d5:
                com.alibaba.motu.crashreporter.CatcherManager r7 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x010c }
                java.lang.String r7 = r7.mCurrentViewName     // Catch:{ Throwable -> 0x010c }
                boolean r7 = android.text.TextUtils.isEmpty(r7)     // Catch:{ Throwable -> 0x010c }
                if (r7 == 0) goto L_0x00f5
                com.alibaba.motu.crashreporter.CatcherManager r7 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x010c }
                boolean r7 = r7.mIsForeground     // Catch:{ Throwable -> 0x010c }
                if (r7 == 0) goto L_0x00ed
                java.lang.String r7 = "_controller"
                java.lang.String r1 = "noActivity:foreground"
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x010c }
                goto L_0x00fe
            L_0x00ed:
                java.lang.String r7 = "_controller"
                java.lang.String r1 = "noActivity:background"
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x010c }
                goto L_0x00fe
            L_0x00f5:
                java.lang.String r7 = "_controller"
                com.alibaba.motu.crashreporter.CatcherManager r1 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x010c }
                java.lang.String r1 = r1.mCurrentViewName     // Catch:{ Throwable -> 0x010c }
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x010c }
            L_0x00fe:
                java.lang.String r7 = "_foreground"
                com.alibaba.motu.crashreporter.CatcherManager r1 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ Throwable -> 0x010c }
                boolean r1 = r1.mIsForeground     // Catch:{ Throwable -> 0x010c }
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Throwable -> 0x010c }
                r0.put(r7, r1)     // Catch:{ Throwable -> 0x010c }
                goto L_0x0112
            L_0x010c:
                r7 = move-exception
                java.lang.String r1 = "externalData"
                com.alibaba.motu.crashreporter.LogUtil.e(r1, r7)
            L_0x0112:
                com.alibaba.motu.crashreporter.CatcherManager r7 = com.alibaba.motu.crashreporter.CatcherManager.this
                com.alibaba.motu.crashreporter.ReportBuilder r7 = r7.mReportBuilder
                com.alibaba.motu.crashreporter.CrashReport r5 = r7.buildUncaughtExceptionReport(r6, r5, r0)
                com.alibaba.motu.crashreporter.CatcherManager r6 = com.alibaba.motu.crashreporter.CatcherManager.this
                com.alibaba.motu.crashreporter.SendManager r6 = r6.mSendManager
                r6.sendReport(r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.CatcherManager.UncaughtExceptionCatcher.onUncaughtException(java.lang.Thread, java.lang.Throwable, boolean):void");
        }

        private Throwable getCausedThrowable(Throwable th) {
            Throwable th2;
            Throwable cause = th.getCause();
            while (true) {
                Throwable th3 = cause;
                th2 = th;
                th = th3;
                if (th == null || th2 == th) {
                    return th2;
                }
                cause = th.getCause();
            }
            return th2;
        }

        public void uncaughtException(Thread thread, Throwable th) {
            long currentTimeMillis = System.currentTimeMillis();
            try {
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    sb.append("\tat " + stackTraceElement);
                }
                TLogAdapter.log(CatcherManager.TAG, thread.getName(), sb.toString());
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
            try {
                LogUtil.d(String.format("catch uncaught exception:%s on thread:%s.", new Object[]{thread.getName(), th.toString()}));
                boolean booleanValue = Utils.isMainThread(thread).booleanValue();
                if (CatcherManager.this.mConfiguration.getBoolean(Configuration.enableUncaughtExceptionIgnore, true) && !booleanValue) {
                    Iterator<UncaughtExceptionIgnore> it = this.mIgnoreList.iterator();
                    while (it.hasNext()) {
                        if (it.next().uncaughtExceptionIgnore(thread, th)) {
                            onUncaughtException(thread, th, true);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.e("ignore uncaught exception.", e);
            } catch (Throwable th3) {
                LogUtil.e("uncaught exception.", th3);
            }
            if (1 == this.count.addAndGet(1)) {
                onUncaughtException(thread, th, false);
            } else {
                LogUtil.i("uncaught exception count: " + this.count.get());
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            LogUtil.d("catch uncaught exception complete elapsed time:" + (currentTimeMillis2 - currentTimeMillis) + ".ms");
            if (this.mDefaultUncaughtExceptionHandler != null) {
                this.mDefaultUncaughtExceptionHandler.uncaughtException(thread, th);
            }
        }
    }

    class UCNativeExceptionCatcher {
        volatile boolean enable = false;
        volatile boolean initCrashsdkSuccess = false;
        Context mContext;
        File mSystemTombstonesDir;
        String mSystemTombstonesDirPath;
        File mUCCrashsdkBackupDir;
        String mUCCrashsdkBackupDirPath;
        File mUCCrashsdkLogsDir;
        String mUCCrashsdkLogsDirPath;
        File mUCCrashsdkTagsDir;
        String mUCCrashsdkTagsDirPath;
        File mUCCrashsdkTombstonesDir;
        String mUCCrashsdkTombstonesDirPath;

        public void closeNativeSignalTerm() {
        }

        public UCNativeExceptionCatcher(Context context) {
            this.mContext = context;
            this.mUCCrashsdkTombstonesDirPath = CatcherManager.this.mStorageManager.mProcessTombstoneDirPath + File.separator + "crashsdk";
            this.mUCCrashsdkTagsDirPath = this.mUCCrashsdkTombstonesDirPath + File.separator + CommandMessage.TYPE_TAGS;
            this.mUCCrashsdkLogsDirPath = this.mUCCrashsdkTombstonesDirPath + File.separator + TLogInitializer.DEFAULT_DIR;
            this.mUCCrashsdkBackupDirPath = this.mUCCrashsdkTombstonesDirPath + File.separator + "backup";
            this.mUCCrashsdkTombstonesDir = new File(this.mUCCrashsdkTombstonesDirPath);
            this.mUCCrashsdkTagsDir = new File(this.mUCCrashsdkTagsDirPath);
            this.mUCCrashsdkLogsDir = new File(this.mUCCrashsdkLogsDirPath);
            this.mUCCrashsdkBackupDir = new File(this.mUCCrashsdkBackupDirPath);
            if (!this.mUCCrashsdkTombstonesDir.exists()) {
                this.mUCCrashsdkTombstonesDir.mkdirs();
            }
            if (!this.mUCCrashsdkTagsDir.exists()) {
                this.mUCCrashsdkTagsDir.mkdirs();
            }
            if (!this.mUCCrashsdkLogsDir.exists()) {
                this.mUCCrashsdkLogsDir.mkdirs();
            }
            if (!this.mUCCrashsdkBackupDir.exists()) {
                this.mUCCrashsdkBackupDir.mkdirs();
            }
            Bundle bundle = new Bundle();
            String name = CatcherManager.this.mStorageManager.mTombstoneDir.getName();
            bundle.putBoolean("mBackupLogs", false);
            bundle.putString("mLogsBackupPathName", this.mUCCrashsdkBackupDirPath);
            bundle.putString("mTagFilesFolderName", name + "/" + CatcherManager.this.mStorageManager.mProcessName + "/crashsdk/tags");
            bundle.putString("mCrashLogsFolderName", name + "/" + CatcherManager.this.mStorageManager.mProcessName + "/crashsdk/logs");
            StringBuilder sb = new StringBuilder();
            sb.append("java_");
            sb.append(System.currentTimeMillis());
            sb.append("_java.log");
            bundle.putString("mJavaCrashLogFileName", sb.toString());
            bundle.putString("mNativeCrashLogFileName", "native_" + System.currentTimeMillis() + "_jni.log");
            bundle.putBoolean("enableJavaLog", false);
            bundle.putBoolean("enableUnexpLog", true);
            bundle.putBoolean("mCallJavaDefaultHandler", false);
            bundle.putBoolean("mCallNativeDefaultHandler", true);
            bundle.putBoolean("mZipLog", false);
            bundle.putBoolean("mEnableStatReport", false);
            bundle.putBoolean("useApplicationContext", false);
            bundle.putBoolean("mSyncUploadSetupCrashLogs", false);
            bundle.putInt("mDisableSignals", 16384);
            bundle.putInt("mDisableBackgroundSignals", 16384);
            bundle.putString("mBuildId", CatcherManager.this.mAppVersion);
            CatcherManager.this.mCrashApi = CrashApi.createInstanceEx(context, "native", false, bundle);
            CatcherManager.this.mCrashApi.registerCallback(1, new ValueCallback<Bundle>(CatcherManager.this) {
                public void onReceiveValue(Bundle bundle) {
                    try {
                        TLogAdapter.log(CatcherManager.TAG, "native", "crash happened");
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            CatcherManager.this.mCrashApi.registerCallback(4, new ValueCallback<Bundle>(CatcherManager.this) {
                public void onReceiveValue(Bundle bundle) {
                    String string = bundle.getString("filePathName");
                    String string2 = bundle.getString("processName");
                    if (!TextUtils.isEmpty(string)) {
                        File file = new File(string);
                        if (file.exists()) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("processName", string2);
                            CatcherManager.this.mSendManager.sendReport(CatcherManager.this.mReportBuilder.buildNativeExceptionReport(file, hashMap));
                        }
                    }
                }
            });
            initCrashsdkSo((String) null);
            LogUtil.d("nativeSetForeground set background after startup");
        }

        /* access modifiers changed from: package-private */
        public void initCrashsdkSo(String str) {
            try {
                System.currentTimeMillis();
                File file = new File(str, "libcrashsdk.so");
                if (file.exists()) {
                    System.load(file.getPath());
                } else {
                    System.loadLibrary("crashsdk");
                }
                CatcherManager.this.mCrashApi.crashSoLoaded();
                CatcherManager.this.mCrashApi.setForeground(true);
                VersionInfo versionInfo = new VersionInfo();
                versionInfo.mVersion = CatcherManager.this.mAppVersion;
                versionInfo.mBuildId = CatcherManager.this.mAppVersion;
                CatcherManager.this.mCrashApi.updateVersionInfo(versionInfo);
                this.initCrashsdkSuccess = true;
            } catch (Throwable th) {
                LogUtil.e("init uc crashsdk", th);
            }
        }

        public void refreshNativeInfo() {
            if (this.initCrashsdkSuccess) {
                try {
                    VersionInfo versionInfo = new VersionInfo();
                    versionInfo.mBuildId = CatcherManager.this.mAppVersion;
                    versionInfo.mVersion = CatcherManager.this.mAppVersion;
                    CatcherManager.this.mCrashApi.updateVersionInfo(versionInfo);
                } catch (Throwable th) {
                    LogUtil.e("refresh native version info", th);
                }
            }
        }

        public void addNativeHeaderInfo(String str, String str2) {
            if (this.initCrashsdkSuccess) {
                try {
                    CatcherManager.this.mCrashApi.addHeaderInfo(str, str2);
                } catch (Exception e) {
                    LogUtil.e("refresh native header info", e);
                } catch (UnsatisfiedLinkError unused) {
                    LogUtil.i("not impl this method  nativeAddHeaderInfo");
                }
            }
        }

        public void setNativeForeground(boolean z) {
            if (this.initCrashsdkSuccess) {
                try {
                    CatcherManager.this.mCrashApi.setForeground(z);
                } catch (Exception e) {
                    LogUtil.e("setNativeForeground", e);
                } catch (UnsatisfiedLinkError unused) {
                    LogUtil.i("not impl this method  setNativeForeground");
                }
            }
        }

        public void enable() {
            if (this.initCrashsdkSuccess && !this.enable) {
                this.enable = true;
            }
        }

        public void disable() {
            if (this.initCrashsdkSuccess && this.enable) {
                try {
                    CatcherManager.this.mCrashApi.disableLog(1);
                } catch (Exception e) {
                    LogUtil.e("disable crashsdk", e);
                }
                this.enable = false;
            }
        }

        /* access modifiers changed from: private */
        public void doScan() {
            File[] listFiles;
            long currentTimeMillis = System.currentTimeMillis();
            try {
                if (this.mUCCrashsdkLogsDir != null && this.mUCCrashsdkLogsDir.exists() && (listFiles = this.mUCCrashsdkLogsDir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        if (file.getName().endsWith("jni.log") && file.canRead()) {
                            return true;
                        }
                        file.delete();
                        return false;
                    }
                })) != null && listFiles.length > 0) {
                    for (File buildNativeExceptionReport : listFiles) {
                        CatcherManager.this.mSendManager.sendReport(CatcherManager.this.mReportBuilder.buildNativeExceptionReport(buildNativeExceptionReport, new HashMap()));
                    }
                }
            } catch (Exception e) {
                LogUtil.e("find uc native log.", e);
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            LogUtil.d("find uc native log complete elapsed time:" + (currentTimeMillis2 - currentTimeMillis) + ".ms");
        }
    }

    class ANRCatcher implements Runnable {
        volatile boolean canScan = false;
        volatile boolean enable = false;
        File mProcessANRFlagFile;
        File mSystemTraceFile;
        String mSystemTraceFilePath;
        AtomicBoolean scaning = new AtomicBoolean(false);

        public ANRCatcher() {
        }

        public void run() {
            try {
                this.mSystemTraceFilePath = "/data/anr/traces.txt";
                this.mSystemTraceFile = new File(this.mSystemTraceFilePath);
                if (!this.mSystemTraceFile.exists()) {
                    String str = Utils.SystemPropertiesUtils.get("dalvik.vm.stack-trace-file");
                    if (!this.mSystemTraceFile.equals(str)) {
                        try {
                            this.mSystemTraceFile = new File(str);
                            this.mSystemTraceFilePath = str;
                        } catch (Exception e) {
                            LogUtil.e("system traces file error", e);
                        }
                    }
                }
                if (this.mSystemTraceFile != null) {
                    this.mProcessANRFlagFile = CatcherManager.this.mStorageManager.getProcessTombstoneFile("ANR_MONITOR");
                    if (this.mProcessANRFlagFile.exists() || AppUtils.writeFile(this.mProcessANRFlagFile, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())))) {
                        this.canScan = true;
                    }
                }
            } catch (Exception e2) {
                LogUtil.e("anr catcher error ", e2);
            }
        }

        public class TracesFinder {
            boolean found = false;
            File mSystemTraceFile;
            String strEndFlag = "";
            String strPid = "";
            String strProcessName = "";
            String strStartFlag = "";
            String strTriggerTime = "";

            public TracesFinder(File file) {
                this.mSystemTraceFile = file;
            }

            /* JADX WARNING: Removed duplicated region for block: B:14:0x002e A[SYNTHETIC, Splitter:B:14:0x002e] */
            /* JADX WARNING: Removed duplicated region for block: B:18:0x0039 A[SYNTHETIC, Splitter:B:18:0x0039] */
            /* JADX WARNING: Removed duplicated region for block: B:60:0x00fa A[SYNTHETIC, Splitter:B:60:0x00fa] */
            /* JADX WARNING: Removed duplicated region for block: B:66:0x0108 A[SYNTHETIC, Splitter:B:66:0x0108] */
            /* JADX WARNING: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void find() {
                /*
                    r9 = this;
                    r0 = 0
                    java.io.File r1 = r9.mSystemTraceFile     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    if (r1 == 0) goto L_0x00e4
                    java.io.File r1 = r9.mSystemTraceFile     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    boolean r1 = r1.exists()     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    if (r1 != 0) goto L_0x000f
                    goto L_0x00e4
                L_0x000f:
                    java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    java.io.File r4 = r9.mSystemTraceFile     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    r3.<init>(r4)     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    r2.<init>(r3)     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    r1.<init>(r2)     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                L_0x0020:
                    java.lang.String r0 = r1.readLine()     // Catch:{ IOException -> 0x00e2 }
                    if (r0 == 0) goto L_0x002c
                    boolean r2 = com.alibaba.motu.tbrest.utils.StringUtils.isNotBlank(r0)     // Catch:{ IOException -> 0x00e2 }
                    if (r2 == 0) goto L_0x0020
                L_0x002c:
                    if (r0 != 0) goto L_0x0039
                    r1.close()     // Catch:{ IOException -> 0x0032 }
                    goto L_0x0038
                L_0x0032:
                    r0 = move-exception
                    java.lang.String r1 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r1, r0)
                L_0x0038:
                    return
                L_0x0039:
                    java.lang.String r2 = r1.readLine()     // Catch:{ IOException -> 0x00e2 }
                    if (r2 != 0) goto L_0x004a
                    r1.close()     // Catch:{ IOException -> 0x0043 }
                    goto L_0x0049
                L_0x0043:
                    r0 = move-exception
                    java.lang.String r1 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r1, r0)
                L_0x0049:
                    return
                L_0x004a:
                    java.lang.String r3 = "-----\\spid\\s(\\d+?)\\sat\\s(.+?)\\s-----"
                    java.util.regex.Pattern r3 = java.util.regex.Pattern.compile(r3)     // Catch:{ IOException -> 0x00e2 }
                    java.util.regex.Matcher r3 = r3.matcher(r0)     // Catch:{ IOException -> 0x00e2 }
                    boolean r4 = r3.find()     // Catch:{ IOException -> 0x00e2 }
                    if (r4 == 0) goto L_0x00de
                    r4 = 1
                    java.lang.String r5 = r3.group(r4)     // Catch:{ IOException -> 0x00e2 }
                    r9.strPid = r5     // Catch:{ IOException -> 0x00e2 }
                    r5 = 2
                    java.lang.String r3 = r3.group(r5)     // Catch:{ IOException -> 0x00e2 }
                    r9.strTriggerTime = r3     // Catch:{ IOException -> 0x00e2 }
                    java.lang.String r3 = "Cmd\\sline:\\s(.+)"
                    java.util.regex.Pattern r3 = java.util.regex.Pattern.compile(r3)     // Catch:{ IOException -> 0x00e2 }
                    java.util.regex.Matcher r2 = r3.matcher(r2)     // Catch:{ IOException -> 0x00e2 }
                    boolean r3 = r2.find()     // Catch:{ IOException -> 0x00e2 }
                    if (r3 == 0) goto L_0x00de
                    java.lang.String r2 = r2.group(r4)     // Catch:{ IOException -> 0x00e2 }
                    r9.strProcessName = r2     // Catch:{ IOException -> 0x00e2 }
                    java.lang.String r2 = r9.strProcessName     // Catch:{ IOException -> 0x00e2 }
                    com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher r3 = com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.this     // Catch:{ IOException -> 0x00e2 }
                    com.alibaba.motu.crashreporter.CatcherManager r3 = com.alibaba.motu.crashreporter.CatcherManager.this     // Catch:{ IOException -> 0x00e2 }
                    java.lang.String r3 = r3.mProcessName     // Catch:{ IOException -> 0x00e2 }
                    boolean r2 = r2.equals(r3)     // Catch:{ IOException -> 0x00e2 }
                    if (r2 == 0) goto L_0x00de
                    com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher r2 = com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.this     // Catch:{ IOException -> 0x00e2 }
                    java.io.File r2 = r2.mProcessANRFlagFile     // Catch:{ IOException -> 0x00e2 }
                    java.lang.String r2 = com.alibaba.motu.tbrest.utils.AppUtils.readLine((java.io.File) r2)     // Catch:{ IOException -> 0x00e2 }
                    boolean r3 = com.alibaba.motu.tbrest.utils.StringUtils.isNotBlank(r2)     // Catch:{ IOException -> 0x00e2 }
                    if (r3 == 0) goto L_0x00de
                    java.text.SimpleDateFormat r3 = new java.text.SimpleDateFormat     // Catch:{ Exception -> 0x00d8 }
                    java.lang.String r5 = "yyyy-MM-dd HH:mm:ss"
                    r3.<init>(r5)     // Catch:{ Exception -> 0x00d8 }
                    java.util.Date r2 = r3.parse(r2)     // Catch:{ Exception -> 0x00d8 }
                    java.lang.String r5 = r9.strTriggerTime     // Catch:{ Exception -> 0x00d8 }
                    java.util.Date r3 = r3.parse(r5)     // Catch:{ Exception -> 0x00d8 }
                    long r5 = r3.getTime()     // Catch:{ Exception -> 0x00d8 }
                    long r2 = r2.getTime()     // Catch:{ Exception -> 0x00d8 }
                    int r7 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
                    if (r7 <= 0) goto L_0x00de
                    com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher r2 = com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.this     // Catch:{ Exception -> 0x00d8 }
                    java.io.File r2 = r2.mProcessANRFlagFile     // Catch:{ Exception -> 0x00d8 }
                    java.lang.String r3 = r9.strTriggerTime     // Catch:{ Exception -> 0x00d8 }
                    boolean r2 = com.alibaba.motu.tbrest.utils.AppUtils.writeFile(r2, r3)     // Catch:{ Exception -> 0x00d8 }
                    if (r2 == 0) goto L_0x00de
                    r9.strStartFlag = r0     // Catch:{ Exception -> 0x00d8 }
                    java.lang.String r0 = "----- end %s -----"
                    java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x00d8 }
                    r3 = 0
                    java.lang.String r5 = r9.strPid     // Catch:{ Exception -> 0x00d8 }
                    r2[r3] = r5     // Catch:{ Exception -> 0x00d8 }
                    java.lang.String r0 = java.lang.String.format(r0, r2)     // Catch:{ Exception -> 0x00d8 }
                    r9.strEndFlag = r0     // Catch:{ Exception -> 0x00d8 }
                    r9.found = r4     // Catch:{ Exception -> 0x00d8 }
                    goto L_0x00de
                L_0x00d8:
                    r0 = move-exception
                    java.lang.String r2 = "compare triggerTime"
                    com.alibaba.motu.crashreporter.LogUtil.e(r2, r0)     // Catch:{ IOException -> 0x00e2 }
                L_0x00de:
                    r1.close()     // Catch:{ IOException -> 0x00fe }
                    goto L_0x0104
                L_0x00e2:
                    r0 = move-exception
                    goto L_0x00f3
                L_0x00e4:
                    java.lang.String r1 = "try to find system trace file, but file not exist. "
                    com.alibaba.motu.crashreporter.LogUtil.e(r1)     // Catch:{ IOException -> 0x00ef, all -> 0x00ea }
                    return
                L_0x00ea:
                    r1 = move-exception
                    r8 = r1
                    r1 = r0
                    r0 = r8
                    goto L_0x0106
                L_0x00ef:
                    r1 = move-exception
                    r8 = r1
                    r1 = r0
                    r0 = r8
                L_0x00f3:
                    java.lang.String r2 = "do scan traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r2, r0)     // Catch:{ all -> 0x0105 }
                    if (r1 == 0) goto L_0x0104
                    r1.close()     // Catch:{ IOException -> 0x00fe }
                    goto L_0x0104
                L_0x00fe:
                    r0 = move-exception
                    java.lang.String r1 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r1, r0)
                L_0x0104:
                    return
                L_0x0105:
                    r0 = move-exception
                L_0x0106:
                    if (r1 == 0) goto L_0x0112
                    r1.close()     // Catch:{ IOException -> 0x010c }
                    goto L_0x0112
                L_0x010c:
                    r1 = move-exception
                    java.lang.String r2 = "close traces file"
                    com.alibaba.motu.crashreporter.LogUtil.e(r2, r1)
                L_0x0112:
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.TracesFinder.find():void");
            }
        }

        public void doScan() {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.canScan && this.scaning.compareAndSet(false, true)) {
                try {
                    MotuCrashReporter.getInstance().asyncTaskThread.start(new Runnable() {
                        public void run() {
                            try {
                                long uptimeMillis = SystemClock.uptimeMillis();
                                TracesFinder tracesFinder = new TracesFinder(ANRCatcher.this.mSystemTraceFile);
                                tracesFinder.find();
                                long uptimeMillis2 = SystemClock.uptimeMillis();
                                LogUtil.d("CatcherManager scan anr time:" + (uptimeMillis2 - uptimeMillis));
                                if (tracesFinder.found) {
                                    CatcherManager.this.mSendManager.sendReport(CatcherManager.this.mReportBuilder.buildANRReport(tracesFinder, new HashMap()));
                                }
                            } catch (Exception e) {
                                LogUtil.e("send anr report", e);
                            }
                        }
                    });
                } catch (Exception e) {
                    LogUtil.e("do scan traces file", e);
                }
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            LogUtil.d("scaning anr complete elapsed time:" + (currentTimeMillis2 - currentTimeMillis) + ".ms");
        }
    }
}
