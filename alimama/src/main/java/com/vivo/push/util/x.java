package com.vivo.push.util;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SpCache */
public final class x implements c {
    private static String a = "SpCache";
    private static String b = "com.vivo.push.cache";
    private SharedPreferences c;

    public final boolean a(Context context) {
        if (this.c != null) {
            return true;
        }
        this.c = context.getSharedPreferences(b, 0);
        return true;
    }

    public final String a(String str, String str2) {
        String string = this.c.getString(str, str2);
        String str3 = a;
        p.d(str3, "getString " + str + " is " + string);
        return string;
    }

    public final void b(String str, String str2) {
        SharedPreferences.Editor edit = this.c.edit();
        if (edit != null) {
            edit.putString(str, str2);
            a.a(edit);
            String str3 = a;
            p.d(str3, "putString by " + str);
            return;
        }
        String str4 = a;
        p.b(str4, "putString error by " + str);
    }

    public final void a() {
        SharedPreferences.Editor edit = this.c.edit();
        if (edit != null) {
            edit.clear();
            a.a(edit);
        }
        p.d(a, "system cache is cleared");
    }
}
