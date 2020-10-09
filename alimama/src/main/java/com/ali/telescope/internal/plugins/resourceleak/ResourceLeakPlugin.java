package com.ali.telescope.internal.plugins.resourceleak;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.internal.data.DataManagerProxy;
import com.ali.telescope.util.Reflector;
import com.ali.telescope.util.TMSharePreferenceUtil;
import com.ali.telescope.util.TelescopeLog;
import com.ali.telescope.util.TimeUtils;
import dalvik.system.CloseGuard;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;

public class ResourceLeakPlugin extends Plugin implements CloseGuard.Reporter {
    private boolean isDebug;
    private int mAllowPickTimes = 3;
    private Application mApplication;
    private long mCurrentPickTimes;
    private boolean mIsCommit;
    private boolean mIsDestroy;
    private int mLimitCount = 20;
    private String mPrefKey;
    private Set<Integer> mRecorded = new HashSet();
    private ITelescopeContext mTelescopeContext;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mApplication = application;
        this.mTelescopeContext = iTelescopeContext;
        if (jSONObject != null) {
            this.isDebug = jSONObject.optBoolean("debug", false);
            this.mAllowPickTimes = jSONObject.optInt("pick_times", 3);
        }
        this.mPrefKey = "resource_leak_pick_times_" + AppConfig.versionName;
        this.mCurrentPickTimes = TMSharePreferenceUtil.getLong(this.mApplication, this.mPrefKey, 0);
        if (this.mCurrentPickTimes < ((long) this.mAllowPickTimes)) {
            installStrictModePolicy();
            hijackReport();
        }
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mIsDestroy = true;
    }

    private void installStrictModePolicy() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        builder.detectLeakedClosableObjects();
        builder.detectLeakedSqlLiteObjects();
        if (Build.VERSION.SDK_INT >= 16) {
            builder.detectLeakedRegistrationObjects();
        }
        builder.penaltyLog();
        StrictMode.setVmPolicy(builder.build());
    }

    private void hijackReport() {
        try {
            Reflector.field(getClass().getClassLoader(), "dalvik.system.CloseGuard", "REPORTER").set((Object) null, this);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void report(String str, Throwable th) {
        boolean z;
        if (!this.mIsDestroy) {
            if (!this.mIsCommit) {
                this.mIsCommit = true;
                TMSharePreferenceUtil.putLong(this.mApplication, this.mPrefKey, this.mCurrentPickTimes + 1);
            }
            if (this.mLimitCount > 0) {
                String stackTraceString = Log.getStackTraceString(th);
                int length = stackTraceString.length();
                synchronized (this.mRecorded) {
                    if (!this.mRecorded.contains(Integer.valueOf(length))) {
                        this.mRecorded.add(Integer.valueOf(length));
                        z = true;
                    } else {
                        z = false;
                    }
                }
                if (z) {
                    this.mLimitCount--;
                    if (this.isDebug) {
                        TelescopeLog.d(this.pluginID, stackTraceString);
                    }
                    ResourceLeakReportBean resourceLeakReportBean = new ResourceLeakReportBean(TimeUtils.getTime(), th);
                    if (this.isDebug) {
                        DataManagerProxy.instance().putData(PluginIDContant.KEY_RESOURCELEAKPLUGIN, "resource leak", resourceLeakReportBean.toString(), resourceLeakReportBean);
                    }
                    this.mTelescopeContext.getBeanReport().send(resourceLeakReportBean);
                }
            }
        }
    }
}
