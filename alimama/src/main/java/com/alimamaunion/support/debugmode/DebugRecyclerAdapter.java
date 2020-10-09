package com.alimamaunion.support.debugmode;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DebugRecyclerAdapter extends RecyclerView.Adapter<DebugItemViewHolder> {
    public int getItemCount() {
        return 0;
    }

    public void onBindViewHolder(DebugItemViewHolder debugItemViewHolder, int i) {
    }

    public void setData(List<DebugItemData> list) {
    }

    public DebugItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DebugItemViewHolder(new View(viewGroup.getContext()));
    }
}
