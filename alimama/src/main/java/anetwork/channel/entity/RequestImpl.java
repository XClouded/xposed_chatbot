package anetwork.channel.entity;

import android.text.TextUtils;
import anet.channel.request.BodyEntry;
import anet.channel.util.ALog;
import anetwork.channel.Header;
import anetwork.channel.IBodyHandler;
import anetwork.channel.Param;
import anetwork.channel.Request;
import anetwork.channel.util.RequestConstant;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestImpl implements Request {
    private static final String TAG = "anet.RequestImpl";
    private String bizId;
    private BodyEntry bodyEntry = null;
    private String charset = "utf-8";
    private int connectTimeout;
    private Map<String, String> extProperties;
    private List<Header> headers;
    private boolean isRedirect = true;
    private String method = "GET";
    private List<Param> params;
    private int readTimeout;
    private int retryTime = 2;
    private String seqNo;
    @Deprecated
    private URI uri;
    @Deprecated
    private URL url;
    private String urlString;

    @Deprecated
    public IBodyHandler getBodyHandler() {
        return null;
    }

    public RequestImpl() {
    }

    @Deprecated
    public RequestImpl(URI uri2) {
        this.uri = uri2;
        this.urlString = uri2.toString();
    }

    @Deprecated
    public RequestImpl(URL url2) {
        this.url = url2;
        this.urlString = url2.toString();
    }

    public RequestImpl(String str) {
        this.urlString = str;
    }

    @Deprecated
    public URI getURI() {
        if (this.uri != null) {
            return this.uri;
        }
        if (this.urlString != null) {
            try {
                this.uri = new URI(this.urlString);
            } catch (Exception e) {
                ALog.e(TAG, "uri error", this.seqNo, e, new Object[0]);
            }
        }
        return this.uri;
    }

    @Deprecated
    public void setUri(URI uri2) {
        this.uri = uri2;
    }

    @Deprecated
    public URL getURL() {
        if (this.url != null) {
            return this.url;
        }
        if (this.urlString != null) {
            try {
                this.url = new URL(this.urlString);
            } catch (Exception e) {
                ALog.e(TAG, "url error", this.seqNo, e, new Object[0]);
            }
        }
        return this.url;
    }

    @Deprecated
    public void setUrL(URL url2) {
        this.url = url2;
        this.urlString = url2.toString();
    }

    public String getUrlString() {
        return this.urlString;
    }

    public boolean getFollowRedirects() {
        return this.isRedirect;
    }

    public void setFollowRedirects(boolean z) {
        this.isRedirect = z;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public void setHeaders(List<Header> list) {
        this.headers = list;
    }

    public void addHeader(String str, String str2) {
        if (str != null && str2 != null) {
            if (this.headers == null) {
                this.headers = new ArrayList();
            }
            this.headers.add(new BasicHeader(str, str2));
        }
    }

    public void removeHeader(Header header) {
        if (this.headers != null) {
            this.headers.remove(header);
        }
    }

    public void setHeader(Header header) {
        if (header != null) {
            if (this.headers == null) {
                this.headers = new ArrayList();
            }
            int i = 0;
            int size = this.headers.size();
            while (true) {
                if (i >= size) {
                    break;
                }
                if (header.getName().equalsIgnoreCase(this.headers.get(i).getName())) {
                    this.headers.set(i, header);
                    break;
                }
                i++;
            }
            if (i < this.headers.size()) {
                this.headers.add(header);
            }
        }
    }

    public Header[] getHeaders(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (this.headers == null) {
            return null;
        }
        for (int i = 0; i < this.headers.size(); i++) {
            if (!(this.headers.get(i) == null || this.headers.get(i).getName() == null || !this.headers.get(i).getName().equalsIgnoreCase(str))) {
                arrayList.add(this.headers.get(i));
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        Header[] headerArr = new Header[arrayList.size()];
        arrayList.toArray(headerArr);
        return headerArr;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void setRetryTime(int i) {
        this.retryTime = i;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String str) {
        this.charset = str;
    }

    public List<Param> getParams() {
        return this.params;
    }

    public void setParams(List<Param> list) {
        this.params = list;
    }

    public BodyEntry getBodyEntry() {
        return this.bodyEntry;
    }

    public void setBodyEntry(BodyEntry bodyEntry2) {
        this.bodyEntry = bodyEntry2;
    }

    public void setBodyHandler(IBodyHandler iBodyHandler) {
        this.bodyEntry = new BodyHandlerEntry(iBodyHandler);
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setConnectTimeout(int i) {
        this.connectTimeout = i;
    }

    public void setReadTimeout(int i) {
        this.readTimeout = i;
    }

    @Deprecated
    public void setBizId(int i) {
        this.bizId = String.valueOf(i);
    }

    public void setBizId(String str) {
        this.bizId = str;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setSeqNo(String str) {
        this.seqNo = str;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    @Deprecated
    public boolean isCookieEnabled() {
        return !"false".equals(getExtProperty(RequestConstant.ENABLE_COOKIE));
    }

    @Deprecated
    public void setCookieEnabled(boolean z) {
        setExtProperty(RequestConstant.ENABLE_COOKIE, z ? "true" : "false");
    }

    public void setExtProperty(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            if (this.extProperties == null) {
                this.extProperties = new HashMap();
            }
            this.extProperties.put(str, str2);
        }
    }

    public String getExtProperty(String str) {
        if (this.extProperties == null) {
            return null;
        }
        return this.extProperties.get(str);
    }

    public Map<String, String> getExtProperties() {
        return this.extProperties;
    }
}
