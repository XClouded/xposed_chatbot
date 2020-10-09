package com.alimamaunion.common.listpage;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alimamaunion.base.safejson.SafeJSONObject;
import org.json.JSONObject;

public class CommonItemInfo {
    public static int COMMON_TYPE_BASE = 1000;
    public static final int DEFAULT_TYPE = 0;
    public static final int FOOT_TYPE = 9999;
    public static final int MAX_SPAN_SIZE = 20;
    public static final String TAG = "CommonItemInfo";
    private static Class[] baseParam = {String.class, Integer.TYPE, JSONObject.class};
    private static CommonItemInfo[] sCommonItemInfos = new CommonItemInfo[0];
    public CommonBaseItem commonBaseItem;
    public Class<? extends CommonBaseViewHolder> holderClass;
    public int priority;
    public int spanSize;
    public String type;
    public Class<? extends CommonBaseItem> viewClass;
    public int viewType;

    static {
        for (CommonItemInfo registItem : sCommonItemInfos) {
            CommonItemFactory.registItem(registItem);
        }
    }

    public CommonItemInfo() {
        this.spanSize = 20;
    }

    public CommonItemInfo(String str, int i, Class<? extends CommonBaseItem> cls, Class<? extends CommonBaseViewHolder> cls2) {
        this(str, i, cls, cls2, 0);
    }

    public CommonItemInfo(String str, int i, Class<? extends CommonBaseItem> cls, Class<? extends CommonBaseViewHolder> cls2, int i2) {
        this(str, i, cls, cls2, i2, 20);
    }

    public CommonItemInfo(String str, int i, Class<? extends CommonBaseItem> cls, Class<? extends CommonBaseViewHolder> cls2, int i2, int i3) {
        this.spanSize = 20;
        this.type = str;
        this.viewType = i;
        this.viewClass = cls;
        this.holderClass = cls2;
        this.spanSize = i3;
    }

    public static void setBaseParam(Class[] clsArr) {
        baseParam = clsArr;
    }

    @Nullable
    public static CommonItemInfo createCommonItem(String str, SafeJSONObject safeJSONObject) {
        CommonItemInfo findCommonItemInfo;
        if (!TextUtils.isEmpty(str) && (findCommonItemInfo = CommonItemFactory.findCommonItemInfo(str)) != null) {
            return createItemInfo(findCommonItemInfo, safeJSONObject);
        }
        return null;
    }

    public static CommonBaseViewHolder createHomeItemViewHolder(int i) {
        CommonItemInfo findCommonItemInfo = CommonItemFactory.findCommonItemInfo(i);
        if (findCommonItemInfo == null) {
            return null;
        }
        try {
            return (CommonBaseViewHolder) findCommonItemInfo.holderClass.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    @Nullable
    public static CommonItemInfo findItemInfo(String str) {
        CommonItemInfo findCommonItemInfo = CommonItemFactory.findCommonItemInfo(str);
        if (findCommonItemInfo != null) {
            return findCommonItemInfo;
        }
        return null;
    }

    @Nullable
    public static CommonItemInfo findItemInfo(int i) {
        CommonItemInfo findCommonItemInfo = CommonItemFactory.findCommonItemInfo(i);
        if (findCommonItemInfo != null) {
            return findCommonItemInfo;
        }
        return null;
    }

    public static String findViewTypeStr(int i) {
        CommonItemInfo findItemInfo = findItemInfo(i);
        return findItemInfo != null ? findItemInfo.type : "";
    }

    private static CommonItemInfo createItemInfo(CommonItemInfo commonItemInfo, SafeJSONObject safeJSONObject) {
        try {
            Object newInstance = commonItemInfo.viewClass.getConstructor(baseParam).newInstance(new Object[]{commonItemInfo.type, Integer.valueOf(commonItemInfo.viewType), safeJSONObject});
            CommonItemInfo commonItemInfo2 = new CommonItemInfo();
            commonItemInfo2.type = commonItemInfo.type;
            commonItemInfo2.priority = commonItemInfo.priority;
            commonItemInfo2.viewType = commonItemInfo.viewType;
            commonItemInfo2.viewClass = commonItemInfo.viewClass;
            commonItemInfo2.spanSize = commonItemInfo.spanSize;
            commonItemInfo2.holderClass = commonItemInfo.holderClass;
            commonItemInfo2.commonBaseItem = (CommonBaseItem) newInstance;
            return commonItemInfo2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
