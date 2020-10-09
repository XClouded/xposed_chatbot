package com.huawei.updatesdk.sdk.service.download;

import android.os.Handler;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.huawei.updatesdk.sdk.a.d.e;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.taobao.weex.BuildConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class d {
    private static volatile d e;
    private static final Object f = new Object();
    protected List<DownloadTask> a = new ArrayList();
    protected b b;
    private ExecutorService c;
    private ExecutorService d;
    private Handler g;
    private boolean h = false;

    protected d() {
        a.a("HiAppDownload", "create executor with thread pool number:1");
        this.c = Executors.newFixedThreadPool(1);
        this.d = Executors.newFixedThreadPool(2);
        this.h = false;
    }

    public static d a() {
        d dVar;
        synchronized (f) {
            if (e == null) {
                e = new d();
            }
            dVar = e;
        }
        return dVar;
    }

    private void e(DownloadTask downloadTask) {
        downloadTask.d(0);
        this.g.sendMessage(this.g.obtainMessage(downloadTask.n(), downloadTask));
        e eVar = new e(downloadTask, this.g);
        if (this.b != null) {
            eVar.a(this.b);
        }
        downloadTask.a(this.c.submit(eVar));
        a.c("HiAppDownload", "DownloadManager submit new task:" + downloadTask.w());
        downloadTask.c(System.currentTimeMillis());
    }

    public int a(int i) {
        a.a("HiAppDownload", "pauseAll all download task, reason:" + i + ",tasklist size:" + f());
        int i2 = 0;
        for (DownloadTask next : g()) {
            if (next.n() != 6) {
                a(next, i);
                i2++;
            }
        }
        return i2;
    }

    public DownloadTask a(String str) {
        synchronized (this.a) {
            for (DownloadTask next : this.a) {
                if (next.w().equalsIgnoreCase(str)) {
                    return next;
                }
            }
            return null;
        }
    }

    public void a(Handler handler) {
        this.g = handler;
    }

    public void a(b bVar) {
        this.b = bVar;
    }

    public void a(DownloadTask downloadTask) {
        if (downloadTask != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("downloadManager addTask : ");
            sb.append(e.a(downloadTask.w()) ? BuildConfig.buildJavascriptFrameworkVersion : downloadTask.w());
            a.c("HiAppDownload", sb.toString());
            downloadTask.a(true);
            if (!b(downloadTask)) {
                if (downloadTask.o() == -1) {
                    downloadTask.e(DownloadTask.a());
                }
                synchronized (this.a) {
                    this.a.add(downloadTask);
                }
            }
            downloadTask.a(false, 0);
            downloadTask.b(false);
            e(downloadTask);
        }
    }

    public void a(DownloadTask downloadTask, int i) {
        if (downloadTask != null) {
            a.c("HiAppDownload", "pauseTask, package:" + downloadTask.w() + ",status:" + downloadTask.n() + ", reason:" + i);
            if (downloadTask.n() != 6 || this.g == null) {
                if (downloadTask.n() == 0) {
                    if (downloadTask.v() != null) {
                        downloadTask.v().cancel(true);
                    }
                    downloadTask.d(6);
                    if (this.g != null) {
                        this.g.sendMessage(this.g.obtainMessage(downloadTask.n(), downloadTask));
                    }
                    a.c("HiAppDownload", "task interrupted by pause, package:" + downloadTask.w());
                }
                synchronized (downloadTask) {
                    downloadTask.a(true, i);
                    if (downloadTask.v() != null) {
                        downloadTask.v().cancel(true);
                    }
                    downloadTask.notifyAll();
                    a.c("HiAppDownload", "task interrupted by pause, package:" + downloadTask.w());
                }
                return;
            }
            this.g.sendMessage(this.g.obtainMessage(downloadTask.n(), downloadTask));
        }
    }

    public void a(String str, boolean z) {
        DownloadTask a2 = a(str);
        if (a2 != null) {
            a.c("HiAppDownload", "cancel task, package:" + a2.w());
            a2.a(z);
            if (a2.n() == 0 || a2.n() == 6) {
                if (a2.v() != null) {
                    a2.v().cancel(true);
                }
                a2.d(3);
                a2.x();
                this.g.sendMessage(this.g.obtainMessage(a2.n(), a2));
                a.c("HiAppDownload", "task interrupted by cancel, package:" + a2.w());
            }
            synchronized (a2) {
                a2.a(true, 3);
                if (a2.v() != null) {
                    a2.v().cancel(true);
                }
                a2.notifyAll();
                a.c("HiAppDownload", "task interrupted by cancel, package:" + a2.w());
            }
        }
    }

    public void a(boolean z) {
        this.h = z;
    }

    public void b(String str) {
        a(str, true);
    }

    public boolean b() {
        return this.h;
    }

    public boolean b(DownloadTask downloadTask) {
        synchronized (this.a) {
            for (DownloadTask o : this.a) {
                if (o.o() == downloadTask.o()) {
                    return true;
                }
            }
            return false;
        }
    }

    public ExecutorService c() {
        return this.d;
    }

    public void c(DownloadTask downloadTask) {
        synchronized (this.a) {
            this.a.remove(downloadTask);
        }
    }

    public void d() {
        a(0);
        synchronized (this.a) {
            this.a.clear();
        }
    }

    public void d(DownloadTask downloadTask) {
        if (downloadTask != null) {
            if (downloadTask.n() != 6) {
                a.d("HiAppDownload", "task status isn't DOWNLOAD_PAUSED(6), ignore task:" + downloadTask.w());
                return;
            }
            a.c("HiAppDownload", "resumeTask, package:" + downloadTask.w());
            downloadTask.a(false, 0);
            a(downloadTask);
        }
    }

    public boolean e() {
        boolean z;
        synchronized (this.a) {
            Iterator<DownloadTask> it = this.a.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().n() != 6) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
        }
        return z;
    }

    public int f() {
        int size;
        synchronized (this.a) {
            size = this.a.size();
        }
        return size;
    }

    public List<DownloadTask> g() {
        ArrayList arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList(this.a);
        }
        return arrayList;
    }
}
