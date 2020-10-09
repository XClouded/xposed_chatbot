package com.huawei.hianalytics.f.f;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.f.b.h;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.i;
import com.huawei.hianalytics.f.g.j;
import com.huawei.hianalytics.g.b;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONObject;

class a implements g {
    private j a;
    private Context b;
    private String c;
    private String d;
    private String e;
    private List<com.huawei.hianalytics.f.b.a> f;
    private String g = "";

    a(Context context, List<com.huawei.hianalytics.f.b.a> list, String str, String str2, String str3, String str4) {
        this.b = context;
        this.f = list;
        this.d = str;
        this.a = l.a();
        this.c = str2;
        this.e = str3;
        this.g = str4;
    }

    private boolean a(h hVar) {
        JSONObject b2 = hVar.b();
        if (b2 == null) {
            b.b("HiAnalytics/event", "appActionDatas is null, no data report");
            return false;
        }
        try {
            byte[] a2 = com.huawei.hianalytics.f.g.h.a(b2.toString().getBytes("UTF-8"));
            SharedPreferences c2 = g.c(this.b, "global_v2");
            String a3 = i.a(this.c, this.d, this.g);
            b.b("DataSendTask", "Record the data reqID being reported,reqID: " + a3);
            g.a(c2, "request_id", (Object) a3);
            return this.a.a(a2, this.d, this.c, this.g);
        } catch (UnsupportedEncodingException unused) {
            b.c("DataSendTask", "sendData():  getBytes - Unsupported coding format!!");
            return false;
        }
    }

    public void run() {
        b.b("HiAnalytics/event", " begin to send event data TYPE : %s, TAG : %s ,reqID:" + this.g, this.d, this.c);
        if ("preins".equals(this.d) && TextUtils.isEmpty(com.huawei.hianalytics.a.b.j())) {
            b.b("HiAnalytics/event", "upload url now : preins ,reqID:" + this.g);
            new n(this.b).a();
        }
        h a2 = m.a(this.f, this.d, this.c, this.e, this.g);
        com.huawei.hianalytics.f.b.a[] a3 = a2.a();
        if (a3.length == 0) {
            b.c("DataSendTask", "Data is out of date and no data is reported.reqID:" + this.g);
            return;
        }
        boolean a4 = a(a2);
        b.b("HiAnalytics/event", "data send result: %s ,reqID:" + this.g, Boolean.valueOf(a4));
        j.a((com.huawei.hianalytics.i.a) new f(this.b, a3, this.c, this.d, this.g, a4));
    }
}
