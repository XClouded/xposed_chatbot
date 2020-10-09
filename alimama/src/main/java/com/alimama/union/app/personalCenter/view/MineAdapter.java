package com.alimama.union.app.personalCenter.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.union.app.personalCenter.model.MineItemData;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineAdapter extends BaseAdapter {
    private static final String TYPE_CONTENT = "view_type_content";
    private static final String TYPE_EMPTY = "view_type_empty";
    private static final int VIEW_HOLDER = 2130837788;
    private static final int VIEW_TYPE = 2130837787;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MineAdapter.class);
    private Context context;
    private List<MineItemData> dataList;
    private LayoutInflater inflater;

    public long getItemId(int i) {
        return (long) i;
    }

    public MineAdapter(List<MineItemData> list, Context context2) {
        this.dataList = list;
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
    }

    public List<MineItemData> getDataList() {
        return this.dataList;
    }

    public int getCount() {
        if (this.dataList == null) {
            return 0;
        }
        return this.dataList.size();
    }

    public Object getItem(int i) {
        if (this.dataList == null) {
            return null;
        }
        return this.dataList.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (!this.dataList.get(i).isEmpty()) {
            if (view == null || !TYPE_CONTENT.equals(view.getTag(R.drawable.ic_launcher))) {
                view = this.inflater.inflate(R.layout.item_mine_content, (ViewGroup) null, false);
                view.setTag(R.drawable.ic_launcher, TYPE_CONTENT);
                ViewHolder buildHolder = buildHolder(view);
                view.setTag(R.drawable.ic_logo, buildHolder);
                viewHolder = buildHolder;
            } else {
                viewHolder = (ViewHolder) view.getTag(R.drawable.ic_logo);
            }
            bindViewData(viewHolder, this.dataList.get(i));
            return view;
        } else if (view != null && TYPE_EMPTY.equals(view.getTag(R.drawable.ic_launcher))) {
            return view;
        } else {
            View inflate = this.inflater.inflate(R.layout.item_mine_empty, (ViewGroup) null, false);
            inflate.setTag(R.drawable.ic_launcher, TYPE_EMPTY);
            return inflate;
        }
    }

    private void bindViewData(ViewHolder viewHolder, MineItemData mineItemData) {
        if (mineItemData.isHidden()) {
            viewHolder.topLayout.setVisibility(8);
        } else {
            viewHolder.topLayout.setVisibility(0);
        }
        String iconUrl = mineItemData.getIconUrl();
        if (!TextUtils.isEmpty(iconUrl)) {
            viewHolder.icon.setAnyImageUrl(iconUrl);
        }
        viewHolder.content.setText(mineItemData.getItemName());
    }

    private ViewHolder buildHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.icon = (EtaoDraweeView) view.findViewById(R.id.icon_image);
        viewHolder.content = (TextView) view.findViewById(R.id.item_content_tv);
        viewHolder.dote = (ImageView) view.findViewById(R.id.item_content_dote);
        viewHolder.divideLine = view.findViewById(R.id.item_divide_line);
        viewHolder.topLayout = view.findViewById(R.id.top_layout);
        return viewHolder;
    }

    private static class ViewHolder {
        TextView content;
        View divideLine;
        ImageView dote;
        EtaoDraweeView icon;
        View topLayout;

        private ViewHolder() {
        }
    }
}
