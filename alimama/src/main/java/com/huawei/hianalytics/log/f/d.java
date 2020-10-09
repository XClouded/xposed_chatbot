package com.huawei.hianalytics.log.f;

import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.f.a.c;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

class d {
    static int a(List<c> list) {
        ByteArrayOutputStream byteArrayOutputStream;
        if (list == null || list.size() == 0) {
            b.d("LogHttpClient", "uploadParameterList is empty!");
            return -1;
        }
        for (c next : list) {
            String a = next.a();
            if (TextUtils.isEmpty(a)) {
                return -1;
            }
            URL url = new URL(a);
            String b = next.b();
            Map<String, String> d = next.d();
            DataInputStream dataInputStream = null;
            try {
                File a2 = a(next.c());
                if (a2 == null) {
                    b.d("LogHttpClient", "not have file in bigzip! No access to the path,upload over");
                    com.huawei.hianalytics.log.e.d.a(7, (Closeable) null);
                    com.huawei.hianalytics.log.e.d.a(8, (Closeable) null);
                    return -1;
                }
                DataInputStream dataInputStream2 = new DataInputStream(new FileInputStream(a2));
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream(1024);
                } catch (IOException unused) {
                    byteArrayOutputStream = null;
                    dataInputStream = dataInputStream2;
                    try {
                        b.d("LogHttpClient", "upLoadLogPutRequest() file input Stream Exception!");
                        com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                        com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                    } catch (Throwable th) {
                        th = th;
                        com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                        com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    byteArrayOutputStream = null;
                    dataInputStream = dataInputStream2;
                    com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                    com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                    throw th;
                }
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = dataInputStream2.read(bArr);
                        if (read != -1) {
                            byteArrayOutputStream.write(bArr, 0, read);
                        } else {
                            int a3 = com.huawei.hianalytics.h.c.a(url.toString(), byteArrayOutputStream.toByteArray(), b, d).a();
                            com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream2);
                            com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                            return a3;
                        }
                    }
                } catch (IOException unused2) {
                    dataInputStream = dataInputStream2;
                    b.d("LogHttpClient", "upLoadLogPutRequest() file input Stream Exception!");
                    com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                    com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                } catch (Throwable th3) {
                    th = th3;
                    dataInputStream = dataInputStream2;
                    com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                    com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                    throw th;
                }
            } catch (IOException unused3) {
                byteArrayOutputStream = null;
                b.d("LogHttpClient", "upLoadLogPutRequest() file input Stream Exception!");
                com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = null;
                com.huawei.hianalytics.log.e.d.a(7, (Closeable) dataInputStream);
                com.huawei.hianalytics.log.e.d.a(8, (Closeable) byteArrayOutputStream);
                throw th;
            }
        }
        return -1;
    }

    private static File a(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles == null) {
            return null;
        }
        return listFiles[0];
    }
}
