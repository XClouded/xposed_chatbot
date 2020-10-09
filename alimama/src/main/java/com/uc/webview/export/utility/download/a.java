package com.uc.webview.export.utility.download;

/* compiled from: U4Source */
final class a implements Runnable {
    final /* synthetic */ Runnable a;
    final /* synthetic */ DownloadTask b;

    a(DownloadTask downloadTask, Runnable runnable) {
        this.b = downloadTask;
        this.a = runnable;
    }

    public final void run() {
        try {
            synchronized (this.b) {
                this.a.run();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
