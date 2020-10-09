package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ao {
    private static volatile ao a;

    /* renamed from: a  reason: collision with other field name */
    private Context f32a;

    /* renamed from: a  reason: collision with other field name */
    private List<ab> f33a = new ArrayList();

    private ao(Context context) {
        this.f32a = context.getApplicationContext();
        if (this.f32a == null) {
            this.f32a = context;
        }
    }

    public static ao a(Context context) {
        if (a == null) {
            synchronized (ao.class) {
                if (a == null) {
                    a = new ao(context);
                }
            }
        }
        return a;
    }

    public int a(String str) {
        synchronized (this.f33a) {
            ab abVar = new ab();
            abVar.f28a = str;
            if (this.f33a.contains(abVar)) {
                for (ab next : this.f33a) {
                    if (next.equals(abVar)) {
                        int i = next.a;
                        return i;
                    }
                }
            }
            return 0;
        }
    }

    public synchronized String a(bd bdVar) {
        return this.f32a.getSharedPreferences("mipush_extra", 0).getString(bdVar.name(), "");
    }

    public synchronized void a(bd bdVar, String str) {
        SharedPreferences sharedPreferences = this.f32a.getSharedPreferences("mipush_extra", 0);
        sharedPreferences.edit().putString(bdVar.name(), str).commit();
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m32a(String str) {
        synchronized (this.f33a) {
            ab abVar = new ab();
            abVar.a = 0;
            abVar.f28a = str;
            if (this.f33a.contains(abVar)) {
                this.f33a.remove(abVar);
            }
            this.f33a.add(abVar);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m33a(String str) {
        synchronized (this.f33a) {
            ab abVar = new ab();
            abVar.f28a = str;
            return this.f33a.contains(abVar);
        }
    }

    public void b(String str) {
        synchronized (this.f33a) {
            ab abVar = new ab();
            abVar.f28a = str;
            if (this.f33a.contains(abVar)) {
                Iterator<ab> it = this.f33a.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ab next = it.next();
                    if (abVar.equals(next)) {
                        abVar = next;
                        break;
                    }
                }
            }
            abVar.a++;
            this.f33a.remove(abVar);
            this.f33a.add(abVar);
        }
    }

    public void c(String str) {
        synchronized (this.f33a) {
            ab abVar = new ab();
            abVar.f28a = str;
            if (this.f33a.contains(abVar)) {
                this.f33a.remove(abVar);
            }
        }
    }
}
