package alimama.com.unwcache;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IDiskCache;
import alimama.com.unwbase.tools.UNWLog;
import android.content.Context;
import com.taobao.alivfsadapter.AVFSAdapterManager;
import com.taobao.alivfssdk.cache.AVFSCacheConfig;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;

public class DiskCacheImpl implements IDiskCache {
    private Context context = UNWManager.getInstance().application;
    private String modulename;

    public DiskCacheImpl(String str) {
        this.modulename = str;
    }

    public void init() {
        AVFSAdapterManager.getInstance().ensureInitialized(UNWManager.getInstance().application);
    }

    public void putDataToDisk(String str, Object obj) {
        try {
            AVFSCacheManager.getInstance().cacheForModule(this.modulename).setClassLoader(this.context.getClassLoader()).moduleConfig(new AVFSCacheConfig()).getFileCache().setObjectForKey(str, obj);
        } catch (Exception e) {
            UNWLog.error("TLiveCacheAdapter  writeData:" + e.getMessage());
        }
    }

    public Object getDataFromDisk(String str) {
        try {
            IAVFSCache fileCache = AVFSCacheManager.getInstance().cacheForModule(this.modulename).setClassLoader(this.context.getClassLoader()).moduleConfig(new AVFSCacheConfig()).getFileCache();
            if (fileCache == null) {
                return null;
            }
            return fileCache.objectForKey(str);
        } catch (Exception e) {
            UNWLog.error("TLiveCacheAdapter  readData:" + e.getMessage());
            return null;
        }
    }
}
