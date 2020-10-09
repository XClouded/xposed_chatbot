package com.taobao.weex.ui.view.border;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import com.taobao.weex.dom.CSSShorthand;

enum BorderStyle {
    SOLID,
    DASHED,
    DOTTED;

    /* access modifiers changed from: package-private */
    @Nullable
    public Shader getLineShader(float f, int i, CSSShorthand.EDGE edge) {
        CSSShorthand.EDGE edge2 = edge;
        switch (this) {
            case DOTTED:
                if (edge2 == CSSShorthand.EDGE.LEFT || edge2 == CSSShorthand.EDGE.RIGHT) {
                    return new LinearGradient(0.0f, 0.0f, 0.0f, f * 2.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
                } else if (edge2 == CSSShorthand.EDGE.TOP || edge2 == CSSShorthand.EDGE.BOTTOM) {
                    return new LinearGradient(0.0f, 0.0f, f * 2.0f, 0.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
                }
                break;
            case DASHED:
                break;
            default:
                return null;
        }
        if (edge2 == CSSShorthand.EDGE.LEFT || edge2 == CSSShorthand.EDGE.RIGHT) {
            return new LinearGradient(0.0f, 0.0f, 0.0f, f * 6.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
        } else if (edge2 != CSSShorthand.EDGE.TOP && edge2 != CSSShorthand.EDGE.BOTTOM) {
            return null;
        } else {
            return new LinearGradient(0.0f, 0.0f, f * 6.0f, 0.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
        }
    }
}
