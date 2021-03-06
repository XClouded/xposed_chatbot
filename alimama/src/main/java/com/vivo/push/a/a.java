package com.vivo.push.a;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.vivo.push.b;
import com.vivo.push.util.p;
import com.vivo.push.util.s;
import com.vivo.push.y;
import java.util.List;

/* compiled from: CommandBridge */
public final class a {
    public static void a(Context context, String str, y yVar) {
        boolean c = yVar.c();
        b a = b.a(context, c ? "com.vivo.vms.upstageservice" : "com.vivo.vms.aidlservice");
        boolean a2 = a.a();
        if (TextUtils.isEmpty(yVar.a())) {
            yVar.a(context.getPackageName());
        }
        if (a2 && !"com.vivo.pushservice".equals(context.getPackageName())) {
            com.vivo.push.a aVar = new com.vivo.push.a(yVar.a(), str, new Bundle());
            yVar.a(aVar);
            if (!a.a(aVar.b())) {
                p.b("CommandBridge", "send command error by aidl");
                p.c(context, "send command error by aidl");
            } else {
                return;
            }
        }
        Intent intent = new Intent("com.vivo.pushservice.action.METHOD");
        intent.setPackage(str);
        intent.setClassName(str, c ? "com.vivo.push.sdk.service.UpstageService" : "com.vivo.push.sdk.service.PushService");
        yVar.a(intent);
        try {
            a(context, intent);
        } catch (Exception e) {
            p.a("CommandBridge", "CommandBridge startService exception: ", e);
        }
    }

    private static void a(Context context, Intent intent) throws Exception {
        if (context != null) {
            try {
                context.startService(intent);
            } catch (Exception e) {
                p.a("CommandBridge", "start service error", e);
                intent.setComponent((ComponentName) null);
                context.sendBroadcast(intent);
            }
        } else {
            p.d("CommandBridge", "enter startService context is null");
            throw new Exception("context is null");
        }
    }

    public static void a(Context context, y yVar, String str) {
        try {
            boolean b = s.b(context, str);
            String str2 = b ? "com.vivo.pushclient.action.RECEIVE" : "com.vivo.pushservice.action.RECEIVE";
            if (TextUtils.isEmpty(str)) {
                p.c(context, "消息接受者包名为空！");
                throw new Exception("消息接受者包名为空！");
            } else if (a(context, str2, str)) {
                if (TextUtils.isEmpty(yVar.a())) {
                    yVar.a(context.getPackageName());
                }
                Intent intent = new Intent();
                intent.setFlags(1048576);
                if (!TextUtils.isEmpty(str2)) {
                    intent.setAction(str2);
                }
                intent.setPackage(str);
                intent.setClassName(str, b ? "com.vivo.push.sdk.service.CommandClientService" : "com.vivo.push.sdk.service.CommandService");
                yVar.b(intent);
                intent.putExtra("command_type", "reflect_receiver");
                a(context, intent);
            } else {
                throw new Exception("校验action异常");
            }
        } catch (Exception e) {
            p.a("CommandBridge", "CommandBridge sendCommandToClient exception", e);
        }
    }

    private static boolean a(Context context, String str, String str2) {
        Intent intent = new Intent(str);
        intent.setPackage(str2);
        try {
            List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 576);
            if (queryBroadcastReceivers != null) {
                if (queryBroadcastReceivers.size() > 0) {
                    return true;
                }
            }
            p.b("CommandBridge", "action check error：action>>" + str + ";pkgname>>" + str2);
            return false;
        } catch (Exception unused) {
            p.b("CommandBridge", "queryBroadcastReceivers error");
            return false;
        }
    }
}
