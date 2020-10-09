package com.huawei.hianalytics.f.g;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Pair;
import com.huawei.hianalytics.g.b;
import java.io.File;

public final class a {
    private static a d;
    private String a;
    private String b;
    private Context c;
    private String e;

    private a(Context context) {
        this.c = context;
        this.e = context.getFilesDir().getPath();
    }

    public static a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (d == null) {
                d = new a(context);
            }
            aVar = d;
        }
        return aVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x005d A[SYNTHETIC, Splitter:B:35:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0074 A[SYNTHETIC, Splitter:B:45:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0082 A[SYNTHETIC, Splitter:B:51:0x0082] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(java.io.File r9) {
        /*
            r8 = this;
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0052 }
            r1.<init>(r9)     // Catch:{ FileNotFoundException -> 0x0069, IOException -> 0x0052 }
            com.huawei.hianalytics.util.a r9 = new com.huawei.hianalytics.util.a     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            r0 = 1024(0x400, float:1.435E-42)
            r9.<init>(r0)     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            byte[] r0 = new byte[r0]     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
        L_0x000f:
            int r2 = r1.read(r0)     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            long r3 = (long) r2     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            r5 = -1
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 == 0) goto L_0x001e
            r9.a(r0, r2)     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            goto L_0x000f
        L_0x001e:
            int r0 = r9.a()     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            if (r0 != 0) goto L_0x0032
            java.lang.String r9 = ""
            r1.close()     // Catch:{ IOException -> 0x002a }
            goto L_0x0031
        L_0x002a:
            java.lang.String r0 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "inputStream Not closed!"
            com.huawei.hianalytics.g.b.c(r0, r1)
        L_0x0031:
            return r9
        L_0x0032:
            java.lang.String r0 = new java.lang.String     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            byte[] r9 = r9.b()     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            java.lang.String r2 = "UTF-8"
            r0.<init>(r9, r2)     // Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b, all -> 0x0049 }
            r1.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0048
        L_0x0041:
            java.lang.String r9 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "inputStream Not closed!"
            com.huawei.hianalytics.g.b.c(r9, r1)
        L_0x0048:
            return r0
        L_0x0049:
            r9 = move-exception
            goto L_0x0080
        L_0x004b:
            r0 = r1
            goto L_0x0052
        L_0x004d:
            r0 = r1
            goto L_0x0069
        L_0x004f:
            r9 = move-exception
            r1 = r0
            goto L_0x0080
        L_0x0052:
            java.lang.String r9 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "getInfoFromFile(): stream.read or new string exception"
            com.huawei.hianalytics.g.b.c(r9, r1)     // Catch:{ all -> 0x004f }
            java.lang.String r9 = ""
            if (r0 == 0) goto L_0x0068
            r0.close()     // Catch:{ IOException -> 0x0061 }
            goto L_0x0068
        L_0x0061:
            java.lang.String r0 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "inputStream Not closed!"
            com.huawei.hianalytics.g.b.c(r0, r1)
        L_0x0068:
            return r9
        L_0x0069:
            java.lang.String r9 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "getInfoFromFile(): No files need to be read"
            com.huawei.hianalytics.g.b.c(r9, r1)     // Catch:{ all -> 0x004f }
            java.lang.String r9 = ""
            if (r0 == 0) goto L_0x007f
            r0.close()     // Catch:{ IOException -> 0x0078 }
            goto L_0x007f
        L_0x0078:
            java.lang.String r0 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "inputStream Not closed!"
            com.huawei.hianalytics.g.b.c(r0, r1)
        L_0x007f:
            return r9
        L_0x0080:
            if (r1 == 0) goto L_0x008d
            r1.close()     // Catch:{ IOException -> 0x0086 }
            goto L_0x008d
        L_0x0086:
            java.lang.String r0 = "HiAnalytics/event/aes128key"
            java.lang.String r1 = "inputStream Not closed!"
            com.huawei.hianalytics.g.b.c(r0, r1)
        L_0x008d:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.f.g.a.a(java.io.File):java.lang.String");
    }

    private String a(File file, String str) {
        if (!file.exists()) {
            return "";
        }
        return a(new File(file.getPath(), "hianalytics_" + str));
    }

    private String a(String str, String str2) {
        File file;
        String a2 = a(new File(f() + File.separator + str), str);
        if (TextUtils.isEmpty(a2)) {
            a2 = a(new File(f() + File.separator + "hianalytics" + File.separator + str2), str2);
            if (TextUtils.isEmpty(a2)) {
                a2 = e.b();
                File file2 = new File(f() + File.separator + "hianalytics" + File.separator + str2);
                if (!file2.exists() && file2.mkdirs()) {
                    b.c("HiAnalytics/event", "The secret key file creates the OK!");
                }
                file = new File(f() + File.separator + "hianalytics" + File.separator + str2, "hianalytics_" + str2);
            }
            return a2;
        }
        File file3 = new File(f() + File.separator + str);
        d.a(file3);
        if (file3.isDirectory() && file3.delete()) {
            b.c("HiAnalytics/event", "The secret key file is Directory del! change new file");
        }
        File file4 = new File(f() + File.separator + "hianalytics" + File.separator + str2);
        if (!file4.exists() && file4.mkdirs()) {
            b.c("HiAnalytics/event", "The secret key file creates the OK!");
        }
        file = new File(f() + File.separator + "hianalytics" + File.separator + str2, "hianalytics_" + str2);
        b(file, a2);
        return a2;
    }

    private void a(SharedPreferences sharedPreferences, String str) {
        byte[] a2 = c.a();
        long currentTimeMillis = System.currentTimeMillis();
        g.a(sharedPreferences, "PrivacyData", (Object) c.a(a2, c.a(d(), a2, str)));
        g.a(sharedPreferences, "flashKeyTime", (Object) Long.valueOf(currentTimeMillis));
    }

    public static String b() {
        String a2 = i.a();
        String a3 = com.huawei.hianalytics.f.a.a.a();
        String c2 = e.c();
        return i.a(a2 + a3 + c2, 4);
    }

    private String b(Context context) {
        String str = (String) g.b(g.c(context, "analytics_key"), "analytics_key", "");
        if (TextUtils.isEmpty(str)) {
            SharedPreferences c2 = g.c(context, "Privacy_MY");
            String str2 = (String) g.b(c2, "PrivacyData", "");
            if (TextUtils.isEmpty(str2)) {
                String a2 = e.a();
                a(c2, a2);
                return a2;
            }
            Pair<byte[], String> a3 = c.a(str2);
            return c.b(d(), (byte[]) a3.first, (String) a3.second);
        }
        Pair<byte[], String> a4 = c.a(str);
        String b2 = c.b(d(), (byte[]) a4.first, (String) a4.second);
        if (!TextUtils.isEmpty(b2)) {
            return b2;
        }
        String a5 = e.a();
        a(g.c(context, "Privacy_MY"), a5);
        return a5;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0028 A[Catch:{ all -> 0x001d }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0040 A[SYNTHETIC, Splitter:B:29:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void b(java.io.File r3, java.lang.String r4) {
        /*
            r2 = this;
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0029, IOException -> 0x001f }
            r1.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0029, IOException -> 0x001f }
            java.lang.String r3 = "UTF-8"
            byte[] r3 = r4.getBytes(r3)     // Catch:{ FileNotFoundException -> 0x001b, IOException -> 0x0019, all -> 0x0016 }
            r1.write(r3)     // Catch:{ FileNotFoundException -> 0x001b, IOException -> 0x0019, all -> 0x0016 }
            r1.flush()     // Catch:{ FileNotFoundException -> 0x001b, IOException -> 0x0019, all -> 0x0016 }
            r1.close()     // Catch:{ IOException -> 0x0036 }
            goto L_0x003d
        L_0x0016:
            r3 = move-exception
            r0 = r1
            goto L_0x003e
        L_0x0019:
            r0 = r1
            goto L_0x001f
        L_0x001b:
            r0 = r1
            goto L_0x0029
        L_0x001d:
            r3 = move-exception
            goto L_0x003e
        L_0x001f:
            java.lang.String r3 = "HiAnalytics/event/aes128key"
            java.lang.String r4 = "saveInfoToFile(): io exc from write info to file!"
            com.huawei.hianalytics.g.b.c(r3, r4)     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x003d
            goto L_0x0032
        L_0x0029:
            java.lang.String r3 = "HiAnalytics/event/aes128key"
            java.lang.String r4 = "saveInfoToFile(): No files need to be read"
            com.huawei.hianalytics.g.b.c(r3, r4)     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x003d
        L_0x0032:
            r0.close()     // Catch:{ IOException -> 0x0036 }
            goto L_0x003d
        L_0x0036:
            java.lang.String r3 = "HiAnalytics/event/aes128key"
            java.lang.String r4 = "OutputStream not closed"
            com.huawei.hianalytics.g.b.c(r3, r4)
        L_0x003d:
            return
        L_0x003e:
            if (r0 == 0) goto L_0x004b
            r0.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x004b
        L_0x0044:
            java.lang.String r4 = "HiAnalytics/event/aes128key"
            java.lang.String r0 = "OutputStream not closed"
            com.huawei.hianalytics.g.b.c(r4, r0)
        L_0x004b:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.f.g.a.b(java.io.File, java.lang.String):void");
    }

    private void c() {
        b(new File(f() + File.separator + "hianalytics" + File.separator + "aprpap", "hianalytics_aprpap"), e.b());
        b(new File(f() + File.separator + "hianalytics" + File.separator + "febdoc", "hianalytics_febdoc"), e.b());
        b(new File(f() + File.separator + "hianalytics" + File.separator + "marfil", "hianalytics_marfil"), e.b());
        b(new File(f() + File.separator + "hianalytics" + File.separator + "maywnj", "hianalytics_maywnj"), e.b());
    }

    private String d() {
        if (TextUtils.isEmpty(this.a)) {
            this.a = e();
        }
        return this.a;
    }

    private String e() {
        return i.a(b(), a("secondAssembly", "aprpap"), a("thirdAssembly", "febdoc"), a("fourthAssembly", "marfil"), a("fiveAssembly", "maywnj"));
    }

    private String f() {
        return this.e;
    }

    public String a() {
        if (TextUtils.isEmpty(this.b)) {
            this.b = b(this.c);
        }
        return this.b;
    }

    public void a(String str, int i) {
        SharedPreferences sharedPreferences;
        if (!TextUtils.isEmpty(str) && i == 1) {
            long longValue = ((Long) g.b(g.c(this.c, "analytics_key"), "flashKeyTime", -1L)).longValue();
            if (longValue == -1) {
                sharedPreferences = g.c(this.c, "Privacy_MY");
                longValue = ((Long) g.b(sharedPreferences, "flashKeyTime", -1L)).longValue();
            } else {
                g.a(this.c, "../shared_prefs/", "analytics_key");
                sharedPreferences = g.c(this.c, "Privacy_MY");
                a(sharedPreferences, str);
                this.b = str;
            }
            if (System.currentTimeMillis() - longValue > 43200000) {
                this.b = str;
                long longValue2 = ((Long) g.b(sharedPreferences, "assemblyFlash", -1L)).longValue();
                if (longValue2 == -1) {
                    g.a(sharedPreferences, "assemblyFlash", (Object) Long.valueOf(System.currentTimeMillis()));
                } else if (System.currentTimeMillis() - longValue2 > 31536000000L) {
                    c();
                    g.a(sharedPreferences, "assemblyFlash", (Object) Long.valueOf(System.currentTimeMillis()));
                    this.a = e();
                }
                a(sharedPreferences, str);
            }
        }
    }
}
