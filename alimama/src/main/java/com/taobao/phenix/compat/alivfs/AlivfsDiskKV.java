package com.taobao.phenix.compat.alivfs;

import android.text.TextUtils;
import com.taobao.alivfssdk.cache.AVFSCache;
import com.taobao.alivfssdk.cache.AVFSCacheConfig;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;
import com.taobao.phenix.cache.disk.DiskCacheKeyValueStore;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.tcommon.log.FLog;

public class AlivfsDiskKV implements DiskCacheKeyValueStore {
    private static final String ALIVFS_IMAGE_PREFIX_KV = "phximgs_KV";
    private IAVFSCache mAVFSExtCache;
    private AlivfsVerifyListener mListener;
    private int mMaxSize = 2097152;
    private String mName;

    public AlivfsDiskKV(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mName = ALIVFS_IMAGE_PREFIX_KV;
            return;
        }
        this.mName = "phximgs_KV_" + str;
    }

    public String get(String str) {
        if (this.mAVFSExtCache != null) {
            return (String) this.mAVFSExtCache.objectForKey(str);
        }
        UnitedLog.e(6, "AlivfsDiskKV", "please call init before use!!!");
        return "";
    }

    public boolean put(String str, long j) {
        if (this.mAVFSExtCache != null) {
            long currentTime = getCurrentTime();
            if (currentTime <= 0) {
                return false;
            }
            return this.mAVFSExtCache.setObjectForKey(str, String.valueOf(currentTime + j));
        }
        UnitedLog.e(6, "AlivfsDiskKV", "please call init before use!!!");
        return false;
    }

    public void init() {
        AVFSCache cacheForModule;
        if (this.mAVFSExtCache == null && (cacheForModule = AVFSCacheManager.getInstance().cacheForModule(this.mName)) != null) {
            AVFSCacheConfig aVFSCacheConfig = new AVFSCacheConfig();
            aVFSCacheConfig.limitSize = Long.valueOf((long) this.mMaxSize);
            cacheForModule.moduleConfig(aVFSCacheConfig);
            this.mAVFSExtCache = cacheForModule.getFileCache();
        }
    }

    public long getCurrentTime() {
        try {
            if (this.mListener != null) {
                return this.mListener.getCurrentTime();
            }
            return -1;
        } catch (Exception e) {
            FLog.e("TTL", "ttl getCurrentTime error=%s", e);
            return -1;
        }
    }

    public boolean isTTLDomain(String str) {
        try {
            if (this.mListener == null || TextUtils.isEmpty(str)) {
                return false;
            }
            return this.mListener.isTTLDomain(str);
        } catch (Exception e) {
            FLog.e("TTL", "ttl isTTLDomain error=%s", e);
            return false;
        }
    }

    public boolean isExpectedTime(long j) {
        try {
            if (this.mListener != null) {
                return this.mListener.isExpectedTime(j);
            }
            return false;
        } catch (Exception e) {
            FLog.e("TTL", "ttl isExpectedTime error=%s", e);
            return false;
        }
    }

    public void setAlivfsVerifyListener(AlivfsVerifyListener alivfsVerifyListener) {
        this.mListener = alivfsVerifyListener;
    }
}
