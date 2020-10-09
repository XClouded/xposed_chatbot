package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.text.TextUtils;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ai;
import com.xiaomi.push.as;
import com.xiaomi.push.ay;
import com.xiaomi.push.hl;
import com.xiaomi.push.r;
import com.xiaomi.push.service.ag;
import java.lang.Thread;

class x implements Thread.UncaughtExceptionHandler {
    private static final Object a = new Object();

    /* renamed from: a  reason: collision with other field name */
    private static final String[] f74a = {"com.xiaomi.channel.commonutils", "com.xiaomi.common.logger", "com.xiaomi.measite.smack", "com.xiaomi.metoknlp", "com.xiaomi.mipush.sdk", "com.xiaomi.network", "com.xiaomi.push", "com.xiaomi.slim", "com.xiaomi.smack", "com.xiaomi.stats", "com.xiaomi.tinyData", "com.xiaomi.xmpush.thrift", "com.xiaomi.clientreport"};

    /* renamed from: a  reason: collision with other field name */
    private Context f75a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f76a;

    /* renamed from: a  reason: collision with other field name */
    private Thread.UncaughtExceptionHandler f77a;

    public x(Context context) {
        this(context, Thread.getDefaultUncaughtExceptionHandler());
    }

    public x(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.f75a = context;
        this.f77a = uncaughtExceptionHandler;
    }

    private String a(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(3, stackTrace.length); i++) {
            sb.append(stackTrace[i].toString() + "\r\n");
        }
        String sb2 = sb.toString();
        return TextUtils.isEmpty(sb2) ? "" : ay.a(sb2);
    }

    private void a() {
        ai.a(this.f75a).a((Runnable) new y(this));
    }

    /* renamed from: a  reason: collision with other method in class */
    private void m74a(Throwable th) {
        String b = b(th);
        if (!TextUtils.isEmpty(b)) {
            String a2 = a(th);
            if (!TextUtils.isEmpty(a2)) {
                u.a(this.f75a).a(b, a2);
                if (a()) {
                    a();
                }
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m75a() {
        this.f76a = this.f75a.getSharedPreferences("mipush_extra", 4);
        if (as.e(this.f75a)) {
            if (!ag.a(this.f75a).a(hl.Crash4GUploadSwitch.a(), true)) {
                return false;
            }
            return ((float) Math.abs((System.currentTimeMillis() / 1000) - this.f76a.getLong("last_crash_upload_time_stamp", 0))) >= ((float) Math.max(3600, ag.a(this.f75a).a(hl.Crash4GUploadFrequency.a(), 3600))) * 0.9f;
        } else if (!as.d(this.f75a)) {
            return true;
        } else {
            return Math.abs((System.currentTimeMillis() / 1000) - this.f76a.getLong("last_crash_upload_time_stamp", 0)) >= ((long) Math.max(60, ag.a(this.f75a).a(hl.CrashWIFIUploadFrequency.a(), 1800)));
        }
    }

    private boolean a(boolean z, String str) {
        for (String contains : f74a) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return z;
    }

    private String b(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        StringBuilder sb = new StringBuilder(th.toString());
        sb.append("\r\n");
        boolean z = false;
        for (StackTraceElement stackTraceElement : stackTrace) {
            String stackTraceElement2 = stackTraceElement.toString();
            z = a(z, stackTraceElement2);
            sb.append(stackTraceElement2);
            sb.append("\r\n");
        }
        return z ? sb.toString() : "";
    }

    private void b() {
        this.f76a = this.f75a.getSharedPreferences("mipush_extra", 4);
        SharedPreferences.Editor edit = this.f76a.edit();
        edit.putLong("last_crash_upload_time_stamp", System.currentTimeMillis() / 1000);
        r.a(edit);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        a(th);
        synchronized (a) {
            try {
                a.wait(TBToast.Duration.MEDIUM);
            } catch (InterruptedException e) {
                b.a((Throwable) e);
            }
        }
        if (this.f77a != null) {
            this.f77a.uncaughtException(thread, th);
            return;
        }
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
