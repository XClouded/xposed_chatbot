package com.taobao.uikit.extend.feature.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.taobao.phenix.cache.memory.ReleasableBitmapDrawable;
import com.taobao.uikit.feature.view.IGetBitmap;
import com.taobao.uikit.utils.UIKITLog;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DrawableProxy extends Drawable implements IGetBitmap {
    private TUrlImageView mBindView;
    private boolean mIsRecovering = false;
    protected BitmapDrawable mRealDrawable;

    public static DrawableProxy obtain(BitmapDrawable bitmapDrawable) {
        return new DrawableProxy(bitmapDrawable);
    }

    private DrawableProxy(BitmapDrawable bitmapDrawable) {
        this.mRealDrawable = bitmapDrawable;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean recover() {
        if (this.mIsRecovering || ((this.mRealDrawable != null && (this.mRealDrawable.getBitmap() == null || !this.mRealDrawable.getBitmap().isRecycled())) || this.mBindView == null)) {
            return false;
        }
        this.mIsRecovering = true;
        this.mBindView.reload();
        return true;
    }

    public synchronized boolean release() {
        if (this.mRealDrawable == null) {
            return false;
        }
        if (this.mRealDrawable instanceof ReleasableBitmapDrawable) {
            ((ReleasableBitmapDrawable) this.mRealDrawable).release();
        }
        this.mRealDrawable = null;
        return true;
    }

    public void draw(Canvas canvas) {
        if (recover()) {
            UIKITLog.i(TUrlImageView.LOG_TAG, "recover on draw, width=%d, height=%d, id=%s, url=%s", Integer.valueOf(canvas.getWidth()), Integer.valueOf(canvas.getHeight()), this.mBindView, this.mBindView.getLoadingUrl());
        } else if (this.mRealDrawable != null) {
            this.mRealDrawable.setChangingConfigurations(getChangingConfigurations());
            this.mRealDrawable.setBounds(getBounds());
            this.mRealDrawable.setCallback(getCallback());
            this.mRealDrawable.draw(canvas);
            this.mRealDrawable.setCallback((Drawable.Callback) null);
        }
    }

    private void tryDowngrade2Passable() {
        if (this.mRealDrawable instanceof ReleasableBitmapDrawable) {
            ((ReleasableBitmapDrawable) this.mRealDrawable).downgrade2Passable();
        }
    }

    /* access modifiers changed from: protected */
    public Drawable getRealDrawable(boolean z) {
        if (!z) {
            tryDowngrade2Passable();
        }
        return this.mRealDrawable;
    }

    public Bitmap getBitmap() {
        if (this.mRealDrawable == null) {
            return null;
        }
        tryDowngrade2Passable();
        return this.mRealDrawable.getBitmap();
    }

    /* access modifiers changed from: protected */
    public boolean isContentDifferent(Drawable drawable) {
        if (drawable instanceof DrawableProxy) {
            if (this.mRealDrawable != ((DrawableProxy) drawable).mRealDrawable) {
                return true;
            }
            return false;
        } else if (this != drawable) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public DrawableProxy bindHostView(TUrlImageView tUrlImageView) {
        this.mBindView = tUrlImageView;
        return this;
    }

    public void setFilterBitmap(boolean z) {
        if (this.mRealDrawable != null) {
            this.mRealDrawable.setFilterBitmap(z);
            invalidateSelf();
        }
    }

    public void setDither(boolean z) {
        if (this.mRealDrawable != null) {
            this.mRealDrawable.setDither(z);
            invalidateSelf();
        }
    }

    public void setAlpha(int i) {
        if (this.mRealDrawable != null) {
            this.mRealDrawable.setAlpha(i);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mRealDrawable != null) {
            this.mRealDrawable.setColorFilter(colorFilter);
            invalidateSelf();
        }
    }

    public void setColorFilter(int i, PorterDuff.Mode mode) {
        if (this.mRealDrawable != null) {
            this.mRealDrawable.setColorFilter(i, mode);
        }
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        if (this.mRealDrawable != null) {
            this.mRealDrawable.inflate(resources, xmlPullParser, attributeSet);
        }
    }

    public int getIntrinsicWidth() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getIntrinsicWidth();
        }
        return super.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getIntrinsicHeight();
        }
        return super.getIntrinsicHeight();
    }

    public int getOpacity() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getOpacity();
        }
        return 0;
    }

    public boolean isStateful() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.isStateful();
        }
        return super.isStateful();
    }

    public int getChangingConfigurations() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations();
    }

    public Region getTransparentRegion() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getTransparentRegion();
        }
        return super.getTransparentRegion();
    }

    public int getMinimumWidth() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getMinimumWidth();
        }
        return super.getMinimumWidth();
    }

    public int getMinimumHeight() {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getMinimumHeight();
        }
        return super.getMinimumHeight();
    }

    public boolean getPadding(Rect rect) {
        if (this.mRealDrawable != null) {
            return this.mRealDrawable.getPadding(rect);
        }
        return super.getPadding(rect);
    }

    public String toString() {
        return "DrawableProxy@" + Integer.toHexString(hashCode());
    }
}
