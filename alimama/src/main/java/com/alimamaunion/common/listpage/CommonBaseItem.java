package com.alimamaunion.common.listpage;

import org.json.JSONObject;

public abstract class CommonBaseItem {
    public boolean mItemTheme;
    public String navTypeValue;
    public int viewType;

    public void notifyUpdate(int i, CommonRecyclerAdapter commonRecyclerAdapter, CommonItemInfo commonItemInfo) {
    }

    public CommonBaseItem(String str, int i) {
        this(str, i, new JSONObject());
    }

    public CommonBaseItem(String str, int i, JSONObject jSONObject) {
        this.mItemTheme = false;
        this.navTypeValue = str;
        this.viewType = i;
    }
}
