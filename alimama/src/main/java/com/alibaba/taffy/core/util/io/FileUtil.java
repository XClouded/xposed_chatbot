package com.alibaba.taffy.core.util.io;

import com.alibaba.taffy.core.io.NullOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class FileUtil {
    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
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

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                throw new IOException("File '" + file + "' could not be created");
            }
        } else if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
        } else if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
        }
        return new FileOutputStream(file);
    }

    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            IOUtil.closeQuietly((Closeable) openOutputStream(file));
        }
        if (!file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    public static void forceMkdir(File file) throws IOException {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IOException("File " + file + " exists and is not a directory. Unable to create directory.");
            }
        } else if (!file.mkdirs() && !file.isDirectory()) {
            throw new IOException("Unable to create directory " + file);
        }
    }

    public static String readFileToString(File file, String str) throws IOException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openInputStream(file);
            try {
                String iOUtil = IOUtil.toString(fileInputStream, str);
                IOUtil.closeQuietly((Closeable) fileInputStream);
                return iOUtil;
            } catch (Throwable th) {
                th = th;
                IOUtil.closeQuietly((Closeable) fileInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            IOUtil.closeQuietly((Closeable) fileInputStream);
            throw th;
        }
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openInputStream(file);
            try {
                byte[] byteArray = IOUtil.toByteArray((InputStream) fileInputStream);
                IOUtil.closeQuietly((Closeable) fileInputStream);
                return byteArray;
            } catch (Throwable th) {
                th = th;
                IOUtil.closeQuietly((Closeable) fileInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            IOUtil.closeQuietly((Closeable) fileInputStream);
            throw th;
        }
    }

    public static List<String> readLines(File file, String str) throws IOException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openInputStream(file);
            try {
                List<String> readLines = IOUtil.readLines(fileInputStream, str);
                IOUtil.closeQuietly((Closeable) fileInputStream);
                return readLines;
            } catch (Throwable th) {
                th = th;
                IOUtil.closeQuietly((Closeable) fileInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            IOUtil.closeQuietly((Closeable) fileInputStream);
            throw th;
        }
    }

    public static void writeStringToFile(File file, String str, String str2) throws IOException {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openOutputStream(file);
            try {
                IOUtil.write(str, fileOutputStream, str2);
                IOUtil.closeQuietly((Closeable) fileOutputStream);
            } catch (Throwable th) {
                th = th;
                IOUtil.closeQuietly((Closeable) fileOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
            IOUtil.closeQuietly((Closeable) fileOutputStream);
            throw th;
        }
    }

    public static void write(File file, CharSequence charSequence, String str) throws IOException {
        writeStringToFile(file, charSequence == null ? null : charSequence.toString(), str);
    }

    public static void writeByteArrayToFile(File file, byte[] bArr) throws IOException {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openOutputStream(file);
            try {
                fileOutputStream.write(bArr);
                IOUtil.closeQuietly((Closeable) fileOutputStream);
            } catch (Throwable th) {
                th = th;
                IOUtil.closeQuietly((Closeable) fileOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
            IOUtil.closeQuietly((Closeable) fileOutputStream);
            throw th;
        }
    }

    public static void writeLines(File file, String str, Collection<?> collection, String str2) throws IOException {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openOutputStream(file);
            try {
                IOUtil.writeLines(collection, str2, fileOutputStream, str);
                IOUtil.closeQuietly((Closeable) fileOutputStream);
            } catch (Throwable th) {
                th = th;
                IOUtil.closeQuietly((Closeable) fileOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
            IOUtil.closeQuietly((Closeable) fileOutputStream);
            throw th;
        }
    }

    public static long sizeOf(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(file + " does not exist");
        } else if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }
    }

    public static long sizeOfDirectory(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(file + " does not exist");
        } else if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            long j = 0;
            if (listFiles == null) {
                return 0;
            }
            for (File sizeOf : listFiles) {
                j += sizeOf(sizeOf);
            }
            return j;
        } else {
            throw new IllegalArgumentException(file + " is not a directory");
        }
    }

    public static long checksumCRC32(File file) throws IOException {
        CRC32 crc32 = new CRC32();
        checksum(file, crc32);
        return crc32.getValue();
    }

    public static Checksum checksum(File file, Checksum checksum) throws IOException {
        if (!file.isDirectory()) {
            CheckedInputStream checkedInputStream = null;
            try {
                CheckedInputStream checkedInputStream2 = new CheckedInputStream(new FileInputStream(file), checksum);
                try {
                    IOUtil.copy((InputStream) checkedInputStream2, (OutputStream) new NullOutputStream());
                    IOUtil.closeQuietly((Closeable) checkedInputStream2);
                    return checksum;
                } catch (Throwable th) {
                    th = th;
                    checkedInputStream = checkedInputStream2;
                    IOUtil.closeQuietly((Closeable) checkedInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                IOUtil.closeQuietly((Closeable) checkedInputStream);
                throw th;
            }
        } else {
            throw new IllegalArgumentException("Checksums can't be computed on directories");
        }
    }

    public static boolean deleteDir(File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    if (file.exists() && file.isDirectory()) {
                        File[] listFiles = file.listFiles();
                        if (listFiles.length > 0) {
                            for (File deleteDir : listFiles) {
                                deleteDir(deleteDir);
                            }
                        }
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    } else if (file.isFile()) {
                        file.delete();
                    }
                    return true;
                }
            } catch (Exception unused) {
                return false;
            }
        }
        return true;
    }
}
