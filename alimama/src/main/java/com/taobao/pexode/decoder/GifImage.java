package com.taobao.pexode.decoder;

import com.taobao.pexode.animate.AnimatedImage;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;

public class GifImage implements AnimatedImage {
    private static final int LOOP_COUNT_FOREVER = 0;
    private static final int LOOP_COUNT_INFINITE = 0;
    private static final int LOOP_COUNT_MISSING = -1;
    private long mNativeContext;

    private static native GifImage nativeCreateFromDirectByteBuffer(ByteBuffer byteBuffer);

    private static native GifImage nativeCreateFromFileDescriptor(FileDescriptor fileDescriptor);

    private static native GifImage nativeCreateFromNativeMemory(long j, int i);

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDuration();

    private native GifFrame nativeGetFrame(int i);

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

    GifImage(long j) {
        this.mNativeContext = j;
    }

    public static GifImage create(byte[] bArr) {
        return create(bArr, 0, bArr.length);
    }

    public static GifImage create(byte[] bArr, int i, int i2) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2);
        allocateDirect.put(bArr, i, i2);
        allocateDirect.rewind();
        return nativeCreateFromDirectByteBuffer(allocateDirect);
    }

    public static GifImage create(FileDescriptor fileDescriptor) {
        return nativeCreateFromFileDescriptor(fileDescriptor);
    }

    public static GifImage create(long j, int i) {
        return nativeCreateFromNativeMemory(j, i);
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
        switch (nativeGetLoopCount) {
            case -1:
                return 1;
            case 0:
                return 0;
            default:
                return nativeGetLoopCount + 1;
        }
    }

    public GifFrame getFrame(int i) {
        GifFrame nativeGetFrame = nativeGetFrame(i);
        nativeGetFrame.setFrameNumber(i);
        return nativeGetFrame;
    }

    public int getSizeInBytes() {
        return nativeGetSizeInBytes();
    }
}
