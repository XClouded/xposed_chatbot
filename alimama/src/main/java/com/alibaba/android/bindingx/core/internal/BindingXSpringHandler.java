package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.BindingXEventType;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.PhysicsAnimationDriver;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BindingXSpringHandler extends AbstractEventHandler implements PhysicsAnimationDriver.OnAnimationUpdateListener, PhysicsAnimationDriver.OnAnimationEndListener {
    private SpringAnimationDriver mSpringAnimationDriver;

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public boolean onCreate(@NonNull String str, @NonNull String str2) {
        return true;
    }

    public void onStart(@NonNull String str, @NonNull String str2) {
    }

    public BindingXSpringHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    public void onBindExpression(@NonNull String str, @Nullable Map<String, Object> map, @Nullable ExpressionPair expressionPair, @NonNull List<Map<String, Object>> list, @Nullable BindingXCore.JavaScriptCallback javaScriptCallback) {
        double d;
        double d2;
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
        if (this.mSpringAnimationDriver != null) {
            double currentVelocity = this.mSpringAnimationDriver.getCurrentVelocity();
            double currentValue = this.mSpringAnimationDriver.getCurrentValue();
            this.mSpringAnimationDriver.cancel();
            d = currentVelocity;
            d2 = currentValue;
        } else {
            d2 = 0.0d;
            d = 0.0d;
        }
        this.mSpringAnimationDriver = new SpringAnimationDriver();
        this.mSpringAnimationDriver.setOnAnimationUpdateListener(this);
        this.mSpringAnimationDriver.setOnAnimationEndListener(this);
        this.mSpringAnimationDriver.start(resolveParams(this.mOriginParams, d2, d));
        fireEventByState("start", this.mSpringAnimationDriver.getCurrentValue(), this.mSpringAnimationDriver.getCurrentVelocity(), new Object[0]);
    }

    private Map<String, Object> resolveParams(Map<String, Object> map, double d, double d2) {
        if (map == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> mapValue = Utils.getMapValue(map, BindingXConstants.KEY_EVENT_CONFIG);
        if (mapValue.get("initialVelocity") == null) {
            if (mapValue.isEmpty()) {
                mapValue = new HashMap<>();
            }
            mapValue.put("initialVelocity", Double.valueOf(d2));
        }
        if (mapValue.get("fromValue") == null) {
            if (mapValue.isEmpty()) {
                mapValue = new HashMap<>();
            }
            mapValue.put("fromValue", Double.valueOf(d));
        }
        return mapValue;
    }

    /* access modifiers changed from: protected */
    public void onExit(@NonNull Map<String, Object> map) {
        fireEventByState("exit", ((Double) map.get("p")).doubleValue(), ((Double) map.get("v")).doubleValue(), new Object[0]);
        if (this.mSpringAnimationDriver != null) {
            this.mSpringAnimationDriver.cancel();
        }
    }

    private void fireEventByState(String str, double d, double d2, Object... objArr) {
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str);
            hashMap.put("position", Double.valueOf(d));
            hashMap.put("velocity", Double.valueOf(d2));
            hashMap.put("token", this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + ",position:" + d + ",velocity:" + d2 + Operators.BRACKET_END_STR);
        }
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, @NonNull Map<String, Object> map) {
        if (this.mSpringAnimationDriver != null) {
            fireEventByState(BindingXConstants.STATE_INTERCEPTOR, this.mSpringAnimationDriver.getCurrentValue(), this.mSpringAnimationDriver.getCurrentVelocity(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
        }
    }

    public boolean onDisable(@NonNull String str, @NonNull String str2) {
        clearExpressions();
        if (this.mSpringAnimationDriver == null) {
            return true;
        }
        fireEventByState("end", this.mSpringAnimationDriver.getCurrentValue(), this.mSpringAnimationDriver.getCurrentVelocity(), new Object[0]);
        this.mSpringAnimationDriver.setOnAnimationEndListener((PhysicsAnimationDriver.OnAnimationEndListener) null);
        this.mSpringAnimationDriver.setOnAnimationUpdateListener((PhysicsAnimationDriver.OnAnimationUpdateListener) null);
        this.mSpringAnimationDriver.cancel();
        return true;
    }

    public void onAnimationUpdate(@NonNull PhysicsAnimationDriver physicsAnimationDriver, double d, double d2) {
        if (LogProxy.sEnableLog) {
            LogProxy.v(String.format(Locale.getDefault(), "animation update, [value: %f, velocity: %f]", new Object[]{Double.valueOf(d), Double.valueOf(d2)}));
        }
        try {
            JSMath.applySpringValueToScope(this.mScope, d, d2);
            if (!evaluateExitExpression(this.mExitExpressionPair, this.mScope)) {
                consumeExpression(this.mExpressionHoldersMap, this.mScope, BindingXEventType.TYPE_SPRING);
            }
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
    }

    public void onAnimationEnd(@NonNull PhysicsAnimationDriver physicsAnimationDriver, double d, double d2) {
        if (LogProxy.sEnableLog) {
            LogProxy.v(String.format(Locale.getDefault(), "animation end, [value: %f, velocity: %f]", new Object[]{Double.valueOf(d), Double.valueOf(d2)}));
        }
        fireEventByState("end", this.mSpringAnimationDriver.getCurrentValue(), this.mSpringAnimationDriver.getCurrentVelocity(), new Object[0]);
    }
}
