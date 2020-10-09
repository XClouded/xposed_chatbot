package com.huawei.hianalytics.log.g;

import android.text.TextUtils;
import com.huawei.hianalytics.a.d;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.b.a;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public class f implements e {
    private com.huawei.hianalytics.log.c.a a;
    private String b;
    private String c;
    private Throwable d;

    private static class a implements Serializable, Comparator<File> {
        private a() {
        }

        /* renamed from: a */
        public int compare(File file, File file2) {
            return (int) (file.length() - file2.length());
        }
    }

    public f(com.huawei.hianalytics.log.c.a aVar, Throwable th, String str) {
        this.a = aVar;
        this.c = str + a.C0006a.b;
        this.d = th;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: java.io.Writer} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: java.io.Writer} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a() {
        /*
            r12 = this;
            java.io.File r0 = new java.io.File
            java.lang.String r1 = r12.c
            java.lang.String r2 = r12.b
            r0.<init>(r1, r2)
            boolean r1 = com.huawei.hianalytics.log.f.a.a((java.io.File) r0)
            if (r1 != 0) goto L_0x0020
            java.lang.String r0 = com.huawei.hianalytics.log.e.f.a()
            com.huawei.hianalytics.a.d.c(r0)
            java.lang.String r1 = r12.c
            int r2 = com.huawei.hianalytics.a.d.f()
            java.io.File r0 = com.huawei.hianalytics.log.f.a.a(r1, r0, r2)
        L_0x0020:
            if (r0 != 0) goto L_0x002a
            java.lang.String r0 = "LogWrite"
            java.lang.String r1 = "writerLog():Failed to create file!"
            com.huawei.hianalytics.g.b.d(r0, r1)
            return
        L_0x002a:
            r1 = 0
            r2 = 10
            r3 = 9
            r4 = 0
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x00bb, UnsupportedEncodingException -> 0x00b1, IOException -> 0x00aa, all -> 0x00a4 }
            r6 = 1
            r5.<init>(r0, r6)     // Catch:{ FileNotFoundException -> 0x00bb, UnsupportedEncodingException -> 0x00b1, IOException -> 0x00aa, all -> 0x00a4 }
            java.io.OutputStreamWriter r0 = new java.io.OutputStreamWriter     // Catch:{ FileNotFoundException -> 0x00a2, UnsupportedEncodingException -> 0x00a0, IOException -> 0x009e, all -> 0x009b }
            java.lang.String r7 = "UTF-8"
            r0.<init>(r5, r7)     // Catch:{ FileNotFoundException -> 0x00a2, UnsupportedEncodingException -> 0x00a0, IOException -> 0x009e, all -> 0x009b }
            java.io.BufferedWriter r7 = new java.io.BufferedWriter     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00b3, IOException -> 0x00ac }
            r7.<init>(r0)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00b3, IOException -> 0x00ac }
            java.util.Locale r1 = java.util.Locale.getDefault()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r8 = "%s: %s/%s: %s"
            r9 = 4
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r10 = com.huawei.hianalytics.log.e.f.b()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r9[r4] = r10     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            com.huawei.hianalytics.log.c.a r10 = r12.a     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r10 = r10.a()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r9[r6] = r10     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r6 = 2
            com.huawei.hianalytics.log.c.a r10 = r12.a     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r10 = r10.b()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r9[r6] = r10     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r6 = 3
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r10.<init>()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            com.huawei.hianalytics.log.c.a r11 = r12.a     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r11 = r11.c()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r10.append(r11)     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r11 = "\n"
            r10.append(r11)     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.Throwable r11 = r12.d     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r11 = android.util.Log.getStackTraceString(r11)     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r10.append(r11)     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r10 = r10.toString()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r9[r6] = r10     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            java.lang.String r1 = java.lang.String.format(r1, r8, r9)     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r7.append(r1)     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            r7.flush()     // Catch:{ FileNotFoundException -> 0x0099, UnsupportedEncodingException -> 0x0097, IOException -> 0x0095, all -> 0x0093 }
            com.huawei.hianalytics.log.e.d.a((int) r3, (java.io.Closeable) r7)
            goto L_0x00c5
        L_0x0093:
            r1 = move-exception
            goto L_0x00cf
        L_0x0095:
            r1 = r7
            goto L_0x00ac
        L_0x0097:
            r1 = r7
            goto L_0x00b3
        L_0x0099:
            r1 = r7
            goto L_0x00bd
        L_0x009b:
            r0 = move-exception
            r7 = r1
            goto L_0x00a7
        L_0x009e:
            r0 = r1
            goto L_0x00ac
        L_0x00a0:
            r0 = r1
            goto L_0x00b3
        L_0x00a2:
            r0 = r1
            goto L_0x00bd
        L_0x00a4:
            r0 = move-exception
            r5 = r1
            r7 = r5
        L_0x00a7:
            r1 = r0
            r0 = r7
            goto L_0x00cf
        L_0x00aa:
            r0 = r1
            r5 = r0
        L_0x00ac:
            java.lang.String r6 = "LogWrite"
            java.lang.String r7 = "writeLog append IO Exception !"
            goto L_0x00b7
        L_0x00b1:
            r0 = r1
            r5 = r0
        L_0x00b3:
            java.lang.String r6 = "LogWrite"
            java.lang.String r7 = "writeLog() OutputStreamWriter - Unsupported coding format"
        L_0x00b7:
            com.huawei.hianalytics.g.b.d(r6, r7)     // Catch:{ all -> 0x00cc }
            goto L_0x00c2
        L_0x00bb:
            r0 = r1
            r5 = r0
        L_0x00bd:
            java.lang.String r6 = "LogWrite"
            java.lang.String r7 = "writeLog() No files that need to be written!"
            goto L_0x00b7
        L_0x00c2:
            com.huawei.hianalytics.log.e.d.a((int) r3, (java.io.Closeable) r1)
        L_0x00c5:
            com.huawei.hianalytics.log.e.d.a((int) r2, (java.io.Closeable) r0)
            com.huawei.hianalytics.log.e.d.a((int) r4, (java.io.Closeable) r5)
            return
        L_0x00cc:
            r6 = move-exception
            r7 = r1
            r1 = r6
        L_0x00cf:
            com.huawei.hianalytics.log.e.d.a((int) r3, (java.io.Closeable) r7)
            com.huawei.hianalytics.log.e.d.a((int) r2, (java.io.Closeable) r0)
            com.huawei.hianalytics.log.e.d.a((int) r4, (java.io.Closeable) r5)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.log.g.f.a():void");
    }

    private void a(String str) {
        String str2;
        File file;
        this.b = d.d();
        if (TextUtils.isEmpty(this.b)) {
            File[] listFiles = new File(str).listFiles();
            com.huawei.hianalytics.log.e.a.a(str);
            if (listFiles == null || listFiles.length == 0) {
                str2 = com.huawei.hianalytics.log.e.f.a();
            } else {
                if (listFiles.length == 1) {
                    file = listFiles[0];
                } else {
                    File[] a2 = com.huawei.hianalytics.log.f.a.a(listFiles);
                    Arrays.sort(a2, new a());
                    file = a2[0];
                }
                str2 = file.getName();
            }
            this.b = str2;
        }
    }

    public void run() {
        File file = new File(this.c);
        if (file.exists()) {
            a(this.c);
        } else if (file.mkdirs()) {
            this.b = com.huawei.hianalytics.log.e.f.a();
            d.c(this.b);
        } else {
            b.d("LogWrite", "create logsfile fail!");
            return;
        }
        synchronized (f.class) {
            a();
        }
    }
}
