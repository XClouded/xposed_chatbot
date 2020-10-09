package com.taobao.android.dinamicx.template.download;

import android.text.TextUtils;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader implements IDXDownloader {
    public byte[] download(String str) {
        if (TextUtils.isEmpty(str)) {
            DXRemoteLog.remoteLogi(str + "下载链接为空");
            return null;
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[2048];
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read == -1) {
                    httpURLConnection.disconnect();
                    inputStream.close();
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.flush();
                    return byteArray;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (IOException e) {
            if (DinamicXEngine.isDebug()) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
