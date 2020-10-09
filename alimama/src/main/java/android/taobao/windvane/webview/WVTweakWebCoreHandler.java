package android.taobao.windvane.webview;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.util.TaoLog;
import android.webkit.WebView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WVTweakWebCoreHandler {
    static Handler sProxyHandler;

    public static void tryTweakWebCoreHandler() {
        if (Build.VERSION.SDK_INT == 15 && !"SAMSUNG".equalsIgnoreCase(Build.BRAND)) {
            if (TaoLog.getLogStatus()) {
                TaoLog.w("TweakWebCoreHandler", "BRAND: " + Build.BRAND);
            }
            tweakWebCoreHandler();
        }
    }

    public static void tweakWebCoredump(WebView webView) {
        try {
            Field declaredField = Class.forName("android.webkit.WebView").getDeclaredField("mProvider");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(webView);
            Class<?> cls = Class.forName("android.webkit.WebViewClassic");
            cls.cast(obj);
            Field declaredField2 = cls.getDeclaredField("mWebViewCore");
            declaredField2.setAccessible(true);
            Object obj2 = declaredField2.get(obj);
            Method declaredMethod = Class.forName("android.webkit.WebViewCore").getDeclaredMethod("sendMessage", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj2, new Object[]{170, 1, 0});
            declaredMethod.invoke(obj2, new Object[]{171, 1, 0});
            Method declaredMethod2 = cls.getDeclaredMethod("nativeDumpDisplayTree", new Class[]{String.class});
            declaredMethod2.setAccessible(true);
            declaredMethod2.invoke(obj, new Object[]{webView.getUrl()});
        } catch (Throwable th) {
            TaoLog.e("TweakWebCoreHandler", "tweakWebCoreHandler exception: " + th);
        }
    }

    private static void tweakWebCoreHandler() {
        if (sProxyHandler == null) {
            try {
                Field declaredField = Class.forName("android.webkit.WebViewCore").getDeclaredField("sWebCoreHandler");
                declaredField.setAccessible(true);
                Object obj = declaredField.get((Object) null);
                Method declaredMethod = Handler.class.getDeclaredMethod("getIMessenger", (Class[]) null);
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke(obj, (Object[]) null);
                sProxyHandler = new WebCoreProxyHandler((Handler) obj);
                if (invoke != null) {
                    Field declaredField2 = Handler.class.getDeclaredField("mMessenger");
                    declaredField2.setAccessible(true);
                    declaredField2.set(sProxyHandler, invoke);
                }
                declaredField.set((Object) null, sProxyHandler);
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("TweakWebCoreHandler", "sWebCoreHandler: " + obj);
                }
            } catch (Throwable th) {
                TaoLog.e("TweakWebCoreHandler", "tweakWebCoreHandler exception: " + th);
            }
            if (sProxyHandler == null) {
                sProxyHandler = new Handler();
            }
        }
    }

    static class WebCoreProxyHandler extends Handler {
        final Handler handler;

        public WebCoreProxyHandler(Handler handler2) {
            super(handler2.getLooper());
            this.handler = handler2;
        }

        public void handleMessage(Message message) {
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("WebCoreProxyHandler", "handle message: " + message.what);
                }
                this.handler.handleMessage(message);
            } catch (Throwable th) {
                TaoLog.e("WebCoreProxyHandler", "handleMessage exception: " + th);
            }
        }
    }
}
