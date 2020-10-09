package com.uc.webview.export.internal.utility;

import android.content.Context;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.g;
import java.io.File;
import java.util.Stack;

/* compiled from: U4Source */
public final class e {
    public static long a = 2;
    private static final String b = "e";
    private static long c = 1;
    private static long d = 4;
    private static long e = 8;
    private static long f = 16;
    private static String g = "com.eg.android.AlipayGphone";
    private static long h;
    private static long i;
    private static long j;

    /* compiled from: U4Source */
    public static class a {
        public static long a = 1;
        public static long b = 2;
        public static long c = 4;
        public static long d = 8;
        public static long e = 16;
        public static long f = 32;
        public static long g = 64;
        public static long h = 128;
        public static long i = 256;
        public static long j = 512;
        public static long k = 1024;
        public static long l = 2048;
        public static long m = 4096;
        public static long n = 8192;
        public static long o = 16384;
        public static long p = 32768;
        public static long q = 65536;
        public static long r = 131072;
        public long s = 0;

        public final void a(long j2) {
            this.s = j2 | this.s;
        }
    }

    public static void a(String str) {
        if (!k.a(str)) {
            try {
                IGlobalSettings e2 = SDKFactory.e();
                if (e2 != null) {
                    e2.setStringValue("LoadShareCoreHost", str);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private static boolean a(String str, String str2, a aVar) {
        if (k.a(str) || k.a(str2)) {
            if (aVar != null) {
                aVar.a(a.c);
            }
            return false;
        }
        for (String str3 : str.split(CDParamKeys.CD_VALUE_STRING_SPLITER)) {
            if (str2.equals(str3) || str2.matches(str3)) {
                return true;
            }
        }
        if (aVar != null) {
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDK_VERSION_CHECK_FAILURE);
        }
        if (aVar != null) {
            aVar.a(a.d);
        }
        return false;
    }

    private static long d(String str) {
        long j2 = 0;
        if (k.a(str)) {
            return 0;
        }
        String[] split = str.split("\\.");
        for (int i2 = 0; i2 < split.length; i2++) {
            j2 += (long) Integer.parseInt(split[i2]);
            if (i2 < split.length - 1) {
                j2 *= 100;
            }
        }
        return j2;
    }

    private static int a(String str, String str2) {
        long d2 = d(str);
        long d3 = d(str2);
        if (d2 > d3) {
            return 1;
        }
        return d2 == d3 ? 0 : -1;
    }

    public static String b(String str) {
        int min;
        if (k.a(str)) {
            return str;
        }
        try {
            String replaceAll = str.replaceAll("Exception", "E");
            try {
                int indexOf = replaceAll.indexOf(":");
                int i2 = -1;
                if (indexOf >= 0) {
                    i2 = replaceAll.lastIndexOf(".", indexOf);
                }
                if (i2 >= 0 && (min = Math.min(i2 + 30, replaceAll.length())) > i2) {
                    return replaceAll.substring(i2, min);
                }
            } catch (Throwable unused) {
            }
            return replaceAll;
        } catch (Throwable unused2) {
            return str;
        }
    }

    public static long a(Context context) {
        long j2;
        long j3 = a;
        try {
            String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_COMMONALITY_TARGET_FPATH);
            if (k.a(param)) {
                j2 = d;
            } else if (context.getPackageManager().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()) != 0) {
                j2 = e;
            } else {
                File file = new File(param, "uws");
                UCCyclone.expectCreateDirFile(file);
                if (file.exists()) {
                    return j3;
                }
                j2 = f;
            }
            return j2;
        } catch (Throwable th) {
            Log.d(b, ".sdcardAuthority", th);
            return j3;
        }
    }

    public static boolean b(Context context) {
        return a(context) == a;
    }

    public static String a() {
        return new File(UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_COMMONALITY_TARGET_FPATH), "uws").getAbsolutePath();
    }

    public static File a(File file, String str) {
        return k.b(file, k.e(str));
    }

    public static File c(String str) {
        return a(new File(a()), str);
    }

    public static boolean c(Context context) {
        return g.equals(context.getPackageName());
    }

    public static boolean a(File file) {
        return file.getAbsolutePath().contains("uws") && file.getAbsolutePath().contains(k.e(g));
    }

    public static boolean a(Context context, File file, a aVar) {
        return g.a(file.getAbsolutePath(), context, context, "com.UCMobile", new g.b("sc_cvsv"), aVar);
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ad A[SYNTHETIC, Splitter:B:39:0x00ad] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b4 A[Catch:{ all -> 0x00a8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00bb A[Catch:{ all -> 0x00a8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00cb A[SYNTHETIC, Splitter:B:46:0x00cb] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00d1 A[Catch:{ Throwable -> 0x00d5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String b(android.content.Context r8, java.io.File r9, com.uc.webview.export.internal.utility.e.a r10) {
        /*
            r0 = 0
            java.lang.String r1 = "sdk_shell"
            java.lang.String r9 = com.uc.webview.export.internal.utility.k.a((java.io.File) r9, (java.lang.String) r1)     // Catch:{ Throwable -> 0x00d5 }
            boolean r1 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r9)     // Catch:{ Throwable -> 0x00d5 }
            r2 = 0
            if (r1 == 0) goto L_0x0028
            if (r10 == 0) goto L_0x0015
            long r3 = com.uc.webview.export.internal.utility.e.a.e     // Catch:{ Throwable -> 0x00d5 }
            r10.a(r3)     // Catch:{ Throwable -> 0x00d5 }
        L_0x0015:
            com.uc.webview.export.cyclone.UCKnownException r8 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x00d5 }
            r1 = 8004(0x1f44, float:1.1216E-41)
            java.lang.String r3 = "[%s] no found after UCCyclone.decompress."
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x00d5 }
            r4[r2] = r9     // Catch:{ Throwable -> 0x00d5 }
            java.lang.String r9 = java.lang.String.format(r3, r4)     // Catch:{ Throwable -> 0x00d5 }
            r8.<init>((int) r1, (java.lang.String) r9)     // Catch:{ Throwable -> 0x00d5 }
            throw r8     // Catch:{ Throwable -> 0x00d5 }
        L_0x0028:
            if (r10 == 0) goto L_0x0038
            java.lang.String r1 = "csc_vvfgscl"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)     // Catch:{ Throwable -> 0x0034, all -> 0x0030 }
            goto L_0x0038
        L_0x0030:
            r8 = move-exception
            r1 = r0
            goto L_0x00c9
        L_0x0034:
            r8 = move-exception
            r1 = r0
            goto L_0x00ab
        L_0x0038:
            r1 = r0
        L_0x0039:
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x00aa }
            java.io.File r4 = r8.getCacheDir()     // Catch:{ Throwable -> 0x00aa }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00aa }
            java.lang.String r6 = "temp_dex_verify_"
            r5.<init>(r6)     // Catch:{ Throwable -> 0x00aa }
            int r6 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x00aa }
            r5.append(r6)     // Catch:{ Throwable -> 0x00aa }
            java.lang.String r6 = "_"
            r5.append(r6)     // Catch:{ Throwable -> 0x00aa }
            int r6 = android.os.Process.myTid()     // Catch:{ Throwable -> 0x00aa }
            r5.append(r6)     // Catch:{ Throwable -> 0x00aa }
            java.lang.String r6 = "_"
            r5.append(r6)     // Catch:{ Throwable -> 0x00aa }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00aa }
            java.lang.String r6 = java.lang.String.valueOf(r6)     // Catch:{ Throwable -> 0x00aa }
            r5.append(r6)     // Catch:{ Throwable -> 0x00aa }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x00aa }
            r3.<init>(r4, r5)     // Catch:{ Throwable -> 0x00aa }
            boolean r1 = r3.exists()     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            if (r1 != 0) goto L_0x00a0
            com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r3)     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            dalvik.system.DexClassLoader r8 = new dalvik.system.DexClassLoader     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            java.lang.String r1 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            java.lang.String r4 = ""
            java.lang.Class<com.uc.webview.export.internal.utility.e> r5 = com.uc.webview.export.internal.utility.e.class
            java.lang.ClassLoader r5 = r5.getClassLoader()     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            r8.<init>(r9, r1, r4, r5)     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            if (r10 == 0) goto L_0x0091
            java.lang.String r9 = "csc_vvfdscl"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r9)     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
        L_0x0091:
            if (r10 == 0) goto L_0x0098
            long r4 = com.uc.webview.export.internal.utility.e.a.r     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            r10.a(r4)     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
        L_0x0098:
            java.lang.String r8 = com.uc.webview.export.internal.utility.k.b((java.lang.ClassLoader) r8)     // Catch:{ Throwable -> 0x00a5, all -> 0x00a2 }
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r3, r2, r0)     // Catch:{ Throwable -> 0x00d5 }
            return r8
        L_0x00a0:
            r1 = r3
            goto L_0x0039
        L_0x00a2:
            r8 = move-exception
            r1 = r3
            goto L_0x00c9
        L_0x00a5:
            r8 = move-exception
            r1 = r3
            goto L_0x00ab
        L_0x00a8:
            r8 = move-exception
            goto L_0x00c9
        L_0x00aa:
            r8 = move-exception
        L_0x00ab:
            if (r10 == 0) goto L_0x00b2
            long r3 = com.uc.webview.export.internal.utility.e.a.h     // Catch:{ all -> 0x00a8 }
            r10.a(r3)     // Catch:{ all -> 0x00a8 }
        L_0x00b2:
            if (r10 == 0) goto L_0x00b9
            java.lang.String r9 = "csc_vvfexc"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r9)     // Catch:{ all -> 0x00a8 }
        L_0x00b9:
            if (r10 == 0) goto L_0x00cf
            java.lang.String r9 = "csc_vvfexc_v"
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00a8 }
            java.lang.String r8 = b((java.lang.String) r8)     // Catch:{ all -> 0x00a8 }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r9, r8)     // Catch:{ all -> 0x00a8 }
            goto L_0x00cf
        L_0x00c9:
            if (r1 == 0) goto L_0x00ce
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r1, r2, r0)     // Catch:{ Throwable -> 0x00d5 }
        L_0x00ce:
            throw r8     // Catch:{ Throwable -> 0x00d5 }
        L_0x00cf:
            if (r1 == 0) goto L_0x00fa
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r1, r2, r0)     // Catch:{ Throwable -> 0x00d5 }
            goto L_0x00fa
        L_0x00d5:
            r8 = move-exception
            if (r10 == 0) goto L_0x00dd
            long r1 = com.uc.webview.export.internal.utility.e.a.g
            r10.a(r1)
        L_0x00dd:
            if (r10 == 0) goto L_0x00e4
            java.lang.String r9 = "csc_vvferr"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r9)
        L_0x00e4:
            if (r10 == 0) goto L_0x00f3
            java.lang.String r9 = "csc_vvferr_v"
            java.lang.String r10 = r8.toString()
            java.lang.String r10 = b((java.lang.String) r10)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r9, r10)
        L_0x00f3:
            java.lang.String r9 = b
            java.lang.String r10 = ".getCoreCompressFileVersion"
            com.uc.webview.export.internal.utility.Log.d(r9, r10, r8)
        L_0x00fa:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.e.b(android.content.Context, java.io.File, com.uc.webview.export.internal.utility.e$a):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0110 A[Catch:{ all -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0117 A[Catch:{ all -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x011e A[Catch:{ all -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0133  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.content.Context r11, java.io.File r12, java.lang.String r13, com.uc.webview.export.internal.utility.e.a r14) {
        /*
            java.lang.String r0 = b
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".getLegalVersionsFromCoreCompressFile "
            r1.<init>(r2)
            java.lang.String r2 = r12.getAbsolutePath()
            r1.append(r2)
            java.lang.String r2 = ", "
            r1.append(r2)
            r1.append(r13)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            r0 = 0
            r1 = r0
        L_0x0021:
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0106 }
            java.io.File r4 = r11.getCacheDir()     // Catch:{ Throwable -> 0x0106 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0106 }
            java.lang.String r6 = "temp_dec_core_"
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0106 }
            int r6 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0106 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0106 }
            java.lang.String r6 = "_"
            r5.append(r6)     // Catch:{ Throwable -> 0x0106 }
            int r6 = android.os.Process.myTid()     // Catch:{ Throwable -> 0x0106 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0106 }
            java.lang.String r6 = "_"
            r5.append(r6)     // Catch:{ Throwable -> 0x0106 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0106 }
            java.lang.String r6 = java.lang.String.valueOf(r6)     // Catch:{ Throwable -> 0x0106 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0106 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0106 }
            r3.<init>(r4, r5)     // Catch:{ Throwable -> 0x0106 }
            boolean r1 = r3.exists()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            if (r1 != 0) goto L_0x00fb
            com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r3)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r1 = r12.getAbsolutePath()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            boolean r1 = com.uc.webview.export.cyclone.UCCyclone.detectZipByFileType(r1)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r4 = 1
            r5 = r1 ^ 1
            java.lang.String r7 = r12.getAbsolutePath()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r8 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r9 = "sdk_shell"
            com.uc.webview.export.internal.utility.f r10 = new com.uc.webview.export.internal.utility.f     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r10.<init>()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r6 = r11
            com.uc.webview.export.cyclone.UCCyclone.decompress(r5, r6, r7, r8, r9, r10)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            if (r14 == 0) goto L_0x0087
            java.lang.String r12 = "csc_vvfdecs"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r12)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
        L_0x0087:
            if (r14 == 0) goto L_0x008e
            long r5 = com.uc.webview.export.internal.utility.e.a.q     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r14.a(r5)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
        L_0x008e:
            java.lang.String r12 = b(r11, r3, r14)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            boolean r1 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r12)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            if (r1 == 0) goto L_0x00a1
            if (r14 == 0) goto L_0x009f
            long r11 = com.uc.webview.export.internal.utility.e.a.b     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r14.a(r11)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
        L_0x009f:
            r12 = r0
            goto L_0x00f7
        L_0x00a1:
            if (r14 == 0) goto L_0x00a8
            java.lang.String r1 = "csc_vval"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r12)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
        L_0x00a8:
            java.lang.String r1 = b     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r6 = ".getLegalVersionsFromCoreDir ucmVersion: "
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r5.append(r12)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r6 = " of "
            r5.append(r6)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r6 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r5.append(r6)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            com.uc.webview.export.internal.utility.Log.d(r1, r5)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            boolean r13 = a((java.lang.String) r13, (java.lang.String) r12, (com.uc.webview.export.internal.utility.e.a) r14)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            if (r13 == 0) goto L_0x009f
            java.lang.String r13 = "sdk_shell"
            java.lang.String r13 = com.uc.webview.export.internal.utility.k.a((java.io.File) r3, (java.lang.String) r13)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r1.<init>(r13)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            boolean r11 = a((android.content.Context) r11, (java.io.File) r1, (com.uc.webview.export.internal.utility.e.a) r14)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            if (r11 != 0) goto L_0x00f7
            if (r14 == 0) goto L_0x00e5
            long r11 = com.uc.webview.export.internal.utility.e.a.f     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r14.a(r11)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
        L_0x00e5:
            com.uc.webview.export.cyclone.UCKnownException r11 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r12 = 8005(0x1f45, float:1.1217E-41)
            java.lang.String r1 = "[%s] verify failure."
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r4[r2] = r13     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            java.lang.String r13 = java.lang.String.format(r1, r4)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            r11.<init>((int) r12, (java.lang.String) r13)     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
            throw r11     // Catch:{ Throwable -> 0x0100, all -> 0x00fe }
        L_0x00f7:
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r3, r2, r0)
            return r12
        L_0x00fb:
            r1 = r3
            goto L_0x0021
        L_0x00fe:
            r11 = move-exception
            goto L_0x0131
        L_0x0100:
            r11 = move-exception
            r1 = r3
            goto L_0x0107
        L_0x0103:
            r11 = move-exception
            r3 = r1
            goto L_0x0131
        L_0x0106:
            r11 = move-exception
        L_0x0107:
            java.lang.String r12 = b     // Catch:{ all -> 0x0103 }
            java.lang.String r13 = ".getLegalVersionsFromCoreCompressFile"
            com.uc.webview.export.internal.utility.Log.d(r12, r13, r11)     // Catch:{ all -> 0x0103 }
            if (r14 == 0) goto L_0x0115
            long r12 = com.uc.webview.export.internal.utility.e.a.a     // Catch:{ all -> 0x0103 }
            r14.a(r12)     // Catch:{ all -> 0x0103 }
        L_0x0115:
            if (r14 == 0) goto L_0x011c
            java.lang.String r12 = "csc_vvfdece"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r12)     // Catch:{ all -> 0x0103 }
        L_0x011c:
            if (r14 == 0) goto L_0x012b
            java.lang.String r12 = "csc_vvfdece_v"
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0103 }
            java.lang.String r11 = b((java.lang.String) r11)     // Catch:{ all -> 0x0103 }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r12, r11)     // Catch:{ all -> 0x0103 }
        L_0x012b:
            if (r1 == 0) goto L_0x0130
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r1, r2, r0)
        L_0x0130:
            return r0
        L_0x0131:
            if (r3 == 0) goto L_0x0136
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r3, r2, r0)
        L_0x0136:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.e.a(android.content.Context, java.io.File, java.lang.String, com.uc.webview.export.internal.utility.e$a):java.lang.String");
    }

    public static String d(Context context) {
        String str;
        try {
            File[] listFiles = k.a(context, "decompresses2").listFiles();
            if (listFiles.length == 0) {
                return null;
            }
            int length = listFiles.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    str = null;
                    break;
                }
                File file = listFiles[i2];
                if (file.isDirectory()) {
                    str = k.a(file, "sdk_shell");
                    if (!k.a(str)) {
                        if (!k.a(k.a(file, UCCyclone.DecFileOrign.DecFileOrignFlag + UCCyclone.DecFileOrign.Sdcard_Share_Core))) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                i2++;
            }
            if (k.a(str)) {
                return null;
            }
            return new File(str).getParent();
        } catch (Throwable th) {
            Log.d(b, ".getLocationDecDir ", th);
            return null;
        }
    }

    static {
        long j2 = c << 1;
        h = j2;
        long j3 = j2 << 1;
        i = j3;
        j = j3 << 1;
    }

    public static String e(Context context) {
        Context context2 = context;
        long j2 = h;
        a aVar = null;
        try {
            if (!b(context)) {
                Log.d(b, ".getSdcardShareCoreDecFilePath Sdcard配置及权限校验失败");
                long j3 = i;
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_GET_CORE_DEC_FILE_PATH, Long.toString(j3));
                Log.d(b, ".getSdcardShareCoreDecFilePath fStat: " + j3);
                return null;
            }
            String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_SPECIAL_HOST_PKG_NAME_LIST);
            if (k.a(param)) {
                long j4 = j;
                try {
                    Log.d(b, ".getSdcardShareCoreDecFilePath CDKeys.CD_KEY_SHARE_CORE_HOST_PKG_NAME_LIST配置为空");
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_GET_CORE_DEC_FILE_PATH, Long.toString(j4));
                    Log.d(b, ".getSdcardShareCoreDecFilePath fStat: " + j4);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    j2 = j4;
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_GET_CORE_DEC_FILE_PATH, Long.toString(j2));
                    Log.d(b, ".getSdcardShareCoreDecFilePath fStat: " + j2);
                    throw th;
                }
            } else {
                String[] split = param.split(CDParamKeys.CD_VALUE_STRING_SPLITER);
                Stack stack = new Stack();
                int length = split.length;
                String str = null;
                int i2 = 0;
                while (i2 < length) {
                    String str2 = split[i2];
                    if (!k.a(str2)) {
                        File c2 = c(str2);
                        if (!c2.exists()) {
                            Log.d(b, ".getSdcardShareCoreDecFilePath " + c2.getAbsolutePath() + " not exists.");
                        } else {
                            File[] listFiles = c2.listFiles();
                            if (listFiles != null) {
                                if (listFiles.length != 0) {
                                    int length2 = listFiles.length;
                                    String str3 = str;
                                    int i3 = 0;
                                    while (i3 < length2) {
                                        File file = listFiles[i3];
                                        Log.d(b, ".getSdcardShareCoreDecFilePath coreFile: " + file.getAbsolutePath());
                                        String a2 = a(context2, file, UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_UCM_VERSIONS), aVar);
                                        if (k.a(a2)) {
                                            Log.d(b, ".getSdcardShareCoreDecFilePath version is empty.");
                                        } else if (s.a(file.getAbsolutePath(), context2)) {
                                            Log.d(b, ".getSdcardShareCoreDecFilePath " + file.getAbsolutePath() + " once shared.");
                                            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SKIP_ONCE_VERIFY_CORE_FILE);
                                        } else {
                                            Log.d(b, ".getSdcardShareCoreDecFilePath version : " + a2);
                                            if (a(str3, a2) < 0) {
                                                stack.push(file);
                                                str3 = a2;
                                            }
                                        }
                                        i3++;
                                        aVar = null;
                                    }
                                    str = str3;
                                }
                            }
                            Log.d(b, ".getSdcardShareCoreDecFilePath " + c2.getAbsolutePath() + " empty.");
                        }
                    }
                    i2++;
                    aVar = null;
                }
                while (!stack.empty()) {
                    File file2 = (File) stack.pop();
                    if (UCCyclone.detectZipByFileType(file2.getAbsolutePath()) && !a(file2)) {
                        if (!g.a(file2.getAbsolutePath(), context2, context2, "com.UCMobile", new g.b("sc_cvsv"))) {
                            Log.d(b, ".getSdcardShareCoreDecFilePath verifySignature failure!");
                        }
                    }
                    s.a(file2.getAbsolutePath(), context2, true);
                    String absolutePath = file2.getAbsolutePath();
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_GET_CORE_DEC_FILE_PATH, Long.toString(j2));
                    Log.d(b, ".getSdcardShareCoreDecFilePath fStat: " + j2);
                    return absolutePath;
                }
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_GET_CORE_DEC_FILE_PATH, Long.toString(j2));
                Log.d(b, ".getSdcardShareCoreDecFilePath fStat: " + j2);
                return null;
            }
        } catch (Throwable th2) {
            th = th2;
            Log.d(b, ".getSdcardShareCoreDecFilePath", th);
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_GET_CORE_DEC_FILE_PATH, Long.toString(j2));
            Log.d(b, ".getSdcardShareCoreDecFilePath fStat: " + j2);
            return null;
        }
    }

    public static void f(Context context) {
        String d2 = d(context);
        File a2 = k.a(context, "decompresses2");
        File file = new File(d2);
        Log.d(b, ".deleteShareCoreDecompressDir decRootDir:" + a2);
        Log.d(b, ".deleteShareCoreDecompressDir scDecDir:" + file);
        File file2 = file;
        int i2 = 5;
        while (true) {
            Log.d(b, ".deleteShareCoreDecompressDir scParentDir:" + file);
            if (file.getAbsolutePath().equals(a2.getAbsolutePath())) {
                Log.d(b, ".deleteShareCoreDecompressDir delete share core decompress dir.");
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_DELETE_DEC_DIR);
                UCCyclone.recursiveDelete(file2, false, (Object) null);
                return;
            }
            File parentFile = file2.getParentFile();
            i2--;
            if (i2 > 0) {
                File file3 = file;
                file = parentFile;
                file2 = file3;
            } else {
                return;
            }
        }
    }
}
