package com.xiaomi.push;

public final class ee {

    public static final class a extends e {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private long f248a = 0;

        /* renamed from: a  reason: collision with other field name */
        private String f249a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f250a;
        private int b = 1;

        /* renamed from: b  reason: collision with other field name */
        private String f251b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f252b;
        private int c = 0;

        /* renamed from: c  reason: collision with other field name */
        private String f253c = "";

        /* renamed from: c  reason: collision with other field name */
        private boolean f254c;
        private int d = 0;

        /* renamed from: d  reason: collision with other field name */
        private String f255d = "";

        /* renamed from: d  reason: collision with other field name */
        private boolean f256d;
        private int e = -1;

        /* renamed from: e  reason: collision with other field name */
        private String f257e = "";

        /* renamed from: e  reason: collision with other field name */
        private boolean f258e;
        private String f = "";

        /* renamed from: f  reason: collision with other field name */
        private boolean f259f;
        private boolean g;
        private boolean h;
        private boolean i;
        private boolean j;
        private boolean k;

        public int a() {
            if (this.e < 0) {
                b();
            }
            return this.e;
        }

        /* renamed from: a  reason: collision with other method in class */
        public long m205a() {
            return this.f248a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public a m206a() {
            this.f259f = false;
            this.f255d = "";
            return this;
        }

        public a a(int i2) {
            this.f250a = true;
            this.a = i2;
            return this;
        }

        public a a(long j2) {
            this.f252b = true;
            this.f248a = j2;
            return this;
        }

        public a a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                switch (a2) {
                    case 0:
                        return this;
                    case 8:
                        a(bVar.b());
                        break;
                    case 16:
                        a(bVar.b());
                        break;
                    case 26:
                        a(bVar.a());
                        break;
                    case 34:
                        b(bVar.a());
                        break;
                    case 42:
                        c(bVar.a());
                        break;
                    case 50:
                        d(bVar.a());
                        break;
                    case 58:
                        e(bVar.a());
                        break;
                    case 64:
                        b(bVar.b());
                        break;
                    case 72:
                        c(bVar.b());
                        break;
                    case 80:
                        d(bVar.b());
                        break;
                    case 90:
                        f(bVar.a());
                        break;
                    default:
                        if (a(bVar, a2)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public a a(String str) {
            this.f254c = true;
            this.f249a = str;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m207a() {
            return this.f249a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, c());
            }
            if (b()) {
                cVar.b(2, a());
            }
            if (c()) {
                cVar.a(3, a());
            }
            if (d()) {
                cVar.a(4, b());
            }
            if (e()) {
                cVar.a(5, c());
            }
            if (f()) {
                cVar.a(6, d());
            }
            if (g()) {
                cVar.a(7, e());
            }
            if (h()) {
                cVar.a(8, d());
            }
            if (i()) {
                cVar.a(9, e());
            }
            if (j()) {
                cVar.a(10, f());
            }
            if (k()) {
                cVar.a(11, f());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m208a() {
            return this.f250a;
        }

        public int b() {
            int i2 = 0;
            if (a()) {
                i2 = 0 + c.a(1, c());
            }
            if (b()) {
                i2 += c.b(2, a());
            }
            if (c()) {
                i2 += c.a(3, a());
            }
            if (d()) {
                i2 += c.a(4, b());
            }
            if (e()) {
                i2 += c.a(5, c());
            }
            if (f()) {
                i2 += c.a(6, d());
            }
            if (g()) {
                i2 += c.a(7, e());
            }
            if (h()) {
                i2 += c.a(8, d());
            }
            if (i()) {
                i2 += c.a(9, e());
            }
            if (j()) {
                i2 += c.a(10, f());
            }
            if (k()) {
                i2 += c.a(11, f());
            }
            this.e = i2;
            return i2;
        }

        public a b(int i2) {
            this.h = true;
            this.b = i2;
            return this;
        }

        public a b(String str) {
            this.f256d = true;
            this.f251b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m209b() {
            return this.f251b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m210b() {
            return this.f252b;
        }

        public int c() {
            return this.a;
        }

        public a c(int i2) {
            this.i = true;
            this.c = i2;
            return this;
        }

        public a c(String str) {
            this.f258e = true;
            this.f253c = str;
            return this;
        }

        /* renamed from: c  reason: collision with other method in class */
        public String m211c() {
            return this.f253c;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m212c() {
            return this.f254c;
        }

        public int d() {
            return this.b;
        }

        public a d(int i2) {
            this.j = true;
            this.d = i2;
            return this;
        }

        public a d(String str) {
            this.f259f = true;
            this.f255d = str;
            return this;
        }

        /* renamed from: d  reason: collision with other method in class */
        public String m213d() {
            return this.f255d;
        }

        /* renamed from: d  reason: collision with other method in class */
        public boolean m214d() {
            return this.f256d;
        }

        public int e() {
            return this.c;
        }

        public a e(String str) {
            this.g = true;
            this.f257e = str;
            return this;
        }

        /* renamed from: e  reason: collision with other method in class */
        public String m215e() {
            return this.f257e;
        }

        /* renamed from: e  reason: collision with other method in class */
        public boolean m216e() {
            return this.f258e;
        }

        public int f() {
            return this.d;
        }

        public a f(String str) {
            this.k = true;
            this.f = str;
            return this;
        }

        /* renamed from: f  reason: collision with other method in class */
        public String m217f() {
            return this.f;
        }

        /* renamed from: f  reason: collision with other method in class */
        public boolean m218f() {
            return this.f259f;
        }

        public boolean g() {
            return this.g;
        }

        public boolean h() {
            return this.h;
        }

        public boolean i() {
            return this.i;
        }

        public boolean j() {
            return this.j;
        }

        public boolean k() {
            return this.k;
        }
    }

    public static final class b extends e {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private boolean f260a;
        private int b = 0;

        /* renamed from: b  reason: collision with other field name */
        private boolean f261b = false;
        private int c = 0;

        /* renamed from: c  reason: collision with other field name */
        private boolean f262c;
        private int d = -1;

        /* renamed from: d  reason: collision with other field name */
        private boolean f263d;
        private boolean e;

        public static b a(byte[] bArr) {
            return (b) new b().a(bArr);
        }

        public int a() {
            if (this.d < 0) {
                b();
            }
            return this.d;
        }

        public b a(int i) {
            this.f262c = true;
            this.a = i;
            return this;
        }

        public b a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 8) {
                    a(bVar.a());
                } else if (a2 == 24) {
                    a(bVar.b());
                } else if (a2 == 32) {
                    b(bVar.b());
                } else if (a2 == 40) {
                    c(bVar.b());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public b a(boolean z) {
            this.f260a = true;
            this.f261b = z;
            return this;
        }

        public void a(c cVar) {
            if (b()) {
                cVar.a(1, a());
            }
            if (c()) {
                cVar.a(3, c());
            }
            if (d()) {
                cVar.a(4, d());
            }
            if (e()) {
                cVar.a(5, e());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m219a() {
            return this.f261b;
        }

        public int b() {
            int i = 0;
            if (b()) {
                i = 0 + c.a(1, a());
            }
            if (c()) {
                i += c.a(3, c());
            }
            if (d()) {
                i += c.a(4, d());
            }
            if (e()) {
                i += c.a(5, e());
            }
            this.d = i;
            return i;
        }

        public b b(int i) {
            this.f263d = true;
            this.b = i;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m220b() {
            return this.f260a;
        }

        public int c() {
            return this.a;
        }

        public b c(int i) {
            this.e = true;
            this.c = i;
            return this;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m221c() {
            return this.f262c;
        }

        public int d() {
            return this.b;
        }

        /* renamed from: d  reason: collision with other method in class */
        public boolean m222d() {
            return this.f263d;
        }

        public int e() {
            return this.c;
        }

        /* renamed from: e  reason: collision with other method in class */
        public boolean m223e() {
            return this.e;
        }
    }

    public static final class c extends e {
        private int a = -1;

        /* renamed from: a  reason: collision with other field name */
        private String f264a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f265a;
        private String b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f266b;
        private String c = "";

        /* renamed from: c  reason: collision with other field name */
        private boolean f267c;
        private String d = "";

        /* renamed from: d  reason: collision with other field name */
        private boolean f268d;
        private String e = "";

        /* renamed from: e  reason: collision with other field name */
        private boolean f269e;
        private String f = "";

        /* renamed from: f  reason: collision with other field name */
        private boolean f270f;

        public int a() {
            if (this.a < 0) {
                b();
            }
            return this.a;
        }

        public c a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 10) {
                    a(bVar.a());
                } else if (a2 == 18) {
                    b(bVar.a());
                } else if (a2 == 26) {
                    c(bVar.a());
                } else if (a2 == 34) {
                    d(bVar.a());
                } else if (a2 == 42) {
                    e(bVar.a());
                } else if (a2 == 50) {
                    f(bVar.a());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public c a(String str) {
            this.f265a = true;
            this.f264a = str;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m224a() {
            return this.f264a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, a());
            }
            if (b()) {
                cVar.a(2, b());
            }
            if (c()) {
                cVar.a(3, c());
            }
            if (d()) {
                cVar.a(4, d());
            }
            if (e()) {
                cVar.a(5, e());
            }
            if (f()) {
                cVar.a(6, f());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m225a() {
            return this.f265a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, a());
            }
            if (b()) {
                i += c.a(2, b());
            }
            if (c()) {
                i += c.a(3, c());
            }
            if (d()) {
                i += c.a(4, d());
            }
            if (e()) {
                i += c.a(5, e());
            }
            if (f()) {
                i += c.a(6, f());
            }
            this.a = i;
            return i;
        }

        public c b(String str) {
            this.f266b = true;
            this.b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m226b() {
            return this.b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m227b() {
            return this.f266b;
        }

        public c c(String str) {
            this.f267c = true;
            this.c = str;
            return this;
        }

        public String c() {
            return this.c;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m228c() {
            return this.f267c;
        }

        public c d(String str) {
            this.f268d = true;
            this.d = str;
            return this;
        }

        public String d() {
            return this.d;
        }

        /* renamed from: d  reason: collision with other method in class */
        public boolean m229d() {
            return this.f268d;
        }

        public c e(String str) {
            this.f269e = true;
            this.e = str;
            return this;
        }

        public String e() {
            return this.e;
        }

        /* renamed from: e  reason: collision with other method in class */
        public boolean m230e() {
            return this.f269e;
        }

        public c f(String str) {
            this.f270f = true;
            this.f = str;
            return this;
        }

        public String f() {
            return this.f;
        }

        /* renamed from: f  reason: collision with other method in class */
        public boolean m231f() {
            return this.f270f;
        }
    }

    public static final class d extends e {
        private int a = -1;

        /* renamed from: a  reason: collision with other field name */
        private String f271a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f272a;
        private String b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f273b = false;
        private String c = "";

        /* renamed from: c  reason: collision with other field name */
        private boolean f274c;
        private boolean d;
        private boolean e;

        public static d a(byte[] bArr) {
            return (d) new d().a(bArr);
        }

        public int a() {
            if (this.a < 0) {
                b();
            }
            return this.a;
        }

        public d a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 8) {
                    a(bVar.a());
                } else if (a2 == 18) {
                    a(bVar.a());
                } else if (a2 == 26) {
                    b(bVar.a());
                } else if (a2 == 34) {
                    c(bVar.a());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public d a(String str) {
            this.f274c = true;
            this.f271a = str;
            return this;
        }

        public d a(boolean z) {
            this.f272a = true;
            this.f273b = z;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m232a() {
            return this.f271a;
        }

        public void a(c cVar) {
            if (b()) {
                cVar.a(1, a());
            }
            if (c()) {
                cVar.a(2, a());
            }
            if (d()) {
                cVar.a(3, b());
            }
            if (e()) {
                cVar.a(4, c());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m233a() {
            return this.f273b;
        }

        public int b() {
            int i = 0;
            if (b()) {
                i = 0 + c.a(1, a());
            }
            if (c()) {
                i += c.a(2, a());
            }
            if (d()) {
                i += c.a(3, b());
            }
            if (e()) {
                i += c.a(4, c());
            }
            this.a = i;
            return i;
        }

        public d b(String str) {
            this.d = true;
            this.b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m234b() {
            return this.b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m235b() {
            return this.f272a;
        }

        public d c(String str) {
            this.e = true;
            this.c = str;
            return this;
        }

        public String c() {
            return this.c;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m236c() {
            return this.f274c;
        }

        public boolean d() {
            return this.d;
        }

        public boolean e() {
            return this.e;
        }
    }

    public static final class e extends e {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private b f275a = null;

        /* renamed from: a  reason: collision with other field name */
        private String f276a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f277a;
        private int b = 0;

        /* renamed from: b  reason: collision with other field name */
        private String f278b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f279b;
        private int c = 0;

        /* renamed from: c  reason: collision with other field name */
        private String f280c = "";

        /* renamed from: c  reason: collision with other field name */
        private boolean f281c;
        private int d = -1;

        /* renamed from: d  reason: collision with other field name */
        private String f282d = "";

        /* renamed from: d  reason: collision with other field name */
        private boolean f283d;
        private String e = "";

        /* renamed from: e  reason: collision with other field name */
        private boolean f284e;
        private String f = "";

        /* renamed from: f  reason: collision with other field name */
        private boolean f285f;
        private boolean g;
        private boolean h;
        private boolean i;
        private boolean j;

        public int a() {
            if (this.d < 0) {
                b();
            }
            return this.d;
        }

        /* renamed from: a  reason: collision with other method in class */
        public b m237a() {
            return this.f275a;
        }

        public e a(int i2) {
            this.f277a = true;
            this.a = i2;
            return this;
        }

        public e a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                switch (a2) {
                    case 0:
                        return this;
                    case 8:
                        a(bVar.c());
                        break;
                    case 18:
                        a(bVar.a());
                        break;
                    case 26:
                        b(bVar.a());
                        break;
                    case 34:
                        c(bVar.a());
                        break;
                    case 40:
                        b(bVar.b());
                        break;
                    case 50:
                        d(bVar.a());
                        break;
                    case 58:
                        e(bVar.a());
                        break;
                    case 66:
                        f(bVar.a());
                        break;
                    case 74:
                        b bVar2 = new b();
                        bVar.a((e) bVar2);
                        a(bVar2);
                        break;
                    case 80:
                        c(bVar.b());
                        break;
                    default:
                        if (a(bVar, a2)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public e a(b bVar) {
            if (bVar != null) {
                this.i = true;
                this.f275a = bVar;
                return this;
            }
            throw new NullPointerException();
        }

        public e a(String str) {
            this.f279b = true;
            this.f276a = str;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m238a() {
            return this.f276a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.b(1, c());
            }
            if (b()) {
                cVar.a(2, a());
            }
            if (c()) {
                cVar.a(3, b());
            }
            if (d()) {
                cVar.a(4, c());
            }
            if (e()) {
                cVar.a(5, d());
            }
            if (f()) {
                cVar.a(6, d());
            }
            if (g()) {
                cVar.a(7, e());
            }
            if (h()) {
                cVar.a(8, f());
            }
            if (i()) {
                cVar.a(9, (e) a());
            }
            if (j()) {
                cVar.a(10, e());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m239a() {
            return this.f277a;
        }

        public int b() {
            int i2 = 0;
            if (a()) {
                i2 = 0 + c.b(1, c());
            }
            if (b()) {
                i2 += c.a(2, a());
            }
            if (c()) {
                i2 += c.a(3, b());
            }
            if (d()) {
                i2 += c.a(4, c());
            }
            if (e()) {
                i2 += c.a(5, d());
            }
            if (f()) {
                i2 += c.a(6, d());
            }
            if (g()) {
                i2 += c.a(7, e());
            }
            if (h()) {
                i2 += c.a(8, f());
            }
            if (i()) {
                i2 += c.a(9, (e) a());
            }
            if (j()) {
                i2 += c.a(10, e());
            }
            this.d = i2;
            return i2;
        }

        public e b(int i2) {
            this.f284e = true;
            this.b = i2;
            return this;
        }

        public e b(String str) {
            this.f281c = true;
            this.f278b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m240b() {
            return this.f278b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m241b() {
            return this.f279b;
        }

        public int c() {
            return this.a;
        }

        public e c(int i2) {
            this.j = true;
            this.c = i2;
            return this;
        }

        public e c(String str) {
            this.f283d = true;
            this.f280c = str;
            return this;
        }

        /* renamed from: c  reason: collision with other method in class */
        public String m242c() {
            return this.f280c;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m243c() {
            return this.f281c;
        }

        public int d() {
            return this.b;
        }

        public e d(String str) {
            this.f285f = true;
            this.f282d = str;
            return this;
        }

        /* renamed from: d  reason: collision with other method in class */
        public String m244d() {
            return this.f282d;
        }

        /* renamed from: d  reason: collision with other method in class */
        public boolean m245d() {
            return this.f283d;
        }

        public int e() {
            return this.c;
        }

        public e e(String str) {
            this.g = true;
            this.e = str;
            return this;
        }

        /* renamed from: e  reason: collision with other method in class */
        public String m246e() {
            return this.e;
        }

        /* renamed from: e  reason: collision with other method in class */
        public boolean m247e() {
            return this.f284e;
        }

        public e f(String str) {
            this.h = true;
            this.f = str;
            return this;
        }

        public String f() {
            return this.f;
        }

        /* renamed from: f  reason: collision with other method in class */
        public boolean m248f() {
            return this.f285f;
        }

        public boolean g() {
            return this.g;
        }

        public boolean h() {
            return this.h;
        }

        public boolean i() {
            return this.i;
        }

        public boolean j() {
            return this.j;
        }
    }

    public static final class f extends e {
        private int a = -1;

        /* renamed from: a  reason: collision with other field name */
        private b f286a = null;

        /* renamed from: a  reason: collision with other field name */
        private String f287a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f288a;
        private String b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f289b;
        private boolean c;

        public static f a(byte[] bArr) {
            return (f) new f().a(bArr);
        }

        public int a() {
            if (this.a < 0) {
                b();
            }
            return this.a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public b m249a() {
            return this.f286a;
        }

        public f a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 10) {
                    a(bVar.a());
                } else if (a2 == 18) {
                    b(bVar.a());
                } else if (a2 == 26) {
                    b bVar2 = new b();
                    bVar.a((e) bVar2);
                    a(bVar2);
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public f a(b bVar) {
            if (bVar != null) {
                this.c = true;
                this.f286a = bVar;
                return this;
            }
            throw new NullPointerException();
        }

        public f a(String str) {
            this.f288a = true;
            this.f287a = str;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m250a() {
            return this.f287a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, a());
            }
            if (b()) {
                cVar.a(2, b());
            }
            if (c()) {
                cVar.a(3, (e) a());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m251a() {
            return this.f288a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, a());
            }
            if (b()) {
                i += c.a(2, b());
            }
            if (c()) {
                i += c.a(3, (e) a());
            }
            this.a = i;
            return i;
        }

        public f b(String str) {
            this.f289b = true;
            this.b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m252b() {
            return this.b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m253b() {
            return this.f289b;
        }

        public boolean c() {
            return this.c;
        }
    }

    public static final class g extends e {
        private int a = -1;

        /* renamed from: a  reason: collision with other field name */
        private String f290a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f291a;
        private String b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f292b;
        private String c = "";

        /* renamed from: c  reason: collision with other field name */
        private boolean f293c;

        public static g a(byte[] bArr) {
            return (g) new g().a(bArr);
        }

        public int a() {
            if (this.a < 0) {
                b();
            }
            return this.a;
        }

        public g a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 10) {
                    a(bVar.a());
                } else if (a2 == 18) {
                    b(bVar.a());
                } else if (a2 == 26) {
                    c(bVar.a());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public g a(String str) {
            this.f291a = true;
            this.f290a = str;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m254a() {
            return this.f290a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, a());
            }
            if (b()) {
                cVar.a(2, b());
            }
            if (c()) {
                cVar.a(3, c());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m255a() {
            return this.f291a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, a());
            }
            if (b()) {
                i += c.a(2, b());
            }
            if (c()) {
                i += c.a(3, c());
            }
            this.a = i;
            return i;
        }

        public g b(String str) {
            this.f292b = true;
            this.b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m256b() {
            return this.b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m257b() {
            return this.f292b;
        }

        public g c(String str) {
            this.f293c = true;
            this.c = str;
            return this;
        }

        public String c() {
            return this.c;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m258c() {
            return this.f293c;
        }
    }

    public static final class h extends e {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private String f294a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f295a;
        private int b = -1;

        /* renamed from: b  reason: collision with other field name */
        private boolean f296b;

        public static h a(byte[] bArr) {
            return (h) new h().a(bArr);
        }

        public int a() {
            if (this.b < 0) {
                b();
            }
            return this.b;
        }

        public h a(int i) {
            this.f295a = true;
            this.a = i;
            return this;
        }

        public h a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 8) {
                    a(bVar.b());
                } else if (a2 == 18) {
                    a(bVar.a());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public h a(String str) {
            this.f296b = true;
            this.f294a = str;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m259a() {
            return this.f294a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, c());
            }
            if (b()) {
                cVar.a(2, a());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m260a() {
            return this.f295a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, c());
            }
            if (b()) {
                i += c.a(2, a());
            }
            this.b = i;
            return i;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m261b() {
            return this.f296b;
        }

        public int c() {
            return this.a;
        }
    }

    public static final class i extends e {
        private int a = -1;

        /* renamed from: a  reason: collision with other field name */
        private a f297a = a.a;

        /* renamed from: a  reason: collision with other field name */
        private boolean f298a;

        public static i a(byte[] bArr) {
            return (i) new i().a(bArr);
        }

        public int a() {
            if (this.a < 0) {
                b();
            }
            return this.a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public a m262a() {
            return this.f297a;
        }

        public i a(a aVar) {
            this.f298a = true;
            this.f297a = aVar;
            return this;
        }

        public i a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 10) {
                    a(bVar.a());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, a());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m263a() {
            return this.f298a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, a());
            }
            this.a = i;
            return i;
        }
    }

    public static final class j extends e {
        private int a = -1;

        /* renamed from: a  reason: collision with other field name */
        private a f299a = a.a;

        /* renamed from: a  reason: collision with other field name */
        private b f300a = null;

        /* renamed from: a  reason: collision with other field name */
        private boolean f301a;
        private boolean b;

        public static j a(byte[] bArr) {
            return (j) new j().a(bArr);
        }

        public int a() {
            if (this.a < 0) {
                b();
            }
            return this.a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public a m264a() {
            return this.f299a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public b m265a() {
            return this.f300a;
        }

        public j a(a aVar) {
            this.f301a = true;
            this.f299a = aVar;
            return this;
        }

        public j a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 10) {
                    a(bVar.a());
                } else if (a2 == 18) {
                    b bVar2 = new b();
                    bVar.a((e) bVar2);
                    a(bVar2);
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public j a(b bVar) {
            if (bVar != null) {
                this.b = true;
                this.f300a = bVar;
                return this;
            }
            throw new NullPointerException();
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, a());
            }
            if (b()) {
                cVar.a(2, (e) a());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m266a() {
            return this.f301a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, a());
            }
            if (b()) {
                i += c.a(2, (e) a());
            }
            this.a = i;
            return i;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m267b() {
            return this.b;
        }
    }

    public static final class k extends e {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private long f302a = 0;

        /* renamed from: a  reason: collision with other field name */
        private String f303a = "";

        /* renamed from: a  reason: collision with other field name */
        private boolean f304a;
        private int b = -1;

        /* renamed from: b  reason: collision with other field name */
        private long f305b = 0;

        /* renamed from: b  reason: collision with other field name */
        private String f306b = "";

        /* renamed from: b  reason: collision with other field name */
        private boolean f307b;
        private boolean c;
        private boolean d;
        private boolean e;
        private boolean f = false;
        private boolean g;

        public static k a(byte[] bArr) {
            return (k) new k().a(bArr);
        }

        public int a() {
            if (this.b < 0) {
                b();
            }
            return this.b;
        }

        /* renamed from: a  reason: collision with other method in class */
        public long m268a() {
            return this.f302a;
        }

        public k a(int i) {
            this.g = true;
            this.a = i;
            return this;
        }

        public k a(long j) {
            this.c = true;
            this.f302a = j;
            return this;
        }

        public k a(b bVar) {
            while (true) {
                int a2 = bVar.a();
                if (a2 == 0) {
                    return this;
                }
                if (a2 == 10) {
                    a(bVar.a());
                } else if (a2 == 18) {
                    b(bVar.a());
                } else if (a2 == 24) {
                    a(bVar.a());
                } else if (a2 == 32) {
                    b(bVar.a());
                } else if (a2 == 40) {
                    a(bVar.a());
                } else if (a2 == 48) {
                    a(bVar.b());
                } else if (!a(bVar, a2)) {
                    return this;
                }
            }
        }

        public k a(String str) {
            this.f304a = true;
            this.f303a = str;
            return this;
        }

        public k a(boolean z) {
            this.e = true;
            this.f = z;
            return this;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m269a() {
            return this.f303a;
        }

        public void a(c cVar) {
            if (a()) {
                cVar.a(1, a());
            }
            if (b()) {
                cVar.a(2, b());
            }
            if (c()) {
                cVar.a(3, a());
            }
            if (d()) {
                cVar.a(4, b());
            }
            if (f()) {
                cVar.a(5, e());
            }
            if (g()) {
                cVar.a(6, c());
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m270a() {
            return this.f304a;
        }

        public int b() {
            int i = 0;
            if (a()) {
                i = 0 + c.a(1, a());
            }
            if (b()) {
                i += c.a(2, b());
            }
            if (c()) {
                i += c.a(3, a());
            }
            if (d()) {
                i += c.a(4, b());
            }
            if (f()) {
                i += c.a(5, e());
            }
            if (g()) {
                i += c.a(6, c());
            }
            this.b = i;
            return i;
        }

        /* renamed from: b  reason: collision with other method in class */
        public long m271b() {
            return this.f305b;
        }

        public k b(long j) {
            this.d = true;
            this.f305b = j;
            return this;
        }

        public k b(String str) {
            this.f307b = true;
            this.f306b = str;
            return this;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m272b() {
            return this.f306b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public boolean m273b() {
            return this.f307b;
        }

        public int c() {
            return this.a;
        }

        /* renamed from: c  reason: collision with other method in class */
        public boolean m274c() {
            return this.c;
        }

        public boolean d() {
            return this.d;
        }

        public boolean e() {
            return this.f;
        }

        public boolean f() {
            return this.e;
        }

        public boolean g() {
            return this.g;
        }
    }
}
