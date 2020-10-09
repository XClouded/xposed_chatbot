package com.taobao.application.common;

import android.app.Application;

public interface Apm {

    public interface OnActivityLifecycleCallbacks extends Application.ActivityLifecycleCallbacks {
    }

    public interface OnApmEventListener extends IApmEventListener {
    }

    public interface OnAppLaunchListener extends IAppLaunchListener {
    }

    public interface OnPageListener extends IPageListener {
    }
}
