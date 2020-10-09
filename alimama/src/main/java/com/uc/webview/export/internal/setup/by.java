package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class by implements ValueCallback<UpdateTask> {
    final /* synthetic */ Context a;
    final /* synthetic */ Callable b;
    final /* synthetic */ bw c;

    by(bw bwVar, Context context, Callable callable) {
        this.c = bwVar;
        this.a = context;
        this.b = callable;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r2.getAbsolutePath().startsWith(r0.getAbsolutePath()) == false) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ void onReceiveValue(java.lang.Object r5) {
        /*
            r4 = this;
            r5 = 0
            android.content.Context r0 = r4.a     // Catch:{ Throwable -> 0x003e }
            java.lang.String r1 = "updates"
            java.io.File r0 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x003e }
            com.uc.webview.export.internal.setup.UCMRunningInfo r1 = com.uc.webview.export.internal.setup.UCSetupTask.getTotalLoadedUCM()     // Catch:{ Throwable -> 0x003e }
            if (r1 == 0) goto L_0x002c
            com.uc.webview.export.internal.setup.br r2 = r1.ucmPackageInfo     // Catch:{ Throwable -> 0x003e }
            if (r2 == 0) goto L_0x002c
            com.uc.webview.export.internal.setup.br r1 = r1.ucmPackageInfo     // Catch:{ Throwable -> 0x003e }
            java.lang.String r1 = r1.dataDir     // Catch:{ Throwable -> 0x003e }
            if (r1 == 0) goto L_0x002c
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x003e }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x003e }
            java.lang.String r1 = r2.getAbsolutePath()     // Catch:{ Throwable -> 0x003e }
            java.lang.String r3 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x003e }
            boolean r1 = r1.startsWith(r3)     // Catch:{ Throwable -> 0x003e }
            if (r1 != 0) goto L_0x002d
        L_0x002c:
            r2 = r5
        L_0x002d:
            r1 = 1
            if (r2 != 0) goto L_0x003a
            com.uc.webview.export.internal.setup.bw r2 = r4.c     // Catch:{ Throwable -> 0x003e }
            com.uc.webview.export.utility.download.UpdateTask r2 = r2.a     // Catch:{ Throwable -> 0x003e }
            java.io.File r2 = r2.getUpdateDir()     // Catch:{ Throwable -> 0x003e }
        L_0x003a:
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r0, r1, r2)     // Catch:{ Throwable -> 0x003e }
            goto L_0x0042
        L_0x003e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0042:
            java.util.concurrent.Callable r0 = r4.b     // Catch:{ Exception -> 0x006a }
            if (r0 == 0) goto L_0x005d
            java.util.concurrent.Callable r0 = r4.b     // Catch:{ Exception -> 0x006a }
            java.lang.Object r0 = r0.call()     // Catch:{ Exception -> 0x006a }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Exception -> 0x006a }
            boolean r0 = r0.booleanValue()     // Catch:{ Exception -> 0x006a }
            if (r0 == 0) goto L_0x0055
            goto L_0x005d
        L_0x0055:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x006a }
            java.lang.String r0 = "Update should be in wifi network."
            r5.<init>(r0)     // Catch:{ Exception -> 0x006a }
            throw r5     // Catch:{ Exception -> 0x006a }
        L_0x005d:
            com.uc.webview.export.internal.setup.bw r0 = r4.c     // Catch:{ Exception -> 0x006a }
            android.util.Pair r1 = new android.util.Pair     // Catch:{ Exception -> 0x006a }
            java.lang.String r2 = "sdk_ucm_wifi"
            r1.<init>(r2, r5)     // Catch:{ Exception -> 0x006a }
            r0.callbackStat(r1)     // Catch:{ Exception -> 0x006a }
            return
        L_0x006a:
            r5 = move-exception
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r5 = r5.getMessage()
            r0.<init>(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.by.onReceiveValue(java.lang.Object):void");
    }
}
