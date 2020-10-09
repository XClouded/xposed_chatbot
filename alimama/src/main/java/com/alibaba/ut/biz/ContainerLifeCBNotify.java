package com.alibaba.ut.biz;

import android.app.Activity;
import com.alibaba.ut.IWebView;
import com.alibaba.ut.comm.ActivityLifecycleCB;
import com.alibaba.ut.comm.JsBridge;
import com.alibaba.ut.utils.Logger;
import com.alibaba.ut.utils.ViewTools;
import com.ut.mini.UTAnalytics;
import java.util.HashSet;
import java.util.Set;

public class ContainerLifeCBNotify implements ActivityLifecycleCB.ActivityResumedCallBack, ActivityLifecycleCB.ActivityPausedCallBack {
    private Set<Integer> excludeWebViews = new HashSet();

    public static void addExcludeWebView(int i) {
    }

    public void init() {
        ActivityLifecycleCB.getInstance().addResumedCallback(this);
        ActivityLifecycleCB.getInstance().addPauseCallback(this);
    }

    public void onActivityResumed(Activity activity) {
        Logger.d((String) null, "activity", activity);
        IWebView findWebView = ViewTools.findWebView(activity);
        if (findWebView != null && !this.excludeWebViews.contains(Integer.valueOf(findWebView.getDelegateHashCode()))) {
            JsBridge.nativeToJs(findWebView, "Aplus4UT.onPageShow", (String[]) null);
        }
    }

    public void onActivityPaused(Activity activity) {
        Logger.d((String) null, "activity", activity);
        IWebView findWebView = ViewTools.findWebView(activity);
        if (findWebView != null && !this.excludeWebViews.contains(Integer.valueOf(findWebView.getDelegateHashCode()))) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
            JsBridge.nativeToJs(findWebView, "Aplus4UT.onPageHide", (String[]) null);
        }
    }
}
