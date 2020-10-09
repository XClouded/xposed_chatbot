package com.alibaba.aliweex.adapter.component;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.ICheckBindingScroller;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class WXParallax extends WXDiv implements OnWXScrollListener, ICheckBindingScroller {
    public static final String BINDING_SCROLLER = "bindingScroller";
    public static final String PARALLAX = "parallax";
    public static final String WX_OPACITY = "opacity";
    public static final String WX_TRANSFORM = "transform";
    BackgroundColorCreator mBackgroundColor;
    String mBindingRef = "";
    /* access modifiers changed from: private */
    public float mOffsetY = 0.0f;
    private Integer mPreBackGroundColor = null;
    ArrayList<TransformCreator> mTransformPropArrayList = new ArrayList<>();

    public void onScrollStateChanged(View view, int i, int i2, int i3) {
    }

    public WXParallax(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        initTransform(String.valueOf(getAttrs().get("transform")));
        initOpacity(String.valueOf(getAttrs().get("opacity")));
        initBackgroundColor(String.valueOf(getAttrs().get("backgroundColor")));
        this.mBindingRef = (String) getAttrs().get(BINDING_SCROLLER);
        wXSDKInstance.registerOnWXScrollListener(this);
    }

    private void initBackgroundColor(String str) {
        JSONObject parseObject;
        if (!TextUtils.isEmpty(str) && (parseObject = JSON.parseObject(str)) != null) {
            this.mBackgroundColor = new BackgroundColorCreator();
            JSONArray jSONArray = parseObject.getJSONArray("in");
            this.mBackgroundColor.input = new int[jSONArray.size()];
            for (int i = 0; i < jSONArray.size(); i++) {
                this.mBackgroundColor.input[i] = (int) WXViewUtils.getRealPxByWidth((float) jSONArray.getInteger(i).intValue(), getInstance().getInstanceViewPortWidth());
            }
            JSONArray jSONArray2 = parseObject.getJSONArray("out");
            this.mBackgroundColor.output = new int[jSONArray2.size()];
            for (int i2 = 0; i2 < jSONArray2.size(); i2++) {
                this.mBackgroundColor.output[i2] = WXResourceUtils.getColor(jSONArray2.getString(i2));
            }
        }
    }

    private void initOpacity(String str) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.w("WXParallax initOpacity opacity == null");
            return;
        }
        JSONObject parseObject = JSON.parseObject(str);
        if (parseObject != null) {
            this.mTransformPropArrayList.add(new TransformCreator("opacity", parseObject));
        }
    }

    private void initTransform(String str) {
        if (str == null) {
            WXLogUtils.w("WXParallax initTransform == null");
            return;
        }
        JSONArray parseArray = JSON.parseArray(str);
        if (parseArray != null) {
            for (int i = 0; i < parseArray.size(); i++) {
                JSONObject jSONObject = parseArray.getJSONObject(i);
                this.mTransformPropArrayList.add(i, new TransformCreator(jSONObject.getString("type"), jSONObject));
            }
        }
    }

    public boolean isNeedScroller(String str, Object obj) {
        WXComponent rootComponent;
        Scrollable firstScroller;
        this.mBindingRef = (String) getAttrs().get(BINDING_SCROLLER);
        if (TextUtils.isEmpty(this.mBindingRef) && (rootComponent = getInstance().getRootComponent()) != null && (rootComponent instanceof WXVContainer) && (firstScroller = rootComponent.getFirstScroller()) != null) {
            this.mBindingRef = firstScroller.getRef();
        }
        return !TextUtils.isEmpty(this.mBindingRef) && !TextUtils.isEmpty(str) && str.equals(this.mBindingRef);
    }

    public void onScrolled(View view, int i, int i2) {
        float f = (float) i2;
        this.mOffsetY += f;
        if (getHostView() != null) {
            Iterator<TransformCreator> it = this.mTransformPropArrayList.iterator();
            while (it.hasNext()) {
                it.next().animate((float) i, f);
            }
            if (this.mBackgroundColor != null) {
                int color = this.mBackgroundColor.getColor(i, i2);
                if (this.mPreBackGroundColor == null || this.mPreBackGroundColor.intValue() != color) {
                    ((WXFrameLayout) getHostView()).setBackgroundColor(color);
                    this.mPreBackGroundColor = Integer.valueOf(color);
                }
            }
        }
    }

    private class BackgroundColorCreator {
        int[] input;
        int[] output;

        private BackgroundColorCreator() {
        }

        /* access modifiers changed from: package-private */
        public int getColor(int i, int i2) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("WXParallax:getColor: XDelta" + i + " YDelta:" + i2 + " mOffsetY" + WXParallax.this.mOffsetY);
            }
            if (WXParallax.this.mOffsetY > ((float) this.input[1])) {
                return this.output[1];
            }
            if (WXParallax.this.mOffsetY < ((float) this.input[0])) {
                return this.output[0];
            }
            int red = Color.red(this.output[0]) + ((((int) (WXParallax.this.mOffsetY - ((float) this.input[0]))) * (Color.red(this.output[1]) - Color.red(this.output[0]))) / (this.input[1] - this.input[0]));
            int green = Color.green(this.output[0]) + ((((int) (WXParallax.this.mOffsetY - ((float) this.input[0]))) * (Color.green(this.output[1]) - Color.green(this.output[0]))) / (this.input[1] - this.input[0]));
            int blue = Color.blue(this.output[0]) + ((((int) (WXParallax.this.mOffsetY - ((float) this.input[0]))) * (Color.blue(this.output[1]) - Color.blue(this.output[0]))) / (this.input[1] - this.input[0]));
            int alpha = Color.alpha(this.output[0]) + ((((int) (WXParallax.this.mOffsetY - ((float) this.input[0]))) * (Color.alpha(this.output[1]) - Color.alpha(this.output[0]))) / (this.input[1] - this.input[0]));
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("WXParallax:getColor: r1" + red + " g1:" + green + " b1:" + blue);
            }
            return Color.argb(alpha, red, green, blue);
        }
    }

    private class TransformCreator {
        float fromOpacity;
        float fromRotate;
        float fromScaleX;
        float fromScaleY;
        float fromTranslateX;
        float fromTranslateY;
        float[] input;
        float[] output;
        String transformType;

        /* JADX WARNING: Removed duplicated region for block: B:25:0x0086  */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x008d  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0094  */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x00a1  */
        /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        TransformCreator(java.lang.String r8, com.alibaba.fastjson.JSONObject r9) {
            /*
                r6 = this;
                com.alibaba.aliweex.adapter.component.WXParallax.this = r7
                r6.<init>()
                r6.transformType = r8
                java.lang.String r8 = "in"
                com.alibaba.fastjson.JSONArray r8 = r9.getJSONArray(r8)
                int r0 = r8.size()
                float[] r1 = new float[r0]
                r6.input = r1
                r1 = 0
                r2 = 0
            L_0x0017:
                if (r2 >= r0) goto L_0x0030
                float[] r3 = r6.input
                float r4 = r8.getFloatValue(r2)
                com.taobao.weex.WXSDKInstance r5 = r7.getInstance()
                int r5 = r5.getInstanceViewPortWidth()
                float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r4, r5)
                r3[r2] = r4
                int r2 = r2 + 1
                goto L_0x0017
            L_0x0030:
                java.lang.String r8 = "out"
                com.alibaba.fastjson.JSONArray r8 = r9.getJSONArray(r8)
                float[] r8 = r6.parseParamArray(r8)
                r6.output = r8
                java.lang.String r8 = r6.transformType
                r9 = -1
                int r0 = r8.hashCode()
                r2 = -1267206133(0xffffffffb477f80b, float:-2.3093905E-7)
                r3 = 1
                if (r0 == r2) goto L_0x0077
                r2 = -925180581(0xffffffffc8dadd5b, float:-448234.84)
                if (r0 == r2) goto L_0x006d
                r2 = 109250890(0x683094a, float:4.929037E-35)
                if (r0 == r2) goto L_0x0063
                r2 = 1052832078(0x3ec0f14e, float:0.376841)
                if (r0 == r2) goto L_0x0059
                goto L_0x0081
            L_0x0059:
                java.lang.String r0 = "translate"
                boolean r8 = r8.equals(r0)
                if (r8 == 0) goto L_0x0081
                r8 = 0
                goto L_0x0082
            L_0x0063:
                java.lang.String r0 = "scale"
                boolean r8 = r8.equals(r0)
                if (r8 == 0) goto L_0x0081
                r8 = 1
                goto L_0x0082
            L_0x006d:
                java.lang.String r0 = "rotate"
                boolean r8 = r8.equals(r0)
                if (r8 == 0) goto L_0x0081
                r8 = 2
                goto L_0x0082
            L_0x0077:
                java.lang.String r0 = "opacity"
                boolean r8 = r8.equals(r0)
                if (r8 == 0) goto L_0x0081
                r8 = 3
                goto L_0x0082
            L_0x0081:
                r8 = -1
            L_0x0082:
                switch(r8) {
                    case 0: goto L_0x00a1;
                    case 1: goto L_0x0094;
                    case 2: goto L_0x008d;
                    case 3: goto L_0x0086;
                    default: goto L_0x0085;
                }
            L_0x0085:
                goto L_0x00ca
            L_0x0086:
                float[] r7 = r6.output
                r7 = r7[r1]
                r6.fromOpacity = r7
                goto L_0x00ca
            L_0x008d:
                float[] r7 = r6.output
                r7 = r7[r1]
                r6.fromRotate = r7
                goto L_0x00ca
            L_0x0094:
                float[] r7 = r6.output
                r7 = r7[r1]
                r6.fromScaleX = r7
                float[] r7 = r6.output
                r7 = r7[r3]
                r6.fromScaleY = r7
                goto L_0x00ca
            L_0x00a1:
                r8 = 0
            L_0x00a2:
                float[] r9 = r6.output
                int r9 = r9.length
                if (r8 >= r9) goto L_0x00be
                float[] r9 = r6.output
                float[] r0 = r6.output
                r0 = r0[r8]
                com.taobao.weex.WXSDKInstance r2 = r7.getInstance()
                int r2 = r2.getInstanceViewPortWidth()
                float r0 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r0, r2)
                r9[r8] = r0
                int r8 = r8 + 1
                goto L_0x00a2
            L_0x00be:
                float[] r7 = r6.output
                r7 = r7[r1]
                r6.fromTranslateX = r7
                float[] r7 = r6.output
                r7 = r7[r3]
                r6.fromTranslateY = r7
            L_0x00ca:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.component.WXParallax.TransformCreator.<init>(com.alibaba.aliweex.adapter.component.WXParallax, java.lang.String, com.alibaba.fastjson.JSONObject):void");
        }

        /* access modifiers changed from: package-private */
        public float[] parseParamArray(JSONArray jSONArray) {
            int size = jSONArray.size();
            float[] fArr = new float[size];
            for (int i = 0; i < size; i++) {
                fArr[i] = jSONArray.getFloatValue(i);
            }
            return fArr;
        }

        /* access modifiers changed from: package-private */
        public void animate(float f, float f2) {
            float access$100 = WXParallax.this.mOffsetY;
            if (access$100 > this.input[1]) {
                access$100 = this.input[1];
            }
            if (access$100 < this.input[0]) {
                access$100 = this.input[0];
            }
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("WXParallax", "type:" + this.transformType + " XDelta:" + f + " YDelta:" + f2);
            }
            String str = this.transformType;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -1267206133) {
                if (hashCode != -925180581) {
                    if (hashCode != 109250890) {
                        if (hashCode == 1052832078 && str.equals("translate")) {
                            c = 0;
                        }
                    } else if (str.equals("scale")) {
                        c = 1;
                    }
                } else if (str.equals("rotate")) {
                    c = 2;
                }
            } else if (str.equals("opacity")) {
                c = 3;
            }
            switch (c) {
                case 0:
                    float f3 = this.output[0] + (((this.output[2] - this.output[0]) * (access$100 - this.input[0])) / (this.input[1] - this.input[0]));
                    float f4 = this.output[1] + (((this.output[3] - this.output[1]) * (access$100 - this.input[0])) / (this.input[1] - this.input[0]));
                    if (this.fromTranslateX != f3 || this.fromTranslateY != f4) {
                        ((WXFrameLayout) WXParallax.this.getHostView()).setTranslationX(f3);
                        ((WXFrameLayout) WXParallax.this.getHostView()).setTranslationY(f4);
                        if (WXEnvironment.isApkDebugable()) {
                            WXLogUtils.d("WXParallax", "XDelta:" + f + " YDelta:" + f2);
                            WXLogUtils.d("WXParallax", " fromTranslateX:" + this.fromTranslateX + " toTranslateX:" + f3 + " fromTranslateY:" + this.fromTranslateY + " toTranslateY:" + f4);
                        }
                        this.fromTranslateX = f3;
                        this.fromTranslateY = f4;
                        return;
                    }
                    return;
                case 1:
                    float f5 = this.output[0] + (((this.output[2] - this.output[0]) * (access$100 - this.input[0])) / (this.input[1] - this.input[0]));
                    float f6 = this.output[1] + (((this.output[3] - this.output[1]) * (access$100 - this.input[0])) / (this.input[1] - this.input[0]));
                    if (this.fromScaleX != f5 || this.fromScaleY != f6) {
                        ((WXFrameLayout) WXParallax.this.getHostView()).setScaleX(f5);
                        ((WXFrameLayout) WXParallax.this.getHostView()).setScaleY(f6);
                        if (WXEnvironment.isApkDebugable()) {
                            WXLogUtils.d("WXParallax", " fromScaleX:" + this.fromScaleX + " toScaleX:" + f5 + " fromScaleY:" + this.fromScaleY + " toScaleY:" + f6);
                        }
                        this.fromScaleX = f5;
                        this.fromScaleY = f6;
                        return;
                    }
                    return;
                case 2:
                    float f7 = this.output[0] + (((this.output[1] - this.output[0]) * (access$100 - this.input[0])) / (this.input[1] - this.input[0]));
                    if (this.fromRotate != f7) {
                        ((WXFrameLayout) WXParallax.this.getHostView()).setRotation(f7);
                        this.fromRotate = f7;
                        return;
                    }
                    return;
                case 3:
                    float f8 = this.output[0] + (((this.output[1] - this.output[0]) * (access$100 - this.input[0])) / (this.input[1] - this.input[0]));
                    if (this.fromOpacity != f8) {
                        WXParallax.this.setOpacity(f8);
                        if (WXEnvironment.isApkDebugable()) {
                            WXLogUtils.d("WXParallax", "opacity fromOpacity:" + this.fromOpacity + " toOpacity:" + f8);
                        }
                        this.fromOpacity = f8;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
