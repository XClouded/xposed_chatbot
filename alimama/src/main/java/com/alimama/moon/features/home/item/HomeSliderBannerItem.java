package com.alimama.moon.features.home.item;

import android.view.View;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.utils.CommonUtils;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class HomeSliderBannerItem {
    public String imgUrl;
    private int mIndex;
    private String srcUrl;

    public HomeSliderBannerItem(SafeJSONObject safeJSONObject, int i) {
        this.imgUrl = CommonUtils.imageUrl(safeJSONObject.optString("img"));
        this.srcUrl = safeJSONObject.optString("src");
        this.mIndex = i;
    }

    public void onClick(View view) {
        UTHelper.HomePage.clickBannerItem(this.mIndex);
        MoonComponentManager.getInstance().getPageRouter().gotoPage(this.srcUrl);
    }
}
