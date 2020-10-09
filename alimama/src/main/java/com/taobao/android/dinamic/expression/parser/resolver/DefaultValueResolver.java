package com.taobao.android.dinamic.expression.parser.resolver;

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

    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x009e */
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
            if (r1 == 0) goto L_0x0034
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x002b }
            java.lang.Object r7 = r1.invoke(r7, r8)     // Catch:{ Exception -> 0x002b }
            return r7
        L_0x002b:
            r7 = move-exception
            java.lang.String r8 = "DinamicLog"
            java.lang.String[] r9 = new java.lang.String[r3]
            com.taobao.android.dinamic.log.DinamicLog.w(r8, r7, r9)
            return r2
        L_0x0034:
            android.util.LruCache<java.lang.String, java.lang.reflect.Field> r1 = fieldCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.reflect.Field r1 = (java.lang.reflect.Field) r1
            r4 = 1
            if (r1 == 0) goto L_0x0053
            java.lang.Object r7 = r1.get(r7)     // Catch:{ Exception -> 0x0044 }
            return r7
        L_0x0044:
            r7 = move-exception
            java.lang.String r8 = "DinamicLog"
            java.lang.String[] r9 = new java.lang.String[r4]
            java.lang.String r7 = r7.getMessage()
            r9[r3] = r7
            com.taobao.android.dinamic.log.DinamicLog.w(r8, r9)
            return r2
        L_0x0053:
            android.util.LruCache<java.lang.String, java.lang.String> r1 = notExistMethodCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.String r5 = ""
            if (r1 == r5) goto L_0x00e2
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            r1.<init>()     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.String r5 = "get"
            r1.append(r5)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            char r5 = r9.charAt(r3)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            char r5 = java.lang.Character.toUpperCase(r5)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            r1.append(r5)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.String r5 = r9.substring(r4)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            r1.append(r5)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.String r1 = r1.toString()     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.Class[] r5 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.reflect.Method r1 = r8.getMethod(r1, r5)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            android.util.LruCache<java.lang.String, java.lang.reflect.Method> r5 = methodCache     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            r5.put(r0, r1)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            java.lang.Object r1 = r1.invoke(r7, r5)     // Catch:{ NoSuchMethodException -> 0x009e, Exception -> 0x008f }
            return r1
        L_0x008f:
            r7 = move-exception
            java.lang.String r8 = "DinamicLog"
            java.lang.String[] r9 = new java.lang.String[r4]
            java.lang.String r7 = r7.getMessage()
            r9[r3] = r7
            com.taobao.android.dinamic.log.DinamicLog.w(r8, r9)
            return r2
        L_0x009e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            r1.<init>()     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.String r5 = "is"
            r1.append(r5)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            char r5 = r9.charAt(r3)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            char r5 = java.lang.Character.toUpperCase(r5)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            r1.append(r5)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.String r5 = r9.substring(r4)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            r1.append(r5)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.String r1 = r1.toString()     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.Class[] r5 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.reflect.Method r1 = r8.getMethod(r1, r5)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            android.util.LruCache<java.lang.String, java.lang.reflect.Method> r5 = methodCache     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            r5.put(r0, r1)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            java.lang.Object r1 = r1.invoke(r7, r5)     // Catch:{ NoSuchMethodException -> 0x00db, Exception -> 0x00d0 }
            return r1
        L_0x00d0:
            r7 = move-exception
            java.lang.String r7 = r7.getMessage()
            java.lang.String[] r8 = new java.lang.String[r3]
            com.taobao.android.dinamic.log.DinamicLog.w(r7, r8)
            return r2
        L_0x00db:
            android.util.LruCache<java.lang.String, java.lang.String> r1 = notExistMethodCache
            java.lang.String r5 = ""
            r1.put(r0, r5)
        L_0x00e2:
            android.util.LruCache<java.lang.String, java.lang.String> r1 = notExistFieldCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.String r5 = ""
            if (r1 == r5) goto L_0x0110
            java.lang.reflect.Field r8 = r8.getField(r9)     // Catch:{ NoSuchFieldException -> 0x0109, Exception -> 0x00fa }
            android.util.LruCache<java.lang.String, java.lang.reflect.Field> r9 = fieldCache     // Catch:{ NoSuchFieldException -> 0x0109, Exception -> 0x00fa }
            r9.put(r0, r8)     // Catch:{ NoSuchFieldException -> 0x0109, Exception -> 0x00fa }
            java.lang.Object r7 = r8.get(r7)     // Catch:{ NoSuchFieldException -> 0x0109, Exception -> 0x00fa }
            return r7
        L_0x00fa:
            r7 = move-exception
            java.lang.String r8 = "DinamicLog"
            java.lang.String[] r9 = new java.lang.String[r4]
            java.lang.String r7 = r7.getMessage()
            r9[r3] = r7
            com.taobao.android.dinamic.log.DinamicLog.w(r8, r9)
            return r2
        L_0x0109:
            android.util.LruCache<java.lang.String, java.lang.String> r7 = notExistFieldCache
            java.lang.String r8 = ""
            r7.put(r0, r8)
        L_0x0110:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.expression.parser.resolver.DefaultValueResolver.resolve(java.lang.Object, java.lang.Class, java.lang.String):java.lang.Object");
    }
}
