package com.uc.webview.export;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import com.uc.webview.export.GeolocationPermissions;
import com.uc.webview.export.annotations.Api;

@Api
@SuppressLint({"HandlerLeak"})
/* compiled from: U4Source */
public class WebChromeClient {

    @Api
    /* compiled from: U4Source */
    public interface CustomViewCallback {
        void onCustomViewHidden();
    }

    public Bitmap getDefaultVideoPoster() {
        return null;
    }

    public View getVideoLoadingProgressView() {
        return null;
    }

    public void getVisitedHistory(ValueCallback<String[]> valueCallback) {
    }

    public void onCloseWindow(WebView webView) {
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return false;
    }

    public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
        return false;
    }

    public void onGeolocationPermissionsHidePrompt() {
    }

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
    }

    public void onHideCustomView() {
    }

    public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
        return false;
    }

    public boolean onJsBeforeUnload(WebView webView, String str, String str2, JsResult jsResult) {
        return false;
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
        return false;
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        return false;
    }

    public void onProgressChanged(WebView webView, int i) {
    }

    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
    }

    public void onReceivedTitle(WebView webView, String str) {
    }

    public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
    }

    public void onRequestFocus(WebView webView) {
    }

    public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return false;
    }

    @Deprecated
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        valueCallback.onReceiveValue((Object) null);
    }

    @Api
    /* compiled from: U4Source */
    public static abstract class FileChooserParams {
        public static final int MODE_OPEN = 0;
        public static final int MODE_OPEN_FOLDER = 2;
        public static final int MODE_OPEN_MULTIPLE = 1;
        public static final int MODE_SAVE = 3;

        public abstract Intent createIntent();

        public abstract String[] getAcceptTypes();

        public abstract String getFilenameHint();

        public abstract int getMode();

        public abstract CharSequence getTitle();

        public abstract boolean isCaptureEnabled();

        public static Uri[] parseResult(int i, Intent intent) {
            ClipData clipData;
            if (i == 0 || i != -1) {
                return null;
            }
            Uri data = intent.getData();
            if (data != null) {
                return new Uri[]{data};
            } else if (Build.VERSION.SDK_INT < 18 || (clipData = intent.getClipData()) == null) {
                return null;
            } else {
                Uri[] uriArr = new Uri[clipData.getItemCount()];
                for (int i2 = 0; i2 < clipData.getItemCount(); i2++) {
                    uriArr[i2] = clipData.getItemAt(i2).getUri();
                }
                return uriArr;
            }
        }
    }
}
