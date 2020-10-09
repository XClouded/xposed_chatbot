package com.ali.user.mobile.utils;

import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.model.CountryCode;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.rpc.login.model.GroupedCountryCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountryCodeUtil {
    public static final String TAG = "login.CountryCodeUtil";

    public static ArrayList<RegionInfo> fillData(String str, List<GroupedCountryCode> list, HashMap<String, Integer> hashMap, List<String> list2) {
        ArrayList<RegionInfo> arrayList = new ArrayList<>();
        int i = 0;
        int i2 = 0;
        while (i < list.size()) {
            GroupedCountryCode groupedCountryCode = list.get(i);
            List<CountryCode> list3 = groupedCountryCode.countryCodeList;
            int i3 = i2;
            for (int i4 = 0; i4 < list3.size(); i4++) {
                CountryCode countryCode = list3.get(i4);
                RegionInfo regionInfo = new RegionInfo();
                if ("#".equals(groupedCountryCode.index)) {
                    regionInfo.character = str;
                    groupedCountryCode.index = "★";
                } else {
                    regionInfo.character = groupedCountryCode.index;
                }
                if (i4 == 0) {
                    regionInfo.isDisplayLetter = true;
                    if (groupedCountryCode.index != null) {
                        hashMap.put(groupedCountryCode.index, Integer.valueOf(i3));
                        list2.add(groupedCountryCode.index);
                    } else {
                        TLogAdapter.e(TAG, "error!! index can not be null!");
                    }
                } else {
                    regionInfo.isDisplayLetter = false;
                }
                regionInfo.name = countryCode.name;
                regionInfo.code = "" + countryCode.code;
                regionInfo.domain = countryCode.domain;
                regionInfo.checkPattern = countryCode.checkPattern;
                regionInfo.pinyin = countryCode.pinyin;
                i3++;
                arrayList.add(regionInfo);
            }
            i++;
            i2 = i3;
        }
        return arrayList;
    }

    public static RegionInfo convertDefaultCountryToRegionInfo(CountryCode countryCode) {
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.name = countryCode.name;
        regionInfo.code = "" + countryCode.code;
        regionInfo.domain = countryCode.domain;
        regionInfo.checkPattern = countryCode.checkPattern;
        return regionInfo;
    }
}
