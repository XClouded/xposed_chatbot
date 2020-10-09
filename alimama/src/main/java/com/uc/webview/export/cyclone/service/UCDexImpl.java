package com.uc.webview.export.cyclone.service;

import android.content.Context;
import android.os.Build;
import android.util.Pair;
import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.ui.component.WXComponent;
import com.uc.webview.export.cyclone.Constant;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCHashMap;
import com.uc.webview.export.cyclone.UCKnownException;
import com.uc.webview.export.cyclone.UCLibrary;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.cyclone.UCService;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;

@Constant
/* compiled from: U4Source */
public class UCDexImpl implements UCDex, Runnable {
    private static final boolean ALWAYS_STAT = true;
    /* access modifiers changed from: private */
    public static String CLASSES_DEX = "classes.dex";
    private static final String DEX_CLASS_LOADER = "DSL";
    private static final String DEX_FILE = "DF";
    private static final String FLAG_FILE = "dex_opt_crash_flag";
    private static final String LOG_TAG = "UCDexImplCode";
    private static final int TYPE_DEX_CLASSLOADER = 1;
    private static final int TYPE_DEX_FILE = 2;
    private static boolean mSoIsLoaded = false;
    private static UCKnownException mSoIsLoadedException = null;
    private static Context sCtx = null;
    private static ConcurrentLinkedQueue<Pair<Integer, Object>> sDelayedDexs = new ConcurrentLinkedQueue<>();
    private static boolean sHookReady = false;
    private static boolean sInited = false;
    private static int sLastSyncDataResult = 0;
    private static Method sOpenDexFileMethod1 = null;
    private static Method sOpenDexFileMethod2 = null;
    private static boolean sOptRunAsExpected = true;

    private static native int hookArt(String str);

    private static native int initDvm(int i);

    private static native int openDexFile(String str, String str2, int i);

    private static native int openDexFile(byte[] bArr);

    private static native int syncData(boolean z);

    public int getServiceVersion() {
        return 0;
    }

    static {
        try {
            UCService.registerImpl(UCDex.class, new UCDexImpl());
        } catch (Throwable th) {
            UCLogger create = UCLogger.create(WXComponent.PROP_FS_WRAP_CONTENT, LOG_TAG);
            if (create != null) {
                create.print("UCDexImplCode register exception:", th);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.uc.webview.export.cyclone.Constant
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int openDexFileUC(java.lang.String r9, java.lang.String r10, int r11) {
        /*
            java.lang.String r9 = r9.trim()
            r10 = 1
            r11 = 0
            r0 = 0
            int r1 = r9.length()     // Catch:{ Throwable -> 0x011e }
            int r1 = r1 + -4
            java.lang.String r1 = r9.substring(r1)     // Catch:{ Throwable -> 0x011e }
            java.lang.String r1 = r1.toLowerCase()     // Catch:{ Throwable -> 0x011e }
            java.lang.String r2 = ".dex"
            boolean r1 = r1.endsWith(r2)     // Catch:{ Throwable -> 0x011e }
            if (r1 != 0) goto L_0x0066
            java.io.File r4 = com.uc.webview.export.cyclone.UCCyclone.expectFile((java.lang.String) r9)     // Catch:{ Throwable -> 0x011e }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x011e }
            r9.<init>()     // Catch:{ Throwable -> 0x011e }
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.getDecompressFileHash(r4)     // Catch:{ Throwable -> 0x011e }
            r9.append(r1)     // Catch:{ Throwable -> 0x011e }
            java.lang.String r1 = ".dex"
            r9.append(r1)     // Catch:{ Throwable -> 0x011e }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x011e }
            android.content.Context r1 = sCtx     // Catch:{ Throwable -> 0x011e }
            java.io.File r1 = com.uc.webview.export.cyclone.UCCyclone.getDataFolder(r1)     // Catch:{ Throwable -> 0x011e }
            java.io.File r8 = new java.io.File     // Catch:{ Throwable -> 0x011e }
            r8.<init>(r1, r9)     // Catch:{ Throwable -> 0x011e }
            boolean r9 = r8.exists()     // Catch:{ Throwable -> 0x011e }
            if (r9 != 0) goto L_0x0062
            com.uc.webview.export.cyclone.service.UCDexImpl$1 r6 = new com.uc.webview.export.cyclone.service.UCDexImpl$1     // Catch:{ Throwable -> 0x011e }
            r6.<init>()     // Catch:{ Throwable -> 0x011e }
            android.content.Context r2 = sCtx     // Catch:{ Throwable -> 0x011e }
            r3 = 0
            r7 = 0
            r5 = r1
            com.uc.webview.export.cyclone.UCCyclone.decompressIfNeeded((android.content.Context) r2, (boolean) r3, (java.io.File) r4, (java.io.File) r5, (java.io.FilenameFilter) r6, (boolean) r7)     // Catch:{ Throwable -> 0x011e }
            java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x011e }
            java.lang.String r2 = CLASSES_DEX     // Catch:{ Throwable -> 0x011e }
            r9.<init>(r1, r2)     // Catch:{ Throwable -> 0x011e }
            boolean r9 = r9.renameTo(r8)     // Catch:{ Throwable -> 0x011e }
            if (r9 != 0) goto L_0x0062
            return r0
        L_0x0062:
            java.lang.String r9 = r8.getAbsolutePath()     // Catch:{ Throwable -> 0x011e }
        L_0x0066:
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x011e }
            r1.<init>(r9)     // Catch:{ Throwable -> 0x011e }
            java.util.concurrent.ConcurrentLinkedQueue<java.io.File> r2 = com.uc.webview.export.cyclone.UCCyclone.sInusedFiles     // Catch:{ Throwable -> 0x011e }
            if (r2 == 0) goto L_0x0074
            java.util.concurrent.ConcurrentLinkedQueue<java.io.File> r2 = com.uc.webview.export.cyclone.UCCyclone.sInusedFiles     // Catch:{ Throwable -> 0x011e }
            r2.add(r1)     // Catch:{ Throwable -> 0x011e }
        L_0x0074:
            long r2 = r1.length()     // Catch:{ Throwable -> 0x011e }
            r4 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 >= 0) goto L_0x013a
            long r2 = r1.length()     // Catch:{ Throwable -> 0x011e }
            int r2 = (int) r2     // Catch:{ Throwable -> 0x011e }
            byte[] r3 = new byte[r2]     // Catch:{ Throwable -> 0x011e }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x011e }
            r4.<init>(r1)     // Catch:{ Throwable -> 0x011e }
            int r1 = r4.read(r3)     // Catch:{ Throwable -> 0x011e }
            if (r1 != r2) goto L_0x0102
            java.lang.reflect.Method r1 = sOpenDexFileMethod1     // Catch:{ Exception -> 0x00f6 }
            if (r1 != 0) goto L_0x009d
            int r9 = openDexFile(r3)     // Catch:{ Exception -> 0x00f6 }
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x011e }
            return r9
        L_0x009d:
            java.lang.Class<dalvik.system.DexFile> r1 = dalvik.system.DexFile.class
            java.lang.reflect.Method r2 = sOpenDexFileMethod1     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object[] r5 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x00f6 }
            r5[r0] = r3     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r1 = com.uc.webview.export.cyclone.UCCyclone.invoke((java.lang.Object) r11, (java.lang.Class<?>) r1, (java.lang.reflect.Method) r2, (java.lang.Object[]) r5)     // Catch:{ Exception -> 0x00f6 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x00f6 }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x00f6 }
            boolean r2 = com.uc.webview.export.cyclone.UCCyclone.enableDebugLog     // Catch:{ Exception -> 0x00f6 }
            if (r2 != 0) goto L_0x00b5
            r2 = r11
            goto L_0x00bd
        L_0x00b5:
            java.lang.String r2 = "d"
            java.lang.String r3 = "UCDexImplCode"
            com.uc.webview.export.cyclone.UCLogger r2 = com.uc.webview.export.cyclone.UCLogger.create(r2, r3)     // Catch:{ Exception -> 0x00f6 }
        L_0x00bd:
            if (r2 == 0) goto L_0x00da
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r5 = "UCDexImplCode.openDexFileUC: "
            r3.<init>(r5)     // Catch:{ Exception -> 0x00f6 }
            r3.append(r9)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r9 = " = "
            r3.append(r9)     // Catch:{ Exception -> 0x00f6 }
            r3.append(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r9 = r3.toString()     // Catch:{ Exception -> 0x00f6 }
            java.lang.Throwable[] r3 = new java.lang.Throwable[r0]     // Catch:{ Exception -> 0x00f6 }
            r2.print(r9, r3)     // Catch:{ Exception -> 0x00f6 }
        L_0x00da:
            if (r1 == 0) goto L_0x00e0
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x011e }
            return r1
        L_0x00e0:
            java.lang.Exception r9 = new java.lang.Exception     // Catch:{ Exception -> 0x00f6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r3 = "OpenDexFile:"
            r2.<init>(r3)     // Catch:{ Exception -> 0x00f6 }
            r2.append(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x00f6 }
            r9.<init>(r1)     // Catch:{ Exception -> 0x00f6 }
            throw r9     // Catch:{ Exception -> 0x00f6 }
        L_0x00f4:
            r9 = move-exception
            goto L_0x00fe
        L_0x00f6:
            r9 = move-exception
            r9.printStackTrace()     // Catch:{ all -> 0x00f4 }
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x011e }
            goto L_0x013a
        L_0x00fe:
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x011e }
            throw r9     // Catch:{ Throwable -> 0x011e }
        L_0x0102:
            java.lang.Exception r9 = new java.lang.Exception     // Catch:{ Throwable -> 0x011e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x011e }
            java.lang.String r4 = "Read length not match:"
            r3.<init>(r4)     // Catch:{ Throwable -> 0x011e }
            r3.append(r1)     // Catch:{ Throwable -> 0x011e }
            java.lang.String r1 = "/"
            r3.append(r1)     // Catch:{ Throwable -> 0x011e }
            r3.append(r2)     // Catch:{ Throwable -> 0x011e }
            java.lang.String r1 = r3.toString()     // Catch:{ Throwable -> 0x011e }
            r9.<init>(r1)     // Catch:{ Throwable -> 0x011e }
            throw r9     // Catch:{ Throwable -> 0x011e }
        L_0x011e:
            r9 = move-exception
            boolean r1 = com.uc.webview.export.cyclone.UCCyclone.enableDebugLog
            if (r1 != 0) goto L_0x0124
            goto L_0x012c
        L_0x0124:
            java.lang.String r11 = "e"
            java.lang.String r1 = "UCDexImplCode"
            com.uc.webview.export.cyclone.UCLogger r11 = com.uc.webview.export.cyclone.UCLogger.create(r11, r1)
        L_0x012c:
            if (r11 == 0) goto L_0x0137
            java.lang.String r1 = "UCDexImplCode.openDexFileUC: opt_dex error: "
            java.lang.Throwable[] r10 = new java.lang.Throwable[r10]
            r10[r0] = r9
            r11.print(r1, r10)
        L_0x0137:
            r9.printStackTrace()
        L_0x013a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.service.UCDexImpl.openDexFileUC(java.lang.String, java.lang.String, int):int");
    }

    @Constant
    private static int openDexFileUCSys(String str, String str2, int i) {
        try {
            int openDexFile = openDexFile(str, str2, i);
            UCLogger create = !UCCyclone.enableDebugLog ? null : UCLogger.create("d", LOG_TAG);
            if (create != null) {
                create.print("UCDexImplCode.openDexFileUCSys: unopt_dex = " + str, new Throwable[0]);
            }
            return openDexFile;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static boolean detectFirstOdex(String str, String str2) {
        for (String optimizedFileFor : str.split(":")) {
            if (!UCCyclone.optimizedFileFor(optimizedFileFor, str2).exists()) {
                return true;
            }
        }
        return false;
    }

    public DexClassLoader createDexClassLoader(Context context, Boolean bool, String str, String str2, String str3, ClassLoader classLoader) {
        if (classLoader == null) {
            try {
                classLoader = UCDexImpl.class.getClassLoader();
            } catch (IOException e) {
                throw new UCKnownException(6012, (Throwable) e);
            }
        }
        return (DexClassLoader) create(DEX_CLASS_LOADER, context, bool, str, str2, str3, classLoader, 0);
    }

    public DexFile createDexFile(Context context, Boolean bool, String str, String str2, int i) throws IOException {
        return (DexFile) create(DEX_FILE, context, bool, str, str2, (String) null, (ClassLoader) null, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:119:0x01fc A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x018e A[SYNTHETIC, Splitter:B:95:0x018e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object create(java.lang.String r40, android.content.Context r41, java.lang.Boolean r42, java.lang.String r43, java.lang.String r44, java.lang.String r45, java.lang.ClassLoader r46, int r47) throws java.io.IOException {
        /*
            r2 = r40
            r12 = r43
            r1 = r44
            r13 = r45
            r14 = r46
            r15 = r47
            com.uc.webview.export.cyclone.UCElapseTime r16 = new com.uc.webview.export.cyclone.UCElapseTime
            r16.<init>()
            r17 = 1
            r11 = 0
            r18 = 0
            boolean r0 = sOptRunAsExpected     // Catch:{ Throwable -> 0x0093 }
            if (r0 == 0) goto L_0x0089
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0093 }
            r3 = 14
            if (r0 < r3) goto L_0x0089
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0093 }
            r3 = 21
            if (r0 == r3) goto L_0x0089
            if (r42 != 0) goto L_0x002d
            boolean r0 = detectFirstOdex(r43, r44)     // Catch:{ Throwable -> 0x0093 }
            goto L_0x0031
        L_0x002d:
            boolean r0 = r42.booleanValue()     // Catch:{ Throwable -> 0x0093 }
        L_0x0031:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x0093 }
            boolean r0 = r4.booleanValue()     // Catch:{ Throwable -> 0x0087 }
            if (r0 == 0) goto L_0x008b
            java.io.File r0 = com.uc.webview.export.cyclone.UCCyclone.getDataFolder(r41)     // Catch:{ Throwable -> 0x0087 }
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0087 }
            java.lang.String r5 = "dex_opt_crash_flag"
            r3.<init>(r0, r5)     // Catch:{ Throwable -> 0x0087 }
            boolean r0 = r3.exists()     // Catch:{ Throwable -> 0x0087 }
            if (r0 != 0) goto L_0x005a
            r3.createNewFile()     // Catch:{ Throwable -> 0x0058 }
            java.util.concurrent.ConcurrentLinkedQueue<java.io.File> r0 = com.uc.webview.export.cyclone.UCCyclone.sInusedFiles     // Catch:{ Throwable -> 0x0058 }
            if (r0 == 0) goto L_0x0058
            java.util.concurrent.ConcurrentLinkedQueue<java.io.File> r0 = com.uc.webview.export.cyclone.UCCyclone.sInusedFiles     // Catch:{ Throwable -> 0x0058 }
            r0.add(r3)     // Catch:{ Throwable -> 0x0058 }
        L_0x0058:
            r0 = 1
            goto L_0x005b
        L_0x005a:
            r0 = 0
        L_0x005b:
            java.lang.String r3 = "sdk_ucdexopt_crash"
            com.uc.webview.export.cyclone.UCHashMap r5 = new com.uc.webview.export.cyclone.UCHashMap     // Catch:{ Throwable -> 0x0087 }
            r5.<init>()     // Catch:{ Throwable -> 0x0087 }
            java.lang.String r6 = "cnt"
            java.lang.String r7 = "1"
            com.uc.webview.export.cyclone.UCHashMap r5 = r5.set(r6, r7)     // Catch:{ Throwable -> 0x0087 }
            java.lang.String r6 = "sdk_int"
            int r7 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0087 }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ Throwable -> 0x0087 }
            com.uc.webview.export.cyclone.UCHashMap r5 = r5.set(r6, r7)     // Catch:{ Throwable -> 0x0087 }
            java.lang.String r6 = "crash"
            if (r0 == 0) goto L_0x007d
            java.lang.String r7 = "T"
            goto L_0x007f
        L_0x007d:
            java.lang.String r7 = "F"
        L_0x007f:
            com.uc.webview.export.cyclone.UCHashMap r5 = r5.set(r6, r7)     // Catch:{ Throwable -> 0x0087 }
            com.uc.webview.export.cyclone.UCCyclone.stat(r3, r5)     // Catch:{ Throwable -> 0x0087 }
            goto L_0x008c
        L_0x0087:
            r0 = move-exception
            goto L_0x0096
        L_0x0089:
            r4 = r42
        L_0x008b:
            r0 = 0
        L_0x008c:
            r19 = r0
            r20 = r4
            r21 = r11
            goto L_0x00a3
        L_0x0093:
            r0 = move-exception
            r4 = r42
        L_0x0096:
            com.uc.webview.export.cyclone.UCKnownException r3 = new com.uc.webview.export.cyclone.UCKnownException
            r5 = 6011(0x177b, float:8.423E-42)
            r3.<init>((int) r5, (java.lang.Throwable) r0)
            r21 = r3
            r20 = r4
            r19 = 0
        L_0x00a3:
            if (r1 == 0) goto L_0x00c5
            java.io.File r0 = new java.io.File
            r0.<init>(r1)
            boolean r3 = r0.exists()
            if (r3 != 0) goto L_0x00c5
            java.lang.String r3 = r0.getAbsolutePath()
            java.io.File r4 = r41.getCacheDir()
            java.lang.String r4 = r4.getAbsolutePath()
            boolean r3 = r3.startsWith(r4)
            if (r3 == 0) goto L_0x00c5
            com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r0)
        L_0x00c5:
            if (r19 != 0) goto L_0x00dc
            java.lang.String r0 = "DSL"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x00d6
            com.uc.webview.export.cyclone.UCLoader r0 = new com.uc.webview.export.cyclone.UCLoader
            r0.<init>(r12, r1, r13, r14)
            goto L_0x01dd
        L_0x00d6:
            dalvik.system.DexFile r0 = dalvik.system.DexFile.loadDex(r12, r1, r15)
            goto L_0x01dd
        L_0x00dc:
            if (r19 == 0) goto L_0x0119
            java.lang.String r0 = "DSL"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0109 }
            if (r0 == 0) goto L_0x00f9
            r4 = 0
            r5 = 1
            r6 = 1
            r0 = 0
            r3 = r41
            r7 = r43
            r8 = r44
            r9 = r45
            r10 = r46
            r11 = r0
            doDexOpt(r3, r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x0109 }
            goto L_0x0119
        L_0x00f9:
            r4 = 0
            r5 = 1
            r6 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r3 = r41
            r7 = r43
            r8 = r44
            doDexOpt(r3, r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x0109 }
            goto L_0x0119
        L_0x0109:
            r0 = move-exception
            sOptRunAsExpected = r18     // Catch:{ Throwable -> 0x0116 }
            com.uc.webview.export.cyclone.UCKnownException r3 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0116 }
            r4 = 6009(0x1779, float:8.42E-42)
            r3.<init>((int) r4, (java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x0116 }
            r21 = r3
            goto L_0x0119
        L_0x0116:
            r0 = 0
            goto L_0x01db
        L_0x0119:
            java.lang.String r0 = "DSL"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x012d }
            if (r0 == 0) goto L_0x0127
            com.uc.webview.export.cyclone.UCLoader r0 = new com.uc.webview.export.cyclone.UCLoader     // Catch:{ Throwable -> 0x012d }
            r0.<init>(r12, r1, r13, r14)     // Catch:{ Throwable -> 0x012d }
            goto L_0x012b
        L_0x0127:
            dalvik.system.DexFile r0 = dalvik.system.DexFile.loadDex(r12, r1, r15)     // Catch:{ Throwable -> 0x012d }
        L_0x012b:
            r11 = r0
            goto L_0x018c
        L_0x012d:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r3 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0116 }
            r4 = 6002(0x1772, float:8.41E-42)
            r3.<init>((int) r4, (java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x0116 }
            if (r19 == 0) goto L_0x0189
            boolean r0 = sOptRunAsExpected     // Catch:{ Throwable -> 0x0186 }
            if (r0 == 0) goto L_0x0189
            sOptRunAsExpected = r18     // Catch:{ Throwable -> 0x0186 }
            java.lang.String r0 = "DSL"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0170 }
            if (r0 == 0) goto L_0x015b
            r23 = 0
            r24 = 0
            r25 = 1
            r26 = 0
            r27 = 0
            r28 = 0
            r29 = 0
            r30 = 0
            r22 = r41
            doDexOpt(r22, r23, r24, r25, r26, r27, r28, r29, r30)     // Catch:{ Throwable -> 0x0170 }
            goto L_0x0170
        L_0x015b:
            r32 = 0
            r33 = 0
            r34 = 0
            r35 = 0
            r36 = 0
            r37 = 0
            r38 = 0
            r39 = 0
            r31 = r41
            doDexOpt(r31, r32, r33, r34, r35, r36, r37, r38, r39)     // Catch:{ Throwable -> 0x0170 }
        L_0x0170:
            java.lang.String r0 = "DSL"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0186 }
            if (r0 == 0) goto L_0x017e
            com.uc.webview.export.cyclone.UCLoader r0 = new com.uc.webview.export.cyclone.UCLoader     // Catch:{ Throwable -> 0x0186 }
            r0.<init>(r12, r1, r13, r14)     // Catch:{ Throwable -> 0x0186 }
            goto L_0x0182
        L_0x017e:
            dalvik.system.DexFile r0 = dalvik.system.DexFile.loadDex(r12, r1, r15)     // Catch:{ Throwable -> 0x0186 }
        L_0x0182:
            r11 = r0
            r21 = r3
            goto L_0x018c
        L_0x0186:
            r21 = r3
            goto L_0x0116
        L_0x0189:
            r21 = r3
            r11 = 0
        L_0x018c:
            if (r19 == 0) goto L_0x01d5
            boolean r0 = sOptRunAsExpected     // Catch:{ Throwable -> 0x01d3 }
            if (r0 == 0) goto L_0x01d5
            java.lang.String r0 = "DSL"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x01c6 }
            if (r0 == 0) goto L_0x01b0
            r23 = 0
            r24 = 0
            r25 = 1
            r26 = 0
            r27 = 0
            r28 = 0
            r29 = 0
            r30 = 0
            r22 = r41
            doDexOpt(r22, r23, r24, r25, r26, r27, r28, r29, r30)     // Catch:{ Throwable -> 0x01c6 }
            goto L_0x01d9
        L_0x01b0:
            r32 = 0
            r33 = 0
            r34 = 0
            r35 = 0
            r36 = 0
            r37 = 0
            r38 = 0
            r39 = 0
            r31 = r41
            doDexOpt(r31, r32, r33, r34, r35, r36, r37, r38, r39)     // Catch:{ Throwable -> 0x01c6 }
            goto L_0x01d9
        L_0x01c6:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r1 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x01d3 }
            r3 = 6010(0x177a, float:8.422E-42)
            r1.<init>((int) r3, (java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x01d3 }
            sOptRunAsExpected = r18     // Catch:{ Throwable -> 0x01d1 }
            goto L_0x01d7
        L_0x01d1:
            r21 = r1
        L_0x01d3:
            r0 = r11
            goto L_0x01db
        L_0x01d5:
            r1 = r21
        L_0x01d7:
            r21 = r1
        L_0x01d9:
            r0 = r11
            goto L_0x01dd
        L_0x01db:
            r17 = 0
        L_0x01dd:
            java.lang.String r1 = "sdk_ucdexopt"
            r3 = 0
            com.uc.webview.export.cyclone.UCCyclone.stat(r1, r3)
            long r7 = r16.getMilis()
            long r9 = r16.getMilisCpu()
            r1 = r17
            r2 = r40
            r3 = r19
            r4 = r20
            r5 = r43
            r6 = r21
            doStat(r1, r2, r3, r4, r5, r6, r7, r9)
            if (r17 != 0) goto L_0x0200
            if (r21 != 0) goto L_0x01ff
            goto L_0x0200
        L_0x01ff:
            throw r21
        L_0x0200:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.service.UCDexImpl.create(java.lang.String, android.content.Context, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.ClassLoader, int):java.lang.Object");
    }

    private static void doStat(boolean z, String str, boolean z2, Boolean bool, String str2, UCKnownException uCKnownException, long j, long j2) {
        String str3;
        if (UCCyclone.statCallback != null) {
            int lastIndexOf = str2.lastIndexOf("/");
            if (lastIndexOf != -1) {
                str2 = str2.substring(lastIndexOf + 1);
            } else if (str2.length() >= 7) {
                str2 = str2.substring(str2.length() - 7);
            }
            String str4 = "";
            String str5 = "";
            String str6 = "";
            if (uCKnownException != null) {
                try {
                    str6 = String.valueOf(uCKnownException.errCode());
                } catch (Throwable unused) {
                }
            }
            if (uCKnownException != null) {
                try {
                    str4 = uCKnownException.getRootCause().getClass().getSimpleName();
                } catch (Throwable unused2) {
                }
            }
            if (uCKnownException != null) {
                try {
                    str5 = uCKnownException.getRootCause().getMessage();
                } catch (Throwable unused3) {
                }
            }
            UCHashMap uCHashMap = new UCHashMap().set("cnt", "1").set("succ", z ? ApiConstants.UTConstants.UT_SUCCESS_T : ApiConstants.UTConstants.UT_SUCCESS_F).set("task", str).set("enable", z2 ? ApiConstants.UTConstants.UT_SUCCESS_T : ApiConstants.UTConstants.UT_SUCCESS_F).set("hook_succ", sHookReady ? ApiConstants.UTConstants.UT_SUCCESS_T : ApiConstants.UTConstants.UT_SUCCESS_F).set("run_expected", sOptRunAsExpected ? ApiConstants.UTConstants.UT_SUCCESS_T : ApiConstants.UTConstants.UT_SUCCESS_F);
            if (bool == null) {
                str3 = BuildConfig.buildJavascriptFrameworkVersion;
            } else {
                str3 = bool.booleanValue() ? ApiConstants.UTConstants.UT_SUCCESS_T : ApiConstants.UTConstants.UT_SUCCESS_F;
            }
            UCCyclone.stat("sdk_ucdexopt", uCHashMap.set("frun", str3).set("data", String.valueOf(sLastSyncDataResult)).set("sdk_int", String.valueOf(Build.VERSION.SDK_INT)).set("code", str2).set("cost_cpu", String.valueOf(j2)).set("cost", String.valueOf(j)).set("err", str6).set("cls", str4).set("msg", str5));
        }
    }

    private static synchronized void loadSo(Context context) {
        synchronized (UCDexImpl.class) {
            if (!mSoIsLoaded) {
                if (mSoIsLoadedException == null) {
                    try {
                        UCLibrary.load(context, UCCyclone.genFile(context, (String) null, "libdexhook", ".so", 0, UCDexImplConstant.sLibMD5Hash, UCDexImplConstant.genCodes(), new Object[0]).getAbsolutePath(), (ClassLoader) null);
                        mSoIsLoaded = true;
                    } catch (Throwable th) {
                        UCKnownException uCKnownException = new UCKnownException(th);
                        mSoIsLoadedException = uCKnownException;
                        throw uCKnownException;
                    }
                } else {
                    throw mSoIsLoadedException;
                }
            }
        }
    }

    private static void doDexOpt(Context context, boolean z, boolean z2, boolean z3, String str, String str2, String str3, ClassLoader classLoader, int i) {
        UCKnownException uCKnownException;
        int hookArt;
        if (!sInited) {
            synchronized (UCDexImpl.class) {
                if (!sInited) {
                    sInited = true;
                    UCKnownException uCKnownException2 = null;
                    int i2 = 999;
                    try {
                        loadSo(context);
                        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 26) {
                            if (Build.VERSION.SDK_INT >= 25) {
                                hookArt = hookArt("execve");
                            } else {
                                hookArt = hookArt("execv");
                            }
                            i2 = hookArt;
                        }
                    } catch (Throwable th) {
                        try {
                            uCKnownException = new UCKnownException(6003, th);
                        } finally {
                            sCtx = context;
                            sHookReady = false;
                        }
                    }
                    sCtx = context;
                    sHookReady = i2 == 0;
                    uCKnownException = uCKnownException2;
                    if (!z && UCCyclone.statCallback != null) {
                        String valueOf = String.valueOf(i2);
                        String str4 = "";
                        String str5 = "";
                        String str6 = "";
                        if (uCKnownException != null) {
                            try {
                                str6 = String.valueOf(uCKnownException.errCode());
                            } catch (Throwable unused) {
                            }
                        }
                        if (uCKnownException != null) {
                            try {
                                str4 = uCKnownException.getRootCause().getClass().getSimpleName();
                            } catch (Throwable unused2) {
                            }
                        }
                        if (uCKnownException != null) {
                            try {
                                str5 = uCKnownException.getRootCause().getMessage();
                            } catch (Throwable unused3) {
                            }
                        }
                        UCCyclone.stat("sdk_hookdex", new UCHashMap().set("cnt", "1").set("hook_succ", sHookReady ? ApiConstants.UTConstants.UT_SUCCESS_T : ApiConstants.UTConstants.UT_SUCCESS_F).set("art", valueOf).set("dvm", "999").set("dvm2", "999").set("sdk_int", String.valueOf(Build.VERSION.SDK_INT)).set("err", str6).set("cls", str4).set("msg", str5));
                    }
                }
            }
        }
        if (sHookReady) {
            if (sLastSyncDataResult != 0) {
                throw new UCKnownException(6008, "syncData:" + sLastSyncDataResult);
            } else if (!z && z2) {
                sLastSyncDataResult = syncData(true);
                if (z3) {
                    sDelayedDexs.add(new Pair(1, new Object[]{str, str2, str3, classLoader}));
                } else {
                    sDelayedDexs.add(new Pair(2, new Object[]{str, str2, Integer.valueOf(i)}));
                }
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|(1:19)(2:20|21)|22|23) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0057 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void run() {
        /*
            r7 = this;
            monitor-enter(r7)
            java.util.concurrent.ConcurrentLinkedQueue<android.util.Pair<java.lang.Integer, java.lang.Object>> r0 = sDelayedDexs     // Catch:{ all -> 0x0063 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0063 }
            if (r0 == 0) goto L_0x000b
            monitor-exit(r7)
            return
        L_0x000b:
            java.lang.Class<com.uc.webview.export.cyclone.service.UCDexImpl> r0 = com.uc.webview.export.cyclone.service.UCDexImpl.class
            monitor-enter(r0)     // Catch:{ all -> 0x0063 }
            r1 = 0
            syncData(r1)     // Catch:{ all -> 0x0060 }
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            java.util.concurrent.ConcurrentLinkedQueue<android.util.Pair<java.lang.Integer, java.lang.Object>> r0 = sDelayedDexs     // Catch:{ all -> 0x0063 }
            java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x0063 }
        L_0x0019:
            android.util.Pair r0 = (android.util.Pair) r0     // Catch:{ all -> 0x0063 }
            if (r0 == 0) goto L_0x005e
            java.lang.Object r2 = r0.second     // Catch:{ all -> 0x0063 }
            java.lang.Object[] r2 = (java.lang.Object[]) r2     // Catch:{ all -> 0x0063 }
            java.lang.Object r0 = r0.first     // Catch:{ all -> 0x0063 }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x0063 }
            int r0 = r0.intValue()     // Catch:{ all -> 0x0063 }
            r3 = 2
            r4 = 1
            if (r0 != r4) goto L_0x0044
            com.uc.webview.export.cyclone.UCLoader r0 = new com.uc.webview.export.cyclone.UCLoader     // Catch:{ all -> 0x0063 }
            r5 = r2[r1]     // Catch:{ all -> 0x0063 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x0063 }
            r4 = r2[r4]     // Catch:{ all -> 0x0063 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x0063 }
            r3 = r2[r3]     // Catch:{ all -> 0x0063 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0063 }
            r6 = 3
            r2 = r2[r6]     // Catch:{ all -> 0x0063 }
            java.lang.ClassLoader r2 = (java.lang.ClassLoader) r2     // Catch:{ all -> 0x0063 }
            r0.<init>(r5, r4, r3, r2)     // Catch:{ all -> 0x0063 }
            goto L_0x0057
        L_0x0044:
            r0 = r2[r1]     // Catch:{ Throwable -> 0x0057 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0057 }
            r4 = r2[r4]     // Catch:{ Throwable -> 0x0057 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x0057 }
            r2 = r2[r3]     // Catch:{ Throwable -> 0x0057 }
            java.lang.Integer r2 = (java.lang.Integer) r2     // Catch:{ Throwable -> 0x0057 }
            int r2 = r2.intValue()     // Catch:{ Throwable -> 0x0057 }
            dalvik.system.DexFile.loadDex(r0, r4, r2)     // Catch:{ Throwable -> 0x0057 }
        L_0x0057:
            java.util.concurrent.ConcurrentLinkedQueue<android.util.Pair<java.lang.Integer, java.lang.Object>> r0 = sDelayedDexs     // Catch:{ all -> 0x0063 }
            java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x0063 }
            goto L_0x0019
        L_0x005e:
            monitor-exit(r7)
            return
        L_0x0060:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0060 }
            throw r1     // Catch:{ all -> 0x0063 }
        L_0x0063:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.service.UCDexImpl.run():void");
    }
}
