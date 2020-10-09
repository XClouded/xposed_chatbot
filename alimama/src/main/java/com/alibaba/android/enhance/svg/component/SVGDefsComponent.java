package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.DefinitionSVGComponent;
import com.alibaba.android.enhance.svg.ISVGVirtualNode;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGDefsComponent extends DefinitionSVGComponent {
    public void draw(Canvas canvas, Paint paint, float f) {
    }

    public SVGDefsComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void saveDefinition() {
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                if (iSVGVirtualNode instanceof AbstractSVGVirtualComponent) {
                    ((AbstractSVGVirtualComponent) iSVGVirtualNode).saveDefinition();
                }
            }
        });
    }
}
