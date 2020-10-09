package com.xiaomi.push;

import com.xiaomi.push.dd;
import java.io.File;
import java.util.Date;

class de extends dd.b {
    final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ dd f224a;

    /* renamed from: a  reason: collision with other field name */
    File f225a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f226a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Date f227a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ boolean f228a;
    final /* synthetic */ String b;

    /* renamed from: b  reason: collision with other field name */
    final /* synthetic */ Date f229b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    de(dd ddVar, int i, Date date, Date date2, String str, String str2, boolean z) {
        super();
        this.f224a = ddVar;
        this.a = i;
        this.f227a = date;
        this.f229b = date2;
        this.f226a = str;
        this.b = str2;
        this.f228a = z;
    }

    public void b() {
        if (aa.d()) {
            try {
                File file = new File(dd.a(this.f224a).getExternalFilesDir((String) null) + "/.logcache");
                file.mkdirs();
                if (file.isDirectory()) {
                    dc dcVar = new dc();
                    dcVar.a(this.a);
                    this.f225a = dcVar.a(dd.a(this.f224a), this.f227a, this.f229b, file);
                }
            } catch (NullPointerException unused) {
            }
        }
    }

    public void c() {
        if (this.f225a != null && this.f225a.exists()) {
            dd.a(this.f224a).add(new dd.c(this.f226a, this.b, this.f225a, this.f228a));
        }
        this.f224a.a(0);
    }
}
