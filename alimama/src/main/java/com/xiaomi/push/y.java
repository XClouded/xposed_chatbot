package com.xiaomi.push;

import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class y {
    public static final String[] a = {"jpg", "png", "bmp", "gif", "webp"};

    public static String a(File file) {
        InputStreamReader inputStreamReader;
        StringWriter stringWriter = new StringWriter();
        try {
            inputStreamReader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)));
            try {
                char[] cArr = new char[2048];
                while (true) {
                    int read = inputStreamReader.read(cArr);
                    if (read != -1) {
                        stringWriter.write(cArr, 0, read);
                    } else {
                        String stringWriter2 = stringWriter.toString();
                        a((Closeable) inputStreamReader);
                        a((Closeable) stringWriter);
                        return stringWriter2;
                    }
                }
            } catch (IOException e) {
                e = e;
                try {
                    b.c("read file :" + file.getAbsolutePath() + " failure :" + e.getMessage());
                    a((Closeable) inputStreamReader);
                    a((Closeable) stringWriter);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    a((Closeable) inputStreamReader);
                    a((Closeable) stringWriter);
                    throw th;
                }
            }
        } catch (IOException e2) {
            e = e2;
            inputStreamReader = null;
            b.c("read file :" + file.getAbsolutePath() + " failure :" + e.getMessage());
            a((Closeable) inputStreamReader);
            a((Closeable) stringWriter);
            return null;
        } catch (Throwable th2) {
            th = th2;
            inputStreamReader = null;
            a((Closeable) inputStreamReader);
            a((Closeable) stringWriter);
            throw th;
        }
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m635a(File file) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File a2 : listFiles) {
                a(a2);
            }
        } else if (!file.exists()) {
            return;
        }
        file.delete();
    }

    public static void a(File file, File file2) {
        ZipOutputStream zipOutputStream = null;
        try {
            ZipOutputStream zipOutputStream2 = new ZipOutputStream(new FileOutputStream(file, false));
            try {
                a(zipOutputStream2, file2, (String) null, (FileFilter) null);
                a((Closeable) zipOutputStream2);
            } catch (FileNotFoundException unused) {
                zipOutputStream = zipOutputStream2;
                a((Closeable) zipOutputStream);
            } catch (IOException e) {
                e = e;
                zipOutputStream = zipOutputStream2;
                try {
                    b.a("zip file failure + " + e.getMessage());
                    a((Closeable) zipOutputStream);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                zipOutputStream = zipOutputStream2;
                a((Closeable) zipOutputStream);
                throw th;
            }
        } catch (FileNotFoundException unused2) {
            a((Closeable) zipOutputStream);
        } catch (IOException e2) {
            e = e2;
            b.a("zip file failure + " + e.getMessage());
            a((Closeable) zipOutputStream);
        }
    }

    public static void a(File file, String str) {
        if (!file.exists()) {
            b.c("mkdir " + file.getAbsolutePath());
            file.getParentFile().mkdirs();
        }
        BufferedWriter bufferedWriter = null;
        try {
            BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            try {
                bufferedWriter2.write(str);
                a((Closeable) bufferedWriter2);
            } catch (IOException e) {
                e = e;
                bufferedWriter = bufferedWriter2;
                try {
                    b.c("write file :" + file.getAbsolutePath() + " failure :" + e.getMessage());
                    a((Closeable) bufferedWriter);
                } catch (Throwable th) {
                    th = th;
                    a((Closeable) bufferedWriter);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedWriter = bufferedWriter2;
                a((Closeable) bufferedWriter);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            b.c("write file :" + file.getAbsolutePath() + " failure :" + e.getMessage());
            a((Closeable) bufferedWriter);
        }
    }

    public static void a(ZipOutputStream zipOutputStream, File file, String str, FileFilter fileFilter) {
        FileInputStream fileInputStream;
        ZipEntry zipEntry;
        String str2;
        if (str == null) {
            str = "";
        }
        FileInputStream fileInputStream2 = null;
        try {
            if (file.isDirectory()) {
                File[] listFiles = fileFilter != null ? file.listFiles(fileFilter) : file.listFiles();
                zipOutputStream.putNextEntry(new ZipEntry(str + File.separator));
                if (TextUtils.isEmpty(str)) {
                    str2 = "";
                } else {
                    str2 = str + File.separator;
                }
                for (int i = 0; i < listFiles.length; i++) {
                    a(zipOutputStream, listFiles[i], str2 + listFiles[i].getName(), (FileFilter) null);
                }
                File[] listFiles2 = file.listFiles(new z());
                if (listFiles2 != null) {
                    for (File file2 : listFiles2) {
                        a(zipOutputStream, file2, str2 + File.separator + file2.getName(), fileFilter);
                    }
                }
                fileInputStream = null;
            } else {
                if (!TextUtils.isEmpty(str)) {
                    zipEntry = new ZipEntry(str);
                } else {
                    Date date = new Date();
                    zipEntry = new ZipEntry(String.valueOf(date.getTime()) + ".txt");
                }
                zipOutputStream.putNextEntry(zipEntry);
                fileInputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        zipOutputStream.write(bArr, 0, read);
                    }
                } catch (IOException e) {
                    e = e;
                    fileInputStream2 = fileInputStream;
                    try {
                        b.d("zipFiction failed with exception:" + e.toString());
                        a((Closeable) fileInputStream2);
                    } catch (Throwable th) {
                        th = th;
                        a((Closeable) fileInputStream2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream2 = fileInputStream;
                    a((Closeable) fileInputStream2);
                    throw th;
                }
            }
            a((Closeable) fileInputStream);
        } catch (IOException e2) {
            e = e2;
            b.d("zipFiction failed with exception:" + e.toString());
            a((Closeable) fileInputStream2);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m636a(File file) {
        try {
            if (file.isDirectory()) {
                return false;
            }
            if (file.exists()) {
                return true;
            }
            File parentFile = file.getParentFile();
            if (parentFile.exists() || parentFile.mkdirs()) {
                return file.createNewFile();
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public static byte[] a(byte[] bArr) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.finish();
            gZIPOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (Exception unused) {
            return bArr;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void b(java.io.File r3, java.io.File r4) {
        /*
            java.lang.String r0 = r3.getAbsolutePath()
            java.lang.String r1 = r4.getAbsolutePath()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x000f
            return
        L_0x000f:
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0035 }
            r1.<init>(r3)     // Catch:{ all -> 0x0035 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x0033 }
            r3.<init>(r4)     // Catch:{ all -> 0x0033 }
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r4]     // Catch:{ all -> 0x0030 }
        L_0x001e:
            int r0 = r1.read(r4)     // Catch:{ all -> 0x0030 }
            if (r0 < 0) goto L_0x0029
            r2 = 0
            r3.write(r4, r2, r0)     // Catch:{ all -> 0x0030 }
            goto L_0x001e
        L_0x0029:
            r1.close()
            r3.close()
            return
        L_0x0030:
            r4 = move-exception
            r0 = r3
            goto L_0x0037
        L_0x0033:
            r4 = move-exception
            goto L_0x0037
        L_0x0035:
            r4 = move-exception
            r1 = r0
        L_0x0037:
            if (r1 == 0) goto L_0x003c
            r1.close()
        L_0x003c:
            if (r0 == 0) goto L_0x0041
            r0.close()
        L_0x0041:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.y.b(java.io.File, java.io.File):void");
    }
}
