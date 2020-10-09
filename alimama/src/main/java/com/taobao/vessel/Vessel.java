package com.taobao.vessel;

import android.taobao.windvane.extra.uc.WVUCWebView;
import android.view.ViewGroup;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;

public class Vessel {
    private static Vessel sInstance;
    private Config mConfig;

    private Vessel() {
    }

    public static Vessel getInstance() {
        if (sInstance == null) {
            synchronized (Vessel.class) {
                if (sInstance == null) {
                    sInstance = new Vessel();
                }
            }
        }
        return sInstance;
    }

    public void init(Config config) {
        this.mConfig = config;
    }

    public IActivityNavBarSetter getActivityBarSetter() {
        if (this.mConfig != null) {
            return this.mConfig.barSetter;
        }
        return null;
    }

    public WVUCWebView getWebview() {
        if (this.mConfig != null) {
            return this.mConfig.webView;
        }
        return null;
    }

    public void destroy() {
        if (this.mConfig != null) {
            if (this.mConfig.webView != null) {
                if (this.mConfig.webView.getParent() != null) {
                    ((ViewGroup) this.mConfig.webView.getParent()).removeView(this.mConfig.webView);
                }
                this.mConfig.webView.coreDestroy();
            }
            if (this.mConfig.barSetter != null) {
                IActivityNavBarSetter unused = this.mConfig.barSetter = null;
            }
            this.mConfig = null;
        }
    }

    public static class Config {
        /* access modifiers changed from: private */
        public IActivityNavBarSetter barSetter;
        /* access modifiers changed from: private */
        public boolean interceptException;
        /* access modifiers changed from: private */
        public WVUCWebView webView;

        public static class Builder {
            boolean interceptException;
            IActivityNavBarSetter navBar;
            WVUCWebView webView;

            public Builder setWebView(WVUCWebView wVUCWebView) {
                this.webView = wVUCWebView;
                return this;
            }

            public Builder setActivityBarSetter(IActivityNavBarSetter iActivityNavBarSetter) {
                this.navBar = iActivityNavBarSetter;
                return this;
            }

            public Builder setInterceptException(boolean z) {
                this.interceptException = z;
                return this;
            }

            public Config build() {
                Config config = new Config();
                IActivityNavBarSetter unused = config.barSetter = this.navBar;
                boolean unused2 = config.interceptException = this.interceptException;
                WVUCWebView unused3 = config.webView = this.webView;
                return config;
            }
        }
    }
}
