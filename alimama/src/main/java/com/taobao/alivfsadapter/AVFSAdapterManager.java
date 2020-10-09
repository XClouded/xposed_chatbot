package com.taobao.alivfsadapter;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.taobao.alivfsadapter.appmonitor.AVFSSDKAppMonitorImpl;
import com.taobao.alivfsadapter.database.alidb.AVFSAliDBFactory;
import com.taobao.alivfsadapter.database.alidb.AVFSAliDBLogger;
import com.taobao.alivfsadapter.utils.AVFSApplicationUtils;
import com.taobao.android.alivfsdb.AliDBLogger;
import com.taobao.orange.OConstant;

public class AVFSAdapterManager {
    private static final String TAG = "AVFSAdapterManager";
    private static volatile AVFSAdapterManager sInstance;
    private Application mApplication;
    private AVFSDBFactory mDBFactory;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public final Runnable mInitRunnable = new Runnable() {
        public void run() {
            synchronized (AVFSAdapterManager.this.mInitRunnable) {
                AVFSAdapterManager.this.ensureInitialized(AVFSApplicationUtils.getApplication(), (AVFSSDKAppMonitor) null, (AVFSDBFactory) null);
                AVFSAdapterManager.this.mInitRunnable.notify();
            }
        }
    };
    private boolean mInitialized = false;
    private AVFSSDKAppMonitor mMonitor;

    public static synchronized AVFSAdapterManager getInstance() {
        AVFSAdapterManager aVFSAdapterManager;
        synchronized (AVFSAdapterManager.class) {
            if (sInstance == null && sInstance == null) {
                sInstance = new AVFSAdapterManager();
            }
            aVFSAdapterManager = sInstance;
        }
        return aVFSAdapterManager;
    }

    public void ensureInitialized(Application application) {
        ensureInitialized(application, (AVFSSDKAppMonitor) null, (AVFSDBFactory) null);
    }

    public void ensureInitialized() {
        if (!isInitialized()) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                this.mHandler.post(this.mInitRunnable);
                synchronized (this.mInitRunnable) {
                    try {
                        this.mInitRunnable.wait();
                    } catch (IllegalStateException unused) {
                        this.mInitialized = false;
                        return;
                    } catch (InterruptedException unused2) {
                        this.mInitialized = false;
                        return;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                return;
            }
            ensureInitialized(AVFSApplicationUtils.getApplication(), (AVFSSDKAppMonitor) null, (AVFSDBFactory) null);
        }
    }

    public synchronized void ensureInitialized(Application application, AVFSSDKAppMonitor aVFSSDKAppMonitor, AVFSDBFactory aVFSDBFactory) {
        initialize(application, aVFSSDKAppMonitor, aVFSDBFactory);
    }

    private void initialize(Application application, AVFSSDKAppMonitor aVFSSDKAppMonitor, AVFSDBFactory aVFSDBFactory) {
        this.mApplication = application;
        if (aVFSSDKAppMonitor == null) {
            try {
                Class.forName(OConstant.REFLECT_APPMONITOR);
                Class.forName("com.alibaba.mtl.appmonitor.AppMonitorStatTable");
                this.mMonitor = new AVFSSDKAppMonitorImpl();
            } catch (ClassNotFoundException unused) {
            }
        } else {
            this.mMonitor = aVFSSDKAppMonitor;
        }
        if (aVFSDBFactory == null) {
            try {
                Class.forName("com.taobao.android.alivfsdb.AliDB");
                this.mDBFactory = new AVFSAliDBFactory();
                AliDBLogger.logger = new AVFSAliDBLogger();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                this.mDBFactory = new AVFSDefaultDBFactory();
            }
        } else {
            this.mDBFactory = aVFSDBFactory;
        }
        this.mInitialized = this.mApplication != null;
        Log.d(TAG, "- AVFSAdapterManager initialize: mInitialized=" + this.mInitialized);
    }

    public AVFSDBFactory getDBFactory() {
        ensureInitialized();
        if (this.mDBFactory != null) {
            return this.mDBFactory;
        }
        throw new RuntimeException("AVFSAdapterManager not initialized!");
    }

    public AVFSSDKAppMonitor getCacheMonitor() {
        ensureInitialized();
        return this.mMonitor;
    }

    public Application getApplication() {
        ensureInitialized();
        if (this.mApplication != null) {
            return this.mApplication;
        }
        throw new RuntimeException("AVFSAdapterManager not initialized!");
    }

    public boolean isInitialized() {
        return this.mInitialized;
    }
}
