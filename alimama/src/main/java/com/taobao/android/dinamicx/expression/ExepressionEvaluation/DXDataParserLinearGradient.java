package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXDataParserLinearGradient extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_LINEAR_GRADIENT = 5808997026297879479L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        GradientDrawable.Orientation orientation;
        if (objArr == null || objArr.length < 3 || !(objArr[0] instanceof String)) {
            return null;
        }
        String str = objArr[0];
        int[] iArr = new int[(objArr.length - 1)];
        int length = objArr.length;
        for (int i = 1; i < length; i++) {
            try {
                iArr[i - 1] = Color.parseColor(String.valueOf(objArr[i]));
            } catch (Exception unused) {
                String obj = objArr[i] != null ? objArr[i].toString() : null;
                iArr[i - 1] = -12303292;
                Log.e("ParserLinearGradient", obj + "parse Color failed. color miss");
            }
        }
        if ("toLeft".equals(str)) {
            orientation = GradientDrawable.Orientation.RIGHT_LEFT;
        } else if ("toRight".equals(str)) {
            orientation = GradientDrawable.Orientation.LEFT_RIGHT;
        } else if ("toTop".equals(str)) {
            orientation = GradientDrawable.Orientation.BOTTOM_TOP;
        } else if ("toBottom".equals(str)) {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        } else if ("toTopLeft".equals(str)) {
            orientation = GradientDrawable.Orientation.BR_TL;
        } else if ("toTopRight".equals(str)) {
            orientation = GradientDrawable.Orientation.BL_TR;
        } else if ("toBottomLeft".equals(str)) {
            orientation = GradientDrawable.Orientation.TR_BL;
        } else if ("toBottomRight".equals(str)) {
            orientation = GradientDrawable.Orientation.TL_BR;
        } else {
            orientation = GradientDrawable.Orientation.LEFT_RIGHT;
        }
        DXWidgetNode.GradientInfo gradientInfo = new DXWidgetNode.GradientInfo();
        gradientInfo.setGradientType(0);
        gradientInfo.setLinearGradientDirection(orientation);
        gradientInfo.setLinearGradientColors(iArr);
        return gradientInfo;
    }
}
