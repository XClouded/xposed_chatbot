package com.alibaba.android.prefetchx.config;

import androidx.annotation.NonNull;
import com.alibaba.android.prefetchx.config.OrangeRemoteConfigImpl;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;

public class GlobalOnlineConfigManager {
    private RemoteConfigSpec.IDataModuleRemoteConfig mDataModuleConfigImpl;
    private RemoteConfigSpec.IFileModuleRemoteConfig mFileModuleConfImpl;
    private RemoteConfigSpec.IImageModuleRemoteConfig mImageModuleConfImpl;
    private RemoteConfigSpec.IJSModuleRemoteConfig mJSModuleConfigImpl;
    private RemoteConfigSpec.IResourceModuleRemoteConfig mResourceModuleConfImpl;

    public static GlobalOnlineConfigManager createdAll() {
        return createdWith(OrangeRemoteConfigImpl.newFileModuleConfig(), OrangeRemoteConfigImpl.newDataModuleConfig(), OrangeRemoteConfigImpl.newJSModuleConfig(), OrangeRemoteConfigImpl.newResourceModuleConfig(), OrangeRemoteConfigImpl.newImageModuleConfig());
    }

    public static GlobalOnlineConfigManager createdWith(@NonNull RemoteConfigSpec.IFileModuleRemoteConfig iFileModuleRemoteConfig, @NonNull RemoteConfigSpec.IDataModuleRemoteConfig iDataModuleRemoteConfig, @NonNull RemoteConfigSpec.IJSModuleRemoteConfig iJSModuleRemoteConfig, @NonNull RemoteConfigSpec.IResourceModuleRemoteConfig iResourceModuleRemoteConfig, @NonNull RemoteConfigSpec.IImageModuleRemoteConfig iImageModuleRemoteConfig) {
        GlobalOnlineConfigManager globalOnlineConfigManager = new GlobalOnlineConfigManager();
        globalOnlineConfigManager.mDataModuleConfigImpl = iDataModuleRemoteConfig;
        globalOnlineConfigManager.mFileModuleConfImpl = iFileModuleRemoteConfig;
        globalOnlineConfigManager.mResourceModuleConfImpl = iResourceModuleRemoteConfig;
        globalOnlineConfigManager.mJSModuleConfigImpl = iJSModuleRemoteConfig;
        globalOnlineConfigManager.mImageModuleConfImpl = iImageModuleRemoteConfig;
        return globalOnlineConfigManager;
    }

    public void initDefault() {
        this.mDataModuleConfigImpl = new OrangeRemoteConfigImpl.DataModuleRemoteConf();
        this.mFileModuleConfImpl = new OrangeRemoteConfigImpl.FileModuleRemoteConf();
        this.mJSModuleConfigImpl = new OrangeRemoteConfigImpl.JSModuleRemoteConf();
        this.mResourceModuleConfImpl = new OrangeRemoteConfigImpl.ResourceModuleRemoteConf();
        this.mImageModuleConfImpl = new OrangeRemoteConfigImpl.ImageModuleRemoteConf();
    }

    @NonNull
    public RemoteConfigSpec.IDataModuleRemoteConfig getDataModuleConfig() {
        return this.mDataModuleConfigImpl;
    }

    @NonNull
    public RemoteConfigSpec.IFileModuleRemoteConfig getFileModuleConfig() {
        return this.mFileModuleConfImpl;
    }

    @NonNull
    public RemoteConfigSpec.IJSModuleRemoteConfig getJSModuleConfig() {
        return this.mJSModuleConfigImpl;
    }

    @NonNull
    public RemoteConfigSpec.IResourceModuleRemoteConfig getResourceModuleConfig() {
        return this.mResourceModuleConfImpl;
    }

    @NonNull
    public RemoteConfigSpec.IImageModuleRemoteConfig getImageModuleConfig() {
        return this.mImageModuleConfImpl;
    }
}
