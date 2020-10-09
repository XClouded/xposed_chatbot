package com.ali.telescope.internal.plugins.systemcompoment;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.plugins.PhonePerformanceLevel;
import com.ali.telescope.util.TMSharePreferenceUtil;
import com.ali.telescope.util.TelescopeLog;
import com.ali.telescope.util.TimeConsStackAnalyzer;
import com.ali.telescope.util.TimeUtils;
import dalvik.system.VMStack;
import org.json.JSONObject;

public class SystemComponentPlugin extends Plugin implements LifecycleCallStateDispatchListener {
    private static final String TAG = "SystemComponent";
    /* access modifiers changed from: private */
    public boolean enableTrace;
    /* access modifiers changed from: private */
    public boolean isDebug;
    private boolean isDestroy;
    private boolean isPause;
    /* access modifiers changed from: private */
    public boolean isStrongHook;
    /* access modifiers changed from: private */
    public int mActivityLaunchThreshold = 500;
    private int mAllowPickTimes = 3;
    private Application mApplication;
    private long mCurrentPickTimes;
    private Handler mDispatcherHandler;
    private boolean mIsCommit;
    private String mPrefKey;
    /* access modifiers changed from: private */
    public Handler mReportHandler = new ReportHandler();
    /* access modifiers changed from: private */
    public Handler mSampleHandler;
    /* access modifiers changed from: private */
    public int mSampleInterval = 2;
    /* access modifiers changed from: private */
    public ITelescopeContext mTelescopeContext;
    /* access modifiers changed from: private */
    public int mThreshold = 100;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public void onCreate(final Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mApplication = application;
        this.boundType = 7;
        this.priority = 2;
        this.mTelescopeContext = iTelescopeContext;
        if (jSONObject != null) {
            this.enableTrace = jSONObject.optBoolean("enable_trace", false);
            this.isDebug = jSONObject.optBoolean("debug", false);
            this.isStrongHook = jSONObject.optBoolean("strong_hook", false);
            this.mActivityLaunchThreshold = jSONObject.optInt("launch_activity_threshold", 500);
            this.mThreshold = jSONObject.optInt("threshold", 100);
            this.mSampleInterval = jSONObject.optInt("sample_interval", 2);
            this.mAllowPickTimes = jSONObject.optInt("pick_times", 3);
        }
        this.mPrefKey = "system_comp_pick_times_" + AppConfig.versionName;
        this.mCurrentPickTimes = TMSharePreferenceUtil.getLong(this.mApplication, this.mPrefKey, 0);
        if (this.mCurrentPickTimes < ((long) this.mAllowPickTimes)) {
            this.mTelescopeContext.registerBroadcast(2, this.pluginID);
            if (PhonePerformanceLevel.getLevel() == 2) {
                this.mActivityLaunchThreshold = (this.mActivityLaunchThreshold * 3) / 2;
                this.mThreshold = (this.mThreshold * 3) / 2;
            } else if (PhonePerformanceLevel.getLevel() == 3) {
                this.mActivityLaunchThreshold *= 3;
                this.mThreshold *= 3;
            }
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    if (!SystemComponentPlugin.this.isStrongHook || Build.VERSION.SDK_INT > 25) {
                        new HandlerCallbackHook().hook(application, SystemComponentPlugin.this);
                    } else {
                        new HandlerReplaceHook().hook(application, SystemComponentPlugin.this);
                    }
                }
            });
            HandlerThread handlerThread = new HandlerThread("SystemComponent:dispatch");
            handlerThread.start();
            this.mDispatcherHandler = new DispatchHandler(handlerThread.getLooper());
            if (this.enableTrace) {
                HandlerThread handlerThread2 = new HandlerThread("SystemComponent:sample");
                handlerThread2.start();
                this.mSampleHandler = new SampleHandler(handlerThread2.getLooper());
            }
        }
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
        if (event.eventType == 2 && !this.mIsCommit) {
            this.mIsCommit = true;
            TMSharePreferenceUtil.putLong(this.mApplication, this.mPrefKey, this.mCurrentPickTimes + 1);
        }
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
        this.isPause = true;
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
        this.isPause = false;
    }

    public boolean isPaused() {
        return this.isPause;
    }

    public void onDestroy() {
        super.onDestroy();
        this.isDestroy = true;
    }

    public void onLifecycleStateChange(LifecycleCallState lifecycleCallState) {
        if (!this.isDestroy && !this.isPause) {
            Message obtain = Message.obtain();
            if (!lifecycleCallState.isHandling) {
                obtain.what = 1;
            } else {
                obtain.what = 2;
            }
            lifecycleCallState.isHandling = true;
            obtain.obj = lifecycleCallState;
            this.mDispatcherHandler.sendMessage(obtain);
        }
    }

    private class DispatchHandler extends Handler {
        public static final int MSG_AFTER_HANDLE = 2;
        public static final int MSG_BEFORE_HANDLE = 1;

        public DispatchHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            LifecycleCallState lifecycleCallState = (LifecycleCallState) message.obj;
            if (i == 1) {
                if (SystemComponentPlugin.this.enableTrace) {
                    Message obtain = Message.obtain();
                    obtain.what = 1;
                    obtain.obj = lifecycleCallState;
                    SystemComponentPlugin.this.mSampleHandler.sendMessage(obtain);
                }
            } else if (i == 2) {
                if (SystemComponentPlugin.this.enableTrace) {
                    Message obtain2 = Message.obtain();
                    obtain2.what = 3;
                    obtain2.obj = lifecycleCallState;
                    SystemComponentPlugin.this.mSampleHandler.sendMessage(obtain2);
                } else {
                    if (lifecycleCallState.afterHandleTime - lifecycleCallState.beforeHandleTime > ((long) (lifecycleCallState.what == MessageConstants.LAUNCH_ACTIVITY ? SystemComponentPlugin.this.mActivityLaunchThreshold : SystemComponentPlugin.this.mThreshold))) {
                        Message obtain3 = Message.obtain();
                        obtain3.what = 1;
                        obtain3.obj = lifecycleCallState;
                        SystemComponentPlugin.this.mReportHandler.sendMessage(obtain3);
                    }
                }
                if (SystemComponentPlugin.this.isDebug) {
                    TelescopeLog.d("", "msg.what : " + lifecycleCallState.what + " <" + MessageConstants.readableString(lifecycleCallState.what) + "> className: " + lifecycleCallState.className + " time cost: " + (lifecycleCallState.afterHandleTime - lifecycleCallState.beforeHandleTime));
                }
            }
        }
    }

    private class SampleHandler extends Handler {
        public static final int MSG_FINISH_SAMPLE = 3;
        public static final int MSG_SAMPLE = 2;
        public static final int MSG_START_SAMPLE = 1;
        private TimeConsStackAnalyzer mTimeConsStackAnalyzer = new TimeConsStackAnalyzer();
        private Thread uiThread = Looper.getMainLooper().getThread();

        public SampleHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i;
            LifecycleCallState lifecycleCallState = (LifecycleCallState) message.obj;
            if (message.what == 1) {
                this.mTimeConsStackAnalyzer.reset();
                Message obtain = Message.obtain();
                obtain.obj = lifecycleCallState;
                obtain.what = 2;
                if (lifecycleCallState.what == MessageConstants.LAUNCH_ACTIVITY) {
                    i = SystemComponentPlugin.this.mActivityLaunchThreshold;
                } else {
                    i = SystemComponentPlugin.this.mThreshold;
                }
                SystemComponentPlugin.this.mSampleHandler.sendMessageDelayed(obtain, (long) i);
            } else if (message.what == 2) {
                if (lifecycleCallState.handleState != 1) {
                    this.mTimeConsStackAnalyzer.appendStack(VMStack.getThreadStackTrace(this.uiThread), TimeUtils.getTime());
                    lifecycleCallState.sampleTimes++;
                    Message obtain2 = Message.obtain();
                    obtain2.obj = lifecycleCallState;
                    obtain2.what = 2;
                    SystemComponentPlugin.this.mSampleHandler.sendMessageDelayed(obtain2, (long) SystemComponentPlugin.this.mSampleInterval);
                }
            } else if (message.what == 3) {
                SystemComponentPlugin.this.mSampleHandler.removeMessages(2);
                JSONObject export = this.mTimeConsStackAnalyzer.export();
                if (export != null) {
                    lifecycleCallState.methodTrace = export;
                }
                if (lifecycleCallState.afterHandleTime - lifecycleCallState.beforeHandleTime > ((long) (lifecycleCallState.what == MessageConstants.LAUNCH_ACTIVITY ? SystemComponentPlugin.this.mActivityLaunchThreshold : SystemComponentPlugin.this.mThreshold))) {
                    Message obtain3 = Message.obtain();
                    obtain3.what = 1;
                    obtain3.obj = lifecycleCallState;
                    SystemComponentPlugin.this.mReportHandler.sendMessage(obtain3);
                }
            }
            super.handleMessage(message);
        }
    }

    private class ReportHandler extends Handler {
        public static final int MSG_REPORT = 1;

        public ReportHandler() {
            super(Loopers.getTelescopeLooper());
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                LifecycleCallState lifecycleCallState = (LifecycleCallState) message.obj;
                SystemComponentReportBean systemComponentReportBean = new SystemComponentReportBean(lifecycleCallState.beforeHandleTime, lifecycleCallState.className, lifecycleCallState.what, (int) (lifecycleCallState.afterHandleTime - lifecycleCallState.beforeHandleTime), lifecycleCallState.methodTrace, lifecycleCallState.sampleTimes);
                SystemComponentPlugin.this.mTelescopeContext.getBeanReport().send(systemComponentReportBean);
                if (SystemComponentPlugin.this.isDebug) {
                    TelescopeLog.d(SystemComponentPlugin.TAG, systemComponentReportBean.getDebugUseObject().toString());
                }
            }
        }
    }
}
