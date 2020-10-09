package android.taobao.windvane.jsbridge;

import android.taobao.windvane.monitor.WVMonitorService;
import android.text.TextUtils;

public class WVAppEvent extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        return true;
    }

    public void onPause() {
        this.mWebView.fireEvent("WV.Event.APP.Background", "{}");
        if (WVMonitorService.getPerformanceMonitor() != null) {
            WVMonitorService.getPerformanceMonitor().didExitAtTime(this.mWebView.getUrl(), System.currentTimeMillis());
        }
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        String dataOnActive = this.mWebView.getDataOnActive();
        if (TextUtils.isEmpty(dataOnActive)) {
            dataOnActive = "{}";
        }
        this.mWebView.fireEvent("WV.Event.APP.Active", dataOnActive);
        this.mWebView.setDataOnActive((String) null);
    }
}
