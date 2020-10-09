package com.taobao.weex.devtools.inspector.network;

import com.taobao.weex.devtools.inspector.network.utils.RequestConverter;
import com.taobao.weex.devtools.inspector.network.utils.ResponseConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class GeneralEventReporter {
    private static GeneralEventReporter sInstance;
    private NetworkEventReporter mReporter = NetworkEventReporterManager.get();

    public static synchronized GeneralEventReporter getInstance() {
        GeneralEventReporter generalEventReporter;
        synchronized (GeneralEventReporter.class) {
            if (sInstance == null) {
                sInstance = new GeneralEventReporter();
            }
            generalEventReporter = sInstance;
        }
        return generalEventReporter;
    }

    private GeneralEventReporter() {
    }

    public void requestWillBeSent(Map<String, Object> map) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.requestWillBeSent(RequestConverter.convertFrom(map));
        }
    }

    public void responseHeadersReceived(Map<String, Object> map) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.responseHeadersReceived(ResponseConverter.convertFrom(map));
        }
    }

    public void httpExchangeFailed(String str, String str2) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.httpExchangeFailed(str, str2);
        }
    }

    public InputStream interpretResponseStream(String str, String str2, String str3, InputStream inputStream, boolean z) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            InputStream interpretResponseStream = this.mReporter.interpretResponseStream(str, str2, str3, inputStream, new DefaultResponseHandler(this.mReporter, str));
            if (!z) {
                try {
                    read(interpretResponseStream);
                    if (interpretResponseStream != null) {
                        interpretResponseStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (interpretResponseStream != null) {
                return interpretResponseStream;
            }
        }
        return inputStream;
    }

    public void responseReadFailed(String str, String str2) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.responseReadFailed(str, str2);
        }
    }

    public void responseReadFinished(String str) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.responseReadFinished(str);
        }
    }

    public void dataSent(String str, int i, int i2) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.dataSent(str, i, i2);
        }
    }

    public void dataReceived(String str, int i, int i2) {
        if (this.mReporter != null && this.mReporter.isEnabled()) {
            this.mReporter.dataReceived(str, i, i2);
        }
    }

    private byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                return byteArray;
            }
        }
    }
}
