package com.alibaba.android;

import alimama.com.unweventparse.popup.PopUpExecer;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.BindingXPropertyInterceptor;
import com.alibaba.android.enhance.gpuimage.ImageFilterPlugin;
import com.alibaba.android.enhance.lottie.LottieProgressInterceptor;
import com.alibaba.android.enhance.lottie.WXLottieComponent;
import com.alibaba.android.enhance.nested.NestedPlugin;
import com.alibaba.android.enhance.svg.SVGInterceptor;
import com.alibaba.android.enhance.svg.SVGJSFunction;
import com.alibaba.android.enhance.svg.SVGPlugin;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WeexEnhance {
    private static final String TAG = "WeexEnhance";
    private static ImageLoadAdapter sImageAdapter;

    public interface ImageLoadAdapter {
        void fetchBitmapAsync(String str, OnImageLoadListener onImageLoadListener);

        Bitmap fetchBitmapSync(String str);
    }

    public interface OnImageLoadListener {
        void onLoadFailed(@NonNull String str);

        void onLoadSuccess(@NonNull Bitmap bitmap);
    }

    private WeexEnhance() {
    }

    public static void prepare() {
        prepare((ImageLoadAdapter) null);
    }

    public static void prepare(@Nullable ImageLoadAdapter imageLoadAdapter) {
        if (imageLoadAdapter == null) {
            imageLoadAdapter = new ImageLoadAdapter() {
                public void fetchBitmapAsync(String str, OnImageLoadListener onImageLoadListener) {
                    if (WeexEnhance.isPhenixExists()) {
                        WeexEnhance.fetchBitmapWithPhenixAsync(str, onImageLoadListener);
                    } else {
                        WXLogUtils.e(WeexEnhance.TAG, "Error! phenix is not exists, can not fetch bitmap");
                    }
                }

                public Bitmap fetchBitmapSync(String str) {
                    if (WeexEnhance.isPhenixExists()) {
                        return WeexEnhance.fetchBitmapWithPhenix(str);
                    }
                    WXLogUtils.e(WeexEnhance.TAG, "Error! phenix is not exists, can not fetch bitmap");
                    return null;
                }
            };
        }
        sImageAdapter = imageLoadAdapter;
        ImageFilterPlugin.register();
        NestedPlugin.register();
        try {
            WXSDKEngine.registerComponent(PopUpExecer.LOTTIETYPE, (Class<? extends WXComponent>) WXLottieComponent.class);
            SVGPlugin.registerSVG();
            if (isBindingXExists()) {
                BindingXPropertyInterceptor.getInstance().addInterceptor(new LottieProgressInterceptor());
                BindingXPropertyInterceptor.getInstance().addInterceptor(new SVGInterceptor());
                SVGJSFunction.registerAll();
            }
        } catch (WXException e) {
            WXLogUtils.e(TAG, e.toString());
        }
    }

    @Nullable
    public static ImageLoadAdapter getImageAdapter() {
        return sImageAdapter;
    }

    /* access modifiers changed from: private */
    public static void fetchBitmapWithPhenixAsync(@NonNull String str, @NonNull final OnImageLoadListener onImageLoadListener) {
        PhenixCreator addLoaderExtra = Phenix.instance().load(str).addLoaderExtra(Constant.BUNDLE_BIZ_CODE, Integer.toString(70));
        addLoaderExtra.succListener(new IPhenixListener<SuccPhenixEvent>() {
            public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                BitmapDrawable drawable;
                if (succPhenixEvent == null || (drawable = succPhenixEvent.getDrawable()) == null || drawable.getBitmap() == null) {
                    return false;
                }
                onImageLoadListener.onLoadSuccess(drawable.getBitmap());
                return false;
            }
        });
        addLoaderExtra.failListener(new IPhenixListener<FailPhenixEvent>() {
            public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                if (failPhenixEvent == null) {
                    return false;
                }
                Log.e(WeexEnhance.TAG, "phenix fetch bitmap failed:");
                OnImageLoadListener onImageLoadListener = onImageLoadListener;
                onImageLoadListener.onLoadFailed("errcode:" + failPhenixEvent.getResultCode());
                return false;
            }
        });
        addLoaderExtra.fetch();
    }

    /* access modifiers changed from: private */
    public static Bitmap fetchBitmapWithPhenix(@NonNull String str) {
        PhenixCreator addLoaderExtra = Phenix.instance().load(str).addLoaderExtra(Constant.BUNDLE_BIZ_CODE, Integer.toString(70));
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final ArrayList arrayList = new ArrayList(2);
        addLoaderExtra.succListener(new IPhenixListener<SuccPhenixEvent>() {
            public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                if (succPhenixEvent != null) {
                    try {
                        BitmapDrawable drawable = succPhenixEvent.getDrawable();
                        if (drawable != null) {
                            arrayList.add(drawable.getBitmap());
                        }
                    } catch (Throwable th) {
                        countDownLatch.countDown();
                        throw th;
                    }
                }
                countDownLatch.countDown();
                return false;
            }
        });
        addLoaderExtra.failListener(new IPhenixListener<FailPhenixEvent>() {
            public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                if (failPhenixEvent != null) {
                    Log.e(WeexEnhance.TAG, "phenix fetch bitmap failed:" + failPhenixEvent.getResultCode());
                }
                countDownLatch.countDown();
                return false;
            }
        });
        addLoaderExtra.fetch();
        try {
            countDownLatch.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
        }
        if (arrayList.size() > 0) {
            return (Bitmap) arrayList.get(0);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static boolean isPhenixExists() {
        try {
            Class.forName("com.taobao.phenix.intf.PhenixCreator");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    private static boolean isBindingXExists() {
        try {
            Class.forName("com.alibaba.android.bindingx.core.BindingXPropertyInterceptor");
            Class.forName("com.alibaba.android.bindingx.core.BindingXJSFunctionRegister");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
