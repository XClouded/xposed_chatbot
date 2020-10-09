package com.alimama.moon.features.home.item;

import com.alimama.unionwl.utils.CommonUtils;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class HomeCircleNavItem extends HomeBaseItem {
    public List<Item> itemList = new ArrayList();
    private SafeJSONObject mLastData;

    public HomeCircleNavItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        this.mLastData = new SafeJSONObject(jSONObject);
    }

    public void notifyUpdate(int i, CommonRecyclerAdapter commonRecyclerAdapter, CommonItemInfo commonItemInfo) {
        super.notifyUpdate(i, commonRecyclerAdapter, commonItemInfo);
        notifyUpdate();
    }

    public void notifyUpdate() {
        renderServerData();
    }

    private void renderServerData() {
        if (this.mLastData != null) {
            this.itemList.clear();
            SafeJSONArray optJSONArray = this.mLastData.optJSONArray("data");
            for (int i = 0; i < optJSONArray.length(); i++) {
                this.itemList.add(new Item(optJSONArray.optJSONObject(i)));
            }
        }
    }

    public static class Item {
        public String img;
        public int index;
        public String name;
        public String src;

        public Item() {
        }

        public Item(SafeJSONObject safeJSONObject) {
            this.img = CommonUtils.imageUrl(safeJSONObject.optString("img"));
            this.name = safeJSONObject.optString("name");
            this.src = safeJSONObject.optString("src");
        }
    }
}
