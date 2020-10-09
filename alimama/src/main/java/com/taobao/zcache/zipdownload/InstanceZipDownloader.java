package com.taobao.zcache.zipdownload;

import java.lang.Thread;
import java.util.Map;

public class InstanceZipDownloader extends Thread implements IDownLoader {
    private static final String TAG = "InstanceZipDownloader";
    private String destFile = null;
    private DownLoadManager downLoadManager = null;
    private Map<String, String> header = null;
    private int timeout = 5000;

    public void cancelTask(boolean z) {
    }

    public InstanceZipDownloader(String str, DownLoadListener downLoadListener) {
        this.downLoadManager = new DownLoadManager(str, downLoadListener);
        this.downLoadManager.setTimeout(this.timeout);
        this.downLoadManager.setHeaders(this.header);
        this.downLoadManager.isTBDownloaderEnabled = false;
    }

    public void setTimeout(int i) {
        if (this.downLoadManager != null) {
            this.downLoadManager.setTimeout(i);
        }
    }

    public void setHeader(Map<String, String> map) {
        if (this.downLoadManager != null) {
            this.downLoadManager.setHeaders(map);
        }
    }

    public void setUrl(String str) {
        if (this.downLoadManager != null) {
            this.downLoadManager.setZipUrl(str);
        }
    }

    public void setDestFile(String str) {
        if (this.downLoadManager != null) {
            this.downLoadManager.setDestFile(str);
        }
    }

    public void run() {
        if (this.downLoadManager != null) {
            this.downLoadManager.doTask();
        }
    }

    public Thread.State getDownLoaderStatus() {
        return Thread.currentThread().getState();
    }
}
