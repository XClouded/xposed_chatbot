package org.android.netutil;

import androidx.annotation.Keep;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Keep
public class PingTask {
    private static int PING_DEFAULT_TIME = 5;
    private int interval;
    private int maxtime;
    private int payload;
    private String pingIPStr;
    private int ttl;

    /* access modifiers changed from: private */
    public static native long createPingTask(PingFuture pingFuture, String str, int i, int i2, int i3, int i4);

    /* access modifiers changed from: private */
    public static native void releasePingTask(long j);

    /* access modifiers changed from: private */
    public static native boolean waitPingTask(long j, long j2);

    @Keep
    class PingFuture extends AsyncTask implements Future<PingResponse> {
        private PingResponse _inner_response;
        private long native_ptr;

        public boolean cancel(boolean z) {
            return false;
        }

        public boolean isCancelled() {
            return false;
        }

        private PingFuture() {
            this.native_ptr = 0;
            this._inner_response = null;
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            super.finalize();
            if (this.native_ptr != 0) {
                PingTask.releasePingTask(this.native_ptr);
            }
        }

        public boolean isDone() {
            return this.done;
        }

        public PingResponse get() throws InterruptedException, ExecutionException {
            try {
                return get(0, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                e.printStackTrace();
                return null;
            }
        }

        public PingResponse get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            synchronized (this) {
                if (!this.done) {
                    if (this.native_ptr == 0) {
                        return null;
                    }
                    if (PingTask.waitPingTask(this.native_ptr, timeUnit.toSeconds(j))) {
                        PingTask.releasePingTask(this.native_ptr);
                        this.native_ptr = 0;
                    } else {
                        throw new TimeoutException();
                    }
                }
                PingResponse pingResponse = this._inner_response;
                return pingResponse;
            }
        }

        /* access modifiers changed from: private */
        public PingFuture start(String str, int i, int i2, int i3, int i4, PingTaskWatcher pingTaskWatcher) {
            this._inner_response = new PingResponse(i2);
            this._inner_response.registerWatcher(pingTaskWatcher);
            this.native_ptr = PingTask.createPingTask(this, str, i, i2, i3, i4);
            return this;
        }

        /* access modifiers changed from: protected */
        public void onTaskFinish(String str, int i) {
            this._inner_response.setLocalIPStr(str);
            this._inner_response.setEndWithErrcode(i);
        }

        /* access modifiers changed from: protected */
        public void onPingEntry(int i, int i2, double d) {
            this._inner_response.appendEntry(i, i2, d);
        }

        /* access modifiers changed from: protected */
        public void onTimxceed(String str) {
            this._inner_response.setLastHopIPStr(str);
        }
    }

    public PingTask(String str, int i, int i2, int i3, int i4) {
        this.pingIPStr = null;
        this.pingIPStr = str;
        this.interval = i;
        this.maxtime = i2 == 0 ? PING_DEFAULT_TIME : i2;
        this.payload = i3;
        this.ttl = i4;
    }

    public Future<PingResponse> launchWith(PingTaskWatcher pingTaskWatcher) {
        return new PingFuture().start(this.pingIPStr, this.interval, this.maxtime, this.payload, this.ttl, pingTaskWatcher);
    }

    public Future<PingResponse> launch() {
        return launchWith((PingTaskWatcher) null);
    }

    public PingTask(String str) {
        this(str, 0, 0, 0, 0);
    }
}
