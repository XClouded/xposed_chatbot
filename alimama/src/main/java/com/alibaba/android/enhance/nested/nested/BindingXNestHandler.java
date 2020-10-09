package com.alibaba.android.enhance.nested.nested;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.AbstractScrollEventHandler;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import com.alibaba.android.bindingx.plugin.weex.WXModuleUtils;
import com.alibaba.android.enhance.nested.nested.WXNestedHeader;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.component.WXComponent;
import java.util.List;
import java.util.Map;

public class BindingXNestHandler extends AbstractScrollEventHandler {
    public static String NEST_PLUGIN_NAME = "nestRefresh";
    private WXNestedHeader.OnNestedRefreshOffsetChangedListener mNestedRefreshOffsetChangedListener;

    /* access modifiers changed from: private */
    public boolean isSameDirection(int i, int i2) {
        return (i > 0 && i2 > 0) || (i < 0 && i2 < 0);
    }

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public void onStart(@NonNull String str, @NonNull String str2) {
    }

    public BindingXNestHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    public boolean onCreate(@NonNull String str, @NonNull String str2) {
        WXComponent findComponentByRef = WXModuleUtils.findComponentByRef(TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId, str);
        if (findComponentByRef == null) {
            LogProxy.e("[ExpressionScrollHandler]source component not found.");
            return false;
        } else if (!(findComponentByRef instanceof WXNestedHeader)) {
            return false;
        } else {
            this.mNestedRefreshOffsetChangedListener = new NestRefreshOffsetListener();
            ((WXNestedHeader) findComponentByRef).addOnRefreshOffsetChangedListener(this.mNestedRefreshOffsetChangedListener);
            return true;
        }
    }

    public void onBindExpression(@NonNull String str, @Nullable Map<String, Object> map, @Nullable ExpressionPair expressionPair, @NonNull List<Map<String, Object>> list, @Nullable BindingXCore.JavaScriptCallback javaScriptCallback) {
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
    }

    public boolean onDisable(@NonNull String str, @NonNull String str2) {
        super.onDisable(str, str2);
        WXComponent findComponentByRef = WXModuleUtils.findComponentByRef(TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId, str);
        if (findComponentByRef == null) {
            LogProxy.e("[ExpressionScrollHandler]source component not found.");
            return false;
        } else if (!(findComponentByRef instanceof WXNestedHeader)) {
            return false;
        } else {
            ((WXNestedHeader) findComponentByRef).removeOnRefreshOffsetChangedListener(this.mNestedRefreshOffsetChangedListener);
            return true;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mNestedRefreshOffsetChangedListener = null;
    }

    private class NestRefreshOffsetListener implements WXNestedHeader.OnNestedRefreshOffsetChangedListener {
        /* access modifiers changed from: private */
        public int mContentOffsetY;
        private int mLastDy;
        private int mTy;

        private NestRefreshOffsetListener() {
            this.mContentOffsetY = 0;
            this.mTy = 0;
            this.mLastDy = 0;
        }

        public void onOffsetChanged(int i) {
            boolean z;
            int i2 = -i;
            final int i3 = i2 - this.mContentOffsetY;
            this.mContentOffsetY = i2;
            if (i3 != 0) {
                if (!BindingXNestHandler.this.isSameDirection(i3, this.mLastDy)) {
                    this.mTy = this.mContentOffsetY;
                    z = true;
                } else {
                    z = false;
                }
                final int i4 = this.mContentOffsetY - this.mTy;
                this.mLastDy = i3;
                if (z) {
                    BindingXNestHandler.super.fireEventByState(BindingXConstants.STATE_TURNING, (double) BindingXNestHandler.this.mContentOffsetX, (double) this.mContentOffsetY, 0.0d, (double) i3, 0.0d, (double) i4, new Object[0]);
                }
                WXBridgeManager.getInstance().post(new Runnable() {
                    public void run() {
                        NestRefreshOffsetListener.super.handleScrollEvent(BindingXNestHandler.this.mContentOffsetX, NestRefreshOffsetListener.this.mContentOffsetY, 0, i3, 0, i4, BindingXNestHandler.NEST_PLUGIN_NAME);
                    }
                }, BindingXNestHandler.this.mInstanceId);
            }
        }
    }

    private static class ContentOffsetHolder {
        int x;
        int y;

        ContentOffsetHolder(int i, int i2) {
            this.x = i;
            this.y = i2;
        }
    }
}
