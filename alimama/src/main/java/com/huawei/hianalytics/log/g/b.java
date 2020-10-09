package com.huawei.hianalytics.log.g;

import android.os.Handler;
import com.huawei.hianalytics.a.d;
import com.huawei.hianalytics.log.b.a;
import com.huawei.hianalytics.log.e.f;
import com.huawei.hianalytics.log.f.a;
import java.io.File;
import java.util.Arrays;

public class b implements e {
    private String a;
    private String b;
    private String c;
    private Handler d;

    public b(String str, Handler handler, String str2) {
        this.a = str;
        this.d = handler;
        this.b = str2 + a.C0006a.b;
        this.c = str2 + a.C0006a.c;
    }

    public void run() {
        if (!f.a(this.c)) {
            com.huawei.hianalytics.g.b.d("CreateFirstZip", "file create fail");
            return;
        }
        com.huawei.hianalytics.log.f.b bVar = new com.huawei.hianalytics.log.f.b();
        com.huawei.hianalytics.log.f.a.a(this.b, d.f() + 1);
        if (!bVar.a(this.b, this.a, this.c)) {
            com.huawei.hianalytics.g.b.d("CreateFirstZip", "log create zip fail");
        }
        File[] a2 = com.huawei.hianalytics.log.f.a.a(this.c);
        int length = a2.length;
        if (length == 0) {
            com.huawei.hianalytics.g.b.c("CreateFirstZip", "There is no file in zips, do not carry out the report");
            return;
        }
        if (length > d.g() && f.b(a2) > d.g()) {
            File[] a3 = com.huawei.hianalytics.log.f.a.a(this.c);
            Arrays.sort(a3, new a.C0007a());
            com.huawei.hianalytics.log.f.a.a(a3, d.g());
        }
        this.d.sendEmptyMessageDelayed(6, 2000);
    }
}
