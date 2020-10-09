package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BindingXTouchHandler extends AbstractEventHandler implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private float mDownX;
    private float mDownY;
    private double mDx;
    private double mDy;
    private GestureDetector mGestureDetector;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private VelocityTracker mVelocityTracker;

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public void onStart(@NonNull String str, @NonNull String str2) {
    }

    public BindingXTouchHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
        this.mGestureDetector = new GestureDetector(context, this, new Handler(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper()));
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            switch (motionEvent.getActionMasked()) {
                case 0:
                    this.mDownX = motionEvent.getRawX();
                    this.mDownY = motionEvent.getRawY();
                    fireEventByState("start", 0.0d, 0.0d, 0.0f, 0.0f, new Object[0]);
                    break;
                case 1:
                    this.mDownX = 0.0f;
                    this.mDownY = 0.0f;
                    clearExpressions();
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
                    fireEventByState("end", this.mDx, this.mDy, this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity(), new Object[0]);
                    this.mDx = 0.0d;
                    this.mDy = 0.0d;
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
                case 2:
                    if (this.mDownX != 0.0f || this.mDownY != 0.0f) {
                        this.mDx = (double) (motionEvent.getRawX() - this.mDownX);
                        this.mDy = (double) (motionEvent.getRawY() - this.mDownY);
                        break;
                    } else {
                        this.mDownX = motionEvent.getRawX();
                        this.mDownY = motionEvent.getRawY();
                        fireEventByState("start", 0.0d, 0.0d, 0.0f, 0.0f, new Object[0]);
                        break;
                    }
                    break;
                case 3:
                    this.mDownX = 0.0f;
                    this.mDownY = 0.0f;
                    clearExpressions();
                    fireEventByState("cancel", this.mDx, this.mDy, 0.0f, 0.0f, new Object[0]);
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
            }
        } catch (Exception e) {
            LogProxy.e("runtime error ", e);
        }
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float f3;
        float f4;
        if (motionEvent == null) {
            f4 = this.mDownX;
            f3 = this.mDownY;
        } else {
            float rawX = motionEvent.getRawX();
            f3 = motionEvent.getRawY();
            f4 = rawX;
        }
        if (motionEvent2 == null) {
            return false;
        }
        float rawX2 = motionEvent2.getRawX() - f4;
        float rawY = motionEvent2.getRawY() - f3;
        try {
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "[TouchHandler] pan moved. (x:%f,y:%f)", new Object[]{Float.valueOf(rawX2), Float.valueOf(rawY)}));
            }
            JSMath.applyXYToScope(this.mScope, (double) rawX2, (double) rawY, this.mPlatformManager.getResolutionTranslator());
            if (!evaluateExitExpression(this.mExitExpressionPair, this.mScope)) {
                consumeExpression(this.mExpressionHoldersMap, this.mScope, "pan");
            }
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
        return false;
    }

    public boolean onCreate(@NonNull String str, @NonNull String str2) {
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        if (findViewBy == null) {
            LogProxy.e("[ExpressionTouchHandler] onCreate failed. sourceView not found:" + str);
            return false;
        }
        findViewBy.setOnTouchListener(this);
        LogProxy.d("[ExpressionTouchHandler] onCreate success. {source:" + str + ",type:" + str2 + "}");
        return true;
    }

    public void onBindExpression(@NonNull String str, @Nullable Map<String, Object> map, @Nullable ExpressionPair expressionPair, @NonNull List<Map<String, Object>> list, @Nullable BindingXCore.JavaScriptCallback javaScriptCallback) {
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
    }

    public boolean onDisable(@NonNull String str, @NonNull String str2) {
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        if (findViewBy != null) {
            findViewBy.setOnTouchListener((View.OnTouchListener) null);
        }
        LogProxy.d("remove touch listener success.[" + str + "," + str2 + Operators.ARRAY_END_STR);
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mExpressionHoldersMap != null) {
            this.mExpressionHoldersMap.clear();
            this.mExpressionHoldersMap = null;
        }
        this.mExitExpressionPair = null;
        this.mCallback = null;
    }

    /* access modifiers changed from: protected */
    public void onExit(@NonNull Map<String, Object> map) {
        fireEventByState("exit", ((Double) map.get("internal_x")).doubleValue(), ((Double) map.get("internal_y")).doubleValue(), 0.0f, 0.0f, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, @NonNull Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, ((Double) map.get("internal_x")).doubleValue(), ((Double) map.get("internal_y")).doubleValue(), 0.0f, 0.0f, Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    private void fireEventByState(String str, double d, double d2, float f, float f2, Object... objArr) {
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str);
            double nativeToWeb = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d, new Object[0]);
            double nativeToWeb2 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d2, new Object[0]);
            hashMap.put("deltaX", Double.valueOf(nativeToWeb));
            hashMap.put("deltaY", Double.valueOf(nativeToWeb2));
            if ("end".equals(str)) {
                hashMap.put("velocityX", Float.valueOf(f));
                hashMap.put("velocityY", Float.valueOf(f2));
            }
            hashMap.put("token", this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + "," + nativeToWeb + "," + nativeToWeb2 + Operators.BRACKET_END_STR);
        }
    }
}
