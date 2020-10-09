package com.huawei.updatesdk.service.deamon.download;

import android.taobao.windvane.jsbridge.api.WVFile;
import android.text.TextUtils;
import com.huawei.updatesdk.sdk.service.download.b;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.support.a.a;

public class b extends com.huawei.updatesdk.sdk.service.download.b {
    public static final String a = (a.a() + ".DownloadDiskSpacePolicy");

    public b.a a(DownloadTask downloadTask) {
        b.a aVar = new b.a();
        aVar.a(false);
        com.huawei.updatesdk.support.b.a a2 = com.huawei.updatesdk.support.b.b.a();
        if (a2 != null && !TextUtils.isEmpty(a2.a())) {
            aVar.a(a2.a());
            aVar.a(a2.b());
            if (a(downloadTask, aVar, 0, false)) {
                aVar.a(true);
            }
        }
        if (!aVar.a()) {
            a(downloadTask, aVar);
        }
        return aVar;
    }

    public void a(DownloadTask downloadTask, b.a aVar) {
        com.huawei.updatesdk.sdk.a.c.a.a.a.d("DownloadDiskSpacePolicy", "onSpaceNotEnough");
    }

    public void a(DownloadTask downloadTask, String str) {
        com.huawei.updatesdk.sdk.a.c.a.a.a.d("DownloadDiskSpacePolicy", "onWriteEnd");
    }

    /* access modifiers changed from: protected */
    public boolean a(long j, long j2) {
        return j + WVFile.FILE_MAX_SIZE <= j2;
    }

    public boolean a(DownloadTask downloadTask, b.a aVar, long j, boolean z) {
        return a(downloadTask.r() - downloadTask.s(), aVar.b() + j);
    }
}
