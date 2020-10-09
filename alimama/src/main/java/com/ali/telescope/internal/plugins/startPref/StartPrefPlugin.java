package com.ali.telescope.internal.plugins.startPref;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.SystemClock;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.event.StartUpEvent;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.util.TelescopeLog;
import com.ali.telescope.util.TimeUtils;
import com.ali.telescope.util.process.CpuTracker;
import java.util.ArrayList;
import org.json.JSONObject;

public class StartPrefPlugin extends Plugin {
    public static final String TAG = "StartPrefPlugin";
    public static boolean sIsCodeBoot = true;
    Application application;
    private short bootActivityIndex = 0;
    private boolean[] bootActivityLaunchedList = null;
    /* access modifiers changed from: private */
    public ArrayList<String> bootActivityNameList = new ArrayList<>();
    ITelescopeContext context;
    /* access modifiers changed from: private */
    public short mActNum = 0;
    /* access modifiers changed from: private */
    public boolean mBootFinished = false;
    /* access modifiers changed from: private */
    public ColdBootCheck mColdBootCheck;
    private boolean mIsBackground = true;
    private boolean mIsFirstBoot = true;
    /* access modifiers changed from: private */
    public boolean mTerminatedWhenBooting = false;
    /* access modifiers changed from: private */
    public long sCurActivityCreatedTime = -1;
    /* access modifiers changed from: private */
    public long sFristActivityTime = -1;
    /* access modifiers changed from: private */
    public long sLaunchTime;

    private void checkBootBeginPoint() {
    }

    private void notifyBootFinished() {
    }

    public void onDestroy() {
    }

    public void onEvent(int i, Event event) {
    }

    static /* synthetic */ short access$008(StartPrefPlugin startPrefPlugin) {
        short s = startPrefPlugin.mActNum;
        startPrefPlugin.mActNum = (short) (s + 1);
        return s;
    }

    static /* synthetic */ short access$010(StartPrefPlugin startPrefPlugin) {
        short s = startPrefPlugin.mActNum;
        startPrefPlugin.mActNum = (short) (s - 1);
        return s;
    }

    static /* synthetic */ short access$708(StartPrefPlugin startPrefPlugin) {
        short s = startPrefPlugin.bootActivityIndex;
        startPrefPlugin.bootActivityIndex = (short) (s + 1);
        return s;
    }

    /* access modifiers changed from: private */
    public boolean isBootChainFinished() {
        if (this.bootActivityLaunchedList == null) {
            return false;
        }
        for (boolean z : this.bootActivityLaunchedList) {
            if (!z) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean checkCurrentActivityValid(Activity activity) {
        boolean z = false;
        if (this.bootActivityIndex >= this.bootActivityNameList.size()) {
            return false;
        }
        if (activity != null && activity.getClass().getName().endsWith(this.bootActivityNameList.get(this.bootActivityIndex))) {
            z = true;
        }
        if (this.bootActivityLaunchedList != null) {
            this.bootActivityLaunchedList[this.bootActivityIndex] = z;
        }
        return z;
    }

    public void onCreate(Application application2, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        this.context = iTelescopeContext;
        this.application = application2;
        this.bootActivityNameList = AppConfig.bootActivityNameList;
        if (this.bootActivityNameList != null && this.bootActivityNameList.size() > 0) {
            this.bootActivityLaunchedList = new boolean[this.bootActivityNameList.size()];
        }
        this.sLaunchTime = System.currentTimeMillis();
        this.mColdBootCheck = new ColdBootCheck();
        this.mColdBootCheck.startChecker();
        this.context.getBeanReport().send(new StartUpBeginBean(TimeUtils.getTime()));
        application2.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                long currentTimeMillis = System.currentTimeMillis();
                if (StartPrefPlugin.this.mActNum == 0) {
                    long unused = StartPrefPlugin.this.sCurActivityCreatedTime = currentTimeMillis;
                }
                if (StartPrefPlugin.this.sFristActivityTime < 0 || StartPrefPlugin.this.mActNum == 0) {
                    boolean unused2 = StartPrefPlugin.this.mTerminatedWhenBooting = false;
                    if (StartPrefPlugin.this.mColdBootCheck != null) {
                        StartPrefPlugin.this.mColdBootCheck.stopChecker();
                        ColdBootCheck unused3 = StartPrefPlugin.this.mColdBootCheck = null;
                    }
                    if (StartPrefPlugin.this.sFristActivityTime > 0) {
                        StartPrefPlugin.sIsCodeBoot = false;
                    }
                    if (!StartPrefPlugin.sIsCodeBoot && currentTimeMillis - StartPrefPlugin.this.sLaunchTime <= 1000) {
                        StartPrefPlugin.sIsCodeBoot = true;
                    }
                    long unused4 = StartPrefPlugin.this.sFristActivityTime = currentTimeMillis;
                }
                if (StartPrefPlugin.this.checkCurrentActivityValid(activity)) {
                    StartPrefPlugin.access$708(StartPrefPlugin.this);
                    if (activity.getClass().getName().endsWith((String) StartPrefPlugin.this.bootActivityNameList.get(StartPrefPlugin.this.bootActivityNameList.size() - 1)) && StartPrefPlugin.this.isBootChainFinished()) {
                        boolean unused5 = StartPrefPlugin.this.mBootFinished = true;
                        StartPrefPlugin.this.checkBootUIPoint();
                    }
                }
            }

            public void onActivityStarted(Activity activity) {
                StartPrefPlugin.access$008(StartPrefPlugin.this);
            }

            public void onActivityStopped(Activity activity) {
                StartPrefPlugin.access$010(StartPrefPlugin.this);
                if (StartPrefPlugin.this.mActNum < 0) {
                    short unused = StartPrefPlugin.this.mActNum = 0;
                }
                if (!StartPrefPlugin.this.mBootFinished) {
                    boolean unused2 = StartPrefPlugin.this.mTerminatedWhenBooting = true;
                }
            }
        });
        checkBootBeginPoint();
        judgeIsFirstBoot();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0085 A[SYNTHETIC, Splitter:B:27:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0090 A[SYNTHETIC, Splitter:B:32:0x0090] */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void judgeIsFirstBoot() {
        /*
            r8 = this;
            android.app.Application r0 = r8.application
            java.lang.String r0 = com.ali.telescope.util.AppUtils.getVersionName(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            android.app.Application r2 = r8.application
            java.lang.String r3 = ""
            java.lang.String r2 = com.ali.telescope.util.FileUtils.getTelescopeDataPath(r2, r3)
            r1.append(r2)
            java.lang.String r2 = "/cacheappversion"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            if (r1 != 0) goto L_0x0022
            return
        L_0x0022:
            java.io.File r2 = new java.io.File
            r2.<init>(r1)
            boolean r1 = r2.exists()
            r3 = 1
            if (r1 == 0) goto L_0x0099
            r1 = 0
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x007f }
            java.io.FileReader r5 = new java.io.FileReader     // Catch:{ Throwable -> 0x007f }
            r5.<init>(r2)     // Catch:{ Throwable -> 0x007f }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r1 = r4.readLine()     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r5 = 0
            if (r0 == 0) goto L_0x0051
            boolean r6 = r0.equals(r1)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            if (r6 == 0) goto L_0x0049
            r8.mIsFirstBoot = r5     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            goto L_0x0051
        L_0x0049:
            r8.mIsFirstBoot = r3     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r2.delete()     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r8.saveVersionInfo(r2, r0)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
        L_0x0051:
            java.lang.String r2 = "StartPrefPlugin"
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r6.<init>()     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            java.lang.String r7 = "Saved Version= "
            r6.append(r7)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r6.append(r1)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            java.lang.String r1 = ", version="
            r6.append(r1)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r6.append(r0)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            java.lang.String r0 = r6.toString()     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r3[r5] = r0     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            com.ali.telescope.util.TelescopeLog.d(r2, r3)     // Catch:{ Throwable -> 0x0079, all -> 0x0077 }
            r4.close()     // Catch:{ IOException -> 0x0089 }
            goto L_0x009e
        L_0x0077:
            r0 = move-exception
            goto L_0x008e
        L_0x0079:
            r0 = move-exception
            r1 = r4
            goto L_0x0080
        L_0x007c:
            r0 = move-exception
            r4 = r1
            goto L_0x008e
        L_0x007f:
            r0 = move-exception
        L_0x0080:
            r0.printStackTrace()     // Catch:{ all -> 0x007c }
            if (r1 == 0) goto L_0x009e
            r1.close()     // Catch:{ IOException -> 0x0089 }
            goto L_0x009e
        L_0x0089:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x009e
        L_0x008e:
            if (r4 == 0) goto L_0x0098
            r4.close()     // Catch:{ IOException -> 0x0094 }
            goto L_0x0098
        L_0x0094:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0098:
            throw r0
        L_0x0099:
            r8.mIsFirstBoot = r3
            r8.saveVersionInfo(r2, r0)
        L_0x009e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.plugins.startPref.StartPrefPlugin.judgeIsFirstBoot():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0023 A[SYNTHETIC, Splitter:B:16:0x0023] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0031 A[SYNTHETIC, Splitter:B:21:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void saveVersionInfo(java.io.File r4, java.lang.String r5) {
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
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.plugins.startPref.StartPrefPlugin.saveVersionInfo(java.io.File, java.lang.String):void");
    }

    /* access modifiers changed from: private */
    public void checkBootUIPoint() {
        long j;
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = this.sLaunchTime;
        if (!sIsCodeBoot) {
            j2 = this.sCurActivityCreatedTime;
        }
        StartUpEndBean.boot1StartTimeStamp = j2;
        if (sIsCodeBoot) {
            if (CpuTracker.mPidStartTime == 0) {
                CpuTracker.generateCpuStat();
            }
            j = SystemClock.elapsedRealtime() - CpuTracker.mPidStartTime;
        } else {
            j = 0;
        }
        long j3 = currentTimeMillis - j2;
        if (sIsCodeBoot) {
            StartUpEndBean.preparePidTime = (long) ((int) (j - j3));
        }
        if (j <= 0 || j <= j3 || j - j3 > 5000) {
            StartUpEndBean.boot1EndTimeStamp = currentTimeMillis;
            StartUpEndBean.bootDuration1 = j3;
        } else {
            currentTimeMillis += StartUpEndBean.preparePidTime / 2;
            StartUpEndBean.boot1EndTimeStamp = currentTimeMillis;
            StartUpEndBean.bootDuration1 = j3;
        }
        TelescopeLog.i("StartPrefPlugin", "StartTimeS :" + this.sLaunchTime + ", EndTimeS : " + currentTimeMillis + ", Duration :" + j3);
        StartUpEndBean.isColdBoot = sIsCodeBoot;
        if (this.mIsFirstBoot) {
            StartUpEndBean.bootType = 0;
        } else if (sIsCodeBoot) {
            StartUpEndBean.bootType = 1;
        } else {
            StartUpEndBean.bootType = 2;
        }
        if (!this.mTerminatedWhenBooting) {
            this.context.getBeanReport().send(new StartUpEndBean(System.currentTimeMillis()));
        }
        this.context.broadcastEvent(new StartUpEvent());
    }

    private void checkFirstViewLayoutedPoint() {
        long currentTimeMillis = System.currentTimeMillis();
        StartUpEndBean.bootDuration2 = currentTimeMillis - StartUpEndBean.preparePidTime;
        this.context.getBeanReport().send(new StartUpEndBean(currentTimeMillis));
        notifyBootFinished();
    }
}
