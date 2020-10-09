package com.taobao.zcache.zipdownload;

import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.log.ZLog;
import java.lang.Thread;

public class WVZipBPDownloader extends Thread implements IDownLoader {
    private static final String TAG = "WVZipBPDownloader";
    private DownLoadManager downLoadManager = null;

    public WVZipBPDownloader(String str, DownLoadListener downLoadListener) {
        this.downLoadManager = new DownLoadManager(str, downLoadListener);
        this.downLoadManager.isTBDownloaderEnabled = true;
    }

    public void run() {
        ZLog.i("WVThread", "current thread = [" + Thread.currentThread().getName() + Operators.ARRAY_END_STR);
        if (this.downLoadManager != null) {
            this.downLoadManager.doTask();
        }
    }

    public Thread.State getDownLoaderStatus() {
        return Thread.currentThread().getState();
    }

    public void cancelTask(boolean z) {
        if (!isInterrupted()) {
            Thread.currentThread().interrupt();
        }
    }
}
