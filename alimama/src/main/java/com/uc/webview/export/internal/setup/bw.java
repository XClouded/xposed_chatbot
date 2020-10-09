package com.uc.webview.export.internal.setup;

import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
public class bw extends l {
    /* access modifiers changed from: private */
    public static final String e = "bw";
    public UpdateTask a = null;
    /* access modifiers changed from: package-private */
    public String b;
    /* access modifiers changed from: package-private */
    public String c;
    /* access modifiers changed from: package-private */
    public String d;
    /* access modifiers changed from: private */
    public boolean f = false;
    /* access modifiers changed from: private */
    public boolean g = false;

    /* JADX WARNING: Code restructure failed: missing block: B:114:0x02cf, code lost:
        if (((java.lang.Integer) r2.first).intValue() == 1) goto L_0x031e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x02d1, code lost:
        r3 = ((java.lang.Integer) r2.first).intValue();
        r2 = r2.second;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:?, code lost:
        r5 = getTotalLoadedUCM();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x02e5, code lost:
        if (r5 == null) goto L_0x0316;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x02ea, code lost:
        if (r5.coreType != 2) goto L_0x02f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x02f6, code lost:
        if (com.uc.webview.export.internal.utility.d.a().b(com.uc.webview.export.extension.UCCore.OPTION_MULTI_CORE_TYPE) != false) goto L_0x0316;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:?, code lost:
        com.uc.webview.export.internal.utility.Log.d(e, ".shareCoreWaitTimeout UCCore had initialized.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x0308, code lost:
        if (((java.lang.Integer) r2.first).intValue() == 1) goto L_0x031e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x030a, code lost:
        r3 = ((java.lang.Integer) r2.first).intValue();
        r2 = r2.second;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:?, code lost:
        com.uc.webview.export.internal.utility.i.a((java.lang.Runnable) new com.uc.webview.export.internal.setup.bz(r1, r11, r3, r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0347, code lost:
        r0 = th;
     */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x032e A[Catch:{ all -> 0x0513 }] */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0339 A[Catch:{ all -> 0x0513 }] */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x0350 A[SYNTHETIC, Splitter:B:152:0x0350] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r22 = this;
            r1 = r22
            java.lang.String r0 = "CONTEXT"
            java.lang.Object r0 = r1.getOption(r0)
            r10 = r0
            android.content.Context r10 = (android.content.Context) r10
            java.lang.String r0 = "ucmUpdUrl"
            java.lang.Object r0 = r1.getOption(r0)
            r4 = r0
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r0 = "chkMultiCore"
            java.lang.Object r0 = r1.getOption(r0)
            r11 = r0
            java.lang.Boolean r11 = (java.lang.Boolean) r11
            java.lang.String r0 = "dlChecker"
            java.lang.Object r0 = r1.getOption(r0)
            r12 = r0
            java.util.concurrent.Callable r12 = (java.util.concurrent.Callable) r12
            java.lang.String r0 = "i"
            java.lang.String r2 = e
            int r2 = com.uc.webview.export.cyclone.UCLogger.createToken(r0, r2)
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = getTotalLoadedUCM()
            r13 = 2
            if (r0 == 0) goto L_0x0083
            boolean r3 = r0.isShareCore
            if (r3 == 0) goto L_0x0040
            int r0 = r0.coreType
            if (r0 != r13) goto L_0x003e
            goto L_0x0040
        L_0x003e:
            r0 = 0
            goto L_0x0041
        L_0x0040:
            r0 = 1
        L_0x0041:
            if (r0 != 0) goto L_0x0068
            java.lang.String r3 = "sc_udst"
            java.lang.String r3 = com.uc.webview.export.extension.UCCore.getParam(r3)
            java.lang.String r5 = e
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "stileUpdate : "
            r6.<init>(r7)
            r6.append(r3)
            java.lang.String r6 = r6.toString()
            com.uc.webview.export.internal.utility.Log.d(r5, r6)
            boolean r3 = com.uc.webview.export.internal.utility.k.b((java.lang.String) r3)
            if (r3 == 0) goto L_0x0068
            java.lang.String r0 = "csc_usl"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            r0 = 1
        L_0x0068:
            java.lang.String r3 = e
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "stile update task : "
            r5.<init>(r6)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            com.uc.webview.export.internal.utility.Log.d(r3, r5)
            if (r0 != 0) goto L_0x0083
            java.lang.String r0 = "csc_usp"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            return
        L_0x0083:
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r4)
            if (r0 != 0) goto L_0x051d
            com.uc.webview.export.internal.setup.bo r9 = new com.uc.webview.export.internal.setup.bo
            r9.<init>()
            java.lang.String r0 = "updWait"
            java.lang.Object r0 = r1.getOption(r0)
            if (r0 != 0) goto L_0x009a
            r5 = 7200000(0x6ddd00, double:3.5572727E-317)
            goto L_0x00b8
        L_0x009a:
            boolean r3 = r0 instanceof java.lang.Long
            if (r3 == 0) goto L_0x00a5
            java.lang.Long r0 = (java.lang.Long) r0
            long r5 = r0.longValue()
            goto L_0x00b8
        L_0x00a5:
            boolean r3 = r0 instanceof java.lang.Integer
            if (r3 == 0) goto L_0x00b0
            java.lang.Integer r0 = (java.lang.Integer) r0
            long r5 = r0.longValue()
            goto L_0x00b8
        L_0x00b0:
            java.lang.String r0 = java.lang.String.valueOf(r0)
            long r5 = java.lang.Long.parseLong(r0)
        L_0x00b8:
            java.lang.Long r8 = java.lang.Long.valueOf(r5)
            long r5 = r8.longValue()
            r13 = 600000(0x927c0, double:2.964394E-318)
            long r5 = java.lang.Math.min(r5, r13)
            java.lang.Long r3 = java.lang.Long.valueOf(r5)
            java.lang.String r0 = "sc_ustwm"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ Exception -> 0x00ed }
            boolean r5 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ Exception -> 0x00ed }
            if (r5 != 0) goto L_0x00f5
            java.lang.Long r0 = java.lang.Long.valueOf(r0)     // Catch:{ Exception -> 0x00ed }
            long r5 = r0.longValue()     // Catch:{ Exception -> 0x00ed }
            long r13 = r8.longValue()     // Catch:{ Exception -> 0x00ed }
            long r5 = java.lang.Math.min(r5, r13)     // Catch:{ Exception -> 0x00ed }
            java.lang.Long r0 = java.lang.Long.valueOf(r5)     // Catch:{ Exception -> 0x00ed }
            r3 = r0
            goto L_0x00f5
        L_0x00ed:
            r0 = move-exception
            java.lang.String r5 = e
            java.lang.String r6 = "Long.valueOf(String) exceptin."
            com.uc.webview.export.internal.utility.Log.d(r5, r6, r0)
        L_0x00f5:
            r0 = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r5 = "run:update from ["
            r3.<init>(r5)
            r3.append(r4)
            java.lang.String r5 = "]"
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            r5 = 0
            java.lang.Throwable[] r6 = new java.lang.Throwable[r5]
            com.uc.webview.export.cyclone.UCLogger.print((int) r2, (java.lang.String) r3, (java.lang.Throwable[]) r6)
            java.lang.String r2 = e
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r5 = "shareCoreWait: "
            r3.<init>(r5)
            r3.append(r0)
            java.lang.String r5 = " wait: "
            r3.append(r5)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            com.uc.webview.export.cyclone.UCElapseTime r13 = new com.uc.webview.export.cyclone.UCElapseTime
            r13.<init>()
            java.lang.String r2 = "upd_pro_lk"
            java.lang.Object r2 = r1.getOption(r2)
            r14 = r2
            android.webkit.ValueCallback r14 = (android.webkit.ValueCallback) r14
            if (r14 == 0) goto L_0x0143
            java.lang.String r2 = "lock"
            r14.onReceiveValue(r2)     // Catch:{ all -> 0x0140 }
            goto L_0x0143
        L_0x0140:
            r0 = move-exception
            goto L_0x0515
        L_0x0143:
            monitor-enter(r9)     // Catch:{ all -> 0x0140 }
            java.lang.String r2 = "updates"
            java.io.File r2 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r10, (java.lang.String) r2)     // Catch:{ all -> 0x050f }
            java.lang.String r3 = "dwnRetryWait"
            java.lang.Object r3 = r1.getOption(r3)     // Catch:{ all -> 0x050f }
            r7 = 0
            if (r3 != 0) goto L_0x0156
            r17 = r7
            goto L_0x017a
        L_0x0156:
            boolean r5 = r3 instanceof java.lang.Long     // Catch:{ all -> 0x050f }
            if (r5 == 0) goto L_0x0161
            java.lang.Long r3 = (java.lang.Long) r3     // Catch:{ all -> 0x050f }
            long r5 = r3.longValue()     // Catch:{ all -> 0x050f }
            goto L_0x0174
        L_0x0161:
            boolean r5 = r3 instanceof java.lang.Integer     // Catch:{ all -> 0x050f }
            if (r5 == 0) goto L_0x016c
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x050f }
            long r5 = r3.longValue()     // Catch:{ all -> 0x050f }
            goto L_0x0174
        L_0x016c:
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x050f }
            long r5 = java.lang.Long.parseLong(r3)     // Catch:{ all -> 0x050f }
        L_0x0174:
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x050f }
            r17 = r3
        L_0x017a:
            java.lang.String r3 = "dwnRetryMaxWait"
            java.lang.Object r3 = r1.getOption(r3)     // Catch:{ all -> 0x050f }
            if (r3 != 0) goto L_0x0185
            r18 = r7
            goto L_0x01a9
        L_0x0185:
            boolean r5 = r3 instanceof java.lang.Long     // Catch:{ all -> 0x050f }
            if (r5 == 0) goto L_0x0190
            java.lang.Long r3 = (java.lang.Long) r3     // Catch:{ all -> 0x050f }
            long r5 = r3.longValue()     // Catch:{ all -> 0x050f }
            goto L_0x01a3
        L_0x0190:
            boolean r5 = r3 instanceof java.lang.Integer     // Catch:{ all -> 0x050f }
            if (r5 == 0) goto L_0x019b
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x050f }
            long r5 = r3.longValue()     // Catch:{ all -> 0x050f }
            goto L_0x01a3
        L_0x019b:
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x050f }
            long r5 = java.lang.Long.parseLong(r3)     // Catch:{ all -> 0x050f }
        L_0x01a3:
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x050f }
            r18 = r3
        L_0x01a9:
            com.uc.webview.export.utility.download.UpdateTask r6 = new com.uc.webview.export.utility.download.UpdateTask     // Catch:{ all -> 0x050f }
            java.lang.String r5 = r2.getAbsolutePath()     // Catch:{ all -> 0x050f }
            java.lang.String r19 = "sdk_shell.jar"
            com.uc.webview.export.internal.utility.g$b r3 = new com.uc.webview.export.internal.utility.g$b     // Catch:{ all -> 0x050f }
            java.lang.String r2 = "ut_cvsv"
            r3.<init>(r2)     // Catch:{ all -> 0x050f }
            r2 = r6
            r20 = r3
            r3 = r10
            r15 = r6
            r6 = r19
            r21 = r11
            r11 = r7
            r7 = r20
            r19 = r8
            r8 = r17
            r11 = r9
            r9 = r18
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0513 }
            r1.a = r15     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r1.a     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "check"
            com.uc.webview.export.internal.setup.by r4 = new com.uc.webview.export.internal.setup.by     // Catch:{ all -> 0x0513 }
            r4.<init>(r1, r10, r12)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "exception"
            com.uc.webview.export.internal.setup.ch r4 = new com.uc.webview.export.internal.setup.ch     // Catch:{ all -> 0x0513 }
            r4.<init>(r1)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "downloadException"
            com.uc.webview.export.internal.setup.cg r4 = new com.uc.webview.export.internal.setup.cg     // Catch:{ all -> 0x0513 }
            r4.<init>(r1)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "success"
            com.uc.webview.export.internal.setup.cf r4 = new com.uc.webview.export.internal.setup.cf     // Catch:{ all -> 0x0513 }
            r4.<init>(r1, r11)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "failed"
            com.uc.webview.export.internal.setup.ce r4 = new com.uc.webview.export.internal.setup.ce     // Catch:{ all -> 0x0513 }
            r4.<init>(r1, r11)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "exists"
            com.uc.webview.export.internal.setup.cd r4 = new com.uc.webview.export.internal.setup.cd     // Catch:{ all -> 0x0513 }
            r4.<init>(r1, r11)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "deleteDownFile"
            com.uc.webview.export.internal.setup.cc r4 = new com.uc.webview.export.internal.setup.cc     // Catch:{ all -> 0x0513 }
            r4.<init>(r1)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "progress"
            com.uc.webview.export.internal.setup.cb r4 = new com.uc.webview.export.internal.setup.cb     // Catch:{ all -> 0x0513 }
            r4.<init>(r1)     // Catch:{ all -> 0x0513 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.onEvent(r3, r4)     // Catch:{ all -> 0x0513 }
            r2.start()     // Catch:{ all -> 0x0513 }
            long r2 = r0.longValue()     // Catch:{ all -> 0x0513 }
            android.util.Pair r2 = r11.a(r2)     // Catch:{ all -> 0x0513 }
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            r4 = 4
            if (r3 == 0) goto L_0x0369
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            if (r3 == r4) goto L_0x0369
            java.lang.String r3 = e     // Catch:{ all -> 0x0513 }
            java.lang.String r5 = ".shareCoreWaitTimeout"
            com.uc.webview.export.internal.utility.Log.d(r3, r5)     // Catch:{ all -> 0x0513 }
            java.lang.String r3 = "sc_ldpl"
            java.lang.String r3 = com.uc.webview.export.extension.UCCore.getParam(r3)     // Catch:{ all -> 0x034b }
            java.lang.String r5 = "sc_lshco"
            boolean r3 = r5.equals(r3)     // Catch:{ all -> 0x034b }
            if (r3 != 0) goto L_0x0285
            java.lang.String r3 = e     // Catch:{ all -> 0x0280 }
            java.lang.String r5 = ".shareCoreWaitTimeout !CDKeys.CD_VALUE_LOAD_POLICY_SHARE_CORE.equals(shareCoreLoadPolicy)."
            com.uc.webview.export.internal.utility.Log.d(r3, r5)     // Catch:{ all -> 0x0280 }
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            r5 = 1
            if (r3 == r5) goto L_0x031e
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            java.lang.Object r2 = r2.second     // Catch:{ all -> 0x0513 }
        L_0x027b:
            r11.a(r3, r2)     // Catch:{ all -> 0x0513 }
            goto L_0x031e
        L_0x0280:
            r0 = move-exception
            r16 = 1
            goto L_0x034e
        L_0x0285:
            java.lang.String r3 = "shareCoreEvt"
            android.webkit.ValueCallback r3 = r1.getCallback(r3)     // Catch:{ all -> 0x034b }
            if (r3 != 0) goto L_0x02aa
            java.lang.String r3 = e     // Catch:{ all -> 0x0280 }
            java.lang.String r5 = ".shareCoreWaitTimeout dlShareCoreCB == null."
            com.uc.webview.export.internal.utility.Log.d(r3, r5)     // Catch:{ all -> 0x0280 }
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            r5 = 1
            if (r3 == r5) goto L_0x031e
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            java.lang.Object r2 = r2.second     // Catch:{ all -> 0x0513 }
            goto L_0x027b
        L_0x02aa:
            monitor-enter(r22)     // Catch:{ all -> 0x034b }
            boolean r5 = r1.f     // Catch:{ all -> 0x0342 }
            if (r5 != 0) goto L_0x02e0
            boolean r5 = r1.g     // Catch:{ all -> 0x0342 }
            if (r5 != 0) goto L_0x02e0
            java.lang.Object r5 = r2.first     // Catch:{ all -> 0x0342 }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ all -> 0x0342 }
            int r5 = r5.intValue()     // Catch:{ all -> 0x0342 }
            r6 = 1
            if (r5 == r6) goto L_0x02e0
            java.lang.String r3 = e     // Catch:{ all -> 0x02dc }
            java.lang.String r5 = ".shareCoreWaitTimeout !mHasExcepted && !mHasFailed"
            com.uc.webview.export.internal.utility.Log.d(r3, r5)     // Catch:{ all -> 0x02dc }
            monitor-exit(r22)     // Catch:{ all -> 0x02dc }
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            r5 = 1
            if (r3 == r5) goto L_0x031e
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            java.lang.Object r2 = r2.second     // Catch:{ all -> 0x0513 }
            goto L_0x027b
        L_0x02dc:
            r0 = move-exception
            r16 = 1
            goto L_0x0345
        L_0x02e0:
            monitor-exit(r22)     // Catch:{ all -> 0x0342 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r5 = getTotalLoadedUCM()     // Catch:{ all -> 0x034b }
            if (r5 == 0) goto L_0x0316
            int r5 = r5.coreType     // Catch:{ all -> 0x034b }
            r6 = 2
            if (r5 != r6) goto L_0x02f8
            com.uc.webview.export.internal.utility.d r5 = com.uc.webview.export.internal.utility.d.a()     // Catch:{ all -> 0x034b }
            java.lang.String r6 = "MULTI_CORE_TYPE"
            boolean r5 = r5.b(r6)     // Catch:{ all -> 0x034b }
            if (r5 != 0) goto L_0x0316
        L_0x02f8:
            java.lang.String r3 = e     // Catch:{ all -> 0x0280 }
            java.lang.String r5 = ".shareCoreWaitTimeout UCCore had initialized."
            com.uc.webview.export.internal.utility.Log.d(r3, r5)     // Catch:{ all -> 0x0280 }
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            r5 = 1
            if (r3 == r5) goto L_0x031e
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            java.lang.Object r2 = r2.second     // Catch:{ all -> 0x0513 }
            goto L_0x027b
        L_0x0316:
            com.uc.webview.export.internal.setup.bz r5 = new com.uc.webview.export.internal.setup.bz     // Catch:{ all -> 0x034b }
            r5.<init>(r1, r11, r3, r2)     // Catch:{ all -> 0x034b }
            com.uc.webview.export.internal.utility.i.a((java.lang.Runnable) r5)     // Catch:{ all -> 0x034b }
        L_0x031e:
            long r2 = r19.longValue()     // Catch:{ all -> 0x0513 }
            long r5 = r0.longValue()     // Catch:{ all -> 0x0513 }
            r7 = 0
            long r2 = r2 - r5
            r5 = 0
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0339
            long r2 = r19.longValue()     // Catch:{ all -> 0x0513 }
            long r5 = r0.longValue()     // Catch:{ all -> 0x0513 }
            r0 = 0
            long r2 = r2 - r5
            goto L_0x033d
        L_0x0339:
            long r2 = r19.longValue()     // Catch:{ all -> 0x0513 }
        L_0x033d:
            android.util.Pair r2 = r11.a(r2)     // Catch:{ all -> 0x0513 }
            goto L_0x0369
        L_0x0342:
            r0 = move-exception
            r16 = 0
        L_0x0345:
            monitor-exit(r22)     // Catch:{ all -> 0x0349 }
            throw r0     // Catch:{ all -> 0x0347 }
        L_0x0347:
            r0 = move-exception
            goto L_0x034e
        L_0x0349:
            r0 = move-exception
            goto L_0x0345
        L_0x034b:
            r0 = move-exception
            r16 = 0
        L_0x034e:
            if (r16 == 0) goto L_0x0368
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            r4 = 1
            if (r3 == r4) goto L_0x0368
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0513 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0513 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0513 }
            java.lang.Object r2 = r2.second     // Catch:{ all -> 0x0513 }
            r11.a(r3, r2)     // Catch:{ all -> 0x0513 }
        L_0x0368:
            throw r0     // Catch:{ all -> 0x0513 }
        L_0x0369:
            monitor-exit(r11)     // Catch:{ all -> 0x0513 }
            if (r14 == 0) goto L_0x0371
            java.lang.String r0 = "unlock"
            r14.onReceiveValue(r0)
        L_0x0371:
            java.lang.String r0 = e
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r5 = "result.first: "
            r3.<init>(r5)
            java.lang.Object r5 = r2.first
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r3)
            java.lang.Object r0 = r2.first
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r3 = 1
            if (r0 == r3) goto L_0x04ec
            java.lang.Object r0 = r2.first
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r3 = 3
            if (r0 == r3) goto L_0x04e0
            java.lang.Object r0 = r2.first
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r3 = 8
            if (r0 == r3) goto L_0x04c4
            java.lang.Object r0 = r2.first
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 != 0) goto L_0x03b4
            r0 = 1
            goto L_0x03b5
        L_0x03b4:
            r0 = 0
        L_0x03b5:
            java.lang.Object r2 = r2.first
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            if (r2 != r4) goto L_0x03c1
            r2 = 1
            goto L_0x03c2
        L_0x03c1:
            r2 = 0
        L_0x03c2:
            if (r0 != 0) goto L_0x03ca
            if (r2 == 0) goto L_0x03c7
            goto L_0x03ca
        L_0x03c7:
            r16 = 0
            goto L_0x03cc
        L_0x03ca:
            r16 = 1
        L_0x03cc:
            com.uc.webview.export.utility.download.UpdateTask r3 = r1.a
            java.io.File r3 = r3.getUpdateDir()
            if (r0 == 0) goto L_0x03e1
            java.lang.String r4 = "csc_udetm"
            long r5 = r13.getMilis()
            java.lang.String r5 = java.lang.Long.toString(r5)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r4, r5)
        L_0x03e1:
            if (r16 == 0) goto L_0x04bc
            if (r0 != 0) goto L_0x03f5
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = com.uc.webview.export.utility.SetupTask.getTotalLoadedUCM()
            if (r0 == 0) goto L_0x03f5
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = getTotalLoadedUCM()
            boolean r0 = com.uc.webview.export.internal.utility.k.a((com.uc.webview.export.internal.setup.UCMRunningInfo) r0)
            if (r0 == 0) goto L_0x04bc
        L_0x03f5:
            java.lang.String r0 = e
            java.lang.String r4 = "new ThinSetupTask."
            com.uc.webview.export.internal.utility.Log.d(r0, r4)
            com.uc.webview.export.internal.utility.d r0 = com.uc.webview.export.internal.utility.d.a()
            java.lang.String r4 = "gk_upd_exist"
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            r0.a(r4, r2)
            boolean r0 = com.uc.webview.export.internal.utility.k.g()
            if (r0 == 0) goto L_0x0415
            com.uc.webview.export.internal.setup.az r0 = new com.uc.webview.export.internal.setup.az
            r0.<init>()
            goto L_0x041a
        L_0x0415:
            com.uc.webview.export.internal.setup.bf r0 = new com.uc.webview.export.internal.setup.bf
            r0.<init>()
        L_0x041a:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setParent(r1)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.util.concurrent.ConcurrentHashMap r2 = r1.mOptions
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setOptions((java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r2)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.util.concurrent.ConcurrentHashMap r2 = r1.mCallbacks
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setCallbacks(r2)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "dexFilePath"
            r4 = 0
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "soFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "resFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "ucmCfgFile"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "ucmLibDir"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "ucmZipDir"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "ucmZipFile"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "ucmKrlDir"
            java.lang.String r3 = r3.getAbsolutePath()
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r3)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "bo_enable_load_class"
            com.uc.webview.export.internal.setup.UCMRunningInfo r3 = getTotalLoadedUCM()
            boolean r3 = com.uc.webview.export.internal.utility.k.a((com.uc.webview.export.internal.setup.UCMRunningInfo) r3)
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r3)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "switch"
            com.uc.webview.export.internal.setup.ca r3 = new com.uc.webview.export.internal.setup.ca
            r3.<init>(r1)
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.onEvent((java.lang.String) r2, r3)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "stop"
            com.uc.webview.export.internal.setup.UCAsyncTask$b r3 = new com.uc.webview.export.internal.setup.UCAsyncTask$b
            r3.<init>()
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.onEvent((java.lang.String) r2, r3)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r2 = "setup"
            boolean r3 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r21)
            if (r3 == 0) goto L_0x04ac
            goto L_0x04b2
        L_0x04ac:
            com.uc.webview.export.internal.setup.bx r7 = new com.uc.webview.export.internal.setup.bx
            r7.<init>(r1)
            r4 = r7
        L_0x04b2:
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.onEvent((java.lang.String) r2, r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            r0.start()
            return
        L_0x04bc:
            java.lang.String r0 = e
            java.lang.String r2 = "else, need not new ThinSetupTask."
            com.uc.webview.export.internal.utility.Log.d(r0, r2)
            return
        L_0x04c4:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r2 = 4030(0xfbe, float:5.647E-42)
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Thread r4 = java.lang.Thread.currentThread()
            java.lang.String r4 = r4.getName()
            r5 = 0
            r3[r5] = r4
            java.lang.String r4 = "Thread [%s] waiting timeout for share core task."
            java.lang.String r3 = java.lang.String.format(r4, r3)
            r0.<init>((int) r2, (java.lang.String) r3)
            throw r0
        L_0x04e0:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r3 = 4019(0xfb3, float:5.632E-42)
            java.lang.Object r2 = r2.second
            java.lang.Exception r2 = (java.lang.Exception) r2
            r0.<init>((int) r3, (java.lang.Throwable) r2)
            throw r0
        L_0x04ec:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r2 = 4010(0xfaa, float:5.619E-42)
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Thread r4 = java.lang.Thread.currentThread()
            java.lang.String r4 = r4.getName()
            r5 = 0
            r3[r5] = r4
            java.lang.String r4 = java.lang.String.valueOf(r19)
            r5 = 1
            r3[r5] = r4
            java.lang.String r4 = "Thread [%s] waiting for update is up to [%s] milis."
            java.lang.String r3 = java.lang.String.format(r4, r3)
            r0.<init>((int) r2, (java.lang.String) r3)
            throw r0
        L_0x050f:
            r0 = move-exception
            r11 = r9
        L_0x0511:
            monitor-exit(r11)     // Catch:{ all -> 0x0513 }
            throw r0     // Catch:{ all -> 0x0140 }
        L_0x0513:
            r0 = move-exception
            goto L_0x0511
        L_0x0515:
            if (r14 == 0) goto L_0x051c
            java.lang.String r2 = "unlock"
            r14.onReceiveValue(r2)
        L_0x051c:
            throw r0
        L_0x051d:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r2 = 3014(0xbc6, float:4.224E-42)
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r4 = "ucmUpdUrl"
            r5 = 0
            r3[r5] = r4
            java.lang.String r4 = "Option [%s] expected."
            java.lang.String r3 = java.lang.String.format(r4, r3)
            r0.<init>((int) r2, (java.lang.String) r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.bw.run():void");
    }
}
