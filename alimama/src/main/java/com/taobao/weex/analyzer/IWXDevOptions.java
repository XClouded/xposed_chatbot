package com.taobao.weex.analyzer;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;

public interface IWXDevOptions {
    void onCreate();

    void onDestroy();

    void onException(WXSDKInstance wXSDKInstance, String str, String str2);

    boolean onKeyUp(int i, KeyEvent keyEvent);

    void onPause();

    void onReceiveTouchEvent(MotionEvent motionEvent);

    void onReceiveWindmillPerformanceLog(String str);

    void onResume();

    void onStart();

    void onStop();

    void onWeexRenderSuccess(@Nullable WXSDKInstance wXSDKInstance);

    View onWeexViewCreated(WXSDKInstance wXSDKInstance, View view);

    void onWindmillException(Object obj, String str, String str2);

    void onWindmillRenderSuccess(@Nullable Object obj);

    View onWindmillViewCreated(View view);
}
