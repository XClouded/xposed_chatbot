package com.facebook.imagepipeline.memory;

import com.alibaba.android.prefetchx.PrefetchX;
import com.facebook.infer.annotation.ThreadSafe;

public class BitmapCounterProvider {
    private static final long KB = 1024;
    public static final int MAX_BITMAP_COUNT = 384;
    public static final int MAX_BITMAP_TOTAL_SIZE = getMaxSizeHardCap();
    private static final long MB = 1048576;
    private static volatile BitmapCounter sBitmapCounter;

    private static int getMaxSizeHardCap() {
        int min = (int) Math.min(Runtime.getRuntime().maxMemory(), 2147483647L);
        if (((long) min) > PrefetchX.SUPPORT_IMAGE_WEEX_MODULE) {
            return (min / 4) * 3;
        }
        return min / 2;
    }

    @ThreadSafe
    public static BitmapCounter get() {
        if (sBitmapCounter == null) {
            synchronized (BitmapCounterProvider.class) {
                if (sBitmapCounter == null) {
                    sBitmapCounter = new BitmapCounter(MAX_BITMAP_COUNT, MAX_BITMAP_TOTAL_SIZE);
                }
            }
        }
        return sBitmapCounter;
    }
}
