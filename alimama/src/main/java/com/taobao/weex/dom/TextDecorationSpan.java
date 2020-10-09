package com.taobao.weex.dom;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import androidx.annotation.NonNull;
import com.taobao.weex.ui.component.WXTextDecoration;

public class TextDecorationSpan extends CharacterStyle implements UpdateAppearance {
    private final WXTextDecoration mTextDecoration;

    public TextDecorationSpan(@NonNull WXTextDecoration wXTextDecoration) {
        this.mTextDecoration = wXTextDecoration;
    }

    public void updateDrawState(TextPaint textPaint) {
        switch (this.mTextDecoration) {
            case LINETHROUGH:
                textPaint.setUnderlineText(false);
                textPaint.setStrikeThruText(true);
                return;
            case UNDERLINE:
                textPaint.setUnderlineText(true);
                textPaint.setStrikeThruText(false);
                return;
            case NONE:
                textPaint.setUnderlineText(false);
                textPaint.setStrikeThruText(false);
                return;
            default:
                return;
        }
    }
}
