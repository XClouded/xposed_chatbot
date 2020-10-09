package com.alibaba.motu.tbrest.rest;

public class RestHttpUtils {
    public static final int HTTP_REQ_TYPE_GET = 1;
    public static final int HTTP_REQ_TYPE_POST_FORM_DATA = 2;
    public static final int HTTP_REQ_TYPE_POST_URL_ENCODED = 3;
    public static final int MAX_CONNECTION_TIME_OUT = 10000;
    public static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    private static final String POST_Field_BOTTOM = "--GJircTeP--\r\n";
    private static final String POST_Field_TOP = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n";

    /* JADX WARNING: Removed duplicated region for block: B:102:0x01c1 A[Catch:{ IOException -> 0x01dd }, LOOP:1: B:100:0x01ba->B:102:0x01c1, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01c5 A[EDGE_INSN: B:103:0x01c5->B:104:? ?: BREAK  , SYNTHETIC, Splitter:B:103:0x01c5] */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x01d6  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x01db A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01e6 A[SYNTHETIC, Splitter:B:117:0x01e6] */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x01f9 A[SYNTHETIC, Splitter:B:124:0x01f9] */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0227 A[SYNTHETIC, Splitter:B:144:0x0227] */
    /* JADX WARNING: Removed duplicated region for block: B:170:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] sendRequest(int r17, java.lang.String r18, java.util.Map<java.lang.String, java.lang.Object> r19, boolean r20) {
        /*
            r1 = r17
            r2 = r19
            boolean r0 = com.alibaba.motu.tbrest.utils.StringUtils.isEmpty(r18)
            r4 = 0
            if (r0 == 0) goto L_0x000c
            return r4
        L_0x000c:
            java.net.URL r0 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x023d }
            r5 = r18
            r0.<init>(r5)     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x023d }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x023d }
            r5 = r0
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x023d }
            if (r5 == 0) goto L_0x023b
            r6 = 1
            r7 = 3
            r8 = 2
            if (r1 == r8) goto L_0x0023
            if (r1 != r7) goto L_0x0026
        L_0x0023:
            r5.setDoOutput(r6)
        L_0x0026:
            r5.setDoInput(r6)
            if (r1 == r8) goto L_0x0034
            if (r1 != r7) goto L_0x002e
            goto L_0x0034
        L_0x002e:
            java.lang.String r0 = "GET"
            r5.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x0233 }
            goto L_0x0039
        L_0x0034:
            java.lang.String r0 = "POST"
            r5.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x0233 }
        L_0x0039:
            r9 = 0
            r5.setUseCaches(r9)
            r0 = 10000(0x2710, float:1.4013E-41)
            r5.setConnectTimeout(r0)
            r0 = 60000(0xea60, float:8.4078E-41)
            r5.setReadTimeout(r0)
            java.lang.String r0 = "Connection"
            java.lang.String r10 = "close"
            r5.setRequestProperty(r0, r10)
            if (r20 == 0) goto L_0x0058
            java.lang.String r0 = "Accept-Encoding"
            java.lang.String r10 = "gzip,deflate"
            r5.setRequestProperty(r0, r10)
        L_0x0058:
            r5.setInstanceFollowRedirects(r6)
            if (r1 == r8) goto L_0x005f
            if (r1 != r7) goto L_0x0153
        L_0x005f:
            if (r1 != r8) goto L_0x0069
            java.lang.String r0 = "Content-Type"
            java.lang.String r10 = "multipart/form-data; boundary=GJircTeP"
            r5.setRequestProperty(r0, r10)
            goto L_0x0072
        L_0x0069:
            if (r1 != r7) goto L_0x0072
            java.lang.String r0 = "Content-Type"
            java.lang.String r10 = "application/x-www-form-urlencoded"
            r5.setRequestProperty(r0, r10)
        L_0x0072:
            if (r2 == 0) goto L_0x0144
            int r0 = r19.size()
            if (r0 <= 0) goto L_0x0144
            java.io.ByteArrayOutputStream r10 = new java.io.ByteArrayOutputStream
            r10.<init>()
            java.util.Set r0 = r19.keySet()
            int r11 = r0.size()
            java.lang.String[] r11 = new java.lang.String[r11]
            r0.toArray(r11)
            com.alibaba.motu.tbrest.rest.RestKeyArraySorter r0 = com.alibaba.motu.tbrest.rest.RestKeyArraySorter.getInstance()
            java.lang.String[] r11 = r0.sortResourcesList(r11, r6)
            int r12 = r11.length
            r13 = 0
        L_0x0096:
            if (r13 >= r12) goto L_0x012b
            r0 = r11[r13]
            if (r1 != r8) goto L_0x00cc
            java.lang.Object r14 = r2.get(r0)
            byte[] r14 = (byte[]) r14
            if (r14 == 0) goto L_0x0126
            java.lang.String r15 = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n"
            java.lang.Object[] r4 = new java.lang.Object[r8]     // Catch:{ IOException -> 0x00c4 }
            r4[r9] = r0     // Catch:{ IOException -> 0x00c4 }
            r4[r6] = r0     // Catch:{ IOException -> 0x00c4 }
            java.lang.String r0 = java.lang.String.format(r15, r4)     // Catch:{ IOException -> 0x00c4 }
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x00c4 }
            r10.write(r0)     // Catch:{ IOException -> 0x00c4 }
            r10.write(r14)     // Catch:{ IOException -> 0x00c4 }
            java.lang.String r0 = "\r\n"
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x00c4 }
            r10.write(r0)     // Catch:{ IOException -> 0x00c4 }
            goto L_0x0126
        L_0x00c4:
            r0 = move-exception
            java.lang.String r4 = "write lBaos error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r4, r0)
            goto L_0x0126
        L_0x00cc:
            if (r1 != r7) goto L_0x0126
            java.lang.Object r4 = r2.get(r0)
            java.lang.String r4 = (java.lang.String) r4
            int r14 = r10.size()
            if (r14 <= 0) goto L_0x0103
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00fb }
            r14.<init>()     // Catch:{ IOException -> 0x00fb }
            java.lang.String r15 = "&"
            r14.append(r15)     // Catch:{ IOException -> 0x00fb }
            r14.append(r0)     // Catch:{ IOException -> 0x00fb }
            java.lang.String r0 = "="
            r14.append(r0)     // Catch:{ IOException -> 0x00fb }
            r14.append(r4)     // Catch:{ IOException -> 0x00fb }
            java.lang.String r0 = r14.toString()     // Catch:{ IOException -> 0x00fb }
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x00fb }
            r10.write(r0)     // Catch:{ IOException -> 0x00fb }
            goto L_0x0126
        L_0x00fb:
            r0 = move-exception
            java.lang.String r4 = "write lBaos error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r4, r0)
            goto L_0x0126
        L_0x0103:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x011f }
            r14.<init>()     // Catch:{ IOException -> 0x011f }
            r14.append(r0)     // Catch:{ IOException -> 0x011f }
            java.lang.String r0 = "="
            r14.append(r0)     // Catch:{ IOException -> 0x011f }
            r14.append(r4)     // Catch:{ IOException -> 0x011f }
            java.lang.String r0 = r14.toString()     // Catch:{ IOException -> 0x011f }
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x011f }
            r10.write(r0)     // Catch:{ IOException -> 0x011f }
            goto L_0x0126
        L_0x011f:
            r0 = move-exception
            java.lang.String r4 = "write lBaos error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r4, r0)
        L_0x0126:
            int r13 = r13 + 1
            r4 = 0
            goto L_0x0096
        L_0x012b:
            if (r1 != r8) goto L_0x013e
            java.lang.String r0 = "--GJircTeP--\r\n"
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x0137 }
            r10.write(r0)     // Catch:{ IOException -> 0x0137 }
            goto L_0x013e
        L_0x0137:
            r0 = move-exception
            java.lang.String r2 = "write lBaos error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r2, r0)
        L_0x013e:
            byte[] r0 = r10.toByteArray()
            r4 = r0
            goto L_0x0145
        L_0x0144:
            r4 = 0
        L_0x0145:
            if (r4 == 0) goto L_0x0149
            int r0 = r4.length
            goto L_0x014a
        L_0x0149:
            r0 = 0
        L_0x014a:
            java.lang.String r2 = "Content-Length"
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r5.setRequestProperty(r2, r0)
        L_0x0153:
            r5.connect()     // Catch:{ Exception -> 0x020a, all -> 0x0205 }
            if (r1 == r8) goto L_0x015a
            if (r1 != r7) goto L_0x0179
        L_0x015a:
            if (r4 == 0) goto L_0x0179
            int r0 = r4.length     // Catch:{ Exception -> 0x020a, all -> 0x0205 }
            if (r0 <= 0) goto L_0x0179
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x020a, all -> 0x0205 }
            java.io.OutputStream r0 = r5.getOutputStream()     // Catch:{ Exception -> 0x020a, all -> 0x0205 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x020a, all -> 0x0205 }
            r1.write(r4)     // Catch:{ Exception -> 0x0175, all -> 0x016f }
            r1.flush()     // Catch:{ Exception -> 0x0175, all -> 0x016f }
            goto L_0x017a
        L_0x016f:
            r0 = move-exception
            r16 = r1
            r1 = r0
            goto L_0x0225
        L_0x0175:
            r0 = move-exception
            r4 = r1
            goto L_0x020c
        L_0x0179:
            r1 = 0
        L_0x017a:
            if (r1 == 0) goto L_0x0187
            r1.close()     // Catch:{ IOException -> 0x0180 }
            goto L_0x0187
        L_0x0180:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "out close error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r0, r1)
        L_0x0187:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r1.<init>()
            if (r20 == 0) goto L_0x01ac
            java.lang.String r0 = "gzip"
            java.lang.String r2 = r5.getContentEncoding()     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            boolean r0 = r0.equals(r2)     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            if (r0 == 0) goto L_0x01ac
            java.util.zip.GZIPInputStream r0 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            java.io.InputStream r2 = r5.getInputStream()     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            goto L_0x01b5
        L_0x01a4:
            r0 = move-exception
            r1 = r0
            r16 = 0
            goto L_0x01f7
        L_0x01a9:
            r0 = move-exception
            r4 = 0
            goto L_0x01de
        L_0x01ac:
            java.io.DataInputStream r0 = new java.io.DataInputStream     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            java.io.InputStream r2 = r5.getInputStream()     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x01a9, all -> 0x01a4 }
        L_0x01b5:
            r4 = r0
            r0 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r0]     // Catch:{ IOException -> 0x01dd }
        L_0x01ba:
            int r3 = r4.read(r2, r9, r0)     // Catch:{ IOException -> 0x01dd }
            r5 = -1
            if (r3 == r5) goto L_0x01c5
            r1.write(r2, r9, r3)     // Catch:{ IOException -> 0x01dd }
            goto L_0x01ba
        L_0x01c5:
            r4.close()     // Catch:{ Exception -> 0x01c9 }
            goto L_0x01d0
        L_0x01c9:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "out close error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r0, r2)
        L_0x01d0:
            int r0 = r1.size()
            if (r0 <= 0) goto L_0x01db
            byte[] r0 = r1.toByteArray()
            return r0
        L_0x01db:
            r1 = 0
            return r1
        L_0x01dd:
            r0 = move-exception
        L_0x01de:
            java.lang.String r1 = "write out error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r1, r0)     // Catch:{ all -> 0x01f3 }
            if (r4 == 0) goto L_0x01f1
            r4.close()     // Catch:{ Exception -> 0x01ea }
            goto L_0x01f1
        L_0x01ea:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "out close error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r0, r1)
        L_0x01f1:
            r1 = 0
            return r1
        L_0x01f3:
            r0 = move-exception
            r1 = r0
            r16 = r4
        L_0x01f7:
            if (r16 == 0) goto L_0x0204
            r16.close()     // Catch:{ Exception -> 0x01fd }
            goto L_0x0204
        L_0x01fd:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "out close error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r0, r2)
        L_0x0204:
            throw r1
        L_0x0205:
            r0 = move-exception
            r1 = r0
            r16 = 0
            goto L_0x0225
        L_0x020a:
            r0 = move-exception
            r4 = 0
        L_0x020c:
            java.lang.String r1 = "write out error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r1, r0)     // Catch:{ all -> 0x0221 }
            if (r4 == 0) goto L_0x021f
            r4.close()     // Catch:{ IOException -> 0x0218 }
            goto L_0x021f
        L_0x0218:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "out close error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r0, r1)
        L_0x021f:
            r1 = 0
            return r1
        L_0x0221:
            r0 = move-exception
            r1 = r0
            r16 = r4
        L_0x0225:
            if (r16 == 0) goto L_0x0232
            r16.close()     // Catch:{ IOException -> 0x022b }
            goto L_0x0232
        L_0x022b:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "out close error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r0, r2)
        L_0x0232:
            throw r1
        L_0x0233:
            r0 = move-exception
            java.lang.String r1 = "connection error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r1, r0)
            r1 = 0
            return r1
        L_0x023b:
            r1 = r4
            return r1
        L_0x023d:
            r0 = move-exception
            java.lang.String r1 = "connection error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r1, r0)
            r1 = 0
            return r1
        L_0x0245:
            r0 = move-exception
            r1 = r4
            java.lang.String r2 = "connection error!"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r2, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.rest.RestHttpUtils.sendRequest(int, java.lang.String, java.util.Map, boolean):byte[]");
    }
}
