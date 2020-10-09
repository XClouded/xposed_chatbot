package com.xiaomi.clientreport.processor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.data.EventClientReport;
import com.xiaomi.push.ac;
import com.xiaomi.push.ay;
import com.xiaomi.push.be;
import com.xiaomi.push.h;
import com.xiaomi.push.y;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class a implements IEventProcessor {
    protected Context a;

    /* renamed from: a  reason: collision with other field name */
    private HashMap<String, ArrayList<com.xiaomi.clientreport.data.a>> f12a;

    public a(Context context) {
        a(context);
    }

    public static String a(com.xiaomi.clientreport.data.a aVar) {
        return String.valueOf(aVar.production);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005c, code lost:
        r8 = "eventData read from cache file failed cause lengthBuffer < 1 || lengthBuffer > 4K";
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String> a(java.lang.String r8) {
        /*
            r7 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 4
            byte[] r2 = new byte[r1]
            byte[] r3 = new byte[r1]
            r4 = 0
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x006b }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x006b }
            r6.<init>(r8)     // Catch:{ Exception -> 0x006b }
            r5.<init>(r6)     // Catch:{ Exception -> 0x006b }
        L_0x0015:
            int r8 = r5.read(r2)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            r4 = -1
            if (r8 != r4) goto L_0x001d
            goto L_0x005f
        L_0x001d:
            if (r8 == r1) goto L_0x0025
            java.lang.String r8 = "eventData read from cache file failed because magicNumber error"
        L_0x0021:
            com.xiaomi.channel.commonutils.logger.b.d(r8)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            goto L_0x005f
        L_0x0025:
            int r8 = com.xiaomi.push.ac.a((byte[]) r2)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            r6 = -573785174(0xffffffffddccbbaa, float:-1.84407149E18)
            if (r8 == r6) goto L_0x0031
            java.lang.String r8 = "eventData read from cache file failed because magicNumber error"
            goto L_0x0021
        L_0x0031:
            int r8 = r5.read(r3)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            if (r8 != r4) goto L_0x0038
            goto L_0x005f
        L_0x0038:
            if (r8 == r1) goto L_0x003d
            java.lang.String r8 = "eventData read from cache file failed cause lengthBuffer error"
            goto L_0x0021
        L_0x003d:
            int r8 = com.xiaomi.push.ac.a((byte[]) r3)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            r4 = 1
            if (r8 < r4) goto L_0x005c
            r4 = 4096(0x1000, float:5.74E-42)
            if (r8 <= r4) goto L_0x0049
            goto L_0x005c
        L_0x0049:
            byte[] r4 = new byte[r8]     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            int r6 = r5.read(r4)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            if (r6 == r8) goto L_0x0054
            java.lang.String r8 = "eventData read from cache file failed cause buffer size not equal length"
            goto L_0x0021
        L_0x0054:
            java.lang.String r8 = r7.bytesToString(r4)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            r0.add(r8)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            goto L_0x0015
        L_0x005c:
            java.lang.String r8 = "eventData read from cache file failed cause lengthBuffer < 1 || lengthBuffer > 4K"
            goto L_0x0021
        L_0x005f:
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            goto L_0x0072
        L_0x0063:
            r8 = move-exception
            goto L_0x0073
        L_0x0065:
            r8 = move-exception
            r4 = r5
            goto L_0x006c
        L_0x0068:
            r8 = move-exception
            r5 = r4
            goto L_0x0073
        L_0x006b:
            r8 = move-exception
        L_0x006c:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r8)     // Catch:{ all -> 0x0068 }
            com.xiaomi.push.y.a((java.io.Closeable) r4)
        L_0x0072:
            return r0
        L_0x0073:
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.clientreport.processor.a.a(java.lang.String):java.util.List");
    }

    private void a(com.xiaomi.clientreport.data.a[] aVarArr, String str) {
        if (aVarArr != null && aVarArr.length > 0 && !TextUtils.isEmpty(str)) {
            BufferedOutputStream bufferedOutputStream = null;
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(new File(str), true));
                try {
                    for (com.xiaomi.clientreport.data.a aVar : aVarArr) {
                        if (aVar != null) {
                            byte[] stringToBytes = stringToBytes(aVar.toJsonString());
                            if (stringToBytes != null && stringToBytes.length >= 1) {
                                if (stringToBytes.length <= 4096) {
                                    bufferedOutputStream2.write(ac.a(-573785174));
                                    bufferedOutputStream2.write(ac.a(stringToBytes.length));
                                    bufferedOutputStream2.write(stringToBytes);
                                    bufferedOutputStream2.flush();
                                }
                            }
                            b.d("event data throw a invalid item ");
                        }
                    }
                    y.a((Closeable) bufferedOutputStream2);
                } catch (Exception e) {
                    e = e;
                    bufferedOutputStream = bufferedOutputStream2;
                    try {
                        b.a("event data write to cache file failed cause exception", (Throwable) e);
                        y.a((Closeable) bufferedOutputStream);
                    } catch (Throwable th) {
                        th = th;
                        bufferedOutputStream2 = bufferedOutputStream;
                        y.a((Closeable) bufferedOutputStream2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    y.a((Closeable) bufferedOutputStream2);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                b.a("event data write to cache file failed cause exception", (Throwable) e);
                y.a((Closeable) bufferedOutputStream);
            }
        }
    }

    private String b(com.xiaomi.clientreport.data.a aVar) {
        File externalFilesDir = this.a.getExternalFilesDir("event");
        String a2 = a(aVar);
        if (externalFilesDir == null) {
            return null;
        }
        String str = externalFilesDir.getAbsolutePath() + File.separator + a2;
        for (int i = 0; i < 100; i++) {
            String str2 = str + i;
            if (be.a(this.a, str2)) {
                return str2;
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:71:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00bf A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a() {
        /*
            r11 = this;
            android.content.Context r0 = r11.a
            java.lang.String r1 = "event"
            java.lang.String r2 = "eventUploading"
            com.xiaomi.push.be.a(r0, r1, r2)
            android.content.Context r0 = r11.a
            java.lang.String r1 = "eventUploading"
            java.io.File[] r0 = com.xiaomi.push.be.a((android.content.Context) r0, (java.lang.String) r1)
            if (r0 == 0) goto L_0x00dc
            int r1 = r0.length
            if (r1 > 0) goto L_0x0018
            goto L_0x00dc
        L_0x0018:
            int r1 = r0.length
            r2 = 0
            r3 = 0
            r4 = r3
            r5 = r4
        L_0x001d:
            if (r2 >= r1) goto L_0x00dc
            r6 = r0[r2]
            if (r6 != 0) goto L_0x003d
            if (r3 == 0) goto L_0x0033
            boolean r6 = r3.isValid()
            if (r6 == 0) goto L_0x0033
            r3.release()     // Catch:{ IOException -> 0x002f }
            goto L_0x0033
        L_0x002f:
            r6 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r6)
        L_0x0033:
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            if (r5 == 0) goto L_0x00bf
        L_0x0038:
            r5.delete()
            goto L_0x00bf
        L_0x003d:
            java.lang.String r7 = r6.getAbsolutePath()     // Catch:{ Exception -> 0x00a4 }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x00a4 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a4 }
            r9.<init>()     // Catch:{ Exception -> 0x00a4 }
            r9.append(r7)     // Catch:{ Exception -> 0x00a4 }
            java.lang.String r10 = ".lock"
            r9.append(r10)     // Catch:{ Exception -> 0x00a4 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x00a4 }
            r8.<init>(r9)     // Catch:{ Exception -> 0x00a4 }
            com.xiaomi.push.y.a((java.io.File) r8)     // Catch:{ Exception -> 0x009e, all -> 0x009b }
            java.io.RandomAccessFile r5 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x009e, all -> 0x009b }
            java.lang.String r9 = "rw"
            r5.<init>(r8, r9)     // Catch:{ Exception -> 0x009e, all -> 0x009b }
            java.nio.channels.FileChannel r4 = r5.getChannel()     // Catch:{ Exception -> 0x0097, all -> 0x0094 }
            java.nio.channels.FileLock r4 = r4.lock()     // Catch:{ Exception -> 0x0097, all -> 0x0094 }
            java.util.List r3 = r11.a((java.lang.String) r7)     // Catch:{ Exception -> 0x0090, all -> 0x008d }
            r11.a((java.util.List<java.lang.String>) r3)     // Catch:{ Exception -> 0x0090, all -> 0x008d }
            r6.delete()     // Catch:{ Exception -> 0x0090, all -> 0x008d }
            if (r4 == 0) goto L_0x0083
            boolean r3 = r4.isValid()
            if (r3 == 0) goto L_0x0083
            r4.release()     // Catch:{ IOException -> 0x007f }
            goto L_0x0083
        L_0x007f:
            r3 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r3)
        L_0x0083:
            com.xiaomi.push.y.a((java.io.Closeable) r5)
            r8.delete()
            r3 = r4
            r4 = r5
            r5 = r8
            goto L_0x00bf
        L_0x008d:
            r0 = move-exception
            r3 = r4
            goto L_0x0095
        L_0x0090:
            r3 = move-exception
            r6 = r3
            r3 = r4
            goto L_0x0099
        L_0x0094:
            r0 = move-exception
        L_0x0095:
            r4 = r5
            goto L_0x009c
        L_0x0097:
            r4 = move-exception
            r6 = r4
        L_0x0099:
            r4 = r5
            goto L_0x00a0
        L_0x009b:
            r0 = move-exception
        L_0x009c:
            r5 = r8
            goto L_0x00c3
        L_0x009e:
            r5 = move-exception
            r6 = r5
        L_0x00a0:
            r5 = r8
            goto L_0x00a5
        L_0x00a2:
            r0 = move-exception
            goto L_0x00c3
        L_0x00a4:
            r6 = move-exception
        L_0x00a5:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r6)     // Catch:{ all -> 0x00a2 }
            if (r3 == 0) goto L_0x00b8
            boolean r6 = r3.isValid()
            if (r6 == 0) goto L_0x00b8
            r3.release()     // Catch:{ IOException -> 0x00b4 }
            goto L_0x00b8
        L_0x00b4:
            r6 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r6)
        L_0x00b8:
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            if (r5 == 0) goto L_0x00bf
            goto L_0x0038
        L_0x00bf:
            int r2 = r2 + 1
            goto L_0x001d
        L_0x00c3:
            if (r3 == 0) goto L_0x00d3
            boolean r1 = r3.isValid()
            if (r1 == 0) goto L_0x00d3
            r3.release()     // Catch:{ IOException -> 0x00cf }
            goto L_0x00d3
        L_0x00cf:
            r1 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r1)
        L_0x00d3:
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            if (r5 == 0) goto L_0x00db
            r5.delete()
        L_0x00db:
            throw r0
        L_0x00dc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.clientreport.processor.a.a():void");
    }

    public void a(Context context) {
        this.a = context;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m22a(com.xiaomi.clientreport.data.a aVar) {
        if ((aVar instanceof EventClientReport) && this.f12a != null) {
            EventClientReport eventClientReport = (EventClientReport) aVar;
            String a2 = a((com.xiaomi.clientreport.data.a) eventClientReport);
            ArrayList arrayList = this.f12a.get(a2);
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(eventClientReport);
            this.f12a.put(a2, arrayList);
        }
    }

    public void a(List<String> list) {
        be.a(this.a, list);
    }

    public void a(com.xiaomi.clientreport.data.a[] aVarArr) {
        RandomAccessFile randomAccessFile;
        FileLock lock;
        if (aVarArr != null && aVarArr.length > 0) {
            if (aVarArr[0] != null) {
                String b = b(aVarArr[0]);
                if (!TextUtils.isEmpty(b)) {
                    FileLock fileLock = null;
                    try {
                        File file = new File(b + ".lock");
                        y.a(file);
                        randomAccessFile = new RandomAccessFile(file, "rw");
                        try {
                            lock = randomAccessFile.getChannel().lock();
                        } catch (Exception e) {
                            e = e;
                            try {
                                b.a((Throwable) e);
                                try {
                                    fileLock.release();
                                } catch (IOException e2) {
                                    e = e2;
                                }
                                y.a((Closeable) randomAccessFile);
                            } catch (Throwable th) {
                                th = th;
                                if (fileLock != null && fileLock.isValid()) {
                                    try {
                                        fileLock.release();
                                    } catch (IOException e3) {
                                        b.a((Throwable) e3);
                                    }
                                }
                                y.a((Closeable) randomAccessFile);
                                throw th;
                            }
                        }
                        try {
                            for (com.xiaomi.clientreport.data.a aVar : aVarArr) {
                                if (aVar != null) {
                                    a(aVarArr, b);
                                }
                            }
                            if (lock != null && lock.isValid()) {
                                try {
                                    lock.release();
                                } catch (IOException e4) {
                                    e = e4;
                                }
                            }
                        } catch (Exception e5) {
                            e = e5;
                            fileLock = lock;
                            b.a((Throwable) e);
                            fileLock.release();
                            y.a((Closeable) randomAccessFile);
                        } catch (Throwable th2) {
                            th = th2;
                            fileLock = lock;
                            fileLock.release();
                            y.a((Closeable) randomAccessFile);
                            throw th;
                        }
                    } catch (Exception e6) {
                        e = e6;
                        randomAccessFile = null;
                        b.a((Throwable) e);
                        if (fileLock != null && fileLock.isValid()) {
                            fileLock.release();
                        }
                        y.a((Closeable) randomAccessFile);
                    } catch (Throwable th3) {
                        th = th3;
                        randomAccessFile = null;
                        fileLock.release();
                        y.a((Closeable) randomAccessFile);
                        throw th;
                    }
                    y.a((Closeable) randomAccessFile);
                }
                return;
            }
            return;
        }
        return;
        b.a((Throwable) e);
        y.a((Closeable) randomAccessFile);
    }

    public void b() {
        if (this.f12a != null) {
            if (this.f12a.size() > 0) {
                for (String str : this.f12a.keySet()) {
                    ArrayList arrayList = this.f12a.get(str);
                    if (arrayList != null && arrayList.size() > 0) {
                        com.xiaomi.clientreport.data.a[] aVarArr = new com.xiaomi.clientreport.data.a[arrayList.size()];
                        arrayList.toArray(aVarArr);
                        a(aVarArr);
                    }
                }
            }
            this.f12a.clear();
        }
    }

    public String bytesToString(byte[] bArr) {
        byte[] a2;
        if (bArr != null && bArr.length >= 1) {
            if (!com.xiaomi.clientreport.manager.a.a(this.a).a().isEventEncrypted()) {
                return ay.a(bArr);
            }
            String a3 = be.a(this.a);
            if (!TextUtils.isEmpty(a3) && (a2 = be.a(a3)) != null && a2.length > 0) {
                try {
                    return ay.a(Base64.decode(h.a(a2, bArr), 2));
                } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
                    b.a(e);
                }
            }
        }
        return null;
    }

    public void setEventMap(HashMap<String, ArrayList<com.xiaomi.clientreport.data.a>> hashMap) {
        this.f12a = hashMap;
    }

    public byte[] stringToBytes(String str) {
        byte[] a2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!com.xiaomi.clientreport.manager.a.a(this.a).a().isEventEncrypted()) {
            return ay.a(str);
        }
        String a3 = be.a(this.a);
        byte[] a4 = ay.a(str);
        if (!TextUtils.isEmpty(a3) && a4 != null && a4.length > 1 && (a2 = be.a(a3)) != null) {
            try {
                if (a2.length > 1) {
                    return h.b(a2, Base64.encode(a4, 2));
                }
            } catch (Exception e) {
                b.a((Throwable) e);
            }
        }
        return null;
    }
}
