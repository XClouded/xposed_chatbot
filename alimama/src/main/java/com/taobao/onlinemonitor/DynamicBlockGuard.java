package com.taobao.onlinemonitor;

import java.lang.reflect.Field;

public class DynamicBlockGuard extends BaseDynamicProxy {
    public DynamicBlockGuard(OnLineMonitor onLineMonitor) {
        super(onLineMonitor);
    }

    public void doProxy() {
        try {
            Class<?> cls = Class.forName("dalvik.system.BlockGuard");
            Field declaredField = cls.getDeclaredField("LAX_POLICY");
            declaredField.setAccessible(true);
            declaredField.set(cls, newProxyInstance(declaredField.get(cls)));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003b A[Catch:{ Throwable -> 0x0233 }] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x022d A[SYNTHETIC, Splitter:B:87:0x022d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object invoke(java.lang.Object r17, java.lang.reflect.Method r18, java.lang.Object[] r19) throws java.lang.Throwable {
        /*
            r16 = this;
            r0 = r16
            r1 = 0
            java.lang.String r10 = r18.getName()     // Catch:{ Throwable -> 0x0233 }
            android.os.Looper r2 = android.os.Looper.getMainLooper()     // Catch:{ Throwable -> 0x0233 }
            android.os.Looper r3 = android.os.Looper.myLooper()     // Catch:{ Throwable -> 0x0233 }
            r11 = 2
            if (r2 != r3) goto L_0x0035
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.lang.Thread r2 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            java.lang.StackTraceElement[] r2 = r2.getStackTrace()     // Catch:{ Throwable -> 0x0233 }
            r3 = 10
            java.lang.String r2 = com.taobao.onlinemonitor.OnLineMonitor.getStackTraceElement(r2, r11, r3)     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r3 = "onWriteToDisk"
            boolean r3 = r10.equals(r3)     // Catch:{ Throwable -> 0x0233 }
            if (r3 == 0) goto L_0x0033
            java.lang.String r3 = "dumpGfxInfo"
            boolean r3 = r2.contains(r3)     // Catch:{ Throwable -> 0x0233 }
            if (r3 == 0) goto L_0x0033
            goto L_0x0035
        L_0x0033:
            r12 = r2
            goto L_0x0036
        L_0x0035:
            r12 = r1
        L_0x0036:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            r13 = 0
            if (r2 == 0) goto L_0x0221
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r2 = r2.mOnlineStatistics     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x008c
            if (r12 == 0) goto L_0x008c
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r2 = r2.mOnlineStatistics     // Catch:{ Throwable -> 0x0233 }
            int r14 = r2.size()     // Catch:{ Throwable -> 0x0233 }
            r15 = 0
        L_0x004c:
            if (r15 >= r14) goto L_0x0074
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.ArrayList<com.taobao.onlinemonitor.OnlineStatistics> r2 = r2.mOnlineStatistics     // Catch:{ Throwable -> 0x0233 }
            java.lang.Object r2 = r2.get(r15)     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnlineStatistics r2 = (com.taobao.onlinemonitor.OnlineStatistics) r2     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x0071
            com.taobao.onlinemonitor.OnLineMonitor r3 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r3.mOnLineStat     // Catch:{ Throwable -> 0x0233 }
            r4 = 2
            com.taobao.onlinemonitor.OnLineMonitor r5 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r6 = r5.mActivityName     // Catch:{ Throwable -> 0x0233 }
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r7 = r5.getName()     // Catch:{ Throwable -> 0x0233 }
            r9 = 0
            r5 = r10
            r8 = r12
            r2.onBlockOrCloseGuard(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x0233 }
        L_0x0071:
            int r15 = r15 + 1
            goto L_0x004c
        L_0x0074:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r2 = "OnLineMonitor"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0233 }
            r3.<init>()     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r4 = "主线程耗时访问："
            r3.append(r4)     // Catch:{ Throwable -> 0x0233 }
            r3.append(r12)     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0233 }
            android.util.Log.e(r2, r3)     // Catch:{ Throwable -> 0x0233 }
        L_0x008c:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0233 }
            r3 = 1
            if (r2 == 0) goto L_0x00cf
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0233 }
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r2.mMainThreadBlockGuardInfo     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x00cf
            boolean r2 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Throwable -> 0x0233 }
            if (r2 != 0) goto L_0x00cf
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0233 }
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r2.mMainThreadBlockGuardInfo     // Catch:{ Throwable -> 0x0233 }
            java.lang.Object r2 = r2.get(r12)     // Catch:{ Throwable -> 0x0233 }
            java.lang.Integer r2 = (java.lang.Integer) r2     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x00c2
            com.taobao.onlinemonitor.OnLineMonitor r4 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.TraceDetail r4 = r4.mTraceDetail     // Catch:{ Throwable -> 0x0233 }
            java.util.HashMap<java.lang.String, java.lang.Integer> r4 = r4.mMainThreadBlockGuardInfo     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2.intValue()     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2 + r3
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0233 }
            r4.put(r12, r2)     // Catch:{ Throwable -> 0x0233 }
            goto L_0x00cf
        L_0x00c2:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x0233 }
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r2.mMainThreadBlockGuardInfo     // Catch:{ Throwable -> 0x0233 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0233 }
            r2.put(r12, r4)     // Catch:{ Throwable -> 0x0233 }
        L_0x00cf:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfo     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x0221
            java.lang.Thread r2 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x011b
            java.lang.String r4 = "@("
            boolean r4 = r2.contains(r4)     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x0101
            java.lang.String r4 = "["
            boolean r4 = r2.contains(r4)     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x0101
            java.lang.String r4 = "@("
            int r4 = r2.indexOf(r4)     // Catch:{ Throwable -> 0x0233 }
            r5 = 91
            int r5 = r2.indexOf(r5)     // Catch:{ Throwable -> 0x0233 }
            int r4 = r4 + r11
            java.lang.String r2 = r2.substring(r4, r5)     // Catch:{ Throwable -> 0x0233 }
            goto L_0x011b
        L_0x0101:
            int r4 = r2.length()     // Catch:{ Throwable -> 0x0233 }
            r5 = 8
            if (r4 <= r5) goto L_0x011b
            java.lang.String r4 = "@"
            boolean r4 = r2.contains(r4)     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x011b
            r4 = 64
            int r4 = r2.indexOf(r4)     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r2 = r2.substring(r13, r4)     // Catch:{ Throwable -> 0x0233 }
        L_0x011b:
            java.lang.String r4 = "SharedPreferencesImpl-load"
            boolean r4 = r2.equals(r4)     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x0134
            com.taobao.onlinemonitor.OnLineMonitor r4 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.ProblemCheck r4 = r4.mProblemCheck     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x0134
            java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor r5 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.ProblemCheck r5 = r5.mProblemCheck     // Catch:{ Throwable -> 0x0233 }
            r5.regSharedPreferenceListener(r4)     // Catch:{ Throwable -> 0x0233 }
        L_0x0134:
            com.taobao.onlinemonitor.OnLineMonitor r4 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            boolean r4 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x0157
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0233 }
            r4.<init>()     // Catch:{ Throwable -> 0x0233 }
            r4.append(r2)     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r5 = "-"
            r4.append(r5)     // Catch:{ Throwable -> 0x0233 }
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            long r5 = r5.getId()     // Catch:{ Throwable -> 0x0233 }
            r4.append(r5)     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0233 }
            goto L_0x0158
        L_0x0157:
            r4 = r2
        L_0x0158:
            com.taobao.onlinemonitor.OnLineMonitor r5 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r5 = r5.mBlockGuardThreadInfo     // Catch:{ Throwable -> 0x0233 }
            java.lang.Object r5 = r5.get(r4)     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo r5 = (com.taobao.onlinemonitor.OnLineMonitor.ThreadIoInfo) r5     // Catch:{ Throwable -> 0x0233 }
            if (r5 == 0) goto L_0x019b
            java.lang.String r2 = "onWriteToDisk"
            boolean r2 = r10.equals(r2)     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x0178
            int r2 = r5.writeTimes     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2 + r3
            r5.writeTimes = r2     // Catch:{ Throwable -> 0x0233 }
            int r2 = r5.readWriteTimes     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2 + r3
            r5.readWriteTimes = r2     // Catch:{ Throwable -> 0x0233 }
            goto L_0x0221
        L_0x0178:
            java.lang.String r2 = "onReadFromDisk"
            boolean r2 = r10.equals(r2)     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x018c
            int r2 = r5.readTimes     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2 + r3
            r5.readTimes = r2     // Catch:{ Throwable -> 0x0233 }
            int r2 = r5.readWriteTimes     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2 + r3
            r5.readWriteTimes = r2     // Catch:{ Throwable -> 0x0233 }
            goto L_0x0221
        L_0x018c:
            java.lang.String r2 = "onNetwork"
            boolean r2 = r10.equals(r2)     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x0221
            int r2 = r5.netTimes     // Catch:{ Throwable -> 0x0233 }
            int r2 = r2 + r3
            r5.netTimes = r2     // Catch:{ Throwable -> 0x0233 }
            goto L_0x0221
        L_0x019b:
            com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo r5 = new com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo     // Catch:{ Throwable -> 0x0233 }
            r5.<init>()     // Catch:{ Throwable -> 0x0233 }
            java.lang.String r6 = "onWriteToDisk"
            boolean r6 = r10.equals(r6)     // Catch:{ Throwable -> 0x0233 }
            if (r6 == 0) goto L_0x01ad
            r5.writeTimes = r3     // Catch:{ Throwable -> 0x0233 }
            r5.readWriteTimes = r3     // Catch:{ Throwable -> 0x0233 }
            goto L_0x01c4
        L_0x01ad:
            java.lang.String r6 = "onReadFromDisk"
            boolean r6 = r10.equals(r6)     // Catch:{ Throwable -> 0x0233 }
            if (r6 == 0) goto L_0x01ba
            r5.readTimes = r3     // Catch:{ Throwable -> 0x0233 }
            r5.readWriteTimes = r3     // Catch:{ Throwable -> 0x0233 }
            goto L_0x01c4
        L_0x01ba:
            java.lang.String r6 = "onNetwork"
            boolean r6 = r10.equals(r6)     // Catch:{ Throwable -> 0x0233 }
            if (r6 == 0) goto L_0x01c4
            r5.netTimes = r3     // Catch:{ Throwable -> 0x0233 }
        L_0x01c4:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            long r6 = r6.getId()     // Catch:{ Throwable -> 0x0233 }
            r5.id = r6     // Catch:{ Throwable -> 0x0233 }
            int r6 = android.os.Process.myTid()     // Catch:{ Throwable -> 0x0233 }
            r5.tid = r6     // Catch:{ Throwable -> 0x0233 }
            int r6 = r5.tid     // Catch:{ Throwable -> 0x0233 }
            int r6 = android.os.Process.getThreadPriority(r6)     // Catch:{ Throwable -> 0x0233 }
            r5.nice = r6     // Catch:{ Throwable -> 0x0233 }
            r5.name = r2     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x0233 }
            if (r2 == 0) goto L_0x01f7
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.lang.Thread r2 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0233 }
            java.lang.StackTraceElement[] r2 = r2.getStackTrace()     // Catch:{ Throwable -> 0x0233 }
            r6 = 7
            r7 = 15
            java.lang.String r2 = com.taobao.onlinemonitor.OnLineMonitor.getStackTraceElement(r2, r6, r7)     // Catch:{ Throwable -> 0x0233 }
            r5.stacks = r2     // Catch:{ Throwable -> 0x0233 }
        L_0x01f7:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r2 = r2.mBlockGuardThreadInfo     // Catch:{ Throwable -> 0x0233 }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor r2 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            boolean r2 = r2.mFileSchedIsNotExists     // Catch:{ Throwable -> 0x0233 }
            if (r2 != 0) goto L_0x0221
            int r2 = r5.tid     // Catch:{ Throwable -> 0x0233 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor r4 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r4 = r4.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0233 }
            java.lang.Object r4 = r4.remove(r2)     // Catch:{ Throwable -> 0x0233 }
            com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo r4 = (com.taobao.onlinemonitor.OnLineMonitor.ThreadIoInfo) r4     // Catch:{ Throwable -> 0x0233 }
            if (r4 == 0) goto L_0x021a
            r4.multiplex = r3     // Catch:{ Throwable -> 0x0233 }
            r5.multiplex = r3     // Catch:{ Throwable -> 0x0233 }
        L_0x021a:
            com.taobao.onlinemonitor.OnLineMonitor r3 = r0.mOnLineMonitor     // Catch:{ Throwable -> 0x0233 }
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.taobao.onlinemonitor.OnLineMonitor$ThreadIoInfo> r3 = r3.mBlockGuardThreadInfoTid     // Catch:{ Throwable -> 0x0233 }
            r3.put(r2, r5)     // Catch:{ Throwable -> 0x0233 }
        L_0x0221:
            java.lang.Object r2 = r0.mTargetObject     // Catch:{ Throwable -> 0x0233 }
            r3 = r18
            r4 = r19
            java.lang.Object r2 = r3.invoke(r2, r4)     // Catch:{ Throwable -> 0x0233 }
            if (r2 != 0) goto L_0x0232
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)     // Catch:{ Throwable -> 0x0232 }
            goto L_0x0233
        L_0x0232:
            r1 = r2
        L_0x0233:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.DynamicBlockGuard.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }
}
