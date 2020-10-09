package com.taobao.weex.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WXFrameLayout extends BaseFrameLayout implements WXGestureObservable, IRenderStatus<WXDiv>, IRenderResult<WXDiv> {
    private WeakReference<WXDiv> mWeakReference;
    private WXGesture wxGesture;

    public WXFrameLayout(Context context) {
        super(context);
    }

    @Nullable
    public WXDiv getComponent() {
        if (this.mWeakReference != null) {
            return (WXDiv) this.mWeakReference.get();
        }
        return null;
    }

    public void holdComponent(WXDiv wXDiv) {
        this.mWeakReference = new WeakReference<>(wXDiv);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        return this.wxGesture != null ? dispatchTouchEvent | this.wxGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        WXSDKInstance sDKInstance;
        try {
            super.dispatchDrawInterval(canvas);
        } catch (Throwable th) {
            if (getComponent() != null) {
                notifyLayerOverFlow();
                if (!(getComponent() == null || (sDKInstance = WXSDKManager.getInstance().getSDKInstance(getComponent().getInstanceId())) == null || sDKInstance.getApmForInstance() == null || sDKInstance.getApmForInstance().hasReportLayerOverDraw)) {
                    sDKInstance.getApmForInstance().hasReportLayerOverDraw = true;
                    reportLayerOverFlowError();
                }
            }
            WXLogUtils.e("Layer overflow limit error", WXLogUtils.getStackTrace(th));
        }
    }

    private int reportLayerOverFlowError() {
        int calLayerDeep = calLayerDeep(this, 0);
        if (getComponent() != null) {
            String instanceId = getComponent().getInstanceId();
            WXErrorCode wXErrorCode = WXErrorCode.WX_RENDER_ERR_LAYER_OVERFLOW;
            WXExceptionUtils.commitCriticalExceptionRT(instanceId, wXErrorCode, "draw android view", WXErrorCode.WX_RENDER_ERR_LAYER_OVERFLOW.getErrorMsg() + "Layer overflow limit error: " + calLayerDeep + " layers!", (Map<String, String>) null);
        }
        return calLayerDeep;
    }

    private int calLayerDeep(View view, int i) {
        int i2 = i + 1;
        return (view.getParent() == null || !(view.getParent() instanceof View)) ? i2 : calLayerDeep((View) view.getParent(), i2);
    }

    public void notifyLayerOverFlow() {
        WXSDKInstance instance;
        if (getComponent() != null && (instance = getComponent().getInstance()) != null && instance.getLayerOverFlowListeners() != null) {
            for (String next : instance.getLayerOverFlowListeners()) {
                WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(instance.getInstanceId(), next);
                HashMap hashMap = new HashMap();
                hashMap.put("ref", next);
                hashMap.put(Constants.Weex.INSTANCEID, wXComponent.getInstanceId());
                wXComponent.fireEvent(Constants.Event.LAYEROVERFLOW, hashMap);
            }
        }
    }
}
