package com.ut.mini.anti_cheat;

import alimama.com.unweventparse.constants.EventConstants;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.alibaba.analytics.utils.BuildCompatUtils;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.anti_cheat.ScreenshotDetector;
import com.ut.mini.extend.UTExtendSwitch;
import com.ut.mini.module.appstatus.UTAppStatusDelayCallbacks;
import com.ut.mini.module.appstatus.UTAppStatusRegHelper;

public class AntiCheatTracker implements UTAppStatusDelayCallbacks, ScreenshotDetector.ScreenshotListener {
    private static AntiCheatTracker instance = new AntiCheatTracker();
    private boolean init = false;
    private Activity mActivity = null;
    private ScreenshotDetector mDetector = null;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onScreenCapturedWithDeniedPermission() {
    }

    public void onSwitchBackground() {
    }

    public static AntiCheatTracker getInstance() {
        return instance;
    }

    public void init(Application application) {
        if (UTExtendSwitch.bAntiCheat && !BuildCompatUtils.isAtLeastQ()) {
            Logger.i();
            if (!this.init) {
                this.init = true;
                this.mDetector = new ScreenshotDetector(application.getBaseContext());
                UTAppStatusRegHelper.registerAppStatusCallbacks(this);
            }
        }
    }

    public void onSwitchBackgroundDelay() {
        if (this.mDetector != null) {
            this.mDetector.stop();
        }
    }

    public void onSwitchForeground() {
        if (this.mDetector != null) {
            this.mDetector.start(this);
        }
    }

    public void onActivityPaused(Activity activity) {
        this.mActivity = null;
    }

    public void onActivityResumed(Activity activity) {
        this.mActivity = activity;
    }

    public void onScreenCaptured(String str) {
        Logger.i();
        try {
            String currentPageName = UTPageHitHelper.getInstance().getCurrentPageName();
            String str2 = "";
            if (this.mActivity != null) {
                str2 = this.mActivity.getClass().getCanonicalName();
            }
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("screen_capture");
            uTCustomHitBuilder.setEventPage("anti_cheat");
            uTCustomHitBuilder.setProperty(EventConstants.UT.PAGE_NAME, currentPageName);
            uTCustomHitBuilder.setProperty("contain_name", str2);
            uTCustomHitBuilder.setProperty("current_time", System.currentTimeMillis() + "");
            UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        } catch (Throwable unused) {
        }
    }
}
