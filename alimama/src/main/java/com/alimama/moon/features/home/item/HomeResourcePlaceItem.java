package com.alimama.moon.features.home.item;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class HomeResourcePlaceItem extends HomeBaseItem {
    public List<HomeResourceItem> resourceItemList;

    public HomeResourcePlaceItem(String str, int i) {
        super(str, i);
    }

    public HomeResourcePlaceItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        SafeJSONArray optJSONArray = new SafeJSONObject(jSONObject).optJSONArray("data");
        this.resourceItemList = new ArrayList();
        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
            this.resourceItemList.add(new HomeResourceItem(optJSONArray.optJSONObject(i2)));
        }
    }

    public void notifyUpdate(int i, CommonRecyclerAdapter commonRecyclerAdapter, CommonItemInfo commonItemInfo) {
        super.notifyUpdate(i, commonRecyclerAdapter, commonItemInfo);
    }

    public static class HomeResourceItem {
        public String mImgUrl;
        public String mJumpUrl;

        public HomeResourceItem(SafeJSONObject safeJSONObject) {
            this.mImgUrl = safeJSONObject.optString("img");
            this.mJumpUrl = safeJSONObject.optString("src");
        }
    }
}
