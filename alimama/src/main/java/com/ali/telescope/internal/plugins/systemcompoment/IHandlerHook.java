package com.ali.telescope.internal.plugins.systemcompoment;

import android.app.Application;

public interface IHandlerHook {
    boolean hook(Application application, LifecycleCallStateDispatchListener lifecycleCallStateDispatchListener);
}
