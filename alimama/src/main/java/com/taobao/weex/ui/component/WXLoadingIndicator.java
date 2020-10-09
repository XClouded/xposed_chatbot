package com.taobao.weex.ui.component;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.refresh.circlebar.CircleProgressBar;
import com.taobao.weex.utils.WXResourceUtils;

@Component(lazyload = false)
public class WXLoadingIndicator extends WXComponent<CircleProgressBar> {
    public WXLoadingIndicator(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public CircleProgressBar initComponentHostView(@NonNull Context context) {
        return new CircleProgressBar(context);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 94842723(0x5a72f63, float:1.5722012E-35)
            r2 = 1
            if (r0 == r1) goto L_0x001a
            r1 = 1118509918(0x42ab1b5e, float:85.55345)
            if (r0 == r1) goto L_0x0010
            goto L_0x0024
        L_0x0010:
            java.lang.String r0 = "animating"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0024
            r0 = 1
            goto L_0x0025
        L_0x001a:
            java.lang.String r0 = "color"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0024
            r0 = 0
            goto L_0x0025
        L_0x0024:
            r0 = -1
        L_0x0025:
            r1 = 0
            switch(r0) {
                case 0: goto L_0x003c;
                case 1: goto L_0x002e;
                default: goto L_0x0029;
            }
        L_0x0029:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x002e:
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r1)
            if (r4 == 0) goto L_0x003b
            boolean r4 = r4.booleanValue()
            r3.setAnimating(r4)
        L_0x003b:
            return r2
        L_0x003c:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r1)
            if (r4 == 0) goto L_0x0045
            r3.setColor(r4)
        L_0x0045:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXLoadingIndicator.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "color")
    public void setColor(String str) {
        if (str != null && !str.equals("")) {
            ((CircleProgressBar) getHostView()).setColorSchemeColors(WXResourceUtils.getColor(str, SupportMenu.CATEGORY_MASK));
        }
    }

    @WXComponentProp(name = "animating")
    public void setAnimating(boolean z) {
        if (z) {
            ((CircleProgressBar) getHostView()).start();
        } else {
            ((CircleProgressBar) getHostView()).stop();
        }
    }
}
