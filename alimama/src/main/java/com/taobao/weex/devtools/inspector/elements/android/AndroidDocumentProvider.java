package com.taobao.weex.devtools.inspector.elements.android;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.Predicate;
import com.taobao.weex.devtools.common.ThreadBound;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.common.android.ViewUtil;
import com.taobao.weex.devtools.inspector.elements.Descriptor;
import com.taobao.weex.devtools.inspector.elements.DescriptorMap;
import com.taobao.weex.devtools.inspector.elements.DocumentProvider;
import com.taobao.weex.devtools.inspector.elements.DocumentProviderListener;
import com.taobao.weex.devtools.inspector.elements.NodeDescriptor;
import com.taobao.weex.devtools.inspector.elements.ObjectDescriptor;
import com.taobao.weex.devtools.inspector.helper.ThreadBoundProxy;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

final class AndroidDocumentProvider extends ThreadBoundProxy implements DocumentProvider, AndroidDescriptorHost {
    private static final int INSPECT_HOVER_COLOR = 1077952767;
    private static final int INSPECT_OVERLAY_COLOR = 1090519039;
    private static final long REPORT_CHANGED_INTERVAL_MS = 1000;
    /* access modifiers changed from: private */
    public final Application mApplication;
    private final DescriptorMap mDescriptorMap;
    private final AndroidDocumentRoot mDocumentRoot;
    /* access modifiers changed from: private */
    public final ViewHighlighter mHighlighter;
    private final InspectModeHandler mInspectModeHandler;
    /* access modifiers changed from: private */
    public boolean mIsReportChangesTimerPosted = false;
    /* access modifiers changed from: private */
    @Nullable
    public DocumentProviderListener mListener;
    private final Runnable mReportChangesTimer = new Runnable() {
        public void run() {
            boolean unused = AndroidDocumentProvider.this.mIsReportChangesTimerPosted = false;
            if (AndroidDocumentProvider.this.mListener != null) {
                AndroidDocumentProvider.this.mListener.onPossiblyChanged();
                boolean unused2 = AndroidDocumentProvider.this.mIsReportChangesTimerPosted = true;
                AndroidDocumentProvider.this.postDelayed(this, AndroidDocumentProvider.REPORT_CHANGED_INTERVAL_MS);
            }
        }
    };

    public AndroidDocumentProvider(Application application, ThreadBound threadBound) {
        super(threadBound);
        this.mApplication = (Application) Util.throwIfNull(application);
        this.mDocumentRoot = new AndroidDocumentRoot(application);
        this.mDescriptorMap = new DescriptorMap().beginInit().register(WXSDKInstance.class, new WXSDKInstanceDescriptor()).register(WXComponent.class, new WXComponentDescriptor()).register(WXVContainer.class, new WXVContainerDescriptor()).register(Activity.class, new ActivityDescriptor()).register(AndroidDocumentRoot.class, this.mDocumentRoot).register(Application.class, new ApplicationDescriptor()).register(Dialog.class, new DialogDescriptor());
        DialogFragmentDescriptor.register(this.mDescriptorMap);
        FragmentDescriptor.register(this.mDescriptorMap).register(Object.class, new ObjectDescriptor()).register(TextView.class, new TextViewDescriptor()).register(View.class, new ViewDescriptor()).register(ViewGroup.class, new ViewGroupDescriptor()).register(Window.class, new WindowDescriptor()).setHost(this).endInit();
        this.mHighlighter = ViewHighlighter.newInstance();
        this.mInspectModeHandler = new InspectModeHandler();
    }

    public void dispose() {
        verifyThreadAccess();
        this.mHighlighter.clearHighlight();
        this.mInspectModeHandler.disable();
        removeCallbacks(this.mReportChangesTimer);
        this.mIsReportChangesTimerPosted = false;
        this.mListener = null;
    }

    public void setListener(DocumentProviderListener documentProviderListener) {
        verifyThreadAccess();
        this.mListener = documentProviderListener;
        if (this.mListener == null && this.mIsReportChangesTimerPosted) {
            this.mIsReportChangesTimerPosted = false;
            removeCallbacks(this.mReportChangesTimer);
        } else if (this.mListener != null && !this.mIsReportChangesTimerPosted) {
            this.mIsReportChangesTimerPosted = true;
            postDelayed(this.mReportChangesTimer, REPORT_CHANGED_INTERVAL_MS);
        }
    }

    public Object getRootElement() {
        verifyThreadAccess();
        return this.mDocumentRoot;
    }

    public NodeDescriptor getNodeDescriptor(Object obj) {
        verifyThreadAccess();
        return getDescriptor(obj);
    }

    public void highlightElement(Object obj, int i) {
        verifyThreadAccess();
        View highlightingView = getHighlightingView(obj);
        if (highlightingView == null) {
            this.mHighlighter.clearHighlight();
        } else {
            this.mHighlighter.setHighlightedView(highlightingView, i);
        }
    }

    public void hideHighlight() {
        verifyThreadAccess();
        this.mHighlighter.clearHighlight();
    }

    public void setInspectModeEnabled(boolean z) {
        verifyThreadAccess();
        if (z) {
            this.mInspectModeHandler.enable();
        } else {
            this.mInspectModeHandler.disable();
        }
    }

    public void setAttributesAsText(Object obj, String str) {
        verifyThreadAccess();
        Descriptor descriptor = this.mDescriptorMap.get(obj.getClass());
        if (descriptor != null) {
            descriptor.setAttributesAsText(obj, str);
        }
    }

    public Descriptor getDescriptor(Object obj) {
        if (obj == null) {
            return null;
        }
        return this.mDescriptorMap.get(obj.getClass());
    }

    public void onAttributeModified(Object obj, String str, String str2) {
        if (this.mListener != null) {
            this.mListener.onAttributeModified(obj, str, str2);
        }
    }

    public void onAttributeRemoved(Object obj, String str) {
        if (this.mListener != null) {
            this.mListener.onAttributeRemoved(obj, str);
        }
    }

    public View getHighlightingView(Object obj) {
        if (obj == null) {
            return null;
        }
        Descriptor descriptor = null;
        Class<?> cls = obj.getClass();
        View view = null;
        while (view == null && cls != null) {
            Descriptor descriptor2 = this.mDescriptorMap.get(cls);
            if (descriptor2 == null) {
                return null;
            }
            if (descriptor2 != descriptor && (descriptor2 instanceof HighlightableDescriptor)) {
                view = ((HighlightableDescriptor) descriptor2).getViewForHighlighting(obj);
            }
            cls = cls.getSuperclass();
            descriptor = descriptor2;
        }
        return view;
    }

    /* access modifiers changed from: private */
    public void getWindows(final Accumulator<Window> accumulator) {
        Descriptor descriptor = getDescriptor(this.mApplication);
        if (descriptor != null) {
            descriptor.getChildren(this.mApplication, new Accumulator<Object>() {
                public void store(Object obj) {
                    if (obj instanceof Window) {
                        accumulator.store((Window) obj);
                        return;
                    }
                    Descriptor descriptor = AndroidDocumentProvider.this.getDescriptor(obj);
                    if (descriptor != null) {
                        descriptor.getChildren(obj, this);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void getWXSDKInstances() {
        Descriptor descriptor = getDescriptor(this.mDocumentRoot);
        if (descriptor != null) {
            descriptor.getChildren(this.mDocumentRoot, new Accumulator<Object>() {
                public void store(Object obj) {
                    Descriptor descriptor = AndroidDocumentProvider.this.getDescriptor(obj);
                    if (descriptor != null) {
                        descriptor.getChildren(obj, this);
                    }
                }
            });
        }
    }

    private final class InspectModeHandler {
        /* access modifiers changed from: private */
        public List<View> mOverlays;
        /* access modifiers changed from: private */
        public final Predicate<View> mViewSelector;

        private InspectModeHandler() {
            this.mViewSelector = new Predicate<View>() {
                public boolean apply(View view) {
                    return !(view instanceof DocumentHiddenView);
                }
            };
        }

        public void enable() {
            AndroidDocumentProvider.this.verifyThreadAccess();
            if (this.mOverlays != null) {
                disable();
            }
            this.mOverlays = new ArrayList();
            AndroidDocumentProvider.this.getWXSDKInstances();
            AndroidDocumentProvider.this.getWindows(new Accumulator<Window>() {
                public void store(Window window) {
                    if (window.peekDecorView() instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) window.peekDecorView();
                        OverlayView overlayView = new OverlayView(AndroidDocumentProvider.this.mApplication);
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.width = -1;
                        layoutParams.height = -1;
                        viewGroup.addView(overlayView, layoutParams);
                        viewGroup.bringChildToFront(overlayView);
                        InspectModeHandler.this.mOverlays.add(overlayView);
                    }
                }
            });
        }

        public void disable() {
            AndroidDocumentProvider.this.verifyThreadAccess();
            if (this.mOverlays != null) {
                for (int i = 0; i < this.mOverlays.size(); i++) {
                    View view = this.mOverlays.get(i);
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                this.mOverlays = null;
            }
        }

        private final class OverlayView extends DocumentHiddenView {
            public OverlayView(Context context) {
                super(context);
            }

            /* access modifiers changed from: protected */
            public void onDraw(Canvas canvas) {
                canvas.drawColor(AndroidDocumentProvider.INSPECT_OVERLAY_COLOR);
                super.onDraw(canvas);
            }

            public boolean onTouchEvent(MotionEvent motionEvent) {
                View hitTest;
                if (!(!(getParent() instanceof View) || (hitTest = ViewUtil.hitTest((View) getParent(), motionEvent.getX(), motionEvent.getY(), InspectModeHandler.this.mViewSelector)) == null || motionEvent.getAction() == 3)) {
                    AndroidDocumentProvider.this.mHighlighter.setHighlightedView(hitTest, AndroidDocumentProvider.INSPECT_HOVER_COLOR);
                    if (motionEvent.getAction() == 1 && AndroidDocumentProvider.this.mListener != null) {
                        AndroidDocumentProvider.this.mListener.onInspectRequested(hitTest);
                    }
                }
                return true;
            }
        }
    }
}
