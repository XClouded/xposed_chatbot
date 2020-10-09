package com.xiaomi.push;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import anet.channel.util.HttpConstant;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class as {
    public static final Pattern a = Pattern.compile("([^\\s;]+)(.*)");
    public static final Pattern b = Pattern.compile("(.*?charset\\s*=[^a-zA-Z0-9]*)([-a-zA-Z0-9]+)(.*)", 2);
    public static final Pattern c = Pattern.compile("(\\<\\?xml\\s+.*?encoding\\s*=[^a-zA-Z0-9]*)([-a-zA-Z0-9]+)(.*)", 2);

    public static final class a extends FilterInputStream {
        private boolean a;

        public a(InputStream inputStream) {
            super(inputStream);
        }

        public int read(byte[] bArr, int i, int i2) {
            int read;
            if (!this.a && (read = super.read(bArr, i, i2)) != -1) {
                return read;
            }
            this.a = true;
            return -1;
        }
    }

    public static class b {
        public int a;

        /* renamed from: a  reason: collision with other field name */
        public Map<String, String> f124a;

        public String toString() {
            return String.format("resCode = %1$d, headers = %2$s", new Object[]{Integer.valueOf(this.a), this.f124a.toString()});
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int a(android.content.Context r2) {
        /*
            r0 = -1
            java.lang.String r1 = "connectivity"
            java.lang.Object r2 = r2.getSystemService(r1)     // Catch:{ Exception -> 0x0018 }
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2     // Catch:{ Exception -> 0x0018 }
            if (r2 != 0) goto L_0x000c
            return r0
        L_0x000c:
            android.net.NetworkInfo r2 = r2.getActiveNetworkInfo()     // Catch:{  }
            if (r2 != 0) goto L_0x0013
            return r0
        L_0x0013:
            int r2 = r2.getType()
            return r2
        L_0x0018:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.as.a(android.content.Context):int");
    }

    /* renamed from: a  reason: collision with other method in class */
    public static NetworkInfo m88a(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return null;
            }
            return connectivityManager.getActiveNetworkInfo();
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v17, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v22, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v25, resolved type: java.io.BufferedReader} */
    /* JADX WARNING: type inference failed for: r6v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r6v18 */
    /* JADX WARNING: type inference failed for: r6v24 */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00f6, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00f7, code lost:
        r6 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00f9, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00fa, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0105, code lost:
        r4 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x012e, code lost:
        r4 = th;
        r6 = r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00aa */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00f6 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:22:0x006c] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f9 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:1:0x0006] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.xiaomi.push.aq a(android.content.Context r4, java.lang.String r5, java.lang.String r6, java.util.Map<java.lang.String, java.lang.String> r7, java.lang.String r8) {
        /*
            com.xiaomi.push.aq r0 = new com.xiaomi.push.aq
            r0.<init>()
            r1 = 0
            java.net.URL r2 = a((java.lang.String) r5)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.net.HttpURLConnection r4 = a((android.content.Context) r4, (java.net.URL) r2)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r2 = 10000(0x2710, float:1.4013E-41)
            r4.setConnectTimeout(r2)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r2 = 15000(0x3a98, float:2.102E-41)
            r4.setReadTimeout(r2)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            if (r6 != 0) goto L_0x001c
            java.lang.String r6 = "GET"
        L_0x001c:
            r4.setRequestMethod(r6)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            if (r7 == 0) goto L_0x003f
            java.util.Set r6 = r7.keySet()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
        L_0x0029:
            boolean r2 = r6.hasNext()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            if (r2 == 0) goto L_0x003f
            java.lang.Object r2 = r6.next()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.Object r3 = r7.get(r2)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r4.setRequestProperty(r2, r3)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            goto L_0x0029
        L_0x003f:
            boolean r6 = android.text.TextUtils.isEmpty(r8)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r7 = 0
            r2 = 1
            if (r6 != 0) goto L_0x006c
            r4.setDoOutput(r2)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            byte[] r6 = r8.getBytes()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.io.OutputStream r8 = r4.getOutputStream()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            int r3 = r6.length     // Catch:{ IOException -> 0x0067, Throwable -> 0x0062, all -> 0x005d }
            r8.write(r6, r7, r3)     // Catch:{ IOException -> 0x0067, Throwable -> 0x0062, all -> 0x005d }
            r8.flush()     // Catch:{ IOException -> 0x0067, Throwable -> 0x0062, all -> 0x005d }
            r8.close()     // Catch:{ IOException -> 0x0067, Throwable -> 0x0062, all -> 0x005d }
            goto L_0x006c
        L_0x005d:
            r4 = move-exception
            r6 = r1
            r1 = r8
            goto L_0x012f
        L_0x0062:
            r4 = move-exception
            r6 = r1
            r1 = r8
            goto L_0x00fb
        L_0x0067:
            r4 = move-exception
            r6 = r1
            r1 = r8
            goto L_0x0107
        L_0x006c:
            int r6 = r4.getResponseCode()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r0.a = r6     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.String r6 = "com.xiaomi.common.Network"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r8.<init>()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.String r3 = "Http POST Response Code: "
            r8.append(r3)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            int r3 = r0.a     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r8.append(r3)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            android.util.Log.d(r6, r8)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
        L_0x008a:
            java.lang.String r6 = r4.getHeaderFieldKey(r7)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.lang.String r8 = r4.getHeaderField(r7)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            if (r6 != 0) goto L_0x00ed
            if (r8 != 0) goto L_0x00ed
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            com.xiaomi.push.as$a r8 = new com.xiaomi.push.as$a     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            java.io.InputStream r2 = r4.getInputStream()     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            r8.<init>(r2)     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            r6.<init>(r7)     // Catch:{ IOException -> 0x00aa, Throwable -> 0x00f9, all -> 0x00f6 }
            goto L_0x00bd
        L_0x00aa:
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            com.xiaomi.push.as$a r8 = new com.xiaomi.push.as$a     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            java.io.InputStream r4 = r4.getErrorStream()     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r8.<init>(r4)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r6.<init>(r7)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
        L_0x00bd:
            java.lang.String r4 = r6.readLine()     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            java.lang.StringBuffer r7 = new java.lang.StringBuffer     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            r7.<init>()     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            java.lang.String r8 = "line.separator"
            java.lang.String r8 = java.lang.System.getProperty(r8)     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
        L_0x00cc:
            if (r4 == 0) goto L_0x00d9
            r7.append(r4)     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            r7.append(r8)     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            java.lang.String r4 = r6.readLine()     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            goto L_0x00cc
        L_0x00d9:
            java.lang.String r4 = r7.toString()     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            r0.f122a = r4     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            r6.close()     // Catch:{ IOException -> 0x00eb, Throwable -> 0x00e9 }
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            return r0
        L_0x00e9:
            r4 = move-exception
            goto L_0x00fb
        L_0x00eb:
            r4 = move-exception
            goto L_0x0107
        L_0x00ed:
            java.util.Map<java.lang.String, java.lang.String> r3 = r0.f123a     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            r3.put(r6, r8)     // Catch:{ IOException -> 0x0105, Throwable -> 0x00f9, all -> 0x00f6 }
            int r7 = r7 + 1
            int r7 = r7 + r2
            goto L_0x008a
        L_0x00f6:
            r4 = move-exception
            r6 = r1
            goto L_0x012f
        L_0x00f9:
            r4 = move-exception
            r6 = r1
        L_0x00fb:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x012e }
            java.lang.String r4 = r4.getMessage()     // Catch:{ all -> 0x012e }
            r5.<init>(r4)     // Catch:{ all -> 0x012e }
            throw r5     // Catch:{ all -> 0x012e }
        L_0x0105:
            r4 = move-exception
            r6 = r1
        L_0x0107:
            java.io.IOException r7 = new java.io.IOException     // Catch:{ all -> 0x012e }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x012e }
            r8.<init>()     // Catch:{ all -> 0x012e }
            java.lang.String r0 = "err while request "
            r8.append(r0)     // Catch:{ all -> 0x012e }
            r8.append(r5)     // Catch:{ all -> 0x012e }
            java.lang.String r5 = ":"
            r8.append(r5)     // Catch:{ all -> 0x012e }
            java.lang.Class r4 = r4.getClass()     // Catch:{ all -> 0x012e }
            java.lang.String r4 = r4.getSimpleName()     // Catch:{ all -> 0x012e }
            r8.append(r4)     // Catch:{ all -> 0x012e }
            java.lang.String r4 = r8.toString()     // Catch:{ all -> 0x012e }
            r7.<init>(r4)     // Catch:{ all -> 0x012e }
            throw r7     // Catch:{ all -> 0x012e }
        L_0x012e:
            r4 = move-exception
        L_0x012f:
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r6)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.as.a(android.content.Context, java.lang.String, java.lang.String, java.util.Map, java.lang.String):com.xiaomi.push.aq");
    }

    public static aq a(Context context, String str, Map<String, String> map) {
        return a(context, str, "POST", (Map<String, String>) null, a(map));
    }

    public static InputStream a(Context context, URL url, boolean z, String str, String str2) {
        return a(context, url, z, str, str2, (Map<String, String>) null, (b) null);
    }

    public static InputStream a(Context context, URL url, boolean z, String str, String str2, Map<String, String> map, b bVar) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        } else if (url != null) {
            URL url2 = !z ? new URL(a(url.toString())) : url;
            try {
                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection a2 = a(context, url2);
                a2.setConnectTimeout(10000);
                a2.setReadTimeout(15000);
                if (!TextUtils.isEmpty(str)) {
                    a2.setRequestProperty("User-Agent", str);
                }
                if (str2 != null) {
                    a2.setRequestProperty(HttpConstant.COOKIE, str2);
                }
                if (map != null) {
                    for (String next : map.keySet()) {
                        a2.setRequestProperty(next, map.get(next));
                    }
                }
                if (bVar != null && (url.getProtocol().equals("http") || url.getProtocol().equals("https"))) {
                    bVar.a = a2.getResponseCode();
                    if (bVar.f124a == null) {
                        bVar.f124a = new HashMap();
                    }
                    int i = 0;
                    while (true) {
                        String headerFieldKey = a2.getHeaderFieldKey(i);
                        String headerField = a2.getHeaderField(i);
                        if (headerFieldKey == null && headerField == null) {
                            break;
                        }
                        if (!TextUtils.isEmpty(headerFieldKey)) {
                            if (!TextUtils.isEmpty(headerField)) {
                                bVar.f124a.put(headerFieldKey, headerField);
                            }
                        }
                        i++;
                    }
                }
                return new a(a2.getInputStream());
            } catch (IOException e) {
                throw new IOException("IOException:" + e.getClass().getSimpleName());
            } catch (Throwable th) {
                throw new IOException(th.getMessage());
            }
        } else {
            throw new IllegalArgumentException("url");
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m89a(Context context) {
        if (d(context)) {
            return "wifi";
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return "";
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return "";
                }
                return (activeNetworkInfo.getTypeName() + "-" + activeNetworkInfo.getSubtypeName() + "-" + activeNetworkInfo.getExtraInfo()).toLowerCase();
            } catch (Exception unused) {
                return "";
            }
        } catch (Exception unused2) {
            return "";
        }
    }

    public static String a(Context context, URL url) {
        return a(context, url, false, (String) null, "UTF-8", (String) null);
    }

    public static String a(Context context, URL url, boolean z, String str, String str2, String str3) {
        InputStream inputStream;
        try {
            inputStream = a(context, url, z, str, str3);
            try {
                StringBuilder sb = new StringBuilder(1024);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, str2));
                char[] cArr = new char[4096];
                while (true) {
                    int read = bufferedReader.read(cArr);
                    if (-1 != read) {
                        sb.append(cArr, 0, read);
                    } else {
                        y.a((Closeable) inputStream);
                        return sb.toString();
                    }
                }
            } catch (Throwable th) {
                th = th;
                y.a((Closeable) inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            y.a((Closeable) inputStream);
            throw th;
        }
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        new String();
        return String.format("%s&key=%s", new Object[]{str, ax.a(String.format("%sbe988a6134bc8254465424e5a70ef037", new Object[]{str}))});
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v9, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v10, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v19, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v25, resolved type: java.io.File} */
    /* JADX WARNING: type inference failed for: r7v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r7v21 */
    /* JADX WARNING: type inference failed for: r7v24 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(java.lang.String r5, java.util.Map<java.lang.String, java.lang.String> r6, java.io.File r7, java.lang.String r8) {
        /*
            boolean r0 = r7.exists()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.String r0 = r7.getName()
            java.net.URL r2 = new java.net.URL     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r2.<init>(r5)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.net.URLConnection r5 = r2.openConnection()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r2 = 15000(0x3a98, float:2.102E-41)
            r5.setReadTimeout(r2)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r2 = 10000(0x2710, float:1.4013E-41)
            r5.setConnectTimeout(r2)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r2 = 1
            r5.setDoInput(r2)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r5.setDoOutput(r2)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r2 = 0
            r5.setUseCaches(r2)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r3 = "POST"
            r5.setRequestMethod(r3)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r3 = "Connection"
            java.lang.String r4 = "Keep-Alive"
            r5.setRequestProperty(r3, r4)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r3 = "Content-Type"
            java.lang.String r4 = "multipart/form-data;boundary=*****"
            r5.setRequestProperty(r3, r4)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            if (r6 == 0) goto L_0x0065
            java.util.Set r6 = r6.entrySet()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
        L_0x0049:
            boolean r3 = r6.hasNext()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            if (r3 == 0) goto L_0x0065
            java.lang.Object r3 = r6.next()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.Object r4 = r3.getKey()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.Object r3 = r3.getValue()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r5.setRequestProperty(r4, r3)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            goto L_0x0049
        L_0x0065:
            int r6 = r0.length()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            int r6 = r6 + 77
            long r3 = r7.length()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            int r0 = (int) r3     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            int r6 = r6 + r0
            int r0 = r8.length()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            int r6 = r6 + r0
            r5.setFixedLengthStreamingMode(r6)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.io.DataOutputStream r6 = new java.io.DataOutputStream     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.io.OutputStream r0 = r5.getOutputStream()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r6.<init>(r0)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r0 = "--*****\r\n"
            r6.writeBytes(r0)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r0.<init>()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r3 = "Content-Disposition: form-data; name=\""
            r0.append(r3)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r0.append(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r8 = "\";filename=\""
            r0.append(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r8 = r7.getName()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r0.append(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r8 = "\""
            r0.append(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r8 = "\r\n"
            r0.append(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r8 = r0.toString()     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r6.writeBytes(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.lang.String r8 = "\r\n"
            r6.writeBytes(r8)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r8.<init>(r7)     // Catch:{ IOException -> 0x0137, Throwable -> 0x012b, all -> 0x0128 }
            r7 = 1024(0x400, float:1.435E-42)
            byte[] r7 = new byte[r7]     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
        L_0x00bf:
            int r0 = r8.read(r7)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r3 = -1
            if (r0 == r3) goto L_0x00cd
            r6.write(r7, r2, r0)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r6.flush()     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            goto L_0x00bf
        L_0x00cd:
            java.lang.String r7 = "\r\n"
            r6.writeBytes(r7)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.lang.String r7 = "--"
            r6.writeBytes(r7)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.lang.String r7 = "*****"
            r6.writeBytes(r7)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.lang.String r7 = "--"
            r6.writeBytes(r7)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.lang.String r7 = "\r\n"
            r6.writeBytes(r7)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r6.flush()     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.lang.StringBuffer r6 = new java.lang.StringBuffer     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r6.<init>()     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            com.xiaomi.push.as$a r2 = new com.xiaomi.push.as$a     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            java.io.InputStream r5 = r5.getInputStream()     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r2.<init>(r5)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r0.<init>(r2)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
            r7.<init>(r0)     // Catch:{ IOException -> 0x0124, Throwable -> 0x0120, all -> 0x011c }
        L_0x0101:
            java.lang.String r5 = r7.readLine()     // Catch:{ IOException -> 0x011a, Throwable -> 0x0118, all -> 0x0116 }
            if (r5 == 0) goto L_0x010b
            r6.append(r5)     // Catch:{ IOException -> 0x011a, Throwable -> 0x0118, all -> 0x0116 }
            goto L_0x0101
        L_0x010b:
            java.lang.String r5 = r6.toString()     // Catch:{ IOException -> 0x011a, Throwable -> 0x0118, all -> 0x0116 }
            com.xiaomi.push.y.a((java.io.Closeable) r8)
            com.xiaomi.push.y.a((java.io.Closeable) r7)
            return r5
        L_0x0116:
            r5 = move-exception
            goto L_0x011e
        L_0x0118:
            r5 = move-exception
            goto L_0x0122
        L_0x011a:
            r5 = move-exception
            goto L_0x0126
        L_0x011c:
            r5 = move-exception
            r7 = r1
        L_0x011e:
            r1 = r8
            goto L_0x0159
        L_0x0120:
            r5 = move-exception
            r7 = r1
        L_0x0122:
            r1 = r8
            goto L_0x012d
        L_0x0124:
            r5 = move-exception
            r7 = r1
        L_0x0126:
            r1 = r8
            goto L_0x0139
        L_0x0128:
            r5 = move-exception
            r7 = r1
            goto L_0x0159
        L_0x012b:
            r5 = move-exception
            r7 = r1
        L_0x012d:
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x0158 }
            java.lang.String r5 = r5.getMessage()     // Catch:{ all -> 0x0158 }
            r6.<init>(r5)     // Catch:{ all -> 0x0158 }
            throw r6     // Catch:{ all -> 0x0158 }
        L_0x0137:
            r5 = move-exception
            r7 = r1
        L_0x0139:
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x0158 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0158 }
            r8.<init>()     // Catch:{ all -> 0x0158 }
            java.lang.String r0 = "IOException:"
            r8.append(r0)     // Catch:{ all -> 0x0158 }
            java.lang.Class r5 = r5.getClass()     // Catch:{ all -> 0x0158 }
            java.lang.String r5 = r5.getSimpleName()     // Catch:{ all -> 0x0158 }
            r8.append(r5)     // Catch:{ all -> 0x0158 }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x0158 }
            r6.<init>(r5)     // Catch:{ all -> 0x0158 }
            throw r6     // Catch:{ all -> 0x0158 }
        L_0x0158:
            r5 = move-exception
        L_0x0159:
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r7)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.as.a(java.lang.String, java.util.Map, java.io.File, java.lang.String):java.lang.String");
    }

    public static String a(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry next : map.entrySet()) {
            if (!(next.getKey() == null || next.getValue() == null)) {
                try {
                    stringBuffer.append(URLEncoder.encode((String) next.getKey(), "UTF-8"));
                    stringBuffer.append(SymbolExpUtil.SYMBOL_EQUAL);
                    stringBuffer.append(URLEncoder.encode((String) next.getValue(), "UTF-8"));
                    stringBuffer.append("&");
                } catch (UnsupportedEncodingException e) {
                    Log.d("com.xiaomi.common.Network", "Failed to convert from params map to string: " + e.toString());
                    Log.d("com.xiaomi.common.Network", "map: " + map.toString());
                    return null;
                }
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    /* renamed from: a  reason: collision with other method in class */
    public static HttpURLConnection m90a(Context context, URL url) {
        return (HttpURLConnection) (("http".equals(url.getProtocol()) && a(context)) ? url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80))) : url.openConnection());
    }

    /* renamed from: a  reason: collision with other method in class */
    private static URL m91a(String str) {
        return new URL(str);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* renamed from: a  reason: collision with other method in class */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean m92a(android.content.Context r3) {
        /*
            java.lang.String r0 = "phone"
            java.lang.Object r0 = r3.getSystemService(r0)
            android.telephony.TelephonyManager r0 = (android.telephony.TelephonyManager) r0
            java.lang.String r0 = r0.getSimCountryIso()
            java.lang.String r1 = "CN"
            boolean r0 = r1.equalsIgnoreCase(r0)
            r1 = 0
            if (r0 != 0) goto L_0x0016
            return r1
        L_0x0016:
            java.lang.String r0 = "connectivity"
            java.lang.Object r3 = r3.getSystemService(r0)     // Catch:{ Exception -> 0x0044 }
            android.net.ConnectivityManager r3 = (android.net.ConnectivityManager) r3     // Catch:{ Exception -> 0x0044 }
            if (r3 != 0) goto L_0x0021
            return r1
        L_0x0021:
            android.net.NetworkInfo r3 = r3.getActiveNetworkInfo()     // Catch:{  }
            if (r3 != 0) goto L_0x0028
            return r1
        L_0x0028:
            java.lang.String r3 = r3.getExtraInfo()
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 != 0) goto L_0x0044
            int r0 = r3.length()
            r2 = 3
            if (r0 >= r2) goto L_0x003a
            goto L_0x0044
        L_0x003a:
            java.lang.String r0 = "ctwap"
            boolean r3 = r3.contains(r0)
            if (r3 == 0) goto L_0x0044
            r3 = 1
            return r3
        L_0x0044:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.as.m92a(android.content.Context):boolean");
    }

    public static boolean b(Context context) {
        return a(context) >= 0;
    }

    public static boolean c(Context context) {
        NetworkInfo networkInfo;
        try {
            networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception unused) {
            networkInfo = null;
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        r2 = r2.getActiveNetworkInfo();
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean d(android.content.Context r2) {
        /*
            r0 = 0
            java.lang.String r1 = "connectivity"
            java.lang.Object r2 = r2.getSystemService(r1)     // Catch:{ Exception -> 0x001b }
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2     // Catch:{ Exception -> 0x001b }
            if (r2 != 0) goto L_0x000c
            return r0
        L_0x000c:
            android.net.NetworkInfo r2 = r2.getActiveNetworkInfo()     // Catch:{  }
            if (r2 != 0) goto L_0x0013
            return r0
        L_0x0013:
            int r2 = r2.getType()
            r1 = 1
            if (r1 != r2) goto L_0x001b
            r0 = 1
        L_0x001b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.as.d(android.content.Context):boolean");
    }

    public static boolean e(Context context) {
        return f(context) || g(context) || h(context);
    }

    public static boolean f(Context context) {
        NetworkInfo a2 = a(context);
        return a2 != null && a2.getType() == 0 && 13 == a2.getSubtype();
    }

    public static boolean g(Context context) {
        NetworkInfo a2 = a(context);
        if (a2 == null || a2.getType() != 0) {
            return false;
        }
        String subtypeName = a2.getSubtypeName();
        if (!"TD-SCDMA".equalsIgnoreCase(subtypeName) && !"CDMA2000".equalsIgnoreCase(subtypeName) && !"WCDMA".equalsIgnoreCase(subtypeName)) {
            switch (a2.getSubtype()) {
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static boolean h(Context context) {
        NetworkInfo a2 = a(context);
        if (a2 == null || a2.getType() != 0) {
            return false;
        }
        int subtype = a2.getSubtype();
        if (subtype == 4 || subtype == 7 || subtype == 11) {
            return true;
        }
        switch (subtype) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }
}
