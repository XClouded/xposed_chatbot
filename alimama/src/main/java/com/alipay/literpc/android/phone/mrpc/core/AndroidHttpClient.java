package com.alipay.literpc.android.phone.mrpc.core;

import android.content.ContentResolver;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import anet.channel.util.HttpConstant;
import com.taobao.accs.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Security;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;

public final class AndroidHttpClient implements HttpClient {
    private static final int CONNECTION_POOL_TIMEOUT = 60000;
    private static final int CONNECTION_TIMEOUT = 20000;
    public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 160;
    private static final int MAX_CONNECTIONS = 50;
    private static final int SOCKET_OPERATION_TIMEOUT = 30000;
    private static final String TAG = "AndroidHttpClient";
    /* access modifiers changed from: private */
    public static final HttpRequestInterceptor sThreadCheckInterceptor = new HttpRequestInterceptor() {
        public void process(HttpRequest httpRequest, HttpContext httpContext) {
            if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
                throw new RuntimeException("This thread forbids HTTP requests");
            }
        }
    };
    private static String[] textContentTypes = {"text/", "application/xml", "application/json"};
    /* access modifiers changed from: private */
    public volatile LoggingConfiguration curlConfiguration;
    private final HttpClient delegate;
    private RuntimeException mLeakedException = new IllegalStateException("AndroidHttpClient created and never closed");

    public static AndroidHttpClient newInstance(String str, Context context) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, true);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpClientParams.setRedirecting(basicHttpParams, true);
        HttpClientParams.setAuthenticating(basicHttpParams, false);
        HttpProtocolParams.setUserAgent(basicHttpParams, str);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLCertificateSocketFactory.getHttpSocketFactory(30000, context == null ? null : new SSLSessionCache(context)), Constants.PORT));
        ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
        ConnManagerParams.setTimeout(basicHttpParams, 60000);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 50);
        Security.setProperty("networkaddress.cache.ttl", String.valueOf(-1));
        setDefaultHostnameVerifier();
        return new AndroidHttpClient(threadSafeClientConnManager, basicHttpParams);
    }

    public static AndroidHttpClient newInstance(String str) {
        return newInstance(str, (Context) null);
    }

    private AndroidHttpClient(ClientConnectionManager clientConnectionManager, HttpParams httpParams) {
        this.delegate = new DefaultHttpClient(clientConnectionManager, httpParams) {
            /* access modifiers changed from: protected */
            public BasicHttpProcessor createHttpProcessor() {
                BasicHttpProcessor createHttpProcessor = AndroidHttpClient.super.createHttpProcessor();
                createHttpProcessor.addRequestInterceptor(AndroidHttpClient.sThreadCheckInterceptor);
                createHttpProcessor.addRequestInterceptor(new CurlLogger());
                return createHttpProcessor;
            }

            /* access modifiers changed from: protected */
            public HttpContext createHttpContext() {
                BasicHttpContext basicHttpContext = new BasicHttpContext();
                basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
                basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
                basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
                return basicHttpContext;
            }

            /* access modifiers changed from: protected */
            public RedirectHandler createRedirectHandler() {
                return new DefaultRedirectHandler() {
                    int mRedirects;

                    public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
                        int statusCode;
                        this.mRedirects++;
                        boolean isRedirectRequested = AnonymousClass2.super.isRedirectRequested(httpResponse, httpContext);
                        if (isRedirectRequested || this.mRedirects >= 5 || ((statusCode = httpResponse.getStatusLine().getStatusCode()) != 301 && statusCode != 302)) {
                            return isRedirectRequested;
                        }
                        return true;
                    }
                };
            }

            /* access modifiers changed from: protected */
            public ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
                return new ConnectionKeepAliveStrategy() {
                    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                        return 180000;
                    }
                };
            }
        };
    }

    public static void modifyRequestToAcceptGzipResponse(HttpRequest httpRequest) {
        httpRequest.addHeader(HttpConstant.ACCEPT_ENCODING, "gzip");
    }

    public void setHttpRequestRetryHandler(HttpRequestRetryHandler httpRequestRetryHandler) {
        this.delegate.setHttpRequestRetryHandler(httpRequestRetryHandler);
    }

    public static void modifyRequestToKeepAlive(HttpRequest httpRequest) {
        httpRequest.addHeader("Connection", "Keep-Alive");
    }

    public static InputStream getUngzippedContent(HttpEntity httpEntity) throws IOException {
        Header contentEncoding;
        String value;
        InputStream content = httpEntity.getContent();
        if (content == null || (contentEncoding = httpEntity.getContentEncoding()) == null || (value = contentEncoding.getValue()) == null) {
            return content;
        }
        return value.contains("gzip") ? new GZIPInputStream(content) : content;
    }

    public void close() {
        if (this.mLeakedException != null) {
            getConnectionManager().shutdown();
            this.mLeakedException = null;
        }
    }

    public HttpParams getParams() {
        return this.delegate.getParams();
    }

    public ClientConnectionManager getConnectionManager() {
        return this.delegate.getConnectionManager();
    }

    public HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException {
        return this.delegate.execute(httpUriRequest);
    }

    public HttpResponse execute(HttpUriRequest httpUriRequest, HttpContext httpContext) throws IOException {
        return this.delegate.execute(httpUriRequest, httpContext);
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        return this.delegate.execute(httpHost, httpRequest);
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException {
        return this.delegate.execute(httpHost, httpRequest, httpContext);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.delegate.execute(httpUriRequest, responseHandler);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        return this.delegate.execute(httpUriRequest, responseHandler, httpContext);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.delegate.execute(httpHost, httpRequest, responseHandler);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        return this.delegate.execute(httpHost, httpRequest, responseHandler, httpContext);
    }

    public static AbstractHttpEntity getCompressedEntity(byte[] bArr, ContentResolver contentResolver) throws IOException {
        Log.i("RPC_PERF", "gzip...");
        if (((long) bArr.length) < getMinGzipSize(contentResolver)) {
            return new ByteArrayEntity(bArr);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(bArr);
        gZIPOutputStream.close();
        AbstractHttpEntity byteArrayEntity = new ByteArrayEntity(byteArrayOutputStream.toByteArray());
        byteArrayEntity.setContentEncoding("gzip");
        Log.i("RPC_PERF", "gzip size:" + bArr.length + "->" + byteArrayEntity.getContentLength());
        return byteArrayEntity;
    }

    public static long getMinGzipSize(ContentResolver contentResolver) {
        return DEFAULT_SYNC_MIN_GZIP_BYTES;
    }

    private static class LoggingConfiguration {
        private final int level;
        private final String tag;

        private LoggingConfiguration(String str, int i) {
            this.tag = str;
            this.level = i;
        }

        /* access modifiers changed from: private */
        public boolean isLoggable() {
            return Log.isLoggable(this.tag, this.level);
        }

        /* access modifiers changed from: private */
        public void println(String str) {
            Log.println(this.level, this.tag, str);
        }
    }

    public void enableCurlLogging(String str, int i) {
        if (str == null) {
            throw new NullPointerException("name");
        } else if (i < 2 || i > 7) {
            throw new IllegalArgumentException("Level is out of range [2..7]");
        } else {
            this.curlConfiguration = new LoggingConfiguration(str, i);
        }
    }

    public void disableCurlLogging() {
        this.curlConfiguration = null;
    }

    private class CurlLogger implements HttpRequestInterceptor {
        private CurlLogger() {
        }

        public void process(HttpRequest httpRequest, HttpContext httpContext) throws IOException {
            LoggingConfiguration access$300 = AndroidHttpClient.this.curlConfiguration;
            if (access$300 != null && access$300.isLoggable() && (httpRequest instanceof HttpUriRequest)) {
                access$300.println(AndroidHttpClient.toCurl((HttpUriRequest) httpRequest, false));
            }
        }
    }

    /* access modifiers changed from: private */
    public static String toCurl(HttpUriRequest httpUriRequest, boolean z) throws IOException {
        HttpEntity entity;
        StringBuilder sb = new StringBuilder();
        sb.append("curl ");
        for (Header header : httpUriRequest.getAllHeaders()) {
            if (z || (!header.getName().equals(HttpConstant.AUTHORIZATION) && !header.getName().equals(HttpConstant.COOKIE))) {
                sb.append("--header \"");
                sb.append(header.toString().trim());
                sb.append("\" ");
            }
        }
        URI uri = httpUriRequest.getURI();
        if (httpUriRequest instanceof RequestWrapper) {
            HttpUriRequest original = ((RequestWrapper) httpUriRequest).getOriginal();
            if (original instanceof HttpUriRequest) {
                uri = original.getURI();
            }
        }
        sb.append("\"");
        sb.append(uri);
        sb.append("\"");
        if ((httpUriRequest instanceof HttpEntityEnclosingRequest) && (entity = ((HttpEntityEnclosingRequest) httpUriRequest).getEntity()) != null && entity.isRepeatable()) {
            if (entity.getContentLength() < 1024) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                entity.writeTo(byteArrayOutputStream);
                if (isBinaryContent(httpUriRequest)) {
                    sb.insert(0, "echo '" + Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2) + "' | base64 -d > /tmp/$$.bin; ");
                    sb.append(" --data-binary @/tmp/$$.bin");
                } else {
                    String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                    sb.append(" --data-ascii \"");
                    sb.append(byteArrayOutputStream2);
                    sb.append("\"");
                }
            } else {
                sb.append(" [TOO MUCH DATA TO INCLUDE]");
            }
        }
        return sb.toString();
    }

    private static boolean isBinaryContent(HttpUriRequest httpUriRequest) {
        Header[] headers = httpUriRequest.getHeaders("content-encoding");
        if (headers != null) {
            for (Header value : headers) {
                if ("gzip".equalsIgnoreCase(value.getValue())) {
                    return true;
                }
            }
        }
        Header[] headers2 = httpUriRequest.getHeaders("content-type");
        if (headers2 != null) {
            for (Header header : headers2) {
                for (String startsWith : textContentTypes) {
                    if (header.getValue().startsWith(startsWith)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static long parseDate(String str) {
        return HttpDateTime.parse(str);
    }

    private static void setDefaultHostnameVerifier() {
        HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
    }
}
