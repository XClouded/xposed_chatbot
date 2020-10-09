package com.taobao.weex.utils;

import androidx.annotation.NonNull;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.component.WXComponent;

public class WXDomUtils {
    public static float getContentWidth(WXComponent wXComponent) {
        float layoutWidth = wXComponent.getLayoutWidth();
        CSSShorthand padding = wXComponent.getPadding();
        CSSShorthand border = wXComponent.getBorder();
        float f = padding.get(CSSShorthand.EDGE.LEFT);
        if (!CSSConstants.isUndefined(f)) {
            layoutWidth -= f;
        }
        float f2 = padding.get(CSSShorthand.EDGE.RIGHT);
        if (!CSSConstants.isUndefined(f2)) {
            layoutWidth -= f2;
        }
        float f3 = border.get(CSSShorthand.EDGE.LEFT);
        if (!CSSConstants.isUndefined(f3)) {
            layoutWidth -= f3;
        }
        float f4 = border.get(CSSShorthand.EDGE.RIGHT);
        return !CSSConstants.isUndefined(f4) ? layoutWidth - f4 : layoutWidth;
    }

    public static float getContentHeight(WXComponent wXComponent) {
        float layoutHeight = wXComponent.getLayoutHeight();
        CSSShorthand padding = wXComponent.getPadding();
        CSSShorthand border = wXComponent.getBorder();
        float f = padding.get(CSSShorthand.EDGE.TOP);
        if (!CSSConstants.isUndefined(f)) {
            layoutHeight -= f;
        }
        float f2 = padding.get(CSSShorthand.EDGE.BOTTOM);
        if (!CSSConstants.isUndefined(f2)) {
            layoutHeight -= f2;
        }
        float f3 = border.get(CSSShorthand.EDGE.TOP);
        if (!CSSConstants.isUndefined(f3)) {
            layoutHeight -= f3;
        }
        float f4 = border.get(CSSShorthand.EDGE.BOTTOM);
        return !CSSConstants.isUndefined(f4) ? layoutHeight - f4 : layoutHeight;
    }

    public static float getContentWidth(@NonNull CSSShorthand cSSShorthand, @NonNull CSSShorthand cSSShorthand2, float f) {
        float f2 = cSSShorthand.get(CSSShorthand.EDGE.LEFT);
        if (!CSSConstants.isUndefined(f2)) {
            f -= f2;
        }
        float f3 = cSSShorthand.get(CSSShorthand.EDGE.RIGHT);
        if (!CSSConstants.isUndefined(f3)) {
            f -= f3;
        }
        float f4 = cSSShorthand2.get(CSSShorthand.EDGE.LEFT);
        if (!CSSConstants.isUndefined(f4)) {
            f -= f4;
        }
        float f5 = cSSShorthand2.get(CSSShorthand.EDGE.RIGHT);
        return !CSSConstants.isUndefined(f5) ? f - f5 : f;
    }
}
