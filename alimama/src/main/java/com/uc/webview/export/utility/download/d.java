package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: U4Source */
final class d implements Runnable {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ String b;
    final /* synthetic */ DownloadTask c;
    final /* synthetic */ ValueCallback d;
    final /* synthetic */ ValueCallback e;
    final /* synthetic */ ValueCallback f;
    final /* synthetic */ ValueCallback g;
    final /* synthetic */ ValueCallback h;
    final /* synthetic */ UpdateTask i;

    d(UpdateTask updateTask, ValueCallback valueCallback, String str, DownloadTask downloadTask, ValueCallback valueCallback2, ValueCallback valueCallback3, ValueCallback valueCallback4, ValueCallback valueCallback5, ValueCallback valueCallback6) {
        this.i = updateTask;
        this.a = valueCallback;
        this.b = str;
        this.c = downloadTask;
        this.d = valueCallback2;
        this.e = valueCallback3;
        this.f = valueCallback4;
        this.g = valueCallback5;
        this.h = valueCallback6;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:62:0x016c */
    /* JADX WARNING: Removed duplicated region for block: B:102:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x018b A[Catch:{ Exception -> 0x00c2, Throwable -> 0x01f1, Throwable -> 0x000f, Throwable -> 0x01f6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0196 A[Catch:{ Exception -> 0x00c2, Throwable -> 0x01f1, Throwable -> 0x000f, Throwable -> 0x01f6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01a4 A[Catch:{ Throwable -> 0x01ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01b4 A[Catch:{ Throwable -> 0x01bc }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r17 = this;
            r1 = r17
            r2 = 1
            android.webkit.ValueCallback r0 = r1.a     // Catch:{ Throwable -> 0x000f }
            if (r0 == 0) goto L_0x0013
            android.webkit.ValueCallback r0 = r1.a     // Catch:{ Throwable -> 0x000f }
            com.uc.webview.export.utility.download.UpdateTask r3 = r1.i     // Catch:{ Throwable -> 0x000f }
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x000f }
            goto L_0x0013
        L_0x000f:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
        L_0x0013:
            r3 = 0
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x002f }
            android.webkit.ValueCallback r0 = r0.j     // Catch:{ Throwable -> 0x002f }
            if (r0 == 0) goto L_0x0033
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x002f }
            android.webkit.ValueCallback r0 = r0.j     // Catch:{ Throwable -> 0x002f }
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x002f }
            r5 = 3
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x002f }
            r4[r3] = r5     // Catch:{ Throwable -> 0x002f }
            r0.onReceiveValue(r4)     // Catch:{ Throwable -> 0x002f }
            goto L_0x0033
        L_0x002f:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
        L_0x0033:
            java.lang.String r0 = r1.b     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r0 = r0.toLowerCase()     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r4 = ".7z"
            boolean r0 = r0.endsWith(r4)     // Catch:{ Throwable -> 0x01f6 }
            if (r0 != 0) goto L_0x00a6
            com.uc.webview.export.utility.download.DownloadTask r0 = r1.c     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r4 = r0.getFilePath()     // Catch:{ Throwable -> 0x01f6 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f6 }
            java.lang.Object[] r0 = r0.f     // Catch:{ Throwable -> 0x01f6 }
            r0 = r0[r3]     // Catch:{ Throwable -> 0x01f6 }
            r5 = r0
            android.content.Context r5 = (android.content.Context) r5     // Catch:{ Throwable -> 0x01f6 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f6 }
            java.lang.Object[] r0 = r0.f     // Catch:{ Throwable -> 0x01f6 }
            r0 = r0[r3]     // Catch:{ Throwable -> 0x01f6 }
            r6 = r0
            android.content.Context r6 = (android.content.Context) r6     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r7 = "com.UCMobile"
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f6 }
            android.webkit.ValueCallback r8 = r0.j     // Catch:{ Throwable -> 0x01f6 }
            r9 = 0
            boolean r0 = com.uc.webview.export.internal.utility.g.a(r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x01f6 }
            if (r0 != 0) goto L_0x0096
            java.lang.String r0 = "sdk_dec7z_ls"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x01f6 }
            com.uc.webview.export.utility.download.DownloadTask r0 = r1.c     // Catch:{ Throwable -> 0x01f6 }
            r0.delete()     // Catch:{ Throwable -> 0x01f6 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f6 }
            java.lang.Object[] r0 = r0.f     // Catch:{ Throwable -> 0x01f6 }
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r4 = "Archive verify failed."
            r3.<init>(r4)     // Catch:{ Throwable -> 0x01f6 }
            r0[r2] = r3     // Catch:{ Throwable -> 0x01f6 }
            android.webkit.ValueCallback r0 = r1.d     // Catch:{ Throwable -> 0x0091 }
            if (r0 == 0) goto L_0x0090
            android.webkit.ValueCallback r0 = r1.d     // Catch:{ Throwable -> 0x0091 }
            com.uc.webview.export.utility.download.UpdateTask r3 = r1.i     // Catch:{ Throwable -> 0x0091 }
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x0091 }
        L_0x0090:
            return
        L_0x0091:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
            return
        L_0x0096:
            android.webkit.ValueCallback r0 = r1.a     // Catch:{ Throwable -> 0x00a2 }
            if (r0 == 0) goto L_0x00a6
            android.webkit.ValueCallback r0 = r1.a     // Catch:{ Throwable -> 0x00a2 }
            com.uc.webview.export.utility.download.UpdateTask r4 = r1.i     // Catch:{ Throwable -> 0x00a2 }
            r0.onReceiveValue(r4)     // Catch:{ Throwable -> 0x00a2 }
            goto L_0x00a6
        L_0x00a2:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
        L_0x00a6:
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f6 }
            java.io.File r15 = r0.getUpdateDir()     // Catch:{ Throwable -> 0x01f6 }
            com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r15)     // Catch:{ Throwable -> 0x01f6 }
            new java.io.File(r15, com.uc.webview.export.utility.download.UpdateTask.STOP_FLG_FILE_NAME).delete()     // Catch:{ Throwable -> 0x01f6 }
            com.uc.webview.export.utility.download.UpdateTask.a((java.io.File) r15, (boolean) r3)     // Catch:{ Throwable -> 0x01f6 }
            r14 = 2
            android.webkit.ValueCallback r0 = r1.e     // Catch:{ Throwable -> 0x00c6 }
            if (r0 == 0) goto L_0x00ca
            android.webkit.ValueCallback r0 = r1.e     // Catch:{ Throwable -> 0x00c6 }
            com.uc.webview.export.utility.download.UpdateTask r4 = r1.i     // Catch:{ Throwable -> 0x00c6 }
            r0.onReceiveValue(r4)     // Catch:{ Throwable -> 0x00c6 }
            goto L_0x00ca
        L_0x00c2:
            r0 = move-exception
            r3 = r0
            goto L_0x01c1
        L_0x00c6:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x00c2 }
        L_0x00ca:
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Exception -> 0x00c2 }
            java.lang.Object[] r0 = r0.f     // Catch:{ Exception -> 0x00c2 }
            r0 = r0[r3]     // Catch:{ Exception -> 0x00c2 }
            r4 = r0
            android.content.Context r4 = (android.content.Context) r4     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r0 = r1.b     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r5 = ".7z"
            boolean r5 = r0.endsWith(r5)     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r6 = r1.b     // Catch:{ Exception -> 0x00c2 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Exception -> 0x00c2 }
            long[] r0 = r0.d     // Catch:{ Exception -> 0x00c2 }
            r7 = r0[r2]     // Catch:{ Exception -> 0x00c2 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Exception -> 0x00c2 }
            long[] r0 = r0.d     // Catch:{ Exception -> 0x00c2 }
            r9 = r0[r14]     // Catch:{ Exception -> 0x00c2 }
            java.io.File r11 = new java.io.File     // Catch:{ Exception -> 0x00c2 }
            com.uc.webview.export.utility.download.DownloadTask r0 = r1.c     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r0 = r0.getFilePath()     // Catch:{ Exception -> 0x00c2 }
            r11.<init>(r0)     // Catch:{ Exception -> 0x00c2 }
            r13 = 0
            r0 = 0
            int r16 = com.uc.webview.export.cyclone.UCCyclone.DecFileOrign.Update     // Catch:{ Exception -> 0x00c2 }
            r12 = r15
            r14 = r0
            r3 = r15
            r15 = r16
            com.uc.webview.export.cyclone.UCCyclone.decompressIfNeeded((android.content.Context) r4, (boolean) r5, (java.lang.String) r6, (long) r7, (long) r9, (java.io.File) r11, (java.io.File) r12, (java.io.FilenameFilter) r13, (boolean) r14, (int) r15)     // Catch:{ Exception -> 0x00c2 }
            android.webkit.ValueCallback r0 = r1.f     // Catch:{ Throwable -> 0x0112 }
            if (r0 == 0) goto L_0x0116
            android.webkit.ValueCallback r0 = r1.f     // Catch:{ Throwable -> 0x0112 }
            com.uc.webview.export.utility.download.UpdateTask r4 = r1.i     // Catch:{ Throwable -> 0x0112 }
            r0.onReceiveValue(r4)     // Catch:{ Throwable -> 0x0112 }
            goto L_0x0116
        L_0x0112:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x00c2 }
        L_0x0116:
            com.uc.webview.export.utility.download.UpdateTask.a((java.io.File) r3, (boolean) r2)     // Catch:{ Throwable -> 0x01f6 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x016c }
            r0.<init>()     // Catch:{ Throwable -> 0x016c }
            java.lang.String r4 = r1.b     // Catch:{ Throwable -> 0x016c }
            r0.append(r4)     // Catch:{ Throwable -> 0x016c }
            java.lang.String r4 = "_pv"
            r0.append(r4)     // Catch:{ Throwable -> 0x016c }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x016c }
            com.uc.webview.export.utility.download.UpdateTask r4 = r1.i     // Catch:{ Throwable -> 0x016c }
            java.lang.Object[] r4 = r4.f     // Catch:{ Throwable -> 0x016c }
            r5 = 0
            r4 = r4[r5]     // Catch:{ Throwable -> 0x016c }
            android.content.Context r4 = (android.content.Context) r4     // Catch:{ Throwable -> 0x016c }
            boolean r0 = com.uc.webview.export.internal.utility.s.a(r0, r4)     // Catch:{ Throwable -> 0x016c }
            if (r0 == 0) goto L_0x014b
            java.lang.String r0 = com.uc.webview.export.utility.download.UpdateTask.a     // Catch:{ Throwable -> 0x016c }
            java.lang.String r4 = "multiple download success."
            com.uc.webview.export.internal.utility.Log.d(r0, r4)     // Catch:{ Throwable -> 0x016c }
            java.lang.String r0 = "csc_updod"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x016c }
        L_0x014b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x016c }
            r0.<init>()     // Catch:{ Throwable -> 0x016c }
            java.lang.String r4 = r1.b     // Catch:{ Throwable -> 0x016c }
            r0.append(r4)     // Catch:{ Throwable -> 0x016c }
            java.lang.String r4 = "_pv"
            r0.append(r4)     // Catch:{ Throwable -> 0x016c }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x016c }
            com.uc.webview.export.utility.download.UpdateTask r4 = r1.i     // Catch:{ Throwable -> 0x016c }
            java.lang.Object[] r4 = r4.f     // Catch:{ Throwable -> 0x016c }
            r5 = 0
            r4 = r4[r5]     // Catch:{ Throwable -> 0x016c }
            android.content.Context r4 = (android.content.Context) r4     // Catch:{ Throwable -> 0x016c }
            com.uc.webview.export.internal.utility.s.a(r0, r4, r2)     // Catch:{ Throwable -> 0x016c }
        L_0x016c:
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f6 }
            java.lang.Object[] r0 = r0.f     // Catch:{ Throwable -> 0x01f6 }
            r4 = 0
            r0 = r0[r4]     // Catch:{ Throwable -> 0x01f6 }
            android.content.Context r0 = (android.content.Context) r0     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r4 = "updates"
            java.io.File r0 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r0, (java.lang.String) r4)     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x01f6 }
            boolean r0 = r3.contains(r0)     // Catch:{ Throwable -> 0x01f6 }
            if (r0 == 0) goto L_0x0196
            com.uc.webview.export.utility.download.DownloadTask r0 = r1.c     // Catch:{ Throwable -> 0x01f6 }
            r0.delete(r2)     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r0 = "sdk_ucm_s"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x01f6 }
            goto L_0x01a0
        L_0x0196:
            com.uc.webview.export.utility.download.DownloadTask r0 = r1.c     // Catch:{ Throwable -> 0x01f6 }
            r0.delete()     // Catch:{ Throwable -> 0x01f6 }
            java.lang.String r0 = "sdk_ucm_so"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x01f6 }
        L_0x01a0:
            android.webkit.ValueCallback r0 = r1.a     // Catch:{ Throwable -> 0x01ac }
            if (r0 == 0) goto L_0x01b0
            android.webkit.ValueCallback r0 = r1.a     // Catch:{ Throwable -> 0x01ac }
            com.uc.webview.export.utility.download.UpdateTask r3 = r1.i     // Catch:{ Throwable -> 0x01ac }
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x01ac }
            goto L_0x01b0
        L_0x01ac:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
        L_0x01b0:
            android.webkit.ValueCallback r0 = r1.g     // Catch:{ Throwable -> 0x01bc }
            if (r0 == 0) goto L_0x01bb
            android.webkit.ValueCallback r0 = r1.g     // Catch:{ Throwable -> 0x01bc }
            com.uc.webview.export.utility.download.UpdateTask r3 = r1.i     // Catch:{ Throwable -> 0x01bc }
            r0.onReceiveValue(r3)     // Catch:{ Throwable -> 0x01bc }
        L_0x01bb:
            return
        L_0x01bc:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
            return
        L_0x01c1:
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f1 }
            android.webkit.ValueCallback r0 = r0.j     // Catch:{ Throwable -> 0x01f1 }
            if (r0 == 0) goto L_0x01f5
            com.uc.webview.export.utility.download.UpdateTask r0 = r1.i     // Catch:{ Throwable -> 0x01f1 }
            android.webkit.ValueCallback r0 = r0.j     // Catch:{ Throwable -> 0x01f1 }
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x01f1 }
            r5 = 9
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x01f1 }
            r6 = 0
            r4[r6] = r5     // Catch:{ Throwable -> 0x01f1 }
            java.lang.Class r5 = r3.getClass()     // Catch:{ Throwable -> 0x01f1 }
            java.lang.String r5 = r5.getName()     // Catch:{ Throwable -> 0x01f1 }
            int r5 = r5.hashCode()     // Catch:{ Throwable -> 0x01f1 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x01f1 }
            r4[r2] = r5     // Catch:{ Throwable -> 0x01f1 }
            r0.onReceiveValue(r4)     // Catch:{ Throwable -> 0x01f1 }
            goto L_0x01f5
        L_0x01f1:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x01f6 }
        L_0x01f5:
            throw r3     // Catch:{ Throwable -> 0x01f6 }
        L_0x01f6:
            r0 = move-exception
            r0.printStackTrace()
            java.lang.String r3 = "sdk_dec7z"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r3)
            java.lang.Class r3 = r0.getClass()
            java.lang.String r3 = r3.getName()
            int r3 = r3.hashCode()
            java.lang.String r4 = "sdk_ucm_le"
            java.lang.String r3 = java.lang.String.valueOf(r3)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r4, r3)
            com.uc.webview.export.utility.download.UpdateTask r3 = r1.i
            java.lang.Object[] r3 = r3.f
            r3[r2] = r0
            android.webkit.ValueCallback r0 = r1.h     // Catch:{ Throwable -> 0x0228 }
            if (r0 == 0) goto L_0x0227
            android.webkit.ValueCallback r0 = r1.h     // Catch:{ Throwable -> 0x0228 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r1.i     // Catch:{ Throwable -> 0x0228 }
            r0.onReceiveValue(r2)     // Catch:{ Throwable -> 0x0228 }
        L_0x0227:
            return
        L_0x0228:
            r0 = move-exception
            r0.printStackTrace()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.d.run():void");
    }
}
