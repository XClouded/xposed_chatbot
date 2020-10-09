package mtopsdk.network.domain;

import java.io.IOException;
import java.io.InputStream;

public abstract class ResponseBody {
    private static final String TAG = "mtopsdk.ResponseBody";
    private byte[] bodyBytes = null;

    public abstract InputStream byteStream();

    public abstract long contentLength() throws IOException;

    public abstract String contentType();

    public byte[] getBytes() throws IOException {
        if (this.bodyBytes == null) {
            this.bodyBytes = readBytes();
        }
        return this.bodyBytes;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0057 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] readBytes() throws java.io.IOException {
        /*
            r8 = this;
            long r0 = r8.contentLength()
            r2 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 > 0) goto L_0x0076
            java.io.InputStream r2 = r8.byteStream()
            r3 = 0
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch:{ Exception -> 0x0044, all -> 0x0040 }
            r4.<init>(r2)     // Catch:{ Exception -> 0x0044, all -> 0x0040 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            r2.<init>()     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r5 = new byte[r5]     // Catch:{ Exception -> 0x0038 }
        L_0x001e:
            int r6 = r4.read(r5)     // Catch:{ Exception -> 0x0038 }
            r7 = -1
            if (r6 == r7) goto L_0x002a
            r7 = 0
            r2.write(r5, r7, r6)     // Catch:{ Exception -> 0x0038 }
            goto L_0x001e
        L_0x002a:
            r2.flush()     // Catch:{ Exception -> 0x0038 }
            byte[] r5 = r2.toByteArray()     // Catch:{ Exception -> 0x0038 }
            mtopsdk.network.util.NetworkUtils.closeQuietly(r4)
            mtopsdk.network.util.NetworkUtils.closeQuietly(r2)
            goto L_0x0055
        L_0x0038:
            r5 = move-exception
            goto L_0x0047
        L_0x003a:
            r0 = move-exception
            r2 = r3
            goto L_0x006f
        L_0x003d:
            r5 = move-exception
            r2 = r3
            goto L_0x0047
        L_0x0040:
            r0 = move-exception
            r2 = r3
            r4 = r2
            goto L_0x006f
        L_0x0044:
            r5 = move-exception
            r2 = r3
            r4 = r2
        L_0x0047:
            java.lang.String r6 = "mtopsdk.ResponseBody"
            java.lang.String r7 = "[readBytes] read bytes from byteStream error."
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r6, (java.lang.String) r7, (java.lang.Throwable) r5)     // Catch:{ all -> 0x006e }
            mtopsdk.network.util.NetworkUtils.closeQuietly(r4)
            mtopsdk.network.util.NetworkUtils.closeQuietly(r2)
            r5 = r3
        L_0x0055:
            if (r5 != 0) goto L_0x0058
            return r3
        L_0x0058:
            r2 = -1
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 == 0) goto L_0x006d
            int r2 = r5.length
            long r2 = (long) r2
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0065
            goto L_0x006d
        L_0x0065:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Content-Length and stream length disagree"
            r0.<init>(r1)
            throw r0
        L_0x006d:
            return r5
        L_0x006e:
            r0 = move-exception
        L_0x006f:
            mtopsdk.network.util.NetworkUtils.closeQuietly(r4)
            mtopsdk.network.util.NetworkUtils.closeQuietly(r2)
            throw r0
        L_0x0076:
            java.io.IOException r2 = new java.io.IOException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Cannot buffer entire body for content length: "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.<init>(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.network.domain.ResponseBody.readBytes():byte[]");
    }
}
