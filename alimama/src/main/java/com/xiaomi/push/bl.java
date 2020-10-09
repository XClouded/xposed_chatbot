package com.xiaomi.push;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ai;

class bl extends ai.a {
    final /* synthetic */ bk a;

    bl(bk bkVar) {
        this.a = bkVar;
    }

    public int a() {
        return 10052;
    }

    public void run() {
        b.c("exec== mUploadJob");
        if (bk.a(this.a) != null) {
            bk.a(this.a).a(bk.a(this.a));
            this.a.b("upload_time");
        }
    }
}
