package com.taobao.rxm.schedule;

import java.util.concurrent.PriorityBlockingQueue;

class CentralSchedulerQueue extends PriorityBlockingQueue<Runnable> {
    public static final int ACT_BE_QUEUED = 3;
    public static final int ACT_BE_REJECTED = 2;
    public static final int ACT_TO_EXECUTE = 1;
    private final ExecutorStateInspector mExecutorStateInspector;
    private final int mNormalCapacity;
    private final int mPatienceCapacity;

    public CentralSchedulerQueue(ExecutorStateInspector executorStateInspector, int i, int i2) {
        this.mExecutorStateInspector = executorStateInspector;
        this.mNormalCapacity = i;
        this.mPatienceCapacity = i2;
    }

    public boolean offer(Runnable runnable) {
        return moveIn((ScheduledAction) runnable, true) == 3;
    }

    public int size() {
        try {
            return super.size();
        } catch (IllegalMonitorStateException unused) {
            return 0;
        }
    }

    public boolean reachPatienceCapacity() {
        return size() >= this.mPatienceCapacity;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005e, code lost:
        return 2;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x0078 */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x007e A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0080 A[DONT_GENERATE] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int moveIn(com.taobao.rxm.schedule.ScheduledAction r6, boolean r7) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 1
            if (r7 == 0) goto L_0x0010
            com.taobao.rxm.schedule.ExecutorStateInspector r1 = r5.mExecutorStateInspector     // Catch:{ all -> 0x000e }
            boolean r1 = r1.isNotFull()     // Catch:{ all -> 0x000e }
            if (r1 == 0) goto L_0x0010
            monitor-exit(r5)
            return r0
        L_0x000e:
            r6 = move-exception
            goto L_0x0082
        L_0x0010:
            int r1 = r5.size()     // Catch:{ all -> 0x000e }
            int r2 = r5.mPatienceCapacity     // Catch:{ all -> 0x000e }
            r3 = 2
            if (r1 < r2) goto L_0x005f
            java.lang.String r6 = "RxSysLog"
            java.lang.String r7 = "SOX current size(%d) of central queue exceeded max patience(%d)!"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x000e }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x000e }
            r4 = 0
            r2[r4] = r1     // Catch:{ all -> 0x000e }
            int r1 = r5.mPatienceCapacity     // Catch:{ all -> 0x000e }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x000e }
            r2[r0] = r1     // Catch:{ all -> 0x000e }
            com.taobao.tcommon.log.FLog.e((java.lang.String) r6, (java.lang.String) r7, (java.lang.Object[]) r2)     // Catch:{ all -> 0x000e }
            com.taobao.rxm.schedule.ExecutorStateInspector r6 = r5.mExecutorStateInspector     // Catch:{ all -> 0x000e }
            java.lang.String r6 = r6.getStatus()     // Catch:{ all -> 0x000e }
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x000e }
            if (r7 != 0) goto L_0x005d
            java.lang.String r7 = "%"
            java.lang.String r0 = "%%"
            java.lang.String r6 = r6.replace(r7, r0)     // Catch:{ all -> 0x000e }
            java.lang.String r7 = "RxSysLog"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x000e }
            r0.<init>()     // Catch:{ all -> 0x000e }
            java.lang.String r1 = "SOX detail:"
            r0.append(r1)     // Catch:{ all -> 0x000e }
            r0.append(r6)     // Catch:{ all -> 0x000e }
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x000e }
            java.lang.Object[] r0 = new java.lang.Object[r4]     // Catch:{ all -> 0x000e }
            com.taobao.tcommon.log.FLog.e((java.lang.String) r7, (java.lang.String) r6, (java.lang.Object[]) r0)     // Catch:{ all -> 0x000e }
        L_0x005d:
            monitor-exit(r5)
            return r3
        L_0x005f:
            int r2 = r5.mNormalCapacity     // Catch:{ all -> 0x000e }
            if (r1 < r2) goto L_0x006b
            boolean r1 = r6.canRunDirectly()     // Catch:{ all -> 0x000e }
            if (r1 == 0) goto L_0x006b
            monitor-exit(r5)
            return r3
        L_0x006b:
            if (r7 != 0) goto L_0x006f
            monitor-exit(r5)
            return r0
        L_0x006f:
            boolean r7 = super.offer(r6)     // Catch:{ IllegalMonitorStateException -> 0x0078 }
            if (r7 == 0) goto L_0x0078
            r6 = 3
            monitor-exit(r5)
            return r6
        L_0x0078:
            boolean r6 = r6.canRunDirectly()     // Catch:{ all -> 0x000e }
            if (r6 == 0) goto L_0x0080
            monitor-exit(r5)
            return r3
        L_0x0080:
            monitor-exit(r5)
            return r0
        L_0x0082:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.schedule.CentralSchedulerQueue.moveIn(com.taobao.rxm.schedule.ScheduledAction, boolean):int");
    }
}
