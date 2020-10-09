package android.taobao.windvane.monitor;

public interface WVMonitorInterface {
    void WebViewWrapType(String str);

    void commitCoreInitTime(long j, String str);

    void commitCoreTypeByPV(String str, String str2);

    void commitRenderType(String str, String str2, int i);
}
