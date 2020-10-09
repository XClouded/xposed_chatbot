package com.xiaomi.push.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import com.xiaomi.push.x;
import com.xiaomi.push.y;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class af {
    private static long a;

    public static class a {
        int a;

        /* renamed from: a  reason: collision with other field name */
        byte[] f840a;

        public a(byte[] bArr, int i) {
            this.f840a = bArr;
            this.a = i;
        }
    }

    public static class b {
        public long a;

        /* renamed from: a  reason: collision with other field name */
        public Bitmap f841a;

        public b(Bitmap bitmap, long j) {
            this.f841a = bitmap;
            this.a = j;
        }
    }

    private static int a(Context context, InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        if (options.outWidth == -1 || options.outHeight == -1) {
            com.xiaomi.channel.commonutils.logger.b.a("decode dimension failed for bitmap.");
            return 1;
        }
        int round = Math.round((((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * 48.0f);
        if (options.outWidth <= round || options.outHeight <= round) {
            return 1;
        }
        return Math.min(options.outWidth / round, options.outHeight / round);
    }

    public static Bitmap a(Context context, String str) {
        InputStream inputStream;
        InputStream inputStream2;
        int a2;
        Uri parse = Uri.parse(str);
        try {
            inputStream2 = context.getContentResolver().openInputStream(parse);
            try {
                a2 = a(context, inputStream2);
                inputStream = context.getContentResolver().openInputStream(parse);
            } catch (IOException e) {
                e = e;
                inputStream = null;
                try {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                    y.a((Closeable) inputStream);
                    y.a((Closeable) inputStream2);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    y.a((Closeable) inputStream);
                    y.a((Closeable) inputStream2);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
                y.a((Closeable) inputStream);
                y.a((Closeable) inputStream2);
                throw th;
            }
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = a2;
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, (Rect) null, options);
                y.a((Closeable) inputStream);
                y.a((Closeable) inputStream2);
                return decodeStream;
            } catch (IOException e2) {
                e = e2;
                com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                y.a((Closeable) inputStream);
                y.a((Closeable) inputStream2);
                return null;
            }
        } catch (IOException e3) {
            e = e3;
            inputStream = null;
            inputStream2 = null;
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
            y.a((Closeable) inputStream);
            y.a((Closeable) inputStream2);
            return null;
        } catch (Throwable th3) {
            th = th3;
            inputStream = null;
            inputStream2 = null;
            y.a((Closeable) inputStream);
            y.a((Closeable) inputStream2);
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00d6, code lost:
        if (r1 == null) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00d8, code lost:
        r1.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00f5, code lost:
        if (r1 == null) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00f8, code lost:
        return null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:63:0x00de */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0100  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:56:0x00d0=Splitter:B:56:0x00d0, B:63:0x00de=Splitter:B:63:0x00de} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.xiaomi.push.service.af.a a(java.lang.String r10, boolean r11) {
        /*
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch:{ SocketTimeoutException -> 0x00dc, IOException -> 0x00cd, all -> 0x00ca }
            r1.<init>(r10)     // Catch:{ SocketTimeoutException -> 0x00dc, IOException -> 0x00cd, all -> 0x00ca }
            java.net.URLConnection r1 = r1.openConnection()     // Catch:{ SocketTimeoutException -> 0x00dc, IOException -> 0x00cd, all -> 0x00ca }
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch:{ SocketTimeoutException -> 0x00dc, IOException -> 0x00cd, all -> 0x00ca }
            r2 = 8000(0x1f40, float:1.121E-41)
            r1.setConnectTimeout(r2)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r2 = 20000(0x4e20, float:2.8026E-41)
            r1.setReadTimeout(r2)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r1.connect()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            int r2 = r1.getContentLength()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r3 = 102400(0x19000, float:1.43493E-40)
            if (r11 == 0) goto L_0x0049
            if (r2 <= r3) goto L_0x0049
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r11.<init>()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.lang.String r3 = "Bitmap size is too big, max size is 102400  contentLen size is "
            r11.append(r3)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r11.append(r2)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.lang.String r2 = " from url "
            r11.append(r2)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r11.append(r10)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.lang.String r11 = r11.toString()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r11)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            if (r1 == 0) goto L_0x0048
            r1.disconnect()
        L_0x0048:
            return r0
        L_0x0049:
            int r2 = r1.getResponseCode()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r4 = 200(0xc8, float:2.8E-43)
            if (r2 == r4) goto L_0x0073
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r11.<init>()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.lang.String r3 = "Invalid Http Response Code "
            r11.append(r3)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            r11.append(r2)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.lang.String r2 = " received"
            r11.append(r2)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.lang.String r11 = r11.toString()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r11)     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            if (r1 == 0) goto L_0x0072
            r1.disconnect()
        L_0x0072:
            return r0
        L_0x0073:
            java.io.InputStream r2 = r1.getInputStream()     // Catch:{ SocketTimeoutException -> 0x00c8, IOException -> 0x00c5, all -> 0x00c3 }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            r4.<init>()     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            if (r11 == 0) goto L_0x0082
            r11 = 102400(0x19000, float:1.43493E-40)
            goto L_0x0085
        L_0x0082:
            r11 = 2048000(0x1f4000, float:2.869859E-39)
        L_0x0085:
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r5]     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
        L_0x0089:
            if (r11 <= 0) goto L_0x0099
            r7 = 0
            int r8 = r2.read(r6, r7, r5)     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            r9 = -1
            if (r8 != r9) goto L_0x0094
            goto L_0x0099
        L_0x0094:
            int r11 = r11 - r8
            r4.write(r6, r7, r8)     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            goto L_0x0089
        L_0x0099:
            if (r11 > 0) goto L_0x00ae
            java.lang.String r11 = "length 102400 exhausted."
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r11)     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            com.xiaomi.push.service.af$a r11 = new com.xiaomi.push.service.af$a     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            r11.<init>(r0, r3)     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            if (r1 == 0) goto L_0x00ad
            r1.disconnect()
        L_0x00ad:
            return r11
        L_0x00ae:
            byte[] r11 = r4.toByteArray()     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            com.xiaomi.push.service.af$a r3 = new com.xiaomi.push.service.af$a     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            int r4 = r11.length     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            r3.<init>(r11, r4)     // Catch:{ SocketTimeoutException -> 0x00de, IOException -> 0x00c1 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            if (r1 == 0) goto L_0x00c0
            r1.disconnect()
        L_0x00c0:
            return r3
        L_0x00c1:
            r10 = move-exception
            goto L_0x00d0
        L_0x00c3:
            r10 = move-exception
            goto L_0x00fb
        L_0x00c5:
            r10 = move-exception
            r2 = r0
            goto L_0x00d0
        L_0x00c8:
            r2 = r0
            goto L_0x00de
        L_0x00ca:
            r10 = move-exception
            r1 = r0
            goto L_0x00fb
        L_0x00cd:
            r10 = move-exception
            r1 = r0
            r2 = r1
        L_0x00d0:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r10)     // Catch:{ all -> 0x00f9 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            if (r1 == 0) goto L_0x00f8
        L_0x00d8:
            r1.disconnect()
            goto L_0x00f8
        L_0x00dc:
            r1 = r0
            r2 = r1
        L_0x00de:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f9 }
            r11.<init>()     // Catch:{ all -> 0x00f9 }
            java.lang.String r3 = "Connect timeout to "
            r11.append(r3)     // Catch:{ all -> 0x00f9 }
            r11.append(r10)     // Catch:{ all -> 0x00f9 }
            java.lang.String r10 = r11.toString()     // Catch:{ all -> 0x00f9 }
            com.xiaomi.channel.commonutils.logger.b.d(r10)     // Catch:{ all -> 0x00f9 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            if (r1 == 0) goto L_0x00f8
            goto L_0x00d8
        L_0x00f8:
            return r0
        L_0x00f9:
            r10 = move-exception
            r0 = r2
        L_0x00fb:
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            if (r1 == 0) goto L_0x0103
            r1.disconnect()
        L_0x0103:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.af.a(java.lang.String, boolean):com.xiaomi.push.service.af$a");
    }

    public static b a(Context context, String str, boolean z) {
        ByteArrayInputStream byteArrayInputStream = null;
        b bVar = new b((Bitmap) null, 0);
        Bitmap b2 = b(context, str);
        if (b2 != null) {
            bVar.f841a = b2;
            return bVar;
        }
        try {
            a a2 = a(str, z);
            if (a2 == null) {
                y.a((Closeable) null);
                return bVar;
            }
            bVar.a = (long) a2.a;
            byte[] bArr = a2.f840a;
            if (bArr != null) {
                if (z) {
                    ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr);
                    try {
                        int a3 = a(context, (InputStream) byteArrayInputStream2);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = a3;
                        bVar.f841a = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                        byteArrayInputStream = byteArrayInputStream2;
                    } catch (Exception e) {
                        e = e;
                        byteArrayInputStream = byteArrayInputStream2;
                        try {
                            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                            y.a((Closeable) byteArrayInputStream);
                            return bVar;
                        } catch (Throwable th) {
                            th = th;
                            y.a((Closeable) byteArrayInputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        byteArrayInputStream = byteArrayInputStream2;
                        y.a((Closeable) byteArrayInputStream);
                        throw th;
                    }
                } else {
                    bVar.f841a = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
                }
            }
            a(context, a2.f840a, str);
            y.a((Closeable) byteArrayInputStream);
            return bVar;
        } catch (Exception e2) {
            e = e2;
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
            y.a((Closeable) byteArrayInputStream);
            return bVar;
        }
    }

    private static void a(Context context) {
        File file = new File(context.getCacheDir().getPath() + File.separator + "mipush_icon");
        if (file.exists()) {
            if (a == 0) {
                a = x.a(file);
            }
            if (a > 15728640) {
                try {
                    File[] listFiles = file.listFiles();
                    for (int i = 0; i < listFiles.length; i++) {
                        if (!listFiles[i].isDirectory() && Math.abs(System.currentTimeMillis() - listFiles[i].lastModified()) > 1209600) {
                            listFiles[i].delete();
                        }
                    }
                } catch (Exception e) {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                }
                a = 0;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r4, byte[] r5, java.lang.String r6) {
        /*
            if (r5 != 0) goto L_0x0008
            java.lang.String r4 = "cannot save small icon cause bitmap is null"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r4)
            return
        L_0x0008:
            a(r4)
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.io.File r2 = r4.getCacheDir()
            java.lang.String r2 = r2.getPath()
            r1.append(r2)
            java.lang.String r2 = java.io.File.separator
            r1.append(r2)
            java.lang.String r2 = "mipush_icon"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            boolean r1 = r0.exists()
            if (r1 != 0) goto L_0x0037
            r0.mkdirs()
        L_0x0037:
            java.io.File r1 = new java.io.File
            java.lang.String r6 = com.xiaomi.push.ay.a((java.lang.String) r6)
            r1.<init>(r0, r6)
            r6 = 0
            boolean r0 = r1.exists()     // Catch:{ Exception -> 0x0069, all -> 0x0066 }
            if (r0 != 0) goto L_0x004a
            r1.createNewFile()     // Catch:{ Exception -> 0x0069, all -> 0x0066 }
        L_0x004a:
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0069, all -> 0x0066 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x0069, all -> 0x0066 }
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0064 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0064 }
            r2.write(r5)     // Catch:{ Exception -> 0x0061, all -> 0x005e }
            r2.flush()     // Catch:{ Exception -> 0x0061, all -> 0x005e }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            goto L_0x0071
        L_0x005e:
            r4 = move-exception
            r6 = r2
            goto L_0x00ac
        L_0x0061:
            r5 = move-exception
            r6 = r2
            goto L_0x006b
        L_0x0064:
            r5 = move-exception
            goto L_0x006b
        L_0x0066:
            r4 = move-exception
            r0 = r6
            goto L_0x00ac
        L_0x0069:
            r5 = move-exception
            r0 = r6
        L_0x006b:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x00ab }
            com.xiaomi.push.y.a((java.io.Closeable) r6)
        L_0x0071:
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            long r5 = a
            r2 = 0
            int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x00aa
            java.io.File r5 = new java.io.File
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.io.File r4 = r4.getCacheDir()
            java.lang.String r4 = r4.getPath()
            r6.append(r4)
            java.lang.String r4 = java.io.File.separator
            r6.append(r4)
            java.lang.String r4 = "mipush_icon"
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            r5.<init>(r4)
            long r4 = com.xiaomi.push.x.a(r5)
            long r0 = r1.length()
            long r4 = r4 + r0
            a = r4
        L_0x00aa:
            return
        L_0x00ab:
            r4 = move-exception
        L_0x00ac:
            com.xiaomi.push.y.a((java.io.Closeable) r6)
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.af.a(android.content.Context, byte[], java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0043, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0044, code lost:
        r4 = r6;
        r6 = r5;
        r5 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0048, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0049, code lost:
        r4 = r6;
        r6 = r5;
        r5 = r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0048 A[ExcHandler: all (r6v6 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:5:0x0034] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Bitmap b(android.content.Context r5, java.lang.String r6) {
        /*
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.io.File r5 = r5.getCacheDir()
            java.lang.String r5 = r5.getPath()
            r1.append(r5)
            java.lang.String r5 = java.io.File.separator
            r1.append(r5)
            java.lang.String r5 = "mipush_icon"
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            java.lang.String r6 = com.xiaomi.push.ay.a((java.lang.String) r6)
            r0.<init>(r5, r6)
            boolean r5 = r0.exists()
            r6 = 0
            if (r5 != 0) goto L_0x002f
            return r6
        L_0x002f:
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0054 }
            r5.<init>(r0)     // Catch:{ Exception -> 0x0054 }
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r5)     // Catch:{ Exception -> 0x004d, all -> 0x0048 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0043, all -> 0x0048 }
            r0.setLastModified(r2)     // Catch:{ Exception -> 0x0043, all -> 0x0048 }
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            goto L_0x005c
        L_0x0043:
            r6 = move-exception
            r4 = r6
            r6 = r5
            r5 = r4
            goto L_0x0056
        L_0x0048:
            r6 = move-exception
            r4 = r6
            r6 = r5
            r5 = r4
            goto L_0x005d
        L_0x004d:
            r0 = move-exception
            r1 = r6
            r6 = r5
            r5 = r0
            goto L_0x0056
        L_0x0052:
            r5 = move-exception
            goto L_0x005d
        L_0x0054:
            r5 = move-exception
            r1 = r6
        L_0x0056:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x0052 }
            com.xiaomi.push.y.a((java.io.Closeable) r6)
        L_0x005c:
            return r1
        L_0x005d:
            com.xiaomi.push.y.a((java.io.Closeable) r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.af.b(android.content.Context, java.lang.String):android.graphics.Bitmap");
    }
}
