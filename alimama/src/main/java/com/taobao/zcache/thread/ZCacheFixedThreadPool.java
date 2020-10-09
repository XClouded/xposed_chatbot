package com.taobao.zcache.thread;

import com.taobao.zcache.log.ZLog;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZCacheFixedThreadPool {
    private static final int CORE_POOL_SIZE = 1;
    private static ExecutorService SingleExecutor = null;
    public static int bufferSize = 4096;
    private static ZCacheFixedThreadPool threadManager;
    private BufferWrapper tempBuffer = null;

    public static ZCacheFixedThreadPool getInstance() {
        if (threadManager == null) {
            synchronized (ZCacheFixedThreadPool.class) {
                if (threadManager == null) {
                    threadManager = new ZCacheFixedThreadPool();
                }
            }
        }
        return threadManager;
    }

    public void execute(Runnable runnable) {
        if (SingleExecutor == null) {
            SingleExecutor = Executors.newFixedThreadPool(1);
        }
        if (runnable == null) {
            ZLog.w("ZCacheThreadPool", "executeSingle is null.");
        } else {
            SingleExecutor.execute(runnable);
        }
    }

    public BufferWrapper getTempBuffer() {
        if (this.tempBuffer == null) {
            this.tempBuffer = new BufferWrapper();
        }
        return this.tempBuffer;
    }

    public void reSetTempBuffer() {
        if (this.tempBuffer != null || this.tempBuffer.isFree) {
            this.tempBuffer.tempBuffer = null;
            boolean unused = this.tempBuffer.isFree = false;
            this.tempBuffer = null;
        }
    }

    public static class BufferWrapper {
        /* access modifiers changed from: private */
        public boolean isFree;
        public byte[] tempBuffer;

        BufferWrapper() {
            this.tempBuffer = null;
            this.isFree = false;
            this.tempBuffer = new byte[ZCacheFixedThreadPool.bufferSize];
        }

        public boolean isFree() {
            return this.isFree;
        }

        public void setIsFree(boolean z) {
            this.isFree = z;
        }
    }
}
