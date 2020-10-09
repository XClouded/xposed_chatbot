package com.ali.telescope.internal.plugins.memory;

import android.app.Application;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.TimeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class MemoryPlugin extends Plugin {
    private static final int DEFAULT_PICK_INTEVAL = 3000;
    private static final int DEFAULT_REPORT_INTEVAL = 55000;
    /* access modifiers changed from: private */
    public boolean isDestroyed = false;
    private boolean isPaused = false;
    private boolean isProcessing = false;
    private Application mApplication;
    private ITelescopeContext mITelescopeContext;
    private List<MemoryRecord> mMemoryRecordList = Collections.synchronizedList(new ArrayList());
    /* access modifiers changed from: private */
    public int mPickInterval = 3000;
    /* access modifiers changed from: private */
    public Runnable mPickRunnable = new Runnable() {
        public void run() {
            if (!MemoryPlugin.this.isDestroyed) {
                MemoryPlugin.this.pickMemory();
                Loopers.getTelescopeHandler().postDelayed(MemoryPlugin.this.mPickRunnable, (long) MemoryPlugin.this.mPickInterval);
            }
        }
    };
    private int mReportInterval = DEFAULT_REPORT_INTEVAL;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mApplication = application;
        this.mITelescopeContext = iTelescopeContext;
        if (jSONObject != null) {
            this.mPickInterval = jSONObject.optInt("pick_interval", 3000);
            this.mReportInterval = jSONObject.optInt("report_interval", DEFAULT_REPORT_INTEVAL);
        }
        this.mITelescopeContext.registerBroadcast(1, this.pluginID);
        this.mITelescopeContext.registerBroadcast(2, this.pluginID);
        Loopers.getTelescopeHandler().post(this.mPickRunnable);
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
        this.isPaused = true;
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
        this.isPaused = false;
    }

    public void onDestroy() {
        super.onDestroy();
        this.isDestroyed = true;
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
        if (!this.isDestroyed && i != 1 && i == 2) {
            AppEvent appEvent = (AppEvent) event;
            if (appEvent.subEvent == 1) {
                Loopers.getTelescopeHandler().removeCallbacks(this.mPickRunnable);
            } else if (appEvent.subEvent == 2) {
                Loopers.getTelescopeHandler().post(this.mPickRunnable);
            }
        }
    }

    public boolean isPaused() {
        return this.isPaused && !this.isProcessing;
    }

    /* access modifiers changed from: private */
    public void pickMemory() {
        if (!this.isDestroyed && !this.isPaused) {
            this.isProcessing = true;
            MemoryRecord realTimeStatus = MemoryTracker.getRealTimeStatus(this.mApplication);
            this.isProcessing = false;
            if (realTimeStatus != null) {
                this.mITelescopeContext.getBeanReport().send(new MemoryBean(TimeUtils.getTime(), realTimeStatus));
            }
        }
    }
}
