package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.view.WXMaskView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;

public class WXMask extends WXVContainer {
    private static final int BOTTOM = 3;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 2;
    /* access modifiers changed from: private */
    public FrameLayout mFrameLayout;
    /* access modifiers changed from: private */
    public int mHeight;
    /* access modifiers changed from: private */
    public boolean mMaskViewIsAttached = false;
    /* access modifiers changed from: private */
    public WindowManager mWindowManager;

    public boolean isVirtualComponent() {
        return true;
    }

    public WXMask(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(@NonNull Context context) {
        WXMaskView wXMaskView = new WXMaskView(context);
        initWindow(wXMaskView);
        fireVisibleChangedEvent(true);
        return wXMaskView;
    }

    private void initWindow(final WXMaskView wXMaskView) {
        this.mWindowManager = (WindowManager) wXMaskView.getContext().getSystemService("window");
        this.mFrameLayout = new FrameLayout(wXMaskView.getContext()) {
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 4) {
                    return super.dispatchKeyEvent(keyEvent);
                }
                if (WXMask.this.mWindowManager == null || WXMask.this.mFrameLayout == null || !WXMask.this.mMaskViewIsAttached) {
                    return true;
                }
                WXMask.this.mWindowManager.removeViewImmediate(WXMask.this.mFrameLayout);
                return true;
            }
        };
        this.mHeight = 0;
        this.mFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > 21 && WXMask.this.mFrameLayout != null && WXMask.this.mWindowManager != null && wXMaskView != null) {
                    Rect rect = new Rect();
                    WXMask.this.mFrameLayout.getWindowVisibleDisplayFrame(rect);
                    if (!WXMask.this.isFullScreen()) {
                        WXLogUtils.w("Mask", "Mask is not fullscreen");
                        return;
                    }
                    int i = rect.bottom;
                    if (i != WXMask.this.mHeight) {
                        int unused = WXMask.this.mHeight = i;
                        WXBridgeManager.getInstance().post(new Runnable() {
                            public void run() {
                                WXBridgeManager.getInstance().setStyleHeight(WXMask.this.getInstanceId(), WXMask.this.getRef(), (float) WXMask.this.mHeight);
                            }
                        });
                    }
                }
            }
        });
        this.mFrameLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
                WXMask.this.fireVisibleChangedEvent(true);
                boolean unused = WXMask.this.mMaskViewIsAttached = true;
            }

            public void onViewDetachedFromWindow(View view) {
                WXMask.this.fireVisibleChangedEvent(false);
                boolean unused = WXMask.this.mMaskViewIsAttached = false;
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.type = 1000;
        layoutParams.gravity = 0;
        layoutParams.format = 1;
        layoutParams.softInputMode = 48;
        this.mWindowManager.addView(this.mFrameLayout, layoutParams);
        this.mFrameLayout.postDelayed(new Runnable() {
            public void run() {
                WXMask.this.mFrameLayout.addView(wXMaskView, new FrameLayout.LayoutParams(-1, -1));
            }
        }, 100);
    }

    public void removeVirtualComponent() {
        fireVisibleChangedEvent(false);
        if (this.mWindowManager != null && this.mFrameLayout != null && this.mMaskViewIsAttached) {
            this.mWindowManager.removeViewImmediate(this.mFrameLayout);
        }
    }

    /* access modifiers changed from: private */
    public void fireVisibleChangedEvent(boolean z) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("visible", Boolean.valueOf(z));
        fireEvent("visiblechanged", hashMap);
    }

    /* access modifiers changed from: private */
    public boolean isFullScreen() {
        try {
            if (!(getAttrs() == null || getAttrs().get("fullscreen") == null)) {
                return WXUtils.getBoolean(getAttrs().get("fullscreen"), true).booleanValue();
            }
        } catch (Throwable unused) {
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(View view, int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = get(0);
        int i8 = get(1);
        int i9 = get(2);
        int i10 = get(3);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, i2);
        layoutParams.setMargins(i7, i9, i8, i10);
        getHostView().setLayoutParams(layoutParams);
    }

    private int get(int i) {
        try {
            CSSShorthand margin = getMargin();
            WXStyle styles = getStyles();
            switch (i) {
                case 0:
                    return add(styles.getLeft(), margin.get(CSSShorthand.EDGE.LEFT));
                case 1:
                    return add(styles.getRight(), margin.get(CSSShorthand.EDGE.RIGHT));
                case 2:
                    return add(styles.getTop(), margin.get(CSSShorthand.EDGE.TOP));
                case 3:
                    return add(styles.getBottom(), margin.get(CSSShorthand.EDGE.BOTTOM));
                default:
                    return 0;
            }
        } catch (Throwable unused) {
            return 0;
        }
    }

    private int add(float f, float f2) {
        if (Float.isNaN(f)) {
            f = 0.0f;
        }
        if (Float.isNaN(f2)) {
            f2 = 0.0f;
        }
        return (int) (f + f2);
    }
}
