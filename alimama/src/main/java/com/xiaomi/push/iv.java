package com.xiaomi.push;

import com.xiaomi.push.iy;

public class iv {
    private final jc a;

    /* renamed from: a  reason: collision with other field name */
    private final jl f774a;

    public iv() {
        this(new iy.a());
    }

    public iv(je jeVar) {
        this.f774a = new jl();
        this.a = jeVar.a(this.f774a);
    }

    public void a(ir irVar, byte[] bArr) {
        try {
            this.f774a.a(bArr);
            irVar.a(this.a);
        } finally {
            this.a.k();
        }
    }
}
