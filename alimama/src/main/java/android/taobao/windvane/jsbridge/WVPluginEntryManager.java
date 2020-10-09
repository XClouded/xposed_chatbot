package android.taobao.windvane.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.webview.IWVWebView;

import com.taobao.weex.BuildConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WVPluginEntryManager {
    private Map<String, Object> entryMap = new HashMap();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Context mContext = null;
    private IWVWebView mWebView = null;

    public WVPluginEntryManager(Context context, IWVWebView iWVWebView) {
        this.mContext = context;
        this.mWebView = iWVWebView;
    }

    public void addEntry(String str, Object obj) {
        this.lock.writeLock().lock();
        try {
            this.entryMap.put(str, obj);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Object getEntry(String str) {
        if (this.mContext == null) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
        this.lock.readLock().lock();
        try {
            Object obj = this.entryMap.get(str);
            if (obj == null) {
                this.lock.writeLock().lock();
                try {
                    if (this.entryMap.get(str) == null) {
                        WVApiPlugin createPlugin = WVPluginManager.createPlugin(str, this.mContext, this.mWebView);
                        if (createPlugin != null) {
                            this.entryMap.put(str, createPlugin);
                            obj = createPlugin;
                        }
                    } else {
                        obj = this.entryMap.get(str);
                    }
                } finally {
                    this.lock.writeLock().unlock();
                }
            }
            return obj;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public void onDestroy() {
        this.lock.readLock().lock();
        try {
            for (Object next : this.entryMap.values()) {
                if (next instanceof WVApiPlugin) {
                    ((WVApiPlugin) next).onDestroy();
                }
            }
            this.lock.readLock().unlock();
            this.lock.writeLock().lock();
            try {
                this.entryMap.clear();
                this.mContext = null;
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (Throwable th) {
            this.lock.readLock().unlock();
            throw th;
        }
    }

    public void onPause() {
        this.lock.readLock().lock();
        try {
            for (Object next : this.entryMap.values()) {
                if (next instanceof WVApiPlugin) {
                    ((WVApiPlugin) next).onPause();
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void onResume() {
        this.lock.readLock().lock();
        try {
            for (Object next : this.entryMap.values()) {
                if (next instanceof WVApiPlugin) {
                    ((WVApiPlugin) next).onResume();
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.lock.readLock().lock();
        try {
            for (Object next : this.entryMap.values()) {
                if (next instanceof WVApiPlugin) {
                    ((WVApiPlugin) next).onActivityResult(i, i2, intent);
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        this.lock.readLock().lock();
        try {
            for (Object next : this.entryMap.values()) {
                if (next instanceof WVApiPlugin) {
                    ((WVApiPlugin) next).onScrollChanged(i, i2, i3, i4);
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
