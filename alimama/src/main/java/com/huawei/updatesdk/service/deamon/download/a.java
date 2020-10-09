package com.huawei.updatesdk.service.deamon.download;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.huawei.updatesdk.sdk.service.b.b;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.support.d.c;

public final class a {
    public static final String a() {
        return com.huawei.updatesdk.support.a.a.a() + ".service.downloadservice.Receiver";
    }

    public static void a(Context context, DownloadTask downloadTask, int i) {
        if (context != null) {
            Intent intent = new Intent();
            String a = a();
            if (downloadTask != null) {
                Bundle bundle = new Bundle();
                Bundle bundle2 = new Bundle();
                downloadTask.b(bundle2);
                bundle.putBundle("downloadtask.all", bundle2);
                bundle.putInt("downloadtask.status", i);
                intent.putExtras(bundle);
                if (i == 2) {
                    a = b();
                }
            }
            b a2 = b.a(intent);
            if (a().equals(a)) {
                c.a().a(a2);
            } else {
                c.a().b(a2);
            }
        }
    }

    public static final String b() {
        return com.huawei.updatesdk.support.a.a.a() + ".service.downloadservice.progress.Receiver";
    }
}
