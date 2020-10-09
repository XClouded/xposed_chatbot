package com.alimama.moon.features.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;

public class SortByDropDown extends LinearLayout {
    private ImageView mDropdownImageView;
    private TextView mSortByTextView;
    @Nullable
    private SearchOptionModel mSortOption;

    public SortByDropDown(Context context) {
        this(context, (AttributeSet) null);
    }

    public SortByDropDown(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SortByDropDown(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.merge_sort_by_dropdown, this);
        this.mSortByTextView = (TextView) findViewById(R.id.tv_sort_by);
        this.mDropdownImageView = (ImageView) findViewById(R.id.iv_dropdown);
    }

    public void setSortBy(SearchOptionModel searchOptionModel) {
        this.mSortOption = searchOptionModel;
        if (searchOptionModel != null) {
            this.mSortByTextView.setText(searchOptionModel.getName());
        }
    }

    @Nullable
    public SearchOptionModel getSortOption() {
        return this.mSortOption;
    }

    public void expand() {
        this.mDropdownImageView.animate().rotationBy(180.0f).start();
    }

    public void collapse() {
        this.mDropdownImageView.animate().rotation(0.0f).start();
    }
}
