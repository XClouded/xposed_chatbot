package com.taobao.uikit.extend.component.unify.Dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import androidx.core.view.GravityCompat;

public enum GravityEnum {
    START,
    CENTER,
    END;
    
    private static final boolean HAS_RTL = false;

    @SuppressLint({"RtlHandcoded"})
    public int getGravityInt() {
        switch (this) {
            case START:
                if (HAS_RTL) {
                    return GravityCompat.START;
                }
                return 3;
            case CENTER:
                return 1;
            case END:
                if (HAS_RTL) {
                    return GravityCompat.END;
                }
                return 5;
            default:
                throw new IllegalStateException("Invalid gravity constant");
        }
    }

    @TargetApi(17)
    public int getTextAlignment() {
        switch (this) {
            case CENTER:
                return 4;
            case END:
                return 6;
            default:
                return 5;
        }
    }
}
