package com.huawei.hianalytics.f.f;

import android.util.Pair;
import com.huawei.hianalytics.f.b.b;
import com.huawei.hianalytics.f.b.c;
import com.huawei.hianalytics.f.b.g;
import com.huawei.hianalytics.f.b.h;
import com.huawei.hianalytics.f.b.i;
import com.huawei.hianalytics.f.b.j;
import com.huawei.hianalytics.f.d.a;
import com.huawei.hianalytics.f.g.e;
import com.huawei.hianalytics.f.g.f;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class m {

    private static class a implements a.C0005a {
        private i a;

        public a(i iVar) {
            this.a = iVar;
        }

        public void a(long j, byte[] bArr) {
            this.a.g(String.valueOf(j));
            this.a.f(e.a(bArr));
        }
    }

    private static b a(String str, String str2) {
        b bVar = new b();
        a(bVar, str, str2);
        return bVar;
    }

    static h a(List<com.huawei.hianalytics.f.b.a> list, String str, String str2, String str3, String str4) {
        String str5;
        String str6;
        com.huawei.hianalytics.f.c.a b = h.a().b();
        long c = b.c();
        if (c == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            str6 = e.a();
            str5 = f.a(str6);
            b.a(currentTimeMillis);
            b.a(str6);
            b.b(str5);
        } else {
            long currentTimeMillis2 = System.currentTimeMillis();
            if (currentTimeMillis2 - c > 43200000) {
                String a2 = e.a();
                String a3 = f.a(a2);
                b.a(currentTimeMillis2);
                b.a(a2);
                b.b(a3);
                str6 = a2;
                str5 = a3;
            } else {
                str6 = b.a();
                str5 = b.b();
            }
        }
        h hVar = new h(str6, str2, str);
        hVar.a(a(str5, str2, str, str4));
        hVar.a(a(str, str2));
        hVar.a(a(str, str2, str3));
        hVar.a(list);
        return hVar;
    }

    private static i a(String str, String str2, String str3, String str4) {
        String str5;
        String str6;
        i iVar = new i();
        iVar.c(str);
        iVar.e(com.huawei.hianalytics.a.b.f());
        iVar.b(str2);
        iVar.a(str4);
        StringBuffer stringBuffer = new StringBuffer("hmshi");
        stringBuffer.append(str3);
        stringBuffer.append("qrt");
        iVar.d(stringBuffer.toString());
        try {
            com.huawei.hianalytics.f.d.a.a(iVar.a(), com.huawei.hianalytics.a.b.b(com.huawei.hianalytics.d.a.a().g(str2, str3)), new a(iVar));
        } catch (NoSuchAlgorithmException unused) {
            str6 = "HiAnalytics/event";
            str5 = "generateHeadData(): NoSuchAlgorithmException";
        } catch (UnsatisfiedLinkError unused2) {
            str6 = "HiAnalytics/event";
            str5 = "generateHeadData(): UnsatisfiedLinkError";
        }
        return iVar;
        com.huawei.hianalytics.g.b.c(str6, str5);
        return iVar;
    }

    private static j a(String str, String str2, String str3) {
        j jVar = new j();
        a(jVar, str, str2, str3);
        return jVar;
    }

    static List<com.huawei.hianalytics.f.b.e> a(c[] cVarArr) {
        ArrayList arrayList = new ArrayList(cVarArr.length);
        for (c gVar : cVarArr) {
            arrayList.add(new g(gVar));
        }
        return arrayList;
    }

    private static void a(b bVar, String str, String str2) {
        if (bVar != null) {
            String str3 = "";
            String str4 = "";
            String str5 = "";
            String d = com.huawei.hianalytics.d.a.a().d(str2, str);
            com.huawei.hianalytics.c.a a2 = com.huawei.hianalytics.d.a.a().a(str2, str);
            switch (a2.a()) {
                case SN:
                    str5 = a2.b();
                    break;
                case IMEI:
                    str3 = a2.b();
                    break;
                case UDID:
                    str4 = a2.b();
                    break;
            }
            bVar.a(d);
            bVar.c(com.huawei.hianalytics.a.b.b(com.huawei.hianalytics.d.a.a().g(str2, str)));
            bVar.b(str3);
            bVar.d(com.huawei.hianalytics.d.a.a().c(str2, str));
            String a3 = com.huawei.hianalytics.a.c.a(str2, str);
            bVar.e(str4);
            bVar.g(str5);
            bVar.f(a3);
        }
    }

    private static void a(j jVar, String str, String str2, String str3) {
        jVar.c(com.huawei.hianalytics.a.b.c());
        Pair<String, String> b = com.huawei.hianalytics.d.a.a().b(str2, str);
        jVar.f((String) b.first);
        jVar.g((String) b.second);
        jVar.d(com.huawei.hianalytics.a.b.e());
        jVar.e(str3);
        jVar.b(com.huawei.hianalytics.d.a.a().e(str2, str));
        jVar.a(com.huawei.hianalytics.d.a.a().f(str2, str));
    }
}
