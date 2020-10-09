package com.taobao.phenix.entity;

import android.content.res.AssetManager;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import com.taobao.phenix.common.StreamUtil;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.loader.StreamResultHandler;
import com.taobao.tcommon.core.BytesPool;
import java.io.FileInputStream;
import java.io.InputStream;

public class EncodedData extends ResponseData {
    public final boolean completed;
    protected boolean mReleased;

    private EncodedData(int i, boolean z, byte[] bArr, int i2, InputStream inputStream, int i3, TypedValue typedValue) {
        super(i, bArr, i2, inputStream, i3, typedValue);
        boolean z2 = true;
        if (i == 1) {
            this.completed = (!z || bArr == null || bArr.length - i2 < i3) ? false : z2;
        } else {
            this.completed = z;
        }
    }

    protected EncodedData(EncodedData encodedData) {
        this(encodedData.type, encodedData.completed, encodedData.bytes, encodedData.offset, encodedData.inputStream, encodedData.length, encodedData.resourceValue);
    }

    public EncodedData(InputStream inputStream, int i, TypedValue typedValue) {
        this(3, true, (byte[]) null, 0, inputStream, i, typedValue);
    }

    public EncodedData(boolean z, byte[] bArr, int i, int i2) {
        this(1, z, bArr, i, (InputStream) null, i2, (TypedValue) null);
    }

    public EncodedData(byte[] bArr, int i, int i2) {
        this(1, true, bArr, i, (InputStream) null, i2, (TypedValue) null);
    }

    public boolean isAvailable() {
        if (this.mReleased || this.length <= 0) {
            return false;
        }
        if (this.type == 1) {
            if (this.bytes == null || this.offset < 0 || this.offset >= this.length) {
                return false;
            }
            return true;
        } else if (this.inputStream != null) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void release(boolean r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.mReleased     // Catch:{ all -> 0x0058 }
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x001c
            if (r6 == 0) goto L_0x001a
            java.lang.String r6 = "EncodedData"
            java.lang.String r0 = "has been released when trying to release it[type: %d]"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0058 }
            int r3 = r5.type     // Catch:{ all -> 0x0058 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0058 }
            r2[r1] = r3     // Catch:{ all -> 0x0058 }
            com.taobao.phenix.common.UnitedLog.w(r6, r0, r2)     // Catch:{ all -> 0x0058 }
        L_0x001a:
            monitor-exit(r5)
            return
        L_0x001c:
            if (r6 != 0) goto L_0x002f
            java.lang.String r6 = "EncodedData"
            java.lang.String r0 = "final release called from finalize[type: %d]"
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ all -> 0x0058 }
            int r4 = r5.type     // Catch:{ all -> 0x0058 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x0058 }
            r3[r1] = r4     // Catch:{ all -> 0x0058 }
            com.taobao.phenix.common.UnitedLog.w(r6, r0, r3)     // Catch:{ all -> 0x0058 }
        L_0x002f:
            int r6 = r5.type     // Catch:{ all -> 0x0058 }
            if (r6 == r2) goto L_0x0041
            r0 = 3
            if (r6 == r0) goto L_0x0037
            goto L_0x0054
        L_0x0037:
            java.io.InputStream r6 = r5.inputStream     // Catch:{ all -> 0x0058 }
            if (r6 == 0) goto L_0x0054
            java.io.InputStream r6 = r5.inputStream     // Catch:{ IOException -> 0x0054 }
            r6.close()     // Catch:{ IOException -> 0x0054 }
            goto L_0x0054
        L_0x0041:
            com.taobao.phenix.intf.Phenix r6 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ all -> 0x0058 }
            com.taobao.phenix.builder.BytesPoolBuilder r6 = r6.bytesPoolBuilder()     // Catch:{ all -> 0x0058 }
            com.taobao.tcommon.core.BytesPool r6 = r6.build()     // Catch:{ all -> 0x0058 }
            if (r6 == 0) goto L_0x0054
            byte[] r0 = r5.bytes     // Catch:{ all -> 0x0058 }
            r6.release(r0)     // Catch:{ all -> 0x0058 }
        L_0x0054:
            r5.mReleased = r2     // Catch:{ all -> 0x0058 }
            monitor-exit(r5)
            return
        L_0x0058:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.entity.EncodedData.release(boolean):void");
    }

    public synchronized void release() {
        release(true);
    }

    public static EncodedData transformFrom(@NonNull ResponseData responseData, StreamResultHandler streamResultHandler) throws Exception {
        if (responseData.type == 3) {
            InputStream inputStream = responseData.inputStream;
            if ((inputStream instanceof FileInputStream) || (inputStream instanceof AssetManager.AssetInputStream)) {
                return new EncodedData(inputStream, responseData.length, responseData.resourceValue);
            }
            BytesPool build = Phenix.instance().bytesPoolBuilder().build();
            if (streamResultHandler == null) {
                return StreamUtil.readBytes(inputStream, build, new int[]{responseData.length});
            }
            StreamUtil.readBytes(inputStream, build, streamResultHandler);
            return streamResultHandler.getEncodeData();
        } else if (responseData.type == 1) {
            return new EncodedData(responseData.bytes, responseData.offset, responseData.length);
        } else {
            throw new RuntimeException("unrecognized response type: " + responseData.type);
        }
    }
}
