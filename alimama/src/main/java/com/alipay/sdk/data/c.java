package com.alipay.sdk.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.TextView;
import com.alipay.mobilesecuritysdk.face.SecurityClientMobile;
import com.alipay.sdk.app.AlipayResultActivity;
import com.alipay.sdk.app.i;
import com.alipay.sdk.cons.a;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.n;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class c {
    private static final String a = "virtualImeiAndImsi";
    private static final String b = "virtual_imei";
    private static final String c = "virtual_imsi";
    private static c d;
    private String e;
    private String f = "sdk-and-lite";
    private String g;

    private String e() {
        return "1";
    }

    private String f() {
        return "-1;-1";
    }

    public String a() {
        return this.g;
    }

    private c() {
        String a2 = i.a();
        if (!i.b()) {
            this.f += '_' + a2;
        }
    }

    public static synchronized c b() {
        c cVar;
        synchronized (c.class) {
            if (d == null) {
                d = new c();
            }
            cVar = d;
        }
        return cVar;
    }

    public synchronized void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            PreferenceManager.getDefaultSharedPreferences(b.a().b()).edit().putString(com.alipay.sdk.cons.b.i, str).commit();
            a.c = str;
        }
    }

    private String b(Context context) {
        return Float.toString(new TextView(context).getTextSize());
    }

    public String a(com.alipay.sdk.tid.b bVar) {
        Context b2 = b.a().b();
        com.alipay.sdk.util.a a2 = com.alipay.sdk.util.a.a(b2);
        if (TextUtils.isEmpty(this.e)) {
            String b3 = n.b();
            String c2 = n.c();
            String d2 = n.d(b2);
            String g2 = n.g(b2);
            String e2 = n.e(b2);
            String b4 = b(b2);
            this.e = "Msp/15.6.2" + " (" + b3 + ";" + c2 + ";" + d2 + ";" + g2 + ";" + e2 + ";" + b4;
        }
        String b5 = com.alipay.sdk.util.a.b(b2).b();
        String h = n.h(b2);
        String e3 = e();
        String a3 = a2.a();
        String b6 = a2.b();
        String d3 = d();
        String c3 = c();
        if (bVar != null) {
            this.g = bVar.b();
        }
        String replace = Build.MANUFACTURER.replace(";", Operators.SPACE_STR);
        String replace2 = Build.MODEL.replace(";", Operators.SPACE_STR);
        boolean d4 = b.d();
        String d5 = a2.d();
        String c4 = c(b2);
        String d6 = d(b2);
        Context context = b2;
        StringBuilder sb = new StringBuilder();
        sb.append(this.e);
        sb.append(";");
        sb.append(b5);
        sb.append(";");
        sb.append(h);
        sb.append(";");
        sb.append(e3);
        sb.append(";");
        sb.append(a3);
        sb.append(";");
        sb.append(b6);
        sb.append(";");
        sb.append(this.g);
        sb.append(";");
        sb.append(replace);
        sb.append(";");
        sb.append(replace2);
        sb.append(";");
        sb.append(d4);
        sb.append(";");
        sb.append(d5);
        sb.append(";");
        sb.append(f());
        sb.append(";");
        sb.append(this.f);
        sb.append(";");
        sb.append(d3);
        sb.append(";");
        sb.append(c3);
        sb.append(";");
        sb.append(c4);
        sb.append(";");
        sb.append(d6);
        if (bVar != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("tid", com.alipay.sdk.tid.b.a(context).a());
            hashMap.put("utdid", b.a().e());
            String b7 = b(context, hashMap);
            if (!TextUtils.isEmpty(b7)) {
                sb.append(";");
                sb.append(b7);
            }
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }

    public String c() {
        String b2;
        Context b3 = b.a().b();
        SharedPreferences sharedPreferences = b3.getSharedPreferences(a, 0);
        String string = sharedPreferences.getString(b, (String) null);
        if (TextUtils.isEmpty(string)) {
            if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a(b3).a())) {
                b2 = g();
            } else {
                b2 = com.alipay.sdk.util.a.a(b3).b();
            }
            string = b2;
            sharedPreferences.edit().putString(b, string).commit();
        }
        return string;
    }

    public String d() {
        String str;
        Context b2 = b.a().b();
        SharedPreferences sharedPreferences = b2.getSharedPreferences(a, 0);
        String string = sharedPreferences.getString(c, (String) null);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a(b2).a())) {
            String e2 = b.a().e();
            if (TextUtils.isEmpty(e2)) {
                str = g();
            } else {
                str = e2.substring(3, 18);
            }
        } else {
            str = com.alipay.sdk.util.a.a(b2).a();
        }
        String str2 = str;
        sharedPreferences.edit().putString(c, str2).commit();
        return str2;
    }

    private String g() {
        String hexString = Long.toHexString(System.currentTimeMillis());
        Random random = new Random();
        return hexString + (random.nextInt(AlipayResultActivity.a) + 1000);
    }

    private String c(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        return connectionInfo != null ? connectionInfo.getSSID() : "-1";
    }

    private String d(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        return connectionInfo != null ? connectionInfo.getBSSID() : "00";
    }

    /* access modifiers changed from: private */
    public String a(Context context, HashMap<String, String> hashMap) {
        String str;
        try {
            str = SecurityClientMobile.GetApdid(context, hashMap);
        } catch (Throwable th) {
            com.alipay.sdk.util.c.a(th);
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.e, com.alipay.sdk.app.statistic.c.h, th);
            str = "";
        }
        if (TextUtils.isEmpty(str)) {
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.e, com.alipay.sdk.app.statistic.c.i, "apdid == null");
        }
        com.alipay.sdk.util.c.d("msp", "apdid:" + str);
        return str;
    }

    private String b(Context context, HashMap<String, String> hashMap) {
        try {
            return (String) Executors.newFixedThreadPool(2).submit(new d(this, context, hashMap)).get(TBToast.Duration.MEDIUM, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.e, com.alipay.sdk.app.statistic.c.j, th);
            return "";
        }
    }

    public String a(Context context) {
        if (context == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            sb.append(Operators.BRACKET_START_STR);
            sb.append(packageName);
            sb.append(";");
            sb.append(packageInfo.versionCode);
            sb.append(Operators.BRACKET_END_STR);
            return sb.toString();
        } catch (Exception unused) {
            return "";
        }
    }
}
