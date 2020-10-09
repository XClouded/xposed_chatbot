package com.taobao.onlinemonitor;

import java.lang.reflect.Field;

public class DynamicActivityManagerNative extends BaseDynamicProxy {
    public DynamicActivityManagerNative(OnLineMonitor onLineMonitor) {
        super(onLineMonitor);
    }

    public void doProxy() {
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel < 26) {
            try {
                Class<?> cls = Class.forName("android.app.ActivityManagerNative");
                Field declaredField = cls.getDeclaredField("gDefault");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls.getClass());
                Field declaredField2 = Class.forName("android.util.Singleton").getDeclaredField("mInstance");
                declaredField2.setAccessible(true);
                declaredField2.set(obj, newProxyInstance(declaredField2.get(obj)));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } else {
            try {
                Class<?> cls2 = Class.forName("android.app.ActivityManager");
                Field declaredField3 = cls2.getDeclaredField("IActivityManagerSingleton");
                declaredField3.setAccessible(true);
                Object obj2 = declaredField3.get(cls2.getClass());
                Field declaredField4 = Class.forName("android.util.Singleton").getDeclaredField("mInstance");
                declaredField4.setAccessible(true);
                declaredField4.set(obj2, newProxyInstance(declaredField4.get(obj2)));
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x009d, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x009f, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00b6, code lost:
        if (r4.startsWith("broadcastIntent") != false) goto L_0x00ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e7, code lost:
        if (r4.equals("isTopOfTask") != false) goto L_0x00e9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00f3, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00f8, code lost:
        throw r8.getTargetException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return java.lang.Boolean.FALSE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
        return 0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00f3 A[ExcHandler: InvocationTargetException (r8v2 'e' java.lang.reflect.InvocationTargetException A[CUSTOM_DECLARE]), Splitter:B:1:0x0013] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object invoke(java.lang.Object r8, java.lang.reflect.Method r9, java.lang.Object[] r10) throws java.lang.Throwable {
        /*
            r7 = this;
            long r0 = java.lang.System.nanoTime()
            r2 = 1000000(0xf4240, double:4.940656E-318)
            long r0 = r0 / r2
            android.os.Debug.threadCpuTimeNanos()
            java.lang.String r8 = ""
            com.taobao.onlinemonitor.OnLineMonitor r0 = r7.mOnLineMonitor
            com.taobao.onlinemonitor.TraceDetail r0 = r0.mTraceDetail
            r0 = 0
            r1 = 0
            java.lang.String r4 = r9.getName()     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x00a1 }
            java.lang.Object r8 = r7.mTargetObject     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009f }
            java.lang.Object r8 = r9.invoke(r8, r10)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009f }
            java.lang.String r9 = "startActivity"
            boolean r9 = r4.startsWith(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x0044
            com.taobao.onlinemonitor.OnLineMonitor r9 = r7.mOnLineMonitor     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            com.taobao.onlinemonitor.LoadTimeCalculate r9 = r9.mLoadTimeCalculate     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x0036
            com.taobao.onlinemonitor.OnLineMonitor r9 = r7.mOnLineMonitor     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            com.taobao.onlinemonitor.LoadTimeCalculate r9 = r9.mLoadTimeCalculate     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            long r5 = java.lang.System.nanoTime()     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            long r5 = r5 / r2
            r9.mStartActivityTime = r5     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
        L_0x0036:
            com.taobao.onlinemonitor.OnLineMonitor r9 = r7.mOnLineMonitor     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            com.taobao.onlinemonitor.SmoothCalculate r9 = r9.mSmoothCalculate     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x0052
            com.taobao.onlinemonitor.OnLineMonitor r9 = r7.mOnLineMonitor     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            com.taobao.onlinemonitor.SmoothCalculate r9 = r9.mSmoothCalculate     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            r10 = 1
            r9.mStartActivityOnTouch = r10     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            goto L_0x0052
        L_0x0044:
            java.lang.String r9 = "registerReceiver"
            boolean r9 = r4.equals(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 != 0) goto L_0x0052
            java.lang.String r9 = "unregisterReceiver"
            boolean r9 = r4.equals(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
        L_0x0052:
            long r9 = java.lang.System.nanoTime()     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            long r9 = r9 / r2
            android.os.Debug.threadCpuTimeNanos()     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r8 != 0) goto L_0x00f1
            java.lang.String r9 = "broadcastIntent"
            boolean r9 = r4.startsWith(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 != 0) goto L_0x0098
            java.lang.String r9 = "startActivity"
            boolean r9 = r4.startsWith(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x0074
            java.lang.String r9 = "startActivityAndWait"
            boolean r9 = r4.startsWith(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x0098
        L_0x0074:
            java.lang.String r9 = "bindService"
            boolean r9 = r4.equals(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 != 0) goto L_0x0098
            java.lang.String r9 = "stopService"
            boolean r9 = r4.equals(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 != 0) goto L_0x0098
            java.lang.String r9 = "getActivityDisplayId"
            boolean r9 = r4.startsWith(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x008d
            goto L_0x0098
        L_0x008d:
            java.lang.String r9 = "isTopOfTask"
            boolean r9 = r4.equals(r9)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            if (r9 == 0) goto L_0x00f1
            java.lang.Boolean r9 = java.lang.Boolean.FALSE     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            goto L_0x00f2
        L_0x0098:
            java.lang.Integer r9 = java.lang.Integer.valueOf(r0)     // Catch:{ InvocationTargetException -> 0x00f3, Exception -> 0x009d }
            goto L_0x00f2
        L_0x009d:
            r9 = move-exception
            goto L_0x00a4
        L_0x009f:
            r9 = move-exception
            goto L_0x00a3
        L_0x00a1:
            r9 = move-exception
            r4 = r8
        L_0x00a3:
            r8 = r1
        L_0x00a4:
            java.lang.String r10 = "OnLineMonitor"
            java.lang.String r1 = "onlinemonitor dynamicHandler exception catched!"
            android.util.Log.e(r10, r1)
            r9.printStackTrace()
            if (r8 != 0) goto L_0x00f1
            java.lang.String r9 = "broadcastIntent"
            boolean r9 = r4.startsWith(r9)
            if (r9 != 0) goto L_0x00ec
            java.lang.String r9 = "startActivity"
            boolean r9 = r4.startsWith(r9)
            if (r9 == 0) goto L_0x00c8
            java.lang.String r9 = "startActivityAndWait"
            boolean r9 = r4.startsWith(r9)
            if (r9 == 0) goto L_0x00ec
        L_0x00c8:
            java.lang.String r9 = "bindService"
            boolean r9 = r4.equals(r9)
            if (r9 != 0) goto L_0x00ec
            java.lang.String r9 = "stopService"
            boolean r9 = r4.equals(r9)
            if (r9 != 0) goto L_0x00ec
            java.lang.String r9 = "getActivityDisplayId"
            boolean r9 = r4.startsWith(r9)
            if (r9 == 0) goto L_0x00e1
            goto L_0x00ec
        L_0x00e1:
            java.lang.String r9 = "isTopOfTask"
            boolean r9 = r4.equals(r9)
            if (r9 == 0) goto L_0x00f1
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            goto L_0x00f2
        L_0x00ec:
            java.lang.Integer r9 = java.lang.Integer.valueOf(r0)
            goto L_0x00f2
        L_0x00f1:
            r9 = r8
        L_0x00f2:
            return r9
        L_0x00f3:
            r8 = move-exception
            java.lang.Throwable r8 = r8.getTargetException()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.DynamicActivityManagerNative.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }
}
