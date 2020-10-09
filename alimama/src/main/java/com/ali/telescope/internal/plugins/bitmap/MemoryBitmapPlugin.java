package com.ali.telescope.internal.plugins.bitmap;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.internal.data.DataManagerProxy;
import com.ali.telescope.internal.plugins.SoLoader;
import com.ali.telescope.internal.plugins.bitmap.BitmapMonitor;
import com.ali.telescope.util.TMSharePreferenceUtil;
import com.ali.telescope.util.TelescopeLog;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;

public class MemoryBitmapPlugin extends Plugin implements BitmapMonitor.CallBack {
    private static final int DEFAULT_BITMAP_BIGSIZE = 8388608;
    private static final int DEFAULT_SCREEN_SIZE = 2073600;
    private static final int DEFAULT_UI_THREAD_BIGSIZE = 2097152;
    Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            String unused = MemoryBitmapPlugin.this.topPageName = activity.getClass().getName();
        }
    };
    private int mAllowPickTimes = 3;
    private Application mApplication;
    private int mBitmapBigSize = 8388608;
    private long mCurrentPickTimes;
    private boolean mIsCommit;
    /* access modifiers changed from: private */
    public boolean mIsDebug;
    private boolean mIsDestroyed;
    private String mPrefKey;
    private Set<Integer> mRecorded = new HashSet();
    /* access modifiers changed from: private */
    public ITelescopeContext mTelescopeContext;
    private Handler mUIHandler;
    private int mUIThreadBitmapBigSize = 2097152;
    /* access modifiers changed from: private */
    public volatile String topPageName;

    public static class Result {
        public int bitmapSize;
        public boolean isMainThread;
        public Throwable stackTrace;
        public String topActivityName;
    }

    public void onEvent(int i, Event event) {
    }

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        this.mTelescopeContext = iTelescopeContext;
        this.mApplication = application;
        this.boundType = 0;
        if (BitmapMonitor.isSupport()) {
            if (jSONObject != null) {
                this.mBitmapBigSize = jSONObject.optInt("non_ui_thread_bitmap_size", 8388608);
                this.mUIThreadBitmapBigSize = jSONObject.optInt("ui_thread_bitmap_size", 2097152);
                this.mIsDebug = jSONObject.optBoolean("debug", false);
                this.mAllowPickTimes = jSONObject.optInt("pick_times", 3);
            }
            this.mPrefKey = "bitmap_pick_times_" + AppConfig.versionName;
            this.mCurrentPickTimes = TMSharePreferenceUtil.getLong(this.mApplication, this.mPrefKey, 0);
            if (this.mCurrentPickTimes < ((long) this.mAllowPickTimes)) {
                SoLoader.loadHookSo();
                float screenWidth = ((float) (DeviceInfoManager.instance().getScreenWidth() * DeviceInfoManager.instance().getScreenHeight())) / 2073600.0f;
                this.mBitmapBigSize = (int) (((float) this.mBitmapBigSize) * screenWidth);
                this.mUIThreadBitmapBigSize = (int) (screenWidth * ((float) this.mUIThreadBitmapBigSize));
                if (this.mIsDebug) {
                    String str = this.pluginID;
                    TelescopeLog.d(str, "mBitmapBigSize : " + this.mBitmapBigSize + " mUIThreadBitmapBigSize : " + this.mUIThreadBitmapBigSize);
                }
                BitmapMonitor.init(this.mUIThreadBitmapBigSize, this.mBitmapBigSize);
                BitmapMonitor.setCallBack(this);
                this.mUIHandler = new Handler(Looper.getMainLooper());
                this.mApplication.registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
            }
        }
    }

    public void onDestroy() {
        this.mIsDestroyed = true;
    }

    public void callback(int i, Throwable th, boolean z) {
        if (this.mIsDebug) {
            String str = this.pluginID;
            TelescopeLog.d(str, "bitmapSize : " + i + " trace: " + Log.getStackTraceString(th));
        }
        if (!this.mIsDestroyed) {
            if (!this.mIsCommit) {
                this.mIsCommit = true;
                TMSharePreferenceUtil.putLong(this.mApplication, this.mPrefKey, this.mCurrentPickTimes + 1);
            }
            String stackTraceString = Log.getStackTraceString(th);
            int length = stackTraceString.length();
            if (!this.mRecorded.contains(Integer.valueOf(length))) {
                this.mRecorded.add(Integer.valueOf(length));
                if (stackTraceString != null && z && !stackTraceString.contains("View.buildDrawingCache")) {
                    final Result result = new Result();
                    result.bitmapSize = i;
                    result.stackTrace = th;
                    result.isMainThread = z;
                    this.mUIHandler.post(new Runnable() {
                        public void run() {
                            result.topActivityName = MemoryBitmapPlugin.this.topPageName != null ? MemoryBitmapPlugin.this.topPageName : "";
                            MemoryBitmapBean memoryBitmapBean = new MemoryBitmapBean(System.currentTimeMillis(), result.topActivityName, result.bitmapSize, result.stackTrace, result.isMainThread);
                            MemoryBitmapPlugin.this.mTelescopeContext.getBeanReport().send(memoryBitmapBean);
                            if (MemoryBitmapPlugin.this.mIsDebug) {
                                DataManagerProxy.instance().putData(PluginIDContant.KEY_MEMBITMAPPLUGIN, MemoryBitmapPlugin.this.topPageName, memoryBitmapBean);
                            }
                        }
                    });
                }
            }
        }
    }
}
