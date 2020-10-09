package alimama.com.unwupdate;

import com.taobao.weex.el.parse.Operators;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

public class FileUtils {
    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            char[] cArr = new char[1024];
            while (true) {
                int read = reader.read(cArr);
                if (read == -1) {
                    return stringWriter.toString();
                }
                stringWriter.write(cArr, 0, read);
            }
        } finally {
            reader.close();
        }
    }

    public static String readAsciiLine(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                throw new EOFException();
            } else if (read == 10) {
                int length = sb.length();
                if (length > 0) {
                    int i = length - 1;
                    if (sb.charAt(i) == 13) {
                        sb.setLength(i);
                    }
                }
                return sb.toString();
            } else {
                sb.append((char) read);
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    public static void deleteDirectoryQuickly(File file) throws IOException {
        if (file.exists()) {
            File file2 = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(file2);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file2.exists()) {
                try {
                    Runtime.getRuntime().exec("rm -r " + file2).waitFor();
                } catch (IOException | InterruptedException unused) {
                }
            }
            if (file2.exists()) {
                deleteDirectoryRecursively(file2);
                if (file2.exists()) {
                    file2.delete();
                }
            }
        }
    }

    public static void chmod(String str, String str2) {
        try {
            Runtime.getRuntime().exec("chmod " + str + Operators.SPACE_STR + str2).waitFor();
        } catch (IOException | InterruptedException unused) {
        }
    }

    public static void deleteDirectoryRecursively(File file) throws IOException {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            int length = listFiles.length;
            int i = 0;
            while (i < length) {
                File file2 = listFiles[i];
                if (file2.isDirectory()) {
                    deleteDirectoryRecursively(file2);
                }
                if (file2.delete()) {
                    i++;
                } else {
                    throw new IOException("failed to delete file: " + file2);
                }
            }
            return;
        }
        throw new IllegalArgumentException("not a directory: " + file);
    }

    public static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002d A[SYNTHETIC, Splitter:B:17:0x002d] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0034 A[SYNTHETIC, Splitter:B:24:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeString(java.lang.String r3, java.lang.String r4) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r3)
            java.io.File r3 = r0.getParentFile()
            boolean r3 = r3.exists()
            if (r3 != 0) goto L_0x0016
            java.io.File r3 = r0.getParentFile()
            r3.mkdirs()
        L_0x0016:
            r3 = 0
            r1 = 1
            java.io.FileWriter r2 = new java.io.FileWriter     // Catch:{ IOException -> 0x0032, all -> 0x002a }
            r2.<init>(r0)     // Catch:{ IOException -> 0x0032, all -> 0x002a }
            r2.write(r4)     // Catch:{ IOException -> 0x0028, all -> 0x0024 }
            r2.close()     // Catch:{ IOException -> 0x0038 }
            return r1
        L_0x0024:
            r3 = move-exception
            r4 = r3
            r3 = r2
            goto L_0x002b
        L_0x0028:
            r3 = r2
            goto L_0x0032
        L_0x002a:
            r4 = move-exception
        L_0x002b:
            if (r3 == 0) goto L_0x0031
            r3.close()     // Catch:{ IOException -> 0x0031 }
            return r1
        L_0x0031:
            throw r4
        L_0x0032:
            if (r3 == 0) goto L_0x0038
            r3.close()     // Catch:{ IOException -> 0x0038 }
            return r1
        L_0x0038:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwupdate.FileUtils.writeString(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x004c A[SYNTHETIC, Splitter:B:26:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0053 A[SYNTHETIC, Splitter:B:30:0x0053] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x005b A[SYNTHETIC, Splitter:B:37:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0062 A[SYNTHETIC, Splitter:B:41:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readString(java.lang.String r5) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            boolean r0 = r0.exists()
            r1 = 0
            if (r0 != 0) goto L_0x000d
            return r1
        L_0x000d:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0057, all -> 0x0046 }
            r0.<init>(r5)     // Catch:{ Exception -> 0x0057, all -> 0x0046 }
            java.nio.channels.FileChannel r5 = r0.getChannel()     // Catch:{ Exception -> 0x0044, all -> 0x003f }
            long r2 = r5.size()     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            int r2 = (int) r2     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            java.nio.ByteBuffer r2 = java.nio.ByteBuffer.allocate(r2)     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r5.read(r2)     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r3.<init>()     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            byte[] r2 = r2.array()     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r3.write(r2)     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r0.close()     // Catch:{ IOException -> 0x0035 }
        L_0x0035:
            if (r5 == 0) goto L_0x003a
            r5.close()     // Catch:{ IOException -> 0x003a }
        L_0x003a:
            return r2
        L_0x003b:
            r1 = move-exception
            goto L_0x004a
        L_0x003d:
            goto L_0x0059
        L_0x003f:
            r5 = move-exception
            r4 = r1
            r1 = r5
            r5 = r4
            goto L_0x004a
        L_0x0044:
            r5 = r1
            goto L_0x0059
        L_0x0046:
            r5 = move-exception
            r0 = r1
            r1 = r5
            r5 = r0
        L_0x004a:
            if (r0 == 0) goto L_0x0051
            r0.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0051
        L_0x0050:
        L_0x0051:
            if (r5 == 0) goto L_0x0056
            r5.close()     // Catch:{ IOException -> 0x0056 }
        L_0x0056:
            throw r1
        L_0x0057:
            r5 = r1
            r0 = r5
        L_0x0059:
            if (r0 == 0) goto L_0x0060
            r0.close()     // Catch:{ IOException -> 0x005f }
            goto L_0x0060
        L_0x005f:
        L_0x0060:
            if (r5 == 0) goto L_0x0065
            r5.close()     // Catch:{ IOException -> 0x0065 }
        L_0x0065:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwupdate.FileUtils.readString(java.lang.String):java.lang.String");
    }
}
