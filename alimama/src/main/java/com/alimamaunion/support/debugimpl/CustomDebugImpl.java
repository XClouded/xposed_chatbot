package com.alimamaunion.support.debugimpl;

import android.app.Activity;
import android.app.Application;

public interface CustomDebugImpl {
    boolean clear(Application application);

    void switchEnv(Activity activity);
}
