package com.alimama.union.app.infrastructure.socialShare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WVClipboard extends WVApiPlugin {
    private Logger logger = LoggerFactory.getLogger((Class<?>) WVClipboard.class);

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (((str.hashCode() == -1975568730 && str.equals("copyToClipboard")) ? (char) 0 : 65535) != 0) {
            return false;
        }
        copyToClipboard(str2, wVCallBackContext);
        return true;
    }

    private void copyToClipboard(String str, WVCallBackContext wVCallBackContext) {
        String str2;
        try {
            str2 = JSON.parseObject(str).getString("text");
        } catch (Exception e) {
            this.logger.error("parse params error: {}", (Object) e.getMessage());
            str2 = null;
        }
        if (TextUtils.isEmpty(str2)) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            return;
        }
        ((ClipboardManager) wVCallBackContext.getWebview().getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("WINDVANE_CLIP_KEY_MAIN", str2));
        wVCallBackContext.success(new WVResult(WVResult.SUCCESS));
    }
}
