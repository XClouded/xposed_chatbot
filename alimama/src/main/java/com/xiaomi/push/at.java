package com.xiaomi.push;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class at {
    private static final Map<Class<?>, Class<?>> a = new HashMap();

    public static class a<T> {
        public final Class<? extends T> a;

        /* renamed from: a  reason: collision with other field name */
        public final T f125a;
    }

    static {
        a.put(Boolean.class, Boolean.TYPE);
        a.put(Byte.class, Byte.TYPE);
        a.put(Character.class, Character.TYPE);
        a.put(Short.class, Short.TYPE);
        a.put(Integer.class, Integer.TYPE);
        a.put(Float.class, Float.TYPE);
        a.put(Long.class, Long.TYPE);
        a.put(Double.class, Double.TYPE);
        a.put(Boolean.TYPE, Boolean.TYPE);
        a.put(Byte.TYPE, Byte.TYPE);
        a.put(Character.TYPE, Character.TYPE);
        a.put(Short.TYPE, Short.TYPE);
        a.put(Integer.TYPE, Integer.TYPE);
        a.put(Float.TYPE, Float.TYPE);
        a.put(Long.TYPE, Long.TYPE);
        a.put(Double.TYPE, Double.TYPE);
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0015 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T a(java.lang.Class<? extends java.lang.Object> r3, java.lang.Object r4, java.lang.String r5) {
        /*
            r0 = 0
        L_0x0001:
            r1 = 1
            if (r0 != 0) goto L_0x001b
            java.lang.reflect.Field r2 = r3.getDeclaredField(r5)     // Catch:{ NoSuchFieldException -> 0x000e }
            r2.setAccessible(r1)     // Catch:{ NoSuchFieldException -> 0x000d }
            r0 = r2
            goto L_0x0012
        L_0x000d:
            r0 = r2
        L_0x000e:
            java.lang.Class r3 = r3.getSuperclass()
        L_0x0012:
            if (r3 == 0) goto L_0x0015
            goto L_0x0001
        L_0x0015:
            java.lang.NoSuchFieldException r3 = new java.lang.NoSuchFieldException
            r3.<init>()
            throw r3
        L_0x001b:
            r0.setAccessible(r1)
            java.lang.Object r3 = r0.get(r4)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.at.a(java.lang.Class, java.lang.Object, java.lang.String):java.lang.Object");
    }

    public static <T> T a(Class<? extends Object> cls, String str) {
        try {
            return a(cls, (Object) null, str);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static <T> T a(Class<?> cls, String str, Object... objArr) {
        return a(cls, str, (Class<?>[]) a(objArr)).invoke((Object) null, a(objArr));
    }

    public static <T> T a(Object obj, String str) {
        try {
            return a((Class<? extends Object>) obj.getClass(), obj, str);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static <T> T a(Object obj, String str, Object... objArr) {
        try {
            return b(obj, str, objArr);
        } catch (Exception e) {
            Log.w("JavaCalls", "Meet exception when call Method '" + str + "' in " + obj, e);
            return null;
        }
    }

    public static <T> T a(String str, String str2) {
        try {
            return a((Class<? extends Object>) Class.forName(str), (Object) null, str2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static <T> T a(String str, String str2, Object... objArr) {
        try {
            return a(Class.forName(str), str2, objArr);
        } catch (Exception e) {
            Log.w("JavaCalls", "Meet exception when call Method '" + str2 + "' in " + str, e);
            return null;
        }
    }

    private static Method a(Class<?> cls, String str, Class<?>... clsArr) {
        Method a2 = a(cls.getDeclaredMethods(), str, clsArr);
        if (a2 != null) {
            a2.setAccessible(true);
            return a2;
        } else if (cls.getSuperclass() != null) {
            return a((Class<?>) cls.getSuperclass(), str, clsArr);
        } else {
            throw new NoSuchMethodException();
        }
    }

    private static Method a(Method[] methodArr, String str, Class<?>[] clsArr) {
        if (str != null) {
            for (Method method : methodArr) {
                if (method.getName().equals(str) && a((Class<?>[]) method.getParameterTypes(), clsArr)) {
                    return method;
                }
            }
            return null;
        }
        throw new NullPointerException("Method name must not be null.");
    }

    private static boolean a(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr == null) {
            return clsArr2 == null || clsArr2.length == 0;
        }
        if (clsArr2 == null) {
            return clsArr.length == 0;
        }
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        for (int i = 0; i < clsArr.length; i++) {
            if (clsArr2[i] != null && !clsArr[i].isAssignableFrom(clsArr2[i]) && (!a.containsKey(clsArr[i]) || !a.get(clsArr[i]).equals(a.get(clsArr2[i])))) {
                return false;
            }
        }
        return true;
    }

    private static Class<?>[] a(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return null;
        }
        Class[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            a aVar = objArr[i];
            if (aVar == null || !(aVar instanceof a)) {
                clsArr[i] = aVar == null ? null : aVar.getClass();
            } else {
                clsArr[i] = aVar.a;
            }
        }
        return clsArr;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static Object[] m93a(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return null;
        }
        Object[] objArr2 = new Object[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            a aVar = objArr[i];
            if (aVar == null || !(aVar instanceof a)) {
                objArr2[i] = aVar;
            } else {
                objArr2[i] = aVar.f125a;
            }
        }
        return objArr2;
    }

    public static <T> T b(Object obj, String str, Object... objArr) {
        return a(obj.getClass(), str, (Class<?>[]) a(objArr)).invoke(obj, a(objArr));
    }
}
