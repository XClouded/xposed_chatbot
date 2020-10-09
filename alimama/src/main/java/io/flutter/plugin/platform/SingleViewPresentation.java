package io.flutter.plugin.platform;

import android.annotation.TargetApi;
import android.app.Presentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@TargetApi(17)
@Keep
class SingleViewPresentation extends Presentation {
    private final AccessibilityEventsDelegate accessibilityEventsDelegate;
    private FrameLayout container;
    private Object createParams;
    private final View.OnFocusChangeListener focusChangeListener;
    private AccessibilityDelegatingFrameLayout rootView;
    private boolean startFocused = false;
    private PresentationState state;
    private final PlatformViewFactory viewFactory;
    private int viewId;

    static class PresentationState {
        /* access modifiers changed from: private */
        public FakeWindowViewGroup fakeWindowViewGroup;
        /* access modifiers changed from: private */
        public PlatformView platformView;
        /* access modifiers changed from: private */
        public WindowManagerHandler windowManagerHandler;

        PresentationState() {
        }
    }

    public SingleViewPresentation(Context context, Display display, PlatformViewFactory platformViewFactory, AccessibilityEventsDelegate accessibilityEventsDelegate2, int i, Object obj, View.OnFocusChangeListener onFocusChangeListener) {
        super(new ImmContext(context), display);
        this.viewFactory = platformViewFactory;
        this.accessibilityEventsDelegate = accessibilityEventsDelegate2;
        this.viewId = i;
        this.createParams = obj;
        this.focusChangeListener = onFocusChangeListener;
        this.state = new PresentationState();
        getWindow().setFlags(8, 8);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().setType(2030);
        }
    }

    public SingleViewPresentation(Context context, Display display, AccessibilityEventsDelegate accessibilityEventsDelegate2, PresentationState presentationState, View.OnFocusChangeListener onFocusChangeListener, boolean z) {
        super(new ImmContext(context), display);
        this.accessibilityEventsDelegate = accessibilityEventsDelegate2;
        this.viewFactory = null;
        this.state = presentationState;
        this.focusChangeListener = onFocusChangeListener;
        getWindow().setFlags(8, 8);
        this.startFocused = z;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        if (this.state.fakeWindowViewGroup == null) {
            FakeWindowViewGroup unused = this.state.fakeWindowViewGroup = new FakeWindowViewGroup(getContext());
        }
        if (this.state.windowManagerHandler == null) {
            WindowManagerHandler unused2 = this.state.windowManagerHandler = new WindowManagerHandler((WindowManager) getContext().getSystemService("window"), this.state.fakeWindowViewGroup);
        }
        this.container = new FrameLayout(getContext());
        PresentationContext presentationContext = new PresentationContext(getContext(), this.state.windowManagerHandler);
        if (this.state.platformView == null) {
            PlatformView unused3 = this.state.platformView = this.viewFactory.create(presentationContext, this.viewId, this.createParams);
        }
        View view = this.state.platformView.getView();
        this.container.addView(view);
        this.rootView = new AccessibilityDelegatingFrameLayout(getContext(), this.accessibilityEventsDelegate, view);
        this.rootView.addView(this.container);
        this.rootView.addView(this.state.fakeWindowViewGroup);
        view.setOnFocusChangeListener(this.focusChangeListener);
        this.rootView.setFocusableInTouchMode(true);
        if (this.startFocused) {
            view.requestFocus();
        } else {
            this.rootView.requestFocus();
        }
        setContentView(this.rootView);
    }

    public PresentationState detachState() {
        this.container.removeAllViews();
        this.rootView.removeAllViews();
        return this.state;
    }

    public PlatformView getView() {
        if (this.state.platformView == null) {
            return null;
        }
        return this.state.platformView;
    }

    static class FakeWindowViewGroup extends ViewGroup {
        private final Rect childRect = new Rect();
        private final Rect viewBounds = new Rect();

        public FakeWindowViewGroup(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            for (int i5 = 0; i5 < getChildCount(); i5++) {
                View childAt = getChildAt(i5);
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) childAt.getLayoutParams();
                this.viewBounds.set(i, i2, i3, i4);
                Gravity.apply(layoutParams.gravity, childAt.getMeasuredWidth(), childAt.getMeasuredHeight(), this.viewBounds, layoutParams.x, layoutParams.y, this.childRect);
                childAt.layout(this.childRect.left, this.childRect.top, this.childRect.right, this.childRect.bottom);
            }
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                getChildAt(i3).measure(atMost(i), atMost(i2));
            }
            super.onMeasure(i, i2);
        }

        private static int atMost(int i) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), Integer.MIN_VALUE);
        }
    }

    private static class ImmContext extends ContextWrapper {
        @NonNull
        private final InputMethodManager inputMethodManager;

        ImmContext(Context context) {
            this(context, (InputMethodManager) null);
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: android.view.inputmethod.InputMethodManager} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private ImmContext(android.content.Context r1, @androidx.annotation.Nullable android.view.inputmethod.InputMethodManager r2) {
            /*
                r0 = this;
                r0.<init>(r1)
                if (r2 == 0) goto L_0x0006
                goto L_0x000f
            L_0x0006:
                java.lang.String r2 = "input_method"
                java.lang.Object r1 = r1.getSystemService(r2)
                r2 = r1
                android.view.inputmethod.InputMethodManager r2 = (android.view.inputmethod.InputMethodManager) r2
            L_0x000f:
                r0.inputMethodManager = r2
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.flutter.plugin.platform.SingleViewPresentation.ImmContext.<init>(android.content.Context, android.view.inputmethod.InputMethodManager):void");
        }

        public Object getSystemService(String str) {
            if ("input_method".equals(str)) {
                return this.inputMethodManager;
            }
            return super.getSystemService(str);
        }

        public Context createDisplayContext(Display display) {
            return new ImmContext(super.createDisplayContext(display), this.inputMethodManager);
        }
    }

    private static class PresentationContext extends ContextWrapper {
        @Nullable
        private WindowManager windowManager;
        @NonNull
        private final WindowManagerHandler windowManagerHandler;

        PresentationContext(Context context, @NonNull WindowManagerHandler windowManagerHandler2) {
            super(context);
            this.windowManagerHandler = windowManagerHandler2;
        }

        public Object getSystemService(String str) {
            if ("window".equals(str)) {
                return getWindowManager();
            }
            return super.getSystemService(str);
        }

        private WindowManager getWindowManager() {
            if (this.windowManager == null) {
                this.windowManager = this.windowManagerHandler.getWindowManager();
            }
            return this.windowManager;
        }
    }

    static class WindowManagerHandler implements InvocationHandler {
        private static final String TAG = "PlatformViewsController";
        private final WindowManager delegate;
        FakeWindowViewGroup fakeWindowRootView;

        WindowManagerHandler(WindowManager windowManager, FakeWindowViewGroup fakeWindowViewGroup) {
            this.delegate = windowManager;
            this.fakeWindowRootView = fakeWindowViewGroup;
        }

        public WindowManager getWindowManager() {
            return (WindowManager) Proxy.newProxyInstance(WindowManager.class.getClassLoader(), new Class[]{WindowManager.class}, this);
        }

        /* JADX WARNING: Removed duplicated region for block: B:23:0x004a A[SYNTHETIC, Splitter:B:23:0x004a] */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x004d  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0051  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0055  */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x0059  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object invoke(java.lang.Object r3, java.lang.reflect.Method r4, java.lang.Object[] r5) throws java.lang.Throwable {
            /*
                r2 = this;
                java.lang.String r3 = r4.getName()
                int r0 = r3.hashCode()
                r1 = -1148522778(0xffffffffbb8aeee6, float:-0.0042399047)
                if (r0 == r1) goto L_0x003b
                r1 = 542766184(0x2059f468, float:1.8461482E-19)
                if (r0 == r1) goto L_0x0031
                r1 = 931413976(0x37843fd8, float:1.5765356E-5)
                if (r0 == r1) goto L_0x0027
                r1 = 1098630473(0x417bc549, float:15.735665)
                if (r0 == r1) goto L_0x001d
                goto L_0x0045
            L_0x001d:
                java.lang.String r0 = "removeView"
                boolean r3 = r3.equals(r0)
                if (r3 == 0) goto L_0x0045
                r3 = 1
                goto L_0x0046
            L_0x0027:
                java.lang.String r0 = "updateViewLayout"
                boolean r3 = r3.equals(r0)
                if (r3 == 0) goto L_0x0045
                r3 = 3
                goto L_0x0046
            L_0x0031:
                java.lang.String r0 = "removeViewImmediate"
                boolean r3 = r3.equals(r0)
                if (r3 == 0) goto L_0x0045
                r3 = 2
                goto L_0x0046
            L_0x003b:
                java.lang.String r0 = "addView"
                boolean r3 = r3.equals(r0)
                if (r3 == 0) goto L_0x0045
                r3 = 0
                goto L_0x0046
            L_0x0045:
                r3 = -1
            L_0x0046:
                r0 = 0
                switch(r3) {
                    case 0: goto L_0x0059;
                    case 1: goto L_0x0055;
                    case 2: goto L_0x0051;
                    case 3: goto L_0x004d;
                    default: goto L_0x004a;
                }
            L_0x004a:
                android.view.WindowManager r3 = r2.delegate     // Catch:{ InvocationTargetException -> 0x0062 }
                goto L_0x005d
            L_0x004d:
                r2.updateViewLayout(r5)
                return r0
            L_0x0051:
                r2.removeViewImmediate(r5)
                return r0
            L_0x0055:
                r2.removeView(r5)
                return r0
            L_0x0059:
                r2.addView(r5)
                return r0
            L_0x005d:
                java.lang.Object r3 = r4.invoke(r3, r5)     // Catch:{ InvocationTargetException -> 0x0062 }
                return r3
            L_0x0062:
                r3 = move-exception
                java.lang.Throwable r3 = r3.getCause()
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: io.flutter.plugin.platform.SingleViewPresentation.WindowManagerHandler.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
        }

        private void addView(Object[] objArr) {
            if (this.fakeWindowRootView == null) {
                Log.w(TAG, "Embedded view called addView while detached from presentation");
                return;
            }
            this.fakeWindowRootView.addView(objArr[0], objArr[1]);
        }

        private void removeView(Object[] objArr) {
            if (this.fakeWindowRootView == null) {
                Log.w(TAG, "Embedded view called removeView while detached from presentation");
                return;
            }
            this.fakeWindowRootView.removeView(objArr[0]);
        }

        private void removeViewImmediate(Object[] objArr) {
            if (this.fakeWindowRootView == null) {
                Log.w(TAG, "Embedded view called removeViewImmediate while detached from presentation");
                return;
            }
            View view = objArr[0];
            view.clearAnimation();
            this.fakeWindowRootView.removeView(view);
        }

        private void updateViewLayout(Object[] objArr) {
            if (this.fakeWindowRootView == null) {
                Log.w(TAG, "Embedded view called updateViewLayout while detached from presentation");
                return;
            }
            this.fakeWindowRootView.updateViewLayout(objArr[0], objArr[1]);
        }
    }

    private static class AccessibilityDelegatingFrameLayout extends FrameLayout {
        private final AccessibilityEventsDelegate accessibilityEventsDelegate;
        private final View embeddedView;

        public AccessibilityDelegatingFrameLayout(Context context, AccessibilityEventsDelegate accessibilityEventsDelegate2, View view) {
            super(context);
            this.accessibilityEventsDelegate = accessibilityEventsDelegate2;
            this.embeddedView = view;
        }

        public boolean requestSendAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            return this.accessibilityEventsDelegate.requestSendAccessibilityEvent(this.embeddedView, view, accessibilityEvent);
        }
    }
}
