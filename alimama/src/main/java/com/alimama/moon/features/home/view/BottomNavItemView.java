package com.alimama.moon.features.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.config.OrangeConfigCenterManager;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;

public class BottomNavItemView {
    public static int sPreTabIndex;
    /* access modifiers changed from: private */
    public BottomNavItemView itemView = this;
    private TextView mBottomNavItemNameTv;
    public EtaoDraweeView mImgView;
    public int mIndex;
    IBottomNavItem mListener;

    public BottomNavItemView(IBottomNavItem iBottomNavItem) {
        this.mListener = iBottomNavItem;
    }

    public View create(LayoutInflater layoutInflater, int i) {
        this.mIndex = i;
        View inflate = layoutInflater.inflate(R.layout.bottom_nav_bar_item_view, (ViewGroup) null);
        inflate.setTag(Integer.valueOf(this.mIndex));
        this.mImgView = (EtaoDraweeView) inflate.findViewById(R.id.home_bottom_nav_bar_item_bg);
        this.mBottomNavItemNameTv = (TextView) inflate.findViewById(R.id.home_bottom_nav_bar_item_name);
        render(inflate);
        return inflate;
    }

    public void updateSelectedState(Context context) {
        this.mBottomNavItemNameTv.setTextColor(context.getResources().getColor(R.color.home_bottom_nav_bar_name_selected_color));
        if (BottomNavActivity.sIsSwitchMidH5Tab) {
            switch (this.mIndex) {
                case 0:
                    this.mImgView.setBackgroundResource(R.drawable.ic_discover_selected);
                    return;
                case 1:
                    this.mImgView.setBackgroundResource(R.drawable.ic_tools_selected);
                    return;
                case 2:
                    this.mImgView.setImageResource(R.drawable.ic_goods_selected);
                    this.mImgView.setAnyImageUrl(OrangeConfigCenterManager.getInstance().getMideH5TabModel().getHighlightedImageUrl());
                    return;
                case 3:
                    this.mImgView.setBackgroundResource(R.drawable.ic_report_selected);
                    return;
                case 4:
                    this.mImgView.setBackgroundResource(R.drawable.ic_mine_selected);
                    return;
                default:
                    return;
            }
        } else {
            switch (this.mIndex) {
                case 0:
                    this.mImgView.setBackgroundResource(R.drawable.ic_discover_selected);
                    return;
                case 1:
                    this.mImgView.setBackgroundResource(R.drawable.ic_tools_selected);
                    return;
                case 2:
                    this.mImgView.setBackgroundResource(R.drawable.ic_report_selected);
                    return;
                case 3:
                    this.mImgView.setBackgroundResource(R.drawable.ic_mine_selected);
                    return;
                default:
                    return;
            }
        }
    }

    public void updateUnSelectedState(Context context) {
        this.mBottomNavItemNameTv.setTextColor(context.getResources().getColor(R.color.home_bottom_nav_bar_name_unselected_color));
        if (BottomNavActivity.sIsSwitchMidH5Tab) {
            switch (this.mIndex) {
                case 0:
                    this.mImgView.setBackgroundResource(R.drawable.ic_discover_unselected);
                    return;
                case 1:
                    this.mImgView.setBackgroundResource(R.drawable.ic_tools_unselected);
                    return;
                case 2:
                    this.mImgView.setImageResource(R.drawable.ic_goods_unselected);
                    this.mImgView.setAnyImageUrl(OrangeConfigCenterManager.getInstance().getMideH5TabModel().getImageUrl());
                    return;
                case 3:
                    this.mImgView.setBackgroundResource(R.drawable.ic_report_unselected);
                    return;
                case 4:
                    this.mImgView.setBackgroundResource(R.drawable.ic_mine_unselected);
                    return;
                default:
                    return;
            }
        } else {
            switch (this.mIndex) {
                case 0:
                    this.mImgView.setBackgroundResource(R.drawable.ic_discover_unselected);
                    return;
                case 1:
                    this.mImgView.setBackgroundResource(R.drawable.ic_tools_unselected);
                    return;
                case 2:
                    this.mImgView.setBackgroundResource(R.drawable.ic_report_unselected);
                    return;
                case 3:
                    this.mImgView.setBackgroundResource(R.drawable.ic_mine_unselected);
                    return;
                default:
                    return;
            }
        }
    }

    private void render(View view) {
        if (!BottomNavActivity.sIsSwitchMidH5Tab) {
            switch (this.mIndex) {
                case 0:
                    this.mImgView.setBackgroundResource(R.drawable.ic_discover_selected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_discovery));
                    this.mBottomNavItemNameTv.setTextColor(view.getContext().getResources().getColor(R.color.home_bottom_nav_bar_name_selected_color));
                    break;
                case 1:
                    this.mImgView.setBackgroundResource(R.drawable.ic_tools_unselected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_tool));
                    break;
                case 2:
                    this.mImgView.setBackgroundResource(R.drawable.ic_report_unselected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_account));
                    break;
                case 3:
                    this.mImgView.setBackgroundResource(R.drawable.ic_mine_unselected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_mine));
                    break;
            }
        } else {
            switch (this.mIndex) {
                case 0:
                    this.mImgView.setBackgroundResource(R.drawable.ic_discover_selected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_discovery));
                    this.mBottomNavItemNameTv.setTextColor(view.getContext().getResources().getColor(R.color.home_bottom_nav_bar_name_selected_color));
                    break;
                case 1:
                    this.mImgView.setBackgroundResource(R.drawable.ic_tools_unselected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_tool));
                    break;
                case 2:
                    this.mImgView.setImageResource(R.drawable.ic_goods_unselected);
                    this.mImgView.setAnyImageUrl(OrangeConfigCenterManager.getInstance().getMideH5TabModel().getImageUrl());
                    this.mBottomNavItemNameTv.setText(OrangeConfigCenterManager.getInstance().getMideH5TabModel().getTitle());
                    break;
                case 3:
                    this.mImgView.setBackgroundResource(R.drawable.ic_report_unselected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_account));
                    break;
                case 4:
                    this.mImgView.setBackgroundResource(R.drawable.ic_mine_unselected);
                    this.mBottomNavItemNameTv.setText(view.getContext().getResources().getString(R.string.tab_mine));
                    break;
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BottomNavItemView.this.mListener.onClickBottomNavItem(BottomNavItemView.this.itemView.mIndex, BottomNavActivity.mCurrentIndex == BottomNavItemView.this.itemView.mIndex);
            }
        });
    }
}
