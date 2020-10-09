package com.taobao.weex.analyzer.core.inspector.view;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public final class ViewInspectorManager {
    private ExecutorService mExecutor;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public WXSDKInstance mInstance;
    /* access modifiers changed from: private */
    public OnInspectorListener mListener;
    /* access modifiers changed from: private */
    public ViewPropertiesSupplier mSupplier;

    interface OnInspectorListener {
        void onInspectorFailed(@NonNull String str);

        void onInspectorSuccess(@NonNull InspectorInfo inspectorInfo);
    }

    private ViewInspectorManager(@NonNull ExecutorService executorService, @NonNull ViewPropertiesSupplier viewPropertiesSupplier, @NonNull OnInspectorListener onInspectorListener) {
        this.mListener = onInspectorListener;
        this.mSupplier = viewPropertiesSupplier;
        this.mExecutor = executorService;
    }

    public void setInstance(@Nullable WXSDKInstance wXSDKInstance) {
        this.mInstance = wXSDKInstance;
    }

    @NonNull
    public static ViewInspectorManager newInstance(@NonNull ExecutorService executorService, @NonNull ViewPropertiesSupplier viewPropertiesSupplier, @NonNull OnInspectorListener onInspectorListener) {
        return new ViewInspectorManager(executorService, viewPropertiesSupplier, onInspectorListener);
    }

    /* access modifiers changed from: package-private */
    public void inspectByMotionEvent(@NonNull final MotionEvent motionEvent) {
        if (this.mExecutor != null && this.mInstance != null) {
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    WXComponent rootComponent;
                    View hostView;
                    if (ViewInspectorManager.this.mInstance != null && (rootComponent = ViewInspectorManager.this.mInstance.getRootComponent()) != null && (hostView = rootComponent.getHostView()) != null) {
                        View access$100 = ViewInspectorManager.this.findPossibleTouchedView(hostView, motionEvent.getRawX(), motionEvent.getRawY());
                        if (access$100 != null) {
                            WXComponent access$200 = ViewInspectorManager.this.findBoundComponentBy(access$100, rootComponent);
                            final InspectorInfo inspectorInfo = new InspectorInfo();
                            inspectorInfo.targetView = access$100;
                            inspectorInfo.targetComponent = access$200;
                            inspectorInfo.nativeViewInfo = Collections.emptyMap();
                            inspectorInfo.virtualViewInfo = Collections.emptyMap();
                            if (access$200 != null) {
                                inspectorInfo.simpleName = ViewUtils.getComponentName(access$200);
                            } else {
                                inspectorInfo.simpleName = access$100.getClass().getSimpleName();
                            }
                            if (ViewInspectorManager.this.mSupplier != null) {
                                Map<String, String> supplyPropertiesFromNativeView = ViewInspectorManager.this.mSupplier.supplyPropertiesFromNativeView(access$100);
                                Map<String, String> map = null;
                                if (access$200 != null) {
                                    map = ViewInspectorManager.this.mSupplier.supplyPropertiesFromVirtualView(access$200);
                                }
                                inspectorInfo.nativeViewInfo = supplyPropertiesFromNativeView;
                                inspectorInfo.virtualViewInfo = map;
                            }
                            if (ViewInspectorManager.this.mListener != null) {
                                ViewInspectorManager.this.mHandler.post(new Runnable() {
                                    public void run() {
                                        ViewInspectorManager.this.mListener.onInspectorSuccess(inspectorInfo);
                                    }
                                });
                            }
                        } else if (ViewInspectorManager.this.mListener != null) {
                            ViewInspectorManager.this.mHandler.post(new Runnable() {
                                public void run() {
                                    ViewInspectorManager.this.mListener.onInspectorFailed("target view not found");
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    @Nullable
    public View findPossibleTouchedView(@NonNull View view, float f, float f2) {
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(view);
        int[] iArr = new int[2];
        View view2 = null;
        while (!arrayDeque.isEmpty()) {
            View view3 = (View) arrayDeque.removeFirst();
            view3.getLocationInWindow(iArr);
            if (f > ((float) iArr[0]) && f < ((float) (iArr[0] + view3.getWidth())) && f2 > ((float) iArr[1]) && f2 < ((float) (iArr[1] + view3.getHeight()))) {
                view2 = view3;
            }
            if (view3 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view3;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    arrayDeque.add(viewGroup.getChildAt(i));
                }
            }
        }
        return view2;
    }

    /* access modifiers changed from: private */
    @Nullable
    public WXComponent findBoundComponentBy(@NonNull View view, @NonNull WXComponent wXComponent) {
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(wXComponent);
        WXComponent wXComponent2 = null;
        while (!arrayDeque.isEmpty()) {
            WXComponent wXComponent3 = (WXComponent) arrayDeque.removeFirst();
            View hostView = wXComponent3.getHostView();
            if (hostView != null && hostView.equals(view)) {
                wXComponent2 = wXComponent3;
            }
            if (wXComponent3 instanceof WXEmbed) {
                WXComponent nestedRootComponent = ViewUtils.getNestedRootComponent((WXEmbed) wXComponent3);
                if (nestedRootComponent != null) {
                    arrayDeque.add(nestedRootComponent);
                }
            } else if (wXComponent3 instanceof WXVContainer) {
                WXVContainer wXVContainer = (WXVContainer) wXComponent3;
                int childCount = wXVContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    arrayDeque.add(wXVContainer.getChild(i));
                }
            }
        }
        return wXComponent2;
    }

    public void destroy() {
        if (this.mExecutor != null) {
            this.mExecutor.shutdown();
            this.mExecutor = null;
        }
        this.mSupplier = null;
        this.mListener = null;
        this.mInstance = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
    }

    public static class InspectorInfo {
        public Map<String, String> nativeViewInfo;
        public String simpleName;
        @JSONField(serialize = false)
        public WXComponent targetComponent;
        @JSONField(serialize = false)
        public View targetView;
        public Map<String, String> virtualViewInfo;

        public String toString() {
            return "InspectorInfo{virtualViewInfo=" + this.virtualViewInfo + ", nativeViewInfo=" + this.nativeViewInfo + ", targetComponent=" + this.targetComponent + ", targetView=" + this.targetView + ", simpleName='" + this.simpleName + '\'' + '}';
        }
    }
}
