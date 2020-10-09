package com.alimamaunion.common.listpage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonItemFactory {
    private static Map<String, CommonItemInfo> sInfoMap = new ConcurrentHashMap();
    private static Map<Integer, CommonItemInfo> sInfoSparseArray = new ConcurrentHashMap();

    public static void registItem(CommonItemInfo commonItemInfo) {
        if (commonItemInfo != null) {
            CommonItemInfo commonItemInfo2 = sInfoMap.get(commonItemInfo.type);
            if (commonItemInfo2 == null) {
                sInfoMap.put(commonItemInfo.type, commonItemInfo);
                sInfoSparseArray.put(Integer.valueOf(commonItemInfo.viewType), commonItemInfo);
            } else if (commonItemInfo2.priority < commonItemInfo.priority) {
                sInfoMap.put(commonItemInfo.type, commonItemInfo);
                sInfoSparseArray.put(Integer.valueOf(commonItemInfo.viewType), commonItemInfo);
            }
        }
    }

    public static CommonItemInfo findCommonItemInfo(String str) {
        CommonItemInfo commonItemInfo = sInfoMap.get(str);
        if (commonItemInfo != null) {
            return commonItemInfo;
        }
        return null;
    }

    public static CommonItemInfo findCommonItemInfo(int i) {
        CommonItemInfo commonItemInfo = sInfoSparseArray.get(Integer.valueOf(i));
        if (commonItemInfo != null) {
            return commonItemInfo;
        }
        return null;
    }
}
