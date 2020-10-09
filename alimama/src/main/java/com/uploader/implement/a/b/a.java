package com.uploader.implement.a.b;

import android.text.TextUtils;
import com.uploader.implement.a.f;
import java.util.Map;

/* compiled from: ActionResponse */
public class a implements f {
    final int a;
    final Map<String, String> b;
    public final Object[] c;

    public a(int i, Map<String, String> map, Object... objArr) {
        this.a = i;
        this.b = map;
        this.c = objArr;
    }

    public a(int i, Map<String, String> map) {
        this.a = i;
        this.b = map;
        this.c = null;
    }

    public int a() {
        return this.a;
    }

    public Map<String, String> b() {
        return this.b;
    }

    public String a(String str) {
        if (TextUtils.isEmpty(str) || this.b == null) {
            return null;
        }
        return this.b.get(str);
    }
}
