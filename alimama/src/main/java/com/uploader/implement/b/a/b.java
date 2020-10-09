package com.uploader.implement.b.a;

import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import androidx.annotation.NonNull;
import com.uploader.implement.b.c;
import com.uploader.implement.b.d;
import com.uploader.implement.b.e;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: ConnectionRecycler */
public class b implements c {
    private ArrayList<e> a = new ArrayList<>();
    private ArrayList<C0029b> b = new ArrayList<>();
    private ArrayList<a> c = new ArrayList<>();
    private ArrayList<Pair<a, e>> d = new ArrayList<>();
    private final Handler e;
    private final int f;
    private final com.uploader.implement.c g;

    /* compiled from: ConnectionRecycler */
    private static class a {
        final com.uploader.implement.d.b a;
        final com.uploader.implement.a.e b;
        final d c;

        a(com.uploader.implement.d.b bVar, com.uploader.implement.a.e eVar, d dVar) {
            this.a = bVar;
            this.b = eVar;
            this.c = dVar;
        }
    }

    /* renamed from: com.uploader.implement.b.a.b$b  reason: collision with other inner class name */
    /* compiled from: ConnectionRecycler */
    private static final class C0029b implements Runnable {
        final e a;
        final ArrayList<e> b;
        final ArrayList<C0029b> c;

        C0029b(e eVar, ArrayList<e> arrayList, ArrayList<C0029b> arrayList2) {
            this.a = eVar;
            this.b = arrayList;
            this.c = arrayList2;
        }

        public void run() {
            this.b.remove(this.a);
            this.c.remove(this);
            this.a.c();
        }
    }

    public b(com.uploader.implement.c cVar, Looper looper) {
        this.g = cVar;
        this.e = new Handler(looper);
        this.f = hashCode();
    }

    private static int a(com.uploader.implement.d.b bVar, com.uploader.implement.a.e eVar, ArrayList<Pair<a, e>> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            a aVar = (a) arrayList.get(i).first;
            if (aVar.b.equals(eVar) && aVar.a.equals(bVar)) {
                return i;
            }
        }
        return -1;
    }

    private static int a(com.uploader.implement.b.a aVar, ArrayList<e> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (arrayList.get(i).a().equals(aVar)) {
                return i;
            }
        }
        return -1;
    }

    private static int b(com.uploader.implement.d.b bVar, com.uploader.implement.a.e eVar, ArrayList<a> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            a aVar = arrayList.get(i);
            if (aVar.b.equals(eVar) && aVar.a.equals(bVar)) {
                return i;
            }
        }
        return -1;
    }

    private static int b(com.uploader.implement.b.a aVar, ArrayList<a> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (arrayList.get(i).b.a().equals(aVar)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean c(com.uploader.implement.b.a aVar, ArrayList<Pair<a, e>> arrayList) {
        int size = arrayList.size();
        int i = 2;
        for (int i2 = 0; i2 < size; i2++) {
            if (((a) arrayList.get(i2).first).b.a().equals(aVar) && i - 1 == 0) {
                return false;
            }
        }
        return true;
    }

    private static int a(e eVar, ArrayList<C0029b> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (arrayList.get(i).a.equals(eVar)) {
                return i;
            }
        }
        return -1;
    }

    public boolean a(@NonNull com.uploader.implement.d.b bVar, @NonNull com.uploader.implement.a.e eVar, @NonNull com.uploader.implement.a.e eVar2, @NonNull d dVar, boolean z) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " replace start, session:" + bVar.hashCode() + " request:" + eVar.hashCode() + " newRequest:" + eVar2.hashCode() + " keepAlive:" + z);
        }
        com.uploader.implement.b.a a2 = eVar.a();
        if (!a2.equals(eVar2.a())) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " replace:failure, false !=, request:" + eVar.hashCode() + " newRequest:" + eVar2.hashCode());
            }
            return false;
        } else if (!a2.e) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " replace:false, !isLongLived");
            }
            return false;
        } else {
            int b2 = b(bVar, eVar, this.c);
            if (b2 != -1) {
                this.c.set(b2, new a(bVar, eVar2, dVar));
                if (com.uploader.implement.a.a(2)) {
                    com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " replace, waiting, request:" + eVar.hashCode() + " newRequest:" + eVar2.hashCode());
                }
                return true;
            }
            int a3 = a(bVar, eVar, this.d);
            if (a3 == -1) {
                if (com.uploader.implement.a.a(2)) {
                    com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " replace failure, !bounding, request:" + eVar.hashCode());
                }
                return false;
            }
            e a4 = a(a2, (e) this.d.get(a3).second, z);
            this.d.set(a3, new Pair(new a(bVar, eVar2, dVar), a4));
            dVar.a(bVar, eVar2, a4);
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "ConnectionRecycler", this.f + " replace, bounding, request:" + eVar.hashCode() + " newRequest:" + eVar2.hashCode() + " available connection:" + a4.hashCode());
            }
            return true;
        }
    }

    public boolean a(@NonNull com.uploader.implement.d.b bVar, @NonNull com.uploader.implement.a.e eVar, @NonNull d dVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " register start, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
        }
        com.uploader.implement.b.a a2 = eVar.a();
        if (!a2.e) {
            e a3 = a2.a(this.g);
            dVar.a(bVar, eVar, a3);
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " register, onAvailable short lived connection:" + a3.hashCode() + " request:" + eVar.hashCode());
            }
            return true;
        }
        if (a(bVar, eVar, this.d) != -1 && com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "ConnectionRecycler", this.f + " register, is bounding, request:" + eVar.hashCode());
        }
        if (b(bVar, eVar, this.c) != -1 && com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "ConnectionRecycler", this.f + " register, is waiting, request:" + eVar.hashCode());
        }
        a aVar = new a(bVar, eVar, dVar);
        if (3 <= this.d.size() || !c(a2, this.d)) {
            this.c.add(aVar);
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " register, waiting request:" + eVar.hashCode());
            }
            return true;
        }
        e a4 = a(a2, (e) null, true);
        this.d.add(new Pair(aVar, a4));
        dVar.a(bVar, eVar, a4);
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " register, onAvailable long lived connection:" + a4.hashCode() + " request:" + eVar.hashCode());
        }
        return true;
    }

    public boolean a(@NonNull com.uploader.implement.d.b bVar, @NonNull com.uploader.implement.a.e eVar, boolean z) {
        com.uploader.implement.b.a a2 = eVar.a();
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " unregister start, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
        }
        if (!a2.e) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " unregister, !isLongLived, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
            }
            return false;
        }
        int b2 = b(bVar, eVar, this.c);
        if (b2 != -1) {
            this.c.remove(b2);
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " unregister, waiting, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
            }
            return true;
        }
        int a3 = a(bVar, eVar, this.d);
        if (a3 == -1) {
            return false;
        }
        Pair remove = this.d.remove(a3);
        e a4 = a(((a) remove.first).b.a(), (e) remove.second, z);
        a(a4);
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " unregister, session:" + bVar.hashCode() + " request:" + eVar.hashCode() + " connection:" + a4.hashCode());
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000b, code lost:
        r5 = r3.a.remove(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.uploader.implement.b.e a(com.uploader.implement.b.a r4, com.uploader.implement.b.e r5, boolean r6) {
        /*
            r3 = this;
            if (r5 != 0) goto L_0x0028
            java.util.ArrayList<com.uploader.implement.b.e> r0 = r3.a
            int r0 = a((com.uploader.implement.b.a) r4, (java.util.ArrayList<com.uploader.implement.b.e>) r0)
            r1 = -1
            if (r0 == r1) goto L_0x0028
            java.util.ArrayList<com.uploader.implement.b.e> r5 = r3.a
            java.lang.Object r5 = r5.remove(r0)
            com.uploader.implement.b.e r5 = (com.uploader.implement.b.e) r5
            java.util.ArrayList<com.uploader.implement.b.a.b$b> r0 = r3.b
            int r0 = a((com.uploader.implement.b.e) r5, (java.util.ArrayList<com.uploader.implement.b.a.b.C0029b>) r0)
            if (r0 == r1) goto L_0x0028
            android.os.Handler r1 = r3.e
            java.util.ArrayList<com.uploader.implement.b.a.b$b> r2 = r3.b
            java.lang.Object r0 = r2.remove(r0)
            java.lang.Runnable r0 = (java.lang.Runnable) r0
            r1.removeCallbacks(r0)
        L_0x0028:
            if (r5 == 0) goto L_0x0040
            r0 = 0
            r5.a(r0)
            if (r6 != 0) goto L_0x0033
            r5.c()
        L_0x0033:
            boolean r0 = r5.d()
            if (r0 == 0) goto L_0x0046
            com.uploader.implement.c r5 = r3.g
            com.uploader.implement.b.e r5 = r4.a(r5)
            goto L_0x0046
        L_0x0040:
            com.uploader.implement.c r5 = r3.g
            com.uploader.implement.b.e r5 = r4.a(r5)
        L_0x0046:
            r4 = 2
            boolean r0 = com.uploader.implement.a.a((int) r4)
            if (r0 == 0) goto L_0x0074
            java.lang.String r0 = "ConnectionRecycler"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r2 = r3.f
            r1.append(r2)
            java.lang.String r2 = " retrieve, connection:"
            r1.append(r2)
            int r2 = r5.hashCode()
            r1.append(r2)
            java.lang.String r2 = " keepAlive:"
            r1.append(r2)
            r1.append(r6)
            java.lang.String r6 = r1.toString()
            com.uploader.implement.a.a(r4, r0, r6)
        L_0x0074:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.b.a.b.a(com.uploader.implement.b.a, com.uploader.implement.b.e, boolean):com.uploader.implement.b.e");
    }

    private void a(e eVar) {
        int b2 = b(eVar.a(), this.c);
        if (b2 != -1) {
            a remove = this.c.remove(b2);
            this.d.add(new Pair(remove, eVar));
            remove.c.a(remove.a, remove.b, eVar);
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " rebind, onAvailable:" + remove.a.hashCode() + " request:" + remove.b.hashCode());
                return;
            }
            return;
        }
        this.a.add(eVar);
        C0029b bVar = new C0029b(eVar, this.a, this.b);
        this.e.postDelayed(bVar, 27000);
        this.b.add(bVar);
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " rebind, start timeout connection:" + eVar.hashCode());
        }
    }

    public boolean a(@NonNull com.uploader.implement.d.b bVar) {
        boolean z = false;
        for (int size = this.c.size() - 1; size >= 0; size--) {
            if (this.c.get(size).a.equals(bVar)) {
                this.c.remove(size);
                z = true;
            }
        }
        ArrayList arrayList = new ArrayList();
        for (int size2 = this.d.size() - 1; size2 >= 0; size2--) {
            Pair pair = this.d.get(size2);
            if (((a) pair.first).a.equals(bVar)) {
                this.d.remove(size2);
                arrayList.add(a(((a) pair.first).b.a(), (e) pair.second, false));
                z = true;
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            a((e) it.next());
        }
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " unregister, session:" + bVar.hashCode() + " removed:" + z);
        }
        return z;
    }

    public void a() {
        this.c.clear();
        for (int size = this.b.size() - 1; size >= 0; size--) {
            this.e.removeCallbacks(this.b.get(size));
        }
        this.b.clear();
        int size2 = this.d.size();
        while (true) {
            size2--;
            if (size2 < 0) {
                break;
            }
            e eVar = (e) this.d.get(size2).second;
            eVar.a((com.uploader.implement.b.b) null);
            eVar.c();
        }
        this.d.clear();
        for (int size3 = this.a.size() - 1; size3 >= 0; size3--) {
            e eVar2 = this.a.get(size3);
            eVar2.a((com.uploader.implement.b.b) null);
            eVar2.c();
        }
        this.a.clear();
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "ConnectionRecycler", this.f + " reset");
        }
    }
}
