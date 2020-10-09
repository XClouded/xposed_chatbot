package com.taobao.weex.analyzer.core.inspector.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.inspector.view.ViewInspectorManager;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.highlight.ViewHighlighter;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;
import com.taobao.weex.el.parse.Operators;
import java.util.concurrent.Executors;

public class InspectorView extends PermissionOverlayView {
    private static final int BTN_DISABLED_COLOR = 16777215;
    private static final int BTN_ENABLED_COLOR = -1127359431;
    private static final int INSPECTOR_COLOR = 1107296511;
    private View closeBtn;
    private GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public ViewInspectorManager mInspectorManager;
    /* access modifiers changed from: private */
    public WXInspectorItemView mNativeInspectorItemView;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private TextView mTips;
    private ViewHighlighter mViewHighlighter;
    /* access modifiers changed from: private */
    public WXInspectorItemView mVirtualInspectorItemView;
    /* access modifiers changed from: private */
    public TextView nativeLayoutBtn;
    /* access modifiers changed from: private */
    public TextView virtualDomBtn;

    public InspectorView(Context context, Config config) {
        super(context, true, config);
        this.mWidth = -1;
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_VIEW_INSPECTOR);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_inspector_view, (ViewGroup) null);
        this.mVirtualInspectorItemView = (WXInspectorItemView) inflate.findViewById(R.id.panel_virtual_dom);
        this.mVirtualInspectorItemView.setType(WXInspectorItemView.TYPE_VIRTUAL_DOM);
        this.mNativeInspectorItemView = (WXInspectorItemView) inflate.findViewById(R.id.panel_native_layout);
        this.mNativeInspectorItemView.setType(WXInspectorItemView.TYPE_NATIVE_LAYOUT);
        this.virtualDomBtn = (TextView) inflate.findViewById(R.id.btn_panel_virtual_dom);
        this.nativeLayoutBtn = (TextView) inflate.findViewById(R.id.btn_panel_native_layout);
        this.closeBtn = inflate.findViewById(R.id.close);
        this.mTips = (TextView) inflate.findViewById(R.id.tips);
        this.virtualDomBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InspectorView.this.mVirtualInspectorItemView.setVisibility(0);
                InspectorView.this.mNativeInspectorItemView.setVisibility(8);
                InspectorView.this.virtualDomBtn.setBackgroundColor(InspectorView.BTN_ENABLED_COLOR);
                InspectorView.this.nativeLayoutBtn.setBackgroundColor(16777215);
            }
        });
        this.nativeLayoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InspectorView.this.mVirtualInspectorItemView.setVisibility(8);
                InspectorView.this.mNativeInspectorItemView.setVisibility(0);
                InspectorView.this.nativeLayoutBtn.setBackgroundColor(InspectorView.BTN_ENABLED_COLOR);
                InspectorView.this.virtualDomBtn.setBackgroundColor(16777215);
            }
        });
        this.closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (InspectorView.this.isViewAttached && InspectorView.this.mOnCloseListener != null) {
                    InspectorView.this.mOnCloseListener.close(InspectorView.this);
                    InspectorView.this.dismiss();
                }
            }
        });
        return inflate;
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void bindInstance(@Nullable WXSDKInstance wXSDKInstance) {
        if (this.mInspectorManager != null) {
            this.mInspectorManager.setInstance(wXSDKInstance);
        }
    }

    public void receiveTouchEvent(@NonNull MotionEvent motionEvent) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(this.mContext, new GestureDetector.SimpleOnGestureListener() {
                public void onLongPress(MotionEvent motionEvent) {
                }

                public void onShowPress(MotionEvent motionEvent) {
                    if (InspectorView.this.mInspectorManager != null) {
                        InspectorView.this.mInspectorManager.inspectByMotionEvent(motionEvent);
                    }
                }
            });
        }
        this.mGestureDetector.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        this.mInspectorManager = ViewInspectorManager.newInstance(Executors.newSingleThreadExecutor(), new ViewPropertiesSupplier(), new ViewInspectorManager.OnInspectorListener() {
            public void onInspectorFailed(@NonNull String str) {
            }

            public void onInspectorSuccess(@NonNull ViewInspectorManager.InspectorInfo inspectorInfo) {
                InspectorView.this.notifyOnInspectorSuccess(inspectorInfo);
            }
        });
    }

    /* access modifiers changed from: private */
    public void notifyOnInspectorSuccess(@NonNull ViewInspectorManager.InspectorInfo inspectorInfo) {
        if (inspectorInfo.targetView != null && this.mWholeView != null) {
            if (this.mViewHighlighter == null) {
                this.mViewHighlighter = ViewHighlighter.newInstance();
            }
            this.mViewHighlighter.setHighlightedView(inspectorInfo.targetView, INSPECTOR_COLOR);
            if (inspectorInfo.targetComponent != null) {
                String componentName = ViewUtils.getComponentName(inspectorInfo.targetComponent);
                TextView textView = this.mTips;
                textView.setText("tips:你选中了weex元素[" + componentName + Operators.ARRAY_END_STR);
            } else if (!(this.mContext == null || inspectorInfo.targetView == null)) {
                TextView textView2 = this.mTips;
                textView2.setText("tips:你选中了native元素[" + inspectorInfo.targetView.getClass().getSimpleName() + Operators.ARRAY_END_STR);
            }
            if (inspectorInfo.virtualViewInfo != null) {
                this.mVirtualInspectorItemView.inflateData(inspectorInfo);
            }
            if (inspectorInfo.nativeViewInfo != null) {
                this.mNativeInspectorItemView.inflateData(inspectorInfo);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        this.mGestureDetector = null;
        if (this.mInspectorManager != null) {
            this.mInspectorManager.destroy();
            this.mInspectorManager = null;
        }
        if (this.mViewHighlighter != null) {
            this.mViewHighlighter.clearHighlight();
        }
        this.mViewHighlighter = null;
    }
}
