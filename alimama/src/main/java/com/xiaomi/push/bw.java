package com.xiaomi.push;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.xiaomi.push.service.ag;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class bw {
    private static volatile bw a;

    /* renamed from: a  reason: collision with other field name */
    private Context f155a;

    /* renamed from: a  reason: collision with other field name */
    private bv f156a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public final ArrayList<a> f157a = new ArrayList<>();

    /* renamed from: a  reason: collision with other field name */
    private final HashMap<String, bu> f158a = new HashMap<>();

    /* renamed from: a  reason: collision with other field name */
    private ThreadPoolExecutor f159a = new ThreadPoolExecutor(1, 1, 15, TimeUnit.SECONDS, new LinkedBlockingQueue());

    public static abstract class a implements Runnable {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        protected bu f160a = null;

        /* renamed from: a  reason: collision with other field name */
        private a f161a;

        /* renamed from: a  reason: collision with other field name */
        private String f162a;

        /* renamed from: a  reason: collision with other field name */
        private WeakReference<Context> f163a;

        /* renamed from: a  reason: collision with other field name */
        private Random f164a = new Random();
        protected String b;

        public a(String str) {
            this.f162a = str;
        }

        public SQLiteDatabase a() {
            return this.f160a.getWritableDatabase();
        }

        /* renamed from: a  reason: collision with other method in class */
        public Object m124a() {
            return null;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m125a() {
            return this.f162a;
        }

        /* access modifiers changed from: package-private */
        public void a(Context context) {
            if (this.f161a != null) {
                this.f161a.a(context, a());
            }
            b(context);
        }

        public abstract void a(Context context, SQLiteDatabase sQLiteDatabase);

        public void a(Context context, Object obj) {
            bw.a(context).a(this);
        }

        /* access modifiers changed from: package-private */
        public void a(bu buVar, Context context) {
            this.f160a = buVar;
            this.b = this.f160a.a();
            this.f163a = new WeakReference<>(context);
        }

        public void a(a aVar) {
            this.f161a = aVar;
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m126a() {
            return this.f160a == null || TextUtils.isEmpty(this.b) || this.f163a == null;
        }

        public void b(Context context) {
        }

        public final void run() {
            Context context;
            if (this.f163a != null && (context = (Context) this.f163a.get()) != null && context.getFilesDir() != null && this.f160a != null && !TextUtils.isEmpty(this.f162a)) {
                File file = new File(this.f162a);
                v.a(context, new File(file.getParentFile(), ax.b(file.getAbsolutePath())), new by(this, context));
            }
        }
    }

    public static abstract class b<T> extends a {
        private int a;

        /* renamed from: a  reason: collision with other field name */
        private String f165a;

        /* renamed from: a  reason: collision with other field name */
        private List<String> f166a;

        /* renamed from: a  reason: collision with other field name */
        private String[] f167a;
        private List<T> b = new ArrayList();
        private String c;
        private String d;
        private String e;

        public b(String str, List<String> list, String str2, String[] strArr, String str3, String str4, String str5, int i) {
            super(str);
            this.f166a = list;
            this.f165a = str2;
            this.f167a = strArr;
            this.c = str3;
            this.d = str4;
            this.e = str5;
            this.a = i;
        }

        public SQLiteDatabase a() {
            return this.f160a.getReadableDatabase();
        }

        public abstract T a(Context context, Cursor cursor);

        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            String[] strArr;
            this.b.clear();
            String str = null;
            if (this.f166a == null || this.f166a.size() <= 0) {
                strArr = null;
            } else {
                String[] strArr2 = new String[this.f166a.size()];
                this.f166a.toArray(strArr2);
                strArr = strArr2;
            }
            if (this.a > 0) {
                str = String.valueOf(this.a);
            }
            Cursor query = sQLiteDatabase.query(this.b, strArr, this.f165a, this.f167a, this.c, this.d, this.e, str);
            if (query != null && query.moveToFirst()) {
                do {
                    Object a2 = a(context, query);
                    if (a2 != null) {
                        this.b.add(a2);
                    }
                } while (query.moveToNext());
                query.close();
            }
            a(context, this.b);
        }

        public abstract void a(Context context, List<T> list);
    }

    public static class c extends a {
        private ArrayList<a> a = new ArrayList<>();

        public c(String str, ArrayList<a> arrayList) {
            super(str);
            this.a.addAll(arrayList);
        }

        public final void a(Context context) {
            super.a(context);
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                a next = it.next();
                if (next != null) {
                    next.a(context);
                }
            }
        }

        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                a next = it.next();
                if (next != null) {
                    next.a(context, sQLiteDatabase);
                }
            }
        }
    }

    public static class d extends a {
        private String a;

        /* renamed from: a  reason: collision with other field name */
        protected String[] f168a;

        public d(String str, String str2, String[] strArr) {
            super(str);
            this.a = str2;
            this.f168a = strArr;
        }

        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.delete(this.b, this.a, this.f168a);
        }
    }

    public static class e extends a {
        private ContentValues a;

        public e(String str, ContentValues contentValues) {
            super(str);
            this.a = contentValues;
        }

        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.insert(this.b, (String) null, this.a);
        }
    }

    private bw(Context context) {
        this.f155a = context;
    }

    private bu a(String str) {
        bu buVar = this.f158a.get(str);
        if (buVar == null) {
            synchronized (this.f158a) {
                if (buVar == null) {
                    try {
                        buVar = this.f156a.a(this.f155a, str);
                        this.f158a.put(str, buVar);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
        return buVar;
    }

    public static bw a(Context context) {
        if (a == null) {
            synchronized (bw.class) {
                if (a == null) {
                    a = new bw(context);
                }
            }
        }
        return a;
    }

    private void a() {
        ai.a(this.f155a).b(new bx(this), ag.a(this.f155a).a(hl.StatDataProcessFrequency.a(), 5));
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m123a(String str) {
        return a(str).a();
    }

    public void a(a aVar) {
        bu buVar;
        if (aVar != null) {
            if (this.f156a != null) {
                String a2 = aVar.a();
                synchronized (this.f158a) {
                    buVar = this.f158a.get(a2);
                    if (buVar == null) {
                        buVar = this.f156a.a(this.f155a, a2);
                        this.f158a.put(a2, buVar);
                    }
                }
                if (!this.f159a.isShutdown()) {
                    aVar.a(buVar, this.f155a);
                    synchronized (this.f157a) {
                        this.f157a.add(aVar);
                        a();
                    }
                    return;
                }
                return;
            }
            throw new IllegalStateException("should exec init method first!");
        }
    }

    public void a(Runnable runnable) {
        if (!this.f159a.isShutdown()) {
            this.f159a.execute(runnable);
        }
    }

    public void a(ArrayList<a> arrayList) {
        if (this.f156a != null) {
            HashMap hashMap = new HashMap();
            if (!this.f159a.isShutdown()) {
                Iterator<a> it = arrayList.iterator();
                while (it.hasNext()) {
                    a next = it.next();
                    if (next.a()) {
                        next.a(a(next.a()), this.f155a);
                    }
                    ArrayList arrayList2 = (ArrayList) hashMap.get(next.a());
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                        hashMap.put(next.a(), arrayList2);
                    }
                    arrayList2.add(next);
                }
                for (String str : hashMap.keySet()) {
                    ArrayList arrayList3 = (ArrayList) hashMap.get(str);
                    if (arrayList3 != null && arrayList3.size() > 0) {
                        c cVar = new c(str, arrayList3);
                        cVar.a(((a) arrayList3.get(0)).f160a, this.f155a);
                        this.f159a.execute(cVar);
                    }
                }
                return;
            }
            return;
        }
        throw new IllegalStateException("should exec setDbHelperFactory method first!");
    }

    public void b(a aVar) {
        bu buVar;
        if (aVar != null) {
            if (this.f156a != null) {
                String a2 = aVar.a();
                synchronized (this.f158a) {
                    buVar = this.f158a.get(a2);
                    if (buVar == null) {
                        buVar = this.f156a.a(this.f155a, a2);
                        this.f158a.put(a2, buVar);
                    }
                }
                if (!this.f159a.isShutdown()) {
                    aVar.a(buVar, this.f155a);
                    a((Runnable) aVar);
                    return;
                }
                return;
            }
            throw new IllegalStateException("should exec init method first!");
        }
    }
}
