package com.taobao.mtop.wvplugin;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.WindVaneInterface;
import mtopsdk.common.util.TBSdkLog;

public class MtopWVPlugin extends WVApiPlugin {
    public static final String API_SERVER_NAME = "MtopWVPlugin";
    public static final String ERR_SID_INVALID = "ERR_SID_INVALID";
    public static final String FAIL = "HY_FAILED";
    public static final String PARAM_ERR = "HY_PARAM_ERR";
    private static final String TAG = "mtopsdk.MtopWVPlugin";
    public static final String TIME_OUT = "MP_TIME_OUT";
    private ANetBridge aNetBridge = new ANetBridge();
    private MtopBridge mtopBridge = new MtopBridge(this);

    public static void register() {
        WVPluginManager.registerPlugin(API_SERVER_NAME, (Class<? extends WVApiPlugin>) MtopWVPlugin.class);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "register MtopWVPlugin succeed!");
        }
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("send".equals(str)) {
            send(wVCallBackContext, str2);
            return true;
        } else if (!"sendANet".equals(str)) {
            return false;
        } else {
            this.aNetBridge.sendRequest(wVCallBackContext, str2);
            return true;
        }
    }

    @WindVaneInterface
    public void send(WVCallBackContext wVCallBackContext, String str) {
        this.mtopBridge.sendRequest(wVCallBackContext, str);
    }

    public void wvCallback(MtopResult mtopResult) {
        if (mtopResult.isSuccess()) {
            mtopResult.getJsContext().success(mtopResult.toString());
        } else {
            mtopResult.getJsContext().error(mtopResult.toString());
        }
        mtopResult.setJsContext((WVCallBackContext) null);
    }

    public String getUserAgent() {
        try {
            return this.mWebView.getUserAgentString();
        } catch (Exception unused) {
            return "";
        }
    }

    public String getCurrentUrl() {
        try {
            return this.mWebView.getUrl();
        } catch (Exception unused) {
            return "";
        }
    }
}
