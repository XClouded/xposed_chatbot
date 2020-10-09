package com.ali.telescope.internal.plugins.cpu;

import android.app.Application;
import com.ali.telescope.base.event.ActivityEvent;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.TimeUtils;
import com.ali.telescope.util.process.CpuTracker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class CpuPlugin extends Plugin {
    private static final int DEFAULT_FOREGROUND_PICK_INTEVAL = 10000;
    private static final int DEFAULT_MAJOR_PICK_COUNT = 5;
    private static final int DEFAULT_MAJOR_PICK_INTEVAL = 2000;
    private static final int DEFAULT_REPORT_INTEVAL = 30000;
    private static final String TAG = "CpuPlugin";
    private boolean isDestroyed = false;
    private boolean isPaused = false;
    private boolean isProcessing = false;
    Application mApplication;
    List<CpuRecord> mCpuRecordList = Collections.synchronizedList(new ArrayList());
    int mForegroundPickInterval = 10000;
    ITelescopeContext mITelescopeContext;
    int mMajorPickCount = 5;
    int mMajorPickInterval = 2000;
    /* access modifiers changed from: private */
    public Runnable mMajorPickRunnable = new Runnable() {
        public void run() {
            if (CpuPlugin.this.mTempCount < CpuPlugin.this.mMajorPickCount) {
                CpuPlugin.this.pickCpuData();
                Loopers.getTelescopeHandler().postDelayed(CpuPlugin.this.mMajorPickRunnable, (long) CpuPlugin.this.mMajorPickInterval);
                CpuPlugin.this.mTempCount++;
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mPickRunnable = new Runnable() {
        public void run() {
            CpuPlugin.this.pickCpuData();
            Loopers.getTelescopeHandler().postDelayed(CpuPlugin.this.mPickRunnable, (long) CpuPlugin.this.mForegroundPickInterval);
        }
    };
    int mReportInterval = 30000;
    int mTempCount = 0;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mApplication = application;
        this.mITelescopeContext = iTelescopeContext;
        if (jSONObject != null) {
            this.mForegroundPickInterval = jSONObject.optInt("foreground_pick_interval", 10000);
            this.mMajorPickInterval = jSONObject.optInt("major_pick_interval", 2000);
            this.mMajorPickCount = jSONObject.optInt("major_pick_count", 2000);
            this.mReportInterval = jSONObject.optInt("report_interval", 30000);
        }
        this.mITelescopeContext.registerBroadcast(1, this.pluginID);
        this.mITelescopeContext.registerBroadcast(2, this.pluginID);
        Loopers.getTelescopeHandler().post(this.mPickRunnable);
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
        if (!this.isDestroyed) {
            if (i == 1) {
                if (((ActivityEvent) event).subEvent == 1) {
                    Loopers.getTelescopeHandler().post(this.mMajorPickRunnable);
                }
            } else if (i == 2) {
                AppEvent appEvent = (AppEvent) event;
                if (appEvent.subEvent == 1) {
                    Loopers.getTelescopeHandler().removeCallbacks(this.mPickRunnable);
                    Loopers.getTelescopeHandler().post(this.mMajorPickRunnable);
                } else if (appEvent.subEvent == 2) {
                    Loopers.getTelescopeHandler().removeCallbacks(this.mMajorPickRunnable);
                    Loopers.getTelescopeHandler().post(this.mPickRunnable);
                }
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.isDestroyed = true;
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
        this.isPaused = true;
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
        this.isPaused = false;
    }

    public boolean isPaused() {
        return this.isPaused && !this.isProcessing;
    }

    /* access modifiers changed from: private */
    public void pickCpuData() {
        if (!this.isDestroyed && !this.isPaused) {
            this.isProcessing = true;
            CpuRecord generateCpuStat = CpuTracker.generateCpuStat();
            if (generateCpuStat != null) {
                CpuBean cpuBean = new CpuBean(TimeUtils.getTime(), generateCpuStat);
                if (cpuBean.body != null) {
                    this.mITelescopeContext.getBeanReport().send(cpuBean);
                }
            }
            this.isProcessing = false;
        }
    }
}
