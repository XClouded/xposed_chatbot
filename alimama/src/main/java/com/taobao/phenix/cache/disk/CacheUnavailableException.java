package com.taobao.phenix.cache.disk;

public class CacheUnavailableException extends Exception {
    public CacheUnavailableException(DiskCache diskCache, String str) {
        super("disk cache=" + diskCache + " open failed, url=" + str);
    }
}
