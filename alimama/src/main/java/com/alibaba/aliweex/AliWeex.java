package com.alibaba.aliweex;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.adapter.IAliPayModuleAdapter;
import com.alibaba.aliweex.adapter.IConfigGeneratorAdapter;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;
import com.alibaba.aliweex.adapter.IFestivalModuleAdapter;
import com.alibaba.aliweex.adapter.IGodEyeStageAdapter;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.alibaba.aliweex.adapter.IPageInfoModuleAdapter;
import com.alibaba.aliweex.adapter.IShareModuleAdapter;
import com.alibaba.aliweex.adapter.IUserModuleAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.adapter.ClassLoaderAdapter;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AliWeex {
    private static AliWeex sInstance;
    @NonNull
    private Application mApp;
    private Config mConfig;

    public static AliWeex getInstance() {
        if (sInstance == null) {
            synchronized (AliWeex.class) {
                if (sInstance == null) {
                    sInstance = new AliWeex();
                }
            }
        }
        return sInstance;
    }

    @Deprecated
    public void init(Application application) {
        this.mApp = application;
    }

    public void initWithConfig(Application application, Config config) {
        this.mApp = application;
        this.mConfig = config;
    }

    public Application getApplication() {
        return this.mApp;
    }

    public Context getContext() {
        return this.mApp.getApplicationContext();
    }

    public InitConfig getInitConfig() {
        if (this.mConfig != null) {
            return this.mConfig.getInitConfig();
        }
        return null;
    }

    public IShareModuleAdapter getShareModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getShareModuleAdapter();
        }
        return null;
    }

    public IUserModuleAdapter getUserModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getUserModuleAdapter();
        }
        return null;
    }

    public IEventModuleAdapter getEventModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getEventModuleAdapter();
        }
        return null;
    }

    public IPageInfoModuleAdapter getPageInfoModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getPageInfoModuleAdapter();
        }
        return null;
    }

    public IAliPayModuleAdapter getAliPayModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getAliPayModuleAdapter();
        }
        return null;
    }

    public IConfigGeneratorAdapter getConfigGeneratorAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getConfigGenerator();
        }
        return null;
    }

    public INavigationBarModuleAdapter getNavigationBarModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getNavigationBarModuleAdapter();
        }
        return null;
    }

    public IConfigAdapter getConfigAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getConfigAdapter();
        }
        return null;
    }

    public IFestivalModuleAdapter getFestivalModuleAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getFestivalModuleAdapter();
        }
        return null;
    }

    public IWXImgLoaderAdapter getImgLoaderAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getImgLoaderAdapter();
        }
        return null;
    }

    public IWXHttpAdapter getHttpAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getHttpAdapter();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Iterable<String> getNativeLibraryList() {
        if (this.mConfig != null) {
            return this.mConfig.getNativeLibraryList();
        }
        return null;
    }

    public ClassLoaderAdapter getClassLoaderAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.getClassLoaderAdapter();
        }
        return null;
    }

    public IGodEyeStageAdapter getGodEyeStageAdapter() {
        if (this.mConfig != null) {
            return this.mConfig.godEyeStageAdapter;
        }
        return null;
    }

    public static class Config {
        IAliPayModuleAdapter alipay;
        ClassLoaderAdapter classLoaderAdapter;
        IConfigGeneratorAdapter config;
        IConfigAdapter configAdapter;
        IEventModuleAdapter event;
        IFestivalModuleAdapter festival;
        IGodEyeStageAdapter godEyeStageAdapter;
        IWXHttpAdapter httpAdapter;
        IWXImgLoaderAdapter imgLoaderAdapter;
        InitConfig initConfig;
        List<String> nativeLibraryList;
        INavigationBarModuleAdapter navBar;
        IPageInfoModuleAdapter pageInfo;
        IShareModuleAdapter share;
        IUserModuleAdapter user;

        /* access modifiers changed from: package-private */
        public IShareModuleAdapter getShareModuleAdapter() {
            return this.share;
        }

        /* access modifiers changed from: package-private */
        public IUserModuleAdapter getUserModuleAdapter() {
            return this.user;
        }

        /* access modifiers changed from: package-private */
        public IEventModuleAdapter getEventModuleAdapter() {
            return this.event;
        }

        /* access modifiers changed from: package-private */
        public IPageInfoModuleAdapter getPageInfoModuleAdapter() {
            return this.pageInfo;
        }

        /* access modifiers changed from: package-private */
        public IAliPayModuleAdapter getAliPayModuleAdapter() {
            return this.alipay;
        }

        /* access modifiers changed from: package-private */
        public IConfigGeneratorAdapter getConfigGenerator() {
            return this.config;
        }

        /* access modifiers changed from: package-private */
        public INavigationBarModuleAdapter getNavigationBarModuleAdapter() {
            return this.navBar;
        }

        /* access modifiers changed from: package-private */
        public IConfigAdapter getConfigAdapter() {
            return this.configAdapter;
        }

        /* access modifiers changed from: package-private */
        public IFestivalModuleAdapter getFestivalModuleAdapter() {
            return this.festival;
        }

        /* access modifiers changed from: package-private */
        public IWXImgLoaderAdapter getImgLoaderAdapter() {
            return this.imgLoaderAdapter;
        }

        /* access modifiers changed from: package-private */
        public IWXHttpAdapter getHttpAdapter() {
            return this.httpAdapter;
        }

        /* access modifiers changed from: package-private */
        @NonNull
        public Iterable<String> getNativeLibraryList() {
            if (this.nativeLibraryList == null) {
                this.nativeLibraryList = new LinkedList();
            }
            return this.nativeLibraryList;
        }

        public ClassLoaderAdapter getClassLoaderAdapter() {
            return this.classLoaderAdapter;
        }

        /* access modifiers changed from: package-private */
        public InitConfig getInitConfig() {
            return this.initConfig;
        }

        public static class Builder {
            IAliPayModuleAdapter alipay;
            ClassLoaderAdapter classLoaderAdapter;
            IConfigGeneratorAdapter config;
            IConfigAdapter configAdapter;
            IEventModuleAdapter event;
            IFestivalModuleAdapter festival;
            IGodEyeStageAdapter godEyeStageAdapter;
            IWXHttpAdapter httpAdapter;
            IWXImgLoaderAdapter imgLoaderAdapter;
            InitConfig initConfig;
            List<String> nativeLibraryList = new LinkedList();
            INavigationBarModuleAdapter navBar;
            IPageInfoModuleAdapter pageInfo;
            IShareModuleAdapter share;
            IUserModuleAdapter user;

            public Builder setShareModuleAdapter(IShareModuleAdapter iShareModuleAdapter) {
                this.share = iShareModuleAdapter;
                return this;
            }

            public Builder setUserModuleAdapter(IUserModuleAdapter iUserModuleAdapter) {
                this.user = iUserModuleAdapter;
                return this;
            }

            public Builder setEventModuleAdapter(IEventModuleAdapter iEventModuleAdapter) {
                this.event = iEventModuleAdapter;
                return this;
            }

            public Builder setPageInfoModuleAdapter(IPageInfoModuleAdapter iPageInfoModuleAdapter) {
                this.pageInfo = iPageInfoModuleAdapter;
                return this;
            }

            public Builder setAliPayModuleAdapter(IAliPayModuleAdapter iAliPayModuleAdapter) {
                this.alipay = iAliPayModuleAdapter;
                return this;
            }

            public Builder setConfigGeneratorAdapter(IConfigGeneratorAdapter iConfigGeneratorAdapter) {
                this.config = iConfigGeneratorAdapter;
                return this;
            }

            public Builder setNavigationBarModuleAdapter(INavigationBarModuleAdapter iNavigationBarModuleAdapter) {
                this.navBar = iNavigationBarModuleAdapter;
                return this;
            }

            public Builder setConfigAdapter(IConfigAdapter iConfigAdapter) {
                this.configAdapter = iConfigAdapter;
                return this;
            }

            public Builder setFestivalModuleAdapter(IFestivalModuleAdapter iFestivalModuleAdapter) {
                this.festival = iFestivalModuleAdapter;
                return this;
            }

            public Builder setImgLoaderAdapter(IWXImgLoaderAdapter iWXImgLoaderAdapter) {
                this.imgLoaderAdapter = iWXImgLoaderAdapter;
                return this;
            }

            public Builder setHttpAdapter(IWXHttpAdapter iWXHttpAdapter) {
                this.httpAdapter = iWXHttpAdapter;
                return this;
            }

            public Builder setInitConfig(InitConfig initConfig2) {
                this.initConfig = initConfig2;
                return this;
            }

            public Builder setClassLoaderAdapter(ClassLoaderAdapter classLoaderAdapter2) {
                this.classLoaderAdapter = classLoaderAdapter2;
                return this;
            }

            public Builder addNativeLibrary(String str) {
                this.nativeLibraryList.add(str);
                return this;
            }

            public Builder setGodEyeStageAdapter(IGodEyeStageAdapter iGodEyeStageAdapter) {
                this.godEyeStageAdapter = iGodEyeStageAdapter;
                return this;
            }

            public Config build() {
                Config config2 = new Config();
                config2.share = this.share;
                config2.user = this.user;
                config2.event = this.event;
                config2.pageInfo = this.pageInfo;
                config2.alipay = this.alipay;
                config2.config = this.config;
                config2.navBar = this.navBar;
                config2.configAdapter = this.configAdapter;
                config2.festival = this.festival;
                config2.imgLoaderAdapter = this.imgLoaderAdapter;
                config2.httpAdapter = this.httpAdapter;
                config2.initConfig = this.initConfig;
                config2.classLoaderAdapter = this.classLoaderAdapter;
                config2.nativeLibraryList = this.nativeLibraryList;
                config2.godEyeStageAdapter = this.godEyeStageAdapter;
                return config2;
            }
        }
    }

    public void onStage(String str, Map<String, Object> map) {
        if (this.mConfig != null && this.mConfig.godEyeStageAdapter != null) {
            this.mConfig.godEyeStageAdapter.onStage(str, map);
        }
    }

    public void onError(String str, String str2, Map<String, Object> map) {
        if (this.mConfig != null && this.mConfig.godEyeStageAdapter != null) {
            this.mConfig.godEyeStageAdapter.onError(str, str2, map);
        }
    }
}
