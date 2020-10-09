package com.taobao.onlinemonitor.evaluate;

import com.taobao.onlinemonitor.HardWareInfo;

public class HardwareJavaMemory implements CalScore {
    public int mJavaHeapLimitLargeMemory;
    public int mJavaHeapLimitMemory = 0;

    public int getScore(HardWareInfo hardWareInfo) {
        int i;
        int i2 = 8;
        if (this.mJavaHeapLimitMemory > 256) {
            i = 10;
        } else if (this.mJavaHeapLimitMemory >= 256) {
            i = 8;
        } else if (this.mJavaHeapLimitMemory >= 192) {
            i = 7;
        } else if (this.mJavaHeapLimitMemory >= 128) {
            i = 5;
        } else {
            i = this.mJavaHeapLimitMemory >= 96 ? 3 : 4;
        }
        if (this.mJavaHeapLimitLargeMemory >= 512) {
            i2 = 10;
        } else if (this.mJavaHeapLimitLargeMemory < 256) {
            i2 = this.mJavaHeapLimitLargeMemory >= 128 ? 6 : 1;
        }
        return (i2 + i) / 2;
    }
}
