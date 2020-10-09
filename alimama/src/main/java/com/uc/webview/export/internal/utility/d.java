package com.uc.webview.export.internal.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* compiled from: U4Source */
public final class d {
    private static d c;
    private final int a;
    private Map<String, Object> b;

    private d() {
        this.a = 128;
        this.b = null;
        this.b = Collections.synchronizedMap(new HashMap());
    }

    public static d a() {
        if (c == null) {
            synchronized (d.class) {
                if (c == null) {
                    c = new d();
                }
            }
        }
        return c;
    }

    public final boolean a(String str, Object obj) {
        if (this.b.size() >= 128) {
            return false;
        }
        this.b.put(str, obj);
        return true;
    }

    public final Object a(String str) {
        if (this.b.containsKey(str)) {
            return this.b.get(str);
        }
        return null;
    }

    public final boolean b(String str) {
        Boolean bool = (Boolean) a(str);
        return bool != null && bool.booleanValue();
    }
}
