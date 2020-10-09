package com.taobao.downloader.request.task;

import android.text.TextUtils;
import com.taobao.downloader.request.Item;
import com.taobao.downloader.request.Param;
import com.taobao.downloader.util.MonitorUtil;
import java.io.File;
import java.net.URL;

public class SingleTask {
    public MonitorUtil.DownloadStat downloadStat;
    public int errorCode;
    public String errorMsg;
    public boolean foreground;
    public Item item;
    public Param param;
    public RetryStrategy retryStrategy = new RetryStrategy();
    public String storeDir;
    public String storeFilePath;
    public boolean success;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SingleTask)) {
            return false;
        }
        SingleTask singleTask = (SingleTask) obj;
        if (this.item == null ? singleTask.item != null : !this.item.equals(singleTask.item)) {
            return false;
        }
        if (this.storeDir != null) {
            if (!this.storeDir.equals(singleTask.storeDir)) {
                return false;
            }
            return true;
        } else if (singleTask.storeDir == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (this.item != null ? this.item.hashCode() : 0) * 31;
        if (this.storeDir != null) {
            i = this.storeDir.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return super.toString() + "@Task{" + "success=" + this.success + ", errorCode=" + this.errorCode + ", errorMsg='" + this.errorMsg + '\'' + ", item=" + this.item + ", storeDir='" + this.storeDir + '\'' + '}';
    }

    public String getFileName() {
        if (!TextUtils.isEmpty(this.item.name)) {
            return this.item.name;
        }
        try {
            return new File(new URL(this.item.url).getFile()).getName();
        } catch (Throwable unused) {
            return this.item.url;
        }
    }

    public void reset(boolean z) {
        this.errorCode = 0;
        this.errorMsg = "";
        this.success = false;
        if (z) {
            this.retryStrategy = new RetryStrategy();
        }
    }

    public SingleTask copyNewTask() {
        SingleTask singleTask = new SingleTask();
        singleTask.item = this.item;
        singleTask.param = this.param;
        singleTask.storeDir = this.storeDir;
        singleTask.foreground = this.foreground;
        return singleTask;
    }

    public class RetryStrategy {
        private int connectError;
        private int readStreamError;

        public RetryStrategy() {
        }

        public void increaseError(boolean z) {
            if (z) {
                this.connectError++;
            } else {
                this.readStreamError++;
            }
        }

        public boolean canRetry() {
            return SingleTask.this.param.retryTimes > this.connectError + this.readStreamError;
        }
    }
}
