package com.alimama.union.app.toolCenter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.union.app.toolCenter.data.ToolItemBean;
import java.util.ArrayList;
import java.util.List;

public class ToolFragmentAdapter extends RecyclerView.Adapter<ToolViewHolder> {
    private Context mContext;
    private List<ToolItemBean> mList = new ArrayList();

    public ToolFragmentAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(@NonNull List<ToolItemBean> list) {
        if (list != null && list.size() != 0) {
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public ToolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ToolViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.fragment_tool_item_layout, (ViewGroup) null));
    }

    public void onBindViewHolder(ToolViewHolder toolViewHolder, int i) {
        ToolItemBean toolItemBean = this.mList.get(i);
        if (!TextUtils.isEmpty(toolItemBean.title)) {
            toolViewHolder.textView.setText(toolItemBean.title);
        } else {
            toolViewHolder.textView.setText("");
        }
        toolViewHolder.gridView.setAdapter(new ToolChildItemAdapter(this.mContext, toolItemBean.items, toolItemBean.groupName));
    }

    public int getItemCount() {
        if (this.mList == null) {
            return 0;
        }
        return this.mList.size();
    }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        public GridView gridView;
        public TextView textView;

        public ToolViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.tv_tool_title);
            this.gridView = (GridView) view.findViewById(R.id.tool_grid_view);
        }
    }
}
