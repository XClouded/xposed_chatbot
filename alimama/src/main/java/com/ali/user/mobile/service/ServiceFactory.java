package com.ali.user.mobile.service;

import com.ali.user.mobile.log.TLogAdapter;
import java.lang.reflect.InvocationTargetException;

public class ServiceFactory {
    private static final String TAG = "BeanLoader";

    private ServiceFactory() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r2 = r1.getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x002f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean registerService(java.lang.Class<?> r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.ali.user.mobile.service.ServiceContainer r1 = com.ali.user.mobile.service.ServiceContainer.getInstance()     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            java.lang.Object r1 = r1.getService(r4)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            if (r1 == 0) goto L_0x0010
            r4 = 1
            return r4
        L_0x0010:
            java.lang.String r1 = r4.getName()     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            java.lang.String r1 = getBeanClassName(r1)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            if (r2 == 0) goto L_0x001f
            return r0
        L_0x001f:
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            java.lang.String r2 = "INSTANCE"
            java.lang.reflect.Field r2 = r1.getField(r2)     // Catch:{ NoSuchFieldException -> 0x002f }
            r3 = 0
            java.lang.Object r2 = r2.get(r3)     // Catch:{ NoSuchFieldException -> 0x002f }
            goto L_0x003b
        L_0x002f:
            java.lang.Class[] r2 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            java.lang.reflect.Constructor r1 = r1.getConstructor(r2)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            java.lang.Object r2 = r1.newInstance(r2)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
        L_0x003b:
            if (r4 == 0) goto L_0x0048
            if (r2 == 0) goto L_0x0048
            com.ali.user.mobile.service.ServiceContainer r1 = com.ali.user.mobile.service.ServiceContainer.getInstance()     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            boolean r4 = r1.registerService(r4, r2)     // Catch:{ ClassNotFoundException -> 0x0060, InvocationTargetException -> 0x0055, Exception -> 0x004a }
            goto L_0x0049
        L_0x0048:
            r4 = 0
        L_0x0049:
            return r4
        L_0x004a:
            r4 = move-exception
            java.lang.String r1 = "BeanLoader"
            java.lang.String r4 = r4.getMessage()
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r1, (java.lang.String) r4)
            return r0
        L_0x0055:
            r4 = move-exception
            java.lang.String r1 = "BeanLoader"
            java.lang.String r4 = r4.getMessage()
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r1, (java.lang.String) r4)
            return r0
        L_0x0060:
            r4 = move-exception
            java.lang.String r1 = "BeanLoader"
            java.lang.String r4 = r4.getMessage()
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r1, (java.lang.String) r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.service.ServiceFactory.registerService(java.lang.Class):boolean");
    }

    public static <T> T getService(Class<T> cls) {
        registerService(cls);
        return ServiceContainer.getInstance().getService(cls);
    }

    private static String getBeanClassName(String str) {
        try {
            Class<?> cls = Class.forName("com.ali.user.mobile.config.BeanConfig");
            Object newInstance = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            return (String) cls.getMethod("getBeanClassName", new Class[]{String.class}).invoke(newInstance, new Object[]{str});
        } catch (ClassNotFoundException e) {
            TLogAdapter.d(TAG, e.getMessage());
            return "";
        } catch (InvocationTargetException e2) {
            TLogAdapter.d(TAG, e2.getMessage());
            return "";
        } catch (Exception e3) {
            TLogAdapter.d(TAG, e3.getMessage());
            return "";
        }
    }
}
