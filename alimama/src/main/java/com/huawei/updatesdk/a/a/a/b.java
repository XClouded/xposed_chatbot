package com.huawei.updatesdk.a.a.a;

import android.text.TextUtils;
import com.huawei.updatesdk.service.a.a;

public class b extends com.huawei.updatesdk.a.a.b {
    public static final int SUCCESS = 0;
    private String sign_;

    public b() {
        b(1);
    }

    public String a() {
        return this.sign_;
    }

    public void b() {
        if (!TextUtils.isEmpty(a())) {
            a a = a.a();
            String d = a.d();
            if (d == null || !d.equals(a())) {
                a.b(a());
            }
        }
    }
}
