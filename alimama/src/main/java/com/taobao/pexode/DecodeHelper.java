package com.taobao.pexode;

import android.graphics.BitmapFactory;
import com.taobao.accs.data.Message;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.entity.IncrementalStaging;
import com.taobao.tcommon.core.BytesPool;
import com.taobao.tcommon.log.FLog;

public class DecodeHelper implements DegradeEventListener {
    private static final int THRESHOLD_OF_NO_ASHMEM_FOREVER = 8;
    private static final int THRESHOLD_OF_NO_IN_BITMAP_FOREVER = 8;
    private static final int THRESHOLD_OF_SYSTEM_FOREVER = 8;
    private BytesPool bytesPool;
    public boolean forcedDegrade2NoAshmem;
    public boolean forcedDegrade2NoInBitmap;
    int historyOfDegrade2NoAshmem;
    int historyOfDegrade2NoInBitmap;
    int historyOfDegrade2System;

    private int calculateNewBinary(int i, boolean z) {
        return ((i << 1) + (z ? 1 : 0)) & Message.EXT_HEADER_VALUE_MAX_LEN;
    }

    private static class Singleton {
        /* access modifiers changed from: private */
        public static final DecodeHelper INSTANCE = new DecodeHelper();

        private Singleton() {
        }
    }

    public static DecodeHelper instance() {
        return Singleton.INSTANCE;
    }

    public synchronized void onDegraded2System(boolean z) {
        if (!Pexode.isForcedDegrade2System()) {
            this.historyOfDegrade2System = calculateNewBinary(this.historyOfDegrade2System, z);
            if (countBinaryBit(this.historyOfDegrade2System) >= 8) {
                Pexode.forceDegrade2System(true);
                Pexode.ForcedDegradationListener forcedDegradationListener = Pexode.getForcedDegradationListener();
                if (forcedDegradationListener != null) {
                    forcedDegradationListener.onForcedDegrade2System();
                }
            }
        }
    }

    public synchronized void onDegraded2NoAshmem(boolean z) {
        if (!this.forcedDegrade2NoAshmem) {
            this.historyOfDegrade2NoAshmem = calculateNewBinary(this.historyOfDegrade2NoAshmem, z);
            if (countBinaryBit(this.historyOfDegrade2NoAshmem) >= 8) {
                this.forcedDegrade2NoAshmem = true;
                FLog.w(Pexode.TAG, "auto degrading to no ashmem, history=%d", Integer.valueOf(this.historyOfDegrade2NoAshmem));
                Pexode.ForcedDegradationListener forcedDegradationListener = Pexode.getForcedDegradationListener();
                if (forcedDegradationListener != null) {
                    forcedDegradationListener.onForcedDegrade2NoAshmem();
                }
            }
        }
    }

    public synchronized void onDegraded2NoInBitmap(boolean z) {
        if (!this.forcedDegrade2NoInBitmap) {
            this.historyOfDegrade2NoInBitmap = calculateNewBinary(this.historyOfDegrade2NoInBitmap, z);
            if (countBinaryBit(this.historyOfDegrade2NoInBitmap) >= 8) {
                this.forcedDegrade2NoInBitmap = true;
                FLog.w(Pexode.TAG, "auto degrading to no inBitmap, history=%d", Integer.valueOf(this.historyOfDegrade2NoInBitmap));
                Pexode.ForcedDegradationListener forcedDegradationListener = Pexode.getForcedDegradationListener();
                if (forcedDegradationListener != null) {
                    forcedDegradationListener.onForcedDegrade2NoInBitmap();
                }
            }
        }
    }

    private int countBinaryBit(int i) {
        int i2 = (i - ((i >> 1) & -613566757)) - ((i >> 2) & 1227133513);
        return (-954437177 & (i2 + (i2 >> 3))) % 63;
    }

    /* access modifiers changed from: package-private */
    public void setBytesPool(BytesPool bytesPool2) {
        this.bytesPool = bytesPool2;
    }

    public byte[] offerBytes(int i) {
        byte[] offer = this.bytesPool != null ? this.bytesPool.offer(i) : null;
        return offer == null ? new byte[i] : offer;
    }

    public void releaseBytes(byte[] bArr) {
        if (this.bytesPool != null) {
            this.bytesPool.release(bArr);
        }
    }

    public static boolean resultOK(PexodeResult pexodeResult, PexodeOptions pexodeOptions) {
        return (pexodeOptions.justDecodeBounds && pexodeOptions.isSizeAvailable()) || (pexodeOptions.incrementalDecode && pexodeOptions.mIncrementalStaging != null) || !(pexodeResult == null || (pexodeResult.bitmap == null && pexodeResult.animated == null));
    }

    public static boolean resultEnd(PexodeResult pexodeResult, PexodeOptions pexodeOptions) {
        return pexodeOptions.cancelled || resultOK(pexodeResult, pexodeOptions);
    }

    public static void setUponSysOptions(PexodeOptions pexodeOptions, BitmapFactory.Options options) {
        pexodeOptions.setUponSysOptions(options);
    }

    public static boolean cancelledInOptions(PexodeOptions pexodeOptions) {
        return pexodeOptions.cancelled;
    }

    public static int getLastSampleSizeInOptions(PexodeOptions pexodeOptions) {
        return pexodeOptions.lastSampleSize;
    }

    public static void setLastSampleSizeInOptions(PexodeOptions pexodeOptions, int i) {
        pexodeOptions.lastSampleSize = i;
    }

    public static IncrementalStaging getIncrementalStaging(PexodeOptions pexodeOptions) {
        return pexodeOptions.mIncrementalStaging;
    }

    public static void setIncrementalStaging(PexodeOptions pexodeOptions, IncrementalStaging incrementalStaging) {
        pexodeOptions.mIncrementalStaging = incrementalStaging;
    }
}
