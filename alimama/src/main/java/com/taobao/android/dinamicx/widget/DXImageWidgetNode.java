package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamicx.DXGlobalCenter;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.lang.ref.WeakReference;

public class DXImageWidgetNode extends DXWidgetNode {
    public static final String HEIGHT_LIMIT = "heightLimit";
    private static final int IMAGE_VIEW_SCALE_TYPE_CENTER_CROP = 2;
    private static final int IMAGE_VIEW_SCALE_TYPE_FIT_CENTER = 0;
    private static final int IMAGE_VIEW_SCALE_TYPE_FIT_XY = 1;
    public static final String TAG = "DXImageWidgetNode";
    public static final String WIDTH_LIMIT = "widthLimit";
    static LruCache<String, Double> imageRatioCache = new LruCache<>(1024);
    static LruCache<String, Integer> resCache = new LruCache<>(100);
    private boolean animated;
    private double aspectRatio = -1.0d;
    private boolean asyncImageLoad = true;
    private boolean autoRelease = true;
    private String darkImageUrl;
    private double darkModeOverlayOpacity = 0.5d;
    private boolean forceOriginal = false;
    private String imageName;
    private String imageUrl;
    private Drawable localImageDrawable;
    private boolean needDarkModeOverlay;
    private Drawable placeHolder;
    private String placeHolderName;
    private int scaleType;

    public interface ImageLoadListener {
        boolean onHappen(ImageResult imageResult);
    }

    public static class ImageResult {
        public Drawable drawable;
    }

    public DXImageWidgetNode() {
        this.cornerRadius = -1;
        this.cornerRadiusLeftBottom = -1;
        this.cornerRadiusRightBottom = -1;
        this.cornerRadiusRightTop = -1;
        this.cornerRadiusLeftTop = -1;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXImageWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXImageWidgetNode();
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXHashConstant.DX_IMAGEVIEW_DARK_MODE_IMAGEURL) {
            this.darkImageUrl = str;
        } else if (DXHashConstant.DX_IMAGEVIEW_IMAGEURL == j) {
            this.imageUrl = str;
        } else if (DXHashConstant.DX_IMAGEVIEW_IMAGENAME == j) {
            this.imageName = str;
        } else if (DXHashConstant.DX_IMAGEVIEW_PLACEHOLDERNAME == j) {
            this.placeHolderName = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }

    /* access modifiers changed from: protected */
    public void onSetIntAttribute(long j, int i) {
        if (DXHashConstant.DX_IMAGEVIEW_SCALETYPE == j) {
            this.scaleType = i;
            return;
        }
        boolean z = false;
        if (DXHashConstant.DX_IMAGEVIEW_ANIMATED == j) {
            if (i == 1) {
                z = true;
            }
            this.animated = z;
        } else if (DXHashConstant.DX_IMAGEVIEW_AUTORELEASE == j) {
            if (i == 1) {
                z = true;
            }
            this.autoRelease = z;
        } else if (-273786109416499313L == j) {
            if (i == 1) {
                z = true;
            }
            this.asyncImageLoad = z;
        } else if (j == DXHashConstant.DX_IMAGEVIEW_FORCE_ORIGINAL) {
            if (i != 0) {
                z = true;
            }
            this.forceOriginal = z;
        } else if (j == DXHashConstant.DX_IMAGEVIEW_NEED_DARK_MODE_OVERLAY) {
            if (i != 0) {
                z = true;
            }
            this.needDarkModeOverlay = z;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (DXHashConstant.DX_IMAGEVIEW_AUTORELEASE == j || -273786109416499313L == j) {
            return 1;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onSetDoubleAttribute(long j, double d) {
        if (DXHashConstant.DX_IMAGEVIEW_ASPECTRATIO == j) {
            this.aspectRatio = d;
        } else if (j == DXHashConstant.DX_IMAGEVIEW_DARK_MODE_OVERLAYOPACITY) {
            this.darkModeOverlayOpacity = d;
        }
    }

    /* access modifiers changed from: protected */
    public void onSetObjAttribute(long j, Object obj) {
        if (DXHashConstant.DX_IMAGEVIEW_IMAGE == j) {
            if (obj instanceof Drawable) {
                this.localImageDrawable = (Drawable) obj;
            }
        } else if (5980555813819279758L == j && (obj instanceof Drawable)) {
            this.placeHolder = (Drawable) obj;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        int mode2 = DXWidgetNode.DXMeasureSpec.getMode(i2);
        boolean z = true;
        int i6 = 0;
        boolean z2 = mode != 1073741824;
        if (mode2 == 1073741824) {
            z = false;
        }
        if (z2 || z) {
            double d = this.aspectRatio;
            if (d <= 0.0d) {
                if (!TextUtils.isEmpty(this.imageUrl)) {
                    Double d2 = imageRatioCache.get(this.imageUrl);
                    if (d2 != null) {
                        d = d2.doubleValue();
                    }
                } else if (this.localImageDrawable != null) {
                    int intrinsicWidth = this.localImageDrawable.getIntrinsicWidth();
                    int intrinsicHeight = this.localImageDrawable.getIntrinsicHeight();
                    if (intrinsicHeight > 0) {
                        double d3 = (double) intrinsicWidth;
                        double d4 = (double) intrinsicHeight;
                        Double.isNaN(d3);
                        Double.isNaN(d4);
                        d = d3 / d4;
                    }
                }
            }
            if (!z2 || z) {
                if (!z2 && z) {
                    int size = View.MeasureSpec.getSize(i);
                    if (d > 0.0d) {
                        double d5 = (double) size;
                        Double.isNaN(d5);
                        i6 = size;
                        i5 = (int) (d5 / d);
                    } else {
                        i6 = size;
                    }
                }
                i5 = 0;
            } else {
                i5 = View.MeasureSpec.getSize(i2);
                if (d > 0.0d) {
                    double d6 = (double) i5;
                    Double.isNaN(d6);
                    i6 = (int) (d6 * d);
                }
            }
            int max = Math.max(i6, getSuggestedMinimumWidth());
            i3 = Math.max(i5, getSuggestedMinimumHeight());
            i4 = max;
        } else {
            i4 = DXWidgetNode.DXMeasureSpec.getSize(i);
            i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
        }
        setMeasuredDimension(resolveSize(i4, i), resolveSize(i3, i2));
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        IDXWebImageInterface dxWebImageInterface = DXGlobalCenter.getDxWebImageInterface();
        if (dxWebImageInterface == null) {
            return new ImageView(context);
        }
        return dxWebImageInterface.buildView(context);
    }

    /* access modifiers changed from: protected */
    public void setImageScaleType(ImageView imageView, int i) {
        switch (i) {
            case 0:
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return;
            case 1:
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return;
            case 2:
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return;
            default:
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return;
        }
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXImageWidgetNode) {
            DXImageWidgetNode dXImageWidgetNode = (DXImageWidgetNode) dXWidgetNode;
            this.aspectRatio = dXImageWidgetNode.aspectRatio;
            this.imageName = dXImageWidgetNode.imageName;
            this.imageUrl = dXImageWidgetNode.imageUrl;
            this.scaleType = dXImageWidgetNode.scaleType;
            this.localImageDrawable = dXImageWidgetNode.localImageDrawable;
            this.animated = dXImageWidgetNode.animated;
            this.autoRelease = dXImageWidgetNode.autoRelease;
            this.asyncImageLoad = dXImageWidgetNode.asyncImageLoad;
            this.placeHolderName = dXImageWidgetNode.placeHolderName;
            this.placeHolder = dXImageWidgetNode.placeHolder;
            this.forceOriginal = dXImageWidgetNode.forceOriginal;
            this.darkImageUrl = dXImageWidgetNode.darkImageUrl;
            this.darkModeOverlayOpacity = dXImageWidgetNode.darkModeOverlayOpacity;
            this.needDarkModeOverlay = dXImageWidgetNode.needDarkModeOverlay;
        }
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        final String str;
        ImageView imageView = (ImageView) view;
        ImageOption imageOption = new ImageOption();
        setImageScaleType(imageView, this.scaleType);
        if (!needHandleDark()) {
            str = this.imageUrl;
        } else if (!TextUtils.isEmpty(this.darkImageUrl)) {
            str = this.darkImageUrl;
        } else {
            str = this.imageUrl;
        }
        if (!TextUtils.isEmpty(str)) {
            boolean unused = imageOption.isNeedSetImageUrl = true;
            if (getMeasuredHeight() == 0 || getMeasuredWidth() == 0) {
                imageOption.listener = new ImageLoadListener() {
                    public boolean onHappen(ImageResult imageResult) {
                        Drawable drawable = imageResult.drawable;
                        if (drawable != null) {
                            int intrinsicWidth = drawable.getIntrinsicWidth();
                            int intrinsicHeight = drawable.getIntrinsicHeight();
                            if (intrinsicHeight > 0) {
                                LruCache<String, Double> lruCache = DXImageWidgetNode.imageRatioCache;
                                String str = str;
                                double d = (double) intrinsicWidth;
                                double d2 = (double) intrinsicHeight;
                                Double.isNaN(d);
                                Double.isNaN(d2);
                                lruCache.put(str, Double.valueOf(d / d2));
                            }
                        }
                        DXWidgetNode widgetNode = DXImageWidgetNode.this.getDXRuntimeContext().getWidgetNode();
                        if (widgetNode == null) {
                            return false;
                        }
                        widgetNode.setNeedLayout();
                        return false;
                    }
                };
            }
        } else if (this.localImageDrawable != null) {
            setLocalImage(imageView, this.localImageDrawable);
        } else if (!TextUtils.isEmpty(this.imageName)) {
            setLocalRes(imageView, this.imageName);
        } else {
            imageView.setImageDrawable((Drawable) null);
            boolean unused2 = imageOption.isNeedSetImageUrl = true;
        }
        if (imageOption.isNeedSetImageUrl) {
            imageOption.placeHolderResId = getDrawableId(context, this.placeHolderName);
            if (imageOption.placeHolderResId == 0) {
                imageOption.placeHolder = this.placeHolder;
            }
        }
        if (this.needSetBackground) {
            imageOption.cornerRadii = this.cornerRadius > 0 ? new int[]{this.cornerRadius, this.cornerRadius, this.cornerRadius, this.cornerRadius} : new int[]{this.cornerRadiusLeftTop, this.cornerRadiusRightTop, this.cornerRadiusRightBottom, this.cornerRadiusLeftBottom};
            boolean unused3 = imageOption.isNeedClipRadius = true;
        }
        if (this.needSetBackground) {
            imageOption.borderColor = tryFetchDarkModeColor("borderColor", 2, this.borderColor);
            imageOption.borderWidth = this.borderWidth;
            boolean unused4 = imageOption.isNeedBorderWidth = true;
            boolean unused5 = imageOption.isNeedBorderColor = true;
        }
        if (this.layoutWidth == -2 && this.layoutHeight != -2) {
            imageOption.sizeType = "heightLimit";
            boolean unused6 = imageOption.isNeedLimitSize = true;
        } else if (this.layoutWidth != -2 && this.layoutHeight == -2) {
            imageOption.sizeType = "widthLimit";
            boolean unused7 = imageOption.isNeedLimitSize = true;
        }
        boolean unused8 = imageOption.animated = this.animated;
        imageOption.autoRelease = this.autoRelease;
        boolean unused9 = imageOption.forceOriginal = this.forceOriginal;
        double unused10 = imageOption.darkModeOverlayOpacity = this.darkModeOverlayOpacity;
        boolean unused11 = imageOption.needDarkModeOverlay = this.needDarkModeOverlay;
        IDXWebImageInterface dxWebImageInterface = DXGlobalCenter.getDxWebImageInterface();
        if (dxWebImageInterface != null) {
            dxWebImageInterface.setImage(imageView, str, imageOption);
        }
    }

    /* access modifiers changed from: protected */
    public boolean extraHandleDark() {
        return !TextUtils.isEmpty(this.darkImageUrl) || this.needDarkModeOverlay;
    }

    public void setBackground(View view) {
        if (this.needSetBackground) {
            view.setBackgroundColor(tryFetchDarkModeColor("backGroundColor", 1, this.backGroundColor));
        }
    }

    /* access modifiers changed from: protected */
    public void setLocalImage(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    /* access modifiers changed from: protected */
    public void setLocalRes(ImageView imageView, String str) {
        if (str == null) {
            imageView.setImageDrawable((Drawable) null);
            imageView.setTag(DinamicTagKey.TAG_IMAGE_NAME, (Object) null);
        } else if (!str.equals((String) imageView.getTag(DinamicTagKey.TAG_IMAGE_NAME))) {
            LoadDrawableTask loadDrawableTask = new LoadDrawableTask(imageView, str);
            if (this.asyncImageLoad) {
                imageView.setTag(DinamicTagKey.TAG_CURRENT_IMAGE_NAME, str);
                DXRunnableManager.scheduledAsyncTask(loadDrawableTask, new Void[0]);
                return;
            }
            imageView.setImageDrawable(loadDrawableTask.getDrawable());
            imageView.setTag(DinamicTagKey.TAG_IMAGE_NAME, str);
        }
    }

    public static class LoadDrawableTask extends AsyncTask<Void, Void, Drawable> {
        private Context context;
        private WeakReference<ImageView> imageViewWeakReference;
        private String localImageName;

        public LoadDrawableTask(ImageView imageView, String str) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
            this.localImageName = str;
            this.context = imageView.getContext().getApplicationContext();
        }

        /* access modifiers changed from: protected */
        public Drawable doInBackground(Void... voidArr) {
            return getDrawable();
        }

        @Nullable
        public Drawable getDrawable() {
            Drawable drawable;
            int drawableId = DXImageWidgetNode.getDrawableId(this.context, this.localImageName);
            if (drawableId == 0) {
                return null;
            }
            try {
                if (Build.VERSION.SDK_INT >= 21) {
                    drawable = this.context.getDrawable(drawableId);
                } else {
                    drawable = this.context.getResources().getDrawable(drawableId);
                }
                return drawable;
            } catch (Exception e) {
                Log.e(DXImageWidgetNode.TAG, "Get layout parser exception", e);
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Drawable drawable) {
            ImageView imageView = (ImageView) this.imageViewWeakReference.get();
            if (imageView != null) {
                if (this.localImageName.equals((String) imageView.getTag(DinamicTagKey.TAG_CURRENT_IMAGE_NAME))) {
                    imageView.setImageDrawable(drawable);
                    imageView.setTag(DinamicTagKey.TAG_IMAGE_NAME, this.localImageName);
                }
            }
        }
    }

    public static int getDrawableId(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return 0;
        }
        Integer num = resCache.get(str);
        if (num == null) {
            try {
                num = Integer.valueOf(context.getResources().getIdentifier(str, "drawable", context.getPackageName()));
                resCache.put(str, num);
            } catch (Exception unused) {
                return 0;
            }
        }
        return num.intValue();
    }

    public static class ImageOption {
        /* access modifiers changed from: private */
        public boolean animated;
        public boolean autoRelease = true;
        public int borderColor;
        public int borderWidth;
        public int[] cornerRadii;
        /* access modifiers changed from: private */
        public double darkModeOverlayOpacity;
        /* access modifiers changed from: private */
        public boolean forceOriginal;
        /* access modifiers changed from: private */
        public boolean isNeedBorderColor;
        /* access modifiers changed from: private */
        public boolean isNeedBorderWidth;
        /* access modifiers changed from: private */
        public boolean isNeedClipRadius;
        /* access modifiers changed from: private */
        public boolean isNeedLimitSize;
        /* access modifiers changed from: private */
        public boolean isNeedSetImageUrl;
        public ImageLoadListener listener;
        public String module;
        /* access modifiers changed from: private */
        public boolean needDarkModeOverlay;
        public Drawable placeHolder;
        public int placeHolderResId;
        public String sizeType;

        public boolean isNeedSetImageUrl() {
            return this.isNeedSetImageUrl;
        }

        public boolean isNeedClipRadius() {
            return this.isNeedClipRadius;
        }

        public boolean isNeedLimitSize() {
            return this.isNeedLimitSize;
        }

        public boolean isNeedBorderColor() {
            return this.isNeedBorderColor;
        }

        public boolean isNeedBorderWidth() {
            return this.isNeedBorderWidth;
        }

        public boolean isForceOriginal() {
            return this.forceOriginal;
        }

        public boolean isNeedDarkModeOverlay() {
            return this.needDarkModeOverlay;
        }

        public boolean isAnimated() {
            return this.animated;
        }

        public boolean isAutoRelease() {
            return this.autoRelease;
        }

        public double getDarkModeOverlayOpacity() {
            return this.darkModeOverlayOpacity;
        }

        public int[] getCornerRadii() {
            return this.cornerRadii;
        }

        public String getModule() {
            return this.module;
        }

        public int getPlaceHolderResId() {
            return this.placeHolderResId;
        }

        public Drawable getPlaceHolder() {
            return this.placeHolder;
        }

        public String getSizeType() {
            return this.sizeType;
        }

        public ImageLoadListener getListener() {
            return this.listener;
        }

        public int getBorderColor() {
            return this.borderColor;
        }

        public int getBorderWidth() {
            return this.borderWidth;
        }
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setScaleType(int i) {
        this.scaleType = i;
    }

    public int getScaleType() {
        return this.scaleType;
    }

    public void setLocalImageDrawable(Drawable drawable) {
        this.localImageDrawable = drawable;
    }

    public Drawable getLocalImageDrawable() {
        return this.localImageDrawable;
    }

    public void setAspectRatio(double d) {
        this.aspectRatio = d;
    }

    public double getAspectRatio() {
        return this.aspectRatio;
    }

    public void setImageName(String str) {
        this.imageName = str;
    }

    public String getImageName() {
        return this.imageName;
    }

    public boolean isAnimated() {
        return this.animated;
    }

    public void setAnimated(boolean z) {
        this.animated = z;
    }

    public boolean isAsyncImageLoad() {
        return this.asyncImageLoad;
    }

    public void setAsyncImageLoad(boolean z) {
        this.asyncImageLoad = z;
    }

    public boolean isAutoRelease() {
        return this.autoRelease;
    }

    public void setAutoRelease(boolean z) {
        this.autoRelease = z;
    }

    public void setPlaceHolderName(String str) {
        this.placeHolderName = str;
    }

    public void setPlaceHolder(Drawable drawable) {
        this.placeHolder = drawable;
    }

    public String getPlaceHolderName() {
        return this.placeHolderName;
    }

    public Drawable getPlaceHolder() {
        return this.placeHolder;
    }
}
