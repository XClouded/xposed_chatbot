package com.taobao.weex.analyzer.core.lint;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.core.HandlerThreadWrapper;
import com.taobao.weex.analyzer.pojo.HealthReport;
import com.taobao.weex.utils.WXLogUtils;

@Deprecated
public class StandardVDomMonitor implements IVDomMonitor, Handler.Callback {
    private static final String TAG = "VDomController";
    private static final int TRACK = 1;
    /* access modifiers changed from: private */
    public HandlerThreadWrapper mHandlerThreadWrapper = new HandlerThreadWrapper("vdom-tracker", this);
    /* access modifiers changed from: private */
    public WXSDKInstance mInstance;
    private LayoutChangeListener mLayoutChangeListener = new LayoutChangeListener();

    public void monitor(@NonNull WXSDKInstance wXSDKInstance) {
        this.mInstance = wXSDKInstance;
        View containerView = wXSDKInstance.getContainerView();
        if (containerView == null) {
            WXLogUtils.e(TAG, "host view is null");
        } else {
            containerView.getViewTreeObserver().addOnGlobalLayoutListener(this.mLayoutChangeListener);
        }
    }

    public void destroy() {
        View containerView;
        if (this.mHandlerThreadWrapper != null) {
            this.mHandlerThreadWrapper.quit();
            this.mHandlerThreadWrapper = null;
        }
        if (this.mInstance != null && this.mLayoutChangeListener != null && (containerView = this.mInstance.getContainerView()) != null) {
            if (Build.VERSION.SDK_INT < 16) {
                containerView.getViewTreeObserver().removeGlobalOnLayoutListener(this.mLayoutChangeListener);
            } else {
                containerView.getViewTreeObserver().removeOnGlobalLayoutListener(this.mLayoutChangeListener);
            }
        }
    }

    public boolean handleMessage(Message message) {
        if (message.what != 1) {
            return false;
        }
        try {
            HealthReport traverse = new DomTracker((WXSDKInstance) message.obj).traverse();
            if (traverse != null) {
                traverse.writeToConsole();
            }
        } catch (Exception e) {
            WXLogUtils.e(e.getMessage());
        }
        return true;
    }

    private class LayoutChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private LayoutChangeListener() {
        }

        public void onGlobalLayout() {
            if (StandardVDomMonitor.this.mInstance == null) {
                WXLogUtils.e("detect layout change but instance is null");
                return;
            }
            WXLogUtils.d(StandardVDomMonitor.TAG, "we detect that layout has changed for instance " + StandardVDomMonitor.this.mInstance.getInstanceId());
            if (StandardVDomMonitor.this.mHandlerThreadWrapper.isAlive()) {
                Message obtain = Message.obtain();
                obtain.what = 1;
                obtain.obj = StandardVDomMonitor.this.mInstance;
                StandardVDomMonitor.this.mHandlerThreadWrapper.getHandler().sendMessage(obtain);
            }
        }
    }
}
