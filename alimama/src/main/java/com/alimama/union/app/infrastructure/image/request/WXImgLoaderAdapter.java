package com.alimama.union.app.infrastructure.image.request;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.utils.BlurTool;
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
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.ui.view.WXImageView;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WXImgLoaderAdapter implements IWXImgLoaderAdapter {
    private ImageStrategyConfig mConfig;

    public void setImage(String str, ImageView imageView, WXImageQuality wXImageQuality, WXImageStrategy wXImageStrategy) {
        final ImageView imageView2 = imageView;
        final String str2 = str;
        final WXImageQuality wXImageQuality2 = wXImageQuality;
        final WXImageStrategy wXImageStrategy2 = wXImageStrategy;
        WXSDKManager.getInstance().postOnUiThread(new Runnable() {
            public void run() {
                String str;
                if (imageView2 != null) {
                    if (imageView2.getTag() instanceof PhenixTicket) {
                        Phenix.instance().cancel((PhenixTicket) imageView2.getTag());
                    }
                    if (TextUtils.isEmpty(str2)) {
                        imageView2.setImageDrawable((Drawable) null);
                        return;
                    }
                    if (!str2.endsWith(".gif")) {
                        str = WXImgLoaderAdapter.this.getImageRealURL(imageView2, str2, wXImageQuality2, wXImageStrategy2);
                    } else {
                        str = str2;
                    }
                    if (!TextUtils.isEmpty(wXImageStrategy2.placeHolder)) {
                        Phenix.instance().load(wXImageStrategy2.placeHolder).fetch();
                    }
                    PhenixCreator addLoaderExtra = Phenix.instance().load(str).secondary(wXImageStrategy2.placeHolder).limitSize(imageView2).notSharedDrawable(true).addLoaderExtra(Constant.BUNDLE_BIZ_CODE, "70");
                    addLoaderExtra.succListener(new WXSucPhenixListener(wXImageStrategy2, imageView2, str2));
                    addLoaderExtra.failListener(new WXFailPhenixListener(wXImageStrategy2, imageView2, str2));
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
        this.mConfig = getConfig(z, wXImageQuality);
        if (this.mConfig == null) {
            return str;
        }
        if (imageView.getLayoutParams() != null) {
            i = imageView.getLayoutParams().height;
            i2 = imageView.getLayoutParams().width;
        } else {
            i = imageView.getHeight();
            i2 = imageView.getWidth();
        }
        return ImageStrategyDecider.decideUrl(str, Integer.valueOf(i2), Integer.valueOf(i), this.mConfig);
    }

    private ImageStrategyConfig getConfig(boolean z, WXImageQuality wXImageQuality) {
        String str;
        TaobaoImageUrlStrategy.ImageQuality imageQuality = TaobaoImageUrlStrategy.ImageQuality.q75;
        if (wXImageQuality == null || wXImageQuality == WXImageQuality.NORMAL) {
            imageQuality = TaobaoImageUrlStrategy.ImageQuality.q75;
        } else if (wXImageQuality == WXImageQuality.LOW) {
            imageQuality = TaobaoImageUrlStrategy.ImageQuality.q50;
        } else if (wXImageQuality == WXImageQuality.HIGH) {
            imageQuality = TaobaoImageUrlStrategy.ImageQuality.q90;
        } else if (wXImageQuality == WXImageQuality.ORIGINAL) {
            imageQuality = TaobaoImageUrlStrategy.ImageQuality.non;
        }
        if (z) {
            str = ImageStrategyConfig.WEAPPSHARPEN;
        } else {
            str = ImageStrategyConfig.WEAPP;
        }
        return ImageStrategyConfig.newBuilderWithName(str, 70).setFinalImageQuality(imageQuality).build();
    }

    static class WXFailPhenixListener implements IPhenixListener<FailPhenixEvent> {
        private WXImageStrategy mImageStrategy;
        private ImageView mImageView;
        private String mUrl;

        WXFailPhenixListener(WXImageStrategy wXImageStrategy, ImageView imageView, String str) {
            this.mImageStrategy = wXImageStrategy;
            this.mImageView = imageView;
            this.mUrl = str;
        }

        public boolean onHappen(FailPhenixEvent failPhenixEvent) {
            if (this.mImageStrategy.getImageListener() != null) {
                this.mImageStrategy.getImageListener().onImageFinish(this.mUrl, this.mImageView, false, (Map) null);
            }
            return false;
        }
    }

    static class WXSucPhenixListener implements IPhenixListener<SuccPhenixEvent> {
        private static final String DRAWABLE_KEY = "drawable";
        private WXImageStrategy mImageStrategy;
        /* access modifiers changed from: private */
        public ImageView mImageView;
        private String mUrl;

        WXSucPhenixListener(WXImageStrategy wXImageStrategy, ImageView imageView, String str) {
            this.mImageStrategy = wXImageStrategy;
            this.mImageView = imageView;
            this.mUrl = str;
        }

        public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
            final BitmapDrawable drawable = succPhenixEvent.getDrawable();
            if (drawable == null) {
                return false;
            }
            if (!succPhenixEvent.isIntermediate() && this.mImageStrategy.getImageListener() != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(DRAWABLE_KEY, new WeakReference(drawable));
                this.mImageStrategy.getImageListener().onImageFinish(this.mUrl, this.mImageView, true, hashMap);
            }
            if ((this.mImageView instanceof WXImageView) && (drawable instanceof AnimatedImageDrawable)) {
                ((WXImageView) this.mImageView).setImageDrawable(drawable, true);
                return false;
            } else if (this.mImageStrategy.blurRadius <= 0) {
                this.mImageView.setImageDrawable(drawable);
                return false;
            } else {
                BlurTool.asyncBlur(drawable.getBitmap(), this.mImageStrategy.blurRadius, new BlurTool.OnBlurCompleteListener() {
                    public void onBlurComplete(@NonNull Bitmap bitmap) {
                        try {
                            WXSucPhenixListener.this.mImageView.setImageDrawable(new BitmapDrawable(WXSucPhenixListener.this.mImageView.getContext().getResources(), bitmap));
                        } catch (Exception unused) {
                            WXSucPhenixListener.this.mImageView.setImageDrawable(drawable);
                        }
                    }
                });
                return false;
            }
        }
    }
}
