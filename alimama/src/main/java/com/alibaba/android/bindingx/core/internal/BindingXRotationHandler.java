package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.android.bindingx.core.BindingXEventType;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.RotationGestureDetector;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BindingXRotationHandler extends AbstractEventHandler implements View.OnTouchListener, RotationGestureDetector.OnRotationGestureListener {
    private double mAbsoluteRotationInDegrees;
    private RotationGestureDetector mRotationGestureDetector = new RotationGestureDetector(this);

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public void onStart(@NonNull String str, @NonNull String str2) {
    }

    public BindingXRotationHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    /* access modifiers changed from: protected */
    public void onExit(@NonNull Map<String, Object> map) {
        fireEventByState("exit", ((Double) map.get(UploadQueueMgr.MSGTYPE_REALTIME)).doubleValue(), new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, @NonNull Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, ((Double) map.get(UploadQueueMgr.MSGTYPE_REALTIME)).doubleValue(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    public boolean onCreate(@NonNull String str, @NonNull String str2) {
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        if (findViewBy == null) {
            LogProxy.e("[RotationHandler] onCreate failed. sourceView not found:" + str);
            return false;
        }
        findViewBy.setOnTouchListener(this);
        LogProxy.d("[RotationHandler] onCreate success. {source:" + str + ",type:" + str2 + "}");
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
            hashMap.put(BindingXEventType.TYPE_ROTATION, Double.valueOf(d));
            hashMap.put("token", this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + "," + d + Operators.BRACKET_END_STR);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.mRotationGestureDetector.onTouchEvent(motionEvent);
    }

    public void onRotation(RotationGestureDetector rotationGestureDetector) {
        try {
            this.mAbsoluteRotationInDegrees += rotationGestureDetector.getRotationInDegrees();
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "[RotationHandler] current rotation in degrees: %f", new Object[]{Double.valueOf(this.mAbsoluteRotationInDegrees)}));
            }
            JSMath.applyRotationInDegreesToScope(this.mScope, this.mAbsoluteRotationInDegrees);
            if (!evaluateExitExpression(this.mExitExpressionPair, this.mScope)) {
                consumeExpression(this.mExpressionHoldersMap, this.mScope, BindingXEventType.TYPE_ROTATION);
            }
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
    }

    public void onRotationBegin(RotationGestureDetector rotationGestureDetector) {
        LogProxy.d("[RotationHandler] rotation gesture begin");
        fireEventByState("start", 0.0d, new Object[0]);
    }

    public void onRotationEnd(RotationGestureDetector rotationGestureDetector) {
        LogProxy.d("[RotationHandler] rotation gesture end");
        fireEventByState("end", this.mAbsoluteRotationInDegrees, new Object[0]);
        this.mAbsoluteRotationInDegrees = 0.0d;
    }
}
