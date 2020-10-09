package com.huawei.updatesdk.service.deamon.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.huawei.updatesdk.sdk.service.download.b;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.sdk.service.download.d;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadService extends Service {
    private static boolean b = false;
    protected d a;
    private final AtomicInteger c = new AtomicInteger();
    private final IBinder d = new a();

    public class a extends Binder {
        public a() {
        }

        public DownloadService a() {
            return DownloadService.this;
        }
    }

    private static void a(boolean z) {
        b = z;
    }

    public static boolean a() {
        return b;
    }

    private boolean b() {
        return com.huawei.updatesdk.sdk.a.d.b.a.e();
    }

    public void a(String str) {
        this.a.b(str);
    }

    public boolean a(DownloadTask downloadTask) {
        if (!b() || downloadTask == null) {
            return false;
        }
        this.a.a(downloadTask);
        return true;
    }

    public DownloadTask b(String str) {
        return this.a.a(str);
    }

    public boolean b(DownloadTask downloadTask) {
        if (!b()) {
            return false;
        }
        this.a.d(downloadTask);
        return true;
    }

    public IBinder onBind(Intent intent) {
        this.c.incrementAndGet();
        return this.d;
    }

    public void onCreate() {
        super.onCreate();
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("DownloadService", "DownloadService onCreate");
        a(true);
        this.a = d.a();
        this.a.a((b) new b());
        this.a.a((Handler) new c(this));
    }

    public void onDestroy() {
        super.onDestroy();
        a(false);
        try {
            this.a.d();
            stopForeground(true);
        } catch (Exception e) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("DownloadService", "unRegister NetworkConnectivityListener:", e);
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("DownloadService", "DownloadService onDestroy");
    }

    public void onRebind(Intent intent) {
        this.c.incrementAndGet();
        super.onRebind(intent);
    }

    public boolean onUnbind(Intent intent) {
        this.c.decrementAndGet();
        if (this.c.intValue() <= 0 && !this.a.e()) {
            new Handler(new Handler.Callback() {
                public boolean handleMessage(Message message) {
                    if (message.what == 1) {
                        DownloadService.this.stopSelf();
                    }
                    return true;
                }
            }).sendEmptyMessage(1);
        }
        return true;
    }
}
