package com.taobao.windvane.plugins;

import android.app.Activity;
import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.util.Log;

public class GUtilPlugin extends WVApiPlugin {
    public static String PLUGIN_NAME = "GUtil";
    Activity mActivity = null;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        return true;
    }

    public void onDestroy() {
    }

    public void initialize(Context context, IWVWebView iWVWebView) {
        TaoLog.i("GCANVASPLUGIN", "test InitActivity start util");
        Log.e("luanxuan", "enter plugin initialize");
        if (context instanceof Activity) {
            this.mActivity = (Activity) context;
        }
        TaoLog.i("GCANVASPLUGIN", "test InitActivity end util");
    }
}
