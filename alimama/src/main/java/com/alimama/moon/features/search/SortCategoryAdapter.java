package com.alimama.moon.features.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortCategoryAdapter extends RecyclerView.Adapter<SortCategoryViewHolder> {
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) SortCategoryAdapter.class);
    /* access modifiers changed from: private */
    public String mSelectedCondition;
    /* access modifiers changed from: private */
    public SortByClickListener mSortByClickListener;
    private final List<SearchOptionModel> mSortCategories = new ArrayList();
    private final View.OnClickListener mSortCategoryClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (view.getTag() instanceof SearchOptionModel) {
                SearchOptionModel searchOptionModel = (SearchOptionModel) view.getTag();
                SortCategoryAdapter.logger.debug("{} clicked ", (Object) searchOptionModel.getName());
                String unused = SortCategoryAdapter.this.mSelectedCondition = searchOptionModel.getCondition();
                SortCategoryAdapter.this.notifyDataSetChanged();
                if (SortCategoryAdapter.this.mSortByClickListener != null) {
                    SortCategoryAdapter.this.mSortByClickListener.onSortCategoryClicked(searchOptionModel);
                }
            }
        }
    };

    public interface SortByClickListener {
        void onSortCategoryClicked(SearchOptionModel searchOptionModel);
    }

    public static class SortCategoryViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView categoryText;
        /* access modifiers changed from: private */
        public ImageView checkmarkImageView;

        public SortCategoryViewHolder(View view) {
            super(view);
            this.categoryText = (TextView) view.findViewById(R.id.tv_sort_by);
            this.checkmarkImageView = (ImageView) view.findViewById(R.id.iv_checkmark);
        }
    }

    @NonNull
    public SortCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_sort_category, viewGroup, false);
        inflate.setOnClickListener(this.mSortCategoryClickListener);
        return new SortCategoryViewHolder(inflate);
    }

    public void onBindViewHolder(@NonNull SortCategoryViewHolder sortCategoryViewHolder, int i) {
        if (i >= 0 && i < this.mSortCategories.size()) {
            Context context = sortCategoryViewHolder.itemView.getContext();
            SearchOptionModel searchOptionModel = this.mSortCategories.get(i);
            sortCategoryViewHolder.itemView.setTag(searchOptionModel);
            if (TextUtils.equals(searchOptionModel.getCondition(), this.mSelectedCondition)) {
                sortCategoryViewHolder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.common_red_color));
                sortCategoryViewHolder.checkmarkImageView.setVisibility(0);
            } else {
                sortCategoryViewHolder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.search_sort_category_default_text_color));
                sortCategoryViewHolder.checkmarkImageView.setVisibility(4);
            }
            sortCategoryViewHolder.itemView.setTag(searchOptionModel);
            sortCategoryViewHolder.categoryText.setText(searchOptionModel.getName());
        }
    }

    public int getItemCount() {
        return this.mSortCategories.size();
    }

    public void setSortCategoryClickListener(SortByClickListener sortByClickListener) {
        this.mSortByClickListener = sortByClickListener;
    }

    public void setSortCategories(List<SearchOptionModel> list) {
        this.mSortCategories.clear();
        this.mSortCategories.addAll(list);
        if (!this.mSortCategories.isEmpty()) {
            this.mSelectedCondition = this.mSortCategories.get(0).getCondition();
        }
        notifyDataSetChanged();
    }
}
