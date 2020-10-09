package com.taobao.android.zcache.dev;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheManager;
import com.taobao.zcache.log.ZLog;
import com.taobao.zcachecorewrapper.IZCacheCore;
import org.json.JSONException;
import org.json.JSONObject;

public class ZCacheDev extends WVApiPlugin {
    public static final String PLUGIN_NAME = "ZCacheDev";

    public boolean execute(String str, String str2, final WVCallBackContext wVCallBackContext) {
        ZCacheManager.instance().invokeZCacheDev(str, str2, new IZCacheCore.DevCallback() {
            public void onDevBack(boolean z, String str) {
                WVResult wVResult = new WVResult();
                try {
                    wVResult.setData(new JSONObject(str));
                } catch (JSONException unused) {
                    wVResult.addData("msg", str);
                }
                if (z) {
                    wVCallBackContext.success(wVResult);
                    ZLog.i("ZCacheDev back = [" + wVResult.toJsonString() + Operators.ARRAY_END_STR);
                    return;
                }
                wVCallBackContext.error(wVResult);
                ZLog.e("ZCacheDev back = [" + wVResult.toJsonString() + Operators.ARRAY_END_STR);
            }
        });
        return true;
    }
}
