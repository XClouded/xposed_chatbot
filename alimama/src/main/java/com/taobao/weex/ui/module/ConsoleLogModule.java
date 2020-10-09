package com.taobao.weex.ui.module;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.tao.log.TLogConstant;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.LogLevel;

public class ConsoleLogModule extends WXModule {
    @JSMethod(uiThread = false)
    public void switchLogLevel(@Nullable String str, @Nullable JSCallback jSCallback) {
        LogLevel logLevel = getLogLevel(str);
        ArrayMap arrayMap = new ArrayMap();
        if (logLevel != null) {
            WXEnvironment.sLogLevel = logLevel;
            WXBridgeManager.getInstance().setLogLevel(WXEnvironment.sLogLevel.getValue(), WXEnvironment.isPerf());
            arrayMap.put("status", "success");
        } else {
            arrayMap.put("status", "failure");
        }
        if (jSCallback != null) {
            jSCallback.invoke(arrayMap);
        }
    }

    @JSMethod(uiThread = false)
    public void setPerfMode(@Nullable String str) {
        WXEnvironment.isPerf = "true".equals(str);
        WXBridgeManager.getInstance().setLogLevel(WXEnvironment.sLogLevel.getValue(), WXEnvironment.isPerf());
    }

    @Nullable
    private LogLevel getLogLevel(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            char c = 65535;
            switch (str.hashCode()) {
                case 109935:
                    if (str.equals(TLogConstant.TLOG_MODULE_OFF)) {
                        c = 0;
                        break;
                    }
                    break;
                case 3237038:
                    if (str.equals(ApiConstants.ApiField.INFO)) {
                        c = 3;
                        break;
                    }
                    break;
                case 95458899:
                    if (str.equals("debug")) {
                        c = 4;
                        break;
                    }
                    break;
                case 96784904:
                    if (str.equals("error")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1124446108:
                    if (str.equals("warning")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return LogLevel.OFF;
                case 1:
                    return LogLevel.ERROR;
                case 2:
                    return LogLevel.WARN;
                case 3:
                    return LogLevel.INFO;
                case 4:
                    return LogLevel.DEBUG;
            }
        }
        return null;
    }
}
