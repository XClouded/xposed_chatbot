package com.huawei.hms.update.b;

import android.content.Context;
import com.huawei.hms.c.e;
import com.huawei.hms.support.log.a;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* compiled from: HttpRequestHelper */
public class b implements d {
    private HttpURLConnection a;
    private volatile int b = -1;

    public void a() {
        this.b = -1;
        if (this.a != null) {
            this.a.disconnect();
        }
    }

    public void b() {
        this.b = 1;
    }

    public int a(String str, OutputStream outputStream, int i, int i2, Context context) throws IOException, a {
        InputStream inputStream;
        InputStream inputStream2 = null;
        try {
            a(str, context);
            this.a.setRequestMethod("GET");
            if (i > 0) {
                this.a.addRequestProperty("Range", "bytes=" + i + "-" + i2);
            }
            int responseCode = this.a.getResponseCode();
            if ((i <= 0 || responseCode != 206) && (i > 0 || responseCode != 200)) {
                inputStream = null;
            } else {
                inputStream = this.a.getInputStream();
                try {
                    a((InputStream) new BufferedInputStream(inputStream, 4096), outputStream);
                    outputStream.flush();
                } catch (Throwable th) {
                    th = th;
                    inputStream2 = inputStream;
                }
            }
            e.a(inputStream);
            return responseCode;
        } catch (Throwable th2) {
            th = th2;
            e.a(inputStream2);
            throw th;
        }
    }

    private void a(String str, Context context) throws IOException {
        if (this.b == 0) {
            a.d("HttpRequestHelper", "Not allowed to repeat open http(s) connection.");
        }
        this.a = (HttpURLConnection) new URL(str).openConnection();
        if (this.a instanceof HttpsURLConnection) {
            c.a((HttpsURLConnection) this.a, context);
        }
        this.a.setConnectTimeout(30000);
        this.a.setReadTimeout(30000);
        this.a.setDoInput(true);
        this.a.setDoOutput(true);
        this.a.setUseCaches(false);
        this.b = 0;
    }

    private void a(InputStream inputStream, OutputStream outputStream) throws IOException, a {
        byte[] bArr = new byte[4096];
        do {
            int read = inputStream.read(bArr);
            if (-1 != read) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        } while (this.b != 1);
        throw new a("HTTP(s) request was canceled.");
    }
}
