package com.taobao.phenix.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.rxm.common.Releasable;
import com.taobao.weex.el.parse.Operators;

public class DecodedImage implements Releasable {
    public static final int ANIMATE_IMAGE = 2;
    public static final int STATIC_BITMAP = 1;
    private final AnimatedImage mAnimatedImage;
    private final Bitmap mBitmap;
    private final Rect mBitmapPadding;
    private EncodedImage mEncodedImage;
    private final int mType;

    public DecodedImage(EncodedImage encodedImage, Bitmap bitmap) {
        this(encodedImage, bitmap, (AnimatedImage) null, (Rect) null);
    }

    public DecodedImage(EncodedImage encodedImage, Bitmap bitmap, AnimatedImage animatedImage, Rect rect) {
        if (bitmap != null) {
            this.mType = 1;
        } else {
            this.mType = 2;
        }
        this.mEncodedImage = encodedImage;
        this.mBitmap = bitmap;
        this.mAnimatedImage = animatedImage;
        this.mBitmapPadding = rect;
    }

    public boolean needCached() {
        return this.mEncodedImage == null || this.mEncodedImage.completed;
    }

    public EncodedImage getEncodedImage() {
        return this.mEncodedImage;
    }

    public int getType() {
        return this.mType;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public Rect getBitmapPadding() {
        return this.mBitmapPadding;
    }

    public AnimatedImage getAnimatedImage() {
        return this.mAnimatedImage;
    }

    public boolean isStaticBitmap() {
        return this.mType == 1;
    }

    public boolean isAvailable() {
        if (this.mType != 1 || this.mBitmap == null) {
            return this.mType == 2 && this.mAnimatedImage != null;
        }
        return true;
    }

    public void release() {
        if (this.mEncodedImage != null) {
            this.mEncodedImage.release();
        }
        if (this.mAnimatedImage != null) {
            this.mAnimatedImage.dispose();
        }
    }

    public String toString() {
        return "DecodedImage(type=" + this.mType + ", bitmap=" + this.mBitmap + ", animated=" + this.mAnimatedImage + Operators.BRACKET_END_STR;
    }
}
