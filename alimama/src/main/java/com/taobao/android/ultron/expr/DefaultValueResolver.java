package com.taobao.android.ultron.expr;

import android.util.LruCache;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class DefaultValueResolver implements ValueResolver {
    private static final String MARK = "";
    private static final LruCache<String, Field> fieldCache = new LruCache<>(32);
    private static final LruCache<String, Method> methodCache = new LruCache<>(64);
    private static final LruCache<String, String> notExistFieldCache = new LruCache<>(16);
    private static final LruCache<String, String> notExistMethodCache = new LruCache<>(16);

    public boolean canResolve(Object obj, Class<?> cls, String str) {
        return true;
    }

    DefaultValueResolver() {
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0098 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object resolve(java.lang.Object r7, java.lang.Class<?> r8, java.lang.String r9) {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r8.getName()
            r0.append(r1)
            java.lang.String r1 = "[]"
            r0.append(r1)
            r0.append(r9)
            java.lang.String r0 = r0.toString()
            android.util.LruCache<java.lang.String, java.lang.reflect.Method> r1 = methodCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.reflect.Method r1 = (java.lang.reflect.Method) r1
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L_0x0036
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x002b }
            java.lang.Object r7 = r1.invoke(r7, r8)     // Catch:{ Exception -> 0x002b }
            return r7
        L_0x002b:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String[] r8 = new java.lang.String[r3]
            com.taobao.android.ultron.common.utils.UnifyLog.w(r7, r8)
            return r2
        L_0x0036:
            android.util.LruCache<java.lang.String, java.lang.reflect.Field> r1 = fieldCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.reflect.Field r1 = (java.lang.reflect.Field) r1
            if (r1 == 0) goto L_0x0050
            java.lang.Object r7 = r1.get(r7)     // Catch:{ Exception -> 0x0045 }
            return r7
        L_0x0045:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String[] r8 = new java.lang.String[r3]
            com.taobao.android.ultron.common.utils.UnifyLog.w(r7, r8)
            return r2
        L_0x0050:
            android.util.LruCache<java.lang.String, java.lang.String> r1 = notExistMethodCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.String r4 = ""
            if (r1 == r4) goto L_0x00dc
            r1 = 1
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            r4.<init>()     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.String r5 = "get"
            r4.append(r5)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            char r5 = r9.charAt(r3)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            char r5 = java.lang.Character.toUpperCase(r5)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            r4.append(r5)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.String r5 = r9.substring(r1)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            r4.append(r5)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.String r4 = r4.toString()     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.Class[] r5 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.reflect.Method r4 = r8.getMethod(r4, r5)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            android.util.LruCache<java.lang.String, java.lang.reflect.Method> r5 = methodCache     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            r5.put(r0, r4)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            java.lang.Object r4 = r4.invoke(r7, r5)     // Catch:{ NoSuchMethodException -> 0x0098, Exception -> 0x008d }
            return r4
        L_0x008d:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String[] r8 = new java.lang.String[r3]
            com.taobao.android.ultron.common.utils.UnifyLog.w(r7, r8)
            return r2
        L_0x0098:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            r4.<init>()     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.String r5 = "is"
            r4.append(r5)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            char r5 = r9.charAt(r3)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            char r5 = java.lang.Character.toUpperCase(r5)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            r4.append(r5)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.String r1 = r9.substring(r1)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            r4.append(r1)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.String r1 = r4.toString()     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.reflect.Method r1 = r8.getMethod(r1, r4)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            android.util.LruCache<java.lang.String, java.lang.reflect.Method> r4 = methodCache     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            r4.put(r0, r1)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            java.lang.Object r1 = r1.invoke(r7, r4)     // Catch:{ NoSuchMethodException -> 0x00d5, Exception -> 0x00ca }
            return r1
        L_0x00ca:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String[] r8 = new java.lang.String[r3]
            com.taobao.android.ultron.common.utils.UnifyLog.w(r7, r8)
            return r2
        L_0x00d5:
            android.util.LruCache<java.lang.String, java.lang.String> r1 = notExistMethodCache
            java.lang.String r4 = ""
            r1.put(r0, r4)
        L_0x00dc:
            android.util.LruCache<java.lang.String, java.lang.String> r1 = notExistFieldCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.String r4 = ""
            if (r1 == r4) goto L_0x0106
            java.lang.reflect.Field r8 = r8.getField(r9)     // Catch:{ NoSuchFieldException -> 0x00ff, Exception -> 0x00f4 }
            android.util.LruCache<java.lang.String, java.lang.reflect.Field> r9 = fieldCache     // Catch:{ NoSuchFieldException -> 0x00ff, Exception -> 0x00f4 }
            r9.put(r0, r8)     // Catch:{ NoSuchFieldException -> 0x00ff, Exception -> 0x00f4 }
            java.lang.Object r7 = r8.get(r7)     // Catch:{ NoSuchFieldException -> 0x00ff, Exception -> 0x00f4 }
            return r7
        L_0x00f4:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String[] r8 = new java.lang.String[r3]
            com.taobao.android.ultron.common.utils.UnifyLog.w(r7, r8)
            return r2
        L_0x00ff:
            android.util.LruCache<java.lang.String, java.lang.String> r7 = notExistFieldCache
            java.lang.String r8 = ""
            r7.put(r0, r8)
        L_0x0106:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.expr.DefaultValueResolver.resolve(java.lang.Object, java.lang.Class, java.lang.String):java.lang.Object");
    }
}
