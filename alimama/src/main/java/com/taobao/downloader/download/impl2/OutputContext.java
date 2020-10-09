package com.taobao.downloader.download.impl2;

import com.taobao.downloader.download.IListener;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.util.MonitorUtil;

public class OutputContext {
    public MonitorUtil.DownloadStat downloadStat = new MonitorUtil.DownloadStat();
    public ErrorInfo errorInfo = new ErrorInfo();
    public boolean hasReadData = false;
    public long mDownloadSize = 0;
    public IListener mListener;
    public int successCode = 10;

    public OutputContext(IListener iListener) {
        this.mListener = iListener;
    }

    public void callback(InputContext inputContext) {
        if (this.mListener != null) {
            SingleTask singleTask = inputContext.mTask;
            if (!this.errorInfo.success) {
                singleTask.success = false;
                singleTask.errorCode = this.errorInfo.errorCode;
                singleTask.retryStrategy.increaseError(this.errorInfo.connectError);
                int i = singleTask.errorCode;
                if (i != -18 && i != -15) {
                    switch (i) {
                        case -12:
                            singleTask.errorMsg = "网络错误";
                            break;
                        case -11:
                            singleTask.errorMsg = "文件读写错误";
                            break;
                        case -10:
                            singleTask.errorMsg = "url错误";
                            break;
                        default:
                            singleTask.errorMsg = "下载失败";
                            break;
                    }
                } else {
                    singleTask.errorMsg = "文件校验失败";
                }
            } else {
                singleTask.success = true;
                singleTask.storeFilePath = inputContext.downloadFile.getAbsolutePath();
                singleTask.errorCode = this.successCode;
                singleTask.errorMsg = "下载成功";
            }
            this.downloadStat.url = inputContext.url;
            this.downloadStat.size = singleTask.item.size;
            if (0 != this.downloadStat.downloadTime) {
                MonitorUtil.DownloadStat downloadStat2 = this.downloadStat;
                double d = (double) this.downloadStat.traffic;
                Double.isNaN(d);
                double d2 = (double) this.downloadStat.downloadTime;
                Double.isNaN(d2);
                downloadStat2.downloadSpeed = (d / 1024.0d) / (d2 / 1000.0d);
            }
            this.downloadStat.success = singleTask.success;
            if (this.downloadStat.success) {
                this.downloadStat.error_code = String.valueOf(this.successCode);
            } else {
                this.downloadStat.error_code = String.valueOf((this.errorInfo.errorCode * 1000) - this.errorInfo.originalErrorCode);
            }
            this.downloadStat.error_msg = this.errorInfo.errorMsg;
            this.downloadStat.biz = singleTask.param.bizId;
            singleTask.downloadStat = this.downloadStat;
            this.mListener.onResult(singleTask);
        }
    }

    public void updateProgress() {
        if (this.mListener != null) {
            this.mListener.onProgress(this.mDownloadSize);
        }
    }

    public static class ErrorInfo {
        public boolean connectError;
        public int errorCode;
        public String errorMsg;
        public boolean ioError;
        public int originalErrorCode;
        public boolean readStreamError;
        public boolean success = true;
        public boolean urlError;

        public ErrorInfo addErrorInfo(int i, int i2, String str) {
            this.success = false;
            this.errorCode = i;
            this.originalErrorCode = i2;
            this.errorMsg = str;
            return this;
        }
    }
}
