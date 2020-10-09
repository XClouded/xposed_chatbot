package com.taobao.downloader.download.protocol;

import com.taobao.downloader.request.Item;
import com.taobao.downloader.request.task.SingleTask;

public class DLConfig {
    public static int DEFAULT_CONNECT_TIMEOUT = 10000;
    public static int DEFAULT_READSTREAM_TIMEOUT = 15000;
    public static int LARGE_BUFFER_SIZE = 32768;
    public static int NORMAL_BUFFER_SIZE = 4096;
    public static boolean REDIRECTABLE = false;
    public int MAX_CONNECT_FAIL_TIMES = 3;
    public int MAX_READSTREAM_FAIL_TIMES = 3;
    private int cdnHeadError;
    private int connectFailTime;
    private int errorType;
    private Item item;
    private int readFailTime;
    private int readRetryTime;

    public DLConfig(Item item2) {
        this.item = item2;
    }

    public DLConfig(SingleTask singleTask) {
        this.item = singleTask.item;
        if (singleTask.param.retryTimes > 0) {
            this.MAX_CONNECT_FAIL_TIMES = singleTask.param.retryTimes;
            this.MAX_READSTREAM_FAIL_TIMES = singleTask.param.retryTimes;
        }
    }

    public int getConnectTimeout() {
        return DEFAULT_CONNECT_TIMEOUT;
    }

    public int getReadTimeout() {
        if (0 == this.item.size) {
            return DEFAULT_READSTREAM_TIMEOUT * 10;
        }
        int i = (int) (this.item.size / 10);
        return i > DEFAULT_READSTREAM_TIMEOUT ? i : DEFAULT_READSTREAM_TIMEOUT;
    }

    public int getBufferSize() {
        if (this.readFailTime == 0) {
            return LARGE_BUFFER_SIZE;
        }
        return NORMAL_BUFFER_SIZE;
    }

    public boolean isLastConnect() {
        return this.MAX_CONNECT_FAIL_TIMES - this.connectFailTime == 1;
    }

    public boolean isLastRead() {
        return this.MAX_READSTREAM_FAIL_TIMES - this.readFailTime == 1;
    }

    public long getWaitTime() {
        if (1 == this.errorType) {
            return (long) (this.connectFailTime * 10000);
        }
        return 0;
    }

    public void increaseConnectFail() {
        this.errorType = 1;
        this.connectFailTime++;
    }

    public void increaseHeadError() {
        this.cdnHeadError++;
    }

    public void increaseReadFail(boolean z) {
        this.errorType = this.readFailTime;
        this.readRetryTime++;
        if (z) {
            this.readFailTime = 0;
        } else {
            this.readFailTime++;
        }
    }

    public boolean hasRetryChance() {
        return this.readFailTime < this.MAX_READSTREAM_FAIL_TIMES && this.connectFailTime < this.MAX_CONNECT_FAIL_TIMES && this.cdnHeadError < 3;
    }

    public int getReadFailTime() {
        return this.readFailTime;
    }

    public int getConnectFailTime() {
        return this.connectFailTime;
    }

    public int getReadRetryTime() {
        return this.readRetryTime;
    }
}
