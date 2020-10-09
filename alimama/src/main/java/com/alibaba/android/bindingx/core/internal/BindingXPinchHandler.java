package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.BindingXEventType;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BindingXPinchHandler extends AbstractEventHandler implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private double mAbsoluteScaleFactor = 1.0d;
    private boolean mInProgress;
    private int[] mPointerIds = new int[2];
    private ScaleGestureDetector mScaleGestureDetector;

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
    }

    public void onStart(@NonNull String str, @NonNull String str2) {
    }

    public BindingXPinchHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
        Handler handler = new Handler(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
        if (Build.VERSION.SDK_INT >= 19) {
            this.mScaleGestureDetector = new ScaleGestureDetector(context, this, handler);
        } else {
            this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        }
    }

    /* access modifiers changed from: protected */
    public void onExit(@NonNull Map<String, Object> map) {
        fireEventByState("exit", ((Double) map.get("s")).doubleValue(), new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, @NonNull Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, ((Double) map.get("s")).doubleValue(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    public boolean onCreate(@NonNull String str, @NonNull String str2) {
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        if (findViewBy == null) {
            LogProxy.e("[BindingXPinchHandler] onCreate failed. sourceView not found:" + str);
            return false;
        }
        findViewBy.setOnTouchListener(this);
        LogProxy.d("[BindingXPinchHandler] onCreate success. {source:" + str + ",type:" + str2 + "}");
        return true;
    }

    public boolean onDisable(@NonNull String str, @NonNull String str2) {
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        LogProxy.d("remove touch listener success.[" + str + "," + str2 + Operators.ARRAY_END_STR);
        if (findViewBy == null) {
            return false;
        }
        findViewBy.setOnTouchListener((View.OnTouchListener) null);
        return true;
    }

    private void fireEventByState(String str, double d, Object... objArr) {
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str);
            hashMap.put("scale", Double.valueOf(d));
            hashMap.put("token", this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + "," + d + Operators.BRACKET_END_STR);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int pointerId;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.mInProgress = false;
                this.mPointerIds[0] = motionEvent.getPointerId(motionEvent.getActionIndex());
                this.mPointerIds[1] = -1;
                break;
            case 1:
                pinchEnd();
                break;
            case 5:
                if (!this.mInProgress) {
                    this.mPointerIds[1] = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.mInProgress = true;
                    pinchStart();
                    break;
                }
                break;
            case 6:
                if (this.mInProgress && ((pointerId = motionEvent.getPointerId(motionEvent.getActionIndex())) == this.mPointerIds[0] || pointerId == this.mPointerIds[1])) {
                    pinchEnd();
                    break;
                }
        }
        return this.mScaleGestureDetector.onTouchEvent(motionEvent);
    }

    private void pinchStart() {
        LogProxy.d("[PinchHandler] pinch gesture begin");
        fireEventByState("start", 1.0d, new Object[0]);
    }

    private void pinchEnd() {
        if (this.mInProgress) {
            LogProxy.d("[PinchHandler] pinch gesture end");
            fireEventByState("end", this.mAbsoluteScaleFactor, new Object[0]);
            this.mInProgress = false;
            this.mPointerIds[0] = -1;
            this.mPointerIds[1] = -1;
            this.mAbsoluteScaleFactor = 1.0d;
        }
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
            return true;
        }
        double d = this.mAbsoluteScaleFactor;
        double d2 = (double) scaleFactor;
        Double.isNaN(d2);
        this.mAbsoluteScaleFactor = d * d2;
        try {
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "[PinchHandler] current scale factor: %f", new Object[]{Double.valueOf(this.mAbsoluteScaleFactor)}));
            }
            JSMath.applyScaleFactorToScope(this.mScope, this.mAbsoluteScaleFactor);
            if (!evaluateExitExpression(this.mExitExpressionPair, this.mScope)) {
                consumeExpression(this.mExpressionHoldersMap, this.mScope, BindingXEventType.TYPE_PINCH);
            }
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
        return true;
    }
}
