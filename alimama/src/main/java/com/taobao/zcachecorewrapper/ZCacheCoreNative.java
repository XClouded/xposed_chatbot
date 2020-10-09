package com.taobao.zcachecorewrapper;

import android.text.TextUtils;
import com.taobao.zcachecorewrapper.IZCacheCore;
import com.taobao.zcachecorewrapper.log.ZCLog;
import com.taobao.zcachecorewrapper.model.AppConfigUpdateInfo;
import com.taobao.zcachecorewrapper.model.Error;
import com.taobao.zcachecorewrapper.model.ResourceInfo;
import com.taobao.zcachecorewrapper.model.ResourceItemInfo;
import com.taobao.zcachecorewrapper.model.ZConfigUpdateInfo;
import com.taobao.zcachecorewrapper.model.ZProxyRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZCacheCoreNative implements IZCacheCore, IZCache {
    private static int initCount;
    private final String TAG = "ZCacheCoreNative";
    private int initState = -1;
    private Lock lock = new ReentrantLock();
    private IZCache real;

    private native String getMiniAppFilePathNative(String str, String str2);

    private native ResourceInfo getResourceInfoNative(String str, int i);

    private native ResourceItemInfo getResourceItemInfoForAppNative(String str, String str2);

    private native String getSessionIDNative();

    private native void initAppsNative(Set<String> set);

    private native void initConfigNative();

    private native void installPreloadNative(String str);

    private native void invokeDevBridge(String str, String str2, IZCacheCore.DevCallback devCallback);

    private native boolean isPackInstalledNative(String str);

    private native void onBackgroundNative();

    private native void onForegroundNative();

    private native void onRequestAppConfigCallbackNative(long j, String str, int i, String str2);

    private native void onRequestConfigCallbackNative(long j, String str, int i, int i2, String str2);

    private native void onRequestZConfigCallbackNative(long j, String str, int i, String str2);

    private native void onRequestZIPCallbackNative(long j, String str, int i, int i2, String str2);

    private native void onSendRequestCallbackNative(long j, String str, int i, int i2, String str2);

    private native void receiveZConfigUpdateMessageNative(List<String> list, long j);

    private native void registerAppInfoCallbackNative(String str, IZCacheCore.AppInfoCallback appInfoCallback);

    private native void removeAZCacheNative(String str);

    private native void removeAllZCacheNative();

    private native void setEnvNative(int i);

    private native void setProxyNative(ZCacheCoreNative zCacheCoreNative);

    private native void setUseNewUnZipNative(boolean z);

    private native void setupSubProcessNative();

    private native void setupWithHTTPNative(String str, String str2, boolean z);

    private native void startUpdateQueueNative();

    private native void updateNative(Set<String> set, int i);

    private native void updatePackNative(String str, String str2, int i, IZCacheCore.UpdateCallbackInner updateCallbackInner);

    public void init(IZCache iZCache) {
        loadSO();
        this.real = iZCache;
        if (isSoLoadedSucc()) {
            setProxyNative(this);
        }
    }

    private void loadSO() {
        if (this.lock.tryLock()) {
            try {
                System.loadLibrary("zcachecore");
                this.initState = 1;
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
            this.lock.unlock();
            initCount++;
        }
    }

    public boolean isSoLoadedSucc() {
        if (this.initState == 0 && initCount <= 5) {
            loadSO();
        }
        return this.initState == 1;
    }

    public String initZCacheFolder() {
        if (this.real != null) {
            return this.real.initZCacheFolder();
        }
        ZCLog.e("initZCacheFolder called, but real is null");
        return null;
    }

    public String initTempFolder() {
        if (this.real != null) {
            return this.real.initTempFolder();
        }
        ZCLog.e("initTempFolder called, but real is null");
        return null;
    }

    public void requestZConfig(ZConfigUpdateInfo zConfigUpdateInfo, long j) {
        if (this.real != null) {
            this.real.requestZConfig(zConfigUpdateInfo, j);
        } else {
            ZCLog.e("requestZConfig called, but real is null");
        }
    }

    public void requestAppConfig(AppConfigUpdateInfo appConfigUpdateInfo, long j) {
        if (this.real != null) {
            this.real.requestAppConfig(appConfigUpdateInfo, j);
        } else {
            ZCLog.e("requestAppConfig called, but real is null");
        }
    }

    public void requestZIP(String str, long j) {
        if (this.real != null) {
            this.real.requestZIP(str, j);
        } else {
            ZCLog.e("requestZIP called, but real is null");
        }
    }

    public void requestConfig(String str, long j) {
        if (this.real != null) {
            this.real.requestConfig(str, j);
        } else {
            ZCLog.e("requestConfig called, but real is null");
        }
    }

    public void sendRequest(ZProxyRequest zProxyRequest, long j) {
        if (this.real != null) {
            this.real.sendRequest(zProxyRequest, j);
        } else {
            ZCLog.e("sendRequest called, but real is null");
        }
    }

    public int networkStatus() {
        if (this.real != null) {
            return this.real.networkStatus();
        }
        ZCLog.e("networkStatus called, but real is null");
        return 0;
    }

    public Error unzip(String str, String str2) {
        if (this.real != null) {
            return this.real.unzip(str, str2);
        }
        ZCLog.e("unzip called, but real is null");
        Error error = new Error();
        error.errCode = 1;
        error.errMsg = "real is null";
        return error;
    }

    public boolean verifySign(byte[] bArr, byte[] bArr2) {
        if (this.real != null) {
            return this.real.verifySign(bArr, bArr2);
        }
        return false;
    }

    public void sendLog(int i, String str) {
        if (this.real != null) {
            this.real.sendLog(i, str);
        }
    }

    public void onFirstUpdateQueueFinished(int i) {
        if (this.real != null) {
            this.real.onFirstUpdateQueueFinished(i);
        }
    }

    public void commitMonitor(String str, HashMap<String, String> hashMap, HashMap<String, Double> hashMap2) {
        if (this.real != null) {
            this.real.commitMonitor(str, hashMap, hashMap2);
        }
    }

    public void subscribePushMessageByGroup(List<String> list, String str, long j) {
        if (this.real != null) {
            this.real.subscribePushMessageByGroup(list, str, j);
        }
    }

    public String getSessionID() {
        if (isSoLoadedSucc()) {
            return getSessionIDNative();
        }
        return null;
    }

    public void update(Set<String> set, int i) {
        if (isSoLoadedSucc()) {
            updateNative(set, i);
        }
    }

    public void updatePack(String str, String str2, int i, IZCacheCore.UpdateCallbackInner updateCallbackInner) {
        if (isSoLoadedSucc()) {
            updatePackNative(str, str2, i, updateCallbackInner);
        }
    }

    public void removeAllZCache() {
        if (isSoLoadedSucc()) {
            removeAllZCacheNative();
        }
    }

    public String getMiniAppFilePath(String str, String str2) {
        return isSoLoadedSucc() ? getMiniAppFilePathNative(str, str2) : "";
    }

    public boolean isPackInstalled(String str) {
        if (isSoLoadedSucc()) {
            return isPackInstalledNative(str);
        }
        return false;
    }

    public void initApps(Set<String> set) {
        if (isSoLoadedSucc()) {
            initAppsNative(set);
        }
    }

    public void receiveZConfigUpdateMessage(List<String> list, long j) {
        if (isSoLoadedSucc()) {
            receiveZConfigUpdateMessageNative(list, j);
        }
    }

    public void invokeZCacheDev(String str, String str2, IZCacheCore.DevCallback devCallback) {
        if (isSoLoadedSucc()) {
            invokeDevBridge(str, str2, devCallback);
        }
    }

    public void installPreload(String str) {
        if (isSoLoadedSucc()) {
            installPreloadNative(str);
        }
    }

    public void removeAZCache(String str) {
        if (isSoLoadedSucc()) {
            removeAZCacheNative(str);
        }
    }

    public void initConfig() {
        if (isSoLoadedSucc()) {
            initConfigNative();
        }
    }

    public void setUseNewUnzip(boolean z) {
        if (isSoLoadedSucc()) {
            setUseNewUnZipNative(z);
        }
    }

    public void setupWithHTTP(String str, String str2, boolean z) {
        if (isSoLoadedSucc()) {
            setupWithHTTPNative(str, str2, z);
        }
    }

    public void setupSubProcess() {
        if (isSoLoadedSucc()) {
            setupSubProcessNative();
        }
    }

    public void setEnv(int i) {
        if (isSoLoadedSucc()) {
            setEnvNative(i);
        }
    }

    public void startUpdateQueue() {
        if (isSoLoadedSucc()) {
            startUpdateQueueNative();
        }
    }

    public void onForeground() {
        if (isSoLoadedSucc()) {
            onForegroundNative();
        }
    }

    public void onBackground() {
        if (isSoLoadedSucc()) {
            onBackgroundNative();
        }
    }

    public ResourceInfo getResourceInfo(String str, int i) {
        if (isSoLoadedSucc()) {
            return getResourceInfoNative(str, i);
        }
        return null;
    }

    public void registerAppInfoCallback(String str, IZCacheCore.AppInfoCallback appInfoCallback) {
        if (isSoLoadedSucc()) {
            registerAppInfoCallbackNative(str, appInfoCallback);
        }
    }

    public ResourceItemInfo getResourceItemInfoForApp(String str, String str2) {
        if (isSoLoadedSucc()) {
            return getResourceItemInfoForAppNative(str, str2);
        }
        return null;
    }

    public void onRequestZConfigCallback(long j, String str, int i, String str2) {
        if (isSoLoadedSucc()) {
            onRequestZConfigCallbackNative(j, str, i, str2);
        }
    }

    public void onRequestAppConfigCallback(long j, String str, int i, String str2) {
        if (isSoLoadedSucc()) {
            onRequestAppConfigCallbackNative(j, str, i, str2);
        }
    }

    public void onRequestZIPCallback(long j, String str, int i, int i2, String str2) {
        if (isSoLoadedSucc()) {
            onRequestZIPCallbackNative(j, str, i, i2, str2);
        }
    }

    public void onRequestConfigCallback(long j, String str, int i, int i2, String str2) {
        if (isSoLoadedSucc()) {
            onRequestConfigCallbackNative(j, str, i, i2, str2);
        }
    }

    public void onSendRequestCallback(long j, String str, int i, int i2, String str2) {
        if (isSoLoadedSucc()) {
            if (!TextUtils.isEmpty(str2) && str2.length() > 500) {
                str2 = str2.substring(0, 500);
            }
            onSendRequestCallbackNative(j, str, i, i2, str2);
        }
    }
}
