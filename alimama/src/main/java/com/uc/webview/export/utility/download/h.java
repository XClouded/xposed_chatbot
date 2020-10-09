package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: U4Source */
final class h implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ ValueCallback b;
    final /* synthetic */ ValueCallback c;
    final /* synthetic */ ValueCallback d;
    final /* synthetic */ UpdateTask e;

    h(UpdateTask updateTask, ValueCallback valueCallback, ValueCallback valueCallback2, ValueCallback valueCallback3, ValueCallback valueCallback4) {
        this.e = updateTask;
        this.a = valueCallback;
        this.b = valueCallback2;
        this.c = valueCallback3;
        this.d = valueCallback4;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0080 A[Catch:{ Throwable -> 0x003f, Throwable -> 0x00a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0099 A[Catch:{ Throwable -> 0x00a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ void onReceiveValue(java.lang.Object r11) {
        /*
            r10 = this;
            com.uc.webview.export.utility.download.DownloadTask r11 = (com.uc.webview.export.utility.download.DownloadTask) r11
            r0 = 1
            com.uc.webview.export.utility.download.UpdateTask r1 = r10.e     // Catch:{ Throwable -> 0x00a6 }
            long[] r1 = r1.d     // Catch:{ Throwable -> 0x00a6 }
            long r2 = r11.getTotalSize()     // Catch:{ Throwable -> 0x00a6 }
            r1[r0] = r2     // Catch:{ Throwable -> 0x00a6 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r10.e     // Catch:{ Throwable -> 0x00a6 }
            long[] r1 = r1.d     // Catch:{ Throwable -> 0x00a6 }
            r2 = 2
            long r3 = r11.getLastModified()     // Catch:{ Throwable -> 0x00a6 }
            r1[r2] = r3     // Catch:{ Throwable -> 0x00a6 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r10.e     // Catch:{ Throwable -> 0x00a6 }
            java.io.File r1 = r1.getUpdateDir()     // Catch:{ Throwable -> 0x00a6 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r10.e     // Catch:{ Throwable -> 0x00a6 }
            java.lang.String r2 = r2.i     // Catch:{ Throwable -> 0x00a6 }
            boolean r2 = com.uc.webview.export.utility.download.UpdateTask.isFinished(r1, r2)     // Catch:{ Throwable -> 0x00a6 }
            if (r2 == 0) goto L_0x0047
            java.lang.String r1 = "sdk_ucm_e"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)     // Catch:{ Throwable -> 0x00a6 }
            android.webkit.ValueCallback r1 = r10.a     // Catch:{ Throwable -> 0x003f }
            if (r1 == 0) goto L_0x0043
            android.webkit.ValueCallback r1 = r10.a     // Catch:{ Throwable -> 0x003f }
            com.uc.webview.export.utility.download.UpdateTask r2 = r10.e     // Catch:{ Throwable -> 0x003f }
            r1.onReceiveValue(r2)     // Catch:{ Throwable -> 0x003f }
            goto L_0x0043
        L_0x003f:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Throwable -> 0x00a6 }
        L_0x0043:
            r11.stop()     // Catch:{ Throwable -> 0x00a6 }
            return
        L_0x0047:
            long r2 = r11.getTotalSize()     // Catch:{ Exception -> 0x0076 }
            r4 = 0
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 != 0) goto L_0x0053
            r2 = r4
            goto L_0x005e
        L_0x0053:
            long r6 = r11.getCurrentSize()     // Catch:{ Exception -> 0x0076 }
            r8 = 10
            long r6 = r6 * r8
            long r6 = r6 / r2
            long r2 = r6 * r8
        L_0x005e:
            r6 = 100
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 > 0) goto L_0x006b
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 >= 0) goto L_0x0069
            goto L_0x006b
        L_0x0069:
            int r2 = (int) r2     // Catch:{ Exception -> 0x0076 }
            goto L_0x006c
        L_0x006b:
            r2 = -1
        L_0x006c:
            java.lang.String r3 = "sdk_ucm_p"
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x0076 }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r3, r2)     // Catch:{ Exception -> 0x0076 }
            goto L_0x007a
        L_0x0076:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ Throwable -> 0x00a6 }
        L_0x007a:
            boolean r1 = r1.exists()     // Catch:{ Throwable -> 0x00a6 }
            if (r1 == 0) goto L_0x0095
            java.lang.String r1 = "sdk_ucm_f"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)     // Catch:{ Throwable -> 0x00a6 }
            android.webkit.ValueCallback r1 = r10.b     // Catch:{ Throwable -> 0x0091 }
            if (r1 == 0) goto L_0x0095
            android.webkit.ValueCallback r1 = r10.b     // Catch:{ Throwable -> 0x0091 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r10.e     // Catch:{ Throwable -> 0x0091 }
            r1.onReceiveValue(r2)     // Catch:{ Throwable -> 0x0091 }
            goto L_0x0095
        L_0x0091:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Throwable -> 0x00a6 }
        L_0x0095:
            android.webkit.ValueCallback r1 = r10.c     // Catch:{ Throwable -> 0x00a1 }
            if (r1 == 0) goto L_0x00a0
            android.webkit.ValueCallback r1 = r10.c     // Catch:{ Throwable -> 0x00a1 }
            com.uc.webview.export.utility.download.UpdateTask r2 = r10.e     // Catch:{ Throwable -> 0x00a1 }
            r1.onReceiveValue(r2)     // Catch:{ Throwable -> 0x00a1 }
        L_0x00a0:
            return
        L_0x00a1:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Throwable -> 0x00a6 }
            return
        L_0x00a6:
            r1 = move-exception
            r11.stop()
            r1.printStackTrace()
            java.lang.String r11 = "sdk_ucm_en"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r11)
            java.lang.Class r11 = r1.getClass()
            java.lang.String r11 = r11.getName()
            int r11 = r11.hashCode()
            java.lang.String r2 = "sdk_ucm_le"
            java.lang.String r11 = java.lang.String.valueOf(r11)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r11)
            com.uc.webview.export.utility.download.UpdateTask r11 = r10.e
            java.lang.Object[] r11 = r11.f
            r11[r0] = r1
            android.webkit.ValueCallback r11 = r10.d     // Catch:{ Throwable -> 0x00db }
            if (r11 == 0) goto L_0x00da
            android.webkit.ValueCallback r11 = r10.d     // Catch:{ Throwable -> 0x00db }
            com.uc.webview.export.utility.download.UpdateTask r0 = r10.e     // Catch:{ Throwable -> 0x00db }
            r11.onReceiveValue(r0)     // Catch:{ Throwable -> 0x00db }
        L_0x00da:
            return
        L_0x00db:
            r11 = move-exception
            r11.printStackTrace()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.h.onReceiveValue(java.lang.Object):void");
    }
}
