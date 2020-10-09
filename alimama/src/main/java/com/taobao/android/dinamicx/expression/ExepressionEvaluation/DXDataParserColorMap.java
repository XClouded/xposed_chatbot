package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import android.graphics.Color;
import android.util.Log;
import com.taobao.android.dinamicx.DXDarkModeCenter;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;
import java.util.HashMap;

public class DXDataParserColorMap extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_COLORMAP = 1756245084560162885L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length < 2 || objArr.length % 2 == 1 || !DXDarkModeCenter.isSupportDarkMode()) {
            return null;
        }
        HashMap hashMap = new HashMap(5);
        int i = 0;
        while (i < objArr.length / 2) {
            int i2 = i * 2;
            if (objArr[i2] instanceof String) {
                int i3 = i2 + 1;
                if (objArr[i3] instanceof String) {
                    hashMap.put(objArr[i2], Integer.valueOf(parseColor(objArr[i3])));
                    i++;
                }
            }
            return null;
        }
        if (hashMap.size() > 0) {
            return hashMap;
        }
        return null;
    }

    private int parseColor(String str) {
        try {
            return Color.parseColor(str);
        } catch (Exception unused) {
            Log.e("ParserLinearGradient", str + "parse Color failed. color miss");
            return -12303292;
        }
    }
}
