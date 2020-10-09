package com.alibaba.aliweex.bundle;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alibaba.dynamic.DynamicSdk;
import com.taobao.weex.WXSDKInstance;

public class DynamicUrlPresenter implements WeexPageContract.IDynamicUrlPresenter {
    private final UrlInfo mUrlInfo = new UrlInfo();

    public void onWXException(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (this.mUrlInfo.mNewRenderUrl != null && wXSDKInstance != null && str2.contains("404")) {
            try {
                DynamicSdk.getInstance().redirectUrlFailed(this.mUrlInfo.mPureRenderUrl);
            } catch (Throwable unused) {
            }
        }
    }

    public void transformUrl(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            setUrls(str, str2);
        }
    }

    private void setUrls(String str, String str2) {
        this.mUrlInfo.clear();
        this.mUrlInfo.mOldBundleUrl = str;
        this.mUrlInfo.mOldRenderUrl = str2;
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
            try {
                if (DynamicSdk.getInstance().isSdkWork()) {
                    Uri parse = Uri.parse(str);
                    String queryParameter = parse.getQueryParameter("_wx_tpl");
                    String queryParameter2 = parse.getQueryParameter("wh_weex");
                    if (TextUtils.isEmpty(queryParameter)) {
                        return;
                    }
                    if ("true".equals(queryParameter2)) {
                        String uri = Uri.parse(str2).buildUpon().clearQuery().build().toString();
                        if (queryParameter.contains(uri)) {
                            this.mUrlInfo.mPureRenderUrl = null;
                            this.mUrlInfo.mPureRenderUrl = uri;
                            String redirectUrl = DynamicSdk.getInstance().redirectUrl(uri);
                            if (!uri.equals(redirectUrl)) {
                                this.mUrlInfo.mNewBundleUrl = str.replace(uri, redirectUrl);
                                this.mUrlInfo.mNewRenderUrl = str2.replace(uri, redirectUrl);
                            }
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    public String getUrl() {
        return this.mUrlInfo.getBundleUrl();
    }

    public String getOriginalUrl() {
        return this.mUrlInfo.mOldBundleUrl;
    }

    public String getRenderUrl() {
        return this.mUrlInfo.getRenderUrl();
    }

    public String getOriginalRenderUrl() {
        return this.mUrlInfo.mOldRenderUrl;
    }

    private class UrlInfo {
        String mNewBundleUrl;
        String mNewRenderUrl;
        String mOldBundleUrl;
        String mOldRenderUrl;
        String mPureRenderUrl;

        private UrlInfo() {
        }

        /* access modifiers changed from: package-private */
        public String getBundleUrl() {
            if (TextUtils.isEmpty(this.mNewBundleUrl)) {
                return this.mOldBundleUrl;
            }
            return this.mNewBundleUrl;
        }

        /* access modifiers changed from: package-private */
        public String getRenderUrl() {
            if (TextUtils.isEmpty(this.mNewRenderUrl)) {
                return this.mOldRenderUrl;
            }
            return this.mNewRenderUrl;
        }

        public void clear() {
            this.mOldRenderUrl = null;
            this.mOldRenderUrl = null;
            this.mNewBundleUrl = null;
            this.mNewRenderUrl = null;
            this.mPureRenderUrl = null;
        }
    }
}
