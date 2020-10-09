package com.taobao.pexode.decoder;

import android.graphics.Bitmap;
import com.taobao.pexode.animate.AnimatedDrawableFrameInfo;
import com.taobao.pexode.animate.AnimatedImageFrame;

public class GifFrame implements AnimatedImageFrame {
    private int mFrameNumber;
    private long mNativeContext;

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDisposalMode();

    private native int nativeGetDurationMs();

    private native int nativeGetHeight();

    private native int nativeGetWidth();

    private native int nativeGetXOffset();

    private native int nativeGetYOffset();

    private native boolean nativeHasTransparency();

    private native void nativeRenderFrame(int i, int i2, Bitmap bitmap);

    GifFrame(long j) {
        this.mNativeContext = j;
    }

    private static AnimatedDrawableFrameInfo.DisposalMode fromGifDisposalMethod(int i) {
        if (i == 0) {
            return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_DO_NOT;
        }
        if (i == 1) {
            return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_DO_NOT;
        }
        if (i == 2) {
            return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_BACKGROUND;
        }
        if (i == 3) {
            return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_PREVIOUS;
        }
        return AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_DO_NOT;
    }

    public void setFrameNumber(int i) {
        this.mFrameNumber = i;
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

    public boolean hasTransparency() {
        return nativeHasTransparency();
    }

    public int getDisposalMode() {
        return nativeGetDisposalMode();
    }

    public AnimatedDrawableFrameInfo getFrameInfo() {
        return new AnimatedDrawableFrameInfo(this.mFrameNumber, getXOffset(), getYOffset(), getWidth(), getHeight(), AnimatedDrawableFrameInfo.BlendMode.BLEND_WITH_PREVIOUS, fromGifDisposalMethod(getDisposalMode()));
    }
}
