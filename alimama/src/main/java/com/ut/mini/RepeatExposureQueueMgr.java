package com.ut.mini;

import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.ut.mini.UTPageHitHelper;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class RepeatExposureQueueMgr implements Runnable {
    private static final Map<String, String> EMPTY_MAP = new HashMap();
    private static final String TAG = "RepeatExposureQueueMgr";
    private static RepeatExposureQueueMgr mRepeatExposureQueueMgr = new RepeatExposureQueueMgr();
    private boolean isOpenLog = false;
    private boolean isRunning = false;
    private HashSet<Integer> mExposureSet = new HashSet<>();
    private BlockingQueue<Map<String, String>> mQueueCache = new LinkedBlockingQueue();
    private Map<String, String> mSortMap = new TreeMap(new Comparator<String>() {
        public int compare(String str, String str2) {
            return str.compareTo(str2);
        }
    });

    RepeatExposureQueueMgr() {
    }

    public static RepeatExposureQueueMgr getInstance() {
        return mRepeatExposureQueueMgr;
    }

    public synchronized void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            TaskExecutor.getInstance().submit(getInstance());
            UTPageHitHelper.addPageChangerListener(new RepeatExposurePageChangerMonitor());
        }
    }

    public synchronized void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            try {
                this.mQueueCache.clear();
                this.mExposureSet.clear();
            } catch (Exception unused) {
            }
        }
    }

    public void run() {
        int i;
        int i2 = 0;
        while (true) {
            if (this.isRunning || i2 > 0) {
                try {
                    if (this.isOpenLog) {
                        Logger.d(TAG, "------");
                        Logger.d(TAG, "take mQueueCache size", Integer.valueOf(this.mQueueCache.size()));
                        Logger.d(TAG, "mExposureSet size", Integer.valueOf(this.mExposureSet.size()));
                    }
                    Map take = this.mQueueCache.take();
                    if (this.isOpenLog) {
                        long currentTimeMillis = System.currentTimeMillis();
                        i = getMapHashCode(take);
                        Logger.d(TAG, "getMapHashCode cost", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    } else {
                        i = getMapHashCode(take);
                    }
                    if (i == 0) {
                        Logger.d(TAG, "clear ExposureSet");
                        this.mExposureSet.clear();
                    } else if (this.mExposureSet.contains(Integer.valueOf(i))) {
                        Logger.d(TAG, "repeat Exposure");
                    } else {
                        this.mExposureSet.add(Integer.valueOf(i));
                        Logger.d(TAG, "send Exposure");
                        UTAnalytics.getInstance().transferLog(take);
                    }
                    int size = this.mQueueCache.size();
                    try {
                        if (this.isOpenLog) {
                            Logger.d(TAG, "isRunning", Boolean.valueOf(this.isRunning), "mQueueCache size", Integer.valueOf(size));
                        }
                        i2 = size;
                    } catch (Throwable th) {
                        int i3 = size;
                        th = th;
                        i2 = i3;
                        Logger.d("", th);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    Logger.d("", th);
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void putExposureEvent(Map<String, String> map) {
        if (this.isRunning && map != null && !map.isEmpty()) {
            this.mQueueCache.add(map);
        }
    }

    /* access modifiers changed from: private */
    public void putClearEvent() {
        if (this.isRunning) {
            this.mQueueCache.add(EMPTY_MAP);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0054, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized int getMapHashCode(java.util.Map<java.lang.String, java.lang.String> r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            r0 = 0
            if (r5 == 0) goto L_0x0053
            boolean r1 = r5.isEmpty()     // Catch:{ all -> 0x0050 }
            if (r1 == 0) goto L_0x000b
            goto L_0x0053
        L_0x000b:
            java.util.Map<java.lang.String, java.lang.String> r1 = r4.mSortMap     // Catch:{ all -> 0x0050 }
            r1.putAll(r5)     // Catch:{ all -> 0x0050 }
            java.util.Map<java.lang.String, java.lang.String> r5 = r4.mSortMap     // Catch:{ all -> 0x0050 }
            java.util.Set r5 = r5.entrySet()     // Catch:{ all -> 0x0050 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x0050 }
        L_0x001a:
            boolean r1 = r5.hasNext()     // Catch:{ all -> 0x0050 }
            if (r1 == 0) goto L_0x0049
            java.lang.Object r1 = r5.next()     // Catch:{ all -> 0x0050 }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ all -> 0x0050 }
            java.lang.Object r2 = r1.getKey()     // Catch:{ all -> 0x0050 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x0050 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x0050 }
            if (r3 != 0) goto L_0x0037
            int r2 = r2.hashCode()     // Catch:{ all -> 0x0050 }
            int r0 = r0 + r2
        L_0x0037:
            java.lang.Object r1 = r1.getValue()     // Catch:{ all -> 0x0050 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0050 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0050 }
            if (r2 != 0) goto L_0x001a
            int r1 = r1.hashCode()     // Catch:{ all -> 0x0050 }
            int r0 = r0 + r1
            goto L_0x001a
        L_0x0049:
            java.util.Map<java.lang.String, java.lang.String> r5 = r4.mSortMap     // Catch:{ all -> 0x0050 }
            r5.clear()     // Catch:{ all -> 0x0050 }
            monitor-exit(r4)
            return r0
        L_0x0050:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        L_0x0053:
            monitor-exit(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.RepeatExposureQueueMgr.getMapHashCode(java.util.Map):int");
    }

    static class RepeatExposurePageChangerMonitor implements UTPageHitHelper.PageChangeListener {
        RepeatExposurePageChangerMonitor() {
        }

        public void onPageAppear(Object obj) {
            RepeatExposureQueueMgr.getInstance().putClearEvent();
        }

        public void onPageDisAppear(Object obj) {
            RepeatExposureQueueMgr.getInstance().putClearEvent();
        }
    }
}
