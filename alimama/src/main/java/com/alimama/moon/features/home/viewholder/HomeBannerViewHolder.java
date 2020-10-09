package com.alimama.moon.features.home.viewholder;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeBannerItem;
import com.alimama.moon.features.home.theme.HomeThemeDataItem;
import com.alimama.moon.features.home.theme.HomeThemeDataManager;
import com.alimama.moon.features.home.view.HomeSliderBanner;
import com.alimama.moon.features.home.view.HomeSliderBannerController;

public class HomeBannerViewHolder implements HomeBaseViewHolder<HomeBannerItem> {
    private View mContainer;
    private HomeSliderBannerController mHomeSliderBannerController;
    private HomeSliderBanner sliderBanner;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.home_views_banner, (ViewGroup) null);
        this.mContainer = inflate.findViewById(R.id.home_slider_container);
        this.sliderBanner = (HomeSliderBanner) inflate.findViewById(R.id.home_slider_banner);
        this.mHomeSliderBannerController = new HomeSliderBannerController();
        this.mHomeSliderBannerController.managerBanner(this.sliderBanner);
        return inflate;
    }

    public void onBindViewHolder(int i, HomeBannerItem homeBannerItem) {
        setBannerBackground();
        this.mHomeSliderBannerController.play(homeBannerItem.bannerList);
    }

    private void setBannerBackground() {
        GradientDrawable gradientDrawable;
        try {
            if (HomeThemeDataManager.getInstance().isSwitchConfigCenterTheme()) {
                HomeThemeDataItem homeThemeDataItem = HomeThemeDataManager.getInstance().themeDataItem;
                gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(homeThemeDataItem.bannerStartColor), Color.parseColor(homeThemeDataItem.bannerEndColor)});
            } else {
                gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{this.mContainer.getResources().getColor(R.color.banner_bg_gradient_top), this.mContainer.getResources().getColor(R.color.banner_bg_gradient_bottom)});
            }
            this.mContainer.setBackground(gradientDrawable);
        } catch (Exception unused) {
        }
    }
}
