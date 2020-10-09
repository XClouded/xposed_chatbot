package com.vivo.push.model;

/* compiled from: ConfigItem */
public final class a {
    private String a;
    private String b;

    public a(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public final String a() {
        return this.a;
    }

    public final String b() {
        return this.b;
    }

    public final int hashCode() {
        return (this.a == null ? 0 : this.a.hashCode()) + 31;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        a aVar = (a) obj;
        if (this.a == null) {
            if (aVar.a != null) {
                return false;
            }
        } else if (!this.a.equals(aVar.a)) {
            return false;
        }
        return true;
    }

    public final String toString() {
        return "ConfigItem{mKey='" + this.a + '\'' + ", mValue='" + this.b + '\'' + '}';
    }
}
