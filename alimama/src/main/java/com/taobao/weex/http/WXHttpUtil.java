package com.taobao.weex.http;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Map;

public class WXHttpUtil {
    public static final String KEY_USER_AGENT = "user-agent";
    private static String sDefaultUA;

    public static String assembleUserAgent(Context context, Map<String, String> map) {
        if (TextUtils.isEmpty(sDefaultUA)) {
            StringBuilder sb = new StringBuilder();
            sb.append(map.get(WXConfig.sysModel));
            sb.append("(Android/");
            sb.append(map.get(WXConfig.sysVersion));
            sb.append(Operators.BRACKET_END_STR);
            sb.append(Operators.SPACE_STR);
            sb.append(TextUtils.isEmpty(map.get(WXConfig.appGroup)) ? "" : map.get(WXConfig.appGroup));
            sb.append(Operators.BRACKET_START_STR);
            sb.append(TextUtils.isEmpty(map.get("appName")) ? "" : map.get("appName"));
            sb.append("/");
            sb.append(map.get("appVersion"));
            sb.append(Operators.BRACKET_END_STR);
            sb.append(Operators.SPACE_STR);
            sb.append("Weex/");
            sb.append(map.get("weexVersion"));
            sb.append(Operators.SPACE_STR);
            sb.append(TextUtils.isEmpty(map.get(WXConfig.externalUserAgent)) ? "" : map.get(WXConfig.externalUserAgent));
            sb.append(TextUtils.isEmpty(map.get(WXConfig.externalUserAgent)) ? "" : Operators.SPACE_STR);
            sb.append(WXViewUtils.getScreenWidth(context) + Constants.Name.X + WXViewUtils.getScreenHeight(context));
            sDefaultUA = sb.toString();
        }
        return sDefaultUA;
    }
}
