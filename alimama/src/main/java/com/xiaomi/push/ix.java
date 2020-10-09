package com.xiaomi.push;

import com.xiaomi.push.iy;
import java.io.ByteArrayOutputStream;

public class ix {
    private jc a;

    /* renamed from: a  reason: collision with other field name */
    private final jj f775a;

    /* renamed from: a  reason: collision with other field name */
    private final ByteArrayOutputStream f776a;

    public ix() {
        this(new iy.a());
    }

    public ix(je jeVar) {
        this.f776a = new ByteArrayOutputStream();
        this.f775a = new jj(this.f776a);
        this.a = jeVar.a(this.f775a);
    }

    public byte[] a(ir irVar) {
        this.f776a.reset();
        irVar.b(this.a);
        return this.f776a.toByteArray();
    }
}
