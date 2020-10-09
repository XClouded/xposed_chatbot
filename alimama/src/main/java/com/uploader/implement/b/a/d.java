package com.uploader.implement.b.a;

import com.uploader.implement.b.a.c;
import com.uploader.implement.b.b;
import com.uploader.implement.b.e;
import com.uploader.implement.b.f;
import com.uploader.implement.c;
import java.lang.ref.WeakReference;
import org.android.agoo.message.MessageService;

/* compiled from: LongLivedConnection */
public class d extends a {
    final c e;

    d(c cVar, f fVar) {
        super(cVar, fVar);
        this.e = new c(cVar, fVar);
        this.e.a((c.a) new a(this));
    }

    public boolean b() {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "LongLivedConnection", this.c + " connectAsync");
        }
        this.e.a();
        return true;
    }

    public boolean c() {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "LongLivedConnection", this.c + " closeAsync");
        }
        this.e.b();
        return true;
    }

    public void a(f fVar, int i) {
        byte[] bArr;
        int i2 = fVar.d;
        if (fVar.c != 0) {
            bArr = new byte[i2];
            System.arraycopy(fVar.b, fVar.c, bArr, 0, i2);
        } else {
            bArr = fVar.b;
        }
        b e2 = e();
        if (e2 != null) {
            e2.a((e) this, i);
        }
        this.e.a(i, bArr, i2);
    }

    public boolean d() {
        return this.e.c();
    }

    /* compiled from: LongLivedConnection */
    static class a implements c.a {
        private final WeakReference<d> a;

        a(d dVar) {
            this.a = new WeakReference<>(dVar);
        }

        public void a(int i) {
            b e;
            com.uploader.implement.c.a aVar;
            d dVar = (d) this.a.get();
            if (dVar != null && (e = dVar.e()) != null) {
                if (i == -2032) {
                    String num = Integer.toString(i);
                    aVar = new com.uploader.implement.c.a("300", num, "error=" + i, true);
                } else {
                    String num2 = (i == -2601 || i == -2613 || i == -2413) ? "-1" : Integer.toString(i);
                    aVar = new com.uploader.implement.c.a(MessageService.MSG_DB_COMPLETE, num2, "error=" + i, true);
                }
                e.a((e) dVar, aVar);
            }
        }

        public void a() {
            b e;
            d dVar = (d) this.a.get();
            if (dVar != null && (e = dVar.e()) != null) {
                e.a(dVar);
            }
        }

        public void b(int i) {
            b e;
            d dVar = (d) this.a.get();
            if (dVar != null && (e = dVar.e()) != null) {
                e.b(dVar, i);
            }
        }

        public void a(int i, int i2) {
            b e;
            d dVar = (d) this.a.get();
            if (dVar != null && (e = dVar.e()) != null) {
                String num = Integer.toString(i2);
                e.a((e) dVar, new com.uploader.implement.c.a(MessageService.MSG_DB_COMPLETE, num, "onSendFailed" + i2, false));
            }
        }

        public void a(byte[] bArr, int i) {
            b e;
            d dVar = (d) this.a.get();
            if (dVar != null && (e = dVar.e()) != null) {
                f fVar = new f();
                fVar.b = bArr;
                fVar.c = 0;
                fVar.d = i;
                e.a((e) dVar, fVar);
            }
        }
    }
}
