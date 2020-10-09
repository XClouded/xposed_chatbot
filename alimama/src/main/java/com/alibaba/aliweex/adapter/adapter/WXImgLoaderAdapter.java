package com.alibaba.aliweex.adapter.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.IGodEyeStageAdapter;
import com.alibaba.aliweex.interceptor.phenix.PhenixTracker;
import com.alibaba.aliweex.utils.BlurTool;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.taobao.phenix.animate.AnimatedImageDrawable;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.PhenixTicket;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.util.ImageStrategyDecider;
import com.taobao.tao.util.TaobaoImageUrlStrategy;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.ui.view.WXImageView;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WXImgLoaderAdapter implements IWXImgLoaderAdapter {
    private static final int BIZ_ID = 70;
    public static final String TRUE = "true";
    private static final int WEEX_IMAG_KEY = -308;
    public static final String WX_ALLOW_RELEASE_DOMAIN = "allow_active_release";
    public static final String WX_IMAGE_RELEASE_CONFIG = "android_aliweex_image_release";
    /* access modifiers changed from: private */
    public static boolean phenixIsNewVersion = true;
    /* access modifiers changed from: private */
    public PhenixTracker tracker = null;

    @Deprecated
    public WXImgLoaderAdapter(WXSDKInstance wXSDKInstance) {
    }

    public WXImgLoaderAdapter() {
    }

    public void setImage(@Nullable String str, @Nullable ImageView imageView, WXImageQuality wXImageQuality, WXImageStrategy wXImageStrategy) {
        final ImageView imageView2 = imageView;
        final String str2 = str;
        final WXImageStrategy wXImageStrategy2 = wXImageStrategy;
        final WXImageQuality wXImageQuality2 = wXImageQuality;
        WXSDKManager.getInstance().postOnUiThread(new Runnable() {
            public void run() {
                String str;
                if (imageView2 != null) {
                    if (imageView2.getTag() instanceof PhenixTicket) {
                        ((PhenixTicket) imageView2.getTag()).cancel();
                    }
                    if (TextUtils.isEmpty(str2)) {
                        imageView2.setImageDrawable((Drawable) null);
                        return;
                    }
                    WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(wXImageStrategy2.instanceId);
                    if (sDKInstance != null) {
                        sDKInstance.getApmForInstance().actionLoadImg();
                        str = sDKInstance.getBundleUrl();
                    } else {
                        str = null;
                    }
                    String imageRealURL = WXImgLoaderAdapter.this.getImageRealURL(imageView2, str2, wXImageQuality2, wXImageStrategy2);
                    if (!TextUtils.isEmpty(wXImageStrategy2.placeHolder)) {
                        Phenix.instance().load(wXImageStrategy2.placeHolder).fetch();
                    }
                    if (WXEnvironment.isApkDebugable() && WXImgLoaderAdapter.this.tracker == null) {
                        PhenixTracker unused = WXImgLoaderAdapter.this.tracker = PhenixTracker.newInstance();
                    }
                    PhenixCreator addLoaderExtra = Phenix.instance().load(imageRealURL).secondary(wXImageStrategy2.placeHolder).limitSize(imageView2).releasableDrawable(true).addLoaderExtra(Constant.BUNDLE_BIZ_CODE, Integer.toString(70));
                    if (!TextUtils.isEmpty(str)) {
                        addLoaderExtra.addLoaderExtra("pageURL", str);
                    }
                    IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
                    if (configAdapter != null) {
                        String config = configAdapter.getConfig(WXImgLoaderAdapter.WX_IMAGE_RELEASE_CONFIG, WXImgLoaderAdapter.WX_ALLOW_RELEASE_DOMAIN, "");
                        if (TextUtils.isEmpty(config) || !TextUtils.equals("true", config)) {
                            addLoaderExtra.releasableDrawable(false);
                        }
                    }
                    WXImgLoaderAdapter.recordImageState("weex-image-start", str2, (String) null);
                    addLoaderExtra.succListener(new WXSucPhenixListener(wXImageStrategy2, imageView2, str2, WXImgLoaderAdapter.this.tracker));
                    addLoaderExtra.failListener(new WXFailPhenixListener(wXImageStrategy2, imageView2, str2, WXImgLoaderAdapter.this.tracker));
                    imageView2.setTag(WXImgLoaderAdapter.WEEX_IMAG_KEY, "START");
                    if (WXImgLoaderAdapter.this.tracker != null) {
                        HashMap hashMap = new HashMap();
                        if (WXEnvironment.isApkDebugable()) {
                            hashMap.put("quality", wXImageQuality2.name());
                            hashMap.put(Constant.BUNDLE_BIZ_CODE, String.valueOf(70));
                            hashMap.put("sharpen", String.valueOf(wXImageStrategy2.isSharpen));
                            hashMap.put("blurRaduis", String.valueOf(wXImageStrategy2.blurRadius));
                            hashMap.put("placeHolder", wXImageStrategy2.placeHolder);
                        }
                        WXImgLoaderAdapter.this.tracker.preRequest(addLoaderExtra, hashMap);
                    }
                    imageView2.setTag(addLoaderExtra.fetch());
                }
            }
        }, 0);
    }

    public String getImageRealURL(ImageView imageView, String str, WXImageQuality wXImageQuality, WXImageStrategy wXImageStrategy) {
        return (imageView == null || TextUtils.isEmpty(str) || wXImageQuality == WXImageQuality.ORIGINAL) ? str : decideUrl(imageView, str, wXImageStrategy.isSharpen, wXImageQuality);
    }

    public String decideUrl(ImageView imageView, String str, boolean z, WXImageQuality wXImageQuality) {
        int i;
        int i2;
        ImageStrategyConfig config = getConfig(z, wXImageQuality);
        if (config == null) {
            return str;
        }
        if (imageView.getLayoutParams() != null) {
            i = imageView.getLayoutParams().height;
            i2 = imageView.getLayoutParams().width;
        } else {
            i = imageView.getHeight();
            i2 = imageView.getWidth();
        }
        return ImageStrategyDecider.decideUrl(str, Integer.valueOf(i2), Integer.valueOf(i), config);
    }

    private ImageStrategyConfig getConfig(boolean z, WXImageQuality wXImageQuality) {
        ImageStrategyConfig.Builder newBuilderWithName = ImageStrategyConfig.newBuilderWithName(z ? ImageStrategyConfig.WEAPPSHARPEN : ImageStrategyConfig.WEAPP, 70);
        if (wXImageQuality != null) {
            switch (wXImageQuality) {
                case LOW:
                    newBuilderWithName.setFinalImageQuality(TaobaoImageUrlStrategy.ImageQuality.q50);
                    break;
                case NORMAL:
                    newBuilderWithName.setFinalImageQuality(TaobaoImageUrlStrategy.ImageQuality.q75);
                    break;
                case HIGH:
                    newBuilderWithName.setFinalImageQuality(TaobaoImageUrlStrategy.ImageQuality.q90);
                    break;
            }
        }
        return newBuilderWithName.build();
    }

    static class WXSucPhenixListener implements IPhenixListener<SuccPhenixEvent> {
        private static final String DRAWABLE_KEY = "drawable";
        private WXImageStrategy mImageStrategy;
        private WeakReference<ImageView> mImageViewRef;
        private String mUrl;
        private PhenixTracker phenixTracker;

        WXSucPhenixListener(WXImageStrategy wXImageStrategy, ImageView imageView, String str, PhenixTracker phenixTracker2) {
            this.mImageStrategy = wXImageStrategy;
            this.mImageViewRef = new WeakReference<>(imageView);
            this.mUrl = str;
            this.phenixTracker = phenixTracker2;
        }

        public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.mImageStrategy.instanceId);
            if (sDKInstance != null) {
                sDKInstance.getApmForInstance().actionLoadImgResult(true, (String) null);
            }
            final BitmapDrawable drawable = succPhenixEvent.getDrawable();
            final ImageView imageView = (ImageView) this.mImageViewRef.get();
            if (imageView == null) {
                return false;
            }
            String str = this.mUrl;
            StringBuilder sb = new StringBuilder();
            sb.append("drawable is null?");
            sb.append(drawable == null);
            WXImgLoaderAdapter.recordImageState("weex-image-success", str, sb.toString());
            imageView.setTag(WXImgLoaderAdapter.WEEX_IMAG_KEY, "END");
            if (drawable != null) {
                if ((imageView instanceof WXImageView) && (drawable instanceof AnimatedImageDrawable)) {
                    ((WXImageView) imageView).setImageDrawable(drawable, true);
                } else if (this.mImageStrategy.blurRadius <= 0) {
                    imageView.setImageDrawable(drawable);
                } else {
                    BitmapDrawable bitmapDrawable = drawable;
                    if (bitmapDrawable.getBitmap() != null) {
                        BlurTool.asyncBlur(bitmapDrawable.getBitmap(), this.mImageStrategy.blurRadius, new BlurTool.OnBlurCompleteListener() {
                            public void onBlurComplete(@NonNull Bitmap bitmap) {
                                try {
                                    imageView.setImageDrawable(new BitmapDrawable(imageView.getContext().getResources(), bitmap));
                                } catch (Exception e) {
                                    try {
                                        WXLogUtils.e(e.getMessage());
                                        imageView.setImageDrawable(drawable);
                                    } catch (Exception e2) {
                                        WXLogUtils.e(e2.getMessage());
                                    }
                                }
                            }
                        });
                    } else {
                        try {
                            imageView.setImageDrawable(drawable);
                        } catch (Exception e) {
                            WXLogUtils.e(e.getMessage());
                        }
                    }
                }
                if (!succPhenixEvent.isIntermediate() && this.mImageStrategy.getImageListener() != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(DRAWABLE_KEY, new WeakReference(drawable));
                    this.mImageStrategy.getImageListener().onImageFinish(this.mUrl, imageView, true, hashMap);
                }
            }
            if (this.phenixTracker != null) {
                this.phenixTracker.onSuccess(succPhenixEvent);
            }
            return false;
        }
    }

    static class WXFailPhenixListener implements IPhenixListener<FailPhenixEvent> {
        private WXImageStrategy mImageStrategy;
        private WeakReference<ImageView> mImageViewRef;
        private String mUrl;
        private PhenixTracker phenixTracker;

        WXFailPhenixListener(WXImageStrategy wXImageStrategy, ImageView imageView, String str, PhenixTracker phenixTracker2) {
            this.mImageStrategy = wXImageStrategy;
            this.mImageViewRef = new WeakReference<>(imageView);
            this.mUrl = str;
            this.phenixTracker = phenixTracker2;
        }

        public boolean onHappen(FailPhenixEvent failPhenixEvent) {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.mImageStrategy.instanceId);
            if (sDKInstance != null) {
                sDKInstance.getApmForInstance().actionLoadImgResult(false, String.valueOf(failPhenixEvent.getResultCode()));
            }
            ImageView imageView = (ImageView) this.mImageViewRef.get();
            if (imageView == null) {
                return false;
            }
            String str = "";
            if (WXImgLoaderAdapter.phenixIsNewVersion && failPhenixEvent != null) {
                try {
                    str = "resultCode:" + failPhenixEvent.getResultCode() + ',' + "httpCode:" + failPhenixEvent.getHttpCode() + ',' + "httpMessage" + failPhenixEvent.getHttpMessage();
                } catch (Throwable unused) {
                    boolean unused2 = WXImgLoaderAdapter.phenixIsNewVersion = false;
                }
            }
            WXImgLoaderAdapter.recordImageState("weex-image-Fail", this.mUrl, str);
            imageView.setTag(WXImgLoaderAdapter.WEEX_IMAG_KEY, "ERROR");
            if (this.mImageStrategy.getImageListener() != null) {
                this.mImageStrategy.getImageListener().onImageFinish(this.mUrl, imageView, false, (Map) null);
            }
            if (this.phenixTracker != null) {
                this.phenixTracker.onFail(failPhenixEvent);
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static void recordImageState(String str, String str2, String str3) {
        IGodEyeStageAdapter godEyeStageAdapter;
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if ((configAdapter == null || Boolean.valueOf(configAdapter.getConfig(WXInitConfigManager.WXAPM_CONFIG_GROUP, "recordImageState", "true")).booleanValue()) && (godEyeStageAdapter = AliWeex.getInstance().getGodEyeStageAdapter()) != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("url", str2);
            hashMap.put("msg", str3);
            godEyeStageAdapter.onStage(str, hashMap);
        }
    }
}
