package com.taobao.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class AidlService<I extends IInterface, Stub extends Binder & IInterface> extends Service {
    private static final String TAG = "AidlService";
    private Binder mBinder;

    public void onCreate() {
        super.onCreate();
        Type[] actualTypeArguments = getActualTypeArguments(getClass());
        if (!(actualTypeArguments[0] instanceof Class)) {
            throw new IllegalArgumentException(actualTypeArguments[0] + " is not an AIDL interface");
        } else if (actualTypeArguments[1] instanceof Class) {
            Class cls = (Class) actualTypeArguments[0];
            Class cls2 = (Class) actualTypeArguments[1];
            if (cls.isAssignableFrom(cls2)) {
                this.mBinder = createBinder(cls2);
                return;
            }
            throw new IllegalArgumentException(cls2 + " is not paired with " + cls);
        } else {
            throw new IllegalArgumentException(actualTypeArguments[1] + " is not an AIDL Stub class");
        }
    }

    public void onDestroy() {
        this.mBinder = null;
        super.onDestroy();
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        Log.w(TAG, "Start operation is not allowed for AIDL service.");
        stopSelf(i2);
        return 2;
    }

    public final IBinder onBind(Intent intent) {
        if (this.mBinder != null) {
            return this.mBinder;
        }
        throw new IllegalStateException("AidlService is not initialized. Did you forget to call super.onCreate() in onCreate() method of AidlService derived class?");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:11|12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004e, code lost:
        throw new java.lang.IllegalArgumentException(r5 + " must be either standalone class or inner class of " + getClass() + ", and have a empty constructor.");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.os.Binder createBinder(java.lang.Class<Stub> r5) {
        /*
            r4 = this;
            r0 = 0
            java.lang.Class[] r1 = new java.lang.Class[r0]     // Catch:{ NoSuchMethodException -> 0x0014 }
            java.lang.reflect.Constructor r1 = r5.getDeclaredConstructor(r1)     // Catch:{ NoSuchMethodException -> 0x0014 }
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ NoSuchMethodException -> 0x0014 }
            java.lang.Object r1 = r1.newInstance(r2)     // Catch:{ NoSuchMethodException -> 0x0014 }
            android.os.Binder r1 = (android.os.Binder) r1     // Catch:{ NoSuchMethodException -> 0x0014 }
            return r1
        L_0x0010:
            r0 = move-exception
            goto L_0x004f
        L_0x0012:
            r5 = move-exception
            goto L_0x008e
        L_0x0014:
            r1 = 1
            java.lang.Class[] r2 = new java.lang.Class[r1]     // Catch:{ NoSuchMethodException -> 0x002c }
            java.lang.Class r3 = r4.getClass()     // Catch:{ NoSuchMethodException -> 0x002c }
            r2[r0] = r3     // Catch:{ NoSuchMethodException -> 0x002c }
            java.lang.reflect.Constructor r2 = r5.getDeclaredConstructor(r2)     // Catch:{ NoSuchMethodException -> 0x002c }
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ NoSuchMethodException -> 0x002c }
            r1[r0] = r4     // Catch:{ NoSuchMethodException -> 0x002c }
            java.lang.Object r0 = r2.newInstance(r1)     // Catch:{ NoSuchMethodException -> 0x002c }
            android.os.Binder r0 = (android.os.Binder) r0     // Catch:{ NoSuchMethodException -> 0x002c }
            return r0
        L_0x002c:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            r1.<init>()     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            r1.append(r5)     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            java.lang.String r2 = " must be either standalone class or inner class of "
            r1.append(r2)     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            java.lang.Class r2 = r4.getClass()     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            r1.append(r2)     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            java.lang.String r2 = ", and have a empty constructor."
            r1.append(r2)     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            java.lang.String r1 = r1.toString()     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            r0.<init>(r1)     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
            throw r0     // Catch:{ InvocationTargetException -> 0x0012, IllegalAccessException -> 0x0077, Exception -> 0x0010 }
        L_0x004f:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Stub "
            r2.append(r3)
            r2.append(r5)
            java.lang.String r5 = " of service "
            r2.append(r5)
            java.lang.Class r5 = r4.getClass()
            r2.append(r5)
            java.lang.String r5 = " cannot be instantiated."
            r2.append(r5)
            java.lang.String r5 = r2.toString()
            r1.<init>(r5, r0)
            throw r1
        L_0x0077:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r5)
            java.lang.String r5 = " and its empty constructor must be both public."
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r0.<init>(r5)
            throw r0
        L_0x008e:
            java.lang.Throwable r5 = r5.getTargetException()
            boolean r0 = r5 instanceof java.lang.RuntimeException
            if (r0 == 0) goto L_0x0099
            java.lang.RuntimeException r5 = (java.lang.RuntimeException) r5
            goto L_0x009f
        L_0x0099:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r0.<init>(r5)
            r5 = r0
        L_0x009f:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.service.AidlService.createBinder(java.lang.Class):android.os.Binder");
    }

    private static Type[] getActualTypeArguments(Class<?> cls) {
        Class<? super Object> cls2;
        while (cls2 != null) {
            Type genericSuperclass = cls2.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                if (AidlService.class.equals(parameterizedType.getRawType())) {
                    return parameterizedType.getActualTypeArguments();
                }
            }
            Class<? super Object> superclass = cls2.getSuperclass();
            cls2 = cls;
            cls2 = superclass;
        }
        throw new IllegalArgumentException();
    }

    public String toString() {
        if (this.mBinder == null) {
            return "AidlService[null]";
        }
        return "AidlService[" + this.mBinder.getInterfaceDescriptor() + Operators.ARRAY_END_STR;
    }
}
