package android.taobao.windvane.jsbridge;

import android.taobao.windvane.util.TaoLog;

public class WVAsyncAuthCheckCallBackforJsBridge implements WVAsyncAuthCheck.AsyncAuthCheckCallBack {
    String TAG = "WVAsyncAuthCheckCallBackforJsBridge";

    public void callBackSuccess(String str, WVCallMethodContext wVCallMethodContext) {
        WVJsBridge.aftercallMethod(wVCallMethodContext, str);
        TaoLog.w(this.TAG, "Async preprocessor callBackSuccess ");
    }

    public void callBackFail(String str, WVCallMethodContext wVCallMethodContext) {
        WVJsBridge.startCall(3, wVCallMethodContext);
        TaoLog.w(this.TAG, "Async preprocessor callBackSuccess ,");
    }
}
