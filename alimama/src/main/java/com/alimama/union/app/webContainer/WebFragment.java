package com.alimama.union.app.webContainer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.config.OrangeConfigCenterManager;
import com.alimama.moon.config.model.MidH5TabModel;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.moon.usertrack.MidH5TabUTHelper;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.unionwl.uiframe.views.base.ISPtrFrameLayout;
import com.alimama.unionwl.uiframe.views.base.ISPtrHeaderView;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebResourceRequest;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebView;
import com.ut.mini.UTAnalytics;
import in.srain.cube.ptr.PtrDefaultHandler;
import in.srain.cube.ptr.PtrFrameLayout;

public class WebFragment extends Fragment implements IBottomNavFragment, IWebFragmentInterface {
    public static final String PAGE_NAME = "Page_tblm_lmapp_mid_h5_tab";
    private static final String SPM_CNT = "a21wq.14028885";
    private final int CAN_PULL_TO_REFRESH_OF_MIN_Y_VALUE = 1;
    private Context mContext;
    private String mTitle = "好物圈";
    private String mUrl = "https://mo.m.taobao.com/union/haowuquan";
    private ISPtrFrameLayout ptrFrameLayout;
    private FrameLayout webviewContent;
    /* access modifiers changed from: private */
    public WVUCWebView wvWebView;

    public void willBeDisplayed() {
        UTHelper.sendControlHit(PAGE_NAME, "showMidH5Tab");
        UnionLensUtil.generatePrepvid();
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getFragContext(), PAGE_NAME);
    }

    public void willBeHidden() {
        SpmProcessor.pageDisappear(getFragContext(), SPM_CNT);
    }

    public void refresh() {
        if (this.wvWebView != null) {
            this.wvWebView.refresh();
        }
    }

    public String currFragmentTitle() {
        return this.mTitle;
    }

    public void onPageLoadFinished() {
        if (this.ptrFrameLayout != null) {
            this.ptrFrameLayout.refreshComplete();
        }
    }

    public static WebFragment newInstance() {
        return new WebFragment();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_web, viewGroup, false);
        this.ptrFrameLayout = (ISPtrFrameLayout) inflate.findViewById(R.id.pull_refresh_layout);
        this.webviewContent = (FrameLayout) inflate.findViewById(R.id.content_web_fragment);
        initData();
        return inflate;
    }

    private Context getFragContext() {
        if (getActivity() != null) {
            this.mContext = getActivity();
        } else {
            this.mContext = App.sApplication;
        }
        return this.mContext;
    }

    public void onResume() {
        super.onResume();
    }

    private void initData() {
        MidH5TabModel mideH5TabModel = OrangeConfigCenterManager.getInstance().getMideH5TabModel();
        if (mideH5TabModel != null) {
            this.mTitle = mideH5TabModel.getTitle();
            this.mUrl = mideH5TabModel.getSchema();
            MidH5TabUTHelper.midH5PageOperate(MidH5TabUTHelper.RENDER_MID_TAB_CONTROL_NAME + mideH5TabModel.getType());
        }
        try {
            this.wvWebView = new WVUCWebView(getFragContext());
            this.wvWebView.getSettings().setJavaScriptEnabled(true);
            this.wvWebView.getSettings().setLoadsImagesAutomatically(true);
            if (Build.VERSION.SDK_INT >= 21) {
                this.wvWebView.getSettings().setMixedContentMode(0);
            }
            this.webviewContent.addView(this.wvWebView, new FrameLayout.LayoutParams(-1, -1));
            this.wvWebView.setWebViewClient(new FancyWebViewClient(getFragContext(), this));
            this.wvWebView.setWebChromeClient(new FancyWebChromeClient(getFragContext()));
            this.wvWebView.getWvUIModel().enableShowLoading();
            if (UnionLensUtil.isUnionLensReport()) {
                this.mUrl = UnionLensUtil.appendUrlUnionLens(this.mUrl);
            }
            if (!TextUtils.isEmpty(this.mUrl)) {
                this.wvWebView.loadUrl(this.mUrl);
            }
            ISPtrHeaderView iSPtrHeaderView = new ISPtrHeaderView(getFragContext());
            this.ptrFrameLayout.initView(iSPtrHeaderView, iSPtrHeaderView);
            this.ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
                public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                    if (WebFragment.this.wvWebView.getCoreView().getScrollY() > 1) {
                        return false;
                    }
                    return super.checkCanDoRefresh(ptrFrameLayout, WebFragment.this.wvWebView, view2);
                }

                public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                    MidH5TabUTHelper.midH5PageOperate(MidH5TabUTHelper.REFRESH_MID_H5_PAGE_CONTROL_NAME + OrangeConfigCenterManager.getInstance().getMideH5TabModel().getType());
                    WebFragment.this.refresh();
                }
            });
        } catch (Exception unused) {
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.wvWebView != null) {
            if (this.webviewContent != null) {
                this.webviewContent.removeView(this.wvWebView);
            }
            this.wvWebView.destroy();
            this.wvWebView = null;
        }
    }

    private static class FancyWebChromeClient extends WVUCWebChromeClient {
        public FancyWebChromeClient(Context context) {
            super(context);
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }

    private static class FancyWebViewClient extends WVUCWebViewClient {
        IWebFragmentInterface mWebFragment;

        public FancyWebViewClient(Context context, IWebFragmentInterface iWebFragmentInterface) {
            super(context);
            this.mWebFragment = iWebFragmentInterface;
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        public void onPageFinished(WebView webView, String str) {
            if (!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
            this.mWebFragment.onPageLoadFinished();
            super.onPageFinished(webView, str);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return super.shouldOverrideUrlLoading(webView, str);
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            return super.shouldInterceptRequest(webView, str);
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }
    }
}
