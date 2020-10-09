package android.taobao.windvane.file;

import java.io.File;
import java.nio.ByteBuffer;

public class FileAccesser {
    public static boolean exists(String str) {
        if (str == null) {
            return false;
        }
        return new File(str).exists();
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0041 A[SYNTHETIC, Splitter:B:31:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0048 A[SYNTHETIC, Splitter:B:35:0x0048] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x004f A[SYNTHETIC, Splitter:B:41:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0056 A[SYNTHETIC, Splitter:B:45:0x0056] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] read(java.io.File r5) {
        /*
            r0 = 0
            boolean r1 = r5.exists()     // Catch:{ Exception -> 0x0036, all -> 0x0031 }
            if (r1 != 0) goto L_0x0008
            return r0
        L_0x0008:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0036, all -> 0x0031 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0036, all -> 0x0031 }
            java.nio.channels.FileChannel r5 = r1.getChannel()     // Catch:{ Exception -> 0x002f, all -> 0x002a }
            long r2 = r5.size()     // Catch:{ Exception -> 0x0038 }
            int r2 = (int) r2     // Catch:{ Exception -> 0x0038 }
            java.nio.ByteBuffer r2 = java.nio.ByteBuffer.allocate(r2)     // Catch:{ Exception -> 0x0038 }
            r5.read(r2)     // Catch:{ Exception -> 0x0038 }
            byte[] r2 = r2.array()     // Catch:{ Exception -> 0x0038 }
            r1.close()     // Catch:{ IOException -> 0x0024 }
        L_0x0024:
            if (r5 == 0) goto L_0x0029
            r5.close()     // Catch:{ IOException -> 0x0029 }
        L_0x0029:
            return r2
        L_0x002a:
            r5 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
            goto L_0x004d
        L_0x002f:
            r5 = r0
            goto L_0x0038
        L_0x0031:
            r5 = move-exception
            r1 = r0
            r0 = r5
            r5 = r1
            goto L_0x004d
        L_0x0036:
            r5 = r0
            r1 = r5
        L_0x0038:
            java.lang.String r2 = "FileAccesser"
            java.lang.String r3 = "read loacl file failed"
            android.taobao.windvane.util.TaoLog.w(r2, r3)     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0046
            r1.close()     // Catch:{ IOException -> 0x0045 }
            goto L_0x0046
        L_0x0045:
        L_0x0046:
            if (r5 == 0) goto L_0x004b
            r5.close()     // Catch:{ IOException -> 0x004b }
        L_0x004b:
            return r0
        L_0x004c:
            r0 = move-exception
        L_0x004d:
            if (r1 == 0) goto L_0x0054
            r1.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0054
        L_0x0053:
        L_0x0054:
            if (r5 == 0) goto L_0x0059
            r5.close()     // Catch:{ IOException -> 0x0059 }
        L_0x0059:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileAccesser.read(java.io.File):byte[]");
    }

    public static byte[] read(String str) {
        if (str == null) {
            return null;
        }
        return read(new File(str));
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0052 A[Catch:{ all -> 0x0080 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0065 A[Catch:{ all -> 0x0080 }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x006d A[SYNTHETIC, Splitter:B:47:0x006d] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0077 A[SYNTHETIC, Splitter:B:52:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0083 A[SYNTHETIC, Splitter:B:59:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x008d A[SYNTHETIC, Splitter:B:64:0x008d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean write(java.io.File r5, java.nio.ByteBuffer r6) throws android.taobao.windvane.file.NotEnoughSpace {
        /*
            r0 = 0
            r1 = 0
            boolean r2 = r5.exists()     // Catch:{ Exception -> 0x004a, all -> 0x0047 }
            if (r2 != 0) goto L_0x0015
            java.io.File r2 = r5.getParentFile()     // Catch:{ Exception -> 0x004a, all -> 0x0047 }
            if (r2 != 0) goto L_0x000f
            return r1
        L_0x000f:
            r2.mkdirs()     // Catch:{ Exception -> 0x004a, all -> 0x0047 }
            r5.createNewFile()     // Catch:{ Exception -> 0x004a, all -> 0x0047 }
        L_0x0015:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x004a, all -> 0x0047 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x004a, all -> 0x0047 }
            java.nio.channels.FileChannel r3 = r2.getChannel()     // Catch:{ Exception -> 0x0043, all -> 0x003f }
            r6.position(r1)     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r3.write(r6)     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r6 = 1
            r3.force(r6)     // Catch:{ Exception -> 0x003d, all -> 0x003b }
            r2.close()     // Catch:{ IOException -> 0x002c }
            goto L_0x0030
        L_0x002c:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0030:
            if (r3 == 0) goto L_0x003a
            r3.close()     // Catch:{ IOException -> 0x0036 }
            goto L_0x003a
        L_0x0036:
            r5 = move-exception
            r5.printStackTrace()
        L_0x003a:
            return r6
        L_0x003b:
            r5 = move-exception
            goto L_0x0041
        L_0x003d:
            r6 = move-exception
            goto L_0x0045
        L_0x003f:
            r5 = move-exception
            r3 = r0
        L_0x0041:
            r0 = r2
            goto L_0x0081
        L_0x0043:
            r6 = move-exception
            r3 = r0
        L_0x0045:
            r0 = r2
            goto L_0x004c
        L_0x0047:
            r5 = move-exception
            r3 = r0
            goto L_0x0081
        L_0x004a:
            r6 = move-exception
            r3 = r0
        L_0x004c:
            java.lang.String r2 = r6.getMessage()     // Catch:{ all -> 0x0080 }
            if (r2 == 0) goto L_0x0063
            java.lang.String r4 = "ENOSPC"
            boolean r2 = r2.contains(r4)     // Catch:{ all -> 0x0080 }
            if (r2 != 0) goto L_0x005b
            goto L_0x0063
        L_0x005b:
            android.taobao.windvane.file.NotEnoughSpace r5 = new android.taobao.windvane.file.NotEnoughSpace     // Catch:{ all -> 0x0080 }
            java.lang.String r6 = "not enouth space in flash"
            r5.<init>(r6)     // Catch:{ all -> 0x0080 }
            throw r5     // Catch:{ all -> 0x0080 }
        L_0x0063:
            if (r5 == 0) goto L_0x0068
            r5.delete()     // Catch:{ all -> 0x0080 }
        L_0x0068:
            r6.printStackTrace()     // Catch:{ all -> 0x0080 }
            if (r0 == 0) goto L_0x0075
            r0.close()     // Catch:{ IOException -> 0x0071 }
            goto L_0x0075
        L_0x0071:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0075:
            if (r3 == 0) goto L_0x007f
            r3.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x007f
        L_0x007b:
            r5 = move-exception
            r5.printStackTrace()
        L_0x007f:
            return r1
        L_0x0080:
            r5 = move-exception
        L_0x0081:
            if (r0 == 0) goto L_0x008b
            r0.close()     // Catch:{ IOException -> 0x0087 }
            goto L_0x008b
        L_0x0087:
            r6 = move-exception
            r6.printStackTrace()
        L_0x008b:
            if (r3 == 0) goto L_0x0095
            r3.close()     // Catch:{ IOException -> 0x0091 }
            goto L_0x0095
        L_0x0091:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0095:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileAccesser.write(java.io.File, java.nio.ByteBuffer):boolean");
    }

    public static boolean write(String str, ByteBuffer byteBuffer) throws NotEnoughSpace {
        if (str == null) {
            return false;
        }
        return write(new File(str), byteBuffer);
    }

    public static boolean deleteFile(String str) {
        if (str == null) {
            return false;
        }
        return deleteFile(new File(str), true);
    }

    public static boolean deleteFile(File file) {
        return deleteFile(file, true);
    }

    public static boolean deleteFile(File file, boolean z) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                try {
                    for (File file2 : file.listFiles()) {
                        if (file2.isDirectory()) {
                            deleteFile(file2, true);
                        } else {
                            try {
                                file2.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                }
            }
            if (z) {
                try {
                    return file.delete();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        return false;
    }
}
