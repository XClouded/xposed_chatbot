package com.xiaomi.push;

import android.text.TextUtils;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ee;
import com.xiaomi.push.service.al;
import java.util.HashMap;

class fe {
    public static void a(al.b bVar, String str, fm fmVar) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        ee.c cVar = new ee.c();
        if (!TextUtils.isEmpty(bVar.c)) {
            cVar.a(bVar.c);
        }
        if (!TextUtils.isEmpty(bVar.e)) {
            cVar.d(bVar.e);
        }
        if (!TextUtils.isEmpty(bVar.f)) {
            cVar.e(bVar.f);
        }
        cVar.b(bVar.f858a ? "1" : "0");
        cVar.c(!TextUtils.isEmpty(bVar.d) ? bVar.d : "XIAOMI-SASL");
        ff ffVar = new ff();
        ffVar.c(bVar.f859b);
        ffVar.a(Integer.parseInt(bVar.g));
        ffVar.b(bVar.f856a);
        ffVar.a("BIND", (String) null);
        ffVar.a(ffVar.e());
        b.a("[Slim]: bind id=" + ffVar.e());
        HashMap hashMap = new HashMap();
        hashMap.put("challenge", str);
        hashMap.put("token", bVar.c);
        hashMap.put("chid", bVar.g);
        hashMap.put(WeexDevOptions.EXTRA_FROM, bVar.f859b);
        hashMap.put("id", ffVar.e());
        hashMap.put("to", "xiaomi.com");
        if (bVar.f858a) {
            str2 = "kick";
            str3 = "1";
        } else {
            str2 = "kick";
            str3 = "0";
        }
        hashMap.put(str2, str3);
        if (!TextUtils.isEmpty(bVar.e)) {
            str4 = "client_attrs";
            str5 = bVar.e;
        } else {
            str4 = "client_attrs";
            str5 = "";
        }
        hashMap.put(str4, str5);
        if (!TextUtils.isEmpty(bVar.f)) {
            str6 = "cloud_attrs";
            str7 = bVar.f;
        } else {
            str6 = "cloud_attrs";
            str7 = "";
        }
        hashMap.put(str6, str7);
        if (bVar.d.equals("XIAOMI-PASS") || bVar.d.equals("XMPUSH-PASS")) {
            str8 = aw.a(bVar.d, (String) null, hashMap, bVar.h);
        } else {
            bVar.d.equals("XIAOMI-SASL");
            str8 = null;
        }
        cVar.f(str8);
        ffVar.a(cVar.a(), (String) null);
        fmVar.b(ffVar);
    }

    public static void a(String str, String str2, fm fmVar) {
        ff ffVar = new ff();
        ffVar.c(str2);
        ffVar.a(Integer.parseInt(str));
        ffVar.a("UBND", (String) null);
        fmVar.b(ffVar);
    }
}
