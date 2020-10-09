package com.uc.webview.export.internal.setup;

import com.taobao.weex.BuildConfig;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import java.util.List;

/* compiled from: U4Source */
public abstract class av extends l {
    protected bs a;

    /* access modifiers changed from: protected */
    public abstract bs a(br brVar);

    /* access modifiers changed from: protected */
    public void a(String str, Object obj) {
    }

    /* access modifiers changed from: protected */
    public final void a(List<br> list) {
        int i;
        StringBuilder sb = new StringBuilder("runQuick ucms:");
        sb.append(list == null ? BuildConfig.buildJavascriptFrameworkVersion : Integer.valueOf(list.size()));
        Log.i("StandardSetupTask", sb.toString());
        if (list == null) {
            i = 0;
        } else {
            i = list.size();
        }
        IWaStat.WaStat.stat(IWaStat.SETUP_UCM_LIST_SIZE, Integer.toString(i));
        if (list == null || list.size() <= 0) {
            throw new UCSetupException(3004, "UCM packages not found, status:" + af.c());
        }
        b(list.get(0));
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0248  */
    /* JADX WARNING: Removed duplicated region for block: B:92:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void b(com.uc.webview.export.internal.setup.br r22) {
        /*
            r21 = this;
            r1 = r21
            r0 = r22
            com.uc.webview.export.internal.utility.d r2 = com.uc.webview.export.internal.utility.d.a()
            java.lang.String r3 = "gk_quick_init"
            r4 = 1
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r4)
            r2.a(r3, r5)
            r2 = 6
            r3 = 11
            r5 = 0
            r1.mUCM = r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.af.c = r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 296(0x128, float:4.15E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r0 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r0 = r0.soDirPath     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r6 = "e_in_lp_cb"
            java.lang.Object r6 = r1.getOption(r6)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r1.a(r0, r6)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r0 = com.uc.webview.export.internal.setup.af.b()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            if (r0 == 0) goto L_0x0038
            com.uc.webview.export.internal.setup.br r0 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r0 = r0.isFromDisk     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            if (r0 != 0) goto L_0x0043
        L_0x0038:
            com.uc.webview.export.internal.setup.br r0 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.bs r0 = r1.a((com.uc.webview.export.internal.setup.br) r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r1.a = r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0.c()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
        L_0x0043:
            r0 = 297(0x129, float:4.16E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 255(0xff, float:3.57E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.af.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r0 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r0.coreImplModule     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r6 = 0
            if (r0 == 0) goto L_0x00ca
            com.uc.webview.export.internal.setup.br r0 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r0.coreImplModule     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object r0 = r0.first     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r7 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            android.util.Pair<java.lang.String, java.lang.String> r7 = r7.coreImplModule     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object r7 = r7.second     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r8 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r8 = r8.soDirPath     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.ClassLoader r0 = com.uc.webview.export.internal.setup.af.a(r0, r7, r8)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r7 = 259(0x103, float:3.63E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r7)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.bs r7 = r1.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            if (r7 == 0) goto L_0x009d
            com.uc.webview.export.internal.setup.bs r7 = r1.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae$b r8 = com.uc.webview.export.internal.setup.ae.b.VERIFY_CORE_JAR     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object r9 = com.uc.webview.export.internal.setup.bs.g     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            monitor-enter(r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.util.HashSet<com.uc.webview.export.internal.setup.ae$b> r10 = r7.h     // Catch:{ all -> 0x009a }
            boolean r10 = r10.isEmpty()     // Catch:{ all -> 0x009a }
            if (r10 == 0) goto L_0x0089
            monitor-exit(r9)     // Catch:{ all -> 0x009a }
            goto L_0x009d
        L_0x0089:
            java.util.HashSet<com.uc.webview.export.internal.setup.ae$b> r7 = r7.h     // Catch:{ all -> 0x009a }
            boolean r7 = r7.contains(r8)     // Catch:{ all -> 0x009a }
            if (r7 == 0) goto L_0x0098
            com.uc.webview.export.internal.setup.ae r7 = com.uc.webview.export.internal.setup.ae.a()     // Catch:{ all -> 0x009a }
            r7.b((com.uc.webview.export.internal.setup.ae.b) r8)     // Catch:{ all -> 0x009a }
        L_0x0098:
            monitor-exit(r9)     // Catch:{ all -> 0x009a }
            goto L_0x009d
        L_0x009a:
            r0 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x009a }
            throw r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
        L_0x009d:
            r7 = 260(0x104, float:3.64E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r7)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            long r7 = com.uc.webview.export.internal.uc.startup.b.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r9 = "setup"
            r1.callback(r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r9 = 312(0x138, float:4.37E-43)
            long r10 = com.uc.webview.export.internal.uc.startup.b.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r12 = 0
            long r10 = r10 - r7
            com.uc.webview.export.internal.uc.startup.b.a((int) r9, (long) r10)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r7 = r21.isStopped()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            if (r7 == 0) goto L_0x00cb
            java.lang.String r0 = "StandardSetupTask"
            java.lang.String r4 = "startQuickSetupTransaction process stopped"
            com.uc.webview.export.internal.utility.Log.i(r0, r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object[] r0 = new java.lang.Object[r6]     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.af.a(r2, r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            goto L_0x0200
        L_0x00ca:
            r0 = r5
        L_0x00cb:
            r7 = 305(0x131, float:4.27E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r7)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r7[r6] = r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.af.a(r4, r7)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 313(0x139, float:4.39E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r0 = com.uc.webview.export.internal.uc.startup.a.b()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            if (r0 == 0) goto L_0x00fe
            com.uc.webview.export.internal.setup.ae r0 = com.uc.webview.export.internal.setup.ae.a()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            int r4 = com.uc.webview.export.internal.setup.ae.d.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae$b r7 = com.uc.webview.export.internal.setup.ae.b.INIT_UCMOBILE_WEBKIT     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae$a r8 = new com.uc.webview.export.internal.setup.ae$a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae r9 = com.uc.webview.export.internal.setup.ae.a()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r9.getClass()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.aw r10 = new com.uc.webview.export.internal.setup.aw     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r10.<init>(r1)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r8.<init>(r10, r5)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0.a(r4, r7, r8, r5)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
        L_0x00fe:
            r0 = 306(0x132, float:4.29E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.i r0 = new com.uc.webview.export.internal.setup.i     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0.<init>()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0.run()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 263(0x107, float:3.69E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 273(0x111, float:3.83E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.bs r0 = r1.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            if (r0 == 0) goto L_0x0147
            com.uc.webview.export.internal.setup.bs r0 = r1.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object r4 = com.uc.webview.export.internal.setup.bs.g     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            monitor-enter(r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.util.HashSet<com.uc.webview.export.internal.setup.ae$b> r7 = r0.h     // Catch:{ all -> 0x0144 }
            boolean r7 = r7.isEmpty()     // Catch:{ all -> 0x0144 }
            if (r7 == 0) goto L_0x0128
            monitor-exit(r4)     // Catch:{ all -> 0x0144 }
            goto L_0x0147
        L_0x0128:
            java.util.HashSet<com.uc.webview.export.internal.setup.ae$b> r0 = r0.h     // Catch:{ all -> 0x0144 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0144 }
        L_0x012e:
            boolean r7 = r0.hasNext()     // Catch:{ all -> 0x0144 }
            if (r7 == 0) goto L_0x0142
            java.lang.Object r7 = r0.next()     // Catch:{ all -> 0x0144 }
            com.uc.webview.export.internal.setup.ae$b r7 = (com.uc.webview.export.internal.setup.ae.b) r7     // Catch:{ all -> 0x0144 }
            com.uc.webview.export.internal.setup.ae r8 = com.uc.webview.export.internal.setup.ae.a()     // Catch:{ all -> 0x0144 }
            r8.b((com.uc.webview.export.internal.setup.ae.b) r7)     // Catch:{ all -> 0x0144 }
            goto L_0x012e
        L_0x0142:
            monitor-exit(r4)     // Catch:{ all -> 0x0144 }
            goto L_0x0147
        L_0x0144:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0144 }
            throw r0     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
        L_0x0147:
            r0 = 274(0x112, float:3.84E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.j.b()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            long r7 = com.uc.webview.export.internal.uc.startup.b.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            android.content.Context r0 = com.uc.webview.export.internal.setup.af.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r4 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.util.concurrent.ConcurrentHashMap r9 = r1.mOptions     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r15 = com.uc.webview.export.internal.setup.g.a(r0, r4, r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = new com.uc.webview.export.internal.setup.UCMRunningInfo     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            android.content.Context r11 = r21.getContext()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r12 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.ClassLoader r13 = com.uc.webview.export.internal.setup.af.e()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.br r4 = r1.mUCM     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.ClassLoader r14 = r4.mSdkShellClassLoader     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r16 = 0
            android.content.Context r4 = com.uc.webview.export.internal.setup.af.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.interfaces.UCMobileWebKit r17 = com.uc.webview.export.internal.setup.j.a((android.content.Context) r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            int r18 = com.uc.webview.export.internal.SDKFactory.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.util.concurrent.ConcurrentHashMap r4 = r1.mOptions     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r9 = "scst_flag"
            java.lang.Object r4 = r4.get(r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r19 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.util.concurrent.ConcurrentHashMap r4 = r1.mOptions     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r9 = "scst_flag"
            java.lang.Object r4 = r4.get(r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            boolean r4 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            int r20 = com.uc.webview.export.internal.utility.k.a((boolean) r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r10 = r0
            r10.<init>(r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r1.setLoadedUCM(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 309(0x135, float:4.33E-43)
            long r9 = com.uc.webview.export.internal.uc.startup.b.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r4 = 0
            long r9 = r9 - r7
            com.uc.webview.export.internal.uc.startup.b.a((int) r0, (long) r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            long r7 = com.uc.webview.export.internal.uc.startup.b.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.String r0 = "init"
            r1.callback(r0)     // Catch:{ UCSetupException -> 0x01c6, Throwable -> 0x01ba }
            java.lang.String r0 = "switch"
            r1.callback(r0)     // Catch:{ UCSetupException -> 0x01c6, Throwable -> 0x01ba }
            goto L_0x01ca
        L_0x01ba:
            r0 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r4 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r9 = 4018(0xfb2, float:5.63E-42)
            r4.<init>((int) r9, (java.lang.Throwable) r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r1.setException(r4)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            goto L_0x01ca
        L_0x01c6:
            r0 = move-exception
            r1.setException(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
        L_0x01ca:
            r0 = 307(0x133, float:4.3E-43)
            long r9 = com.uc.webview.export.internal.uc.startup.b.d()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r4 = 0
            long r9 = r9 - r7
            com.uc.webview.export.internal.uc.startup.b.a((int) r0, (long) r9)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae r0 = com.uc.webview.export.internal.setup.ae.a()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            int r4 = com.uc.webview.export.internal.setup.ae.d.a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae$b r7 = com.uc.webview.export.internal.setup.ae.b.INIT_SDK_SETTINGS     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae$a r8 = new com.uc.webview.export.internal.setup.ae$a     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ae r9 = com.uc.webview.export.internal.setup.ae.a()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r9.getClass()     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.ax r10 = new com.uc.webview.export.internal.setup.ax     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r10.<init>(r1)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r8.<init>(r10, r5)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0.a(r4, r7, r8, r5)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            java.lang.Object[] r0 = new java.lang.Object[r6]     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            com.uc.webview.export.internal.setup.af.a(r3, r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 264(0x108, float:3.7E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
            r0 = 256(0x100, float:3.59E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)     // Catch:{ UCSetupException -> 0x0230, Throwable -> 0x020f }
        L_0x0200:
            com.uc.webview.export.internal.setup.bs r0 = r1.a
            if (r0 == 0) goto L_0x020b
            com.uc.webview.export.internal.setup.bs r0 = r1.a
            r0.e()
            r1.a = r5
        L_0x020b:
            r0 = r5
            goto L_0x023c
        L_0x020d:
            r0 = move-exception
            goto L_0x0224
        L_0x020f:
            r0 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r4 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ all -> 0x020d }
            r6 = 3003(0xbbb, float:4.208E-42)
            r4.<init>((int) r6, (java.lang.Throwable) r0)     // Catch:{ all -> 0x020d }
            com.uc.webview.export.internal.setup.bs r0 = r1.a
            if (r0 == 0) goto L_0x0222
            com.uc.webview.export.internal.setup.bs r0 = r1.a
            r0.e()
            r1.a = r5
        L_0x0222:
            r0 = r4
            goto L_0x023c
        L_0x0224:
            com.uc.webview.export.internal.setup.bs r2 = r1.a
            if (r2 == 0) goto L_0x022f
            com.uc.webview.export.internal.setup.bs r2 = r1.a
            r2.e()
            r1.a = r5
        L_0x022f:
            throw r0
        L_0x0230:
            r0 = move-exception
            com.uc.webview.export.internal.setup.bs r4 = r1.a
            if (r4 == 0) goto L_0x023c
            com.uc.webview.export.internal.setup.bs r4 = r1.a
            r4.e()
            r1.a = r5
        L_0x023c:
            int r4 = com.uc.webview.export.internal.setup.af.c()
            if (r4 == r3) goto L_0x0264
            int r3 = com.uc.webview.export.internal.setup.af.c()
            if (r3 == r2) goto L_0x0264
            if (r0 != 0) goto L_0x0263
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r2 = 3004(0xbbc, float:4.21E-42)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "UCM packages not found, status:"
            r3.<init>(r4)
            int r4 = com.uc.webview.export.internal.setup.af.c()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r0.<init>((int) r2, (java.lang.String) r3)
        L_0x0263:
            throw r0
        L_0x0264:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.av.b(com.uc.webview.export.internal.setup.br):void");
    }
}
