package com.taobao.zcache;

import com.taobao.zcache.global.ZCacheImpl;
import com.taobao.zcachecorewrapper.IZCache;
import com.taobao.zcachecorewrapper.IZCacheCore;
import com.taobao.zcachecorewrapper.ZCacheCoreNative;
import com.taobao.zcachecorewrapper.model.ResourceInfo;
import com.taobao.zcachecorewrapper.model.ResourceItemInfo;
import java.util.List;
import java.util.Set;

class ZCacheCoreProxy implements IZCacheCore {
    private static ZCacheCoreProxy INSTANCE;
    private ZCacheCoreNative real = new ZCacheCoreNative();

    public void removeAllZCache() {
    }

    private ZCacheCoreProxy() {
        initCore(new ZCacheImpl(this));
    }

    public static ZCacheCoreProxy instance() {
        if (INSTANCE == null) {
            synchronized (ZCacheCoreProxy.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ZCacheCoreProxy();
                }
            }
        }
        return INSTANCE;
    }

    private void initCore(IZCache iZCache) {
        this.real.init(iZCache);
    }

    public void setupWithHTTP(String str, String str2, boolean z) {
        this.real.setupWithHTTP(str, str2, z);
    }

    public void setupSubProcess() {
        this.real.setupSubProcess();
    }

    public void setEnv(int i) {
        this.real.setEnv(i);
    }

    public void startUpdateQueue() {
        this.real.startUpdateQueue();
    }

    public void onForeground() {
        this.real.onForeground();
    }

    public void onBackground() {
        this.real.onBackground();
    }

    public ResourceInfo getResourceInfo(String str, int i) {
        return this.real.getResourceInfo(str, i);
    }

    public void registerAppInfoCallback(String str, IZCacheCore.AppInfoCallback appInfoCallback) {
        this.real.registerAppInfoCallback(str, appInfoCallback);
    }

    public ResourceItemInfo getResourceItemInfoForApp(String str, String str2) {
        return this.real.getResourceItemInfoForApp(str, str2);
    }

    public void onRequestZConfigCallback(long j, String str, int i, String str2) {
        this.real.onRequestZConfigCallback(j, str, i, str2);
    }

    public void onRequestAppConfigCallback(long j, String str, int i, String str2) {
        this.real.onRequestAppConfigCallback(j, str, i, str2);
    }

    public void onRequestZIPCallback(long j, String str, int i, int i2, String str2) {
        this.real.onRequestZIPCallback(j, str, i, i2, str2);
    }

    public void onRequestConfigCallback(long j, String str, int i, int i2, String str2) {
        this.real.onRequestConfigCallback(j, str, i, i2, str2);
    }

    public void onSendRequestCallback(long j, String str, int i, int i2, String str2) {
        this.real.onSendRequestCallback(j, str, i, i2, str2);
    }

    public String getSessionID() {
        return this.real.getSessionID();
    }

    public void update(Set<String> set, int i) {
        this.real.update(set, i);
    }

    public void updatePack(String str, String str2, int i, IZCacheCore.UpdateCallbackInner updateCallbackInner) {
        this.real.updatePack(str, str2, i, updateCallbackInner);
    }

    public String getMiniAppFilePath(String str, String str2) {
        return this.real.getMiniAppFilePath(str, str2);
    }

    public boolean isPackInstalled(String str) {
        return this.real.isPackInstalled(str);
    }

    public void initApps(Set<String> set) {
        this.real.initApps(set);
    }

    public void receiveZConfigUpdateMessage(List<String> list, long j) {
        this.real.receiveZConfigUpdateMessage(list, j);
    }

    public void invokeZCacheDev(String str, String str2, IZCacheCore.DevCallback devCallback) {
        this.real.invokeZCacheDev(str, str2, devCallback);
    }

    public void installPreload(String str) {
        this.real.installPreload(str);
    }

    public void removeAZCache(String str) {
        this.real.removeAZCache(str);
    }

    public void initConfig() {
        this.real.initConfig();
    }

    public void setUseNewUnzip(boolean z) {
        this.real.setUseNewUnzip(z);
    }
}
