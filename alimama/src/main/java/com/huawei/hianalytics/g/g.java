package com.huawei.hianalytics.g;

import android.os.Process;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.weex.el.parse.Operators;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class g {
    private String a = null;
    private String b = "HA";
    private int c = 0;
    private long d = 0;
    private long e = 0;
    private String f;
    private int g;
    private int h;
    private int i = 0;
    private final StringBuilder j = new StringBuilder();

    g(int i2, String str, int i3, String str2) {
        this.i = i2;
        this.a = str;
        this.c = i3;
        if (str2 != null) {
            this.b = str2;
        }
        c();
    }

    private StringBuilder a(StringBuilder sb) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        sb.append(Operators.ARRAY_START);
        sb.append(simpleDateFormat.format(Long.valueOf(this.d)));
        String a2 = e.a(this.c);
        sb.append(' ');
        sb.append(a2);
        sb.append(DXTemplateNamePathUtil.DIR);
        sb.append(this.a);
        sb.append(DXTemplateNamePathUtil.DIR);
        sb.append(this.b);
        sb.append(' ');
        sb.append(this.g);
        sb.append(Operators.CONDITION_IF_MIDDLE);
        sb.append(this.e);
        sb.append(' ');
        sb.append(this.f);
        sb.append(Operators.CONDITION_IF_MIDDLE);
        sb.append(this.h);
        sb.append(Operators.ARRAY_END);
        return sb;
    }

    private StringBuilder b(StringBuilder sb) {
        sb.append(' ');
        sb.append(this.j.toString());
        return sb;
    }

    private g c() {
        this.d = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        this.e = currentThread.getId();
        this.g = Process.myPid();
        StackTraceElement[] stackTrace = currentThread.getStackTrace();
        if (stackTrace.length > this.i) {
            StackTraceElement stackTraceElement = stackTrace[this.i];
            this.f = stackTraceElement.getFileName();
            this.h = stackTraceElement.getLineNumber();
        }
        return this;
    }

    public <T> g a(T t) {
        this.j.append(t);
        return this;
    }

    public String a() {
        StringBuilder sb = new StringBuilder();
        a(sb);
        return sb.toString();
    }

    public String b() {
        StringBuilder sb = new StringBuilder();
        b(sb);
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(4096);
        a(sb);
        b(sb);
        return sb.toString();
    }
}
