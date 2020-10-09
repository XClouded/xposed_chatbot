package com.huawei.hianalytics.log.g;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.huawei.hianalytics.a.d;
import com.huawei.hianalytics.i.b;
import com.huawei.hianalytics.log.b.a;
import com.huawei.hianalytics.log.e.e;
import com.huawei.hianalytics.log.e.f;
import java.io.File;

public final class a {
    /* access modifiers changed from: private */
    public static Context a;
    private static int b;
    private static volatile C0008a c;
    /* access modifiers changed from: private */
    public static String d;
    private static a e;

    /* renamed from: com.huawei.hianalytics.log.g.a$a  reason: collision with other inner class name */
    private static class C0008a extends Handler {
        C0008a(Looper looper) {
            super(looper);
        }

        private void a() {
            if (f.a(a.a)) {
                if (f.a(a.d + a.C0006a.c)) {
                    b.d().a(new g(a.a, e.a(a.a, true, false), a.d));
                }
            }
        }

        public void handleMessage(Message message) {
            if (6 == message.what) {
                a();
            }
        }
    }

    public static a a() {
        a aVar;
        synchronized (a.class) {
            if (e == null) {
                e = new a();
            }
            aVar = e;
        }
        return aVar;
    }

    public static void a(int i, String str, String str2, String str3) {
        String str4;
        String str5;
        if (a == null) {
            str4 = "AppLogApiImpl";
            str5 = "No init of logServer";
        } else if (b(str2, str3)) {
            str4 = "AppLogApiImpl";
            str5 = "tag or msg Parameter error!";
        } else if (a(i)) {
            b.c().a(new f(new com.huawei.hianalytics.log.c.a(str, str2, str3), (Throwable) null, d));
            return;
        } else {
            com.huawei.hianalytics.g.b.c("AppLogApiImpl", "levelInt < Specified level for write log");
            return;
        }
        com.huawei.hianalytics.g.b.d(str4, str5);
    }

    public static void a(String str, String str2) {
        String a2 = com.huawei.hianalytics.util.f.a("logClintID", str, "[a-zA-Z0-9_]{1,256}", "");
        if (!com.huawei.hianalytics.util.f.a("logClintKey", str2, 4096)) {
            str2 = "";
        }
        d.a(a2);
        d.b(str2);
    }

    @TargetApi(18)
    public static void a(String str, String str2, String str3, Bundle bundle) {
        String str4;
        String str5;
        if (a == null) {
            str4 = "AppLogApiImpl";
            str5 = "No init of SDK logServer";
        } else {
            if (com.huawei.hianalytics.util.f.a("errorCode", str3, 256)) {
                if (bundle == null) {
                    com.huawei.hianalytics.g.b.c("AppLogApiImpl", "bundle is null");
                    bundle = new Bundle();
                }
                if (!b(str, str2)) {
                    String str6 = "";
                    if (com.huawei.hianalytics.util.f.a("metaData", bundle.getString("MetaData"), 20480)) {
                        str6 = bundle.getString("MetaData");
                    }
                    bundle.putString("MetaData", str6);
                    a(6, "E", str, str2);
                    bundle.putString("Eventid", str3);
                    d dVar = new d(bundle, "eventinfo.log", d + a.C0006a.b);
                    b c2 = b.c();
                    c2.a(dVar);
                    String a2 = e.a(a, false, "CrashHandler".equals(str));
                    if (c != null) {
                        c2.a(new b(a2, c, d));
                        return;
                    }
                    return;
                }
            }
            str4 = "AppLogApiImpl";
            str5 = "This method is stopped from execution due to a parameter error";
        }
        com.huawei.hianalytics.g.b.c(str4, str5);
    }

    private static boolean a(int i) {
        return i >= b;
    }

    private static boolean a(File[] fileArr) {
        for (File name : fileArr) {
            if ("eventinfo.log".equals(name.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void b() {
        a = null;
    }

    @TargetApi(18)
    private static void b(Context context) {
        a = context;
        d = a.getFilesDir().getPath();
        if (TextUtils.isEmpty(com.huawei.hianalytics.a.b.e())) {
            com.huawei.hianalytics.a.b.d(context.getPackageName());
        }
        if (c == null) {
            HandlerThread handlerThread = new HandlerThread(a.class.getCanonicalName(), 10);
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            if (looper == null) {
                com.huawei.hianalytics.g.b.d("AppLogApiImpl", "handler thread looper is null,send data over!");
                handlerThread.quitSafely();
                return;
            }
            c = new C0008a(looper);
        }
        b = d.e();
    }

    private static boolean b(String str, String str2) {
        boolean z = false;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && str.length() <= 256 && str2.length() <= 20480) {
            z = true;
        }
        return !z;
    }

    public static void c() {
        com.huawei.hianalytics.i.a aVar;
        b bVar;
        if (f.b(a) && f.a(a)) {
            String str = d + a.C0006a.c;
            File file = new File(d + a.C0006a.b);
            if (!file.exists()) {
                com.huawei.hianalytics.g.b.c("AppLogApiImpl", "checkUploadLog() No logs file");
                return;
            }
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length <= 0 || listFiles.length > 5) {
                com.huawei.hianalytics.log.e.a.a(file);
                File[] listFiles2 = new File(str).listFiles();
                if (listFiles2 == null || listFiles2.length == 0) {
                    com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "No error log.");
                    return;
                }
                com.huawei.hianalytics.log.e.a.a(new File(d + a.C0006a.d));
                aVar = new g(a, e.a(a, true, false), d);
                bVar = b.d();
            } else if (a(listFiles)) {
                aVar = new b(e.a(a, false, false), c, d);
                bVar = b.c();
            } else {
                com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "No error log.");
                return;
            }
            bVar.a(aVar);
        }
    }

    public synchronized void a(Context context) {
        b(context);
    }
}
