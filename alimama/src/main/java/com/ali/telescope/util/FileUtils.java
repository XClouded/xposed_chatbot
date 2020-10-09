package com.ali.telescope.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileUtils {
    static final String ROOT_PATH = "Telescope/";

    public static void write(File file, CharSequence charSequence, Charset charset) throws IOException {
        write(file, charSequence, charset, false);
    }

    public static void write(File file, CharSequence charSequence, Charset charset, boolean z) throws IOException {
        writeStringToFile(file, charSequence == null ? null : charSequence.toString(), charset, z);
    }

    public static String readFileToString(File file, Charset charset) throws IOException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openInputStream(file);
            try {
                String iOUtils = IOUtils.toString(fileInputStream, IOUtils.toCharset(charset));
                IOUtils.closeQuietly((Closeable) fileInputStream);
                return iOUtils;
            } catch (Throwable th) {
                th = th;
                IOUtils.closeQuietly((Closeable) fileInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            IOUtils.closeQuietly((Closeable) fileInputStream);
            throw th;
        }
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

    public static void writeStringToFile(File file, String str, Charset charset, boolean z) throws IOException {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openOutputStream(file, z);
            try {
                IOUtils.write(str, fileOutputStream, charset);
                IOUtils.closeQuietly((Closeable) fileOutputStream);
            } catch (Throwable th) {
                th = th;
                IOUtils.closeQuietly((Closeable) fileOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
            IOUtils.closeQuietly((Closeable) fileOutputStream);
            throw th;
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        } else if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
        } else if (file.canRead()) {
            return new FileInputStream(file);
        } else {
            throw new IOException("File '" + file + "' cannot be read");
        }
    }

    public static FileOutputStream openOutputStream(File file, boolean z) throws IOException {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Directory '" + parentFile + "' could not be created");
            }
        } else if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
        } else if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
        }
        return new FileOutputStream(file, z);
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

    public static String getTelescopeFilePath(Context context, String str) {
        return getTelescopeSDRootPath(context, str, false);
    }

    public static String getTelescopeCachePath(Context context, String str) {
        return getTelescopeSDRootPath(context, str, true);
    }

    private static String getTelescopeSDRootPath(Context context, String str, boolean z) {
        if (str == null) {
            str = "";
        }
        File file = null;
        if (TextUtils.equals("mounted", Environment.getExternalStorageState())) {
            File externalCacheDir = z ? context.getExternalCacheDir() : context.getExternalFilesDir((String) null);
            if (externalCacheDir != null) {
                file = new File(externalCacheDir, ROOT_PATH + str);
            }
        }
        if (file == null) {
            File cacheDir = z ? context.getCacheDir() : context.getFilesDir();
            file = new File(cacheDir, ROOT_PATH + str);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getTelescopeDataPath(Context context, String str) {
        File dir = context.getDir("telescope", 0);
        if (dir == null) {
            return "";
        }
        File file = new File(dir.getAbsolutePath(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
