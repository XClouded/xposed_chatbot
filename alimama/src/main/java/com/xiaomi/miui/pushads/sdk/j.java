package com.xiaomi.miui.pushads.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.push.ce;
import java.io.File;
import java.lang.reflect.Method;
import org.json.JSONException;
import org.json.JSONObject;

class j extends AsyncTask<String, Integer, Integer> {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private Context f81a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f82a;

    /* renamed from: a  reason: collision with other field name */
    private c f83a;

    /* renamed from: a  reason: collision with other field name */
    private ce f84a;

    /* renamed from: a  reason: collision with other field name */
    private String f85a;
    private String b;

    public j(Context context, SharedPreferences sharedPreferences, String str, int i, String str2, c cVar) {
        this.f81a = context;
        this.f83a = cVar;
        this.f85a = str;
        this.f82a = sharedPreferences;
        this.b = str2;
    }

    private int a(File file) {
        String str;
        h hVar = (h) this.f84a;
        String str2 = hVar.b;
        if (str2 == null) {
            return -1;
        }
        int a2 = b.a(this.f81a, file, str2, hVar);
        d.a("下载广告 imgUrl: " + str2 + " 结果： " + a2);
        if (isCancelled() || a2 != 0) {
            if (isCancelled()) {
                str = "asynctask 被cancel";
            } else {
                str = "网络类型改变，中断下载: " + f.a(this.f81a) + Operators.SPACE_STR + a2;
            }
            d.a(str);
        }
        return a2;
    }

    private int a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            int a2 = a(jSONObject);
            k.a("解析参数并检查, 返回结果: " + a(a2));
            if (a2 != 0) {
                return a2;
            }
            int c = c(jSONObject);
            if (this.f84a != null) {
                k.a("广告获取最终结果： " + c + " 类型: " + this.f84a.a);
            }
            return c;
        } catch (JSONException unused) {
            return -1;
        }
    }

    private int a(JSONObject jSONObject) {
        if (jSONObject == null || !jSONObject.optString("status").equals("success")) {
            return -1;
        }
        int optInt = jSONObject.optInt("nonsense");
        if (optInt != 0) {
            Log.e("MIUIADSPUSH", "广告无效标志设置: " + optInt);
            d.a("广告无效");
            return -2;
        }
        long optLong = jSONObject.optLong("lastShowTime", 0);
        l.a("expireTime: " + optLong + " currentTime: " + System.currentTimeMillis());
        if (optLong == 0 || optLong >= System.currentTimeMillis()) {
            return 0;
        }
        d.a("广告已经过期 lastShow: " + optLong + " current: " + System.currentTimeMillis());
        return -4;
    }

    private ce a(int i) {
        ce ceVar = new ce();
        switch (i) {
            case 1:
                ceVar = new a();
                break;
            case 2:
                ceVar = new h();
                break;
        }
        ceVar.d = this.a;
        ceVar.h = this.f85a;
        return ceVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    private String m79a(int i) {
        switch (i) {
            case -5:
                return "消息不匹配";
            case -4:
                return "过期";
            case -3:
                return "到达上限";
            case -2:
                return "广告失效";
            case -1:
                return "未知原因";
            case 0:
                return "成功";
            default:
                return "";
        }
    }

    private boolean a(int i, int i2) {
        synchronized (this.f82a) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.f82a.getLong("starttime", 0);
            if (j == 0) {
                this.f82a.edit().putLong("starttime", currentTimeMillis).commit();
                return true;
            } else if (currentTimeMillis - j > 86400000) {
                this.f82a.edit().putLong("starttime", 0).commit();
                this.f82a.edit().putInt("notifycount", 0).commit();
                this.f82a.edit().putInt("bubblecount", 0).commit();
                return true;
            } else {
                if (i2 == 2) {
                    if (this.f82a.getInt("notifycount", 0) < i) {
                        return true;
                    }
                } else if (i2 == 1 && this.f82a.getInt("bubblecount", 0) < i * 4) {
                    return true;
                }
                d.b("超过了每天接受广告的上限");
                return false;
            }
        }
    }

    private int b(JSONObject jSONObject) {
        return jSONObject.optInt("showType");
    }

    private int c(JSONObject jSONObject) {
        boolean z;
        int b2 = b(jSONObject);
        try {
            Method method = Class.forName("miui.util.NotificationFilterHelper").getMethod("canSendNotifications", new Class[]{Context.class, String.class});
            k.a(this.b);
            z = !((Boolean) method.invoke((Object) null, new Object[]{this.f81a, this.b})).booleanValue();
        } catch (Exception e) {
            Log.d("NotifyAdsDownloader", "reflect errors!");
            e.printStackTrace();
            z = false;
        }
        k.a("是否禁用了通知栏广告 " + z);
        int optInt = jSONObject.optInt("receiveUpperBound");
        boolean a2 = optInt > 0 ? true ^ a(optInt, b2) : false;
        k.a("是否达到上限 " + a2);
        if (a2 || (b2 == 2 && z)) {
            k.a("使用候选广告 ");
            if (jSONObject.optLong("subAdId") <= 0) {
                k.a("没有候选广告 ");
                return -5;
            }
            JSONObject jSONObject2 = new JSONObject(jSONObject.optString("subAdInfo"));
            int b3 = b(jSONObject2);
            if (b3 == 2 && z) {
                return -6;
            }
            int a3 = a(jSONObject2);
            k.a("候选广告解析参数并检查： " + a3);
            if (a3 != 0) {
                return a3;
            }
            this.f84a = a(b3);
            this.f84a.a(jSONObject2);
        } else {
            try {
                k.a("使用主广告 ");
                this.f84a = a(b2);
                this.f84a.a(jSONObject);
            } catch (JSONException unused) {
                return -1;
            }
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public Integer doInBackground(String... strArr) {
        int a2 = a(this.f85a);
        if (a2 != 0) {
            d.a("广告解析失败 " + a2);
            return Integer.valueOf(a2);
        }
        if (this.f84a.a == 2) {
            a2 = a(this.f81a.getDir("comxiaomimiuipushadssdk", 0));
        }
        return Integer.valueOf(a2);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(Integer num) {
        super.onPostExecute(num);
        if (this.f83a != null) {
            d.a("下载 post 的结果是: " + num);
            this.f83a.a(num.intValue(), this.f84a, this);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        super.onCancelled();
        Log.d("ADS_DOWNLOAD", "onCancelled");
    }
}
