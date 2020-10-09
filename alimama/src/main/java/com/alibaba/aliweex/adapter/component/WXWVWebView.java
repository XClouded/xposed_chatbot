package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import anet.channel.util.HttpConstant;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXWeb;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.uc.webview.export.JavascriptInterface;
import com.uc.webview.export.JsPromptResult;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WXWVWebView implements IWebView {
    private static final String BRIDGE_NAME = "__WEEX_WEB_VIEW_BRIDGE";
    /* access modifiers changed from: private */
    public static final boolean DOWNGRADE_JS_INTERFACE = (SDK_VERSION < 17);
    private static final int POST_MESSAGE = 1;
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    /* access modifiers changed from: private */
    public static String forceWx = "forceWx=true";
    /* access modifiers changed from: private */
    public WXComponent mComponent;
    private Context mContext;
    /* access modifiers changed from: private */
    public WXSDKInstance mInstance;
    private Handler mMessageHandler;
    protected IWebView.OnErrorListener mOnErrorListener;
    protected IWebView.OnMessageListener mOnMessageListener;
    protected IWebView.OnPageListener mOnPageListener;
    private String mOrigin;
    private ProgressBar mProgressBar;
    private boolean mShowLoading = true;
    private AliWVUCWebView mWebView;
    /* access modifiers changed from: private */
    public ArrayList<String> wxurls = new ArrayList<>();

    public WXWVWebView(WXSDKInstance wXSDKInstance, WXComponent wXComponent) {
        String str = null;
        try {
            Uri parse = Uri.parse(wXSDKInstance.getBundleUrl());
            String scheme = parse.getScheme();
            String authority = parse.getAuthority();
            if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(authority)) {
                str = scheme + HttpConstant.SCHEME_SPLIT + authority;
            }
        } catch (Exception unused) {
        }
        this.mInstance = wXSDKInstance;
        this.mComponent = wXComponent;
        this.mContext = wXSDKInstance.getContext();
        this.mOrigin = str;
    }

    public View getView() {
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.setBackgroundColor(-1);
        this.mWebView = new AliWVUCWebView(this.mContext);
        this.mWebView.onScrollChangeListener = new AliWVUCWebView.OnScrollListener() {
            public void onScroll(int i, int i2, int i3, int i4) {
                if (WXWVWebView.this.mInstance != null && WXWVWebView.this.mComponent != null && WXWVWebView.this.mComponent.getEvents().contains("onscroll")) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("l", Integer.valueOf(i));
                    hashMap.put("t", Integer.valueOf(i2));
                    WXWVWebView.this.mInstance.fireEvent(WXWVWebView.this.mComponent.getRef(), "onscroll", hashMap, (Map<String, Object>) null);
                }
            }
        };
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.mWebView.setLayoutParams(layoutParams);
        frameLayout.addView(this.mWebView);
        initWebView(this.mWebView);
        this.mProgressBar = new ProgressBar(this.mContext);
        showProgressBar(false);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
        this.mProgressBar.setLayoutParams(layoutParams2);
        layoutParams2.gravity = 17;
        frameLayout.addView(this.mProgressBar);
        this.mMessageHandler = new MessageHandler();
        if (!(this.mComponent == null || this.mComponent.getStyles() == null)) {
            this.mWebView.setBackgroundColor(WXResourceUtils.getColor((String) this.mComponent.getStyles().get("backgroundColor"), 0));
        }
        return frameLayout;
    }

    public void destroy() {
        if (this.mWebView != null) {
            this.mWebView.destroy();
        }
    }

    public void loadUrl(String str) {
        if (this.mWebView != null) {
            this.wxurls.add(str);
            this.mWebView.loadUrl(str);
        }
    }

    public void loadDataWithBaseURL(String str) {
        if (this.mWebView != null) {
            this.mWebView.loadDataWithBaseURL(this.mOrigin, str, "text/html", "utf-8", (String) null);
        }
    }

    public void reload() {
        if (this.mWebView != null) {
            this.mWebView.reload();
        }
    }

    public void goBack() {
        if (this.mWebView != null && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        }
    }

    public void goForward() {
        if (this.mWebView != null && this.mWebView.canGoForward()) {
            this.mWebView.goForward();
        }
    }

    public void postMessage(Object obj) {
        if (this.mWebView != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", (Object) "message");
                jSONObject.put("data", obj);
                jSONObject.put(BindingXConstants.KEY_ORIGIN, (Object) this.mOrigin);
                evaluateJS("javascript:(function () {var initData = " + jSONObject.toString() + ";" + "try {" + "var event = new MessageEvent('message', initData);" + "window.dispatchEvent(event);" + "} catch (e) {}" + "})();");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setShowLoading(boolean z) {
        this.mShowLoading = z;
    }

    public void setOnErrorListener(IWebView.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPageListener(IWebView.OnPageListener onPageListener) {
        this.mOnPageListener = onPageListener;
    }

    public void setOnMessageListener(IWebView.OnMessageListener onMessageListener) {
        this.mOnMessageListener = onMessageListener;
    }

    private void showWebView(boolean z) {
        this.mWebView.setVisibility(z ? 0 : 4);
    }

    /* access modifiers changed from: private */
    public void showProgressBar(boolean z) {
        if (this.mShowLoading) {
            this.mProgressBar.setVisibility(z ? 0 : 8);
        }
    }

    private void initWebView(WVUCWebView wVUCWebView) {
        WebSettings settings = wVUCWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        wVUCWebView.setWebViewClient(new WVUCWebViewClient(this.mContext) {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                WXLogUtils.v("tag", "onPageStarted " + str);
                if (WXWVWebView.this.mOnPageListener != null) {
                    WXWVWebView.this.mOnPageListener.onPageStart(str);
                }
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                WXLogUtils.v("tag", "onPageFinished " + str);
                if (WXWVWebView.this.mOnPageListener != null) {
                    WXWVWebView.this.mOnPageListener.onPageFinish(str, webView.canGoBack(), webView.canGoForward());
                }
                if (WXWVWebView.this.mOnMessageListener != null) {
                    WXWVWebView wXWVWebView = WXWVWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("javascript:(window.postMessage = function(message, targetOrigin) {if (message == null || !targetOrigin) return;");
                    sb.append(WXWVWebView.DOWNGRADE_JS_INTERFACE ? "prompt('__WEEX_WEB_VIEW_BRIDGE://postMessage?message=' + JSON.stringify(message) + '&targetOrigin=' + targetOrigin)" : "__WEEX_WEB_VIEW_BRIDGE.postMessage(JSON.stringify(message), targetOrigin);");
                    sb.append("})");
                    wXWVWebView.evaluateJS(sb.toString());
                }
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                super.onReceivedError(webView, i, str, str2);
                if (WXWVWebView.this.mOnErrorListener != null) {
                    WXWVWebView.this.mOnErrorListener.onError("error", "page error");
                }
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str == null || ((WXWVWebView.this.wxurls != null && WXWVWebView.this.wxurls.contains(str)) || str.contains(WXWVWebView.forceWx) || this.mContext.get() == null)) {
                    return false;
                }
                IEventModuleAdapter eventModuleAdapter = AliWeex.getInstance().getEventModuleAdapter();
                if (eventModuleAdapter == null) {
                    return true;
                }
                eventModuleAdapter.openURL(WXWVWebView.this.mInstance.getContext(), str);
                return true;
            }

            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                if (WXWVWebView.this.mOnErrorListener != null) {
                    WXWVWebView.this.mOnErrorListener.onError("error", "ssl error");
                }
            }
        });
        wVUCWebView.setWebChromeClient(new WVUCWebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                WXWVWebView.this.showProgressBar(i != 100);
                WXLogUtils.v("tag", "onPageProgressChanged " + i);
            }

            public void onReceivedTitle(WebView webView, String str) {
                super.onReceivedTitle(webView, str);
                if (WXWVWebView.this.mOnPageListener != null) {
                    WXWVWebView.this.mOnPageListener.onReceivedTitle(webView.getTitle());
                }
            }

            public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
                Uri parse = Uri.parse(str2);
                if (!TextUtils.equals(parse.getScheme(), WXWVWebView.BRIDGE_NAME)) {
                    return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
                }
                if (TextUtils.equals(parse.getAuthority(), WXWeb.POST_MESSAGE)) {
                    WXWVWebView.this.onMessage(parse.getQueryParameter("message"), parse.getQueryParameter("targetOrigin"));
                    jsPromptResult.confirm("success");
                    return true;
                }
                jsPromptResult.confirm(Constants.Event.FAIL);
                return true;
            }
        });
        if (!DOWNGRADE_JS_INTERFACE) {
            wVUCWebView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void postMessage(String str, String str2) {
                    WXWVWebView.this.onMessage(str, str2);
                }
            }, BRIDGE_NAME);
        }
    }

    /* access modifiers changed from: private */
    public void onMessage(String str, String str2) {
        if (str != null && str2 != null && this.mOnMessageListener != null) {
            try {
                HashMap hashMap = new HashMap();
                hashMap.put("data", JSON.parse(str));
                hashMap.put(BindingXConstants.KEY_ORIGIN, str2);
                hashMap.put("type", "message");
                Message message = new Message();
                message.what = 1;
                message.obj = hashMap;
                this.mMessageHandler.sendMessage(message);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void evaluateJS(String str) {
        if (SDK_VERSION < 19) {
            this.mWebView.loadUrl(str);
        } else {
            this.mWebView.evaluateJavascript(str, (ValueCallback<String>) null);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mWebView != null) {
            this.mWebView.onActivityResult(i, i2, intent);
        }
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<WXWVWebView> mWv;

        private MessageHandler(WXWVWebView wXWVWebView) {
            this.mWv = new WeakReference<>(wXWVWebView);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1 && this.mWv.get() != null && ((WXWVWebView) this.mWv.get()).mOnMessageListener != null) {
                ((WXWVWebView) this.mWv.get()).mOnMessageListener.onMessage((Map) message.obj);
            }
        }
    }

    private static class AliWVUCWebView extends WVUCWebView {
        OnScrollListener onScrollChangeListener;

        public interface OnScrollListener {
            void onScroll(int i, int i2, int i3, int i4);
        }

        public AliWVUCWebView(Context context) {
            super(context);
        }

        public void OnScrollChanged(int i, int i2, int i3, int i4) {
            if (this.onScrollChangeListener != null) {
                this.onScrollChangeListener.onScroll(i, i2, i3, i4);
            }
            super.OnScrollChanged(i, i2, i3, i4);
        }
    }
}
