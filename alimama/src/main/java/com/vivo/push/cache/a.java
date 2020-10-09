package com.vivo.push.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.vivo.push.util.g;
import com.vivo.push.util.p;
import com.vivo.push.util.z;
import java.util.ArrayList;
import java.util.List;

/* compiled from: AppConfigSettings */
public final class a extends d<com.vivo.push.model.a> {
    /* access modifiers changed from: protected */
    public final String a() {
        return "com.vivo.pushservice.back_up";
    }

    public a(Context context) {
        super(context);
    }

    public final List<com.vivo.push.model.a> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str)) {
            for (String trim : str.trim().split("@#")) {
                String trim2 = trim.trim();
                String[] split = trim2.trim().split(",");
                if (split.length >= 2) {
                    try {
                        arrayList.add(new com.vivo.push.model.a(split[0], trim2.substring(split[0].length() + 1)));
                    } catch (Exception e) {
                        p.d("AppConfigSettings", "str2Clients E: " + e);
                    }
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public final String b(String str) throws Exception {
        return new String(g.a(g.a(a), g.a(b), Base64.decode(str, 2)), "utf-8");
    }

    public final com.vivo.push.model.a c(String str) {
        synchronized (c) {
            for (com.vivo.push.model.a aVar : this.d) {
                if (!TextUtils.isEmpty(aVar.a()) && aVar.a().equals(str)) {
                    return aVar;
                }
            }
            return null;
        }
    }

    public final int b() {
        com.vivo.push.model.a c = c("push_mode");
        if (c == null || TextUtils.isEmpty(c.b())) {
            return -1;
        }
        try {
            return Integer.parseInt(c.b());
        } catch (Exception unused) {
            return -1;
        }
    }

    public static boolean a(int i) {
        if (i != -1) {
            return (i & 1) != 0;
        }
        return z.b("persist.sys.log.ctrl", "no").equals("yes");
    }
}
