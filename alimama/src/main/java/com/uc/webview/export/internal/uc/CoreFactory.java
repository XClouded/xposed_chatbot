package com.uc.webview.export.internal.uc;

import android.content.Context;
import android.util.AttributeSet;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.extension.ARManager;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.ICookieManager;
import com.uc.webview.export.internal.interfaces.IGeolocationPermissions;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IMimeTypeMap;
import com.uc.webview.export.internal.interfaces.IServiceWorkerController;
import com.uc.webview.export.internal.interfaces.IWebStorage;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import java.util.HashMap;

/* compiled from: U4Source */
public class CoreFactory {
    protected static a a;
    protected static Runnable b;

    /* compiled from: U4Source */
    public static class a {
        public final Class<?> a = a();
        final ReflectionUtil.BindingMethod<IGlobalSettings> b = new ReflectionUtil.BindingMethod<>(this.a, "getGlobalSettings");
        final ReflectionUtil.BindingMethod<ICookieManager> c = new ReflectionUtil.BindingMethod<>(this.a, "getCookieManager");
        final ReflectionUtil.BindingMethod<IServiceWorkerController> d = new ReflectionUtil.BindingMethod<>(this.a, "getServiceWorkerController");
        final ReflectionUtil.BindingMethod<UCMobileWebKit> e = new ReflectionUtil.BindingMethod<>(this.a, "getUCMobileWebKit");
        final ReflectionUtil.BindingMethod<IGeolocationPermissions> f = new ReflectionUtil.BindingMethod<>(this.a, "getGeolocationPermissions");
        final ReflectionUtil.BindingMethod<IWebStorage> g = new ReflectionUtil.BindingMethod<>(this.a, "getWebStorage");
        final ReflectionUtil.BindingMethod<IMimeTypeMap> h = new ReflectionUtil.BindingMethod<>(this.a, "getMimeTypeMap");
        final ReflectionUtil.BindingMethod<IWebView> i = new ReflectionUtil.BindingMethod<>(this.a, "createWebView", new Class[]{Context.class});
        final ReflectionUtil.BindingMethod<IWebView> j;
        final ReflectionUtil.BindingMethod<UCMobileWebKit> k;
        final ReflectionUtil.BindingMethod<Boolean> l;
        final ReflectionUtil.BindingMethod<Integer> m;
        final ReflectionUtil.BindingMethod<Object> n;
        final ReflectionUtil.BindingMethod<Object> o;
        final ReflectionUtil.BindingMethod<WebResourceResponse> p;
        final ReflectionUtil.BindingMethod<ARManager> q;

        private static Class<?> a() {
            try {
                return Class.forName(br.CORE_FACTORY_IMPL_CLASS, true, SDKFactory.c);
            } catch (ClassNotFoundException e2) {
                throw new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_REMOVE_PARENT_MODEL_NOT_EXIST, (Throwable) e2);
            }
        }

        public a() {
            ReflectionUtil.BindingMethod<IWebView> bindingMethod;
            ReflectionUtil.BindingMethod<ARManager> bindingMethod2 = null;
            try {
                bindingMethod = new ReflectionUtil.BindingMethod<>(this.a, "createWebView", new Class[]{Context.class, AttributeSet.class});
            } catch (Throwable unused) {
                bindingMethod = null;
            }
            this.j = bindingMethod;
            this.k = new ReflectionUtil.BindingMethod<>(this.a, "initUCMobileWebKit", new Class[]{Context.class, Boolean.TYPE, Boolean.TYPE});
            this.m = new ReflectionUtil.BindingMethod<>(this.a, "getCoreType");
            this.n = new ReflectionUtil.BindingMethod<>(this.a, "initSDK", new Class[]{Context.class});
            this.o = new ReflectionUtil.BindingMethod<>(this.a, "handlePerformanceTests", new Class[]{String.class});
            this.p = new ReflectionUtil.BindingMethod<>(this.a, "getResponseByUrl", new Class[]{String.class});
            try {
                bindingMethod2 = new ReflectionUtil.BindingMethod<>(this.a, "getARManager");
            } catch (Throwable unused2) {
            }
            this.q = bindingMethod2;
            this.l = new ReflectionUtil.BindingMethod<>(this.a, "initUCMobileWebkitCoreSoEnv", new Class[]{Context.class, HashMap.class});
        }
    }

    public static void a() {
        h();
    }

    private static synchronized a h() {
        a aVar;
        synchronized (CoreFactory.class) {
            if (a == null) {
                b.a(145);
                a = new a();
                if (b != null) {
                    b.run();
                }
                b.a(146);
            }
            aVar = a;
        }
        return aVar;
    }

    public static IGlobalSettings b() {
        return h().b.getInstance();
    }

    @Reflection
    public static ICookieManager getCookieManager() {
        return h().c.getInstance();
    }

    @Reflection
    public static IServiceWorkerController getServiceWorkerController() {
        return h().d.getInstance();
    }

    @Reflection
    public static UCMobileWebKit getUCMobileWebKit() {
        return h().e.getInstance();
    }

    public static IGeolocationPermissions c() {
        return h().f.getInstance();
    }

    public static IWebStorage d() {
        return h().g.getInstance();
    }

    public static IMimeTypeMap e() {
        return h().h.getInstance();
    }

    @Reflection
    public static IWebView createWebView(Context context, AttributeSet attributeSet) {
        if (h().j == null) {
            return h().i.invoke(new Object[]{context});
        }
        return h().j.invoke(new Object[]{context, attributeSet});
    }

    public static boolean f() {
        return h().j != null;
    }

    @Reflection
    public static UCMobileWebKit initUCMobileWebKit(Context context, boolean z, boolean z2) {
        return h().k.invoke(new Object[]{context, Boolean.valueOf(z), Boolean.valueOf(z2)});
    }

    @Reflection
    public static boolean initUCMobileWebkitCoreSoEnv(Context context, HashMap<String, String> hashMap) throws RuntimeException {
        return h().l.invoke(new Object[]{context, hashMap}).booleanValue();
    }

    @Reflection
    public static Integer getCoreType() {
        return h().m.invoke();
    }

    public static void a(Context context) {
        h().n.invoke(new Object[]{context});
    }

    public static void a(String str) {
        h().o.invoke(new Object[]{str});
    }

    public static WebResourceResponse b(String str) {
        return h().p.invoke(new Object[]{str});
    }

    public static ARManager g() {
        return h().q.invoke();
    }
}
