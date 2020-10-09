package com.taobao.uikit.extend.feature.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TIconFontTextView extends TextView {
    private static Typeface sIconfont;

    public TIconFontTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public TIconFontTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TIconFontTextView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setTypeface(getTypeface());
        setIncludeFontPadding(false);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        setTypeface((Typeface) null);
        super.onDetachedFromWindow();
    }

    public Typeface getTypeface() {
        if (sIconfont == null) {
            try {
                sIconfont = Typeface.createFromAsset(getContext().getAssets(), "uik_iconfont.ttf");
            } catch (Exception unused) {
            }
        }
        return sIconfont;
    }
}
