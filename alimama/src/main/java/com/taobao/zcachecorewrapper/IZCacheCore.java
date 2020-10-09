package com.taobao.zcachecorewrapper;

import com.taobao.zcachecorewrapper.model.AppInfo;
import com.taobao.zcachecorewrapper.model.Error;
import com.taobao.zcachecorewrapper.model.ResourceInfo;
import com.taobao.zcachecorewrapper.model.ResourceItemInfo;
import java.util.List;
import java.util.Set;

public interface IZCacheCore {

    public interface AppInfoCallback {
        void onReceive(AppInfo appInfo, Error error);
    }

    public interface DevCallback {
        void onDevBack(boolean z, String str);
    }

    public interface UpdateCallback {
        void finish(String str, Error error);
    }

    public interface UpdateCallbackInner {
        void finish(Error error);
    }

    String getMiniAppFilePath(String str, String str2);

    ResourceInfo getResourceInfo(String str, int i);

    ResourceItemInfo getResourceItemInfoForApp(String str, String str2);

    String getSessionID();

    void initApps(Set<String> set);

    void initConfig();

    void installPreload(String str);

    void invokeZCacheDev(String str, String str2, DevCallback devCallback);

    boolean isPackInstalled(String str);

    void onBackground();

    void onForeground();

    void onRequestAppConfigCallback(long j, String str, int i, String str2);

    void onRequestConfigCallback(long j, String str, int i, int i2, String str2);

    void onRequestZConfigCallback(long j, String str, int i, String str2);

    void onRequestZIPCallback(long j, String str, int i, int i2, String str2);

    void onSendRequestCallback(long j, String str, int i, int i2, String str2);

    void receiveZConfigUpdateMessage(List<String> list, long j);

    void registerAppInfoCallback(String str, AppInfoCallback appInfoCallback);

    void removeAZCache(String str);

    void removeAllZCache();

    void setEnv(int i);

    void setUseNewUnzip(boolean z);

    void setupSubProcess();

    void setupWithHTTP(String str, String str2, boolean z);

    void startUpdateQueue();

    void update(Set<String> set, int i);

    void updatePack(String str, String str2, int i, UpdateCallbackInner updateCallbackInner);
}
