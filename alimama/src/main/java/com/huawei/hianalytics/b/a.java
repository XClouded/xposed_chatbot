package com.huawei.hianalytics.b;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.huawei.hianalytics.g.b;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import com.taobao.weex.el.parse.Operators;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class a implements Thread.UncaughtExceptionHandler {
    private static a d;
    private b a;
    private b b;
    private Thread.UncaughtExceptionHandler c;
    private String[] e = new String[0];
    private boolean f;
    private boolean g;
    private Context h;
    private String i = "";
    private String j = "";
    private Map<String, String> k = new HashMap();

    public static a a() {
        a aVar;
        synchronized (a.class) {
            if (d == null) {
                d = new a();
            }
            aVar = d;
        }
        return aVar;
    }

    private void a(Context context) {
        synchronized (a.class) {
            if (this.h == null) {
                this.h = context;
                this.c = Thread.getDefaultUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler(this);
            }
        }
    }

    private boolean a(String str) {
        String[] strArr = {"java.io.FileNotFoundException", "java.sql.SQLException", "java.net.BindException", "java.util.ConcurrentModificationException", "javax.naming.InsufficientResourcesException", "java.util.MissingResourceException", "java.util.jar.JarException", "java.lang.OutOfMemoryError", "java.lang.StackOverflowError", "java.security.acl.NotOwnerException"};
        for (String equals : strArr) {
            if (str.equals(equals)) {
                return false;
            }
        }
        return true;
    }

    private String b(Throwable th) {
        return d(th);
    }

    private void b(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            this.k.put("packageName", context.getPackageName());
            if (packageInfo != null) {
                this.k.put("versionName", packageInfo.versionName == null ? BuildConfig.buildJavascriptFrameworkVersion : packageInfo.versionName);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            b.d("CrashHandler", "an error occured when collect package info,package name not found!");
        }
    }

    private String c(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        Throwable cause = th.getCause();
        for (StackTraceElement stackTraceElement : cause != null ? cause.getStackTrace() : th.getStackTrace()) {
            stringBuffer.append(stackTraceElement.toString().trim());
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    private String d(Throwable th) {
        String name = th.getClass().getName();
        b.b("HianalyticsSDK", "crash error is Grey list");
        for (String equals : this.e) {
            if (name.equals(equals)) {
                return "An exception occurred";
            }
        }
        if (!a(name)) {
            return "An exception occurred";
        }
        this.i = name;
        this.j = c(th);
        return name + "\n" + this.j;
    }

    public void a(Context context, b bVar) {
        this.a = bVar;
        this.g = true;
        a(context);
    }

    public void a(Context context, String[] strArr, b bVar) {
        this.b = bVar;
        this.e = (String[]) strArr.clone();
        this.f = true;
        a(context);
    }

    public boolean a(Throwable th) {
        if (th == null) {
            return false;
        }
        b(this.h);
        String b2 = b(th);
        String str = this.k.get("packageName");
        String str2 = this.k.get("versionName");
        if (this.f) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("packageName", str);
                jSONObject.put("versionName", str2);
                jSONObject.put("errStack", b2.replaceAll("(\r\n|\r|\n|\n\r)", Operators.SPACE_STR));
                jSONObject.put(WXDebugConstants.ENV_OS_VERSION, Build.VERSION.RELEASE);
            } catch (JSONException unused) {
                b.c("CrashHandler", "logManager handlerExc json put error!");
            }
            this.b.a(jSONObject);
        }
        if (!this.g) {
            return true;
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("_crash_class", this.i);
            jSONObject2.put("_crash_stack", this.j);
        } catch (JSONException unused2) {
            b.c("CrashHandler", "eventManager handlerEx json put error!");
        }
        this.a.a(jSONObject2);
        this.i = "";
        this.j = "";
        return true;
    }

    public void b() {
        b.c("CrashHandler", "crash log server unInit!");
        this.f = false;
        this.e = new String[0];
    }

    public void c() {
        this.g = false;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (this.c != null) {
            if (this.h != null) {
                b.d("CrashHandler", "uncaughtException.");
                if (a(th)) {
                    b.d("CrashHandler", "Throwable is doing.");
                }
            }
            this.c.uncaughtException(thread, th);
        }
    }
}
