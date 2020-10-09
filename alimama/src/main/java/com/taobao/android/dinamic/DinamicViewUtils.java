package com.taobao.android.dinamic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.weex.el.parse.Operators;
import java.util.Map;

public class DinamicViewUtils {
    public static DinamicProperty getViewProperty(View view) {
        if (view == null) {
            return new DinamicProperty();
        }
        Object tag = view.getTag(DinamicTagKey.PROPERTY_KEY);
        return tag == null ? new DinamicProperty() : (DinamicProperty) tag;
    }

    public static Map<String, String> getViewDinamicProperty(View view) {
        return getViewProperty(view).dinamicProperty;
    }

    public static Pair<String, String> getEventInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int indexOf = str.indexOf(Operators.BRACKET_START_STR);
        int indexOf2 = str.indexOf(Operators.BRACKET_END_STR);
        if (indexOf < 0 || indexOf2 < 0) {
            if (Dinamic.isDebugable()) {
                DinamicLog.i(Dinamic.TAG, String.format("事件属性:%s语法出错,没有包含\"（）\"", new Object[]{str}));
            }
            return null;
        }
        String trim = str.trim();
        String substring = trim.substring(0, indexOf);
        String substring2 = trim.substring(indexOf + 1, indexOf2);
        if (!TextUtils.isEmpty(substring)) {
            return new Pair<>(substring, substring2);
        }
        return null;
    }

    public static void clipCorner(View view, Canvas canvas) {
        float[] fArr;
        if (canvas != null && canvas.getWidth() > 0 && Build.VERSION.SDK_INT >= 16 && (fArr = (float[]) view.getTag(DinamicTagKey.LAYOUT_RADII)) != null) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            if (Build.VERSION.SDK_INT >= 11 && canvas.isHardwareAccelerated() && Build.VERSION.SDK_INT < 18) {
                view.setLayerType(1, (Paint) null);
            }
            Path path = new Path();
            path.addRoundRect(new RectF(0.0f, 0.0f, (float) width, (float) height), fArr, Path.Direction.CW);
            canvas.clipPath(path);
        }
    }
}
