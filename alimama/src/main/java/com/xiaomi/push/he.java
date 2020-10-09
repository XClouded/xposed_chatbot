package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.be;
import com.xiaomi.push.service.bf;
import java.util.HashMap;
import java.util.Map;

public class he {
    private static he a;

    /* renamed from: a  reason: collision with other field name */
    private final Context f445a;

    /* renamed from: a  reason: collision with other field name */
    private Map<String, hf> f446a = new HashMap();

    private he(Context context) {
        this.f445a = context;
    }

    public static he a(Context context) {
        if (context == null) {
            b.d("[TinyDataManager]:mContext is null, TinyDataManager.getInstance(Context) failed.");
            return null;
        }
        if (a == null) {
            synchronized (he.class) {
                if (a == null) {
                    a = new he(context);
                }
            }
        }
        return a;
    }

    private boolean a(String str, String str2, String str3, String str4, long j, String str5) {
        hk hkVar = new hk();
        hkVar.d(str3);
        hkVar.c(str4);
        hkVar.a(j);
        hkVar.b(str5);
        hkVar.a(true);
        hkVar.a("push_sdk_channel");
        hkVar.e(str2);
        b.a("TinyData TinyDataManager.upload item:" + hkVar.d() + "   ts:" + System.currentTimeMillis());
        return a(hkVar, str);
    }

    /* access modifiers changed from: package-private */
    public hf a() {
        hf hfVar = this.f446a.get("UPLOADER_PUSH_CHANNEL");
        if (hfVar != null) {
            return hfVar;
        }
        hf hfVar2 = this.f446a.get("UPLOADER_HTTP");
        if (hfVar2 != null) {
            return hfVar2;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public Map<String, hf> m362a() {
        return this.f446a;
    }

    public void a(hf hfVar, String str) {
        if (hfVar == null) {
            b.d("[TinyDataManager]: please do not add null mUploader to TinyDataManager.");
        } else if (TextUtils.isEmpty(str)) {
            b.d("[TinyDataManager]: can not add a provider from unkown resource.");
        } else {
            a().put(str, hfVar);
        }
    }

    public boolean a(hk hkVar, String str) {
        if (TextUtils.isEmpty(str)) {
            b.a("pkgName is null or empty, upload ClientUploadDataItem failed.");
            return false;
        } else if (be.a(hkVar, false)) {
            return false;
        } else {
            if (TextUtils.isEmpty(hkVar.d())) {
                hkVar.f(be.a());
            }
            hkVar.g(str);
            bf.a(this.f445a, hkVar);
            return true;
        }
    }

    public boolean a(String str, String str2, long j, String str3) {
        return a(this.f445a.getPackageName(), this.f445a.getPackageName(), str, str2, j, str3);
    }
}
