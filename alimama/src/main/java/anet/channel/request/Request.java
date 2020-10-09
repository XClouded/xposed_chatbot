package anet.channel.request;

import android.text.TextUtils;
import anet.channel.statist.RequestStatistic;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import com.taobao.weex.el.parse.Operators;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.text.Typography;

public class Request {
    public static final String DEFAULT_CHARSET = "UTF-8";
    private String bizId;
    private BodyEntry body;
    private String charset;
    private int connectTimeout;
    private HttpUrl formattedUrl;
    private Map<String, String> headers;
    private HostnameVerifier hostnameVerifier;
    private boolean isRedirectEnable;
    private String method;
    private HttpUrl originUrl;
    private Map<String, String> params;
    private int readTimeout;
    private int redirectTimes;
    public final RequestStatistic rs;
    private HttpUrl sendUrl;
    private String seq;
    private SSLSocketFactory sslSocketFactory;
    private URL url;

    public static final class Method {
        public static final String DELETE = "DELETE";
        public static final String GET = "GET";
        public static final String HEAD = "HEAD";
        public static final String OPTION = "OPTIONS";
        public static final String POST = "POST";
        public static final String PUT = "PUT";

        static boolean requiresRequestBody(String str) {
            return str.equals("POST") || str.equals(PUT);
        }

        static boolean permitsRequestBody(String str) {
            return requiresRequestBody(str) || str.equals(DELETE) || str.equals(OPTION);
        }
    }

    private Request(Builder builder) {
        this.method = "GET";
        this.isRedirectEnable = true;
        this.redirectTimes = 0;
        this.connectTimeout = 10000;
        this.readTimeout = 10000;
        this.method = builder.method;
        this.headers = builder.headers;
        this.params = builder.params;
        this.body = builder.body;
        this.charset = builder.charset;
        this.isRedirectEnable = builder.isRedirectEnable;
        this.redirectTimes = builder.redirectTimes;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.sslSocketFactory = builder.sslSocketFactory;
        this.bizId = builder.bizId;
        this.seq = builder.seq;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.originUrl = builder.originUrl;
        this.formattedUrl = builder.formattedUrl;
        if (this.formattedUrl == null) {
            formatUrl();
        }
        this.rs = builder.rs != null ? builder.rs : new RequestStatistic(getHost(), this.bizId);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        String unused = builder.method = this.method;
        Map unused2 = builder.headers = this.headers;
        Map unused3 = builder.params = this.params;
        BodyEntry unused4 = builder.body = this.body;
        String unused5 = builder.charset = this.charset;
        boolean unused6 = builder.isRedirectEnable = this.isRedirectEnable;
        int unused7 = builder.redirectTimes = this.redirectTimes;
        HostnameVerifier unused8 = builder.hostnameVerifier = this.hostnameVerifier;
        SSLSocketFactory unused9 = builder.sslSocketFactory = this.sslSocketFactory;
        HttpUrl unused10 = builder.originUrl = this.originUrl;
        HttpUrl unused11 = builder.formattedUrl = this.formattedUrl;
        String unused12 = builder.bizId = this.bizId;
        String unused13 = builder.seq = this.seq;
        int unused14 = builder.connectTimeout = this.connectTimeout;
        int unused15 = builder.readTimeout = this.readTimeout;
        RequestStatistic unused16 = builder.rs = this.rs;
        return builder;
    }

    public HttpUrl getHttpUrl() {
        return this.formattedUrl;
    }

    public String getUrlString() {
        return this.formattedUrl.urlString();
    }

    public URL getUrl() {
        if (this.url == null) {
            this.url = (this.sendUrl != null ? this.sendUrl : this.formattedUrl).toURL();
        }
        return this.url;
    }

    public void setDnsOptimize(String str, int i) {
        if (str != null) {
            if (this.sendUrl == null) {
                this.sendUrl = new HttpUrl(this.formattedUrl);
            }
            this.sendUrl.replaceIpAndPort(str, i);
        } else {
            this.sendUrl = null;
        }
        this.url = null;
        this.rs.setIPAndPort(str, i);
    }

    public void setUrlScheme(boolean z) {
        if (this.sendUrl == null) {
            this.sendUrl = new HttpUrl(this.formattedUrl);
        }
        this.sendUrl.setScheme(z ? "https" : "http");
        this.url = null;
    }

    public int getRedirectTimes() {
        return this.redirectTimes;
    }

    public String getHost() {
        return this.formattedUrl.host();
    }

    public String getMethod() {
        return this.method;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    public String getContentEncoding() {
        return this.charset != null ? this.charset : "UTF-8";
    }

    public boolean isRedirectEnable() {
        return this.isRedirectEnable;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public int postBody(OutputStream outputStream) throws IOException {
        if (this.body != null) {
            return this.body.writeTo(outputStream);
        }
        return 0;
    }

    public byte[] getBodyBytes() {
        if (this.body == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
        try {
            postBody(byteArrayOutputStream);
        } catch (IOException unused) {
        }
        return byteArrayOutputStream.toByteArray();
    }

    public boolean containsBody() {
        return this.body != null;
    }

    public String getBizId() {
        return this.bizId;
    }

    public String getSeq() {
        return this.seq;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    private void formatUrl() {
        String encodeQueryParams = Utils.encodeQueryParams(this.params, getContentEncoding());
        if (!TextUtils.isEmpty(encodeQueryParams)) {
            if (!Method.requiresRequestBody(this.method) || this.body != null) {
                String urlString = this.originUrl.urlString();
                StringBuilder sb = new StringBuilder(urlString);
                if (sb.indexOf("?") == -1) {
                    sb.append(Operators.CONDITION_IF);
                } else if (urlString.charAt(urlString.length() - 1) != '&') {
                    sb.append(Typography.amp);
                }
                sb.append(encodeQueryParams);
                HttpUrl parse = HttpUrl.parse(sb.toString());
                if (parse != null) {
                    this.formattedUrl = parse;
                }
            } else {
                try {
                    this.body = new ByteArrayEntry(encodeQueryParams.getBytes(getContentEncoding()));
                    Map<String, String> map = this.headers;
                    map.put("Content-Type", "application/x-www-form-urlencoded; charset=" + getContentEncoding());
                } catch (UnsupportedEncodingException unused) {
                }
            }
        }
        if (this.formattedUrl == null) {
            this.formattedUrl = this.originUrl;
        }
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public String bizId;
        /* access modifiers changed from: private */
        public BodyEntry body;
        /* access modifiers changed from: private */
        public String charset;
        /* access modifiers changed from: private */
        public int connectTimeout = 10000;
        /* access modifiers changed from: private */
        public HttpUrl formattedUrl;
        /* access modifiers changed from: private */
        public Map<String, String> headers = new HashMap();
        /* access modifiers changed from: private */
        public HostnameVerifier hostnameVerifier;
        /* access modifiers changed from: private */
        public boolean isRedirectEnable = true;
        /* access modifiers changed from: private */
        public String method = "GET";
        /* access modifiers changed from: private */
        public HttpUrl originUrl;
        /* access modifiers changed from: private */
        public Map<String, String> params;
        /* access modifiers changed from: private */
        public int readTimeout = 10000;
        /* access modifiers changed from: private */
        public int redirectTimes = 0;
        /* access modifiers changed from: private */
        public RequestStatistic rs = null;
        /* access modifiers changed from: private */
        public String seq;
        /* access modifiers changed from: private */
        public SSLSocketFactory sslSocketFactory;

        public Builder setUrl(HttpUrl httpUrl) {
            this.originUrl = httpUrl;
            this.formattedUrl = null;
            return this;
        }

        public Builder setUrl(String str) {
            this.originUrl = HttpUrl.parse(str);
            this.formattedUrl = null;
            if (this.originUrl != null) {
                return this;
            }
            throw new IllegalArgumentException("toURL is invalid! toURL = " + str);
        }

        public Builder setMethod(String str) {
            if (!TextUtils.isEmpty(str)) {
                if ("GET".equalsIgnoreCase(str)) {
                    this.method = "GET";
                } else if ("POST".equalsIgnoreCase(str)) {
                    this.method = "POST";
                } else if (Method.OPTION.equalsIgnoreCase(str)) {
                    this.method = Method.OPTION;
                } else if (Method.HEAD.equalsIgnoreCase(str)) {
                    this.method = Method.HEAD;
                } else if (Method.PUT.equalsIgnoreCase(str)) {
                    this.method = Method.PUT;
                } else if (Method.DELETE.equalsIgnoreCase(str)) {
                    this.method = Method.DELETE;
                } else {
                    this.method = "GET";
                }
                return this;
            }
            throw new IllegalArgumentException("method is null or empty");
        }

        public Builder setHeaders(Map<String, String> map) {
            this.headers.clear();
            if (map != null) {
                this.headers.putAll(map);
            }
            return this;
        }

        public Builder addHeader(String str, String str2) {
            this.headers.put(str, str2);
            return this;
        }

        public Builder setParams(Map<String, String> map) {
            this.params = map;
            this.formattedUrl = null;
            return this;
        }

        public Builder addParam(String str, String str2) {
            if (this.params == null) {
                this.params = new HashMap();
            }
            this.params.put(str, str2);
            this.formattedUrl = null;
            return this;
        }

        public Builder setCharset(String str) {
            this.charset = str;
            this.formattedUrl = null;
            return this;
        }

        public Builder setBody(BodyEntry bodyEntry) {
            this.body = bodyEntry;
            return this;
        }

        public Builder setRedirectEnable(boolean z) {
            this.isRedirectEnable = z;
            return this;
        }

        public Builder setRedirectTimes(int i) {
            this.redirectTimes = i;
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier2) {
            this.hostnameVerifier = hostnameVerifier2;
            return this;
        }

        public Builder setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.sslSocketFactory = sSLSocketFactory;
            return this;
        }

        public Builder setBizId(String str) {
            this.bizId = str;
            return this;
        }

        public Builder setSeq(String str) {
            this.seq = str;
            return this;
        }

        public Builder setReadTimeout(int i) {
            if (i > 0) {
                this.readTimeout = i;
            }
            return this;
        }

        public Builder setConnectTimeout(int i) {
            if (i > 0) {
                this.connectTimeout = i;
            }
            return this;
        }

        public Builder setRequestStatistic(RequestStatistic requestStatistic) {
            this.rs = requestStatistic;
            return this;
        }

        public Request build() {
            if (this.body == null && this.params == null && Method.requiresRequestBody(this.method)) {
                ALog.e("awcn.Request", "method " + this.method + " must have a request body", (String) null, new Object[0]);
            }
            if (this.body != null && !Method.permitsRequestBody(this.method)) {
                ALog.e("awcn.Request", "method " + this.method + " should not have a request body", (String) null, new Object[0]);
                this.body = null;
            }
            if (!(this.body == null || this.body.getContentType() == null)) {
                addHeader("Content-Type", this.body.getContentType());
            }
            return new Request(this);
        }
    }
}
