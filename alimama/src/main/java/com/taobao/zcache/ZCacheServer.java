package com.taobao.zcache;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.taobao.zcachecorewrapper.model.IZCacheInterface;
import com.taobao.zcachecorewrapper.model.ResourceInfo;

public class ZCacheServer extends Service {
    private final IZCacheInterface.Stub binder = new IZCacheInterface.Stub() {
        public ResourceInfo getZCacheInfo(String str, int i) throws RemoteException {
            return ZCacheCoreProxy.instance().getResourceInfo(str, i);
        }

        public boolean isAppInstall(String str) throws RemoteException {
            return ZCacheCoreProxy.instance().isPackInstalled(str);
        }

        public void installPreload(String str) throws RemoteException {
            ZCacheCoreProxy.instance().installPreload(str);
        }

        public void removeAZCache(String str) throws RemoteException {
            ZCacheCoreProxy.instance().removeAZCache(str);
        }

        public void removeAllZCache() throws RemoteException {
            ZCacheCoreProxy.instance().removeAllZCache();
        }
    };

    public int onStartCommand(Intent intent, int i, int i2) {
        return 2;
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }
}
