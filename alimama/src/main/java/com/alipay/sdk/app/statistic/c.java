package com.alipay.sdk.app.statistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alipay.sdk.tid.b;
import com.alipay.sdk.util.a;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.mipush.sdk.Constants;
import java.text.SimpleDateFormat;
import java.util.Date;
import mtopsdk.common.util.SymbolExpUtil;

public class c {
    public static final String A = "ClientBindServiceFailed";
    public static final String B = "BindWaitTimeoutEx";
    public static final String C = "CheckClientExistEx";
    public static final String D = "CheckClientSignEx";
    public static final String E = "GetInstalledAppEx";
    public static final String F = "ParserTidClientKeyEx";
    public static final String G = "GetInstalledAppEx";
    public static final String H = "StartLaunchAppTransEx";
    public static final String I = "CheckLaunchAppExistEx";
    public static final String J = "LogCurrentAppLaunchSwitch";
    public static final String K = "LogCurrentQueryTime";
    public static final String L = "LogCalledPackage";
    public static final String M = "LogBindCalledH5";
    public static final String N = "LogCalledH5";
    public static final String O = "LogHkLoginByIntent";
    public static final String P = "SchemePayWrongHashEx";
    public static final String Q = "LogAppLaunchSwitchEnabled";
    public static final String R = "H5CbUrlEmpty";
    public static final String S = "H5CbEx";
    public static final String T = "tid_context_null";
    public static final String U = "partner";
    public static final String V = "out_trade_no";
    public static final String W = "trade_no";
    public static final String a = "net";
    public static final String b = "biz";
    public static final String c = "cp";
    public static final String d = "auth";
    public static final String e = "third";
    public static final String f = "tid";
    public static final String g = "FormatResultEx";
    public static final String h = "GetApdidEx";
    public static final String i = "GetApdidNull";
    public static final String j = "GetApdidTimeout";
    public static final String k = "GetUtdidEx";
    public static final String l = "GetPackageInfoEx";
    public static final String m = "NotIncludeSignatures";
    public static final String n = "GetInstalledPackagesEx";
    public static final String o = "GetPublicKeyFromSignEx";
    public static final String p = "H5PayNetworkError";
    public static final String q = "H5AuthNetworkError";
    public static final String r = "SSLError";
    public static final String s = "H5PayDataAnalysisError";
    public static final String t = "H5AuthDataAnalysisError";
    public static final String u = "PublicKeyUnmatch";
    public static final String v = "ClientBindFailed";
    public static final String w = "TriDesEncryptError";
    public static final String x = "TriDesDecryptError";
    public static final String y = "ClientBindException";
    public static final String z = "SaveTradeTokenError";
    private String X;
    private String Y;
    private String Z;
    private String aa;
    private String ab;
    private String ac;
    private String ad;
    private String ae;
    private String af = "";
    private String ag;

    public c(Context context) {
        context = context != null ? context.getApplicationContext() : context;
        this.X = b();
        this.Z = a(context);
        this.aa = c();
        this.ab = d();
        this.ac = b(context);
        this.ad = "-";
        this.ae = "-";
        this.ag = "-";
    }

    public boolean a() {
        return TextUtils.isEmpty(this.af);
    }

    public void a(String str, String str2, Throwable th) {
        a(str, str2, a(th));
    }

    public void a(String str, String str2, Throwable th, String str3) {
        a(str, str2, a(th), str3);
    }

    public void a(String str, String str2, String str3, String str4) {
        String str5 = "";
        if (!TextUtils.isEmpty(this.af)) {
            str5 = str5 + "^";
        }
        this.af += (str5 + String.format("%s,%s,%s,%s", new Object[]{str, str2, b(str3), str4}));
    }

    public void a(String str, String str2, String str3) {
        a(str, str2, str3, "-");
    }

    private String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replace(Operators.ARRAY_START_STR, "【").replace(Operators.ARRAY_END_STR, "】").replace(Operators.BRACKET_START_STR, "（").replace(Operators.BRACKET_END_STR, "）").replace(",", "，").replace("-", SymbolExpUtil.SYMBOL_EQUAL).replace("^", Constants.WAVE_SEPARATOR);
    }

    private String a(Throwable th) {
        if (th == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append(th.getClass().getName());
            stringBuffer.append(":");
            stringBuffer.append(th.getMessage());
            stringBuffer.append(" 》 ");
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                for (int i2 = 0; i2 < stackTrace.length; i2++) {
                    stringBuffer.append(stackTrace[i2].toString() + " 》 ");
                }
            }
        } catch (Throwable unused) {
        }
        return stringBuffer.toString();
    }

    public String a(String str) {
        if (a()) {
            return "";
        }
        this.Y = c(str);
        return String.format("[(%s),(%s),(%s),(%s),(%s),(%s),(%s),(%s),(%s),(%s)]", new Object[]{this.X, this.Y, this.Z, this.aa, this.ab, this.ac, this.ad, this.ae, this.af, this.ag});
    }

    @SuppressLint({"SimpleDateFormat"})
    private String b() {
        return String.format("123456789,%s", new Object[]{new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date())});
    }

    private String c(String str) {
        String str2;
        String[] split = str.split("&");
        String str3 = null;
        if (split != null) {
            str2 = null;
            String str4 = null;
            for (String split2 : split) {
                String[] split3 = split2.split(SymbolExpUtil.SYMBOL_EQUAL);
                if (split3 != null && split3.length == 2) {
                    if (split3[0].equalsIgnoreCase(U)) {
                        split3[1].replace("\"", "");
                    } else if (split3[0].equalsIgnoreCase(V)) {
                        str2 = split3[1].replace("\"", "");
                    } else if (split3[0].equalsIgnoreCase(W)) {
                        str4 = split3[1].replace("\"", "");
                    }
                }
            }
            str3 = str4;
        } else {
            str2 = null;
        }
        String b2 = b(str3);
        String b3 = b(str2);
        return String.format("%s,%s,-,%s,-,-,-", new Object[]{b2, b3, b(b3)});
    }

    private String a(Context context) {
        String str = "-";
        String str2 = "-";
        if (context != null) {
            try {
                Context applicationContext = context.getApplicationContext();
                String packageName = applicationContext.getPackageName();
                try {
                    str2 = applicationContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
                } catch (Throwable unused) {
                }
                str = packageName;
            } catch (Throwable unused2) {
            }
        }
        return String.format("%s,%s,-,-,-", new Object[]{str, str2});
    }

    private String c() {
        return String.format("android,3,%s,%s,com.alipay.mcpay,5.0,-,-,-", new Object[]{b("15.6.2"), b("h.a.3.6.2")});
    }

    private String d() {
        return String.format("%s,%s,-,-,-", new Object[]{b(b.a(com.alipay.sdk.sys.b.a().b()).a()), b(com.alipay.sdk.sys.b.a().e())});
    }

    private String b(Context context) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,-", new Object[]{b(a.d(context)), "android", b(Build.VERSION.RELEASE), b(Build.MODEL), "-", b(a.a(context).a()), b(a.b(context).b()), "gw", b(a.a(context).b())});
    }
}
