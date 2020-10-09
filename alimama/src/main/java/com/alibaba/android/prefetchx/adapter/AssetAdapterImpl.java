package com.alibaba.android.prefetchx.adapter;

import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import androidx.annotation.NonNull;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo;
import com.taobao.alivfssdk.cache.AVFSCache;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;

public class AssetAdapterImpl implements AssetAdapter {
    AVFSCache cacheModule = null;

    public String getAssetFromZCache(String str) {
        try {
            return ZipAppUtils.getStreamByUrl(str);
        } catch (Throwable unused) {
            return "";
        }
    }

    public JSModulePojo getJSModuleFromFile(String str) {
        return (JSModulePojo) getCache().setClassLoader(JSModulePojo.class.getClassLoader()).getFileCache().objectForKey(str, JSModulePojo.class);
    }

    public void putJSModuleToFile(final String str, JSModulePojo jSModulePojo) {
        getCache().setClassLoader(JSModulePojo.class.getClassLoader()).getFileCache().setObjectForKey(str, (Object) jSModulePojo, (IAVFSCache.OnObjectSetCallback) new IAVFSCache.OnObjectSetCallback() {
            public void onObjectSetCallback(@NonNull String str, boolean z) {
                if (!z) {
                    PFLog.JSModule.w("putJSModuleToFile failed at key : " + str, new Throwable[0]);
                }
            }
        });
    }

    public void removeJSModule(String str) {
        getCache().setClassLoader(AssetAdapterImpl.class.getClassLoader()).getFileCache().removeObjectForKey(str);
    }

    public String getStringToFile(String str) {
        return (String) getCache().getFileCache().objectForKey(str, String.class);
    }

    public void putStringToFile(String str, String str2) {
        getCache().getFileCache().setObjectForKey(str, str2);
    }

    private synchronized AVFSCache getCache() {
        if (this.cacheModule == null) {
            this.cacheModule = AVFSCacheManager.getInstance().cacheForModule("PrefetchX");
        }
        return this.cacheModule;
    }
}
