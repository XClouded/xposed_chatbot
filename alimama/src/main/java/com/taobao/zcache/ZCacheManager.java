package com.taobao.zcache;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.global.ZCacheGlobal;
import com.taobao.zcache.intelligent.ZIntelligentManger;
import com.taobao.zcache.log.ZLog;
import com.taobao.zcache.model.ZCacheResourceResponse;
import com.taobao.zcache.util.CommonUtils;
import com.taobao.zcachecorewrapper.IZCacheCore;
import com.taobao.zcachecorewrapper.model.AppInfo;
import com.taobao.zcachecorewrapper.model.Error;
import com.taobao.zcachecorewrapper.model.IZCacheInterface;
import com.taobao.zcachecorewrapper.model.ResourceInfo;
import com.uc.webview.export.internal.setup.br;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZCacheManager {
    private static ZCacheManager INSTANCE;
    /* access modifiers changed from: private */
    public IZCacheInterface realZCache = null;
    public ServiceConnection zcacheProxy = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ZLog.i("service connected, name=[" + componentName + Operators.ARRAY_END_STR);
            IZCacheInterface unused = ZCacheManager.this.realZCache = IZCacheInterface.Stub.asInterface(iBinder);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ZLog.i("service disconnected, name=[" + componentName + Operators.ARRAY_END_STR);
        }
    };

    private ZCacheManager() {
    }

    public static ZCacheManager instance() {
        if (INSTANCE == null) {
            synchronized (ZCacheManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ZCacheManager();
                }
            }
        }
        return INSTANCE;
    }

    public ZCacheResourceResponse getZCacheResource(String str) {
        return getZCacheResource(str, new HashMap());
    }

    public ZCacheResourceResponse getZCacheResource(String str, @Nullable Map<String, String> map) {
        if (CommonUtils.isMainProcess(ZCacheGlobal.instance().context())) {
            return ZCacheResourceResponse.buildFrom(ZCacheCoreProxy.instance().getResourceInfo(str, 3), map);
        }
        if (this.realZCache == null) {
            return null;
        }
        if (this.realZCache.asBinder().isBinderAlive()) {
            try {
                return ZCacheResourceResponse.buildFrom(this.realZCache.getZCacheInfo(str, 3), map);
            } catch (RemoteException e) {
                ZLog.e(e.getMessage());
                return null;
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
            ZCacheGlobal.instance().context().bindService(intent, this.zcacheProxy, 1);
            ZLog.e("service rebind");
            return null;
        }
    }

    public void getAppPath(final String str, final IZCacheCore.AppInfoCallback appInfoCallback) {
        ZCacheCoreProxy.instance().registerAppInfoCallback(str, new IZCacheCore.AppInfoCallback() {
            public void onReceive(AppInfo appInfo, Error error) {
                String str;
                if (appInfo == null) {
                    appInfoCallback.onReceive((AppInfo) null, error);
                    return;
                }
                if (appInfo.isFirstVisit) {
                    HashMap hashMap = new HashMap(5);
                    HashMap hashMap2 = new HashMap(6);
                    hashMap.put("appName", str);
                    if (appInfo == null) {
                        str = "0";
                    } else {
                        str = String.valueOf(appInfo.seq);
                    }
                    hashMap.put("seq", str);
                    hashMap.put("errorCode", String.valueOf(error.errCode));
                    hashMap.put(ILocatable.ERROR_MSG, error.errMsg);
                    hashMap.put("isHit", appInfo.isAppInstalled ? "true" : "false");
                    if (ZIntelligentManger.getInstance().getIntelligentImpl() != null) {
                        ZIntelligentManger.getInstance().getIntelligentImpl().commitFirstVisit(hashMap, hashMap2, appInfo.appName, appInfo.isAppInstalled);
                    }
                }
                appInfoCallback.onReceive(appInfo, error);
            }
        });
    }

    public boolean isResourceInstalled(String str) {
        ResourceInfo resourceInfo;
        if (CommonUtils.isMainProcess(ZCacheGlobal.instance().context())) {
            resourceInfo = ZCacheCoreProxy.instance().getResourceInfo(str, 3);
        } else {
            if (this.realZCache != null) {
                if (this.realZCache.asBinder().isBinderAlive()) {
                    try {
                        resourceInfo = this.realZCache.getZCacheInfo(str, 3);
                    } catch (RemoteException e) {
                        ZLog.e(e.getMessage());
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
                    ZCacheGlobal.instance().context().bindService(intent, this.zcacheProxy, 1);
                    ZLog.e("service rebind");
                }
            }
            resourceInfo = null;
        }
        if (resourceInfo == null || resourceInfo.errCode != 0) {
            return false;
        }
        return true;
    }

    public void updatePack(final String str, String str2, int i, final IZCacheCore.UpdateCallback updateCallback) {
        ZLog.i("更新ZCache 3.0, appName=[" + str + Operators.ARRAY_END_STR);
        ZCacheCoreProxy.instance().updatePack(str, str2, i, new IZCacheCore.UpdateCallbackInner() {
            public void finish(Error error) {
                updateCallback.finish(str, error);
            }
        });
    }

    public String getMiniAppFilePath(String str, String str2) {
        String miniAppFilePath = ZCacheCoreProxy.instance().getMiniAppFilePath(str, str2);
        ZLog.i("zcache 3.0 ;miniApp path = [" + miniAppFilePath + Operators.ARRAY_END_STR);
        return miniAppFilePath;
    }

    public boolean isAppInstall(String str) {
        if (CommonUtils.isMainProcess(ZCacheGlobal.instance().context())) {
            return ZCacheCoreProxy.instance().isPackInstalled(str);
        }
        if (this.realZCache != null) {
            if (this.realZCache.asBinder().isBinderAlive()) {
                try {
                    return this.realZCache.isAppInstall(str);
                } catch (RemoteException e) {
                    ZLog.e(e.getMessage());
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
                ZCacheGlobal.instance().context().bindService(intent, this.zcacheProxy, 1);
                ZLog.e("service rebind");
            }
        }
        return false;
    }

    public void startUpdateQueue() {
        ZCacheCoreProxy.instance().startUpdateQueue();
    }

    public void removeAllZCache() {
        if (CommonUtils.isMainProcess(ZCacheGlobal.instance().context())) {
            ZCacheCoreProxy.instance().removeAllZCache();
        } else if (this.realZCache == null) {
        } else {
            if (this.realZCache.asBinder().isBinderAlive()) {
                try {
                    this.realZCache.removeAllZCache();
                } catch (RemoteException e) {
                    ZLog.e(e.getMessage());
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
                ZCacheGlobal.instance().context().bindService(intent, this.zcacheProxy, 1);
                ZLog.e("service rebind");
            }
        }
    }

    public String getSeesionID() {
        return ZCacheCoreProxy.instance().getSessionID();
    }

    public void invokeZCacheDev(String str, String str2, IZCacheCore.DevCallback devCallback) {
        ZCacheCoreProxy.instance().invokeZCacheDev(str, str2, devCallback);
        ZLog.i("ZCache Dev, name=[" + str + "], param = [" + str2 + Operators.ARRAY_END_STR);
    }

    public void receiveZConfigUpdateMessage(List<String> list, long j) {
        ZCacheCoreProxy.instance().receiveZConfigUpdateMessage(list, j);
    }

    public void initApps(Set<String> set) {
        ZCacheCoreProxy.instance().initApps(set);
    }

    public void update(Set<String> set, int i) {
        ZCacheCoreProxy.instance().update(set, i);
    }

    public void onBackground() {
        ZCacheCoreProxy.instance().onBackground();
    }

    public void onForeground() {
        ZCacheCoreProxy.instance().onForeground();
    }

    public void installPreload(String str) {
        ZLog.i("ZCache 3.0 preload, file=[" + str + Operators.ARRAY_END_STR);
        String str2 = File.separator + br.ASSETS_DIR + File.separator + str;
        if (CommonUtils.isMainProcess(ZCacheGlobal.instance().context())) {
            ZCacheCoreProxy.instance().installPreload(str2);
        } else if (this.realZCache == null) {
        } else {
            if (this.realZCache.asBinder().isBinderAlive()) {
                try {
                    this.realZCache.installPreload(str2);
                } catch (RemoteException e) {
                    ZLog.e(e.getMessage());
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
                ZCacheGlobal.instance().context().bindService(intent, this.zcacheProxy, 1);
                ZLog.e("service rebind");
            }
        }
    }

    public void removeAZCache(String str) {
        ZLog.i("remove zcache = [" + str + Operators.ARRAY_END_STR);
        if (CommonUtils.isMainProcess(ZCacheGlobal.instance().context())) {
            ZCacheCoreProxy.instance().removeAZCache(str);
        } else if (this.realZCache == null) {
        } else {
            if (this.realZCache.asBinder().isBinderAlive()) {
                try {
                    this.realZCache.removeAZCache(str);
                } catch (RemoteException e) {
                    ZLog.e(e.getMessage());
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
                ZCacheGlobal.instance().context().bindService(intent, this.zcacheProxy, 1);
                ZLog.e("service rebind");
            }
        }
    }

    public void setUseNewUnzip(boolean z) {
        ZCacheCoreProxy.instance().setUseNewUnzip(z);
    }
}
