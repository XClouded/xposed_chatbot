package com.alimama.union.app.toolCenter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.toolCenter.data.ToolItemBean;
import com.alimama.union.app.toolCenter.view.ToolFragment;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.ArrayList;
import java.util.List;

public class ToolChildItemAdapter extends BaseAdapter {
    private static final String TOOL_ACTION_NAME = "click_%s_tools_%d";
    /* access modifiers changed from: private */
    public List<ToolItemBean.ChildItem> mChildItems = new ArrayList();
    private Context mContext;
    private String mGroupName;

    public static class CellViewHolder {
        public EtaoDraweeView draweeView;
        public TextView textView;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public ToolChildItemAdapter(Context context, @NonNull List<ToolItemBean.ChildItem> list, String str) {
        this.mContext = context;
        this.mChildItems.addAll(list);
        this.mGroupName = str;
    }

    public int getCount() {
        return this.mChildItems.size();
    }

    public Object getItem(int i) {
        return this.mChildItems.get(i);
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        CellViewHolder cellViewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.tool_child_item_layout, (ViewGroup) null);
            cellViewHolder = new CellViewHolder();
            cellViewHolder.textView = (TextView) view.findViewById(R.id.tool_child_item_text);
            cellViewHolder.draweeView = (EtaoDraweeView) view.findViewById(R.id.tool_child_item_image);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String str = ((ToolItemBean.ChildItem) ToolChildItemAdapter.this.mChildItems.get(((Integer) ((ViewGroup) view).getChildAt(0).getTag()).intValue())).url;
                    if (!TextUtils.isEmpty(str)) {
                        MoonComponentManager.getInstance().getPageRouter().gotoPage(str);
                    }
                    ToolChildItemAdapter.this.doUTAnalytics(i);
                }
            });
            view.setTag(cellViewHolder);
        } else {
            cellViewHolder = (CellViewHolder) view.getTag();
        }
        ToolItemBean.ChildItem childItem = this.mChildItems.get(i);
        if (childItem.title != null) {
            cellViewHolder.textView.setText(childItem.title);
        } else {
            cellViewHolder.textView.setText("");
        }
        cellViewHolder.draweeView.setAnyImageUrl(childItem.img);
        cellViewHolder.draweeView.setTag(Integer.valueOf(i));
        return view;
    }

    /* access modifiers changed from: private */
    public void doUTAnalytics(int i) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(ToolFragment.PAGE_NAME, String.format(TOOL_ACTION_NAME, new Object[]{this.mGroupName, Integer.valueOf(i)})).build());
    }
}
