package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DAttrUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class DImageViewConstructor extends DinamicViewAdvancedConstructor {
    public static final String HEIGHT_LIMIT = "heightLimit";
    private static final String IMAGEVIEW_ASPECT_RATIO = "dAspectRatio";
    private static final String IMAGEVIEW_IMAGE_URL = "dImageUrl";
    private static final String IMAGEVIEW_LOCAL_IMAGE = "dImage";
    private static final String IMAGEVIEW_LOCAL_IMAGE_NAME = "dImageName";
    private static final String IMAGEVIEW_SCALE_TYPE = "dScaleType";
    private static final String IMAGEVIEW_SCALE_TYPE_CENTER_CROP = "centerCrop";
    private static final String IMAGEVIEW_SCALE_TYPE_FIT_CENTER = "fitCenter";
    private static final String IMAGEVIEW_SCALE_TYPE_FIT_XY = "fitXY";
    public static final String TAG = "DImageViewConstructor";
    public static final String WIDTH_LIMIT = "widthLimit";
    private DXWebImageInterface dxWebImageInterface;

    public interface DXWebImageInterface {
        ImageView buildView(Context context);

        void setImage(ImageView imageView, String str, ImageOption imageOption);
    }

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        if (this.dxWebImageInterface == null) {
            return new ImageView(context);
        }
        return this.dxWebImageInterface.buildView(context);
    }

    private void setImage(ImageView imageView, String str, ImageOption imageOption) {
        if (this.dxWebImageInterface != null) {
            this.dxWebImageInterface.setImage(imageView, str, imageOption);
        }
    }

    public void setDxWebImageInterface(DXWebImageInterface dXWebImageInterface) {
        this.dxWebImageInterface = dXWebImageInterface;
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        super.setAttributes(view, map, arrayList, dinamicParams);
        ImageView imageView = (ImageView) view;
        if (arrayList.contains("dImage")) {
            setLocalImage(imageView, (Drawable) map.get("dImage"));
        }
        if (arrayList.contains(IMAGEVIEW_LOCAL_IMAGE_NAME)) {
            setLocalRes(imageView, (String) map.get(IMAGEVIEW_LOCAL_IMAGE_NAME));
        }
        if (arrayList.contains("dScaleType")) {
            setImageScaleType(imageView, (String) map.get("dScaleType"));
        }
        String str = (String) map.get(DAttrConstant.VIEW_WIDTH);
        String str2 = (String) map.get(DAttrConstant.VIEW_HEIGHT);
        ImageOption imageOption = new ImageOption();
        imageOption.module = dinamicParams.getModule();
        if (TextUtils.equals(str, DAttrConstant.MATCH_CONTENT) && !TextUtils.equals(str2, DAttrConstant.MATCH_CONTENT)) {
            imageOption.sizeType = "heightLimit";
            boolean unused = imageOption.isNeedLimitSize = true;
            imageView.setAdjustViewBounds(true);
        } else if (!TextUtils.equals(str, DAttrConstant.MATCH_CONTENT) && TextUtils.equals(str2, DAttrConstant.MATCH_CONTENT)) {
            imageOption.sizeType = "widthLimit";
            boolean unused2 = imageOption.isNeedLimitSize = true;
            imageView.setAdjustViewBounds(true);
        }
        if (arrayList.contains(IMAGEVIEW_ASPECT_RATIO)) {
            setAspectRatio(imageView, str, str2, (String) map.get(IMAGEVIEW_ASPECT_RATIO), imageOption);
            if (imageOption.isNeedLimitSize()) {
                setImageScaleType(imageView, (String) map.get("dScaleType"));
            }
        }
        if (arrayList.contains(DAttrConstant.VIEW_CORNER_RADIUS)) {
            imageOption.cornerRadius = (String) map.get(DAttrConstant.VIEW_CORNER_RADIUS);
            boolean unused3 = imageOption.isNeedClipRadius = true;
        }
        if (arrayList.contains(DAttrConstant.VIEW_BORDER_COLOR)) {
            imageOption.borderColor = (String) map.get(DAttrConstant.VIEW_BORDER_COLOR);
            boolean unused4 = imageOption.isNeedBorderColor = true;
        }
        if (arrayList.contains(DAttrConstant.VIEW_BORDER_WIDTH)) {
            imageOption.borderWidth = (String) map.get(DAttrConstant.VIEW_BORDER_WIDTH);
            boolean unused5 = imageOption.isNeedBorderWidth = true;
        }
        if (arrayList.contains("dImageUrl")) {
            boolean unused6 = imageOption.isNeedSetImageUrl = true;
        }
        setImage(imageView, (String) map.get("dImageUrl"), imageOption);
    }

    private void setLocalImage(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    /* access modifiers changed from: protected */
    public void setAspectRatio(ImageView imageView, String str, String str2, String str3, ImageOption imageOption) {
        boolean z = !TextUtils.equals(str, DAttrConstant.MATCH_CONTENT) && TextUtils.equals(str2, DAttrConstant.MATCH_CONTENT);
        if (z || (TextUtils.equals(str, DAttrConstant.MATCH_CONTENT) && !TextUtils.equals(str2, DAttrConstant.MATCH_CONTENT))) {
            double d = -1.0d;
            try {
                if (!TextUtils.isEmpty(str3)) {
                    d = Double.valueOf(str3).doubleValue();
                }
            } catch (Throwable unused) {
            }
            if (z) {
                if (d > 0.0d) {
                    imageOption.ratio = (float) (1.0d / d);
                    imageOption.orientation = 0;
                    boolean unused2 = imageOption.isNeedRatio = true;
                } else if (imageView.getLayoutParams() != null) {
                    imageView.getLayoutParams().height = 0;
                }
            } else if (d > 0.0d) {
                imageOption.ratio = (float) d;
                imageOption.orientation = 1;
                boolean unused3 = imageOption.isNeedRatio = true;
            } else if (imageView.getLayoutParams() != null) {
                imageView.getLayoutParams().width = 0;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setLocalRes(ImageView imageView, String str) {
        imageView.setTag(DinamicTagKey.TAG_CURRENT_IMAGE_NAME, str);
        if (str == null) {
            imageView.setImageDrawable((Drawable) null);
            imageView.setTag(DinamicTagKey.TAG_IMAGE_NAME, (Object) null);
        } else if (!str.equals((String) imageView.getTag(DinamicTagKey.TAG_IMAGE_NAME))) {
            new LoadDrawableTask(imageView, str).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
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
            Drawable drawable;
            int drawableId = getDrawableId(this.context, this.localImageName);
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
                Log.e(DImageViewConstructor.TAG, "Get layout parser exception", e);
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

        private int getDrawableId(Context context2, String str) {
            if (context2 == null || TextUtils.isEmpty(str)) {
                return 0;
            }
            try {
                return context2.getResources().getIdentifier(str, "drawable", context2.getPackageName());
            } catch (Exception e) {
                Log.e(DImageViewConstructor.TAG, "getDrawableId exception", e);
                return 0;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setImageScaleType(ImageView imageView, String str) {
        if (TextUtils.isEmpty(str)) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else if (IMAGEVIEW_SCALE_TYPE_FIT_XY.equals(str)) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if (IMAGEVIEW_SCALE_TYPE_FIT_CENTER.equals(str)) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else if (IMAGEVIEW_SCALE_TYPE_CENTER_CROP.equals(str)) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public void setBackground(View view, String str, String str2, String str3, String str4) {
        view.setBackgroundColor(DAttrUtils.parseColor(str4, 0));
    }

    public static class ImageOption {
        public String borderColor;
        public String borderWidth;
        public String cornerRadius;
        /* access modifiers changed from: private */
        public boolean isNeedBorderColor;
        /* access modifiers changed from: private */
        public boolean isNeedBorderWidth;
        /* access modifiers changed from: private */
        public boolean isNeedClipRadius;
        /* access modifiers changed from: private */
        public boolean isNeedLimitSize;
        /* access modifiers changed from: private */
        public boolean isNeedRatio;
        /* access modifiers changed from: private */
        public boolean isNeedSetImageUrl;
        public String module;
        public int orientation;
        public float ratio;
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

        public boolean isNeedRatio() {
            return this.isNeedRatio;
        }
    }
}
