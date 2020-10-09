package com.taobao.phenix.compat;

import com.taobao.android.task.Coordinator;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.weex.el.parse.Operators;

public class TBScheduler4Phenix implements Scheduler {
    private static final int QUEUE_PRIORITY_IMAGE = 27;
    private final Coordinator.CoordThreadPoolExecutor mExecutor = ((Coordinator.CoordThreadPoolExecutor) Coordinator.getDefaultThreadPoolExecutor());

    public boolean isScheduleMainThread() {
        return false;
    }

    private TBScheduler4Phenix() {
    }

    public void schedule(ScheduledAction scheduledAction) {
        this.mExecutor.execute(scheduledAction, 27);
    }

    public String getStatus() {
        return "TBScheduler4Phenix[queue=" + getQueueSize() + ",active=" + this.mExecutor.getActiveCount() + ",pool=" + this.mExecutor.getPoolSize() + ",largest=" + this.mExecutor.getLargestPoolSize() + ",tasks=" + this.mExecutor.getTaskCount() + ",completes=" + this.mExecutor.getCompletedTaskCount() + Operators.ARRAY_END_STR;
    }

    public int getQueueSize() {
        return this.mExecutor.getQueue().size();
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x006e A[Catch:{ RuntimeException -> 0x001e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setupScheduler(boolean r14, boolean r15) {
        /*
            r0 = 1
            r1 = 0
            if (r14 == 0) goto L_0x0021
            com.taobao.phenix.intf.Phenix r2 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.builder.SchedulerBuilder r2 = r2.schedulerBuilder()     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.compat.TBScheduler4Phenix r3 = new com.taobao.phenix.compat.TBScheduler4Phenix     // Catch:{ RuntimeException -> 0x001e }
            r3.<init>()     // Catch:{ RuntimeException -> 0x001e }
            r2.central(r3)     // Catch:{ RuntimeException -> 0x001e }
            java.lang.String r2 = "TBScheduler4Phenix"
            java.lang.String r3 = "enable unify thread pool"
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.common.UnitedLog.i(r2, r3, r4)     // Catch:{ RuntimeException -> 0x001e }
            goto L_0x0021
        L_0x001e:
            r14 = move-exception
            goto L_0x00b9
        L_0x0021:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = com.taobao.onlinemonitor.OnLineMonitor.getOnLineStat()     // Catch:{ RuntimeException -> 0x001e }
            if (r2 == 0) goto L_0x0038
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = com.taobao.onlinemonitor.OnLineMonitor.getOnLineStat()     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r2 = r2.performanceInfo     // Catch:{ RuntimeException -> 0x001e }
            if (r2 == 0) goto L_0x0038
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = com.taobao.onlinemonitor.OnLineMonitor.getOnLineStat()     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r2 = r2.performanceInfo     // Catch:{ RuntimeException -> 0x001e }
            int r2 = r2.deviceScore     // Catch:{ RuntimeException -> 0x001e }
            goto L_0x0039
        L_0x0038:
            r2 = 0
        L_0x0039:
            r3 = -1
            r4 = 4
            r5 = 5
            r6 = 2
            r7 = 6
            r8 = 3
            if (r2 <= 0) goto L_0x0068
            r9 = 40
            if (r2 > r9) goto L_0x0049
            r14 = 4
            r9 = 2
            r10 = 3
            goto L_0x006b
        L_0x0049:
            r9 = 60
            if (r2 > r9) goto L_0x0051
            r14 = 5
            r9 = 3
            r10 = 4
            goto L_0x006b
        L_0x0051:
            r9 = 75
            if (r2 > r9) goto L_0x005a
            r14 = 6
            r9 = 3
            r10 = 5
        L_0x0058:
            r11 = 3
            goto L_0x006c
        L_0x005a:
            r9 = 90
            if (r2 > r9) goto L_0x0062
            r14 = 7
            r9 = 3
        L_0x0060:
            r10 = 6
            goto L_0x0058
        L_0x0062:
            if (r14 == 0) goto L_0x0068
            r14 = 8
            r9 = 4
            goto L_0x0060
        L_0x0068:
            r14 = 6
            r9 = 3
            r10 = 5
        L_0x006b:
            r11 = 2
        L_0x006c:
            if (r15 == 0) goto L_0x0070
            r3 = 25000(0x61a8, float:3.5032E-41)
        L_0x0070:
            java.lang.String r15 = "TBScheduler4Phenix"
            java.lang.String r12 = "setup max running=%d, decode=%d, fast network=%d, slow network=%d, network expired=%d, score=%d"
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ RuntimeException -> 0x001e }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r14)     // Catch:{ RuntimeException -> 0x001e }
            r7[r1] = r13     // Catch:{ RuntimeException -> 0x001e }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r9)     // Catch:{ RuntimeException -> 0x001e }
            r7[r0] = r13     // Catch:{ RuntimeException -> 0x001e }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r10)     // Catch:{ RuntimeException -> 0x001e }
            r7[r6] = r13     // Catch:{ RuntimeException -> 0x001e }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r11)     // Catch:{ RuntimeException -> 0x001e }
            r7[r8] = r6     // Catch:{ RuntimeException -> 0x001e }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r3)     // Catch:{ RuntimeException -> 0x001e }
            r7[r4] = r6     // Catch:{ RuntimeException -> 0x001e }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ RuntimeException -> 0x001e }
            r7[r5] = r2     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.common.UnitedLog.i(r15, r12, r7)     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.intf.Phenix r15 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.builder.SchedulerBuilder r15 = r15.schedulerBuilder()     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.builder.SchedulerBuilder r14 = r15.maxRunning(r14)     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.builder.SchedulerBuilder r14 = r14.maxDecodeRunning(r9)     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.builder.SchedulerBuilder r14 = r14.maxNetworkRunningAtFast(r10)     // Catch:{ RuntimeException -> 0x001e }
            com.taobao.phenix.builder.SchedulerBuilder r14 = r14.maxNetworkRunningAtSlow(r11)     // Catch:{ RuntimeException -> 0x001e }
            r14.networkRunningExpired(r3)     // Catch:{ RuntimeException -> 0x001e }
            goto L_0x00c4
        L_0x00b9:
            java.lang.String r15 = "TBScheduler4Phenix"
            java.lang.String r2 = "init running scheduler error=%s"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r0[r1] = r14
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r15, (java.lang.String) r2, (java.lang.Object[]) r0)
        L_0x00c4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.TBScheduler4Phenix.setupScheduler(boolean, boolean):void");
    }
}
