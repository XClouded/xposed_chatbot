package com.taobao.onlinemonitor.evaluate;

import com.taobao.onlinemonitor.HardWareInfo;

public class HardwareScreen implements CalScore {
    public int getScore(HardWareInfo hardWareInfo) {
        float f;
        if (hardWareInfo == null) {
            return 0;
        }
        if (hardWareInfo.mWidth == 0 || hardWareInfo.mHeight == 0) {
            return 5;
        }
        float f2 = 8.0f;
        if (hardWareInfo.mDesty <= 1.4f) {
            f = 1.0f;
        } else if (hardWareInfo.mDesty <= 1.5f) {
            f = 2.0f;
        } else if (hardWareInfo.mDesty <= 2.0f) {
            f = 4.0f;
        } else if (hardWareInfo.mDesty <= 2.5f) {
            f = 6.0f;
        } else if (hardWareInfo.mDesty <= 3.0f) {
            f = 8.0f;
        } else {
            f = hardWareInfo.mDesty <= 3.5f ? 9.0f : 10.0f;
        }
        int i = hardWareInfo.mWidth * hardWareInfo.mHeight;
        if (i >= 8847360) {
            f2 = 10.0f;
        } else if (i >= 3686400) {
            f2 = 9.0f;
        } else if (i <= 2073600) {
            f2 = i == 2073600 ? 7.0f : i > 921600 ? 6.0f : i >= 921600 ? 4.0f : i >= 786432 ? 3.0f : i >= 614400 ? 2.0f : 1.0f;
        }
        return Math.round((f + f2) / 2.0f);
    }
}
