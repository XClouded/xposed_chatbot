package com.ali.telescope.internal.plugins.fdoverflow;

import android.app.Application;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.plugins.SoLoader;
import com.ali.telescope.util.Reflector;
import com.ali.telescope.util.TelescopeLog;
import java.io.FileDescriptor;
import java.lang.reflect.Field;
import libcore.io.BlockGuardOsWrapper;
import org.json.JSONObject;

public class FdOverflowMonitorPlugin extends Plugin implements BlockGuardOsWrapper.FDNewListener {
    private Field mDescriptorField;
    /* access modifiers changed from: private */
    public int mFdLimit;
    private int mFdThreshold;
    /* access modifiers changed from: private */
    public boolean mHasReported;
    /* access modifiers changed from: private */
    public boolean mIsDebug;
    /* access modifiers changed from: private */
    public boolean mIsDestroy;
    Runnable mRunnable = new Runnable() {
        public void run() {
            if (!FdOverflowMonitorPlugin.this.mHasReported && !FdOverflowMonitorPlugin.this.mIsDestroy) {
                boolean unused = FdOverflowMonitorPlugin.this.mHasReported = true;
                try {
                    String[] fileList = FdInfoFetcher.getFileList();
                    if (fileList != null) {
                        if (FdOverflowMonitorPlugin.this.mIsDebug) {
                            TelescopeLog.d(FdOverflowMonitorPlugin.this.pluginID, "mFdLimit : " + FdOverflowMonitorPlugin.this.mFdLimit);
                            for (String str : fileList) {
                                if (str != null) {
                                    TelescopeLog.d(FdOverflowMonitorPlugin.this.pluginID, str);
                                }
                            }
                        }
                        FdOverflowMonitorPlugin.this.mTelescopeContext.getBeanReport().send(new FdOverflowReportBean(System.currentTimeMillis(), fileList));
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public ITelescopeContext mTelescopeContext;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mDescriptorField = Reflector.field(FileDescriptor.class, "descriptor");
        this.mTelescopeContext = iTelescopeContext;
        SoLoader.loadHookSo();
        this.mFdLimit = FdInfoFetcher.getFdLimit();
        if (this.mFdLimit == -1) {
            this.mFdLimit = 1024;
        }
        if (jSONObject != null) {
            int optInt = jSONObject.optInt("threshold", 90);
            this.mIsDebug = jSONObject.optBoolean("debug", false);
            this.mFdThreshold = (this.mFdLimit * optInt) / 100;
        } else {
            this.mFdThreshold = (this.mFdLimit * 90) / 100;
        }
        BlockGuardOsWrapper.instance().setFDNewListener(this);
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mIsDestroy = true;
        BlockGuardOsWrapper.instance().setFDNewListener((BlockGuardOsWrapper.FDNewListener) null);
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
    }

    public void onNewFd(FileDescriptor fileDescriptor) {
        if (fileDescriptor != null) {
            try {
                if (((Integer) this.mDescriptorField.get(fileDescriptor)).intValue() > this.mFdThreshold) {
                    BlockGuardOsWrapper.instance().setFDNewListener((BlockGuardOsWrapper.FDNewListener) null);
                    Loopers.getTelescopeHandler().post(this.mRunnable);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
