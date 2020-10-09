package com.alimama.moon.view.design;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class LineSpacingTextView extends AppCompatTextView {
    public LineSpacingTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LineSpacingTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LineSpacingTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        setLineSpacing((getTextSize() * 1.5f) - ((float) getLineHeight()), 1.0f);
    }
}
