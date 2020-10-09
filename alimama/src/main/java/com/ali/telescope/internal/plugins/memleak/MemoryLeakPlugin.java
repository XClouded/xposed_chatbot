package com.ali.telescope.internal.plugins.memleak;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import com.ali.telescope.base.event.ActivityEvent;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.TMSharePreferenceUtil;
import com.ali.telescope.util.TelescopeLog;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import org.json.JSONObject;

public class MemoryLeakPlugin extends Plugin {
    private static final int CHECK_DEDLAY = 5000;
    private static final int CHECK_TIMES_THRESHOLD = 20;
    private static final long DAY = 86400;
    private static final String KEY_PERF = "last_memleak_time";
    private static final String TAG = "MemoryLeak";
    private List<Object> containerForNotGcCollect = new ArrayList();
    /* access modifiers changed from: private */
    public boolean isBackground;
    /* access modifiers changed from: private */
    public volatile boolean isLeakAnalyzing;
    /* access modifiers changed from: private */
    public Application mApplication;
    /* access modifiers changed from: private */
    public File mDumpFile;
    private int mIntervalDays = 14;
    /* access modifiers changed from: private */
    public boolean mIsDestroyed;
    /* access modifiers changed from: private */
    public Runnable mLeakAnalyzeRunnable = new Runnable() {
        public void run() {
            new Thread("leak-analyze") {
                public void run() {
                    super.run();
                    TelescopeLog.w(MemoryLeakPlugin.TAG, "start memory leak");
                    boolean unused = MemoryLeakPlugin.this.isLeakAnalyzing = true;
                    boolean unused2 = MemoryLeakPlugin.this.mStop = true;
                    MemoryLeakPlugin.this.mTelescopeContext.requestPause(7, MemoryLeakPlugin.this.pluginID, MemoryLeakPlugin.this.priority);
                    TMSharePreferenceUtil.putLong(MemoryLeakPlugin.this.mApplication, MemoryLeakPlugin.KEY_PERF, System.currentTimeMillis());
                    MemoryLeak.forkAndAnalyze(MemoryLeakPlugin.this.mDumpFile.getPath(), MemoryLeakPlugin.this.mResultFile.getPath(), KeyedWeakReference.class.getName(), MemoryLeakPlugin.this.mApplication.getPackageName());
                    MemoryLeakPlugin.this.mTelescopeContext.requestResume(7, MemoryLeakPlugin.this.pluginID, MemoryLeakPlugin.this.priority);
                    boolean unused3 = MemoryLeakPlugin.this.isLeakAnalyzing = false;
                }
            }.start();
        }
    };
    /* access modifiers changed from: private */
    public Runnable mLeakCheckRunnable = new Runnable() {
        public void run() {
            if (!MemoryLeakPlugin.this.mIsDestroyed && !MemoryLeakPlugin.this.mStop) {
                HashSet hashSet = new HashSet(MemoryLeakPlugin.this.mWeakHashMap.keySet());
                TelescopeLog.d(MemoryLeakPlugin.TAG, "map size : " + MemoryLeakPlugin.this.mWeakHashMap.size());
                ArrayList<Activity> arrayList = new ArrayList<>();
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    Activity activity = (Activity) it.next();
                    if (activity != null) {
                        TelescopeLog.d(MemoryLeakPlugin.TAG, activity + " check");
                        int intValue = ((Integer) MemoryLeakPlugin.this.mWeakHashMap.get(activity)).intValue() + 1;
                        MemoryLeakPlugin.this.mWeakHashMap.put(activity, Integer.valueOf(intValue));
                        if (intValue > 20) {
                            arrayList.add(activity);
                            TelescopeLog.e(MemoryLeakPlugin.TAG, activity.getClass() + " may leak");
                        }
                    }
                }
                TelescopeLog.d(MemoryLeakPlugin.TAG, "leak size : " + arrayList.size() + " isBackground: " + MemoryLeakPlugin.this.isBackground);
                if (arrayList.size() <= 0 || !MemoryLeakPlugin.this.isBackground) {
                    MemoryLeakPlugin.this.mWorkHandler.removeCallbacks(MemoryLeakPlugin.this.mLeakCheckRunnable);
                    MemoryLeakPlugin.this.mWorkHandler.postDelayed(MemoryLeakPlugin.this.mLeakCheckRunnable, 5000);
                    return;
                }
                for (Activity generateLeakKey : arrayList) {
                    MemoryLeakPlugin.this.generateLeakKey(generateLeakKey);
                }
                MemoryLeakPlugin.this.mWorkHandler.post(MemoryLeakPlugin.this.mLeakAnalyzeRunnable);
                MemoryLeakPlugin.this.mWorkHandler.removeCallbacks(MemoryLeakPlugin.this.mLeakCheckRunnable);
            }
        }
    };
    private Runnable mResultCheck = new Runnable() {
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0091 A[SYNTHETIC, Splitter:B:32:0x0091] */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x00a6 A[SYNTHETIC, Splitter:B:39:0x00a6] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r8 = this;
                com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin r0 = com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin.this
                java.io.File r0 = r0.mResultFile
                boolean r0 = r0.exists()
                if (r0 == 0) goto L_0x00b8
                r0 = 0
                java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin r4 = com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin.this     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                java.io.File r4 = r4.mResultFile     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                r3.<init>(r4)     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                r2.<init>(r3)     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                r1.<init>(r2)     // Catch:{ Exception -> 0x0088, all -> 0x0083 }
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0081 }
                r0.<init>()     // Catch:{ Exception -> 0x0081 }
                java.lang.String r2 = ""
            L_0x0029:
                java.lang.String r3 = r1.readLine()     // Catch:{ Exception -> 0x0081 }
                if (r3 == 0) goto L_0x007b
                java.lang.String r4 = "*start"
                boolean r4 = r3.equals(r4)     // Catch:{ Exception -> 0x0081 }
                if (r4 == 0) goto L_0x003c
                r3 = 0
                r0.setLength(r3)     // Catch:{ Exception -> 0x0081 }
                goto L_0x0029
            L_0x003c:
                java.lang.String r4 = "*end"
                boolean r4 = r3.equals(r4)     // Catch:{ Exception -> 0x0081 }
                if (r4 == 0) goto L_0x0071
                boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0081 }
                if (r3 != 0) goto L_0x0029
                java.lang.String r3 = r0.toString()     // Catch:{ Exception -> 0x0081 }
                java.lang.String r4 = "InputMethodManager"
                boolean r3 = r3.contains(r4)     // Catch:{ Exception -> 0x0081 }
                if (r3 != 0) goto L_0x0029
                com.ali.telescope.internal.plugins.memleak.MemoryLeakBean r3 = new com.ali.telescope.internal.plugins.memleak.MemoryLeakBean     // Catch:{ Exception -> 0x0081 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0081 }
                java.lang.String r6 = r0.toString()     // Catch:{ Exception -> 0x0081 }
                r3.<init>(r4, r2, r6)     // Catch:{ Exception -> 0x0081 }
                com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin r4 = com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin.this     // Catch:{ Exception -> 0x0081 }
                com.ali.telescope.base.plugin.ITelescopeContext r4 = r4.mTelescopeContext     // Catch:{ Exception -> 0x0081 }
                com.ali.telescope.base.report.IBeanReport r4 = r4.getBeanReport()     // Catch:{ Exception -> 0x0081 }
                r4.send(r3)     // Catch:{ Exception -> 0x0081 }
                goto L_0x0029
            L_0x0071:
                r0.append(r3)     // Catch:{ Exception -> 0x0081 }
                java.lang.String r2 = "\n"
                r0.append(r2)     // Catch:{ Exception -> 0x0081 }
                r2 = r3
                goto L_0x0029
            L_0x007b:
                r1.close()     // Catch:{ IOException -> 0x007f }
                goto L_0x0099
            L_0x007f:
                r0 = move-exception
                goto L_0x0096
            L_0x0081:
                r0 = move-exception
                goto L_0x008c
            L_0x0083:
                r1 = move-exception
                r7 = r1
                r1 = r0
                r0 = r7
                goto L_0x00a4
            L_0x0088:
                r1 = move-exception
                r7 = r1
                r1 = r0
                r0 = r7
            L_0x008c:
                r0.printStackTrace()     // Catch:{ all -> 0x00a3 }
                if (r1 == 0) goto L_0x0099
                r1.close()     // Catch:{ IOException -> 0x0095 }
                goto L_0x0099
            L_0x0095:
                r0 = move-exception
            L_0x0096:
                r0.printStackTrace()
            L_0x0099:
                com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin r0 = com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin.this
                java.io.File r0 = r0.mResultFile
                r0.delete()
                goto L_0x00b8
            L_0x00a3:
                r0 = move-exception
            L_0x00a4:
                if (r1 == 0) goto L_0x00ae
                r1.close()     // Catch:{ IOException -> 0x00aa }
                goto L_0x00ae
            L_0x00aa:
                r1 = move-exception
                r1.printStackTrace()
            L_0x00ae:
                com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin r1 = com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin.this
                java.io.File r1 = r1.mResultFile
                r1.delete()
                throw r0
            L_0x00b8:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin.AnonymousClass1.run():void");
        }
    };
    /* access modifiers changed from: private */
    public File mResultFile;
    /* access modifiers changed from: private */
    public boolean mStop;
    /* access modifiers changed from: private */
    public ITelescopeContext mTelescopeContext;
    /* access modifiers changed from: private */
    public WeakHashMap<Activity, Integer> mWeakHashMap = new WeakHashMap<>();
    /* access modifiers changed from: private */
    public Handler mWorkHandler;
    private boolean requestPaused;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        if (jSONObject != null) {
            this.mIntervalDays = jSONObject.optInt("interval_days", this.mIntervalDays);
        }
        this.mDumpFile = new File(application.getCacheDir(), "dump.hprof");
        this.mResultFile = new File(application.getExternalFilesDir((String) null), "leak_result.txt");
        this.priority = 1;
        this.mApplication = application;
        this.mTelescopeContext = iTelescopeContext;
        Loopers.getTelescopeHandler().postDelayed(this.mResultCheck, 10000);
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - TMSharePreferenceUtil.getLong(application, KEY_PERF, currentTimeMillis);
        if (j == 0 || j >= ((long) this.mIntervalDays) * 86400) {
            this.mTelescopeContext.registerBroadcast(2, this.pluginID);
            this.mTelescopeContext.registerBroadcast(1, this.pluginID);
            this.mWorkHandler = Loopers.getTelescopeHandler();
            this.mWorkHandler.postDelayed(this.mLeakCheckRunnable, 5000);
            KeyedWeakReference.class.getName();
        }
    }

    public void onEvent(int i, Event event) {
        if (i == 1) {
            ActivityEvent activityEvent = (ActivityEvent) event;
            if (activityEvent.subEvent == 6) {
                this.mWeakHashMap.put(activityEvent.target, 0);
            }
        } else if (i == 2) {
            AppEvent appEvent = (AppEvent) event;
            if (appEvent.subEvent == 1) {
                this.isBackground = true;
            } else if (appEvent.subEvent == 2) {
                this.isBackground = false;
                this.mWorkHandler.removeCallbacks(this.mLeakAnalyzeRunnable);
            }
        }
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
        this.mWorkHandler.removeCallbacks(this.mLeakCheckRunnable);
        this.requestPaused = true;
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
        this.mWorkHandler.removeCallbacks(this.mLeakCheckRunnable);
        this.mWorkHandler.postDelayed(this.mLeakCheckRunnable, 5000);
        this.requestPaused = false;
    }

    public void onDestroy() {
        this.mIsDestroyed = true;
        this.mWorkHandler.removeCallbacks(this.mLeakCheckRunnable);
    }

    public boolean isPaused() {
        return this.requestPaused && !this.isLeakAnalyzing;
    }

    public String generateLeakKey(Object obj) {
        String uuid = UUID.randomUUID().toString();
        TelescopeLog.d(TAG, "watchedObject : " + obj);
        this.containerForNotGcCollect.add(new KeyedWeakReference(obj, uuid));
        return uuid;
    }
}
