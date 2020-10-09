package com.taobao.monitor.impl.data.activity;

import com.taobao.application.common.IApmEventListener;
import com.taobao.application.common.data.AppLaunchHelper;
import com.taobao.application.common.data.BackgroundForegroundHelper;
import com.taobao.application.common.impl.ApmImpl;

class BackgroundForegroundEventImpl {
    private static final long DARK_BACKGROUND = 300000;
    private static final long LIGHT_BACKGROUND = 10000;
    /* access modifiers changed from: private */
    public final IApmEventListener apmEventListener = ApmImpl.instance().getApmEventListenerGroup();
    /* access modifiers changed from: private */
    public final BackgroundForegroundHelper backgroundForegroundHelper = new BackgroundForegroundHelper();
    private final Runnable fullBackgroundRunnable = new Runnable() {
        public void run() {
            if (BackgroundForegroundEventImpl.this.isInBackground) {
                BackgroundForegroundEventImpl.this.backgroundForegroundHelper.setIsInFullBackground(true);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean isInBackground = false;
    private final AppLaunchHelper launchHelper = new AppLaunchHelper();
    private final Runnable lightBackgroundRunnable = new Runnable() {
        public void run() {
            if (BackgroundForegroundEventImpl.this.isInBackground) {
                BackgroundForegroundEventImpl.this.apmEventListener.onEvent(50);
            }
        }
    };

    BackgroundForegroundEventImpl() {
    }

    /* access modifiers changed from: package-private */
    public void background2Foreground() {
        this.isInBackground = false;
        this.backgroundForegroundHelper.setIsInBackground(false);
        this.backgroundForegroundHelper.setIsInFullBackground(false);
        this.apmEventListener.onEvent(2);
        ApmImpl.instance().getAsyncHandler().removeCallbacks(this.fullBackgroundRunnable);
        ApmImpl.instance().getAsyncHandler().removeCallbacks(this.lightBackgroundRunnable);
    }

    /* access modifiers changed from: package-private */
    public void foreground2Background() {
        this.isInBackground = true;
        this.backgroundForegroundHelper.setIsInBackground(true);
        this.apmEventListener.onEvent(1);
        ApmImpl.instance().getAsyncHandler().postDelayed(this.fullBackgroundRunnable, 300000);
        ApmImpl.instance().getAsyncHandler().postDelayed(this.lightBackgroundRunnable, LIGHT_BACKGROUND);
    }
}
