package android.taobao.windvane.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVWebView;

public abstract class WVApiPlugin {
    public static final int REQUEST_MULTI_PICK_PHOTO = 4003;
    public static final int REQUEST_PICK_PHONE = 4003;
    public static final int REQUEST_PICK_PHOTO = 4002;
    public static final int REQUEST_TAKE_PHOTO = 4001;
    /* access modifiers changed from: protected */
    public boolean isAlive = true;
    /* access modifiers changed from: protected */
    public Context mContext;
    /* access modifiers changed from: protected */
    public IWVWebView mWebView;
    protected Object paramObj;

    /* access modifiers changed from: protected */
    public abstract boolean execute(String str, String str2, WVCallBackContext wVCallBackContext);

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void initialize(Context context, IWVWebView iWVWebView) {
        this.mContext = context;
        this.mWebView = iWVWebView;
    }

    @Deprecated
    public void initialize(Context context, WVWebView wVWebView) {
        initialize(context, wVWebView, (Object) null);
    }

    @Deprecated
    public void initialize(Context context, IWVWebView iWVWebView, Object obj) {
        initialize(context, iWVWebView);
    }

    public void initialize(Context context, IWVWebView iWVWebView, Object obj, String str) {
        this.paramObj = obj;
        initialize(context, iWVWebView, obj);
    }

    public boolean executeSafe(String str, String str2, WVCallBackContext wVCallBackContext) {
        try {
            return execute(str, str2, wVCallBackContext);
        } catch (Exception e) {
            TaoLog.e("WVJsBridge", e.getMessage());
            return false;
        }
    }

    public void onDestroy() {
        this.isAlive = false;
    }

    public void onPause() {
        this.isAlive = false;
    }

    public void onResume() {
        this.isAlive = true;
    }
}
