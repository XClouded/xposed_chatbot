package com.taobao.onlinemonitor.evaluate;

import android.os.Build;
import android.text.TextUtils;
import com.taobao.onlinemonitor.HardWareInfo;
import org.android.agoo.common.AgooConstants;

public class HardwareBrand implements CalScore {
    public int getScore(HardWareInfo hardWareInfo) {
        String lowerCase = Build.BRAND.toLowerCase();
        if (TextUtils.isEmpty(lowerCase)) {
            return 1;
        }
        if (lowerCase.contains("samsung") || lowerCase.contains("google")) {
            return 10;
        }
        if (lowerCase.contains(AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI) || lowerCase.contains("honor") || lowerCase.contains("xiaomi")) {
            return 9;
        }
        if (lowerCase.contains(AgooConstants.MESSAGE_SYSTEM_SOURCE_MEIZU) || lowerCase.contains(AgooConstants.MESSAGE_SYSTEM_SOURCE_VIVO) || lowerCase.contains(AgooConstants.MESSAGE_SYSTEM_SOURCE_OPPO) || lowerCase.contains("lge") || lowerCase.contains("verizon")) {
            return 8;
        }
        if (lowerCase.contains("motorola") || lowerCase.contains("zte") || lowerCase.contains("sony") || lowerCase.contains("zuk") || lowerCase.contains("smartisan")) {
            return 7;
        }
        if (lowerCase.contains("gionee") || lowerCase.contains("letv") || lowerCase.contains("leeco") || lowerCase.contains("coolpad") || lowerCase.contains("htc")) {
            return 6;
        }
        return (lowerCase.contains("nubia") || lowerCase.contains("oneplus") || lowerCase.contains("qiku") || lowerCase.contains("360") || lowerCase.contains("lenovo") || lowerCase.contains("cmcc") || lowerCase.contains("asus")) ? 5 : 4;
    }
}
