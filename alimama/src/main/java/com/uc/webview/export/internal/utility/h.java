package com.uc.webview.export.internal.utility;

import android.content.Context;
import androidx.core.view.InputDeviceCompat;
import com.facebook.imageutils.JfifUtil;
import com.uc.webview.export.Build;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.af;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.uc.startup.b;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.UByte;
import org.json.JSONObject;

/* compiled from: U4Source */
public final class h {
    public static String a = "lastucm";
    public static String b = "SO_DIR_PATH";
    public static String c = "RES_DIR_PATH";
    public static String d = "DATA_DIR_PATH";
    public static String e = "BUILD.TIME";
    public static String f = "COREIMPL_FILE_PATH";
    public static String g = "COREIMPL_ODEX_DIR_PATH";
    public static boolean h = true;
    public static String i = "quickpath";
    public static String j = "PKG_NAME";
    public static boolean k = true;
    public static final int[] l = {126, 147, 115, 241, 101, 198, JfifUtil.MARKER_RST7, 134};
    static br m;
    static a n = a.NUndefined;
    static Object o = new Object();

    /* compiled from: U4Source */
    enum a {
        NUndefined,
        NDisable,
        NNoFile,
        NEmptyFile,
        NEmptyLast,
        NNewZip,
        NNewDex,
        NNewLib,
        NNewKrl,
        NBadLast,
        NExceptions,
        YLastOK,
        NEmptyJSON,
        NMisMatchVersion
    }

    public static void a(br brVar, ConcurrentHashMap<String, Object> concurrentHashMap) {
        if (brVar == null) {
            new Exception("info should not be null").printStackTrace();
            return;
        }
        if (concurrentHashMap.get(UCCore.OPTION_DEX_FILE_PATH) != null) {
            brVar.setInitInfo(UCCore.OPTION_DEX_FILE_PATH, (String) concurrentHashMap.get(UCCore.OPTION_DEX_FILE_PATH));
        }
        if (concurrentHashMap.get(UCCore.OPTION_SO_FILE_PATH) != null) {
            brVar.setInitInfo(UCCore.OPTION_SO_FILE_PATH, (String) concurrentHashMap.get(UCCore.OPTION_SO_FILE_PATH));
        }
        if (concurrentHashMap.get(UCCore.OPTION_RES_FILE_PATH) != null) {
            brVar.setInitInfo(UCCore.OPTION_RES_FILE_PATH, (String) concurrentHashMap.get(UCCore.OPTION_RES_FILE_PATH));
        }
        if (concurrentHashMap.get(UCCore.OPTION_UCM_ZIP_FILE) != null) {
            brVar.setInitInfo(UCCore.OPTION_UCM_ZIP_FILE, (String) concurrentHashMap.get(UCCore.OPTION_UCM_ZIP_FILE));
        }
        if (concurrentHashMap.get(UCCore.OPTION_UCM_LIB_DIR) != null) {
            brVar.setInitInfo(UCCore.OPTION_UCM_LIB_DIR, (String) concurrentHashMap.get(UCCore.OPTION_UCM_LIB_DIR));
        }
        if (concurrentHashMap.get(UCCore.OPTION_UCM_UPD_URL) != null) {
            brVar.setInitInfo(UCCore.OPTION_UCM_UPD_URL, (String) concurrentHashMap.get(UCCore.OPTION_UCM_UPD_URL));
        }
        if (concurrentHashMap.get(UCCore.OPTION_UCM_KRL_DIR) != null) {
            brVar.setInitInfo(UCCore.OPTION_UCM_KRL_DIR, (String) concurrentHashMap.get(UCCore.OPTION_UCM_KRL_DIR));
        }
    }

    public static void a(br brVar, String str) {
        b.a(324);
        if (brVar == null) {
            new Exception("info should not be null").printStackTrace();
            return;
        }
        if (h) {
            Log.d("quickpath", "saveInfoToJsonFile path=" + str + ",isFromDisk=" + brVar.isFromDisk);
        }
        if (!brVar.isFromDisk) {
            try {
                a(a(brVar).toString(), str, k);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            b.a(325);
        }
    }

    private static JSONObject a(br brVar) {
        JSONObject jSONObject = new JSONObject();
        try {
            a(jSONObject, UCCore.OPTION_DEX_FILE_PATH, brVar.getInitInfo(UCCore.OPTION_DEX_FILE_PATH));
            a(jSONObject, UCCore.OPTION_SO_FILE_PATH, brVar.getInitInfo(UCCore.OPTION_SO_FILE_PATH));
            a(jSONObject, UCCore.OPTION_RES_FILE_PATH, brVar.getInitInfo(UCCore.OPTION_RES_FILE_PATH));
            a(jSONObject, UCCore.OPTION_UCM_ZIP_FILE, brVar.getInitInfo(UCCore.OPTION_UCM_ZIP_FILE));
            a(jSONObject, UCCore.OPTION_UCM_LIB_DIR, brVar.getInitInfo(UCCore.OPTION_UCM_LIB_DIR));
            a(jSONObject, UCCore.OPTION_UCM_UPD_URL, brVar.getInitInfo(UCCore.OPTION_UCM_UPD_URL));
            a(jSONObject, UCCore.OPTION_UCM_KRL_DIR, brVar.getInitInfo(UCCore.OPTION_UCM_KRL_DIR));
            a(jSONObject, j, brVar.pkgName);
            a(jSONObject, b, brVar.soDirPath);
            a(jSONObject, c, brVar.resDirPath);
            a(jSONObject, d, brVar.dataDir);
            a(jSONObject, e, Build.TIME);
            if (brVar.coreImplModule != null) {
                a(jSONObject, f, (String) brVar.coreImplModule.first);
                a(jSONObject, g, (String) brVar.coreImplModule.second);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return jSONObject;
    }

    public static String a(Context context) {
        return new File(context.getDir("ucmsdk", 0), a).getAbsolutePath();
    }

    public static void b(Context context) {
        if (context != null) {
            a(context, a(context));
        }
    }

    public static void c(Context context) {
        try {
            new File(a(context)).delete();
        } catch (Exception unused) {
        }
    }

    private static br a(Context context, String str) {
        if (m != null) {
            return m;
        }
        synchronized (o) {
            try {
                b.a(322);
                if (m != null) {
                    br brVar = m;
                    b.a(323);
                    return brVar;
                }
                JSONObject a2 = a(str);
                if (a2 == null) {
                    a(a.NEmptyJSON);
                    b.a(323);
                    return null;
                }
                br a3 = a(context, a2);
                m = a3;
                if (a3 == null) {
                    a(a.NEmptyLast);
                }
                b.a(323);
                return m;
            } catch (Throwable th) {
                b.a(323);
                throw th;
            }
        }
    }

    private static JSONObject a(String str) {
        if (!new File(str).exists()) {
            a(a.NNoFile);
            return null;
        }
        try {
            String a2 = a(str, k);
            if (a2 == null) {
                a(a.NEmptyFile);
                return null;
            }
            b.a(InputDeviceCompat.SOURCE_DPAD);
            JSONObject b2 = b(a2);
            b.a(514);
            return b2;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static String a(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (Exception unused) {
            return null;
        }
    }

    private static void a(JSONObject jSONObject, String str, String str2) {
        if (str2 != null && str != null) {
            try {
                jSONObject.put(str, str2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private static br a(Context context, JSONObject jSONObject) {
        UCMPackageInfo uCMPackageInfo;
        if (jSONObject == null) {
            return null;
        }
        try {
            b.a(512);
            uCMPackageInfo = new UCMPackageInfo(context, a(jSONObject, j), a(jSONObject, b), a(jSONObject, c), a(jSONObject, d), (String) null, (String) null, a(jSONObject, f), a(jSONObject, g), false, true);
            try {
                if (a(jSONObject, e) != null) {
                    uCMPackageInfo.setInitInfo(e, a(jSONObject, e));
                }
                if (a(jSONObject, UCCore.OPTION_DEX_FILE_PATH) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_DEX_FILE_PATH, a(jSONObject, UCCore.OPTION_DEX_FILE_PATH));
                }
                if (a(jSONObject, UCCore.OPTION_SO_FILE_PATH) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_SO_FILE_PATH, a(jSONObject, UCCore.OPTION_SO_FILE_PATH));
                }
                if (a(jSONObject, UCCore.OPTION_RES_FILE_PATH) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_RES_FILE_PATH, a(jSONObject, UCCore.OPTION_RES_FILE_PATH));
                }
                if (a(jSONObject, UCCore.OPTION_UCM_ZIP_FILE) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_UCM_ZIP_FILE, a(jSONObject, UCCore.OPTION_UCM_ZIP_FILE));
                }
                if (a(jSONObject, UCCore.OPTION_UCM_LIB_DIR) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_UCM_LIB_DIR, a(jSONObject, UCCore.OPTION_UCM_LIB_DIR));
                }
                if (a(jSONObject, UCCore.OPTION_UCM_UPD_URL) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_UCM_UPD_URL, a(jSONObject, UCCore.OPTION_UCM_UPD_URL));
                }
                if (a(jSONObject, UCCore.OPTION_UCM_KRL_DIR) != null) {
                    uCMPackageInfo.setInitInfo(UCCore.OPTION_UCM_KRL_DIR, a(jSONObject, UCCore.OPTION_UCM_KRL_DIR));
                }
                uCMPackageInfo.isFromDisk = true;
                b.a(515);
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return uCMPackageInfo;
            }
        } catch (Exception e3) {
            e = e3;
            uCMPackageInfo = null;
            e.printStackTrace();
            return uCMPackageInfo;
        }
        return uCMPackageInfo;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        return null;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.json.JSONObject b(java.lang.String r2) {
        /*
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r2)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ ClassCastException | Throwable | JSONException -> 0x0010, all -> 0x000e }
            r0.<init>(r2)     // Catch:{ ClassCastException | Throwable | JSONException -> 0x0010, all -> 0x000e }
            goto L_0x0011
        L_0x000e:
            r2 = move-exception
            throw r2
        L_0x0010:
            r0 = r1
        L_0x0011:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.h.b(java.lang.String):org.json.JSONObject");
    }

    private static byte[] a(byte[] bArr, int[] iArr) {
        if (bArr == null || iArr == null || iArr.length != 8) {
            return null;
        }
        int length = bArr.length;
        try {
            byte[] bArr2 = new byte[(length + 2)];
            byte b2 = 0;
            for (int i2 = 0; i2 < length; i2++) {
                byte b3 = bArr[i2];
                bArr2[i2] = (byte) (iArr[i2 % 8] ^ b3);
                b2 = (byte) (b2 ^ b3);
            }
            bArr2[length] = (byte) (iArr[0] ^ b2);
            bArr2[length + 1] = (byte) (iArr[1] ^ b2);
            return bArr2;
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] b(byte[] bArr, int[] iArr) {
        if (bArr.length < 2 || iArr == null || iArr.length != 8) {
            return null;
        }
        int length = bArr.length - 2;
        try {
            byte[] bArr2 = new byte[length];
            byte b2 = 0;
            for (int i2 = 0; i2 < length; i2++) {
                byte b3 = (byte) (bArr[i2] ^ iArr[i2 % 8]);
                bArr2[i2] = b3;
                b2 = (byte) (b2 ^ b3);
            }
            if (bArr[length] == ((byte) ((iArr[0] ^ b2) & UByte.MAX_VALUE)) && bArr[length + 1] == ((byte) ((iArr[1] ^ b2) & UByte.MAX_VALUE))) {
                return bArr2;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    private static boolean a(String str, String str2, boolean z) {
        if (h) {
            String str3 = i;
            Log.d(str3, "saveStringToFile str=" + str);
        }
        try {
            a(str.getBytes("UTF-8"), str2, z);
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return true;
        }
    }

    private static String a(String str, boolean z) {
        byte[] b2 = b(str, z);
        if (b2 == null) {
            return null;
        }
        try {
            return new String(b2, "UTF-8");
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static boolean a(byte[] bArr, String str, boolean z) {
        try {
            File file = new File(str);
            File file2 = new File(file.getParent());
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            if (z) {
                bArr = a(bArr, l);
            }
            fileOutputStream.write(bArr);
            fileOutputStream.close();
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private static byte[] b(String str, boolean z) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[((int) file.length())];
            fileInputStream.read(bArr);
            fileInputStream.close();
            return z ? b(bArr, l) : bArr;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static br a() {
        return m;
    }

    public static boolean b() {
        return m != null && m.isFromDisk;
    }

    public static boolean a(Context context, ConcurrentHashMap<String, Object> concurrentHashMap) {
        if (context == null) {
            return true;
        }
        try {
            if (!af.b()) {
                return true;
            }
            if (!n.equals(a.YLastOK) && !n.equals(a.NUndefined)) {
                return true;
            }
            if (m == null) {
                m = a(context, a(context));
            }
            if (m == null) {
                return true;
            }
            String initInfo = m.getInitInfo(UCCore.OPTION_UCM_UPD_URL);
            String str = (String) concurrentHashMap.get(UCCore.OPTION_UCM_UPD_URL);
            if (str == null || str.equals(initInfo)) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void a(a aVar) {
        n = aVar;
        if (h) {
            String str = i;
            Log.d(str, "statQuickPathStatus st=" + aVar);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(aVar.ordinal());
        b.a(501, sb.toString());
    }

    public static boolean b(Context context, ConcurrentHashMap<String, Object> concurrentHashMap) {
        if (!af.b()) {
            a(a.NDisable);
            if (h) {
                String str = i;
                Log.d(str, "willReuseOldCore false. rz=" + a.NDisable);
            }
            return false;
        } else if (n.equals(a.YLastOK)) {
            if (!h) {
                return true;
            }
            Log.d(i, "willReuseOldCore true. rz=okincache");
            return true;
        } else if (n.equals(a.YLastOK) || n.equals(a.NUndefined)) {
            boolean c2 = c(context, concurrentHashMap);
            d.a().a("gk_quick_path", Boolean.valueOf(c2));
            return c2;
        } else {
            if (h) {
                String str2 = i;
                Log.d(str2, "willReuseOldCore false. rz=" + n);
            }
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:71:0x0161 A[Catch:{ Throwable -> 0x0178 }] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0162 A[Catch:{ Throwable -> 0x0178 }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0165 A[Catch:{ Throwable -> 0x0178 }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x016b A[Catch:{ Throwable -> 0x0178 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean c(android.content.Context r16, java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object> r17) {
        /*
            r0 = r16
            r1 = r17
            r2 = 0
            if (r0 != 0) goto L_0x0008
            return r2
        L_0x0008:
            boolean r3 = com.uc.webview.export.internal.setup.af.b()     // Catch:{ Throwable -> 0x0178 }
            if (r3 != 0) goto L_0x0014
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NDisable     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            return r2
        L_0x0014:
            com.uc.webview.export.internal.setup.br r3 = m     // Catch:{ Throwable -> 0x0178 }
            if (r3 != 0) goto L_0x0022
            java.lang.String r3 = a((android.content.Context) r16)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r0 = a((android.content.Context) r0, (java.lang.String) r3)     // Catch:{ Throwable -> 0x0178 }
            m = r0     // Catch:{ Throwable -> 0x0178 }
        L_0x0022:
            com.uc.webview.export.internal.setup.br r0 = m     // Catch:{ Throwable -> 0x0178 }
            if (r0 != 0) goto L_0x0032
            boolean r0 = h     // Catch:{ Throwable -> 0x0178 }
            if (r0 == 0) goto L_0x0031
            java.lang.String r0 = i     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r1 = "willReuseOldCoreInternal false. null"
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x0178 }
        L_0x0031:
            return r2
        L_0x0032:
            com.uc.webview.export.internal.setup.br r0 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r3 = "ucmZipFile"
            java.lang.String r0 = r0.getInitInfo(r3)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r3 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r4 = "dexFilePath"
            java.lang.String r3 = r3.getInitInfo(r4)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r4 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r5 = "soFilePath"
            java.lang.String r4 = r4.getInitInfo(r5)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r5 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r6 = "resFilePath"
            java.lang.String r5 = r5.getInitInfo(r6)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r6 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r7 = "ucmLibDir"
            java.lang.String r6 = r6.getInitInfo(r7)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r7 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r8 = "ucmKrlDir"
            java.lang.String r7 = r7.getInitInfo(r8)     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.setup.br r8 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r9 = e     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r8 = r8.getInitInfo(r9)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r9 = "ucmZipFile"
            java.lang.Object r9 = r1.get(r9)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r10 = "dexFilePath"
            java.lang.Object r10 = r1.get(r10)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r11 = "soFilePath"
            java.lang.Object r11 = r1.get(r11)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r12 = "resFilePath"
            java.lang.Object r12 = r1.get(r12)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r13 = "ucmLibDir"
            java.lang.Object r13 = r1.get(r13)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r14 = "ucmKrlDir"
            java.lang.Object r1 = r1.get(r14)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x0178 }
            boolean r14 = h     // Catch:{ Throwable -> 0x0178 }
            if (r14 == 0) goto L_0x00d6
            java.lang.String r14 = i     // Catch:{ Throwable -> 0x0178 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r2 = "buildtime="
            r15.<init>(r2)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r2 = com.uc.webview.export.Build.TIME     // Catch:{ Throwable -> 0x0178 }
            r15.append(r2)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r2 = ",lasttime="
            r15.append(r2)     // Catch:{ Throwable -> 0x0178 }
            r15.append(r8)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r2 = r15.toString()     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.utility.Log.d(r14, r2)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r2 = i     // Catch:{ Throwable -> 0x0178 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r15 = "zipFilePath="
            r14.<init>(r15)     // Catch:{ Throwable -> 0x0178 }
            r14.append(r9)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r15 = ",last="
            r14.append(r15)     // Catch:{ Throwable -> 0x0178 }
            r14.append(r0)     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r14 = r14.toString()     // Catch:{ Throwable -> 0x0178 }
            com.uc.webview.export.internal.utility.Log.d(r2, r14)     // Catch:{ Throwable -> 0x0178 }
        L_0x00d6:
            if (r8 == 0) goto L_0x0171
            java.lang.String r2 = com.uc.webview.export.Build.TIME     // Catch:{ Throwable -> 0x0178 }
            boolean r2 = r8.equals(r2)     // Catch:{ Throwable -> 0x0178 }
            if (r2 != 0) goto L_0x00e2
            goto L_0x0171
        L_0x00e2:
            if (r9 == 0) goto L_0x00f1
            boolean r2 = r9.equals(r0)     // Catch:{ Throwable -> 0x0178 }
            if (r2 != 0) goto L_0x00f1
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NNewZip     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x00f1:
            if (r10 == 0) goto L_0x0100
            boolean r2 = r10.equals(r3)     // Catch:{ Throwable -> 0x0178 }
            if (r2 != 0) goto L_0x0100
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NNewDex     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x0100:
            if (r11 == 0) goto L_0x010f
            boolean r2 = r11.equals(r4)     // Catch:{ Throwable -> 0x0178 }
            if (r2 != 0) goto L_0x010f
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NNewDex     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x010f:
            if (r12 == 0) goto L_0x011e
            boolean r2 = r12.equals(r5)     // Catch:{ Throwable -> 0x0178 }
            if (r2 != 0) goto L_0x011e
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NNewDex     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x011e:
            if (r13 == 0) goto L_0x012d
            boolean r2 = r13.equals(r6)     // Catch:{ Throwable -> 0x0178 }
            if (r2 != 0) goto L_0x012d
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NNewLib     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x012d:
            if (r1 == 0) goto L_0x013c
            boolean r1 = r1.equals(r7)     // Catch:{ Throwable -> 0x0178 }
            if (r1 != 0) goto L_0x013c
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NNewKrl     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x013c:
            r2 = 1
            if (r0 != 0) goto L_0x014c
            if (r3 != 0) goto L_0x014c
            if (r4 != 0) goto L_0x014c
            if (r5 != 0) goto L_0x014c
            if (r6 != 0) goto L_0x014c
            if (r7 == 0) goto L_0x014a
            goto L_0x014c
        L_0x014a:
            r0 = 0
            goto L_0x014d
        L_0x014c:
            r0 = 1
        L_0x014d:
            if (r0 != 0) goto L_0x0163
            com.uc.webview.export.internal.setup.br r0 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r0 = r0.soDirPath     // Catch:{ Throwable -> 0x0178 }
            if (r0 != 0) goto L_0x0163
            com.uc.webview.export.internal.setup.br r0 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r0 = r0.resDirPath     // Catch:{ Throwable -> 0x0178 }
            if (r0 != 0) goto L_0x0163
            com.uc.webview.export.internal.setup.br r0 = m     // Catch:{ Throwable -> 0x0178 }
            java.lang.String r0 = r0.dataDir     // Catch:{ Throwable -> 0x0178 }
            if (r0 == 0) goto L_0x0162
            goto L_0x0163
        L_0x0162:
            r2 = 0
        L_0x0163:
            if (r2 != 0) goto L_0x016b
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NBadLast     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            goto L_0x0170
        L_0x016b:
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.YLastOK     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
        L_0x0170:
            return r2
        L_0x0171:
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NMisMatchVersion     // Catch:{ Throwable -> 0x0178 }
            a((com.uc.webview.export.internal.utility.h.a) r0)     // Catch:{ Throwable -> 0x0178 }
            r1 = 0
            return r1
        L_0x0178:
            r0 = move-exception
            r0.printStackTrace()
            com.uc.webview.export.internal.utility.h$a r0 = com.uc.webview.export.internal.utility.h.a.NExceptions
            a((com.uc.webview.export.internal.utility.h.a) r0)
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.h.c(android.content.Context, java.util.concurrent.ConcurrentHashMap):boolean");
    }
}
