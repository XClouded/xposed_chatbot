package com.taobao.downloader.wrapper;

import com.taobao.downloader.adpater.Monitor;
import com.taobao.downloader.request.DownloadListener;
import com.taobao.downloader.request.DownloadRequest;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.util.LogUtil;
import com.taobao.downloader.util.MonitorUtil;

public class CallbackWrapper implements Runnable {
    private String bizId;
    private DownloadListener downloadListener;
    private String errorCode;
    private String errorMsg;
    private boolean hasError;
    private int onResultCount;
    private DownloadRequest request;
    private SingleTask task;

    public CallbackWrapper(String str, SingleTask singleTask, DownloadRequest downloadRequest, DownloadListener downloadListener2) {
        this.bizId = str;
        this.task = singleTask;
        this.request = downloadRequest;
        this.downloadListener = downloadListener2;
    }

    public CallbackWrapper(String str, DownloadRequest downloadRequest, DownloadListener downloadListener2) {
        this.bizId = str;
        this.request = downloadRequest;
        this.downloadListener = downloadListener2;
    }

    public void setTask(SingleTask singleTask) {
        this.task = singleTask;
    }

    public void run() {
        try {
            if (this.task != null) {
                if (this.task.success) {
                    LogUtil.debug("listener.onDownloadFinish", "task on result {}", this.task);
                    this.downloadListener.onDownloadFinish(this.task.item.url, this.task.storeFilePath);
                } else {
                    LogUtil.debug("listener.onDownloadError", "task on result {}", this.task);
                    this.downloadListener.onDownloadError(this.task.item.url, this.task.errorCode, this.task.errorMsg);
                    this.hasError = true;
                    this.errorCode = String.valueOf(this.task.errorCode);
                    this.errorMsg = this.task.item.url;
                }
                int i = this.onResultCount + 1;
                this.onResultCount = i;
                if (i == this.request.downloadList.size()) {
                    LogUtil.debug("listener.onFinish", "task on result {},", this.task);
                    if (this.hasError) {
                        MonitorUtil.monitorFail(Monitor.POINT_ALL_CALLBACK, this.task.param.from + this.bizId, this.errorCode, this.errorMsg);
                    } else {
                        MonitorUtil.monitorSuccess(Monitor.POINT_ALL_CALLBACK, this.task.param.from + this.bizId);
                    }
                    this.downloadListener.onFinish(!this.hasError);
                }
            }
        } catch (Throwable th) {
            LogUtil.error("callbak", "on callback ", th);
        }
    }
}
