package com.ali.user.mobile.utils;

import android.content.Context;
import android.text.TextUtils;
import com.ali.user.mobile.model.CountryCode;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.ui.R;
import com.alibaba.fastjson.JSON;
import java.util.Iterator;
import java.util.List;

public class CountryUtil {
    public static RegionInfo matchRegionFromLocal(Context context, String str) {
        List<CountryCode> parseArray;
        RegionInfo regionInfo = new RegionInfo();
        if (!TextUtils.isEmpty(str) && (parseArray = JSON.parseArray(context.getString(R.string.aliuser_hot_region_list), CountryCode.class)) != null) {
            Iterator<CountryCode> it = parseArray.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CountryCode next = it.next();
                if (TextUtils.equals(str.toLowerCase(), next.domain.toLowerCase())) {
                    regionInfo.name = next.name;
                    regionInfo.code = next.code;
                    regionInfo.checkPattern = next.checkPattern;
                    regionInfo.domain = next.domain;
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(regionInfo.name)) {
            regionInfo.name = "中国大陆";
            regionInfo.code = "+86";
            regionInfo.checkPattern = "^(86){0,1}1\\d{10}$";
            regionInfo.domain = "CN";
        }
        return regionInfo;
    }
}
