package com.taobao.weex.ui.action;

import android.widget.ScrollView;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;
import java.util.Set;

public class GraphicActionCreateBody extends GraphicActionAbstractAddElement {
    private WXComponent component;

    public GraphicActionCreateBody(@NonNull WXSDKInstance wXSDKInstance, String str, String str2, Map<String, String> map, Map<String, String> map2, Set<String> set, float[] fArr, float[] fArr2, float[] fArr3) {
        super(wXSDKInstance, str);
        this.mComponentType = str2;
        this.mStyle = map;
        this.mAttributes = map2;
        this.mEvents = set;
        this.mMargins = fArr;
        this.mPaddings = fArr2;
        this.mBorders = fArr3;
        if (wXSDKInstance.getContext() != null) {
            this.component = createComponent(wXSDKInstance, (WXVContainer) null, new BasicComponentData(getRef(), this.mComponentType, (String) null));
            if (this.component != null) {
                this.component.setTransition(WXTransition.fromMap(this.component.getStyles(), this.component));
            }
        }
    }

    public void executeAction() {
        super.executeAction();
        try {
            this.component.createView();
            this.component.applyLayoutAndEvent(this.component);
            this.component.bindData(this.component);
            WXSDKInstance wXSDKIntance = getWXSDKIntance();
            if (this.component instanceof WXScroller) {
                WXScroller wXScroller = (WXScroller) this.component;
                if (wXScroller.getInnerView() instanceof ScrollView) {
                    wXSDKIntance.setRootScrollView((ScrollView) wXScroller.getInnerView());
                }
            }
            wXSDKIntance.onRootCreated(this.component);
            if (wXSDKIntance.getRenderStrategy() != WXRenderStrategy.APPEND_ONCE) {
                wXSDKIntance.onCreateFinish();
            }
        } catch (Exception e) {
            WXLogUtils.e("create body failed.", (Throwable) e);
        }
    }
}
