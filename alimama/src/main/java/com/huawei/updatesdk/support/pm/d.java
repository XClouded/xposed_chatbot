package com.huawei.updatesdk.support.pm;

import com.huawei.updatesdk.support.pm.c;
import java.util.concurrent.ConcurrentHashMap;

public class d extends ConcurrentHashMap<String, b> {
    private int a;

    public enum a {
        INSTALL_TYPE
    }

    /* renamed from: a */
    public b put(String str, b bVar) {
        if (bVar == null) {
            return null;
        }
        if (-1 != bVar.b()) {
            int i = this.a + 1;
            this.a = i;
            bVar.a(i);
        }
        if (bVar.g() == c.b.INSTALL) {
            str = "pre_hispace_install_" + str;
        }
        super.remove(str);
        return (b) super.put(str, bVar);
    }

    public b a(String str, a aVar) {
        return (b) super.get("pre_hispace_install_" + str);
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
