package com.xiaomi.push.service;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class o {
    private static ArrayList<Pair<String, byte[]>> a = new ArrayList<>();

    /* renamed from: a  reason: collision with other field name */
    private static final Map<String, byte[]> f918a = new HashMap();

    public static void a(Context context, int i, String str) {
        synchronized (f918a) {
            for (String next : f918a.keySet()) {
                a(context, next, f918a.get(next), i, str);
            }
            f918a.clear();
        }
    }

    public static void a(Context context, String str, byte[] bArr, int i, String str2) {
        Intent intent = new Intent("com.xiaomi.mipush.ERROR");
        intent.setPackage(str);
        intent.putExtra("mipush_payload", bArr);
        intent.putExtra("mipush_error_code", i);
        intent.putExtra("mipush_error_msg", str2);
        context.sendBroadcast(intent, w.a(str));
    }

    public static void a(XMPushService xMPushService) {
        try {
            synchronized (f918a) {
                for (String next : f918a.keySet()) {
                    w.a(xMPushService, next, f918a.get(next));
                }
                f918a.clear();
            }
        } catch (fx e) {
            b.a((Throwable) e);
            xMPushService.a(10, (Exception) e);
        }
    }

    public static void a(String str, byte[] bArr) {
        synchronized (f918a) {
            f918a.put(str, bArr);
        }
    }

    public static void b(XMPushService xMPushService) {
        ArrayList<Pair<String, byte[]>> arrayList;
        try {
            synchronized (a) {
                arrayList = a;
                a = new ArrayList<>();
            }
            Iterator<Pair<String, byte[]>> it = arrayList.iterator();
            while (it.hasNext()) {
                Pair next = it.next();
                w.a(xMPushService, (String) next.first, (byte[]) next.second);
            }
        } catch (fx e) {
            b.a((Throwable) e);
            xMPushService.a(10, (Exception) e);
        }
    }

    public static void b(String str, byte[] bArr) {
        synchronized (a) {
            a.add(new Pair(str, bArr));
            if (a.size() > 50) {
                a.remove(0);
            }
        }
    }
}
