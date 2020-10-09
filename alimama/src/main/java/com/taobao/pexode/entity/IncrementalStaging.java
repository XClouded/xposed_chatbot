package com.taobao.pexode.entity;

import android.graphics.Bitmap;

public class IncrementalStaging {
    private final Bitmap mInterBitmap;
    private long mNativeConfigOut;
    private final NativeDestructor mNativeDestructor;

    public interface NativeDestructor {
        void destruct(long j);
    }

    public IncrementalStaging(Bitmap bitmap, long j, NativeDestructor nativeDestructor) {
        this.mInterBitmap = bitmap;
        this.mNativeConfigOut = j;
        this.mNativeDestructor = nativeDestructor;
    }

    public Bitmap getInterBitmap() {
        return this.mInterBitmap;
    }

    public long getNativeConfigOut() {
        return this.mNativeConfigOut;
    }

    public synchronized void release() {
        if (this.mNativeConfigOut != 0) {
            this.mNativeDestructor.destruct(this.mNativeConfigOut);
            this.mNativeConfigOut = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            release();
            super.finalize();
        } catch (Throwable unused) {
        }
    }
}
