package com.uc.webview.export.internal.uc;

import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;

/* compiled from: U4Source */
public class CoreClassPreLoader {
    @Reflection
    protected static LazyClass Lazy;
    @Reflection
    protected static Runnable sLazyUpdateCallback;

    /* compiled from: U4Source */
    public static class LazyClass {
        final ReflectionUtil.BindingMethod<Boolean> a;
        ReflectionUtil.BindingMethod<Boolean> b = null;
        @Reflection
        public final Class<?> sCoreClassLoaderImpl;

        private static Class<?> a(ClassLoader classLoader) {
            try {
                return Class.forName(br.CORE_CLASS_LOADER_IMPL_CLASS, true, classLoader);
            } catch (ClassNotFoundException e) {
                throw new UCSetupException(4028, (Throwable) e);
            }
        }

        public LazyClass(ClassLoader classLoader) {
            this.sCoreClassLoaderImpl = a(classLoader);
            this.a = new ReflectionUtil.BindingMethod<>(this.sCoreClassLoaderImpl, "loadCoreClass", new Class[]{ClassLoader.class});
            try {
                this.b = new ReflectionUtil.BindingMethod<>(this.sCoreClassLoaderImpl, "loadCoreClassLevel", new Class[]{ClassLoader.class, Integer.class});
            } catch (Throwable unused) {
            }
        }
    }

    @Reflection
    public static synchronized void updateLazy(ClassLoader classLoader) {
        synchronized (CoreClassPreLoader.class) {
            if (Lazy == null) {
                Lazy = new LazyClass(classLoader);
                if (sLazyUpdateCallback != null) {
                    sLazyUpdateCallback.run();
                }
            }
        }
    }

    @Reflection
    public static boolean loadCoreClass(ClassLoader classLoader) {
        return Lazy.a.invoke(new Object[]{classLoader}).booleanValue();
    }

    public static boolean a(ClassLoader classLoader) {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            Class.forName(br.CORE_FACTORY_IMPL_CLASS, true, classLoader);
            Class.forName(br.CORE_CLASS_LOADER_IMPL_CLASS, true, classLoader);
            z = true;
        } catch (Exception unused) {
            z = false;
        }
        Log.i("CoreClassPreLoader", "loadCoreClassUrgent result:" + z + ", cost:" + (System.currentTimeMillis() - currentTimeMillis));
        updateLazy(classLoader);
        if (Lazy.b == null) {
            return z;
        }
        return z & Lazy.b.invoke(new Object[]{classLoader, 3}).booleanValue();
    }
}
