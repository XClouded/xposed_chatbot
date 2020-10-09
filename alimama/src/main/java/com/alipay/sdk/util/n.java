package com.alipay.sdk.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;
import androidx.annotation.Nullable;
import anet.channel.util.HttpConstant;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.i;
import com.alipay.sdk.app.j;
import com.alipay.sdk.app.k;
import com.alipay.sdk.app.statistic.c;
import com.alipay.sdk.data.a;
import com.facebook.imageutils.JfifUtil;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONObject;

@SuppressLint({"SetJavaScriptEnabled", "DefaultLocale"})
public class n {
    static final String a = "com.alipay.android.app";
    public static final String b = "com.eg.android.AlipayGphone";
    public static final int c = 99;
    public static final int d = 73;
    public static final String[] e = {"10.1.5.1013151", "10.1.5.1013148"};
    public static final int f = 125;
    private static final String g = "com.eg.android.AlipayGphoneRC";
    private static final String h = ".alipay.wallet";
    private static final char[] i = com.coloros.mcssdk.c.a.f.toCharArray();

    public static String h(Context context) {
        return "-1;-1";
    }

    public static String a() {
        if (EnvUtils.isSandBox()) {
            return "com.eg.android.AlipayGphoneRC";
        }
        try {
            return i.a.get(0).a;
        } catch (Throwable unused) {
            return "com.eg.android.AlipayGphone";
        }
    }

    public static String a(String str) {
        return (!EnvUtils.isSandBox() || !TextUtils.equals(str, "com.eg.android.AlipayGphoneRC")) ? "com.eg.android.AlipayGphone.IAlixPay" : "com.eg.android.AlipayGphoneRC.IAlixPay";
    }

    public static Map<String, String> b(String str) {
        HashMap hashMap = new HashMap();
        for (String str2 : str.split("&")) {
            int indexOf = str2.indexOf(SymbolExpUtil.SYMBOL_EQUAL, 1);
            if (-1 != indexOf) {
                hashMap.put(str2.substring(0, indexOf), URLDecoder.decode(str2.substring(indexOf + 1)));
            }
        }
        return hashMap;
    }

    public static Map<String, String> c(String str) {
        HashMap hashMap = new HashMap(4);
        int indexOf = str.indexOf(63);
        if (indexOf != -1 && indexOf < str.length() - 1) {
            for (String str2 : str.substring(indexOf + 1).split("&")) {
                int indexOf2 = str2.indexOf(61, 1);
                if (indexOf2 != -1 && indexOf2 < str2.length() - 1) {
                    hashMap.put(str2.substring(0, indexOf2), e(str2.substring(indexOf2 + 1)));
                }
            }
        }
        return hashMap;
    }

    public static JSONObject d(String str) {
        try {
            return new JSONObject(str);
        } catch (Throwable unused) {
            return new JSONObject();
        }
    }

    public static String e(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e2) {
            com.alipay.sdk.app.statistic.a.a(c.b, c.s, (Throwable) e2);
            return "";
        }
    }

    public static String a(String str, String str2, String str3) {
        try {
            int indexOf = str3.indexOf(str) + str.length();
            if (indexOf <= str.length()) {
                return "";
            }
            int i2 = 0;
            if (!TextUtils.isEmpty(str2)) {
                i2 = str3.indexOf(str2, indexOf);
            }
            if (i2 < 1) {
                return str3.substring(indexOf);
            }
            return str3.substring(indexOf, i2);
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String a(byte[] bArr) {
        BigInteger modulus;
        try {
            PublicKey publicKey = ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bArr))).getPublicKey();
            if (!(publicKey instanceof RSAPublicKey) || (modulus = ((RSAPublicKey) publicKey).getModulus()) == null) {
                return null;
            }
            return modulus.toString(16);
        } catch (Exception e2) {
            com.alipay.sdk.app.statistic.a.a("auth", c.o, (Throwable) e2);
            return null;
        }
    }

    public static a a(Context context, List<a.C0001a> list) {
        a a2;
        if (list == null) {
            return null;
        }
        for (a.C0001a next : list) {
            if (next != null && (a2 = a(context, next.a, next.b, next.c)) != null && !a2.a() && !a2.b()) {
                return a2;
            }
        }
        return null;
    }

    private static a a(Context context, String str, int i2, String str2) {
        PackageInfo packageInfo;
        if (EnvUtils.isSandBox() && "com.eg.android.AlipayGphone".equals(str)) {
            str = "com.eg.android.AlipayGphoneRC";
        }
        try {
            packageInfo = d(context, str);
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a("auth", c.l, th);
            packageInfo = null;
        }
        if (!b(packageInfo)) {
            return null;
        }
        return a(packageInfo, i2, str2);
    }

    private static boolean b(PackageInfo packageInfo) {
        String str = "";
        boolean z = false;
        if (packageInfo == null) {
            str = str + "info == null";
        } else if (packageInfo.signatures == null) {
            str = str + "info.signatures == null";
        } else if (packageInfo.signatures.length <= 0) {
            str = str + "info.signatures.length <= 0";
        } else {
            z = true;
        }
        if (!z) {
            com.alipay.sdk.app.statistic.a.a("auth", c.m, str);
        }
        return z;
    }

    private static PackageInfo d(Context context, String str) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(str, JfifUtil.MARKER_SOFn);
    }

    private static a a(PackageInfo packageInfo, int i2, String str) {
        if (packageInfo == null) {
            return null;
        }
        return new a(packageInfo, i2, str);
    }

    public static final class a {
        public final PackageInfo a;
        public final int b;
        public final String c;

        public a(PackageInfo packageInfo, int i, String str) {
            this.a = packageInfo;
            this.b = i;
            this.c = str;
        }

        public boolean a() {
            Signature[] signatureArr = this.a.signatures;
            if (signatureArr == null || signatureArr.length == 0) {
                return false;
            }
            int length = signatureArr.length;
            int i = 0;
            while (i < length) {
                String a2 = n.a(signatureArr[i].toByteArray());
                if (a2 == null || TextUtils.equals(a2, this.c)) {
                    i++;
                } else {
                    com.alipay.sdk.app.statistic.a.a(c.b, c.u, String.format("Got %s, expected %s", new Object[]{a2, this.c}));
                    return true;
                }
            }
            return false;
        }

        public boolean b() {
            return this.a.versionCode < this.b;
        }
    }

    public static boolean a(Context context) {
        try {
            if (context.getPackageManager().getPackageInfo(a, 128) == null) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean b(Context context, List<a.C0001a> list) {
        try {
            for (a.C0001a next : list) {
                if (next != null) {
                    String str = next.a;
                    if (EnvUtils.isSandBox() && "com.eg.android.AlipayGphone".equals(str)) {
                        str = "com.eg.android.AlipayGphoneRC";
                    }
                    try {
                        if (context.getPackageManager().getPackageInfo(str, 128) != null) {
                            return true;
                        }
                    } catch (PackageManager.NameNotFoundException unused) {
                        continue;
                    }
                }
            }
            return false;
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(c.b, c.I, th);
            return false;
        }
    }

    public static boolean a(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        try {
            String str = packageInfo.versionName;
            if (TextUtils.equals(str, e[0]) || TextUtils.equals(str, e[1])) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean b(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(a(), 128);
            if (packageInfo != null && packageInfo.versionCode < 99) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            c.a(th);
            return false;
        }
    }

    public static String c(Context context) {
        String b2 = b();
        String c2 = c();
        String d2 = d(context);
        String e2 = e(context);
        return " (" + b2 + ";" + c2 + ";" + d2 + ";" + ";" + e2 + Operators.BRACKET_END_STR + "(sdk android)";
    }

    public static String b() {
        return "Android " + Build.VERSION.RELEASE;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x00cf */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x00dd A[Catch:{ Throwable -> 0x00f9 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.webkit.WebView a(android.app.Activity r5, java.lang.String r6, java.lang.String r7) {
        /*
            android.content.Context r0 = r5.getApplicationContext()
            boolean r1 = android.text.TextUtils.isEmpty(r7)
            if (r1 != 0) goto L_0x001f
            android.webkit.CookieSyncManager r1 = android.webkit.CookieSyncManager.createInstance(r0)
            r1.sync()
            android.webkit.CookieManager r1 = android.webkit.CookieManager.getInstance()
            r1.setCookie(r6, r7)
            android.webkit.CookieSyncManager r7 = android.webkit.CookieSyncManager.getInstance()
            r7.sync()
        L_0x001f:
            android.widget.LinearLayout r7 = new android.widget.LinearLayout
            r7.<init>(r0)
            android.widget.LinearLayout$LayoutParams r1 = new android.widget.LinearLayout$LayoutParams
            r2 = -1
            r1.<init>(r2, r2)
            r2 = 1
            r7.setOrientation(r2)
            r5.setContentView(r7, r1)
            android.webkit.WebView r5 = new android.webkit.WebView
            r5.<init>(r0)
            r3 = 1065353216(0x3f800000, float:1.0)
            r1.weight = r3
            r3 = 0
            r5.setVisibility(r3)
            r7.addView(r5, r1)
            android.webkit.WebSettings r7 = r5.getSettings()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = r7.getUserAgentString()
            r1.append(r4)
            java.lang.String r4 = c((android.content.Context) r0)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            r7.setUserAgentString(r1)
            android.webkit.WebSettings$RenderPriority r1 = android.webkit.WebSettings.RenderPriority.HIGH
            r7.setRenderPriority(r1)
            r7.setSupportMultipleWindows(r2)
            r7.setJavaScriptEnabled(r2)
            r7.setSavePassword(r3)
            r7.setJavaScriptCanOpenWindowsAutomatically(r2)
            int r1 = r7.getMinimumFontSize()
            int r1 = r1 + 8
            r7.setMinimumFontSize(r1)
            r7.setAllowFileAccess(r3)
            r7.setAllowFileAccessFromFileURLs(r3)
            r7.setAllowUniversalAccessFromFileURLs(r3)
            r7.setAllowContentAccess(r3)
            android.webkit.WebSettings$TextSize r1 = android.webkit.WebSettings.TextSize.NORMAL
            r7.setTextSize(r1)
            r5.setVerticalScrollbarOverlay(r2)
            com.alipay.sdk.util.o r7 = new com.alipay.sdk.util.o
            r7.<init>(r0)
            r5.setDownloadListener(r7)
            int r7 = android.os.Build.VERSION.SDK_INT
            r0 = 7
            if (r7 < r0) goto L_0x00bf
            android.webkit.WebSettings r7 = r5.getSettings()     // Catch:{ Exception -> 0x00bf }
            java.lang.Class r7 = r7.getClass()     // Catch:{ Exception -> 0x00bf }
            java.lang.String r0 = "setDomStorageEnabled"
            java.lang.Class[] r1 = new java.lang.Class[r2]     // Catch:{ Exception -> 0x00bf }
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x00bf }
            r1[r3] = r4     // Catch:{ Exception -> 0x00bf }
            java.lang.reflect.Method r7 = r7.getMethod(r0, r1)     // Catch:{ Exception -> 0x00bf }
            if (r7 == 0) goto L_0x00bf
            android.webkit.WebSettings r0 = r5.getSettings()     // Catch:{ Exception -> 0x00bf }
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x00bf }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x00bf }
            r1[r3] = r4     // Catch:{ Exception -> 0x00bf }
            r7.invoke(r0, r1)     // Catch:{ Exception -> 0x00bf }
        L_0x00bf:
            java.lang.String r7 = "searchBoxJavaBridge_"
            r5.removeJavascriptInterface(r7)     // Catch:{ Throwable -> 0x00cf }
            java.lang.String r7 = "accessibility"
            r5.removeJavascriptInterface(r7)     // Catch:{ Throwable -> 0x00cf }
            java.lang.String r7 = "accessibilityTraversal"
            r5.removeJavascriptInterface(r7)     // Catch:{ Throwable -> 0x00cf }
            goto L_0x00fa
        L_0x00cf:
            java.lang.Class r7 = r5.getClass()     // Catch:{ Throwable -> 0x00f9 }
            java.lang.String r0 = "removeJavascriptInterface"
            java.lang.Class[] r1 = new java.lang.Class[r3]     // Catch:{ Throwable -> 0x00f9 }
            java.lang.reflect.Method r7 = r7.getMethod(r0, r1)     // Catch:{ Throwable -> 0x00f9 }
            if (r7 == 0) goto L_0x00fa
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00f9 }
            java.lang.String r1 = "searchBoxJavaBridge_"
            r0[r3] = r1     // Catch:{ Throwable -> 0x00f9 }
            r7.invoke(r5, r0)     // Catch:{ Throwable -> 0x00f9 }
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00f9 }
            java.lang.String r1 = "accessibility"
            r0[r3] = r1     // Catch:{ Throwable -> 0x00f9 }
            r7.invoke(r5, r0)     // Catch:{ Throwable -> 0x00f9 }
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00f9 }
            java.lang.String r1 = "accessibilityTraversal"
            r0[r3] = r1     // Catch:{ Throwable -> 0x00f9 }
            r7.invoke(r5, r0)     // Catch:{ Throwable -> 0x00f9 }
            goto L_0x00fa
        L_0x00f9:
        L_0x00fa:
            int r7 = android.os.Build.VERSION.SDK_INT
            r0 = 19
            if (r7 < r0) goto L_0x0108
            android.webkit.WebSettings r7 = r5.getSettings()
            r0 = 2
            r7.setCacheMode(r0)
        L_0x0108:
            r5.loadUrl(r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.util.n.a(android.app.Activity, java.lang.String, java.lang.String):android.webkit.WebView");
    }

    public static String c() {
        String d2 = d();
        int indexOf = d2.indexOf("-");
        if (indexOf != -1) {
            d2 = d2.substring(0, indexOf);
        }
        int indexOf2 = d2.indexOf("\n");
        if (indexOf2 != -1) {
            d2 = d2.substring(0, indexOf2);
        }
        return "Linux " + d2;
    }

    public static String d() {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/version"), 256);
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            Matcher matcher = Pattern.compile("\\w+\\s+\\w+\\s+([^\\s]+)\\s+\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+([^\\s]+)\\s+(?:PREEMPT\\s+)?(.+)").matcher(readLine);
            if (!matcher.matches() || matcher.groupCount() < 4) {
                return "Unavailable";
            }
            return matcher.group(1) + "\n" + matcher.group(2) + Operators.SPACE_STR + matcher.group(3) + "\n" + matcher.group(4);
        } catch (IOException unused) {
            return "Unavailable";
        } catch (Throwable th) {
            bufferedReader.close();
            throw th;
        }
    }

    public static String d(Context context) {
        return context.getResources().getConfiguration().locale.toString();
    }

    public static String e(Context context) {
        DisplayMetrics f2 = f(context);
        return f2.widthPixels + "*" + f2.heightPixels;
    }

    public static DisplayMetrics f(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String g(Context context) {
        String a2 = m.a(context);
        return a2.substring(0, a2.indexOf(HttpConstant.SCHEME_SPLIT));
    }

    public static String a(int i2) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            switch (random.nextInt(3)) {
                case 0:
                    sb.append(String.valueOf((char) ((int) Math.round((Math.random() * 25.0d) + 65.0d))));
                    break;
                case 1:
                    sb.append(String.valueOf((char) ((int) Math.round((Math.random() * 25.0d) + 97.0d))));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

    public static boolean f(String str) {
        return Pattern.compile("^http(s)?://([a-z0-9_\\-]+\\.)*(alipaydev|alipay|taobao)\\.(com|net)(:\\d+)?(/.*)?$").matcher(str).matches();
    }

    public static String a(Context context, String str) {
        String str2 = "";
        try {
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getApplicationContext().getSystemService("activity")).getRunningAppProcesses()) {
                if (next.processName.equals(str)) {
                    str2 = str2 + "#M";
                } else {
                    if (next.processName.startsWith(str + ":")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append("#");
                        sb.append(next.processName.replace(str + ":", ""));
                        str2 = sb.toString();
                    }
                }
            }
        } catch (Throwable unused) {
            str2 = "";
        }
        if (str2.length() > 0) {
            str2 = str2.substring(1);
        }
        return str2.length() == 0 ? "N" : str2;
    }

    @SuppressLint({"InlinedApi"})
    private static boolean c(PackageInfo packageInfo) {
        int i2 = packageInfo.applicationInfo.flags;
        return (i2 & 1) == 0 && (i2 & 128) == 0;
    }

    public static boolean a(WebView webView, String str, @Nullable Activity activity) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if (activity == null) {
            return false;
        }
        if (str.toLowerCase().startsWith(com.alipay.sdk.cons.a.j.toLowerCase()) || str.toLowerCase().startsWith(com.alipay.sdk.cons.a.k.toLowerCase())) {
            try {
                a a2 = a((Context) activity, i.a);
                if (a2 != null && !a2.b()) {
                    if (!a2.a()) {
                        if (str.startsWith("intent://platformapi/startapp")) {
                            str = str.replaceFirst("intent://platformapi/startapp\\?", com.alipay.sdk.cons.a.j);
                        }
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                        return true;
                    }
                }
                return true;
            } catch (Throwable unused) {
            }
        } else if (TextUtils.equals(str, com.alipay.sdk.cons.a.m) || TextUtils.equals(str, com.alipay.sdk.cons.a.n)) {
            j.a(j.c());
            activity.finish();
            return true;
        } else if (!str.startsWith(com.alipay.sdk.cons.a.l)) {
            return false;
        } else {
            try {
                String substring = str.substring(str.indexOf(com.alipay.sdk.cons.a.l) + com.alipay.sdk.cons.a.l.length());
                int parseInt = Integer.parseInt(substring.substring(substring.lastIndexOf(com.alipay.sdk.cons.a.o) + com.alipay.sdk.cons.a.o.length()));
                if (parseInt != k.SUCCEEDED.a()) {
                    if (parseInt != k.PAY_WAITTING.a()) {
                        k b2 = k.b(k.FAILED.a());
                        j.a(j.a(b2.a(), b2.b(), ""));
                        activity.runOnUiThread(new p(activity));
                        return true;
                    }
                }
                if (com.alipay.sdk.cons.a.s) {
                    StringBuilder sb = new StringBuilder();
                    String decode = URLDecoder.decode(str);
                    String decode2 = URLDecoder.decode(decode);
                    String str3 = decode2.substring(decode2.indexOf(com.alipay.sdk.cons.a.l) + com.alipay.sdk.cons.a.l.length(), decode2.lastIndexOf(com.alipay.sdk.cons.a.o)).split(com.alipay.sdk.cons.a.q)[0];
                    int indexOf = decode.indexOf(com.alipay.sdk.cons.a.q) + com.alipay.sdk.cons.a.q.length();
                    sb.append(str3);
                    sb.append(com.alipay.sdk.cons.a.q);
                    sb.append(decode.substring(indexOf, decode.indexOf("&", indexOf)));
                    sb.append(decode.substring(decode.indexOf("&", indexOf)));
                    str2 = sb.toString();
                } else {
                    String decode3 = URLDecoder.decode(str);
                    str2 = decode3.substring(decode3.indexOf(com.alipay.sdk.cons.a.l) + com.alipay.sdk.cons.a.l.length(), decode3.lastIndexOf(com.alipay.sdk.cons.a.o));
                }
                k b3 = k.b(parseInt);
                j.a(j.a(b3.a(), b3.b(), str2));
            } catch (Exception unused2) {
                j.a(j.e());
            }
            activity.runOnUiThread(new p(activity));
            return true;
        }
    }

    public static String i(Context context) {
        return b(context, context.getPackageName());
    }

    public static String b(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 128).versionName;
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(c.b, c.l, th);
            return "";
        }
    }

    public static int j(Context context) {
        return c(context, context.getPackageName());
    }

    public static int c(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 128).versionCode;
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(c.b, c.l, th);
            return -1;
        }
    }

    public static String a(Signature signature) {
        try {
            return b(MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256).digest(signature.toByteArray()));
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(c.b, c.D, th);
            return "";
        }
    }

    private static String b(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer((bArr.length * 3) - 1);
        int length = bArr.length - 1;
        for (int i2 = 0; i2 <= length; i2++) {
            byte b2 = bArr[i2];
            stringBuffer.append(i[(b2 & 240) >> 4]);
            stringBuffer.append(i[b2 & 15]);
            if (i2 < length) {
                stringBuffer.append(Operators.CONDITION_IF_MIDDLE);
            }
        }
        return stringBuffer.toString();
    }
}
