package com.taobao.weex.adapter;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.appfram.navigator.INavigator;
import com.taobao.weex.utils.WXLogUtils;
import org.json.JSONObject;

public class TBNavigatorAdapter implements INavigator {
    public boolean pop(Activity activity, String str) {
        return false;
    }

    public boolean push(Activity activity, String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("url", "");
            if (TextUtils.isEmpty(optString)) {
                return true;
            }
            Uri parse = Uri.parse(optString);
            String scheme = parse.getScheme();
            Uri.Builder buildUpon = parse.buildUpon();
            if (!TextUtils.equals(scheme, "http") && !TextUtils.equals(scheme, "https")) {
                buildUpon.scheme("http");
            }
            push(activity, optString, jSONObject);
            return true;
        } catch (Exception e) {
            WXLogUtils.e("WXNavBarAdapter", WXLogUtils.getStackTrace(e));
            return true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0048, code lost:
        if (r6 == false) goto L_0x004c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void push(android.app.Activity r4, java.lang.String r5, org.json.JSONObject r6) {
        /*
            r3 = this;
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            java.lang.String r1 = "wx_options"
            java.lang.String r2 = r6.toString()
            com.alibaba.fastjson.JSONObject r2 = com.alibaba.fastjson.JSON.parseObject(r2)
            r0.putSerializable(r1, r2)
            com.taobao.android.nav.Nav r1 = com.taobao.android.nav.Nav.from(r4)
            com.taobao.android.nav.Nav r0 = r1.withExtras(r0)
            java.lang.String r5 = r5.trim()
            r0.toUri((java.lang.String) r5)
            java.lang.String r5 = "transform"
            java.lang.String r5 = r6.optString(r5)
            java.lang.String r0 = "animated"
            java.lang.Object r6 = r6.opt(r0)
            r0 = 0
            if (r6 == 0) goto L_0x004b
            boolean r1 = r6 instanceof java.lang.String
            r2 = 1
            if (r1 == 0) goto L_0x003c
            java.lang.String r6 = (java.lang.String) r6
            boolean r6 = java.lang.Boolean.parseBoolean(r6)
            goto L_0x0048
        L_0x003c:
            boolean r1 = r6 instanceof java.lang.Boolean
            if (r1 == 0) goto L_0x0047
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            goto L_0x0048
        L_0x0047:
            r6 = 1
        L_0x0048:
            if (r6 != 0) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r2 = 0
        L_0x004c:
            if (r2 == 0) goto L_0x0052
            r4.overridePendingTransition(r0, r0)
            goto L_0x005d
        L_0x0052:
            java.lang.String r6 = "3d"
            boolean r5 = r6.equals(r5)
            if (r5 == 0) goto L_0x005d
            r4.overridePendingTransition(r0, r0)
        L_0x005d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.adapter.TBNavigatorAdapter.push(android.app.Activity, java.lang.String, org.json.JSONObject):void");
    }
}
