package com.alibaba.weex.plugin.gcanvas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.weex.plugin.annotation.WeexModule;
import com.taobao.gcanvas.GCanvasJNI;
import com.taobao.gcanvas.surface.GTextureView;
import com.taobao.gcanvas.util.GLog;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.PhenixEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WeexModule(name = "gcanvas")
public class GCanvasLightningModule extends WXModule implements Destroyable {
    private static final String TAG = "GCanvasLightningModule";
    private HashMap<String, WXGCanvasLigntningComponent> mComponentMap = new HashMap<>(1);
    private GCanvasImageLoader mImageLoader = new GCanvasImageLoader();

    @JSMethod(uiThread = false)
    public String execGcanvaSyncCMD(String str, String str2, String str3) {
        return "";
    }

    @JSMethod
    public void registerRetachFunction(String str, JSCallback jSCallback) {
    }

    @JSMethod(uiThread = false)
    public void render(String str, String str2, JSCallback jSCallback) {
    }

    @JSMethod
    public void resetComponent(String str) {
    }

    @JSMethod
    public void setDevicePixelRatio(String str) {
    }

    @JSMethod
    public void setHiQuality(String str, String str2) {
    }

    @JSMethod
    public void setup(String str, String str2, JSCallback jSCallback) {
    }

    public GCanvasLightningModule() {
        if (Build.VERSION.SDK_INT < 24) {
            GCanvasJNI.registerCallback((String) null, Build.VERSION.SDK_INT);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    @com.taobao.weex.annotation.JSMethod(uiThread = false)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindImageTexture(java.lang.String r16, java.lang.String r17, com.taobao.weex.bridge.JSCallback r18) {
        /*
            r15 = this;
            r10 = r15
            boolean r0 = android.text.TextUtils.isEmpty(r16)
            r1 = 0
            r2 = 0
            if (r0 != 0) goto L_0x0029
            org.json.JSONArray r0 = new org.json.JSONArray     // Catch:{ Throwable -> 0x001f }
            r3 = r16
            r0.<init>(r3)     // Catch:{ Throwable -> 0x001f }
            java.lang.String r3 = r0.getString(r1)     // Catch:{ Throwable -> 0x001f }
            r2 = 1
            int r0 = r0.getInt(r2)     // Catch:{ Throwable -> 0x001c }
            r7 = r3
            r3 = r0
            goto L_0x002b
        L_0x001c:
            r0 = move-exception
            r2 = r3
            goto L_0x0020
        L_0x001f:
            r0 = move-exception
        L_0x0020:
            java.lang.String r3 = "GCanvasLightningModule"
            java.lang.String r4 = r0.getMessage()
            com.taobao.gcanvas.util.GLog.e(r3, r4, r0)
        L_0x0029:
            r7 = r2
            r3 = 0
        L_0x002b:
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 != 0) goto L_0x0130
            java.lang.Object r11 = new java.lang.Object
            r11.<init>()
            java.util.concurrent.atomic.AtomicBoolean r0 = new java.util.concurrent.atomic.AtomicBoolean
            r0.<init>(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "start to load texture in 2dmodule.start time = "
            r1.append(r2)
            long r4 = java.lang.System.currentTimeMillis()
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            com.taobao.gcanvas.util.GLog.d(r1)
            java.lang.String r1 = "data:image"
            boolean r1 = r7.startsWith(r1)     // Catch:{ Throwable -> 0x0126 }
            if (r1 == 0) goto L_0x00c1
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0126 }
            r0.<init>()     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r1 = "start to decode base64 texture in 2dmodule.start time = "
            r0.append(r1)     // Catch:{ Throwable -> 0x0126 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0126 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0126 }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0126 }
            com.alibaba.weex.plugin.gcanvas.GCanvasImageLoader r0 = r10.mImageLoader     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r1 = "base64,"
            int r1 = r7.indexOf(r1)     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r2 = "base64,"
            int r2 = r2.length()     // Catch:{ Throwable -> 0x0126 }
            int r1 = r1 + r2
            java.lang.String r1 = r7.substring(r1)     // Catch:{ Throwable -> 0x0126 }
            android.graphics.Bitmap r3 = r0.handleBase64Texture(r1)     // Catch:{ Throwable -> 0x0126 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0126 }
            r0.<init>()     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r1 = "start to decode base64 texture in 2dmodule.end time = "
            r0.append(r1)     // Catch:{ Throwable -> 0x0126 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0126 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0126 }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0126 }
            if (r3 == 0) goto L_0x00ba
            java.lang.String r0 = "start to bind base64 format texture in 2dmodule."
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0126 }
            r4 = 0
            r5 = 3553(0xde1, float:4.979E-42)
            r6 = 0
            r7 = 6408(0x1908, float:8.98E-42)
            r8 = 6408(0x1908, float:8.98E-42)
            r9 = 5121(0x1401, float:7.176E-42)
            r2 = r17
            com.taobao.gcanvas.GCanvasJNI.bindTexture(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x0126 }
            goto L_0x0130
        L_0x00ba:
            java.lang.String r0 = "decode base64 texture failed,bitmap is null."
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0126 }
            goto L_0x0130
        L_0x00c1:
            java.util.HashMap r12 = new java.util.HashMap     // Catch:{ Throwable -> 0x0126 }
            r12.<init>()     // Catch:{ Throwable -> 0x0126 }
            com.taobao.phenix.intf.Phenix r1 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ Throwable -> 0x0126 }
            com.taobao.phenix.intf.PhenixCreator r13 = r1.load(r7)     // Catch:{ Throwable -> 0x0126 }
            com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$3 r14 = new com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$3     // Catch:{ Throwable -> 0x0126 }
            r1 = r14
            r2 = r15
            r4 = r17
            r5 = r18
            r6 = r12
            r8 = r11
            r9 = r0
            r1.<init>(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x0126 }
            com.taobao.phenix.intf.PhenixCreator r1 = r13.succListener(r14)     // Catch:{ Throwable -> 0x0126 }
            com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$2 r2 = new com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$2     // Catch:{ Throwable -> 0x0126 }
            r2.<init>(r11, r0)     // Catch:{ Throwable -> 0x0126 }
            com.taobao.phenix.intf.PhenixCreator r1 = r1.failListener(r2)     // Catch:{ Throwable -> 0x0126 }
            com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$1 r2 = new com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$1     // Catch:{ Throwable -> 0x0126 }
            r2.<init>(r11, r0)     // Catch:{ Throwable -> 0x0126 }
            com.taobao.phenix.intf.PhenixCreator r1 = r1.cancelListener(r2)     // Catch:{ Throwable -> 0x0126 }
            r1.fetch()     // Catch:{ Throwable -> 0x0126 }
            monitor-enter(r11)     // Catch:{ Throwable -> 0x0126 }
            java.lang.String r1 = "start wait bindtexture in 2dmodule."
            com.taobao.gcanvas.util.GLog.d(r1)     // Catch:{ all -> 0x0123 }
            boolean r0 = r0.get()     // Catch:{ all -> 0x0123 }
            if (r0 != 0) goto L_0x0104
            r11.wait()     // Catch:{ all -> 0x0123 }
        L_0x0104:
            r1 = r18
            r1.invoke(r12)     // Catch:{ all -> 0x0123 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0123 }
            r0.<init>()     // Catch:{ all -> 0x0123 }
            java.lang.String r1 = "finish wait bindtexture in 2dmodule,end time = "
            r0.append(r1)     // Catch:{ all -> 0x0123 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0123 }
            r0.append(r1)     // Catch:{ all -> 0x0123 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0123 }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ all -> 0x0123 }
            monitor-exit(r11)     // Catch:{ all -> 0x0123 }
            goto L_0x0130
        L_0x0123:
            r0 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0123 }
            throw r0     // Catch:{ Throwable -> 0x0126 }
        L_0x0126:
            r0 = move-exception
            java.lang.String r1 = "GCanvasLightningModule"
            java.lang.String r2 = r0.getMessage()
            com.taobao.gcanvas.util.GLog.e(r1, r2, r0)
        L_0x0130:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule.bindImageTexture(java.lang.String, java.lang.String, com.taobao.weex.bridge.JSCallback):void");
    }

    @JSMethod(uiThread = false)
    public void preLoadImage(String str, JSCallback jSCallback) {
        GLog.d(TAG, "preLoadImage() in GCanvasLightningModule,args: " + str);
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                this.mImageLoader.loadImage(jSONArray.getString(0), jSONArray.getInt(1), jSCallback);
            } catch (Throwable th) {
                GLog.e(TAG, th.getMessage(), th);
            }
        }
    }

    @JSMethod(uiThread = false)
    public void setLogLevel(String str) {
        GLog.d(TAG, "setLogLevel() args: " + str);
        GLog.setLevel(str);
    }

    @JSMethod(uiThread = false)
    public void enable(String str, JSCallback jSCallback) {
        try {
            String string = new JSONObject(str).getString("componentId");
            WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.mWXSDKInstance.getInstanceId(), string);
            if (wXComponent instanceof WXGCanvasLigntningComponent) {
                this.mComponentMap.put(string, (WXGCanvasLigntningComponent) wXComponent);
            }
        } catch (Throwable unused) {
        }
    }

    @JSMethod(uiThread = false)
    public void getDeviceInfo(String str, JSCallback jSCallback) {
        if (!TextUtils.isEmpty(str)) {
            HashMap hashMap = new HashMap();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("platform", TimeCalculator.PLATFORM_ANDROID);
            } catch (JSONException unused) {
            }
            hashMap.put("data", jSONObject.toString());
            jSCallback.invoke(hashMap);
        }
    }

    @JSMethod(uiThread = false)
    public void setContextType(String str, String str2, JSCallback jSCallback) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            Context context = this.mWXSDKInstance.getContext();
            if (context == null) {
                GLog.e(TAG, "setDevicePixelRatio error ctx == null");
                return;
            }
            int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            double d = (double) width;
            Double.isNaN(d);
            double d2 = d / 750.0d;
            GLog.d(TAG, "enable width " + width);
            GLog.d(TAG, "enable devicePixelRatio " + d2);
            ContextType contextType = ContextType._2D;
            if (Config.TYPE_3D.equals(str) || "1".equals(str)) {
                contextType = ContextType._3D;
            }
            GCanvasJNI.setContextType(str2, contextType.value());
            GCanvasJNI.setDevicePixelRatio(str2, d2);
            if (GCanvasJNI.sendEvent(str2)) {
                GLog.d("start to send event in module.");
                WXGCanvasLigntningComponent wXGCanvasLigntningComponent = this.mComponentMap.get(str2);
                if (wXGCanvasLigntningComponent != null) {
                    wXGCanvasLigntningComponent.sendEvent();
                }
            }
            WXGCanvasLigntningComponent wXGCanvasLigntningComponent2 = this.mComponentMap.get(str2);
            if (wXGCanvasLigntningComponent2 != null) {
                wXGCanvasLigntningComponent2.mType = contextType;
            }
        }
    }

    @JSMethod(uiThread = false)
    public void setAlpha(String str, float f) {
        GTextureView surfaceView;
        GLog.d("start to set alpha in 3dmodule.");
        WXGCanvasLigntningComponent wXGCanvasLigntningComponent = this.mComponentMap.get(str);
        if (wXGCanvasLigntningComponent != null && (surfaceView = wXGCanvasLigntningComponent.getSurfaceView()) != null) {
            GLog.d("set alpha success in 3dmodule.");
            surfaceView.setAlpha(f);
        }
    }

    public void destroy() {
        Log.i(TAG, "canvas module destroy!!!");
        for (Map.Entry next : this.mComponentMap.entrySet()) {
            GLog.d("component destroy id=" + next.getKey());
            ((WXGCanvasLigntningComponent) next.getValue()).onActivityDestroy();
        }
        this.mComponentMap.clear();
    }

    enum ContextType {
        _2D(0),
        _3D(1);
        
        private int value;

        private ContextType(int i) {
            this.value = i;
        }

        public int value() {
            return this.value;
        }
    }

    @JSMethod(uiThread = false)
    public void texImage2D(String str, int i, int i2, int i3, int i4, int i5, String str2) {
        String str3 = str2;
        GLog.d("texImage2D in 3dmodule,refid=" + str + ",target=" + i + ",level=" + i2 + ",internalformat=" + i3 + ",format=" + i4 + ",type=" + i5 + ",path=" + str3);
        if (!TextUtils.isEmpty(str2)) {
            final Object obj = new Object();
            final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            GLog.d("start to load texture in 3dmodule.start time = " + System.currentTimeMillis());
            try {
                if (str3.startsWith("data:image")) {
                    GLog.d("start to decode base64 texture in 3dmodule.start time = " + System.currentTimeMillis());
                    Bitmap handleBase64Texture = this.mImageLoader.handleBase64Texture(str3.substring(str3.indexOf("base64,") + "base64,".length()));
                    GLog.d("start to decode base64 texture in 3dmodule.end time = " + System.currentTimeMillis());
                    if (handleBase64Texture != null) {
                        GLog.d("start to bind base64 format texture in 3dmodule.");
                        GCanvasJNI.bindTexture(str, handleBase64Texture, 0, i, i2, i3, i4, i5);
                        return;
                    }
                    GLog.d("decode base64 texture failed,bitmap is null.");
                    return;
                }
                final String str4 = str;
                final int i6 = i;
                final int i7 = i2;
                final int i8 = i3;
                final int i9 = i4;
                final int i10 = i5;
                final Object obj2 = obj;
                final AtomicBoolean atomicBoolean2 = atomicBoolean;
                Phenix.instance().load(str3).succListener(new IPhenixListener<SuccPhenixEvent>() {
                    public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                        Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                        if (bitmap != null) {
                            GLog.d("start to bindtexture in 3dmodule.");
                            GCanvasJNI.bindTexture(str4, bitmap, 0, i6, i7, i8, i9, i10);
                        } else {
                            GLog.d("bitmap is null in teximage2D.");
                        }
                        synchronized (obj2) {
                            GLog.d("[texImage2D]finish bindtexture in 3dmodule.");
                            obj2.notifyAll();
                            atomicBoolean2.set(true);
                            GLog.d("[texImage2D]finish notify in 3dmodule.");
                        }
                        return true;
                    }
                }).failListener(new IPhenixListener<FailPhenixEvent>() {
                    public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                        GLog.d("teximage2D load picture failed.");
                        synchronized (obj) {
                            GLog.d("[texImage2D]finish bindtexture in 3dmodule.");
                            obj.notifyAll();
                            atomicBoolean.set(true);
                            GLog.d("[texImage2D]finish notify in 3dmodule.");
                        }
                        return true;
                    }
                }).cancelListener(new IPhenixListener<PhenixEvent>() {
                    public boolean onHappen(PhenixEvent phenixEvent) {
                        GLog.d("teximage2D load picture cancel.");
                        synchronized (obj) {
                            GLog.d("[texImage2D]finish bindtexture in 3dmodule.");
                            obj.notifyAll();
                            atomicBoolean.set(true);
                            GLog.d("[texImage2D]finish notify in 3dmodule.");
                        }
                        return true;
                    }
                }).fetch();
                synchronized (obj) {
                    GLog.d("[texImage2D] start wait bindtexture in 3dmodule");
                    if (!atomicBoolean.get()) {
                        obj.wait();
                    }
                    GLog.d("finish wait bindtexture in 3dmodule,end time = " + System.currentTimeMillis());
                }
            } catch (Throwable th) {
                GLog.e(TAG, th.getMessage(), th);
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    @com.taobao.weex.annotation.JSMethod(uiThread = false)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void texSubImage2D(java.lang.String r17, int r18, int r19, int r20, int r21, int r22, int r23, java.lang.String r24) {
        /*
            r16 = this;
            r12 = r16
            r0 = r24
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "texSubImage2D in 3dmodule,refid="
            r1.append(r2)
            r3 = r17
            r1.append(r3)
            java.lang.String r2 = ",target="
            r1.append(r2)
            r6 = r18
            r1.append(r6)
            java.lang.String r2 = ",level="
            r1.append(r2)
            r7 = r19
            r1.append(r7)
            java.lang.String r2 = ",xoffset="
            r1.append(r2)
            r8 = r20
            r1.append(r8)
            java.lang.String r2 = ",yoffset="
            r1.append(r2)
            r9 = r21
            r1.append(r9)
            java.lang.String r2 = ",format="
            r1.append(r2)
            r10 = r22
            r1.append(r10)
            java.lang.String r2 = ",type="
            r1.append(r2)
            r11 = r23
            r1.append(r11)
            java.lang.String r2 = ",path="
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            com.taobao.gcanvas.util.GLog.d(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r24)
            if (r1 != 0) goto L_0x0173
            java.lang.Object r13 = new java.lang.Object
            r13.<init>()
            java.util.concurrent.atomic.AtomicBoolean r14 = new java.util.concurrent.atomic.AtomicBoolean
            r1 = 0
            r14.<init>(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "start to texSubImage2D in 3dmodule.start time = "
            r1.append(r2)
            long r4 = java.lang.System.currentTimeMillis()
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            com.taobao.gcanvas.util.GLog.d(r1)
            java.lang.String r1 = "data:image"
            boolean r1 = r0.startsWith(r1)     // Catch:{ Throwable -> 0x0169 }
            if (r1 == 0) goto L_0x0104
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0169 }
            r1.<init>()     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r2 = "[texSubImage2D] start to decode base64 texture in 3dmodule.start time = "
            r1.append(r2)     // Catch:{ Throwable -> 0x0169 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0169 }
            r1.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0169 }
            com.taobao.gcanvas.util.GLog.d(r1)     // Catch:{ Throwable -> 0x0169 }
            com.alibaba.weex.plugin.gcanvas.GCanvasImageLoader r1 = r12.mImageLoader     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0169 }
            r2.<init>()     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = "base64,"
            r2.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = "base64,"
            int r4 = r4.length()     // Catch:{ Throwable -> 0x0169 }
            r2.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0169 }
            int r2 = r0.indexOf(r2)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = r0.substring(r2)     // Catch:{ Throwable -> 0x0169 }
            android.graphics.Bitmap r4 = r1.handleBase64Texture(r0)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0169 }
            r0.<init>()     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = "[texSubImage2D] start to decode base64 texture in 3dmodule.end time = "
            r0.append(r1)     // Catch:{ Throwable -> 0x0169 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0169 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0169 }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0169 }
            if (r4 == 0) goto L_0x00fe
            java.lang.String r0 = "[texSubImage2D] start to bind base64 format texture in 3dmodule."
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0169 }
            r5 = 0
            r3 = r17
            r6 = r18
            r7 = r19
            r8 = r20
            r9 = r21
            r10 = r22
            r11 = r23
            com.taobao.gcanvas.GCanvasJNI.texSubImage2D(r3, r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x0169 }
            goto L_0x0103
        L_0x00fe:
            java.lang.String r0 = "[texSubImage2D] decode base64 texture failed,bitmap is null."
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x0169 }
        L_0x0103:
            return
        L_0x0104:
            com.taobao.phenix.intf.Phenix r1 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ Throwable -> 0x0169 }
            com.taobao.phenix.intf.PhenixCreator r0 = r1.load(r0)     // Catch:{ Throwable -> 0x0169 }
            com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$9 r15 = new com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$9     // Catch:{ Throwable -> 0x0169 }
            r1 = r15
            r2 = r16
            r3 = r17
            r4 = r18
            r5 = r19
            r6 = r20
            r7 = r21
            r8 = r22
            r9 = r23
            r10 = r13
            r11 = r14
            r1.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x0169 }
            com.taobao.phenix.intf.PhenixCreator r0 = r0.succListener(r15)     // Catch:{ Throwable -> 0x0169 }
            com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$8 r1 = new com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$8     // Catch:{ Throwable -> 0x0169 }
            r1.<init>(r13, r14)     // Catch:{ Throwable -> 0x0169 }
            com.taobao.phenix.intf.PhenixCreator r0 = r0.failListener(r1)     // Catch:{ Throwable -> 0x0169 }
            com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$7 r1 = new com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule$7     // Catch:{ Throwable -> 0x0169 }
            r1.<init>(r13, r14)     // Catch:{ Throwable -> 0x0169 }
            com.taobao.phenix.intf.PhenixCreator r0 = r0.cancelListener(r1)     // Catch:{ Throwable -> 0x0169 }
            r0.fetch()     // Catch:{ Throwable -> 0x0169 }
            monitor-enter(r13)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = "[texSubImage2D] start wait bindtexture in 3dmodule"
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ all -> 0x0166 }
            boolean r0 = r14.get()     // Catch:{ all -> 0x0166 }
            if (r0 != 0) goto L_0x014c
            r13.wait()     // Catch:{ all -> 0x0166 }
        L_0x014c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0166 }
            r0.<init>()     // Catch:{ all -> 0x0166 }
            java.lang.String r1 = "[texSubImage2D] finish wait bindtexture in 3dmodule,end time = "
            r0.append(r1)     // Catch:{ all -> 0x0166 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0166 }
            r0.append(r1)     // Catch:{ all -> 0x0166 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0166 }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ all -> 0x0166 }
            monitor-exit(r13)     // Catch:{ all -> 0x0166 }
            goto L_0x0173
        L_0x0166:
            r0 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x0166 }
            throw r0     // Catch:{ Throwable -> 0x0169 }
        L_0x0169:
            r0 = move-exception
            java.lang.String r1 = "GCanvasLightningModule"
            java.lang.String r2 = r0.getMessage()
            com.taobao.gcanvas.util.GLog.e(r1, r2, r0)
        L_0x0173:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.weex.plugin.gcanvas.GCanvasLightningModule.texSubImage2D(java.lang.String, int, int, int, int, int, int, java.lang.String):void");
    }

    static class ImageInfo {
        static final int IDLE = -1;
        static final int LOADED = 512;
        static final int LOADING = 256;
        public int height;
        public int id;
        public AtomicInteger status = new AtomicInteger(-1);
        public int width;

        ImageInfo() {
        }
    }
}
