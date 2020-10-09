package com.vivo.push.util;

import android.content.Context;
import android.os.Process;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import com.vivo.push.a.a;
import com.vivo.push.b.p;
import com.vivo.push.y;

/* compiled from: LogController */
public final class n implements o {
    private static final String a = (Operators.BRACKET_START_STR + Process.myPid() + Operators.BRACKET_END_STR);

    public final int a(String str, String str2) {
        return Log.e("VivoPush." + str, a + str2);
    }

    public final int a(String str, Throwable th) {
        return Log.e("VivoPush." + str, Log.getStackTraceString(th));
    }

    public final int a(String str, String str2, Throwable th) {
        return Log.e("VivoPush." + str, a + str2, th);
    }

    public final int b(String str, String str2) {
        return Log.w("VivoPush." + str, a + str2);
    }

    public final int c(String str, String str2) {
        return Log.d("VivoPush." + str, a + str2);
    }

    public final int d(String str, String str2) {
        if (!p.a()) {
            return -1;
        }
        return Log.i("VivoPush." + str, a + str2);
    }

    public final int b(String str, String str2, Throwable th) {
        if (!p.a()) {
            return -1;
        }
        return Log.i("VivoPush." + str, a + str2, th);
    }

    public final int e(String str, String str2) {
        if (!p.a()) {
            return -1;
        }
        return Log.v("VivoPush." + str, a + str2);
    }

    public final String a(Throwable th) {
        return Log.getStackTraceString(th);
    }

    public final void a(Context context, String str) {
        if (p.a()) {
            a(context, str, 0);
        }
    }

    public final void b(Context context, String str) {
        if (p.a()) {
            a(context, str, 1);
        }
    }

    public final void c(Context context, String str) {
        if (p.a()) {
            a(context, str, 2);
        }
    }

    private void a(Context context, String str, int i) {
        p pVar = new p();
        pVar.b(str);
        pVar.a(i);
        if (i > 0) {
            d("LogController", str);
        }
        if (z.a(context)) {
            pVar.a(true);
            for (String a2 : s.c(context)) {
                a(context, pVar, a2);
            }
            return;
        }
        pVar.a(false);
        a(context, pVar, context.getPackageName());
    }

    private static void a(Context context, p pVar, String str) {
        if (str.contains("test") || str.equals(s.b(context))) {
            a.a(context, (y) pVar, str);
        }
    }
}
