package com.alibaba.aliweex.adapter.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.aliweex.R;
import java.util.ArrayList;
import java.util.List;

public class ElevatorAdapter extends BaseAdapter {
    private Context context;
    private List<ElevatorItem> itemList = new ArrayList();
    private String normalColor;
    private int resourceId;
    private String selectedColor;

    public long getItemId(int i) {
        return (long) i;
    }

    public ElevatorAdapter(Context context2, int i, List<ElevatorItem> list) {
        this.context = context2;
        this.resourceId = i;
        this.itemList = list;
        this.selectedColor = "#EE0A3B";
        this.normalColor = "#333333";
    }

    public int getCount() {
        return this.itemList.size();
    }

    public ElevatorItem getItem(int i) {
        return this.itemList.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        ElevatorItem item = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(this.resourceId, (ViewGroup) null);
            view.setLayoutParams(new AbsListView.LayoutParams(-2, -1));
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.loc_text);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.loc_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(item.getName());
        if (item.getIsHighLight()) {
            viewHolder.textView.setTextColor(Color.parseColor(this.selectedColor));
        } else {
            viewHolder.textView.setTextColor(Color.parseColor(this.normalColor));
        }
        if (item.getIsImgShow()) {
            viewHolder.imageView.setVisibility(0);
        } else {
            viewHolder.imageView.setVisibility(4);
        }
        return view;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder() {
        }
    }

    public void setSelectedColor(String str) {
        this.selectedColor = str;
        notifyDataSetChanged();
    }

    public void setNormalColor(String str) {
        this.normalColor = str;
        notifyDataSetChanged();
    }
}
