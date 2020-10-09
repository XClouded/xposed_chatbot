package com.taobao.phenix.animate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import com.taobao.pexode.animate.AnimatedDrawableFrameInfo;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.pexode.animate.AnimatedImageFrame;
import com.taobao.phenix.bitmap.BitmapSupplier;
import com.taobao.phenix.common.UnitedLog;

public class AnimatedFrameCompositor {
    private final AnimatedFramesBuffer mAnimatedFramesBuffer;
    private final AnimatedImage mAnimatedImage;
    private String mDrawableId;
    private final AnimatedDrawableFrameInfo[] mFrameInfos;
    private final int mImageHeight = this.mAnimatedImage.getHeight();
    private final int mImageWidth = this.mAnimatedImage.getWidth();
    private Bitmap mTempRenderBitmap;
    private final Paint mTransparentFillPaint;

    public interface Callback {
        Bitmap getCachedBitmap(int i);

        void onIntermediateResult(int i, Bitmap bitmap);
    }

    private enum CompositedFrameRenderingType {
        REQUIRED,
        NOT_REQUIRED,
        SKIP,
        ABORT
    }

    /* JADX INFO: finally extract failed */
    public AnimatedFrameCompositor(AnimatedImage animatedImage, AnimatedFramesBuffer animatedFramesBuffer, String str) {
        this.mAnimatedImage = animatedImage;
        this.mDrawableId = str;
        this.mAnimatedFramesBuffer = animatedFramesBuffer;
        this.mTransparentFillPaint = new Paint();
        int i = 0;
        this.mTransparentFillPaint.setColor(0);
        this.mTransparentFillPaint.setStyle(Paint.Style.FILL);
        this.mTransparentFillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.mFrameInfos = new AnimatedDrawableFrameInfo[this.mAnimatedImage.getFrameCount()];
        while (i < this.mAnimatedImage.getFrameCount()) {
            AnimatedImageFrame frame = this.mAnimatedImage.getFrame(i);
            try {
                this.mFrameInfos[i] = frame.getFrameInfo();
                frame.dispose();
                i++;
            } catch (Throwable th) {
                frame.dispose();
                throw th;
            }
        }
    }

    public AnimatedDrawableFrameInfo getFrameInfoAt(int i) {
        return this.mFrameInfos[i];
    }

    public synchronized void dropCaches() {
        this.mTempRenderBitmap = null;
    }

    public void renderFrame(int i, Bitmap bitmap) {
        int i2;
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0, PorterDuff.Mode.SRC);
        if (isKeyFrame(i)) {
            i2 = i;
        } else {
            i2 = prepareWithClosestCompositedFrame(i - 1, canvas);
        }
        while (i2 < i) {
            AnimatedDrawableFrameInfo animatedDrawableFrameInfo = this.mFrameInfos[i2];
            AnimatedDrawableFrameInfo.DisposalMode disposalMode = animatedDrawableFrameInfo.mDisposalMode;
            if (disposalMode != AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_PREVIOUS) {
                if (animatedDrawableFrameInfo.mBlendMode == AnimatedDrawableFrameInfo.BlendMode.NO_BLEND) {
                    disposeToBackground(canvas, animatedDrawableFrameInfo);
                }
                renderFrameAt(i2, canvas);
                if (disposalMode == AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_BACKGROUND) {
                    disposeToBackground(canvas, animatedDrawableFrameInfo);
                }
            }
            i2++;
        }
        AnimatedDrawableFrameInfo animatedDrawableFrameInfo2 = this.mFrameInfos[i];
        if (animatedDrawableFrameInfo2.mBlendMode == AnimatedDrawableFrameInfo.BlendMode.NO_BLEND) {
            disposeToBackground(canvas, animatedDrawableFrameInfo2);
        }
        renderFrameAt(i, canvas);
    }

    private void ensureTempRenderBitmap() {
        if (this.mTempRenderBitmap == null) {
            this.mTempRenderBitmap = BitmapSupplier.getInstance().get(this.mImageWidth, this.mImageHeight, Bitmap.Config.ARGB_8888);
        } else {
            this.mTempRenderBitmap.eraseColor(0);
        }
    }

    private void renderFrameAt(int i, Canvas canvas) {
        AnimatedImageFrame frame = this.mAnimatedImage.getFrame(i);
        try {
            synchronized (this) {
                ensureTempRenderBitmap();
                frame.renderFrame(frame.getWidth(), frame.getHeight(), this.mTempRenderBitmap);
                canvas.save();
                canvas.translate((float) frame.getXOffset(), (float) frame.getYOffset());
                canvas.drawBitmap(this.mTempRenderBitmap, 0.0f, 0.0f, (Paint) null);
                canvas.restore();
            }
        } catch (Throwable th) {
            try {
                UnitedLog.e("AnimatedImage", "%s compositor render frame[%d] error=%s", this.mDrawableId, Integer.valueOf(i), th);
            } catch (Throwable th2) {
                frame.dispose();
                throw th2;
            }
        }
        frame.dispose();
    }

    private int prepareWithClosestCompositedFrame(int i, Canvas canvas) {
        while (i >= 0) {
            switch (getCompositedFrameRenderingType(i)) {
                case REQUIRED:
                    AnimatedDrawableFrameInfo animatedDrawableFrameInfo = this.mFrameInfos[i];
                    Bitmap cachedBitmapAt = this.mAnimatedFramesBuffer.getCachedBitmapAt(i);
                    if (cachedBitmapAt == null) {
                        if (!isKeyFrame(i)) {
                            break;
                        } else {
                            return i;
                        }
                    } else {
                        canvas.drawBitmap(cachedBitmapAt, 0.0f, 0.0f, (Paint) null);
                        this.mAnimatedFramesBuffer.freeBitmap(cachedBitmapAt);
                        if (animatedDrawableFrameInfo.mDisposalMode == AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_BACKGROUND) {
                            disposeToBackground(canvas, animatedDrawableFrameInfo);
                        }
                        return i + 1;
                    }
                case NOT_REQUIRED:
                    return i + 1;
                case ABORT:
                    return i;
            }
            i--;
        }
        return 0;
    }

    private CompositedFrameRenderingType getCompositedFrameRenderingType(int i) {
        AnimatedDrawableFrameInfo animatedDrawableFrameInfo = this.mFrameInfos[i];
        AnimatedDrawableFrameInfo.DisposalMode disposalMode = animatedDrawableFrameInfo.mDisposalMode;
        if (disposalMode == AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_DO_NOT) {
            return CompositedFrameRenderingType.REQUIRED;
        }
        if (disposalMode == AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_BACKGROUND) {
            if (isFullFrame(animatedDrawableFrameInfo)) {
                return CompositedFrameRenderingType.NOT_REQUIRED;
            }
            return CompositedFrameRenderingType.REQUIRED;
        } else if (disposalMode == AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_PREVIOUS) {
            return CompositedFrameRenderingType.SKIP;
        } else {
            return CompositedFrameRenderingType.ABORT;
        }
    }

    private boolean isKeyFrame(int i) {
        if (i == 0) {
            return true;
        }
        AnimatedDrawableFrameInfo animatedDrawableFrameInfo = this.mFrameInfos[i];
        AnimatedDrawableFrameInfo animatedDrawableFrameInfo2 = this.mFrameInfos[i - 1];
        if (animatedDrawableFrameInfo.mBlendMode == AnimatedDrawableFrameInfo.BlendMode.NO_BLEND && isFullFrame(animatedDrawableFrameInfo)) {
            return true;
        }
        if (animatedDrawableFrameInfo2.mDisposalMode != AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_BACKGROUND || !isFullFrame(animatedDrawableFrameInfo2)) {
            return false;
        }
        return true;
    }

    private boolean isFullFrame(AnimatedDrawableFrameInfo animatedDrawableFrameInfo) {
        return animatedDrawableFrameInfo.xOffset == 0 && animatedDrawableFrameInfo.yOffset == 0 && animatedDrawableFrameInfo.width == this.mImageWidth && animatedDrawableFrameInfo.height == this.mImageHeight;
    }

    private void disposeToBackground(Canvas canvas, AnimatedDrawableFrameInfo animatedDrawableFrameInfo) {
        canvas.drawRect((float) animatedDrawableFrameInfo.xOffset, (float) animatedDrawableFrameInfo.yOffset, (float) (animatedDrawableFrameInfo.xOffset + animatedDrawableFrameInfo.width), (float) (animatedDrawableFrameInfo.yOffset + animatedDrawableFrameInfo.height), this.mTransparentFillPaint);
    }
}
