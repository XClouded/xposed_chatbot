package com.alibaba.android.enhance.svg.component.gradient;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.alibaba.android.enhance.svg.DefinitionSVGComponent;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXResourceUtils;

public class SVGStopComponent extends DefinitionSVGComponent {
    private int mStopColor = -16777216;
    private float mStopOffset;
    private float mStopOpacity = 1.0f;

    public SVGStopComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "stopColor")
    public void setStopColor(String str) {
        this.mStopColor = WXResourceUtils.getColor(str, -16777216);
    }

    @WXComponentProp(name = "stopOpacity")
    public void setStopOpacity(String str) {
        this.mStopOpacity = Math.max(0.0f, Math.min(PropHelper.resolveFloatFromString(str), 1.0f));
        markUpdated();
    }

    @WXComponentProp(name = "offset")
    public void setStopOffset(String str) {
        this.mStopOffset = Math.max(0.0f, Math.min(PropHelper.resolveFloatFromString(str), 1.0f));
        markUpdated();
    }

    @NonNull
    public StopInfo getStopInfo() {
        return new StopInfo(this.mStopColor, this.mStopOpacity, this.mStopOffset);
    }

    static class StopInfo {
        public int argbColor;
        public float offset;

        StopInfo(@ColorInt int i, float f, float f2) {
            this.argbColor = Color.argb((int) (((f * ((float) Color.alpha(i))) / 255.0f) * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
            this.offset = f2;
        }
    }
}
