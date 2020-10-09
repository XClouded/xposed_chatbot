package com.taobao.phenix.cache.memory;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import java.lang.ref.WeakReference;

public class ReleasableBitmapDrawable extends PassableBitmapDrawable {
    private WeakReference<Drawable.Callback> mLastCallbackRef;
    private ReleasableReferenceListener mReferenceListener;
    private boolean mReleased;

    public ReleasableBitmapDrawable(Resources resources, Bitmap bitmap, Rect rect, String str, String str2, int i, int i2) {
        super(resources, bitmap, rect, str, str2, i, i2);
    }

    public synchronized void setReleasableReferenceListener(ReleasableReferenceListener releasableReferenceListener) {
        this.mReferenceListener = releasableReferenceListener;
    }

    public NinePatchDrawable convert2NinePatchDrawable() {
        NinePatchDrawable convert2NinePatchDrawable = super.convert2NinePatchDrawable();
        synchronized (this) {
            if (convert2NinePatchDrawable != null) {
                try {
                    if (this.mReferenceListener != null) {
                        this.mReferenceListener.onReferenceDowngrade2Passable(this);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return convert2NinePatchDrawable;
    }

    public synchronized void downgrade2Passable() {
        if (this.mReferenceListener != null) {
            this.mReferenceListener.onReferenceDowngrade2Passable(this);
        }
    }

    public void release() {
        setCallback((Drawable.Callback) null);
        this.mReleased = true;
        synchronized (this) {
            if (this.mReferenceListener != null) {
                this.mReferenceListener.onReferenceReleased(this);
            }
        }
    }

    public void draw(Canvas canvas) {
        if (!this.mReleased) {
            super.draw(canvas);
            Drawable.Callback callback = getCallback();
            Drawable.Callback callback2 = null;
            if (this.mLastCallbackRef == null || (callback2 = (Drawable.Callback) this.mLastCallbackRef.get()) != callback) {
                synchronized (this) {
                    if (callback2 != null) {
                        try {
                            if (this.mReferenceListener != null) {
                                this.mReferenceListener.onReferenceDowngrade2Passable(this);
                            }
                        } catch (Throwable th) {
                            while (true) {
                                throw th;
                            }
                        }
                    }
                }
                this.mLastCallbackRef = new WeakReference<>(callback);
                return;
            }
            return;
        }
        throw new RuntimeException("ReleasableBitmapDrawable has been released before drawing!");
    }
}
