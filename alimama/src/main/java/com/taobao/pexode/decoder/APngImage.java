package com.taobao.pexode.decoder;

import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.pexode.entity.RewindableStream;
import java.io.FileDescriptor;

public class APngImage implements AnimatedImage {
    private static final int LOOP_COUNT_MISSING = -1;
    private long mNativePtr;

    static native APngImage nativeCreateFromBytes(byte[] bArr, int i, int i2);

    static native APngImage nativeCreateFromFd(FileDescriptor fileDescriptor);

    static native APngImage nativeCreateFromRewindableStream(RewindableStream rewindableStream, byte[] bArr);

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDuration();

    private native APngFrame nativeGetFrame(int i);

    private native int nativeGetFrameCount();

    private native int[] nativeGetFrameDurations();

    private native int nativeGetHeight();

    private native int nativeGetLoopCount();

    private native int nativeGetSizeInBytes();

    private native int nativeGetWidth();

    static native int nativeLoadedVersionTest();

    public boolean doesRenderSupportScaling() {
        return false;
    }

    APngImage(long j) {
        this.mNativePtr = j;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        nativeFinalize();
    }

    public void dispose() {
        nativeDispose();
    }

    public int getWidth() {
        return nativeGetWidth();
    }

    public int getHeight() {
        return nativeGetHeight();
    }

    public int getFrameCount() {
        return nativeGetFrameCount();
    }

    public int getDuration() {
        return nativeGetDuration();
    }

    public int[] getFrameDurations() {
        return nativeGetFrameDurations();
    }

    public int getLoopCount() {
        int nativeGetLoopCount = nativeGetLoopCount();
        if (nativeGetLoopCount == -1) {
            return 1;
        }
        return nativeGetLoopCount;
    }

    public APngFrame getFrame(int i) {
        return nativeGetFrame(i);
    }

    public int getSizeInBytes() {
        return nativeGetSizeInBytes();
    }
}
