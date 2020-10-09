package com.taobao.weex;

import alimama.com.unwrouter.UNWRouter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.webview.WVSchemeInterceptService;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.taobao.TBActionBar;
import androidx.collection.ArrayMap;
import androidx.core.util.Pair;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.adapter.IFeedBackCallBack;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.core.jsmodule.PFJSModuleIntegration;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.android.festival.FestivalMgr;
import com.taobao.android.tracker.TrackerManager;
import com.taobao.baseactivity.CustomBaseActivity;
import com.taobao.linkmanager.AfcLifeCycleCenter;
import com.taobao.orange.OrangeConfig;
import com.taobao.tao.Globals;
import com.taobao.tao.util.SystemBarDecorator;
import com.taobao.uikit.actionbar.TBActionView;
import com.taobao.uikit.actionbar.TBPublicMenu;
import com.taobao.uikit.extend.component.TBErrorView;
import com.taobao.uikit.extend.component.error.Error;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import com.taobao.uikit.feature.features.RatioFeature;
import com.taobao.weex.WXSecurityGuardPageTrack;
import com.taobao.weex.adapter.R;
import com.taobao.weex.adapter.TBNavBarAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.constant.Constants;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.BoxShadowUtil;
import com.taobao.weex.utils.ContainerMonitor;
import com.taobao.weex.utils.TBWXConfigManger;
import com.taobao.weex.utils.Utility;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXTBUtil;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

public class WXActivity extends CustomBaseActivity implements IFeedBackCallBack {
    private static final String ACTION_DEGRADE_TO_WINDVANE = "degradeToWindVane";
    private static LinkedList<Activity> ACTIVITY_STACK = new LinkedList<>();
    private static final String BROWSER_ONLY_CATEGORY = "com.taobao.intent.category.HYBRID_UI";
    private static final String CONFIG_GROUP_WEEX_EXT = "android_weex_ext_config";
    private static final String CONFIG_GROUP_WEEX_HC = "android_weex_huichang_config";
    private static final String DEGRADE_MSG = "degrade_msg";
    private static final String DEGRADE_TYPE = "degrade_type";
    private static final String DEGRADE_TYPE_INIT_ERROR = "DEGRADE_TYPE_INIT_ERROR";
    private static final String DEGRADE_TYPE_JS_ERROR = "DEGRADE_TYPE_JS_ERROR";
    private static final String DEGRADE_TYPE_PARAMS_ERROR = "DEGRADE_TYPE_PARAMS_ERROR";
    private static final String FROM = "_wx_f_";
    private static final String FROM_WEEX = "1";
    private static final String FROM_WEEX_DEGRADE_H5 = "2";
    private static final String HIDDEN_STATUS_BAR_WITH_DARK_TEXT = "hidden_dark_text";
    private static final String HIDDEN_STATUS_BAR_WITH_LIGHT_TEXT = "hidden_light_text";
    private static final String KEY_BOX_SHADOW_ENABLED = "box_shadow_enabled";
    private static final String KEY_NAV_TRANSPARENT = "weex_navbar_transparent";
    private static final String NAV_HIDDEN = "wx_navbar_hidden";
    private static final String NAV_OVERLAY = "wx_navbar_transparent";
    public static final String STATUSBAR_HEIGHT = "statusbarHeight";
    private static final String TAG = "WXActivity";
    private static final String WX_APPBAR = "_wx_appbar";
    private static final String WX_SECURE = "wx_secure";
    private static final String WX_STATUSBAR_HIDDEN = "_wx_statusbar_hidden";
    private static volatile Boolean usePrefetchXUrlMapping = null;
    private boolean actionBarOverlay = false;
    private ImageView bg_transparent_bottom_bone;
    private View bg_transparent_image_bg;
    /* access modifiers changed from: private */
    public TUrlImageView bg_transparent_image_view;
    private float bg_transparent_img_ratio = 1.0f;
    private String bg_transparent_img_url = null;
    private int bg_transparent_margin_left = 0;
    private int bg_transparent_margin_top = 0;
    private boolean bg_transparent_switch = false;
    private String fatBundleUrl;
    private AtomicBoolean hasCommited = new AtomicBoolean(false);
    private boolean isMainHc = false;
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    private WXRenderListenerImp listenerAdapter;
    private WXAnalyzerDelegate mAnalyzerDelegate;
    private String mBundleUrl;
    private BroadcastReceiver mDegradeReceive = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), WXActivity.ACTION_DEGRADE_TO_WINDVANE)) {
                WXActivity.this.degradeToWindVane(WXActivity.this.getIntent());
                WXActivity.this.finishWithException();
            }
        }
    };
    private ImageView mFakeTitle;
    private boolean mIsDegrade = false;
    private boolean mMainHcNaviShow = false;
    private WeexPageFragment mPageFragment;
    private String mPageName;
    private String mPageUserInfo;
    private PFJSModuleIntegration mPrefetchXIntegration = null;
    private TBNavBarAdapter mTBNavBarAdapter;
    private long mTimeContainerStart = 0;
    private String mWeexUrl;
    private String mWeexUserInfo;
    private ViewTreeObserver observer;
    public TBActionView overflowButton;
    private long prefetchxProcessStartTime;
    private String thinHostPath;
    private TrackerManager trackerManager;
    private WXSecurityGuardPageTrack wxSecurityGuardPageTrack;

    private enum StatusBarTextColor {
        Dark,
        Light,
        Undefine
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXActivity, android.app.Activity] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        constrainStackSize();
        processTransparentBG();
        if (!isEnvironmentReady()) {
            if (WXEnvironment.isApkDebugable()) {
                Toast.makeText(this, "WEEX_SDK 初始化失败!", 0).show();
            }
            WXActivity.super.onCreate(bundle);
            Intent intent = getIntent();
            intent.putExtra(DEGRADE_TYPE, DEGRADE_TYPE_INIT_ERROR);
            intent.putExtra(DEGRADE_MSG, "WEEX_SDK 初始化失败!降级到h5");
            degradeToWindVane(intent);
            AppMonitor.Alarm.commitFail("weex", "renderResult", generateAppMonitorArgs(), "99302", "weex framework init failed");
            finishWithException();
        } else if (!isParamValid(getIntent())) {
            if (WXEnvironment.isApkDebugable()) {
                Toast.makeText(this, "参数非法!", 0).show();
            }
            WXActivity.super.onCreate(bundle);
            Intent intent2 = getIntent();
            intent2.putExtra(DEGRADE_TYPE, DEGRADE_TYPE_PARAMS_ERROR);
            String str = BuildConfig.buildJavascriptFrameworkVersion;
            if (getIntent().getData() != null) {
                str = getIntent().getData().toString();
            }
            intent2.putExtra(DEGRADE_MSG, "参数非法 ! 降级到h5! params is " + str);
            degradeToWindVane(intent2);
            AppMonitor.Alarm.commitFail("weex", "renderResult", generateAppMonitorArgs(), "99303", "error params");
            finishWithException();
        } else {
            pageStart();
            ensureBoxShadow();
            overwriteWeexUrl();
            setScreenCaptureEnabledOrNot();
            chooseAppBarMode(bundle);
            if (this.bg_transparent_switch) {
                setContentView(R.layout.weex_activity_root_transparent_layout);
                prepareAniImage();
            } else {
                setContentView(R.layout.weex_activity_root_layout);
            }
            showPredefinedAppbar();
            prepareStatusBar();
            this.mPageName = assemblePageName(this.mBundleUrl);
            AliWXSDKEngine.setCurCrashUrl(this.mPageName);
            this.mAnalyzerDelegate = new WXAnalyzerDelegate(this);
            this.mAnalyzerDelegate.onCreate();
            hideAppBar();
            initFragmentWithAdapter();
            try {
                this.trackerManager = new TrackerManager();
                this.trackerManager.init(this, this.mWeexUrl);
            } catch (Throwable th) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(th));
                }
            }
            try {
                this.wxSecurityGuardPageTrack = WXSecurityGuardPageTrack.createInstance(getApplicationContext());
            } catch (Exception unused) {
                WXLogUtils.e(TAG, "[PageTrackLog]init wsg sdk error");
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:20|21|(1:23)(1:24)|25|(1:27)|28|(1:30)(1:31)|32|33|34|38) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00a4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void processTransparentBG() {
        /*
            r5 = this;
            r0 = 0
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00b7 }
            r2 = 21
            if (r1 >= r2) goto L_0x0008
            return
        L_0x0008:
            android.content.Intent r1 = r5.getIntent()     // Catch:{ Throwable -> 0x00b7 }
            if (r1 != 0) goto L_0x000f
            return
        L_0x000f:
            android.os.Bundle r1 = r1.getExtras()     // Catch:{ Throwable -> 0x00b7 }
            if (r1 != 0) goto L_0x0016
            return
        L_0x0016:
            java.lang.String r2 = "transition"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ Throwable -> 0x00b7 }
            java.lang.String r3 = "imageUrl"
            java.lang.String r1 = r1.getString(r3)     // Catch:{ Throwable -> 0x00b7 }
            r5.bg_transparent_img_url = r1     // Catch:{ Throwable -> 0x00b7 }
            java.lang.String r1 = "scale"
            boolean r1 = android.text.TextUtils.equals(r1, r2)     // Catch:{ Throwable -> 0x00b7 }
            if (r1 == 0) goto L_0x0036
            java.lang.String r1 = r5.bg_transparent_img_url     // Catch:{ Throwable -> 0x00b7 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x00b7 }
            if (r1 != 0) goto L_0x0036
            r1 = 1
            goto L_0x0037
        L_0x0036:
            r1 = 0
        L_0x0037:
            r5.bg_transparent_switch = r1     // Catch:{ Throwable -> 0x00b7 }
            boolean r1 = r5.bg_transparent_switch     // Catch:{ Throwable -> 0x00b7 }
            if (r1 != 0) goto L_0x003e
            return
        L_0x003e:
            android.content.Intent r1 = r5.getIntent()     // Catch:{ Throwable -> 0x00a4 }
            android.net.Uri r1 = r1.getData()     // Catch:{ Throwable -> 0x00a4 }
            java.lang.String r2 = "marginTop"
            java.lang.String r2 = r1.getQueryParameter(r2)     // Catch:{ Throwable -> 0x00a4 }
            r3 = 0
            if (r2 == 0) goto L_0x005e
            java.lang.String r2 = "marginTop"
            java.lang.String r2 = r1.getQueryParameter(r2)     // Catch:{ Throwable -> 0x00a4 }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Throwable -> 0x00a4 }
            float r2 = r2.floatValue()     // Catch:{ Throwable -> 0x00a4 }
            goto L_0x005f
        L_0x005e:
            r2 = 0
        L_0x005f:
            java.lang.String r4 = "marginLeft"
            java.lang.String r4 = r1.getQueryParameter(r4)     // Catch:{ Throwable -> 0x00a4 }
            if (r4 == 0) goto L_0x0075
            java.lang.String r3 = "marginLeft"
            java.lang.String r3 = r1.getQueryParameter(r3)     // Catch:{ Throwable -> 0x00a4 }
            java.lang.Float r3 = java.lang.Float.valueOf(r3)     // Catch:{ Throwable -> 0x00a4 }
            float r3 = r3.floatValue()     // Catch:{ Throwable -> 0x00a4 }
        L_0x0075:
            java.lang.String r4 = "imgRatio"
            java.lang.String r4 = r1.getQueryParameter(r4)     // Catch:{ Throwable -> 0x00a4 }
            if (r4 == 0) goto L_0x008c
            java.lang.String r4 = "imgRatio"
            java.lang.String r1 = r1.getQueryParameter(r4)     // Catch:{ Throwable -> 0x00a4 }
            java.lang.Float r1 = java.lang.Float.valueOf(r1)     // Catch:{ Throwable -> 0x00a4 }
            float r1 = r1.floatValue()     // Catch:{ Throwable -> 0x00a4 }
            goto L_0x008e
        L_0x008c:
            r1 = 1065353216(0x3f800000, float:1.0)
        L_0x008e:
            float r2 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r2)     // Catch:{ Throwable -> 0x00a4 }
            int r2 = java.lang.Math.round(r2)     // Catch:{ Throwable -> 0x00a4 }
            r5.bg_transparent_margin_top = r2     // Catch:{ Throwable -> 0x00a4 }
            float r2 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r3)     // Catch:{ Throwable -> 0x00a4 }
            int r2 = java.lang.Math.round(r2)     // Catch:{ Throwable -> 0x00a4 }
            r5.bg_transparent_margin_left = r2     // Catch:{ Throwable -> 0x00a4 }
            r5.bg_transparent_img_ratio = r1     // Catch:{ Throwable -> 0x00a4 }
        L_0x00a4:
            android.view.Window r1 = r5.getWindow()     // Catch:{ Throwable -> 0x00b7 }
            r2 = 13
            r1.requestFeature(r2)     // Catch:{ Throwable -> 0x00b7 }
            android.view.Window r1 = r5.getWindow()     // Catch:{ Throwable -> 0x00b7 }
            r2 = 12
            r1.requestFeature(r2)     // Catch:{ Throwable -> 0x00b7 }
            goto L_0x00b9
        L_0x00b7:
            r5.bg_transparent_switch = r0
        L_0x00b9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXActivity.processTransparentBG():void");
    }

    private void prepareAniImage() {
        try {
            this.bg_transparent_image_view = (TUrlImageView) findViewById(R.id.share_element_img);
            this.bg_transparent_bottom_bone = (ImageView) findViewById(R.id.share_element_bone);
            this.bg_transparent_image_bg = findViewById(R.id.share_element_img_bg);
            if (Build.VERSION.SDK_INT >= 21) {
                Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
                if (sharedElementEnterTransition != null) {
                    RatioFeature ratioFeature = new RatioFeature();
                    ratioFeature.setRatio(1.0f / this.bg_transparent_img_ratio);
                    this.bg_transparent_image_view.addFeature(ratioFeature);
                    this.bg_transparent_bottom_bone.setLayoutParams(new LinearLayout.LayoutParams(-1, Math.round(WXViewUtils.getRealPxByWidth(312.0f))));
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
                    layoutParams.leftMargin = this.bg_transparent_margin_left;
                    layoutParams.rightMargin = this.bg_transparent_margin_left;
                    layoutParams.topMargin = this.bg_transparent_margin_top;
                    this.bg_transparent_image_bg.setLayoutParams(layoutParams);
                    this.bg_transparent_image_bg.setVisibility(0);
                    this.bg_transparent_image_view.setImageUrl(this.bg_transparent_img_url);
                    sharedElementEnterTransition.addListener(new Transition.TransitionListener() {
                        public void onTransitionPause(Transition transition) {
                        }

                        public void onTransitionResume(Transition transition) {
                        }

                        public void onTransitionStart(Transition transition) {
                        }

                        public void onTransitionEnd(Transition transition) {
                            transition.removeListener(this);
                            WXActivity.this.bg_transparent_image_view.postDelayed(new Runnable() {
                                public void run() {
                                    WXActivity.this.hideImageView();
                                }
                            }, 1000);
                        }

                        public void onTransitionCancel(Transition transition) {
                            transition.removeListener(this);
                        }
                    });
                    return;
                }
                return;
            }
            this.bg_transparent_image_bg.setVisibility(8);
        } catch (Throwable unused) {
        }
    }

    public void hideImageView() {
        if (this.bg_transparent_image_bg != null && this.bg_transparent_image_view != null) {
            this.bg_transparent_image_bg.setVisibility(8);
        }
    }

    /* JADX WARNING: type inference failed for: r12v0, types: [com.taobao.weex.WXActivity, android.app.Activity, androidx.appcompat.app.AppCompatActivity] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00be  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initFragmentWithAdapter() {
        /*
            r12 = this;
            android.content.Intent r0 = r12.getIntent()
            if (r0 == 0) goto L_0x0011
            android.content.Intent r0 = r12.getIntent()
            java.lang.String r1 = "wx_options"
            java.io.Serializable r0 = r0.getSerializableExtra(r1)
            goto L_0x0012
        L_0x0011:
            r0 = 0
        L_0x0012:
            r9 = r0
            java.lang.Class<com.alibaba.aliweex.bundle.WeexPageFragment> r0 = com.alibaba.aliweex.bundle.WeexPageFragment.class
            java.lang.String r1 = r12.mWeexUrl
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            r2 = 0
            r10 = 1
            if (r1 != 0) goto L_0x002b
            java.lang.String r1 = r12.mWeexUrl
            java.lang.String r3 = "wh_biz=tm"
            boolean r1 = r1.contains(r3)
            if (r1 == 0) goto L_0x002b
            r1 = 1
            goto L_0x002c
        L_0x002b:
            r1 = 0
        L_0x002c:
            if (r1 != 0) goto L_0x0062
            com.alibaba.aliweex.AliWeex r3 = com.alibaba.aliweex.AliWeex.getInstance()
            com.alibaba.aliweex.IConfigAdapter r3 = r3.getConfigAdapter()
            if (r3 == 0) goto L_0x0062
            java.lang.String r4 = "wx_tm_biz_cfg"
            java.lang.String r5 = "hosts"
            java.lang.String r6 = "pages.tmall.com,pre-wormhole.tmall.com"
            java.lang.String r3 = r3.getConfig(r4, r5, r6)
            java.lang.String r4 = ","
            java.lang.String[] r3 = r3.split(r4)
            int r4 = r3.length
        L_0x0049:
            if (r2 >= r4) goto L_0x0062
            r5 = r3[r2]
            java.lang.String r6 = r12.mWeexUrl
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x005f
            java.lang.String r6 = r12.mWeexUrl
            boolean r5 = r6.contains(r5)
            if (r5 == 0) goto L_0x005f
            r11 = 1
            goto L_0x0063
        L_0x005f:
            int r2 = r2 + 1
            goto L_0x0049
        L_0x0062:
            r11 = r1
        L_0x0063:
            if (r11 == 0) goto L_0x0067
            java.lang.Class<com.alibaba.aliweex.hc.bundle.HCWeexPageFragment> r0 = com.alibaba.aliweex.hc.bundle.HCWeexPageFragment.class
        L_0x0067:
            r2 = r0
            com.taobao.weex.common.WXRenderStrategy r0 = r12.parserRenderStrategy()
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            java.lang.String r1 = "render_strategy"
            java.lang.String r0 = r0.toString()
            r5.put(r1, r0)
            java.lang.String r3 = r12.mWeexUrl
            java.lang.String r4 = r12.mBundleUrl
            r6 = 0
            int r7 = com.taobao.weex.adapter.R.id.wa_plus_root_layout
            r8 = 0
            r1 = r12
            androidx.fragment.app.Fragment r0 = com.alibaba.aliweex.bundle.WeexPageFragment.newInstanceWithUrl(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = (com.alibaba.aliweex.bundle.WeexPageFragment) r0
            r12.mPageFragment = r0
            com.taobao.weex.WXAnalyzerDelegate r0 = r12.mAnalyzerDelegate
            com.alibaba.aliweex.bundle.WeexPageFragment r1 = r12.mPageFragment
            java.lang.String r2 = r12.mBundleUrl
            java.lang.String r3 = r12.mPageName
            com.alibaba.aliweex.bundle.WeexPageFragment$WXRenderListenerAdapter r0 = r12.getWXRenderListenerAdapter(r0, r1, r2, r3)
            com.taobao.weex.WXActivity$WXRenderListenerImp r0 = (com.taobao.weex.WXActivity.WXRenderListenerImp) r0
            r12.listenerAdapter = r0
            com.taobao.weex.WXActivity$WXRenderListenerImp r0 = r12.listenerAdapter
            android.view.ViewTreeObserver$OnGlobalLayoutListener r1 = r12.listener
            r0.setListener(r1)
            com.taobao.weex.WXActivity$WXRenderListenerImp r0 = r12.listenerAdapter
            android.view.ViewTreeObserver r1 = r12.observer
            r0.setObserver(r1)
            com.taobao.weex.WXActivity$WXRenderListenerImp r0 = r12.listenerAdapter
            com.alibaba.android.prefetchx.core.jsmodule.PFJSModuleIntegration r1 = r12.mPrefetchXIntegration
            r0.setPrefetchXProcessor(r1)
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = r12.mPageFragment
            com.taobao.weex.WXActivity$WXRenderListenerImp r1 = r12.listenerAdapter
            r0.setRenderListener(r1)
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = r12.mPageFragment
            r0.setDynamicUrlEnable(r10)
            if (r11 == 0) goto L_0x00c8
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = r12.mPageFragment
            com.alibaba.aliweex.hc.bundle.HCUTPresenter r1 = new com.alibaba.aliweex.hc.bundle.HCUTPresenter
            r1.<init>(r12)
            r0.setUserTrackPresenter(r1)
        L_0x00c8:
            com.taobao.weex.adapter.TBNavBarAdapter r0 = new com.taobao.weex.adapter.TBNavBarAdapter
            r0.<init>(r12)
            r12.mTBNavBarAdapter = r0
            com.taobao.weex.adapter.TBNavBarAdapter r0 = r12.mTBNavBarAdapter
            java.lang.String r1 = r12.mWeexUrl
            r0.setWeexUrl(r1)
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = r12.mPageFragment
            com.taobao.weex.adapter.TBNavBarAdapter r1 = r12.mTBNavBarAdapter
            r0.setNavBarAdapter(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXActivity.initFragmentWithAdapter():void");
    }

    @NonNull
    private WXRenderStrategy parserRenderStrategy() {
        WXRenderStrategy wXRenderStrategy;
        WXRenderStrategy wXRenderStrategy2 = WXRenderStrategy.APPEND_ASYNC;
        try {
            if (TextUtils.isEmpty(this.mBundleUrl)) {
                return wXRenderStrategy2;
            }
            Uri parse = Uri.parse(this.mBundleUrl);
            if (TextUtils.equals(parse.getQueryParameter("__eagle"), Boolean.TRUE.toString())) {
                wXRenderStrategy = WXRenderStrategy.DATA_RENDER_BINARY;
            } else if (!TextUtils.equals(parse.getQueryParameter("__data_render"), Boolean.TRUE.toString())) {
                return wXRenderStrategy2;
            } else {
                wXRenderStrategy = WXRenderStrategy.DATA_RENDER;
            }
            return wXRenderStrategy;
        } catch (Exception e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            return wXRenderStrategy2;
        }
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [android.content.Context, com.taobao.weex.WXActivity, android.app.Activity] */
    private void prepareStatusBar() {
        WXEnvironment.addCustomOptions(STATUSBAR_HEIGHT, Integer.toString(0));
        if (((Boolean) hideStatusBar().first).booleanValue()) {
            View findViewById = findViewById(R.id.wa_plus_root_layout);
            findViewById.setFitsSystemWindows(true);
            WXEnvironment.addCustomOptions(STATUSBAR_HEIGHT, Integer.toString(SystemBarDecorator.getStatusBarHeight(this)));
            switch ((StatusBarTextColor) r0.second) {
                case Dark:
                    new SystemBarDecorator(this).enableImmersiveStatusBar(true);
                    break;
                case Light:
                    new SystemBarDecorator(this).enableImmersiveStatusBar(false);
                    break;
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    if (TextUtils.equals(WXEnvironment.getCustomOptions().get(WXActivity.STATUSBAR_HEIGHT), "-1")) {
                        WXEnvironment.addCustomOptions(WXActivity.STATUSBAR_HEIGHT, Integer.toString(windowInsetsCompat.getSystemWindowInsetTop()));
                    }
                    return windowInsetsCompat.consumeSystemWindowInsets();
                }
            });
        }
    }

    private void constrainStackSize() {
        Activity removeFirst;
        if (needConstrainStackSize()) {
            ACTIVITY_STACK.addLast(this);
            if (exceedStackConstraint() && (removeFirst = ACTIVITY_STACK.removeFirst()) != null) {
                if (removeFirst instanceof WXActivity) {
                    ((WXActivity) removeFirst).finishWithException();
                } else {
                    removeFirst.finish();
                }
            }
        }
    }

    private boolean needConstrainStackSize() {
        return Boolean.TRUE.toString().equals(OrangeConfig.getInstance().getConfig("WXActivityStackSize", "constrainStackSize", "false").toLowerCase());
    }

    private boolean exceedStackConstraint() {
        String config = OrangeConfig.getInstance().getConfig("WXActivityStackSize", "stackSizeThreshold", "UNLIMITED");
        if (TextUtils.equals(config, "UNLIMITED")) {
            return false;
        }
        try {
            if (ACTIVITY_STACK.size() > Integer.parseInt(config)) {
                return true;
            }
            return false;
        } catch (NumberFormatException unused) {
            WXLogUtils.e("Weex orange", "The value of WXActivityStackSize is wrong, which should never happen.");
            return false;
        }
    }

    private void removeActivityFromStack() {
        if (needConstrainStackSize()) {
            ACTIVITY_STACK.remove(this);
        }
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [android.content.Context, com.taobao.weex.WXActivity] */
    private void overwriteWeexUrl() {
        IConfigAdapter configAdapter;
        try {
            if (!TextUtils.isEmpty(this.mWeexUrl)) {
                if ("true".equals(AliWeex.getInstance().getConfigAdapter().getConfig("prefetchx_config", "jsmodule_enable", "false"))) {
                    if (this.mPrefetchXIntegration == null) {
                        this.mPrefetchXIntegration = new PFJSModuleIntegration();
                    }
                    String evolve = this.mPrefetchXIntegration.evolve(this, this.mWeexUrl, this.mBundleUrl);
                    if (!TextUtils.isEmpty(evolve)) {
                        this.mBundleUrl = evolve;
                        this.mWeexUrl = evolve;
                    }
                }
                if (!"true".equals(Uri.parse(this.mWeexUrl).getQueryParameter(NAV_OVERLAY)) && (configAdapter = AliWeex.getInstance().getConfigAdapter()) != null) {
                    String config = configAdapter.getConfig("android_weex_huichang_config", KEY_NAV_TRANSPARENT, "");
                    if (!TextUtils.isEmpty(config)) {
                        String[] split = config.split(",");
                        int length = split.length;
                        int i = 0;
                        while (i < length) {
                            String str = split[i];
                            if (TextUtils.isEmpty(this.mWeexUrl) || !this.mWeexUrl.contains(str)) {
                                i++;
                            } else {
                                this.mWeexUrl = Uri.parse(this.mWeexUrl).buildUpon().appendQueryParameter(NAV_OVERLAY, "true").build().toString();
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            WXLogUtils.w("Unexpected exception on overwrite weex url: " + th.toString(), th);
        }
    }

    private void ensureBoxShadow() {
        try {
            IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
            if (configAdapter != null) {
                String config = configAdapter.getConfig("android_weex_ext_config", KEY_BOX_SHADOW_ENABLED, "true");
                if (!TextUtils.isEmpty(config)) {
                    WXLogUtils.w(TAG, "box-shadow-enabled: " + config);
                    BoxShadowUtil.setBoxShadowEnabled("true".equalsIgnoreCase(config));
                }
            }
        } catch (Throwable th) {
            WXLogUtils.w(TAG, "Unexpected exception on read box-shadow config: " + th.toString());
        }
    }

    @Deprecated
    public WeexPageFragment.WXRenderListenerAdapter getWXRenderListenerAdapter(WXAnalyzerDelegate wXAnalyzerDelegate, WeexPageFragment weexPageFragment) {
        return new WXRenderListenerImp(wXAnalyzerDelegate, weexPageFragment);
    }

    public WeexPageFragment.WXRenderListenerAdapter getWXRenderListenerAdapter(WXAnalyzerDelegate wXAnalyzerDelegate, WeexPageFragment weexPageFragment, String str, String str2) {
        return new WXRenderListenerImp(wXAnalyzerDelegate, weexPageFragment, str, str2);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        WXActivity.super.onActivityResult(i, i2, intent);
        if (this.mPageFragment != null) {
            this.mPageFragment.onActivityResult(i, i2, intent);
        }
    }

    public boolean isImmersiveStatus() {
        return ((Boolean) hideStatusBar().first).booleanValue();
    }

    private boolean isParamValid(Intent intent) {
        Uri data = intent.getData();
        this.mBundleUrl = intent.getStringExtra(Constants.WEEX_BUNDLE_URL);
        this.mWeexUrl = intent.getStringExtra(Constants.WEEX_URL);
        if (TextUtils.isEmpty(this.mBundleUrl) || TextUtils.isEmpty(this.mWeexUrl)) {
            return false;
        }
        String queryParameter = data.getQueryParameter(FROM);
        if (TextUtils.isEmpty(queryParameter)) {
            WXLogUtils.d(TAG, "weex url from:" + queryParameter);
            try {
                HashMap hashMap = new HashMap();
                hashMap.put("weexUrl", TextUtils.isEmpty(this.mWeexUrl) ? getIntent().getStringExtra(Constants.WEEX_URL) : this.mWeexUrl);
                hashMap.put("bundleUrl", TextUtils.isEmpty(this.mBundleUrl) ? getIntent().getStringExtra(Constants.WEEX_BUNDLE_URL) : this.mBundleUrl);
                hashMap.put(UNWRouter.PAGE_NAME, TextUtils.isEmpty(this.mPageName) ? assemblePageName(getIntent().getStringExtra(Constants.WEEX_BUNDLE_URL)) : this.mPageName);
                AppMonitor.Alarm.commitFail("weex", "from_not_nav", JSON.toJSONString(hashMap), "99402", ACTION_DEGRADE_TO_WINDVANE);
            } catch (Throwable unused) {
            }
        }
        WVSchemeIntercepterInterface wVSchemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
        if (wVSchemeIntercepter != null) {
            this.mBundleUrl = wVSchemeIntercepter.dealUrlScheme(this.mBundleUrl);
        }
        WXLogUtils.d(TAG, "bundleUrl:" + this.mBundleUrl);
        WXLogUtils.d(TAG, "weexUrl:" + this.mWeexUrl);
        return true;
    }

    private boolean isEnvironmentReady() {
        boolean isDegrade = TBWXConfigManger.getInstance().isDegrade();
        boolean isCPUSupport = WXEnvironment.isCPUSupport();
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d(TAG, "Debug 模式下强制开启support=true,原始的WXEnvironment.isCPUSupport()为" + isCPUSupport);
            isCPUSupport = true;
        }
        boolean isInitialized = WXSDKEngine.isInitialized();
        WXLogUtils.d(TAG, "degrade:" + isDegrade + " support:" + isCPUSupport + " init:" + isInitialized);
        if (isDegrade || !isCPUSupport || !isInitialized) {
            return false;
        }
        boolean isSupporTablet = TBWXConfigManger.getInstance().isSupporTablet();
        WXLogUtils.d(TAG, "degrade:" + isDegrade + " support:" + isCPUSupport + " init:" + isInitialized);
        return true;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        supportRequestWindowFeature(8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004c, code lost:
        getWindow().setFormat(-3);
        com.taobao.weex.WXActivity.super.onCreate(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0056, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0033 */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void chooseAppBarMode(android.os.Bundle r4) {
        /*
            r3 = this;
            boolean r0 = r3.useWXappbar()
            if (r0 == 0) goto L_0x000f
            int r0 = com.taobao.weex.adapter.R.style.Theme_NoBackgroundAndTitle_NoActionBar
            r3.setTheme(r0)
            com.taobao.weex.WXActivity.super.onCreate(r4)
            goto L_0x004b
        L_0x000f:
            r0 = 8
            r1 = -3
            boolean r2 = r3.isAppBarOverlay()     // Catch:{ Exception -> 0x0033 }
            r3.actionBarOverlay = r2     // Catch:{ Exception -> 0x0033 }
            boolean r2 = r3.isMainHc()     // Catch:{ Exception -> 0x0033 }
            r3.isMainHc = r2     // Catch:{ Exception -> 0x0033 }
            boolean r2 = r3.actionBarOverlay     // Catch:{ Exception -> 0x0033 }
            if (r2 != 0) goto L_0x002b
            boolean r2 = r3.isMainHc     // Catch:{ Exception -> 0x0033 }
            if (r2 == 0) goto L_0x0027
            goto L_0x002b
        L_0x0027:
            r3.supportRequestWindowFeature(r0)     // Catch:{ Exception -> 0x0033 }
            goto L_0x0036
        L_0x002b:
            r2 = 9
            r3.supportRequestWindowFeature(r2)     // Catch:{ Exception -> 0x0033 }
            goto L_0x0036
        L_0x0031:
            r0 = move-exception
            goto L_0x004c
        L_0x0033:
            r3.supportRequestWindowFeature(r0)     // Catch:{ all -> 0x0031 }
        L_0x0036:
            android.view.Window r0 = r3.getWindow()
            r0.setFormat(r1)
            com.taobao.weex.WXActivity.super.onCreate(r4)
            androidx.appcompat.app.ActionBar r4 = r3.getSupportActionBar()
            if (r4 == 0) goto L_0x004b
            java.lang.String r0 = "手机淘宝"
            r4.setTitle((java.lang.CharSequence) r0)
        L_0x004b:
            return
        L_0x004c:
            android.view.Window r2 = r3.getWindow()
            r2.setFormat(r1)
            com.taobao.weex.WXActivity.super.onCreate(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXActivity.chooseAppBarMode(android.os.Bundle):void");
    }

    private void showPredefinedAppbar() {
        if (useWXappbar()) {
            View findViewById = findViewById(R.id.weex_appbar);
            if (findViewById instanceof ImageButton) {
                ImageButton imageButton = (ImageButton) findViewById;
                imageButton.setVisibility(0);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        WXActivity.this.finish();
                    }
                });
            }
        }
    }

    private void hideAppBar() {
        try {
            String queryParameter = Uri.parse(this.mWeexUrl).getQueryParameter(NAV_HIDDEN);
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null && TextUtils.equals(queryParameter, Boolean.toString(true))) {
                supportActionBar.hide();
            }
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
        }
    }

    private void setScreenCaptureEnabledOrNot() {
        if (!TextUtils.isEmpty(this.mWeexUrl)) {
            try {
                if ("true".equalsIgnoreCase(Uri.parse(this.mWeexUrl).getQueryParameter(WX_SECURE))) {
                    getWindow().addFlags(8192);
                } else {
                    getWindow().clearFlags(8192);
                }
            } catch (Throwable th) {
                WXLogUtils.e(TAG, th);
            }
        }
    }

    public boolean isMainHc() {
        try {
            if (TextUtils.equals(Uri.parse(this.mWeexUrl).getQueryParameter("wx_main_hc"), Boolean.toString(true))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
            return false;
        }
    }

    private boolean isAppBarOverlay() {
        try {
            if (TextUtils.equals(Uri.parse(this.mWeexUrl).getQueryParameter(NAV_OVERLAY), Boolean.toString(true))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean useWXappbar() {
        try {
            if (TextUtils.equals(Uri.parse(this.mWeexUrl).getQueryParameter(WX_APPBAR), Boolean.toString(true))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            return false;
        }
    }

    private Pair<Boolean, StatusBarTextColor> hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                String queryParameter = Uri.parse(this.mWeexUrl).getQueryParameter(WX_STATUSBAR_HIDDEN);
                if (TextUtils.equals(queryParameter, Boolean.toString(true))) {
                    return new Pair<>(true, StatusBarTextColor.Undefine);
                }
                if (TextUtils.equals(queryParameter, HIDDEN_STATUS_BAR_WITH_DARK_TEXT)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        return new Pair<>(true, StatusBarTextColor.Dark);
                    }
                    return new Pair<>(false, StatusBarTextColor.Dark);
                } else if (TextUtils.equals(queryParameter, HIDDEN_STATUS_BAR_WITH_LIGHT_TEXT)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        return new Pair<>(true, StatusBarTextColor.Light);
                    }
                    return new Pair<>(false, StatusBarTextColor.Light);
                }
            } catch (Exception e) {
                WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            }
        }
        return new Pair<>(false, StatusBarTextColor.Undefine);
    }

    /* JADX WARNING: type inference failed for: r14v0, types: [android.content.Context, java.lang.Object, com.taobao.weex.WXActivity] */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0087, code lost:
        r2 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b0, code lost:
        if (android.text.TextUtils.equals(r10, r11) != false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b2, code lost:
        r8.put("processorOriginalUrl", r11);
        r8.put("pageFragmentOriginalUrl", r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c4, code lost:
        if (android.text.TextUtils.isEmpty(r14.mWeexUrl) == false) goto L_0x00d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c6, code lost:
        r3 = getIntent().getStringExtra(com.taobao.weex.constant.Constants.WEEX_URL);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d1, code lost:
        r3 = r14.mWeexUrl;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00d3, code lost:
        r8.put("weexUrl", r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00de, code lost:
        if (android.text.TextUtils.isEmpty(r14.mBundleUrl) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e0, code lost:
        r3 = getIntent().getStringExtra(com.taobao.weex.constant.Constants.WEEX_BUNDLE_URL);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00eb, code lost:
        r3 = r14.mBundleUrl;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00ed, code lost:
        r8.put("bundleUrl", r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00f8, code lost:
        if (android.text.TextUtils.isEmpty(r14.mPageName) == false) goto L_0x0109;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00fa, code lost:
        r3 = assemblePageName(getIntent().getStringExtra(com.taobao.weex.constant.Constants.WEEX_BUNDLE_URL));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0109, code lost:
        r3 = r14.mPageName;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x010b, code lost:
        r8.put(alimama.com.unwrouter.UNWRouter.PAGE_NAME, r3);
        com.alibaba.mtl.appmonitor.AppMonitor.Alarm.commitFail("weex", "degrade_to_windvane", com.alibaba.fastjson.JSON.toJSONString(r8), r2, ACTION_DEGRADE_TO_WINDVANE);
        com.taobao.weex.utils.WXLogUtils.d(TAG, "degradeToWindVane:[DEGRADE_TYPE_PARAMS_ERROR][processorOriginalUrl:" + r11 + "][pageFragmentOriginalUrl:" + r10 + com.taobao.weex.el.parse.Operators.ARRAY_END_STR);
     */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x016d  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void degradeToWindVane(android.content.Intent r15) {
        /*
            r14 = this;
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            com.alibaba.aliweex.bundle.WeexPageFragment r2 = r14.mPageFragment
            r9 = 1
            if (r2 == 0) goto L_0x0167
            com.alibaba.aliweex.bundle.WeexPageFragment r2 = r14.mPageFragment     // Catch:{ Throwable -> 0x0166 }
            java.lang.String r10 = r2.getOriginalUrl()     // Catch:{ Throwable -> 0x0166 }
            java.lang.String r2 = com.taobao.weex.constant.Constants.WEEX_BUNDLE_URL     // Catch:{ Throwable -> 0x0166 }
            java.lang.String r2 = r15.getStringExtra(r2)     // Catch:{ Throwable -> 0x0166 }
            java.lang.String r11 = com.taobao.weex.adapter.TBWXNavPreProcessor.getOriginalUrl(r2)     // Catch:{ Throwable -> 0x0166 }
            java.lang.String r2 = "degrade_type"
            java.lang.String r12 = r15.getStringExtra(r2)     // Catch:{ Throwable -> 0x0166 }
            java.lang.String r1 = "degrade_msg"
            java.lang.String r1 = r15.getStringExtra(r1)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r2 = r14.mWeexUrl     // Catch:{ Throwable -> 0x0164 }
            com.taobao.weex.utils.WXExceptionUtils.degradeUrl = r2     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r2 = "99600"
            r3 = -1
            int r4 = r12.hashCode()     // Catch:{ Throwable -> 0x0164 }
            r5 = -556841547(0xffffffffdecf45b5, float:-7.4677714E18)
            if (r4 == r5) goto L_0x0059
            r5 = 1343170219(0x500f26ab, float:9.6067041E9)
            if (r4 == r5) goto L_0x004f
            r5 = 1606541870(0x5fc1e22e, float:2.794156E19)
            if (r4 == r5) goto L_0x0045
            goto L_0x0062
        L_0x0045:
            java.lang.String r4 = "DEGRADE_TYPE_JS_ERROR"
            boolean r4 = r12.equals(r4)     // Catch:{ Throwable -> 0x0164 }
            if (r4 == 0) goto L_0x0062
            r3 = 2
            goto L_0x0062
        L_0x004f:
            java.lang.String r4 = "DEGRADE_TYPE_PARAMS_ERROR"
            boolean r4 = r12.equals(r4)     // Catch:{ Throwable -> 0x0164 }
            if (r4 == 0) goto L_0x0062
            r3 = 1
            goto L_0x0062
        L_0x0059:
            java.lang.String r4 = "DEGRADE_TYPE_INIT_ERROR"
            boolean r4 = r12.equals(r4)     // Catch:{ Throwable -> 0x0164 }
            if (r4 == 0) goto L_0x0062
            r3 = 0
        L_0x0062:
            switch(r3) {
                case 0: goto L_0x0089;
                case 1: goto L_0x0069;
                case 2: goto L_0x0066;
                default: goto L_0x0065;
            }     // Catch:{ Throwable -> 0x0164 }
        L_0x0065:
            goto L_0x00ac
        L_0x0066:
            java.lang.String r2 = "99603"
            goto L_0x00ac
        L_0x0069:
            java.lang.String r13 = "99602"
            java.lang.String r2 = r14.mBundleUrl     // Catch:{ Throwable -> 0x0164 }
            r3 = 0
            com.taobao.weex.common.WXErrorCode r4 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = "degradeToH5"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0164 }
            r6.<init>()     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r7 = "degradeToH5"
            r6.append(r7)     // Catch:{ Throwable -> 0x0164 }
            r6.append(r1)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0164 }
            r7 = r8
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionWithDefaultUrl(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0164 }
        L_0x0087:
            r2 = r13
            goto L_0x00ac
        L_0x0089:
            java.lang.String r13 = "99601"
            java.lang.String r2 = com.taobao.weex.constant.Constants.WEEX_BUNDLE_URL     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r2 = r15.getStringExtra(r2)     // Catch:{ Throwable -> 0x0164 }
            r3 = 0
            com.taobao.weex.common.WXErrorCode r4 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = "degradeToH5"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0164 }
            r6.<init>()     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r7 = "degradeToH5"
            r6.append(r7)     // Catch:{ Throwable -> 0x0164 }
            r6.append(r1)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0164 }
            r7 = r8
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionWithDefaultUrl(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0164 }
            goto L_0x0087
        L_0x00ac:
            boolean r1 = android.text.TextUtils.equals(r10, r11)     // Catch:{ Throwable -> 0x013f }
            if (r1 != 0) goto L_0x0163
            java.lang.String r1 = "processorOriginalUrl"
            r8.put(r1, r11)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r1 = "pageFragmentOriginalUrl"
            r8.put(r1, r10)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r1 = "weexUrl"
            java.lang.String r3 = r14.mWeexUrl     // Catch:{ Throwable -> 0x013f }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x013f }
            if (r3 == 0) goto L_0x00d1
            android.content.Intent r3 = r14.getIntent()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r4 = com.taobao.weex.constant.Constants.WEEX_URL     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = r3.getStringExtra(r4)     // Catch:{ Throwable -> 0x013f }
            goto L_0x00d3
        L_0x00d1:
            java.lang.String r3 = r14.mWeexUrl     // Catch:{ Throwable -> 0x013f }
        L_0x00d3:
            r8.put(r1, r3)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r1 = "bundleUrl"
            java.lang.String r3 = r14.mBundleUrl     // Catch:{ Throwable -> 0x013f }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x013f }
            if (r3 == 0) goto L_0x00eb
            android.content.Intent r3 = r14.getIntent()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r4 = com.taobao.weex.constant.Constants.WEEX_BUNDLE_URL     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = r3.getStringExtra(r4)     // Catch:{ Throwable -> 0x013f }
            goto L_0x00ed
        L_0x00eb:
            java.lang.String r3 = r14.mBundleUrl     // Catch:{ Throwable -> 0x013f }
        L_0x00ed:
            r8.put(r1, r3)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r1 = "pageName"
            java.lang.String r3 = r14.mPageName     // Catch:{ Throwable -> 0x013f }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x013f }
            if (r3 == 0) goto L_0x0109
            android.content.Intent r3 = r14.getIntent()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r4 = com.taobao.weex.constant.Constants.WEEX_BUNDLE_URL     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = r3.getStringExtra(r4)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = r14.assemblePageName(r3)     // Catch:{ Throwable -> 0x013f }
            goto L_0x010b
        L_0x0109:
            java.lang.String r3 = r14.mPageName     // Catch:{ Throwable -> 0x013f }
        L_0x010b:
            r8.put(r1, r3)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r1 = "weex"
            java.lang.String r3 = "degrade_to_windvane"
            java.lang.String r4 = com.alibaba.fastjson.JSON.toJSONString(r8)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r5 = "degradeToWindVane"
            com.alibaba.mtl.appmonitor.AppMonitor.Alarm.commitFail(r1, r3, r4, r2, r5)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r1 = "WXActivity"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013f }
            r2.<init>()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = "degradeToWindVane:[DEGRADE_TYPE_PARAMS_ERROR][processorOriginalUrl:"
            r2.append(r3)     // Catch:{ Throwable -> 0x013f }
            r2.append(r11)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = "][pageFragmentOriginalUrl:"
            r2.append(r3)     // Catch:{ Throwable -> 0x013f }
            r2.append(r10)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r3 = "]"
            r2.append(r3)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x013f }
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Throwable -> 0x013f }
            goto L_0x0163
        L_0x013f:
            r1 = move-exception
            java.lang.String r2 = "WXActivity"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0164 }
            r3.<init>()     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = "degradeToWindVane:[DEGRADE_TYPE_PARAMS_ERROR][processorOriginalUrl:"
            r3.append(r4)     // Catch:{ Throwable -> 0x0164 }
            r3.append(r11)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = "][pageFragmentOriginalUrl:"
            r3.append(r4)     // Catch:{ Throwable -> 0x0164 }
            r3.append(r10)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = "]"
            r3.append(r4)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0164 }
            android.util.Log.e(r2, r3, r1)     // Catch:{ Throwable -> 0x0164 }
        L_0x0163:
            r0 = r10
        L_0x0164:
            r1 = r12
            goto L_0x0167
        L_0x0166:
        L_0x0167:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x0173
            java.lang.String r0 = com.taobao.weex.constant.Constants.WEEX_URL
            java.lang.String r0 = r15.getStringExtra(r0)
        L_0x0173:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x0241
            java.lang.String r2 = "?"
            boolean r2 = r0.contains(r2)
            if (r2 == 0) goto L_0x0184
            java.lang.String r2 = "&disableAB=1&hybrid=true&_wx_f_=2"
            goto L_0x0186
        L_0x0184:
            java.lang.String r2 = "?disableAB=1&hybrid=true&_wx_f_=2"
        L_0x0186:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            r3.append(r2)
            java.lang.String r0 = r3.toString()
            java.lang.String r2 = "WXActivity"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "degrade url:"
            r3.append(r4)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r2, (java.lang.String) r3)
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            java.lang.String r3 = "unvalid"
            java.lang.String r4 = "1"
            r2.put(r3, r4)
            java.lang.String r3 = "weex"
            java.lang.String r4 = "1"
            r2.put(r3, r4)
            com.ut.mini.UTAnalytics r3 = com.ut.mini.UTAnalytics.getInstance()
            com.ut.mini.UTTracker r3 = r3.getDefaultTracker()
            r3.updatePageProperties(r14, r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "degrade to H5: ["
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = "] weexUrl:"
            r2.append(r1)
            java.lang.String r1 = r14.mWeexUrl
            r2.append(r1)
            java.lang.String r1 = ", bundleUrl:"
            r2.append(r1)
            java.lang.String r1 = r14.mBundleUrl
            r2.append(r1)
            java.lang.String r1 = ", h5:"
            r2.append(r1)
            r2.append(r0)
            java.lang.String r1 = r2.toString()
            java.lang.String r2 = "h5Url"
            r8.put(r2, r0)
            java.lang.String r2 = "WXActivity"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.String) r1)
            int r15 = r15.getFlags()
            r1 = 33554432(0x2000000, float:9.403955E-38)
            if (r15 != r1) goto L_0x0226
            com.taobao.android.nav.Nav r15 = com.taobao.android.nav.Nav.from(r14)
            java.lang.String r2 = "com.taobao.intent.category.HYBRID_UI"
            com.taobao.android.nav.Nav r15 = r15.withCategory(r2)
            com.taobao.android.nav.Nav r15 = r15.withFlags(r1)
            com.taobao.android.nav.Nav r15 = r15.skipPreprocess()
            com.taobao.android.nav.Nav r15 = r15.disableTransition()
            com.taobao.android.nav.Nav r15 = r15.disallowLoopback()
            r15.toUri((java.lang.String) r0)
            goto L_0x023f
        L_0x0226:
            com.taobao.android.nav.Nav r15 = com.taobao.android.nav.Nav.from(r14)
            java.lang.String r1 = "com.taobao.intent.category.HYBRID_UI"
            com.taobao.android.nav.Nav r15 = r15.withCategory(r1)
            com.taobao.android.nav.Nav r15 = r15.skipPreprocess()
            com.taobao.android.nav.Nav r15 = r15.disableTransition()
            com.taobao.android.nav.Nav r15 = r15.disallowLoopback()
            r15.toUri((java.lang.String) r0)
        L_0x023f:
            r14.mIsDegrade = r9
        L_0x0241:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXActivity.degradeToWindVane(android.content.Intent):void");
    }

    private String assemblePageName(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Uri.parse(str).buildUpon().clearQuery().build().toString();
            }
        } catch (Exception unused) {
        }
        return str;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onReceiveTouchEvent(motionEvent);
        }
        if (this.wxSecurityGuardPageTrack != null) {
            this.wxSecurityGuardPageTrack.addTouchEventReflection(this.mBundleUrl, motionEvent);
        }
        return WXActivity.super.dispatchTouchEvent(motionEvent);
    }

    public boolean onSupportNavigateUp() {
        if (processOnSupportNavigateUp()) {
            return true;
        }
        return WXActivity.super.onSupportNavigateUp();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        WXActivity.super.onStart();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onStart();
        }
        if (this.wxSecurityGuardPageTrack != null) {
            this.wxSecurityGuardPageTrack.onPageStartReflection(this.mBundleUrl);
        }
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXActivity] */
    /* access modifiers changed from: protected */
    public void onResume() {
        WXActivity.super.onResume();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onResume();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mDegradeReceive, new IntentFilter(ACTION_DEGRADE_TO_WINDVANE));
        if (this.isMainHc) {
            setNaviTransparent(this.mMainHcNaviShow);
        } else if (this.actionBarOverlay) {
            View findViewById = findViewById(R.id.action_bar_container);
            if (findViewById != null) {
                findViewById.setBackgroundColor(getResources().getColor(17170445));
            }
            View findViewById2 = findViewById(R.id.action_bar);
            if (findViewById2 != null) {
                findViewById2.setBackgroundColor(getResources().getColor(17170445));
            }
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXActivity] */
    /* access modifiers changed from: protected */
    public void onPause() {
        WXActivity.super.onPause();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onPause();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mDegradeReceive);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        WXActivity.super.onStop();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onStop();
        }
    }

    public void finishWithException() {
        try {
            if (!AfcLifeCycleCenter.isLauncherStart) {
                WXInitConfigManager.ConfigKV configKV = WXInitConfigManager.getInstance().c_back_to_home;
                if (Boolean.parseBoolean(WXInitConfigManager.getInstance().tryGetConfigFromSpAndOrange(configKV.group, configKV.key, configKV.defaultValue))) {
                    WXActivity.super.finish(true);
                    if (this.mIsDegrade) {
                        overridePendingTransition(0, 0);
                        return;
                    }
                    return;
                }
            }
        } catch (Throwable unused) {
        }
        finish();
    }

    public void finish() {
        WXActivity.super.finish();
        if (this.mIsDegrade) {
            overridePendingTransition(0, 0);
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXActivity, android.app.Activity] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001d */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0021 A[Catch:{ Throwable -> 0x0026 }] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003f A[Catch:{ Exception -> 0x004f }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDestroy() {
        /*
            r2 = this;
            com.taobao.weex.WXActivity.super.onDestroy()
            r2.removeActivityFromStack()
            com.taobao.weex.WXAnalyzerDelegate r0 = r2.mAnalyzerDelegate
            if (r0 == 0) goto L_0x000f
            com.taobao.weex.WXAnalyzerDelegate r0 = r2.mAnalyzerDelegate
            r0.onDestroy()
        L_0x000f:
            com.taobao.android.tracker.TrackerManager r0 = r2.trackerManager     // Catch:{ Throwable -> 0x001d }
            if (r0 == 0) goto L_0x001d
            com.taobao.android.tracker.TrackerManager r0 = r2.trackerManager     // Catch:{ Throwable -> 0x001d }
            java.lang.String r1 = r2.mBundleUrl     // Catch:{ Throwable -> 0x001d }
            r0.unInit(r2, r1)     // Catch:{ Throwable -> 0x001d }
            r0 = 0
            r2.trackerManager = r0     // Catch:{ Throwable -> 0x001d }
        L_0x001d:
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = r2.mPageFragment     // Catch:{ Throwable -> 0x0026 }
            if (r0 == 0) goto L_0x0026
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = r2.mPageFragment     // Catch:{ Throwable -> 0x0026 }
            r0.onDestroy()     // Catch:{ Throwable -> 0x0026 }
        L_0x0026:
            com.taobao.weex.WXSecurityGuardPageTrack r0 = r2.wxSecurityGuardPageTrack
            if (r0 == 0) goto L_0x0031
            com.taobao.weex.WXSecurityGuardPageTrack r0 = r2.wxSecurityGuardPageTrack
            java.lang.String r1 = r2.mBundleUrl
            r0.onPageDestroyReflection(r1)
        L_0x0031:
            boolean r0 = com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ Exception -> 0x004f }
            if (r0 == 0) goto L_0x0056
            com.taobao.weex.WXActivity$WXRenderListenerImp r0 = r2.listenerAdapter     // Catch:{ Exception -> 0x004f }
            android.view.ViewTreeObserver r0 = r0.getObserver()     // Catch:{ Exception -> 0x004f }
            if (r0 == 0) goto L_0x0056
            com.taobao.weex.WXActivity$WXRenderListenerImp r0 = r2.listenerAdapter     // Catch:{ Exception -> 0x004f }
            android.view.ViewTreeObserver r0 = r0.getObserver()     // Catch:{ Exception -> 0x004f }
            com.taobao.weex.WXActivity$WXRenderListenerImp r1 = r2.listenerAdapter     // Catch:{ Exception -> 0x004f }
            android.view.ViewTreeObserver$OnGlobalLayoutListener r1 = r1.getListener()     // Catch:{ Exception -> 0x004f }
            r0.removeGlobalOnLayoutListener(r1)     // Catch:{ Exception -> 0x004f }
            goto L_0x0056
        L_0x004f:
            java.lang.String r0 = "WXActivity"
            java.lang.String r1 = "weex test-id remove listener error!"
            android.util.Log.e(r0, r1)
        L_0x0056:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXActivity.onDestroy():void");
    }

    public void onBackPressed() {
        if (!processOnBackPressed()) {
            WXActivity.super.onBackPressed();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return processOnBackPressed() || WXActivity.super.onKeyDown(i, keyEvent);
        }
        if (this.wxSecurityGuardPageTrack != null) {
            this.wxSecurityGuardPageTrack.addKeyEventReflection(this.mBundleUrl, keyEvent);
        }
        return WXActivity.super.onKeyDown(i, keyEvent);
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, java.lang.Object, com.taobao.weex.WXActivity, android.app.Activity] */
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean onCreateOptionsMenu = WXActivity.super.onCreateOptionsMenu(menu);
        if (this.mTBNavBarAdapter != null && !this.mTBNavBarAdapter.isMainHC() && ((!useWXappbar() || (useWXappbar() && WXTBUtil.hasFestival())) && !isIgnoreFestival() && !this.mTBNavBarAdapter.hasSetNavBarColor())) {
            FestivalMgr.getInstance().setBgUI4Actionbar(this, TBActionBar.ActionBarStyle.NORMAL);
        }
        boolean z = true;
        try {
            Field declaredField = getClass().getSuperclass().getSuperclass().getDeclaredField("mNeedPublicMenuShow");
            declaredField.setAccessible(true);
            z = ((Boolean) declaredField.get(this)).booleanValue();
        } catch (Exception e) {
            try {
                WXLogUtils.d("error in get mNeedPublicMenuShow from BaseActivity", (Throwable) e);
            } catch (Exception e2) {
                WXLogUtils.d(TAG, "error in find overflow menu button. " + e2.getMessage());
            }
        }
        if (z) {
            if (menu == null || menu.findItem(R.id.uik_menu_overflow) == null) {
                menu = new TBPublicMenu(this).onCreateOptionsMenu(getMenuInflater(), menu);
            }
            if (menu.findItem(R.id.uik_menu_overflow) == null || menu.findItem(R.id.uik_menu_overflow).getActionView() == null) {
                this.overflowButton = getPublicMenu().getCustomOverflow();
                getPublicMenu().setCustomOverflow(this.overflowButton);
            } else {
                this.overflowButton = (TBActionView) menu.findItem(R.id.uik_menu_overflow).getActionView();
            }
        }
        return onCreateOptionsMenu;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return WXActivity.super.onOptionsItemSelected(menuItem);
        }
        return processOnBackPressed() || WXActivity.super.onOptionsItemSelected(menuItem);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXActivity] */
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        WXActivity.super.onRequestPermissionsResult(i, strArr, iArr);
        Intent intent = new Intent(WXModule.ACTION_REQUEST_PERMISSIONS_RESULT);
        intent.putExtra("requestCode", i);
        intent.putExtra(WXModule.PERMISSIONS, strArr);
        intent.putExtra(WXModule.GRANT_RESULTS, iArr);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public boolean processOnBackPressed() {
        return this.mPageFragment != null && !this.mPageFragment.isDetached() && this.mPageFragment.onBackPressed();
    }

    public boolean processOnSupportNavigateUp() {
        return this.mPageFragment != null && !this.mPageFragment.isDetached() && this.mPageFragment.onSupportNavigateUp();
    }

    public void setNaviTransparent(boolean z) {
        if (isMainHc()) {
            this.mMainHcNaviShow = z;
            if (this.mFakeTitle == null) {
                this.mFakeTitle = (ImageView) findViewById(R.id.fake_title);
            }
            View findViewById = findViewById(R.id.action_bar);
            if (z) {
                ViewGroup.LayoutParams layoutParams = this.mFakeTitle.getLayoutParams();
                if (findViewById != null) {
                    layoutParams.height = findViewById.getHeight();
                    this.mFakeTitle.setLayoutParams(layoutParams);
                }
                this.mFakeTitle.setVisibility(0);
            } else {
                this.mFakeTitle.setVisibility(8);
            }
            View findViewById2 = findViewById(R.id.action_bar_container);
            int i = z ? 17170443 : 17170445;
            if (findViewById2 != null) {
                findViewById2.setBackgroundColor(getResources().getColor(i));
            }
            if (findViewById != null) {
                findViewById.setBackgroundColor(getResources().getColor(i));
            }
        }
    }

    public void onLowMemory() {
        WXActivity.super.onLowMemory();
        if (this.mPrefetchXIntegration != null) {
            this.mPrefetchXIntegration.onLowMemory(this.mBundleUrl);
        }
    }

    public static class WXRenderListenerImp extends WeexPageFragment.WXRenderListenerAdapter {
        boolean layoutChangeSignal = true;
        ViewTreeObserver.OnGlobalLayoutListener listener;
        WXAnalyzerDelegate mAnalyzerDelegate;
        String mBundleUrlForTrack;
        Map<String, String> mIDMap = new ArrayMap();
        WXSDKInstance mInstance;
        private PFJSModuleIntegration mPFJSModuleIntegration;
        String mPageNameForTrack;
        Handler mWXHandler = new Handler() {
            public void handleMessage(Message message) {
                Log.d("TestHandlerActivity", "handling message");
                WXRenderListenerImp.this.layoutChangeSignal = true;
            }
        };
        WeexPageFragment mWeexPageFragment;
        ViewTreeObserver observer;

        public WXRenderListenerImp(WXAnalyzerDelegate wXAnalyzerDelegate, WeexPageFragment weexPageFragment) {
            this.mAnalyzerDelegate = wXAnalyzerDelegate;
            this.mWeexPageFragment = weexPageFragment;
        }

        public WXRenderListenerImp(WXAnalyzerDelegate wXAnalyzerDelegate, WeexPageFragment weexPageFragment, String str, String str2) {
            this.mAnalyzerDelegate = wXAnalyzerDelegate;
            this.mWeexPageFragment = weexPageFragment;
            this.mBundleUrlForTrack = str;
            this.mPageNameForTrack = str2;
        }

        public View onCreateView(WXSDKInstance wXSDKInstance, View view) {
            this.observer = view.getViewTreeObserver();
            this.mInstance = wXSDKInstance;
            if (WXEnvironment.isApkDebugable() && (wXSDKInstance.getContext() instanceof WXActivity)) {
                this.listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (WXRenderListenerImp.this.layoutChangeSignal) {
                            WXRenderListenerImp.this.layoutChangeSignal = false;
                            new collectIdTask().execute(new Integer[0]);
                        }
                    }
                };
                this.observer.addOnGlobalLayoutListener(this.listener);
            }
            View onWeexViewCreated = this.mAnalyzerDelegate != null ? this.mAnalyzerDelegate.onWeexViewCreated(wXSDKInstance, view) : null;
            return onWeexViewCreated == null ? view : onWeexViewCreated;
        }

        public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
            View findViewById;
            WXLogUtils.d(WXActivity.TAG, "into--[onViewCreated]");
            if (wXSDKInstance.getContext() instanceof WXActivity) {
                WXActivity wXActivity = (WXActivity) wXSDKInstance.getContext();
                if (wXActivity.useWXappbar() && (findViewById = wXActivity.findViewById(R.id.weex_appbar)) != null) {
                    findViewById.setVisibility(8);
                }
            }
            wXSDKInstance.registerOnWXScrollListener(new WXSecurityGuardPageTrack.OnWXScrollListenerEX(this.mBundleUrlForTrack, wXSDKInstance.getContext()));
        }

        public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
            WXLogUtils.d(WXActivity.TAG, "into--[onRenderSuccess]");
            if (this.mAnalyzerDelegate != null) {
                this.mAnalyzerDelegate.onWeexRenderSuccess(wXSDKInstance);
            }
            AppMonitor.Alarm.commitSuccess("weex", "renderResult", generateAppMonitorArgs());
        }

        public void onException(WXSDKInstance wXSDKInstance, boolean z, String str, String str2) {
            View findViewById;
            super.onException(wXSDKInstance, z, str, str2);
            if (this.mAnalyzerDelegate != null) {
                this.mAnalyzerDelegate.onException(wXSDKInstance, str, str2);
            }
            try {
                if (this.mPFJSModuleIntegration != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", this.mBundleUrlForTrack);
                    hashMap.put(UNWRouter.PAGE_NAME, this.mPageNameForTrack);
                    android.util.Pair<String, String> degenerate = this.mPFJSModuleIntegration.degenerate(Globals.getApplication(), wXSDKInstance.getBundleUrl(), hashMap);
                    if (degenerate != null) {
                        this.mWeexPageFragment.replace((String) degenerate.first, (String) degenerate.second);
                        return;
                    }
                }
            } catch (Throwable th) {
                String str3 = this.mBundleUrlForTrack;
                AppMonitor.Alarm.commitFail("PrefetchX", PFConstant.JSModule.TAG_BUNDLE, str3, "-52002", "prefetchx degenerate exception : " + th.getMessage() + " | " + WXLogUtils.getStackTrace(th));
            }
            if (z) {
                if (this.mWeexPageFragment != null) {
                    this.mWeexPageFragment.destroyWeex();
                }
                Intent intent = new Intent(WXActivity.ACTION_DEGRADE_TO_WINDVANE);
                intent.putExtra(Constants.WEEX_BUNDLE_URL, wXSDKInstance.getBundleUrl());
                intent.putExtra(WXActivity.DEGRADE_TYPE, WXActivity.DEGRADE_TYPE_JS_ERROR);
                intent.putExtra(WXActivity.DEGRADE_MSG, "降级到h5 Instance创建失败或者网络错误ErrorCode= " + str + "详细错误信息\n" + str2);
                LocalBroadcastManager.getInstance(wXSDKInstance.getContext()).sendBroadcast(intent);
                AppMonitor.Alarm.commitFail("weex", "renderResult", generateAppMonitorArgs(), "99301", str2);
            }
            if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_ERR_NETWORK_BUNDLE_DOWNLOAD_FAILED.getErrorCode())) {
                try {
                    final TBErrorView tBErrorView = new TBErrorView(wXSDKInstance.getContext());
                    Error newError = Error.Factory.newError("ANDROID_SYS_NETWORK_ERROR", "网络错误,请稍后再试");
                    newError.url = wXSDKInstance.getBundleUrl();
                    tBErrorView.setError(newError);
                    tBErrorView.setButton(TBErrorView.ButtonType.BUTTON_LEFT, "刷新", new View.OnClickListener() {
                        public void onClick(View view) {
                            if (WXRenderListenerImp.this.mWeexPageFragment != null && !TextUtils.isEmpty(WXRenderListenerImp.this.mWeexPageFragment.getOriginalRenderUrl()) && !TextUtils.isEmpty(WXRenderListenerImp.this.mWeexPageFragment.getOriginalUrl())) {
                                ViewParent parent = tBErrorView.getParent();
                                if (parent instanceof ViewGroup) {
                                    ((ViewGroup) parent).removeView(tBErrorView);
                                }
                                WXRenderListenerImp.this.mWeexPageFragment.replace(WXRenderListenerImp.this.mWeexPageFragment.getOriginalUrl(), WXRenderListenerImp.this.mWeexPageFragment.getOriginalRenderUrl());
                            }
                        }
                    });
                    tBErrorView.setButtonVisibility(TBErrorView.ButtonType.BUTTON_RIGHT, 8);
                    View view = this.mWeexPageFragment.getView();
                    if (view instanceof ViewGroup) {
                        ((ViewGroup) view).addView(tBErrorView);
                        ViewParent parent = view.getParent();
                        if ((parent instanceof ViewGroup) && (findViewById = ((ViewGroup) parent).findViewById(com.alibaba.aliweex.R.id.weex_render_view)) != null) {
                            findViewById.setVisibility(4);
                        }
                    }
                    if (this.mWeexPageFragment.getView() != null) {
                        View findViewById2 = this.mWeexPageFragment.getView().findViewById(R.id.wa_content_error_root);
                        if (findViewById2 != null) {
                            findViewById2.setVisibility(4);
                        } else if ((this.mWeexPageFragment.getView() instanceof FrameLayout) && ((FrameLayout) this.mWeexPageFragment.getView()).getChildCount() > 0) {
                            for (int i = 0; i < ((FrameLayout) this.mWeexPageFragment.getView()).getChildCount(); i++) {
                                if (((FrameLayout) this.mWeexPageFragment.getView()).getChildAt(i) instanceof RelativeLayout) {
                                    ((FrameLayout) this.mWeexPageFragment.getView()).getChildAt(i).setVisibility(4);
                                    return;
                                }
                            }
                        }
                    }
                } catch (Throwable th2) {
                    WXLogUtils.e("error in render network failure view of TBErrorView", th2);
                }
            }
        }

        private String generateAppMonitorArgs() {
            HashMap hashMap = new HashMap();
            hashMap.put("url", this.mBundleUrlForTrack);
            hashMap.put(UNWRouter.PAGE_NAME, this.mPageNameForTrack);
            return JSON.toJSONString(hashMap);
        }

        private static void collectId(WXComponent wXComponent, Map<String, String> map) {
            View hostView;
            if (wXComponent != null) {
                String str = (String) wXComponent.getAttrs().get("testId");
                if (!(str == null || (hostView = wXComponent.getHostView()) == null)) {
                    if (!map.containsKey(str)) {
                        android.util.Pair<String, Integer> nextID = Utility.nextID();
                        hostView.setId(((Integer) nextID.second).intValue());
                        map.put(str, nextID.first);
                        WXLogUtils.d(WXActivity.TAG, "In collectId! viewId = " + nextID.second + " testId = " + str + " viewName = " + ((String) nextID.first));
                    } else {
                        Integer nativeID = Utility.getNativeID(map.get(str));
                        if (nativeID.intValue() != -1) {
                            hostView.setId(nativeID.intValue());
                            WXLogUtils.d(WXActivity.TAG, "In collectId! viewId = " + nativeID + " testId = " + str + " viewName = " + map.get(str));
                        } else {
                            WXLogUtils.d(WXActivity.TAG, "CollectId failed! viewId = " + nativeID + " testId = " + str + " viewName = " + map.get(str));
                        }
                    }
                }
                if (wXComponent instanceof WXVContainer) {
                    WXVContainer wXVContainer = (WXVContainer) wXComponent;
                    for (int childCount = wXVContainer.getChildCount() - 1; childCount >= 0; childCount--) {
                        collectId(wXVContainer.getChild(childCount), map);
                    }
                }
            }
        }

        /* access modifiers changed from: private */
        public void collectIDMap() {
            if (this.mInstance.getContext() instanceof WXActivity) {
                View findViewById = ((WXActivity) this.mInstance.getContext()).findViewById(R.id.container_test_id);
                collectId(this.mInstance.getRootComponent(), this.mIDMap);
                Log.d(WXActivity.TAG, JSON.toJSONString(this.mIDMap));
                if (findViewById != null) {
                    findViewById.setContentDescription(JSON.toJSONString(this.mIDMap));
                }
            }
        }

        public void setListener(ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            this.listener = onGlobalLayoutListener;
        }

        public void setObserver(ViewTreeObserver viewTreeObserver) {
            this.observer = viewTreeObserver;
        }

        public void setPrefetchXProcessor(PFJSModuleIntegration pFJSModuleIntegration) {
            this.mPFJSModuleIntegration = pFJSModuleIntegration;
        }

        public ViewTreeObserver getObserver() {
            return this.observer;
        }

        public ViewTreeObserver.OnGlobalLayoutListener getListener() {
            return this.listener;
        }

        class collectIdTask extends AsyncTask<Integer, Integer, String> {
            collectIdTask() {
            }

            /* access modifiers changed from: protected */
            public void onPreExecute() {
                super.onPreExecute();
            }

            /* access modifiers changed from: protected */
            public String doInBackground(Integer... numArr) {
                try {
                    WXRenderListenerImp.this.collectIDMap();
                    Thread.sleep(1000);
                    return "执行完毕";
                } catch (Exception unused) {
                    return "执行完毕";
                }
            }

            /* access modifiers changed from: protected */
            public void onProgressUpdate(Integer... numArr) {
                super.onProgressUpdate(numArr);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String str) {
                WXRenderListenerImp.this.mWXHandler.sendEmptyMessage(1);
                super.onPostExecute(str);
            }
        }
    }

    private String generateAppMonitorArgs() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("url", TextUtils.isEmpty(this.mBundleUrl) ? getIntent().getStringExtra(Constants.WEEX_BUNDLE_URL) : this.mBundleUrl);
            hashMap.put(UNWRouter.PAGE_NAME, TextUtils.isEmpty(this.mPageName) ? assemblePageName(getIntent().getStringExtra(Constants.WEEX_BUNDLE_URL)) : this.mPageName);
            return JSON.toJSONString(hashMap);
        } catch (Exception unused) {
            return "";
        }
    }

    public void addFeedCallBackInfo(String str, String str2) {
        this.mWeexUserInfo = str2;
    }

    @Keep
    public void setPageUserInfo(String str) {
        WXLogUtils.d(TAG, "setPageUserInfo:" + str);
        this.mPageUserInfo = str;
    }

    public Bundle pageUserInfo() {
        String str = this.mPageUserInfo;
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(str)) {
            Bundle bundle2 = new Bundle();
            bundle2.putString(UNWRouter.PAGE_NAME, this.mBundleUrl);
            if (!TextUtils.isEmpty(this.mWeexUserInfo)) {
                bundle2.putString("extraInfo", buildExtraInfo());
            }
            bundle.putParcelable("ZSUserHelper", bundle2);
        } else {
            Bundle bundle3 = new Bundle();
            try {
                JSONObject jSONObject = new JSONObject(this.mPageUserInfo);
                if (!TextUtils.isEmpty(this.mWeexUserInfo)) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("extraInfo");
                    if (jSONObject2 == null) {
                        jSONObject2 = new JSONObject();
                        jSONObject.put("extraInfo", jSONObject2);
                    }
                    warpExtraInfo(jSONObject2);
                }
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    bundle3.putString(next, jSONObject.optString(next));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            bundle.putParcelable("ZSUserHelper", bundle3);
        }
        return bundle;
    }

    private void warpExtraInfo(JSONObject jSONObject) throws JSONException {
        if (!TextUtils.isEmpty(this.mWeexUserInfo) && jSONObject != null) {
            jSONObject.put("weexInfo", this.mWeexUserInfo == null ? "" : this.mWeexUserInfo);
        }
    }

    private String buildExtraInfo() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("weexInfo", this.mWeexUserInfo == null ? "" : this.mWeexUserInfo);
            return jSONObject.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private void pageStart() {
        this.mTimeContainerStart = System.currentTimeMillis();
    }

    public void pageFinishCommit() {
        String str;
        ContainerMonitor monitor;
        try {
            str = ContainerMonitor.getUrlKey(Uri.parse(this.mBundleUrl));
        } catch (Throwable unused) {
            str = null;
        }
        if (!TextUtils.isEmpty(str) && (monitor = ContainerMonitor.monitor()) != null && this.hasCommited.compareAndSet(false, true)) {
            monitor.fsTime(str, System.currentTimeMillis() - this.mTimeContainerStart);
            monitor.commitData(str);
        }
    }
}
