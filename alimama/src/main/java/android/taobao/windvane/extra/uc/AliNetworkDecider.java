package android.taobao.windvane.extra.uc;

import com.uc.webview.export.internal.interfaces.INetworkDecider;

public class AliNetworkDecider implements INetworkDecider {
    public int chooseNetwork(String str) {
        if (str.startsWith("ws://") || str.startsWith("wss://") || !WVUCWebView.getUseTaobaoNetwork()) {
            return 0;
        }
        return 2;
    }
}
