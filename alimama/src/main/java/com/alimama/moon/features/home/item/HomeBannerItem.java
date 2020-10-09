package com.alimama.moon.features.home.item;

import android.text.TextUtils;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class HomeBannerItem extends HomeBaseItem {
    private final int MAX_BANNER_NUM = 8;
    public List<HomeSliderBannerItem> bannerList;
    public String navTypeName;

    public HomeBannerItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        this.navTypeName = str;
        SafeJSONArray optJSONArray = new SafeJSONObject(jSONObject).optJSONArray("data");
        this.bannerList = new ArrayList();
        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
            HomeSliderBannerItem homeSliderBannerItem = new HomeSliderBannerItem(optJSONArray.optJSONObject(i2), i2);
            if (!TextUtils.isEmpty(homeSliderBannerItem.imgUrl) && this.bannerList.size() < 8) {
                this.bannerList.add(homeSliderBannerItem);
            }
        }
    }
}
