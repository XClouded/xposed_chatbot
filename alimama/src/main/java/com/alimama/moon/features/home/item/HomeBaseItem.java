package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonBaseItem;
import org.json.JSONObject;

public abstract class HomeBaseItem extends CommonBaseItem {
    public HomeBaseItem(String str, int i) {
        this(str, i, new SafeJSONObject());
    }

    public HomeBaseItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
    }
}
