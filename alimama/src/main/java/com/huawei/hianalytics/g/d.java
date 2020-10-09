package com.huawei.hianalytics.g;

import android.content.Context;
import android.util.Log;

public final class d extends a {
    private d() {
    }

    public static f a() {
        return new d();
    }

    public f a(Context context, String str) {
        return this;
    }

    public void a(String str, int i, String str2, String str3) {
        if (str != null) {
            switch (i) {
                case 3:
                    Log.d(str2, str);
                    break;
                case 5:
                    Log.w(str2, str);
                    break;
                case 6:
                    Log.e(str2, str);
                    break;
                default:
                    Log.i(str2, str);
                    break;
            }
            if (this.a != null) {
                this.a.a(str, i, str2, str3);
            }
        }
    }
}
