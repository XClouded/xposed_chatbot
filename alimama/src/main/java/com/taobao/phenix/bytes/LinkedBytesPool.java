package com.taobao.phenix.bytes;

import com.taobao.phenix.common.UnitedLog;
import com.taobao.tcommon.core.BytesPool;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class LinkedBytesPool implements BytesPool {
    protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator<byte[]>() {
        public int compare(byte[] bArr, byte[] bArr2) {
            return bArr.length - bArr2.length;
        }
    };
    private List<byte[]> mBuffersByLastUse = new LinkedList();
    private List<byte[]> mBuffersBySize = new ArrayList(64);
    private int mCurrentSize = 0;
    private int mEvicts;
    private int mHits;
    private int mMisses;
    private int mPuts;
    private int mSizeLimit;

    public LinkedBytesPool(int i) {
        this.mSizeLimit = i;
    }

    public synchronized byte[] offer(int i) {
        for (int i2 = 0; i2 < this.mBuffersBySize.size(); i2++) {
            byte[] bArr = this.mBuffersBySize.get(i2);
            if (bArr.length >= i) {
                this.mCurrentSize -= bArr.length;
                this.mBuffersBySize.remove(i2);
                this.mBuffersByLastUse.remove(bArr);
                this.mHits++;
                UnitedLog.d("BytesPool", "success get buffer from pool, request=%d, result=%d", Integer.valueOf(i), Integer.valueOf(bArr.length));
                report();
                return bArr;
            }
        }
        this.mMisses++;
        UnitedLog.d("BytesPool", "failed get buffer from pool, request=%d", Integer.valueOf(i));
        report();
        return new byte[i];
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void release(byte[] r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            if (r5 == 0) goto L_0x004e
            int r0 = r5.length     // Catch:{ all -> 0x004b }
            int r1 = r4.mSizeLimit     // Catch:{ all -> 0x004b }
            if (r0 > r1) goto L_0x004e
            java.util.List<byte[]> r0 = r4.mBuffersByLastUse     // Catch:{ all -> 0x004b }
            boolean r0 = r0.contains(r5)     // Catch:{ all -> 0x004b }
            if (r0 == 0) goto L_0x0011
            goto L_0x004e
        L_0x0011:
            int r0 = r4.mPuts     // Catch:{ all -> 0x004b }
            r1 = 1
            int r0 = r0 + r1
            r4.mPuts = r0     // Catch:{ all -> 0x004b }
            java.util.List<byte[]> r0 = r4.mBuffersByLastUse     // Catch:{ all -> 0x004b }
            r0.add(r5)     // Catch:{ all -> 0x004b }
            java.util.List<byte[]> r0 = r4.mBuffersBySize     // Catch:{ all -> 0x004b }
            java.util.Comparator<byte[]> r2 = BUF_COMPARATOR     // Catch:{ all -> 0x004b }
            int r0 = java.util.Collections.binarySearch(r0, r5, r2)     // Catch:{ all -> 0x004b }
            if (r0 >= 0) goto L_0x0028
            int r0 = -r0
            int r0 = r0 - r1
        L_0x0028:
            java.util.List<byte[]> r2 = r4.mBuffersBySize     // Catch:{ all -> 0x004b }
            r2.add(r0, r5)     // Catch:{ all -> 0x004b }
            int r0 = r4.mCurrentSize     // Catch:{ all -> 0x004b }
            int r2 = r5.length     // Catch:{ all -> 0x004b }
            int r0 = r0 + r2
            r4.mCurrentSize = r0     // Catch:{ all -> 0x004b }
            int r0 = r4.mSizeLimit     // Catch:{ all -> 0x004b }
            r4.trim(r0)     // Catch:{ all -> 0x004b }
            java.lang.String r0 = "BytesPool"
            java.lang.String r2 = "release a buffer into pool, length=%d"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x004b }
            r3 = 0
            int r5 = r5.length     // Catch:{ all -> 0x004b }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x004b }
            r1[r3] = r5     // Catch:{ all -> 0x004b }
            com.taobao.phenix.common.UnitedLog.d(r0, r2, r1)     // Catch:{ all -> 0x004b }
            monitor-exit(r4)
            return
        L_0x004b:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        L_0x004e:
            monitor-exit(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.bytes.LinkedBytesPool.release(byte[]):void");
    }

    public synchronized byte[] offerMaxAvailable() {
        byte[] bArr;
        if (this.mBuffersBySize.size() > 0) {
            bArr = this.mBuffersBySize.remove(this.mBuffersBySize.size() - 1);
            this.mCurrentSize -= bArr.length;
            this.mBuffersByLastUse.remove(bArr);
            this.mHits++;
            UnitedLog.d("BytesPool", "offer available max successfully from pool, result=%d", Integer.valueOf(bArr.length));
            report();
        } else {
            this.mMisses++;
            UnitedLog.d("BytesPool", "offer available max failed from pool, the linked list is empty", new Object[0]);
            report();
            bArr = new byte[32768];
        }
        return bArr;
    }

    public synchronized void resize(int i) {
        this.mSizeLimit = i;
    }

    public void clear() {
        trim(0);
    }

    private synchronized void trim(int i) {
        while (this.mCurrentSize > i) {
            byte[] remove = this.mBuffersByLastUse.remove(0);
            this.mBuffersBySize.remove(remove);
            this.mCurrentSize -= remove.length;
            this.mEvicts++;
        }
    }

    private void report() {
        if (UnitedLog.isLoggable(3)) {
            UnitedLog.d("BytesPool", "%d/%d , puts:%d, misses:%d, hits:%d, evicts:%d", Integer.valueOf(this.mCurrentSize), Integer.valueOf(this.mSizeLimit), Integer.valueOf(this.mPuts), Integer.valueOf(this.mMisses), Integer.valueOf(this.mHits), Integer.valueOf(this.mEvicts));
        }
    }
}
