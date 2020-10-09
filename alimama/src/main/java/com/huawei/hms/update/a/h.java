package com.huawei.hms.update.a;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.alibaba.wireless.security.SecExceptionCode;
import com.huawei.hms.c.e;
import com.huawei.hms.c.i;
import com.huawei.hms.update.a.a.a;
import com.huawei.hms.update.a.a.c;
import com.huawei.hms.update.b.b;
import com.huawei.hms.update.b.d;
import com.huawei.hms.update.provider.UpdateProvider;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/* compiled from: UpdateDownload */
public class h implements a {
    private final Context a;
    private final d b = new b();
    /* access modifiers changed from: private */
    public final a c = new a();
    private com.huawei.hms.update.a.a.b d;
    private File e;

    public h(Context context) {
        this.a = context.getApplicationContext();
    }

    private static boolean a(String str, File file) {
        byte[] a2 = i.a(file);
        if (a2 == null || !com.huawei.hms.c.d.b(a2, true).equalsIgnoreCase(str)) {
            return false;
        }
        return true;
    }

    private synchronized void a(com.huawei.hms.update.a.a.b bVar) {
        this.d = bVar;
    }

    /* access modifiers changed from: private */
    public synchronized void a(int i, int i2, int i3) {
        if (this.d != null) {
            this.d.a(i, i2, i3, this.e);
        }
    }

    public Context b() {
        return this.a;
    }

    public void a() {
        com.huawei.hms.support.log.a.b("UpdateDownload", "Enter cancel.");
        a((com.huawei.hms.update.a.a.b) null);
        this.b.b();
    }

    public void a(com.huawei.hms.update.a.a.b bVar, c cVar) {
        com.huawei.hms.c.a.a(bVar, "callback must not be null.");
        com.huawei.hms.support.log.a.b("UpdateDownload", "Enter downloadPackage.");
        a(bVar);
        if (cVar == null || !cVar.a()) {
            com.huawei.hms.support.log.a.d("UpdateDownload", "In downloadPackage, Invalid update info.");
            a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, 0, 0);
        } else if (!"mounted".equals(Environment.getExternalStorageState())) {
            com.huawei.hms.support.log.a.d("UpdateDownload", "In downloadPackage, Invalid external storage for downloading file.");
            a((int) SecExceptionCode.SEC_ERROR_LBSRISK_NOT_INIT, 0, 0);
        } else {
            String str = cVar.b;
            if (TextUtils.isEmpty(str)) {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In DownloadHelper.downloadPackage, Download the package,  packageName is null: ");
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, 0, 0);
                return;
            }
            Context context = this.a;
            this.e = UpdateProvider.getLocalFile(context, str + ".apk");
            if (this.e == null) {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In downloadPackage, Failed to get local file for downloading.");
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_NOT_INIT, 0, 0);
                return;
            }
            File parentFile = this.e.getParentFile();
            if (parentFile == null || (!parentFile.mkdirs() && !parentFile.isDirectory())) {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In downloadPackage, Failed to create directory for downloading file.");
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, 0, 0);
            } else if (parentFile.getUsableSpace() < ((long) (cVar.d * 3))) {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In downloadPackage, No space for downloading file.");
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_NO_MEMORY, 0, 0);
            } else {
                try {
                    a(cVar);
                } catch (com.huawei.hms.update.b.a unused) {
                    com.huawei.hms.support.log.a.c("UpdateDownload", "In downloadPackage, Canceled to download the update file.");
                    a(2101, 0, 0);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(c cVar) throws com.huawei.hms.update.b.a {
        b bVar;
        com.huawei.hms.support.log.a.b("UpdateDownload", "Enter downloadPackage.");
        b bVar2 = null;
        try {
            String str = cVar.b;
            if (TextUtils.isEmpty(str)) {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In DownloadHelper.downloadPackage, Download the package,  packageName is null: ");
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, 0, 0);
                this.b.a();
                e.a((OutputStream) null);
                return;
            }
            this.c.a(b(), str);
            if (!this.c.b(cVar.c, cVar.d, cVar.e)) {
                this.c.a(cVar.c, cVar.d, cVar.e);
                bVar = a(this.e, cVar.d, str);
            } else if (this.c.b() != this.c.a()) {
                bVar = a(this.e, cVar.d, str);
                try {
                    bVar.a((long) this.c.b());
                } catch (IOException e2) {
                    e = e2;
                    bVar2 = bVar;
                } catch (Throwable th) {
                    th = th;
                    bVar2 = bVar;
                    this.b.a();
                    e.a((OutputStream) bVar2);
                    throw th;
                }
            } else if (a(cVar.e, this.e)) {
                a(2000, 0, 0);
                this.b.a();
                e.a((OutputStream) null);
                return;
            } else {
                this.c.a(cVar.c, cVar.d, cVar.e);
                bVar = a(this.e, cVar.d, str);
            }
            int a2 = this.b.a(cVar.c, bVar, this.c.b(), this.c.a(), this.a);
            if (a2 != 200 && a2 != 206) {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In DownloadHelper.downloadPackage, Download the package, HTTP code: " + a2);
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, 0, 0);
                this.b.a();
                e.a((OutputStream) bVar);
            } else if (!a(cVar.e, this.e)) {
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_LOCATION_SET, 0, 0);
                this.b.a();
                e.a((OutputStream) bVar);
            } else {
                a(2000, 0, 0);
                this.b.a();
                e.a((OutputStream) bVar);
            }
        } catch (IOException e3) {
            e = e3;
            try {
                com.huawei.hms.support.log.a.d("UpdateDownload", "In DownloadHelper.downloadPackage, Failed to download." + e.getMessage());
                a((int) SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, 0, 0);
                this.b.a();
                e.a((OutputStream) bVar2);
            } catch (Throwable th2) {
                th = th2;
                this.b.a();
                e.a((OutputStream) bVar2);
                throw th;
            }
        }
    }

    private b a(File file, int i, String str) throws IOException {
        return new i(this, file, i, i, str);
    }
}
