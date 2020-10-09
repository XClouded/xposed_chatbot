package com.taobao.weex.ui.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXImageSharpen;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXImageView;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.utils.ImageDrawable;
import com.taobao.weex.utils.ImgURIUtil;
import com.taobao.weex.utils.SingleFunctionParser;
import com.taobao.weex.utils.WXDomUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewToImageUtil;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component(lazyload = false)
public class WXImage extends WXComponent<ImageView> {
    private static SingleFunctionParser.FlatMapper<Integer> BLUR_RADIUS_MAPPER = new SingleFunctionParser.FlatMapper<Integer>() {
        public Integer map(String str) {
            return WXUtils.getInteger(str, 0);
        }
    };
    public static final String ERRORDESC = "errorDesc";
    public static final String SUCCEED = "success";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    private boolean mAutoRecycle;
    private int mBlurRadius;
    private String mSrc;
    private String preImgUrlStr;

    public interface Measurable {
        int getNaturalHeight();

        int getNaturalWidth();
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXImage(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXImage(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXImage(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.mAutoRecycle = true;
        this.preImgUrlStr = "";
    }

    /* access modifiers changed from: protected */
    public ImageView initComponentHostView(@NonNull Context context) {
        WXImageView wXImageView = new WXImageView(context);
        wXImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (Build.VERSION.SDK_INT >= 16) {
            wXImageView.setCropToPadding(true);
        }
        wXImageView.holdComponent(this);
        return wXImageView;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r5, java.lang.Object r6) {
        /*
            r4 = this;
            int r0 = r5.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -1285653259: goto L_0x003c;
                case -1274492040: goto L_0x0032;
                case -934437708: goto L_0x0028;
                case 114148: goto L_0x001e;
                case 1249477412: goto L_0x0014;
                case 2049757303: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x0046
        L_0x000a:
            java.lang.String r0 = "resizeMode"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0046
            r0 = 0
            goto L_0x0047
        L_0x0014:
            java.lang.String r0 = "imageQuality"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0046
            r0 = 3
            goto L_0x0047
        L_0x001e:
            java.lang.String r0 = "src"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0046
            r0 = 2
            goto L_0x0047
        L_0x0028:
            java.lang.String r0 = "resize"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0046
            r0 = 1
            goto L_0x0047
        L_0x0032:
            java.lang.String r0 = "filter"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0046
            r0 = 5
            goto L_0x0047
        L_0x003c:
            java.lang.String r0 = "autoBitmapRecycle"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0046
            r0 = 4
            goto L_0x0047
        L_0x0046:
            r0 = -1
        L_0x0047:
            r3 = 0
            switch(r0) {
                case 0: goto L_0x00a9;
                case 1: goto L_0x009f;
                case 2: goto L_0x0095;
                case 3: goto L_0x0094;
                case 4: goto L_0x006a;
                case 5: goto L_0x0050;
                default: goto L_0x004b;
            }
        L_0x004b:
            boolean r5 = super.setProperty(r5, r6)
            return r5
        L_0x0050:
            if (r6 == 0) goto L_0x005c
            boolean r5 = r6 instanceof java.lang.String
            if (r5 == 0) goto L_0x005c
            java.lang.String r6 = (java.lang.String) r6
            int r1 = r4.parseBlurRadius(r6)
        L_0x005c:
            java.lang.String r5 = r4.mSrc
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x0069
            java.lang.String r5 = r4.mSrc
            r4.setBlurRadius(r5, r1)
        L_0x0069:
            return r2
        L_0x006a:
            boolean r5 = r4.mAutoRecycle
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r5)
            boolean r5 = r5.booleanValue()
            r4.mAutoRecycle = r5
            boolean r5 = r4.mAutoRecycle
            if (r5 != 0) goto L_0x0093
            com.taobao.weex.WXSDKInstance r5 = r4.getInstance()
            if (r5 == 0) goto L_0x0093
            com.taobao.weex.WXSDKInstance r5 = r4.getInstance()
            com.taobao.weex.performance.WXInstanceApm r5 = r5.getApmForInstance()
            java.lang.String r6 = "wxImgUnRecycleCount"
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r5.updateDiffStats(r6, r0)
        L_0x0093:
            return r2
        L_0x0094:
            return r2
        L_0x0095:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x009e
            r4.setSrc(r5)
        L_0x009e:
            return r2
        L_0x009f:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x00a8
            r4.setResize(r5)
        L_0x00a8:
            return r2
        L_0x00a9:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x00b2
            r4.setResizeMode(r5)
        L_0x00b2:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXImage.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    public void refreshData(WXComponent wXComponent) {
        super.refreshData(wXComponent);
        if (wXComponent instanceof WXImage) {
            setSrc(wXComponent.getAttrs().getImageSrc());
        }
    }

    @WXComponentProp(name = "resizeMode")
    public void setResizeMode(String str) {
        ((ImageView) getHostView()).setScaleType(getResizeMode(str));
        ((ImageView) getHostView()).setImageDrawable(((ImageView) getHostView()).getDrawable());
    }

    /* access modifiers changed from: protected */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public ImageView.ScaleType getResizeMode(String str) {
        ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
        if (TextUtils.isEmpty(str)) {
            return scaleType;
        }
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != -1881872635) {
            if (hashCode != 94852023) {
                if (hashCode == 951526612 && str.equals("contain")) {
                    c = 1;
                }
            } else if (str.equals("cover")) {
                c = 0;
            }
        } else if (str.equals("stretch")) {
            c = 2;
        }
        switch (c) {
            case 0:
                return ImageView.ScaleType.CENTER_CROP;
            case 1:
                return ImageView.ScaleType.FIT_CENTER;
            case 2:
                return ImageView.ScaleType.FIT_XY;
            default:
                return scaleType;
        }
    }

    @WXComponentProp(name = "resize")
    public void setResize(String str) {
        setResizeMode(str);
    }

    private void setLocalSrc(Uri uri) {
        ImageView imageView;
        Drawable drawableFromLoaclSrc = ImgURIUtil.getDrawableFromLoaclSrc(getContext(), uri);
        if (drawableFromLoaclSrc != null && (imageView = (ImageView) getHostView()) != null) {
            imageView.setImageDrawable(drawableFromLoaclSrc);
        }
    }

    @WXComponentProp(name = "src")
    public void setSrc(String str) {
        if (getInstance().getImageNetworkHandler() != null) {
            String fetchLocal = getInstance().getImageNetworkHandler().fetchLocal(str);
            if (!TextUtils.isEmpty(fetchLocal)) {
                str = fetchLocal;
            }
        }
        if (str != null) {
            ImageView imageView = (ImageView) getHostView();
            if (!"".equals(str) || imageView == null) {
                if (!(imageView == null || imageView.getDrawable() == null || TextUtils.equals(this.mSrc, str))) {
                    imageView.setImageDrawable((Drawable) null);
                }
                this.mSrc = str;
                Uri rewriteUri = getInstance().rewriteUri(Uri.parse(str), "image");
                if ("local".equals(rewriteUri.getScheme())) {
                    setLocalSrc(rewriteUri);
                } else {
                    setRemoteSrc(rewriteUri, parseBlurRadius(getStyles().getBlur()));
                }
            } else {
                imageView.setImageDrawable((Drawable) null);
            }
        }
    }

    private void setBlurRadius(@NonNull String str, int i) {
        if (getInstance() != null && i != this.mBlurRadius) {
            Uri rewriteUri = getInstance().rewriteUri(Uri.parse(str), "image");
            if (!"local".equals(rewriteUri.getScheme())) {
                setRemoteSrc(rewriteUri, i);
            }
        }
    }

    private int parseBlurRadius(@Nullable String str) {
        if (str == null) {
            return 0;
        }
        try {
            List parse = new SingleFunctionParser(str, BLUR_RADIUS_MAPPER).parse(Constants.Event.BLUR);
            if (parse == null || parse.isEmpty()) {
                return 0;
            }
            return ((Integer) parse.get(0)).intValue();
        } catch (Exception unused) {
            return 0;
        }
    }

    public void recycled() {
        super.recycled();
        if (getInstance().getImgLoaderAdapter() != null) {
            getInstance().getImgLoaderAdapter().setImage((String) null, (ImageView) this.mHost, (WXImageQuality) null, (WXImageStrategy) null);
        } else if (!WXEnvironment.isApkDebugable()) {
            WXLogUtils.e("Error getImgLoaderAdapter() == null");
        } else {
            throw new WXRuntimeException("getImgLoaderAdapter() == null");
        }
    }

    public void autoReleaseImage() {
        if (this.mAutoRecycle && getHostView() != null && getInstance().getImgLoaderAdapter() != null) {
            getInstance().getImgLoaderAdapter().setImage((String) null, (ImageView) this.mHost, (WXImageQuality) null, (WXImageStrategy) null);
        }
    }

    public void autoRecoverImage() {
        if (this.mAutoRecycle) {
            setSrc(this.mSrc);
        }
    }

    private void setRemoteSrc(Uri uri, int i) {
        WXImageStrategy wXImageStrategy = new WXImageStrategy(getInstanceId());
        boolean z = true;
        wXImageStrategy.isClipping = true;
        if (getAttrs().getImageSharpen() != WXImageSharpen.SHARPEN) {
            z = false;
        }
        wXImageStrategy.isSharpen = z;
        wXImageStrategy.blurRadius = Math.max(0, i);
        this.mBlurRadius = i;
        String uri2 = uri.toString();
        wXImageStrategy.setImageListener(new MyImageListener(this, uri2));
        String str = null;
        if (getAttrs().containsKey("placeholder")) {
            str = (String) getAttrs().get("placeholder");
        } else if (getAttrs().containsKey("placeHolder")) {
            str = (String) getAttrs().get("placeHolder");
        }
        if (str != null) {
            wXImageStrategy.placeHolder = getInstance().rewriteUri(Uri.parse(str), "image").toString();
        }
        wXImageStrategy.instanceId = getInstanceId();
        IWXImgLoaderAdapter imgLoaderAdapter = getInstance().getImgLoaderAdapter();
        if (imgLoaderAdapter != null) {
            imgLoaderAdapter.setImage(uri2, (ImageView) getHostView(), getImageQuality(), wXImageStrategy);
        }
    }

    /* access modifiers changed from: protected */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public WXImageQuality getImageQuality() {
        return getAttrs().getImageQuality();
    }

    /* access modifiers changed from: protected */
    public void onFinishLayout() {
        super.onFinishLayout();
        updateBorderRadius();
    }

    public void updateProperties(Map<String, Object> map) {
        super.updateProperties(map);
        updateBorderRadius();
    }

    private void updateBorderRadius() {
        float[] fArr;
        if (getHostView() instanceof WXImageView) {
            WXImageView wXImageView = (WXImageView) getHostView();
            BorderDrawable borderDrawable = WXViewUtils.getBorderDrawable(getHostView());
            if (borderDrawable != null) {
                fArr = borderDrawable.getBorderInnerRadius(new RectF(0.0f, 0.0f, WXDomUtils.getContentWidth(this), WXDomUtils.getContentHeight(this)));
            } else {
                fArr = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            }
            wXImageView.setBorderRadius(fArr);
            if (wXImageView.getDrawable() instanceof ImageDrawable) {
                ImageDrawable imageDrawable = (ImageDrawable) wXImageView.getDrawable();
                if (!Arrays.equals(imageDrawable.getCornerRadii(), fArr)) {
                    imageDrawable.setCornerRadii(fArr);
                }
            }
        }
    }

    @JSMethod(uiThread = false)
    public void save(final JSCallback jSCallback) {
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0 && (getContext() instanceof Activity)) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (jSCallback != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("success", false);
                hashMap.put(ERRORDESC, "Permission denied: android.permission.WRITE_EXTERNAL_STORAGE");
                jSCallback.invoke(hashMap);
            }
        } else if (this.mHost == null) {
            if (jSCallback != null) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("success", false);
                hashMap2.put(ERRORDESC, "Image component not initialized");
                jSCallback.invoke(hashMap2);
            }
        } else if (this.mSrc != null && !this.mSrc.equals("")) {
            WXViewToImageUtil.generateImage(this.mHost, 0, -460552, new WXViewToImageUtil.OnImageSavedCallback() {
                public void onSaveSucceed(String str) {
                    if (jSCallback != null) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("success", true);
                        jSCallback.invoke(hashMap);
                    }
                }

                public void onSaveFailed(String str) {
                    if (jSCallback != null) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("success", false);
                        hashMap.put(WXImage.ERRORDESC, str);
                        jSCallback.invoke(hashMap);
                    }
                }
            });
        } else if (jSCallback != null) {
            HashMap hashMap3 = new HashMap();
            hashMap3.put("success", false);
            hashMap3.put(ERRORDESC, "Image does not have the correct src");
            jSCallback.invoke(hashMap3);
        }
    }

    /* access modifiers changed from: private */
    public void monitorImgSize(ImageView imageView, String str) {
        WXSDKInstance instance;
        String str2 = str;
        if (imageView != null && (instance = getInstance()) != null) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            Drawable drawable = imageView.getDrawable();
            if (layoutParams != null && drawable != null) {
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int intrinsicWidth = drawable.getIntrinsicWidth();
                if (!this.preImgUrlStr.equals(str2)) {
                    this.preImgUrlStr = str2;
                    if (intrinsicHeight > 1081 && intrinsicWidth > 721) {
                        instance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_LARGE_IMG_COUNT, 1.0d);
                        if (WXAnalyzerDataTransfer.isOpenPerformance) {
                            WXAnalyzerDataTransfer.transferPerformance(getInstanceId(), "details", WXInstanceApm.KEY_PAGE_STATS_LARGE_IMG_COUNT, intrinsicWidth + "*" + intrinsicHeight + "," + str2);
                        }
                    }
                    long j = (long) (intrinsicHeight * intrinsicWidth);
                    long measuredHeight = (long) (imageView.getMeasuredHeight() * imageView.getMeasuredWidth());
                    if (measuredHeight != 0) {
                        double d = (double) j;
                        double d2 = (double) measuredHeight;
                        Double.isNaN(d);
                        Double.isNaN(d2);
                        if (d / d2 > 1.2d && j - measuredHeight > 1600) {
                            instance.getWXPerformance().wrongImgSizeCount += 1.0d;
                            instance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_WRONG_IMG_SIZE_COUNT, 1.0d);
                            if (WXAnalyzerDataTransfer.isOpenPerformance) {
                                WXAnalyzerDataTransfer.transferPerformance(getInstanceId(), "details", WXInstanceApm.KEY_PAGE_STATS_WRONG_IMG_SIZE_COUNT, String.format(Locale.ROOT, "imgSize:[%d,%d],viewSize:[%d,%d],urL:%s", new Object[]{Integer.valueOf(intrinsicWidth), Integer.valueOf(intrinsicHeight), Integer.valueOf(imageView.getMeasuredWidth()), Integer.valueOf(imageView.getMeasuredHeight()), str2}));
                            }
                        }
                    }
                }
            }
        }
    }

    public void destroy() {
        if ((getHostView() instanceof WXImageView) && getInstance().getImgLoaderAdapter() != null) {
            getInstance().getImgLoaderAdapter().setImage((String) null, (ImageView) this.mHost, (WXImageQuality) null, (WXImageStrategy) null);
        }
        super.destroy();
    }

    public static class MyImageListener implements WXImageStrategy.ImageListener {
        private String rewritedStr;
        private WeakReference<WXImage> wxImageWeakReference;

        MyImageListener(WXImage wXImage, String str) {
            this.wxImageWeakReference = new WeakReference<>(wXImage);
            this.rewritedStr = str;
        }

        public void onImageFinish(String str, ImageView imageView, boolean z, Map map) {
            WXImage wXImage = (WXImage) this.wxImageWeakReference.get();
            if (wXImage != null) {
                if (wXImage.getEvents().contains("load")) {
                    HashMap hashMap = new HashMap();
                    HashMap hashMap2 = new HashMap(2);
                    if (imageView == null || !(imageView instanceof Measurable)) {
                        hashMap2.put("naturalWidth", 0);
                        hashMap2.put("naturalHeight", 0);
                    } else {
                        Measurable measurable = (Measurable) imageView;
                        hashMap2.put("naturalWidth", Integer.valueOf(measurable.getNaturalWidth()));
                        hashMap2.put("naturalHeight", Integer.valueOf(measurable.getNaturalHeight()));
                    }
                    if (wXImage.containsEvent("load")) {
                        hashMap.put("success", Boolean.valueOf(z));
                        hashMap.put("size", hashMap2);
                        wXImage.fireEvent("load", hashMap);
                    }
                }
                wXImage.monitorImgSize(imageView, this.rewritedStr);
            }
        }
    }
}
