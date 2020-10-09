package com.uc.webview.export.utility.download;

import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.k;
import java.io.File;

/* compiled from: U4Source */
final class c implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ DownloadTask b;

    c(DownloadTask downloadTask, boolean z) {
        this.b = downloadTask;
        this.a = z;
    }

    public final void run() {
        try {
            File file = new File(this.b.d[1]);
            synchronized (this.b) {
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELETE_UPD_FILE_THREAD_PV);
                if (this.a) {
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELETE_UPD_FILE_THREAD_SH_PV);
                    try {
                        if (this.b.c[9] != null) {
                            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELETE_UPD_FILE_THREAD_CB_PV);
                            if (!k.a(this.b.getFilePath())) {
                                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELETE_UPD_FILE_THREAD_CALL_PV);
                                this.b.c[9].onReceiveValue(this.b);
                            }
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                UCCyclone.recursiveDelete(file, false, (Object) null);
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }
}
