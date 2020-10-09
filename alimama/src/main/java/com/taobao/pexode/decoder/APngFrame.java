package com.taobao.pexode.decoder;

import android.graphics.Bitmap;
import com.taobao.pexode.animate.AnimatedDrawableFrameInfo;
import com.taobao.pexode.animate.AnimatedImageFrame;

public class APngFrame implements AnimatedImageFrame {
    private final int mFrameNumber;
    private final long mNativePtr;

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetBlendMode();

    private native int nativeGetDisposalMode();

    private native int nativeGetDurationMs();

    private native int nativeGetHeight();

    private native int nativeGetWidth();

    private native int nativeGetXOffset();

    private native int nativeGetYOffset();

    private native void nativeRenderFrame(int i, int i2, Bitmap bitmap);

    APngFrame(long j, int i) {
        this.mNativePtr = j;
        this.mFrameNumber = i;
    }

    private static AnimatedDrawableFrameInfo.DisposalMode transformNativeDisposalMode(int i) {
        if (i == 1) {
            return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_BACKGROUND;
        }
        if (i == 2) {
            return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_PREVIOUS;
        }
        return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_DO_NOT;
    }

    private static AnimatedDrawableFrameInfo.BlendMode transformNativeBlendMode(int i) {
        if (i == 1) {
            return AnimatedDrawableFrameInfo.BlendMode.BLEND_WITH_PREVIOUS;
        }
        return AnimatedDrawableFrameInfo.BlendMode.NO_BLEND;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        nativeFinalize();
    }

    public void dispose() {
        nativeDispose();
    }

    public void renderFrame(int i, int i2, Bitmap bitmap) {
        nativeRenderFrame(i, i2, bitmap);
    }

    public int getDurationMs() {
        return nativeGetDurationMs();
    }

    public int getWidth() {
        return nativeGetWidth();
    }

    public int getHeight() {
        return nativeGetHeight();
    }

    public int getXOffset() {
        return nativeGetXOffset();
    }

    public int getYOffset() {
        return nativeGetYOffset();
    }

    public AnimatedDrawableFrameInfo getFrameInfo() {
        return new AnimatedDrawableFrameInfo(this.mFrameNumber, getXOffset(), getYOffset(), getWidth(), getHeight(), transformNativeBlendMode(nativeGetBlendMode()), transformNativeDisposalMode(nativeGetDisposalMode()));
    }
}
