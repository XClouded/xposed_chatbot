package com.uploader.implement.b;

import com.uploader.implement.c;

/* compiled from: ConnectionTarget */
public abstract class a {
    public final String a;
    public final int b;
    public final String c;
    public final int d;
    public final boolean e;

    public abstract e a(c cVar);

    public a(String str, int i, String str2, int i2, boolean z) {
        this.a = str;
        this.b = i;
        this.c = str2;
        this.d = i2;
        this.e = z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof a)) {
            return false;
        }
        a aVar = (a) obj;
        if (this.b != aVar.b || this.d != aVar.d || this.e != aVar.e) {
            return false;
        }
        if (this.a == null ? aVar.a != null : !this.a.equals(aVar.a)) {
            return false;
        }
        if (this.c != null) {
            if (!this.c.equals(aVar.c)) {
                return false;
            }
            return true;
        } else if (aVar.c == null) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "ConnectionTarget{address='" + this.a + '\'' + ", port=" + this.b + ", proxyIp='" + this.c + '\'' + ", proxyPort=" + this.d + ", isLongLived=" + this.e + '}';
    }
}
