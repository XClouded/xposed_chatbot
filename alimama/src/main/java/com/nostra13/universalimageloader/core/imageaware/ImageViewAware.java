package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.utils.L;
import java.lang.reflect.Field;

public class ImageViewAware extends ViewAware {
    public ImageViewAware(ImageView imageView) {
        super(imageView);
    }

    public ImageViewAware(ImageView imageView, boolean z) {
        super(imageView, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = (android.widget.ImageView) r2.viewRef.get();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getWidth() {
        /*
            r2 = this;
            int r0 = super.getWidth()
            if (r0 > 0) goto L_0x0016
            java.lang.ref.Reference r1 = r2.viewRef
            java.lang.Object r1 = r1.get()
            android.widget.ImageView r1 = (android.widget.ImageView) r1
            if (r1 == 0) goto L_0x0016
            java.lang.String r0 = "mMaxWidth"
            int r0 = getImageViewFieldValue(r1, r0)
        L_0x0016:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.imageaware.ImageViewAware.getWidth():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = (android.widget.ImageView) r2.viewRef.get();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getHeight() {
        /*
            r2 = this;
            int r0 = super.getHeight()
            if (r0 > 0) goto L_0x0016
            java.lang.ref.Reference r1 = r2.viewRef
            java.lang.Object r1 = r1.get()
            android.widget.ImageView r1 = (android.widget.ImageView) r1
            if (r1 == 0) goto L_0x0016
            java.lang.String r0 = "mMaxHeight"
            int r0 = getImageViewFieldValue(r1, r0)
        L_0x0016:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.imageaware.ImageViewAware.getHeight():int");
    }

    public ViewScaleType getScaleType() {
        ImageView imageView = (ImageView) this.viewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return super.getScaleType();
    }

    public ImageView getWrappedView() {
        return (ImageView) super.getWrappedView();
    }

    /* access modifiers changed from: protected */
    public void setImageDrawableInto(Drawable drawable, View view) {
        ((ImageView) view).setImageDrawable(drawable);
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

    /* access modifiers changed from: protected */
    public void setImageBitmapInto(Bitmap bitmap, View view) {
        ((ImageView) view).setImageBitmap(bitmap);
    }

    private static int getImageViewFieldValue(Object obj, String str) {
        try {
            Field declaredField = ImageView.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            int intValue = ((Integer) declaredField.get(obj)).intValue();
            if (intValue <= 0 || intValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return intValue;
        } catch (Exception e) {
            L.e(e);
            return 0;
        }
    }
}
