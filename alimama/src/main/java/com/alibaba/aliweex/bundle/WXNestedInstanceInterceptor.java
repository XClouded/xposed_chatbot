package com.alibaba.aliweex.bundle;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.taobao.windvane.webview.WVSchemeInterceptService;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.text.TextUtils;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;
import com.alibaba.aliweex.utils.WXPrefetchUtil;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.utils.WXUtils;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WXNestedInstanceInterceptor implements WXSDKInstance.NestedInstanceInterceptor {
    protected Handler mHandler;
    protected ArrayList<NestedInfo> mNestedInfos = new ArrayList<>();

    public WXNestedInstanceInterceptor(Context context, Handler handler) {
        this.mHandler = handler;
    }

    public void onCreateNestInstance(WXSDKInstance wXSDKInstance, NestedContainer nestedContainer) {
        EmbedEventListener embedEventListener = new EmbedEventListener(wXSDKInstance.getContext(), this.mHandler);
        embedEventListener.setWXSDKIntance(wXSDKInstance);
        nestedContainer.setOnNestEventListener(embedEventListener);
        this.mNestedInfos.add(new NestedInfo(embedEventListener, nestedContainer));
    }

    /* access modifiers changed from: package-private */
    public ArrayList<NestedInfo> getNestedInfos() {
        return this.mNestedInfos;
    }

    /* access modifiers changed from: package-private */
    public NestedContainer getNestedContainer(WXSDKInstance wXSDKInstance) {
        if (this.mNestedInfos == null) {
            return null;
        }
        Iterator<NestedInfo> it = this.mNestedInfos.iterator();
        while (it.hasNext()) {
            NestedInfo next = it.next();
            if (next.mEventListener.getInstance() == wXSDKInstance) {
                return next.mNestedContainer;
            }
        }
        return null;
    }

    public void destroy() {
        if (this.mNestedInfos != null) {
            Iterator<NestedInfo> it = this.mNestedInfos.iterator();
            while (it.hasNext()) {
                NestedInfo next = it.next();
                if (next.mEventListener != null) {
                    next.mEventListener.destroy();
                }
            }
            this.mNestedInfos.clear();
            this.mNestedInfos = null;
        }
    }

    public static class NestedInfo {
        public EmbedEventListener mEventListener;
        public NestedContainer mNestedContainer;

        public NestedInfo(EmbedEventListener embedEventListener, NestedContainer nestedContainer) {
            this.mEventListener = embedEventListener;
            this.mNestedContainer = nestedContainer;
        }
    }

    public static class EmbedEventListener extends WXEmbed.ClickToReloadListener {
        private static String WH_WX = "wh_weex";
        private static String WX_TPL = "_wx_tpl";
        private WXSDKInstance instance;
        private boolean isDegrade = false;
        /* access modifiers changed from: private */
        public Context mEmbContext;
        private Handler mHandler;
        Object mPFJSModuleIntegration = null;
        private WVUCWebView mUCWebView;
        String thinBundleUrl = "";

        /* access modifiers changed from: package-private */
        public void setWXSDKIntance(WXSDKInstance wXSDKInstance) {
            this.instance = wXSDKInstance;
        }

        /* access modifiers changed from: package-private */
        public WXSDKInstance getInstance() {
            return this.instance;
        }

        protected EmbedEventListener(Context context, Handler handler) {
            this.mEmbContext = context;
            this.mHandler = handler;
        }

        public boolean onPreCreate(NestedContainer nestedContainer, String str) {
            if (!UrlValidate.isValid(str)) {
                return false;
            }
            if (UrlValidate.shouldShowInvalidUrlTips(str) && this.mHandler != null) {
                this.mHandler.sendEmptyMessage(18);
            }
            ViewGroup viewContainer = nestedContainer.getViewContainer();
            if ((viewContainer.getChildAt(0) instanceof ProgressBar) || this.isDegrade) {
                return true;
            }
            ProgressBar progressBar = new ProgressBar(viewContainer.getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 17;
            progressBar.setLayoutParams(layoutParams);
            viewContainer.removeAllViews();
            viewContainer.addView(progressBar);
            return true;
        }

        public String transformUrl(String str) {
            String realSrc = getRealSrc(str);
            if (this.instance != null) {
                realSrc = WXPrefetchUtil.handleUrl(this.instance, realSrc);
            }
            Pair<String, Object> evolve = WXPrefetchUtil.evolve(this.instance != null ? this.instance.getContext() : null, realSrc, realSrc);
            if (evolve == null) {
                return realSrc;
            }
            String str2 = (String) evolve.first;
            this.mPFJSModuleIntegration = evolve.second;
            this.thinBundleUrl = str2;
            return str2;
        }

        private String getRealSrc(String str) {
            WVSchemeIntercepterInterface wVSchemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
            if (wVSchemeIntercepter != null) {
                str = wVSchemeIntercepter.dealUrlScheme(str);
            }
            Uri parse = Uri.parse(str);
            if (parse == null || !parse.isHierarchical()) {
                return null;
            }
            if (parse.getBooleanQueryParameter(WH_WX, false)) {
                return str;
            }
            String queryParameter = parse.getQueryParameter(WX_TPL);
            if (TextUtils.isEmpty(queryParameter)) {
                return null;
            }
            Uri.Builder buildUpon = Uri.parse(queryParameter).buildUpon();
            Set<String> queryParameterNames = parse.getQueryParameterNames();
            if (queryParameterNames != null && queryParameterNames.size() > 0) {
                for (String next : queryParameterNames) {
                    if (next != WX_TPL) {
                        buildUpon.appendQueryParameter(next, parse.getQueryParameter(next));
                    }
                }
            }
            return buildUpon.toString();
        }

        public void onCreated(NestedContainer nestedContainer, WXSDKInstance wXSDKInstance) {
            super.onCreated(nestedContainer, wXSDKInstance);
            nestedContainer.renderNewURL(WXUtil.ERROR_BUNDLE_URL);
        }

        public void onException(NestedContainer nestedContainer, String str, String str2) {
            boolean z;
            WVUCWebView wVUCWebView;
            boolean shouldDegrade = this.instance != null ? WeexPageFragment.shouldDegrade(this.instance, str, str2) : false;
            if (this.mPFJSModuleIntegration != null && !TextUtils.isEmpty(this.thinBundleUrl) && !shouldDegrade) {
                Map<String, Object> degenerate = WXPrefetchUtil.degenerate(this.instance != null ? this.instance.getContext() : null, this.mPFJSModuleIntegration, this.thinBundleUrl);
                if (!(degenerate == null || degenerate.get("fatBundleUrl") == null)) {
                    ((WXEmbed) nestedContainer).renderNewURL(degenerate.get("fatBundleUrl").toString());
                    this.thinBundleUrl = null;
                }
            }
            if (shouldDegrade) {
                ViewGroup viewContainer = nestedContainer.getViewContainer();
                if (nestedContainer instanceof WXEmbed) {
                    WXEmbed wXEmbed = (WXEmbed) nestedContainer;
                    if (wXEmbed.getEvents().contains("downgrade")) {
                        wXEmbed.fireEvent("downgrade");
                    }
                    z = WXUtils.getBoolean(wXEmbed.getAttrs().get("nestedScrollEnabled"), false).booleanValue();
                } else {
                    z = false;
                }
                if (z) {
                    wVUCWebView = new NestedScrollingWebView(viewContainer.getContext());
                } else {
                    wVUCWebView = new WVUCWebView(viewContainer.getContext());
                }
                this.mUCWebView = wVUCWebView;
                WebSettings settings = wVUCWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setAppCacheEnabled(true);
                settings.setUseWideViewPort(true);
                settings.setDomStorageEnabled(true);
                settings.setSupportZoom(false);
                settings.setBuiltInZoomControls(false);
                wVUCWebView.setVerticalScrollBarEnabled(true);
                wVUCWebView.setScrollBarStyle(0);
                wVUCWebView.setWebViewClient(new WVUCWebViewClient(this.mEmbContext) {
                    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                        if (!TextUtils.isEmpty(str) && str.startsWith("https://err.tmall.com") && str.contains(Utils.WH_WEEX_TRUE)) {
                            str = str.replace(Utils.WH_WEEX_TRUE, "wh_weex=false");
                        }
                        IEventModuleAdapter eventModuleAdapter = AliWeex.getInstance().getEventModuleAdapter();
                        if (eventModuleAdapter == null) {
                            return true;
                        }
                        eventModuleAdapter.openURL(EmbedEventListener.this.mEmbContext, str);
                        return true;
                    }
                });
                wVUCWebView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                viewContainer.removeAllViews();
                viewContainer.addView(wVUCWebView);
                wVUCWebView.loadUrl(((WXEmbed) nestedContainer).getSrc());
                this.isDegrade = true;
                return;
            }
            super.onException(nestedContainer, str, str2);
        }

        public void destroy() {
            if (this.mUCWebView != null) {
                this.mUCWebView.coreDestroy();
                this.mUCWebView = null;
            }
            this.instance = null;
        }
    }
}
