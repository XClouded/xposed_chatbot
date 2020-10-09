package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.mipush.sdk.d;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ay;
import com.xiaomi.push.fa;
import com.xiaomi.push.g;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.hu;
import com.xiaomi.push.hw;
import com.xiaomi.push.i;
import com.xiaomi.push.ig;
import com.xiaomi.push.ih;
import com.xiaomi.push.im;
import com.xiaomi.push.in;
import com.xiaomi.push.iq;
import com.xiaomi.push.l;
import com.xiaomi.push.service.aj;
import com.xiaomi.push.service.z;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MiPushClient4Hybrid {
    private static Map<String, d.a> dataMap = new HashMap();
    private static MiPushCallback sCallback;
    private static Map<String, Long> sRegisterTimeMap = new HashMap();

    public static class MiPushCallback {
        public void onCommandResult(String str, MiPushCommandMessage miPushCommandMessage) {
        }

        public void onReceiveRegisterResult(String str, MiPushCommandMessage miPushCommandMessage) {
        }

        public void onReceiveUnregisterResult(String str, MiPushCommandMessage miPushCommandMessage) {
        }
    }

    private static void addPullNotificationTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        sharedPreferences.edit().putLong("last_pull_notification_" + str, System.currentTimeMillis()).commit();
    }

    private static short getDeviceStatus(MiPushMessage miPushMessage, boolean z) {
        String str = miPushMessage.getExtra() == null ? "" : miPushMessage.getExtra().get(Constants.EXTRA_KEY_HYBRID_DEVICE_STATUS);
        int i = 0;
        if (!TextUtils.isEmpty(str)) {
            i = Integer.valueOf(str).intValue();
        }
        if (!z) {
            i = (i & -4) + g.a.NOT_ALLOWED.a();
        }
        return (short) i;
    }

    public static boolean isRegistered(Context context, String str) {
        return d.a(context).a(str) != null;
    }

    public static void onReceiveRegisterResult(Context context, ih ihVar) {
        d.a aVar;
        String b = ihVar.b();
        if (ihVar.a() == 0 && (aVar = dataMap.get(b)) != null) {
            aVar.a(ihVar.f678e, ihVar.f679f);
            d.a(context).a(b, aVar);
        }
        ArrayList arrayList = null;
        if (!TextUtils.isEmpty(ihVar.f678e)) {
            arrayList = new ArrayList();
            arrayList.add(ihVar.f678e);
        }
        MiPushCommandMessage generateCommandMessage = PushMessageHelper.generateCommandMessage(fa.COMMAND_REGISTER.f328a, arrayList, ihVar.f668a, ihVar.f677d, (String) null);
        if (sCallback != null) {
            sCallback.onReceiveRegisterResult(b, generateCommandMessage);
        }
    }

    public static void onReceiveUnregisterResult(Context context, in inVar) {
        MiPushCommandMessage generateCommandMessage = PushMessageHelper.generateCommandMessage(fa.COMMAND_UNREGISTER.f328a, (List<String>) null, inVar.f744a, inVar.f752d, (String) null);
        String a = inVar.a();
        if (sCallback != null) {
            sCallback.onReceiveUnregisterResult(a, generateCommandMessage);
        }
    }

    public static void registerPush(Context context, String str, String str2, String str3) {
        if (d.a(context).a(str2, str3, str)) {
            ArrayList arrayList = new ArrayList();
            d.a a = d.a(context).a(str);
            if (a != null) {
                arrayList.add(a.c);
                MiPushCommandMessage generateCommandMessage = PushMessageHelper.generateCommandMessage(fa.COMMAND_REGISTER.f328a, arrayList, 0, (String) null, (String) null);
                if (sCallback != null) {
                    sCallback.onReceiveRegisterResult(str, generateCommandMessage);
                }
            }
            if (shouldPullNotification(context, str)) {
                Cif ifVar = new Cif();
                ifVar.b(str2);
                ifVar.c(hq.PullOfflineMessage.f485a);
                ifVar.a(aj.a());
                ifVar.a(false);
                ay.a(context).a(ifVar, hg.Notification, false, true, (ht) null, false, str, str2);
                b.b("MiPushClient4Hybrid pull offline pass through message");
                addPullNotificationTime(context, str);
                return;
            }
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - (sRegisterTimeMap.get(str) != null ? sRegisterTimeMap.get(str).longValue() : 0)) < 5000) {
            b.a("MiPushClient4Hybrid  Could not send register message within 5s repeatedly.");
            return;
        }
        sRegisterTimeMap.put(str, Long.valueOf(currentTimeMillis));
        String a2 = ay.a(6);
        d.a aVar = new d.a(context);
        aVar.c(str2, str3, a2);
        dataMap.put(str, aVar);
        ig igVar = new ig();
        igVar.a(aj.a());
        igVar.b(str2);
        igVar.e(str3);
        igVar.d(str);
        igVar.f(a2);
        igVar.c(g.a(context, context.getPackageName()));
        igVar.b(g.a(context, context.getPackageName()));
        igVar.g("3_6_19");
        igVar.a(30619);
        igVar.h(i.e(context));
        igVar.a(hu.Init);
        if (!l.d()) {
            String g = i.g(context);
            if (!TextUtils.isEmpty(g)) {
                if (l.b()) {
                    igVar.i(g);
                }
                igVar.k(ay.a(g));
            }
        }
        igVar.j(i.a());
        int a3 = i.a();
        if (a3 >= 0) {
            igVar.c(a3);
        }
        Cif ifVar2 = new Cif();
        ifVar2.c(hq.HybridRegister.f485a);
        ifVar2.b(d.a(context).a());
        ifVar2.d(context.getPackageName());
        ifVar2.a(iq.a(igVar));
        ifVar2.a(aj.a());
        ay.a(context).a(ifVar2, hg.Notification, (ht) null);
    }

    public static void removeDuplicateCache(Context context, MiPushMessage miPushMessage) {
        String str = miPushMessage.getExtra() != null ? miPushMessage.getExtra().get("jobkey") : null;
        if (TextUtils.isEmpty(str)) {
            str = miPushMessage.getMessageId();
        }
        av.a(context, str);
    }

    public static void reportMessageArrived(Context context, MiPushMessage miPushMessage, boolean z) {
        if (miPushMessage == null || miPushMessage.getExtra() == null) {
            b.a("do not ack message, message is null");
            return;
        }
        try {
            hw hwVar = new hw();
            hwVar.b(d.a(context).a());
            hwVar.a(miPushMessage.getMessageId());
            hwVar.a(Long.valueOf(miPushMessage.getExtra().get(Constants.EXTRA_KEY_HYBRID_MESSAGE_TS)).longValue());
            hwVar.a(getDeviceStatus(miPushMessage, z));
            if (!TextUtils.isEmpty(miPushMessage.getTopic())) {
                hwVar.c(miPushMessage.getTopic());
            }
            ay.a(context).a(hwVar, hg.AckMessage, false, PushMessageHelper.generateMessage(miPushMessage));
            b.b("MiPushClient4Hybrid ack mina message, messageId is " + miPushMessage.getMessageId());
        } catch (Throwable th) {
            miPushMessage.getExtra().remove(Constants.EXTRA_KEY_HYBRID_MESSAGE_TS);
            miPushMessage.getExtra().remove(Constants.EXTRA_KEY_HYBRID_DEVICE_STATUS);
            throw th;
        }
        miPushMessage.getExtra().remove(Constants.EXTRA_KEY_HYBRID_MESSAGE_TS);
        miPushMessage.getExtra().remove(Constants.EXTRA_KEY_HYBRID_DEVICE_STATUS);
    }

    public static void reportMessageClicked(Context context, MiPushMessage miPushMessage) {
        MiPushClient.reportMessageClicked(context, miPushMessage);
    }

    public static void setCallback(MiPushCallback miPushCallback) {
        sCallback = miPushCallback;
    }

    private static boolean shouldPullNotification(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        StringBuilder sb = new StringBuilder();
        sb.append("last_pull_notification_");
        sb.append(str);
        return Math.abs(System.currentTimeMillis() - sharedPreferences.getLong(sb.toString(), -1)) > 300000;
    }

    public static void unregisterPush(Context context, String str) {
        sRegisterTimeMap.remove(str);
        d.a a = d.a(context).a(str);
        if (a != null) {
            im imVar = new im();
            imVar.a(aj.a());
            imVar.d(str);
            imVar.b(a.f59a);
            imVar.c(a.c);
            imVar.e(a.b);
            Cif ifVar = new Cif();
            ifVar.c(hq.HybridUnregister.f485a);
            ifVar.b(d.a(context).a());
            ifVar.d(context.getPackageName());
            ifVar.a(iq.a(imVar));
            ifVar.a(aj.a());
            ay.a(context).a(ifVar, hg.Notification, (ht) null);
            d.a(context).b(str);
        }
    }

    public static void uploadClearMessageData(Context context, LinkedList<? extends Object> linkedList) {
        z.a(context, linkedList);
    }
}
