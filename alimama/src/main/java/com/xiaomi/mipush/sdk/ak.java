package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.accs.common.Constants;
import com.taobao.weex.common.WXConfig;
import com.vivo.push.PushClientConstants;
import com.xiaomi.push.ay;
import com.xiaomi.push.i;
import com.xiaomi.push.l;
import java.util.HashMap;

class ak {
    public static HashMap<String, String> a(Context context, String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            hashMap.put("appToken", d.a(context).b());
            hashMap.put("regId", MiPushClient.getRegId(context));
            hashMap.put("appId", d.a(context).a());
            hashMap.put("regResource", d.a(context).e());
            if (!l.d()) {
                String g = i.g(context);
                if (!TextUtils.isEmpty(g)) {
                    hashMap.put("imeiMd5", ay.a(g));
                }
            }
            hashMap.put("isMIUI", String.valueOf(l.a()));
            hashMap.put("miuiVersion", l.a());
            hashMap.put(WXConfig.devId, i.a(context, true));
            hashMap.put(Constants.KEY_MODEL, Build.MODEL);
            hashMap.put(PushClientConstants.TAG_PKG_NAME, context.getPackageName());
            hashMap.put("sdkVersion", "3_6_19");
            hashMap.put("androidVersion", String.valueOf(Build.VERSION.SDK_INT));
            hashMap.put("os", Build.VERSION.RELEASE + "-" + Build.VERSION.INCREMENTAL);
            hashMap.put("andId", i.e(context));
            if (!TextUtils.isEmpty(str)) {
                hashMap.put("clientInterfaceId", str);
            }
        } catch (Throwable unused) {
        }
        return hashMap;
    }
}
