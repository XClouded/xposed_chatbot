package com.ali.user.mobile.register.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.widget.AUPinnedHeaderListView;
import java.util.List;

public class RegionAdapter extends BaseAdapter implements AUPinnedHeaderListView.PinnedHeaderAdapter {
    private List<RegionInfo> list = null;
    private Context mContext;
    private RegionInfo mCurrentRegion;

    public void configurePinnedHeader(View view, int i, int i2) {
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getPinnedHeaderState(int i) {
        return 0;
    }

    public RegionAdapter(Context context, List<RegionInfo> list2) {
        this.list = list2;
        this.mContext = context;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int i) {
        return this.list.get(i);
    }

    public void setSelectedItem(RegionInfo regionInfo) {
        this.mCurrentRegion = regionInfo;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(DataProviderFactory.getApplicationContext()).inflate(R.layout.aliuser_region, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.mHeaderLayout = (LinearLayout) view.findViewById(R.id.contact_item_head);
            viewHolder.mHeaderText = (TextView) view.findViewById(R.id.contact_item_header_text);
            viewHolder.mRegiontNameText = (TextView) view.findViewById(R.id.region_name);
            viewHolder.mRegionNubmerText = (TextView) view.findViewById(R.id.region_number);
            viewHolder.mRegionSelectIcon = (ImageView) view.findViewById(R.id.region_select);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RegionInfo regionInfo = this.list.get(i);
        if (regionInfo.isDisplayLetter) {
            if (DataProviderFactory.getDataProvider().showHeadCountry()) {
                viewHolder.mHeaderLayout.setVisibility(0);
            } else {
                viewHolder.mHeaderLayout.setVisibility(8);
            }
            viewHolder.mHeaderText.setText(regionInfo.character);
            view.setBackgroundColor(-1);
        } else {
            viewHolder.mHeaderLayout.setVisibility(8);
            view.setBackgroundResource(R.drawable.aliuser_region_item_bg);
        }
        viewHolder.mRegiontNameText.setText(regionInfo.name);
        viewHolder.mRegionNubmerText.setText(regionInfo.code);
        if (this.mCurrentRegion == null || TextUtils.isEmpty(this.mCurrentRegion.domain) || !this.mCurrentRegion.domain.equals(regionInfo.domain)) {
            viewHolder.mRegionNubmerText.setTextColor(this.mContext.getResources().getColor(R.color.aliuser_color_light_gray));
            viewHolder.mRegiontNameText.setTextColor(this.mContext.getResources().getColor(R.color.aliuser_color_black));
            viewHolder.mRegionSelectIcon.setVisibility(4);
        } else {
            viewHolder.mRegionNubmerText.setTextColor(this.mContext.getResources().getColor(R.color.aliuser_selected_country_color));
            viewHolder.mRegiontNameText.setTextColor(this.mContext.getResources().getColor(R.color.aliuser_selected_country_color));
            viewHolder.mRegionSelectIcon.setVisibility(0);
        }
        return view;
    }

    class ViewHolder {
        protected LinearLayout mHeaderLayout;
        protected TextView mHeaderText;
        protected TextView mRegionNubmerText;
        protected ImageView mRegionSelectIcon;
        protected TextView mRegiontNameText;

        ViewHolder() {
        }
    }
}
