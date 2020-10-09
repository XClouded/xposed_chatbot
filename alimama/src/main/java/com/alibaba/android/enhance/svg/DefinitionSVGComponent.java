package com.alibaba.android.enhance.svg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;

public class DefinitionSVGComponent extends AbstractSVGVirtualComponent {
    public void draw(Canvas canvas, Paint paint, float f) {
    }

    public void draw(Canvas canvas, Paint paint, float f, @Nullable RectF rectF) {
    }

    public Path getPath(Canvas canvas, Paint paint) {
        return null;
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        return null;
    }

    public DefinitionSVGComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }
}
