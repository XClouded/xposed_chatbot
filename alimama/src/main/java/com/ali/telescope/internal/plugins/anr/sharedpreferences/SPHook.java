package com.ali.telescope.internal.plugins.anr.sharedpreferences;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import com.ali.telescope.internal.plugins.anr.FieldUtils;
import com.ali.telescope.internal.plugins.anr.IHook;
import java.lang.reflect.Field;

public class SPHook implements IHook {
    public void startHook(Application application) {
        if ("com.tmall.wireless".equals(application.getPackageName())) {
            try {
                Field declaredField = ContextWrapper.class.getDeclaredField("mBase");
                Context context = (Context) FieldUtils.getObjectFromField(application, declaredField);
                FieldUtils.setFieldToObject(application, declaredField, context, HookFactory.createContextImplProxy(context));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                try {
                    Field declaredField = ContextWrapper.class.getDeclaredField("mBase");
                    Context context = (Context) FieldUtils.getObjectFromField(activity, declaredField);
                    FieldUtils.setFieldToObject(activity, declaredField, context, HookFactory.createContextImplProxy(context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
