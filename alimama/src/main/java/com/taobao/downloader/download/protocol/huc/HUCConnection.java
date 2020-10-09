package com.taobao.downloader.download.protocol.huc;

import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.taobao.downloader.Configuration;
import com.taobao.downloader.download.protocol.DLConfig;
import com.taobao.downloader.download.protocol.DLConnection;
import com.taobao.downloader.download.protocol.DLInputStream;
import com.taobao.downloader.util.LogUtil;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HUCConnection implements DLConnection {
    protected HttpURLConnection httpURLConnection;

    public void openConnection(URL url, DLConfig dLConfig) throws IOException {
        this.httpURLConnection = (HttpURLConnection) ((!url.getProtocol().equals("http") || dLConfig.isLastConnect()) ? url : new URL(replaceUrlByIp(url.toString(), dLConfig.getConnectFailTime()))).openConnection();
        this.httpURLConnection.addRequestProperty(HttpConstant.HOST, url.getHost());
        this.httpURLConnection.setConnectTimeout(dLConfig.getConnectTimeout());
        this.httpURLConnection.setReadTimeout(dLConfig.getReadTimeout());
        this.httpURLConnection.setInstanceFollowRedirects(DLConfig.REDIRECTABLE);
    }

    public void connect() throws IOException {
        this.httpURLConnection.connect();
    }

    public int getStatusCode() throws Exception {
        return this.httpURLConnection.getResponseCode();
    }

    public long getDownloadLength() {
        String headerField = this.httpURLConnection.getHeaderField("Content-Length");
        if (TextUtils.isEmpty(headerField) || !TextUtils.isDigitsOnly(headerField)) {
            return 0;
        }
        return Long.valueOf(headerField).longValue();
    }

    public String getHeaderField(String str) {
        return this.httpURLConnection.getHeaderField(str);
    }

    public DLInputStream getInputStream() throws IOException {
        return new HUCInputStream(this.httpURLConnection.getInputStream());
    }

    public void addRequestProperty(String str, String str2) {
        this.httpURLConnection.addRequestProperty(str, str2);
    }

    public void disConnect() {
        this.httpURLConnection.disconnect();
    }

    public String getErrorMsg() {
        if (this.httpURLConnection == null) {
            return "HttpResponse is empty!";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(this.httpURLConnection.getResponseCode());
            sb.append("\n");
            sb.append(this.httpURLConnection.getResponseMessage());
            sb.append("\n");
            Map headerFields = this.httpURLConnection.getHeaderFields();
            for (Object next : headerFields.keySet()) {
                sb.append(next);
                sb.append(":");
                sb.append(headerFields.get(next));
                sb.append("\n");
            }
            return sb.toString();
        } catch (Throwable th) {
            LogUtil.error("huc", "get error msg", th);
            return "";
        }
    }

    private String replaceUrlByIp(String str, int i) {
        String str2;
        if (Configuration.dnsService == null) {
            return str;
        }
        try {
            URL url = new URL(str);
            String host = url.getHost();
            int port = url.getPort();
            if (port == -1) {
                str2 = host;
            } else {
                str2 = host + ":" + port;
            }
            List<String> ipPorts = Configuration.dnsService.getIpPorts(host);
            if (ipPorts.isEmpty()) {
                return str;
            }
            String str3 = ipPorts.get(i % ipPorts.size());
            if (!TextUtils.isEmpty(str3)) {
                return str.replaceFirst(str2, str3);
            }
            return str;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
