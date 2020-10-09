package com.taobao.taolive.weexext.drawboard;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.alimama.union.app.configproperties.EnvHelper;
import com.taobao.taolive.uikit.drawboard.IDrawBoardCallback;
import com.taobao.taolive.uikit.drawboard.IImagePathCallback;
import com.taobao.taolive.uikit.drawboard.TBLiveDrawBoard;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXResourceUtils;
import java.util.HashMap;

public class TBLiveDrawBoardWeexComponent extends WXComponent {
    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String LINE_COLOR = "lineColor";
    private static final String LINE_WIDTH = "lineWidth";
    public static final String NAME = "livecanvas";
    private TBLiveDrawBoard mDrawBoard;
    private IDrawBoardCallback mDrawBoardCallback = new IDrawBoardCallback() {
        public void onDetectProgressUpdate(int i) {
        }

        public void onDetectSucceeded() {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, "prepare onSucceeded");
            HashMap hashMap = new HashMap();
            hashMap.put("result", "1");
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), EnvHelper.API_ENV_PREPARE, hashMap);
        }

        public void onDetectFailed() {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, " prepare onFailed");
            HashMap hashMap = new HashMap();
            hashMap.put("result", "0");
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), EnvHelper.API_ENV_PREPARE, hashMap);
        }

        public void onBeginDraw() {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, "begin draw ");
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), "drawstart", new HashMap());
        }

        public void onEndDraw() {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, "end draw ");
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), "drawend", new HashMap());
        }

        public void onUploaded(String str) {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, "on uploaded");
            HashMap hashMap = new HashMap();
            hashMap.put("result", str);
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), "upload", hashMap);
        }
    };
    private IImagePathCallback mImagePathCallback = new IImagePathCallback() {
        public void onBitmapSaveSuccess(String str) {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, " onBitmapSaveSuccess");
            HashMap hashMap = new HashMap();
            hashMap.put("result", str);
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), "save", hashMap);
        }

        public void onBitmapSaveFail() {
            Log.d(TBLiveDrawBoardWeexComponent.NAME, "onBitmapSaveFail");
            HashMap hashMap = new HashMap();
            hashMap.put("result", "");
            WXSDKManager.getInstance().fireEvent(TBLiveDrawBoardWeexComponent.this.getInstanceId(), TBLiveDrawBoardWeexComponent.this.getRef(), "save", hashMap);
        }
    };
    private FrameLayout mRootView;

    public TBLiveDrawBoardWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public TBLiveDrawBoardWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, i, basicComponentData);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0053  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r3, java.lang.Object r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = -1822070833(0xffffffff936567cf, float:-2.8955045E-27)
            if (r0 == r1) goto L_0x0028
            r1 = -1803786702(0xffffffff947c6632, float:-1.2742903E-26)
            if (r0 == r1) goto L_0x001e
            r1 = 1287124693(0x4cb7f6d5, float:9.6450216E7)
            if (r0 == r1) goto L_0x0014
            goto L_0x0032
        L_0x0014:
            java.lang.String r0 = "backgroundColor"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 2
            goto L_0x0033
        L_0x001e:
            java.lang.String r0 = "lineWidth"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 1
            goto L_0x0033
        L_0x0028:
            java.lang.String r0 = "lineColor"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 0
            goto L_0x0033
        L_0x0032:
            r0 = -1
        L_0x0033:
            switch(r0) {
                case 0: goto L_0x0053;
                case 1: goto L_0x0041;
                case 2: goto L_0x0037;
                default: goto L_0x0036;
            }
        L_0x0036:
            goto L_0x005c
        L_0x0037:
            java.lang.String r0 = "#FFF7ED"
            java.lang.String r0 = com.taobao.weex.utils.WXUtils.getString(r4, r0)
            r2.setClearColor(r0)
            goto L_0x005c
        L_0x0041:
            r0 = 8
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.Integer r0 = com.taobao.weex.utils.WXUtils.getInteger(r4, r0)
            int r0 = r0.intValue()
            r2.lineWidth(r0)
            goto L_0x005c
        L_0x0053:
            java.lang.String r0 = "#E10D0D"
            java.lang.String r0 = com.taobao.weex.utils.WXUtils.getString(r4, r0)
            r2.lineColor(r0)
        L_0x005c:
            boolean r3 = super.setProperty(r3, r4)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.taolive.weexext.drawboard.TBLiveDrawBoardWeexComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "lineColor")
    public void lineColor(String str) {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "paintColor: " + str);
            this.mDrawBoard.setPaintColor(HexToColor(str));
        }
    }

    @WXComponentProp(name = "lineWidth")
    public void lineWidth(int i) {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "paintWidth: " + i);
            this.mDrawBoard.setPaintWidth(i);
        }
    }

    @WXComponentProp(name = "backgroundColor")
    private void setClearColor(String str) {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "backgroundColor: " + str);
            this.mDrawBoard.setClearColor(HexToColor(str));
        }
    }

    /* access modifiers changed from: protected */
    public FrameLayout initComponentHostView(@NonNull Context context) {
        this.mRootView = new FrameLayout(context);
        this.mDrawBoard = new TBLiveDrawBoard(context);
        this.mDrawBoard.setDrawBoardCallback(this.mDrawBoardCallback);
        this.mRootView.addView(this.mDrawBoard);
        return this.mRootView;
    }

    @JSMethod
    public void prepare() {
        if (this.mDrawBoard != null) {
            Log.d(NAME, EnvHelper.API_ENV_PREPARE);
            this.mDrawBoard.prepareNet();
        }
    }

    @JSMethod
    public void clear() {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "clear");
            this.mDrawBoard.clear();
        }
    }

    @JSMethod
    public void detect() {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "detect");
            WXSDKManager.getInstance().fireEvent(getInstanceId(), getRef(), "detect", this.mDrawBoard.doodleDetect());
            return;
        }
        WXSDKManager.getInstance().fireEvent(getInstanceId(), getRef(), "detect");
    }

    @JSMethod
    public void save() {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "save");
            this.mDrawBoard.getImagePath(this.mImagePathCallback);
        }
    }

    @JSMethod
    public void upload() {
        if (this.mDrawBoard != null) {
            Log.d(NAME, "upload");
            this.mDrawBoard.uploadImage("livedraw");
        }
    }

    public void destroy() {
        super.destroy();
        if (this.mRootView != null) {
            this.mRootView.removeAllViews();
        }
        this.mRootView = null;
        if (this.mDrawBoard != null) {
            this.mDrawBoard.setDrawBoardCallback((IDrawBoardCallback) null);
            this.mDrawBoard.destroy();
        }
        this.mDrawBoard = null;
        this.mImagePathCallback = null;
        this.mDrawBoardCallback = null;
    }

    private static int HexToColor(String str) {
        try {
            return WXResourceUtils.getColor(str);
        } catch (Exception e) {
            e.printStackTrace();
            return -16777216;
        }
    }
}
