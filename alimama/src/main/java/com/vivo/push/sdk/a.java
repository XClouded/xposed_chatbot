package com.vivo.push.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.vivo.push.aa;
import com.vivo.push.util.p;
import com.vivo.push.util.s;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/* compiled from: CommandWorker */
public final class a extends aa {
    private static a c;
    private static final List<Integer> f = Arrays.asList(new Integer[]{3});
    private Handler d = new Handler(Looper.getMainLooper());
    private String e;

    private a() {
    }

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            if (c == null) {
                c = new a();
            }
            aVar = c;
        }
        return aVar;
    }

    public final void b() {
        this.e = null;
    }

    public final void a(Intent intent) {
        if (intent == null || this.a == null) {
            p.d("CommandWorker", " sendMessage error: intent : " + intent + ", mContext: " + this.a);
            return;
        }
        Message obtain = Message.obtain();
        obtain.obj = intent;
        a(obtain);
    }

    public final void b(Message message) {
        Intent intent = (Intent) message.obj;
        if (intent == null || this.a == null) {
            p.d("CommandWorker", " handleMessage error: intent : " + intent + ", mContext: " + this.a);
            return;
        }
        int intExtra = intent.getIntExtra("command", -1);
        if (intExtra < 0) {
            intExtra = intent.getIntExtra("method", -1);
        }
        String packageName = this.a.getPackageName();
        if (!f.contains(Integer.valueOf(intExtra)) || !s.b(this.a, packageName) || s.d(this.a)) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(this.e)) {
                this.e = a(this.a, packageName, action);
                if (TextUtils.isEmpty(this.e)) {
                    p.d("CommandWorker", " reflectReceiver error: receiver for: " + action + " not found, package: " + packageName);
                    intent.setPackage(packageName);
                    this.a.sendBroadcast(intent);
                    return;
                }
            }
            try {
                Class<?> cls = Class.forName(this.e);
                Object newInstance = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
                Method method = cls.getMethod("onReceive", new Class[]{Context.class, Intent.class});
                intent.setClassName(packageName, this.e);
                this.d.post(new b(this, method, newInstance, new Object[]{this.a.getApplicationContext(), intent}));
            } catch (Exception e2) {
                p.b("CommandWorker", "reflect e: ", e2);
            }
        }
    }

    private static String a(Context context, String str, String str2) {
        List<ResolveInfo> queryBroadcastReceivers;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Intent intent = new Intent(str2);
        intent.setPackage(str);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null || (queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 64)) == null || queryBroadcastReceivers.size() <= 0) {
                return null;
            }
            return queryBroadcastReceivers.get(0).activityInfo.name;
        } catch (Exception e2) {
            p.a("CommandWorker", "error  " + e2.getMessage());
            return null;
        }
    }
}
