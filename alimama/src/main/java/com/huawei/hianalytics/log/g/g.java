package com.huawei.hianalytics.log.g;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.a.d;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.b.a;
import com.huawei.hianalytics.log.e.f;
import java.io.File;
import java.security.Key;

public class g implements e {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;

    public g(Context context, String str, String str2) {
        this.a = context;
        this.e = str;
        this.b = str2 + a.C0006a.c;
        this.c = str2 + a.C0006a.d;
        this.d = str2 + a.C0006a.a;
    }

    private boolean a(String str) {
        File file = new File(str);
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            if (listFiles.length != 1 || ((double) listFiles[0].length()) >= 1887436.8d) {
                b.c("uploadTask", "BigZip file size anomaly, delete files");
                com.huawei.hianalytics.log.e.a.a(file);
            } else {
                b.b("uploadTask", "File size validation through,can be reported");
                return true;
            }
        }
        return false;
    }

    public void run() {
        StringBuilder sb;
        com.huawei.hianalytics.log.f.b bVar = new com.huawei.hianalytics.log.f.b();
        com.huawei.hianalytics.log.e.a.a(this.b);
        com.huawei.hianalytics.log.f.a.a(this.b, d.g());
        if (!f.a(this.c)) {
            b.d("uploadTask", "create bigzip file fail");
            return;
        }
        boolean z = false;
        String a2 = com.huawei.hianalytics.log.e.d.a(this.a);
        if (TextUtils.isEmpty(a2) || "2G".equals(a2)) {
            b.b("HiAnalytics/logServer", "The network is bad.");
        } else {
            com.huawei.hianalytics.log.e.a.a(new File(this.c));
            z = bVar.a(this.b, this.e, this.d);
        }
        if (z) {
            Key b2 = com.huawei.hianalytics.log.e.b.b();
            com.huawei.hianalytics.log.f.b bVar2 = new com.huawei.hianalytics.log.f.b();
            bVar2.a(this.d + this.e, this.c + this.e, b2);
            if (a(this.c)) {
                boolean a3 = bVar.a(this.c, com.huawei.hianalytics.log.e.b.a(b2), this.a);
                com.huawei.hianalytics.log.e.a.a(new File(this.c));
                if (a3) {
                    sb = new StringBuilder();
                } else {
                    bVar2.a(this.b, this.d + this.e);
                    return;
                }
            } else {
                sb = new StringBuilder();
            }
            sb.append(this.d);
            sb.append(this.e);
            f.b(sb.toString());
        }
    }
}
