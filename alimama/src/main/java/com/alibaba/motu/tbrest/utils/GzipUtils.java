package com.alibaba.motu.tbrest.utils;

public class GzipUtils {
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0040 A[SYNTHETIC, Splitter:B:29:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x004a A[SYNTHETIC, Splitter:B:34:0x004a] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0058 A[SYNTHETIC, Splitter:B:42:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0062 A[SYNTHETIC, Splitter:B:47:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] gzip(byte[] r4) {
        /*
            if (r4 == 0) goto L_0x006b
            int r0 = r4.length
            if (r0 != 0) goto L_0x0007
            goto L_0x006b
        L_0x0007:
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0038, all -> 0x0035 }
            r1.<init>()     // Catch:{ Exception -> 0x0038, all -> 0x0035 }
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch:{ Exception -> 0x0032, all -> 0x0030 }
            int r3 = r4.length     // Catch:{ Exception -> 0x0032, all -> 0x0030 }
            r2.<init>(r1, r3)     // Catch:{ Exception -> 0x0032, all -> 0x0030 }
            r2.write(r4)     // Catch:{ Exception -> 0x002e }
            r2.finish()     // Catch:{ Exception -> 0x002e }
            byte[] r4 = r1.toByteArray()     // Catch:{ Exception -> 0x002e }
            r2.close()     // Catch:{ IOException -> 0x0021 }
            goto L_0x0025
        L_0x0021:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0025:
            r1.close()     // Catch:{ IOException -> 0x0029 }
            goto L_0x0053
        L_0x0029:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0053
        L_0x002e:
            r4 = move-exception
            goto L_0x003b
        L_0x0030:
            r4 = move-exception
            goto L_0x0056
        L_0x0032:
            r4 = move-exception
            r2 = r0
            goto L_0x003b
        L_0x0035:
            r4 = move-exception
            r1 = r0
            goto L_0x0056
        L_0x0038:
            r4 = move-exception
            r1 = r0
            r2 = r1
        L_0x003b:
            r4.printStackTrace()     // Catch:{ all -> 0x0054 }
            if (r2 == 0) goto L_0x0048
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0048
        L_0x0044:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0048:
            if (r1 == 0) goto L_0x0052
            r1.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x0052
        L_0x004e:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0052:
            r4 = r0
        L_0x0053:
            return r4
        L_0x0054:
            r4 = move-exception
            r0 = r2
        L_0x0056:
            if (r0 == 0) goto L_0x0060
            r0.close()     // Catch:{ IOException -> 0x005c }
            goto L_0x0060
        L_0x005c:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0060:
            if (r1 == 0) goto L_0x006a
            r1.close()     // Catch:{ IOException -> 0x0066 }
            goto L_0x006a
        L_0x0066:
            r0 = move-exception
            r0.printStackTrace()
        L_0x006a:
            throw r4
        L_0x006b:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.utils.GzipUtils.gzip(byte[]):byte[]");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0061 A[SYNTHETIC, Splitter:B:43:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x006b A[SYNTHETIC, Splitter:B:48:0x006b] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0075 A[SYNTHETIC, Splitter:B:53:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0081 A[SYNTHETIC, Splitter:B:59:0x0081] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x008b A[SYNTHETIC, Splitter:B:64:0x008b] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0095 A[SYNTHETIC, Splitter:B:69:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:77:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] unGzip(byte[] r7) {
        /*
            r0 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0058, all -> 0x0052 }
            r1.<init>(r7)     // Catch:{ Exception -> 0x0058, all -> 0x0052 }
            java.util.zip.GZIPInputStream r7 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x004e, all -> 0x004b }
            r7.<init>(r1)     // Catch:{ Exception -> 0x004e, all -> 0x004b }
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
            r3.<init>()     // Catch:{ Exception -> 0x0048, all -> 0x0044 }
        L_0x0014:
            int r4 = r2.length     // Catch:{ Exception -> 0x0042 }
            r5 = 0
            int r4 = r7.read(r2, r5, r4)     // Catch:{ Exception -> 0x0042 }
            r6 = -1
            if (r4 == r6) goto L_0x0021
            r3.write(r2, r5, r4)     // Catch:{ Exception -> 0x0042 }
            goto L_0x0014
        L_0x0021:
            r3.flush()     // Catch:{ Exception -> 0x0042 }
            byte[] r2 = r3.toByteArray()     // Catch:{ Exception -> 0x0042 }
            r3.close()     // Catch:{ Exception -> 0x002c }
            goto L_0x0030
        L_0x002c:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0030:
            r7.close()     // Catch:{ IOException -> 0x0034 }
            goto L_0x0038
        L_0x0034:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0038:
            r1.close()     // Catch:{ IOException -> 0x003c }
            goto L_0x0040
        L_0x003c:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0040:
            r0 = r2
            goto L_0x007d
        L_0x0042:
            r2 = move-exception
            goto L_0x005c
        L_0x0044:
            r2 = move-exception
            r3 = r0
            r0 = r2
            goto L_0x007f
        L_0x0048:
            r2 = move-exception
            r3 = r0
            goto L_0x005c
        L_0x004b:
            r7 = move-exception
            r3 = r0
            goto L_0x0055
        L_0x004e:
            r2 = move-exception
            r7 = r0
            r3 = r7
            goto L_0x005c
        L_0x0052:
            r7 = move-exception
            r1 = r0
            r3 = r1
        L_0x0055:
            r0 = r7
            r7 = r3
            goto L_0x007f
        L_0x0058:
            r2 = move-exception
            r7 = r0
            r1 = r7
            r3 = r1
        L_0x005c:
            r2.printStackTrace()     // Catch:{ all -> 0x007e }
            if (r3 == 0) goto L_0x0069
            r3.close()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0069
        L_0x0065:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0069:
            if (r7 == 0) goto L_0x0073
            r7.close()     // Catch:{ IOException -> 0x006f }
            goto L_0x0073
        L_0x006f:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0073:
            if (r1 == 0) goto L_0x007d
            r1.close()     // Catch:{ IOException -> 0x0079 }
            goto L_0x007d
        L_0x0079:
            r7 = move-exception
            r7.printStackTrace()
        L_0x007d:
            return r0
        L_0x007e:
            r0 = move-exception
        L_0x007f:
            if (r3 == 0) goto L_0x0089
            r3.close()     // Catch:{ Exception -> 0x0085 }
            goto L_0x0089
        L_0x0085:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0089:
            if (r7 == 0) goto L_0x0093
            r7.close()     // Catch:{ IOException -> 0x008f }
            goto L_0x0093
        L_0x008f:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0093:
            if (r1 == 0) goto L_0x009d
            r1.close()     // Catch:{ IOException -> 0x0099 }
            goto L_0x009d
        L_0x0099:
            r7 = move-exception
            r7.printStackTrace()
        L_0x009d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.utils.GzipUtils.unGzip(byte[]):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0029 A[SYNTHETIC, Splitter:B:17:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003a A[SYNTHETIC, Splitter:B:26:0x003a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] gzipAndRc4Bytes(java.lang.String r3) {
        /*
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r1 = 0
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x0023 }
            r2.<init>(r0)     // Catch:{ IOException -> 0x0023 }
            java.lang.String r1 = "UTF-8"
            byte[] r3 = r3.getBytes(r1)     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            r2.write(r3)     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            r2.flush()     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            r2.close()     // Catch:{ Exception -> 0x002c }
            goto L_0x002c
        L_0x001b:
            r3 = move-exception
            goto L_0x0038
        L_0x001d:
            r3 = move-exception
            r1 = r2
            goto L_0x0024
        L_0x0020:
            r3 = move-exception
            r2 = r1
            goto L_0x0038
        L_0x0023:
            r3 = move-exception
        L_0x0024:
            r3.printStackTrace()     // Catch:{ all -> 0x0020 }
            if (r1 == 0) goto L_0x002c
            r1.close()     // Catch:{ Exception -> 0x002c }
        L_0x002c:
            byte[] r3 = r0.toByteArray()
            byte[] r3 = com.alibaba.motu.tbrest.utils.RC4.rc4(r3)
            r0.close()     // Catch:{ Exception -> 0x0037 }
        L_0x0037:
            return r3
        L_0x0038:
            if (r2 == 0) goto L_0x003d
            r2.close()     // Catch:{ Exception -> 0x003d }
        L_0x003d:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.utils.GzipUtils.gzipAndRc4Bytes(java.lang.String):byte[]");
    }
}
