package com.alimama.moon.features.home.theme;

import android.text.TextUtils;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class HomeThemeDataManager {
    private static HomeThemeDataManager sInstance;
    public HomeThemeDataItem themeDataItem;

    private HomeThemeDataManager() {
    }

    public static HomeThemeDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new HomeThemeDataManager();
        }
        return sInstance;
    }

    public void parseTheme() {
        String configResult = EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_THEME);
        this.themeDataItem = new HomeThemeDataItem();
        if (!TextUtils.isEmpty(configResult)) {
            try {
                SafeJSONObject optJSONObject = new SafeJSONObject(configResult).optJSONObject("data");
                this.themeDataItem.switchTheme = optJSONObject.optString("switchTheme");
                this.themeDataItem.statusBarStartColor = optJSONObject.optString("statusBarStartColor");
                this.themeDataItem.statusBarEndColor = optJSONObject.optString("statusBarEndColor");
                this.themeDataItem.tabStartColor = optJSONObject.optString("tabStartColor");
                this.themeDataItem.tabEndColor = optJSONObject.optString("tabEndColor");
                this.themeDataItem.refreshBgTextColor = optJSONObject.optString("refreshBgTextColor");
                this.themeDataItem.bannerStartColor = optJSONObject.optString("bannerStartColor");
                this.themeDataItem.bannerEndColor = optJSONObject.optString("bannerEndColor");
                this.themeDataItem.circleNavImg = optJSONObject.optString("circleNavImg");
                this.themeDataItem.circleNavTextColor = optJSONObject.optString("circleNavTextColor");
                this.themeDataItem.saleBlockImg = optJSONObject.optString("saleBlockImg");
            } catch (Exception unused) {
            }
        }
    }

    public boolean isSwitchConfigCenterTheme() {
        return TextUtils.equals(this.themeDataItem.switchTheme, "1");
    }
}
