package com.alibaba.aliweex.plugin;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.taobao.weex.utils.WXLogUtils;

@Deprecated
public class SimpleAudioPlayer extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        WXLogUtils.e("SimpleAudioPlayer has been removed, use weex audio module instead.");
        return false;
    }
}
