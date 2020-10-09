package com.huawei.updatesdk.support.pm;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.updatesdk.support.b.b;
import com.huawei.updatesdk.support.pm.c;
import com.huawei.updatesdk.support.pm.d;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class g {
    protected static final d a = new d();
    private static final ExecutorService b = Executors.newFixedThreadPool(1);

    public static final class a extends AsyncTask<Void, Void, Boolean> {
        private final String a;
        private final int b;
        private boolean c = false;
        private b d;

        private a(String str, int i, boolean z) {
            this.a = str;
            this.b = i;
            this.c = z;
        }

        private void a() {
            if (!TextUtils.isEmpty(this.d.f())) {
                File file = new File(this.d.f());
                if (file.exists() && !file.delete()) {
                    com.huawei.updatesdk.sdk.a.c.a.a.a.d("PackageService", "file delete error.");
                }
            }
        }

        public static void a(String str, int i) {
            new a(str, i, false).execute(new Void[0]);
        }

        public static void a(String str, int i, boolean z) {
            new a(str, i, z).execute(new Void[0]);
        }

        private void a(String str, File file) {
            String str2 = str + File.separator + file.getName();
            File file2 = new File(str);
            if (!file2.exists() && !file2.mkdirs()) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("PackageService", "getbackFile mkdirs failed!");
            }
            File file3 = new File(str2);
            if (f.a(file, file3)) {
                File parentFile = file3.getParentFile();
                if (parentFile != null) {
                    parentFile.setExecutable(true, false);
                }
                file3.setReadable(true, false);
                this.d.b(str2);
                Thread thread = new Thread(new e(com.huawei.updatesdk.sdk.service.a.a.a().b(), this.d));
                thread.setName("install|pkg:" + this.d.e() + "|path:" + this.d.f());
                thread.start();
            } else if (!b()) {
                g.a(5, 0);
            } else {
                this.c = false;
            }
        }

        private boolean a(String str) {
            String str2;
            String str3;
            this.d = g.b(str);
            if (this.d == null) {
                return false;
            }
            if (1 == this.b) {
                if (!TextUtils.isEmpty(this.d.f())) {
                    g.a.remove(c.b.INSTALL);
                    return com.huawei.updatesdk.support.c.a.a(this.d.f());
                }
            } else if (this.c) {
                this.d.b(this.d.h() + 1);
                if (this.d.h() >= b.b().length) {
                    str2 = "PackageService";
                    str3 = "getRetryBackupPaths large,can not change file path to retry install";
                } else {
                    String str4 = b.b()[this.d.h()];
                    File file = new File(this.d.f());
                    if (!file.exists()) {
                        str2 = "PackageService";
                        str3 = "file do not exist,can not change file path to retry install";
                    } else {
                        a(str4, file);
                    }
                }
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(str2, str3);
                this.c = false;
                return false;
            } else {
                a();
            }
            return false;
        }

        private boolean b() {
            return Build.VERSION.SDK_INT < 23 || -1 != new ContextWrapper(com.huawei.updatesdk.sdk.service.a.a.a().b()).checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(Void... voidArr) {
            boolean a2 = a(this.a);
            if (this.d != null && c.b.INSTALL == this.d.g() && 1 != this.b && !this.c && this.d.h() > -1) {
                a();
            }
            return Boolean.valueOf(a2);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
        }
    }

    private static b a(String str, d.a aVar) {
        b a2 = a.a(str, aVar);
        if (a2 != null) {
            return a2;
        }
        return null;
    }

    protected static void a(int i, int i2) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("INSTALL_STATE", i);
        bundle.putInt("INSTALL_TYPE", i2);
        intent.putExtras(bundle);
        com.huawei.updatesdk.support.d.c.a().c(com.huawei.updatesdk.sdk.service.b.b.a(intent));
    }

    public static void a(String str, String str2, Object obj) {
        b(str, str2, obj);
    }

    /* access modifiers changed from: private */
    public static b b(String str) {
        return a(str, d.a.INSTALL_TYPE);
    }

    private static synchronized void b(String str, String str2, Object obj) {
        synchronized (g.class) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("PackageService", "process:processType=install,path=" + str + ",packageName:" + str2 + ",isNow=" + false);
            if (TextUtils.isEmpty(str)) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("PackageService", "install failed!!!path is empty!!!!");
                return;
            }
            b bVar = new b(str2, str, obj);
            bVar.a(c.a.WAIT_INSTALL);
            bVar.a(false);
            if (bVar.d() == null) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("PackageService", "task.param is null!!");
            }
            a.put(str2, bVar);
            bVar.a(c.b.INSTALL);
            Thread thread = new Thread(new e(com.huawei.updatesdk.sdk.service.a.a.a().b(), bVar));
            thread.setName("install|pkg:" + bVar.e() + "|path:" + bVar.f());
            thread.start();
        }
    }
}
