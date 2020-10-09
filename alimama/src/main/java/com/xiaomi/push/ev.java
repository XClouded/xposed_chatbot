package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.service.WVEventId;
import android.text.TextUtils;
import com.xiaomi.clientreport.data.EventClientReport;
import com.xiaomi.clientreport.data.PerfClientReport;
import com.xiaomi.clientreport.data.a;
import com.xiaomi.clientreport.manager.ClientReportClient;

public class ev {
    private static volatile ev a;

    /* renamed from: a  reason: collision with other field name */
    private Context f320a;

    private ev(Context context) {
        this.f320a = context;
    }

    public static ev a(Context context) {
        if (a == null) {
            synchronized (ev.class) {
                if (a == null) {
                    a = new ev(context);
                }
            }
        }
        return a;
    }

    private void a(a aVar) {
        if (aVar instanceof PerfClientReport) {
            ClientReportClient.reportPerf(this.f320a, (PerfClientReport) aVar);
        } else if (aVar instanceof EventClientReport) {
            ClientReportClient.reportEvent(this.f320a, (EventClientReport) aVar);
        }
    }

    public void a(String str, int i, long j, long j2) {
        if (i >= 0 && j2 >= 0 && j > 0) {
            PerfClientReport a2 = eu.a(this.f320a, i, j, j2);
            a2.setAppPackageName(str);
            a2.setSdkVersion("3_6_19");
            a((a) a2);
        }
    }

    public void a(String str, Intent intent, int i, String str2) {
        if (intent != null) {
            String str3 = str;
            a(str3, eu.a(intent.getIntExtra("eventMessageType", -1)), intent.getStringExtra("messageId"), i, System.currentTimeMillis(), str2);
        }
    }

    public void a(String str, Intent intent, String str2) {
        if (intent != null) {
            String str3 = str;
            a(str3, eu.a(intent.getIntExtra("eventMessageType", -1)), intent.getStringExtra("messageId"), WVEventId.ACCS_ONDATA, System.currentTimeMillis(), str2);
        }
    }

    public void a(String str, Intent intent, Throwable th) {
        if (intent != null) {
            String str2 = str;
            a(str2, eu.a(intent.getIntExtra("eventMessageType", -1)), intent.getStringExtra("messageId"), WVEventId.ACCS_ONDATA, System.currentTimeMillis(), th.getMessage());
        }
    }

    public void a(String str, String str2, String str3, int i, long j, String str4) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            EventClientReport a2 = eu.a(this.f320a, str2, str3, i, j, str4);
            a2.setAppPackageName(str);
            a2.setSdkVersion("3_6_19");
            a((a) a2);
        }
    }

    public void a(String str, String str2, String str3, int i, String str4) {
        a(str, str2, str3, i, System.currentTimeMillis(), str4);
    }

    public void a(String str, String str2, String str3, String str4) {
        a(str, str2, str3, WVEventId.ACCS_ONCONNECTED, System.currentTimeMillis(), str4);
    }

    public void a(String str, String str2, String str3, Throwable th) {
        a(str, str2, str3, WVEventId.ACCS_ONDATA, System.currentTimeMillis(), th.getMessage());
    }

    public void b(String str, String str2, String str3, String str4) {
        a(str, str2, str3, WVEventId.ACCS_ONDATA, System.currentTimeMillis(), str4);
    }

    public void c(String str, String str2, String str3, String str4) {
        a(str, str2, str3, 4002, System.currentTimeMillis(), str4);
    }
}
