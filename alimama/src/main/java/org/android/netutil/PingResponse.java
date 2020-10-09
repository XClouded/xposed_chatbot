package org.android.netutil;

public class PingResponse {
    private int errcode;
    private String lastHopIPStr = null;
    private String localIPStr = null;
    private PingEntry[] results;
    private int successCnt;
    private PingTaskWatcher watcher;

    PingResponse(int i) {
        this.errcode = 0;
        this.successCnt = 0;
        this.results = null;
        this.watcher = null;
        this.results = new PingEntry[i];
        for (int i2 = 0; i2 < i; i2++) {
            this.results[i2] = new PingEntry();
        }
    }

    /* access modifiers changed from: package-private */
    public void registerWatcher(PingTaskWatcher pingTaskWatcher) {
        this.watcher = pingTaskWatcher;
    }

    /* access modifiers changed from: package-private */
    public void appendEntry(int i, int i2, double d) {
        this.results[i].initWith(i, i2, d);
        if (d >= 0.0d) {
            this.successCnt++;
        }
        if (this.watcher != null) {
            this.watcher.OnEntry(i, i2, d);
        }
    }

    /* access modifiers changed from: package-private */
    public void setLocalIPStr(String str) {
        this.localIPStr = str;
    }

    /* access modifiers changed from: package-private */
    public void setLastHopIPStr(String str) {
        this.lastHopIPStr = str;
    }

    /* access modifiers changed from: package-private */
    public void setEndWithErrcode(int i) {
        this.errcode = i;
        if (this.watcher == null) {
            return;
        }
        if (i == 0) {
            this.watcher.OnFinished();
        } else {
            this.watcher.OnFailed(i);
        }
    }

    public String getLocalIPStr() {
        return this.localIPStr;
    }

    public String getLastHopIPStr() {
        return this.lastHopIPStr;
    }

    public int getErrcode() {
        return this.errcode;
    }

    public PingEntry[] getResults() {
        return this.results;
    }

    public int getSuccessCnt() {
        return this.successCnt;
    }
}
