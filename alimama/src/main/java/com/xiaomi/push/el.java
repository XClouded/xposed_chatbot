package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.taobao.weex.BuildConfig;
import com.xiaomi.channel.commonutils.logger.b;
import org.json.JSONException;
import org.json.JSONObject;

class el implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ ek f315a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f316a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;

    el(ek ekVar, String str, Context context, String str2, String str3) {
        this.f315a = ekVar;
        this.f316a = str;
        this.a = context;
        this.b = str2;
        this.c = str3;
    }

    public void run() {
        String str;
        String str2;
        Context context;
        ek ekVar;
        em emVar;
        Context context2;
        if (!TextUtils.isEmpty(this.f316a)) {
            try {
                eg.a(this.a, this.f316a, 1001, "get message");
                JSONObject jSONObject = new JSONObject(this.f316a);
                String optString = jSONObject.optString("action");
                String optString2 = jSONObject.optString("awakened_app_packagename");
                String optString3 = jSONObject.optString("awake_app_packagename");
                String optString4 = jSONObject.optString("awake_app");
                String optString5 = jSONObject.optString("awake_type");
                if (this.b.equals(optString3)) {
                    if (this.c.equals(optString4)) {
                        if (TextUtils.isEmpty(optString5) || TextUtils.isEmpty(optString3) || TextUtils.isEmpty(optString4) || TextUtils.isEmpty(optString2)) {
                            eg.a(this.a, this.f316a, 1008, "A receive a incorrect message with empty type");
                            return;
                        }
                        this.f315a.b(optString3);
                        this.f315a.a(optString4);
                        ej ejVar = new ej();
                        ejVar.b(optString);
                        ejVar.a(optString2);
                        ejVar.d(this.f316a);
                        if (NotificationCompat.CATEGORY_SERVICE.equals(optString5)) {
                            if (!TextUtils.isEmpty(optString)) {
                                ekVar = this.f315a;
                                emVar = em.SERVICE_ACTION;
                                context2 = this.a;
                            } else {
                                ejVar.c("com.xiaomi.mipush.sdk.PushMessageHandler");
                                ekVar = this.f315a;
                                emVar = em.SERVICE_COMPONENT;
                                context2 = this.a;
                            }
                        } else if (em.ACTIVITY.f318a.equals(optString5)) {
                            ekVar = this.f315a;
                            emVar = em.ACTIVITY;
                            context2 = this.a;
                        } else if (em.PROVIDER.f318a.equals(optString5)) {
                            ekVar = this.f315a;
                            emVar = em.PROVIDER;
                            context2 = this.a;
                        } else {
                            Context context3 = this.a;
                            String str3 = this.f316a;
                            eg.a(context3, str3, 1008, "A receive a incorrect message with unknown type " + optString5);
                            return;
                        }
                        ekVar.a(emVar, context2, ejVar);
                        return;
                    }
                }
                Context context4 = this.a;
                String str4 = this.f316a;
                eg.a(context4, str4, 1008, "A receive a incorrect message with incorrect package info" + optString3);
            } catch (JSONException e) {
                b.a((Throwable) e);
                context = this.a;
                str2 = this.f316a;
                str = "A meet a exception when receive the message";
            }
        } else {
            context = this.a;
            str2 = BuildConfig.buildJavascriptFrameworkVersion;
            str = "A receive a incorrect message with empty info";
            eg.a(context, str2, 1008, str);
        }
    }
}
