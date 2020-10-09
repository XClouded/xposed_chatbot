package com.alimama.moon.web;

import alimama.com.unwetaologger.base.ErrorContent;
import alimama.com.unwetaologger.base.UNWLogTracer;
import alimama.com.unwetaologger.base.UNWLogger;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.features.feedback.FeedbackActivity;
import com.alimama.moon.network.login.TaoBaoUrlFilter;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.ui.CommonLoadingView;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.utils.PageRouterUtil;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.moon.view.SegmentedGroup;
import com.alimama.moon.web.IWebContract;
import com.alimama.moon.web.NavTabParam;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.alimama.unionwl.uiframe.views.base.ISPtrFrameLayout;
import com.alimama.unionwl.uiframe.views.base.ISPtrHeaderView;
import com.alimama.unionwl.utils.LocalDisplay;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebResourceRequest;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.RenderProcessGoneDetail;
import com.uc.webview.export.media.MessageID;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTPageStatus;
import in.srain.cube.ptr.PtrDefaultHandler;
import in.srain.cube.ptr.PtrFrameLayout;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebActivity extends BaseActivity implements IWebContract.IWebView {
    private static final String EVENT_INFO_MENU_ITEM_CLICKED = "infoMenuItemClicked";
    public static final String EXTRA_URI = "com.alimama.moon.web.WebActivity.EXTRA_URI";
    private static final int INFO_MENU_ITEM_ID = 1;
    protected static final String TAG = "WebActivity";
    public static final String TYPE_JS = "setCustomPageTitle";
    public static final String TYPE_WEB = "WebActivity";
    public static boolean isChangeTitle = false;
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) WebActivity.class);
    private final int CAN_PULL_TO_REFRESH_OF_MIN_Y_VALUE = 1;
    private ImageView backBtn;
    /* access modifiers changed from: private */
    public boolean canPullToRefresh;
    private TextView closeBtn;
    @Nullable
    private MenuItemParam infoMenuItemParam;
    @Inject
    ILogin login;
    private CommonLoadingView mLoadingView;
    /* access modifiers changed from: private */
    public NavTabParam mNavTabParam;
    private ISPtrFrameLayout mPtrFrameLayout;
    private RelativeLayout mSegmentedContainer;
    /* access modifiers changed from: private */
    public SegmentedGroup mSegmentedGroup;
    private String mUrl;
    /* access modifiers changed from: private */
    public IWebContract.IWebPresenter presenter;
    private String title;
    private FrameLayout webviewContent;
    private TextView webviewTitle;
    /* access modifiers changed from: private */
    public WVUCWebView wvWebView;

    public void closeLoadingView() {
    }

    public void showloadingView() {
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (this.infoMenuItemParam == null) {
            return super.onPrepareOptionsMenu(menu);
        }
        MenuItem add = menu.add(0, 1, 0, this.infoMenuItemParam.getIconTitle());
        if (!TextUtils.isEmpty(this.infoMenuItemParam.getIconUrl())) {
            add.setIcon(new BitmapDrawable(getResources(), this.infoMenuItemParam.getIcon()));
        } else {
            add.setTitle(this.infoMenuItemParam.getIconTitle());
        }
        add.setShowAsAction(1);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.infoMenuItemParam == null) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (1 != menuItem.getItemId()) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.wvWebView.fireEvent(EVENT_INFO_MENU_ITEM_CLICKED, JSON.toJSONString(this.infoMenuItemParam));
        return true;
    }

    public void setPresenter(IWebContract.IWebPresenter iWebPresenter) {
        this.presenter = iWebPresenter;
    }

    public void showPreviousWebPage() {
        if (this.mSegmentedGroup != null && this.mSegmentedGroup.getChildCount() != 0) {
            finish();
        } else if (this.wvWebView.canGoBack()) {
            this.wvWebView.goBack();
        } else {
            finish();
        }
    }

    public void updateClipBoard() {
        if (Build.VERSION.SDK_INT > 28) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!TextUtils.isEmpty(CommonUtils.getClipboardContent())) {
                        PageRouterUtil.CLIP_BOARD_CONTENT_BEFORE_GO_TO_PAGE = CommonUtils.getClipboardContent();
                    }
                    CommonUtils.resumeContentToClipboard(PageRouterUtil.CLIP_BOARD_CONTENT_BEFORE_GO_TO_PAGE);
                }
            }, 500);
        }
    }

    public void closeWebPage() {
        finish();
    }

    public void showCloseBtn() {
        this.closeBtn.setVisibility(0);
    }

    public void hideCloseBtn() {
        this.closeBtn.setVisibility(8);
    }

    public void showShareUI(String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.wvWebView.getCurrentUrl();
        }
        ShareUtils.showShare(this, str);
    }

    public void showLoginUI() {
        this.login.showLoginChooserUI();
    }

    public void showFeedBackUI() {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    public void showOriginalWebPage() {
        this.wvWebView.loadUrl(this.mUrl);
    }

    public void showNewWebView(String str) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.setData(Uri.parse(str));
        startActivity(intent);
    }

    public void changeTitle(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            this.webviewTitle.setText(str);
        }
        if (!TextUtils.isEmpty(str2) && TextUtils.equals(str2, TYPE_JS)) {
            isChangeTitle = true;
        }
        updateClipBoard();
    }

    public void refreshCurrentWebPage() {
        this.wvWebView.refresh();
    }

    public void onPageLoadFinished() {
        if (this.mPtrFrameLayout != null) {
            this.mPtrFrameLayout.refreshComplete();
        }
    }

    private static class FancyWebChromeClient extends WVUCWebChromeClient {
        private final IWebContract.IWebPresenter presenter;

        public FancyWebChromeClient(Context context, IWebContract.IWebPresenter iWebPresenter) {
            super(context);
            this.presenter = iWebPresenter;
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            this.presenter.onProgressChanged(webView, i);
        }
    }

    private static class FancyWebViewClient extends WVUCWebViewClient {
        final ErrorContent errorContent = new ErrorContent();
        private final IWebContract.IWebPresenter presenter;
        final UNWLogTracer tracer = new UNWLogTracer();

        public FancyWebViewClient(Context context, IWebContract.IWebPresenter iWebPresenter) {
            super(context);
            this.presenter = iWebPresenter;
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            this.presenter.onPageStarted(webView, str, bitmap);
            this.errorContent.setType(UNWLogger.LOG_VALUE_TYPE_PROCESS);
            this.errorContent.setSubType(UNWLogger.LOG_VALUE_SUBTYPE_H5);
            this.errorContent.setName("WebActivity");
            this.errorContent.setTracer(this.tracer);
            this.tracer.append("onPageStarted", str);
            WebActivity.isChangeTitle = false;
        }

        public void onPageFinished(WebView webView, String str) {
            if (!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
            this.presenter.onPageFinished(Boolean.valueOf(webView.canGoBack()), str);
            if (!WebActivity.isChangeTitle) {
                this.presenter.receivePageTitle(webView.getTitle());
            }
            super.onPageFinished(webView, str);
        }

        public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
            return super.onRenderProcessGone(webView, renderProcessGoneDetail);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            this.presenter.onReceivedError(webView, i, str, str2);
            NewMonitorLogger.WebView.onReceivedError("WebActivity", this.errorContent, i, str, str2);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            try {
                if (TaoBaoUrlFilter.isHitUserTrack(str)) {
                    return true;
                }
                if (TaoBaoUrlFilter.isHitShare(str)) {
                    Uri parse = Uri.parse(str);
                    WebActivity.logger.info("openShareUI");
                    this.presenter.openShareUI(parse.getQueryParameter("targetUrl"));
                    return true;
                } else if (TaoBaoUrlFilter.isHitOnLogin(str)) {
                    WebActivity.logger.info("openLoginUI, url: {}", (Object) str);
                    this.presenter.openLoginUI();
                    return true;
                } else if (TaoBaoUrlFilter.isHitCloseWindow(str)) {
                    WebActivity.logger.info("clickCloseBtn");
                    this.presenter.clickCloseBtn();
                    return true;
                } else if (TaoBaoUrlFilter.isHitFeedback(str)) {
                    WebActivity.logger.info("openFeedBack");
                    this.presenter.openFeedBack();
                    return true;
                } else {
                    Boolean valueOf = Boolean.valueOf(Uri.parse(str).getBooleanQueryParameter("intercept", true));
                    if (!TextUtils.isEmpty(TaoBaoUrlFilter.extractTqgDetailUrl(str)) && valueOf.booleanValue()) {
                        this.presenter.openTaokeDetailUI(Uri.parse(str).buildUpon().appendQueryParameter("intercept", "false").build().toString());
                        return true;
                    } else if (!TextUtils.isEmpty(TaoBaoUrlFilter.extractTqgNotStartedDetailUrl(str)) && valueOf.booleanValue()) {
                        this.presenter.openTaokeDetailUI(Uri.parse(str).buildUpon().appendQueryParameter("intercept", "false").build().toString());
                        return true;
                    } else if (TextUtils.isEmpty(TaoBaoUrlFilter.extractTttjDetailUrl(str)) || !valueOf.booleanValue()) {
                        if (TaoBaoUrlFilter.isHitAuctionUrl(str) && valueOf.booleanValue()) {
                            this.presenter.openTaokeDetailUI(Uri.parse(str).buildUpon().appendQueryParameter("intercept", "false").build().toString());
                            return true;
                        }
                        return super.shouldOverrideUrlLoading(webView, str);
                    } else {
                        this.presenter.openTaokeDetailUI(Uri.parse(str).buildUpon().appendQueryParameter("intercept", "false").build().toString());
                        return true;
                    }
                }
            } catch (Exception e) {
                WebActivity.logger.error("handle shouldOverrideUrlLoading exception: {}", (Object) e.getMessage());
                NewMonitorLogger.WebView.onInterceptException("WebActivity", e.toString(), str);
            }
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            return super.shouldInterceptRequest(webView, str);
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
            NewMonitorLogger.WebView.onReceivedSslError("WebActivity", this.errorContent, webView, sslError);
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        App.getAppComponent().inject(this);
        UTAnalytics.getInstance().getDefaultTracker().updatePageStatus(this, UTPageStatus.UT_H5_IN_WebView);
        if (bundle != null) {
            this.mUrl = bundle.getString(EXTRA_URI);
        } else {
            Uri data = getIntent().getData();
            if (data != null) {
                this.title = data.getQueryParameter("title");
                this.mUrl = data.toString();
                if ("unionApp".equals(data.getScheme())) {
                    this.mUrl = data.getQueryParameter("url");
                }
            }
        }
        if (UnionLensUtil.isUnionLensReport()) {
            try {
                this.mUrl = UnionLensUtil.appendUrlUnionLens(this.mUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new WebPresenter(this);
        setContentView((int) R.layout.activity_web);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), false);
        this.backBtn = (ImageView) findViewById(R.id.btn_back);
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WebActivity.this.presenter.clickBackBtn();
            }
        });
        this.closeBtn = (TextView) findViewById(R.id.btn_close);
        this.closeBtn.setVisibility(8);
        this.closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WebActivity.this.presenter.clickCloseBtn();
            }
        });
        this.webviewTitle = (TextView) findViewById(R.id.webview_title);
        this.mSegmentedContainer = (RelativeLayout) findViewById(R.id.rl_segmented_container);
        changeTitle(this.title, "WebActivity");
        this.mPtrFrameLayout = (ISPtrFrameLayout) findViewById(R.id.pull_to_refresh_layout);
        ISPtrHeaderView iSPtrHeaderView = new ISPtrHeaderView(this);
        this.mPtrFrameLayout.initView(iSPtrHeaderView, iSPtrHeaderView);
        this.mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                WebActivity.this.refreshCurrentWebPage();
            }

            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                if (!WebActivity.this.canPullToRefresh || WebActivity.this.wvWebView.getCoreView().getScrollY() > 1) {
                    return false;
                }
                return super.checkCanDoRefresh(ptrFrameLayout, view, view2);
            }
        });
        this.webviewContent = (FrameLayout) findViewById(R.id.content_web);
        this.wvWebView = new WVUCWebView(this);
        this.wvWebView.getSettings().setJavaScriptEnabled(true);
        this.wvWebView.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.wvWebView.getSettings().setMixedContentMode(0);
        }
        this.webviewContent.addView(this.wvWebView, new FrameLayout.LayoutParams(-1, -1));
        this.wvWebView.setWebViewClient(new FancyWebViewClient(this, this.presenter));
        this.wvWebView.setWebChromeClient(new FancyWebChromeClient(this, this.presenter));
        if (!TextUtils.isEmpty(this.mUrl)) {
            this.wvWebView.loadUrl(this.mUrl);
        } else {
            NewMonitorLogger.WebView.onEmptyUrl("WebActivity", "加载空url");
        }
        this.canPullToRefresh = false;
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        logger.info("onRestart");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(this, UTHelper.UT_WEBVIEW_PAGE_NAME);
        HashMap hashMap = new HashMap();
        hashMap.put("prePvid", UnionLensUtil.lastPrePvid);
        hashMap.put("selfPvid", UnionLensUtil.prePvid);
        UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(hashMap);
        updateClipBoard();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(this);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        logger.info(MessageID.onStop);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        logger.info("onSaveInstanceState");
        if (!TextUtils.isEmpty(this.mUrl)) {
            bundle.putString(EXTRA_URI, this.mUrl);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
        if (this.wvWebView != null) {
            if (this.webviewContent != null) {
                this.webviewContent.removeView(this.wvWebView);
            }
            this.wvWebView.destroy();
            this.wvWebView = null;
        }
    }

    public void onBackPressed() {
        showPreviousWebPage();
    }

    public void clearToolbarMenu() {
        this.infoMenuItemParam = null;
        invalidateOptionsMenu();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.wvWebView != null) {
            this.wvWebView.onActivityResult(i, i2, intent);
        }
    }

    public void loadToolbarMenu(@NonNull MenuItemParam menuItemParam) {
        this.infoMenuItemParam = menuItemParam;
        String iconUrl = menuItemParam.getIconUrl();
        if (TextUtils.isEmpty(iconUrl)) {
            invalidateOptionsMenu();
        } else {
            ImageLoader.getInstance().loadImage(iconUrl, new MenuItemIconLoadingListener());
        }
    }

    public void updatePullToRefreshSwitch(boolean z) {
        this.canPullToRefresh = z;
    }

    public void onMenuItemIconLoaded(@NonNull Bitmap bitmap) {
        if (this.infoMenuItemParam == null) {
            logger.warn("menu item icon loaded without menu params");
            return;
        }
        this.infoMenuItemParam.setIcon(bitmap);
        invalidateOptionsMenu();
    }

    private static class MenuItemIconLoadingListener extends SimpleImageLoadingListener {
        private final WeakReference<WebActivity> webActivityWeakReference;

        private MenuItemIconLoadingListener(@NonNull WebActivity webActivity) {
            this.webActivityWeakReference = new WeakReference<>(webActivity);
        }

        public void onLoadingComplete(String str, View view, Bitmap bitmap) {
            WebActivity webActivity = (WebActivity) this.webActivityWeakReference.get();
            if (webActivity != null) {
                webActivity.onMenuItemIconLoaded(bitmap);
            }
        }
    }

    public void loadNavTabMenu(@NonNull NavTabParam navTabParam) {
        this.mNavTabParam = navTabParam;
        if (this.mSegmentedGroup != null) {
            this.mSegmentedContainer.removeView(this.mSegmentedGroup);
        }
        if (navTabParam.items != null && navTabParam.items.length != 0) {
            this.mSegmentedGroup = new SegmentedGroup(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, LocalDisplay.dp2px(29.0f));
            this.mSegmentedGroup.setOrientation(0);
            this.mSegmentedContainer.addView(this.mSegmentedGroup, layoutParams);
            NavTabParam.Tab[] tabArr = navTabParam.items;
            for (int i = 0; i < tabArr.length; i++) {
                RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button_item, (ViewGroup) null);
                radioButton.setText(tabArr[i].title);
                radioButton.setId(i);
                this.mSegmentedGroup.addView(radioButton);
            }
            this.mSegmentedGroup.updateBackground();
            ((RadioButton) this.mSegmentedGroup.getChildAt(navTabParam.defaultSelect)).setChecked(true);
            this.mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    WebActivity.this.mSegmentedGroup.updateBackground();
                    NavTabParam.Tab tab = WebActivity.this.mNavTabParam.items[i];
                    if (!TextUtils.isEmpty(tab.url)) {
                        WebActivity.this.wvWebView.loadUrl(tab.url);
                    }
                }
            });
        }
    }
}
