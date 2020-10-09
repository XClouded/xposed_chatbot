package com.taobao.windvane.plugins;

import android.app.Activity;
import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class GCanvasAudioPlugin extends WVApiPlugin {
    public static String PLUGIN_NAME = "GMedia";
    private Activity mActivity = null;

    public void onDestroy() {
    }

    public void initialize(Context context, IWVWebView iWVWebView) {
        TaoLog.i("GCANVAAUDIOSPLUGIN", "test InitActivity start audio");
        if (context instanceof Activity) {
            this.mActivity = (Activity) context;
        }
        TaoLog.i("GCANVAAUDIOSPLUGIN", "test InitActivity end audio");
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (str2 == "{}") {
            try {
                new JSONArray();
                return true;
            } catch (JSONException unused) {
                TaoLog.e("CANVAS", "fail to parse, action:" + str + ", params:" + str2);
                new JSONArray();
                return true;
            }
        } else {
            new JSONArray(str2);
            return true;
        }
    }
}
