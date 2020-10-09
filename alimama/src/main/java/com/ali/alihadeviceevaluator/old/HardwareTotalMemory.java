package com.ali.alihadeviceevaluator.old;

import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.android.prefetchx.PrefetchX;

public class HardwareTotalMemory implements CalScore {
    public long mDeviceTotalMemory = 0;

    public int getScore(HardWareInfo hardWareInfo) {
        if (this.mDeviceTotalMemory >= 6144) {
            return 10;
        }
        if (this.mDeviceTotalMemory >= PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            return 9;
        }
        if (this.mDeviceTotalMemory >= PrefetchX.SUPPORT_FILE_TAOBAO) {
            return 7;
        }
        if (this.mDeviceTotalMemory >= 2048) {
            return 5;
        }
        if (this.mDeviceTotalMemory >= 1024) {
            return 3;
        }
        return this.mDeviceTotalMemory >= 512 ? 1 : 8;
    }
}
