package com.alibaba.analytics.utils;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.taobao.orange.OConstant;
import java.util.HashMap;

public class WuaHelper {
    public static String getMiniWua() {
        long j = 0;
        try {
            if (Logger.isDebug()) {
                j = System.currentTimeMillis();
            }
            Class<?> cls = Class.forName(OConstant.REFLECT_SECURITYGUARD);
            Object invoke = cls.getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{Variables.getInstance().getContext()});
            Class<?> cls2 = Class.forName("com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent");
            Object invoke2 = cls.getMethod("getInterface", new Class[]{Class.class}).invoke(invoke, new Object[]{cls2});
            Class<?> cls3 = Class.forName("com.alibaba.wireless.security.open.securitybody.SecurityBodyDefine");
            int i = cls3.getField("OPEN_SECURITYBODY_FLAG_FORMAT_MINI").getInt(cls3);
            int i2 = cls3.getField("OPEN_SECURITYBODY_ENV_ONLINE").getInt(cls3);
            Logger.d("OPEN_SECURITYBODY_FLAG_FORMAT_MINI:" + i, new Object[0]);
            Logger.d("OPEN_SECURITYBODY_ENV_ONLINE:" + i2, new Object[0]);
            String str = (String) cls2.getMethod("getSecurityBodyDataEx", new Class[]{String.class, String.class, String.class, HashMap.class, Integer.TYPE, Integer.TYPE}).invoke(invoke2, new Object[]{null, null, null, null, Integer.valueOf(i), Integer.valueOf(i2)});
            if (Logger.isDebug()) {
                Logger.d("Mini Wua: " + str + ",waste time:" + (System.currentTimeMillis() - j), new Object[0]);
            }
            return str;
        } catch (Throwable th) {
            Logger.d("", th.toString());
            return null;
        }
    }
}
