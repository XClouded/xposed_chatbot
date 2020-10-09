package com.taobao.zcache.network;

public class ZCacheWorkProxy {
    private static ZCacheWorkProxy mConnectManager;
    private INetWorkProxy mNetWorkProxy = null;

    public static synchronized ZCacheWorkProxy getInstance() {
        ZCacheWorkProxy zCacheWorkProxy;
        synchronized (ZCacheWorkProxy.class) {
            if (mConnectManager == null) {
                mConnectManager = new ZCacheWorkProxy();
            }
            zCacheWorkProxy = mConnectManager;
        }
        return zCacheWorkProxy;
    }

    public INetWorkProxy getNetWorkProxy() {
        return this.mNetWorkProxy;
    }

    public void registerNetWork(INetWorkProxy iNetWorkProxy) {
        this.mNetWorkProxy = iNetWorkProxy;
    }
}
