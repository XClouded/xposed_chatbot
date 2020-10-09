package android.taobao.windvane.extra.uc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.widget.EditText;

import com.taobao.weex.ui.module.WXModalUIModule;
import com.uc.webview.export.GeolocationPermissions;
import com.uc.webview.export.JsPromptResult;
import com.uc.webview.export.JsResult;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebView;

public class WVUCWebChromeClient extends WebChromeClient {
    public static final int FilePathCallbackID = 15;
    private static final String KEY_CANCEL = (EnvUtil.isCN() ? "取消" : WXModalUIModule.CANCEL);
    private static final String KEY_CONFIRM = (EnvUtil.isCN() ? "确定" : WXModalUIModule.OK);
    private static final String KEY_FROM = (EnvUtil.isCN() ? "来自于：" : "From: ");
    private static final String TAG = "WVUCWebChromeClient";
    protected Context mContext;
    public ValueCallback<Uri[]> mFilePathCallback = null;
    public IWVWebView mWebView = null;

    public void openFileChooser(final ValueCallback<Uri> valueCallback) {
        TaoLog.d(TAG, " openFileChooser");
        if (this.mContext == null) {
            TaoLog.e(TAG, "context is null");
            return;
        }
        try {
            PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}).setTaskOnPermissionGranted(new Runnable() {
                public void run() {
                    TaoLog.d(WVUCWebChromeClient.TAG, " openFileChooser permission granted");
                    WVUCWebChromeClient.super.openFileChooser(valueCallback);
                }
            }).setTaskOnPermissionDenied(new Runnable() {
                public void run() {
                    TaoLog.d(WVUCWebChromeClient.TAG, " openFileChooser permission denied");
                }
            }).execute();
        } catch (Exception e) {
            TaoLog.e(TAG, e.getMessage());
        }
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        TaoLog.d(TAG, " onShowFileChooser");
        if (fileChooserParams == null || valueCallback == null) {
            return false;
        }
        this.mFilePathCallback = valueCallback;
        try {
            ((Activity) ((WVUCWebView) webView)._getContext()).startActivityForResult(Intent.createChooser(fileChooserParams.createIntent(), "choose"), 15);
            return true;
        } catch (Throwable th) {
            TaoLog.e(TAG, th.getMessage());
            th.printStackTrace();
            return false;
        }
    }

    public WVUCWebChromeClient() {
    }

    public WVUCWebChromeClient(Context context) {
        this.mContext = context;
    }

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        callback.invoke(str, true, false);
        super.onGeolocationPermissionsShowPrompt(str, callback);
    }

    public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
        if (webView.isDestroied()) {
            TaoLog.e(TAG, "cannot call [onJsAlert], for webview has been destroyed");
            return true;
        }
        Context _getContext = ((WVUCWebView) webView)._getContext();
        if (!(_getContext instanceof Activity) || !((Activity) _getContext).isFinishing()) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                String host = Uri.parse(str).getHost();
                builder.setTitle(KEY_FROM + host).setMessage(str2).setPositiveButton(KEY_CONFIRM, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsResult.confirm();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        jsResult.cancel();
                    }
                });
                AlertDialog create = builder.create();
                create.setCanceledOnTouchOutside(false);
                create.show();
            } catch (Throwable th) {
                TaoLog.e(TAG, th.getMessage());
                jsResult.confirm();
            }
            return true;
        }
        jsResult.confirm();
        return true;
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
        if (webView.isDestroied()) {
            TaoLog.e(TAG, "cannot call [onJsConfirm], for webview has been destroyed");
            return true;
        }
        Context _getContext = ((WVUCWebView) webView)._getContext();
        if (!(_getContext instanceof Activity) || !((Activity) _getContext).isFinishing()) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                String host = Uri.parse(str).getHost();
                builder.setTitle(KEY_FROM + host).setMessage(str2).setPositiveButton(KEY_CONFIRM, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsResult.confirm();
                    }
                }).setNeutralButton(KEY_CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsResult.cancel();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        jsResult.cancel();
                    }
                });
                AlertDialog create = builder.create();
                create.setCanceledOnTouchOutside(false);
                create.show();
            } catch (Throwable th) {
                TaoLog.e(TAG, th.getMessage());
                jsResult.confirm();
            }
            return true;
        }
        jsResult.confirm();
        return true;
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, final JsPromptResult jsPromptResult) {
        if (webView.isDestroied()) {
            TaoLog.e(TAG, "cannot call [onJsPrompt], for webview has been destroyed");
            return true;
        }
        Context _getContext = ((WVUCWebView) webView)._getContext();
        if ((_getContext instanceof Activity) && ((Activity) _getContext).isFinishing()) {
            jsPromptResult.confirm();
            return true;
        } else if (str3 == null || !str3.equals("wv_hybrid:")) {
            try {
                EditText editText = new EditText(webView.getContext());
                editText.setText(str3);
                String host = Uri.parse(str).getHost();
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                AlertDialog create = builder.setTitle(KEY_FROM + host).setView(editText).setMessage(str2).setPositiveButton(KEY_CONFIRM, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsPromptResult.confirm();
                    }
                }).setNegativeButton(KEY_CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsPromptResult.cancel();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        jsPromptResult.cancel();
                    }
                }).create();
                create.setCanceledOnTouchOutside(false);
                create.show();
            } catch (Throwable th) {
                TaoLog.e(TAG, th.getMessage());
                jsPromptResult.confirm();
            }
            return true;
        } else {
            WVJsBridge.getInstance().callMethod((IWVWebView) webView, str2);
            jsPromptResult.confirm("");
            return true;
        }
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (WVEventService.getInstance().onEvent(2001).isSuccess) {
            return true;
        }
        String message = consoleMessage.message();
        if (message != null) {
            if (!TextUtils.isEmpty(message) && message.startsWith("hybrid://")) {
                TaoLog.d(TAG, "Call from console.log");
                if (this.mWebView != null) {
                    WVJsBridge.getInstance().callMethod(this.mWebView, message);
                    return true;
                }
            }
            if (message.startsWith("wvNativeCallback")) {
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
        }
        if (TaoLog.getLogStatus()) {
            switch (AnonymousClass11.$SwitchMap$android$webkit$ConsoleMessage$MessageLevel[consoleMessage.messageLevel().ordinal()]) {
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
        return super.onConsoleMessage(consoleMessage);
    }

    /* renamed from: android.taobao.windvane.extra.uc.WVUCWebChromeClient$11  reason: invalid class name */
    static /* synthetic */ class AnonymousClass11 {
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
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebChromeClient.AnonymousClass11.<clinit>():void");
        }
    }

    public void onReceivedTitle(WebView webView, String str) {
        if (!WVUrlUtil.isCommonUrl(str)) {
            super.onReceivedTitle(webView, str);
            return;
        }
        TaoLog.i(TAG, "ignore default title : " + str);
    }
}
