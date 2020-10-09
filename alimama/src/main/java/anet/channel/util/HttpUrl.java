package anet.channel.util;

import anet.channel.strategy.utils.Utils;
import com.taobao.weex.el.parse.Operators;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrl {
    private String host;
    private volatile boolean isSchemeLocked = false;
    private String path;
    private int port;
    private String scheme;
    private String simpleUrl;
    private String url;

    private HttpUrl() {
    }

    public HttpUrl(HttpUrl httpUrl) {
        this.scheme = httpUrl.scheme;
        this.host = httpUrl.host;
        this.path = httpUrl.path;
        this.url = httpUrl.url;
        this.simpleUrl = httpUrl.simpleUrl;
        this.isSchemeLocked = httpUrl.isSchemeLocked;
    }

    /* JADX WARNING: Removed duplicated region for block: B:65:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x012c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static anet.channel.util.HttpUrl parse(java.lang.String r13) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r13)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.String r13 = r13.trim()
            anet.channel.util.HttpUrl r0 = new anet.channel.util.HttpUrl
            r0.<init>()
            r0.url = r13
            java.lang.String r2 = "//"
            boolean r2 = r13.startsWith(r2)
            r8 = 0
            if (r2 == 0) goto L_0x0020
            r0.scheme = r1
            r2 = 0
            goto L_0x0045
        L_0x0020:
            r3 = 1
            java.lang.String r5 = "https:"
            r6 = 0
            r7 = 6
            r4 = 0
            r2 = r13
            boolean r2 = r2.regionMatches(r3, r4, r5, r6, r7)
            if (r2 == 0) goto L_0x0033
            java.lang.String r2 = "https"
            r0.scheme = r2
            r2 = 6
            goto L_0x0045
        L_0x0033:
            r3 = 1
            java.lang.String r5 = "http:"
            r6 = 0
            r7 = 5
            r4 = 0
            r2 = r13
            boolean r2 = r2.regionMatches(r3, r4, r5, r6, r7)
            if (r2 == 0) goto L_0x0147
            java.lang.String r2 = "http"
            r0.scheme = r2
            r2 = 5
        L_0x0045:
            int r3 = r13.length()
            int r2 = r2 + 2
            r4 = r2
            r5 = 0
        L_0x004d:
            r6 = 58
            r7 = 35
            r9 = 63
            r10 = 47
            if (r4 >= r3) goto L_0x007b
            char r11 = r13.charAt(r4)
            r12 = 91
            if (r11 != r12) goto L_0x0061
            r5 = 1
            goto L_0x0072
        L_0x0061:
            r12 = 93
            if (r11 != r12) goto L_0x0067
            r5 = 0
            goto L_0x0072
        L_0x0067:
            if (r11 == r10) goto L_0x0075
            if (r11 == r9) goto L_0x0075
            if (r11 == r7) goto L_0x0075
            if (r11 != r6) goto L_0x0072
            if (r5 != 0) goto L_0x0072
            goto L_0x0075
        L_0x0072:
            int r4 = r4 + 1
            goto L_0x004d
        L_0x0075:
            java.lang.String r5 = r13.substring(r2, r4)
            r0.host = r5
        L_0x007b:
            if (r4 != r3) goto L_0x0083
            java.lang.String r2 = r13.substring(r2)
            r0.host = r2
        L_0x0083:
            r2 = 0
        L_0x0084:
            if (r4 >= r3) goto L_0x009d
            char r5 = r13.charAt(r4)
            if (r5 != r6) goto L_0x0091
            if (r2 != 0) goto L_0x0091
            int r2 = r4 + 1
            goto L_0x0098
        L_0x0091:
            if (r5 == r10) goto L_0x009b
            if (r5 == r7) goto L_0x009b
            if (r5 != r9) goto L_0x0098
            goto L_0x009b
        L_0x0098:
            int r4 = r4 + 1
            goto L_0x0084
        L_0x009b:
            r5 = r4
            goto L_0x009e
        L_0x009d:
            r5 = r3
        L_0x009e:
            if (r2 == 0) goto L_0x00b7
            java.lang.String r2 = r13.substring(r2, r5)
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ NumberFormatException -> 0x00b6 }
            r0.port = r2     // Catch:{ NumberFormatException -> 0x00b6 }
            int r2 = r0.port     // Catch:{ NumberFormatException -> 0x00b6 }
            if (r2 <= 0) goto L_0x00b5
            int r2 = r0.port     // Catch:{ NumberFormatException -> 0x00b6 }
            r5 = 65535(0xffff, float:9.1834E-41)
            if (r2 <= r5) goto L_0x00b7
        L_0x00b5:
            return r1
        L_0x00b6:
            return r1
        L_0x00b7:
            if (r4 >= r3) goto L_0x00cf
            char r2 = r13.charAt(r4)
            if (r2 != r10) goto L_0x00c3
            if (r8 != 0) goto L_0x00c3
            r8 = r4
            goto L_0x00c8
        L_0x00c3:
            if (r2 == r9) goto L_0x00cb
            if (r2 != r7) goto L_0x00c8
            goto L_0x00cb
        L_0x00c8:
            int r4 = r4 + 1
            goto L_0x00b7
        L_0x00cb:
            if (r8 == 0) goto L_0x00cf
            r2 = r4
            goto L_0x00d0
        L_0x00cf:
            r2 = r3
        L_0x00d0:
            if (r8 == 0) goto L_0x00d9
            java.lang.String r2 = r13.substring(r8, r2)
            r0.path = r2
            goto L_0x00db
        L_0x00d9:
            r0.path = r1
        L_0x00db:
            java.lang.String r2 = r0.scheme
            if (r2 != 0) goto L_0x0101
            int r2 = r0.port
            r5 = 80
            if (r2 != r5) goto L_0x00ea
            java.lang.String r1 = "http"
            r0.scheme = r1
            goto L_0x0101
        L_0x00ea:
            int r2 = r0.port
            r5 = 443(0x1bb, float:6.21E-43)
            if (r2 != r5) goto L_0x00f5
            java.lang.String r1 = "https"
            r0.scheme = r1
            goto L_0x0101
        L_0x00f5:
            anet.channel.strategy.IStrategyInstance r2 = anet.channel.strategy.StrategyCenter.getInstance()
            java.lang.String r5 = r0.host
            java.lang.String r1 = r2.getSchemeByHost(r5, r1)
            r0.scheme = r1
        L_0x0101:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = r0.scheme
            r1.<init>(r2)
            java.lang.String r2 = "://"
            r1.append(r2)
            java.lang.String r2 = r0.host
            r1.append(r2)
            boolean r2 = r0.containsNonDefaultPort()
            if (r2 == 0) goto L_0x0122
            java.lang.String r2 = ":"
            r1.append(r2)
            int r2 = r0.port
            r1.append(r2)
        L_0x0122:
            java.lang.String r2 = r0.path
            if (r2 == 0) goto L_0x012c
            java.lang.String r2 = r0.path
            r1.append(r2)
            goto L_0x0133
        L_0x012c:
            if (r4 == r3) goto L_0x0133
            java.lang.String r2 = "/"
            r1.append(r2)
        L_0x0133:
            java.lang.String r2 = r1.toString()
            r0.simpleUrl = r2
            java.lang.String r13 = r13.substring(r4)
            r1.append(r13)
            java.lang.String r13 = r1.toString()
            r0.url = r13
            return r0
        L_0x0147:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.HttpUrl.parse(java.lang.String):anet.channel.util.HttpUrl");
    }

    public String scheme() {
        return this.scheme;
    }

    public String host() {
        return this.host;
    }

    public String path() {
        return this.path;
    }

    public int getPort() {
        return this.port;
    }

    public String urlString() {
        return this.url;
    }

    public String simpleUrlString() {
        return this.simpleUrl;
    }

    public URL toURL() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException unused) {
            return null;
        }
    }

    public boolean containsNonDefaultPort() {
        return this.port != 0 && (("http".equals(this.scheme) && this.port != 80) || ("https".equals(this.scheme) && this.port != 443));
    }

    public void downgradeSchemeAndLock() {
        this.isSchemeLocked = true;
        if (!"http".equals(this.scheme)) {
            this.scheme = "http";
            this.url = StringUtils.concatString(this.scheme, ":", this.url.substring(this.url.indexOf("//")));
        }
    }

    public boolean isSchemeLocked() {
        return this.isSchemeLocked;
    }

    public void lockScheme() {
        this.isSchemeLocked = true;
    }

    public void setScheme(String str) {
        if (!this.isSchemeLocked && !str.equalsIgnoreCase(this.scheme)) {
            this.scheme = str;
            this.url = StringUtils.concatString(str, ":", this.url.substring(this.url.indexOf("//")));
            this.simpleUrl = StringUtils.concatString(str, ":", this.simpleUrl.substring(this.url.indexOf("//")));
        }
    }

    public void replaceIpAndPort(String str, int i) {
        if (str != null) {
            int indexOf = this.url.indexOf("//") + 2;
            while (indexOf < this.url.length() && this.url.charAt(indexOf) != '/') {
                indexOf++;
            }
            boolean isIPV6Address = Utils.isIPV6Address(str);
            StringBuilder sb = new StringBuilder(this.url.length() + str.length());
            sb.append(this.scheme);
            sb.append(HttpConstant.SCHEME_SPLIT);
            if (isIPV6Address) {
                sb.append(Operators.ARRAY_START);
            }
            sb.append(str);
            if (isIPV6Address) {
                sb.append(Operators.ARRAY_END);
            }
            if (i != 0) {
                sb.append(Operators.CONDITION_IF_MIDDLE);
                sb.append(i);
            } else if (this.port != 0) {
                sb.append(Operators.CONDITION_IF_MIDDLE);
                sb.append(this.port);
            }
            sb.append(this.url.substring(indexOf));
            this.url = sb.toString();
        }
    }

    public String toString() {
        return this.url;
    }
}
