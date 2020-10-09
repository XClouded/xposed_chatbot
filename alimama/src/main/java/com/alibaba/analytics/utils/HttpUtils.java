package com.alibaba.analytics.utils;

public final class HttpUtils {
    public static final int HTTP_REQ_TYPE_GET = 1;
    public static final int HTTP_REQ_TYPE_POST_FORM_DATA = 2;
    public static final int HTTP_REQ_TYPE_POST_URL_ENCODED = 3;
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    public static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    private static final String POST_Field_BOTTOM = "--GJircTeP--\r\n";
    private static final String POST_Field_TOP = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n";

    public static class HttpResponse {
        public byte[] data = null;
        public int httpResponseCode = -1;
        public long rt = 0;
    }

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: Removed duplicated region for block: B:109:0x01d2 A[Catch:{ IOException -> 0x01ec }, LOOP:1: B:107:0x01cb->B:109:0x01d2, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01d6 A[EDGE_INSN: B:110:0x01d6->B:111:? ?: BREAK  , SYNTHETIC, Splitter:B:110:0x01d6] */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01e5  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x01f2 A[SYNTHETIC, Splitter:B:121:0x01f2] */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0202 A[SYNTHETIC, Splitter:B:129:0x0202] */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x0230 A[SYNTHETIC, Splitter:B:149:0x0230] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.analytics.utils.HttpUtils.HttpResponse sendRequest(int r17, java.lang.String r18, java.util.Map<java.lang.String, java.lang.Object> r19, boolean r20) {
        /*
            r1 = r17
            r2 = r19
            com.alibaba.analytics.utils.HttpUtils$HttpResponse r4 = new com.alibaba.analytics.utils.HttpUtils$HttpResponse
            r4.<init>()
            boolean r0 = android.text.TextUtils.isEmpty(r18)
            if (r0 == 0) goto L_0x0010
            return r4
        L_0x0010:
            java.net.URL r0 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x0240 }
            r5 = r18
            r0.<init>(r5)     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x0240 }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x0240 }
            r5 = r0
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ MalformedURLException -> 0x0245, IOException -> 0x0240 }
            if (r5 == 0) goto L_0x023f
            r6 = 1
            r7 = 3
            r8 = 2
            if (r1 == r8) goto L_0x0027
            if (r1 != r7) goto L_0x002a
        L_0x0027:
            r5.setDoOutput(r6)
        L_0x002a:
            r5.setDoInput(r6)
            if (r1 == r8) goto L_0x0038
            if (r1 != r7) goto L_0x0032
            goto L_0x0038
        L_0x0032:
            java.lang.String r0 = "GET"
            r5.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x023a }
            goto L_0x003d
        L_0x0038:
            java.lang.String r0 = "POST"
            r5.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x023a }
        L_0x003d:
            r9 = 0
            r5.setUseCaches(r9)
            r0 = 10000(0x2710, float:1.4013E-41)
            r5.setConnectTimeout(r0)
            r0 = 60000(0xea60, float:8.4078E-41)
            r5.setReadTimeout(r0)
            java.lang.String r0 = "Connection"
            java.lang.String r10 = "close"
            r5.setRequestProperty(r0, r10)
            if (r20 == 0) goto L_0x005c
            java.lang.String r0 = "Accept-Encoding"
            java.lang.String r10 = "gzip,deflate"
            r5.setRequestProperty(r0, r10)
        L_0x005c:
            r5.setInstanceFollowRedirects(r6)
            if (r1 == r8) goto L_0x0067
            if (r1 != r7) goto L_0x0064
            goto L_0x0067
        L_0x0064:
            r10 = 0
            goto L_0x014f
        L_0x0067:
            if (r1 != r8) goto L_0x0071
            java.lang.String r0 = "Content-Type"
            java.lang.String r11 = "multipart/form-data; boundary=GJircTeP"
            r5.setRequestProperty(r0, r11)
            goto L_0x007a
        L_0x0071:
            if (r1 != r7) goto L_0x007a
            java.lang.String r0 = "Content-Type"
            java.lang.String r11 = "application/x-www-form-urlencoded"
            r5.setRequestProperty(r0, r11)
        L_0x007a:
            if (r2 == 0) goto L_0x0140
            int r0 = r19.size()
            if (r0 <= 0) goto L_0x0140
            java.io.ByteArrayOutputStream r11 = new java.io.ByteArrayOutputStream
            r11.<init>()
            java.util.Set r0 = r19.keySet()
            int r12 = r0.size()
            java.lang.String[] r12 = new java.lang.String[r12]
            r0.toArray(r12)
            com.alibaba.analytics.utils.KeyArraySorter r0 = com.alibaba.analytics.utils.KeyArraySorter.getInstance()
            java.lang.String[] r12 = r0.sortResourcesList(r12, r6)
            int r13 = r12.length
            r14 = 0
        L_0x009e:
            if (r14 >= r13) goto L_0x012a
            r0 = r12[r14]
            if (r1 != r8) goto L_0x00d1
            java.lang.Object r15 = r2.get(r0)
            byte[] r15 = (byte[]) r15
            if (r15 == 0) goto L_0x0125
            java.lang.String r10 = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n"
            java.lang.Object[] r7 = new java.lang.Object[r8]     // Catch:{ IOException -> 0x00cc }
            r7[r9] = r0     // Catch:{ IOException -> 0x00cc }
            r7[r6] = r0     // Catch:{ IOException -> 0x00cc }
            java.lang.String r0 = java.lang.String.format(r10, r7)     // Catch:{ IOException -> 0x00cc }
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x00cc }
            r11.write(r0)     // Catch:{ IOException -> 0x00cc }
            r11.write(r15)     // Catch:{ IOException -> 0x00cc }
            java.lang.String r0 = "\r\n"
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x00cc }
            r11.write(r0)     // Catch:{ IOException -> 0x00cc }
            goto L_0x0125
        L_0x00cc:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0125
        L_0x00d1:
            if (r1 != r7) goto L_0x0125
            java.lang.Object r7 = r2.get(r0)
            java.lang.String r7 = (java.lang.String) r7
            int r10 = r11.size()
            if (r10 <= 0) goto L_0x0105
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0100 }
            r10.<init>()     // Catch:{ IOException -> 0x0100 }
            java.lang.String r15 = "&"
            r10.append(r15)     // Catch:{ IOException -> 0x0100 }
            r10.append(r0)     // Catch:{ IOException -> 0x0100 }
            java.lang.String r0 = "="
            r10.append(r0)     // Catch:{ IOException -> 0x0100 }
            r10.append(r7)     // Catch:{ IOException -> 0x0100 }
            java.lang.String r0 = r10.toString()     // Catch:{ IOException -> 0x0100 }
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x0100 }
            r11.write(r0)     // Catch:{ IOException -> 0x0100 }
            goto L_0x0125
        L_0x0100:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0125
        L_0x0105:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0121 }
            r10.<init>()     // Catch:{ IOException -> 0x0121 }
            r10.append(r0)     // Catch:{ IOException -> 0x0121 }
            java.lang.String r0 = "="
            r10.append(r0)     // Catch:{ IOException -> 0x0121 }
            r10.append(r7)     // Catch:{ IOException -> 0x0121 }
            java.lang.String r0 = r10.toString()     // Catch:{ IOException -> 0x0121 }
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x0121 }
            r11.write(r0)     // Catch:{ IOException -> 0x0121 }
            goto L_0x0125
        L_0x0121:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0125:
            int r14 = r14 + 1
            r7 = 3
            goto L_0x009e
        L_0x012a:
            if (r1 != r8) goto L_0x013a
            java.lang.String r0 = "--GJircTeP--\r\n"
            byte[] r0 = r0.getBytes()     // Catch:{ IOException -> 0x0136 }
            r11.write(r0)     // Catch:{ IOException -> 0x0136 }
            goto L_0x013a
        L_0x0136:
            r0 = move-exception
            r0.printStackTrace()
        L_0x013a:
            byte[] r0 = r11.toByteArray()
            r10 = r0
            goto L_0x0141
        L_0x0140:
            r10 = 0
        L_0x0141:
            if (r10 == 0) goto L_0x0145
            int r0 = r10.length
            goto L_0x0146
        L_0x0145:
            r0 = 0
        L_0x0146:
            java.lang.String r2 = "Content-Length"
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r5.setRequestProperty(r2, r0)
        L_0x014f:
            long r6 = java.lang.System.currentTimeMillis()
            r5.connect()     // Catch:{ Exception -> 0x0211, all -> 0x020c }
            if (r1 == r8) goto L_0x015b
            r2 = 3
            if (r1 != r2) goto L_0x017a
        L_0x015b:
            if (r10 == 0) goto L_0x017a
            int r0 = r10.length     // Catch:{ Exception -> 0x0211, all -> 0x020c }
            if (r0 <= 0) goto L_0x017a
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x0211, all -> 0x020c }
            java.io.OutputStream r0 = r5.getOutputStream()     // Catch:{ Exception -> 0x0211, all -> 0x020c }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0211, all -> 0x020c }
            r1.write(r10)     // Catch:{ Exception -> 0x0176, all -> 0x0170 }
            r1.flush()     // Catch:{ Exception -> 0x0176, all -> 0x0170 }
            goto L_0x017b
        L_0x0170:
            r0 = move-exception
            r16 = r1
            r1 = r0
            goto L_0x022e
        L_0x0176:
            r0 = move-exception
            r10 = r1
            goto L_0x0213
        L_0x017a:
            r1 = 0
        L_0x017b:
            if (r1 == 0) goto L_0x0186
            r1.close()     // Catch:{ IOException -> 0x0181 }
            goto L_0x0186
        L_0x0181:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x0186:
            int r0 = r5.getResponseCode()     // Catch:{ IOException -> 0x018d }
            r4.httpResponseCode = r0     // Catch:{ IOException -> 0x018d }
            goto L_0x0191
        L_0x018d:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0191:
            long r0 = java.lang.System.currentTimeMillis()
            long r0 = r0 - r6
            r4.rt = r0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r1.<init>()
            if (r20 == 0) goto L_0x01bd
            java.lang.String r0 = "gzip"
            java.lang.String r2 = r5.getContentEncoding()     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            boolean r0 = r0.equals(r2)     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            if (r0 == 0) goto L_0x01bd
            java.util.zip.GZIPInputStream r0 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            java.io.InputStream r2 = r5.getInputStream()     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            goto L_0x01c6
        L_0x01b5:
            r0 = move-exception
            r1 = r0
            r16 = 0
            goto L_0x0200
        L_0x01ba:
            r0 = move-exception
            r10 = 0
            goto L_0x01ed
        L_0x01bd:
            java.io.DataInputStream r0 = new java.io.DataInputStream     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            java.io.InputStream r2 = r5.getInputStream()     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x01ba, all -> 0x01b5 }
        L_0x01c6:
            r10 = r0
            r0 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r0]     // Catch:{ IOException -> 0x01ec }
        L_0x01cb:
            int r3 = r10.read(r2, r9, r0)     // Catch:{ IOException -> 0x01ec }
            r5 = -1
            if (r3 == r5) goto L_0x01d6
            r1.write(r2, r9, r3)     // Catch:{ IOException -> 0x01ec }
            goto L_0x01cb
        L_0x01d6:
            r10.close()     // Catch:{ Exception -> 0x01da }
            goto L_0x01df
        L_0x01da:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x01df:
            int r0 = r1.size()
            if (r0 <= 0) goto L_0x023f
            byte[] r0 = r1.toByteArray()
            r4.data = r0
            goto L_0x023f
        L_0x01ec:
            r0 = move-exception
        L_0x01ed:
            r0.printStackTrace()     // Catch:{ all -> 0x01fc }
            if (r10 == 0) goto L_0x01fb
            r10.close()     // Catch:{ Exception -> 0x01f6 }
            goto L_0x01fb
        L_0x01f6:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x01fb:
            return r4
        L_0x01fc:
            r0 = move-exception
            r1 = r0
            r16 = r10
        L_0x0200:
            if (r16 == 0) goto L_0x020b
            r16.close()     // Catch:{ Exception -> 0x0206 }
            goto L_0x020b
        L_0x0206:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x020b:
            throw r1
        L_0x020c:
            r0 = move-exception
            r1 = r0
            r16 = 0
            goto L_0x022e
        L_0x0211:
            r0 = move-exception
            r10 = 0
        L_0x0213:
            r0.printStackTrace()     // Catch:{ all -> 0x022a }
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x022a }
            r2 = 0
            long r0 = r0 - r6
            r4.rt = r0     // Catch:{ all -> 0x022a }
            if (r10 == 0) goto L_0x0229
            r10.close()     // Catch:{ IOException -> 0x0224 }
            goto L_0x0229
        L_0x0224:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x0229:
            return r4
        L_0x022a:
            r0 = move-exception
            r1 = r0
            r16 = r10
        L_0x022e:
            if (r16 == 0) goto L_0x0239
            r16.close()     // Catch:{ IOException -> 0x0234 }
            goto L_0x0239
        L_0x0234:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x0239:
            throw r1
        L_0x023a:
            r0 = move-exception
            r0.printStackTrace()
            return r4
        L_0x023f:
            return r4
        L_0x0240:
            r0 = move-exception
            r0.printStackTrace()
            return r4
        L_0x0245:
            r0 = move-exception
            r0.printStackTrace()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.HttpUtils.sendRequest(int, java.lang.String, java.util.Map, boolean):com.alibaba.analytics.utils.HttpUtils$HttpResponse");
    }
}
