package com.uploader.implement.a;

import android.content.Context;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.BuildConfig;
import com.uploader.implement.d.b;
import java.lang.ref.WeakReference;
import org.android.agoo.message.MessageService;

/* compiled from: AbstractUploaderAction */
public abstract class a implements g, com.uploader.implement.d.a {
    final int a;
    final Context b;
    WeakReference<d> c;
    private volatile int d = 0;

    /* access modifiers changed from: package-private */
    public abstract Pair<Integer, Integer> a(@NonNull b bVar, @NonNull e eVar);

    /* access modifiers changed from: package-private */
    @NonNull
    public abstract Pair<com.uploader.implement.c.a, ? extends Object> a(@NonNull b bVar, e eVar, @NonNull com.uploader.implement.a.b.a aVar);

    /* access modifiers changed from: package-private */
    public abstract com.uploader.implement.c.a a(@NonNull b bVar, e eVar, Pair<Integer, Integer> pair);

    /* access modifiers changed from: package-private */
    public abstract com.uploader.implement.c.a a(@NonNull b bVar, e eVar, com.uploader.implement.c.a aVar);

    /* access modifiers changed from: package-private */
    public abstract com.uploader.implement.c.a a(@NonNull b bVar, e eVar, boolean z);

    /* access modifiers changed from: package-private */
    public abstract void a();

    /* access modifiers changed from: package-private */
    public abstract void a(int i, Object obj);

    /* access modifiers changed from: package-private */
    public abstract boolean a(b bVar);

    /* access modifiers changed from: package-private */
    public abstract boolean b();

    a(Context context) {
        this.b = context;
        this.a = hashCode();
    }

    public final void a(d dVar) {
        this.c = new WeakReference<>(dVar);
    }

    private d e() {
        if (this.c == null) {
            return null;
        }
        return (d) this.c.get();
    }

    public final int c() {
        return this.d;
    }

    /* access modifiers changed from: package-private */
    public final void a(b bVar, com.uploader.implement.c.a aVar) {
        if (this.d == 3) {
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " begin, state is finish");
            }
        } else if (aVar != null) {
            a(bVar, aVar, 1);
        } else {
            d(bVar);
        }
    }

    private void d(b bVar) {
        boolean b2 = b();
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " begin, session:" + bVar.hashCode() + " state:" + this.d + " stepUp:" + b2);
        }
        if (b2) {
            a(2);
        }
        com.uploader.implement.c.a a2 = a(bVar, (e) null, true);
        if (a2 != null) {
            a(bVar, a2, 1);
        }
    }

    public final void b(@NonNull b bVar) {
        int i = this.d == 0 ? 5 : 6;
        if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " onStart, state:" + this.d + " notifyType:" + i + " session:" + bVar.hashCode());
        }
        switch (this.d) {
            case 0:
            case 1:
            case 4:
                if (!a(1)) {
                    a();
                    bVar.a((com.uploader.implement.d.a) null);
                    bVar.a();
                }
                bVar.a((com.uploader.implement.d.a) this);
                if (!a(bVar)) {
                    d(bVar);
                }
                if (com.uploader.implement.a.a(2)) {
                    com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " submit timeConsuming, session:" + bVar.hashCode() + " state:" + this.d);
                    break;
                }
                break;
            case 2:
            case 5:
                if (!a(2)) {
                    a();
                    bVar.a((com.uploader.implement.d.a) null);
                    bVar.a();
                }
                bVar.a((com.uploader.implement.d.a) this);
                com.uploader.implement.c.a a2 = a(bVar, (e) null, true);
                if (a2 != null) {
                    a(bVar, a2, 1);
                    break;
                }
                break;
            default:
                if (com.uploader.implement.a.a(8)) {
                    com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " no need to begin, " + " state:" + this.d);
                    break;
                }
                break;
        }
        a(i, (Object) null);
    }

    public final void c(@Nullable b bVar) {
        if (this.d != 3) {
            a(bVar, (com.uploader.implement.c.a) null, 0);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " onCancel, state is finish");
        }
    }

    public final void a(b bVar, e eVar, f fVar) {
        com.uploader.implement.a.b.a aVar = (com.uploader.implement.a.b.a) fVar;
        int a2 = aVar.a();
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " onReceive, session:" + bVar.hashCode() + " request:" + eVar.hashCode() + " response:" + aVar.hashCode() + " state:" + this.d + " type:" + a2 + " content:" + aVar.b().toString());
        }
        if (this.d != 3) {
            Pair<com.uploader.implement.c.a, ? extends Object> a3 = a(bVar, eVar, aVar);
            com.uploader.implement.c.a aVar2 = (com.uploader.implement.c.a) a3.first;
            switch (a2) {
                case 1:
                    if (aVar2 == null) {
                        if (!b()) {
                            a(3);
                            bVar.a(eVar, true);
                            d e = e();
                            if (e != null) {
                                e.a(this);
                                break;
                            }
                        } else {
                            a(2);
                            aVar2 = a(bVar, eVar, true);
                            break;
                        }
                    }
                    break;
                case 2:
                    if (a3.second != null) {
                        a(3, a3.second);
                        break;
                    }
                    break;
                case 3:
                    if (a3.second != null) {
                        aVar2 = a(bVar, eVar, (Pair<Integer, Integer>) (Pair) a3.second);
                        break;
                    }
                    break;
                case 4:
                    a(3);
                    bVar.a((com.uploader.implement.d.a) null);
                    bVar.a(eVar, true);
                    a(0, a3.second);
                    d e2 = e();
                    if (e2 != null) {
                        e2.a(this);
                        break;
                    }
                    break;
                case 5:
                    if (aVar2 != null && "300".equals(aVar2.code) && "2".equals(aVar2.subcode)) {
                        a(1);
                        aVar2 = a(bVar, (e) null, true);
                        break;
                    }
                case 6:
                    aVar2 = new com.uploader.implement.c.a("300", "3", a3.second == null ? "" : a3.second.toString(), true);
                    break;
            }
            c(bVar, eVar, aVar2);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " onReceive, state is finish");
        }
    }

    private boolean a(int i) {
        if (this.d == i) {
            return false;
        }
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " setState, oldState:" + this.d + " state:" + i);
        }
        this.d = i;
        return true;
    }

    public final void b(b bVar, e eVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " onSend, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
        }
        if (this.d != 3) {
            Pair<Integer, Integer> a2 = a(bVar, eVar);
            if (a2 != null) {
                c(bVar, eVar, a(bVar, eVar, a2));
            }
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " onSend, state is finish");
        }
    }

    private void a(@Nullable b bVar, com.uploader.implement.c.a aVar, int i) {
        int i2 = this.d;
        if (com.uploader.implement.a.a(8)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.a);
            sb.append(" stop, session:");
            sb.append(bVar == null ? BuildConfig.buildJavascriptFrameworkVersion : Integer.valueOf(bVar.hashCode()));
            sb.append(" oldState:");
            sb.append(i2);
            sb.append(" error:");
            sb.append(aVar);
            sb.append(" reason:");
            sb.append(i);
            com.uploader.implement.a.a(8, "AbstractUploaderAction", sb.toString());
        }
        if (bVar != null) {
            a();
            bVar.a((com.uploader.implement.d.a) null);
            bVar.a();
        }
        int i3 = 4;
        if (i == 2) {
            a(this.d == 2 ? 5 : 4);
        } else {
            a(3);
            d e = e();
            if (e != null) {
                e.a(this);
            }
            i3 = i == 0 ? 1 : 2;
        }
        a(i3, (Object) aVar);
    }

    public final void b(b bVar, e eVar, com.uploader.implement.c.a aVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " onError, session:" + bVar.hashCode() + " request:" + eVar.hashCode() + " error:" + aVar.toString());
        }
        if (this.d != 3) {
            if (MessageService.MSG_DB_COMPLETE.equals(aVar.code)) {
                boolean a2 = com.uploader.implement.e.a.a(this.b);
                if (com.uploader.implement.a.a(2)) {
                    com.uploader.implement.a.a(2, "AbstractUploaderAction", this.a + " onError, connection error, isConnected:" + a2 + " error:" + aVar.toString());
                }
                if (!a2 || "-1".equals(aVar.subcode)) {
                    a(bVar, aVar, 2);
                    return;
                }
            }
            c(bVar, eVar, aVar);
        } else if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "AbstractUploaderAction", this.a + " onError, state is finish");
        }
    }

    public void d() {
        a(7, (Object) null);
    }

    private void c(b bVar, e eVar, com.uploader.implement.c.a aVar) {
        if (aVar != null) {
            if (!aVar.a) {
                a(bVar, aVar, 1);
                return;
            }
            com.uploader.implement.c.a a2 = a(bVar, eVar, aVar);
            if (a2 != null) {
                a(bVar, a2, 1);
            }
        }
    }
}
