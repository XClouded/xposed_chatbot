package com.alibaba.android.prefetchx;

import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.adapter.AssetAdapter;
import com.alibaba.android.prefetchx.adapter.HttpAdapter;
import com.alibaba.android.prefetchx.adapter.IThreadExecutor;
import com.alibaba.android.prefetchx.adapter.LoginAdapter;
import com.alibaba.android.prefetchx.config.GlobalOnlineConfigManager;

public class PFInitConfig {
    /* access modifiers changed from: private */
    public boolean allowWarmUp;
    /* access modifiers changed from: private */
    public AssetAdapter assetAdapter;
    /* access modifiers changed from: private */
    public HttpAdapter httpAdapter;
    /* access modifiers changed from: private */
    public LoginAdapter loginAdapter;
    /* access modifiers changed from: private */
    public GlobalOnlineConfigManager onlineConfigManager;
    /* access modifiers changed from: private */
    public IThreadExecutor threadExecutor;

    private PFInitConfig() {
        this.allowWarmUp = true;
    }

    public LoginAdapter getLoginAdapter() {
        return this.loginAdapter;
    }

    public AssetAdapter getAssetAdapter() {
        return this.assetAdapter;
    }

    public HttpAdapter getHttpAdapter() {
        return this.httpAdapter;
    }

    public GlobalOnlineConfigManager getOnlineConfigManager() {
        return this.onlineConfigManager;
    }

    public IThreadExecutor getThreadExecutor() {
        return this.threadExecutor;
    }

    public boolean allowWarmUp() {
        return this.allowWarmUp;
    }

    public static class Builder {
        boolean allowWarmUp = true;
        AssetAdapter assetAdapter;
        GlobalOnlineConfigManager configManager;
        HttpAdapter httpAdapter;
        LoginAdapter loginAdapter;
        IThreadExecutor threadExecutor;

        public Builder setLoginAdapter(@Nullable LoginAdapter loginAdapter2) {
            this.loginAdapter = loginAdapter2;
            return this;
        }

        public Builder setAssetAdapter(@Nullable AssetAdapter assetAdapter2) {
            this.assetAdapter = assetAdapter2;
            return this;
        }

        public Builder setHttpAdapter(@Nullable HttpAdapter httpAdapter2) {
            this.httpAdapter = httpAdapter2;
            return this;
        }

        public Builder setGlobalOnlineConfigAdapter(@Nullable GlobalOnlineConfigManager globalOnlineConfigManager) {
            this.configManager = globalOnlineConfigManager;
            return this;
        }

        public Builder setThreadExecutor(@Nullable IThreadExecutor iThreadExecutor) {
            this.threadExecutor = iThreadExecutor;
            return this;
        }

        public Builder allowWarmUp(boolean z) {
            this.allowWarmUp = z;
            return this;
        }

        public PFInitConfig build() {
            PFInitConfig pFInitConfig = new PFInitConfig();
            AssetAdapter unused = pFInitConfig.assetAdapter = this.assetAdapter;
            LoginAdapter unused2 = pFInitConfig.loginAdapter = this.loginAdapter;
            HttpAdapter unused3 = pFInitConfig.httpAdapter = this.httpAdapter;
            GlobalOnlineConfigManager unused4 = pFInitConfig.onlineConfigManager = this.configManager;
            IThreadExecutor unused5 = pFInitConfig.threadExecutor = this.threadExecutor;
            boolean unused6 = pFInitConfig.allowWarmUp = this.allowWarmUp;
            return pFInitConfig;
        }
    }
}
