package com.taobao.windvane.plugins;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.accs.common.Constants;
import com.taobao.gcanvas.GCanvasJNI;
import com.taobao.gcanvas.surface.GSurfaceView;
import com.taobao.gcanvas.util.GLog;
import com.taobao.gcanvas.util.GMonitor;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.PhenixEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.weex.analyzer.Config;
import com.taobao.windvane.plugins.GCanvasViewMgr;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

public class GCanvasPlugin extends WVApiPlugin {
    public static String PLUGIN_NAME = "GCanvas";
    static final String TAG = "GCANVASPLUGIN";
    private static HashMap<String, Double> dprMap = new HashMap<>();
    static ThreadLocal<Boolean> isLoopPrepare = new ThreadLocal<Boolean>() {
        /* access modifiers changed from: protected */
        public Boolean initialValue() {
            return false;
        }
    };
    private static GCanvasImageLoader mImageLoader = new GCanvasImageLoader();
    private static String mRefid;
    private static ContextType mType;
    private Activity mActivity = null;
    private boolean mInjected = false;
    private AtomicBoolean mIsDestroyed = new AtomicBoolean(false);
    private GCanvasViewMgr mViewMgr;

    public static void getDeviceInfo() {
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
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

    public GCanvasPlugin() {
        TaoLog.i(TAG, "gcanvas windvane plugin is created.");
        if (!isLoopPrepare.get().booleanValue()) {
            isLoopPrepare.set(true);
            Looper.getMainLooper();
            Looper.prepare();
        }
        TaoLog.i(TAG, "test InitActivity start canvas");
        TaoLog.i(TAG, "test InitActivity end canvas");
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        Log.d("luanxuan", "execute:  " + str);
        synchronized (this) {
            try {
                if (!"enable".equals(str) || str2 == null) {
                    if ("preLoadImage".equals(str) && str2 != null) {
                        JSONObject jSONObject = new JSONObject(str2);
                        preLoadImage(jSONObject.getJSONArray("data").getString(0), jSONObject.getJSONArray("data").getInt(1), wVCallBackContext);
                    } else if ("disable".equals(str)) {
                        if (this.mViewMgr != null) {
                            this.mViewMgr.onDestroy();
                        }
                        wVCallBackContext.success();
                    } else if ("setSize".equals(str) && str2 != null) {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (this.mViewMgr == null) {
                            wVCallBackContext.error("viewManager null");
                        } else if (this.mViewMgr.setSize(jSONObject2)) {
                            wVCallBackContext.success();
                        } else {
                            wVCallBackContext.error("canvas is null or JsonHandle Exception");
                        }
                    }
                } else if (this.mInjected) {
                    JSONObject jSONObject3 = new JSONObject(str2);
                    String string = jSONObject3.getString("componentId");
                    boolean z = jSONObject3.getBoolean("isFront");
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("size");
                    dprMap.put(string, Double.valueOf(jSONObject4.getDouble("dpr")));
                    boolean enable = enable(string, z, jSONObject4, wVCallBackContext);
                    if (wVCallBackContext != null) {
                        if (enable) {
                            wVCallBackContext.success();
                        } else {
                            wVCallBackContext.error("add canvas view error ");
                        }
                    }
                } else if (this.mWebView != null && (this.mWebView instanceof WVUCWebView) && ((WVUCWebView) this.mWebView).getCurrentViewCoreType() == 3) {
                    boolean injectJavascriptNativeCallback = ((WVUCWebView) this.mWebView).getUCExtension().injectJavascriptNativeCallback(GCanvasJNI.getWindvaneNativeFuncPtr(), 0);
                    Log.i("gcanvas-windvane", "inject success: " + injectJavascriptNativeCallback);
                    if (injectJavascriptNativeCallback) {
                        this.mInjected = true;
                        JSONObject jSONObject5 = new JSONObject(str2);
                        String string2 = jSONObject5.getString("componentId");
                        boolean z2 = jSONObject5.getBoolean("isFront");
                        JSONObject jSONObject6 = jSONObject5.getJSONObject("size");
                        dprMap.put(string2, Double.valueOf(jSONObject6.getDouble("dpr")));
                        boolean enable2 = enable(string2, z2, jSONObject6, wVCallBackContext);
                        if (wVCallBackContext != null) {
                            if (enable2) {
                                wVCallBackContext.success();
                            } else {
                                wVCallBackContext.error("add canvas view error ");
                            }
                        }
                    } else if (wVCallBackContext != null) {
                        wVCallBackContext.error("inject callback error");
                    }
                } else if (wVCallBackContext != null) {
                    wVCallBackContext.error("ucwebview not prepared");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (wVCallBackContext != null) {
                    wVCallBackContext.error("JSONException");
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    public static boolean isAvailable(Context context) {
        return ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072;
    }

    public void initialize(Context context, IWVWebView iWVWebView) {
        super.initialize(context, iWVWebView);
        Log.d("luanxuan", "initialize");
        if (!isAvailable(context)) {
            throw new RuntimeException("gcanvas is not avaliable.");
        } else if (context instanceof Activity) {
            this.mActivity = (Activity) context;
        }
    }

    public void onDestroy() {
        TaoLog.i(TAG, "gcanvas windvane plugin is destoryed.");
        if (GCanvasJNI.getNativeFps(mRefid) != 0) {
            GLog.d("monitor start.");
            MeasureValueSet create = MeasureValueSet.create();
            create.setValue(GMonitor.MEASURE_FPS, (double) GCanvasJNI.getNativeFps(mRefid));
            DimensionValueSet create2 = DimensionValueSet.create();
            create2.setValue(GMonitor.DIMENSION_PLUGIN, "windvane");
            create2.setValue(GMonitor.DIMENSION_TYPE, String.valueOf(mType));
            GMonitor.commitStat(GMonitor.MONITOR_POINT_FPS, create2, create);
            GLog.d("monitor end.");
        }
        this.mIsDestroyed.set(true);
        this.mActivity = null;
        if (this.mViewMgr != null) {
            this.mViewMgr.onDestroy();
        }
        GCanvasJNI.destroyWVGRef();
        this.mInjected = false;
    }

    public static void executeFromNative(String str, String str2) {
        try {
            JSONObject jSONObject = new JSONObject(str2);
            String string = jSONObject.getString("action");
            char c = 65535;
            switch (string.hashCode()) {
                case -1224576314:
                    if (string.equals("texImage2D")) {
                        c = 2;
                        break;
                    }
                    break;
                case -309915358:
                    if (string.equals("setLogLevel")) {
                        c = 5;
                        break;
                    }
                    break;
                case -123030371:
                    if (string.equals("setDevicePixelRatio")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1178770461:
                    if (string.equals("bindImageTexture")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1780122567:
                    if (string.equals("setContextType")) {
                        c = 0;
                        break;
                    }
                    break;
                case 2015424020:
                    if (string.equals("texSubImage2D")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    setContextType(str, jSONObject.getString("type"));
                    return;
                case 1:
                    bindImageTexture(str, jSONObject.getJSONArray("data").getString(0), jSONObject.getJSONArray("data").getInt(1));
                    return;
                case 2:
                    texImage2D(str, jSONObject.getInt(Constants.KEY_TARGET), jSONObject.getInt("level"), jSONObject.getInt("internalFormat"), jSONObject.getInt("format"), jSONObject.getInt("type"), jSONObject.getString("path"));
                    return;
                case 3:
                    texSubImage2D(str, jSONObject.getInt(Constants.KEY_TARGET), jSONObject.getInt("level"), jSONObject.getInt("xoffset"), jSONObject.getInt("yoffset"), jSONObject.getInt("format"), jSONObject.getInt("type"), jSONObject.getString("path"));
                    return;
                case 4:
                    setDevicePixelRatio(str, jSONObject.getDouble("dpr"));
                    return;
                case 5:
                    setLogLevel(jSONObject.getString("level"));
                    return;
                default:
                    return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void preLoadImage(String str, int i, WVCallBackContext wVCallBackContext) {
        if (!TextUtils.isEmpty(str)) {
            try {
                if (mImageLoader != null) {
                    mImageLoader.loadImage(str, i, wVCallBackContext);
                }
            } catch (Throwable th) {
                GLog.e(TAG, th.getMessage(), th);
            }
        }
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public boolean enable(String str, boolean z, JSONObject jSONObject, WVCallBackContext wVCallBackContext) {
        if (!(this.mWebView instanceof WVUCWebView)) {
            return false;
        }
        GSurfaceView gSurfaceView = new GSurfaceView(this.mWebView.getContext(), str);
        if (this.mViewMgr == null) {
            this.mViewMgr = new GCanvasViewMgr((WVUCWebView) this.mWebView, this.mActivity);
        }
        return this.mViewMgr.addCanvas(str, gSurfaceView, z ? GCanvasViewMgr.CanvasAddType.FRONT : GCanvasViewMgr.CanvasAddType.BELOW, jSONObject);
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public static void setContextType(String str, String str2) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
            ContextType contextType = ContextType._2D;
            if (Config.TYPE_3D.equals(str2) || "1".equals(str2)) {
                contextType = ContextType._3D;
            }
            mType = contextType;
            mRefid = str;
            GCanvasJNI.setContextType(str, contextType.value());
            GCanvasJNI.setDevicePixelRatio(str, dprMap.get(str).doubleValue());
        }
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public static void setDevicePixelRatio(String str, double d) {
        if (mImageLoader != null) {
            mImageLoader.setDpr(d);
        }
        GCanvasJNI.setDevicePixelRatio(str, d);
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public static void setLogLevel(String str) {
        GCanvasJNI.setLogLevel(str);
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public static void bindImageTexture(final String str, String str2, final int i) {
        Log.i("luanxuan", "enter bindImageTexture: " + str2);
        if (!TextUtils.isEmpty(str2)) {
            final Object obj = new Object();
            GLog.d("start to load texture in 2dmodule.start time = " + System.currentTimeMillis());
            try {
                if (str2.startsWith("data:image")) {
                    GLog.d("start to decode base64 texture in 2dmodule.start time = " + System.currentTimeMillis());
                    Bitmap handleBase64Texture = mImageLoader.handleBase64Texture(str2.substring(str2.indexOf("base64,") + "base64,".length()));
                    GLog.d("start to decode base64 texture in 2dmodule.end time = " + System.currentTimeMillis());
                    if (handleBase64Texture != null) {
                        GLog.d("start to bind base64 format texture in 2dmodule.");
                        GCanvasJNI.bindTexture(str, handleBase64Texture, i, 3553, 0, 6408, 6408, 5121);
                        return;
                    }
                    GLog.d("decode base64 texture failed,bitmap is null.");
                    return;
                }
                Phenix.instance().load(str2).succListener(new IPhenixListener<SuccPhenixEvent>() {
                    public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                        Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                        if (bitmap != null) {
                            Log.i("luanxuan", "start to bindtexture in 2dmodule.");
                            GCanvasJNI.bindTexture(str, bitmap, i, 3553, 0, 6408, 6408, 5121);
                        } else {
                            GLog.d("bitmap is null in teximage2D.");
                        }
                        synchronized (obj) {
                            GLog.d("finish bindtexture in 2dmodule.");
                            obj.notifyAll();
                        }
                        return true;
                    }
                }).failListener(new IPhenixListener<FailPhenixEvent>() {
                    public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                        GLog.d("teximage2D load picture failed.");
                        synchronized (obj) {
                            GLog.d("finish bindtexture in 2dmodule.");
                            obj.notifyAll();
                        }
                        return true;
                    }
                }).cancelListener(new IPhenixListener<PhenixEvent>() {
                    public boolean onHappen(PhenixEvent phenixEvent) {
                        GLog.d("teximage2D load picture cancel.");
                        synchronized (obj) {
                            GLog.d("finish bindtexture in 2dmodule.");
                            obj.notifyAll();
                        }
                        return true;
                    }
                }).fetch();
                synchronized (obj) {
                    obj.wait();
                    GLog.d("finish wait bindtexture in 2dmodule,end time = " + System.currentTimeMillis());
                }
            } catch (Throwable th) {
                GLog.e(TAG, th.getMessage(), th);
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    @android.webkit.JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void texSubImage2D(java.lang.String r15, int r16, int r17, int r18, int r19, int r20, int r21, java.lang.String r22) {
        /*
            r0 = r22
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "texSubImage2D in 3dmodule,refid="
            r1.append(r2)
            r2 = r15
            r1.append(r15)
            java.lang.String r3 = ",target="
            r1.append(r3)
            r6 = r16
            r1.append(r6)
            java.lang.String r3 = ",level="
            r1.append(r3)
            r7 = r17
            r1.append(r7)
            java.lang.String r3 = ",xoffset="
            r1.append(r3)
            r8 = r18
            r1.append(r8)
            java.lang.String r3 = ",yoffset="
            r1.append(r3)
            r9 = r19
            r1.append(r9)
            java.lang.String r3 = ",format="
            r1.append(r3)
            r10 = r20
            r1.append(r10)
            java.lang.String r3 = ",type="
            r1.append(r3)
            r11 = r21
            r1.append(r11)
            java.lang.String r3 = ",path="
            r1.append(r3)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            com.taobao.gcanvas.util.GLog.d(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r22)
            if (r1 != 0) goto L_0x0159
            java.lang.Object r1 = new java.lang.Object
            r1.<init>()
            java.util.concurrent.atomic.AtomicBoolean r13 = new java.util.concurrent.atomic.AtomicBoolean
            r3 = 0
            r13.<init>(r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "start to texSubImage2D in 3dmodule.start time = "
            r3.append(r4)
            long r4 = java.lang.System.currentTimeMillis()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.taobao.gcanvas.util.GLog.d(r3)
            java.lang.String r3 = "data:image"
            boolean r3 = r0.startsWith(r3)     // Catch:{ Throwable -> 0x014f }
            if (r3 == 0) goto L_0x00f2
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x014f }
            r1.<init>()     // Catch:{ Throwable -> 0x014f }
            java.lang.String r3 = "[texSubImage2D] start to decode base64 texture in 3dmodule.start time = "
            r1.append(r3)     // Catch:{ Throwable -> 0x014f }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x014f }
            r1.append(r3)     // Catch:{ Throwable -> 0x014f }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x014f }
            com.taobao.gcanvas.util.GLog.d(r1)     // Catch:{ Throwable -> 0x014f }
            com.taobao.windvane.plugins.GCanvasImageLoader r1 = mImageLoader     // Catch:{ Throwable -> 0x014f }
            java.lang.String r3 = "base64,"
            int r3 = r0.indexOf(r3)     // Catch:{ Throwable -> 0x014f }
            java.lang.String r4 = "base64,"
            int r4 = r4.length()     // Catch:{ Throwable -> 0x014f }
            int r3 = r3 + r4
            java.lang.String r0 = r0.substring(r3)     // Catch:{ Throwable -> 0x014f }
            android.graphics.Bitmap r4 = r1.handleBase64Texture(r0)     // Catch:{ Throwable -> 0x014f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x014f }
            r0.<init>()     // Catch:{ Throwable -> 0x014f }
            java.lang.String r1 = "[texSubImage2D] start to decode base64 texture in 3dmodule.end time = "
            r0.append(r1)     // Catch:{ Throwable -> 0x014f }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x014f }
            r0.append(r12)     // Catch:{ Throwable -> 0x014f }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x014f }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x014f }
            if (r4 == 0) goto L_0x00ec
            java.lang.String r0 = "[texSubImage2D] start to bind base64 format texture in 3dmodule."
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x014f }
            r5 = 0
            r3 = r15
            r6 = r16
            r7 = r17
            r8 = r18
            r9 = r19
            r10 = r20
            r11 = r21
            com.taobao.gcanvas.GCanvasJNI.texSubImage2D(r3, r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x014f }
            goto L_0x00f1
        L_0x00ec:
            java.lang.String r0 = "[texSubImage2D] decode base64 texture failed,bitmap is null."
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ Throwable -> 0x014f }
        L_0x00f1:
            return
        L_0x00f2:
            com.taobao.phenix.intf.Phenix r3 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ Throwable -> 0x014f }
            com.taobao.phenix.intf.PhenixCreator r0 = r3.load(r0)     // Catch:{ Throwable -> 0x014f }
            com.taobao.windvane.plugins.GCanvasPlugin$7 r14 = new com.taobao.windvane.plugins.GCanvasPlugin$7     // Catch:{ Throwable -> 0x014f }
            r3 = r14
            r4 = r15
            r5 = r16
            r6 = r17
            r7 = r18
            r8 = r19
            r9 = r20
            r10 = r21
            r11 = r1
            r12 = r13
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Throwable -> 0x014f }
            com.taobao.phenix.intf.PhenixCreator r0 = r0.succListener(r14)     // Catch:{ Throwable -> 0x014f }
            com.taobao.windvane.plugins.GCanvasPlugin$6 r2 = new com.taobao.windvane.plugins.GCanvasPlugin$6     // Catch:{ Throwable -> 0x014f }
            r2.<init>(r1, r13)     // Catch:{ Throwable -> 0x014f }
            com.taobao.phenix.intf.PhenixCreator r0 = r0.failListener(r2)     // Catch:{ Throwable -> 0x014f }
            com.taobao.windvane.plugins.GCanvasPlugin$5 r2 = new com.taobao.windvane.plugins.GCanvasPlugin$5     // Catch:{ Throwable -> 0x014f }
            r2.<init>(r1, r13)     // Catch:{ Throwable -> 0x014f }
            com.taobao.phenix.intf.PhenixCreator r0 = r0.cancelListener(r2)     // Catch:{ Throwable -> 0x014f }
            r0.fetch()     // Catch:{ Throwable -> 0x014f }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x014f }
            boolean r0 = r13.get()     // Catch:{ all -> 0x014c }
            if (r0 != 0) goto L_0x0132
            r1.wait()     // Catch:{ all -> 0x014c }
        L_0x0132:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x014c }
            r0.<init>()     // Catch:{ all -> 0x014c }
            java.lang.String r2 = "[texSubImage2D] finish wait bindtexture in 3dmodule,end time = "
            r0.append(r2)     // Catch:{ all -> 0x014c }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x014c }
            r0.append(r2)     // Catch:{ all -> 0x014c }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x014c }
            com.taobao.gcanvas.util.GLog.d(r0)     // Catch:{ all -> 0x014c }
            monitor-exit(r1)     // Catch:{ all -> 0x014c }
            goto L_0x0159
        L_0x014c:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x014c }
            throw r0     // Catch:{ Throwable -> 0x014f }
        L_0x014f:
            r0 = move-exception
            java.lang.String r1 = "GCANVASPLUGIN"
            java.lang.String r2 = r0.getMessage()
            com.taobao.gcanvas.util.GLog.e(r1, r2, r0)
        L_0x0159:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.windvane.plugins.GCanvasPlugin.texSubImage2D(java.lang.String, int, int, int, int, int, int, java.lang.String):void");
    }

    @JavascriptInterface
    @com.uc.webview.export.JavascriptInterface
    public static void texImage2D(String str, int i, int i2, int i3, int i4, int i5, String str2) {
        String str3 = str2;
        StringBuilder sb = new StringBuilder();
        sb.append("texImage2D in 3dmodule,refid=");
        String str4 = str;
        sb.append(str);
        sb.append(",target=");
        int i6 = i;
        sb.append(i);
        sb.append(",level=");
        sb.append(i2);
        sb.append(",internalformat=");
        sb.append(i3);
        sb.append(",format=");
        sb.append(i4);
        sb.append(",type=");
        sb.append(i5);
        sb.append(",path=");
        sb.append(str3);
        GLog.d(sb.toString());
        if (!TextUtils.isEmpty(str2)) {
            final Object obj = new Object();
            final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            GLog.d("start to load texture in 3dmodule.start time = " + System.currentTimeMillis());
            try {
                if (str3.startsWith("data:image")) {
                    GLog.d("start to decode base64 texture in 3dmodule.start time = " + System.currentTimeMillis());
                    Bitmap handleBase64Texture = mImageLoader.handleBase64Texture(str3.substring(str3.indexOf("base64,") + "base64,".length()));
                    GLog.d("start to decode base64 texture in 3dmodule.end time = " + System.currentTimeMillis());
                    if (handleBase64Texture != null) {
                        GLog.d("start to bind base64 format texture in 3dmodule.");
                        GCanvasJNI.bindTexture(str, handleBase64Texture, 0, i, i2, i3, i4, i5);
                        return;
                    }
                    GLog.d("decode base64 texture failed,bitmap is null.");
                    return;
                }
                final String str5 = str;
                final int i7 = i;
                final int i8 = i2;
                final int i9 = i3;
                final int i10 = i4;
                final int i11 = i5;
                final Object obj2 = obj;
                final AtomicBoolean atomicBoolean2 = atomicBoolean;
                Phenix.instance().load(str3).succListener(new IPhenixListener<SuccPhenixEvent>() {
                    public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                        Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                        if (bitmap != null) {
                            GLog.d("start to bindtexture in 3dmodule.");
                            GCanvasJNI.bindTexture(str5, bitmap, 0, i7, i8, i9, i10, i11);
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
}
