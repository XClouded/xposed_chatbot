package com.uploader.implement.b.a;

import com.uploader.implement.b.a;
import com.uploader.implement.b.e;
import com.uploader.implement.c;

/* compiled from: SocketConnectionTarget */
public class f extends a {
    public final boolean f;

    public f(String str, int i, boolean z, boolean z2) {
        super(str, i, (String) null, 0, z);
        this.f = z2;
    }

    public e a(c cVar) {
        if (this.e) {
            return new d(cVar, this);
        }
        return new d(cVar, this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj instanceof f) && super.equals(obj) && this.f == ((f) obj).f) {
            return true;
        }
        return false;
    }
}
