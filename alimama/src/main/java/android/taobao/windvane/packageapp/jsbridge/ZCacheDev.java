package android.taobao.windvane.packageapp.jsbridge;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;

import com.taobao.zcache.ZCacheManager;
import com.taobao.zcachecorewrapper.IZCacheCore;

import org.json.JSONException;
import org.json.JSONObject;

public class ZCacheDev extends WVApiPlugin {
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
                } else {
                    wVCallBackContext.error(wVResult);
                }
            }
        });
        return true;
    }
}
