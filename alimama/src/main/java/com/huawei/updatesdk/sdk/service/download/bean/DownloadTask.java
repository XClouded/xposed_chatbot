package com.huawei.updatesdk.sdk.service.download.bean;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import mtopsdk.common.util.SymbolExpUtil;

public class DownloadTask implements Parcelable {
    public static final Parcelable.Creator<DownloadTask> CREATOR = new Parcelable.Creator<DownloadTask>() {
        /* renamed from: a */
        public DownloadTask createFromParcel(Parcel parcel) {
            return new DownloadTask(parcel);
        }

        /* renamed from: a */
        public DownloadTask[] newArray(int i) {
            return new DownloadTask[i];
        }
    };
    private static int j = a.e();
    private static final Object k = new Object();
    private a A;
    private b B;
    private List<a> C;
    private long D;
    @b
    private String E;
    private Future<?> F;
    public String a;
    @b
    protected int b;
    @b
    protected long c;
    @b
    protected String d;
    @b
    protected int e;
    @b
    protected int f;
    public boolean g;
    @b
    public int h;
    private final String i;
    @b
    private int l;
    @b
    private String m;
    @b
    private long n;
    @b
    private String o;
    @b
    private long p;
    @b
    private int q;
    private Future<?> r;
    @b
    private String s;
    @b
    private int t;
    private boolean u;
    private int v;
    private int w;
    private boolean x;
    private String y;
    private String z;

    public static class a {
        public int a;
        public String b;

        public String toString() {
            return this.a + "-" + this.b;
        }
    }

    public static class b {
        private long a;
        private long b;
        /* access modifiers changed from: private */
        public boolean c = false;

        public void a(long j) {
            this.a = j;
        }

        public void a(boolean z) {
            this.c = z;
        }

        public boolean a() {
            return this.c;
        }

        public void b(long j) {
            this.b = j;
        }
    }

    private static class c implements PrivilegedAction {
        private Field a;

        public c(Field field) {
            this.a = field;
        }

        public Object run() {
            this.a.setAccessible(true);
            return null;
        }
    }

    public DownloadTask() {
        this.i = "DownloadTask";
        this.l = -1;
        this.b = 0;
        this.n = 0;
        this.p = 0;
        this.c = 0;
        this.q = 0;
        this.f = 0;
        this.g = false;
        this.r = null;
        this.h = 0;
        this.t = 0;
        this.u = true;
        this.v = 1;
        this.w = 0;
        this.x = true;
        this.y = null;
        this.z = null;
        this.A = new a();
        this.B = new b();
        this.C = new CopyOnWriteArrayList();
        this.D = 0;
        this.F = null;
    }

    protected DownloadTask(Bundle bundle) {
        Field field;
        Object valueOf;
        this.i = "DownloadTask";
        this.l = -1;
        this.b = 0;
        this.n = 0;
        this.p = 0;
        this.c = 0;
        this.q = 0;
        this.f = 0;
        this.g = false;
        this.r = null;
        this.h = 0;
        this.t = 0;
        this.u = true;
        this.v = 1;
        this.w = 0;
        this.x = true;
        this.y = null;
        this.z = null;
        this.A = new a();
        this.B = new b();
        this.C = new CopyOnWriteArrayList();
        this.D = 0;
        this.F = null;
        Field[] declaredFields = DownloadTask.class.getDeclaredFields();
        for (int i2 = 0; i2 < declaredFields.length; i2++) {
            try {
                AccessController.doPrivileged(new c(declaredFields[i2]));
                if (declaredFields[i2].isAnnotationPresent(b.class)) {
                    String simpleName = declaredFields[i2].getType().getSimpleName();
                    String name = declaredFields[i2].getName();
                    if ("String".equals(simpleName)) {
                        field = declaredFields[i2];
                        valueOf = bundle.getString(name);
                    } else if ("int".equals(simpleName)) {
                        field = declaredFields[i2];
                        valueOf = Integer.valueOf(bundle.getInt(name));
                    } else if ("long".equals(simpleName)) {
                        field = declaredFields[i2];
                        valueOf = Long.valueOf(bundle.getLong(name));
                    } else if ("float".equals(simpleName)) {
                        field = declaredFields[i2];
                        valueOf = Float.valueOf(bundle.getFloat(name));
                    } else {
                        com.huawei.updatesdk.sdk.a.c.a.a.a.d("DownloadTask", "unsupport field type:" + simpleName + Operators.SPACE_STR + declaredFields[i2].getName());
                    }
                    field.set(this, valueOf);
                }
            } catch (IllegalAccessException | RuntimeException e2) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("DownloadTask", "DownloadTask exception:", e2);
            }
        }
    }

    protected DownloadTask(Parcel parcel) {
        this.i = "DownloadTask";
        this.l = -1;
        this.b = 0;
        this.n = 0;
        this.p = 0;
        this.c = 0;
        this.q = 0;
        this.f = 0;
        this.g = false;
        this.r = null;
        this.h = 0;
        this.t = 0;
        this.u = true;
        this.v = 1;
        this.w = 0;
        this.x = true;
        this.y = null;
        this.z = null;
        this.A = new a();
        this.B = new b();
        this.C = new CopyOnWriteArrayList();
        this.D = 0;
        this.F = null;
        this.l = parcel.readInt();
        this.b = parcel.readInt();
        this.m = parcel.readString();
        this.n = parcel.readLong();
        this.c = parcel.readLong();
        this.d = parcel.readString();
        this.s = parcel.readString();
        this.e = parcel.readInt();
        this.f = parcel.readInt();
        this.h = parcel.readInt();
        this.t = parcel.readInt();
        this.o = parcel.readString();
        this.p = parcel.readLong();
        this.q = parcel.readInt();
        this.a = parcel.readString();
    }

    public static int a() {
        int i2;
        synchronized (k) {
            j++;
            if (j == Integer.MIN_VALUE || j == -1) {
                j = a.e();
            }
            i2 = j;
        }
        return i2;
    }

    public static DownloadTask a(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        return new DownloadTask(bundle);
    }

    public void a(int i2) {
        this.t = i2;
    }

    public void a(long j2) {
        this.n = j2;
        if (j2 <= 0) {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("debug : fileSize is wrong \n");
            stringBuffer.append("Stack for setFileSize, fileSize=" + j2 + ":");
            for (int i2 = 0; i2 < stackTrace.length; i2++) {
                stringBuffer.append("\n    " + stackTrace[i2].getClassName() + "." + stackTrace[i2].getMethodName() + Operators.BRACKET_START_STR + stackTrace[i2].getFileName() + ":" + stackTrace[i2].getLineNumber() + Operators.BRACKET_END_STR);
                if (i2 > 14) {
                    break;
                }
            }
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("HiAppDownload", stringBuffer.toString());
        }
    }

    public void a(NetworkInfo networkInfo) {
        if (this.m != null) {
            int lastIndexOf = this.m.lastIndexOf("&");
            String str = this.m;
            if (lastIndexOf != -1 && this.m.substring(lastIndexOf + 1).trim().startsWith(com.alipay.sdk.app.statistic.c.a)) {
                str = this.m.substring(0, lastIndexOf);
            }
            this.m = str + "&" + com.alipay.sdk.app.statistic.c.a + SymbolExpUtil.SYMBOL_EQUAL + com.huawei.updatesdk.sdk.a.d.c.b.a(networkInfo);
        }
    }

    public void a(String str) {
        this.y = str;
    }

    public void a(Future<?> future) {
        this.r = future;
    }

    public void a(boolean z2) {
        this.u = z2;
    }

    public void a(boolean z2, int i2) {
        this.g = z2;
        this.h = i2;
        if (i2 != 4 && z2) {
            boolean unused = this.B.c = true;
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.c("DownloadTask", "setInterrupt,package:" + w() + ", isInterrupt:" + z2 + ",reason:" + i2);
        if (z2) {
            f();
        }
    }

    public List<a> b() {
        return this.C;
    }

    public void b(int i2) {
        this.v = i2;
    }

    public void b(long j2) {
        this.c = j2;
    }

    public void b(Bundle bundle) {
        Field[] declaredFields = DownloadTask.class.getDeclaredFields();
        for (int i2 = 0; i2 < declaredFields.length; i2++) {
            try {
                AccessController.doPrivileged(new c(declaredFields[i2]));
                if (declaredFields[i2].isAnnotationPresent(b.class)) {
                    String simpleName = declaredFields[i2].getType().getSimpleName();
                    String name = declaredFields[i2].getName();
                    Object obj = declaredFields[i2].get(this);
                    if (obj != null) {
                        if ("String".equals(simpleName)) {
                            bundle.putString(name, (String) obj);
                        } else if ("int".equals(simpleName)) {
                            bundle.putInt(name, ((Integer) obj).intValue());
                        } else if ("long".equals(simpleName)) {
                            bundle.putLong(name, ((Long) obj).longValue());
                        } else if ("float".equals(simpleName)) {
                            bundle.putFloat(name, ((Float) obj).floatValue());
                        } else if ("boolean".equals(simpleName)) {
                            bundle.putBoolean(name, ((Boolean) obj).booleanValue());
                        } else {
                            com.huawei.updatesdk.sdk.a.c.a.a.a.d("DownloadTask", "unsupport type");
                        }
                    }
                }
            } catch (IllegalAccessException | RuntimeException e2) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("DownloadTask", "writeToBundle exception:", e2);
            }
        }
    }

    public void b(String str) {
        this.z = str;
    }

    public void b(Future<?> future) {
        this.F = future;
    }

    public void b(boolean z2) {
        this.x = z2;
    }

    public void c() {
        this.y = null;
        this.z = null;
        this.e = 0;
    }

    public void c(int i2) {
        this.w = i2;
    }

    public void c(long j2) {
        this.D = j2;
    }

    public void c(String str) {
        this.E = str;
    }

    public a d() {
        return this.A;
    }

    public void d(int i2) {
        if (i2 == 5) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("downloadtask", "set DownloadCode.downloadfailed");
        }
        this.f = i2;
    }

    public void d(String str) {
        this.m = str;
    }

    public int describeContents() {
        return 0;
    }

    public b e() {
        return this.B;
    }

    public void e(int i2) {
        this.l = i2;
    }

    public void e(String str) {
        this.d = str;
    }

    public void f() {
        if (this.F != null) {
            try {
                this.F.cancel(true);
                com.huawei.updatesdk.sdk.a.c.a.a.a.c("HiAppDownload", "abort http request, pacakge:" + this.s);
            } catch (Exception e2) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("HiAppDownload", "abort http request exception:", e2);
            }
        }
    }

    public void f(int i2) {
        this.b = i2;
    }

    public void f(String str) {
        this.s = str;
    }

    public int g() {
        return this.h;
    }

    public void g(int i2) {
        this.e = i2;
    }

    public long h() {
        return this.p;
    }

    public int i() {
        return this.v;
    }

    public int j() {
        return this.w;
    }

    public String k() {
        return this.y;
    }

    public String l() {
        return this.z;
    }

    public String m() {
        return this.E;
    }

    public int n() {
        return this.f;
    }

    public int o() {
        return this.l;
    }

    public int p() {
        if (this.b > 100) {
            return 100;
        }
        return this.b;
    }

    public String q() {
        return this.m;
    }

    public long r() {
        return this.n;
    }

    public long s() {
        return this.c;
    }

    public String t() {
        return this.d;
    }

    public String toString() {
        return getClass().getName() + " {\n\thash_: " + this.a + "\n\tid_: " + this.l + "\n\tprogress_: " + this.b + "\n\turl_: " + this.m + "\n\ticonUrl_: \n\tfileSize_: " + this.n + "\n\talreadDownloadSize_: " + this.c + "\n\tfilepath_: " + this.d + "\n\tdownloadRate_: " + this.e + "\n\tstatus_: " + this.f + "\n\tisInterrupt: " + this.g + "\n\tpackageName_: " + this.s + "\n\tinterruptReason_: " + this.h + "\n\tinstallType_: " + this.t + "\n\tdownloadErrInfo: " + this.A + "\n\tisDeleteDirtyFile: " + this.u + "\n\tbackupUrl: " + this.o + "\n\tbackupFileSize: " + this.p + "\n\tdownloadProtocol_: " + this.q + "\n}";
    }

    public String u() {
        int lastIndexOf;
        if (this.d == null || (lastIndexOf = this.d.lastIndexOf(File.separator)) == -1) {
            return null;
        }
        return this.d.substring(lastIndexOf + 1);
    }

    public Future<?> v() {
        return this.r;
    }

    public String w() {
        return this.s;
    }

    public void writeToParcel(Parcel parcel, int i2) {
        parcel.writeInt(this.l);
        parcel.writeInt(this.b);
        parcel.writeString(this.m);
        parcel.writeLong(this.n);
        parcel.writeLong(this.c);
        parcel.writeString(this.d);
        parcel.writeString(this.s);
        parcel.writeInt(this.e);
        parcel.writeInt(this.f);
        parcel.writeInt(this.h);
        parcel.writeInt(this.t);
        parcel.writeString(this.o);
        parcel.writeLong(this.p);
        parcel.writeInt(this.q);
        parcel.writeString(this.a);
    }

    public void x() {
        if (this.u && this.d != null) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("DownloadTask", "download failed, delete temp file, task:" + this);
            if (!new File(this.d).delete()) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("DownloadTask", "file delete failed!");
            }
        }
    }

    public boolean y() {
        return this.x;
    }

    public int z() {
        double s2 = (double) s();
        double r2 = (double) r();
        Double.isNaN(s2);
        Double.isNaN(r2);
        int round = (int) Math.round((s2 / r2) * 100.0d);
        if (round > 100) {
            return 100;
        }
        return round;
    }
}
