package com.alimamaunion.common.listpage;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {
    public CommonBaseViewHolder mBaseViewHolder;

    public CommonRecyclerViewHolder(View view, CommonBaseViewHolder commonBaseViewHolder) {
        super(view);
        this.mBaseViewHolder = commonBaseViewHolder;
    }
}
