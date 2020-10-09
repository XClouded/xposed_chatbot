package com.alimama.moon.features.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;

public class SearchInputPlaceholderView extends LinearLayout {
    private TextView mSearchInputText;

    public SearchInputPlaceholderView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SearchInputPlaceholderView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchInputPlaceholderView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context, attributeSet);
    }

    private void initViews(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        inflate(context, R.layout.search_input_placeholder, this);
        this.mSearchInputText = (TextView) findViewById(R.id.tv_search_placeholder_hint);
        setBackgroundResource(R.drawable.bg_search_input);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.common_padding_12);
        int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.common_padding_6);
        setPadding(dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset, dimensionPixelOffset2);
        View findViewById = findViewById(R.id.iv_search_indicator);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SearchInputPlaceholderView);
            int i = 0;
            if (!obtainStyledAttributes.getBoolean(0, true)) {
                i = 8;
            }
            findViewById.setVisibility(i);
            obtainStyledAttributes.recycle();
        }
    }

    public void setInputPlaceholder(String str) {
        if (str != null) {
            this.mSearchInputText.setText(str);
        }
    }

    public String getInputPlaceholder() {
        return this.mSearchInputText.getText().toString();
    }
}
