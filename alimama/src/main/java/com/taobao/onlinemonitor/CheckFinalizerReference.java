package com.taobao.onlinemonitor;

import android.util.Log;
import java.util.Map;

class CheckFinalizerReference {
    Class<?> mClassFinalizer;
    OnLineMonitor mOnLineMonitor;

    CheckFinalizerReference(OnLineMonitor onLineMonitor) {
        this.mOnLineMonitor = onLineMonitor;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x011c A[Catch:{ Throwable -> 0x01ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0138 A[Catch:{ Throwable -> 0x01ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0199 A[Catch:{ Throwable -> 0x01ca }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getFinalizerReferenceQueueSize() {
        /*
            r15 = this;
            com.taobao.onlinemonitor.OnLineMonitor r0 = r15.mOnLineMonitor
            boolean r0 = r0.mIsDeviceSampling
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class<?> r0 = r15.mClassFinalizer
            if (r0 != 0) goto L_0x0016
            java.lang.String r0 = "java.lang.ref.FinalizerReference"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x0015 }
            r15.mClassFinalizer = r0     // Catch:{ Throwable -> 0x0015 }
            goto L_0x0016
        L_0x0015:
        L_0x0016:
            java.lang.Class<?> r0 = r15.mClassFinalizer
            if (r0 != 0) goto L_0x001b
            return r1
        L_0x001b:
            com.taobao.onlinemonitor.OnLineMonitor r0 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r0 = r0.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01ca }
            if (r0 != 0) goto L_0x0022
            return r1
        L_0x0022:
            long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x01ca }
            r4 = 1000000(0xf4240, double:4.940656E-318)
            long r2 = r2 / r4
            long r6 = r0.lastGetFinalizerTime     // Catch:{ Throwable -> 0x01ca }
            r8 = 0
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 <= 0) goto L_0x0043
            long r6 = r0.lastGetFinalizerTime     // Catch:{ Throwable -> 0x01ca }
            r8 = 0
            long r6 = r2 - r6
            com.taobao.onlinemonitor.OnLineMonitor r8 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            int r8 = r8.mCpuCheckIntervalControl     // Catch:{ Throwable -> 0x01ca }
            int r8 = r8 * 2
            long r8 = (long) r8     // Catch:{ Throwable -> 0x01ca }
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 >= 0) goto L_0x0043
            return r1
        L_0x0043:
            r0.lastGetFinalizerTime = r2     // Catch:{ Throwable -> 0x01ca }
            java.lang.Class<?> r0 = r15.mClassFinalizer     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r2 = "head"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r2)     // Catch:{ Throwable -> 0x01ca }
            java.lang.Class<?> r2 = r15.mClassFinalizer     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r3 = "next"
            java.lang.reflect.Field r2 = r2.getDeclaredField(r3)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r3 = "java.lang.ref.Reference"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r6 = "referent"
            java.lang.reflect.Field r6 = r3.getDeclaredField(r6)     // Catch:{ Throwable -> 0x01ca }
            r7 = 1
            r0.setAccessible(r7)     // Catch:{ Throwable -> 0x01ca }
            r2.setAccessible(r7)     // Catch:{ Throwable -> 0x01ca }
            r6.setAccessible(r7)     // Catch:{ Throwable -> 0x01ca }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ Throwable -> 0x01ca }
            if (r0 != 0) goto L_0x0072
            return r1
        L_0x0072:
            boolean r3 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x01ca }
            if (r3 == 0) goto L_0x00a2
            boolean r3 = com.taobao.onlinemonitor.TraceDetail.sTraceFinalizer     // Catch:{ Throwable -> 0x01ca }
            if (r3 == 0) goto L_0x00a2
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            java.util.Map<java.lang.String, java.lang.Integer> r3 = r3.mFinalizerObject     // Catch:{ Throwable -> 0x01ca }
            if (r3 != 0) goto L_0x008e
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            java.util.HashMap r8 = new java.util.HashMap     // Catch:{ Throwable -> 0x01ca }
            r8.<init>()     // Catch:{ Throwable -> 0x01ca }
            r3.mFinalizerObject = r8     // Catch:{ Throwable -> 0x01ca }
            goto L_0x0097
        L_0x008e:
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            java.util.Map<java.lang.String, java.lang.Integer> r3 = r3.mFinalizerObject     // Catch:{ Throwable -> 0x01ca }
            r3.clear()     // Catch:{ Throwable -> 0x01ca }
        L_0x0097:
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            java.util.Map<java.lang.String, java.lang.Integer> r3 = r3.mFinalizerObject     // Catch:{ Throwable -> 0x01ca }
            int r3 = r3.size()     // Catch:{ Throwable -> 0x01ca }
            goto L_0x00a3
        L_0x00a2:
            r3 = 0
        L_0x00a3:
            long r8 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x01ca }
            long r8 = r8 / r4
            java.lang.Object r10 = r2.get(r0)     // Catch:{ Throwable -> 0x0101 }
            r1 = r10
            r11 = 0
        L_0x00ae:
            if (r1 == 0) goto L_0x0107
            int r11 = r11 + 1
            boolean r12 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x00ff }
            if (r12 == 0) goto L_0x00f8
            boolean r12 = com.taobao.onlinemonitor.TraceDetail.sTraceFinalizer     // Catch:{ Throwable -> 0x00ff }
            if (r12 == 0) goto L_0x00f8
            if (r3 != 0) goto L_0x00f8
            java.lang.Object r12 = r6.get(r1)     // Catch:{ Throwable -> 0x00ff }
            if (r12 == 0) goto L_0x00f8
            java.lang.Class r12 = r12.getClass()     // Catch:{ Throwable -> 0x00ff }
            java.lang.String r12 = r12.getName()     // Catch:{ Throwable -> 0x00ff }
            com.taobao.onlinemonitor.OnLineMonitor r13 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x00ff }
            com.taobao.onlinemonitor.TraceDetail r13 = r13.mTraceDetail     // Catch:{ Throwable -> 0x00ff }
            java.util.Map<java.lang.String, java.lang.Integer> r13 = r13.mFinalizerObject     // Catch:{ Throwable -> 0x00ff }
            java.lang.Object r13 = r13.get(r12)     // Catch:{ Throwable -> 0x00ff }
            java.lang.Integer r13 = (java.lang.Integer) r13     // Catch:{ Throwable -> 0x00ff }
            if (r13 == 0) goto L_0x00eb
            int r13 = r13.intValue()     // Catch:{ Throwable -> 0x00ff }
            int r13 = r13 + r7
            com.taobao.onlinemonitor.OnLineMonitor r14 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x00ff }
            com.taobao.onlinemonitor.TraceDetail r14 = r14.mTraceDetail     // Catch:{ Throwable -> 0x00ff }
            java.util.Map<java.lang.String, java.lang.Integer> r14 = r14.mFinalizerObject     // Catch:{ Throwable -> 0x00ff }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch:{ Throwable -> 0x00ff }
            r14.put(r12, r13)     // Catch:{ Throwable -> 0x00ff }
            goto L_0x00f8
        L_0x00eb:
            com.taobao.onlinemonitor.OnLineMonitor r13 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x00ff }
            com.taobao.onlinemonitor.TraceDetail r13 = r13.mTraceDetail     // Catch:{ Throwable -> 0x00ff }
            java.util.Map<java.lang.String, java.lang.Integer> r13 = r13.mFinalizerObject     // Catch:{ Throwable -> 0x00ff }
            java.lang.Integer r14 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x00ff }
            r13.put(r12, r14)     // Catch:{ Throwable -> 0x00ff }
        L_0x00f8:
            java.lang.Object r1 = r2.get(r1)     // Catch:{ Throwable -> 0x00ff }
            if (r1 != r10) goto L_0x00ae
            goto L_0x0107
        L_0x00ff:
            r1 = move-exception
            goto L_0x0104
        L_0x0101:
            r2 = move-exception
            r1 = r2
            r11 = 0
        L_0x0104:
            r1.printStackTrace()     // Catch:{ Throwable -> 0x0109 }
        L_0x0107:
            r1 = r11
            goto L_0x010e
        L_0x0109:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Throwable -> 0x01c7 }
            goto L_0x0107
        L_0x010e:
            boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail     // Catch:{ Throwable -> 0x01ca }
            if (r2 == 0) goto L_0x0179
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            android.util.SparseIntArray r2 = r2.mRunningFinalizerCount     // Catch:{ Throwable -> 0x01ca }
            if (r2 == 0) goto L_0x0179
            if (r1 <= 0) goto L_0x0138
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            android.util.SparseIntArray r2 = r2.mRunningFinalizerCount     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            android.util.SparseIntArray r3 = r3.mRunningFinalizerCount     // Catch:{ Throwable -> 0x01ca }
            int r3 = r3.size()     // Catch:{ Throwable -> 0x01ca }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x01ca }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x01ca }
            r2.put(r3, r6)     // Catch:{ Throwable -> 0x01ca }
            goto L_0x0179
        L_0x0138:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            android.util.SparseIntArray r2 = r2.mRunningFinalizerCount     // Catch:{ Throwable -> 0x01ca }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x01ca }
            if (r2 <= 0) goto L_0x0179
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r2 = r2.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            android.util.SparseIntArray r2 = r2.mRunningFinalizerCount     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x01ca }
            android.util.SparseIntArray r3 = r3.mRunningFinalizerCount     // Catch:{ Throwable -> 0x01ca }
            int r3 = r3.size()     // Catch:{ Throwable -> 0x01ca }
            int r3 = r3 - r7
            int r2 = r2.get(r3)     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor r1 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x0176 }
            com.taobao.onlinemonitor.TraceDetail r1 = r1.mTraceDetail     // Catch:{ Throwable -> 0x0176 }
            android.util.SparseIntArray r1 = r1.mRunningFinalizerCount     // Catch:{ Throwable -> 0x0176 }
            com.taobao.onlinemonitor.OnLineMonitor r3 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x0176 }
            com.taobao.onlinemonitor.TraceDetail r3 = r3.mTraceDetail     // Catch:{ Throwable -> 0x0176 }
            android.util.SparseIntArray r3 = r3.mRunningFinalizerCount     // Catch:{ Throwable -> 0x0176 }
            int r3 = r3.size()     // Catch:{ Throwable -> 0x0176 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0176 }
            int r6 = r6.intValue()     // Catch:{ Throwable -> 0x0176 }
            r1.put(r3, r6)     // Catch:{ Throwable -> 0x0176 }
            r1 = r2
            goto L_0x0179
        L_0x0176:
            r0 = move-exception
            r1 = r2
            goto L_0x01cb
        L_0x0179:
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r2.mOnLineStat     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor$MemroyStat r2 = r2.memroyStat     // Catch:{ Throwable -> 0x01ca }
            r2.finalizerSize = r1     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r2 = r2.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01ca }
            if (r2 == 0) goto L_0x0195
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r2 = r2.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01ca }
            int r2 = r2.finalizerSize     // Catch:{ Throwable -> 0x01ca }
            if (r2 >= r1) goto L_0x0195
            com.taobao.onlinemonitor.OnLineMonitor r2 = r15.mOnLineMonitor     // Catch:{ Throwable -> 0x01ca }
            com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo r2 = r2.mActivityRuntimeInfo     // Catch:{ Throwable -> 0x01ca }
            r2.finalizerSize = r1     // Catch:{ Throwable -> 0x01ca }
        L_0x0195:
            boolean r2 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug     // Catch:{ Throwable -> 0x01ca }
            if (r2 == 0) goto L_0x01ce
            java.lang.String r2 = "OnLineMonitor"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01ca }
            r3.<init>()     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r6 = "FinalizerReference="
            r3.append(r6)     // Catch:{ Throwable -> 0x01ca }
            r3.append(r0)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r0 = ",size="
            r3.append(r0)     // Catch:{ Throwable -> 0x01ca }
            r3.append(r1)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r0 = ", time="
            r3.append(r0)     // Catch:{ Throwable -> 0x01ca }
            long r6 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x01ca }
            long r6 = r6 / r4
            r0 = 0
            long r6 = r6 - r8
            r3.append(r6)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x01ca }
            android.util.Log.e(r2, r0)     // Catch:{ Throwable -> 0x01ca }
            goto L_0x01ce
        L_0x01c7:
            r0 = move-exception
            r1 = r11
            goto L_0x01cb
        L_0x01ca:
            r0 = move-exception
        L_0x01cb:
            r0.printStackTrace()
        L_0x01ce:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.CheckFinalizerReference.getFinalizerReferenceQueueSize():int");
    }

    /* access modifiers changed from: package-private */
    public void mergeFinalize() {
        if (OnLineMonitor.sIsTraceDetail && TraceDetail.sTraceFinalizer && this.mOnLineMonitor.mTraceDetail.mFinalizerObject != null && this.mOnLineMonitor.mTraceDetail.mFinalizerObject.size() > 0) {
            if (this.mOnLineMonitor.mActivityRuntimeInfo != null) {
                StringBuilder sb = new StringBuilder(500);
                try {
                    for (Map.Entry next : this.mOnLineMonitor.mTraceDetail.mFinalizerObject.entrySet()) {
                        Integer num = (Integer) next.getValue();
                        if (num != null && num.intValue() >= 20) {
                            sb.append((String) next.getKey());
                            sb.append("：");
                            sb.append(num);
                            sb.append("</br>");
                            Log.e("OnLineMonitor", "Finalizer=" + ((String) next.getKey()) + ", size=" + num);
                        }
                    }
                } catch (Throwable unused) {
                }
                sb.append(' ');
                this.mOnLineMonitor.mActivityRuntimeInfo.finalizerObject = sb.toString();
            }
            this.mOnLineMonitor.mTraceDetail.mFinalizerObject = null;
        }
    }
}
