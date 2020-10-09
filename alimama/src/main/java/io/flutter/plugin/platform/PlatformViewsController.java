package io.flutter.plugin.platform;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.el.parse.Operators;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.embedding.engine.systemchannels.PlatformViewsChannel;
import io.flutter.plugin.editing.TextInputPlugin;
import io.flutter.plugin.platform.PlatformViewsController;
import io.flutter.view.AccessibilityBridge;
import io.flutter.view.TextureRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlatformViewsController implements PlatformViewsAccessibilityDelegate {
    private static final int MINIMAL_SDK = 20;
    private static final String TAG = "PlatformViewsController";
    /* access modifiers changed from: private */
    public final AccessibilityEventsDelegate accessibilityEventsDelegate = new AccessibilityEventsDelegate();
    private final PlatformViewsChannel.PlatformViewsHandler channelHandler = new PlatformViewsChannel.PlatformViewsHandler() {
        @TargetApi(17)
        public long createPlatformView(@NonNull PlatformViewsChannel.PlatformViewCreationRequest platformViewCreationRequest) {
            ensureValidAndroidVersion();
            if (!PlatformViewsController.validateDirection(platformViewCreationRequest.direction)) {
                throw new IllegalStateException("Trying to create a view with unknown direction value: " + platformViewCreationRequest.direction + "(view id: " + platformViewCreationRequest.viewId + Operators.BRACKET_END_STR);
            } else if (!PlatformViewsController.this.vdControllers.containsKey(Integer.valueOf(platformViewCreationRequest.viewId))) {
                PlatformViewFactory factory = PlatformViewsController.this.registry.getFactory(platformViewCreationRequest.viewType);
                if (factory != null) {
                    Object obj = null;
                    if (platformViewCreationRequest.params != null) {
                        obj = factory.getCreateArgsCodec().decodeMessage(platformViewCreationRequest.params);
                    }
                    Object obj2 = obj;
                    int access$200 = PlatformViewsController.this.toPhysicalPixels(platformViewCreationRequest.logicalWidth);
                    int access$2002 = PlatformViewsController.this.toPhysicalPixels(platformViewCreationRequest.logicalHeight);
                    PlatformViewsController.this.validateVirtualDisplayDimensions(access$200, access$2002);
                    TextureRegistry.SurfaceTextureEntry createSurfaceTexture = PlatformViewsController.this.textureRegistry.createSurfaceTexture();
                    VirtualDisplayController create = VirtualDisplayController.create(PlatformViewsController.this.context, PlatformViewsController.this.accessibilityEventsDelegate, factory, createSurfaceTexture, access$200, access$2002, platformViewCreationRequest.viewId, obj2, new View.OnFocusChangeListener(platformViewCreationRequest) {
                        private final /* synthetic */ PlatformViewsChannel.PlatformViewCreationRequest f$1;

                        {
                            this.f$1 = r2;
                        }

                        public final void onFocusChange(View view, boolean z) {
                            PlatformViewsController.AnonymousClass1.lambda$createPlatformView$0(PlatformViewsController.AnonymousClass1.this, this.f$1, view, z);
                        }
                    });
                    if (create != null) {
                        if (PlatformViewsController.this.flutterView != null) {
                            create.onFlutterViewAttached(PlatformViewsController.this.flutterView);
                        }
                        PlatformViewsController.this.vdControllers.put(Integer.valueOf(platformViewCreationRequest.viewId), create);
                        View view = create.getView();
                        view.setLayoutDirection(platformViewCreationRequest.direction);
                        PlatformViewsController.this.contextToPlatformView.put(view.getContext(), view);
                        return createSurfaceTexture.id();
                    }
                    throw new IllegalStateException("Failed creating virtual display for a " + platformViewCreationRequest.viewType + " with id: " + platformViewCreationRequest.viewId);
                }
                throw new IllegalStateException("Trying to create a platform view of unregistered type: " + platformViewCreationRequest.viewType);
            } else {
                throw new IllegalStateException("Trying to create an already created platform view, view id: " + platformViewCreationRequest.viewId);
            }
        }

        public static /* synthetic */ void lambda$createPlatformView$0(AnonymousClass1 r0, PlatformViewsChannel.PlatformViewCreationRequest platformViewCreationRequest, View view, boolean z) {
            if (z) {
                PlatformViewsController.this.platformViewsChannel.invokeViewFocused(platformViewCreationRequest.viewId);
            }
        }

        public void disposePlatformView(int i) {
            ensureValidAndroidVersion();
            VirtualDisplayController virtualDisplayController = PlatformViewsController.this.vdControllers.get(Integer.valueOf(i));
            if (virtualDisplayController != null) {
                if (PlatformViewsController.this.textInputPlugin != null) {
                    PlatformViewsController.this.textInputPlugin.clearPlatformViewClient(i);
                }
                PlatformViewsController.this.contextToPlatformView.remove(virtualDisplayController.getView().getContext());
                virtualDisplayController.dispose();
                PlatformViewsController.this.vdControllers.remove(Integer.valueOf(i));
                return;
            }
            throw new IllegalStateException("Trying to dispose a platform view with unknown id: " + i);
        }

        public void resizePlatformView(@NonNull PlatformViewsChannel.PlatformViewResizeRequest platformViewResizeRequest, @NonNull final Runnable runnable) {
            ensureValidAndroidVersion();
            final VirtualDisplayController virtualDisplayController = PlatformViewsController.this.vdControllers.get(Integer.valueOf(platformViewResizeRequest.viewId));
            if (virtualDisplayController != null) {
                int access$200 = PlatformViewsController.this.toPhysicalPixels(platformViewResizeRequest.newLogicalWidth);
                int access$2002 = PlatformViewsController.this.toPhysicalPixels(platformViewResizeRequest.newLogicalHeight);
                PlatformViewsController.this.validateVirtualDisplayDimensions(access$200, access$2002);
                PlatformViewsController.this.lockInputConnection(virtualDisplayController);
                virtualDisplayController.resize(access$200, access$2002, new Runnable() {
                    public void run() {
                        PlatformViewsController.this.unlockInputConnection(virtualDisplayController);
                        runnable.run();
                    }
                });
                return;
            }
            throw new IllegalStateException("Trying to resize a platform view with unknown id: " + platformViewResizeRequest.viewId);
        }

        public void onTouch(@NonNull PlatformViewsChannel.PlatformViewTouch platformViewTouch) {
            PlatformViewsChannel.PlatformViewTouch platformViewTouch2 = platformViewTouch;
            ensureValidAndroidVersion();
            float f = PlatformViewsController.this.context.getResources().getDisplayMetrics().density;
            MotionEvent.PointerProperties[] pointerPropertiesArr = (MotionEvent.PointerProperties[]) PlatformViewsController.parsePointerPropertiesList(platformViewTouch2.rawPointerPropertiesList).toArray(new MotionEvent.PointerProperties[platformViewTouch2.pointerCount]);
            MotionEvent.PointerCoords[] pointerCoordsArr = (MotionEvent.PointerCoords[]) PlatformViewsController.parsePointerCoordsList(platformViewTouch2.rawPointerCoords, f).toArray(new MotionEvent.PointerCoords[platformViewTouch2.pointerCount]);
            if (PlatformViewsController.this.vdControllers.containsKey(Integer.valueOf(platformViewTouch2.viewId))) {
                View view = PlatformViewsController.this.vdControllers.get(Integer.valueOf(platformViewTouch2.viewId)).getView();
                long longValue = platformViewTouch2.downTime.longValue();
                long longValue2 = platformViewTouch2.eventTime.longValue();
                int i = platformViewTouch2.action;
                int i2 = platformViewTouch2.pointerCount;
                int i3 = platformViewTouch2.metaState;
                int i4 = platformViewTouch2.buttonState;
                float f2 = platformViewTouch2.xPrecision;
                float f3 = platformViewTouch2.yPrecision;
                int i5 = platformViewTouch2.deviceId;
                int i6 = platformViewTouch2.edgeFlags;
                View view2 = view;
                int i7 = platformViewTouch2.source;
                view2.dispatchTouchEvent(MotionEvent.obtain(longValue, longValue2, i, i2, pointerPropertiesArr, pointerCoordsArr, i3, i4, f2, f3, i5, i6, i7, platformViewTouch2.flags));
                return;
            }
            throw new IllegalStateException("Sending touch to an unknown view with id: " + platformViewTouch2.viewId);
        }

        @TargetApi(17)
        public void setDirection(int i, int i2) {
            ensureValidAndroidVersion();
            if (PlatformViewsController.validateDirection(i2)) {
                View view = PlatformViewsController.this.vdControllers.get(Integer.valueOf(i)).getView();
                if (view != null) {
                    view.setLayoutDirection(i2);
                    return;
                }
                throw new IllegalStateException("Sending touch to an unknown view with id: " + i2);
            }
            throw new IllegalStateException("Trying to set unknown direction value: " + i2 + "(view id: " + i + Operators.BRACKET_END_STR);
        }

        public void clearFocus(int i) {
            PlatformViewsController.this.vdControllers.get(Integer.valueOf(i)).getView().clearFocus();
        }

        private void ensureValidAndroidVersion() {
            if (Build.VERSION.SDK_INT < 20) {
                throw new IllegalStateException("Trying to use platform views with API " + Build.VERSION.SDK_INT + ", required API level is: " + 20);
            }
        }
    };
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public final HashMap<Context, View> contextToPlatformView = new HashMap<>();
    /* access modifiers changed from: private */
    public View flutterView;
    /* access modifiers changed from: private */
    public PlatformViewsChannel platformViewsChannel;
    /* access modifiers changed from: private */
    public final PlatformViewRegistryImpl registry = new PlatformViewRegistryImpl();
    /* access modifiers changed from: private */
    public TextInputPlugin textInputPlugin;
    /* access modifiers changed from: private */
    public TextureRegistry textureRegistry;
    @VisibleForTesting
    final HashMap<Integer, VirtualDisplayController> vdControllers = new HashMap<>();

    /* access modifiers changed from: private */
    public static boolean validateDirection(int i) {
        return i == 0 || i == 1;
    }

    public void attach(Context context2, TextureRegistry textureRegistry2, @NonNull DartExecutor dartExecutor) {
        if (this.context == null) {
            this.context = context2;
            this.textureRegistry = textureRegistry2;
            this.platformViewsChannel = new PlatformViewsChannel(dartExecutor);
            this.platformViewsChannel.setPlatformViewsHandler(this.channelHandler);
            return;
        }
        throw new AssertionError("A PlatformViewsController can only be attached to a single output target.\nattach was called while the PlatformViewsController was already attached.");
    }

    @UiThread
    public void detach() {
        this.platformViewsChannel.setPlatformViewsHandler((PlatformViewsChannel.PlatformViewsHandler) null);
        this.platformViewsChannel = null;
        this.context = null;
        this.textureRegistry = null;
    }

    public void attachToView(@NonNull View view) {
        this.flutterView = view;
        for (VirtualDisplayController onFlutterViewAttached : this.vdControllers.values()) {
            onFlutterViewAttached.onFlutterViewAttached(view);
        }
    }

    public void detachFromView() {
        this.flutterView = null;
        for (VirtualDisplayController onFlutterViewDetached : this.vdControllers.values()) {
            onFlutterViewDetached.onFlutterViewDetached();
        }
    }

    public void attachAccessibilityBridge(AccessibilityBridge accessibilityBridge) {
        this.accessibilityEventsDelegate.setAccessibilityBridge(accessibilityBridge);
    }

    public void detachAccessibiltyBridge() {
        this.accessibilityEventsDelegate.setAccessibilityBridge((AccessibilityBridge) null);
    }

    public void attachTextInputPlugin(TextInputPlugin textInputPlugin2) {
        this.textInputPlugin = textInputPlugin2;
    }

    public void detachTextInputPlugin() {
        this.textInputPlugin = null;
    }

    public boolean checkInputConnectionProxy(View view) {
        if (!this.contextToPlatformView.containsKey(view.getContext())) {
            return false;
        }
        View view2 = this.contextToPlatformView.get(view.getContext());
        if (view2 == view) {
            return true;
        }
        return view2.checkInputConnectionProxy(view);
    }

    public PlatformViewRegistry getRegistry() {
        return this.registry;
    }

    public void onFlutterViewDestroyed() {
        flushAllViews();
    }

    public void onPreEngineRestart() {
        flushAllViews();
    }

    public View getPlatformViewById(Integer num) {
        VirtualDisplayController virtualDisplayController = this.vdControllers.get(num);
        if (virtualDisplayController == null) {
            return null;
        }
        return virtualDisplayController.getView();
    }

    /* access modifiers changed from: private */
    public void lockInputConnection(@NonNull VirtualDisplayController virtualDisplayController) {
        if (this.textInputPlugin != null) {
            this.textInputPlugin.lockPlatformViewInputConnection();
            virtualDisplayController.onInputConnectionLocked();
        }
    }

    /* access modifiers changed from: private */
    public void unlockInputConnection(@NonNull VirtualDisplayController virtualDisplayController) {
        if (this.textInputPlugin != null) {
            this.textInputPlugin.unlockPlatformViewInputConnection();
            virtualDisplayController.onInputConnectionUnlocked();
        }
    }

    /* access modifiers changed from: private */
    public static List<MotionEvent.PointerProperties> parsePointerPropertiesList(Object obj) {
        ArrayList arrayList = new ArrayList();
        for (Object parsePointerProperties : (List) obj) {
            arrayList.add(parsePointerProperties(parsePointerProperties));
        }
        return arrayList;
    }

    private static MotionEvent.PointerProperties parsePointerProperties(Object obj) {
        List list = (List) obj;
        MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
        pointerProperties.id = ((Integer) list.get(0)).intValue();
        pointerProperties.toolType = ((Integer) list.get(1)).intValue();
        return pointerProperties;
    }

    /* access modifiers changed from: private */
    public static List<MotionEvent.PointerCoords> parsePointerCoordsList(Object obj, float f) {
        ArrayList arrayList = new ArrayList();
        for (Object parsePointerCoords : (List) obj) {
            arrayList.add(parsePointerCoords(parsePointerCoords, f));
        }
        return arrayList;
    }

    private static MotionEvent.PointerCoords parsePointerCoords(Object obj, float f) {
        List list = (List) obj;
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        pointerCoords.orientation = (float) ((Double) list.get(0)).doubleValue();
        pointerCoords.pressure = (float) ((Double) list.get(1)).doubleValue();
        pointerCoords.size = (float) ((Double) list.get(2)).doubleValue();
        pointerCoords.toolMajor = ((float) ((Double) list.get(3)).doubleValue()) * f;
        pointerCoords.toolMinor = ((float) ((Double) list.get(4)).doubleValue()) * f;
        pointerCoords.touchMajor = ((float) ((Double) list.get(5)).doubleValue()) * f;
        pointerCoords.touchMinor = ((float) ((Double) list.get(6)).doubleValue()) * f;
        pointerCoords.x = ((float) ((Double) list.get(7)).doubleValue()) * f;
        pointerCoords.y = ((float) ((Double) list.get(8)).doubleValue()) * f;
        return pointerCoords;
    }

    /* access modifiers changed from: private */
    public void validateVirtualDisplayDimensions(int i, int i2) {
        DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics();
        if (i2 > displayMetrics.heightPixels || i > displayMetrics.widthPixels) {
            Log.w(TAG, "Creating a virtual display of size: [" + i + AVFSCacheConstants.COMMA_SEP + i2 + "] may result in problems(https://github.com/flutter/flutter/issues/2897).It is larger than the device screen size: [" + displayMetrics.widthPixels + AVFSCacheConstants.COMMA_SEP + displayMetrics.heightPixels + "].");
        }
    }

    /* access modifiers changed from: private */
    public int toPhysicalPixels(double d) {
        double d2 = (double) this.context.getResources().getDisplayMetrics().density;
        Double.isNaN(d2);
        return (int) Math.round(d * d2);
    }

    private void flushAllViews() {
        for (VirtualDisplayController dispose : this.vdControllers.values()) {
            dispose.dispose();
        }
        this.vdControllers.clear();
    }
}
