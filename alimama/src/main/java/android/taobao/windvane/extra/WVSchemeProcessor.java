package android.taobao.windvane.extra;

import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.text.TextUtils;

import anet.channel.strategy.StrategyCenter;

public class WVSchemeProcessor implements WVSchemeIntercepterInterface {
    public String dealUrlScheme(String str) {
        try {
            if (!TextUtils.isEmpty(str) && !str.startsWith("javascript:")) {
                if (!str.equals("about:blank")) {
                    String formalizeUrl = StrategyCenter.getInstance().getFormalizeUrl(str);
                    return TextUtils.isEmpty(formalizeUrl) ? str : formalizeUrl;
                }
            }
            return str;
        } catch (Throwable unused) {
            TaoLog.e("WVSchemeProcessor", "Can not dealUrlScheme : " + str);
            return str.startsWith("//") ? str.replaceFirst("//", "http://") : str;
        }
    }
}
