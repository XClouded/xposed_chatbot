package com.taobao.weex.ui.component;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import anet.channel.util.HttpConstant;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.ui.view.WXWebView;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
public class WXWeb extends WXComponent {
    public static final String GO_BACK = "goBack";
    public static final String GO_FORWARD = "goForward";
    public static final String POST_MESSAGE = "postMessage";
    public static final String RELOAD = "reload";
    /* access modifiers changed from: protected */
    public IWebView mWebView;

    @Deprecated
    public WXWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        createWebView();
    }

    /* access modifiers changed from: protected */
    public void createWebView() {
        String str = null;
        try {
            Uri parse = Uri.parse(WXSDKManager.getInstance().getSDKInstance(getInstanceId()).getBundleUrl());
            String scheme = parse.getScheme();
            String authority = parse.getAuthority();
            if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(authority)) {
                str = scheme + HttpConstant.SCHEME_SPLIT + authority;
            }
        } catch (Exception unused) {
        }
        this.mWebView = new WXWebView(getContext(), str);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(@NonNull Context context) {
        this.mWebView.setOnErrorListener(new IWebView.OnErrorListener() {
            public void onError(String str, Object obj) {
                WXWeb.this.fireEvent(str, obj);
            }
        });
        this.mWebView.setOnPageListener(new IWebView.OnPageListener() {
            public void onReceivedTitle(String str) {
                if (WXWeb.this.getEvents().contains(Constants.Event.RECEIVEDTITLE)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("title", str);
                    WXWeb.this.fireEvent(Constants.Event.RECEIVEDTITLE, hashMap);
                }
            }

            public void onPageStart(String str) {
                if (WXWeb.this.getEvents().contains(Constants.Event.PAGESTART)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", str);
                    WXWeb.this.fireEvent(Constants.Event.PAGESTART, hashMap);
                }
            }

            public void onPageFinish(String str, boolean z, boolean z2) {
                if (WXWeb.this.getEvents().contains(Constants.Event.PAGEFINISH)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", str);
                    hashMap.put("canGoBack", Boolean.valueOf(z));
                    hashMap.put("canGoForward", Boolean.valueOf(z2));
                    WXWeb.this.fireEvent(Constants.Event.PAGEFINISH, hashMap);
                }
            }
        });
        this.mWebView.setOnMessageListener(new IWebView.OnMessageListener() {
            public void onMessage(Map<String, Object> map) {
                WXWeb.this.fireEvent("message", map);
            }
        });
        return this.mWebView.getView();
    }

    public void destroy() {
        super.destroy();
        getWebView().destroy();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = -896505829(0xffffffffca90681b, float:-4731917.5)
            r2 = 1
            if (r0 == r1) goto L_0x0029
            r1 = 114148(0x1bde4, float:1.59955E-40)
            if (r0 == r1) goto L_0x001f
            r1 = 537088620(0x2003526c, float:1.1123403E-19)
            if (r0 == r1) goto L_0x0015
            goto L_0x0033
        L_0x0015:
            java.lang.String r0 = "show-loading"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 0
            goto L_0x0034
        L_0x001f:
            java.lang.String r0 = "src"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 1
            goto L_0x0034
        L_0x0029:
            java.lang.String r0 = "source"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 2
            goto L_0x0034
        L_0x0033:
            r0 = -1
        L_0x0034:
            r1 = 0
            switch(r0) {
                case 0: goto L_0x0051;
                case 1: goto L_0x0047;
                case 2: goto L_0x003d;
                default: goto L_0x0038;
            }
        L_0x0038:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x003d:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r1)
            if (r4 == 0) goto L_0x0046
            r3.setSource(r4)
        L_0x0046:
            return r2
        L_0x0047:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r1)
            if (r4 == 0) goto L_0x0050
            r3.setUrl(r4)
        L_0x0050:
            return r2
        L_0x0051:
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r1)
            if (r4 == 0) goto L_0x005e
            boolean r4 = r4.booleanValue()
            r3.setShowLoading(r4)
        L_0x005e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXWeb.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "show-loading")
    public void setShowLoading(boolean z) {
        getWebView().setShowLoading(z);
    }

    @WXComponentProp(name = "src")
    public void setUrl(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null && !TextUtils.isEmpty(str)) {
            loadUrl(getInstance().rewriteUri(Uri.parse(str), "web").toString());
        }
    }

    @WXComponentProp(name = "source")
    public void setSource(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null) {
            loadDataWithBaseURL(str);
        }
    }

    public void setAction(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.equals(GO_BACK)) {
            goBack();
        } else if (str.equals(GO_FORWARD)) {
            goForward();
        } else if (str.equals("reload")) {
            reload();
        } else if (str.equals(POST_MESSAGE)) {
            postMessage(obj);
        }
    }

    /* access modifiers changed from: private */
    public void fireEvent(String str, Object obj) {
        if (getEvents().contains("error")) {
            HashMap hashMap = new HashMap();
            hashMap.put("type", str);
            hashMap.put(ILocatable.ERROR_MSG, obj);
            fireEvent("error", hashMap);
        }
    }

    private void loadUrl(String str) {
        getWebView().loadUrl(str);
    }

    private void loadDataWithBaseURL(String str) {
        getWebView().loadDataWithBaseURL(str);
    }

    @JSMethod
    public void reload() {
        getWebView().reload();
    }

    @JSMethod
    public void goForward() {
        getWebView().goForward();
    }

    @JSMethod
    public void goBack() {
        getWebView().goBack();
    }

    @JSMethod
    public void postMessage(Object obj) {
        getWebView().postMessage(obj);
    }

    private IWebView getWebView() {
        return this.mWebView;
    }
}
