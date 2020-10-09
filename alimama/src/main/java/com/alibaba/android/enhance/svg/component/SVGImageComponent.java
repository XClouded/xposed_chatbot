package com.alibaba.android.enhance.svg.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import androidx.annotation.NonNull;
import com.alibaba.android.WeexEnhance;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.SVGPlugin;
import com.alibaba.android.enhance.svg.utils.ViewBox;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.concurrent.atomic.AtomicBoolean;

public class SVGImageComponent extends RenderableSVGVirtualComponent {
    /* access modifiers changed from: private */
    public final LruCache<String, Bitmap> BITMAP_HOLDER = new LruCache<>(1);
    /* access modifiers changed from: private */
    public AtomicBoolean isLoading = new AtomicBoolean(false);
    private String mAlign = "xMidYMid";
    private String mH = "0";
    private String mHref;
    private int mImageHeight;
    private int mImageWidth;
    private int mMeetOrSlice = 0;
    private Paint mPooledAlphaPaint = null;
    private String mW = "0";
    private String mX = "0";
    private String mY = "0";

    public SVGImageComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public Path getPath(Canvas canvas, Paint paint) {
        Path path = new Path();
        path.addRect(getRect(), Path.Direction.CW);
        return path;
    }

    public void draw(Canvas canvas, Paint paint, float f) {
        if (!TextUtils.isEmpty(this.mHref) && this.mOpacity * f > 0.01f && !this.isLoading.get()) {
            this.mPath = getPath(canvas, paint);
            Bitmap bitmap = this.BITMAP_HOLDER.get(this.mHref);
            if (bitmap != null) {
                renderImage(bitmap, canvas, paint, f);
            } else {
                loadImage(this.mHref);
            }
        }
    }

    private void loadImage(@NonNull final String str) {
        WeexEnhance.ImageLoadAdapter imageAdapter = SVGPlugin.getImageAdapter();
        if (imageAdapter == null) {
            Log.e(SVGPlugin.TAG, "[svg-image] error! can not load bitmap. Missing Image Loader");
            return;
        }
        this.isLoading.set(true);
        imageAdapter.fetchBitmapAsync(str, new WeexEnhance.OnImageLoadListener() {
            public void onLoadSuccess(@NonNull Bitmap bitmap) {
                SVGImageComponent.this.isLoading.set(false);
                SVGImageComponent.this.BITMAP_HOLDER.put(str, bitmap);
                SVGImageComponent.this.markUpdated();
            }

            public void onLoadFailed(@NonNull String str) {
                SVGImageComponent.this.isLoading.set(false);
                Log.e(SVGPlugin.TAG, "[svg-image] error! can not load bitmap. " + str);
            }
        });
    }

    private void renderImage(@NonNull Bitmap bitmap, Canvas canvas, Paint paint, float f) {
        float opacity = f * getOpacity();
        if (this.mImageWidth == 0 || this.mImageHeight == 0) {
            this.mImageWidth = bitmap.getWidth();
            this.mImageHeight = bitmap.getHeight();
        }
        if (this.mPooledAlphaPaint == null) {
            this.mPooledAlphaPaint = new Paint(1);
        }
        this.mPooledAlphaPaint.setAlpha((int) (opacity * 255.0f));
        RectF rect = getRect();
        RectF rectF = new RectF(0.0f, 0.0f, (float) this.mImageWidth, (float) this.mImageHeight);
        preProcessIfHasMask(canvas);
        clip(canvas, paint);
        canvas.save();
        canvas.concat(ViewBox.getTransform(rectF, rect, this.mAlign, this.mMeetOrSlice));
        canvas.drawBitmap(bitmap, (Rect) null, rectF, this.mPooledAlphaPaint);
        canvas.restore();
        applyMask(canvas, paint);
    }

    @WXComponentProp(name = "x")
    public void setX(String str) {
        this.mX = str;
        markUpdated();
    }

    @WXComponentProp(name = "y")
    public void setY(String str) {
        this.mY = str;
        markUpdated();
    }

    @WXComponentProp(name = "width")
    public void setWidth(String str) {
        this.mW = str;
        markUpdated();
    }

    @WXComponentProp(name = "height")
    public void setHeight(String str) {
        this.mH = str;
        markUpdated();
    }

    @WXComponentProp(name = "preserveAspectRatio")
    public void setPreserveAspectRatio(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.trim().split("\\s+");
            if (split.length >= 1 && split.length <= 2) {
                if (split.length != 1) {
                    this.mAlign = split[0];
                    this.mMeetOrSlice = ViewBox.parseMeetOrSlice(split[1]);
                } else if (split[0].equals("none")) {
                    this.mMeetOrSlice = 2;
                } else {
                    this.mAlign = split[0];
                    this.mMeetOrSlice = ViewBox.parseMeetOrSlice((String) null);
                }
                markUpdated();
            }
        }
    }

    @WXComponentProp(name = "xlink:href")
    public void setHref(String str) {
        this.mHref = str;
        if (!TextUtils.isEmpty(this.mHref)) {
            this.mHref = this.mHref.trim();
        }
        markUpdated();
    }

    @WXComponentProp(name = "href")
    public void setHref2(String str) {
        this.mHref = str;
        if (!TextUtils.isEmpty(this.mHref)) {
            this.mHref = this.mHref.trim();
        }
        markUpdated();
    }

    @NonNull
    private RectF getRect() {
        double relativeOnWidth = relativeOnWidth(this.mX);
        double relativeOnHeight = relativeOnHeight(this.mY);
        return new RectF((float) relativeOnWidth, (float) relativeOnHeight, (float) (relativeOnWidth + relativeOnWidth(this.mW)), (float) (relativeOnHeight + relativeOnHeight(this.mH)));
    }
}
