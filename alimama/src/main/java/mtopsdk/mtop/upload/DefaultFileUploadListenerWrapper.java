package mtopsdk.mtop.upload;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.mtop.upload.domain.UploadFileInfo;

class DefaultFileUploadListenerWrapper implements FileUploadBaseListener {
    private AtomicBoolean isCancelled = new AtomicBoolean(false);
    private AtomicBoolean isFinished = new AtomicBoolean(false);
    private FileUploadBaseListener listener;
    private AtomicInteger retryTimes = new AtomicInteger(0);
    public long segmentNum;
    public long serverRT;
    private long startTime;
    private long totalTime;

    public DefaultFileUploadListenerWrapper(FileUploadBaseListener fileUploadBaseListener) {
        this.listener = fileUploadBaseListener;
    }

    public void onStart() {
        this.startTime = System.currentTimeMillis();
        if (this.listener != null && !isCancelled()) {
            this.listener.onStart();
        }
    }

    public void onProgress(int i) {
        if (this.listener != null && !isCancelled()) {
            this.listener.onProgress(i);
        }
    }

    public void onFinish(UploadFileInfo uploadFileInfo, String str) {
        this.totalTime = System.currentTimeMillis() - this.startTime;
        if (this.listener != null && !isCancelled()) {
            this.listener.onFinish(uploadFileInfo, str);
            cancel();
        }
    }

    public void onError(String str, String str2, String str3) {
        this.totalTime = System.currentTimeMillis() - this.startTime;
        if (this.listener != null && !isCancelled()) {
            this.listener.onError(str, str2, str3);
        }
    }

    @Deprecated
    public void onFinish(String str) {
        if (this.listener != null && !isCancelled()) {
            this.listener.onFinish(str);
        }
    }

    @Deprecated
    public void onError(String str, String str2) {
        if (this.listener != null && !isCancelled()) {
            this.listener.onError(str, str2);
        }
    }

    public void cancel() {
        this.isCancelled.set(true);
    }

    public boolean isCancelled() {
        return this.isCancelled.get();
    }

    public long getUploadTotalTime() {
        return this.totalTime;
    }

    public AtomicBoolean isFinished() {
        return this.isFinished;
    }

    public void countRetryTimes() {
        this.retryTimes.incrementAndGet();
    }

    public int getTotalRetryTimes() {
        return this.retryTimes.get();
    }
}
