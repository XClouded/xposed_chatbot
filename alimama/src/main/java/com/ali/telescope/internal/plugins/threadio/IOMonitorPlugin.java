package com.ali.telescope.internal.plugins.threadio;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Keep;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.internal.data.DataManagerProxy;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.TelescopeLog;
import com.ali.telescope.util.TimeUtils;
import libcore.io.BlockGuardOsWrapper;
import org.json.JSONObject;

@Keep
public class IOMonitorPlugin extends Plugin implements BlockGuardOsWrapper.IOEventListener {
    private static final String TAG = "IOMonitor";
    /* access modifiers changed from: private */
    public static boolean isDebug;
    private static boolean isDestroy;
    /* access modifiers changed from: private */
    public static ITelescopeContext mTelescopeContext;
    private static Thread sMainThread = Looper.getMainLooper().getThread();
    private int threshold = 20;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        mTelescopeContext = iTelescopeContext;
        if (jSONObject != null) {
            this.threshold = jSONObject.optInt("threshold", 20);
            isDebug = jSONObject.optBoolean("debug", false);
        }
        BlockGuardOsWrapper.instance().setIOEventListener(this);
        if (SqliteConnectionMethodHook.isSupport()) {
            Class<IOMonitorPlugin> cls = IOMonitorPlugin.class;
            Class<IOMonitorPlugin> cls2 = IOMonitorPlugin.class;
            try {
                SqliteConnectionMethodHook.hook(this.threshold, cls, cls2.getDeclaredMethod("onSqlTime", new Class[]{Long.TYPE}));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
    }

    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
    }

    public static void onSqlTime(final long j) {
        if (Thread.currentThread() == sMainThread && !isDestroy) {
            final Throwable th = new Throwable();
            final IOReportBean iOReportBean = new IOReportBean(TimeUtils.getTime(), (int) j, 3, th);
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    IOMonitorPlugin.mTelescopeContext.getBeanReport().send(iOReportBean);
                    if (IOMonitorPlugin.isDebug) {
                        TelescopeLog.d(IOMonitorPlugin.TAG, "Sql time : " + j + " stack : " + Log.getStackTraceString(th));
                    }
                }
            });
        }
    }

    public void onReadFromDisk(final int i) {
        if (i > this.threshold && !isDestroy && Thread.currentThread() == sMainThread) {
            final Throwable th = new Throwable();
            final IOReportBean iOReportBean = new IOReportBean(TimeUtils.getTime(), i, 1, th);
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    IOMonitorPlugin.mTelescopeContext.getBeanReport().send(iOReportBean);
                    if (IOMonitorPlugin.isDebug) {
                        DataManagerProxy.instance().putData(PluginIDContant.KEY_MAINTHREADIOPLUGIN, "Read", iOReportBean);
                        TelescopeLog.d(IOMonitorPlugin.TAG, "read time : " + i + " stack : " + Log.getStackTraceString(th));
                    }
                }
            });
        }
    }

    public void onWriteToDisk(final int i) {
        if (i > this.threshold && !isDestroy && Thread.currentThread() == sMainThread) {
            final Throwable th = new Throwable();
            final IOReportBean iOReportBean = new IOReportBean(TimeUtils.getTime(), i, 2, th);
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    IOMonitorPlugin.mTelescopeContext.getBeanReport().send(iOReportBean);
                    if (IOMonitorPlugin.isDebug) {
                        DataManagerProxy.instance().putData(PluginIDContant.KEY_MAINTHREADIOPLUGIN, "Write", iOReportBean);
                        TelescopeLog.d(IOMonitorPlugin.TAG, "write time : " + i + " stack : " + Log.getStackTraceString(th));
                    }
                }
            });
        }
    }
}
