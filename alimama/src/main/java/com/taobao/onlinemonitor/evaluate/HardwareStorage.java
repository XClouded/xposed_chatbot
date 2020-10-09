package com.taobao.onlinemonitor.evaluate;

import com.taobao.onlinemonitor.HardWareInfo;

public class HardwareStorage implements CalScore {
    public int mInnerFree = 48;
    public int mInnerSize = 48;

    public int getScore(HardWareInfo hardWareInfo) {
        int i;
        if (this.mInnerSize <= 0) {
            this.mInnerSize = 48;
        }
        if (this.mInnerFree <= 0) {
            this.mInnerFree = 24;
        }
        int i2 = 6;
        if (this.mInnerSize >= 220) {
            i = 10;
        } else if (this.mInnerSize >= 100) {
            i = 9;
        } else {
            if (this.mInnerSize < 80) {
                if (this.mInnerSize >= 48) {
                    i = 6;
                } else if (this.mInnerSize >= 24) {
                    i = 5;
                } else if (this.mInnerSize >= 10) {
                    i = 2;
                } else if (this.mInnerSize >= 5) {
                    i = 1;
                }
            }
            i = 8;
        }
        int i3 = (this.mInnerFree * 100) / this.mInnerSize;
        if (i3 >= 80) {
            i2 = 10;
        } else if (i3 >= 70) {
            i2 = 9;
        } else if (i3 >= 60) {
            i2 = 8;
        } else if (i3 >= 50) {
            i2 = 7;
        } else if (i3 < 40) {
            i2 = i3 >= 30 ? 5 : i3 >= 20 ? 4 : i3 >= 10 ? 3 : i3 >= 5 ? 2 : i3 >= 1 ? 1 : 0;
        }
        return (i + i2) / 2;
    }
}
