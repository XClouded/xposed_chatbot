package com.xiaomi.push;

public class jf {
    private static int a = Integer.MAX_VALUE;

    public static void a(jc jcVar, byte b) {
        a(jcVar, b, a);
    }

    public static void a(jc jcVar, byte b, int i) {
        if (i > 0) {
            int i2 = 0;
            switch (b) {
                case 2:
                    jcVar.a();
                    return;
                case 3:
                    jcVar.a();
                    return;
                case 4:
                    jcVar.a();
                    return;
                case 6:
                    jcVar.a();
                    return;
                case 8:
                    jcVar.a();
                    return;
                case 10:
                    jcVar.a();
                    return;
                case 11:
                    jcVar.a();
                    return;
                case 12:
                    jcVar.a();
                    while (true) {
                        iz a2 = jcVar.a();
                        if (a2.a == 0) {
                            jcVar.f();
                            return;
                        } else {
                            a(jcVar, a2.a, i - 1);
                            jcVar.g();
                        }
                    }
                case 13:
                    jb a3 = jcVar.a();
                    while (i2 < a3.f788a) {
                        int i3 = i - 1;
                        a(jcVar, a3.a, i3);
                        a(jcVar, a3.b, i3);
                        i2++;
                    }
                    jcVar.h();
                    return;
                case 14:
                    jg a4 = jcVar.a();
                    while (i2 < a4.f789a) {
                        a(jcVar, a4.a, i - 1);
                        i2++;
                    }
                    jcVar.j();
                    return;
                case 15:
                    ja a5 = jcVar.a();
                    while (i2 < a5.f787a) {
                        a(jcVar, a5.a, i - 1);
                        i2++;
                    }
                    jcVar.i();
                    return;
                default:
                    return;
            }
        } else {
            throw new iw("Maximum skip depth exceeded");
        }
    }
}
