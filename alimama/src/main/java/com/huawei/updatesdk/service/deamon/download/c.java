package com.huawei.updatesdk.service.deamon.download;

import android.os.Handler;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.support.pm.g;
import java.lang.ref.WeakReference;

public final class c extends Handler {
    private WeakReference<DownloadService> a;

    public c(DownloadService downloadService) {
        this.a = new WeakReference<>(downloadService);
    }

    private void a(DownloadTask downloadTask) {
        g.a(downloadTask.t(), downloadTask.w(), downloadTask);
    }

    public void a(DownloadService downloadService, DownloadTask downloadTask) {
        downloadService.a.c(downloadTask);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00bd, code lost:
        r0.append(r2);
        r0.append(r1);
        com.huawei.updatesdk.sdk.a.c.a.a.a.d(r6, r0.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00cb, code lost:
        com.huawei.updatesdk.service.deamon.download.a.a(r0, r1, r6.what);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleMessage(android.os.Message r6) {
        /*
            r5 = this;
            java.lang.ref.WeakReference<com.huawei.updatesdk.service.deamon.download.DownloadService> r0 = r5.a
            java.lang.Object r0 = r0.get()
            com.huawei.updatesdk.service.deamon.download.DownloadService r0 = (com.huawei.updatesdk.service.deamon.download.DownloadService) r0
            if (r0 != 0) goto L_0x0012
            java.lang.String r6 = "DownloadService"
            java.lang.String r0 = "handleMessage, but service object is null."
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r6, r0)
            return
        L_0x0012:
            super.handleMessage(r6)
            java.lang.Object r1 = r6.obj
            if (r1 == 0) goto L_0x00d0
            java.lang.Object r1 = r6.obj
            boolean r1 = r1 instanceof com.huawei.updatesdk.sdk.service.download.bean.DownloadTask
            if (r1 != 0) goto L_0x0021
            goto L_0x00d0
        L_0x0021:
            java.lang.Object r1 = r6.obj
            com.huawei.updatesdk.sdk.service.download.bean.DownloadTask r1 = (com.huawei.updatesdk.sdk.service.download.bean.DownloadTask) r1
            int r2 = r6.what
            switch(r2) {
                case 0: goto L_0x00cb;
                case 1: goto L_0x00cb;
                case 2: goto L_0x00cb;
                case 3: goto L_0x00ac;
                case 4: goto L_0x008a;
                case 5: goto L_0x0078;
                case 6: goto L_0x0050;
                default: goto L_0x002a;
            }
        L_0x002a:
            java.lang.String r0 = "DownloadService"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Unkonw message "
            r2.append(r3)
            int r6 = r6.what
            r2.append(r6)
            java.lang.String r6 = " ,taskid:"
            r2.append(r6)
            int r6 = r1.o()
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r0, r6)
            goto L_0x00d0
        L_0x0050:
            int r2 = r1.g()
            r3 = 6
            if (r2 != r3) goto L_0x005d
            com.huawei.updatesdk.sdk.service.download.d r2 = r0.a
            r3 = 1
            r2.a((int) r3)
        L_0x005d:
            java.lang.String r2 = "DownloadService"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Download downloadPaused task.getId():"
            r3.append(r4)
            int r4 = r1.o()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r2, r3)
            goto L_0x00cb
        L_0x0078:
            r5.a(r0, r1)
            int r6 = r6.what
            com.huawei.updatesdk.service.deamon.download.a.a(r0, r1, r6)
            java.lang.String r6 = "DownloadService"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "task download failed:"
            goto L_0x00bd
        L_0x008a:
            r5.a(r0, r1)
            int r6 = r6.what
            com.huawei.updatesdk.service.deamon.download.a.a(r0, r1, r6)
            r5.a(r1)
            java.lang.String r6 = "DownloadService"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "task download completed:"
            r0.append(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r6, r0)
            goto L_0x00d0
        L_0x00ac:
            r5.a(r0, r1)
            int r6 = r6.what
            com.huawei.updatesdk.service.deamon.download.a.a(r0, r1, r6)
            java.lang.String r6 = "DownloadService"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "task download canceled:"
        L_0x00bd:
            r0.append(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(r6, r0)
            goto L_0x00d0
        L_0x00cb:
            int r6 = r6.what
            com.huawei.updatesdk.service.deamon.download.a.a(r0, r1, r6)
        L_0x00d0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.service.deamon.download.c.handleMessage(android.os.Message):void");
    }
}
