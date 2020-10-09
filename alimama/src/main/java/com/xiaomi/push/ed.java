package com.xiaomi.push;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ed {

    public static final class a extends e {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private List<String> f244a = Collections.emptyList();

        /* renamed from: a  reason: collision with other field name */
        private boolean f245a;
        private int b = 0;

        /* renamed from: b  reason: collision with other field name */
        private boolean f246b;
        private int c = -1;

        /* renamed from: c  reason: collision with other field name */
        private boolean f247c = false;
        private boolean d;
        private boolean e;
        private boolean f = false;

        public static a a(byte[] bArr) {
            return (a) new a().a(bArr);
        }

        public static a b(b bVar) {
            return new a().a(bVar);
        }

        public int a() {
            if (this.c < 0) {
                b();
            }
            return this.c;
        }

        public a a(int i) {
            this.f245a = true;
            this.a = i;
            return this;
        }

        public a a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 8) {
                    a(bVar.c());
                } else if (a2 == 16) {
                    a(bVar.a());
                } else if (a2 == 24) {
                    b(bVar.b());
                } else if (a2 == 32) {
                    b(bVar.a());
                } else if (a2 == 42) {
                    a(bVar.a());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public a a(String str) {
            if (str != null) {
                if (this.f244a.isEmpty()) {
                    this.f244a = new ArrayList();
                }
                this.f244a.add(str);
                return this;
            }
            throw new NullPointerException();
        }

        public a a(boolean z) {
            this.f246b = true;
            this.f247c = z;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public List<String> m199a() {
            return this.f244a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.b(1, c());
            }
            if (c()) {
                cVar.a(2, b());
            }
            if (d()) {
                cVar.a(3, d());
            }
            if (f()) {
                cVar.a(4, e());
            }
            for (String a2 : a()) {
                cVar.a(5, a2);
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m200a() {
            return this.f245a;
        }

        public int b() {
            int i = 0;
            int b2 = a() ? c.b(1, c()) + 0 : 0;
            if (c()) {
                b2 += c.a(2, b());
            }
            if (d()) {
                b2 += c.a(3, d());
            }
            if (f()) {
                b2 += c.a(4, e());
            }
            for (String a2 : a()) {
                i += c.a(a2);
            }
            int size = b2 + i + (a().size() * 1);
            this.c = size;
            return size;
        }

        public a b(int i) {
            this.d = true;
            this.b = i;
            return this;
        }

        public a b(boolean z) {
            this.e = true;
            this.f = z;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m201b() {
            return this.f247c;
        }

        public int c() {
            return this.a;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m202c() {
            return this.f246b;
        }

        public int d() {
            return this.b;
        }

        /* renamed from: d  reason: collision with other method in class */
        public boolean m203d() {
            return this.d;
        }

        public int e() {
            return this.f244a.size();
        }

        /* renamed from: e  reason: collision with other method in class */
        public boolean m204e() {
            return this.f;
        }

        public boolean f() {
            return this.e;
        }
    }
}
