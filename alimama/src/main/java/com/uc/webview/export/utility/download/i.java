package com.uc.webview.export.utility.download;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;

/* compiled from: U4Source */
final class i implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ ValueCallback b;
    final /* synthetic */ UpdateTask c;

    i(UpdateTask updateTask, ValueCallback valueCallback, ValueCallback valueCallback2) {
        this.c = updateTask;
        this.a = valueCallback;
        this.b = valueCallback2;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        DownloadTask downloadTask = (DownloadTask) obj;
        Throwable exception = downloadTask.getException();
        if (exception != null) {
            int hashCode = exception.getClass().getName().hashCode();
            IWaStat.WaStat.stat(IWaStat.UCM_LAST_EXCEPTION, String.valueOf(hashCode));
            try {
                if (this.c.j != null) {
                    this.c.j.onReceiveValue(new Object[]{7, Integer.valueOf(hashCode)});
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        long[] c2 = this.c.d;
        c2[3] = c2[3] + this.c.d[4];
        if (this.c.d[3] < this.c.d[5]) {
            this.c.f[1] = downloadTask.getException();
            try {
                if (this.a != null) {
                    this.a.onReceiveValue(this.c);
                }
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).postDelayed(new j(this, downloadTask), this.c.d[4]);
            return;
        }
        IWaStat.WaStat.stat(IWaStat.UCM_EXCEPTION_DOWNLOAD);
        String message = downloadTask.getException() != null ? downloadTask.getException().getMessage() : "";
        Object[] b2 = this.c.f;
        b2[1] = new RuntimeException("Download aborted because of up to 10080 retries. Last exception is: " + message);
        try {
            if (this.b != null) {
                this.b.onReceiveValue(this.c);
            }
        } catch (Throwable th3) {
            th3.printStackTrace();
        }
        try {
            if (this.c.j != null) {
                this.c.j.onReceiveValue(new Object[]{4});
            }
        } catch (Throwable th4) {
            th4.printStackTrace();
        }
    }
}
