package com.alibaba.analytics.core.store;

import android.taobao.windvane.util.ConfigStorage;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SystemUtils;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.delegate.BackgroundTrigger;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

public class LogStoreMgr implements BackgroundTrigger.AppStatusChangeCallback {
    private static final int DELETE = 2;
    private static final int INSERT = 1;
    private static final int LOG_COUNT_CHECK = 5000;
    private static final Object Lock_Object = new Object();
    private static final int MAX_LOG_COUNT = 9000;
    private static final int MAX_LOG_SIZE = 45;
    private static final long STORE_INTERVAL = 5000;
    private static final String TAG = "LogStoreMgr";
    private static int logCount = 0;
    private static LogStoreMgr mInstance = new LogStoreMgr();
    public static SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private List<ILogChangeListener> mLogChangeListeners = Collections.synchronizedList(new ArrayList());
    private List<Log> mLogs = new CopyOnWriteArrayList();
    private ScheduledFuture mOneMinDBMonitorFuture = null;
    /* access modifiers changed from: private */
    public ILogStore mStore = new LogSqliteStore(Variables.getInstance().getContext());
    private ScheduledFuture mStoreFuture = null;
    private Runnable mStoreTask = new Runnable() {
        public void run() {
            LogStoreMgr.this.store();
        }
    };
    private ScheduledFuture mThrityMinDBMonitorFuture = null;

    public void onForeground() {
    }

    private LogStoreMgr() {
        TaskExecutor.getInstance().submit(new CleanDbTask());
        BackgroundTrigger.registerCallback(this);
    }

    public static LogStoreMgr getInstance() {
        return mInstance;
    }

    public void add(Log log) {
        if (Logger.isDebug()) {
            Logger.i(TAG, "Log", log.getContent());
        }
        this.mLogs.add(log);
        if (this.mLogs.size() >= 45 || Variables.getInstance().isRealTimeDebug()) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.mStoreTask, 0);
        } else if (this.mStoreFuture == null || this.mStoreFuture.isDone()) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule(this.mStoreFuture, this.mStoreTask, STORE_INTERVAL);
        }
        synchronized (Lock_Object) {
            logCount++;
            if (logCount > 5000) {
                logCount = 0;
                TaskExecutor.getInstance().submit(new CleanLogTask());
            }
        }
    }

    public void addLogAndSave(Log log) {
        add(log);
        store();
    }

    public int delete(List<Log> list) {
        return this.mStore.delete(list);
    }

    public void update(List<Log> list) {
        this.mStore.update(list);
    }

    public void updateLogPriority(List<Log> list) {
        this.mStore.updateLogPriority(list);
    }

    public List<Log> get(int i) {
        return this.mStore.get(i);
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(Unknown Source)
        	at java.util.ArrayList.get(Unknown Source)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized void store() {
        /*
            r3 = this;
            monitor-enter(r3)
            r0 = 0
            java.util.List<com.alibaba.analytics.core.model.Log> r1 = r3.mLogs     // Catch:{ Throwable -> 0x0035 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0035 }
            java.util.List<com.alibaba.analytics.core.model.Log> r2 = r3.mLogs     // Catch:{ all -> 0x0030 }
            int r2 = r2.size()     // Catch:{ all -> 0x0030 }
            if (r2 <= 0) goto L_0x0019
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x0030 }
            java.util.List<com.alibaba.analytics.core.model.Log> r2 = r3.mLogs     // Catch:{ all -> 0x0030 }
            r0.<init>(r2)     // Catch:{ all -> 0x0030 }
            java.util.List<com.alibaba.analytics.core.model.Log> r2 = r3.mLogs     // Catch:{ all -> 0x0030 }
            r2.clear()     // Catch:{ all -> 0x0030 }
        L_0x0019:
            monitor-exit(r1)     // Catch:{ all -> 0x0030 }
            if (r0 == 0) goto L_0x003d
            int r1 = r0.size()     // Catch:{ Throwable -> 0x0035 }
            if (r1 <= 0) goto L_0x003d
            com.alibaba.analytics.core.store.ILogStore r1 = r3.mStore     // Catch:{ Throwable -> 0x0035 }
            r1.insert(r0)     // Catch:{ Throwable -> 0x0035 }
            r1 = 1
            int r0 = r0.size()     // Catch:{ Throwable -> 0x0035 }
            r3.dispatcherLogChangeEvent(r1, r0)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x003d
        L_0x0030:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0030 }
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0033:
            r0 = move-exception
            goto L_0x003f
        L_0x0035:
            r0 = move-exception
            java.lang.String r1 = "LogStoreMgr"
            java.lang.String r2 = ""
            android.util.Log.w(r1, r2, r0)     // Catch:{ all -> 0x0033 }
        L_0x003d:
            monitor-exit(r3)
            return
        L_0x003f:
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.store.LogStoreMgr.store():void");
    }

    public void clear() {
        Logger.d(TAG, "[clear]");
        this.mStore.clear();
        this.mLogs.clear();
    }

    public long count() {
        Logger.d(TAG, "[count] memory count:", Integer.valueOf(this.mLogs.size()), " db count:", Integer.valueOf(this.mStore.count()));
        return (long) (this.mStore.count() + this.mLogs.size());
    }

    @Deprecated
    public long memoryCount() {
        return (long) this.mLogs.size();
    }

    public long dbCount() {
        return (long) this.mStore.count();
    }

    public void registerLogChangeListener(ILogChangeListener iLogChangeListener) {
        this.mLogChangeListeners.add(iLogChangeListener);
    }

    public void unRegisterChangeListener(ILogChangeListener iLogChangeListener) {
        this.mLogChangeListeners.remove(iLogChangeListener);
    }

    private void dispatcherLogChangeEvent(int i, int i2) {
        for (int i3 = 0; i3 < this.mLogChangeListeners.size(); i3++) {
            ILogChangeListener iLogChangeListener = this.mLogChangeListeners.get(i3);
            if (iLogChangeListener != null) {
                switch (i) {
                    case 1:
                        iLogChangeListener.onInsert((long) i2, dbCount());
                        break;
                    case 2:
                        iLogChangeListener.onDelete((long) i2, dbCount());
                        break;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public int clearOldLogByTime() {
        Logger.d();
        Calendar instance = Calendar.getInstance();
        instance.add(5, -3);
        return this.mStore.clearOldLogByField("time", String.valueOf(instance.getTimeInMillis()));
    }

    /* access modifiers changed from: private */
    public int clearOldLogByCount(int i) {
        int i2;
        if (i > 9000) {
            i2 = this.mStore.clearOldLogByCount((i - 9000) + 1000);
        } else {
            i2 = 0;
        }
        Logger.d(TAG, "clearOldLogByCount", Integer.valueOf(i2));
        return i;
    }

    public void onBackground() {
        this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.mStoreTask, 0);
        this.mOneMinDBMonitorFuture = TaskExecutor.getInstance().schedule(this.mOneMinDBMonitorFuture, new MonitorDBTask().setMin(1), 60000);
        this.mThrityMinDBMonitorFuture = TaskExecutor.getInstance().schedule(this.mThrityMinDBMonitorFuture, new MonitorDBTask().setMin(30), ConfigStorage.DEFAULT_SMALL_MAX_AGE);
    }

    class CleanDbTask implements Runnable {
        CleanDbTask() {
        }

        public void run() {
            int access$200;
            Logger.d();
            int access$000 = LogStoreMgr.this.clearOldLogByTime();
            if (access$000 > 0) {
                LogStoreMgr.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.CLEAN_DB, "time_ex", Double.valueOf((double) access$000)));
            }
            int count = LogStoreMgr.this.mStore.count();
            if (count > 9000 && (access$200 = LogStoreMgr.this.clearOldLogByCount(count)) > 0) {
                LogStoreMgr.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.CLEAN_DB, "count_ex", Double.valueOf((double) access$200)));
            }
        }
    }

    class CleanLogTask implements Runnable {
        CleanLogTask() {
        }

        public void run() {
            Logger.d(LogStoreMgr.TAG, "CleanLogTask");
            int count = LogStoreMgr.this.mStore.count();
            if (count > 9000) {
                int unused = LogStoreMgr.this.clearOldLogByCount(count);
            }
        }
    }

    class MonitorDBTask implements Runnable {
        private int min = 0;

        MonitorDBTask() {
        }

        public MonitorDBTask setMin(int i) {
            this.min = i;
            return this;
        }

        public void run() {
            try {
                int count = LogStoreMgr.this.mStore.count();
                double dbFileSize = LogStoreMgr.this.mStore.getDbFileSize();
                double systemFreeSize = SystemUtils.getSystemFreeSize();
                HashMap hashMap = new HashMap();
                hashMap.put("min", Integer.valueOf(this.min));
                hashMap.put("dbLeft", Integer.valueOf(count));
                hashMap.put("dbFileSize", Double.valueOf(dbFileSize));
                hashMap.put("freeSize", Double.valueOf(systemFreeSize));
                LogStoreMgr.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.DB_MONITOR, JSON.toJSONString(hashMap), Double.valueOf(1.0d)));
            } catch (Throwable unused) {
            }
        }
    }
}
