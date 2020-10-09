package com.vivo.push.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.vivo.push.model.a;
import com.vivo.push.util.g;
import com.vivo.push.util.p;
import java.util.ArrayList;
import java.util.List;

/* compiled from: PushConfigSettings */
public final class f extends d<a> {
    /* access modifiers changed from: protected */
    public final String a() {
        return "com.vivo.pushservice.other";
    }

    public f(Context context) {
        super(context);
    }

    public final List<a> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String trim : str.trim().split("@#")) {
            String trim2 = trim.trim();
            String[] split = trim2.trim().split(",");
            if (split.length >= 2) {
                try {
                    arrayList.add(new a(split[0], trim2.substring(split[0].length() + 1)));
                } catch (Exception e) {
                    p.d("PushConfigSettings", "str2Clients E: " + e);
                }
            }
        }
        return arrayList;
    }

    public final String c(String str) {
        synchronized (c) {
            for (a aVar : this.d) {
                if (!TextUtils.isEmpty(aVar.a()) && aVar.a().equals(str)) {
                    String b = aVar.b();
                    return b;
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public final String b(String str) throws Exception {
        return new String(g.a(g.a(a), g.a(b), Base64.decode(str, 2)), "utf-8");
    }
}
