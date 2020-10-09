package com.vivo.push.c;

import com.vivo.push.b.v;
import com.vivo.push.p;
import com.vivo.push.w;
import com.vivo.push.y;
import java.util.ArrayList;
import java.util.List;

/* compiled from: OnDelTagsReceiveTask */
final class h extends ab {
    h(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        v vVar = (v) yVar;
        ArrayList<String> d = vVar.d();
        List<String> e = vVar.e();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        int h = vVar.h();
        String g = vVar.g();
        if (d != null) {
            for (String next : d) {
                if (next.startsWith("ali/")) {
                    arrayList2.add(next.replace("ali/", ""));
                } else if (next.startsWith("tag/")) {
                    arrayList.add(next.replace("tag/", ""));
                }
            }
        }
        if (e != null) {
            for (String next2 : e) {
                if (next2.startsWith("ali/")) {
                    arrayList4.add(next2.replace("ali/", ""));
                } else if (next2.startsWith("tag/")) {
                    arrayList3.add(next2.replace("tag/", ""));
                }
            }
        }
        if (arrayList.size() > 0 || arrayList3.size() > 0) {
            if (arrayList.size() > 0) {
                p.a().b((List<String>) arrayList);
            }
            p.a().a(vVar.g(), arrayList3.size() > 0 ? 10000 : h);
            w.b(new i(this, h, arrayList, arrayList3, g));
        }
        if (arrayList2.size() > 0 || arrayList4.size() > 0) {
            if (arrayList2.size() > 0) {
                p.a().c((List<String>) arrayList2);
            }
            p.a().a(vVar.g(), h);
            w.b(new j(this, h, arrayList2, arrayList4, g));
        }
    }
}
