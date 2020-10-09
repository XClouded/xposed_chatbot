package com.taobao.weex.devtools.inspector.elements.android;

import android.view.View;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.inspector.elements.AbstractChainedDescriptor;
import com.taobao.weex.devtools.inspector.elements.AttributeAccumulator;
import com.taobao.weex.devtools.inspector.elements.StyleAccumulator;
import com.taobao.weex.ui.component.WXComponent;
import javax.annotation.Nullable;

public class WXSDKInstanceDescriptor extends AbstractChainedDescriptor<WXSDKInstance> implements HighlightableDescriptor {
    private static final String HEIGHT = "height";
    private static final String ID_NAME = "id";
    private static final String WIDTH = "width";

    /* access modifiers changed from: protected */
    public String onGetNodeName(WXSDKInstance wXSDKInstance) {
        return "instance";
    }

    /* access modifiers changed from: protected */
    public void onGetChildren(WXSDKInstance wXSDKInstance, Accumulator<Object> accumulator) {
        WXComponent rootComponent = wXSDKInstance.getRootComponent();
        if (rootComponent != null) {
            accumulator.store(rootComponent);
        }
    }

    @Nullable
    public View getViewForHighlighting(Object obj) {
        return ((WXSDKInstance) obj).getRootView();
    }

    /* access modifiers changed from: protected */
    public void onGetStyles(WXSDKInstance wXSDKInstance, StyleAccumulator styleAccumulator) {
        styleAccumulator.store("id", wXSDKInstance.getInstanceId(), true);
        styleAccumulator.store("width", String.valueOf(wXSDKInstance.getWeexWidth()), false);
        styleAccumulator.store("height", String.valueOf(wXSDKInstance.getWeexHeight()), false);
    }

    /* access modifiers changed from: protected */
    public void onGetAttributes(WXSDKInstance wXSDKInstance, AttributeAccumulator attributeAccumulator) {
        attributeAccumulator.store("id", wXSDKInstance.getInstanceId());
        attributeAccumulator.store("width", String.valueOf(wXSDKInstance.getWeexWidth()));
        attributeAccumulator.store("height", String.valueOf(wXSDKInstance.getWeexHeight()));
    }
}
