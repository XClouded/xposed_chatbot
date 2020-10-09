package com.taobao.weex.analyzer.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.taobao.weex.analyzer.core.debug.DebugTool;
import com.taobao.weex.analyzer.core.debug.MDSDebugService;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;

public class LaunchAnalyzerReceiver extends BroadcastReceiver {
    private static final String ACTION = "com.taobao.weex.analyzer.LaunchService";
    private static final String CMD_LAUNCH = "launch";
    private static final String CMD_PACKAGE = "package";
    private static final String CMD_WX_SERVER = "wx_server";
    private static final String TRUE = "true";

    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra(CMD_LAUNCH);
            String stringExtra2 = intent.getStringExtra("package");
            if (!TextUtils.isEmpty(stringExtra2)) {
                String packageName = context.getPackageName();
                if (!stringExtra2.equals(packageName)) {
                    WXLogUtils.d("weex-analyzer", "packageName not match(found:" + stringExtra2 + ",expected:" + packageName + Operators.BRACKET_END_STR);
                    return;
                }
            }
            String stringExtra3 = intent.getStringExtra(CMD_WX_SERVER);
            if (!TextUtils.isEmpty(stringExtra)) {
                if ("true".equals(stringExtra)) {
                    String stringExtra4 = intent.getStringExtra(WeexDevOptions.EXTRA_FROM);
                    String stringExtra5 = intent.getStringExtra("deviceId");
                    String stringExtra6 = intent.getStringExtra(WeexDevOptions.EXTRA_WS_URL);
                    WXLogUtils.d("weex-analyzer", "from:" + stringExtra4 + ",deviceId:" + stringExtra5 + ",wxurl:" + stringExtra6);
                    MDSDebugService.launchBy(context, stringExtra6, stringExtra5);
                    return;
                }
                MDSDebugService.stop(context);
            } else if (!TextUtils.isEmpty(stringExtra3)) {
                DebugTool.startRemoteDebug(stringExtra3);
            }
        }
    }
}
