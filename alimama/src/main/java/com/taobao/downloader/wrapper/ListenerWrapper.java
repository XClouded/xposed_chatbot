package com.taobao.downloader.wrapper;

import com.taobao.downloader.request.DownloadListener;
import com.taobao.downloader.request.DownloadRequest;
import com.taobao.downloader.request.Item;
import com.taobao.downloader.request.Param;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.request.task.TaskListener;
import com.taobao.downloader.util.ThreadUtil;

public class ListenerWrapper implements TaskListener {
    private String bizId;
    private CallbackWrapper callbackWrapper;
    private DownloadListener downloadListener;
    private long mFinishSize;
    private long mTotalSize;
    private DownloadRequest request;

    public ListenerWrapper(DownloadRequest downloadRequest, DownloadListener downloadListener2) {
        this.request = downloadRequest;
        this.downloadListener = downloadListener2;
        this.bizId = downloadRequest.downloadParam.bizId;
        this.callbackWrapper = new CallbackWrapper(this.bizId, downloadRequest, this.downloadListener);
    }

    public void onProgress(long j) {
        geDLTotalSize();
        if (0 != this.mTotalSize && this.downloadListener != null) {
            int i = (int) (((this.mFinishSize + j) * 100) / this.mTotalSize);
            if (i > 100) {
                i = 100;
            }
            this.downloadListener.onDownloadProgress(i);
        }
    }

    private long geDLTotalSize() {
        if (0 != this.mTotalSize) {
            return this.mTotalSize;
        }
        long j = 0;
        for (Item next : this.request.downloadList) {
            if (next.size <= 0) {
                return 0;
            }
            j += next.size;
        }
        this.mTotalSize = j;
        return this.mTotalSize;
    }

    public void onResult(SingleTask singleTask) {
        this.mFinishSize += singleTask.item.size;
        if (this.downloadListener != null) {
            this.callbackWrapper.setTask(singleTask);
            ThreadUtil.execute(this.callbackWrapper, true);
        }
    }

    public void onNetworkLimit(int i, Param param, DownloadListener.NetworkLimitCallback networkLimitCallback) {
        this.downloadListener.onNetworkLimit(i, param, networkLimitCallback);
    }

    public void onDownloadStateChange(String str, boolean z) {
        this.downloadListener.onDownloadStateChange(str, z);
    }
}
