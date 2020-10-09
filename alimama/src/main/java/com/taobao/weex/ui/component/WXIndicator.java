package com.taobao.weex.ui.component;

import android.content.Context;
import android.text.TextUtils;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXCircleIndicator;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXViewUtils;

@Component(lazyload = false)
public class WXIndicator extends WXComponent<WXCircleIndicator> {
    @Deprecated
    public WXIndicator(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXIndicator(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(WXCircleIndicator wXCircleIndicator, int i, int i2, int i3, int i4, int i5, int i6) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, i2);
        setMarginsSupportRTL(layoutParams, i3, i5, i4, i6);
        wXCircleIndicator.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public WXCircleIndicator initComponentHostView(@NonNull Context context) {
        WXCircleIndicator wXCircleIndicator = new WXCircleIndicator(context);
        if (getParent() instanceof WXSlider) {
            return wXCircleIndicator;
        }
        if (!WXEnvironment.isApkDebugable()) {
            return null;
        }
        throw new WXRuntimeException("WXIndicator initView error.");
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(WXCircleIndicator wXCircleIndicator) {
        super.onHostViewInitialized(wXCircleIndicator);
        if (getParent() instanceof WXSlider) {
            ((WXSlider) getParent()).addIndicator(this);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 1177488820(0x462f0db4, float:11203.426)
            r2 = 1
            if (r0 == r1) goto L_0x0029
            r1 = 1873297717(0x6fa84135, float:1.0414462E29)
            if (r0 == r1) goto L_0x001f
            r1 = 2127804432(0x7ed3b810, float:1.4071141E38)
            if (r0 == r1) goto L_0x0015
            goto L_0x0033
        L_0x0015:
            java.lang.String r0 = "itemColor"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 0
            goto L_0x0034
        L_0x001f:
            java.lang.String r0 = "itemSelectedColor"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 1
            goto L_0x0034
        L_0x0029:
            java.lang.String r0 = "itemSize"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 2
            goto L_0x0034
        L_0x0033:
            r0 = -1
        L_0x0034:
            r1 = 0
            switch(r0) {
                case 0: goto L_0x0055;
                case 1: goto L_0x004b;
                case 2: goto L_0x003d;
                default: goto L_0x0038;
            }
        L_0x0038:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x003d:
            java.lang.Integer r4 = com.taobao.weex.utils.WXUtils.getInteger(r5, r1)
            if (r4 == 0) goto L_0x004a
            int r4 = r4.intValue()
            r3.setItemSize(r4)
        L_0x004a:
            return r2
        L_0x004b:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r1)
            if (r4 == 0) goto L_0x0054
            r3.setItemSelectedColor(r4)
        L_0x0054:
            return r2
        L_0x0055:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r1)
            if (r4 == 0) goto L_0x005e
            r3.setItemColor(r4)
        L_0x005e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXIndicator.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "itemColor")
    public void setItemColor(String str) {
        int color;
        if (!TextUtils.isEmpty(str) && (color = WXResourceUtils.getColor(str)) != Integer.MIN_VALUE) {
            ((WXCircleIndicator) getHostView()).setPageColor(color);
            ((WXCircleIndicator) getHostView()).forceLayout();
            ((WXCircleIndicator) getHostView()).requestLayout();
        }
    }

    @WXComponentProp(name = "itemSelectedColor")
    public void setItemSelectedColor(String str) {
        int color;
        if (!TextUtils.isEmpty(str) && (color = WXResourceUtils.getColor(str)) != Integer.MIN_VALUE) {
            ((WXCircleIndicator) getHostView()).setFillColor(color);
            ((WXCircleIndicator) getHostView()).forceLayout();
            ((WXCircleIndicator) getHostView()).requestLayout();
        }
    }

    @WXComponentProp(name = "itemSize")
    public void setItemSize(int i) {
        if (i >= 0) {
            ((WXCircleIndicator) getHostView()).setRadius(WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidth()) / 2.0f);
            ((WXCircleIndicator) getHostView()).forceLayout();
            ((WXCircleIndicator) getHostView()).requestLayout();
        }
    }

    public void setShowIndicators(boolean z) {
        if (getHostView() != null) {
            if (z) {
                ((WXCircleIndicator) getHostView()).setVisibility(0);
            } else {
                ((WXCircleIndicator) getHostView()).setVisibility(8);
            }
        }
    }
}
