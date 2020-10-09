package android.taobao.windvane.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class WVWebChromeClient extends WebChromeClient {
    private static final long MAX_QUOTA = 20971520;
    private static final String TAG = "WVWebChromeClient";
    public WebChromeClient extraWebChromeClient = null;
    protected Context mContext;
    public IWVWebView mWebView = null;

    public WVWebChromeClient() {
    }

    public WVWebChromeClient(Context context) {
        this.mContext = context;
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (WVEventService.getInstance().onEvent(2001).isSuccess) {
            return true;
        }
        String message = consoleMessage.message();
        if (!TextUtils.isEmpty(message) && message.startsWith("hybrid://")) {
            TaoLog.d(TAG, "Call from console.log");
            if (this.mWebView != null) {
                WVJsBridge.getInstance().callMethod(this.mWebView, message);
                return true;
            }
        }
        if (message == null || !message.startsWith("wvNativeCallback")) {
            if (TaoLog.getLogStatus()) {
                switch (AnonymousClass1.$SwitchMap$android$webkit$ConsoleMessage$MessageLevel[consoleMessage.messageLevel().ordinal()]) {
                    case 1:
                        TaoLog.d(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                    case 2:
                        TaoLog.e(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                    case 3:
                        TaoLog.w(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                    default:
                        TaoLog.d(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                }
            }
            if (this.extraWebChromeClient != null) {
                return this.extraWebChromeClient.onConsoleMessage(consoleMessage);
            }
            return super.onConsoleMessage(consoleMessage);
        }
        String substring = message.substring(message.indexOf("/") + 1);
        int indexOf = substring.indexOf("/");
        String substring2 = substring.substring(0, indexOf);
        String substring3 = substring.substring(indexOf + 1);
        ValueCallback<String> nativeCallback = WVNativeCallbackUtil.getNativeCallback(substring2);
        if (nativeCallback != null) {
            nativeCallback.onReceiveValue(substring3);
            WVNativeCallbackUtil.clearNativeCallback(substring2);
        } else {
            TaoLog.e(TAG, "NativeCallback failed: " + substring3);
        }
        return true;
    }

    /* renamed from: android.taobao.windvane.webview.WVWebChromeClient$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$webkit$ConsoleMessage$MessageLevel = new int[ConsoleMessage.MessageLevel.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                android.webkit.ConsoleMessage$MessageLevel[] r0 = android.webkit.ConsoleMessage.MessageLevel.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$webkit$ConsoleMessage$MessageLevel = r0
                int[] r0 = $SwitchMap$android$webkit$ConsoleMessage$MessageLevel     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.webkit.ConsoleMessage$MessageLevel r1 = android.webkit.ConsoleMessage.MessageLevel.DEBUG     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$android$webkit$ConsoleMessage$MessageLevel     // Catch:{ NoSuchFieldError -> 0x001f }
                android.webkit.ConsoleMessage$MessageLevel r1 = android.webkit.ConsoleMessage.MessageLevel.ERROR     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$android$webkit$ConsoleMessage$MessageLevel     // Catch:{ NoSuchFieldError -> 0x002a }
                android.webkit.ConsoleMessage$MessageLevel r1 = android.webkit.ConsoleMessage.MessageLevel.WARNING     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$android$webkit$ConsoleMessage$MessageLevel     // Catch:{ NoSuchFieldError -> 0x0035 }
                android.webkit.ConsoleMessage$MessageLevel r1 = android.webkit.ConsoleMessage.MessageLevel.LOG     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$android$webkit$ConsoleMessage$MessageLevel     // Catch:{ NoSuchFieldError -> 0x0040 }
                android.webkit.ConsoleMessage$MessageLevel r1 = android.webkit.ConsoleMessage.MessageLevel.TIP     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.webview.WVWebChromeClient.AnonymousClass1.<clinit>():void");
        }
    }

    @TargetApi(5)
    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(str, callback);
        callback.invoke(str, true, false);
    }

    @TargetApi(5)
    public void onExceededDatabaseQuota(String str, String str2, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
        if (j2 < MAX_QUOTA) {
            quotaUpdater.updateQuota(j2);
        } else {
            quotaUpdater.updateQuota(j);
        }
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "onJsPrompt: %s; defaultValue: %s; url: %s", str2, str3, str);
        }
        if (webView instanceof IWVWebView) {
            if (WVEventService.getInstance().onEvent(2003, (IWVWebView) webView, str, str2, str3, jsPromptResult).isSuccess) {
                return true;
            }
        }
        if (str3 != null && str3.equals("wv_hybrid:")) {
            WVJsBridge.getInstance().callMethod((IWVWebView) (WVWebView) webView, str2);
            jsPromptResult.confirm("");
            return true;
        } else if (this.extraWebChromeClient != null) {
            return this.extraWebChromeClient.onJsPrompt(webView, str, str2, str3, jsPromptResult);
        } else {
            return false;
        }
    }

    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
    }
}
