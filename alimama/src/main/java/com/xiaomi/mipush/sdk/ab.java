package com.xiaomi.mipush.sdk;

import android.text.TextUtils;

class ab {
    int a = 0;

    /* renamed from: a  reason: collision with other field name */
    String f28a = "";

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ab)) {
            return false;
        }
        ab abVar = (ab) obj;
        return !TextUtils.isEmpty(abVar.f28a) && abVar.f28a.equals(this.f28a);
    }
}
