package com.alimama.unionwl.uiframe.views.text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ISIconFontTextView extends TextView {
    private static Typeface sIconFont;

    public static void initIconFont(Context context) {
        sIconFont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }

    public ISIconFontTextView(Context context) {
        super(context);
        initView();
    }

    public ISIconFontTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        super.setText(charSequence, bufferType);
    }

    private void initView() {
        setTypeface(sIconFont);
    }
}
