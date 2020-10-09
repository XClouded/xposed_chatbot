package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.ag;
import java.io.File;

public class hb implements XMPushService.l {
    private static boolean a = false;

    /* renamed from: a  reason: collision with other field name */
    private int f442a;

    /* renamed from: a  reason: collision with other field name */
    private Context f443a;
    private boolean b;

    public hb(Context context) {
        this.f443a = context;
    }

    private String a(String str) {
        return "com.xiaomi.xmsf".equals(str) ? "1000271" : this.f443a.getSharedPreferences("pref_registered_pkg_names", 0).getString(str, (String) null);
    }

    private void a(Context context) {
        this.b = ag.a(context).a(hl.TinyDataUploadSwitch.a(), true);
        this.f442a = ag.a(context).a(hl.TinyDataUploadFrequency.a(), 7200);
        this.f442a = Math.max(60, this.f442a);
    }

    public static void a(boolean z) {
        a = z;
    }

    private boolean a() {
        return Math.abs((System.currentTimeMillis() / 1000) - this.f443a.getSharedPreferences("mipush_extra", 4).getLong("last_tiny_data_upload_timestamp", -1)) > ((long) this.f442a);
    }

    private boolean a(hf hfVar) {
        return as.b(this.f443a) && hfVar != null && !TextUtils.isEmpty(a(this.f443a.getPackageName())) && new File(this.f443a.getFilesDir(), "tiny_data.data").exists() && !a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m361a() {
        a(this.f443a);
        if (this.b && a()) {
            b.a("TinyData TinyDataCacheProcessor.pingFollowUpAction ts:" + System.currentTimeMillis());
            hf a2 = he.a(this.f443a).a();
            if (!a(a2)) {
                b.a("TinyData TinyDataCacheProcessor.pingFollowUpAction !canUpload(uploader) ts:" + System.currentTimeMillis());
                return;
            }
            a = true;
            hc.a(this.f443a, a2);
        }
    }
}
