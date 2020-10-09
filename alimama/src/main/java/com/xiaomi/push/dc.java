package com.xiaomi.push;

import android.annotation.SuppressLint;
import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dc {
    private static String a = "/MiPushLog";

    /* renamed from: a  reason: collision with other field name */
    private int f212a;
    @SuppressLint({"SimpleDateFormat"})

    /* renamed from: a  reason: collision with other field name */
    private final SimpleDateFormat f213a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /* renamed from: a  reason: collision with other field name */
    private ArrayList<File> f214a = new ArrayList<>();

    /* renamed from: a  reason: collision with other field name */
    private boolean f215a;
    private int b = 2097152;

    /* renamed from: b  reason: collision with other field name */
    private String f216b;
    private String c;

    dc() {
    }

    private void a(BufferedReader bufferedReader, BufferedWriter bufferedWriter, Pattern pattern) {
        char[] cArr = new char[4096];
        int read = bufferedReader.read(cArr);
        boolean z = false;
        while (read != -1 && !z) {
            String str = new String(cArr, 0, read);
            Matcher matcher = pattern.matcher(str);
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i >= read || !matcher.find(i)) {
                    break;
                }
                int start = matcher.start();
                String substring = str.substring(start, this.f216b.length() + start);
                if (this.f215a) {
                    if (substring.compareTo(this.c) > 0) {
                        read = start;
                        z = true;
                        break;
                    }
                } else if (substring.compareTo(this.f216b) >= 0) {
                    this.f215a = true;
                    i2 = start;
                }
                int indexOf = str.indexOf(10, start);
                if (indexOf == -1) {
                    indexOf = this.f216b.length();
                }
                i = start + indexOf;
            }
            if (this.f215a) {
                int i3 = read - i2;
                this.f212a += i3;
                if (z) {
                    bufferedWriter.write(cArr, i2, i3);
                    return;
                }
                bufferedWriter.write(cArr, i2, i3);
                if (this.f212a > this.b) {
                    return;
                }
            }
            read = bufferedReader.read(cArr);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v18, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v19, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v20, resolved type: java.io.BufferedWriter} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.io.File r8) {
        /*
            r7 = this;
            java.lang.String r0 = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"
            java.util.regex.Pattern r0 = java.util.regex.Pattern.compile(r0)
            r1 = 0
            java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ FileNotFoundException -> 0x00d5, IOException -> 0x00bd, all -> 0x00ba }
            java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ FileNotFoundException -> 0x00d5, IOException -> 0x00bd, all -> 0x00ba }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x00d5, IOException -> 0x00bd, all -> 0x00ba }
            r4.<init>(r8)     // Catch:{ FileNotFoundException -> 0x00d5, IOException -> 0x00bd, all -> 0x00ba }
            r3.<init>(r4)     // Catch:{ FileNotFoundException -> 0x00d5, IOException -> 0x00bd, all -> 0x00ba }
            r2.<init>(r3)     // Catch:{ FileNotFoundException -> 0x00d5, IOException -> 0x00bd, all -> 0x00ba }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8.<init>()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "model :"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = android.os.Build.MODEL     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "; os :"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = android.os.Build.VERSION.INCREMENTAL     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "; uid :"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = com.xiaomi.push.service.ba.a()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "; lng :"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.util.Locale r3 = java.util.Locale.getDefault()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = r3.toString()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "; sdk :"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r3 = 38
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "; andver :"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r3 = "\n"
            r8.append(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r8 = r8.toString()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r2.write(r8)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r8 = 0
            r7.f212a = r8     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.util.ArrayList<java.io.File> r8 = r7.f214a     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.util.Iterator r8 = r8.iterator()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
        L_0x0074:
            boolean r3 = r8.hasNext()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            if (r3 == 0) goto L_0x009e
            java.lang.Object r3 = r8.next()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.io.File r3 = (java.io.File) r3     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r6.<init>(r3)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r5.<init>(r6)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r7.a(r4, r2, r0)     // Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x009a, all -> 0x0097 }
            r4.close()     // Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x009a, all -> 0x0097 }
            r1 = r4
            goto L_0x0074
        L_0x0097:
            r8 = move-exception
            goto L_0x00f8
        L_0x009a:
            r8 = move-exception
            goto L_0x00b4
        L_0x009c:
            r8 = move-exception
            goto L_0x00b8
        L_0x009e:
            com.xiaomi.push.cu r8 = com.xiaomi.push.cu.a()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            java.lang.String r8 = r8.c()     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            r2.write(r8)     // Catch:{ FileNotFoundException -> 0x00b6, IOException -> 0x00b2, all -> 0x00b0 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            goto L_0x00f5
        L_0x00b0:
            r8 = move-exception
            goto L_0x00f9
        L_0x00b2:
            r8 = move-exception
            r4 = r1
        L_0x00b4:
            r1 = r2
            goto L_0x00bf
        L_0x00b6:
            r8 = move-exception
            r4 = r1
        L_0x00b8:
            r1 = r2
            goto L_0x00d7
        L_0x00ba:
            r8 = move-exception
            r2 = r1
            goto L_0x00f9
        L_0x00bd:
            r8 = move-exception
            r4 = r1
        L_0x00bf:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f6 }
            r0.<init>()     // Catch:{ all -> 0x00f6 }
            java.lang.String r2 = "LOG: filter error = "
            r0.append(r2)     // Catch:{ all -> 0x00f6 }
            java.lang.String r8 = r8.getMessage()     // Catch:{ all -> 0x00f6 }
            r0.append(r8)     // Catch:{ all -> 0x00f6 }
            java.lang.String r8 = r0.toString()     // Catch:{ all -> 0x00f6 }
            goto L_0x00ec
        L_0x00d5:
            r8 = move-exception
            r4 = r1
        L_0x00d7:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f6 }
            r0.<init>()     // Catch:{ all -> 0x00f6 }
            java.lang.String r2 = "LOG: filter error = "
            r0.append(r2)     // Catch:{ all -> 0x00f6 }
            java.lang.String r8 = r8.getMessage()     // Catch:{ all -> 0x00f6 }
            r0.append(r8)     // Catch:{ all -> 0x00f6 }
            java.lang.String r8 = r0.toString()     // Catch:{ all -> 0x00f6 }
        L_0x00ec:
            com.xiaomi.channel.commonutils.logger.b.c(r8)     // Catch:{ all -> 0x00f6 }
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r4)
        L_0x00f5:
            return
        L_0x00f6:
            r8 = move-exception
            r2 = r1
        L_0x00f8:
            r1 = r4
        L_0x00f9:
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.dc.a(java.io.File):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public dc m173a(File file) {
        if (file.exists()) {
            this.f214a.add(file);
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public dc a(Date date, Date date2) {
        String format;
        if (date.after(date2)) {
            this.f216b = this.f213a.format(date2);
            format = this.f213a.format(date);
        } else {
            this.f216b = this.f213a.format(date);
            format = this.f213a.format(date2);
        }
        this.c = format;
        return this;
    }

    /* access modifiers changed from: package-private */
    public File a(Context context, Date date, Date date2, File file) {
        File file2;
        if ("com.xiaomi.xmsf".equalsIgnoreCase(context.getPackageName())) {
            file2 = context.getFilesDir();
            a(new File(file2, "xmsf.log.1"));
            a(new File(file2, "xmsf.log"));
        } else {
            File file3 = new File(context.getExternalFilesDir((String) null) + a);
            a(new File(file3, "log0.txt"));
            a(new File(file3, "log1.txt"));
            file2 = file3;
        }
        if (!file2.isDirectory()) {
            return null;
        }
        File file4 = new File(file, date.getTime() + "-" + date2.getTime() + ".zip");
        if (file4.exists()) {
            return null;
        }
        a(date, date2);
        long currentTimeMillis = System.currentTimeMillis();
        File file5 = new File(file, "log.txt");
        a(file5);
        b.c("LOG: filter cost = " + (System.currentTimeMillis() - currentTimeMillis));
        if (file5.exists()) {
            long currentTimeMillis2 = System.currentTimeMillis();
            y.a(file4, file5);
            b.c("LOG: zip cost = " + (System.currentTimeMillis() - currentTimeMillis2));
            file5.delete();
            if (file4.exists()) {
                return file4;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void a(int i) {
        if (i != 0) {
            this.b = i;
        }
    }
}
