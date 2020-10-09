package com.alibaba.wireless.security.framework;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge;
import com.alibaba.wireless.security.framework.utils.b;
import com.alibaba.wireless.security.framework.utils.c;
import com.alibaba.wireless.security.framework.utils.f;
import com.alibaba.wireless.security.open.SecException;
import com.taobao.weex.el.parse.Operators;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class d implements ISGPluginManager {
    private static String[] n = {"armeabi", "armeabi-v7a", "x86", "arm64-v8a", "x86_64"};
    private static String o = null;
    private static volatile boolean p = true;
    HashMap<Class, Object> a = new HashMap<>();
    c b = null;
    /* access modifiers changed from: private */
    public Context c;
    private final HashMap<String, c> d = new HashMap<>();
    private IRouterComponent e = null;
    private boolean f = true;
    private String g = null;
    private String h = null;
    private b i = null;
    private boolean j = false;
    private File k = null;
    private File l = null;
    private File m = null;

    private static class a {
        File a;
        File b;
        File c;
        boolean d;

        public a(File file, File file2, File file3, boolean z) {
            this.a = file;
            this.b = file2;
            this.c = file3;
            this.d = z;
        }
    }

    private int a(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int length = split.length < split2.length ? split.length : split2.length;
        for (int i2 = 0; i2 < length; i2++) {
            int parseInt = Integer.parseInt(split[i2]);
            int parseInt2 = Integer.parseInt(split2[i2]);
            if (parseInt > parseInt2) {
                return 1;
            }
            if (parseInt < parseInt2) {
                return -1;
            }
        }
        return 0;
    }

    private PackageInfo a(String str, a aVar, String str2) throws SecException {
        PackageInfo packageInfo;
        try {
            packageInfo = this.c.getPackageManager().getPackageArchiveInfo(aVar.a.getAbsolutePath(), 133);
        } catch (Throwable th) {
            a(100043, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "getPackageArchiveInfo failed", "" + th, c(aVar.a.getAbsolutePath() + ""), aVar.c != null ? c(aVar.c.getAbsolutePath()) : "", str2);
            if (aVar.a.exists() && !b(aVar.a)) {
                aVar.a.delete();
            }
            packageInfo = null;
        }
        if (packageInfo != null) {
            return packageInfo;
        }
        a(100043, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "packageInfo == null", str + Operators.ARRAY_START_STR + str2 + Operators.ARRAY_END_STR, c(aVar.a.getAbsolutePath()), aVar.c != null ? c(aVar.c.getAbsolutePath()) : "", c());
        throw new SecException(SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:158:0x04f9, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x04fa, code lost:
        r11 = r5;
        r15 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x051e, code lost:
        r5 = r10.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x052d, code lost:
        if (r35.length() == 0) goto L_0x052f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x052f, code lost:
        r6 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0530, code lost:
        r11 = r33;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0533, code lost:
        r6 = r12 + "->" + r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x054b, code lost:
        if (r11.c != null) goto L_0x054d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x054d, code lost:
        r1 = new java.lang.StringBuilder();
        r1.append("src:");
        r1.append(c(r11.c.getAbsolutePath()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0565, code lost:
        r1 = new java.lang.StringBuilder();
        r1.append("zipfile:");
        r1.append(c(r11.a.getAbsolutePath()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x057d, code lost:
        a(100043, 103, "", r5, r6, r1.toString(), c(r13.getAbsolutePath()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x0591, code lost:
        if (r11.d == false) goto L_0x0593;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x0593, code lost:
        r9.b.a();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x059c, code lost:
        if (r13.exists() != false) goto L_0x059e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x059e, code lost:
        r13.delete();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x05a3, code lost:
        if (r11.d == false) goto L_0x05a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x05a5, code lost:
        r9.b.b();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x05b7, code lost:
        a(100043, com.alibaba.wireless.security.SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "native exception occurred", r10.toString(), "soName=" + r11 + ", authCode=" + r9.h + ", errorCode=" + r10.getErrorCode(), c(r33.a.getAbsolutePath()), c(r13.getAbsolutePath()));
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x051e A[Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x05b7 A[Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ad A[SYNTHETIC, Splitter:B:37:0x00ad] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00f1 A[Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0107 A[ADDED_TO_REGION, Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01b8 A[Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x01bd A[Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x01da A[Catch:{ SecException -> 0x0618, all -> 0x05ab, all -> 0x0684 }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0249 A[SYNTHETIC, Splitter:B:68:0x0249] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.alibaba.wireless.security.framework.c a(java.lang.String r32, com.alibaba.wireless.security.framework.d.a r33, android.content.Context r34, java.lang.String r35) throws com.alibaba.wireless.security.open.SecException {
        /*
            r31 = this;
            r9 = r31
            r10 = r32
            r11 = r33
            r12 = r35
            java.io.File r1 = r11.a
            java.lang.String r13 = r1.getAbsolutePath()
            java.io.File r1 = r11.b
            java.lang.String r14 = r1.getAbsolutePath()
            r15 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r15)
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r15)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r14)
            java.lang.String r4 = java.io.File.separator
            r3.append(r4)
            r3.append(r10)
            java.lang.String r4 = "_"
            r3.append(r4)
            java.io.File r4 = r11.a
            long r4 = r4.lastModified()
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            r3.append(r4)
            java.lang.String r4 = ".pkgInfo"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.alibaba.wireless.security.framework.a r8 = new com.alibaba.wireless.security.framework.a
            r8.<init>(r3)
            r16 = 0
            boolean r3 = r8.a()     // Catch:{ all -> 0x0684 }
            r18 = 1
            r19 = 0
            if (r3 == 0) goto L_0x00a5
            android.content.Context r3 = r9.c     // Catch:{ all -> 0x0684 }
            boolean r3 = com.alibaba.wireless.security.framework.utils.f.a(r3)     // Catch:{ all -> 0x0684 }
            if (r3 != 0) goto L_0x00a5
            org.json.JSONObject r3 = r8.b()     // Catch:{ Exception | JSONException -> 0x009f }
            java.lang.String r4 = "version"
            java.lang.String r4 = r3.getString(r4)     // Catch:{ Exception | JSONException -> 0x009f }
            java.lang.String r5 = "dependencies"
            java.lang.String r5 = r3.getString(r5)     // Catch:{ Exception | JSONException -> 0x009b }
            java.lang.String r6 = "hasso"
            java.lang.String r6 = r3.getString(r6)     // Catch:{ Exception | JSONException -> 0x0098 }
            boolean r6 = java.lang.Boolean.parseBoolean(r6)     // Catch:{ Exception | JSONException -> 0x0098 }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch:{ Exception | JSONException -> 0x0098 }
            java.lang.String r1 = "pluginclass"
            java.lang.String r1 = r3.getString(r1)     // Catch:{ JSONException -> 0x0097, Exception -> 0x0095 }
            java.lang.String r7 = "thirdpartyso"
            boolean r3 = r3.getBoolean(r7)     // Catch:{ Exception | JSONException -> 0x0092 }
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception | JSONException -> 0x0092 }
            r2 = r3
            r3 = r1
            r1 = 1
            goto L_0x00ab
        L_0x0092:
            r3 = r1
            r1 = r6
            goto L_0x00a3
        L_0x0095:
            r1 = r6
            goto L_0x0098
        L_0x0097:
            r1 = r6
        L_0x0098:
            r3 = r19
            goto L_0x00a3
        L_0x009b:
            r3 = r19
            r5 = r3
            goto L_0x00a3
        L_0x009f:
            r3 = r19
            r4 = r3
            r5 = r4
        L_0x00a3:
            r6 = r1
            goto L_0x00aa
        L_0x00a5:
            r6 = r1
            r3 = r19
            r4 = r3
            r5 = r4
        L_0x00aa:
            r1 = 0
        L_0x00ab:
            if (r1 != 0) goto L_0x00f1
            android.content.pm.PackageInfo r1 = r9.a((java.lang.String) r10, (com.alibaba.wireless.security.framework.d.a) r11, (java.lang.String) r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r4 = r1.versionName     // Catch:{ all -> 0x0684 }
            android.content.pm.ApplicationInfo r2 = r1.applicationInfo     // Catch:{ all -> 0x0684 }
            android.os.Bundle r2 = r2.metaData     // Catch:{ all -> 0x0684 }
            java.lang.String r3 = "dependencies"
            java.lang.String r2 = r2.getString(r3)     // Catch:{ all -> 0x0684 }
            android.content.pm.ApplicationInfo r3 = r1.applicationInfo     // Catch:{ all -> 0x0684 }
            android.os.Bundle r3 = r3.metaData     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "hasso"
            boolean r3 = r3.getBoolean(r5, r15)     // Catch:{ all -> 0x0684 }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x0684 }
            android.content.pm.ApplicationInfo r3 = r1.applicationInfo     // Catch:{ all -> 0x0684 }
            android.os.Bundle r3 = r3.metaData     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "pluginclass"
            java.lang.String r3 = r3.getString(r5)     // Catch:{ all -> 0x0684 }
            android.content.pm.ApplicationInfo r5 = r1.applicationInfo     // Catch:{ all -> 0x0684 }
            android.os.Bundle r5 = r5.metaData     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = "thirdpartyso"
            boolean r5 = r5.getBoolean(r7, r15)     // Catch:{ all -> 0x0684 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ all -> 0x0684 }
            r8.a = r1     // Catch:{ all -> 0x0684 }
            r8.a(r1, r10)     // Catch:{ all -> 0x0684 }
            r21 = r3
            r7 = r4
            r22 = r5
            r20 = r6
            r6 = r2
            goto L_0x00f9
        L_0x00f1:
            r22 = r2
            r21 = r3
            r7 = r4
            r20 = r6
            r6 = r5
        L_0x00f9:
            android.content.Context r1 = r9.c     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.getPackageName()     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "com.eg.android.AlipayGphone"
            boolean r1 = r2.equals(r1)     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x0192
            if (r7 == 0) goto L_0x0192
            java.lang.String r1 = "6.4.3448869"
            boolean r1 = r7.contains(r1)     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x0192
            r2 = 100088(0x186f8, float:1.40253E-40)
            java.lang.String r4 = android.os.Build.FINGERPRINT     // Catch:{ all -> 0x0684 }
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x0121
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            goto L_0x0123
        L_0x0121:
            java.lang.String r1 = ""
        L_0x0123:
            r5 = r1
            java.lang.Class<com.alibaba.wireless.security.framework.d> r1 = com.alibaba.wireless.security.framework.d.class
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ all -> 0x0684 }
            java.lang.String r23 = r1.toString()     // Catch:{ all -> 0x0684 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r13)     // Catch:{ all -> 0x0684 }
            java.lang.String r15 = "("
            r1.append(r15)     // Catch:{ all -> 0x0684 }
            java.io.File r15 = r11.a     // Catch:{ all -> 0x0684 }
            r25 = r4
            long r3 = r15.getTotalSpace()     // Catch:{ all -> 0x0684 }
            r1.append(r3)     // Catch:{ all -> 0x0684 }
            java.lang.String r3 = "),"
            r1.append(r3)     // Catch:{ all -> 0x0684 }
            r1.append(r14)     // Catch:{ all -> 0x0684 }
            java.lang.String r15 = r1.toString()     // Catch:{ all -> 0x0684 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            android.content.Context r3 = r9.c     // Catch:{ all -> 0x0684 }
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfo()     // Catch:{ all -> 0x0684 }
            java.lang.String r3 = r3.sourceDir     // Catch:{ all -> 0x0684 }
            r1.append(r3)     // Catch:{ all -> 0x0684 }
            java.lang.String r3 = ","
            r1.append(r3)     // Catch:{ all -> 0x0684 }
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x0684 }
            android.content.Context r4 = r9.c     // Catch:{ all -> 0x0684 }
            android.content.pm.ApplicationInfo r4 = r4.getApplicationInfo()     // Catch:{ all -> 0x0684 }
            java.lang.String r4 = r4.sourceDir     // Catch:{ all -> 0x0684 }
            r3.<init>(r4)     // Catch:{ all -> 0x0684 }
            long r3 = r3.lastModified()     // Catch:{ all -> 0x0684 }
            r1.append(r3)     // Catch:{ all -> 0x0684 }
            java.lang.String r26 = r1.toString()     // Catch:{ all -> 0x0684 }
            r1 = r31
            r3 = 2
            r4 = r25
            r27 = r6
            r6 = r23
            r28 = r7
            r7 = r15
            r15 = r8
            r8 = r26
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            goto L_0x0197
        L_0x0192:
            r27 = r6
            r28 = r7
            r15 = r8
        L_0x0197:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r10)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "("
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r8 = r28
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = ")"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r1.toString()     // Catch:{ all -> 0x0684 }
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x01bd
            r1 = r7
        L_0x01b9:
            r6 = r27
            r2 = 0
            goto L_0x01d2
        L_0x01bd:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "->"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            goto L_0x01b9
        L_0x01d2:
            boolean r1 = r9.a((java.lang.String) r6, (java.lang.String) r1, (boolean) r2)     // Catch:{ all -> 0x0684 }
            r5 = 199(0xc7, float:2.79E-43)
            if (r1 != 0) goto L_0x0249
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 199(0xc7, float:2.79E-43)
            java.lang.String r4 = "loadRequirements failed"
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x01e8
            goto L_0x01fd
        L_0x01e8:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = "->"
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r7 = r1
        L_0x01fd:
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x0219
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = "src:"
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            java.io.File r8 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = r8.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = r9.c((java.lang.String) r8)     // Catch:{ all -> 0x0684 }
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            goto L_0x0231
        L_0x0219:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = "zipfile:"
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            java.io.File r8 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = r8.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = r9.c((java.lang.String) r8)     // Catch:{ all -> 0x0684 }
            r1.append(r8)     // Catch:{ all -> 0x0684 }
        L_0x0231:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r8 = r1
            java.lang.String r10 = ""
            r1 = r31
            r11 = 199(0xc7, float:2.79E-43)
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r10
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            com.alibaba.wireless.security.open.SecException r1 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x0684 }
            r1.<init>(r11)     // Catch:{ all -> 0x0684 }
            throw r1     // Catch:{ all -> 0x0684 }
        L_0x0249:
            r9.b((java.lang.String) r10, (java.lang.String) r8)     // Catch:{ SecException -> 0x0618 }
            java.lang.String r1 = ""
            java.lang.String r2 = ""
            boolean r3 = r20.booleanValue()     // Catch:{ all -> 0x0684 }
            if (r3 == 0) goto L_0x0318
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "libsg"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r10)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "so-"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = ".so"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            java.lang.String r23 = r1.toString()     // Catch:{ all -> 0x0684 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "sg"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r10)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "so-"
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r8)     // Catch:{ all -> 0x0684 }
            java.lang.String r24 = r1.toString()     // Catch:{ all -> 0x0684 }
            java.io.File r4 = r11.c     // Catch:{ all -> 0x0684 }
            boolean r3 = r11.d     // Catch:{ all -> 0x0684 }
            r1 = r31
            r2 = r13
            r25 = r3
            r3 = r14
            r10 = 199(0xc7, float:2.79E-43)
            r5 = r23
            r26 = r6
            r6 = r25
            boolean r1 = r1.a((java.lang.String) r2, (java.lang.String) r3, (java.io.File) r4, (java.lang.String) r5, (boolean) r6)     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x0313
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 107(0x6b, float:1.5E-43)
            java.lang.String r4 = ""
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x02b4
            r6 = r7
            goto L_0x02c9
        L_0x02b4:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "->"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r6 = r1
        L_0x02c9:
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x02e5
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "src:"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            java.io.File r5 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r9.c((java.lang.String) r5)     // Catch:{ all -> 0x0684 }
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            goto L_0x02fd
        L_0x02e5:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "zipfile:"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            java.io.File r5 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r9.c((java.lang.String) r5)     // Catch:{ all -> 0x0684 }
            r1.append(r5)     // Catch:{ all -> 0x0684 }
        L_0x02fd:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r7 = r1
            java.lang.String r8 = ""
            r1 = r31
            r5 = r26
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            com.alibaba.wireless.security.open.SecException r1 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x0684 }
            r2 = 107(0x6b, float:1.5E-43)
            r1.<init>(r2)     // Catch:{ all -> 0x0684 }
            throw r1     // Catch:{ all -> 0x0684 }
        L_0x0313:
            r5 = r23
            r6 = r24
            goto L_0x031e
        L_0x0318:
            r26 = r6
            r10 = 199(0xc7, float:2.79E-43)
            r5 = r1
            r6 = r2
        L_0x031e:
            if (r21 != 0) goto L_0x038c
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 199(0xc7, float:2.79E-43)
            java.lang.String r4 = "miss pluginclass"
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x032f
            r6 = r7
            goto L_0x0344
        L_0x032f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "->"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r6 = r1
        L_0x0344:
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x0360
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "src:"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            java.io.File r5 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r9.c((java.lang.String) r5)     // Catch:{ all -> 0x0684 }
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            goto L_0x0378
        L_0x0360:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "zipfile:"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            java.io.File r5 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r9.c((java.lang.String) r5)     // Catch:{ all -> 0x0684 }
            r1.append(r5)     // Catch:{ all -> 0x0684 }
        L_0x0378:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r7 = r1
            java.lang.String r8 = ""
            r1 = r31
            r5 = r26
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            com.alibaba.wireless.security.open.SecException r1 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x0684 }
            r1.<init>(r10)     // Catch:{ all -> 0x0684 }
            throw r1     // Catch:{ all -> 0x0684 }
        L_0x038c:
            java.lang.String r4 = r21.trim()     // Catch:{ all -> 0x0684 }
            boolean r1 = r11.d     // Catch:{ all -> 0x0684 }
            java.lang.ClassLoader r3 = r9.b(r13, r14, r1)     // Catch:{ all -> 0x0684 }
            java.lang.Class r1 = r9.a((java.lang.ClassLoader) r3, (java.lang.String) r4)     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x0421
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = "load "
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r4)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = " failed from plugin "
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            com.alibaba.wireless.security.framework.utils.a.b(r1)     // Catch:{ all -> 0x0684 }
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 199(0xc7, float:2.79E-43)
            java.lang.String r5 = "clazz == null"
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x03c4
            r6 = r7
            goto L_0x03d9
        L_0x03c4:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r6 = "->"
            r1.append(r6)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r6 = r1
        L_0x03d9:
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x03f5
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = "src:"
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.io.File r7 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r7.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r9.c((java.lang.String) r7)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            goto L_0x040d
        L_0x03f5:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = "zipfile:"
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.io.File r7 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r7.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r9.c((java.lang.String) r7)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
        L_0x040d:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r7 = r1
            r1 = r31
            r8 = r4
            r4 = r5
            r5 = r26
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            com.alibaba.wireless.security.open.SecException r1 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x0684 }
            r1.<init>(r10)     // Catch:{ all -> 0x0684 }
            throw r1     // Catch:{ all -> 0x0684 }
        L_0x0421:
            java.lang.Object r1 = r1.newInstance()     // Catch:{ InstantiationException -> 0x060e, IllegalAccessException -> 0x0604, SecException -> 0x04f9 }
            r24 = r1
            com.alibaba.wireless.security.open.initialize.ISecurityGuardPlugin r24 = (com.alibaba.wireless.security.open.initialize.ISecurityGuardPlugin) r24     // Catch:{ InstantiationException -> 0x060e, IllegalAccessException -> 0x0604, SecException -> 0x04f9 }
            com.alibaba.wireless.security.framework.c r10 = new com.alibaba.wireless.security.framework.c     // Catch:{ InstantiationException -> 0x060e, IllegalAccessException -> 0x0604, SecException -> 0x04f9 }
            r1 = r10
            r2 = r13
            r13 = r3
            r3 = r31
            r4 = r32
            r11 = r5
            r5 = r13
            r30 = r6
            r6 = r15
            r15 = r7
            r7 = r24
            r1.<init>(r2, r3, r4, r5, r6, r7)     // Catch:{ InstantiationException -> 0x060e, IllegalAccessException -> 0x0604, SecException -> 0x04f7 }
            java.lang.String r1 = r31.getMainPluginName()     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r2 = r32
            boolean r1 = r2.equalsIgnoreCase(r1)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r3 = 3
            r4 = 2
            if (r1 == 0) goto L_0x04b7
            boolean r1 = r9.f     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r1 == 0) goto L_0x0451
            r1 = 1
            goto L_0x0452
        L_0x0451:
            r1 = 0
        L_0x0452:
            boolean r5 = r9.j     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r5 == 0) goto L_0x0458
            r1 = r1 | 2
        L_0x0458:
            java.lang.String r5 = r9.g     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r5 == 0) goto L_0x0466
            java.lang.String r5 = r9.g     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            int r5 = r5.length()     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r5 <= 0) goto L_0x0466
            r1 = r1 | 4
        L_0x0466:
            android.content.Context r5 = r9.c     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            boolean r5 = com.alibaba.wireless.security.framework.utils.f.a(r5)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r5 == 0) goto L_0x0470
            r1 = r1 | 8
        L_0x0470:
            android.content.Context r5 = r9.c     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            boolean r5 = com.alibaba.wireless.security.framework.utils.f.b(r5)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r5 == 0) goto L_0x047a
            r1 = r1 | 16
        L_0x047a:
            com.alibaba.wireless.security.framework.b r5 = r9.i     // Catch:{ Exception -> 0x048c }
            if (r5 == 0) goto L_0x0489
            com.alibaba.wireless.security.framework.b r5 = r9.i     // Catch:{ Exception -> 0x048c }
            org.json.JSONObject r5 = r5.a()     // Catch:{ Exception -> 0x048c }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x048c }
            goto L_0x048e
        L_0x0489:
            java.lang.String r5 = ""
            goto L_0x048e
        L_0x048c:
            java.lang.String r5 = ""
        L_0x048e:
            r26 = 0
            r6 = 4
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r7 = 0
            r6[r7] = r1     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r6[r18] = r5     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            java.io.File r1 = r9.l     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r6[r4] = r1     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            java.lang.String r1 = r9.h     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r6[r3] = r1     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r25 = r34
            r27 = r10
            r28 = r30
            r29 = r6
            com.alibaba.wireless.security.framework.IRouterComponent r1 = r24.onPluginLoaded(r25, r26, r27, r28, r29)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r9.e = r1     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            goto L_0x04cb
        L_0x04b7:
            com.alibaba.wireless.security.framework.SGPluginExtras.slot = r16     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            com.alibaba.wireless.security.framework.IRouterComponent r1 = r9.e     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r5 = 0
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r25 = r34
            r26 = r1
            r27 = r10
            r28 = r30
            r29 = r6
            r24.onPluginLoaded(r25, r26, r27, r28, r29)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
        L_0x04cb:
            boolean r1 = r20.booleanValue()     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r1 == 0) goto L_0x0615
            boolean r1 = r22.booleanValue()     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            if (r1 != 0) goto L_0x0615
            r1 = r30
            java.lang.String r1 = com.alibaba.wireless.security.framework.utils.f.a(r13, r1)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            com.alibaba.wireless.security.framework.IRouterComponent r5 = r9.e     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r6 = 10102(0x2776, float:1.4156E-41)
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r7 = 0
            r3[r7] = r2     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r3[r18] = r8     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r3[r4] = r1     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            r5.doCommand(r6, r3)     // Catch:{ InstantiationException -> 0x04f3, IllegalAccessException -> 0x04ef, SecException -> 0x04f7 }
            goto L_0x0615
        L_0x04ef:
            r0 = move-exception
            r1 = r0
            goto L_0x0608
        L_0x04f3:
            r0 = move-exception
            r1 = r0
            goto L_0x0612
        L_0x04f7:
            r0 = move-exception
            goto L_0x04fc
        L_0x04f9:
            r0 = move-exception
            r11 = r5
            r15 = r7
        L_0x04fc:
            r10 = r0
            java.io.File r13 = new java.io.File     // Catch:{ all -> 0x0684 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r14)     // Catch:{ all -> 0x0684 }
            java.lang.String r2 = java.io.File.separator     // Catch:{ all -> 0x0684 }
            r1.append(r2)     // Catch:{ all -> 0x0684 }
            r1.append(r11)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r13.<init>(r1)     // Catch:{ all -> 0x0684 }
            int r1 = r10.getErrorCode()     // Catch:{ all -> 0x0684 }
            r2 = 103(0x67, float:1.44E-43)
            if (r1 != r2) goto L_0x05b7
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 103(0x67, float:1.44E-43)
            java.lang.String r4 = ""
            java.lang.String r5 = r10.toString()     // Catch:{ all -> 0x0684 }
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x0533
            r6 = r15
        L_0x0530:
            r11 = r33
            goto L_0x0549
        L_0x0533:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r6 = "->"
            r1.append(r6)     // Catch:{ all -> 0x0684 }
            r1.append(r15)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r6 = r1
            goto L_0x0530
        L_0x0549:
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x0565
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = "src:"
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.io.File r7 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r7.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r9.c((java.lang.String) r7)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            goto L_0x057d
        L_0x0565:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = "zipfile:"
            r1.append(r7)     // Catch:{ all -> 0x0684 }
            java.io.File r7 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r7.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r9.c((java.lang.String) r7)     // Catch:{ all -> 0x0684 }
            r1.append(r7)     // Catch:{ all -> 0x0684 }
        L_0x057d:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r7 = r1
            java.lang.String r1 = r13.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = r9.c((java.lang.String) r1)     // Catch:{ all -> 0x0684 }
            r1 = r31
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            boolean r1 = r11.d     // Catch:{ all -> 0x05ab }
            if (r1 != 0) goto L_0x0598
            com.alibaba.wireless.security.framework.utils.c r1 = r9.b     // Catch:{ all -> 0x05ab }
            r1.a()     // Catch:{ all -> 0x05ab }
        L_0x0598:
            boolean r1 = r13.exists()     // Catch:{ all -> 0x05ab }
            if (r1 == 0) goto L_0x05a1
            r13.delete()     // Catch:{ all -> 0x05ab }
        L_0x05a1:
            boolean r1 = r11.d     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x0603
            com.alibaba.wireless.security.framework.utils.c r1 = r9.b     // Catch:{ all -> 0x0684 }
            r1.b()     // Catch:{ all -> 0x0684 }
            goto L_0x0603
        L_0x05ab:
            r0 = move-exception
            r1 = r0
            boolean r2 = r11.d     // Catch:{ all -> 0x0684 }
            if (r2 != 0) goto L_0x05b6
            com.alibaba.wireless.security.framework.utils.c r2 = r9.b     // Catch:{ all -> 0x0684 }
            r2.b()     // Catch:{ all -> 0x0684 }
        L_0x05b6:
            throw r1     // Catch:{ all -> 0x0684 }
        L_0x05b7:
            r1 = r11
            r11 = r33
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 199(0xc7, float:2.79E-43)
            java.lang.String r4 = "native exception occurred"
            java.lang.String r5 = r10.toString()     // Catch:{ all -> 0x0684 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r6.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = "soName="
            r6.append(r7)     // Catch:{ all -> 0x0684 }
            r6.append(r1)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = ", authCode="
            r6.append(r1)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r9.h     // Catch:{ all -> 0x0684 }
            r6.append(r1)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = ", errorCode="
            r6.append(r1)     // Catch:{ all -> 0x0684 }
            int r1 = r10.getErrorCode()     // Catch:{ all -> 0x0684 }
            r6.append(r1)     // Catch:{ all -> 0x0684 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0684 }
            java.io.File r1 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r7 = r9.c((java.lang.String) r1)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r13.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r8 = r9.c((java.lang.String) r1)     // Catch:{ all -> 0x0684 }
            r1 = r31
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
        L_0x0603:
            throw r10     // Catch:{ all -> 0x0684 }
        L_0x0604:
            r0 = move-exception
            r1 = r0
            r10 = r19
        L_0x0608:
            java.lang.String r2 = ""
        L_0x060a:
            com.alibaba.wireless.security.framework.utils.a.a(r2, r1)     // Catch:{ all -> 0x0684 }
            goto L_0x0615
        L_0x060e:
            r0 = move-exception
            r1 = r0
            r10 = r19
        L_0x0612:
            java.lang.String r2 = ""
            goto L_0x060a
        L_0x0615:
            com.alibaba.wireless.security.framework.SGPluginExtras.slot = r16
            return r10
        L_0x0618:
            r0 = move-exception
            r10 = r0
            r26 = r6
            r15 = r7
            r2 = 100043(0x186cb, float:1.4019E-40)
            r3 = 199(0xc7, float:2.79E-43)
            java.lang.String r4 = "isMeetReverseDependencies failed"
            int r1 = r35.length()     // Catch:{ all -> 0x0684 }
            if (r1 != 0) goto L_0x062c
            r6 = r15
            goto L_0x0641
        L_0x062c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            r1.append(r12)     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "->"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            r1.append(r15)     // Catch:{ all -> 0x0684 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r6 = r1
        L_0x0641:
            java.io.File r1 = r11.c     // Catch:{ all -> 0x0684 }
            if (r1 == 0) goto L_0x065d
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "src:"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            java.io.File r5 = r11.c     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r9.c((java.lang.String) r5)     // Catch:{ all -> 0x0684 }
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            goto L_0x0675
        L_0x065d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0684 }
            r1.<init>()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = "zipfile:"
            r1.append(r5)     // Catch:{ all -> 0x0684 }
            java.io.File r5 = r11.a     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ all -> 0x0684 }
            java.lang.String r5 = r9.c((java.lang.String) r5)     // Catch:{ all -> 0x0684 }
            r1.append(r5)     // Catch:{ all -> 0x0684 }
        L_0x0675:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0684 }
            r7 = r1
            java.lang.String r8 = ""
            r1 = r31
            r5 = r26
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0684 }
            throw r10     // Catch:{ all -> 0x0684 }
        L_0x0684:
            r0 = move-exception
            r1 = r0
            com.alibaba.wireless.security.framework.SGPluginExtras.slot = r16
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, com.alibaba.wireless.security.framework.d$a, android.content.Context, java.lang.String):com.alibaba.wireless.security.framework.c");
    }

    private File a(Context context) throws SecException {
        if (context != null) {
            String str = null;
            if (context != null) {
                try {
                    String str2 = context.getApplicationInfo().sourceDir;
                    if (str2 != null) {
                        File file = new File(str2);
                        if (file.exists()) {
                            str = (file.lastModified() / 1000) + "";
                        }
                    }
                } catch (Exception e2) {
                    a(100038, 115, "", "" + e2, "", "", "");
                    throw new SecException((Throwable) e2, 115);
                }
            }
            if (str != null) {
                this.l = context.getDir("SGLib", 0);
                if (this.l == null || !this.l.exists()) {
                    a(100038, 109, "", "" + this.l, "", "", "");
                    throw new SecException(109);
                }
                File file2 = new File(this.l.getAbsolutePath(), "app_" + str);
                if (!file2.exists()) {
                    file2.mkdirs();
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                }
                if (p && file2.exists()) {
                    p = false;
                    a(this.l, "app_" + str);
                }
                if (file2.exists()) {
                    return file2;
                }
                a(100038, 114, "", "", "", "", "");
                throw new SecException(114);
            }
            throw new SecException(115);
        }
        a(100038, 116, "", "", "", "", "");
        throw new SecException(116);
    }

    private File a(Context context, b bVar) {
        if (f.a(context) || bVar == null || bVar.b() == 0 || bVar.c() == null || bVar.c().length() <= 0) {
            return null;
        }
        File file = new File(this.k.getAbsolutePath() + File.separator + "libs" + File.separator + bVar.b() + File.separator + bVar.c());
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    private File a(String str) {
        if (this.g != null) {
            return null;
        }
        String a2 = f.a(d.class.getClassLoader(), "sg" + str);
        if (a2 == null || a2.length() <= 0) {
            return null;
        }
        return new File(a2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x008c A[SYNTHETIC, Splitter:B:31:0x008c] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0093 A[SYNTHETIC, Splitter:B:37:0x0093] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File a(java.lang.String r13, java.io.File r14) {
        /*
            r12 = this;
            r0 = 0
            android.content.Context r1 = r12.c     // Catch:{ Exception -> 0x000a }
            android.content.pm.ApplicationInfo r1 = r1.getApplicationInfo()     // Catch:{ Exception -> 0x000a }
            java.lang.String r1 = r1.sourceDir     // Catch:{ Exception -> 0x000a }
            goto L_0x000b
        L_0x000a:
            r1 = r0
        L_0x000b:
            if (r1 != 0) goto L_0x000e
            return r0
        L_0x000e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "libsg"
            r2.append(r3)
            r2.append(r13)
            java.lang.String r3 = ".so"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "Plugin not existed in the application library path, maybe installed in x86 phone, or the armeabi-v7a existed"
            com.alibaba.wireless.security.framework.utils.a.b(r3)     // Catch:{ IOException -> 0x006e, all -> 0x006b }
            java.util.zip.ZipFile r3 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x006e, all -> 0x006b }
            r3.<init>(r1)     // Catch:{ IOException -> 0x006e, all -> 0x006b }
            java.lang.String[] r4 = n     // Catch:{ IOException -> 0x0069 }
            int r5 = r4.length     // Catch:{ IOException -> 0x0069 }
            r6 = 0
        L_0x0032:
            if (r6 >= r5) goto L_0x0064
            r7 = r4[r6]     // Catch:{ IOException -> 0x0069 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0069 }
            r8.<init>()     // Catch:{ IOException -> 0x0069 }
            java.lang.String r9 = "lib"
            r8.append(r9)     // Catch:{ IOException -> 0x0069 }
            java.lang.String r9 = java.io.File.separator     // Catch:{ IOException -> 0x0069 }
            r8.append(r9)     // Catch:{ IOException -> 0x0069 }
            r8.append(r7)     // Catch:{ IOException -> 0x0069 }
            java.lang.String r9 = java.io.File.separator     // Catch:{ IOException -> 0x0069 }
            r8.append(r9)     // Catch:{ IOException -> 0x0069 }
            r8.append(r2)     // Catch:{ IOException -> 0x0069 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x0069 }
            java.util.zip.ZipEntry r9 = r3.getEntry(r8)     // Catch:{ IOException -> 0x0069 }
            if (r9 == 0) goto L_0x0061
            o = r7     // Catch:{ IOException -> 0x0069 }
            java.io.File r14 = r12.a((java.lang.String) r13, (java.io.File) r14, (java.util.zip.ZipFile) r3, (java.lang.String) r8)     // Catch:{ IOException -> 0x0069 }
            goto L_0x0065
        L_0x0061:
            int r6 = r6 + 1
            goto L_0x0032
        L_0x0064:
            r14 = r0
        L_0x0065:
            r3.close()     // Catch:{ Exception -> 0x0068 }
        L_0x0068:
            return r14
        L_0x0069:
            r14 = move-exception
            goto L_0x0070
        L_0x006b:
            r13 = move-exception
            r3 = r0
            goto L_0x0091
        L_0x006e:
            r14 = move-exception
            r3 = r0
        L_0x0070:
            java.lang.String r2 = "getPluginFile throws exception"
            com.alibaba.wireless.security.framework.utils.a.a(r2, r14)     // Catch:{ all -> 0x0090 }
            r5 = 100047(0x186cf, float:1.40196E-40)
            r6 = 3
            java.lang.String r7 = r14.toString()     // Catch:{ all -> 0x0090 }
            java.lang.String r9 = r12.c((java.lang.String) r1)     // Catch:{ all -> 0x0090 }
            java.lang.String r10 = ""
            java.lang.String r11 = ""
            r4 = r12
            r8 = r13
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x0090 }
            if (r3 == 0) goto L_0x008f
            r3.close()     // Catch:{ Exception -> 0x008f }
        L_0x008f:
            return r0
        L_0x0090:
            r13 = move-exception
        L_0x0091:
            if (r3 == 0) goto L_0x0096
            r3.close()     // Catch:{ Exception -> 0x0096 }
        L_0x0096:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, java.io.File):java.io.File");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x003b A[SYNTHETIC, Splitter:B:19:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0043 A[SYNTHETIC, Splitter:B:26:0x0043] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File a(java.lang.String r11, java.io.File r12, java.lang.String r13, java.lang.String r14) {
        /*
            r10 = this;
            r0 = 0
            if (r13 == 0) goto L_0x0047
            if (r14 != 0) goto L_0x0006
            goto L_0x0047
        L_0x0006:
            java.lang.String r1 = "Plugin not existed in the application library path, maybe installed in x86 phone, or the armeabi-v7a existed"
            com.alibaba.wireless.security.framework.utils.a.b(r1)     // Catch:{ IOException -> 0x001d, all -> 0x001a }
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x001d, all -> 0x001a }
            r1.<init>(r13)     // Catch:{ IOException -> 0x001d, all -> 0x001a }
            java.io.File r12 = r10.a((java.lang.String) r11, (java.io.File) r12, (java.util.zip.ZipFile) r1, (java.lang.String) r14)     // Catch:{ IOException -> 0x0018 }
            r1.close()     // Catch:{ Exception -> 0x003f }
            goto L_0x003f
        L_0x0018:
            r12 = move-exception
            goto L_0x001f
        L_0x001a:
            r11 = move-exception
            r1 = r0
            goto L_0x0041
        L_0x001d:
            r12 = move-exception
            r1 = r0
        L_0x001f:
            java.lang.String r14 = "getPluginFile throws exception"
            com.alibaba.wireless.security.framework.utils.a.a(r14, r12)     // Catch:{ all -> 0x0040 }
            r3 = 100047(0x186cf, float:1.40196E-40)
            r4 = 3
            java.lang.String r5 = r12.toString()     // Catch:{ all -> 0x0040 }
            java.lang.String r7 = r10.c((java.lang.String) r13)     // Catch:{ all -> 0x0040 }
            java.lang.String r8 = ""
            java.lang.String r9 = ""
            r2 = r10
            r6 = r11
            r2.a(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0040 }
            if (r1 == 0) goto L_0x003e
            r1.close()     // Catch:{ Exception -> 0x003e }
        L_0x003e:
            r12 = r0
        L_0x003f:
            return r12
        L_0x0040:
            r11 = move-exception
        L_0x0041:
            if (r1 == 0) goto L_0x0046
            r1.close()     // Catch:{ Exception -> 0x0046 }
        L_0x0046:
            throw r11
        L_0x0047:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, java.io.File, java.lang.String, java.lang.String):java.io.File");
    }

    private File a(String str, File file, ZipFile zipFile, String str2) throws IOException {
        ZipEntry entry;
        if (!(zipFile == null || str2 == null || (entry = zipFile.getEntry(str2)) == null || !b.a(entry.getName()))) {
            com.alibaba.wireless.security.framework.utils.a.b("Plugin existed  in " + entry.getName());
            File file2 = new File(file, "libsg" + str + "_inner" + entry.getTime() + ".zip");
            if ((!file2.exists() || file2.length() != entry.getSize()) && !f.a(zipFile, entry, file2)) {
                com.alibaba.wireless.security.framework.utils.a.b("Extract failed!!");
            } else {
                com.alibaba.wireless.security.framework.utils.a.b("Extract success");
                return file2;
            }
        }
        return null;
    }

    private Class<?> a(ClassLoader classLoader, String str) {
        Class<?> cls;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            cls = Class.forName(str, true, classLoader);
        } catch (Throwable th) {
            a(100042, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "Class.forName failed", "" + th, str, classLoader.toString(), "");
            cls = null;
        }
        com.alibaba.wireless.security.framework.utils.a.b("    loadClassFromClassLoader( " + str + " ) used time: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        return cls;
    }

    private String a(Class<?> cls) {
        InterfacePluginInfo interfacePluginInfo = (InterfacePluginInfo) cls.getAnnotation(InterfacePluginInfo.class);
        if (interfacePluginInfo == null) {
            return null;
        }
        return interfacePluginInfo.pluginName();
    }

    private void a() throws SecException {
        this.k = a(this.c);
        if (this.k != null) {
            Context context = this.c;
            this.b = new c(context, this.k + File.separator + "lock.lock");
            this.i = b();
            this.m = a(this.c, this.i);
        }
    }

    private void a(int i2, int i3, String str, String str2, String str3, String str4, String str5) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        int i4 = i2;
        sb.append(i2);
        UserTrackMethodJniBridge.addUtRecord(sb.toString(), i3, 0, com.alibaba.wireless.security.open.initialize.c.a(), 0, str, str2, str3, str4, str5);
    }

    /* access modifiers changed from: private */
    public void a(File file) {
        String[] list;
        if (file.isDirectory() && (list = file.list()) != null) {
            for (String file2 : list) {
                a(new File(file, file2));
            }
        }
        file.delete();
    }

    private void a(final File file, final String str) {
        new Thread(new Runnable() {
            public void run() {
                File filesDir;
                File[] listFiles;
                File[] listFiles2;
                try {
                    if (file != null && file.isDirectory() && (listFiles2 = file.listFiles()) != null && listFiles2.length > 0) {
                        for (File file : listFiles2) {
                            if (file.isDirectory() && file.getName().startsWith("app_") && !file.getName().equals(str)) {
                                d.this.a(file);
                            } else if (file.getName().startsWith("libsg")) {
                                file.delete();
                            }
                        }
                    }
                    if (d.this.c != null && (filesDir = d.this.c.getFilesDir()) != null && filesDir.isDirectory() && (listFiles = filesDir.listFiles()) != null && listFiles.length > 0) {
                        for (File file2 : listFiles) {
                            if (file2.getName().startsWith("libsecuritysdk")) {
                                file2.delete();
                            }
                        }
                    }
                } catch (Throwable unused) {
                }
            }
        }).start();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x009c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x009d, code lost:
        r1 = r0;
        r13 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a0, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00a1, code lost:
        r3 = r19;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009c A[ExcHandler: all (r0v15 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00d5 A[SYNTHETIC, Splitter:B:51:0x00d5] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e2 A[SYNTHETIC, Splitter:B:56:0x00e2] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f6 A[SYNTHETIC, Splitter:B:65:0x00f6] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0103 A[SYNTHETIC, Splitter:B:70:0x0103] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean a(java.io.File r19, java.io.File r20) {
        /*
            r18 = this;
            r9 = r18
            r1 = r20
            java.io.File r10 = new java.io.File
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = r20.getAbsolutePath()
            r0.append(r2)
            java.lang.String r2 = ".tmp."
            r0.append(r2)
            int r2 = android.os.Process.myPid()
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            r10.<init>(r0)
            r11 = 0
            r2 = 0
            boolean r0 = r10.exists()     // Catch:{ Exception -> 0x00a0, all -> 0x009c }
            if (r0 == 0) goto L_0x0030
            r10.delete()     // Catch:{ Exception -> 0x00a0, all -> 0x009c }
        L_0x0030:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00a0, all -> 0x009c }
            r3 = r19
            r0.<init>(r3)     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            java.nio.channels.FileChannel r4 = r0.getChannel()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0096, all -> 0x0090 }
            r0.<init>(r10)     // Catch:{ Exception -> 0x0096, all -> 0x0090 }
            java.nio.channels.FileChannel r5 = r0.getChannel()     // Catch:{ Exception -> 0x0096, all -> 0x0090 }
            r14 = 0
            long r16 = r4.size()     // Catch:{ Exception -> 0x008c, all -> 0x0086 }
            r12 = r5
            r13 = r4
            r12.transferFrom(r13, r14, r16)     // Catch:{ Exception -> 0x008c, all -> 0x0086 }
            r4.close()     // Catch:{ Exception -> 0x008c, all -> 0x0086 }
            r5.close()     // Catch:{ Exception -> 0x0083, all -> 0x0080 }
            long r4 = r10.length()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            long r6 = r19.length()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x00ed
            boolean r0 = r20.exists()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            if (r0 == 0) goto L_0x0079
            long r4 = r20.length()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            long r6 = r19.length()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0076
            r11 = 1
            goto L_0x00ed
        L_0x0076:
            r20.delete()     // Catch:{ Exception -> 0x009a, all -> 0x009c }
        L_0x0079:
            boolean r0 = r10.renameTo(r1)     // Catch:{ Exception -> 0x009a, all -> 0x009c }
            r11 = r0
            goto L_0x00ed
        L_0x0080:
            r0 = move-exception
            r1 = r0
            goto L_0x0089
        L_0x0083:
            r0 = move-exception
            r12 = r2
            goto L_0x008e
        L_0x0086:
            r0 = move-exception
            r1 = r0
            r2 = r4
        L_0x0089:
            r13 = r5
            goto L_0x00f4
        L_0x008c:
            r0 = move-exception
            r12 = r4
        L_0x008e:
            r13 = r5
            goto L_0x00a5
        L_0x0090:
            r0 = move-exception
            r1 = r0
            r13 = r2
            r2 = r4
            goto L_0x00f4
        L_0x0096:
            r0 = move-exception
            r13 = r2
            r12 = r4
            goto L_0x00a5
        L_0x009a:
            r0 = move-exception
            goto L_0x00a3
        L_0x009c:
            r0 = move-exception
            r1 = r0
            r13 = r2
            goto L_0x00f4
        L_0x00a0:
            r0 = move-exception
            r3 = r19
        L_0x00a3:
            r12 = r2
            r13 = r12
        L_0x00a5:
            java.lang.String r2 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r2, r0)     // Catch:{ all -> 0x00f1 }
            r2 = 100047(0x186cf, float:1.40196E-40)
            r4 = 2
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00f1 }
            java.lang.String r3 = r19.getAbsolutePath()     // Catch:{ all -> 0x00f1 }
            java.lang.String r5 = r9.c((java.lang.String) r3)     // Catch:{ all -> 0x00f1 }
            java.lang.String r1 = r20.getAbsolutePath()     // Catch:{ all -> 0x00f1 }
            java.lang.String r6 = r9.c((java.lang.String) r1)     // Catch:{ all -> 0x00f1 }
            java.lang.String r1 = r10.getAbsolutePath()     // Catch:{ all -> 0x00f1 }
            java.lang.String r7 = r9.c((java.lang.String) r1)     // Catch:{ all -> 0x00f1 }
            java.lang.String r8 = ""
            r1 = r18
            r3 = r4
            r4 = r0
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x00f1 }
            if (r12 == 0) goto L_0x00e0
            r12.close()     // Catch:{ Exception -> 0x00d9 }
            goto L_0x00e0
        L_0x00d9:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r0, r1)
        L_0x00e0:
            if (r13 == 0) goto L_0x00ed
            r13.close()     // Catch:{ Exception -> 0x00e6 }
            goto L_0x00ed
        L_0x00e6:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r0, r1)
        L_0x00ed:
            r10.delete()
            return r11
        L_0x00f1:
            r0 = move-exception
            r1 = r0
            r2 = r12
        L_0x00f4:
            if (r2 == 0) goto L_0x0101
            r2.close()     // Catch:{ Exception -> 0x00fa }
            goto L_0x0101
        L_0x00fa:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r0, r2)
        L_0x0101:
            if (r13 == 0) goto L_0x010e
            r13.close()     // Catch:{ Exception -> 0x0107 }
            goto L_0x010e
        L_0x0107:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r0, r2)
        L_0x010e:
            r10.delete()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.io.File, java.io.File):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0050, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x005f, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0076, code lost:
        return true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0098 A[Catch:{ all -> 0x0081 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00a1 A[Catch:{ all -> 0x0081 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00a6 A[Catch:{ all -> 0x0081 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00af A[Catch:{ all -> 0x0081 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00c4 A[SYNTHETIC, Splitter:B:73:0x00c4] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized boolean a(java.lang.String r13, java.lang.String r14, java.io.File r15, java.lang.String r16, boolean r17) {
        /*
            r12 = this;
            r9 = r12
            r0 = r16
            monitor-enter(r12)
            android.content.Context r2 = r9.c     // Catch:{ all -> 0x00d2 }
            boolean r2 = com.alibaba.wireless.security.framework.utils.f.a(r2)     // Catch:{ all -> 0x00d2 }
            r3 = 1
            if (r2 == 0) goto L_0x0015
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x00d2 }
            r4 = 19
            if (r2 <= r4) goto L_0x0015
            monitor-exit(r12)
            return r3
        L_0x0015:
            java.lang.String r2 = r9.g     // Catch:{ all -> 0x00d2 }
            if (r2 == 0) goto L_0x001b
            monitor-exit(r12)
            return r3
        L_0x001b:
            if (r15 == 0) goto L_0x0033
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x00d2 }
            java.lang.String r4 = r15.getParent()     // Catch:{ all -> 0x00d2 }
            r2.<init>(r4, r0)     // Catch:{ all -> 0x00d2 }
            boolean r2 = r2.exists()     // Catch:{ all -> 0x00d2 }
            if (r2 == 0) goto L_0x002e
            monitor-exit(r12)
            return r3
        L_0x002e:
            java.lang.String r2 = r15.getAbsolutePath()     // Catch:{ all -> 0x00d2 }
            goto L_0x0034
        L_0x0033:
            r2 = r13
        L_0x0034:
            if (r17 != 0) goto L_0x003b
            com.alibaba.wireless.security.framework.utils.c r4 = r9.b     // Catch:{ all -> 0x00d2 }
            r4.a()     // Catch:{ all -> 0x00d2 }
        L_0x003b:
            r4 = 0
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0083 }
            r6 = r14
            r5.<init>(r14, r0)     // Catch:{ Exception -> 0x0083 }
            boolean r4 = r5.exists()     // Catch:{ Exception -> 0x007f }
            if (r4 == 0) goto L_0x0051
            if (r17 != 0) goto L_0x004f
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b     // Catch:{ all -> 0x00d2 }
            r0.b()     // Catch:{ all -> 0x00d2 }
        L_0x004f:
            monitor-exit(r12)
            return r3
        L_0x0051:
            boolean r4 = com.alibaba.wireless.security.framework.utils.f.a((java.lang.String) r2, (java.lang.String) r0, (java.io.File) r5)     // Catch:{ Exception -> 0x007f }
            if (r4 == 0) goto L_0x0060
            if (r17 != 0) goto L_0x005e
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b     // Catch:{ all -> 0x00d2 }
            r0.b()     // Catch:{ all -> 0x00d2 }
        L_0x005e:
            monitor-exit(r12)
            return r3
        L_0x0060:
            android.content.Context r4 = r9.c     // Catch:{ Exception -> 0x007f }
            android.content.pm.ApplicationInfo r4 = r4.getApplicationInfo()     // Catch:{ Exception -> 0x007f }
            java.lang.String r4 = r4.sourceDir     // Catch:{ Exception -> 0x007f }
            boolean r0 = com.alibaba.wireless.security.framework.utils.f.a((java.lang.String) r4, (java.lang.String) r0, (java.io.File) r5)     // Catch:{ Exception -> 0x007f }
            if (r0 == 0) goto L_0x0077
            if (r17 != 0) goto L_0x0075
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b     // Catch:{ all -> 0x00d2 }
            r0.b()     // Catch:{ all -> 0x00d2 }
        L_0x0075:
            monitor-exit(r12)
            return r3
        L_0x0077:
            if (r17 != 0) goto L_0x00c7
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b     // Catch:{ all -> 0x00d2 }
        L_0x007b:
            r0.b()     // Catch:{ all -> 0x00d2 }
            goto L_0x00c7
        L_0x007f:
            r0 = move-exception
            goto L_0x0085
        L_0x0081:
            r0 = move-exception
            goto L_0x00ca
        L_0x0083:
            r0 = move-exception
            r5 = r4
        L_0x0085:
            r3 = 100039(0x186c7, float:1.40184E-40)
            r4 = 107(0x6b, float:1.5E-43)
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x0081 }
            java.lang.String r7 = r12.c()     // Catch:{ all -> 0x0081 }
            java.lang.String r8 = r12.c((java.lang.String) r2)     // Catch:{ all -> 0x0081 }
            if (r5 == 0) goto L_0x00a1
            java.lang.String r2 = r5.getAbsolutePath()     // Catch:{ all -> 0x0081 }
            java.lang.String r2 = r12.c((java.lang.String) r2)     // Catch:{ all -> 0x0081 }
            goto L_0x00a3
        L_0x00a1:
            java.lang.String r2 = ""
        L_0x00a3:
            r10 = r2
            if (r15 == 0) goto L_0x00af
            java.lang.String r1 = r15.getAbsolutePath()     // Catch:{ all -> 0x0081 }
            java.lang.String r1 = r12.c((java.lang.String) r1)     // Catch:{ all -> 0x0081 }
            goto L_0x00b1
        L_0x00af:
            java.lang.String r1 = ""
        L_0x00b1:
            r11 = r1
            r1 = r12
            r2 = r3
            r3 = r4
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r10
            r8 = r11
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0081 }
            java.lang.String r1 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r1, r0)     // Catch:{ all -> 0x0081 }
            if (r17 != 0) goto L_0x00c7
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b     // Catch:{ all -> 0x00d2 }
            goto L_0x007b
        L_0x00c7:
            r0 = 0
            monitor-exit(r12)
            return r0
        L_0x00ca:
            if (r17 != 0) goto L_0x00d1
            com.alibaba.wireless.security.framework.utils.c r1 = r9.b     // Catch:{ all -> 0x00d2 }
            r1.b()     // Catch:{ all -> 0x00d2 }
        L_0x00d1:
            throw r0     // Catch:{ all -> 0x00d2 }
        L_0x00d2:
            r0 = move-exception
            monitor-exit(r12)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, java.lang.String, java.io.File, java.lang.String, boolean):boolean");
    }

    /* access modifiers changed from: private */
    public boolean a(String str, String str2, boolean z) throws SecException {
        String str3 = str2;
        boolean z2 = z;
        if (str.trim().length() == 0) {
            return true;
        }
        String[] split = str.split(";");
        for (int i2 = 0; i2 < split.length; i2++) {
            String trim = split[i2].trim();
            if (trim.length() != 0) {
                String[] split2 = trim.split(":");
                if (split2.length == 2) {
                    int indexOf = str3.indexOf(split2[0]);
                    if (indexOf >= 0) {
                        int indexOf2 = str3.indexOf(Operators.BRACKET_START_STR, indexOf);
                        int indexOf3 = str3.indexOf(Operators.BRACKET_END_STR, indexOf);
                        if (indexOf2 < 0 || indexOf3 < 0 || indexOf2 > indexOf3) {
                            a(100040, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "indexLeftP < 0 || indexRightP < 0 || indexLeftP > indexRightP", "" + indexOf2, "" + indexOf3, "", "" + i2);
                            throw new SecException(SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR);
                        }
                        String substring = str3.substring(indexOf2 + 1, indexOf3);
                        if (a(substring, split2[1]) < 0) {
                            String str4 = "version " + substring + " of " + split2[0] + " is not meet the requirement: " + split2[1];
                            if (str2.length() != 0) {
                                str4 = str4 + ", depended by: " + str3;
                            }
                            String str5 = str4;
                            if (!z2) {
                                a(100040, 113, "versionCompare(parentPluginVersion, nameVersionPair[1]) < 0", substring, split2[0], split2[1], "" + i2);
                            }
                            throw new SecException(str5, 113);
                        }
                    } else {
                        ISGPluginInfo iSGPluginInfo = this.d.get(split2[0]);
                        if (iSGPluginInfo == null) {
                            Throwable th = null;
                            try {
                                iSGPluginInfo = d(split2[0], str3, !z2);
                            } catch (Throwable th2) {
                                th = th2;
                            }
                            if (iSGPluginInfo == null) {
                                if (!z2) {
                                    if (th instanceof SecException) {
                                        throw ((SecException) th);
                                    }
                                    a(100040, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "throwable in loadPluginInner", "" + th, str, str2, "" + i2);
                                    throw new SecException(str3, (int) SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR);
                                }
                            }
                        }
                        if (a(iSGPluginInfo.getVersion(), split2[1]) < 0) {
                            String str6 = "version " + iSGPluginInfo.getVersion() + " of " + split2[0] + " is not meet the requirement: " + split2[1];
                            if (str2.length() != 0) {
                                str6 = str6 + ", depended by: " + str3;
                            }
                            if (!z2) {
                                a(100040, 113, "versionCompare(dependPlugin.getVersion(), nameVersionPair[1]) < 0", iSGPluginInfo.getVersion(), split2[0], split2[1], "" + i2);
                            }
                            throw new SecException(str6, 113);
                        }
                    }
                } else {
                    a(100040, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "nameVersionPair.length != 2", trim, str2, "" + z2, "" + i2);
                    throw new SecException(SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR);
                }
            }
        }
        return true;
    }

    private b b() {
        b bVar;
        File file = new File(this.k, "update.config");
        File file2 = new File(this.k, "init.config");
        if (this.j) {
            bVar = b.a(file);
            if (bVar == null) {
                return b.a(file2);
            }
            try {
                this.b.a();
                file2.delete();
                file.renameTo(file2);
            } catch (Throwable th) {
                this.b.b();
                throw th;
            }
        } else {
            try {
                this.b.a();
                bVar = b.a(file2);
            } catch (Throwable th2) {
                this.b.b();
                throw th2;
            }
        }
        this.b.b();
        return bVar;
    }

    private File b(String str) {
        String str2 = this.g;
        if (str2 == null) {
            try {
                str2 = this.c.getApplicationInfo().nativeLibraryDir;
            } catch (Exception unused) {
            }
        }
        if (str2 != null && str2.length() > 0) {
            File file = new File(str2 + File.separator + "libsg" + str + ".so");
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    private ClassLoader b(String str, String str2, boolean z) {
        if (!z) {
            try {
                this.b.a();
            } catch (Throwable th) {
                if (!z) {
                    this.b.b();
                }
                throw th;
            }
        }
        String str3 = this.c.getApplicationInfo().nativeLibraryDir + File.pathSeparator + str2;
        if (this.g != null) {
            str3 = str3 + File.pathSeparator + this.g;
            com.alibaba.wireless.security.framework.utils.a.b("add path to native classloader " + str3);
        }
        if (o != null) {
            str3 = str3 + File.pathSeparator + this.c.getApplicationInfo().sourceDir + "!/lib/" + o;
        }
        ClassLoader dexClassLoader = (Build.VERSION.SDK_INT < 21 || "6.0.1".equalsIgnoreCase(Build.VERSION.RELEASE)) ? new DexClassLoader(str, str2, str3, d.class.getClassLoader()) : new PathClassLoader(str, str3, d.class.getClassLoader());
        if (!z) {
            this.b.b();
        }
        return dexClassLoader;
    }

    private boolean b(File file) {
        try {
            return !file.getParentFile().getCanonicalPath().equals(file.getCanonicalFile().getParentFile().getCanonicalPath()) || !file.getName().equals(file.getCanonicalFile().getName());
        } catch (Exception e2) {
            com.alibaba.wireless.security.framework.utils.a.a("", e2);
            a(100047, 0, e2.toString(), file.getAbsolutePath(), "", "", "");
            return false;
        }
    }

    private boolean b(File file, File file2) {
        Method method;
        try {
            Object obj = null;
            if (Build.VERSION.SDK_INT >= 21) {
                method = Class.forName("android.system.Os").getDeclaredMethod("symlink", new Class[]{String.class, String.class});
            } else {
                Field declaredField = Class.forName("libcore.io.Libcore").getDeclaredField("os");
                declaredField.setAccessible(true);
                obj = declaredField.get((Object) null);
                method = obj.getClass().getMethod("symlink", new Class[]{String.class, String.class});
            }
            method.invoke(obj, new Object[]{file.getAbsolutePath(), file2.getAbsolutePath()});
            return true;
        } catch (Exception e2) {
            com.alibaba.wireless.security.framework.utils.a.a("create symbolic link( " + file2.getAbsolutePath() + " -> " + file.getAbsolutePath() + " ) failed", e2);
            String exc = e2.toString();
            String absolutePath = file.getAbsolutePath();
            String absolutePath2 = file2.getAbsolutePath();
            a(100047, 1, exc, absolutePath, absolutePath2, "" + Build.VERSION.SDK_INT, "");
            return false;
        }
    }

    private boolean b(String str, String str2) throws SecException {
        for (Map.Entry next : this.d.entrySet()) {
            String str3 = (String) next.getKey();
            c cVar = (c) next.getValue();
            String a2 = cVar.a("reversedependencies");
            if (a2 != null) {
                String[] split = a2.split(";");
                for (int i2 = 0; i2 < split.length; i2++) {
                    String trim = split[i2].trim();
                    if (trim.length() != 0) {
                        String[] split2 = trim.split(":");
                        if (split2.length != 2) {
                            a(100041, SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR, "nameVersionPair.length != 2", str + Operators.BRACKET_START_STR + str2 + Operators.BRACKET_END_STR, str3 + Operators.BRACKET_START_STR + cVar.getVersion() + Operators.BRACKET_END_STR, c(cVar.getPluginPath()), a2);
                            throw new SecException(SecExceptionCode.SEC_ERROR_INIT_UNKNOWN_ERROR);
                        } else if (split2[0].equalsIgnoreCase(str) && a(str2, split2[1]) < 0) {
                            String str4 = "plugin " + str + Operators.BRACKET_START_STR + str2 + ") is not meet the reverse dependency of " + str3 + Operators.BRACKET_START_STR + cVar.getVersion() + "): " + split2[0] + Operators.BRACKET_START_STR + split2[1] + Operators.BRACKET_END_STR;
                            a(100041, 117, str4, d.class.getClassLoader().toString(), c(cVar.getPluginPath()), a2, "" + i2);
                            throw new SecException(str4, 117);
                        }
                    }
                }
                continue;
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0054 A[SYNTHETIC, Splitter:B:18:0x0054] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065 A[Catch:{ all -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x013f A[Catch:{ all -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0156  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.alibaba.wireless.security.framework.d.a c(java.lang.String r17, java.lang.String r18, boolean r19) throws com.alibaba.wireless.security.open.SecException {
        /*
            r16 = this;
            r9 = r16
            r0 = r17
            java.io.File r1 = r9.m
            if (r1 == 0) goto L_0x0037
            java.io.File r1 = new java.io.File
            java.io.File r2 = r9.m
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "libsg"
            r3.append(r4)
            r3.append(r0)
            java.lang.String r4 = ".so"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r1.<init>(r2, r3)
            boolean r2 = r1.exists()
            if (r2 == 0) goto L_0x0037
            java.io.File r2 = r9.m
            java.io.File r2 = r9.d(r2)
            java.io.File r3 = r9.m
            if (r2 == r3) goto L_0x0039
            r3 = 1
            goto L_0x003a
        L_0x0037:
            r1 = 0
            r2 = 0
        L_0x0039:
            r3 = 0
        L_0x003a:
            if (r2 != 0) goto L_0x0049
            java.io.File r2 = r9.k
            java.io.File r2 = r9.d(r2)
            java.io.File r3 = r9.k
            if (r2 == r3) goto L_0x0048
            r3 = 1
            goto L_0x0049
        L_0x0048:
            r3 = 0
        L_0x0049:
            r14 = r2
            r13 = r3
            if (r13 != 0) goto L_0x0052
            com.alibaba.wireless.security.framework.utils.c r2 = r9.b
            r2.a()
        L_0x0052:
            if (r1 != 0) goto L_0x0063
            java.io.File r1 = r16.a((java.lang.String) r17)     // Catch:{ all -> 0x0060 }
            boolean r2 = r9.c((java.io.File) r1)     // Catch:{ all -> 0x0060 }
            if (r2 != 0) goto L_0x0063
            r1 = 0
            goto L_0x0063
        L_0x0060:
            r0 = move-exception
            goto L_0x0213
        L_0x0063:
            if (r1 == 0) goto L_0x013d
            java.lang.String r15 = r1.getAbsolutePath()     // Catch:{ all -> 0x0060 }
            android.content.Context r2 = r9.c     // Catch:{ all -> 0x0060 }
            java.lang.String r2 = r2.getPackageName()     // Catch:{ all -> 0x0060 }
            java.lang.String r3 = "com.eg.android.AlipayGphone"
            boolean r2 = r3.equals(r2)     // Catch:{ all -> 0x0060 }
            if (r2 == 0) goto L_0x00e5
            if (r15 == 0) goto L_0x00e5
            java.lang.String r2 = "app_plugins_lib"
            boolean r2 = r15.contains(r2)     // Catch:{ all -> 0x0060 }
            if (r2 == 0) goto L_0x00e5
            r2 = 100088(0x186f8, float:1.40253E-40)
            r3 = 1
            java.lang.String r4 = android.os.Build.FINGERPRINT     // Catch:{ all -> 0x0060 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0060 }
            r5.<init>()     // Catch:{ all -> 0x0060 }
            r5.append(r15)     // Catch:{ all -> 0x0060 }
            java.lang.String r6 = "("
            r5.append(r6)     // Catch:{ all -> 0x0060 }
            long r6 = r1.getTotalSpace()     // Catch:{ all -> 0x0060 }
            r5.append(r6)     // Catch:{ all -> 0x0060 }
            java.lang.String r1 = ")"
            r5.append(r1)     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0060 }
            java.lang.Class<com.alibaba.wireless.security.framework.d> r1 = com.alibaba.wireless.security.framework.d.class
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ all -> 0x0060 }
            java.lang.String r6 = r1.toString()     // Catch:{ all -> 0x0060 }
            java.lang.String r7 = r14.getAbsolutePath()     // Catch:{ all -> 0x0060 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0060 }
            r1.<init>()     // Catch:{ all -> 0x0060 }
            android.content.Context r8 = r9.c     // Catch:{ all -> 0x0060 }
            android.content.pm.ApplicationInfo r8 = r8.getApplicationInfo()     // Catch:{ all -> 0x0060 }
            java.lang.String r8 = r8.sourceDir     // Catch:{ all -> 0x0060 }
            r1.append(r8)     // Catch:{ all -> 0x0060 }
            java.lang.String r8 = ","
            r1.append(r8)     // Catch:{ all -> 0x0060 }
            java.io.File r8 = new java.io.File     // Catch:{ all -> 0x0060 }
            android.content.Context r12 = r9.c     // Catch:{ all -> 0x0060 }
            android.content.pm.ApplicationInfo r12 = r12.getApplicationInfo()     // Catch:{ all -> 0x0060 }
            java.lang.String r12 = r12.sourceDir     // Catch:{ all -> 0x0060 }
            r8.<init>(r12)     // Catch:{ all -> 0x0060 }
            long r10 = r8.lastModified()     // Catch:{ all -> 0x0060 }
            r1.append(r10)     // Catch:{ all -> 0x0060 }
            java.lang.String r8 = r1.toString()     // Catch:{ all -> 0x0060 }
            r1 = r16
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0060 }
            r1 = 0
        L_0x00e5:
            if (r1 == 0) goto L_0x013d
            if (r15 == 0) goto L_0x013d
            java.lang.String r2 = "!/"
            boolean r2 = r15.contains(r2)     // Catch:{ all -> 0x0060 }
            if (r2 == 0) goto L_0x013d
            java.lang.String r2 = "!/"
            r3 = 2
            java.lang.String[] r2 = r15.split(r2, r3)     // Catch:{ all -> 0x0060 }
            r3 = 0
            r4 = r2[r3]     // Catch:{ all -> 0x0060 }
            r5 = 1
            r2 = r2[r5]     // Catch:{ all -> 0x0060 }
            java.lang.String[] r5 = n     // Catch:{ all -> 0x0060 }
            int r6 = r5.length     // Catch:{ all -> 0x0060 }
        L_0x0101:
            if (r3 >= r6) goto L_0x013d
            r7 = r5[r3]     // Catch:{ all -> 0x0060 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0060 }
            r8.<init>()     // Catch:{ all -> 0x0060 }
            java.lang.String r10 = "lib"
            r8.append(r10)     // Catch:{ all -> 0x0060 }
            java.lang.String r10 = java.io.File.separator     // Catch:{ all -> 0x0060 }
            r8.append(r10)     // Catch:{ all -> 0x0060 }
            r8.append(r7)     // Catch:{ all -> 0x0060 }
            java.lang.String r10 = java.io.File.separator     // Catch:{ all -> 0x0060 }
            r8.append(r10)     // Catch:{ all -> 0x0060 }
            java.lang.String r10 = "libsg"
            r8.append(r10)     // Catch:{ all -> 0x0060 }
            r8.append(r0)     // Catch:{ all -> 0x0060 }
            java.lang.String r10 = ".so"
            r8.append(r10)     // Catch:{ all -> 0x0060 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0060 }
            boolean r8 = r8.equals(r2)     // Catch:{ all -> 0x0060 }
            if (r8 == 0) goto L_0x013a
            o = r7     // Catch:{ all -> 0x0060 }
            java.io.File r1 = r9.a((java.lang.String) r0, (java.io.File) r14, (java.lang.String) r4, (java.lang.String) r2)     // Catch:{ all -> 0x0060 }
            goto L_0x013d
        L_0x013a:
            int r3 = r3 + 1
            goto L_0x0101
        L_0x013d:
            if (r1 != 0) goto L_0x0143
            java.io.File r1 = r16.b((java.lang.String) r17)     // Catch:{ all -> 0x0060 }
        L_0x0143:
            if (r1 != 0) goto L_0x014b
            if (r19 == 0) goto L_0x014b
            java.io.File r1 = r9.a((java.lang.String) r0, (java.io.File) r14)     // Catch:{ all -> 0x0060 }
        L_0x014b:
            if (r1 != 0) goto L_0x0156
            if (r13 != 0) goto L_0x0154
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b
            r0.b()
        L_0x0154:
            r2 = 0
            return r2
        L_0x0156:
            r2 = 0
            java.lang.String r3 = r1.getAbsolutePath()     // Catch:{ all -> 0x0060 }
            java.lang.String r4 = ".zip"
            boolean r3 = r3.endsWith(r4)     // Catch:{ all -> 0x0060 }
            if (r3 == 0) goto L_0x016a
            com.alibaba.wireless.security.framework.d$a r12 = new com.alibaba.wireless.security.framework.d$a     // Catch:{ all -> 0x0060 }
            r12.<init>(r1, r14, r2, r13)     // Catch:{ all -> 0x0060 }
            goto L_0x020b
        L_0x016a:
            boolean r3 = com.alibaba.wireless.security.framework.utils.f.a()     // Catch:{ all -> 0x0060 }
            if (r3 == 0) goto L_0x0177
            com.alibaba.wireless.security.framework.d$a r12 = new com.alibaba.wireless.security.framework.d$a     // Catch:{ all -> 0x0060 }
            r12.<init>(r1, r14, r1, r13)     // Catch:{ all -> 0x0060 }
            goto L_0x020b
        L_0x0177:
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x0060 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0060 }
            r4.<init>()     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = "libsg"
            r4.append(r5)     // Catch:{ all -> 0x0060 }
            r4.append(r0)     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = "_"
            r4.append(r5)     // Catch:{ all -> 0x0060 }
            long r5 = r1.lastModified()     // Catch:{ all -> 0x0060 }
            r4.append(r5)     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = ".zip"
            r4.append(r5)     // Catch:{ all -> 0x0060 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0060 }
            r3.<init>(r14, r4)     // Catch:{ all -> 0x0060 }
            boolean r4 = r3.exists()     // Catch:{ all -> 0x0060 }
            if (r4 == 0) goto L_0x01b6
            boolean r4 = r9.b((java.io.File) r3)     // Catch:{ all -> 0x0060 }
            if (r4 == 0) goto L_0x01b6
            boolean r4 = r9.c(r3, r1)     // Catch:{ all -> 0x0060 }
            if (r4 != 0) goto L_0x01b6
            com.alibaba.wireless.security.framework.d$a r12 = new com.alibaba.wireless.security.framework.d$a     // Catch:{ all -> 0x0060 }
            r12.<init>(r3, r14, r1, r13)     // Catch:{ all -> 0x0060 }
            goto L_0x020b
        L_0x01b6:
            r3.delete()     // Catch:{ all -> 0x0060 }
            boolean r4 = r9.b((java.io.File) r1, (java.io.File) r3)     // Catch:{ all -> 0x0060 }
            if (r4 == 0) goto L_0x01c5
            com.alibaba.wireless.security.framework.d$a r12 = new com.alibaba.wireless.security.framework.d$a     // Catch:{ all -> 0x0060 }
            r12.<init>(r3, r14, r1, r13)     // Catch:{ all -> 0x0060 }
            goto L_0x020b
        L_0x01c5:
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x0060 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0060 }
            r4.<init>()     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = "libsg"
            r4.append(r5)     // Catch:{ all -> 0x0060 }
            r4.append(r0)     // Catch:{ all -> 0x0060 }
            java.lang.String r0 = "_cp"
            r4.append(r0)     // Catch:{ all -> 0x0060 }
            long r5 = r1.lastModified()     // Catch:{ all -> 0x0060 }
            r4.append(r5)     // Catch:{ all -> 0x0060 }
            java.lang.String r0 = ".zip"
            r4.append(r0)     // Catch:{ all -> 0x0060 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x0060 }
            r3.<init>(r14, r0)     // Catch:{ all -> 0x0060 }
            boolean r0 = r3.exists()     // Catch:{ all -> 0x0060 }
            if (r0 == 0) goto L_0x01fe
            long r4 = r3.length()     // Catch:{ all -> 0x0060 }
            long r6 = r1.length()     // Catch:{ all -> 0x0060 }
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 == 0) goto L_0x0204
        L_0x01fe:
            boolean r0 = r9.a((java.io.File) r1, (java.io.File) r3)     // Catch:{ all -> 0x0060 }
            if (r0 == 0) goto L_0x020a
        L_0x0204:
            com.alibaba.wireless.security.framework.d$a r12 = new com.alibaba.wireless.security.framework.d$a     // Catch:{ all -> 0x0060 }
            r12.<init>(r3, r14, r1, r13)     // Catch:{ all -> 0x0060 }
            goto L_0x020b
        L_0x020a:
            r12 = r2
        L_0x020b:
            if (r13 != 0) goto L_0x0212
            com.alibaba.wireless.security.framework.utils.c r0 = r9.b
            r0.b()
        L_0x0212:
            return r12
        L_0x0213:
            if (r13 != 0) goto L_0x021a
            com.alibaba.wireless.security.framework.utils.c r1 = r9.b
            r1.b()
        L_0x021a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.c(java.lang.String, java.lang.String, boolean):com.alibaba.wireless.security.framework.d$a");
    }

    private String c() {
        StringBuilder sb = new StringBuilder();
        File file = this.k;
        if (file == null || !file.exists() || !file.isDirectory()) {
            sb.append("not exists!");
        } else {
            try {
                sb.append(Operators.ARRAY_START_STR);
                File[] listFiles = file.listFiles();
                int i2 = 0;
                while (listFiles != null && i2 < listFiles.length) {
                    File file2 = listFiles[i2];
                    if (file2.getName().startsWith("libsg") && (file2.getName().endsWith("zip") || file2.getName().endsWith(".so"))) {
                        sb.append(file2.getName());
                        sb.append(Operators.BRACKET_START_STR);
                        sb.append(b(file2) + " , ");
                        sb.append(file2.length());
                        sb.append(") , ");
                    }
                    i2++;
                }
                sb.append(Operators.ARRAY_END_STR);
            } catch (Throwable unused) {
            }
        }
        return sb.toString();
    }

    private String c(String str) {
        if (str == null || str.length() <= 0) {
            return "";
        }
        File file = new File(str);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (b(file)) {
            sb.append("->");
            try {
                sb.append(file.getCanonicalPath());
            } catch (Exception unused) {
            }
        }
        sb.append(Operators.ARRAY_START);
        sb.append("exists:" + file.exists() + ",");
        sb.append("size:" + file.length() + ",");
        sb.append("canRead:" + file.canRead() + ",");
        sb.append("canWrite:" + file.canWrite() + ",");
        sb.append("totalSpace:" + file.getTotalSpace() + ",");
        sb.append("freeSpace:" + file.getFreeSpace() + ",");
        sb.append(Operators.ARRAY_END);
        return sb.toString();
    }

    private boolean c(File file) {
        if (file != null) {
            try {
                String absolutePath = file.getAbsolutePath();
                if (absolutePath != null) {
                    if (absolutePath.length() > 0) {
                        if (f.a(this.c) || !absolutePath.startsWith("/system/")) {
                            return true;
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private boolean c(File file, File file2) {
        try {
            return file.getCanonicalPath().equals(file2.getCanonicalPath());
        } catch (Exception e2) {
            com.alibaba.wireless.security.framework.utils.a.a("", e2);
            a(100046, 0, e2.toString(), file.getAbsolutePath(), file2.getAbsolutePath(), "", "");
            return false;
        }
    }

    private synchronized ISGPluginInfo d(String str, String str2, boolean z) throws SecException {
        StringBuilder sb;
        c cVar = this.d.get(str);
        if (cVar != null) {
            return cVar;
        }
        if (this.k == null || !this.k.exists()) {
            a();
        }
        a c2 = c(str, str2, z);
        if (!(c2 == null || c2.a == null)) {
            if (c2.a.exists()) {
                c a2 = a(str, c2, this.c, str2);
                if (a2 == null) {
                    if (c2.c != null) {
                        sb = new StringBuilder();
                        sb.append("src:");
                        sb.append(c(c2.c.getAbsolutePath()));
                    } else {
                        sb = new StringBuilder();
                        sb.append("zipfile:");
                        sb.append(c(c2.a.getAbsolutePath()));
                    }
                    a(100044, 110, "", str, str2, sb.toString(), "");
                    throw new SecException(str, 111);
                }
                this.d.put(str, a2);
                final String str3 = str + Operators.BRACKET_START_STR + a2.getVersion() + Operators.BRACKET_END_STR;
                final String a3 = a2.a("weakdependencies");
                if (str2.length() != 0) {
                    str3 = str2 + "->" + str3;
                }
                Looper myLooper = Looper.myLooper();
                if (myLooper == null || myLooper == Looper.getMainLooper()) {
                    com.alibaba.wireless.security.framework.utils.a.a("looper of current thread is null, try to create a new thread with a looper");
                    HandlerThread handlerThread = new HandlerThread("SGBackgroud");
                    handlerThread.start();
                    myLooper = handlerThread.getLooper();
                }
                new Handler(myLooper).postDelayed(new Runnable() {
                    public void run() {
                        try {
                            boolean unused = d.this.a(a3, str3, true);
                        } catch (SecException e) {
                            com.alibaba.wireless.security.framework.utils.a.a("load weak dependency", e);
                        }
                    }
                }, 60000);
                return a2;
            }
        }
        if (!z) {
            return null;
        }
        String str4 = "plugin " + str + " not existed";
        if (str2.length() != 0) {
            str4 = str4 + ", depended by " + str2;
        }
        a(100044, 110, "", str, str2, "", "");
        throw new SecException(str4, 110);
    }

    private File d(File file) {
        if (!file.exists() || !file.isDirectory() || !this.j) {
            return file;
        }
        File file2 = new File(file, "main");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.exists() ? file2 : file;
    }

    public void a(Context context, String str, String str2, boolean z, Object... objArr) {
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        this.c = context;
        this.h = str;
        this.j = f.c(this.c);
        this.f = z;
        UserTrackMethodJniBridge.init(this.c);
        if (str2 != null && !str2.isEmpty()) {
            this.g = str2;
        }
        try {
            a();
        } catch (SecException unused) {
        }
    }

    public synchronized <T> T getInterface(Class<T> cls) throws SecException {
        Object obj = this.a.get(cls);
        if (obj != null) {
            return cls.cast(obj);
        }
        String str = null;
        if (this.i != null && ((str = this.i.d()) == null || str.length() == 0)) {
            str = this.i.a(cls.getName());
        }
        if (str == null || str.length() == 0) {
            str = a((Class<?>) cls);
        }
        if (str == null || str.length() == 0) {
            throw new SecException(150);
        }
        ISGPluginInfo pluginInfo = getPluginInfo(str);
        if (pluginInfo != null) {
            T t = pluginInfo.getSGPluginEntry().getInterface(cls);
            if (t != null) {
                this.a.put(cls, t);
                return cls.cast(t);
            }
            String name = cls.getName();
            a(100045, 112, "", name, str + Operators.BRACKET_START_STR + pluginInfo.getVersion() + Operators.BRACKET_END_STR, c(pluginInfo.getPluginPath()), "");
            throw new SecException(112);
        }
        throw new SecException(110);
    }

    public String getMainPluginName() {
        return "main";
    }

    public ISGPluginInfo getPluginInfo(String str) throws SecException {
        return d(str, "", true);
    }

    public IRouterComponent getRouter() {
        return this.e;
    }
}
