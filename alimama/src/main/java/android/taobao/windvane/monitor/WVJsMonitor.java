package android.taobao.windvane.monitor;

public class WVJsMonitor implements WVJSBrdigeMonitorInterface {
    public void didCallAtURL(String str, String str2, String str3) {
    }

    public void didCallBackAtURL(String str, String str2, String str3, String str4) {
    }

    public void didOccurError(String str, String str2, String str3, String str4) {
    }

    public void onJsBridgeReturn(String str, String str2, String str3, String str4, String str5) {
        AppMonitorUtil.commitJsBridgeReturn(str, str3, str4, str5);
    }

    public void commitParamParseError(String str, String str2, String str3, String str4) {
        AppMonitorUtil.commitParamParseError(str, str2, str3, str4);
    }
}
