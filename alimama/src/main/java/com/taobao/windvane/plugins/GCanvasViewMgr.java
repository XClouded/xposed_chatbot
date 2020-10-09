package com.taobao.windvane.plugins;

import android.app.Activity;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.taobao.gcanvas.surface.GSurfaceView;
import com.taobao.gcanvas.util.GLog;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class GCanvasViewMgr {
    private Activity mActivity;
    private ViewGroup mCanvasRootViewGroup;
    private HashMap<String, CanvasAddType> mCanvasTypes = new HashMap<>();
    private HashMap<String, GSurfaceView> mCanvasViews = new HashMap<>();
    private WVUCWebView mWebView;

    enum CanvasAddType {
        FRONT,
        BELOW
    }

    private static class CanvasSize {
        double dpr;
        int height;
        int offsetLeft;
        int offsetTop;
        int width;

        public CanvasSize(JSONObject jSONObject) throws JSONException {
            this.width = jSONObject.getInt("width");
            this.height = jSONObject.getInt("height");
            this.offsetLeft = jSONObject.getInt("offsetLeft");
            this.offsetTop = jSONObject.getInt("offsetTop");
            this.dpr = jSONObject.getDouble("dpr");
        }
    }

    public GCanvasViewMgr(WVUCWebView wVUCWebView, Activity activity) {
        this.mActivity = activity;
        this.mWebView = wVUCWebView;
    }

    public boolean addCanvas(String str, GSurfaceView gSurfaceView, CanvasAddType canvasAddType, JSONObject jSONObject) {
        if (this.mCanvasViews == null || this.mCanvasTypes == null) {
            return false;
        }
        this.mCanvasViews.put(str, gSurfaceView);
        this.mCanvasTypes.put(str, canvasAddType);
        try {
            boolean attachViews = attachViews(str, new CanvasSize(jSONObject), canvasAddType == CanvasAddType.FRONT);
            this.mWebView.getCoreView().setVisibility(0);
            gSurfaceView.setVisibility(0);
            return attachViews;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setSize(JSONObject jSONObject) {
        try {
            CanvasSize canvasSize = new CanvasSize(jSONObject);
            GSurfaceView gSurfaceView = this.mCanvasViews.get(jSONObject.get("componentId"));
            if (gSurfaceView == null) {
                return false;
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) gSurfaceView.getLayoutParams();
            double d = (double) canvasSize.width;
            double d2 = canvasSize.dpr;
            Double.isNaN(d);
            layoutParams.width = (int) (d * d2);
            double d3 = (double) canvasSize.height;
            double d4 = canvasSize.dpr;
            Double.isNaN(d3);
            layoutParams.height = (int) (d3 * d4);
            double d5 = (double) canvasSize.offsetLeft;
            double d6 = canvasSize.dpr;
            Double.isNaN(d5);
            layoutParams.leftMargin = (int) (d5 * d6);
            double d7 = (double) canvasSize.offsetTop;
            double d8 = canvasSize.dpr;
            Double.isNaN(d7);
            layoutParams.topMargin = (int) (d7 * d8);
            gSurfaceView.setLayoutParams(layoutParams);
            return true;
        } catch (JSONException unused) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean attachViews(String str, CanvasSize canvasSize, boolean z) {
        GSurfaceView gSurfaceView;
        if (canvasSize == null || str == null || this.mActivity == null) {
            return false;
        }
        if (this.mCanvasRootViewGroup == null) {
            this.mCanvasRootViewGroup = new FrameLayout(this.mActivity);
            if (this.mWebView != null) {
                ViewGroup.LayoutParams layoutParams = this.mWebView.getCoreView().getLayoutParams();
                ViewParent parent = this.mWebView.getCoreView().getParent();
                ViewGroup viewGroup = (parent == null || !(parent instanceof ViewGroup)) ? null : (ViewGroup) parent;
                if (viewGroup != null) {
                    viewGroup.addView(this.mCanvasRootViewGroup, layoutParams);
                } else {
                    GLog.w("CANVAS", "webview has no parent which type is ViewGroup. Attach to the actitity.");
                    if (this.mActivity != null) {
                        this.mActivity.setContentView(this.mCanvasRootViewGroup);
                    } else {
                        GLog.w("CANVAS", "Attach to the actitity failed.");
                        dettachView(this.mCanvasRootViewGroup);
                        this.mCanvasRootViewGroup = null;
                        return false;
                    }
                }
                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
                dettachView(this.mWebView.getCoreView());
                this.mCanvasRootViewGroup.addView(this.mWebView.getCoreView(), layoutParams2);
            } else {
                dettachView(this.mCanvasRootViewGroup);
                this.mCanvasRootViewGroup = null;
                return false;
            }
        }
        double d = (double) canvasSize.width;
        double d2 = canvasSize.dpr;
        Double.isNaN(d);
        double d3 = (double) canvasSize.height;
        double d4 = canvasSize.dpr;
        Double.isNaN(d3);
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams((int) (d * d2), (int) (d3 * d4));
        double d5 = (double) canvasSize.offsetLeft;
        double d6 = canvasSize.dpr;
        Double.isNaN(d5);
        double d7 = (double) canvasSize.offsetTop;
        double d8 = canvasSize.dpr;
        Double.isNaN(d7);
        layoutParams3.setMargins((int) (d5 * d6), (int) (d7 * d8), 0, 0);
        if (this.mCanvasViews == null || (gSurfaceView = this.mCanvasViews.get(str)) == null) {
            return false;
        }
        dettachView(gSurfaceView);
        if (z) {
            this.mCanvasRootViewGroup.addView(gSurfaceView, layoutParams3);
        } else {
            this.mCanvasRootViewGroup.addView(gSurfaceView, 0, layoutParams3);
        }
        this.mCanvasRootViewGroup.setBackgroundColor(0);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void dettachView(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void onDestroy() {
        if (this.mCanvasViews != null) {
            for (GSurfaceView next : this.mCanvasViews.values()) {
                next.setSurfaceTextureListener((TextureView.SurfaceTextureListener) null);
                next.requestExit();
                dettachView(next);
            }
        }
        this.mCanvasViews = null;
        this.mCanvasRootViewGroup = null;
    }
}
