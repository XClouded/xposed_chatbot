package com.xiaomi.push;

import java.util.ArrayList;
import java.util.Iterator;

class cw extends cq {
    cq a = this.b;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ cu f208a;
    final /* synthetic */ cq b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    cw(cu cuVar, String str, cq cqVar) {
        super(str);
        this.f208a = cuVar;
        this.b = cqVar;
        this.f194b = this.f194b;
        if (this.b != null) {
            this.f = this.b.f;
        }
    }

    public synchronized ArrayList<String> a(boolean z) {
        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();
        if (this.a != null) {
            arrayList.addAll(this.a.a(true));
        }
        synchronized (cu.b) {
            cq cqVar = cu.b.get(this.f194b);
            if (cqVar != null) {
                Iterator<String> it = cqVar.a(true).iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (arrayList.indexOf(next) == -1) {
                        arrayList.add(next);
                    }
                }
                arrayList.remove(this.f194b);
                arrayList.add(this.f194b);
            }
        }
        return arrayList;
    }

    public synchronized void a(String str, cp cpVar) {
        if (this.a != null) {
            this.a.a(str, cpVar);
        }
    }

    public boolean b() {
        return false;
    }
}
