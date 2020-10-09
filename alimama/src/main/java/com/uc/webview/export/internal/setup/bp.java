package com.uc.webview.export.internal.setup;

import android.os.Handler;
import android.os.Looper;

/* compiled from: U4Source */
final class bp extends Handler {
    final /* synthetic */ UCAsyncTask a;
    private UCAsyncTask b = null;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    bp(UCAsyncTask uCAsyncTask, Looper looper) {
        super(looper);
        this.a = uCAsyncTask;
    }

    private static void a(UCAsyncTask uCAsyncTask) {
        if (uCAsyncTask != null) {
            synchronized (uCAsyncTask.d) {
                uCAsyncTask.mPercent = (int) ((((float) UCAsyncTask.e(uCAsyncTask)) * 100.0f) / ((float) UCAsyncTask.f(uCAsyncTask)));
            }
            uCAsyncTask.callback("progress");
        }
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
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:368)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:172)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public final void dispatchMessage(android.os.Message r9) {
        /*
            r8 = this;
            r0 = 0
            java.lang.Boolean r1 = com.uc.webview.export.internal.setup.UCAsyncTask.p     // Catch:{ Throwable -> 0x0011 }
            boolean r1 = r1.booleanValue()     // Catch:{ Throwable -> 0x0011 }
            if (r1 == 0) goto L_0x0011
            com.uc.webview.export.cyclone.UCElapseTime r1 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ Throwable -> 0x0011 }
            r1.<init>()     // Catch:{ Throwable -> 0x0011 }
            goto L_0x0012
        L_0x0011:
            r1 = r0
        L_0x0012:
            java.lang.Runnable r2 = r9.getCallback()     // Catch:{ Throwable -> 0x009e }
            boolean r3 = r2 instanceof com.uc.webview.export.internal.setup.UCAsyncTask     // Catch:{ Throwable -> 0x009e }
            if (r3 == 0) goto L_0x0044
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = (com.uc.webview.export.internal.setup.UCAsyncTask) r2     // Catch:{ Throwable -> 0x009e }
            r8.b = r2     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x009e }
            long r2 = r2.n     // Catch:{ Throwable -> 0x009e }
            r4 = 0
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x003d
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x0034 }
            long r2 = r2.n     // Catch:{ Throwable -> 0x0034 }
            java.lang.Thread.sleep(r2)     // Catch:{ Throwable -> 0x0034 }
            goto L_0x0038
        L_0x0034:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ Throwable -> 0x009e }
        L_0x0038:
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x009e }
            long unused = r2.n = 0     // Catch:{ Throwable -> 0x009e }
        L_0x003d:
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x009e }
            java.lang.String r3 = "start"
            r2.callback(r3)     // Catch:{ Throwable -> 0x009e }
        L_0x0044:
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.bo r2 = r2.i     // Catch:{ Throwable -> 0x009e }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x009b }
            boolean r3 = r3.h     // Catch:{ all -> 0x009b }
            if (r3 == 0) goto L_0x0054
            r9 = r0
        L_0x0054:
            monitor-exit(r2)     // Catch:{ all -> 0x009b }
            if (r9 == 0) goto L_0x008a
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.a     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.bo r3 = r3.i     // Catch:{ Throwable -> 0x009e }
            monitor-enter(r3)     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = r8.a     // Catch:{ all -> 0x0087 }
            boolean r4 = r4.g     // Catch:{ all -> 0x0087 }
            if (r4 == 0) goto L_0x0085
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = r8.a     // Catch:{ all -> 0x0087 }
            boolean unused = r4.g = false     // Catch:{ all -> 0x0087 }
            java.lang.String r4 = "pause"
            r2.callback(r4)     // Catch:{ all -> 0x0087 }
            com.uc.webview.export.internal.setup.UCAsyncTask r4 = r8.a     // Catch:{ all -> 0x0087 }
            com.uc.webview.export.internal.setup.bo r4 = r4.i     // Catch:{ all -> 0x0087 }
            r5 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r4.a(r5)     // Catch:{ all -> 0x0087 }
            java.lang.String r4 = "resume"
            r2.callback(r4)     // Catch:{ all -> 0x0087 }
        L_0x0085:
            monitor-exit(r3)     // Catch:{ all -> 0x0087 }
            goto L_0x008a
        L_0x0087:
            r9 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0087 }
            throw r9     // Catch:{ Throwable -> 0x009e }
        L_0x008a:
            if (r9 == 0) goto L_0x00c6
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.UCSetupException r2 = r2.mException     // Catch:{ Throwable -> 0x009e }
            if (r2 != 0) goto L_0x00c6
            super.dispatchMessage(r9)     // Catch:{ Throwable -> 0x009e }
            com.uc.webview.export.internal.setup.UCAsyncTask r9 = r8.b     // Catch:{ Throwable -> 0x009e }
            a(r9)     // Catch:{ Throwable -> 0x009e }
            goto L_0x00c6
        L_0x009b:
            r9 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x009b }
            throw r9     // Catch:{ Throwable -> 0x009e }
        L_0x009e:
            r9 = move-exception
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b
            boolean r3 = r9 instanceof com.uc.webview.export.internal.setup.UCSetupException
            if (r3 == 0) goto L_0x00a8
            com.uc.webview.export.internal.setup.UCSetupException r9 = (com.uc.webview.export.internal.setup.UCSetupException) r9
            goto L_0x00ae
        L_0x00a8:
            com.uc.webview.export.internal.setup.UCSetupException r3 = new com.uc.webview.export.internal.setup.UCSetupException
            r3.<init>((java.lang.Throwable) r9)
            r9 = r3
        L_0x00ae:
            r2.setException(r9)
            com.uc.webview.export.internal.setup.UCAsyncTask r9 = r8.b     // Catch:{ Throwable -> 0x00c2 }
            java.lang.Object r9 = r9.d     // Catch:{ Throwable -> 0x00c2 }
            monitor-enter(r9)     // Catch:{ Throwable -> 0x00c2 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b     // Catch:{ all -> 0x00bf }
            com.uc.webview.export.internal.setup.UCAsyncTask.j(r2)     // Catch:{ all -> 0x00bf }
            monitor-exit(r9)     // Catch:{ all -> 0x00bf }
            goto L_0x00c6
        L_0x00bf:
            r2 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00bf }
            throw r2     // Catch:{ Throwable -> 0x00c2 }
        L_0x00c2:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00c6:
            java.lang.Boolean r9 = com.uc.webview.export.internal.setup.UCAsyncTask.p     // Catch:{ Throwable -> 0x00d3 }
            boolean r9 = r9.booleanValue()     // Catch:{ Throwable -> 0x00d3 }
            if (r9 == 0) goto L_0x00d3
            com.uc.webview.export.internal.setup.UCAsyncTask r9 = r8.b     // Catch:{ Throwable -> 0x00d3 }
            goto L_0x00d4
        L_0x00d3:
            r9 = r0
        L_0x00d4:
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.b
            java.lang.Object r2 = r2.d
            monitor-enter(r2)
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCSetupException r3 = r3.mException     // Catch:{ all -> 0x01a7 }
            r4 = 1
            if (r3 == 0) goto L_0x00e4
            r3 = 1
            goto L_0x00e5
        L_0x00e4:
            r3 = 0
        L_0x00e5:
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r8.b     // Catch:{ all -> 0x01a7 }
            boolean r5 = r5.h     // Catch:{ all -> 0x01a7 }
            if (r3 != 0) goto L_0x00ef
            if (r5 == 0) goto L_0x00f4
        L_0x00ef:
            com.uc.webview.export.internal.setup.UCAsyncTask r6 = r8.b     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCAsyncTask.j(r6)     // Catch:{ all -> 0x01a7 }
        L_0x00f4:
            com.uc.webview.export.internal.setup.UCAsyncTask r6 = r8.b     // Catch:{ all -> 0x01a7 }
            java.util.concurrent.ConcurrentLinkedQueue r6 = r6.b     // Catch:{ all -> 0x01a7 }
            if (r6 == 0) goto L_0x0115
            java.lang.Object r6 = r6.poll()     // Catch:{ Throwable -> 0x0109 }
            java.lang.Runnable r6 = (java.lang.Runnable) r6     // Catch:{ Throwable -> 0x0109 }
            if (r6 == 0) goto L_0x0107
            monitor-exit(r2)     // Catch:{ all -> 0x01a7 }
            r0 = r6
            goto L_0x0150
        L_0x0107:
            r0 = r6
            goto L_0x0115
        L_0x0109:
            r3 = move-exception
            com.uc.webview.export.internal.setup.UCAsyncTask r6 = r8.b     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCSetupException r7 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ all -> 0x01a7 }
            r7.<init>((java.lang.Throwable) r3)     // Catch:{ all -> 0x01a7 }
            r6.setException(r7)     // Catch:{ all -> 0x01a7 }
            r3 = 1
        L_0x0115:
            if (r5 == 0) goto L_0x011f
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            java.lang.String r4 = "stop"
            r3.callback(r4)     // Catch:{ all -> 0x01a7 }
            goto L_0x0137
        L_0x011f:
            if (r3 == 0) goto L_0x0129
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            java.lang.String r4 = "exception"
            r3.callback(r4)     // Catch:{ all -> 0x01a7 }
            goto L_0x0130
        L_0x0129:
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            java.lang.String r4 = "success"
            r3.callback(r4)     // Catch:{ all -> 0x01a7 }
        L_0x0130:
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            java.lang.String r4 = "gone"
            r3.callback(r4)     // Catch:{ all -> 0x01a7 }
        L_0x0137:
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            java.lang.String r4 = "die"
            r3.callback(r4)     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r3.a     // Catch:{ all -> 0x01a7 }
            r8.b = r3     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            a(r3)     // Catch:{ all -> 0x01a7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r3 = r8.b     // Catch:{ all -> 0x01a7 }
            if (r3 != 0) goto L_0x01a4
            monitor-exit(r2)     // Catch:{ all -> 0x01a7 }
        L_0x0150:
            if (r0 == 0) goto L_0x015c
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r8.a
            android.os.Handler r2 = r2.l
            r2.post(r0)
            goto L_0x0161
        L_0x015c:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r8.a
            com.uc.webview.export.internal.setup.UCAsyncTask.n(r0)
        L_0x0161:
            java.lang.Boolean r0 = com.uc.webview.export.internal.setup.UCAsyncTask.p     // Catch:{ Throwable -> 0x01a3 }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x01a3 }
            if (r0 == 0) goto L_0x01a2
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r8.a     // Catch:{ Throwable -> 0x01a3 }
            java.util.Vector r0 = r0.q     // Catch:{ Throwable -> 0x01a3 }
            android.util.Pair r2 = new android.util.Pair     // Catch:{ Throwable -> 0x01a3 }
            if (r9 != 0) goto L_0x0178
            java.lang.String r9 = "null"
            goto L_0x0180
        L_0x0178:
            java.lang.Class r9 = r9.getClass()     // Catch:{ Throwable -> 0x01a3 }
            java.lang.String r9 = r9.getSimpleName()     // Catch:{ Throwable -> 0x01a3 }
        L_0x0180:
            android.util.Pair r3 = new android.util.Pair     // Catch:{ Throwable -> 0x01a3 }
            long r4 = r1.getMilis()     // Catch:{ Throwable -> 0x01a3 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x01a3 }
            long r5 = r1.getMilisCpu()     // Catch:{ Throwable -> 0x01a3 }
            java.lang.Long r1 = java.lang.Long.valueOf(r5)     // Catch:{ Throwable -> 0x01a3 }
            r3.<init>(r4, r1)     // Catch:{ Throwable -> 0x01a3 }
            r2.<init>(r9, r3)     // Catch:{ Throwable -> 0x01a3 }
            r0.add(r2)     // Catch:{ Throwable -> 0x01a3 }
            com.uc.webview.export.internal.setup.UCAsyncTask r9 = r8.a     // Catch:{ Throwable -> 0x01a3 }
            java.lang.String r0 = "cost"
            r9.callback(r0)     // Catch:{ Throwable -> 0x01a3 }
        L_0x01a2:
            return
        L_0x01a3:
            return
        L_0x01a4:
            monitor-exit(r2)     // Catch:{ all -> 0x01a7 }
            goto L_0x00d4
        L_0x01a7:
            r9 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x01a7 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.bp.dispatchMessage(android.os.Message):void");
    }
}
