package com.alimama.moon;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;
import com.alimama.moon.App;

public class App_ForegroundBackgroundListener_LifecycleAdapter implements GeneratedAdapter {
    final App.ForegroundBackgroundListener mReceiver;

    App_ForegroundBackgroundListener_LifecycleAdapter(App.ForegroundBackgroundListener foregroundBackgroundListener) {
        this.mReceiver = foregroundBackgroundListener;
    }

    public void callMethods(LifecycleOwner lifecycleOwner, Lifecycle.Event event, boolean z, MethodCallsLogger methodCallsLogger) {
        boolean z2 = methodCallsLogger != null;
        if (z || event != Lifecycle.Event.ON_START) {
            return;
        }
        if (!z2 || methodCallsLogger.approveCall("onEnterForeground", 1)) {
            this.mReceiver.onEnterForeground();
        }
    }
}
