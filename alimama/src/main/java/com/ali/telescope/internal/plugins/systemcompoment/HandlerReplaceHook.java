package com.ali.telescope.internal.plugins.systemcompoment;

import android.app.Application;
import com.ali.telescope.internal.plugins.SoLoader;
import com.ali.telescope.util.AccessibleEnlarger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HandlerReplaceHook implements IHandlerHook {
    public boolean hook(Application application, LifecycleCallStateDispatchListener lifecycleCallStateDispatchListener) {
        try {
            SoLoader.loadHookSo();
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Constructor<?> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            AccessibleEnlarger.enlargeClass(cls);
            AccessibleEnlarger.enlargeMethod(declaredConstructor);
            Object newInstance = Class.forName("com.ali.telescope.internal.plugins.systemcompoment.FakeActivityThread").newInstance();
            Class<?> cls2 = Class.forName("android.app.ActivityThread$H");
            Constructor[] declaredConstructors = cls2.getDeclaredConstructors();
            AccessibleEnlarger.enlargeClass(cls2);
            for (Constructor enlargeMethod : declaredConstructors) {
                AccessibleEnlarger.enlargeMethod(enlargeMethod);
            }
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke((Object) null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mH");
            declaredField.setAccessible(true);
            AccessibleEnlarger.enlargeField(declaredField);
            declaredField.set(invoke, Class.forName("com.ali.telescope.internal.plugins.systemcompoment.FakeActivityThread$FakeActivityThread$H").getDeclaredConstructors()[0].newInstance(new Object[]{newInstance, declaredField.get(invoke), application, invoke, lifecycleCallStateDispatchListener}));
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}
