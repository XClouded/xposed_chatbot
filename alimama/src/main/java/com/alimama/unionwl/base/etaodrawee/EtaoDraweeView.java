package com.alimama.unionwl.base.etaodrawee;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.annotation.Nullable;
import com.ali.user.mobile.login.model.LoginConstant;
import com.alibaba.wireless.security.SecExceptionCode;
import com.facebook.common.internal.Supplier;
import com.facebook.datasource.RetainingDataSourceSupplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;
import com.facebook.imagepipeline.request.ImageRequest;
import com.rd.animation.AbsAnimation;
import com.taobao.login4android.video.AudioRecordFunc;
import com.taobao.vessel.utils.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EtaoDraweeView extends SimpleDraweeView implements AImageViewBasicAction {
    private static final Pattern CDN_HOST_PATTERN = Pattern.compile("^((http|https|Http|Https)://)?([a-zA-Z0-9\\-]{0,64}\\.)*(tbcdn\\.cn|taobaocdn\\.com|alicdn\\.com|wimg\\.taobao\\.com)");
    private static final Pattern CDN_TAIL_PATTERN = Pattern.compile("_(([0-9]{1,10}x[0-9]{1,10}(?:xz|xc)?)|sum|m|b)?((Qq)[0-9]{1,2})?\\.jpg$");
    private static final Pattern CDN_TAIL_WEBP_PATTERN = Pattern.compile("_\\.webp$");
    private static final String[] EXCLUDE_CDN_KEYWORDS = {"avatar"};
    private static final String GIF_CDN_KEYWORDS = ".gif";
    private static int MAX_GIF = 1;
    /* access modifiers changed from: private */
    public static int MEMORY_DIVISER_DEFAULT = 6;
    private static final String TAG = "EtaoDraweeView";
    private static SparseArray<String> mCache = new SparseArray<>();
    /* access modifiers changed from: private */
    public static ImageConfigData mImageConfigData;
    /* access modifiers changed from: private */
    public static IETaoDraweeHelperAction sETaoDraweeHelperAction;
    private static int[] sSize = {16, 20, 24, 30, 32, 36, 40, 48, 50, 60, 64, 70, 72, 75, 80, 88, 90, 100, 110, 120, 125, 128, 130, 140, 142, 145, 150, 160, 170, RotationOptions.ROTATE_180, 190, 196, 200, 210, 220, 230, 234, 240, 250, RotationOptions.ROTATE_270, 284, 290, 292, 300, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 315, 320, 336, AbsAnimation.DEFAULT_ANIMATION_TIME, 360, 400, 430, 440, 460, 468, 480, 490, 540, 560, 570, 580, 600, AudioRecordFunc.FRAME_SIZE, 670, LoginConstant.RESULT_WINDWANE_CLOSEW, 728, 760, 960, 970, 1200, SecExceptionCode.SEC_ERROR_LBSRISK};
    private float attrCornerRadius;
    private boolean attrRoundBottomLeft;
    private boolean attrRoundBottomRight;
    private boolean attrRoundTopLeft;
    private boolean attrRoundTopRight;
    private Uri mCurUri = null;
    private boolean mDelayed = false;
    private int mHeight = 0;
    private int mWidth = 0;

    public EtaoDraweeView(Context context, GenericDraweeHierarchy genericDraweeHierarchy) {
        super(context, genericDraweeHierarchy);
    }

    public EtaoDraweeView(Context context) {
        super(context);
    }

    public EtaoDraweeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getAttrs(context, attributeSet);
    }

    public EtaoDraweeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getAttrs(context, attributeSet);
        handleAttrs();
    }

    public static void init(Context context, IETaoDraweeHelperAction iETaoDraweeHelperAction) {
        sETaoDraweeHelperAction = iETaoDraweeHelperAction;
        Fresco.initialize(context, ImagePipelineConfig.newBuilder(context).setNetworkFetcher(new HttpUrlConnectionNetworkFetcher() {
            public void onFetchCompletion(FetchState fetchState, int i) {
                if (i >= 512000) {
                    EtaoDraweeView.sETaoDraweeHelperAction.fetchNetImgSizeLargeMonitorAction(i);
                }
            }
        }).setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService("activity"))).build());
        Fresco.newDraweeControllerBuilder().setDataSourceSupplier(new RetainingDataSourceSupplier());
        mImageConfigData = sETaoDraweeHelperAction.getDraweeViewConfigData();
    }

    public static void clearCache() {
        try {
            Fresco.getImagePipeline().clearDiskCaches();
        } catch (Exception unused) {
        }
    }

    private void getAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AnyViewImageView);
        this.attrRoundBottomLeft = obtainStyledAttributes.getBoolean(R.styleable.AnyViewImageView_avRoundBottomLeft, false);
        this.attrRoundBottomRight = obtainStyledAttributes.getBoolean(R.styleable.AnyViewImageView_avRoundBottomRight, false);
        this.attrRoundTopLeft = obtainStyledAttributes.getBoolean(R.styleable.AnyViewImageView_avRoundTopLeft, false);
        this.attrRoundTopRight = obtainStyledAttributes.getBoolean(R.styleable.AnyViewImageView_avRoundTopRight, false);
        this.attrCornerRadius = obtainStyledAttributes.getDimension(R.styleable.AnyViewImageView_avRoundedCornerRadius, 0.0f);
        obtainStyledAttributes.recycle();
    }

    private void handleAttrs() {
        float f = 0.0f;
        float f2 = this.attrRoundBottomLeft ? this.attrCornerRadius : 0.0f;
        float f3 = this.attrRoundBottomRight ? this.attrCornerRadius : 0.0f;
        float f4 = this.attrRoundTopLeft ? this.attrCornerRadius : 0.0f;
        if (this.attrRoundTopRight) {
            f = this.attrCornerRadius;
        }
        ((GenericDraweeHierarchy) getHierarchy()).setRoundingParams(RoundingParams.fromCornersRadii(f4, f, f3, f2));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
        if (this.mDelayed) {
            this.mDelayed = false;
            setController(getController());
        }
    }

    public void setImageURI(Uri uri) {
        this.mCurUri = uri;
        super.setImageURI(uri);
    }

    public void setController(DraweeController draweeController) {
        if (getControllerBuilder() instanceof PipelineDraweeControllerBuilder) {
            PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder = (PipelineDraweeControllerBuilder) getControllerBuilder();
            if (pipelineDraweeControllerBuilder.getImageRequest() == null) {
                super.setController(draweeController);
                return;
            }
            Uri sourceUri = ((ImageRequest) pipelineDraweeControllerBuilder.getImageRequest()).getSourceUri();
            String uri = sourceUri.toString();
            if (!TextUtils.isEmpty(uri) && uri.startsWith("//")) {
                uri = Utils.HTTPS_SCHEMA + uri;
                sourceUri = Uri.parse(uri);
            }
            if (uri.startsWith("http") || uri.startsWith("https")) {
                if (this.mWidth == 0 || this.mHeight == 0) {
                    this.mDelayed = true;
                    return;
                } else {
                    draweeController = ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setCallerContext((Object) draweeController)).setUri(Uri.parse(getRemoteUrl(sourceUri.toString(), getWidth(), getHeight()))).setOldController(getController())).setAutoPlayAnimations(true)).build();
                }
            }
        }
        super.setController(draweeController);
    }

    public void setCenterInside() {
        ((GenericDraweeHierarchy) getHierarchy()).setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
    }

    public void setAnyImageRes(Context context, int i) {
        setAnyImageURI(Uri.parse("res://" + context.getPackageName() + "/" + i));
    }

    public void setAnyImageUrl(String str) {
        if (str != null) {
            if (!TextUtils.isEmpty(str) && !str.startsWith("http")) {
                str = Utils.HTTPS_SCHEMA + str;
            }
            setAnyImageURI(Uri.parse(str));
        }
    }

    public void setAnyImageURI(Uri uri) {
        setImageURI(uri);
    }

    public void setAnyImageURI(Uri uri, @Nullable Object obj) {
        this.mCurUri = uri;
        setImageURI(uri, obj);
    }

    public void setRoundedCorners(float f) {
        ((GenericDraweeHierarchy) getHierarchy()).setRoundingParams(RoundingParams.fromCornersRadius(f));
    }

    public void setRoundedCorners(float f, float f2, float f3, float f4) {
        ((GenericDraweeHierarchy) getHierarchy()).setRoundingParams(RoundingParams.fromCornersRadii(f, f2, f3, f4));
    }

    public void setOverlay(Drawable drawable) {
        ((GenericDraweeHierarchy) getHierarchy()).setControllerOverlay(drawable);
    }

    public String getCurrentUrl() {
        if (this.mCurUri != null) {
            return this.mCurUri.toString();
        }
        return null;
    }

    public static String getRemoteUrl(String str, int i, int i2) {
        Uri parse;
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (!CDN_HOST_PATTERN.matcher(trim).find()) {
            return trim;
        }
        if (trim.contains(GIF_CDN_KEYWORDS) && (parse = Uri.parse(trim)) != null && parse.isHierarchical() && "true".equalsIgnoreCase(parse.getQueryParameter("animation"))) {
            return trim;
        }
        for (String contains : EXCLUDE_CDN_KEYWORDS) {
            if (trim.contains(contains)) {
                return trim;
            }
        }
        Matcher matcher = CDN_TAIL_WEBP_PATTERN.matcher(trim);
        if (matcher.find()) {
            trim = matcher.replaceFirst("");
        }
        Matcher matcher2 = CDN_TAIL_PATTERN.matcher(trim);
        if (matcher2.find() && !"_.jpg".equals(matcher2.group())) {
            trim = matcher2.replaceFirst("");
        }
        int max = Math.max(i, i2);
        if (mCache.get(max) == null) {
            String str2 = "";
            int i3 = 0;
            while (true) {
                if (i3 >= sSize.length) {
                    break;
                } else if (sSize[i3] >= max) {
                    str2 = String.format("_%sx%s.jpg", new Object[]{Integer.valueOf(sSize[i3]), Integer.valueOf(sSize[i3])});
                    break;
                } else {
                    i3++;
                }
            }
            mCache.put(max, str2);
        }
        return trim + mCache.get(max);
    }

    static class LollipopBitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
        private ActivityManager activityManager;

        public LollipopBitmapMemoryCacheParamsSupplier(ActivityManager activityManager2) {
            this.activityManager = activityManager2;
        }

        public MemoryCacheParams get() {
            if (Build.VERSION.SDK_INT >= 21) {
                return new MemoryCacheParams(getMaxCacheSize(), 1, 1, 1, 1);
            }
            return new MemoryCacheParams(getMaxCacheSize(), 256, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        private int getMaxCacheSize() {
            int min = Math.min(this.activityManager.getMemoryClass() * 1048576, Integer.MAX_VALUE);
            if (min < 33554432) {
                return 4194304;
            }
            if (min < 67108864) {
                return 6291456;
            }
            if (Build.VERSION.SDK_INT <= 9) {
                return 8388608;
            }
            int frescoMemory = min / getFrescoMemory();
            EtaoDraweeView.sETaoDraweeHelperAction.triggerFrescoMaxMemMonitorAction();
            return frescoMemory;
        }

        private int getFrescoMemory() {
            int access$100 = EtaoDraweeView.MEMORY_DIVISER_DEFAULT;
            if (EtaoDraweeView.mImageConfigData == null) {
                return access$100;
            }
            int i = EtaoDraweeView.mImageConfigData.frescoMemory;
            return i > EtaoDraweeView.MEMORY_DIVISER_DEFAULT ? i : EtaoDraweeView.MEMORY_DIVISER_DEFAULT;
        }
    }
}
