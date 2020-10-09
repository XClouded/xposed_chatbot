package anet.channel.strategy;

import java.io.Serializable;
import kotlin.UByte;

class ConnHistoryItem implements Serializable {
    private static final int BAN_THRESHOLD = 3;
    private static final int BAN_TIME = 300000;
    private static final long UPDATE_INTERVAL = 10000;
    private static final long VALID_PERIOD = 86400000;
    private static final long serialVersionUID = 5245740801355223771L;
    byte history = 0;
    long lastFail = 0;
    long lastSuccess = 0;

    ConnHistoryItem() {
    }

    /* access modifiers changed from: package-private */
    public void update(boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - (z ? this.lastSuccess : this.lastFail) > UPDATE_INTERVAL) {
            this.history = (this.history << 1) | (z ^ true) ? (byte) 1 : 0;
            if (z) {
                this.lastSuccess = currentTimeMillis;
            } else {
                this.lastFail = currentTimeMillis;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int countFail() {
        int i = 0;
        for (int i2 = this.history & UByte.MAX_VALUE; i2 > 0; i2 >>= 1) {
            i += i2 & 1;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public boolean latestFail() {
        return (this.history & 1) == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldBan() {
        if (countFail() >= 3 && System.currentTimeMillis() - this.lastFail <= 300000) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isExpire() {
        long j = this.lastSuccess > this.lastFail ? this.lastSuccess : this.lastFail;
        return j != 0 && System.currentTimeMillis() - j > 86400000;
    }
}
