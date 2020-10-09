package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import com.xiaomi.channel.commonutils.logger.b;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class hc {
    private static boolean a = false;

    static class a implements Runnable {
        private Context a;

        /* renamed from: a  reason: collision with other field name */
        private hf f444a;

        public a(Context context, hf hfVar) {
            this.f444a = hfVar;
            this.a = context;
        }

        public void run() {
            hc.c(this.a, this.f444a);
        }
    }

    private static void a(Context context) {
        File file = new File(context.getFilesDir() + "/tdReadTemp");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void a(Context context, hf hfVar) {
        ai.a(context).a((Runnable) new a(context, hfVar));
    }

    private static void a(Context context, hf hfVar, File file, byte[] bArr) {
        String str;
        int a2;
        ArrayList arrayList = new ArrayList();
        byte[] bArr2 = new byte[4];
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
            loop0:
            while (true) {
                int i = 0;
                int i2 = 0;
                while (true) {
                    try {
                        int read = bufferedInputStream2.read(bArr2);
                        if (read == -1) {
                            break loop0;
                        } else if (read != 4) {
                            str = "TinyData read from cache file failed cause lengthBuffer error. size:" + read;
                            break loop0;
                        } else {
                            a2 = ac.a(bArr2);
                            if (a2 < 1) {
                                break loop0;
                            } else if (a2 > 10240) {
                                break loop0;
                            } else {
                                byte[] bArr3 = new byte[a2];
                                int read2 = bufferedInputStream2.read(bArr3);
                                if (read2 != a2) {
                                    str = "TinyData read from cache file failed cause buffer size not equal length. size:" + read2 + "__length:" + a2;
                                    break loop0;
                                }
                                byte[] a3 = h.a(bArr, bArr3);
                                if (a3 != null) {
                                    if (a3.length != 0) {
                                        hk hkVar = new hk();
                                        iq.a(hkVar, a3);
                                        arrayList.add(hkVar);
                                        i++;
                                        i2 += a3.length;
                                        if (i >= 8 || i2 >= 10240) {
                                            b.a("TinyData readTinyDataFromFile upload tiny data by part. items:" + arrayList.size() + " ts:" + System.currentTimeMillis());
                                            hd.a(context, hfVar, (List<hk>) arrayList);
                                            arrayList = new ArrayList();
                                        }
                                    }
                                }
                                b.d("TinyData read from cache file failed cause decrypt fail");
                            }
                        }
                    } catch (Exception e) {
                        e = e;
                        bufferedInputStream = bufferedInputStream2;
                        try {
                            b.a((Throwable) e);
                            y.a((Closeable) bufferedInputStream);
                        } catch (Throwable th) {
                            th = th;
                            bufferedInputStream2 = bufferedInputStream;
                            y.a((Closeable) bufferedInputStream2);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        y.a((Closeable) bufferedInputStream2);
                        throw th;
                    }
                }
            }
            str = "TinyData read from cache file failed cause lengthBuffer < 1 || too big. length:" + a2;
            b.d(str);
            hd.a(context, hfVar, (List<hk>) arrayList);
            b.a("TinyData readTinyDataFromFile upload tiny data at last. items:" + arrayList.size() + " ts:" + System.currentTimeMillis());
            if (file != null && file.exists() && !file.delete()) {
                b.a("TinyData delete reading temp file failed");
            }
            y.a((Closeable) bufferedInputStream2);
        } catch (Exception e2) {
            e = e2;
            b.a((Throwable) e);
            y.a((Closeable) bufferedInputStream);
        }
    }

    private static void b(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 4).edit();
        edit.putLong("last_tiny_data_upload_timestamp", System.currentTimeMillis() / 1000);
        edit.commit();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c5  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00cb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void c(android.content.Context r7, com.xiaomi.push.hf r8) {
        /*
            boolean r0 = a
            if (r0 != 0) goto L_0x00ee
            r0 = 1
            a = r0
            java.io.File r0 = new java.io.File
            java.io.File r1 = r7.getFilesDir()
            java.lang.String r2 = "tiny_data.data"
            r0.<init>(r1, r2)
            boolean r1 = r0.exists()
            if (r1 != 0) goto L_0x001e
            java.lang.String r7 = "TinyData no ready file to get data."
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r7)
            return
        L_0x001e:
            a(r7)
            byte[] r1 = com.xiaomi.push.service.bf.a((android.content.Context) r7)
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0083, all -> 0x007f }
            java.io.File r4 = r7.getFilesDir()     // Catch:{ Exception -> 0x0083, all -> 0x007f }
            java.lang.String r5 = "tiny_data.lock"
            r3.<init>(r4, r5)     // Catch:{ Exception -> 0x0083, all -> 0x007f }
            com.xiaomi.push.y.a((java.io.File) r3)     // Catch:{ Exception -> 0x0083, all -> 0x007f }
            java.io.RandomAccessFile r4 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0083, all -> 0x007f }
            java.lang.String r5 = "rw"
            r4.<init>(r3, r5)     // Catch:{ Exception -> 0x0083, all -> 0x007f }
            java.nio.channels.FileChannel r3 = r4.getChannel()     // Catch:{ Exception -> 0x007d }
            java.nio.channels.FileLock r3 = r3.lock()     // Catch:{ Exception -> 0x007d }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r5.<init>()     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.io.File r6 = r7.getFilesDir()     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r5.append(r6)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r6 = "/tdReadTemp"
            r5.append(r6)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r6 = "/"
            r5.append(r6)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r6 = "tiny_data.data"
            r5.append(r6)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r0.renameTo(r2)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            if (r3 == 0) goto L_0x0098
            boolean r0 = r3.isValid()
            if (r0 == 0) goto L_0x0098
            r3.release()     // Catch:{ IOException -> 0x0076 }
            goto L_0x0098
        L_0x0076:
            r0 = move-exception
            goto L_0x0095
        L_0x0078:
            r7 = move-exception
            goto L_0x00da
        L_0x007a:
            r0 = move-exception
            r2 = r3
            goto L_0x0085
        L_0x007d:
            r0 = move-exception
            goto L_0x0085
        L_0x007f:
            r7 = move-exception
            r3 = r2
            r4 = r3
            goto L_0x00da
        L_0x0083:
            r0 = move-exception
            r4 = r2
        L_0x0085:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)     // Catch:{ all -> 0x00d8 }
            if (r2 == 0) goto L_0x0098
            boolean r0 = r2.isValid()
            if (r0 == 0) goto L_0x0098
            r2.release()     // Catch:{ IOException -> 0x0094 }
            goto L_0x0098
        L_0x0094:
            r0 = move-exception
        L_0x0095:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x0098:
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.io.File r3 = r7.getFilesDir()
            r2.append(r3)
            java.lang.String r3 = "/tdReadTemp"
            r2.append(r3)
            java.lang.String r3 = "/"
            r2.append(r3)
            java.lang.String r3 = "tiny_data.data"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x00cb
            java.lang.String r7 = "TinyData no ready file to get data."
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r7)
            return
        L_0x00cb:
            a(r7, r8, r0, r1)
            r8 = 0
            com.xiaomi.push.hb.a((boolean) r8)
            b(r7)
            a = r8
            return
        L_0x00d8:
            r7 = move-exception
            r3 = r2
        L_0x00da:
            if (r3 == 0) goto L_0x00ea
            boolean r8 = r3.isValid()
            if (r8 == 0) goto L_0x00ea
            r3.release()     // Catch:{ IOException -> 0x00e6 }
            goto L_0x00ea
        L_0x00e6:
            r8 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r8)
        L_0x00ea:
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            throw r7
        L_0x00ee:
            java.lang.String r7 = "TinyData extractTinyData is running"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hc.c(android.content.Context, com.xiaomi.push.hf):void");
    }
}
