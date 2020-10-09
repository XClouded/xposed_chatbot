package com.taobao.android.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import com.taobao.android.modular.MLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

class LocalAidlServices {
    private static final Method Service_attach;
    private static final String TAG = "LocalSvc";
    private static final HashMap<ServiceConnection, String> mConnectionMap = new HashMap<>();
    private static final Map<String, ServiceRecord> mServices = new HashMap();

    LocalAidlServices() {
    }

    static boolean isServiceLoaded(String str) {
        ServiceRecord serviceRecord = mServices.get(str);
        return (serviceRecord == null || serviceRecord.service == null) ? false : true;
    }

    static boolean bindService(Context context, Intent intent, ServiceConnection serviceConnection) throws ClassNotFoundException {
        ComponentName component = intent.getComponent();
        if (component == null) {
            ResolveInfo resolveService = context.getPackageManager().resolveService(intent, 0);
            if (resolveService == null) {
                return false;
            }
            component = new ComponentName(resolveService.serviceInfo.packageName, resolveService.serviceInfo.name.intern());
        }
        ServiceRecord serviceRecord = mServices.get(component.getClassName());
        if (serviceRecord == null) {
            serviceRecord = createAndBindService(context, intent, component);
            if (serviceRecord == null) {
                return false;
            }
            mServices.put(component.getClassName(), serviceRecord);
        }
        serviceRecord.add(serviceConnection);
        mConnectionMap.put(serviceConnection, component.getClassName());
        try {
            long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
            serviceConnection.onServiceConnected(component, serviceRecord.binder);
            logExcessiveElapse(threadCpuTimeNanos + 2000000, serviceConnection, ".onServiceConnected()");
            return true;
        } catch (RuntimeException e) {
            MLog.w(TAG, serviceConnection + ".onServiceConnected()", e);
            return true;
        }
    }

    static boolean checkConnectionExist(ServiceConnection serviceConnection) {
        return mConnectionMap.get(serviceConnection) != null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a0 A[SYNTHETIC, Splitter:B:25:0x00a0] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00c7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.taobao.android.service.LocalAidlServices.ServiceRecord createAndBindService(android.content.Context r9, android.content.Intent r10, android.content.ComponentName r11) throws java.lang.ClassNotFoundException {
        /*
            java.lang.String r0 = r11.getClassName()
            java.lang.Class r0 = loadServiceClass(r9, r0)
            r1 = 0
            if (r0 != 0) goto L_0x000c
            return r1
        L_0x000c:
            java.lang.Class<com.taobao.android.service.AidlService> r2 = com.taobao.android.service.AidlService.class
            boolean r2 = r2.isAssignableFrom(r0)
            if (r2 != 0) goto L_0x0015
            return r1
        L_0x0015:
            long r2 = android.os.Debug.threadCpuTimeNanos()     // Catch:{ InstantiationException -> 0x00f1, IllegalAccessException -> 0x00d0 }
            java.lang.Object r4 = r0.newInstance()     // Catch:{ InstantiationException -> 0x00f1, IllegalAccessException -> 0x00d0 }
            android.app.Service r4 = (android.app.Service) r4     // Catch:{ InstantiationException -> 0x00f1, IllegalAccessException -> 0x00d0 }
            r5 = 2000000(0x1e8480, double:9.881313E-318)
            long r2 = r2 + r5
            java.lang.String r7 = r0.getName()     // Catch:{ InstantiationException -> 0x00f1, IllegalAccessException -> 0x00d0 }
            java.lang.String r8 = "()"
            logExcessiveElapse(r2, r7, r8)     // Catch:{ InstantiationException -> 0x00f1, IllegalAccessException -> 0x00d0 }
            android.app.Application r11 = getApplication(r9)
            attach(r9, r0, r4, r11)
            r2 = 5000000(0x4c4b40, double:2.470328E-317)
            long r7 = android.os.Debug.threadCpuTimeNanos()     // Catch:{ RuntimeException -> 0x0045 }
            r4.onCreate()     // Catch:{ RuntimeException -> 0x0045 }
            r9 = 0
            long r7 = r7 + r2
            java.lang.String r9 = ".onCreate()"
            logExcessiveElapse(r7, r4, r9)     // Catch:{ RuntimeException -> 0x0045 }
            goto L_0x005c
        L_0x0045:
            r9 = move-exception
            java.lang.String r0 = "LocalSvc"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r4)
            java.lang.String r8 = ".onCreate()"
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            com.taobao.android.modular.MLog.w(r0, r7, r9)
        L_0x005c:
            long r7 = android.os.Debug.threadCpuTimeNanos()     // Catch:{ RuntimeException -> 0x0086 }
            android.os.IBinder r9 = r4.onBind(r10)     // Catch:{ RuntimeException -> 0x0086 }
            r0 = 0
            long r7 = r7 + r5
            java.lang.String r0 = ".onBind()"
            logExcessiveElapse(r7, r4, r0)     // Catch:{ RuntimeException -> 0x0084 }
            if (r9 != 0) goto L_0x009e
            java.lang.String r0 = "LocalSvc"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ RuntimeException -> 0x0084 }
            r5.<init>()     // Catch:{ RuntimeException -> 0x0084 }
            r5.append(r4)     // Catch:{ RuntimeException -> 0x0084 }
            java.lang.String r6 = ".onBind() should never return null."
            r5.append(r6)     // Catch:{ RuntimeException -> 0x0084 }
            java.lang.String r5 = r5.toString()     // Catch:{ RuntimeException -> 0x0084 }
            android.util.Log.e(r0, r5)     // Catch:{ RuntimeException -> 0x0084 }
            goto L_0x009e
        L_0x0084:
            r0 = move-exception
            goto L_0x0088
        L_0x0086:
            r0 = move-exception
            r9 = r1
        L_0x0088:
            java.lang.String r5 = "LocalSvc"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r4)
            java.lang.String r7 = ".onBind()"
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            com.taobao.android.modular.MLog.w(r5, r6, r0)
        L_0x009e:
            if (r9 != 0) goto L_0x00c7
            long r9 = android.os.Debug.threadCpuTimeNanos()     // Catch:{ RuntimeException -> 0x00af }
            r4.onDestroy()     // Catch:{ RuntimeException -> 0x00af }
            r11 = 0
            long r9 = r9 + r2
            java.lang.String r11 = ".onDestroy()"
            logExcessiveElapse(r9, r4, r11)     // Catch:{ RuntimeException -> 0x00af }
            goto L_0x00c6
        L_0x00af:
            r9 = move-exception
            java.lang.String r10 = "LocalSvc"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r4)
            java.lang.String r0 = ".onDestroy()"
            r11.append(r0)
            java.lang.String r11 = r11.toString()
            com.taobao.android.modular.MLog.w(r10, r11, r9)
        L_0x00c6:
            return r1
        L_0x00c7:
            registerComponentCallbacks(r11, r4)
            com.taobao.android.service.LocalAidlServices$ServiceRecord r0 = new com.taobao.android.service.LocalAidlServices$ServiceRecord
            r0.<init>(r11, r10, r9, r4)
            return r0
        L_0x00d0:
            r9 = move-exception
            java.lang.String r10 = "LocalSvc"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "Constructor of "
            r0.append(r2)
            java.lang.String r11 = r11.getClassName()
            r0.append(r11)
            java.lang.String r11 = " is inaccessible"
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            com.taobao.android.modular.MLog.w(r10, r11, r9)
            return r1
        L_0x00f1:
            r9 = move-exception
            java.lang.String r10 = "LocalSvc"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "Failed to instantiate "
            r0.append(r2)
            java.lang.String r11 = r11.getClassName()
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            com.taobao.android.modular.MLog.w(r10, r11, r9)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.service.LocalAidlServices.createAndBindService(android.content.Context, android.content.Intent, android.content.ComponentName):com.taobao.android.service.LocalAidlServices$ServiceRecord");
    }

    private static void logExcessiveElapse(long j, Object obj, String str) {
        long threadCpuTimeNanos = Debug.threadCpuTimeNanos() - j;
        if (threadCpuTimeNanos > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(obj.toString());
            if (str == null) {
                str = "";
            }
            sb.append(str);
            sb.append(" exceed the deadline ");
            sb.append(threadCpuTimeNanos / 1000000);
            sb.append("ms (thread CPU time)");
            Log.w(TAG, sb.toString());
        }
    }

    public static boolean unbindService(Context context, ServiceConnection serviceConnection) {
        ServiceRecord serviceRecord;
        String remove = mConnectionMap.remove(serviceConnection);
        if (remove == null || (serviceRecord = mServices.get(remove)) == null) {
            return false;
        }
        if (!serviceRecord.remove(serviceConnection)) {
            MLog.e(TAG, "Internal inconsistency, please report this to the corresponding dev team: " + serviceConnection + " @ " + remove);
        }
        try {
            serviceConnection.onServiceDisconnected(new ComponentName(context.getPackageName(), serviceRecord.service.getClass().getName()));
        } catch (RuntimeException e) {
            MLog.w(TAG, serviceConnection + ".onServiceDisconnected()", e);
        }
        if (!serviceRecord.isEmpty()) {
            return true;
        }
        mServices.remove(remove);
        unregisterComponentCallbacks(serviceRecord.app, serviceRecord.service);
        try {
            serviceRecord.service.onUnbind(serviceRecord.intent);
        } catch (RuntimeException e2) {
            MLog.w(TAG, serviceRecord.service + ".onUnbind()", e2);
        }
        try {
            serviceRecord.service.onDestroy();
            return true;
        } catch (RuntimeException e3) {
            MLog.w(TAG, serviceRecord.service + ".onDestroy()", e3);
            return true;
        }
    }

    @TargetApi(14)
    private static void registerComponentCallbacks(Application application, ComponentCallbacks componentCallbacks) {
        if (Build.VERSION.SDK_INT >= 14) {
            application.registerComponentCallbacks(componentCallbacks);
        }
    }

    @TargetApi(14)
    private static void unregisterComponentCallbacks(Application application, ComponentCallbacks componentCallbacks) {
        if (application != null && Build.VERSION.SDK_INT >= 14) {
            application.unregisterComponentCallbacks(componentCallbacks);
        }
    }

    static Class<? extends Service> loadServiceClass(Context context, String str) throws ClassNotFoundException {
        try {
            if (Services.getSystemClassloader() != null) {
                return Services.getSystemClassloader().loadClass(str);
            }
            return context.getClassLoader().loadClass(str);
        } catch (ClassCastException unused) {
            MLog.e(TAG, "Not a Service derived class: " + str);
            return null;
        }
    }

    private static void attach(Context context, Class<? extends Service> cls, Service service, Application application) {
        if (Service_attach != null) {
            try {
                Service_attach.invoke(service, new Object[]{context, null, cls.getName(), null, application, null});
            } catch (IllegalAccessException e) {
                MLog.e(TAG, "Unexpected exception when attaching service.", e);
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getTargetException());
            }
        }
    }

    private static Application getApplication(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).getApplication();
        }
        if (context instanceof Service) {
            return ((Service) context).getApplication();
        }
        Context applicationContext = context.getApplicationContext();
        if (applicationContext instanceof Application) {
            return (Application) applicationContext;
        }
        MLog.w(TAG, "Cannot discover application from context " + context);
        return null;
    }

    static {
        Method method = null;
        try {
            Method declaredMethod = Service.class.getDeclaredMethod("attach", new Class[]{Context.class, Class.forName("android.app.ActivityThread"), String.class, IBinder.class, Application.class, Object.class});
            try {
                declaredMethod.setAccessible(true);
                method = declaredMethod;
            } catch (ClassNotFoundException e) {
                Method method2 = declaredMethod;
                e = e;
                method = method2;
                Log.e(TAG, "Incompatible ROM", e);
                Service_attach = method;
            } catch (NoSuchMethodException e2) {
                Method method3 = declaredMethod;
                e = e2;
                method = method3;
                Log.e(TAG, "Incompatible ROM", e);
                Service_attach = method;
            }
        } catch (ClassNotFoundException e3) {
            e = e3;
            Log.e(TAG, "Incompatible ROM", e);
            Service_attach = method;
        } catch (NoSuchMethodException e4) {
            e = e4;
            Log.e(TAG, "Incompatible ROM", e);
            Service_attach = method;
        }
        Service_attach = method;
    }

    private static class ServiceRecord extends CopyOnWriteArrayList<ServiceConnection> {
        private static final long serialVersionUID = 1;
        final Application app;
        final IBinder binder;
        final Intent intent;
        final Service service;

        ServiceRecord(Application application, Intent intent2, IBinder iBinder, Service service2) {
            this.app = application;
            this.intent = intent2;
            this.binder = iBinder;
            this.service = service2;
        }
    }
}
