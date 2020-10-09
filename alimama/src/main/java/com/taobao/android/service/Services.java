package com.taobao.android.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.taobao.android.annotation.NonMainThread;
import com.taobao.android.exception.BindException;
import com.taobao.android.internal.RuntimeGlobals;
import com.taobao.android.modular.IAidlServiceBridge;
import com.taobao.android.modular.MLog;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class Services {
    private static final ComponentName KNullCompName = new ComponentName("", "");
    private static final long KServiceConnectionTimeout = 10000;
    private static final String TAG = "Services";
    private static final Map<String, ComponentName> mResolvedServiceCache = new ConcurrentHashMap();
    private static final Map<String, ComponentName> mResolvedServiceSkipCache = new ConcurrentHashMap();
    private static boolean mSetClassLoader = false;
    private static ClassLoader mSysClassLoader;
    private static final Set<ServiceConnection> sAsyncBindingConnections = Collections.synchronizedSet(new HashSet());
    /* access modifiers changed from: private */
    public static final Set<String> sClassLoadedSet = new HashSet();
    private static final Map<Activity, List<IBinder>> sManagedBridgeBinder = new HashMap();
    private static final Map<Activity, List<ServiceConnection>> sManagedServiceConnections = new HashMap();
    private static final Set<ServiceConnection> sPendingUnBindConnections = Collections.synchronizedSet(new HashSet());
    private static volatile boolean sRecyclerSetup;
    private static final Object sRecyclerSetupLock = new Object();

    public interface IBindAsyncCallback {
        void onBindResult(BindException bindException);
    }

    public static class InvocationOnMainThreadException extends RuntimeException {
        private static final long serialVersionUID = -2830620447552102268L;
    }

    @NonMainThread
    public static <T extends IInterface> T get(Activity activity, Class<T> cls) {
        return get((Context) activity, cls);
    }

    @NonMainThread
    @Deprecated
    public static <T extends IInterface> T get(Context context, Class<T> cls) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (cls != null) {
            if (cls == IAidlServiceBridge.class) {
                IBinder peekMe = AidlBridgeService.peekMe(context);
                if (peekMe != null) {
                    return IAidlServiceBridge.Stub.asInterface(peekMe);
                }
                MLog.w(TAG, "AIDL Service Bridge is not ready", new Throwable());
            }
            Intent buildServiceIntent = buildServiceIntent(context, cls, false);
            if (buildServiceIntent == null) {
                MLog.w(TAG, "No matched service for " + cls.getName());
                return null;
            }
            ensureRecyclerSetup(context);
            ManagedServiceConnection managedServiceConnection = new ManagedServiceConnection();
            if (buildServiceIntent.getComponent().getClassName().equals(AidlBridgeService.class.getName())) {
                IBinder peekMe2 = AidlBridgeService.peekMe(context);
                if (peekMe2 == null) {
                    Log.i(TAG, "Find AidlBridgeService null in other process");
                    AidlBridgeService.init(context, managedServiceConnection);
                    return null;
                }
                try {
                    IBinder bindService = IAidlServiceBridge.Stub.asInterface(peekMe2).bindService(buildServiceIntent(context, cls, true));
                    Activity findActivity = findActivity(context);
                    if (findActivity != null) {
                        List list = sManagedBridgeBinder.get(findActivity);
                        if (list == null) {
                            list = new ArrayList();
                            sManagedBridgeBinder.put(findActivity, list);
                        }
                        list.add(bindService);
                    }
                    return asInterface(bindService, cls);
                } catch (Exception e) {
                    MLog.w(TAG, "Failed to binder to real interface in bridge mode with name is " + cls.getName());
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    if (!LocalAidlServices.bindService(context, buildServiceIntent, managedServiceConnection)) {
                        if (RuntimeGlobals.isMainThread()) {
                            throw new InvocationOnMainThreadException();
                        } else if (!context.bindService(buildServiceIntent, managedServiceConnection, 1)) {
                            MLog.w(TAG, "Failed to bind service: " + cls.getName());
                            try {
                                context.unbindService(managedServiceConnection);
                            } catch (RuntimeException e2) {
                                Log.d(TAG, "Unnecessary unbinding due to " + e2);
                            }
                            return null;
                        }
                    }
                    Activity findActivity2 = findActivity(context);
                    if (findActivity2 != null) {
                        List list2 = sManagedServiceConnections.get(findActivity2);
                        if (list2 == null) {
                            list2 = new ArrayList();
                            sManagedServiceConnections.put(findActivity2, list2);
                        }
                        list2.add(managedServiceConnection);
                    }
                    try {
                        try {
                            return asInterface(managedServiceConnection.waitUntilConnected(KServiceConnectionTimeout), cls);
                        } catch (ClassNotFoundException e3) {
                            MLog.w(TAG, "Failed to cast binder to interface, ClassNotFoundException: " + cls.getName());
                            throw new RuntimeException(e3);
                        } catch (IllegalAccessException e4) {
                            MLog.w(TAG, "Failed to cast binder to interface, IllegalAccessException: " + cls.getName());
                            throw new RuntimeException(e4);
                        } catch (NoSuchMethodException e5) {
                            MLog.w(TAG, "Failed to cast binder to interface, NoSuchMethodException: " + cls.getName());
                            throw new RuntimeException(e5);
                        } catch (InvocationTargetException e6) {
                            Throwable targetException = e6.getTargetException();
                            if (targetException instanceof RuntimeException) {
                                throw ((RuntimeException) targetException);
                            }
                            throw new RuntimeException(e6);
                        }
                    } catch (TimeoutException unused) {
                        MLog.w(TAG, "Service connection timeout: " + cls.getName());
                        return null;
                    } catch (InterruptedException unused2) {
                        MLog.w(TAG, "Service connection interrupted.");
                        return null;
                    }
                } catch (ClassNotFoundException unused3) {
                    return null;
                }
            }
        } else {
            throw new IllegalArgumentException("Service interface is null");
        }
    }

    @Deprecated
    public static <T extends IInterface> void unget(Context context, T t) {
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.noteSlowCall("deprecation");
        }
    }

    public static <T extends IInterface> boolean bind(Context context, Class<T> cls, ServiceConnection serviceConnection) {
        Intent buildServiceIntent = buildServiceIntent(context, cls, false);
        if (buildServiceIntent == null) {
            MLog.w(TAG, "No matched service for " + cls.getName());
            return false;
        } else if (!LocalAidlServices.checkConnectionExist(serviceConnection)) {
            try {
                if (LocalAidlServices.bindService(context, buildServiceIntent, serviceConnection) || context.bindService(buildServiceIntent, serviceConnection, 1)) {
                    return true;
                }
                return false;
            } catch (ClassNotFoundException unused) {
                return false;
            }
        } else {
            throw new RuntimeException("Call bind() with same ServiceConnection instance");
        }
    }

    public static <T extends IInterface> void bindAsync(Context context, Class<T> cls, ServiceConnection serviceConnection, IBindAsyncCallback iBindAsyncCallback) {
        ComponentName component;
        boolean z = false;
        Intent buildServiceIntent = buildServiceIntent(context, cls, false);
        if (buildServiceIntent == null) {
            MLog.e(TAG, "No matched service for " + cls.getName());
            processBindResultCallback(context, iBindAsyncCallback, new BindException(-4, "unfind service " + cls.getName()), serviceConnection, false);
        } else if (LocalAidlServices.checkConnectionExist(serviceConnection)) {
            MLog.e(TAG, "Call bind() with same ServiceConnection instance " + cls.getName());
            processBindResultCallback(context, iBindAsyncCallback, new BindException(-1, "Call bind() with same ServiceConnection instance"), serviceConnection, true);
        } else if (!sAsyncBindingConnections.add(serviceConnection)) {
            MLog.e(TAG, "ServiceConnection is connecting " + cls.getName());
            processBindResultCallback(context, iBindAsyncCallback, new BindException(-3, "ServiceConnection is connecting"), serviceConnection, false);
        } else {
            if (!sClassLoadedSet.contains(cls.getName()) && Looper.myLooper() == Looper.getMainLooper() && (component = buildServiceIntent.getComponent()) != null && !TextUtils.isEmpty(component.getClassName()) && !LocalAidlServices.isServiceLoaded(component.getClassName())) {
                final String className = component.getClassName();
                final Context context2 = context;
                final Class<T> cls2 = cls;
                final IBindAsyncCallback iBindAsyncCallback2 = iBindAsyncCallback;
                final ServiceConnection serviceConnection2 = serviceConnection;
                final Intent intent = buildServiceIntent;
                new Thread("load class") {
                    public void run() {
                        final BindException bindException;
                        super.run();
                        try {
                            if (LocalAidlServices.loadServiceClass(context2, className) != null) {
                                Services.sClassLoadedSet.add(cls2.getName());
                            }
                            bindException = null;
                        } catch (ClassNotFoundException e) {
                            MLog.w(Services.TAG, "bindAsync ClassNotFoundException in child thread " + cls2.getName());
                            bindException = new BindException(-2, "class not found", e);
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                if (bindException != null) {
                                    Services.processBindResultCallback(context2, iBindAsyncCallback2, bindException, serviceConnection2, true);
                                } else {
                                    Services.bindAsyncSerivceWithResult(context2, intent, serviceConnection2, iBindAsyncCallback2);
                                }
                            }
                        });
                    }
                }.start();
                z = true;
            }
            if (!z) {
                bindAsyncSerivceWithResult(context, buildServiceIntent, serviceConnection, iBindAsyncCallback);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0014 A[Catch:{ ClassNotFoundException -> 0x002e, Throwable -> 0x0024, all -> 0x0022 }] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x001d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void bindAsyncSerivceWithResult(android.content.Context r5, android.content.Intent r6, android.content.ServiceConnection r7, com.taobao.android.service.Services.IBindAsyncCallback r8) {
        /*
            r0 = 1
            r1 = 0
            boolean r2 = com.taobao.android.service.LocalAidlServices.bindService(r5, r6, r7)     // Catch:{ ClassNotFoundException -> 0x002e, Throwable -> 0x0024 }
            if (r2 != 0) goto L_0x0011
            boolean r6 = r5.bindService(r6, r7, r0)     // Catch:{ ClassNotFoundException -> 0x002e, Throwable -> 0x0024 }
            if (r6 == 0) goto L_0x000f
            goto L_0x0011
        L_0x000f:
            r6 = 0
            goto L_0x0012
        L_0x0011:
            r6 = 1
        L_0x0012:
            if (r6 != 0) goto L_0x001d
            com.taobao.android.exception.BindException r6 = new com.taobao.android.exception.BindException     // Catch:{ ClassNotFoundException -> 0x002e, Throwable -> 0x0024 }
            r2 = -6
            java.lang.String r3 = "bind service return false"
            r6.<init>(r2, r3)     // Catch:{ ClassNotFoundException -> 0x002e, Throwable -> 0x0024 }
            goto L_0x001e
        L_0x001d:
            r6 = r1
        L_0x001e:
            processBindResultCallback(r5, r8, r6, r7, r0)
            goto L_0x003a
        L_0x0022:
            r6 = move-exception
            goto L_0x003b
        L_0x0024:
            r6 = move-exception
            com.taobao.android.exception.BindException r2 = new com.taobao.android.exception.BindException     // Catch:{ all -> 0x0022 }
            r3 = -5
            java.lang.String r4 = "bind service error"
            r2.<init>(r3, r4, r6)     // Catch:{ all -> 0x0022 }
            goto L_0x0037
        L_0x002e:
            r6 = move-exception
            com.taobao.android.exception.BindException r2 = new com.taobao.android.exception.BindException     // Catch:{ all -> 0x0022 }
            r3 = -2
            java.lang.String r4 = "class not found"
            r2.<init>(r3, r4, r6)     // Catch:{ all -> 0x0022 }
        L_0x0037:
            processBindResultCallback(r5, r8, r2, r7, r0)
        L_0x003a:
            return
        L_0x003b:
            processBindResultCallback(r5, r8, r1, r7, r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.service.Services.bindAsyncSerivceWithResult(android.content.Context, android.content.Intent, android.content.ServiceConnection, com.taobao.android.service.Services$IBindAsyncCallback):void");
    }

    static void processBindResultCallback(Context context, IBindAsyncCallback iBindAsyncCallback, BindException bindException, ServiceConnection serviceConnection, boolean z) {
        if (z) {
            sAsyncBindingConnections.remove(serviceConnection);
        }
        if (iBindAsyncCallback != null) {
            iBindAsyncCallback.onBindResult(bindException);
        } else if (bindException != null) {
            MLog.e(TAG, "bind exception", bindException);
        }
        if (z && sPendingUnBindConnections.remove(serviceConnection) && bindException == null) {
            unbind(context, serviceConnection);
        }
    }

    public static void unBindAsync(Context context, ServiceConnection serviceConnection) {
        if (sAsyncBindingConnections.contains(serviceConnection)) {
            sPendingUnBindConnections.add(serviceConnection);
            return;
        }
        sPendingUnBindConnections.remove(serviceConnection);
        unbind(context, serviceConnection);
    }

    public static void unbind(Context context, ServiceConnection serviceConnection) {
        if (context != null && !LocalAidlServices.unbindService(context, serviceConnection)) {
            try {
                context.unbindService(serviceConnection);
            } catch (IllegalArgumentException unused) {
                Log.d(TAG, "Already unbound: " + serviceConnection.toString());
            }
        }
    }

    @TargetApi(14)
    private static void ensureRecyclerSetup(Context context) {
        if (Build.VERSION.SDK_INT > 14 && !sRecyclerSetup) {
            synchronized (sRecyclerSetupLock) {
                if (!sRecyclerSetup) {
                    findApplication(context).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                        public void onActivityCreated(Activity activity, Bundle bundle) {
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

                        public void onActivityDestroyed(Activity activity) {
                            Services.cleanupOnActivityDestroyed(activity);
                            Services.cleanupBridgeBinderOnActivityDestroyed(activity);
                        }
                    });
                    sRecyclerSetup = true;
                }
            }
        }
    }

    private static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    private static Application findApplication(Context context) {
        Context applicationContext = context.getApplicationContext();
        if (applicationContext instanceof Application) {
            return (Application) applicationContext;
        }
        if (context instanceof Activity) {
            return ((Activity) context).getApplication();
        }
        if (context instanceof Service) {
            return ((Service) context).getApplication();
        }
        if (context instanceof ContextWrapper) {
            return findApplication(((ContextWrapper) context).getBaseContext());
        }
        throw new IllegalArgumentException("Failed to find application from context: " + context);
    }

    private static Intent buildServiceIntent(Context context, Class<?> cls, boolean z) {
        String intern = cls.getName().intern();
        Intent intent = new Intent(intern);
        if (context == null) {
            MLog.w(TAG, "Context shouldn't null");
            return null;
        }
        intent.setPackage(context.getPackageName());
        ComponentName componentName = (z ? mResolvedServiceSkipCache : mResolvedServiceCache).get(intern);
        if (componentName == KNullCompName) {
            return null;
        }
        if (componentName == null) {
            ComponentName resolveServiceIntent = resolveServiceIntent(context, intent, false, z);
            if (resolveServiceIntent == null) {
                resolveServiceIntent = KNullCompName;
            }
            componentName = resolveServiceIntent;
            if (z) {
                mResolvedServiceSkipCache.put(intern, componentName);
            } else {
                mResolvedServiceCache.put(intern, componentName);
            }
        }
        intent.setComponent(componentName);
        return intent;
    }

    private static ComponentName resolveServiceIntent(final Context context, final Intent intent, boolean z, boolean z2) {
        List<ResolveInfo> queryIntentServices;
        if (context == null || (queryIntentServices = context.getPackageManager().queryIntentServices(intent, 0)) == null || queryIntentServices.isEmpty()) {
            return null;
        }
        int size = queryIntentServices.size();
        if (size >= 2) {
            ServiceInfo serviceInfo = queryIntentServices.get(0).serviceInfo;
            if (AidlBridgeService.class.getName().equals(serviceInfo.name)) {
                size--;
                if (equal(serviceInfo.processName, procname(context)) || z2) {
                    queryIntentServices.remove(0);
                }
            }
        }
        if (size > 1) {
            for (ResolveInfo resolveInfo : queryIntentServices) {
                ServiceInfo serviceInfo2 = resolveInfo.serviceInfo;
                if (context.getPackageName().equals(serviceInfo2.packageName) && !z) {
                    Log.w(TAG, "Find one more, use >> " + serviceInfo2.packageName + "/" + serviceInfo2.name);
                    return new ComponentName(serviceInfo2.packageName, serviceInfo2.name.intern());
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Resolve more than one service for ");
            sb.append(intent.getAction());
            if (z2) {
                sb.append(" -s ");
            }
            if (z) {
                sb.append(" -d ");
            }
            sb.append(" [p:");
            sb.append(context.getPackageName());
            sb.append("] ");
            for (ResolveInfo next : queryIntentServices) {
                if (next.serviceInfo != null) {
                    sb.append(">> ");
                    sb.append(next.serviceInfo.packageName);
                    sb.append("/");
                    sb.append(next.serviceInfo.name);
                }
            }
            throw new IllegalStateException(sb.toString());
        } else if (!z) {
            ServiceInfo serviceInfo3 = queryIntentServices.get(0).serviceInfo;
            Log.w(TAG, ">> " + serviceInfo3.packageName + "/" + serviceInfo3.name);
            return new ComponentName(serviceInfo3.packageName, serviceInfo3.name.intern());
        } else {
            int i = context.getApplicationInfo().uid;
            for (ResolveInfo next2 : queryIntentServices) {
                if (size > 1) {
                    Log.w(TAG, ">> " + next2.serviceInfo.packageName + "/" + next2.serviceInfo.name);
                }
                if (next2.serviceInfo.applicationInfo.uid == i) {
                    return new ComponentName(next2.serviceInfo.packageName, next2.serviceInfo.name.intern());
                }
            }
            Activity findActivity = findActivity(context);
            if (findActivity != null) {
                findActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Context context = context;
                        Toast.makeText(context, "Using mismatched service " + intent.getAction() + "\nSee logcat for details (TAG:" + Services.TAG + Operators.BRACKET_END_STR, 1).show();
                    }
                });
            }
            Log.w(TAG, "Potential mismatched service for " + intent.getAction());
            for (ResolveInfo next3 : queryIntentServices) {
                Log.w(TAG, "  " + next3.serviceInfo.packageName + "/" + next3.serviceInfo.name);
            }
            return null;
        }
    }

    private static boolean equal(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equals(str2);
    }

    private static String procname(Context context) {
        int myPid = Process.myPid();
        try {
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (next.pid == myPid) {
                    return next.processName;
                }
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    static void cleanupOnActivityDestroyed(Activity activity) {
        List<ServiceConnection> remove = sManagedServiceConnections.remove(activity);
        if (remove != null && !remove.isEmpty()) {
            for (ServiceConnection serviceConnection : remove) {
                try {
                    unbind(activity, serviceConnection);
                } catch (RuntimeException e) {
                    MLog.w(TAG, "Failed to unbind service: " + serviceConnection, e);
                }
            }
        }
    }

    static void cleanupBridgeBinderOnActivityDestroyed(Activity activity) {
        List<IBinder> remove = sManagedBridgeBinder.remove(activity);
        if (remove != null && !remove.isEmpty()) {
            for (IBinder iBinder : remove) {
                IBinder peekMe = AidlBridgeService.peekMe(activity);
                if (peekMe != null) {
                    try {
                        Log.d(TAG, "cleanupBridgeBinderOnActivityDestroyed :" + peekMe.toString());
                        IAidlServiceBridge.Stub.asInterface(peekMe).unbindService(iBinder);
                    } catch (Exception e) {
                        MLog.w(TAG, "Failed to unbind bridge binder: " + peekMe, e);
                    }
                }
            }
        }
    }

    private static <T extends IInterface> T asInterface(IBinder iBinder, Class<T> cls) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (iBinder == null) {
            return null;
        }
        T queryLocalInterface = iBinder.queryLocalInterface(cls.getName());
        if (queryLocalInterface != null) {
            return queryLocalInterface;
        }
        return (IInterface) Class.forName(cls.getName() + "$Stub", false, cls.getClassLoader()).getMethod("asInterface", new Class[]{IBinder.class}).invoke((Object) null, new Object[]{iBinder});
    }

    public static void setSystemClassloader(ClassLoader classLoader) {
        if (!mSetClassLoader) {
            mSysClassLoader = classLoader;
            mSetClassLoader = true;
        }
    }

    public static ClassLoader getSystemClassloader() {
        return mSysClassLoader;
    }
}
