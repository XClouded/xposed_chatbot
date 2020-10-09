package com.ali.ha.fulltrace;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    static final String ROOT_PATH = "fulltrace/";

    public static String getFulltraceFilePath(Context context, String str) {
        return getFulltraceSDRootPath(context, str, false);
    }

    public static String getFulltraceCachePath(Context context, String str) {
        return getFulltraceSDRootPath(context, str, true);
    }

    private static String getFulltraceSDRootPath(Context context, String str, boolean z) {
        File file;
        if (str == null) {
            str = "";
        }
        File file2 = null;
        if (z) {
            try {
                file = context.getExternalCacheDir();
            } catch (Throwable unused) {
            }
        } else {
            file = context.getExternalFilesDir((String) null);
        }
        if (file != null) {
            file2 = new File(file, ROOT_PATH + str);
        }
        if (file2 == null) {
            File cacheDir = z ? context.getCacheDir() : context.getFilesDir();
            file2 = new File(cacheDir, ROOT_PATH + str);
        }
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static String getFulltraceDataPath(Context context, String str) {
        File dir = context.getDir("fulltrace", 0);
        if (dir == null) {
            return "";
        }
        File file = new File(dir.getAbsolutePath(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static void deleteFile(File file) {
        File[] listFiles;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File deleteFile : listFiles) {
                deleteFile(deleteFile);
            }
        }
        file.delete();
    }

    public static byte[] readFileToBytes(File file) throws IOException {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
        byte[] bArr = new byte[1024];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read > 0) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                fileInputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }
}
