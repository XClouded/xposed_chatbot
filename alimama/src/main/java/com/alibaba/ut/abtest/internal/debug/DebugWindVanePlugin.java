package com.alibaba.ut.abtest.internal.debug;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import mtopsdk.mtop.upload.domain.UploadConstants;

public class DebugWindVanePlugin extends WVApiPlugin {
    public static final String API_SERVER_NAME = "WVUTABDebug";
    private static final String TAG = "DebugWindVanePlugin";

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        LogUtils.logD(TAG, "action=" + str + ", params=" + str2 + ", callback=" + wVCallBackContext);
        if (TextUtils.equals("startRealtimeDebug", str)) {
            ABContext.getInstance().setDebugMode(true);
            if (!TextUtils.isEmpty(str2)) {
                Debug debug = (Debug) JsonUtil.fromJson(str2, Debug.class);
                if (debug == null) {
                    LogUtils.logE(TAG, "开启实时调试失败，参数错误。params=" + str2);
                    if (wVCallBackContext != null) {
                        wVCallBackContext.error();
                    }
                    return true;
                }
                ABContext.getInstance().getMultiProcessService().startRealTimeDebug(debug);
                if (wVCallBackContext != null) {
                    wVCallBackContext.success();
                }
            } else if (wVCallBackContext != null) {
                wVCallBackContext.error();
            }
            return true;
        } else if (!TextUtils.equals("getContextValue", str)) {
            return false;
        } else {
            if (wVCallBackContext != null) {
                WVResult wVResult = new WVResult();
                wVResult.addData("utdid", SystemInformation.getInstance().getUtdid());
                wVResult.addData(UploadConstants.USERID, ABContext.getInstance().getUserId());
                wVResult.addData("usernick", ABContext.getInstance().getUserNick());
                wVCallBackContext.success(wVResult);
            }
            return true;
        }
    }
}
