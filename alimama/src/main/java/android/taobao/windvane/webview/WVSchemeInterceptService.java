package android.taobao.windvane.webview;

public class WVSchemeInterceptService {
    private static WVSchemeIntercepterInterface mIntercepter;

    public static void registerWVURLintercepter(WVSchemeIntercepterInterface wVSchemeIntercepterInterface) {
        mIntercepter = wVSchemeIntercepterInterface;
    }

    public static WVSchemeIntercepterInterface getWVSchemeIntercepter() {
        return mIntercepter;
    }
}
