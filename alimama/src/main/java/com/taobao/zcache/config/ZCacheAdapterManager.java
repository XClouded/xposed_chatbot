package com.taobao.zcache.config;

public class ZCacheAdapterManager {
    private static ZCacheAdapterManager instance;
    private IZConfigRequest request;
    private IZCacheUpdate update;

    public static ZCacheAdapterManager getInstance() {
        if (instance == null) {
            synchronized (ZCacheAdapterManager.class) {
                if (instance == null) {
                    instance = new ZCacheAdapterManager();
                }
            }
        }
        return instance;
    }

    public void setRequest(IZConfigRequest iZConfigRequest) {
        this.request = iZConfigRequest;
    }

    public IZConfigRequest getRequest() {
        return this.request;
    }

    public void setUpdateImpl(IZCacheUpdate iZCacheUpdate) {
        this.update = iZCacheUpdate;
    }

    public IZCacheUpdate getUpdateImpl() {
        return this.update;
    }
}
