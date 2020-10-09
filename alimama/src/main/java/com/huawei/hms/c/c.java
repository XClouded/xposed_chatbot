package com.huawei.hms.c;

import com.huawei.hms.support.log.a;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* compiled from: FileUtil */
final class c implements Runnable {
    final /* synthetic */ File a;
    final /* synthetic */ long b;
    final /* synthetic */ String c;

    c(File file, long j, String str) {
        this.a = file;
        this.b = j;
        this.c = str;
    }

    public void run() {
        if (this.a == null) {
            a.d("FileUtil", "In writeFile Failed to get local file.");
            return;
        }
        File parentFile = this.a.getParentFile();
        if (parentFile == null || (!parentFile.mkdirs() && !parentFile.isDirectory())) {
            a.d("FileUtil", "In writeFile, Failed to create directory.");
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            long length = this.a.length();
            if (length > this.b) {
                String canonicalPath = this.a.getCanonicalPath();
                if (!this.a.delete()) {
                    a.d("FileUtil", "last file delete failed.");
                }
                randomAccessFile = new RandomAccessFile(new File(canonicalPath), "rw");
            } else {
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.a, "rw");
                try {
                    randomAccessFile2.seek(length);
                    randomAccessFile = randomAccessFile2;
                } catch (IOException e) {
                    e = e;
                    randomAccessFile = randomAccessFile2;
                    try {
                        a.a("FileUtil", "writeFile exception:", (Throwable) e);
                        e.a((Closeable) randomAccessFile);
                    } catch (Throwable th) {
                        th = th;
                        e.a((Closeable) randomAccessFile);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    randomAccessFile = randomAccessFile2;
                    e.a((Closeable) randomAccessFile);
                    throw th;
                }
            }
            randomAccessFile.writeBytes(this.c + System.getProperty("line.separator"));
        } catch (IOException e2) {
            e = e2;
        }
        e.a((Closeable) randomAccessFile);
    }
}
