package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.k;
import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public final class g {
    private static final int a = -1;
    private static Boolean b;
    private static final Object c = new Object();

    public static final void a(br brVar, ConcurrentHashMap<String, Object> concurrentHashMap, boolean z, boolean z2, boolean z3) {
        if (z) {
            Boolean bool = null;
            if (!z3) {
                z3 = !z2;
                bool = Boolean.valueOf(k.a((Boolean) concurrentHashMap.get(UCCore.OPTION_SKIP_OLD_KERNEL)));
                if (bool != null) {
                    z3 = bool.booleanValue();
                }
            }
            if (z3) {
                UCLogger.print(a, "checkParamSkipOldKernel:true", new Throwable[0]);
                throw new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_REMOVE_MODEL_NOT_EXIST, String.format("UCM [%s] is excluded by param skip_old_extra_kernel value [%s].", new Object[]{brVar.dataDir, bool}));
            }
        }
    }

    public static final void a(String str, ClassLoader classLoader, String str2, String str3, String str4) {
        String str5;
        if (str != null) {
            try {
                if (str.length() > 0) {
                    String[] split = str.split(str4);
                    if (split.length > 0 && (str5 = (String) Class.forName(str2, false, classLoader).getField(str3).get((Object) null)) != null && str5.length() > 0) {
                        for (String trim : split) {
                            String trim2 = trim.trim();
                            if (trim2.length() > 0) {
                                if (str5.startsWith(trim2) || str5.matches(trim2)) {
                                    throw new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_APPEND_PARENT_MODEL_NOT_EXIST, String.format("UCM version [%s] is excluded by rules [%s].", new Object[]{str5, str}));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_APPEND_MODEL_NOT_EXIST, (Throwable) e);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void b(java.lang.String r3, java.lang.ClassLoader r4, java.lang.String r5, java.lang.String r6, java.lang.String r7) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            if (r3 == 0) goto L_0x004a
            int r2 = r3.length()     // Catch:{ Exception -> 0x0060 }
            if (r2 <= 0) goto L_0x004a
            java.lang.String[] r7 = r3.split(r7)     // Catch:{ Exception -> 0x0060 }
            int r2 = r7.length     // Catch:{ Exception -> 0x0060 }
            if (r2 <= 0) goto L_0x004a
            java.lang.Class r4 = java.lang.Class.forName(r5, r1, r4)     // Catch:{ Exception -> 0x0060 }
            java.lang.reflect.Field r4 = r4.getField(r6)     // Catch:{ Exception -> 0x0060 }
            r5 = 0
            java.lang.Object r4 = r4.get(r5)     // Catch:{ Exception -> 0x0060 }
            r0 = r4
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0060 }
            if (r0 == 0) goto L_0x004a
            int r4 = r0.length()     // Catch:{ Exception -> 0x0060 }
            if (r4 <= 0) goto L_0x004a
            int r4 = r7.length     // Catch:{ Exception -> 0x0060 }
            r5 = 0
        L_0x002c:
            if (r5 >= r4) goto L_0x004a
            r6 = r7[r5]     // Catch:{ Exception -> 0x0060 }
            java.lang.String r6 = r6.trim()     // Catch:{ Exception -> 0x0060 }
            int r2 = r6.length()     // Catch:{ Exception -> 0x0060 }
            if (r2 <= 0) goto L_0x0047
            boolean r2 = r0.equals(r6)     // Catch:{ Exception -> 0x0060 }
            if (r2 != 0) goto L_0x0046
            boolean r6 = r0.matches(r6)     // Catch:{ Exception -> 0x0060 }
            if (r6 == 0) goto L_0x0047
        L_0x0046:
            return
        L_0x0047:
            int r5 = r5 + 1
            goto L_0x002c
        L_0x004a:
            com.uc.webview.export.internal.setup.UCSetupException r4 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Exception -> 0x0060 }
            r5 = 4029(0xfbd, float:5.646E-42)
            java.lang.String r6 = "UCM version [%s] not included by rules [%s]."
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0060 }
            r7[r1] = r0     // Catch:{ Exception -> 0x0060 }
            r0 = 1
            r7[r0] = r3     // Catch:{ Exception -> 0x0060 }
            java.lang.String r3 = java.lang.String.format(r6, r7)     // Catch:{ Exception -> 0x0060 }
            r4.<init>((int) r5, (java.lang.String) r3)     // Catch:{ Exception -> 0x0060 }
            throw r4     // Catch:{ Exception -> 0x0060 }
        L_0x0060:
            r3 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r4 = new com.uc.webview.export.internal.setup.UCSetupException
            r5 = 4012(0xfac, float:5.622E-42)
            r4.<init>((int) r5, (java.lang.Throwable) r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.g.b(java.lang.String, java.lang.ClassLoader, java.lang.String, java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0091  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void a(com.uc.webview.export.internal.setup.br r7, android.content.Context r8, java.lang.ClassLoader r9, java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object> r10) {
        /*
            r0 = 30
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            java.lang.String r0 = "com.uc.webview.browser.shell.SdkAuthentication"
            java.lang.Class r9 = r9.loadClass(r0)     // Catch:{ ClassNotFoundException -> 0x00a4 }
            r0 = 3
            java.lang.Class[] r1 = new java.lang.Class[r0]
            java.lang.Class<android.content.Context> r2 = android.content.Context.class
            r3 = 0
            r1[r3] = r2
            java.lang.Class<com.uc.webview.export.internal.utility.UCMPackageInfo> r2 = com.uc.webview.export.internal.utility.UCMPackageInfo.class
            r4 = 1
            r1[r4] = r2
            java.lang.Class<java.util.HashMap> r2 = java.util.HashMap.class
            r5 = 2
            r1[r5] = r2
            java.lang.String r2 = "tryLoadUCCore"
            java.lang.reflect.Method r1 = r9.getMethod(r2, r1)     // Catch:{ NoSuchMethodException -> 0x0038 }
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ NoSuchMethodException -> 0x0038 }
            int r6 = r10.size()     // Catch:{ NoSuchMethodException -> 0x0038 }
            r2.<init>(r6)     // Catch:{ NoSuchMethodException -> 0x0038 }
            r2.putAll(r10)     // Catch:{ NoSuchMethodException -> 0x0038 }
            java.lang.Object[] r10 = new java.lang.Object[r0]     // Catch:{ NoSuchMethodException -> 0x0038 }
            r10[r3] = r8     // Catch:{ NoSuchMethodException -> 0x0038 }
            r10[r4] = r7     // Catch:{ NoSuchMethodException -> 0x0038 }
            r10[r5] = r2     // Catch:{ NoSuchMethodException -> 0x0038 }
            goto L_0x004e
        L_0x0038:
            java.lang.Class[] r10 = new java.lang.Class[r5]
            java.lang.Class<android.content.Context> r0 = android.content.Context.class
            r10[r3] = r0
            java.lang.Class<com.uc.webview.export.internal.utility.UCMPackageInfo> r0 = com.uc.webview.export.internal.utility.UCMPackageInfo.class
            r10[r4] = r0
            java.lang.String r0 = "tryLoadUCCore"
            java.lang.reflect.Method r1 = r9.getMethod(r0, r10)     // Catch:{ NoSuchMethodException -> 0x009b }
            java.lang.Object[] r10 = new java.lang.Object[r5]     // Catch:{ NoSuchMethodException -> 0x009b }
            r10[r3] = r8     // Catch:{ NoSuchMethodException -> 0x009b }
            r10[r4] = r7     // Catch:{ NoSuchMethodException -> 0x009b }
        L_0x004e:
            r7 = 0
            java.lang.Object r7 = com.uc.webview.export.internal.utility.ReflectionUtil.invoke((java.lang.Object) r7, (java.lang.Class<?>) r9, (java.lang.reflect.Method) r1, (java.lang.Object[]) r10)     // Catch:{ UCKnownException -> 0x0099, Throwable -> 0x006c }
            java.lang.Boolean r7 = (java.lang.Boolean) r7     // Catch:{ UCKnownException -> 0x0099, Throwable -> 0x006c }
            boolean r7 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r7)     // Catch:{ UCKnownException -> 0x0099, Throwable -> 0x006c }
            r7 = r7 ^ r4
            if (r7 == 0) goto L_0x0062
            r7 = 214(0xd6, float:3.0E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r7)
            return
        L_0x0062:
            com.uc.webview.export.internal.setup.UCSetupException r7 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCKnownException -> 0x0099, Throwable -> 0x006c }
            r8 = 4017(0xfb1, float:5.629E-42)
            java.lang.String r9 = "tryLoadUCCore return false."
            r7.<init>((int) r8, (java.lang.String) r9)     // Catch:{ UCKnownException -> 0x0099, Throwable -> 0x006c }
            throw r7     // Catch:{ UCKnownException -> 0x0099, Throwable -> 0x006c }
        L_0x006c:
            r7 = move-exception
            java.lang.String r8 = r7.getMessage()
            if (r8 == 0) goto L_0x0086
            java.lang.String r9 = "9"
            int r9 = r8.indexOf(r9)
            if (r9 != 0) goto L_0x0086
            int r10 = r9 + 4
            java.lang.String r8 = r8.substring(r9, r10)     // Catch:{ Exception -> 0x0086 }
            int r8 = com.uc.webview.export.internal.utility.k.c((java.lang.String) r8)     // Catch:{ Exception -> 0x0086 }
            goto L_0x0087
        L_0x0086:
            r8 = 0
        L_0x0087:
            r9 = 9000(0x2328, float:1.2612E-41)
            if (r8 < r9) goto L_0x0091
            com.uc.webview.export.internal.setup.UCSetupException r9 = new com.uc.webview.export.internal.setup.UCSetupException
            r9.<init>((int) r8, (java.lang.Throwable) r7)
            throw r9
        L_0x0091:
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException
            r9 = 4016(0xfb0, float:5.628E-42)
            r8.<init>((int) r9, (java.lang.Throwable) r7)
            throw r8
        L_0x0099:
            r7 = move-exception
            throw r7
        L_0x009b:
            r7 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException
            r9 = 4015(0xfaf, float:5.626E-42)
            r8.<init>((int) r9, (java.lang.Throwable) r7)
            throw r8
        L_0x00a4:
            r7 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException
            r9 = 4014(0xfae, float:5.625E-42)
            r8.<init>((int) r9, (java.lang.Throwable) r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.g.a(com.uc.webview.export.internal.setup.br, android.content.Context, java.lang.ClassLoader, java.util.concurrent.ConcurrentHashMap):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004c, code lost:
        if (((java.lang.Integer) r1.get((java.lang.Object) null)).intValue() >= 19) goto L_0x002d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0105  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void a(com.uc.webview.export.internal.setup.br r16, android.content.Context r17, java.lang.ClassLoader r18, int r19) {
        /*
            r0 = r16
            r1 = r18
            r2 = 35
            com.uc.webview.export.internal.uc.startup.b.a(r2)
            r2 = 2
            r3 = 0
            r4 = 0
            r5 = 1
            java.lang.String r6 = "com.uc.webview.browser.shell.NativeLibraries"
            java.lang.Class r6 = java.lang.Class.forName(r6, r5, r1)     // Catch:{ Throwable -> 0x0026 }
            if (r6 == 0) goto L_0x007b
            java.lang.String r7 = "LIBRARIES"
            java.lang.reflect.Field r7 = r6.getDeclaredField(r7)     // Catch:{ Throwable -> 0x0027 }
            r7.setAccessible(r5)     // Catch:{ Throwable -> 0x0027 }
            java.lang.Object r7 = r7.get(r3)     // Catch:{ Throwable -> 0x0027 }
            java.lang.String[][] r7 = (java.lang.String[][]) r7     // Catch:{ Throwable -> 0x0027 }
            r3 = r7
            goto L_0x007b
        L_0x0026:
            r6 = r3
        L_0x0027:
            boolean r7 = com.uc.webview.export.internal.utility.k.g()
            if (r7 == 0) goto L_0x002f
        L_0x002d:
            r1 = 1
            goto L_0x0050
        L_0x002f:
            java.lang.String r6 = "com.uc.webview.browser.shell.Build$Version"
            java.lang.Class r6 = java.lang.Class.forName(r6, r5, r1)     // Catch:{ Throwable -> 0x0116 }
            if (r6 == 0) goto L_0x004f
            java.lang.String r1 = "BUILD_SERIAL"
            java.lang.reflect.Field r1 = r6.getDeclaredField(r1)     // Catch:{ Throwable -> 0x0116 }
            r1.setAccessible(r5)     // Catch:{ Throwable -> 0x0116 }
            java.lang.Object r1 = r1.get(r3)     // Catch:{ Throwable -> 0x0116 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Throwable -> 0x0116 }
            int r1 = r1.intValue()     // Catch:{ Throwable -> 0x0116 }
            r7 = 19
            if (r1 < r7) goto L_0x004f
            goto L_0x002d
        L_0x004f:
            r1 = 0
        L_0x0050:
            if (r1 == 0) goto L_0x007b
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            if (r6 != 0) goto L_0x0059
            r1 = 3018(0xbca, float:4.229E-42)
            goto L_0x005b
        L_0x0059:
            r1 = 3019(0xbcb, float:4.23E-42)
        L_0x005b:
            java.util.Locale r3 = java.util.Locale.CHINA
            java.lang.Object[] r2 = new java.lang.Object[r2]
            if (r6 != 0) goto L_0x0064
            java.lang.String r7 = "Class"
            goto L_0x0066
        L_0x0064:
            java.lang.String r7 = "Field"
        L_0x0066:
            r2[r4] = r7
            if (r6 != 0) goto L_0x006d
            java.lang.String r4 = "com.uc.webview.browser.shell.NativeLibraries"
            goto L_0x006f
        L_0x006d:
            java.lang.String r4 = "com.uc.webview.browser.shell.NativeLibraries.LIBRARIES"
        L_0x006f:
            r2[r5] = r4
            java.lang.String r4 = "%s [%s] missing."
            java.lang.String r2 = java.lang.String.format(r3, r4, r2)
            r0.<init>((int) r1, (java.lang.String) r2)
            throw r0
        L_0x007b:
            if (r3 == 0) goto L_0x0110
            int r1 = r3.length
            if (r1 <= 0) goto L_0x0110
            java.lang.String r1 = r0.soDirPath
            if (r1 != 0) goto L_0x008a
            android.content.pm.ApplicationInfo r1 = r17.getApplicationInfo()
            java.lang.String r1 = r1.nativeLibraryDir
        L_0x008a:
            r7 = r19 & 16
            if (r7 == 0) goto L_0x0090
            r7 = 1
            goto L_0x0091
        L_0x0090:
            r7 = 0
        L_0x0091:
            int r8 = r3.length
            r9 = 0
        L_0x0093:
            if (r9 >= r8) goto L_0x0103
            r10 = r3[r9]
            r11 = r10[r4]
            r10 = r10[r5]
            long r12 = com.uc.webview.export.internal.utility.k.d((java.lang.String) r10)
            java.io.File r10 = new java.io.File
            r10.<init>(r1, r11)
            long r14 = r10.length()
            int r11 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x00c2
            int r11 = a
            java.util.Locale r12 = java.util.Locale.CHINA
            java.lang.String r13 = "Check file size ok [%s]."
            java.lang.Object[] r14 = new java.lang.Object[r5]
            r14[r4] = r10
            java.lang.String r10 = java.lang.String.format(r12, r13, r14)
            java.lang.Throwable[] r12 = new java.lang.Throwable[r4]
            com.uc.webview.export.cyclone.UCLogger.print((int) r11, (java.lang.String) r10, (java.lang.Throwable[]) r12)
            int r9 = r9 + 1
            goto L_0x0093
        L_0x00c2:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "组件校验 So Size Failed ["
            r0.<init>(r1)
            java.lang.String r1 = r10.getAbsolutePath()
            r0.append(r1)
            java.lang.String r1 = "]"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "EnvUtils"
            com.uc.webview.export.internal.utility.Log.d(r1, r0)
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r1 = 1008(0x3f0, float:1.413E-42)
            java.util.Locale r3 = java.util.Locale.CHINA
            r6 = 3
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r6[r4] = r10
            long r7 = r10.length()
            java.lang.Long r4 = java.lang.Long.valueOf(r7)
            r6[r5] = r4
            java.lang.Long r4 = java.lang.Long.valueOf(r12)
            r6[r2] = r4
            java.lang.String r2 = "So file [%s] with length [%d] mismatch to predefined [%d]."
            java.lang.String r2 = java.lang.String.format(r3, r2, r6)
            r0.<init>((int) r1, (java.lang.String) r2)
            throw r0
        L_0x0103:
            if (r7 == 0) goto L_0x0110
            java.lang.String r0 = r0.soDirPath
            java.lang.Integer r1 = java.lang.Integer.valueOf(r19)
            r2 = r17
            com.uc.webview.export.internal.setup.h.a(r2, r0, r3, r1)
        L_0x0110:
            r0 = 219(0xdb, float:3.07E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            return
        L_0x0116:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r1 = 3020(0xbcc, float:4.232E-42)
            java.lang.String r2 = "Version.BUILD_SERIAL not found."
            r0.<init>((int) r1, (java.lang.String) r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.g.a(com.uc.webview.export.internal.setup.br, android.content.Context, java.lang.ClassLoader, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void b(com.uc.webview.export.internal.setup.br r12, android.content.Context r13, java.lang.ClassLoader r14, int r15) {
        /*
            r0 = 31
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            r0 = 2
            r1 = 0
            r2 = 0
            r3 = 1
            java.lang.String r4 = "com.uc.webview.browser.shell.PakAssets"
            java.lang.Class r14 = java.lang.Class.forName(r4, r3, r14)     // Catch:{ Throwable -> 0x0022 }
            if (r14 == 0) goto L_0x0052
            java.lang.String r4 = "ASSETS"
            java.lang.reflect.Field r4 = r14.getDeclaredField(r4)     // Catch:{ Throwable -> 0x0023 }
            r4.setAccessible(r3)     // Catch:{ Throwable -> 0x0023 }
            java.lang.Object r4 = r4.get(r1)     // Catch:{ Throwable -> 0x0023 }
            java.lang.String[][] r4 = (java.lang.String[][]) r4     // Catch:{ Throwable -> 0x0023 }
            r1 = r4
            goto L_0x0052
        L_0x0022:
            r14 = r1
        L_0x0023:
            boolean r4 = com.uc.webview.export.internal.utility.k.g()
            if (r4 == 0) goto L_0x0052
            com.uc.webview.export.internal.setup.UCSetupException r12 = new com.uc.webview.export.internal.setup.UCSetupException
            if (r14 != 0) goto L_0x0030
            r13 = 3028(0xbd4, float:4.243E-42)
            goto L_0x0032
        L_0x0030:
            r13 = 3029(0xbd5, float:4.245E-42)
        L_0x0032:
            java.util.Locale r15 = java.util.Locale.CHINA
            java.lang.Object[] r0 = new java.lang.Object[r0]
            if (r14 != 0) goto L_0x003b
            java.lang.String r1 = "Class"
            goto L_0x003d
        L_0x003b:
            java.lang.String r1 = "Field"
        L_0x003d:
            r0[r2] = r1
            if (r14 != 0) goto L_0x0044
            java.lang.String r14 = "com.uc.webview.browser.shell.PakAssets"
            goto L_0x0046
        L_0x0044:
            java.lang.String r14 = "com.uc.webview.browser.shell.PakAssets.ASSETS"
        L_0x0046:
            r0[r3] = r14
            java.lang.String r14 = "%s [%s] missing."
            java.lang.String r14 = java.lang.String.format(r15, r14, r0)
            r12.<init>((int) r13, (java.lang.String) r14)
            throw r12
        L_0x0052:
            if (r1 == 0) goto L_0x00fa
            int r14 = r1.length
            if (r14 <= 0) goto L_0x00fa
            java.lang.String r12 = r12.resDirPath
            if (r12 != 0) goto L_0x005c
            return
        L_0x005c:
            java.io.File r14 = new java.io.File
            java.lang.String r4 = ""
            r14.<init>(r12, r4)
            java.lang.String r12 = r14.getAbsolutePath()
            java.io.File r14 = new java.io.File
            java.lang.String r4 = "paks"
            r14.<init>(r12, r4)
            boolean r4 = r14.exists()
            if (r4 == 0) goto L_0x0078
            java.lang.String r12 = r14.getAbsolutePath()
        L_0x0078:
            r14 = r15 & 64
            if (r14 == 0) goto L_0x007e
            r14 = 1
            goto L_0x007f
        L_0x007e:
            r14 = 0
        L_0x007f:
            int r4 = r1.length
            r5 = 0
        L_0x0081:
            if (r5 >= r4) goto L_0x00f1
            r6 = r1[r5]
            r7 = r6[r2]
            r6 = r6[r3]
            long r8 = com.uc.webview.export.internal.utility.k.d((java.lang.String) r6)
            java.io.File r6 = new java.io.File
            r6.<init>(r12, r7)
            long r10 = r6.length()
            int r7 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r7 != 0) goto L_0x00b0
            int r7 = a
            java.util.Locale r8 = java.util.Locale.CHINA
            java.lang.String r9 = "Check file size ok [%s]."
            java.lang.Object[] r10 = new java.lang.Object[r3]
            r10[r2] = r6
            java.lang.String r6 = java.lang.String.format(r8, r9, r10)
            java.lang.Throwable[] r8 = new java.lang.Throwable[r2]
            com.uc.webview.export.cyclone.UCLogger.print((int) r7, (java.lang.String) r6, (java.lang.Throwable[]) r8)
            int r5 = r5 + 1
            goto L_0x0081
        L_0x00b0:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r13 = "组件校验 Pak Size Failed ["
            r12.<init>(r13)
            java.lang.String r13 = r6.getAbsolutePath()
            r12.append(r13)
            java.lang.String r13 = "]"
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            java.lang.String r13 = "EnvUtils"
            com.uc.webview.export.internal.utility.Log.d(r13, r12)
            com.uc.webview.export.internal.setup.UCSetupException r12 = new com.uc.webview.export.internal.setup.UCSetupException
            r13 = 1014(0x3f6, float:1.421E-42)
            java.util.Locale r14 = java.util.Locale.CHINA
            r15 = 3
            java.lang.Object[] r15 = new java.lang.Object[r15]
            r15[r2] = r6
            long r1 = r6.length()
            java.lang.Long r1 = java.lang.Long.valueOf(r1)
            r15[r3] = r1
            java.lang.Long r1 = java.lang.Long.valueOf(r8)
            r15[r0] = r1
            java.lang.String r0 = "So file [%s] with length [%d] mismatch to predefined [%d]."
            java.lang.String r14 = java.lang.String.format(r14, r0, r15)
            r12.<init>((int) r13, (java.lang.String) r14)
            throw r12
        L_0x00f1:
            if (r14 == 0) goto L_0x00fa
            java.lang.Integer r14 = java.lang.Integer.valueOf(r15)
            com.uc.webview.export.internal.setup.h.b(r13, r12, r1, r14)
        L_0x00fa:
            r12 = 215(0xd7, float:3.01E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.g.b(com.uc.webview.export.internal.setup.br, android.content.Context, java.lang.ClassLoader, int):void");
    }

    public static boolean a(Context context, br brVar, ConcurrentHashMap<String, Object> concurrentHashMap) {
        boolean booleanValue;
        synchronized (c) {
            if (b == null) {
                b = Boolean.valueOf(b(context, brVar, concurrentHashMap));
            }
            booleanValue = b.booleanValue();
        }
        return booleanValue;
    }

    private static boolean b(Context context, br brVar, ConcurrentHashMap<String, Object> concurrentHashMap) {
        String str = (String) concurrentHashMap.get(UCCore.OPTION_UCM_ZIP_FILE);
        if (!k.a(brVar.dataDir)) {
            if (!k.a(str)) {
                return k.c(context, brVar.dataDir, str);
            }
            String str2 = (String) concurrentHashMap.get(UCCore.OPTION_UCM_UPD_URL);
            if (!k.a(str2)) {
                File a2 = k.a(context, "updates");
                if (brVar.dataDir.startsWith(a2.getAbsolutePath())) {
                    File file = new File(a2, UCCyclone.getSourceHash(str2));
                    if (!brVar.dataDir.startsWith(file.getAbsolutePath())) {
                        return true;
                    }
                    if (!d.a().b(UCCore.OPTION_EXACT_OLD_KERNEL_CHECK)) {
                        return false;
                    }
                    try {
                        if (!((Boolean) ((Callable) concurrentHashMap.get(UCCore.OPTION_DOWNLOAD_CHECKER)).call()).booleanValue()) {
                            return false;
                        }
                        Pair<Long, Long> a3 = k.a(str2, (URL) null);
                        if (!brVar.dataDir.startsWith(new File(file, UCCyclone.getSourceHash(((Long) a3.first).longValue(), ((Long) a3.second).longValue())).getAbsolutePath())) {
                            return true;
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
