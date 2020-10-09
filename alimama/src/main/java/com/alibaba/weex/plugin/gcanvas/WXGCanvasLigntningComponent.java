package com.alibaba.weex.plugin.gcanvas;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.view.TextureView;
import androidx.annotation.NonNull;
import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule;
import com.taobao.gcanvas.GCanvasJNI;
import com.taobao.gcanvas.surface.GTextureView;
import com.taobao.gcanvas.util.GLog;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component(lazyload = false)
@WeexComponent(names = {"gcanvas"})
public class WXGCanvasLigntningComponent extends WXComponent<GWXSurfaceView> implements TextureView.SurfaceTextureListener {
    private static final String TAG = "WXGCanvasLigntningComponent";
    public AtomicBoolean mSended = new AtomicBoolean(false);
    private GWXSurfaceView mSurfaceView;
    public GCanvasLightningModule.ContextType mType;

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    private void addGCanvasView() {
        String backgroundColor = getStyles().getBackgroundColor();
        this.mSurfaceView = new GWXSurfaceView(getContext(), this);
        GCanvasJNI.registerWXCallNativeFunc(getContext());
        if (TextUtils.isEmpty(backgroundColor)) {
            backgroundColor = "transparent";
        }
        this.mSurfaceView.setBackgroundColor(backgroundColor);
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXGCanvasLigntningComponent(wXSDKInstance, wXVContainer, i, basicComponentData);
        }

        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXGCanvasLigntningComponent(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXGCanvasLigntningComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, i, basicComponentData);
    }

    public WXGCanvasLigntningComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXGCanvasLigntningComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, str, z, basicComponentData);
    }

    public void onActivityResume() {
        if (this.mSurfaceView != null) {
            this.mSurfaceView.resume();
        }
    }

    public void onActivityDestroy() {
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setSurfaceTextureListener((TextureView.SurfaceTextureListener) null);
            this.mSurfaceView.requestExit();
        }
    }

    public void onActivityPause() {
        if (this.mSurfaceView != null) {
            this.mSurfaceView.pause();
        }
    }

    /* access modifiers changed from: protected */
    public GWXSurfaceView initComponentHostView(@NonNull Context context) {
        this.mSended.set(false);
        addGCanvasView();
        return this.mSurfaceView;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        Context context = getContext();
        if (context == null) {
            GLog.e(TAG, "setDevicePixelRatio error ctx == null");
            return;
        }
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        double d = (double) width;
        Double.isNaN(d);
        double d2 = d / 750.0d;
        String str = TAG;
        GLog.d(str, "enable width " + width);
        String str2 = TAG;
        GLog.d(str2, "enable devicePixelRatio " + d2);
        GCanvasJNI.setDevicePixelRatio(getRef(), d2);
    }

    public GTextureView getSurfaceView() {
        return this.mSurfaceView;
    }

    public void sendEvent() {
        synchronized (this) {
            if (!this.mSended.get()) {
                HashMap hashMap = new HashMap();
                hashMap.put("ref", getRef());
                GLog.d("send event in gcanvas component.params=" + hashMap.toString());
                getInstance().fireGlobalEventCallback("GCanvasReady", hashMap);
                this.mSended.set(true);
            } else {
                GLog.d("event sended.");
            }
        }
    }
}
