package com.alibaba.android.enhance.svg;

import androidx.annotation.Nullable;
import com.alibaba.android.WeexEnhance;
import com.alibaba.android.enhance.svg.component.SVGCircleComponent;
import com.alibaba.android.enhance.svg.component.SVGClipPathComponent;
import com.alibaba.android.enhance.svg.component.SVGDefsComponent;
import com.alibaba.android.enhance.svg.component.SVGEllipseComponent;
import com.alibaba.android.enhance.svg.component.SVGGroupComponent;
import com.alibaba.android.enhance.svg.component.SVGImageComponent;
import com.alibaba.android.enhance.svg.component.SVGLineComponent;
import com.alibaba.android.enhance.svg.component.SVGPathComponent;
import com.alibaba.android.enhance.svg.component.SVGPolygonComponent;
import com.alibaba.android.enhance.svg.component.SVGPolylineComponent;
import com.alibaba.android.enhance.svg.component.SVGRectComponent;
import com.alibaba.android.enhance.svg.component.SVGSymbolComponent;
import com.alibaba.android.enhance.svg.component.SVGUseComponent;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.component.gradient.SVGLinearGradientComponent;
import com.alibaba.android.enhance.svg.component.gradient.SVGRadialGradientComponent;
import com.alibaba.android.enhance.svg.component.gradient.SVGStopComponent;
import com.alibaba.android.enhance.svg.component.mask.SVGMaskComponent;
import com.alibaba.android.enhance.svg.component.pattern.SVGPatternComponent;
import com.alibaba.android.enhance.svg.component.text.SVGTSpanComponent;
import com.alibaba.android.enhance.svg.component.text.SVGTextComponent;
import com.alibaba.android.enhance.svg.component.text.SVGTextPathComponent;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;

public class SVGPlugin {
    public static final String TAG = "svg";

    public static class GlobalVariablesHolder {
        public static float DEVICE_SCALE = 1.0f;
    }

    public static void registerSVG() throws WXException {
        WXSDKEngine.registerComponent(TAG, (Class<? extends WXComponent>) SVGViewComponent.class);
        WXSDKEngine.registerComponent("circle", (Class<? extends WXComponent>) SVGCircleComponent.class);
        WXSDKEngine.registerComponent("rect", (Class<? extends WXComponent>) SVGRectComponent.class);
        WXSDKEngine.registerComponent("line", (Class<? extends WXComponent>) SVGLineComponent.class);
        WXSDKEngine.registerComponent("polyline", (Class<? extends WXComponent>) SVGPolylineComponent.class);
        WXSDKEngine.registerComponent("polygon", (Class<? extends WXComponent>) SVGPolygonComponent.class);
        WXSDKEngine.registerComponent("ellipse", (Class<? extends WXComponent>) SVGEllipseComponent.class);
        WXSDKEngine.registerComponent("path", (Class<? extends WXComponent>) SVGPathComponent.class);
        WXSDKEngine.registerComponent("g", (Class<? extends WXComponent>) SVGGroupComponent.class);
        WXSDKEngine.registerComponent("clipPath", (Class<? extends WXComponent>) SVGClipPathComponent.class);
        WXSDKEngine.registerComponent("defs", (Class<? extends WXComponent>) SVGDefsComponent.class);
        WXSDKEngine.registerComponent("symbol", (Class<? extends WXComponent>) SVGSymbolComponent.class);
        WXSDKEngine.registerComponent("use", (Class<? extends WXComponent>) SVGUseComponent.class);
        WXSDKEngine.registerComponent("stop", (Class<? extends WXComponent>) SVGStopComponent.class);
        WXSDKEngine.registerComponent("linearGradient", (Class<? extends WXComponent>) SVGLinearGradientComponent.class);
        WXSDKEngine.registerComponent("radialGradient", (Class<? extends WXComponent>) SVGRadialGradientComponent.class);
        WXSDKEngine.registerComponent("pattern", (Class<? extends WXComponent>) SVGPatternComponent.class);
        WXSDKEngine.registerComponent("svgMask", (Class<? extends WXComponent>) SVGMaskComponent.class);
        WXSDKEngine.registerComponent("svgImage", (Class<? extends WXComponent>) SVGImageComponent.class);
        WXSDKEngine.registerComponent("svgText", (Class<? extends WXComponent>) SVGTextComponent.class);
        WXSDKEngine.registerComponent("svgTSpan", (Class<? extends WXComponent>) SVGTSpanComponent.class);
        WXSDKEngine.registerComponent("svgTextPath", (Class<? extends WXComponent>) SVGTextPathComponent.class);
        WXSDKEngine.registerModule("svg-helper", SVGHelperModule.class);
    }

    @Nullable
    public static WeexEnhance.ImageLoadAdapter getImageAdapter() {
        return WeexEnhance.getImageAdapter();
    }
}
