package com.taobao.orange.impl;

import android.text.TextUtils;
import com.taobao.orange.inner.INetConnection;
import com.taobao.orange.util.OrangeUtils;
import com.taobao.weex.el.parse.Operators;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HurlNetConnection implements INetConnection {
    private HttpURLConnection httpURLConnection;
    private Map<String, String> params;

    public void openConnection(String str) throws IOException {
        String encodeQueryParams = OrangeUtils.encodeQueryParams(this.params, "utf-8");
        StringBuilder sb = new StringBuilder(str);
        if (!TextUtils.isEmpty(encodeQueryParams)) {
            sb.append(Operators.CONDITION_IF);
            sb.append(encodeQueryParams);
        }
        this.httpURLConnection = (HttpURLConnection) new URL(sb.toString()).openConnection();
        this.httpURLConnection.setConnectTimeout(5000);
        this.httpURLConnection.setReadTimeout(5000);
        this.httpURLConnection.setUseCaches(false);
        this.httpURLConnection.setDoInput(true);
    }

    public void setMethod(String str) throws ProtocolException {
        if (!TextUtils.isEmpty(str)) {
            this.httpURLConnection.setRequestMethod(str);
            if ("POST".equalsIgnoreCase(str)) {
                this.httpURLConnection.setDoOutput(true);
            }
        }
    }

    public void setParams(Map<String, String> map) {
        this.params = map;
    }

    public void addHeader(String str, String str2) {
        this.httpURLConnection.addRequestProperty(str, str2);
    }

    public void setBody(byte[] bArr) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(this.httpURLConnection.getOutputStream());
        dataOutputStream.write(bArr);
        dataOutputStream.flush();
        OrangeUtils.close(dataOutputStream);
    }

    public Map<String, List<String>> getHeadFields() {
        if (this.httpURLConnection == null) {
            return null;
        }
        return this.httpURLConnection.getHeaderFields();
    }

    public int getResponseCode() throws IOException {
        if (this.httpURLConnection == null) {
            return 0;
        }
        return this.httpURLConnection.getResponseCode();
    }

    public String getResponse() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream;
        IOException e;
        if (this.httpURLConnection == null) {
            return null;
        }
        try {
            inputStream = this.httpURLConnection.getInputStream();
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (IOException e2) {
                IOException iOException = e2;
                byteArrayOutputStream = null;
                e = iOException;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                byteArrayOutputStream = null;
                th = th3;
                OrangeUtils.close(inputStream);
                OrangeUtils.close(byteArrayOutputStream);
                throw th;
            }
            try {
                byte[] bArr = new byte[2048];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        byteArrayOutputStream.write(bArr, 0, read);
                    } else {
                        String str = new String(byteArrayOutputStream.toByteArray(), "utf-8");
                        OrangeUtils.close(inputStream);
                        OrangeUtils.close(byteArrayOutputStream);
                        return str;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                throw e;
            }
        } catch (IOException e4) {
            byteArrayOutputStream = null;
            e = e4;
            inputStream = null;
            throw e;
        } catch (Throwable th4) {
            byteArrayOutputStream = null;
            th = th4;
            inputStream = null;
            OrangeUtils.close(inputStream);
            OrangeUtils.close(byteArrayOutputStream);
            throw th;
        }
    }

    public void connect() throws IOException {
        this.httpURLConnection.connect();
    }

    public void disconnect() {
        if (this.httpURLConnection != null) {
            this.httpURLConnection.disconnect();
        }
    }
}
