package com.taobao.uikit.extend.feature.features;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.compat.stat.TBImageLifeCycleMonitor;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.PhenixTicket;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.IRetryHandlerOnFailure;
import com.taobao.phenix.intf.event.MemCacheMissPhenixEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.phenix.loader.network.HttpCodeResponseException;
import com.taobao.phenix.request.SchemeInfo;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.util.TBImageUrlStrategy;
import com.taobao.tcommon.core.RuntimeUtil;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.image.R;
import com.taobao.uikit.utils.UIKITLog;
import com.taobao.weex.BuildConfig;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ImageLoadFeature extends AbsFeature<ImageView> implements LayoutCallback {
    private static final int APM_UI_KEY = -308;
    private static final int L_SCROLLING = 1;
    private static final int L_SHOWING = 0;
    private static final int S_DONE_FAIL = 3;
    private static final int S_DONE_SUC = 2;
    private static final int S_INIT = 0;
    private static final int S_LOADING = 1;
    private static final int S_LOAD_IMMEDIATE = 4;
    protected ObjectAnimator mAlphaAnim;
    private String mCacheKey4PlaceHolder;
    private Context mContext;
    private boolean mEnableSizeInLayoutParams;
    private boolean mEnabledLoadOnFling = true;
    private int mErrorImageId;
    protected boolean mFadeIn;
    private ImageLoadFailListener mFailListener = new ImageLoadFailListener();
    private TUrlImageView.FinalUrlInspector mFinalUrlInspector;
    private PhenixOptions mGlobalPhenixOptions;
    private WeakReference<ImageView> mHostReference;
    /* access modifiers changed from: private */
    public int mKeepBackgroundState;
    private int mLastMaxViewSize;
    private PhenixTicket mLastResTicket;
    protected int mLoadState = 0;
    protected String mLoadingUrl = "";
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private IPhenixListener<MemCacheMissPhenixEvent> mMemoryMissListener = new IPhenixListener<MemCacheMissPhenixEvent>() {
        public boolean onHappen(MemCacheMissPhenixEvent memCacheMissPhenixEvent) {
            int i = ImageLoadFeature.this.mLoadState;
            ImageLoadFeature.this.fillImageDrawable(ImageLoadFeature.this.getHost(), (BitmapDrawable) null, false, ImageLoadFeature.this.mWhenNullClearImg);
            ImageLoadFeature.this.mLoadState = i;
            return false;
        }
    };
    private PhenixOptions mNextPhenixOptions;
    /* access modifiers changed from: private */
    public boolean mNoRepeatOnError = true;
    private Drawable mPlaceHoldForeground;
    protected int mPlaceHoldResourceId;
    private String mPriorityModuleName;
    private ImageRetryHandler mRetryHandler = new ImageRetryHandler();
    private int mScrollState = 0;
    private Boolean mSkipAutoSize;
    private ImageStrategyConfig mStrategyConfig;
    private ImageLoadSuccListener mSuccListener = new ImageLoadSuccListener();
    private PhenixTicket mTicket;
    /* access modifiers changed from: private */
    public String mUrl;
    private boolean mUserCalledSetImageUrl = false;
    protected IPhenixListener<FailPhenixEvent> mUserFailListener;
    protected IPhenixListener<SuccPhenixEvent> mUserSuccListener;
    /* access modifiers changed from: private */
    public boolean mWhenNullClearImg = true;

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void setStrategyConfig(Object obj) {
        if (obj instanceof ImageStrategyConfig) {
            this.mStrategyConfig = (ImageStrategyConfig) obj;
        }
    }

    public void setHost(ImageView imageView) {
        if (imageView == null) {
            this.mHostReference = null;
            this.mUserSuccListener = null;
            this.mUserFailListener = null;
            if (this.mTicket != null) {
                this.mTicket.cancel();
                return;
            }
            return;
        }
        this.mHostReference = new WeakReference<>(imageView);
        this.mContext = imageView.getContext().getApplicationContext();
        if (!TextUtils.isEmpty(this.mUrl)) {
            loadImageIfNecessary(false);
        }
    }

    public ImageView getHost() {
        WeakReference<ImageView> weakReference = this.mHostReference;
        if (weakReference != null) {
            return (ImageView) weakReference.get();
        }
        return null;
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        constructor(context, attributeSet, i, (boolean[]) null);
    }

    public void constructor(Context context, AttributeSet attributeSet, int i, boolean[] zArr) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ImageLoadFeature, i, 0)) != null) {
            this.mFadeIn = obtainStyledAttributes.getBoolean(R.styleable.ImageLoadFeature_uik_fade_in, false);
            if (obtainStyledAttributes.hasValue(R.styleable.ImageLoadFeature_uik_skip_auto_size)) {
                this.mSkipAutoSize = Boolean.valueOf(obtainStyledAttributes.getBoolean(R.styleable.ImageLoadFeature_uik_skip_auto_size, false));
            }
            this.mWhenNullClearImg = obtainStyledAttributes.getBoolean(R.styleable.ImageLoadFeature_uik_when_null_clear_img, true);
            this.mPlaceHoldResourceId = obtainStyledAttributes.getResourceId(R.styleable.ImageLoadFeature_uik_place_hold_background, 0);
            this.mErrorImageId = obtainStyledAttributes.getResourceId(R.styleable.ImageLoadFeature_uik_error_background, 0);
            this.mPlaceHoldForeground = obtainStyledAttributes.getDrawable(R.styleable.ImageLoadFeature_uik_place_hold_foreground);
            if (zArr != null) {
                zArr[0] = obtainStyledAttributes.getBoolean(R.styleable.ImageLoadFeature_uik_auto_release_image, true);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        int max = Math.max(i3 - i, i4 - i2);
        boolean z2 = this.mLastMaxViewSize > 0 && max - this.mLastMaxViewSize >= 100;
        this.mLastMaxViewSize = max;
        if (z2 || this.mLoadState != 2) {
            if (z2) {
                resetState();
            }
            loadImageIfNecessary(true);
        }
    }

    public boolean skipAutoSize(boolean z) {
        this.mSkipAutoSize = Boolean.valueOf(z);
        return z;
    }

    public void setFinalUrlInspector(TUrlImageView.FinalUrlInspector finalUrlInspector) {
        this.mFinalUrlInspector = finalUrlInspector;
    }

    public ImageLoadFeature succListener(IPhenixListener<SuccPhenixEvent> iPhenixListener) {
        this.mUserSuccListener = iPhenixListener;
        return this;
    }

    public ImageLoadFeature failListener(IPhenixListener<FailPhenixEvent> iPhenixListener) {
        this.mUserFailListener = iPhenixListener;
        return this;
    }

    public void reload(boolean z) {
        setImageUrl(this.mUrl, this.mCacheKey4PlaceHolder, z, true, this.mNextPhenixOptions);
    }

    public void setImageUrl(String str) {
        setImageUrl(str, (String) null, false, false, (PhenixOptions) null);
    }

    public void resetState() {
        this.mLoadState = 0;
    }

    public void setImageUrl(String str, String str2, boolean z, boolean z2, PhenixOptions phenixOptions) {
        this.mUserCalledSetImageUrl = true;
        if (z2 || this.mLoadState == 0 || this.mLoadState == 3 || !TextUtils.equals(this.mUrl, str) || !TextUtils.equals(this.mCacheKey4PlaceHolder, str2) || !PhenixOptions.isSame(this.mNextPhenixOptions, phenixOptions)) {
            StringBuilder sb = new StringBuilder();
            sb.append("TUrlImageView setImageUrl | url= ");
            sb.append(str);
            sb.append("| mLoadState:");
            sb.append(this.mLoadState);
            sb.append("| forceLoad:");
            sb.append(z2);
            sb.append("| hostView:");
            sb.append(getHost());
            UnitedLog.e(6, "Phenix", sb.toString());
            HashMap hashMap = new HashMap();
            hashMap.put("url", str);
            hashMap.put("log", sb.toString());
            TBImageLifeCycleMonitor.instance().onEvent("IMAGE", "setImageUrl", hashMap);
            this.mUrl = str;
            this.mCacheKey4PlaceHolder = str2;
            this.mNoRepeatOnError = false;
            resetState();
            this.mNextPhenixOptions = phenixOptions;
            ImageView host = getHost();
            if (host == null) {
                return;
            }
            if (!z) {
                loadImageIfNecessary(false);
            } else if (this.mUrl == null) {
                Phenix.instance().cancel(this.mTicket);
                fillImageDrawable(host, (BitmapDrawable) null, false, true);
            } else {
                this.mMainHandler.post(new Runnable() {
                    public void run() {
                        boolean unused = ImageLoadFeature.this.loadImageIfNecessary(false);
                    }
                });
            }
        }
    }

    private String getPriorityModuleName() {
        if (this.mNextPhenixOptions != null) {
            return this.mNextPhenixOptions.priorityModuleName;
        }
        if (this.mPriorityModuleName != null) {
            return this.mPriorityModuleName;
        }
        if (this.mGlobalPhenixOptions != null) {
            return this.mGlobalPhenixOptions.priorityModuleName;
        }
        return null;
    }

    public ResponseData retrieveImageData() {
        String str = this.mLoadingUrl;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Phenix.instance().fetchDiskCache(getPriorityModuleName(), str, 0, false);
    }

    public void keepBackgroundOnForegroundUpdate(boolean z) {
        this.mKeepBackgroundState = z ? 1 : -1;
    }

    public void enableLoadOnFling(boolean z) {
        this.mEnabledLoadOnFling = z;
    }

    public void enableSizeInLayoutParams(boolean z) {
        this.mEnableSizeInLayoutParams = z;
    }

    public void setPlaceHoldImageResId(int i) {
        this.mPlaceHoldResourceId = i;
    }

    public void setPlaceHoldForeground(Drawable drawable) {
        this.mPlaceHoldForeground = drawable;
    }

    public void setErrorImageResId(int i) {
        this.mErrorImageId = i;
    }

    public void pause() {
        this.mScrollState = 1;
    }

    public void resume() {
        if (this.mScrollState == 1) {
            this.mScrollState = 0;
            if (this.mLoadState == 0 || this.mLoadState == 4) {
                resetState();
                loadImageIfNecessary(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean loadImageIfNecessary(final boolean z) {
        final boolean z2 = TUrlImageView.sIsSpeed;
        if (!z2) {
            loadImageIfNecessaryProxy(z, z2);
        } else if (TextUtils.isEmpty(this.mUrl)) {
            if (this.mTicket != null) {
                this.mTicket.cancel();
            }
            fillImageDrawable(getHost(), (BitmapDrawable) null, false, this.mUserCalledSetImageUrl);
            return false;
        } else {
            if (this.mTicket != null && !this.mTicket.theSame(this.mUrl)) {
                this.mTicket.cancel();
            }
            this.mMainHandler.post(new Runnable() {
                public void run() {
                    boolean unused = ImageLoadFeature.this.loadImageIfNecessaryProxy(z, z2);
                }
            });
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0059  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean loadImageIfNecessaryProxy(boolean r10, boolean r11) {
        /*
            r9 = this;
            android.widget.ImageView r11 = r9.getHost()
            r0 = 0
            if (r11 != 0) goto L_0x0008
            return r0
        L_0x0008:
            int r1 = r11.getWidth()
            int r2 = r11.getHeight()
            android.view.ViewGroup$LayoutParams r3 = r11.getLayoutParams()
            r4 = 1
            if (r3 == 0) goto L_0x0039
            boolean r5 = r9.mEnableSizeInLayoutParams
            if (r5 == 0) goto L_0x002e
            int r5 = r3.width
            if (r5 <= 0) goto L_0x002e
            int r5 = r3.height
            if (r5 <= 0) goto L_0x002e
            int r1 = r3.width
            int r2 = r3.height
            int r3 = java.lang.Math.max(r1, r2)
            r9.mLastMaxViewSize = r3
            goto L_0x0039
        L_0x002e:
            int r5 = r3.height
            r6 = -2
            if (r5 != r6) goto L_0x0039
            int r3 = r3.width
            if (r3 != r6) goto L_0x0039
            r3 = 1
            goto L_0x003a
        L_0x0039:
            r3 = 0
        L_0x003a:
            if (r1 != 0) goto L_0x0041
            if (r2 != 0) goto L_0x0041
            if (r3 != 0) goto L_0x0041
            return r0
        L_0x0041:
            java.lang.String r3 = r9.mUrl
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L_0x0059
            com.taobao.phenix.intf.PhenixTicket r10 = r9.mTicket
            if (r10 == 0) goto L_0x0052
            com.taobao.phenix.intf.PhenixTicket r10 = r9.mTicket
            r10.cancel()
        L_0x0052:
            r10 = 0
            boolean r1 = r9.mUserCalledSetImageUrl
            r9.fillImageDrawable(r11, r10, r0, r1)
            return r0
        L_0x0059:
            com.taobao.phenix.intf.PhenixTicket r3 = r9.mTicket
            if (r3 == 0) goto L_0x006c
            com.taobao.phenix.intf.PhenixTicket r3 = r9.mTicket
            java.lang.String r5 = r9.mUrl
            boolean r3 = r3.theSame(r5)
            if (r3 != 0) goto L_0x006c
            com.taobao.phenix.intf.PhenixTicket r3 = r9.mTicket
            r3.cancel()
        L_0x006c:
            boolean r3 = r9.mNoRepeatOnError
            if (r3 == 0) goto L_0x0071
            return r0
        L_0x0071:
            int r3 = r9.mLoadState
            if (r3 == 0) goto L_0x0076
            return r0
        L_0x0076:
            int r3 = r9.mScrollState
            if (r3 != r4) goto L_0x0081
            boolean r3 = r9.mEnabledLoadOnFling
            if (r3 != 0) goto L_0x007f
            return r0
        L_0x007f:
            r3 = 1
            goto L_0x0082
        L_0x0081:
            r3 = 0
        L_0x0082:
            java.lang.String r5 = r9.mUrl
            java.lang.Boolean r6 = r9.mSkipAutoSize
            if (r6 != 0) goto L_0x008e
            boolean r6 = com.taobao.uikit.extend.feature.view.TUrlImageView.isAutoSizeSkippedGlobally()
            if (r6 == 0) goto L_0x009a
        L_0x008e:
            java.lang.Boolean r6 = r9.mSkipAutoSize
            if (r6 == 0) goto L_0x00aa
            java.lang.Boolean r6 = r9.mSkipAutoSize
            boolean r6 = r6.booleanValue()
            if (r6 != 0) goto L_0x00aa
        L_0x009a:
            java.lang.String r5 = r9.mUrl
            java.lang.Integer r6 = java.lang.Integer.valueOf(r1)
            java.lang.Integer r7 = java.lang.Integer.valueOf(r2)
            com.taobao.tao.image.ImageStrategyConfig r8 = r9.mStrategyConfig
            java.lang.String r5 = com.taobao.tao.util.ImageStrategyDecider.decideUrl(r5, r6, r7, r8)
        L_0x00aa:
            com.taobao.uikit.extend.feature.view.TUrlImageView$FinalUrlInspector r6 = r9.mFinalUrlInspector
            if (r6 == 0) goto L_0x00b4
            com.taobao.uikit.extend.feature.view.TUrlImageView$FinalUrlInspector r6 = r9.mFinalUrlInspector
            java.lang.String r5 = r6.inspectFinalUrl(r5, r1, r2)
        L_0x00b4:
            com.taobao.uikit.extend.feature.view.TUrlImageView$FinalUrlInspector r6 = com.taobao.uikit.extend.feature.view.TUrlImageView.getGlobalFinalUrlInspector()
            if (r6 == 0) goto L_0x00be
            java.lang.String r5 = r6.inspectFinalUrl(r5, r1, r2)
        L_0x00be:
            r9.mLoadingUrl = r5
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageLoadSuccListener r1 = r9.mSuccListener
            r1.setIsInLayoutPass(r10)
            r10 = 4
            if (r3 == 0) goto L_0x00ca
            r1 = 4
            goto L_0x00cb
        L_0x00ca:
            r1 = 1
        L_0x00cb:
            r9.mLoadState = r1
            com.taobao.uikit.extend.feature.features.PhenixOptions r1 = r9.mNextPhenixOptions
            if (r1 == 0) goto L_0x00d4
            com.taobao.uikit.extend.feature.features.PhenixOptions r1 = r9.mNextPhenixOptions
            goto L_0x00d6
        L_0x00d4:
            com.taobao.uikit.extend.feature.features.PhenixOptions r1 = r9.mGlobalPhenixOptions
        L_0x00d6:
            com.taobao.phenix.intf.Phenix r2 = com.taobao.phenix.intf.Phenix.instance()
            android.content.Context r6 = r9.mContext
            com.taobao.phenix.intf.Phenix r2 = r2.with(r6)
            java.lang.String r6 = r9.getPriorityModuleName()
            com.taobao.phenix.intf.PhenixCreator r2 = r2.load((java.lang.String) r6, (java.lang.String) r5)
            com.taobao.phenix.intf.PhenixCreator r2 = r2.releasableDrawable(r4)
            java.lang.String r6 = r9.mCacheKey4PlaceHolder
            com.taobao.phenix.intf.PhenixCreator r2 = r2.secondary(r6)
            com.taobao.phenix.intf.PhenixCreator r2 = r2.memOnly(r3)
            com.taobao.phenix.intf.PhenixCreator r11 = r2.limitSize(r11)
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageLoadSuccListener r2 = r9.mSuccListener
            com.taobao.phenix.intf.PhenixCreator r11 = r11.succListener(r2)
            com.taobao.phenix.intf.event.IPhenixListener<com.taobao.phenix.intf.event.MemCacheMissPhenixEvent> r2 = r9.mMemoryMissListener
            com.taobao.phenix.intf.PhenixCreator r11 = r11.memCacheMissListener(r2)
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageLoadFailListener r2 = r9.mFailListener
            com.taobao.phenix.intf.PhenixCreator r11 = r11.failListener(r2)
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageLoadSuccListener r2 = r9.mSuccListener
            r2.mCreator = r11
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageLoadFailListener r2 = r9.mFailListener
            r2.mCreator = r11
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            java.lang.String r3 = "oriUrl"
            java.lang.String r6 = r9.mUrl
            r2.put(r3, r6)
            java.lang.String r3 = "time"
            long r6 = java.lang.System.currentTimeMillis()
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r2.put(r3, r6)
            com.taobao.phenix.compat.stat.TBImageLifeCycleMonitor r3 = com.taobao.phenix.compat.stat.TBImageLifeCycleMonitor.instance()
            int r6 = r11.id()
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r3.onRequest(r6, r5, r2)
            if (r1 == 0) goto L_0x0186
            boolean r2 = r1.isOpened(r4)
            com.taobao.phenix.intf.PhenixCreator r2 = r11.preloadWithSmall(r2)
            r3 = 2
            boolean r3 = r1.isOpened(r3)
            com.taobao.phenix.intf.PhenixCreator r2 = r2.scaleFromLarge(r3)
            com.taobao.phenix.bitmap.BitmapProcessor[] r3 = r1.bitmapProcessors
            com.taobao.phenix.intf.PhenixCreator r2 = r2.bitmapProcessors(r3)
            int r3 = r1.thumbnailType
            r4 = 16
            boolean r4 = r1.isOpened(r4)
            com.taobao.phenix.intf.PhenixCreator r2 = r2.asThumbnail(r3, r4)
            int r3 = r1.diskCachePriority
            com.taobao.phenix.intf.PhenixCreator r2 = r2.diskCachePriority(r3)
            int r3 = r1.schedulePriority
            com.taobao.phenix.intf.PhenixCreator r2 = r2.schedulePriority(r3)
            int r3 = r1.memoryCachePriority
            r2.memoryCachePriority(r3)
            boolean r10 = r1.isOpened(r10)
            if (r10 == 0) goto L_0x017b
            r11.skipCache()
        L_0x017b:
            r10 = 8
            boolean r10 = r1.isOpened(r10)
            if (r10 == 0) goto L_0x0186
            r11.onlyCache()
        L_0x0186:
            java.lang.String r10 = r9.mUrl
            boolean r10 = r10.equals(r5)
            if (r10 != 0) goto L_0x01a0
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageRetryHandler r10 = r9.mRetryHandler
            java.lang.String r1 = r9.mUrl
            com.taobao.uikit.extend.feature.features.ImageLoadFeature$ImageRetryHandler r10 = r10.setRetryUrl(r1)
            r11.retryHandler(r10)
            java.lang.String r10 = "origin_url"
            java.lang.String r1 = r9.mUrl
            r11.addLoaderExtra(r10, r1)
        L_0x01a0:
            com.taobao.tao.image.ImageStrategyConfig r10 = r9.mStrategyConfig
            if (r10 == 0) goto L_0x01bf
            com.taobao.tao.image.ImageStrategyConfig r10 = r9.mStrategyConfig
            java.lang.String r10 = r10.getBizIdStr()
            boolean r1 = android.text.TextUtils.isEmpty(r10)
            if (r1 == 0) goto L_0x01ba
            com.taobao.tao.image.ImageStrategyConfig r10 = r9.mStrategyConfig
            int r10 = r10.getBizId()
            java.lang.String r10 = java.lang.String.valueOf(r10)
        L_0x01ba:
            java.lang.String r1 = "bundle_biz_code"
            r11.addLoaderExtra(r1, r10)
        L_0x01bf:
            com.taobao.phenix.intf.PhenixTicket r10 = r11.fetch()
            r9.mTicket = r10
            com.taobao.phenix.intf.PhenixTicket r10 = r9.mTicket
            java.lang.String r11 = r9.mUrl
            r10.setUrl(r11)
            android.widget.ImageView r10 = r9.getHost()
            if (r10 == 0) goto L_0x01d9
            r11 = -308(0xfffffffffffffecc, float:NaN)
            java.lang.String r1 = "START"
            r10.setTag(r11, r1)
        L_0x01d9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.extend.feature.features.ImageLoadFeature.loadImageIfNecessaryProxy(boolean, boolean):boolean");
    }

    private boolean isViewDrawableSameWith(ImageView imageView, Drawable drawable) {
        if (imageView instanceof TUrlImageView) {
            return ((TUrlImageView) imageView).isDrawableSameWith(drawable);
        }
        return imageView != null && imageView.getDrawable() == drawable;
    }

    /* access modifiers changed from: private */
    public boolean isViewBitmapDifferentWith(ImageView imageView, Bitmap bitmap) {
        if (imageView instanceof TUrlImageView) {
            return ((TUrlImageView) imageView).isViewBitmapDifferentWith(bitmap);
        }
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            return (drawable instanceof BitmapDrawable) && ((BitmapDrawable) drawable).getBitmap() != bitmap;
        }
    }

    /* access modifiers changed from: private */
    public void fillImageDrawable(ImageView imageView, BitmapDrawable bitmapDrawable, boolean z, boolean z2) {
        if (imageView != null) {
            if (this.mLastResTicket != null) {
                this.mLastResTicket.cancel();
                this.mLastResTicket = null;
            }
            if (bitmapDrawable != null) {
                imageView.setImageDrawable(bitmapDrawable);
                if (this.mKeepBackgroundState < 0 || (this.mKeepBackgroundState == 0 && this.mPlaceHoldResourceId != 0)) {
                    imageView.setBackgroundDrawable((Drawable) null);
                }
            } else if (z && z2) {
                imageView.setImageDrawable((Drawable) null);
                placeBgResImage(this.mErrorImageId != 0 ? this.mErrorImageId : this.mPlaceHoldResourceId);
            } else if ((z2 || isViewDrawableSameWith(imageView, (Drawable) null)) && this.mPlaceHoldForeground != null) {
                imageView.setImageDrawable(this.mPlaceHoldForeground);
            } else if (z2) {
                imageView.setImageDrawable((Drawable) null);
                placeBgResImage(this.mPlaceHoldResourceId);
            }
        }
    }

    private void placeBgResImage(int i) {
        ImageView host = getHost();
        if (i != 0 && host != null) {
            if (!RuntimeUtil.isPictureResource(this.mContext, i)) {
                host.setBackgroundResource(i);
            } else {
                this.mLastResTicket = Phenix.instance().with(this.mContext).load(SchemeInfo.wrapRes(i)).schedulePriority(4).succListener(new IPhenixListener<SuccPhenixEvent>() {
                    /* JADX WARNING: type inference failed for: r1v0, types: [android.graphics.drawable.NinePatchDrawable] */
                    /* JADX WARNING: Multi-variable type inference failed */
                    /* JADX WARNING: Unknown variable types count: 1 */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public boolean onHappen(com.taobao.phenix.intf.event.SuccPhenixEvent r3) {
                        /*
                            r2 = this;
                            com.taobao.uikit.extend.feature.features.ImageLoadFeature r0 = com.taobao.uikit.extend.feature.features.ImageLoadFeature.this
                            android.widget.ImageView r0 = r0.getHost()
                            if (r0 == 0) goto L_0x001a
                            android.graphics.drawable.BitmapDrawable r3 = r3.getDrawable()
                            com.taobao.phenix.cache.memory.PassableBitmapDrawable r3 = (com.taobao.phenix.cache.memory.PassableBitmapDrawable) r3
                            if (r3 == 0) goto L_0x001a
                            android.graphics.drawable.NinePatchDrawable r1 = r3.convert2NinePatchDrawable()
                            if (r1 == 0) goto L_0x0017
                            r3 = r1
                        L_0x0017:
                            r0.setBackgroundDrawable(r3)
                        L_0x001a:
                            r3 = 0
                            return r3
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.extend.feature.features.ImageLoadFeature.AnonymousClass4.onHappen(com.taobao.phenix.intf.event.SuccPhenixEvent):boolean");
                    }
                }).fetch();
            }
        }
    }

    public boolean isFadeIn() {
        return this.mFadeIn;
    }

    public void setFadeIn(boolean z) {
        this.mFadeIn = z;
    }

    public void setWhenNullClearImg(boolean z) {
        this.mWhenNullClearImg = z;
    }

    public String getImageUrl() {
        return this.mUrl;
    }

    public String getLoadingUrl() {
        return this.mLoadingUrl;
    }

    public void setPriorityModuleName(String str) {
        this.mPriorityModuleName = str;
    }

    public void setPhenixOptions(PhenixOptions phenixOptions) {
        this.mGlobalPhenixOptions = phenixOptions;
    }

    class ImageLoadFailListener implements IPhenixListener<FailPhenixEvent> {
        PhenixCreator mCreator;

        ImageLoadFailListener() {
        }

        public boolean onHappen(FailPhenixEvent failPhenixEvent) {
            UIKITLog.d(TUrlImageView.LOG_TAG, "load image failed, state=%d, url=%s", Integer.valueOf(ImageLoadFeature.this.mLoadState), ImageLoadFeature.this.mUrl);
            int resultCode = failPhenixEvent.getResultCode();
            if (resultCode == -1 || resultCode == 404) {
                boolean unused = ImageLoadFeature.this.mNoRepeatOnError = true;
            } else {
                boolean unused2 = ImageLoadFeature.this.mNoRepeatOnError = false;
            }
            ImageView host = ImageLoadFeature.this.getHost();
            if (host != null) {
                host.setTag(ImageLoadFeature.APM_UI_KEY, "ERROR");
            }
            failPhenixEvent.getTicket().setDone(true);
            ImageLoadFeature.this.fillImageDrawable(ImageLoadFeature.this.getHost(), (BitmapDrawable) null, true, ImageLoadFeature.this.mWhenNullClearImg);
            ImageLoadFeature.this.mLoadState = 3;
            if (ImageLoadFeature.this.mUserFailListener != null) {
                ImageLoadFeature.this.mUserFailListener.onHappen(failPhenixEvent);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("resultCode", failPhenixEvent != null ? String.valueOf(failPhenixEvent.getResultCode()) : BuildConfig.buildJavascriptFrameworkVersion);
            hashMap.put("time", Long.valueOf(System.currentTimeMillis()));
            hashMap.put("oriUrl", ImageLoadFeature.this.mUrl);
            TBImageLifeCycleMonitor.instance().onError(this.mCreator != null ? String.valueOf(this.mCreator.id()) : "", failPhenixEvent.getUrl(), hashMap);
            return true;
        }
    }

    class ImageLoadSuccListener implements IPhenixListener<SuccPhenixEvent> {
        private boolean isInLayoutPass;
        PhenixCreator mCreator;

        ImageLoadSuccListener() {
        }

        public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
            return applyEvent(succPhenixEvent, succPhenixEvent.isImmediate());
        }

        public boolean applyEvent(final SuccPhenixEvent succPhenixEvent, boolean z) {
            String url = succPhenixEvent.getUrl();
            ImageView host = ImageLoadFeature.this.getHost();
            if (host != null) {
                host.setTag(ImageLoadFeature.APM_UI_KEY, "END");
            }
            if (url == null || ImageLoadFeature.this.mLoadingUrl == null || url.startsWith(ImageLoadFeature.this.mLoadingUrl)) {
                final ImageView host2 = ImageLoadFeature.this.getHost();
                if (host2 == null) {
                    ImageLoadFeature.this.mLoadState = 3;
                    return false;
                } else if (!z || !this.isInLayoutPass) {
                    ImageLoadFeature.this.mLoadState = 3;
                    BitmapDrawable drawable = succPhenixEvent.getDrawable();
                    if (drawable == null) {
                        ImageLoadFeature.this.fillImageDrawable(host2, (BitmapDrawable) null, false, ImageLoadFeature.this.mWhenNullClearImg);
                        return true;
                    }
                    boolean isIntermediate = succPhenixEvent.isIntermediate();
                    boolean z2 = ImageLoadFeature.this.mFadeIn;
                    if (ImageLoadFeature.this.isViewBitmapDifferentWith(host2, drawable.getBitmap())) {
                        z2 = false;
                    }
                    if (z || isIntermediate || !z2 || ImageLoadFeature.this.mLoadState == 2) {
                        ImageLoadFeature.this.fillImageDrawable(host2, drawable, false, ImageLoadFeature.this.mWhenNullClearImg);
                    } else {
                        host2.setImageDrawable(drawable);
                        if (ImageLoadFeature.this.mAlphaAnim == null) {
                            ImageLoadFeature.this.mAlphaAnim = ObjectAnimator.ofInt(host2, "alpha", new int[]{0, 255});
                            ImageLoadFeature.this.mAlphaAnim.setInterpolator(new AccelerateInterpolator());
                            ImageLoadFeature.this.mAlphaAnim.setDuration(300);
                            ImageLoadFeature.this.mAlphaAnim.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animator) {
                                    if (ImageLoadFeature.this.mKeepBackgroundState < 0 || (ImageLoadFeature.this.mKeepBackgroundState == 0 && ImageLoadFeature.this.mPlaceHoldResourceId != 0)) {
                                        host2.setBackgroundDrawable((Drawable) null);
                                    }
                                }
                            });
                            ImageLoadFeature.this.mAlphaAnim.start();
                        } else if (!ImageLoadFeature.this.mAlphaAnim.isRunning()) {
                            ImageLoadFeature.this.mAlphaAnim.start();
                        }
                    }
                    if (!isIntermediate) {
                        succPhenixEvent.getTicket().setDone(true);
                        ImageLoadFeature.this.mLoadState = 2;
                        if (ImageLoadFeature.this.mUserSuccListener != null) {
                            ImageLoadFeature.this.mUserSuccListener.onHappen(succPhenixEvent);
                        }
                    }
                    HashMap hashMap = new HashMap();
                    hashMap.put("intermediate", Boolean.valueOf(isIntermediate));
                    hashMap.put("animation", Boolean.valueOf(z2));
                    hashMap.put("time", Long.valueOf(System.currentTimeMillis()));
                    TBImageLifeCycleMonitor.instance().onFinished(this.mCreator != null ? String.valueOf(this.mCreator.id()) : "", url, hashMap);
                    return true;
                } else {
                    host2.post(new Runnable() {
                        public void run() {
                            ImageLoadSuccListener.this.applyEvent(succPhenixEvent, false);
                        }
                    });
                    return true;
                }
            } else {
                UIKITLog.w(TUrlImageView.LOG_TAG, "callback url not match target url, callback=%s, target=%s", url, ImageLoadFeature.this.mLoadingUrl);
                return true;
            }
        }

        public void setIsInLayoutPass(boolean z) {
            this.isInLayoutPass = z;
        }
    }

    class ImageRetryHandler implements IRetryHandlerOnFailure {
        private String retryUrl;

        ImageRetryHandler() {
        }

        public ImageRetryHandler setRetryUrl(String str) {
            if (str == null || !str.endsWith(TBImageUrlStrategy.END_IMAGE_URL)) {
                this.retryUrl = str;
            } else {
                this.retryUrl = str.substring(0, str.length() - TBImageUrlStrategy.END_IMAGE_URL.length());
            }
            return this;
        }

        public String getRetryUrl(PhenixCreator phenixCreator, Throwable th) {
            if (!(th instanceof HttpCodeResponseException) || ((HttpCodeResponseException) th).getHttpCode() != 404) {
                return null;
            }
            ImageLoadFeature.this.mLoadingUrl = this.retryUrl;
            return this.retryUrl;
        }
    }
}
