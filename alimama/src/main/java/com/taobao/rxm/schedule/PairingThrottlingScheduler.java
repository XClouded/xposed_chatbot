package com.taobao.rxm.schedule;

import android.util.SparseArray;
import com.taobao.rxm.common.Constant;
import com.taobao.rxm.request.RequestCancelListener;
import com.taobao.rxm.request.RequestContext;
import com.taobao.tcommon.log.FLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PairingThrottlingScheduler implements ThrottlingScheduler, ScheduledActionListener, RequestCancelListener<RequestContext> {
    private static final int AUTO_DEGRADE_EXPIRED_COUNT = 3;
    private int mCurrentRunning;
    private DegradationListener mDegradationListener;
    private final long mExpiredNanos;
    private int mExpiredTotal;
    private final Scheduler mHostScheduler;
    private long mLastClearTime;
    private int mMaxRunningCount;
    private final SparseArray<Long> mProduceTimeMap = new SparseArray<>();
    private final List<Integer> mTempExpiredList = new ArrayList();
    private final Queue<ScheduledAction> mWaitProduceActions = new LinkedList();

    public interface DegradationListener {
        void onDegrade2Unlimited();
    }

    public PairingThrottlingScheduler(Scheduler scheduler, int i, int i2) {
        this.mHostScheduler = scheduler;
        this.mMaxRunningCount = i;
        this.mExpiredNanos = ((long) i2) * 1000000;
    }

    public void onCancel(RequestContext requestContext) {
        if (requestContext != null) {
            int id = requestContext.getId();
            ScheduledAction scheduledAction = null;
            synchronized (this) {
                Iterator it = this.mWaitProduceActions.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ScheduledAction scheduledAction2 = (ScheduledAction) it.next();
                    if (id == scheduledAction2.getContextId()) {
                        scheduledAction = scheduledAction2;
                        break;
                    }
                }
                if (scheduledAction != null) {
                    this.mWaitProduceActions.remove(scheduledAction);
                }
            }
            if (scheduledAction != null) {
                scheduledAction.cancelActing();
                scheduledAction.unregisterCancelListener(this);
                FLog.i(Constant.RX_LOG, "[PairingThrottling] ID=%d cancelled before scheduling the action in queue", Integer.valueOf(id));
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void schedule(com.taobao.rxm.schedule.ScheduledAction r3) {
        /*
            r2 = this;
            r3.setBranchActionListener(r2)
            boolean r0 = r2.isValidConsumeAction(r3)
            monitor-enter(r2)
            boolean r1 = r3.isProduceAction()     // Catch:{ all -> 0x0037 }
            if (r1 == 0) goto L_0x0011
            r2.clearExpiredPairs()     // Catch:{ all -> 0x0037 }
        L_0x0011:
            if (r0 != 0) goto L_0x0024
            int r0 = r2.mCurrentRunning     // Catch:{ all -> 0x0037 }
            int r1 = r2.mMaxRunningCount     // Catch:{ all -> 0x0037 }
            if (r0 < r1) goto L_0x0024
            java.util.Queue<com.taobao.rxm.schedule.ScheduledAction> r0 = r2.mWaitProduceActions     // Catch:{ all -> 0x0037 }
            boolean r0 = r0.offer(r3)     // Catch:{ all -> 0x0037 }
            if (r0 != 0) goto L_0x0022
            goto L_0x0024
        L_0x0022:
            r0 = 0
            goto L_0x0025
        L_0x0024:
            r0 = 1
        L_0x0025:
            if (r0 == 0) goto L_0x002b
            r2.countBeforeScheduling(r3)     // Catch:{ all -> 0x0037 }
            goto L_0x002e
        L_0x002b:
            r3.registerCancelListener(r2)     // Catch:{ all -> 0x0037 }
        L_0x002e:
            monitor-exit(r2)     // Catch:{ all -> 0x0037 }
            if (r0 == 0) goto L_0x0036
            com.taobao.rxm.schedule.Scheduler r0 = r2.mHostScheduler
            r0.schedule(r3)
        L_0x0036:
            return
        L_0x0037:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0037 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.schedule.PairingThrottlingScheduler.schedule(com.taobao.rxm.schedule.ScheduledAction):void");
    }

    private boolean isValidConsumeAction(ScheduledAction scheduledAction) {
        return scheduledAction.getContextId() > 0 && !scheduledAction.isProduceAction() && scheduledAction.isConsumeAction();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b9, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void clearExpiredPairs() {
        /*
            r10 = this;
            monitor-enter(r10)
            long r0 = java.lang.System.nanoTime()     // Catch:{ all -> 0x00ba }
            long r2 = r10.mLastClearTime     // Catch:{ all -> 0x00ba }
            r4 = 0
            long r2 = r0 - r2
            r4 = 30000000(0x1c9c380, double:1.48219694E-316)
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 >= 0) goto L_0x0013
            monitor-exit(r10)
            return
        L_0x0013:
            r10.mLastClearTime = r0     // Catch:{ all -> 0x00ba }
            java.util.List<java.lang.Integer> r0 = r10.mTempExpiredList     // Catch:{ all -> 0x00ba }
            r0.clear()     // Catch:{ all -> 0x00ba }
            android.util.SparseArray<java.lang.Long> r0 = r10.mProduceTimeMap     // Catch:{ all -> 0x00ba }
            int r0 = r0.size()     // Catch:{ all -> 0x00ba }
            long r1 = java.lang.System.nanoTime()     // Catch:{ all -> 0x00ba }
            r3 = 0
            r4 = 0
        L_0x0026:
            if (r4 >= r0) goto L_0x0051
            android.util.SparseArray<java.lang.Long> r5 = r10.mProduceTimeMap     // Catch:{ all -> 0x00ba }
            java.lang.Object r5 = r5.valueAt(r4)     // Catch:{ all -> 0x00ba }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ all -> 0x00ba }
            if (r5 == 0) goto L_0x004e
            long r5 = r5.longValue()     // Catch:{ all -> 0x00ba }
            r7 = 0
            long r5 = r1 - r5
            long r7 = r10.mExpiredNanos     // Catch:{ all -> 0x00ba }
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 < 0) goto L_0x004e
            java.util.List<java.lang.Integer> r5 = r10.mTempExpiredList     // Catch:{ all -> 0x00ba }
            android.util.SparseArray<java.lang.Long> r6 = r10.mProduceTimeMap     // Catch:{ all -> 0x00ba }
            int r6 = r6.keyAt(r4)     // Catch:{ all -> 0x00ba }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ all -> 0x00ba }
            r5.add(r6)     // Catch:{ all -> 0x00ba }
        L_0x004e:
            int r4 = r4 + 1
            goto L_0x0026
        L_0x0051:
            java.util.List<java.lang.Integer> r0 = r10.mTempExpiredList     // Catch:{ all -> 0x00ba }
            int r0 = r0.size()     // Catch:{ all -> 0x00ba }
            r1 = 0
            r2 = 0
        L_0x0059:
            r4 = 1
            if (r1 >= r0) goto L_0x0086
            java.util.List<java.lang.Integer> r5 = r10.mTempExpiredList     // Catch:{ all -> 0x00ba }
            java.lang.Object r5 = r5.get(r1)     // Catch:{ all -> 0x00ba }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ all -> 0x00ba }
            int r5 = r5.intValue()     // Catch:{ all -> 0x00ba }
            java.lang.String r6 = "RxSysLog"
            java.lang.String r7 = "[PairingThrottling] remove expired pair, id=%d"
            java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ all -> 0x00ba }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x00ba }
            r8[r3] = r9     // Catch:{ all -> 0x00ba }
            com.taobao.tcommon.log.FLog.i(r6, r7, r8)     // Catch:{ all -> 0x00ba }
            boolean r5 = r10.updateRunningStatus(r5)     // Catch:{ all -> 0x00ba }
            if (r5 != 0) goto L_0x0082
            if (r2 == 0) goto L_0x0080
            goto L_0x0082
        L_0x0080:
            r2 = 0
            goto L_0x0083
        L_0x0082:
            r2 = 1
        L_0x0083:
            int r1 = r1 + 1
            goto L_0x0059
        L_0x0086:
            int r1 = r10.mExpiredTotal     // Catch:{ all -> 0x00ba }
            r5 = 3
            if (r1 >= r5) goto L_0x00b3
            int r1 = r10.mExpiredTotal     // Catch:{ all -> 0x00ba }
            int r1 = r1 + r0
            r10.mExpiredTotal = r1     // Catch:{ all -> 0x00ba }
            int r0 = r10.mExpiredTotal     // Catch:{ all -> 0x00ba }
            if (r0 < r5) goto L_0x00b3
            r0 = 2147483647(0x7fffffff, float:NaN)
            r10.mMaxRunningCount = r0     // Catch:{ all -> 0x00ba }
            java.lang.String r0 = "RxSysLog"
            java.lang.String r1 = "[PairingThrottling] auto degrade to unlimited scheduler, expired total=%d"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00ba }
            int r5 = r10.mExpiredTotal     // Catch:{ all -> 0x00ba }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x00ba }
            r4[r3] = r5     // Catch:{ all -> 0x00ba }
            com.taobao.tcommon.log.FLog.w(r0, r1, r4)     // Catch:{ all -> 0x00ba }
            com.taobao.rxm.schedule.PairingThrottlingScheduler$DegradationListener r0 = r10.mDegradationListener     // Catch:{ all -> 0x00ba }
            if (r0 == 0) goto L_0x00b3
            com.taobao.rxm.schedule.PairingThrottlingScheduler$DegradationListener r0 = r10.mDegradationListener     // Catch:{ all -> 0x00ba }
            r0.onDegrade2Unlimited()     // Catch:{ all -> 0x00ba }
        L_0x00b3:
            if (r2 == 0) goto L_0x00b8
            r10.checkRunningCount()     // Catch:{ all -> 0x00ba }
        L_0x00b8:
            monitor-exit(r10)
            return
        L_0x00ba:
            r0 = move-exception
            monitor-exit(r10)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.schedule.PairingThrottlingScheduler.clearExpiredPairs():void");
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
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized void setMaxRunningCount(int r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            monitor-enter(r2)     // Catch:{ all -> 0x001d }
            int r0 = r2.mExpiredTotal     // Catch:{ all -> 0x001a }
            r1 = 3
            if (r0 >= r1) goto L_0x000d
            int r0 = r2.mMaxRunningCount     // Catch:{ all -> 0x001a }
            if (r3 == r0) goto L_0x000d
            r0 = 1
            goto L_0x000e
        L_0x000d:
            r0 = 0
        L_0x000e:
            if (r0 == 0) goto L_0x0012
            r2.mMaxRunningCount = r3     // Catch:{ all -> 0x001a }
        L_0x0012:
            monitor-exit(r2)     // Catch:{ all -> 0x001a }
            if (r0 == 0) goto L_0x0018
            r2.checkRunningCount()     // Catch:{ all -> 0x001d }
        L_0x0018:
            monitor-exit(r2)
            return
        L_0x001a:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001a }
            throw r3     // Catch:{ all -> 0x001d }
        L_0x001d:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.schedule.PairingThrottlingScheduler.setMaxRunningCount(int):void");
    }

    private synchronized void countBeforeScheduling(ScheduledAction scheduledAction) {
        int contextId = scheduledAction.getContextId();
        if (contextId <= 0) {
            this.mCurrentRunning++;
        } else if (scheduledAction.isProduceAction() && this.mProduceTimeMap.get(contextId) == null) {
            this.mProduceTimeMap.put(contextId, Long.valueOf(System.nanoTime()));
            this.mCurrentRunning++;
        }
    }

    public void completePairActions(int i) {
        if (updateRunningStatus(i)) {
            checkRunningCount();
        }
    }

    private boolean updateRunningStatus(int i) {
        synchronized (this) {
            if (i <= 0) {
                try {
                    this.mCurrentRunning--;
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            } else if (this.mProduceTimeMap.get(i) == null) {
                return false;
            } else {
                this.mProduceTimeMap.remove(i);
                this.mCurrentRunning--;
                return true;
            }
        }
    }

    private void checkRunningCount() {
        ScheduledAction scheduledAction = ScheduledAction.sActionCallerThreadLocal.get();
        while (true) {
            ScheduledAction scheduledAction2 = null;
            synchronized (this) {
                clearExpiredPairs();
                if (this.mCurrentRunning < this.mMaxRunningCount) {
                    scheduledAction2 = this.mWaitProduceActions.poll();
                }
                if (scheduledAction2 != null) {
                    countBeforeScheduling(scheduledAction2);
                }
            }
            if (scheduledAction2 != null) {
                scheduledAction2.unregisterCancelListener(this);
                this.mHostScheduler.schedule(scheduledAction2);
                ScheduledAction.sActionCallerThreadLocal.set(scheduledAction);
            } else {
                return;
            }
        }
        while (true) {
        }
    }

    public synchronized String getStatus() {
        return this.mHostScheduler.getStatus();
    }

    public synchronized boolean isScheduleMainThread() {
        return this.mHostScheduler.isScheduleMainThread();
    }

    public synchronized int getQueueSize() {
        return this.mWaitProduceActions.size();
    }

    public void onActionFinished(ScheduledAction scheduledAction) {
        int contextId = scheduledAction.getContextId();
        if ((contextId <= 0 || scheduledAction.isConsumeAction()) && updateRunningStatus(contextId)) {
            checkRunningCount();
        }
    }

    public void setDegradationListener(DegradationListener degradationListener) {
        this.mDegradationListener = degradationListener;
    }
}
