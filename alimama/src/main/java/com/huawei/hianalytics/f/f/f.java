package com.huawei.hianalytics.f.f;

import android.content.Context;
import com.alipay.literpc.android.phone.mrpc.core.RpcException;
import com.huawei.hianalytics.f.b.a;
import com.huawei.hianalytics.f.b.c;
import com.huawei.hianalytics.f.b.e;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.i;
import com.huawei.hianalytics.g.b;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

class f implements g {
    private Context a;
    private String b;
    private String c;
    private a[] d;
    private String e;
    private boolean f;

    f(Context context, a[] aVarArr, String str, String str2, String str3, boolean z) {
        this.a = context;
        this.b = str;
        this.c = str2;
        this.d = (a[]) aVarArr.clone();
        this.e = str3;
        this.f = z;
    }

    private void a() {
        g.d(this.a, i.a(this.b, this.c, this.e));
    }

    private void a(a[] aVarArr, String str) {
        JSONArray jSONArray = new JSONArray();
        for (a a2 : aVarArr) {
            a aVar = new a(this.a);
            a2.a((c) aVar);
            jSONArray.put(aVar.a(true));
        }
        g.a(this.a, jSONArray.toString(), "cached_v2_1", str);
    }

    public void run() {
        if (this.f) {
            b.b("EventSendResultHandleTask", "send data ok,reqID:" + this.e);
            a();
            return;
        }
        if (this.d == null || this.d.length <= 0) {
            b.b("EventSendResultHandleTask", "No cache info save! reqID:" + this.e);
        } else {
            String str = "_default_config_tag";
            if (!"_default_config_tag".equals(this.b)) {
                str = this.b + "-" + this.c;
            }
            int m = com.huawei.hianalytics.a.b.m();
            b.b("HiAnalytics/event", "data send failed, write to cache file...reqID:" + this.e);
            if (g.a(this.a, "cached_v2_1", m * 1048576)) {
                b.c("EventSendResultHandleTask", "THe cacheFile is full,Not writing data! reqID:" + this.e);
                return;
            }
            c[] cVarArr = a.a(g.c(this.a, "cached_v2_1"), this.a, str, false).get(str);
            int length = this.d.length;
            ArrayList arrayList = new ArrayList();
            if (cVarArr != null && cVarArr.length > 0) {
                List<e> a2 = m.a(cVarArr);
                int size = a2.size() + length;
                if (size > 6000) {
                    a2 = a2.subList(length, RpcException.ErrorCode.SERVER_SERVICENOTFOUND);
                    length = RpcException.ErrorCode.SERVER_SERVICENOTFOUND;
                } else {
                    length = size;
                }
                for (int i = 0; i < a2.size(); i++) {
                    arrayList.add(a2.get(i).a());
                }
            }
            a[] aVarArr = (a[]) arrayList.toArray(new a[arrayList.size()]);
            a[] aVarArr2 = new a[length];
            System.arraycopy(this.d, 0, aVarArr2, 0, this.d.length);
            if (aVarArr.length > 0) {
                System.arraycopy(aVarArr, 0, aVarArr2, this.d.length, aVarArr.length);
            }
            a(aVarArr2, str);
        }
        a();
    }
}
