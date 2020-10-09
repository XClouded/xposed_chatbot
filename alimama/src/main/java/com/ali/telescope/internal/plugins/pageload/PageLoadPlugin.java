package com.ali.telescope.internal.plugins.pageload;

import android.app.Application;
import com.ali.telescope.base.event.ActivityEvent;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.INameConverter;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.TimeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class PageLoadPlugin extends Plugin {
    private static final int DEFAULT_REPORT_INTEVAL = 30000;
    private static final String TAG = "pageload@PageLoadPlugin";
    Application mApplication;
    INameConverter mINameConvert;
    ITelescopeContext mITelescopeContext;
    List<PageLoadRecord> mPageLoadRecords = Collections.synchronizedList(new ArrayList());
    int mReportInterval = 30000;
    /* access modifiers changed from: private */
    public Runnable mReportRunnable = new Runnable() {
        public void run() {
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    ArrayList arrayList;
                    if (PageLoadPlugin.this.mPageLoadRecords != null && !PageLoadPlugin.this.mPageLoadRecords.isEmpty()) {
                        synchronized (PageLoadPlugin.this.mPageLoadRecords) {
                            arrayList = new ArrayList(PageLoadPlugin.this.mPageLoadRecords);
                            PageLoadPlugin.this.mPageLoadRecords.clear();
                        }
                        byte[] bArr = new PageLoadBean(TimeUtils.getTime(), arrayList).body;
                    }
                }
            });
            Loopers.getTelescopeHandler().postDelayed(PageLoadPlugin.this.mReportRunnable, (long) PageLoadPlugin.this.mReportInterval);
        }
    };

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mApplication = application;
        this.mITelescopeContext = iTelescopeContext;
        this.mINameConvert = iTelescopeContext.getNameConverter();
        if (jSONObject != null) {
            this.mReportInterval = jSONObject.optInt("report_interval", 30000);
        }
        this.mITelescopeContext.registerBroadcast(1, this.pluginID);
        this.mITelescopeContext.registerBroadcast(2, this.pluginID);
        new PageLoadMonitor(this.mApplication, this).start();
        Loopers.getTelescopeHandler().postDelayed(this.mReportRunnable, (long) this.mReportInterval);
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
        if (i == 1) {
            int i2 = ((ActivityEvent) event).subEvent;
        } else if (i == 2) {
            AppEvent appEvent = (AppEvent) event;
            if (appEvent.subEvent == 1) {
                TsAppStat.mIsInBackGround = true;
            } else if (appEvent.subEvent == 2) {
                TsAppStat.mIsInBackGround = false;
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
    }
}
