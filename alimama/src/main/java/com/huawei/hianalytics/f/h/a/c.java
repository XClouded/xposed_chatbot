package com.huawei.hianalytics.f.h.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.f.g.d;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.i.a;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import org.android.agoo.common.AgooConstants;

public class c implements a {
    private Context a;
    private String b;

    public c(Context context, String str) {
        this.a = context;
        this.b = str;
    }

    private String a(Context context) {
        return d.a(context, "cached");
    }

    private String a(SharedPreferences sharedPreferences, String str) {
        return (String) g.b(sharedPreferences, str, "");
    }

    private void a(Context context, String str) {
        SharedPreferences.Editor edit = g.c(context, str).edit();
        edit.clear();
        edit.apply();
    }

    private void a(b bVar, String str, String str2) {
        bVar.a(str, this.b);
        bVar.b(str2, this.b);
        bVar.a(this.b);
    }

    private void a(String str, String str2, String str3) {
        a((b) new d(), str, str3);
        a((b) new e(), str2, str3);
    }

    private void b(Context context) {
        a(context, "state");
        a(context, "sessioncontext");
        a(context, AgooConstants.MESSAGE_FLAG);
        c(context);
    }

    private void c(Context context) {
        d.b(context, "cached");
    }

    public void a() {
        String str;
        String str2;
        SharedPreferences c = g.c(this.a, "state");
        if (c == null) {
            str = "V1CompatibleReportTask";
            str2 = "checkAndReportV1Data: get sharedPreference error.";
        } else {
            String a2 = a(c, ProtocolConst.KEY_EVENTS);
            String a3 = a(c, "activities");
            String a4 = a(this.a);
            b(this.a);
            if (!TextUtils.isEmpty(a2) || !TextUtils.isEmpty(a3) || !TextUtils.isEmpty(a4)) {
                a(a2, a3, a4);
                return;
            } else {
                str = "V1CompatibleReportTask";
                str2 = "checkAndReportV1Data: No cached V1 data found.";
            }
        }
        b.b(str, str2);
    }

    public void run() {
        a();
    }
}
