package com.xiaomi.push;

public final class a {
    public static final a a = new a(new byte[0]);

    /* renamed from: a  reason: collision with other field name */
    private volatile int f106a = 0;

    /* renamed from: a  reason: collision with other field name */
    private final byte[] f107a;

    private a(byte[] bArr) {
        this.f107a = bArr;
    }

    public static a a(byte[] bArr) {
        return a(bArr, 0, bArr.length);
    }

    public static a a(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return new a(bArr2);
    }

    public int a() {
        return this.f107a.length;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m84a() {
        int length = this.f107a.length;
        byte[] bArr = new byte[length];
        System.arraycopy(this.f107a, 0, bArr, 0, length);
        return bArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof a)) {
            return false;
        }
        a aVar = (a) obj;
        int length = this.f107a.length;
        if (length != aVar.f107a.length) {
            return false;
        }
        byte[] bArr = this.f107a;
        byte[] bArr2 = aVar.f107a;
        for (int i = 0; i < length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = this.f106a;
        if (i == 0) {
            byte[] bArr = this.f107a;
            int length = this.f107a.length;
            int i2 = length;
            for (int i3 = 0; i3 < length; i3++) {
                i2 = (i2 * 31) + bArr[i3];
            }
            i = i2 == 0 ? 1 : i2;
            this.f106a = i;
        }
        return i;
    }
}
