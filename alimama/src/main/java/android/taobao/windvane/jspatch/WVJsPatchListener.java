package android.taobao.windvane.jspatch;

import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;

import java.lang.ref.WeakReference;

public class WVJsPatchListener implements WVEventListener {
    private WeakReference<IWVWebView> webviewReference;

    public WVJsPatchListener(IWVWebView iWVWebView) {
        this.webviewReference = new WeakReference<>(iWVWebView);
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        if (i != 3006) {
            if (i != 3014 || this.webviewReference == null) {
                return null;
            }
            IWVWebView iWVWebView = (IWVWebView) this.webviewReference.get();
            if (iWVWebView != null) {
                try {
                    iWVWebView.fireEvent("WV.Event.APP.TakeScreenshot", "{}");
                    return null;
                } catch (Exception unused) {
                    return null;
                }
            } else if (!TaoLog.getLogStatus()) {
                return null;
            } else {
                TaoLog.i("WVJsPatchListener", "WVJsPatchListener is free");
                return null;
            }
        } else if (this.webviewReference == null) {
            return null;
        } else {
            IWVWebView iWVWebView2 = (IWVWebView) this.webviewReference.get();
            if (iWVWebView2 != null) {
                iWVWebView2.fireEvent(objArr[0], objArr[1]);
                return null;
            } else if (!TaoLog.getLogStatus()) {
                return null;
            } else {
                TaoLog.i("WVJsPatchListener", "WVJsPatchListener is free");
                return null;
            }
        }
    }
}
