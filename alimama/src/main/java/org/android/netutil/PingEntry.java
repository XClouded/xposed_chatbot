package org.android.netutil;

public class PingEntry {
    public int hop;
    public double rtt;
    public int seq;

    PingEntry() {
        this.rtt = 0.0d;
        this.seq = 0;
        this.hop = 0;
        this.rtt = -2.0d;
    }

    /* access modifiers changed from: package-private */
    public void initWith(int i, int i2, double d) {
        this.seq = i;
        this.hop = i2;
        this.rtt = d;
    }
}
