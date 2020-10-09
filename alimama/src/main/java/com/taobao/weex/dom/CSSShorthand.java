package com.taobao.weex.dom;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import java.lang.Enum;
import java.util.Arrays;

public class CSSShorthand<T extends Enum<? extends CSSProperty>> implements Cloneable {
    private float[] values;

    public enum CORNER implements CSSProperty {
        BORDER_TOP_LEFT,
        BORDER_TOP_RIGHT,
        BORDER_BOTTOM_RIGHT,
        BORDER_BOTTOM_LEFT,
        ALL
    }

    interface CSSProperty {
    }

    public enum EDGE implements CSSProperty {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        ALL
    }

    public enum TYPE {
        MARGIN,
        PADDING,
        BORDER
    }

    public CSSShorthand(float[] fArr) {
        replace(fArr);
    }

    public CSSShorthand() {
        this(false);
    }

    CSSShorthand(boolean z) {
        this.values = new float[Math.max(EDGE.values().length, CORNER.values().length)];
        if (z) {
            Arrays.fill(this.values, Float.NaN);
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void set(@NonNull EDGE edge, float f) {
        setInternal(edge, f);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void set(@NonNull CORNER corner, float f) {
        setInternal(corner, f);
    }

    public float get(@NonNull EDGE edge) {
        return getInternal(edge);
    }

    public float get(@NonNull CORNER corner) {
        return getInternal(corner);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public final void replace(float[] fArr) {
        this.values = fArr;
    }

    public CSSShorthand clone() throws CloneNotSupportedException {
        return (CSSShorthand) super.clone();
    }

    private void setInternal(@NonNull Enum<? extends CSSProperty> enumR, float f) {
        if (enumR == EDGE.ALL || enumR == CORNER.ALL) {
            Arrays.fill(this.values, f);
        } else {
            this.values[enumR.ordinal()] = f;
        }
    }

    private float getInternal(@NonNull Enum<? extends CSSProperty> enumR) {
        if (enumR == EDGE.ALL || enumR == CORNER.ALL) {
            return 0.0f;
        }
        return this.values[enumR.ordinal()];
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public String toString() {
        return TextUtils.isEmpty(this.values.toString()) ? "" : Arrays.toString(this.values);
    }
}
