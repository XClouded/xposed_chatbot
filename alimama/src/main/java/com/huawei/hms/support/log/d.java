package com.huawei.hms.support.log;

import android.os.Process;
import android.util.Log;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.weex.el.parse.Operators;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* compiled from: LogRecord */
public class d {
    private final StringBuilder a = new StringBuilder();
    private String b = null;
    private String c = "HMS";
    private int d = 0;
    private long e = 0;
    private long f = 0;
    private String g;
    private int h;
    private int i;
    private int j = 0;

    d(int i2, String str, int i3, String str2) {
        this.j = i2;
        this.b = str;
        this.d = i3;
        if (str2 != null) {
            this.c = str2;
        }
        c();
    }

    public static String a(int i2) {
        switch (i2) {
            case 3:
                return "D";
            case 4:
                return "I";
            case 5:
                return "W";
            case 6:
                return "E";
            default:
                return String.valueOf(i2);
        }
    }

    private d c() {
        this.e = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        this.f = currentThread.getId();
        this.h = Process.myPid();
        StackTraceElement[] stackTrace = currentThread.getStackTrace();
        if (stackTrace.length > this.j) {
            StackTraceElement stackTraceElement = stackTrace[this.j];
            this.g = stackTraceElement.getFileName();
            this.i = stackTraceElement.getLineNumber();
        }
        return this;
    }

    public <T> d a(T t) {
        this.a.append(t);
        return this;
    }

    public d a(Throwable th) {
        a(10).a(Log.getStackTraceString(th));
        return this;
    }

    public String a() {
        StringBuilder sb = new StringBuilder();
        a(sb);
        return sb.toString();
    }

    private StringBuilder a(StringBuilder sb) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        sb.append(Operators.ARRAY_START);
        sb.append(simpleDateFormat.format(Long.valueOf(this.e)));
        String a2 = a(this.d);
        sb.append(' ');
        sb.append(a2);
        sb.append(DXTemplateNamePathUtil.DIR);
        sb.append(this.c);
        sb.append(DXTemplateNamePathUtil.DIR);
        sb.append(this.b);
        sb.append(' ');
        sb.append(this.h);
        sb.append(Operators.CONDITION_IF_MIDDLE);
        sb.append(this.f);
        sb.append(' ');
        sb.append(this.g);
        sb.append(Operators.CONDITION_IF_MIDDLE);
        sb.append(this.i);
        sb.append(Operators.ARRAY_END);
        return sb;
    }

    public String b() {
        StringBuilder sb = new StringBuilder();
        b(sb);
        return sb.toString();
    }

    private StringBuilder b(StringBuilder sb) {
        sb.append(' ');
        sb.append(this.a.toString());
        return sb;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        a(sb);
        b(sb);
        return sb.toString();
    }
}
