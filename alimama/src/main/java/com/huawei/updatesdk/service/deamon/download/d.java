package com.huawei.updatesdk.service.deamon.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import com.huawei.updatesdk.service.deamon.download.DownloadService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class d {
    private static final d a = new d();
    private final AtomicInteger b = new AtomicInteger();
    private a c = null;
    /* access modifiers changed from: private */
    public DownloadService d = null;
    /* access modifiers changed from: private */
    public final List<Message> e = new ArrayList();

    private final class a implements ServiceConnection {
        private a() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                DownloadService unused = d.this.d = ((DownloadService.a) iBinder).a();
                synchronized (d.this.e) {
                    for (Message sendToTarget : d.this.e) {
                        sendToTarget.sendToTarget();
                    }
                    d.this.e.clear();
                }
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "Bind to DownloadService sucessfuly");
            } catch (ClassCastException unused2) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "onServiceConnected ClassCastException");
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            DownloadService unused = d.this.d = null;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "unBind DownloadService sucessfuly");
        }
    }

    public static void a() {
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "start DownloadService");
        Context b2 = com.huawei.updatesdk.sdk.service.a.a.a().b();
        b2.startService(new Intent(b2, DownloadService.class));
    }

    public static d b() {
        return a;
    }

    private boolean g() {
        if (this.c != null) {
            return true;
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "bind to DownloadService");
        Context b2 = com.huawei.updatesdk.sdk.service.a.a.a().b();
        Intent intent = new Intent(b2, DownloadService.class);
        this.c = new a();
        return b2.bindService(intent, this.c, 1);
    }

    /* access modifiers changed from: protected */
    public DownloadService c() {
        if (!DownloadService.a()) {
            a();
        }
        if (a.d != null && this.b.get() > 0) {
            return a.d;
        }
        a.g();
        return null;
    }

    public void d() {
        if (this.c != null) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "unBind DownloadService");
            try {
                com.huawei.updatesdk.sdk.service.a.a.a().b().unbindService(this.c);
            } catch (IllegalArgumentException unused) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("ServiceProxy", "unbindServer IllegalArgumentException");
            }
            this.c = null;
            this.b.set(0);
        }
    }

    public DownloadService e() {
        return a.d;
    }

    public DownloadService f() {
        DownloadService c2 = c();
        this.b.incrementAndGet();
        return c2;
    }
}
