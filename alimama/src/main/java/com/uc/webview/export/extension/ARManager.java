package com.uc.webview.export.extension;

import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.extension.IARDetector;
import com.uc.webview.export.internal.uc.CoreFactory;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import java.lang.reflect.Method;

@Api
/* compiled from: U4Source */
public abstract class ARManager implements IARDetector.ResultListener {
    private static ARManager a;
    private static Method b;

    public static ARManager getInstance() {
        if (a == null) {
            try {
                ARManager g = CoreFactory.g();
                a = g;
                b = ReflectionUtil.getMethod(g.getClass(), "invoke");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return a;
    }

    public void registerARDetector(String str, String str2) {
        if (a != null) {
            a.registerARDetector(str, str2);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerARDetector(java.lang.String r5, java.lang.String r6, java.util.HashMap<java.lang.String, java.lang.String> r7) {
        /*
            r4 = this;
            com.uc.webview.export.extension.ARManager r0 = a
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.reflect.Method r0 = b
            if (r0 == 0) goto L_0x0036
            r0 = 3
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1 = 0
            r0[r1] = r5
            r5 = 1
            r0[r5] = r6
            r6 = 2
            r0[r6] = r7
            java.lang.reflect.Method r7 = b     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            com.uc.webview.export.extension.ARManager r2 = a     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            java.lang.Object[] r3 = new java.lang.Object[r6]     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r3[r1] = r6     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r3[r5] = r0     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r7.invoke(r2, r3)     // Catch:{ RuntimeException -> 0x0031, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            return
        L_0x0027:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0036
        L_0x002c:
            r5 = move-exception
            r5.printStackTrace()
            return
        L_0x0031:
            r5 = move-exception
            r5.printStackTrace()
            return
        L_0x0036:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.extension.ARManager.registerARDetector(java.lang.String, java.lang.String, java.util.HashMap):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerARDetector(java.lang.Object r7) {
        /*
            r6 = this;
            com.uc.webview.export.extension.ARManager r0 = a
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.reflect.Method r0 = b
            if (r0 == 0) goto L_0x0032
            r0 = 1
            java.lang.Object[] r1 = new java.lang.Object[r0]
            r2 = 0
            r1[r2] = r7
            java.lang.reflect.Method r7 = b     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            com.uc.webview.export.extension.ARManager r3 = a     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            r5 = 3
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            r4[r2] = r5     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            r4[r0] = r1     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            r7.invoke(r3, r4)     // Catch:{ RuntimeException -> 0x002d, IllegalAccessException -> 0x0028, InvocationTargetException -> 0x0023 }
            return
        L_0x0023:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0032
        L_0x0028:
            r7 = move-exception
            r7.printStackTrace()
            return
        L_0x002d:
            r7 = move-exception
            r7.printStackTrace()
            return
        L_0x0032:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.extension.ARManager.registerARDetector(java.lang.Object):void");
    }
}
