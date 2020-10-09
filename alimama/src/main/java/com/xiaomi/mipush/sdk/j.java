package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.mode.Message;
import com.huawei.hms.support.api.push.PushReceiver;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ai;
import com.xiaomi.push.as;
import com.xiaomi.push.r;
import com.xiaomi.push.service.ag;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class j {
    private static HashMap<String, String> a = new HashMap<>();

    public static MiPushMessage a(String str) {
        MiPushMessage miPushMessage = new MiPushMessage();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("messageId")) {
                    miPushMessage.setMessageId(jSONObject.getString("messageId"));
                }
                if (jSONObject.has(Message.DESCRIPTION)) {
                    miPushMessage.setDescription(jSONObject.getString(Message.DESCRIPTION));
                }
                if (jSONObject.has("title")) {
                    miPushMessage.setTitle(jSONObject.getString("title"));
                }
                if (jSONObject.has("content")) {
                    miPushMessage.setContent(jSONObject.getString("content"));
                }
                if (jSONObject.has("passThrough")) {
                    miPushMessage.setPassThrough(jSONObject.getInt("passThrough"));
                }
                if (jSONObject.has("notifyType")) {
                    miPushMessage.setNotifyType(jSONObject.getInt("notifyType"));
                }
                if (jSONObject.has(PushManager.MESSAGE_TYPE)) {
                    miPushMessage.setMessageType(jSONObject.getInt(PushManager.MESSAGE_TYPE));
                }
                if (jSONObject.has("alias")) {
                    miPushMessage.setAlias(jSONObject.getString("alias"));
                }
                if (jSONObject.has("topic")) {
                    miPushMessage.setTopic(jSONObject.getString("topic"));
                }
                if (jSONObject.has("user_account")) {
                    miPushMessage.setUserAccount(jSONObject.getString("user_account"));
                }
                if (jSONObject.has("notifyId")) {
                    miPushMessage.setNotifyId(jSONObject.getInt("notifyId"));
                }
                if (jSONObject.has("category")) {
                    miPushMessage.setCategory(jSONObject.getString("category"));
                }
                if (jSONObject.has("isNotified")) {
                    miPushMessage.setNotified(jSONObject.getBoolean("isNotified"));
                }
                if (jSONObject.has(ApiConstants.ApiField.EXTRA)) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject(ApiConstants.ApiField.EXTRA);
                    Iterator<String> keys = jSONObject2.keys();
                    HashMap hashMap = new HashMap();
                    while (keys != null && keys.hasNext()) {
                        String next = keys.next();
                        hashMap.put(next, jSONObject2.getString(next));
                    }
                    if (hashMap.size() > 0) {
                        miPushMessage.setExtra(hashMap);
                    }
                }
            } catch (Exception e) {
                b.d(e.toString());
            }
        }
        return miPushMessage;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0041 A[Catch:{ Exception -> 0x0051 }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0050 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static com.xiaomi.mipush.sdk.PushMessageReceiver a(android.content.Context r5) {
        /*
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r1 = "com.xiaomi.mipush.RECEIVE_MESSAGE"
            r0.<init>(r1)
            java.lang.String r1 = r5.getPackageName()
            r0.setPackage(r1)
            android.content.pm.PackageManager r1 = r5.getPackageManager()
            r2 = 32
            r3 = 0
            java.util.List r0 = r1.queryBroadcastReceivers(r0, r2)     // Catch:{ Exception -> 0x0051 }
            if (r0 == 0) goto L_0x003e
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x0051 }
        L_0x001f:
            boolean r1 = r0.hasNext()     // Catch:{ Exception -> 0x0051 }
            if (r1 == 0) goto L_0x003e
            java.lang.Object r1 = r0.next()     // Catch:{ Exception -> 0x0051 }
            android.content.pm.ResolveInfo r1 = (android.content.pm.ResolveInfo) r1     // Catch:{ Exception -> 0x0051 }
            android.content.pm.ActivityInfo r2 = r1.activityInfo     // Catch:{ Exception -> 0x0051 }
            if (r2 == 0) goto L_0x001f
            android.content.pm.ActivityInfo r2 = r1.activityInfo     // Catch:{ Exception -> 0x0051 }
            java.lang.String r2 = r2.packageName     // Catch:{ Exception -> 0x0051 }
            java.lang.String r4 = r5.getPackageName()     // Catch:{ Exception -> 0x0051 }
            boolean r2 = r2.equals(r4)     // Catch:{ Exception -> 0x0051 }
            if (r2 == 0) goto L_0x001f
            goto L_0x003f
        L_0x003e:
            r1 = r3
        L_0x003f:
            if (r1 == 0) goto L_0x0050
            android.content.pm.ActivityInfo r5 = r1.activityInfo     // Catch:{ Exception -> 0x0051 }
            java.lang.String r5 = r5.name     // Catch:{ Exception -> 0x0051 }
            java.lang.Class r5 = java.lang.Class.forName(r5)     // Catch:{ Exception -> 0x0051 }
            java.lang.Object r5 = r5.newInstance()     // Catch:{ Exception -> 0x0051 }
            com.xiaomi.mipush.sdk.PushMessageReceiver r5 = (com.xiaomi.mipush.sdk.PushMessageReceiver) r5     // Catch:{ Exception -> 0x0051 }
            return r5
        L_0x0050:
            return r3
        L_0x0051:
            r5 = move-exception
            java.lang.String r5 = r5.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r5)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.j.a(android.content.Context):com.xiaomi.mipush.sdk.PushMessageReceiver");
    }

    protected static synchronized String a(Context context, String str) {
        String str2;
        synchronized (j.class) {
            str2 = a.get(str);
            if (TextUtils.isEmpty(str2)) {
                str2 = "";
            }
        }
        return str2;
    }

    public static String a(f fVar) {
        switch (l.a[fVar.ordinal()]) {
            case 1:
                return "hms_push_token";
            case 2:
                return "fcm_push_token";
            case 3:
                return "cos_push_token";
            case 4:
                return "ftos_push_token";
            default:
                return null;
        }
    }

    public static HashMap<String, String> a(Context context, f fVar) {
        ApplicationInfo applicationInfo;
        StringBuilder sb;
        ap apVar;
        HashMap<String, String> hashMap = new HashMap<>();
        String a2 = a(fVar);
        if (TextUtils.isEmpty(a2)) {
            return hashMap;
        }
        String str = null;
        switch (l.a[fVar.ordinal()]) {
            case 1:
                try {
                    applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                } catch (Exception e) {
                    b.d(e.toString());
                    applicationInfo = null;
                }
                int i = -1;
                if (applicationInfo != null) {
                    i = applicationInfo.metaData.getInt(Constants.HUAWEI_HMS_CLIENT_APPID);
                }
                sb = new StringBuilder();
                sb.append("brand:");
                sb.append(o.a(context).name());
                sb.append(Constants.WAVE_SEPARATOR);
                sb.append("token");
                sb.append(":");
                sb.append(a(context, a2));
                sb.append(Constants.WAVE_SEPARATOR);
                sb.append(Constants.PACKAGE_NAME);
                sb.append(":");
                sb.append(context.getPackageName());
                sb.append(Constants.WAVE_SEPARATOR);
                sb.append("app_id");
                sb.append(":");
                sb.append(i);
                break;
            case 2:
                sb = new StringBuilder();
                sb.append("brand:");
                apVar = ap.FCM;
                break;
            case 3:
                sb = new StringBuilder();
                sb.append("brand:");
                apVar = ap.OPPO;
                break;
            case 4:
                sb = new StringBuilder();
                sb.append("brand:");
                apVar = ap.VIVO;
                break;
            default:
                hashMap.put(Constants.ASSEMBLE_PUSH_REG_INFO, str);
                return hashMap;
        }
        sb.append(apVar.name());
        sb.append(Constants.WAVE_SEPARATOR);
        sb.append("token");
        sb.append(":");
        sb.append(a(context, a2));
        sb.append(Constants.WAVE_SEPARATOR);
        sb.append(Constants.PACKAGE_NAME);
        sb.append(":");
        sb.append(context.getPackageName());
        str = sb.toString();
        hashMap.put(Constants.ASSEMBLE_PUSH_REG_INFO, str);
        return hashMap;
    }

    /* renamed from: a  reason: collision with other method in class */
    static void m67a(Context context) {
        boolean z = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        String a2 = a(f.ASSEMBLE_PUSH_HUAWEI);
        String a3 = a(f.ASSEMBLE_PUSH_FCM);
        if (!TextUtils.isEmpty(sharedPreferences.getString(a2, "")) && TextUtils.isEmpty(sharedPreferences.getString(a3, ""))) {
            z = true;
        }
        if (z) {
            ay.a(context).a(2, a2);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m68a(Context context, f fVar) {
        String a2 = a(fVar);
        if (!TextUtils.isEmpty(a2)) {
            r.a(context.getSharedPreferences("mipush_extra", 0).edit().putString(a2, ""));
        }
    }

    public static void a(Context context, f fVar, String str) {
        if (!TextUtils.isEmpty(str)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
            String a2 = a(fVar);
            if (TextUtils.isEmpty(a2)) {
                b.a("ASSEMBLE_PUSH : can not find the key of token used in sp file");
                return;
            }
            String string = sharedPreferences.getString(a2, "");
            if (TextUtils.isEmpty(string) || !str.equals(string)) {
                b.a("ASSEMBLE_PUSH : send token upload");
                a(fVar, str);
                bd a3 = m.a(fVar);
                if (a3 != null) {
                    ay.a(context).a((String) null, a3, fVar);
                    return;
                }
                return;
            }
            b.a("ASSEMBLE_PUSH : do not need to send token");
        }
    }

    public static void a(Intent intent) {
        Bundle extras;
        if (intent != null && (extras = intent.getExtras()) != null && extras.containsKey(PushReceiver.BOUND_KEY.pushMsgKey)) {
            intent.putExtra(PushMessageHelper.KEY_MESSAGE, a(extras.getString(PushReceiver.BOUND_KEY.pushMsgKey)));
        }
    }

    private static synchronized void a(f fVar, String str) {
        synchronized (j.class) {
            String a2 = a(fVar);
            if (TextUtils.isEmpty(a2)) {
                b.a("ASSEMBLE_PUSH : can not find the key of token used in sp file");
            } else if (TextUtils.isEmpty(str)) {
                b.a("ASSEMBLE_PUSH : token is null");
            } else {
                a.put(a2, str);
            }
        }
    }

    public static void a(String str, int i) {
        MiTinyDataClient.upload("hms_push_error", str, 1, "error code = " + i);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m69a(Context context) {
        if (context == null) {
            return false;
        }
        return as.b(context);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m70a(Context context, f fVar) {
        if (m.a(fVar) != null) {
            return ag.a(context).a(m.a(fVar).a(), true);
        }
        return false;
    }

    public static String b(f fVar) {
        switch (l.a[fVar.ordinal()]) {
            case 1:
                return "hms_push_error";
            case 2:
                return "fcm_push_error";
            case 3:
                return "cos_push_error";
            case 4:
                return "ftos_push_error";
            default:
                return null;
        }
    }

    public static void b(Context context) {
        g.a(context).register();
    }

    public static void b(Context context, f fVar, String str) {
        ai.a(context).a((Runnable) new k(str, context, fVar));
    }

    public static void c(Context context) {
        g.a(context).unregister();
    }

    /* access modifiers changed from: private */
    public static synchronized void d(Context context, f fVar, String str) {
        synchronized (j.class) {
            String a2 = a(fVar);
            if (TextUtils.isEmpty(a2)) {
                b.a("ASSEMBLE_PUSH : can not find the key of token used in sp file");
                return;
            }
            r.a(context.getSharedPreferences("mipush_extra", 0).edit().putString(a2, str));
            b.a("ASSEMBLE_PUSH : update sp file success!  " + str);
        }
    }
}
