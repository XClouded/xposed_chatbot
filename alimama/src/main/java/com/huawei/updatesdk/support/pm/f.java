package com.huawei.updatesdk.support.pm;

import com.huawei.updatesdk.sdk.a.c.a.a.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class f {
    private static int a = -1;

    public static boolean a(File file, File file2) {
        boolean z = false;
        if (!file.renameTo(file2)) {
            if (!b(file, file2)) {
                a.d("PkgManageUtils", "can not copy the file to new Path");
                return false;
            }
            z = true;
        }
        if (z && !file.delete()) {
            a.d("PkgManageUtils", "can not delete old file");
        }
        return true;
    }

    private static boolean a(InputStream inputStream, File file) {
        FileOutputStream fileOutputStream;
        try {
            if (file.exists() && !file.delete()) {
                a.b("PkgManageUtils", "destFile delete error.");
            }
            fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = inputStream.read(bArr);
                if (read >= 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    try {
                        break;
                    } catch (IOException e) {
                        a.a("PkgManageUtils", "", e);
                    }
                }
            }
            fileOutputStream.flush();
            try {
                fileOutputStream.getFD().sync();
            } catch (IOException e2) {
                a.a("PkgManageUtils", "", e2);
            }
            try {
                fileOutputStream.close();
                return true;
            } catch (IOException e3) {
                a.a("PkgManageUtils", "", e3);
                return true;
            }
        } catch (IOException unused) {
            return false;
        } catch (Throwable th) {
            try {
                fileOutputStream.flush();
            } catch (IOException e4) {
                a.a("PkgManageUtils", "", e4);
            }
            try {
                fileOutputStream.getFD().sync();
            } catch (IOException e5) {
                a.a("PkgManageUtils", "", e5);
            }
            try {
                fileOutputStream.close();
            } catch (IOException e6) {
                a.a("PkgManageUtils", "", e6);
            }
            throw th;
        }
    }

    private static boolean b(File file, File file2) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            boolean a2 = a((InputStream) fileInputStream, file2);
            fileInputStream.close();
            return a2;
        } catch (IOException unused) {
            a.d("PkgManageUtils", "copyFile IOException");
            return false;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }
}
