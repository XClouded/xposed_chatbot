package com.huawei.hms.support.log.b;

import android.content.Context;
import android.util.Log;
import com.huawei.hms.support.log.c;

/* compiled from: LogCatNode */
public class a implements c {
    private c a;

    public void a(Context context, String str) {
        if (this.a != null) {
            this.a.a(context, str);
        }
    }

    public void a(String str, int i, String str2, String str3) {
        Log.println(i, "HMSSDK_" + str2, str3);
        if (this.a != null) {
            this.a.a(str, i, str2, str3);
        }
    }
}
