package com.uploader.implement.b.a;

import com.uploader.implement.b.a;
import com.uploader.implement.b.e;
import com.uploader.implement.c;

/* compiled from: UrlConnectionTarget */
public class g extends a {
    public final String f;
    public final String g;

    public g(String str, int i, String str2, String str3) {
        super(str, i, (String) null, 0, false);
        this.f = str2;
        this.g = str3;
    }

    public e a(c cVar) {
        return new e(cVar, this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof g) || !super.equals(obj)) {
            return false;
        }
        g gVar = (g) obj;
        if (this.f == null ? gVar.f != null : !this.f.equals(gVar.f)) {
            return false;
        }
        if (this.g != null) {
            return this.g.equals(gVar.g);
        }
        if (gVar.g == null) {
            return true;
        }
        return false;
    }
}
