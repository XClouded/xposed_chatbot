package com.alimama.moon.features.search;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;

public class SearchServiceOptionView extends AppCompatTextView {
    private SearchOptionModel mSearchOption;

    public SearchServiceOptionView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SearchServiceOptionView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchServiceOptionView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        setBackground(ContextCompat.getDrawable(context, R.drawable.bg_service_option));
        setSelected(false);
        setGravity(17);
    }

    public void setSelected(boolean z) {
        super.setSelected(z);
        if (z) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            setTextColor(ContextCompat.getColor(getContext(), R.color.scene_content_title));
        }
    }

    public void setSearchOption(SearchOptionModel searchOptionModel) {
        this.mSearchOption = searchOptionModel;
        setText(this.mSearchOption.getName());
    }

    public SearchOptionModel getSearchOption() {
        return this.mSearchOption;
    }
}
