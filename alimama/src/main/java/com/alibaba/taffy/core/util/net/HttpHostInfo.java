package com.alibaba.taffy.core.util.net;

import anet.channel.util.HttpConstant;
import com.taobao.weex.el.parse.Operators;
import java.net.InetAddress;
import java.util.Locale;

public final class HttpHostInfo {
    public static final String DEFAULT_SCHEME_NAME = "http";
    protected final InetAddress address;
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;

    public HttpHostInfo(String str, int i, String str2) {
        this.hostname = str;
        this.lcHostname = str.toLowerCase(Locale.ENGLISH);
        if (str2 != null) {
            this.schemeName = str2.toLowerCase(Locale.ENGLISH);
        } else {
            this.schemeName = "http";
        }
        this.port = i;
        this.address = null;
    }

    public HttpHostInfo(String str, int i) {
        this(str, i, (String) null);
    }

    public HttpHostInfo(String str) {
        this(str, -1, (String) null);
    }

    public HttpHostInfo(InetAddress inetAddress, int i, String str) {
        this.address = inetAddress;
        this.hostname = inetAddress.getHostAddress();
        this.lcHostname = this.hostname.toLowerCase(Locale.ENGLISH);
        if (str != null) {
            this.schemeName = str.toLowerCase(Locale.ENGLISH);
        } else {
            this.schemeName = "http";
        }
        this.port = i;
    }

    public HttpHostInfo(InetAddress inetAddress, int i) {
        this(inetAddress, i, (String) null);
    }

    public HttpHostInfo(InetAddress inetAddress) {
        this(inetAddress, -1, (String) null);
    }

    public HttpHostInfo(HttpHostInfo httpHostInfo) {
        this.hostname = httpHostInfo.hostname;
        this.lcHostname = httpHostInfo.lcHostname;
        this.schemeName = httpHostInfo.schemeName;
        this.port = httpHostInfo.port;
        this.address = httpHostInfo.address;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public String toURI() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.schemeName);
        sb.append(HttpConstant.SCHEME_SPLIT);
        sb.append(this.hostname);
        if (this.port != -1) {
            sb.append(Operators.CONDITION_IF_MIDDLE);
            sb.append(Integer.toString(this.port));
        }
        return sb.toString();
    }

    public String toHostString() {
        if (this.port == -1) {
            return this.hostname;
        }
        StringBuilder sb = new StringBuilder(this.hostname.length() + 6);
        sb.append(this.hostname);
        sb.append(":");
        sb.append(Integer.toString(this.port));
        return sb.toString();
    }

    public String toString() {
        return toURI();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpHostInfo)) {
            return false;
        }
        HttpHostInfo httpHostInfo = (HttpHostInfo) obj;
        if (!this.lcHostname.equals(httpHostInfo.lcHostname) || this.port != httpHostInfo.port || !this.schemeName.equals(httpHostInfo.schemeName)) {
            return false;
        }
        return true;
    }
}
